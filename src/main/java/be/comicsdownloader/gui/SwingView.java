package be.comicsdownloader.gui;

import be.comicsdownloader.model.Model;

import javax.swing.*;
import java.awt.*;

public class SwingView {

    private JFrame frame = new JFrame();
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 530;

    public SwingView(Model model) {
        frame.getContentPane().setLayout(new BorderLayout());
        JTabbedPane jTabbedPane = new JTabbedPane();
        frame.getContentPane().add(jTabbedPane, BorderLayout.CENTER);

        Dimension dimension = new Dimension(WIDTH, HEIGHT);
        frame.setMinimumSize(dimension);
        frame.setPreferredSize(dimension);
        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        final MangaTab mangaTab = new MangaTab(model);
        model.addPropertyChangeListener(mangaTab);
        jTabbedPane.addTab("Manga", mangaTab.getRootPanel());
        jTabbedPane.addTab("Settings", new SettingsTab().getRootPanel());

        frame.pack();
    }

    public void setVisible(boolean isVisible) {
        frame.setVisible(isVisible);
    }
}
