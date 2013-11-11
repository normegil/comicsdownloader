package be.comicsdownloader.model;

import be.comicsdownloader.model.pojo.manga.Chapter;
import be.comicsdownloader.model.pojo.manga.Serie;
import be.comicsdownloader.model.pojo.manga.Website;
import be.comicsdownloader.model.service.Services;
import org.apache.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class ModelImpl implements Model {

    private static final Logger LOG = Logger.getLogger(ModelImpl.class);
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private SortedSet<Serie> series;
    private SortedSet<Website> websites;
    private SortedSet<Chapter> loadedChapters;
    private SortedSet<Chapter> toDownloadChapters;
    private Website selectedWebsite;
    private Serie selectedSerie;

    public ModelImpl() {
        series = new TreeSet<>();
        websites = new TreeSet<>();
        toDownloadChapters = new TreeSet<>();
        selectedSerie = null;
    }

    @Override
    public SortedSet<Serie> getSeries() {
        LOG.trace("Reload series");
        SortedSet<Serie> loadedSeries = Services.getLoadingService().loadAllSeries();
        changes.firePropertyChange(ModelProperties.SERIES.getPropertyName(), series, loadedSeries);
        series = loadedSeries;
        return series;
    }

    @Override
    public SortedSet<Website> getWebsites() {
        LOG.trace("Reload websites [Selected Serie : " + selectedSerie + "]");
        SortedSet<Website> loadedWebsites = Services.getLoadingService().loadSiteForSerie(selectedSerie);
        changes.firePropertyChange(ModelProperties.WEBSITES.getPropertyName(), websites, loadedWebsites);
        websites = loadedWebsites;
        return websites;
    }

    @Override
    public SortedSet<Chapter> getLoadedChapters() {
        LOG.trace("Reload chapters [Selected Serie : " + selectedSerie + "; Selected Websites : " + selectedWebsite + "]");
        SortedSet<Chapter> newChapters = Services.getLoadingService().loadChapterList(selectedWebsite, selectedSerie);
        changes.firePropertyChange(ModelProperties.CHAPTER_LOADED.getPropertyName(), loadedChapters, newChapters);
        loadedChapters = newChapters;
        return loadedChapters;
    }

    @Override
    public SortedSet<Chapter> getToDownloadChapters() {
        return toDownloadChapters;
    }

    @Override
    public void addAllToDownloadChapter(final Collection<Chapter> chapters) {
        LOG.trace("Add chapters to download [Chapters=" + chapters + "]");
        toDownloadChapters.addAll(chapters);
        changes.firePropertyChange(ModelProperties.CHAPTER_TO_DOWNLOAD.getPropertyName(), null, toDownloadChapters);
    }

    @Override
    public void removeAllToDownloadChapter(final Collection<Chapter> chapters) {
        LOG.trace("Remove chapters to download [Chapters=" + chapters + "]");
        toDownloadChapters.removeAll(chapters);
        changes.firePropertyChange(ModelProperties.CHAPTER_TO_DOWNLOAD.getPropertyName(), null, toDownloadChapters);
    }

    @Override
    public void refreshData() {
        LOG.trace("Refresh data");
        Services.getLoadingService().refreshListSerie();
        getSeries();
    }

    @Override
    public void setSelectedSerie(final Serie selectedSerie) {
        LOG.trace("Change selected serie [Was = " + this.selectedSerie + "; Is = " + selectedSerie + "]");
        this.selectedSerie = selectedSerie;
    }

    @Override
    public void setSelectedWebsite(final Website selectedWebsite) {
        LOG.trace("Change selected website [Was = " + this.selectedWebsite + "; Is = " + selectedWebsite + "]");
        this.selectedWebsite = selectedWebsite;
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        changes.removePropertyChangeListener(listener);
    }

    @Override
    public void clearWebsite() {
        LOG.trace("Clear list of websites");
        TreeSet<Website> oldValue = new TreeSet<>(websites);
        websites.clear();
        changes.firePropertyChange(ModelProperties.WEBSITES.getPropertyName(), oldValue, websites);
    }

    @Override
    public void clearChapters() {
        LOG.trace("Clear list of chapters");
        TreeSet<Chapter> oldValue = new TreeSet<>(loadedChapters);
        loadedChapters.clear();
        changes.firePropertyChange(ModelProperties.CHAPTER_LOADED.getPropertyName(), oldValue, loadedChapters);
    }

    @Override
    public void launchDownload() {
        LOG.trace("Launch download of chapters [ToDownloadChapters =" + toDownloadChapters + "]");
        List<Chapter> toDownload = new LinkedList<>(toDownloadChapters);
        Services.getDownloadServices().download(toDownload);

        toDownloadChapters.clear();
        changes.firePropertyChange(ModelProperties.CHAPTER_TO_DOWNLOAD.getPropertyName(), toDownload, toDownloadChapters);
    }
}
