package be.comicsdownloader.model.library;

import be.comicsdownloader.generators.TestStrutureCreator;
import be.comicsdownloader.model.AvailableSite;
import be.comicsdownloader.model.pojo.manga.*;
import be.comicsdownloader.model.service.PropertiesService;
import be.comicsdownloader.model.service.Services;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.Assert.*;

public class FullSplittedFolderLibraryStructureManagerTest {

    private FullSplittedFolderLibraryStructureManager manager;
    private File tempRootDirectory;
    private Website website;
    private Collection<Chapter> chapters;
    private Collection<Image> images;

    public FullSplittedFolderLibraryStructureManagerTest() {
        manager = (FullSplittedFolderLibraryStructureManager) LibraryStructure.FULL_SPLITTED.getManager();
    }

    @Before
    public void setUp() {
        try {
            PropertiesService.setInstance(File.createTempFile("DummySettings", ".properties"));
            Services.getPropertyService().setProperty(PropertiesService.PropertyKey.AUTO_REFRESH, false);
            website = TestStrutureCreator.createWebsite(AvailableSite.MANGA_HERE);

            chapters = new HashSet<>();
            images = new HashSet<>();
            for (Serie serie : website) {
                for (Tome tome : serie) {
                    for (Chapter chapter : tome) {
                        for (Image image : chapter) {
                            images.add(image);
                        }

                        if (chapter.iterator().hasNext()) {
                            chapters.add(chapter);
                        }
                    }
                }
            }

            tempRootDirectory = File.createTempFile("test", "");
            tempRootDirectory.delete();
            tempRootDirectory.mkdir();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @After
    public void tearDown() {
        try {
            FileUtils.deleteDirectory(tempRootDirectory);
            website = null;
            PropertiesService.setInstance(null);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testGetChapterFromImage() {
        Collection<Chapter> findedChapters = manager.getChapters(images);

        assertNotNull(findedChapters);
        assertFalse(findedChapters.isEmpty());
        assertEquals(chapters.size(), findedChapters.size());
        for (Chapter chapter : chapters) {
            boolean finded = false;
            for (Chapter findedChapter : findedChapters) {
                if (chapter.equals(findedChapter)) {
                    finded = true;
                    break;
                }
            }

            if (!finded) {
                fail("The searched Chapter was not found");
            }
        }
    }

    @Test
    public void testGetChapterFromImageWithNullImage() {
        Collection<Chapter> emptyChapters = manager.getChapters(null);
        assertNotNull(emptyChapters);
        assertTrue(emptyChapters.isEmpty());
    }

    @Test
    public void testGetPathForChapter() {
        Serie serie = website.iterator().next();
        Tome tome = serie.iterator().next();
        Chapter chapter = tome.iterator().next();

        StringBuilder builder = new StringBuilder();
        builder.append(tempRootDirectory.getAbsolutePath())
                .append("/")
                .append(chapter.getTome().getSerie().getName())
                .append("/Tome ")
                .append(chapter.getTome().getNumber())
                .append("/Chapter ")
                .append(chapter.getNumber())
                .append("/");

        String pathForChapter = manager.getPathForChapter(tempRootDirectory.getAbsolutePath(), chapter);
        assertEquals(builder.toString(), pathForChapter);
    }

    @Test
    public void testGetPathForChapterWithNullParameters() {
        Serie serie = website.iterator().next();
        Tome tome = serie.iterator().next();
        Chapter chapter = tome.iterator().next();

        String emptyPath = manager.getPathForChapter(null, chapter);
        assertNotNull(emptyPath);
        assertTrue(emptyPath.isEmpty());

        String emptyPath1 = manager.getPathForChapter(tempRootDirectory.getAbsolutePath(), null);
        assertNotNull(emptyPath1);
        assertTrue(emptyPath1.isEmpty());
    }

    @Test
    public void testCreateStructure() {
        manager.createStructure(tempRootDirectory.getAbsolutePath(), images);
        Collection<Chapter> chapters = manager.getChapters(images);

        for (Chapter chapter : chapters) {
            String pathForChapter = manager.getPathForChapter(tempRootDirectory.getAbsolutePath(), chapter);
            File chapterPath = new File(pathForChapter);
            assertTrue(chapterPath.exists());
            assertTrue(chapterPath.isDirectory());
        }
    }

    @Test
    public void testCreateStructureWithNullParameters() {
        manager.createStructure(null, null);
        assertEquals(0, tempRootDirectory.listFiles().length);

        manager.createStructure(null, images);
        assertEquals(0, tempRootDirectory.listFiles().length);

        manager.createStructure(tempRootDirectory.toString(), null);
        assertEquals(0, tempRootDirectory.listFiles().length);
    }
}
