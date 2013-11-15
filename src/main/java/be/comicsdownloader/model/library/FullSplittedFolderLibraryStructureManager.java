package be.comicsdownloader.model.library;

import be.comicsdownloader.helpers.FormatHelper;
import be.comicsdownloader.model.pojo.manga.Chapter;
import org.apache.log4j.Logger;

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
                    .append(FormatHelper.getOutputFloat(chapter.getTome().getNumber()))
                    .append("/Chapter ")
                    .append(FormatHelper.getOutputFloat(chapter.getNumber()))
                    .append("/");
            path = builder.toString();
        }

        LOG.trace("Get path [Path=" + path + "][RootPath=" + rootPath + "; Chapter=" + chapter + "]");
        return path;
    }
}
