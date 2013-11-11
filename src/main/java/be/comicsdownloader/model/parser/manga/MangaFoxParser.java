package be.comicsdownloader.model.parser.manga;

import be.comicsdownloader.model.pojo.manga.*;
import org.jsoup.nodes.Document;

import java.util.LinkedList;
import java.util.List;

public class MangaFoxParser extends AbstractMangaParser {

    @Override
    protected List<Serie> parseSeries(Document htmlDocument, Website parent) {
        return new LinkedList<>();
    }

    @Override
    protected List<Tome> parseTomes(Document htmlDocument, Serie parent) {
        return new LinkedList<>();
    }

    @Override
    protected List<Chapter> parseChapters(Document htmlDocument, Tome parent) {
        return new LinkedList<>();
    }

    @Override
    protected List<Image> parseImages(Document htmlDocument, Chapter parent) {
        return new LinkedList<>();
    }
}
