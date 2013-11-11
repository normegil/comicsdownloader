package be.comicsdownloader.model;

import be.comicsdownloader.model.persistance.PropertyDAO;
import be.comicsdownloader.model.persistance.PropertyDAOImpl;
import be.comicsdownloader.model.service.PropertiesService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PropertiesServiceTest {

    private final Properties referenceDefaultProperties;
    private PropertyDAO dao;
    private PropertiesService service;
    private File sourceFile;

    public PropertiesServiceTest() {
        referenceDefaultProperties = new Properties();
        for (PropertiesService.PropertyKey propertyKey : PropertiesService.PropertyKey.values()) {
            referenceDefaultProperties.put(propertyKey.toString(), propertyKey.getDefaultValue());
        }
    }

    @Before
    public void setUp() {
        try {
            sourceFile = File.createTempFile("FakeProperties", ".csv");
            service = PropertiesService.setInstance(sourceFile);
            dao = new PropertyDAOImpl(sourceFile);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @After
    public void tearDown() {
        PropertiesService.setInstance(null);
        dao = null;
        service = null;
    }

    @Test
    public void testGetProperty() {
        String autoRefresh = service.getProperty(PropertiesService.PropertyKey.AUTO_REFRESH);
        assertEquals("true", autoRefresh);
    }

    @Test
    public void testGetPropertyAfterChange() {
        service.setProperty(PropertiesService.PropertyKey.AUTO_REFRESH, false);
        String changedProperty = service.getProperty(PropertiesService.PropertyKey.AUTO_REFRESH);

        assertEquals("false", changedProperty);
    }

    @Test
    public void testGetPropertyWithNullKey() {
        service.getProperty(null);
    }

    @Test
    public void testSetProperty() {
        service.setProperty(PropertiesService.PropertyKey.AUTO_REFRESH, false);
        String changedProperty = service.getProperty(PropertiesService.PropertyKey.AUTO_REFRESH);

        assertEquals("false", changedProperty);
    }

    @Test
    public void testSetPropertyToNull() {
        service.setProperty(PropertiesService.PropertyKey.AUTO_REFRESH, null);
        String nullProperty = service.getProperty(PropertiesService.PropertyKey.AUTO_REFRESH);

        assertEquals(PropertiesService.PropertyKey.AUTO_REFRESH.getDefaultValue(), nullProperty);
    }

    @Test
    public void testSetPropertyWithNullKey() {
        try {
            service.setProperty(null, this);
            fail("Changing a property with a null key should fail");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testLoadDefaultSettings() {
        Properties defaultSettings = service.loadDefaultSettings();
        compareProperties(referenceDefaultProperties, defaultSettings);
    }

    @Test
    public void testLoadSettings() {
        Properties defaultSettings = service.loadDefaultSettings();
        service.setProperty(PropertiesService.PropertyKey.AUTO_REFRESH, false);
        Properties properties = service.loadSettings();

        defaultSettings.setProperty(PropertiesService.PropertyKey.AUTO_REFRESH.toString(), "false");
        compareProperties(defaultSettings, properties);
    }

    @Test
    public void testSaveEmptyProperties() {
        service.saveSettings(new Properties());
        Properties emptyProperties = dao.getAll();
        compareProperties(referenceDefaultProperties, emptyProperties);
    }

    @Test
    public void testSaveSettings() {
        Properties properties = service.loadSettings();
        properties.setProperty(PropertiesService.PropertyKey.AUTO_REFRESH.toString(), "false");
        service.saveSettings(properties);

        Properties loadedProperties = dao.getAll();
        compareProperties(properties, loadedProperties);
        properties.setProperty(PropertiesService.PropertyKey.VALIDITY_PERIOD.toString(), "3");
        service.saveSettings(properties);
        Properties cacheUpdatedPoperties = service.loadSettings();

        compareProperties(properties, cacheUpdatedPoperties);
    }

    @Test
    public void testSaveNull() {
        service.saveSettings(null);
    }

    private void compareProperties(Properties reference, Properties toTest) {
        for (Map.Entry<Object, Object> entry : reference.entrySet()) {
            assertEquals(entry.getValue(), toTest.getProperty(entry.getKey().toString()));
        }
    }
}
