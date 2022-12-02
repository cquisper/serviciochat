package org.cquisper.servicio.chat.services;

public interface ChatService{

    void enviarMensaje();

    void recibirMensaje();

    void coneccionNotify();

    void desconeccionNotify();
}
