package be.comicsdownloader.model.pojo.manga;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Image extends MangaPojo implements Comparable<Image> {

    @XmlTransient
    public enum ImageType {

        PNG("png"),
        JPG("jpg"),
        BMP("bmp"),
        GIF("gif"),
        TIFF("tiff");
        private String typeName;

        private ImageType(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeName() {
            return typeName;
        }
    }

    private static final int HASH_SEED = 17;
    private String name;
    @XmlAttribute
    private int number;
    @XmlTransient
    private Chapter chapter;
    @XmlTransient
    private boolean mustBeSaved = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    @Override
    public boolean getMustBeSaved() {
        return mustBeSaved;
    }

    @Override
    public void setMustBeSaved(boolean mustBeSaved) {
        this.mustBeSaved = mustBeSaved;
    }

    @Override
    public int hashCode() {
        int hash = HASH_SEED;
        hash = HASH_SEED * hash + Objects.hashCode(this.name);
        hash = HASH_SEED * hash + this.number;
        hash = HASH_SEED * hash + this.getChapter().hashCode();
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
        final Image other = (Image) obj;
        if (this.number != other.number) {
            return false;
        }
        if (this.chapter != other.chapter && (this.chapter == null || !this.chapter.equals(other.chapter))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "; Image{" + "name=" + name + ", number=" + number + '}';
    }

    @Override
    public int compareTo(Image t) {
        return getNumber() - t.getNumber();
    }
}
