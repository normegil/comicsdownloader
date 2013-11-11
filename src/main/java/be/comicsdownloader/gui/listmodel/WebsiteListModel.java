package be.comicsdownloader.gui.listmodel;

import be.comicsdownloader.model.pojo.manga.Website;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

public class WebsiteListModel extends AbstractListModel<Website> {

    private static final Logger LOG = Logger.getLogger(WebsiteListModel.class);
    private List<Website> websites;

    public WebsiteListModel(SortedSet<Website> websites) {
        this.websites = new LinkedList<>();
        this.websites.addAll(websites);
    }

    @Override
    public int getSize() {
        return websites.size();
    }

    @Override
    public Website getElementAt(int i) {
        return websites.get(i);
    }

    public void clear() {
        LOG.trace("Clear Websites list model");
        websites.clear();
        fireContentsChanged(this, 0, getSize());
    }

    public void addAll(Collection<Website> websites) {
        LOG.trace("Add new websites");
        this.websites.addAll(websites);
        fireContentsChanged(this, 0, getSize());
    }
}
