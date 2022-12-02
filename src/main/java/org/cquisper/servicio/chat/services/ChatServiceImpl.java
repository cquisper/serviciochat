package org.cquisper.servicio.chat.services;

import org.cquisper.servicio.chat.models.FlujoDatos;
import org.cquisper.servicio.chat.models.UsuarioDTO;
import org.cquisper.servicio.chat.views.FromChatCliente;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ChatServiceImpl implements ChatService{

    private FromChatCliente chatClienteView;

    private List<UsuarioDTO> ipUsuarios;

    public ChatServiceImpl(FromChatCliente chatClienteView) {
        this.chatClienteView = chatClienteView;
    }

    @Override
    public void enviarMensaje() {
        try(Socket envioMensajeSocket = SocketServidor.getSocketServidor();

            ObjectOutput objetoEnvio = new ObjectOutputStream(envioMensajeSocket.getOutputStream())){

            chatClienteView.getTxaCampoChat().append("\n" + "tú: " + chatClienteView.getTxtChatTexto().getText());

            FlujoDatos flujoDatos = new FlujoDatos();

            flujoDatos.setUsername(chatClienteView.getTxtUsername().getText());

            String ipDestino = getIpDestino();

            flujoDatos.setDirecIp(ipDestino); //Ip del cliente a recibir el mensaje

            flujoDatos.setMensaje(chatClienteView.getTxtChatTexto().getText());

            objetoEnvio.writeObject(flujoDatos);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getIpDestino() {
        DefaultListModel<String> listModel = (DefaultListModel<String>) chatClienteView.getLsContactosOnline().getModel();

        String username = listModel.getElementAt(chatClienteView.getLsContactosOnline().getSelectedIndex()).replaceAll(" ", "");

        username = username.substring(0, username.indexOf("-"));

        String finalUsername = username;

        return ipUsuarios.stream().filter(usuarioDTO -> usuarioDTO.getUsuario().equals(finalUsername)).findAny().orElseThrow().getIp();
    }

    @Override
    public void recibirMensaje() {

        try(ServerSocket serverCliente = SocketServidor.getServerSocketCliente()){

            Socket socketRecibido;

            ObjectInput objetoEntrada;

            FlujoDatos flujoDatosRecibido;

            DefaultListModel<String> listModel;

            while (true){

                socketRecibido = serverCliente.accept();

                objetoEntrada = new ObjectInputStream(socketRecibido.getInputStream());

                flujoDatosRecibido = (FlujoDatos) objetoEntrada.readObject();

                if(flujoDatosRecibido.getMensaje().equals("online") || flujoDatosRecibido.getMensaje().equals("offline")){

                    listModel = new DefaultListModel<>();

                    ipUsuarios = flujoDatosRecibido.getUsuarioDTOList();

                    UsuarioDTO usuarioDTOrepetido = new UsuarioDTO();

                    usuarioDTOrepetido.setIp(flujoDatosRecibido.getDirecIp());

                    for (UsuarioDTO ipUsuario : ipUsuarios) {
                        if(ipUsuario.equals(usuarioDTOrepetido)){
                            chatClienteView.getTxtUsername().setText(ipUsuario.getUsuario());
                        }
                        if(ipUsuario.isOnline()){
                            listModel.addElement(ipUsuario.getUsuario() + " - conectado :D");
                        }else {
                            listModel.addElement(ipUsuario.getUsuario() + " - desconectado x");
                        }
                    }

                    chatClienteView.getLsContactosOnline().setModel(listModel);

                    chatClienteView.getLsContactosOnline().setSelectedIndex(0);

                }else{
                    //chatClienteView.getTxaCampoChat().append("\n" + flujoDatosRecibido.getUsername() + ": " + flujoDatosRecibido.getMensaje() + " para " + flujoDatosRecibido.getDirecIp());
                    chatClienteView.getTxaCampoChat().append("\n" + flujoDatosRecibido.getUsername() + ": " + flujoDatosRecibido.getMensaje());
                }
                objetoEntrada.close();

                socketRecibido.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void coneccionNotify() {
        try(Socket coneccionNotificar = SocketServidor.getSocketServidor();

            ObjectOutput flujoNotificacion = new ObjectOutputStream(coneccionNotificar.getOutputStream())) {

            FlujoDatos datosNotificacion = new FlujoDatos("online");

            datosNotificacion.setUsername(chatClienteView.getTxtUsername().getText());

            flujoNotificacion.writeObject(datosNotificacion);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void desconeccionNotify() {
        try(Socket coneccionNotificar = SocketServidor.getSocketServidor();

            ObjectOutput flujoNotificacion = new ObjectOutputStream(coneccionNotificar.getOutputStream())) {

            FlujoDatos datosNotificacion = new FlujoDatos("offline");

            datosNotificacion.setUsuarioDTOList(ipUsuarios);

            datosNotificacion.setUsername(chatClienteView.getTxtUsername().getText());

            flujoNotificacion.writeObject(datosNotificacion);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
