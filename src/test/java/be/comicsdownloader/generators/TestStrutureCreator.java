package be.comicsdownloader.generators;

import be.comicsdownloader.model.AvailableSite;
import be.comicsdownloader.model.pojo.manga.*;
import be.comicsdownloader.model.service.PropertiesService;

import java.io.File;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class TestStrutureCreator {

    private static final String ROOT_PATH_TO_DUMMY_DATA = "src/test/resources/images/";

    public static Date createDate() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse("2013-07-13");
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Website createWebsite(AvailableSite site) {
        try {
            final long id = site.getId();

            Website website = new Website();
            website.setMustBeSaved(true);
            website.setName("Website" + id);
            website.setUrl("http://FakeWebsite" + id + "/");
            website.setValidityDate(createDate());
            website.setAvailableSite(site);

            Serie serie = new Serie();
            serie.setMustBeSaved(true);
            serie.setName("DummySerie");
            serie.setWebsite(website);
            serie.setValidityDate(createDate());
            serie.setUrl("http://FakeWebsite" + id + "/FakeDummySerie");

            Serie serie1 = new Serie();
            serie1.setMustBeSaved(true);
            serie1.setName("DummySerie1");
            serie1.setWebsite(website);
            serie1.setValidityDate(createDate());
            serie1.setUrl("http://FakeWebsite" + id + "/FakeDummySerie1");

            website.addSerie(serie);
            website.addSerie(serie1);

            Tome tome = new Tome();
            tome.setMustBeSaved(true);
            tome.setName("DummyTome");
            tome.setNumber(0);
            tome.setSerie(serie);
            tome.setUrl("http://FakeWebsite" + id + "/FakeDummySerie/Tome");
            tome.setValidityDate(createDate());

            serie.addTome(tome);

            Chapter chapter = new Chapter();
            chapter.setMustBeSaved(true);
            chapter.setName("chapter");
            chapter.setTome(tome);
            chapter.setUrl("http://FakeWebsite" + id + "/FakeDummySerie/Tome");
            chapter.setNumber(1);
            chapter.setValidityDate(createDate());

            tome.addChapter(chapter);

            Image image = new Image();
            image.setChapter(chapter);
            image.setMustBeSaved(true);
            image.setName("Image1");
            image.setNumber(1);
            image.setUrl(new File(ROOT_PATH_TO_DUMMY_DATA + "image.jpg").toURI().toURL().toString());
            image.setValidityDate(createDate());

            chapter.addImage(image);

            return website;
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Properties createProperties() {
        Properties properties = new Properties();
        for (PropertiesService.PropertyKey key : PropertiesService.PropertyKey.values()) {
            properties.put(key.toString(), key.getDefaultValue());
        }

        return properties;
    }
}
