package be.comicsdownloader.model.pojo.manga;

import be.comicsdownloader.model.AvailableSite;
import be.comicsdownloader.model.parser.Parser.ParsingPart;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Website extends MangaPojo implements Iterable<Serie>, Comparable<Website> {

    private static final int HASH_SEED = 59;
    private String name;
    @XmlElementWrapper(name = "Series")
    private Set<Serie> serie;
    private AvailableSite availableSite;
    @XmlTransient
    private boolean mustBeSaved = false;

    public Website() {
        serie = new TreeSet<>();
    }

    public AvailableSite getAvailableSite() {
        return availableSite;
    }

    public void setAvailableSite(AvailableSite availableSite) {
        this.availableSite = availableSite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Iterator<Serie> iterator() {
        refreshSeries();
        attributeWebsiteToChildSeries();
        return serie.iterator();
    }

    public void addSerie(Serie serieToAdd) {
        serie.add(serieToAdd);
    }

    public Set<Serie> getSeries() {
        refreshSeries();
        attributeWebsiteToChildSeries();
        return serie;
    }

    @Override
    public int hashCode() {
        int hash = HASH_SEED;
        hash = HASH_SEED * hash + Objects.hashCode(this.name);
        hash = HASH_SEED * hash + (this.availableSite != null ? this.availableSite.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Website other = (Website) obj;
        if (this.availableSite != other.availableSite) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    protected void refreshSeries() {
        List<Object> loadedCollection = refreshCollection(availableSite, ParsingPart.SERIES);
        if (loadedCollection != null) {
            for (Object object : loadedCollection) {
                addSerie((Serie) object);
            }
        }
    }

    @Override
    public boolean getMustBeSaved() {
        if (mustBeSaved) {
            return true;
        }
        for (Serie serieToTest : serie) {
            if (serieToTest.getMustBeSaved()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setMustBeSaved(boolean mustBeSaved) {
        this.mustBeSaved = mustBeSaved;
        if (!mustBeSaved) {
            for (Serie serieToTest : serie) {
                serieToTest.setMustBeSaved(mustBeSaved);
            }
        }
    }

    @Override
    public int compareTo(Website t) {
        return getName().compareTo(t.getName());
    }

    protected void attributeWebsiteToChildSeries() {
        for (Serie serieToCheck : serie) {
            if (serieToCheck.getWebsite() == null) {
                serieToCheck.setWebsite(this);
            }
        }
    }
}
