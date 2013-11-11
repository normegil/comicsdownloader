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

public class MangaFoxParser extends AbstractMangaParser {

    private static final Logger LOG = Logger.getLogger(MangaHereParser.class);
    private static final String HTML_LINK = "href";

    @Override
    protected List<Serie> parseSeries(Document htmlDocument, Website parent) {
        List<Serie> series = new LinkedList<>();

        Elements links = htmlDocument.select("a.series_preview");
        for (Element link : links) {
            Serie serie = new Serie();
            serie.setMustBeSaved(true);
            serie.setUrl(link.attr(HTML_LINK));
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
        float highestTomeNumber = -1;

        Elements twitterCount = htmlDocument.select("span.st_twitter_vcount");
        String url = twitterCount.attr("st_url");

        Elements chapterList = htmlDocument.select("a.tips");
        for (Element chapterLink : chapterList) {
            String linkToImage = chapterLink.attr(HTML_LINK);

            String tomeAndChapterInfo = StringUtils.substringAfter(linkToImage, url);

            float tomeNumber;
            try {
                tomeNumber = Float.parseFloat(StringUtils.substringBetween(tomeAndChapterInfo, "v", "/c"));
            } catch (NumberFormatException ex) {
                tomeNumber = -1f;
            }

            Tome foundTome = null;
            boolean found = false;
            for (Tome tome : tomes) {
                if (tomeNumber == tome.getNumber()) {
                    foundTome = tome;
                    found = true;
                }
            }

            if (!found) {
                foundTome = new Tome();
                foundTome.setNumber(tomeNumber);
                foundTome.setName("Tome " + tomeNumber);
                foundTome.setMustBeSaved(true);
                foundTome.setValidityDate(today);
                foundTome.setSerie(parent);

                tomes.add(foundTome);
            }

            if (highestTomeNumber < foundTome.getNumber()) {
                highestTomeNumber = foundTome.getNumber();
            }

            float chapterNumber = Float.parseFloat(StringUtils.substringBetween(tomeAndChapterInfo, "/c", "/"));

            Chapter chapter = new Chapter();
            chapter.setMustBeSaved(true);
            chapter.setUrl(linkToImage);
            chapter.setNumber(chapterNumber);
            chapter.setName(chapterLink.text());
            chapter.setTome(foundTome);
            foundTome.addChapter(chapter);
        }

        for (Tome tome : tomes) {
            if (tome.getNumber() == -1f) {
                tome.setNumber(highestTomeNumber + 1);
                break;
            }
        }

        parent.setValidityDate(today);
        return tomes;
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

    @Override
    protected List<Image> parseImages(Document htmlDocument, Chapter parent) {
        NetLoader loader = new NetLoader();
        parent.setValidityDate(new Date());
        return getListOfFollowingImages(loader, htmlDocument, -1, parent);
    }

    private List<Image> getListOfFollowingImages(Loader loader, Document htmlDocument, int previousNumber, Chapter parent) {

        Element linkToComments = htmlDocument.select("a#comments").first();
        if (linkToComments != null) {
            String baseUrl = linkToComments.attr(HTML_LINK);


            Element viewer = htmlDocument.select("div#viewer").first();
            if (viewer != null) {
                Element link = viewer.select("a").first();
                Element img = link.select("img").first();

                String incompleteUrlToNextPage = link.attr(HTML_LINK);
                String urlToNextPage = baseUrl + incompleteUrlToNextPage;
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
        }
        return new LinkedList<>();
    }
}
