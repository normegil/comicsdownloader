package be.comicsdownloader.model.parser.manga;

import be.comicsdownloader.generators.TestStrutureCreator;
import be.comicsdownloader.model.AvailableSite;
import be.comicsdownloader.model.parser.Parser;
import be.comicsdownloader.model.parser.Parser.ParsingPart;
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

public class MangaHereParserTest {

    private static final String PATH_TO_RESOURCES = "src/test/resources/be/comicsdownloader/model/parser/mangahere/";
    private String mangaHereMangaListPageSnapshot;
    private String mangaHereTomeListPageSnapshot;
    private String mangaHereImagePageSnapshot;
    private Parser parser = new MangaHereParser();
    private Website website;

    public MangaHereParserTest() throws IOException {
        parser = new MangaHereParser();
        mangaHereMangaListPageSnapshot = FileUtils.readFileToString(new File(PATH_TO_RESOURCES + "MangaList.html"));
        mangaHereTomeListPageSnapshot = FileUtils.readFileToString(new File(PATH_TO_RESOURCES + "TomeChapter.html"));
        mangaHereImagePageSnapshot = FileUtils.readFileToString(new File(PATH_TO_RESOURCES + "Image.html"));
    }

    @Before
    public void setUp() throws IOException {
        PropertiesService.setInstance(File.createTempFile("DummySettings", ".properties"));
        Services.getPropertyService().setProperty(PropertiesService.PropertyKey.AUTO_REFRESH, false);
        website = TestStrutureCreator.createWebsite(AvailableSite.MANGA_HERE);
    }

    @After
    public void tearDown() {
        PropertiesService.setInstance(null);
        website = null;
    }

    @Test
    public void testParsingNull() {
        try {
            parser.parse(null, mangaHereMangaListPageSnapshot, website);
            fail("Parsing a null part should throw an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testParseSeries() {
        List<Serie> series = parser.parse(ParsingPart.SERIES, mangaHereMangaListPageSnapshot, website);

        assertNotNull(series);
        assertFalse(series.isEmpty());
    }

    @Test
    public void testParseSeriesWithWrongSource() {
        List<Serie> wrongSeries = parser.parse(ParsingPart.SERIES, mangaHereImagePageSnapshot, website);

        assertNotNull(wrongSeries);
        assertTrue(wrongSeries.isEmpty());
    }

    @Test
    public void testParseSeriesWithNullSource() {
        try {
            parser.parse(ParsingPart.SERIES, null, website);
            fail("Parsing a null string should throw an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testParseTomes() {
        List<Tome> tomes = parser.parse(ParsingPart.TOMES, mangaHereTomeListPageSnapshot, website.getSeries().iterator().next());

        assertNotNull(tomes);
        assertFalse(tomes.isEmpty());
    }

    @Test
    public void testParseTomesWithNullSource() {
        try {
            parser.parse(ParsingPart.TOMES, null, website.getSeries().iterator().next());
            fail("Parsing a null string should throw an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testParseWithWrongSource() {
        List<Tome> wrongTomes = parser.parse(ParsingPart.TOMES, mangaHereImagePageSnapshot, website.getSeries().iterator().next());

        assertNotNull(wrongTomes);
        assertTrue(wrongTomes.isEmpty());
    }

    @Test
    public void testParseChapters() {
        Set<Serie> series = website.getSeries();
        Serie serie = series.iterator().next();
        Tome Tome = serie.iterator().next();
        List<Chapter> chapters = parser.parse(ParsingPart.CHAPTERS, mangaHereTomeListPageSnapshot, Tome);

        assertNotNull(chapters);
        assertFalse(chapters.isEmpty());
    }

    @Test
    public void testParseChaptersWithNullSource() {
        try {
            final Tome Tome = website.getSeries().iterator().next().iterator().next();
            List<Chapter> chapters = parser.parse(ParsingPart.CHAPTERS, null, Tome);
            fail("Parsing a null string should throw an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testParseChaptersWithWrongSource() {
        final Tome Tome = website.getSeries().iterator().next().iterator().next();
        List<Chapter> chapters = parser.parse(ParsingPart.CHAPTERS, mangaHereImagePageSnapshot, Tome);

        assertNotNull(chapters);
        assertTrue(chapters.isEmpty());
    }

    @Test
    public void testParseImages() {
        List<Image> images = parser.parse(ParsingPart.IMAGES, mangaHereImagePageSnapshot, website.getSeries().iterator().next().iterator().next().iterator().next());

        assertNotNull(images);
        assertFalse(images.isEmpty());
    }

    @Test
    public void testParseImageWithWrongSource() {
        List<Image> wrongImages = parser.parse(ParsingPart.IMAGES, mangaHereMangaListPageSnapshot, website.getSeries().iterator().next().iterator().next().iterator().next());

        assertNotNull(wrongImages);
        assertTrue(wrongImages.isEmpty());
    }

    @Test
    public void testParseImageWithNullSource() {
        try {
            parser.parse(ParsingPart.IMAGES, null, website.getSeries().iterator().next().iterator().next().iterator().next());
            fail("Parsing a null string should throw an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
        }
    }
}
