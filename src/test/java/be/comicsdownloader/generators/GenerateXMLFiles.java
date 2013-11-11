package be.comicsdownloader.generators;

import be.comicsdownloader.model.AvailableSite;
import be.comicsdownloader.model.pojo.manga.Website;
import be.comicsdownloader.model.pojo.manga.WebsiteCollection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.File;

public class GenerateXMLFiles {

    private static final String PATH_TO_RESOURCES_FOLDER = "src/test/resources/xml/";

    public static void main(String args[]) {
        try {
            Website website = TestStrutureCreator.createWebsite(AvailableSite.MANGA_HERE);

            JAXBContext context = JAXBContext.newInstance(WebsiteCollection.class);

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            File originalSourceFile = new File(PATH_TO_RESOURCES_FOLDER + "OriginalSourceReference.xml");
            WebsiteCollection websiteCollection = new WebsiteCollection();
            websiteCollection.getCollection().add(website);
            marshaller.marshal(websiteCollection, originalSourceFile);

            Website newWebsite = new Website();
            newWebsite.setMustBeSaved(true);
            newWebsite.setName("Website2");
            newWebsite.setUrl("http://Website2/");
            newWebsite.setValidityDate(TestStrutureCreator.createDate());
            newWebsite.setAvailableSite(AvailableSite.MANGA_FOX);
            websiteCollection.getCollection().add(newWebsite);

            File insertFile = new File(PATH_TO_RESOURCES_FOLDER + "InsertReference.xml");
            marshaller.marshal(websiteCollection, insertFile);
            websiteCollection.getCollection().remove(newWebsite);

            website.setName("Website2");
            File updateFile = new File(PATH_TO_RESOURCES_FOLDER + "UpdateReference.xml");
            marshaller.marshal(websiteCollection, updateFile);

            websiteCollection.getCollection().remove(website);
            File deleteFile = new File(PATH_TO_RESOURCES_FOLDER + "EmptyCollectionReference.xml");
            marshaller.marshal(websiteCollection, deleteFile);

            WebsiteCollection collection = new WebsiteCollection();
            collection.getCollection().add(TestStrutureCreator.createWebsite(AvailableSite.MANGA_HERE));
            collection.getCollection().add(TestStrutureCreator.createWebsite(AvailableSite.MANGA_FOX));
            File persistCollectionFile = new File(PATH_TO_RESOURCES_FOLDER + "PersistCollectionReference.xml");
            marshaller.marshal(collection, persistCollectionFile);

            File persistFile = new File(PATH_TO_RESOURCES_FOLDER + "PersistReference.xml");
            marshaller.marshal(collection, persistFile);

        } catch (PropertyException ex) {
            throw new RuntimeException(ex);
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }
}
