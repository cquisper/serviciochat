package org.cquisper.servicio.chat.util;

import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FilePropertiesUtilImpl implements FilePropertiesUtil{

    private static final String TEXT_FILE_APP_PROPERTIES = "/aplication.properties";

    private Properties propertiesApp = loadPropertiesFile();

    @Override
    public Map<String, String> getValuesAndKeys() {
        Map<String, String> stringMapPropertie = new HashMap<>();
        propertiesApp.forEach((key, value) ->{
            System.out.println(key + " : " + value);
            stringMapPropertie.put(key.toString(), value.toString());
        });
        return stringMapPropertie;
    }

    @Override
    public String getValue(String keyPropertie) {
        return propertiesApp.getProperty(keyPropertie, "empty");
    }

    private Properties loadPropertiesFile(){
        var fileProper = new Properties();
        try {
            //fileProper.load(new FileReader(FilePropertiesUtilImpl.TEXT_FILE_APP_PROPERTIES));
            InputStream fileInputStream = getClass().getResourceAsStream(TEXT_FILE_APP_PROPERTIES);
            fileProper.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileProper;
    }
}
