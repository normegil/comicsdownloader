package be.comicsdownloader.model.download;

import be.comicsdownloader.generators.TestStrutureCreator;
import be.comicsdownloader.model.AvailableSite;
import be.comicsdownloader.model.pojo.manga.*;
import be.comicsdownloader.model.service.PropertiesService;
import be.comicsdownloader.model.service.Services;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ChapterDownloadTaskTest {

    private File tempRootDirectory;
    private Website website;
    private Set<Chapter> chapters;

    public ChapterDownloadTaskTest() {
    }

    @Before
    public void setUp() throws IOException {
        PropertiesService.setInstance(File.createTempFile("DummySettings", ".properties"));
        website = TestStrutureCreator.createWebsite(AvailableSite.MANGA_HERE);

        chapters = new HashSet<>();
        for (Serie serie : website) {
            for (Tome tome : serie) {
                for (Chapter chapter : tome) {
                    chapters.add(chapter);
                }
            }
        }

        tempRootDirectory = File.createTempFile("test", "");
        tempRootDirectory.delete();
        tempRootDirectory.mkdir();

        Services.getPropertyService().setProperty(PropertiesService.PropertyKey.LIBRARY_FOLDER, tempRootDirectory);
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(tempRootDirectory);
        website = null;
        PropertiesService.setInstance(null);
    }

    @Test
    public void testDownloading() {
        for (Chapter chapter : chapters) {
            ChapterDownloadTask task = new ChapterDownloadTask(chapter);
            task.run();
        }

        for (Chapter chapter : chapters) {
            File chapterFolder = new File(Services.getLibraryService().getPathForChapter(chapter));
            for (Image image : chapter) {
                boolean found = false;
                int imageNumber = image.getNumber();
                String imageName = StringUtils.substringAfter(String.valueOf(imageNumber + 1000), "1");

                for (File imageFile : chapterFolder.listFiles()) {
                    if (imageFile.getName().equals(
                            imageName + "."
                                    + Image.ImageType.valueOf(Services.getPropertyService().getProperty(PropertiesService.PropertyKey.IMAGE_FORMAT_TYPE)).getTypeName())) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    fail("Image not found : " + imageNumber);
                }
            }
        }
    }

    @Test
    public void testDownloadingNullList() {
        ChapterDownloadTask task = new ChapterDownloadTask(null);
        task.run();

        File[] listFiles = tempRootDirectory.listFiles();
        assertEquals(0, listFiles.length);
    }
}
