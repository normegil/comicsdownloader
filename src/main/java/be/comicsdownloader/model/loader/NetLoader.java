package be.comicsdownloader.model.loader;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class NetLoader implements Loader {

    private static final Logger LOG = Logger.getLogger(NetLoader.class);

    @SuppressWarnings("ConvertToTryWithResources")
    @Override
    public String loadDocument(final URL url) {
        LOG.info("Load document [URL=" + url + "]");

        if (url == null) {
            throw new IllegalArgumentException("The URL cannot be null !");
        }

        String toReturn = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("UTF-8")));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();

            toReturn = builder.toString();
        } catch (UnknownHostException ex) {
            LOG.warn("No internet connection !");
        } catch (IOException ex) {
            LOG.error("Cannot read the Stream pointed by the URL :" + url, ex);
        }
        return toReturn;
    }

    @Override
    public BufferedImage loadImage(final URL url) {
        LOG.info("Load image [URL=" + url + "]");

        if (url == null) {
            throw new IllegalArgumentException("The URL cannot be null !");
        }

        BufferedImage imageToReturn = null;
        try {
            imageToReturn = ImageIO.read(url);
        } catch (IOException ex) {
            LOG.error("Cannot read the Stream pointed by the URL :" + url, ex);
        }
        return imageToReturn;
    }
}
