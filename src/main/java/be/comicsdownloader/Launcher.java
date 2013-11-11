package be.comicsdownloader;

import be.comicsdownloader.gui.SwingView;
import be.comicsdownloader.model.Model;
import be.comicsdownloader.model.ModelImpl;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

public final class Launcher {

    public static final Logger LOG = Logger.getLogger(Launcher.class);

    private Launcher() {
    }

    public static void main(String[] args) {

        final Model model = new ModelImpl();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingView view = new SwingView(model);
                LOG.trace("View instantiated");
                view.setVisible(true);
                LOG.trace("View visible");
            }
        });
    }
}
