package be.comicsdownloader.gui;

import be.comicsdownloader.model.Model.ModelProperties;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StatusDialog implements PropertyChangeListener {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 50;
    private JFrame frame;
    private JProgressBar bar;

    public StatusDialog(int totalChaptersToDownload) {
        frame = new JFrame();
        bar = new JProgressBar(0, totalChaptersToDownload);
        bar.setValue(0);
        bar.setStringPainted(true);
        bar.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.add(bar);
        frame.pack();
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if (ModelProperties.PROGRESS.getPropertyName().equals(pce.getPropertyName())) {
            bar.setValue(bar.getValue() + 1);
        }
    }
}
