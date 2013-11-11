package be.comicsdownloader.model.pojo.manga;

import be.comicsdownloader.model.parser.Parser.ParsingPart;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@XmlAccessorType(XmlAccessType.FIELD)
public class Chapter extends MangaPojo implements Iterable<Image>, Comparable<Chapter> {

    private static final Logger LOG = Logger.getLogger(Chapter.class);
    private static final int HASH_SEED = 97;
    private float number;
    private String name;
    @XmlElementWrapper(name = "images")
    private Set<Image> image;
    @XmlTransient
    private Tome tome;
    @XmlTransient
    private boolean mustBeSaved = false;

    public Chapter() {
        image = new TreeSet<>();
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

    public Set<Image> getAllImages() {
        refreshImages();
        attributeSerieToChildTomes();
        return image;
    }

    public void addImage(Image imageToAdd) {
        image.add(imageToAdd);
    }

    public void removeImage(Image imageToRemove) {
        image.remove(imageToRemove);
    }

    public Tome getTome() {
        return tome;
    }

    public void setTome(Tome tome) {
        this.tome = tome;
    }

    protected void refreshImages() {
        List<Object> loadedCollection = refreshCollection(getTome().getSerie().getWebsite().getAvailableSite(), ParsingPart.IMAGES);
        if (loadedCollection != null) {
            for (Object object : loadedCollection) {
                addImage((Image) object);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = HASH_SEED;
        hash = HASH_SEED * hash + Float.floatToIntBits(this.number);
        hash = HASH_SEED * hash + this.getTome().hashCode();
        LOG.trace("[" + toString() + ";HashCode=" + hash + "]");
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
        final Chapter otherChapter = (Chapter) obj;
        if (Float.floatToIntBits(this.number) != Float.floatToIntBits(otherChapter.number)) {
            return false;
        }
        if (!this.getTome().equals(otherChapter.getTome())) {
            return false;
        }
        LOG.trace("Chapters are equals [Serie 1=" + getTome().getSerie().toString() + ";Serie 2=" + (otherChapter).getTome().getSerie().toString() + "|Chapter 1=" + toString() + ";Chapter 2=" + obj.toString() + "]");
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
        return getTome().toString() + " - Chapter " + numberString + "(" + getTome().getSerie().toString() + ")";
    }

    @Override
    public Iterator<Image> iterator() {
        refreshImages();
        attributeSerieToChildTomes();
        return image.iterator();
    }

    @Override
    public boolean getMustBeSaved() {
        if (mustBeSaved) {
            return true;
        }
        for (Image imageChanged : image) {
            if (imageChanged.getMustBeSaved()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setMustBeSaved(boolean mustBeSaved) {
        this.mustBeSaved = mustBeSaved;
        if (!mustBeSaved) {
            for (Image imageChanged : image) {
                imageChanged.setMustBeSaved(mustBeSaved);
            }
        }
    }

    @Override
    public int compareTo(Chapter t) {
        return new CompareToBuilder().append(getTome(), t.getTome()).append(getNumber(), t.getNumber()).toComparison();
    }

    protected void attributeSerieToChildTomes() {
        for (Image imageToCheck : image) {
            if (imageToCheck.getChapter() == null) {
                imageToCheck.setChapter(this);
            }
        }
    }
}
