package be.comicsdownloader.generators;

import be.comicsdownloader.model.service.PropertiesService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesGenerator {

    private static final String PATH_TO_TEST_RESOURCES_FOLDER = "src/test/resources/be/comicsdownloader/model/persistance/properties/";

    public static void main(String args[]) {
        try {
            File originalSourceFile = new File(PATH_TO_TEST_RESOURCES_FOLDER + "OriginalSourceReference.properties");
            originalSourceFile.createNewFile();

            Properties properties = new Properties();
            for (PropertiesService.PropertyKey key : PropertiesService.PropertyKey.values()) {
                properties.put(key.toString(), key.getDefaultValue());
            }

            properties.store(new FileWriter(originalSourceFile), "Settings File - Generated");

            File insertFile = new File(PATH_TO_TEST_RESOURCES_FOLDER + "InsertReference.properties");
            insertFile.createNewFile();
            properties.setProperty(PropertiesService.PropertyKey.AUTO_REFRESH.toString(), "false");
            properties.store(new FileWriter(insertFile), "Settings File - Generated - Insert reference for test");

            File deleteFile = new File(PATH_TO_TEST_RESOURCES_FOLDER + "DeleteReference.properties");
            deleteFile.createNewFile();
            properties.setProperty(PropertiesService.PropertyKey.AUTO_REFRESH.toString(), PropertiesService.PropertyKey.AUTO_REFRESH.getDefaultValue());
            properties.store(new FileWriter(deleteFile), "Settings File - Generated - Delete reference for test");

            File updateFile = new File(PATH_TO_TEST_RESOURCES_FOLDER + "UpdateReference.properties");
            updateFile.createNewFile();
            properties.setProperty(PropertiesService.PropertyKey.AUTO_REFRESH.toString(), "false");
            properties.store(new FileWriter(updateFile), "Settings File - Generated - Update reference for test");

        } catch (IOException ex) {
            Logger.getLogger(PropertiesGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
