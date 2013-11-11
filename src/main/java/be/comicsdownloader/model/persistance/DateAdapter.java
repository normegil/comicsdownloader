package be.comicsdownloader.model.persistance;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter extends XmlAdapter<String, Date> {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date unmarshal(final String vt) throws ParseException {
        Date parsed;
        synchronized (FORMAT) {
            parsed = FORMAT.parse(vt);
        }
        return parsed;
    }

    @Override
    public String marshal(final Date bt) {
        String parsed;
        synchronized (FORMAT) {
            parsed = FORMAT.format(bt);
        }
        return parsed;
    }
}
