package be.comicsdownloader.model.library;

import be.comicsdownloader.model.pojo.manga.Chapter;
import be.comicsdownloader.model.pojo.manga.Image;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class BasicLibraryStructureManager implements LibraryStructureManager {

    protected Collection<Chapter> getChapters(final Collection<Image> images) {
        Set<Chapter> chapters = new HashSet<>();
        if (images != null) {
            for (Image image : images) {
                if (!chapters.contains(image.getChapter())) {
                    chapters.add(image.getChapter());
                }
            }
        }
        return chapters;
    }
}
