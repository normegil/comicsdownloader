package be.comicsdownloader.model.parser.manga;

import be.comicsdownloader.model.loader.Loader;
import be.comicsdownloader.model.loader.NetLoader;
import be.comicsdownloader.model.pojo.manga.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MangaHereParser extends AbstractMangaParser {

    private static final Logger LOG = Logger.getLogger(MangaHereParser.class);

    @Override
    protected List<Serie> parseSeries(Document htmlDocument, Website parent) {
        List<Serie> series = new LinkedList<>();

        Elements links = htmlDocument.select("a.manga_info");
        for (Element link : links) {
            Serie serie = new Serie();
            serie.setMustBeSaved(true);
            serie.setUrl(link.attr("href"));
            serie.setName(link.text());
            serie.setWebsite(parent);

            series.add(serie);
        }
        parent.setValidityDate(new Date());
        return series;

    }

    @Override
    protected List<Tome> parseTomes(Document htmlDocument, Serie parent) {
        Date today = new Date();
        List<Tome> tomes = new LinkedList<>();

        Elements divChapters = htmlDocument.select("div.detail_list");
        if (!divChapters.isEmpty()) {
            Elements spansLeft = divChapters.first().select("span.left");
            if (!spansLeft.isEmpty()) {
                for (Element span : spansLeft) {
                    Elements tomeNumberElements = span.select("span.mr6");
                    final String tomeNumberString = StringUtils.substringAfter(tomeNumberElements.first().text(), "Vol ");
                    int tomeNumber = 0;
                    if (tomeNumberString != null && !tomeNumberString.isEmpty()) {
                        Integer.parseInt(tomeNumberString);
                    }

                    Tome foundTome = null;
                    for (Tome tome : tomes) {
                        if (tomeNumber == tome.getNumber()) {
                            foundTome = tome;
                            break;
                        }
                    }

                    if (foundTome == null) {
                        Tome tome = new Tome();
                        tome.setNumber(tomeNumber);
                        tome.setName("Tome " + tomeNumber);
                        tome.setMustBeSaved(true);
                        tome.setValidityDate(today);
                        tome.setSerie(parent);

                        tomes.add(tome);
                        foundTome = tome;
                    }

                    Element link = span.select("a").first();

                    Chapter chapter = new Chapter();
                    chapter.setMustBeSaved(true);
                    chapter.setUrl(link.attr("href"));
                    String chapterNumberToParse = link.text();
                    String tempNumber = StringUtils.substringAfterLast(chapterNumberToParse, " ");
                    chapter.setNumber(Float.parseFloat(tempNumber));
                    chapter.setName(span.text());
                    chapter.setTome(foundTome);

                    foundTome.addChapter(chapter);
                }
            }
        }

        parent.setValidityDate(today);
        return tomes;
    }

    @Override
    protected List<Image> parseImages(Document htmlDocument, Chapter parent) {
        NetLoader loader = new NetLoader();
        parent.setValidityDate(new Date());
        return getListOfFollowingImages(loader, htmlDocument, -1, parent);
    }

    @Override
    protected List<Chapter> parseChapters(Document htmlDocument, Tome parent) {
        List<Chapter> chapters = new LinkedList<>();
        List<Tome> tomes = parseTomes(htmlDocument, parent.getSerie());

        for (Tome tome : tomes) {
            if (tome.equals(parent)) {
                chapters.addAll(tome.getChapters());
            }
        }

        return chapters;
    }

    private List<Image> getListOfFollowingImages(Loader loader, Document htmlDocument, int previousNumber, Chapter parent) {
        Element viewer = htmlDocument.select("section#viewer").first();
        if (viewer != null) {
            Element link = viewer.select("a").first();
            Element img = link.select("img").first();

            String urlToNextPage = link.attr("href");
            if (urlToNextPage.contains("javascript")) {
                Image image = new Image();
                image.setMustBeSaved(true);
                image.setNumber(previousNumber + 1);
                image.setUrl(img.attr("src"));
                image.setChapter(parent);
                return Arrays.asList(image);
            } else {
                List<Image> images = new LinkedList<>();
                try {
                    images.addAll(getListOfFollowingImages(loader, getXMLDocumentFromString(loader.loadDocument(new URL(urlToNextPage))), previousNumber + 1, parent));
                } catch (MalformedURLException ex) {
                    LOG.error("Crash : cannot parse given URL : " + urlToNextPage, ex);
                }
                Image image = new Image();
                image.setMustBeSaved(true);
                image.setNumber(previousNumber + 1);
                image.setUrl(img.attr("src"));
                image.setChapter(parent);
                images.add(image);
                return images;
            }
        }

        return new LinkedList<>();
    }
}
