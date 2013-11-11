package be.comicsdownloader.model.utils;

import be.comicsdownloader.model.CrashException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class CSVParser {

    private File toParse;
    private Map<CSVParserOptions, String> options;

    public CSVParser(File toParse) {
        this.toParse = toParse;
        options = new HashMap<>();
    }

    public CSVParser setProperty(CSVParserOptions option, String value) {
        String existingValue = options.get(option);
        if (existingValue != null) {
            options.remove(option);
        }
        options.put(option, value);
        return this;
    }

    public List<List<String>> parse() {
        BufferedReader bufferedReader = null;
        try {
            String separator = options.get(CSVParserOptions.SEPARATOR) != null ? options.get(CSVParserOptions.SEPARATOR) : CSVParserOptions.SEPARATOR.getDefault();
            String headerComment = options.get(CSVParserOptions.COMMENT_HEADER) != null ? options.get(CSVParserOptions.COMMENT_HEADER) : CSVParserOptions.COMMENT_HEADER.getDefault();

            List<List<String>> csvFileParsed = new LinkedList<>();
            InputStream inputStream = new FileInputStream(toParse);
            Reader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            bufferedReader = new BufferedReader(reader);

            @SuppressWarnings("UnusedAssignment")
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.startsWith(headerComment)) {
                    List<String> newRow = Arrays.asList(line.split(separator));
                    csvFileParsed.add(newRow);
                }
            }

            return csvFileParsed;
        } catch (FileNotFoundException ex) {
            throw new CrashException(ex);
        } catch (IOException ex) {
            throw new CrashException(ex);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw new CrashException("Problem closing the csv file buffered reader (Should not happen)", ex);
                }
            }
        }
    }

    public enum CSVParserOptions {

        SEPARATOR(";"),
        COMMENT_HEADER("#");
        private String defaultOption;

        private CSVParserOptions(String defaultOption) {
            this.defaultOption = defaultOption;
        }

        public String getDefault() {
            return defaultOption;
        }
    }
}
