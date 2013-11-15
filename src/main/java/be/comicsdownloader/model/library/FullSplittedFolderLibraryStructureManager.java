package be.comicsdownloader.model.library;

import be.comicsdownloader.model.CrashException;
import be.comicsdownloader.model.pojo.manga.Chapter;
import be.comicsdownloader.model.pojo.manga.Image;
import be.comicsdownloader.model.service.Services;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Collection;

public class FullSplittedFolderLibraryStructureManager extends AbstractLibraryStructureManager {

    private static final Logger LOG = Logger.getLogger(FullSplittedFolderLibraryStructureManager.class);

    @Override
    public String getPathForChapter(final String rootPath, final Chapter chapter) {
        String path = "";
        if (rootPath != null && chapter != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(rootPath)
                    .append("/")
                    .append(chapter.getTome().getSerie().getName())
                    .append("/Tome ")
                    .append(chapter.getTome().getNumber())
                    .append("/Chapter ")
                    .append(chapter.getNumber())
                    .append("/");
            path = builder.toString();
        }

        LOG.trace("Get path [Path=" + path + "][RootPath=" + rootPath + "; Chapter=" + chapter + "]");
        return path;
    }
}
