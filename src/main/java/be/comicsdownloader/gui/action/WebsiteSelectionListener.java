package be.comicsdownloader.gui.action;

import be.comicsdownloader.model.Model;
import be.comicsdownloader.model.pojo.manga.Website;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class WebsiteSelectionListener implements ListSelectionListener {

    private Model model;

    public WebsiteSelectionListener(Model model) {
        this.model = model;
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
            int selectedIndex = list.getSelectedIndex();
            model.setSelectedWebsite((Website) list.getModel().getElementAt(selectedIndex));
            model.getLoadedChapters();
        }
    }
}
