package be.comicsdownloader.model.service;

import be.comicsdownloader.model.library.LibraryStructure;
import be.comicsdownloader.model.pojo.manga.Chapter;
import be.comicsdownloader.model.pojo.manga.Image;
import be.comicsdownloader.model.service.PropertiesService.PropertyKey;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Collection;
import java.util.Observable;

public final class LibraryService extends Observable {

    private static final Logger LOG = Logger.getLogger(LibraryService.class);
    private static LibraryService instance = new LibraryService();
    private LibraryStructure currentStructure;
    private File currentRootFolder;

    protected static LibraryService getInstance() {
        return instance;
    }

    private LibraryService() {
        currentRootFolder = new File(PropertiesService.getInstance().getProperty(PropertyKey.LIBRARY_FOLDER));
        currentStructure = LibraryStructure.valueOf(PropertiesService.getInstance().getProperty(PropertyKey.LIBRARY_FOLDER_STRUCTURE));
    }

    public void archiveCurrentLibrary() {
        LOG.debug("Archive current library");
        throw new UnsupportedOperationException();
    }

    public void changeLibraryStructure() {
        LOG.debug("Change library Structure");
        throw new UnsupportedOperationException();
    }

    public void createStructure(Collection<Image> images) {
        LOG.info("Create Library Structure");
        currentStructure.getManager().createStructure(currentRootFolder.getAbsolutePath(), images);
    }

    public String getPathForChapter(Chapter chapter) {
        LOG.debug("Get path of chapter : " + chapter);
        return currentStructure.getManager().getPathForChapter(currentRootFolder.getAbsolutePath(), chapter);
    }
}
