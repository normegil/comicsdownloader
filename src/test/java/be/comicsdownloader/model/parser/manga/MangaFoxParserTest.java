package be.comicsdownloader.model.parser.manga;

import be.comicsdownloader.generators.TestStrutureCreator;
import be.comicsdownloader.model.AvailableSite;
import be.comicsdownloader.model.parser.Parser;
import be.comicsdownloader.model.pojo.manga.*;
import be.comicsdownloader.model.service.PropertiesService;
import be.comicsdownloader.model.service.Services;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class MangaFoxParserTest {

    private static final String PATH_TO_RESOURCES = "src/test/resources/be/comicsdownloader/model/parser/mangafox/";
    private AvailableSite site = AvailableSite.MANGA_FOX;
    private Parser parser = new MangaFoxParser();
    private String mangaFoxMangaListPageSnapshot;
    private String mangaFoxTomeListPageSnapshot;
    private String mangaFoxImagePageSnapshot;
    private Website website;

    public MangaFoxParserTest() throws IOException {
        mangaFoxMangaListPageSnapshot = FileUtils.readFileToString(new File(PATH_TO_RESOURCES + "MangaList.html"));
        mangaFoxTomeListPageSnapshot = FileUtils.readFileToString(new File(PATH_TO_RESOURCES + "TomeChapter.html"));
        mangaFoxImagePageSnapshot = FileUtils.readFileToString(new File(PATH_TO_RESOURCES + "Image.html"));
    }

    @Before
    public void setUp() throws IOException {
        PropertiesService.setInstance(File.createTempFile("DummySettings", ".properties"));
        Services.getPropertyService().setProperty(PropertiesService.PropertyKey.AUTO_REFRESH, false);
        website = TestStrutureCreator.createWebsite(site);
    }

    @After
    public void tearDown() {
        PropertiesService.setInstance(null);
        website = null;
    }

    @Test
    public void testParsingNull() {
        try {
            parser.parse(null, mangaFoxMangaListPageSnapshot, website);
            fail("Parsing a null part should throw an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testParseSeries() {
        List<Serie> series = parser.parse(Parser.ParsingPart.SERIES, mangaFoxMangaListPageSnapshot, website);

        assertNotNull(series);
        assertFalse(series.isEmpty());
    }

    @Test
    public void testParseSeriesWithWrongSource() {
        List<Serie> wrongSeries = parser.parse(Parser.ParsingPart.SERIES, mangaFoxImagePageSnapshot, website);

        assertNotNull(wrongSeries);
        assertTrue(wrongSeries.isEmpty());
    }

    @Test
    public void testParseSeriesWithNullSource() {
        try {
            parser.parse(Parser.ParsingPart.SERIES, null, website);
            fail("Parsing a null string should throw an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testParseTomes() {
        List<Tome> tomes = parser.parse(Parser.ParsingPart.TOMES, mangaFoxTomeListPageSnapshot, website.getSeries().iterator().next());

        assertNotNull(tomes);
        assertFalse(tomes.isEmpty());
    }

    @Test
    public void testParseTomesWithNullSource() {
        try {
            parser.parse(Parser.ParsingPart.TOMES, null, website.getSeries().iterator().next());
            fail("Parsing a null string should throw an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testParseWithWrongSource() {
        List<Tome> wrongTomes = parser.parse(Parser.ParsingPart.TOMES, mangaFoxImagePageSnapshot, website.getSeries().iterator().next());

        assertNotNull(wrongTomes);
        assertTrue(wrongTomes.isEmpty());
    }

    @Test
    public void testParseChapters() {
        Set<Serie> series = website.getSeries();
        Serie serie = series.iterator().next();
        Tome tome = serie.iterator().next();
        tome.setNumber(1f);
        List<Chapter> chapters = parser.parse(Parser.ParsingPart.CHAPTERS, mangaFoxTomeListPageSnapshot, tome);

        assertNotNull(chapters);
        assertFalse(chapters.isEmpty());
    }

    @Test
    public void testParseChaptersWithNullSource() {
        try {
            final Tome Tome = website.getSeries().iterator().next().iterator().next();
            List<Chapter> chapters = parser.parse(Parser.ParsingPart.CHAPTERS, null, Tome);
            fail("Parsing a null string should throw an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testParseChaptersWithWrongSource() {
        final Tome Tome = website.getSeries().iterator().next().iterator().next();
        List<Chapter> chapters = parser.parse(Parser.ParsingPart.CHAPTERS, mangaFoxImagePageSnapshot, Tome);

        assertNotNull(chapters);
        assertTrue(chapters.isEmpty());
    }

    @Test
    public void testParseImages() {
        List<Image> images = parser.parse(Parser.ParsingPart.IMAGES, mangaFoxImagePageSnapshot, website.getSeries().iterator().next().iterator().next().iterator().next());

        assertNotNull(images);
        assertFalse(images.isEmpty());
    }

    @Test
    public void testParseImageWithWrongSource() {
        List<Image> wrongImages = parser.parse(Parser.ParsingPart.IMAGES, mangaFoxMangaListPageSnapshot, website.getSeries().iterator().next().iterator().next().iterator().next());

        assertNotNull(wrongImages);
        assertTrue(wrongImages.isEmpty());
    }

    @Test
    public void testParseImageWithNullSource() {
        try {
            parser.parse(Parser.ParsingPart.IMAGES, null, website.getSeries().iterator().next().iterator().next().iterator().next());
            fail("Parsing a null string should throw an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
        }
    }

}
