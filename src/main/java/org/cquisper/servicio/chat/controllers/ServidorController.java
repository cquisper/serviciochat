package org.cquisper.servicio.chat.controllers;

import org.cquisper.servicio.chat.models.FlujoDatos;
import org.cquisper.servicio.chat.models.UsuarioDTO;
import org.cquisper.servicio.chat.services.ServidorService;
import org.cquisper.servicio.chat.services.ServidorServiceImpl;
import org.cquisper.servicio.chat.views.FromServidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServidorController implements Runnable {

    private FromServidor fromServidor;

    private ServidorService servidorService;

    public ServidorController() {
        this.fromServidor = new FromServidor();

        this.fromServidor.setVisible(true);

        this.servidorService = new ServidorServiceImpl(fromServidor);

        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println("Iniciando socket servidor... en espera");
        servidorService.initConeccionCliente();
    }

    public static void main(String[] args) {
        System.out.println("Iniciando servidor");
        new ServidorController();
    }
}
