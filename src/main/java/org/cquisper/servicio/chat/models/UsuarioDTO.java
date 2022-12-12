package org.cquisper.servicio.chat.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsuarioDTO implements Serializable {
    private String usuario;
    private String ip;
    private List<HistorialChat> listHistorialChatt;
    private boolean online;

    public UsuarioDTO() {
        this.listHistorialChatt = new ArrayList<>();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void addHistorialChat(HistorialChat historialChat){
        try {
            this.listHistorialChatt.add(historialChat);
        }catch (UnsupportedOperationException e){
            System.out.println("Excepcion de agregacion en el list Historial chat :/");
            List<HistorialChat> historialChatsEx = new ArrayList<>(this.listHistorialChatt);
            historialChatsEx.add(historialChat);
            this.listHistorialChatt = historialChatsEx;
        }
    }

    public List<HistorialChat> getListHistorialChatt() {
        return listHistorialChatt;
    }

    public void setListHistorialChatt(List<HistorialChat> listHistorialChatt) {
        this.listHistorialChatt = listHistorialChatt;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "usuario='" + usuario + '\'' +
                ", ip='" + ip + '\'' +
                ", listHistorialChatt=" + listHistorialChatt +
                ", online=" + online +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioDTO that = (UsuarioDTO) o;
        return Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }

    @Serial
    private static final long serialVersionUID = 8799656478674716638L;
}
