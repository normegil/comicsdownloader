package be.comicsdownloader.model.pojo.manga;

import be.comicsdownloader.model.parser.Parser.ParsingPart;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Serie extends MangaPojo implements Iterable<Tome>, Comparable<Serie> {

    private static final int HASH_SEED = 83;
    private String name;
    @XmlElementWrapper(name = "tomes")
    private Set<Tome> tome;
    @XmlTransient
    private Website website;
    @XmlTransient
    private boolean mustBeSaved = false;

    public Serie() {
        tome = new TreeSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTome(Tome tomeToAdd) {
        tome.add(tomeToAdd);
    }

    public void removeTome(Tome tomeToUpdate) {
        tome.remove(tomeToUpdate);
    }

    public Set<Tome> getTomes() {
        refreshTomes();
        attributeSerieToChildTomes();
        return tome;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    protected void refreshTomes() {
        List<Object> loadedCollection = refreshCollection(getWebsite().getAvailableSite(), ParsingPart.TOMES);
        if (loadedCollection != null) {
            for (Object object : loadedCollection) {
                addTome((Tome) object);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = HASH_SEED;
        hash = HASH_SEED * hash + Objects.hashCode(this.name);
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
        final Serie other = (Serie) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Iterator<Tome> iterator() {
        refreshTomes();
        attributeSerieToChildTomes();
        return tome.iterator();
    }

    @Override
    public boolean getMustBeSaved() {
        if (mustBeSaved) {
            return true;
        }
        for (Tome tomeToChange : tome) {
            if (tomeToChange.getMustBeSaved()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setMustBeSaved(boolean mustBeSaved) {
        this.mustBeSaved = mustBeSaved;
        if (!mustBeSaved) {
            for (Tome tomeToChange : tome) {
                tomeToChange.setMustBeSaved(mustBeSaved);
            }
        }
    }

    @Override
    public int compareTo(Serie t) {
        return this.getName().compareTo(t.getName());
    }

    protected void attributeSerieToChildTomes() {
        for (Tome tomeToCheck : tome) {
            if (tomeToCheck.getSerie() == null) {
                tomeToCheck.setSerie(this);
            }
        }
    }
}
