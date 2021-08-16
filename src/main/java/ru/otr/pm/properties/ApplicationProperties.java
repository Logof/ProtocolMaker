package ru.otr.pm.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
    String result = "";
    InputStream inputStream;

    public String getPropValues(String propertyName) throws IOException {
        try {
            Properties properties = new Properties();
            String propertiesFileName = "application.config";

            inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propertiesFileName + "' not found in the classpath");
            }
            result = properties.getProperty(propertyName);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        } finally {
            inputStream.close();
        }
        return result;
    }
}
