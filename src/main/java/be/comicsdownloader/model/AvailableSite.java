package be.comicsdownloader.model;

import be.comicsdownloader.model.parser.Parser;
import be.comicsdownloader.model.parser.manga.MangaFoxParser;
import be.comicsdownloader.model.parser.manga.MangaHereParser;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum AvailableSite {

    @XmlEnumValue(value = "MangaHere")
    MANGA_HERE("MangaHere", "http://www.mangahere.com/mangalist/", 0, new MangaHereParser()),
    @XmlEnumValue(value = "MangaFox")
    MANGA_FOX("MangaFox", "http://www.mangafox.me/manga/", 1, new MangaFoxParser());
    private String name;
    private Parser parser;
    private String url;
    private long id;

    private AvailableSite(String name, String url, long id, Parser parser) {
        this.name = name;
        this.parser = parser;
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public long getId() {
        return id;
    }

    public Parser getParser() {
        return parser;
    }

    public String getName() {
        return name;
    }
}
