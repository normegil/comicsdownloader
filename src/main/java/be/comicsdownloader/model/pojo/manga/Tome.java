package be.comicsdownloader.model.pojo.manga;

import be.comicsdownloader.model.parser.Parser.ParsingPart;
import org.apache.commons.lang.builder.CompareToBuilder;

import javax.xml.bind.annotation.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@XmlAccessorType(XmlAccessType.FIELD)
public class Tome extends MangaPojo implements Iterable<Chapter>, Comparable<Tome> {

    private static final int HASH_SEED = 11;
    private String name;
    @XmlAttribute
    private float number;
    @XmlElementWrapper(name = "chapters")
    private Set<Chapter> chapter;
    @XmlTransient
    private Serie serie;
    @XmlTransient
    private boolean mustBeSaved = false;

    public Tome() {
        chapter = new TreeSet<>();
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addChapter(Chapter chapterToAdd) {
        chapter.add(chapterToAdd);
    }

    public void removeChapter(Chapter chapterToUpdate) {
        chapter.remove(chapterToUpdate);
    }

    public Set<Chapter> getChapters() {
        refreshChapters();
        attributeSerieToChildTomes();
        return chapter;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie parentSerie) {
        this.serie = parentSerie;
    }

    protected void refreshChapters() {
        List<Object> loadedCollection = refreshCollection(getSerie().getWebsite().getAvailableSite(), ParsingPart.CHAPTERS);
        if (loadedCollection != null) {
            for (Object object : loadedCollection) {
                addChapter((Chapter) object);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = HASH_SEED;
        hash = HASH_SEED * hash + Float.floatToIntBits(this.number);
        hash = HASH_SEED * hash + this.getSerie().hashCode();
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
        final Tome other = (Tome) obj;
        if (this.number != other.number) {
            return false;
        }
        if (!this.getSerie().equals(other.getSerie())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String numberString;
        if (getNumber() == (int) getNumber()) {
            numberString = String.format("%d", (int) getNumber());
        } else {
            numberString = String.format("%s", getNumber());
        }
        return "Tome " + numberString;
    }

    @Override
    public Iterator<Chapter> iterator() {
        refreshChapters();
        attributeSerieToChildTomes();
        return chapter.iterator();
    }

    @Override
    public boolean getMustBeSaved() {
        if (mustBeSaved) {
            return true;
        }
        for (Chapter chapterToChange : chapter) {
            if (chapterToChange.getMustBeSaved()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setMustBeSaved(boolean mustBeSaved) {
        this.mustBeSaved = mustBeSaved;
        if (!mustBeSaved) {
            for (Chapter chapterToChange : chapter) {
                chapterToChange.setMustBeSaved(mustBeSaved);
            }
        }
    }

    @Override
    public int compareTo(Tome t) {
        return new CompareToBuilder().append(getSerie(), t.getSerie()).append(getNumber(), t.getNumber()).toComparison();
    }

    protected void attributeSerieToChildTomes() {
        for (Chapter chapterToCheck : chapter) {
            if (chapterToCheck.getTome() == null) {
                chapterToCheck.setTome(this);
            }
        }
    }
}
