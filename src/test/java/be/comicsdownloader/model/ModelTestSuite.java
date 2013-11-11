package be.comicsdownloader.model;

import be.comicsdownloader.model.download.ChapterDownloadTaskTest;
import be.comicsdownloader.model.library.LibraryTestSuite;
import be.comicsdownloader.model.loader.NetLoaderTest;
import be.comicsdownloader.model.parser.ParserTestSuite;
import be.comicsdownloader.model.persistance.PersistanceTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({LoadingServiceTest.class, PropertiesServiceTest.class, NetLoaderTest.class, ChapterDownloadTaskTest.class,
        PersistanceTestSuite.class,
        ParserTestSuite.class,
        LibraryTestSuite.class})
public class ModelTestSuite {
}
