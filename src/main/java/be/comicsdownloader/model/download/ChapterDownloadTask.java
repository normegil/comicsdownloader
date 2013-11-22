package be.comicsdownloader.model.download;

import be.comicsdownloader.model.CrashException;
import be.comicsdownloader.model.Model.ModelProperties;
import be.comicsdownloader.model.loader.Loader;
import be.comicsdownloader.model.loader.NetLoader;
import be.comicsdownloader.model.pojo.manga.Chapter;
import be.comicsdownloader.model.pojo.manga.Image;
import be.comicsdownloader.model.pojo.manga.Image.ImageType;
import be.comicsdownloader.model.service.PropertiesService.PropertyKey;
import be.comicsdownloader.model.service.Services;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ChapterDownloadTask implements Runnable {

    private static final Logger LOG = Logger.getLogger(ChapterDownloadTask.class);
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private final Chapter chapter;

    public ChapterDownloadTask(final Chapter chapter) {
        this.chapter = chapter;
    }

    @Override
    public void run() {
        LOG.trace("Downloading [Chapter=" + chapter + "]");
        if (chapter != null) {
            Loader loader = new NetLoader();
            ImageType type = ImageType.valueOf(Services.getPropertyService().getProperty(PropertyKey.IMAGE_FORMAT_TYPE).toUpperCase());

            File chapterFolder = new File(Services.getLibraryService().getPathForChapter(chapter));
            if (!chapterFolder.exists()) {
                boolean mkdirsSuccess = chapterFolder.mkdirs();
                if (!mkdirsSuccess) {
                    throw new CrashException("Folder cannot be created [Path=" + chapterFolder.getAbsolutePath() + "]");
                }
            }

            for (Image image : chapter) {
                try {
                    BufferedImage loadedImage = loader.loadImage(new URL(image.getUrl()));

                    // Add missing 0 before image number
                    int imageNumber = image.getNumber();
                    String imageName = StringUtils.substringAfter(String.valueOf(imageNumber + 1000), "1");

                    File newImage = new File(chapterFolder, imageName + "." + type.getTypeName());
                    if (!newImage.exists()) {
                        boolean createNewFileSuccess = newImage.createNewFile();
                        if (!createNewFileSuccess) {
                            throw new CrashException("File for image cannot be created [Path=" + newImage.getAbsolutePath() + "]");
                        }
                    }

                    ImageIO.write(loadedImage, type.getTypeName(), newImage);
                } catch (MalformedURLException ex) {
                    LOG.error("Cannot download image " + image.toString(), ex);
                } catch (IOException ex) {
                    LOG.error("Cannot download image " + image.toString(), ex);
                }
            }
        }
        changes.firePropertyChange(ModelProperties.PROGRESS.getPropertyName(), null, chapter);
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }
}
