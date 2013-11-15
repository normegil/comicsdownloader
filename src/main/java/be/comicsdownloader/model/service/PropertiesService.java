package be.comicsdownloader.model.service;

import be.comicsdownloader.model.library.LibraryStructure;
import be.comicsdownloader.model.persistance.PropertyDAOImpl;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Map;
import java.util.Observable;
import java.util.Properties;

public final class PropertiesService extends Observable {

    private static final Logger LOG = Logger.getLogger(PropertiesService.class);
    private static volatile PropertiesService instance;
    private PropertyDAOImpl dao;

    protected static PropertiesService getInstance() {
        if (instance == null) {
            synchronized (PropertiesService.class) {
                if (instance == null) {
                    instance = new PropertiesService(new File("settings.csv"));
                }
            }
        }
        return instance;
    }

    public static PropertiesService setInstance(File settingsFile) {
        LOG.warn("This constructor should only be used in test environment !");
        synchronized (PropertiesService.class) {
            if (settingsFile != null) {
                instance = new PropertiesService(settingsFile);
            } else {
                instance = null;
            }
        }
        return instance;
    }
    private Properties defaultProperties;

    private PropertiesService(File settingsFile) {
        defaultProperties = new Properties();
        for (PropertyKey propertyKey : PropertyKey.values()) {
            defaultProperties.put(propertyKey.toString(), propertyKey.getDefaultValue());
        }
        dao = new PropertyDAOImpl(settingsFile);
    }

    public void saveSettings(Properties propertiesToSave) {
        LOG.debug("Save Settings");
        if (propertiesToSave != null) {
            Properties toSave = new Properties();
            Properties actualProperties = loadSettings();
            for (PropertyKey propertyKey : PropertyKey.values()) {
                String property = propertiesToSave.getProperty(propertyKey.toString());
                toSave.put(propertyKey.toString(), property != null ? property : actualProperties.get(propertyKey.toString()));
            }

            for (Map.Entry<Object, Object> entry : toSave.entrySet()) {
                String key = (String) entry.getKey();
                dao.update(PropertiesService.PropertyKey.getEnum(key), entry.getValue());
            }
        }
    }

    public Properties loadSettings() {
        LOG.trace("Load Settings");
        Properties toReturnProperties = new Properties();
        Properties properties = dao.getAll();
        for (PropertyKey propertyKey : PropertyKey.values()) {
            Object property = properties.get(propertyKey.toString());
            toReturnProperties.put(propertyKey.toString(), property != null ? property : defaultProperties.get(propertyKey.toString()));
        }
        notifyObservers(toReturnProperties);
        return toReturnProperties;
    }

    public Properties loadDefaultSettings() {
        LOG.debug("Load default Settings");
        Properties clonedProperties = (Properties) defaultProperties.clone();
        notifyObservers(clonedProperties);
        return clonedProperties;
    }

    public String getProperty(PropertyKey key) {
        LOG.trace("Get Property : " + key);
        if (key != null) {
            return loadSettings().getProperty(key.toString());
        }
        return null;
    }

    public void setProperty(PropertyKey key, Object value) {
        LOG.trace("Set Property : " + key + ";" + value);
        if (key != null) {
            Properties properties = loadSettings();
            properties.setProperty(key.toString(), value != null ? value.toString() : key.getDefaultValue());
            saveSettings(properties);
        } else {
            throw new IllegalArgumentException("The key to property is null");
        }
    }

    public enum PropertyKey {

        AUTO_REFRESH("autorefresh.enable", "true"),
        VALIDITY_PERIOD("autorefresh.period", "7"),
        DOWNLOAD_THREAD_NUMBER("download.nbThread", "4"),
        LIBRARY_FOLDER_STRUCTURE("library.structure", LibraryStructure.FULL_SPLITTED.getName()),
        LIBRARY_FOLDER("library.folder", "library/"),
        DATA_FILE("data.file", "data.xml"),
        IMAGE_FORMAT_TYPE("image.format.type", "PNG");
        private String key;
        private String defaultValue;

        private PropertyKey(String key, String defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        @Override
        public String toString() {
            return key;
        }

        public static PropertyKey getEnum(String keyToFind) {
            for (PropertyKey key : values()) {
                if (key.toString().equals(keyToFind)) {
                    return key;
                }
            }
            throw new IllegalArgumentException("Unknown option key");
        }
    }
}
