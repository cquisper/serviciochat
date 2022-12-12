package org.cquisper.servicio.chat.services;

public interface ChatService{

    void enviarMensaje();

    void recibirMensaje();

    void recibirHistorial();

    void mostrarHistorial();

    void coneccionNotify();

    void desconeccionNotify();
}
