package be.comicsdownloader.helpers;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FormatHelperTest {

    @Test
    public void testGetOutputFloat(){
        assertEquals("12.147", FormatHelper.getOutputFloat(12.147f));

        assertEquals("154", FormatHelper.getOutputFloat(154f));
    }

}
