package be.comicsdownloader.model.parser;

import be.comicsdownloader.model.pojo.manga.MangaPojo;

import java.util.List;

public interface Parser {

    public enum ParsingPart {

        SERIES,
        TOMES,
        CHAPTERS,
        IMAGES;
    }

    List parse(ParsingPart partToParse, String toParse, MangaPojo parent);
}
