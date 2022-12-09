package org.cquisper.servicio.chat.services;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServidor {
    private static Socket socketServidor;

    public static Socket getSocketServidor() throws IOException {

        socketServidor = new Socket("192.168.1.49", 9999); //Ip y puerto del servidor

        return socketServidor;
    }

    public static ServerSocket getServerSocketCliente() throws IOException {

        return new ServerSocket(9090); //Puerto del cliente a enviar el flujo de datos
    }
}
