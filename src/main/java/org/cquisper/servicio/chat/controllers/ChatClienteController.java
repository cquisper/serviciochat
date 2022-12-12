package org.cquisper.servicio.chat.controllers;

import org.cquisper.servicio.chat.services.ChatService;
import org.cquisper.servicio.chat.services.ChatServiceImpl;
import org.cquisper.servicio.chat.views.FromChatCliente;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatClienteController implements Runnable {
    private FromChatCliente chatClienteForm;
    private ChatService chatService;
    public ChatClienteController(String username) {

        chatClienteForm = new FromChatCliente();

        chatClienteForm.getLblUsername().setText(username);

        chatClienteForm.setVisible(true);

        chatService = new ChatServiceImpl(chatClienteForm);

        initEventos();

        chatClienteForm.getTxtChatTexto().requestFocus();

        new Thread(this).start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        initHistorialNotify();
    }

    private void initHistorialNotify() {
        Runnable runHistorialChats = () -> {
            System.out.println("entro recibir notify");
            chatService.recibirHistorial();
        };
        new Thread(runHistorialChats).start();
    }

    private void initEventos() {
        chatClienteForm.getBtnEnviar().addActionListener(e -> {
            chatService.enviarMensaje();
            chatClienteForm.getTxtChatTexto().setText("");
        });

        chatClienteForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {//Evento que se ejecuta cuando se abre la vista Jframe chatcliente
                System.out.println("Abriendo la aplicacion");
                chatService.coneccionNotify();
            }

            @Override
            public void windowClosing(WindowEvent e) {//Evento que se ejecuta cuando se cierra la vista Jframe chatcliente
                //System.out.println("Me estoy cerrando :D");
                chatService.desconeccionNotify();
            }
        });

        chatClienteForm.getTxtChatTexto().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    chatService.enviarMensaje();
                    chatClienteForm.getTxtChatTexto().setText("");
                }
            }
        });

        chatClienteForm.getLsContactosOnline().addListSelectionListener(e -> {
            System.out.println("Evento de selección");
            chatService.mostrarHistorial();
        });
    }

    @Override
    public void run() {
        System.out.println("entro recibir mensaje");

        chatService.recibirMensaje();

    }

    public static void main(String[] args) {
        System.out.println("Iniciando aplicación");

        String username = JOptionPane.showInputDialog("Introduzca su nombre de usuario");

        new ChatClienteController(username);
    }
}
