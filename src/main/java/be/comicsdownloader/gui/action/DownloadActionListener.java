package be.comicsdownloader.gui.action;

import be.comicsdownloader.gui.StatusDialog;
import be.comicsdownloader.model.Model;
import be.comicsdownloader.model.pojo.manga.Chapter;
import be.comicsdownloader.model.service.Services;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.SortedSet;

public class DownloadActionListener implements ActionListener {

    private Model model;

    public DownloadActionListener(Model model) {
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
            SortedSet<Chapter> toDownloadChapters = model.getToDownloadChapters();
            StatusDialog statusDialog = new StatusDialog(toDownloadChapters.size());
            statusDialog.setVisible(true);

            Services.getDownloadServices().addDownloadChangeListener(statusDialog);

            model.launchDownload();
        }
    }
}
