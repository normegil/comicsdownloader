package be.comicsdownloader.model.persistance;

import be.comicsdownloader.generators.TestStrutureCreator;
import be.comicsdownloader.model.service.PropertiesService;
import be.comicsdownloader.model.service.Services;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

public class PropertyDAOImplTest {

    private static final String PATH_TO_RESOURCES_FOLDER = "src/test/resources/be/comicsdownloader/model/persistance/properties/";
    private PropertyDAOImpl dao;
    private File originalSourceFile;
    private File insertFile;
    private File updateFile;
    private File deleteFile;
    private File sourceFile;
    private Properties properties;

    public PropertyDAOImplTest() {
        originalSourceFile = new File(PATH_TO_RESOURCES_FOLDER + "OriginalSourceReference.properties");
        insertFile = new File(PATH_TO_RESOURCES_FOLDER + "InsertReference.properties");
        updateFile = new File(PATH_TO_RESOURCES_FOLDER + "UpdateReference.properties");
        deleteFile = new File(PATH_TO_RESOURCES_FOLDER + "DeleteReference.properties");
    }

    @Before
    public void setUp() throws IOException {
        PropertiesService.setInstance(File.createTempFile("DummySettings", ".properties"));
        Services.getPropertyService().setProperty(PropertiesService.PropertyKey.AUTO_REFRESH, false);
        sourceFile = File.createTempFile("Properties", ".properties");
        FileUtils.copyFile(originalSourceFile, sourceFile);
        dao = new PropertyDAOImpl(sourceFile);
        properties = TestStrutureCreator.createProperties();
    }

    @After
    public void tearDown() {
        PropertiesService.setInstance(null);
    }

    @Test
    public void testGetAll() {
        Properties loadedProperties = dao.getAll();

        assertNotNull(loadedProperties);
        assertFalse(loadedProperties.isEmpty());

        for (Object keyObject : properties.keySet()) {
            String key = (String) keyObject;
            if (!properties.getProperty(key).equals(loadedProperties.getProperty(key))) {
                fail("Property differs : Key = " + key + "; Original Property = " + properties.getProperty(key) + "; Loaded Property = " + loadedProperties.getProperty(key));
            }
        }
    }

    @Test
    public void testInitWithNullSource() {
        try {
            dao = new PropertyDAOImpl(null);
            fail("Initialize with a null source should send an exception");
        } catch (IllegalArgumentException ex) {
        }

    }

    @Test
    public void testGet() {
        String value = dao.get(PropertiesService.PropertyKey.AUTO_REFRESH);

        assertNotNull(value);
        if (!value.equals(properties.getProperty(PropertiesService.PropertyKey.AUTO_REFRESH.toString()))) {
            fail("Property differs : Key = " + PropertiesService.PropertyKey.AUTO_REFRESH.toString() + "; Original Property = " + properties.getProperty(PropertiesService.PropertyKey.AUTO_REFRESH.toString()) + "; Loaded Property = " + value);
        }
    }

    @Test
    public void testInsert() {
        dao.insert(PropertiesService.PropertyKey.AUTO_REFRESH, false);
        comparePropertiesFile(insertFile, sourceFile);
    }

    public void testInsertNullProperties() {
        dao.insert(null, true);
        comparePropertiesFile(insertFile, sourceFile);

        dao.insert(PropertiesService.PropertyKey.AUTO_REFRESH, null);
        comparePropertiesFile(insertFile, sourceFile);
    }

    @Test
    public void testUpdate() {
        dao.update(PropertiesService.PropertyKey.AUTO_REFRESH, false);

        comparePropertiesFile(updateFile, sourceFile);
    }

    @Test
    public void testUpdateWithNullProperties() {
        dao.update(null, true);
        comparePropertiesFile(originalSourceFile, sourceFile);

        dao.update(PropertiesService.PropertyKey.AUTO_REFRESH, null);
        comparePropertiesFile(originalSourceFile, sourceFile);
    }

    @Test
    public void testDelete() {
        dao.delete(PropertiesService.PropertyKey.AUTO_REFRESH);
        comparePropertiesFile(deleteFile, sourceFile);
    }

    @Test
    public void testDeleteWithNullObject() {
        dao.delete(null);
        comparePropertiesFile(deleteFile, sourceFile);
    }

    private void comparePropertiesFile(File reference, File toTest) {
        Properties referenceProperties = new PropertyDAOImpl(reference).getAll();
        Properties testProperties = new PropertyDAOImpl(toTest).getAll();

        assertEquals(referenceProperties.size(), testProperties.size());

        for (Map.Entry<Object, Object> entry : referenceProperties.entrySet()) {
            assertEquals(entry.getValue(), testProperties.get(entry.getKey()));
        }
    }
}
