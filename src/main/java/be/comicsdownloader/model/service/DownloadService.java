package be.comicsdownloader.model.service;

import be.comicsdownloader.model.download.ChapterDownloadTask;
import be.comicsdownloader.model.pojo.manga.Chapter;
import be.comicsdownloader.model.pojo.manga.Image;
import be.comicsdownloader.model.service.PropertiesService.PropertyKey;
import org.apache.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class DownloadService extends Observable {

    private static final Logger LOG = Logger.getLogger(DownloadService.class);
    private static DownloadService instance = new DownloadService();

    protected static DownloadService getInstance() {
        return instance;
    }

    private ExecutorService executor;
    private Collection<PropertyChangeListener> listeners;

    private DownloadService() {
        int nbThread = Integer.parseInt(Services.getPropertyService().getProperty(PropertyKey.DOWNLOAD_THREAD_NUMBER));
        executor = Executors.newFixedThreadPool(nbThread);
        listeners = new LinkedList<>();
    }

    public void download(final List<Chapter> chapters) {
        LibraryService.getInstance().createStructure(getAllImageFromChapters(chapters));

        LOG.info("Downloading [Chapters=" + chapters + "]");
        for (Chapter chapter : chapters) {
            ChapterDownloadTask task = new ChapterDownloadTask(chapter);
            for (PropertyChangeListener listener : listeners) {
                task.addPropertyChangeListener(listener);
            }

            executor.execute(task);
        }

        notifyObservers();
    }

    public void cancelDownload() {
        LOG.info("Cancel Downloading");
        executor.shutdown();
    }

    public void addDownloadChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    private Collection<Image> getAllImageFromChapters(Collection<Chapter> chapters) {
        LOG.trace("Get Image from chapter list");
        Collection<Image> images = new HashSet<>();
        for (Chapter chapter : chapters) {
            images.addAll(chapter.getAllImages());
        }
        return images;
    }
}
