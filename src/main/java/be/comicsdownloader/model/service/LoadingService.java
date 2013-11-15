package be.comicsdownloader.model.service;

import be.comicsdownloader.model.AvailableSite;
import be.comicsdownloader.model.CrashException;
import be.comicsdownloader.model.persistance.XmlWebsiteDAO;
import be.comicsdownloader.model.pojo.manga.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class LoadingService {

    private static final Logger LOG = Logger.getLogger(LoadingService.class);
    private static volatile LoadingService instance;
    private XmlWebsiteDAO dao;

    protected static LoadingService getInstance() {
        if (instance == null) {
            synchronized (LoadingService.class) {
                if (instance == null) {
                    instance = new LoadingService();
                }
            }
        }
        return instance;
    }

    public static LoadingService setInstance(String dataFilePath) {
        synchronized (LoadingService.class) {
            instance = new LoadingService(dataFilePath);
        }
        return instance;
    }

    private LoadingService() {
        this(PropertiesService.getInstance().getProperty(PropertiesService.PropertyKey.DATA_FILE));
    }

    private LoadingService(String dataFilePath) {
        try {
            File dataFile = new File(dataFilePath);
            if (!dataFile.exists()) {
                boolean createNewFileSuccess = dataFile.createNewFile();
                if (!createNewFileSuccess) {
                    throw new IllegalArgumentException("Data file cannot be created [Path=" + dataFile.getAbsolutePath() + "]");
                }
            }

            dao = new XmlWebsiteDAO(dataFile);
        } catch (IOException ex) {
            throw new CrashException(ex);
        }
    }

    public SortedSet<Serie> loadAllSeries() {
        LOG.info("Load all Series");
        Collection<Website> websites = dao.getAll();
        SortedSet<Serie> series = new TreeSet<>();

        if (websites.isEmpty()) {
            LOG.debug("XML was empty - Loading from internet");
            for (AvailableSite site : AvailableSite.values()) {
                Website website = new Website();
                website.setAvailableSite(site);
                website.setMustBeSaved(true);
                website.setName(site.getName());
                website.setUrl(site.getUrl());

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, 0);
                website.setValidityDate(calendar.getTime());

                websites.add(website);
            }
        }

        for (Website website : websites) {
            series.addAll(website.getSeries());
        }

        Collection<Website> websiteCollection = new LinkedList<>();
        websiteCollection.addAll(websites);
        for (Website website : websiteCollection) {
            if (website.getMustBeSaved()) {
                dao.update(website);
            }
        }

        return series;
    }

    public Set<Serie> loadSerieForSite(AvailableSite availableSite) {
        LOG.info("Load series for site" + availableSite);
        Website website = dao.get(availableSite);

        if (website != null) {
            if (website.getMustBeSaved()) {
                dao.update(website);
            }

            return website.getSeries();
        } else {
            return new HashSet<>();
        }
    }

    public void refreshListSerie() {
        LOG.info("Refresh list of site");
        Collection<Website> websites = dao.getAll();

        for (Website website : websites) {
            website.setValidityDate(null);

            for (Serie serie : website) {
                serie.setValidityDate(null);

                for (Tome tome : serie) {
                    tome.setValidityDate(null);

                    for (Chapter chapter : tome) {
                        chapter.setValidityDate(null);
                    }
                }
            }
        }

        loadAllSeries();

        for (Website website : websites) {
            if (website.getMustBeSaved()) {
                dao.update(website);
            }
        }
    }

    public SortedSet<Website> loadSiteForSerie(Serie serie) {
        if (serie != null) {
            LOG.debug("Load all site containing serie :" + serie);
            Collection<Website> websites = dao.getAll();

            SortedSet<Website> websitesContainingSerie = new TreeSet<>();
            for (Website website : websites) {
                for (Serie serieToTest : website) {
                    if (serieToTest.getName().equals(serie.getName())) {
                        websitesContainingSerie.add(website);
                        break;
                    }
                }
            }

            for (Website website : websitesContainingSerie) {
                if (website.getMustBeSaved()) {
                    dao.update(website);
                }
            }

            return websitesContainingSerie;
        } else {
            return new TreeSet<>();
        }
    }

    public SortedSet<Chapter> loadChapterList(Website website, Serie serieToFind) {
        if (website != null && serieToFind != null) {
            LOG.debug("Load chapter list [Site : " + website + ";Serie : " + serieToFind);
            SortedSet<Chapter> chapters = new TreeSet<>();
            for (Serie serie : website) {
                if (serie.getName().equals(serieToFind.getName())) {
                    for (Tome tome : serie) {
                        chapters.addAll(tome.getChapters());
                    }
                }
            }

            if (website.getMustBeSaved()) {
                dao.update(website);
            }

            return chapters;
        } else {
            return new TreeSet<>();
        }
    }

    public Collection<Chapter> getChapters(final Collection<Image> images) {
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
