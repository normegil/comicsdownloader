package be.comicsdownloader.model.loader;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.Assert.*;

public class NetLoaderTest {

    private static final String PATH_TO_RESOURCES = "src/test/resources/be/comicsdownloader/model/";
    private NetLoader loader;
    private File sourceFile;
    private File imageFile;
    private File nullFile;

    public NetLoaderTest() {
        sourceFile = new File(PATH_TO_RESOURCES + "persistance/xml/EmptyCollectionReference.xml");
        imageFile = new File(PATH_TO_RESOURCES + "images/image.jpg");
        nullFile = new File(PATH_TO_RESOURCES + "FakeFile.xml");

        loader = new NetLoader();
    }

    @Test
    public void testLoadDocument() {
        try {
            String document = loader.loadDocument(sourceFile.toURI().toURL());

            assertNotNull(document);
            assertFalse(document.isEmpty());

            final File tempFile = File.createTempFile("LoadedContent", ".xml");
            FileUtils.writeStringToFile(tempFile, document);

            // Cannot compare contents - Loading doesn't keep formatting of the file
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testLoadDocumentWithWrongSource() {
        try {
            String emptyString = loader.loadDocument(nullFile.toURI().toURL());

            assertNotNull(emptyString);
            assertTrue(emptyString.isEmpty());
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testLoadDocumentWithNullSource() {
        try {
            loader.loadDocument(null);
            fail("Null Url should send an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testLoadImage() {
        try {
            BufferedImage loadImage = loader.loadImage(imageFile.toURI().toURL());

            assertNotNull(loadImage);
            assertEquals(1133, loadImage.getHeight());
            assertEquals(800, loadImage.getWidth());
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testLoadImageWithWrongSource() {
        try {
            BufferedImage missingImage = loader.loadImage(nullFile.toURI().toURL());
            assertNull(missingImage);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testLoadImageWithNullSource() {
        try {
            loader.loadImage(null);
            fail("Null Url should send an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

    }
}
