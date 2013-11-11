package be.comicsdownloader.gui.listmodel;

import be.comicsdownloader.model.pojo.manga.Chapter;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ChapterListModel extends AbstractListModel<Chapter> {

    private static final Logger LOG = Logger.getLogger(ChapterListModel.class);
    private List<Chapter> chapters;

    public ChapterListModel(Collection<Chapter> chapters) {
        this.chapters = new LinkedList<>();
        this.chapters.addAll(chapters);
    }

    @Override
    public int getSize() {
        return chapters.size();
    }

    @Override
    public Chapter getElementAt(int i) {
        return chapters.get(i);
    }

    public void clear() {
        LOG.trace("Clear chapters list model");
        chapters.clear();
        fireContentsChanged(this, 0, getSize());
    }

    public void addAll(Collection<Chapter> chapters) {
        LOG.trace("Add new chapters");
        this.chapters.addAll(chapters);
        fireContentsChanged(this, 0, getSize());
    }

    public List<Chapter> getAll() {
        return chapters;
    }
}
