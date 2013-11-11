package be.comicsdownloader.model.parser.manga;

import be.comicsdownloader.model.parser.HTMLParser;
import be.comicsdownloader.model.pojo.manga.*;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import java.util.List;

public abstract class AbstractMangaParser extends HTMLParser {

    private static final Logger LOG = Logger.getLogger(AbstractMangaParser.class);

    @Override
    protected List parseHTML(final ParsingPart partToParse, final Document document, final MangaPojo parent) {
        if (ParsingPart.SERIES.equals(partToParse)) {
            if (parent instanceof Website) {
                LOG.debug("Parse series [Parent website=" + parent + "]");
                return parseSeries(document, (Website) parent);
            } else {
                throw new IllegalArgumentException("Wrong parent for Series [Parent = " + parent + "]");
            }
        } else if (ParsingPart.TOMES.equals(partToParse)) {
            if (parent instanceof Serie) {
                LOG.debug("Parse tomes and chapters [Parent serie=" + parent + "]");
                return parseTomes(document, (Serie) parent);
            } else {
                throw new IllegalArgumentException("Wrong parent for Tomes [Parent = " + parent + "]");
            }
        } else if (ParsingPart.CHAPTERS.equals(partToParse)) {
            if (parent instanceof Tome) {
                LOG.debug("Parse chapter [Parent tome=" + parent + "]");
                return parseChapters(document, (Tome) parent);
            } else {
                throw new IllegalArgumentException("Wrong parent for Chapters [Parent = " + parent + "]");
            }
        } else if (ParsingPart.IMAGES.equals(partToParse)) {
            if (parent instanceof Chapter) {
                LOG.debug("Parse images [Parent chapter=" + parent + "]");
                return parseImages(document, (Chapter) parent);
            } else {
                throw new IllegalArgumentException("Wrong parent for Images [Parent = " + parent + "]");
            }
        } else {
            throw new IllegalArgumentException("Part " + partToParse + " not recognised");
        }
    }

    protected abstract List<Serie> parseSeries(Document htmlDocument, Website parent);

    protected abstract List<Tome> parseTomes(Document htmlDocument, Serie parent);

    protected abstract List<Chapter> parseChapters(Document htmlDocument, Tome parent);

    protected abstract List<Image> parseImages(Document htmlDocument, Chapter parent);
}
