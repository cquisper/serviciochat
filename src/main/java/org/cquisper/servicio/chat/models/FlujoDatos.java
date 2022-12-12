package org.cquisper.servicio.chat.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlujoDatos implements Serializable {

    private String username;

    private String direcIp;

    private String mensaje;

    private UsuarioDTO usuarioDTO;

    private List<UsuarioDTO> usuarioDTOList = new ArrayList<>();

    public FlujoDatos() {
    }

    public FlujoDatos(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDirecIp() {
        return direcIp;
    }

    public void setDirecIp(String direcIp) {
        this.direcIp = direcIp;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<UsuarioDTO> getUsuarioDTOList() {
        return usuarioDTOList;
    }

    public void setUsuarioDTOList(List<UsuarioDTO> usuarioDTOList) {
        this.usuarioDTOList = usuarioDTOList;
    }

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }

    @Serial
    private static final long serialVersionUID = 8799656478674716639L;
}
