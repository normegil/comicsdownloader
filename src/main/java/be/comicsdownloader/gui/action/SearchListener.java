package be.comicsdownloader.gui.action;

import be.comicsdownloader.gui.listmodel.SerieListModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchListener implements DocumentListener {

    private JTextField field;
    private SerieListModel serieListModel;

    public SearchListener(JTextField field, SerieListModel serieListModel) {
        this.field = field;
        this.serieListModel = serieListModel;
    }

    @Override
    public void insertUpdate(DocumentEvent de) {
        filterSerie();
    }

    @Override
    public void removeUpdate(DocumentEvent de) {
        filterSerie();
    }

    @Override
    public void changedUpdate(DocumentEvent de) {
        filterSerie();
    }

    private void filterSerie() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    filterSerie();
                }
            });
        } else {
            serieListModel.filter(field.getText());
        }
    }
}
