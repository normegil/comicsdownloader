package be.comicsdownloader.model.library;

import be.comicsdownloader.model.pojo.manga.Chapter;
import be.comicsdownloader.model.pojo.manga.Image;

import java.util.Collection;

public interface LibraryStructureManager {

    void createStructure(String rootPath, Collection<Image> images);

    String getPathForChapter(String rootPath, Chapter chapter);
}
