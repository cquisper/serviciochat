package org.cquisper.servicio.chat.services;

import org.cquisper.servicio.chat.util.FilePropertiesUtil;
import org.cquisper.servicio.chat.util.FilePropertiesUtilImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServidor {
    private static Socket socketServidor;
    private static FilePropertiesUtil filePropertiesUtil = new FilePropertiesUtilImpl();

    public static Socket getSocketServidor() throws IOException {

        String ipHostServidor = filePropertiesUtil.getValue("ip.host.servidor");

        Integer puertoHostServidor = getPuerto("port.host.servidor");

        socketServidor = new Socket(ipHostServidor, puertoHostServidor);

        return socketServidor;
    }

    public static ServerSocket getServerSocketServidor() throws IOException {
        Integer puertoHostServidor = getPuerto("port.host.servidor");

        return new ServerSocket(puertoHostServidor);
    }

    public static Socket createSocketCliente(String ipCliente) throws IOException {
        Integer puertoCliente = getPuerto("port.cliente");

        return new Socket(ipCliente, puertoCliente);
    }

    public static ServerSocket getServerSocketCliente() throws IOException {

        Integer puertoCliente = getPuerto("port.cliente");

        return new ServerSocket(puertoCliente);
    }

    public static Socket createSocketUsuario(String ipUsuario) throws IOException {
        Integer puertoUsuario = getPuerto("port.usuario");

        return new Socket(ipUsuario, puertoUsuario);
    }

    public static ServerSocket getServerSocketUsuario() throws IOException {

        Integer puertoUsuario = getPuerto("port.usuario");

        return new ServerSocket(puertoUsuario); //Puerto del cliente a enviar el flujo de datos
    }

    @NotNull
    private static Integer getPuerto(String keyPropertie) {
        return Integer.valueOf(filePropertiesUtil.getValue(keyPropertie));
    }
}
