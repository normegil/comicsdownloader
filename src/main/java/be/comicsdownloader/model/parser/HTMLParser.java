package be.comicsdownloader.model.parser;

import be.comicsdownloader.model.pojo.manga.MangaPojo;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

public abstract class HTMLParser implements Parser {

    private static final Logger LOG = Logger.getLogger(HTMLParser.class);

    @Override
    public List parse(final ParsingPart partToParse, final String toParse, final MangaPojo parent) {
        if (toParse != null && partToParse != null) {
            Document document = getXMLDocumentFromString(toParse);
            List objects = parseHTML(partToParse, document, parent);

            return objects;
        } else if (toParse == null) {
            throw new IllegalArgumentException("String to parse cannot be null");
        } else {
            throw new IllegalArgumentException("Parsing part cannot be null");
        }
    }

    protected abstract List parseHTML(ParsingPart partToParse, Document document, MangaPojo parent);

    protected Document getXMLDocumentFromString(final String toParse) {
        LOG.debug("JSoup parsing document");
        return (Document) Jsoup.parse(toParse);
    }
}
