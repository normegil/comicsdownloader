package be.comicsdownloader.model.service;

import org.apache.log4j.Logger;

public final class Services {

    private static final Logger LOG = Logger.getLogger(Services.class);

    private Services() {
    }

    public static LoadingService getLoadingService() {
        LOG.trace("Accessing loading service");
        return LoadingService.getInstance();
    }

    public static LibraryService getLibraryService() {
        LOG.trace("Accessing library service");
        return LibraryService.getInstance();
    }

    public static PropertiesService getPropertyService() {
        LOG.trace("Accessing property service");
        return PropertiesService.getInstance();
    }

    public static DownloadService getDownloadServices() {
        LOG.trace("Accessing download service");
        return DownloadService.getInstance();
    }
}
