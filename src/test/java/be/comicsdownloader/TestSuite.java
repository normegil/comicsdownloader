package be.comicsdownloader;

import be.comicsdownloader.helpers.HelpersTestSuite;
import be.comicsdownloader.model.ModelTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ModelTestSuite.class, HelpersTestSuite.class})
public class TestSuite {
}
