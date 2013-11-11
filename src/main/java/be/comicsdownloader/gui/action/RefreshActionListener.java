package be.comicsdownloader.gui.action;

import be.comicsdownloader.model.Model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RefreshActionListener implements ActionListener {

    private Model model;

    public RefreshActionListener(Model model) {
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
            model.refreshData();
        }
    }
}
