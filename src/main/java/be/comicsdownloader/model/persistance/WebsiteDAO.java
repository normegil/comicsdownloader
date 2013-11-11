package be.comicsdownloader.model.persistance;

import be.comicsdownloader.model.AvailableSite;
import be.comicsdownloader.model.pojo.manga.Website;

import java.util.Collection;

public interface WebsiteDAO {

    Collection<Website> getAll();

    Website get(AvailableSite site);

    void insert(Website website);

    void update(Website website);

    void delete(Website website);
}
