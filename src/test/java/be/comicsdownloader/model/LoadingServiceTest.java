package be.comicsdownloader.model;

import be.comicsdownloader.generators.TestStrutureCreator;
import be.comicsdownloader.model.persistance.XmlWebsiteDAO;
import be.comicsdownloader.model.pojo.manga.Chapter;
import be.comicsdownloader.model.pojo.manga.Serie;
import be.comicsdownloader.model.pojo.manga.Tome;
import be.comicsdownloader.model.pojo.manga.Website;
import be.comicsdownloader.model.service.LoadingService;
import be.comicsdownloader.model.service.PropertiesService;
import be.comicsdownloader.model.service.Services;
import org.apache.commons.lang.NotImplementedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import static org.junit.Assert.*;

public class LoadingServiceTest {

    private List<Website> websites;
    private List<Serie> series;
    private LoadingService service;

    public LoadingServiceTest() {
    }

    @Before
    public void setUp() throws IOException {
        PropertiesService.setInstance(File.createTempFile("DummySettings", ".properties"));
        Services.getPropertyService().setProperty(PropertiesService.PropertyKey.AUTO_REFRESH, false);

        websites = new LinkedList<>();
        websites.add(TestStrutureCreator.createWebsite(AvailableSite.MANGA_HERE));
        websites.add(TestStrutureCreator.createWebsite(AvailableSite.MANGA_FOX));

        File tempFile = File.createTempFile("LoadingServiceTest-Data", ".xml");
        service = LoadingService.setInstance(tempFile.getAbsolutePath());
        XmlWebsiteDAO dao = new XmlWebsiteDAO(tempFile);
        series = new LinkedList<>();
        for (Website website : websites) {
            dao.insert(website);
            series.addAll(website.getSeries());
        }
    }

    @After
    public void tearDown() {
        PropertiesService.setInstance(null);
        service = null;
        websites = null;
    }

    @Test
    public void testLoadAllSeries() {
        SortedSet<Serie> loadedSeries = service.loadAllSeries();

        assertNotNull(loadedSeries);
        for (Serie serie : series) {
            boolean found = false;
            for (Serie loadedSerie : loadedSeries) {
                if (loadedSerie.equals(serie)) {
                    found = true;
                }
            }
            if (!found) {
                fail("Serie " + serie.getName() + " not found in loaded series");
            }
        }

    }

    @Test
    public void testLoadSerieForSite() {
        Set<Serie> loadedSeries = service.loadSerieForSite(AvailableSite.MANGA_HERE);
        assertNotNull(loadedSeries);
        for (Serie serie : series) {
            if (serie.getWebsite().getAvailableSite().equals(AvailableSite.MANGA_HERE)) {
                boolean found = false;
                for (Serie loadedSerie : loadedSeries) {
                    if (loadedSerie.equals(serie)) {
                        found = true;
                    }
                }
                if (!found) {
                    fail("Serie " + serie.getName() + " not found in loaded series");
                }
            }
        }
    }

    @Test
    public void testLoadSerieForNullSite() {
        Set<Serie> emptySeries = service.loadSerieForSite(null);

        assertNotNull(emptySeries);
        assertTrue(emptySeries.isEmpty());
    }

    @Test
    public void testLoadSiteForSerie() {
        final Website website = websites.iterator().next();
        Set<Website> loadedWebsites = service.loadSiteForSerie(website.iterator().next());

        assertEquals(websites.size(), loadedWebsites.size());
    }

    @Test
    public void testLoadSiteForNullSerie() {
        Set<Website> emptyWebsiteSet = service.loadSiteForSerie(null);
        assertNotNull(emptyWebsiteSet);
        assertTrue(emptyWebsiteSet.isEmpty());
    }

    @Test
    public void testLoadSiteForNotLinkedSerie() {
        Serie serie = new Serie();
        Set<Website> fakeSerieWebsites = service.loadSiteForSerie(serie);
        assertNotNull(fakeSerieWebsites);
        assertTrue(fakeSerieWebsites.isEmpty());
    }

    @Test
    public void testLoadChapterList() {
        Website website = websites.iterator().next();
        Serie serie = website.iterator().next();
        Set<Chapter> chapters = service.loadChapterList(website, serie);

        for (Tome tome : serie) {
            for (Chapter chapter : tome) {

                boolean found = false;
                for (Chapter loadedChapter : chapters) {
                    if (loadedChapter.equals(chapter)) {
                        found = true;
                    }
                }
                if (!found) {
                    fail("Chapter " + chapter.getName() + " not found in loaded chapters");
                }
            }
        }
    }

    @Test
    public void testLoadChapterWithNullParameters() {
        Serie serie = websites.iterator().next().iterator().next();
        Set<Chapter> emptyWebsiteChapters = service.loadChapterList(null, serie);
        assertNotNull(emptyWebsiteChapters);
        assertTrue(emptyWebsiteChapters.isEmpty());

        Set<Chapter> emptySerieChapters = service.loadChapterList(websites.iterator().next(), null);
        assertNotNull(emptySerieChapters);
        assertTrue(emptySerieChapters.isEmpty());
    }

    public void testRefreshListSerie() {
        throw new NotImplementedException();
    }
}
