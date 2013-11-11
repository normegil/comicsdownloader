package be.comicsdownloader.model.loader;

import java.awt.image.BufferedImage;
import java.net.URL;

public interface Loader {

    String loadDocument(URL url);

    BufferedImage loadImage(URL url);
}
