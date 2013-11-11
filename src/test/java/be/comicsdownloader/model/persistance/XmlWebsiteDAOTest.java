package be.comicsdownloader.model.persistance;

import be.comicsdownloader.generators.TestStrutureCreator;
import be.comicsdownloader.model.AvailableSite;
import be.comicsdownloader.model.pojo.manga.Website;
import be.comicsdownloader.model.service.PropertiesService;
import be.comicsdownloader.model.service.Services;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.*;

public class XmlWebsiteDAOTest {

    private static final String PATH_TO_RESOURCES_FOLDER = "src/test/resources/xml/";
    private XmlWebsiteDAO dao;
    private Website website;
    private Date dummyDate;
    private File sourceFile;
    private File originalSourceFile;
    private File insertFile;
    private File updateFile;
    private File deleteFile;
    private File wrongSourceFile;

    public XmlWebsiteDAOTest() {
        originalSourceFile = new File(PATH_TO_RESOURCES_FOLDER + "OriginalSourceReference.xml");
        insertFile = new File(PATH_TO_RESOURCES_FOLDER + "InsertReference.xml");
        updateFile = new File(PATH_TO_RESOURCES_FOLDER + "UpdateReference.xml");
        deleteFile = new File(PATH_TO_RESOURCES_FOLDER + "EmptyCollectionReference.xml");
        wrongSourceFile = new File(PATH_TO_RESOURCES_FOLDER + "WrongReference.xml");

        try {
            dummyDate = new SimpleDateFormat("yyyy-MM-dd").parse("2013-07-13");
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Before
    public void setUp() {
        try {
            PropertiesService.setInstance(File.createTempFile("DummySettings", ".properties"));
            Services.getPropertyService().setProperty(PropertiesService.PropertyKey.AUTO_REFRESH, false);

            website = TestStrutureCreator.createWebsite(AvailableSite.MANGA_HERE);

            try {
                sourceFile = File.createTempFile("testBase", ".xml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            dao = new XmlWebsiteDAO(sourceFile);
            FileUtils.copyFile(originalSourceFile, sourceFile);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @After
    public void tearDown() {
        PropertiesService.setInstance(null);
    }

    @Test
    public void testInitWithNullSource() {
        try {
            dao = new XmlWebsiteDAO(null);
            fail("Initialize with a null source should send an exception");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testGetAll() {
        Collection<Website> websites = dao.getAll();

        assertNotNull(websites);
        assertFalse(websites.isEmpty());
        assertEquals(1, websites.size());

        assertEquals(website, websites.iterator().next());
    }

    @Test
    public void testGetAllWithWrongSource() {
        dao = new XmlWebsiteDAO(wrongSourceFile);
        try {
            dao.getAll();
            fail("Wrong XML should throw exception when try to parse");
        } catch (Exception ex) { // Cannot compile if catch UnmarshalException (not throwable by the code in try{}
        }
    }

    @Test
    public void testGet() {
        Website loadedWebsite = dao.get(AvailableSite.MANGA_HERE);

        assertNotNull(loadedWebsite);
        assertEquals(website, loadedWebsite);
    }

    @Test
    public void testGetWithWrongSourceFile() {
        dao = new XmlWebsiteDAO(wrongSourceFile);
        try {
            dao.get(AvailableSite.MANGA_HERE);
            fail("Wrong XML shouuld throw exception when try to parse");
        } catch (Exception ex) { // Cannot compile if catch UnmarshalException (not throwable by the code in try{}
        }
    }

    @Test
    public void testInsert() {
        Website newWebsite = new Website();
        newWebsite.setMustBeSaved(true);
        newWebsite.setName("Website2");
        newWebsite.setUrl("http://Website2/");
        newWebsite.setValidityDate(dummyDate);
        newWebsite.setAvailableSite(AvailableSite.MANGA_FOX);

        dao.insert(newWebsite);
        try {
            if (!FileUtils.contentEquals(sourceFile, insertFile)) {
                fail("InsertFile & SourceFile are different [SourceFile = " + sourceFile.getAbsolutePath() + "; InsertFile = " + insertFile.getAbsolutePath() + "]");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Collection<Website> allWebsites = dao.getAll();
        if (!allWebsites.contains(newWebsite)) {
            fail("DAO not updated with the new website");
        }

        Website websiteToTest = dao.get(AvailableSite.MANGA_FOX);
        assertEquals(newWebsite, websiteToTest);
    }

    @Test
    public void testInsertNullWebsite() {
        dao.insert(null);
        try {
            if (!FileUtils.contentEquals(originalSourceFile, sourceFile)) {
                fail("InsertFile & SourceFile are different [SourceFile = " + sourceFile.getAbsolutePath() + "; InsertFile = " + insertFile.getAbsolutePath() + "]");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testInsertIntoWrongSourceFile() {
        Website newWebsite = TestStrutureCreator.createWebsite(AvailableSite.MANGA_FOX);

        dao = new XmlWebsiteDAO(wrongSourceFile);
        try {
            dao.insert(newWebsite);
            fail("Wrong XML shouuld throw exception when try to parse");
        } catch (Exception ex) { // Cannot compile if catch UnmarshalException (not throwable by the code in try{}
        }
    }

    @Test
    public void testUpdate() {
        Website websiteToUpdate = dao.get(AvailableSite.MANGA_HERE);
        websiteToUpdate.setName("Website2");

        dao.update(websiteToUpdate);
        try {
            if (!FileUtils.contentEquals(sourceFile, updateFile)) {
                fail("Update file & SourceFile are different [SourceFile = " + sourceFile.getAbsolutePath() + "; UpdateFile = " + updateFile.getAbsolutePath() + "]");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Collection<Website> allWebsites = dao.getAll();
        if (!allWebsites.contains(websiteToUpdate)) {
            fail("DAO not updated with the new website");
        }

        Website websiteToTest = dao.get(AvailableSite.MANGA_HERE);
        assertEquals(websiteToUpdate, websiteToTest);
    }

    @Test
    public void testUpdateWithNullWebsite() {
        dao.update(null);
        try {
            if (!FileUtils.contentEquals(originalSourceFile, sourceFile)) {
                fail("Update file & SourceFile are different [SourceFile = " + sourceFile.getAbsolutePath() + "; UpdateFile = " + updateFile.getAbsolutePath() + "]");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testDelete() {
        Website websiteToDelete = dao.get(AvailableSite.MANGA_HERE);

        dao.delete(websiteToDelete);
        try {
            if (!FileUtils.contentEquals(sourceFile, deleteFile)) {
                fail("DeleteFile & SourceFile are different");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Collection<Website> allWebsites = dao.getAll();
        if (allWebsites.contains(websiteToDelete)) {
            fail("DAO not updated");
        }

        Website websiteToTest = dao.get(AvailableSite.MANGA_HERE);
        assertNull(websiteToTest);
    }

    @Test
    public void testDeleteNullWebsite() {
        dao.delete(null);

        try {
            if (!FileUtils.contentEquals(sourceFile, originalSourceFile)) {
                fail("Source file & Original Source File are different");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
