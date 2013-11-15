package be.comicsdownloader.helpers;

public final class FormatHelper {

    public static String getOutputFloat(float toFormat){
        if(toFormat % 1 == 0){
            return Integer.toString((int) toFormat);
        }else {
            return Float.toString(toFormat);
        }
    }

}
