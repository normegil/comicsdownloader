package be.comicsdownloader.model;

import be.comicsdownloader.model.pojo.manga.Chapter;
import be.comicsdownloader.model.pojo.manga.Serie;
import be.comicsdownloader.model.pojo.manga.Website;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.SortedSet;

public interface Model {

    public enum ModelProperties {

        SERIES("Series"),
        WEBSITES("Websites"),
        CHAPTER_LOADED("ChaptersLoaded"),
        CHAPTER_TO_DOWNLOAD("ChaptersToDownload"),
        PROGRESS("Progress");
        private String propertyName;

        private ModelProperties(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyName() {
            return propertyName;
        }
    }

    SortedSet<Serie> getSeries();

    SortedSet<Website> getWebsites();

    SortedSet<Chapter> getLoadedChapters();

    SortedSet<Chapter> getToDownloadChapters();

    void addAllToDownloadChapter(Collection<Chapter> chapters);

    void removeAllToDownloadChapter(Collection<Chapter> chapters);

    void refreshData();

    void setSelectedSerie(Serie selectedSerie);

    void setSelectedWebsite(Website selectedWebsite);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void clearWebsite();

    void clearChapters();

    void launchDownload();
}
