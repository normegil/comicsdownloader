package be.comicsdownloader.model.persistance;

import be.comicsdownloader.model.CrashException;
import be.comicsdownloader.model.service.PropertiesService.PropertyKey;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;

public class PropertyDAOImpl implements PropertyDAO {

    private static final Logger LOG = Logger.getLogger(PropertyDAOImpl.class);
    private final File settingsFile;

    public PropertyDAOImpl(final File settingsFile) {
        if (settingsFile == null) {
            throw new IllegalArgumentException("The source of the dao cannot be null");
        }
        this.settingsFile = settingsFile;
    }

    @Override
    public Properties getAll() {
        InputStreamReader reader = null;
        try {
            Properties properties = new Properties();
            if (settingsFile != null && settingsFile.exists()) {
                LOG.trace("Loading all properties");
                FileInputStream stream = new FileInputStream(settingsFile);
                reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
                properties.load(reader);
            }
            return properties;
        } catch (IOException ex) {
            throw new CrashException(ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    throw new CrashException("Problem closing the settings file output stream (Should not happen)", ex);
                }
            }
        }
    }

    @Override
    public String get(final PropertyKey key) {
        LOG.trace("Get Property [Key=" + key + "]");
        Properties properties = getAll();
        return properties.getProperty(key.toString());
    }

    @Override
    public void insert(final PropertyKey key, final Object value) {
        OutputStreamWriter writer = null;
        try {
            if (key != null && value != null) {
                LOG.trace("Insert [Key=" + key + "; Value=" + value + "]");
                Properties properties = getAll();
                properties.setProperty(key.toString(), value.toString());
                FileOutputStream stream = new FileOutputStream(settingsFile);
                writer = new OutputStreamWriter(stream, Charset.forName("UTF-8"));
                properties.store(writer, null);
            }
        } catch (IOException ex) {
            throw new CrashException("Settings file doesn't exist", ex);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    throw new CrashException("Problem closing the settings file input stream (Should not happen)", ex);
                }
            }
        }
    }

    @Override
    public void update(final PropertyKey key, final Object value) {
        insert(key, value);
    }

    @Override
    public void delete(final PropertyKey key) {
        if (key != null) {
            insert(key, key.getDefaultValue());
        }
    }
}
