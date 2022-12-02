package org.cquisper.servicio.chat.controllers;

import org.cquisper.servicio.chat.services.ChatService;
import org.cquisper.servicio.chat.services.ChatServiceImpl;
import org.cquisper.servicio.chat.views.FromChatCliente;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatClienteController implements Runnable {
    private FromChatCliente chatClienteForm;
    private ChatService chatService;
    public ChatClienteController(String username) {

        chatClienteForm = new FromChatCliente();

        chatClienteForm.getTxtUsername().setText(username);

        chatClienteForm.setVisible(true);

        chatClienteForm.getTxtUsername().setEditable(false);

        chatService = new ChatServiceImpl(chatClienteForm);

        initEventos();

        new Thread(this).start();

        chatClienteForm.getTxtChatTexto().requestFocus();
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
    }

    @Override
    public void run() {
        chatService.recibirMensaje();
    }

    public static void main(String[] args) {
        System.out.println("Iniciando aplicación");

        String username = JOptionPane.showInputDialog("Introduce su nombre de usuario");

        new ChatClienteController(username);
    }
}
