package be.comicsdownloader.gui;

import be.comicsdownloader.gui.action.*;
import be.comicsdownloader.gui.listmodel.ChapterListModel;
import be.comicsdownloader.gui.listmodel.SerieListModel;
import be.comicsdownloader.gui.listmodel.WebsiteListModel;
import be.comicsdownloader.model.Model;
import be.comicsdownloader.model.Model.ModelProperties;
import be.comicsdownloader.model.pojo.manga.Chapter;
import be.comicsdownloader.model.pojo.manga.Serie;
import be.comicsdownloader.model.pojo.manga.Website;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

public class MangaTab implements PropertyChangeListener {

    private static final Logger LOG = Logger.getLogger(MangaTab.class);
    private static final int LIST_WIDTH = 300;
    private static final int LIST_CHAPTER_HEIGHT = 400;
    private static final int LIST_SERIES_HEIGHT = 300;
    private static final int LIST_WEBSITE_HEIGHT = 100;
    private static final int BUTTON_WIDTH = 55;
    private static final int BUTTON_HEIGHT = 55;
    private static final int TEXTFIELD_WIDTH = 80;
    private JPanel rootPanel;
    private Model model;
    private JList<Serie> seriesList;
    private JList<Website> websitesList;
    private JList<Chapter> chapters;
    private JList<Chapter> chaptersToDownload;

    public MangaTab(Model model) {
        LOG.trace("[BEGIN] Initializing Gui");
        this.model = model;

        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());

        ChapterListModel chapterListModel = new ChapterListModel(model.getLoadedChapters());
        ChapterListModel chapterToDownloadListModel = new ChapterListModel(model.getToDownloadChapters());

        chapters = new JList<>(chapterListModel);
        chaptersToDownload = new JList<>(chapterToDownloadListModel);

        JScrollPane chapterScrollPanel = new JScrollPane(chapters);
        JScrollPane chaptersToDownloadScrollPanel = new JScrollPane(chaptersToDownload);
        chapterScrollPanel.setPreferredSize(new Dimension(LIST_WIDTH, LIST_CHAPTER_HEIGHT));
        chaptersToDownloadScrollPanel.setPreferredSize(new Dimension(LIST_WIDTH, LIST_CHAPTER_HEIGHT));

        JPanel listsMainPanel = new JPanel();
        listsMainPanel.setLayout(new MigLayout());
        listsMainPanel.add(getSeriesAndWebsitePanel());
        listsMainPanel.add(chapterScrollPanel);
        listsMainPanel.add(getListButtonPanel());
        listsMainPanel.add(chaptersToDownloadScrollPanel);

        rootPanel.add(getMenuPanel(), BorderLayout.NORTH);
        rootPanel.add(listsMainPanel, BorderLayout.CENTER);
        rootPanel.add(getFooterPanel(), BorderLayout.SOUTH);

        LOG.trace("[END] Initializing Gui");
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private JPanel getMenuPanel() {
        JTextField searchField = new JTextField(TEXTFIELD_WIDTH);

        JPanel menuPanel = new JPanel();
        menuPanel.add(searchField);
        searchField.getDocument().addDocumentListener(new SearchListener(searchField, (SerieListModel) seriesList.getModel()));

        return menuPanel;
    }

