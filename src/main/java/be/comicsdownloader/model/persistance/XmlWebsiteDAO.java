package be.comicsdownloader.model.persistance;

import be.comicsdownloader.model.AvailableSite;
import be.comicsdownloader.model.CrashException;
import be.comicsdownloader.model.pojo.manga.Website;
import be.comicsdownloader.model.pojo.manga.WebsiteCollection;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Collection;

public class XmlWebsiteDAO implements WebsiteDAO {

    private static final Logger LOG = Logger.getLogger(XmlWebsiteDAO.class);
    private final File source;
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;
    private WebsiteCollection websites;

    public XmlWebsiteDAO(final File source) {

        if (source == null) {
            throw new IllegalArgumentException("The source of the dao cannot be null");
        }

        try {
            this.source = source;

            JAXBContext context = JAXBContext.newInstance(WebsiteCollection.class);

            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            unmarshaller = context.createUnmarshaller();

            if (source.length() == 0) {
                createBasicStructure();
            }

        } catch (JAXBException ex) {
            throw new CrashException(ex);
        }
    }

    @Override
    public Collection<Website> getAll() {
        if (websites == null || websites.getCollection().isEmpty()) {
            try {
                LOG.trace("Load all Websites");
                websites = (WebsiteCollection) unmarshaller.unmarshal(source);
            } catch (JAXBException ex) {
                throw new CrashException(ex);
            }
        }
        return websites.getCollection();
    }

    @Override
    public Website get(final AvailableSite site) {
        getAll();
        LOG.trace("Get [AvailableSite=" + site + "]");
        for (Website website : websites) {
            if (website.getAvailableSite().equals(site)) {
                return website;
            }
        }
        return null;
    }

    @Override
    public void insert(final Website website) {
        getAll();
        if (website != null) {
            LOG.trace("Insert [Website=" + website + "]");
            websites.getCollection().add(website);
            save(websites);
        }
    }

    @Override
    public void update(final Website website) {
        if (website != null) {
            LOG.trace("Update [Website=" + website + "]");
            getAll();
            for (Website loadedWebsite : websites) {
                if (loadedWebsite.getAvailableSite().equals(website.getAvailableSite())) {
                    websites.getCollection().remove(loadedWebsite);
                    break;
                }
            }

            websites.getCollection().add(website);
            save(websites);
            website.setMustBeSaved(false);
        }
    }

    @Override
    public void delete(final Website website) {
        if (website != null) {
            LOG.trace("Delete [Website=" + website + "]");
            getAll();
            websites.getCollection().remove(website);
            save(websites);
        }
    }

    private void save(final WebsiteCollection websiteCollection) {
        try {
            LOG.debug("Saving a new data file");
            FileUtils.deleteQuietly(source);
            marshaller.marshal(websiteCollection, source);
        } catch (JAXBException ex) {
            throw new CrashException(ex);
        }
    }

    private void createBasicStructure() {
        websites = new WebsiteCollection();
        save(websites);
    }
}
