package org.cquisper.servicio.chat.models;

import java.io.Serializable;

public class HistorialChat implements Serializable {
    private String historial;
    private String ipReceptor;

    public HistorialChat() {
    }

    public HistorialChat(String historial, String ipReceptor) {
        this.historial = historial;
        this.ipReceptor = ipReceptor;
    }

    public String getHistorial() {
        return historial;
    }

    public void setHistorial(String historial) {
        this.historial = historial;
    }

    public String getIpReceptor() {
        return ipReceptor;
    }

    public void setIpReceptor(String ipReceptor) {
        this.ipReceptor = ipReceptor;
    }

    @Override
    public String toString() {
        return "{historial='" + historial + '\'' + "}";
    }
}
