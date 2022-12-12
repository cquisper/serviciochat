package org.cquisper.servicio.chat.util;

import java.util.Map;

public interface FilePropertiesUtil {

    Map<String, String> getValuesAndKeys();

    String getValue(String keyPropertie);
}
