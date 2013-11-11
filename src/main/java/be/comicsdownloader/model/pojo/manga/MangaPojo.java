package be.comicsdownloader.model.pojo.manga;

import be.comicsdownloader.model.AvailableSite;
import be.comicsdownloader.model.loader.Loader;
import be.comicsdownloader.model.loader.NetLoader;
import be.comicsdownloader.model.parser.Parser.ParsingPart;
import be.comicsdownloader.model.persistance.DateAdapter;
import be.comicsdownloader.model.service.PropertiesService;
import be.comicsdownloader.model.service.Services;
import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class MangaPojo {

    private static final Logger LOG = Logger.getLogger(MangaPojo.class);
    private String url;
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date validityDate;

    public abstract boolean getMustBeSaved();

    public abstract void setMustBeSaved(boolean mustBeSaved);

    public Date getValidityDate() {
        if (validityDate != null) {
            return new Date(validityDate.getTime());
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 0);
            return calendar.getTime();
        }
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = new Date(validityDate.getTime());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    protected List refreshCollection(AvailableSite availableSite, ParsingPart partToParse) {
        LOG.debug("Refreshing a Collection [Parent=" + toString() + "]");
        boolean autoRefreshEnabled = Boolean.parseBoolean(Services.getPropertyService().getProperty(PropertiesService.PropertyKey.AUTO_REFRESH));
        if (autoRefreshEnabled) {
            Loader loader = new NetLoader();
            final Date today = new Date();

            int validityPeriod = Integer.parseInt(Services.getPropertyService().getProperty(PropertiesService.PropertyKey.VALIDITY_PERIOD));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getValidityDate());
            calendar.add(Calendar.DATE, validityPeriod);
            Date outOfValidity = calendar.getTime();

            if (getUrl() != null && (today.after(outOfValidity))) {
                try {
                    setMustBeSaved(true);
                    String htmlDoc = loader.loadDocument(new URL(getUrl()));
                    return availableSite.getParser().parse(partToParse, htmlDoc, this);
                } catch (MalformedURLException ex) {
                    LOG.error("Cannot refresh website", ex);
                }
            }
        }
        return new LinkedList();
    }
}
