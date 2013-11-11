package be.comicsdownloader.gui.action;

import be.comicsdownloader.model.Model;
import be.comicsdownloader.model.pojo.manga.Chapter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RemoveActionListener implements ActionListener {

    private JList<Chapter> chaptersList;
    private Model model;

    public RemoveActionListener(Model model, JList<Chapter> chaptersList) {
        this.chaptersList = chaptersList;
        this.model = model;
    }

    @Override
    public void actionPerformed(final ActionEvent ae) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    actionPerformed(ae);
                }
            });
        } else {
            List<Chapter> selectedValuesList = chaptersList.getSelectedValuesList();
            model.removeAllToDownloadChapter(selectedValuesList);
        }
    }
}
