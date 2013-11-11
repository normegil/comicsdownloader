package be.comicsdownloader.model.pojo.manga;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

@XmlRootElement(name = "Websites")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebsiteCollection implements Iterable<Website> {

    public WebsiteCollection() {
        website = new TreeSet<Website>();
    }

    @XmlElementWrapper(name = "websites")
    private Set<Website> website;

    public Set<Website> getCollection() {
        return website;
    }

    public void setCollection(Set<Website> collection) {
        this.website = collection;
    }

    public Iterator<Website> iterator() {
        return website.iterator();
    }
}
