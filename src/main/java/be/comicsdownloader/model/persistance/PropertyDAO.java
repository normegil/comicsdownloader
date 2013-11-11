package be.comicsdownloader.model.persistance;

import be.comicsdownloader.model.service.PropertiesService.PropertyKey;

import java.util.Properties;

public interface PropertyDAO {

    Properties getAll();

    String get(PropertyKey key);

    void insert(PropertyKey key, Object value);

    void update(PropertyKey key, Object value);

    void delete(PropertyKey key);
}
