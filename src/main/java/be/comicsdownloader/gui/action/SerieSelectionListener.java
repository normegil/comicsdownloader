package be.comicsdownloader.gui.action;

import be.comicsdownloader.model.Model;
import be.comicsdownloader.model.pojo.manga.Chapter;
import be.comicsdownloader.model.pojo.manga.Serie;
import be.comicsdownloader.model.pojo.manga.Website;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SerieSelectionListener implements ListSelectionListener {

    private Model model;
    private JList<Website> websitesList;
    private JList<Chapter> loadedChaptersList;

    public SerieSelectionListener(Model model, JList<Website> websitesList, JList<Chapter> loadedChaptersList) {
        this.model = model;
        this.websitesList = websitesList;
        this.loadedChaptersList = loadedChaptersList;
    }

    @Override
    public void valueChanged(final ListSelectionEvent lse) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    valueChanged(lse);
                }
            });
        } else {
            JList list = (JList) lse.getSource();

            model.setSelectedWebsite(null);
            model.clearWebsite();
            websitesList.clearSelection();

            model.clearChapters();
            loadedChaptersList.clearSelection();

            int selectedIndex = list.getSelectedIndex();
            model.setSelectedSerie((Serie) list.getModel().getElementAt(selectedIndex));
            model.getWebsites();
        }
    }
}
