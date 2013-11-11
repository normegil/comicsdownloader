package be.comicsdownloader.gui.listmodel;

import be.comicsdownloader.model.pojo.manga.Serie;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

public class SerieListModel extends AbstractListModel<Serie> {

    private static final Logger LOG = Logger.getLogger(SerieListModel.class);
    private List<Serie> series;
    private List<Serie> filteredSeries;

    public SerieListModel(SortedSet<Serie> series) {
        this.series = new LinkedList<>();
        filteredSeries = new LinkedList<>();

        this.series.addAll(series);
        this.filteredSeries.addAll(this.series);
    }

    @Override
    public int getSize() {
        return filteredSeries.size();
    }

    @Override
    public Serie getElementAt(int i) {
        return filteredSeries.get(i);
    }

    public void filter(String filter) {
        LOG.trace("Filter list model (Filter=" + filter + ")");
        filteredSeries.clear();
        for (Serie serie : series) {
            if (serie.getName().contains(filter)) {
                filteredSeries.add(serie);
            }
        }
        fireContentsChanged(this, 0, getSize());
    }

    public void clear() {
        LOG.trace("Clear series list model");
        series = new LinkedList<>();
        filteredSeries = new LinkedList<>();
        fireContentsChanged(this, 0, getSize());
    }

    public void addAll(Collection<Serie> series) {
        LOG.trace("Add new series");
        series.addAll(series);
        filteredSeries.addAll(series);
        fireContentsChanged(this, 0, getSize());
    }
}
