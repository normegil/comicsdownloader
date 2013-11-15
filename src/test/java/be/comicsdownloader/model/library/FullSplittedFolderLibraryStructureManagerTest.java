package be.comicsdownloader.model.library;

import be.comicsdownloader.generators.TestStrutureCreator;
import be.comicsdownloader.helpers.FormatHelper;
import be.comicsdownloader.model.AvailableSite;
import be.comicsdownloader.model.pojo.manga.*;
import be.comicsdownloader.model.service.PropertiesService;
import be.comicsdownloader.model.service.Services;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.Assert.*;

public class FullSplittedFolderLibraryStructureManagerTest extends AbstractLibraryStructureManagerTest{

    public FullSplittedFolderLibraryStructureManagerTest() {
        super(LibraryStructure.FULL_SPLITTED.getManager());
    }

    @Override
    protected String getPath(Chapter chapter) {
        StringBuilder builder = new StringBuilder();
        builder.append(getRootDirectoryPath())
                .append("/")
                .append(chapter.getTome().getSerie().getName())
                .append("/Tome ")
                .append(FormatHelper.getOutputFloat(chapter.getTome().getNumber()))
                .append("/Chapter ")
                .append(FormatHelper.getOutputFloat(chapter.getNumber()))
                .append("/");

        return builder.toString();
    }

}