    private JPanel getFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new MigLayout("fillx"));

        JButton refreshButton = new JButton("Refresh");
        footerPanel.add(refreshButton, "center");
        refreshButton.addActionListener(new RefreshActionListener(model));

        JButton downloadButton = new JButton("Download");
        footerPanel.add(downloadButton, "center");
        downloadButton.addActionListener(new DownloadActionListener(model));

        return footerPanel;
    }

    private JComponent getSeriesAndWebsitePanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        SerieListModel serieListModel = new SerieListModel(model.getSeries());
        WebsiteListModel websiteListModel = new WebsiteListModel(model.getWebsites());

        seriesList = new JList<>(serieListModel);
        seriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        websitesList = new JList<>(websiteListModel);
        websitesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        seriesList.addListSelectionListener(new SerieSelectionListener(model, websitesList, chapters));
        websitesList.addListSelectionListener(new WebsiteSelectionListener(model));

        JScrollPane websiteScrollPane = new JScrollPane(websitesList);
        JScrollPane serieScrollPane = new JScrollPane(seriesList);
        serieScrollPane.setPreferredSize(new Dimension(LIST_WIDTH, LIST_SERIES_HEIGHT));
        websiteScrollPane.setPreferredSize(new Dimension(LIST_WIDTH, LIST_WEBSITE_HEIGHT));

        jPanel.add(serieScrollPane);
        jPanel.add(websiteScrollPane);

        return jPanel;

    }

    private JPanel getListButtonPanel() {
        JPanel listButtonPanel = new JPanel();
        listButtonPanel.setLayout(new MigLayout());

        final JButton addAllButton = new JButton(">>");
        final JButton addButton = new JButton(">");
        final JButton removeButton = new JButton("<");
        final JButton removeAllButton = new JButton("<<");

        addAllButton.addActionListener(new AddAllActionListener(model, chapters));
        addButton.addActionListener(new AddActionListener(model, chapters));
        removeButton.addActionListener(new RemoveActionListener(model, chaptersToDownload));
        removeAllButton.addActionListener(new RemoveAllActionListener(model, chaptersToDownload));

        Dimension dimension = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

        addAllButton.setPreferredSize(dimension);
        addButton.setPreferredSize(dimension);
        removeButton.setPreferredSize(dimension);
        removeAllButton.setPreferredSize(dimension);
        final String migLayoutPositionning = "wrap, center";

        listButtonPanel.add(addAllButton, migLayoutPositionning);
        listButtonPanel.add(addButton, migLayoutPositionning);
        listButtonPanel.add(removeButton, migLayoutPositionning);
        listButtonPanel.add(removeAllButton, migLayoutPositionning);

        return listButtonPanel;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent pce) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    propertyChange(pce);
                }
            });
        } else {
            LOG.trace("[BEGIN] PropertyChangeListener - (PropertyName=" + pce.getPropertyName() + ";Old Value=" + pce.getOldValue() + ";New value=" + pce.getNewValue() + ")");
            if (ModelProperties.SERIES.getPropertyName().equals(pce.getPropertyName())) {
                LOG.trace("Refreshing series");
                SerieListModel listModel = (SerieListModel) seriesList.getModel();
                listModel.clear();
                listModel.addAll((Collection<Serie>) pce.getNewValue());
            } else if (ModelProperties.WEBSITES.getPropertyName().equals(pce.getPropertyName())) {
                LOG.trace("Refreshing websites");
                WebsiteListModel listModel = (WebsiteListModel) websitesList.getModel();
                listModel.clear();
                listModel.addAll((Collection<Website>) pce.getNewValue());
            } else if (ModelProperties.CHAPTER_LOADED.getPropertyName().equals(pce.getPropertyName())) {
                LOG.trace("Refreshing chapters");
                ChapterListModel listModel = (ChapterListModel) chapters.getModel();
                listModel.clear();
                listModel.addAll((Collection<Chapter>) pce.getNewValue());
            } else if (ModelProperties.CHAPTER_TO_DOWNLOAD.getPropertyName().equals(pce.getPropertyName())) {
                LOG.trace("Refreshing chapters to download");
                ChapterListModel listModel = (ChapterListModel) chaptersToDownload.getModel();
                listModel.clear();
                listModel.addAll((Collection<Chapter>) pce.getNewValue());
            }
            LOG.trace("[END] PropertyChangeListener - (PropertyName=" + pce.getPropertyName() + ";Old Value=" + pce.getOldValue() + ";New value=" + pce.getNewValue() + ")");
        }
    }
}
