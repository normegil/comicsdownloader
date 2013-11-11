package be.comicsdownloader.model.library;

import be.comicsdownloader.model.CrashException;
import be.comicsdownloader.model.pojo.manga.Chapter;
import be.comicsdownloader.model.pojo.manga.Image;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Collection;

public class FullSplittedFolderLibraryStructureManager extends BasicLibraryStructureManager {

    private static final Logger LOG = Logger.getLogger(FullSplittedFolderLibraryStructureManager.class);

    @Override
    public void createStructure(final String rootPath, final Collection<Image> images) {
        LOG.info("Create Structure [RootPath=" + rootPath + "; Images=" + images + "]");
        if (rootPath != null && images != null) {
            File root = new File(rootPath);
            if (!root.exists()) {
                boolean mkdirSuccess = root.mkdir();
                if (!mkdirSuccess) {
                    throw new CrashException("Folder cannot be created [Path=" + root.getAbsolutePath() + "]");
                }
            }

            Collection<Chapter> chapters = getChapters(images);

            for (Chapter chapter : chapters) {
                StringBuilder builder = new StringBuilder();
                builder.append(rootPath)
                        .append("/")
                        .append(chapter.getTome().getSerie().getName())
                        .append("/Tome ")
                        .append(chapter.getTome().getNumber())
                        .append("/Chapter ")
                        .append(chapter.getNumber())
                        .append("/");

                String chapterPath = builder.toString();
                File chapterFolder = new File(chapterPath);
                if (!chapterFolder.exists()) {
                    boolean mkdirsSuccess = chapterFolder.mkdirs();
                    if (!mkdirsSuccess) {
                        throw new CrashException("Folder cannot be created [Path=" + root.getAbsolutePath() + "]");
                    }
                }
            }
        }
    }

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
