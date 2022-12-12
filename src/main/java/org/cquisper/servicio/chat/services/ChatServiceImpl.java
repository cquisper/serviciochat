package org.cquisper.servicio.chat.services;

import org.cquisper.servicio.chat.models.FlujoDatos;
import org.cquisper.servicio.chat.models.HistorialChat;
import org.cquisper.servicio.chat.models.UsuarioDTO;
import org.cquisper.servicio.chat.util.ImageIconUserUtil;
import org.cquisper.servicio.chat.views.FromChatCliente;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

public class ChatServiceImpl implements ChatService{

    private FromChatCliente chatClienteView;

    private List<UsuarioDTO> ipUsuarios;

    private UsuarioDTO usuarioDTOLogin; //Es el usuario que esta usando la app actualmente

    private static ImageIconUserUtil imageIconUserUtil = new ImageIconUserUtil();

    public ChatServiceImpl(FromChatCliente chatClienteView) {
        this.chatClienteView = chatClienteView;
        this.usuarioDTOLogin = new UsuarioDTO();
    }

    @Override
    public void enviarMensaje() {
        try(Socket envioMensajeSocket = SocketServidor.getSocketServidor();

            ObjectOutput objetoEnvio = new ObjectOutputStream(envioMensajeSocket.getOutputStream())){

            String mensaje = "tú: " + chatClienteView.getTxtChatTexto().getText() + "\n";

            chatClienteView.getTxaCampoChat().append(mensaje);

            FlujoDatos flujoDatos = new FlujoDatos();

            flujoDatos.setUsername(chatClienteView.getLblUsername().getText());

            String ipDestino = getIpDestino();

            flujoDatos.setDirecIp(ipDestino); //Ip del cliente a recibir el mensaje

            addHistorialChats(mensaje, ipDestino);

            flujoDatos.setMensaje(chatClienteView.getTxtChatTexto().getText());

            objetoEnvio.writeObject(flujoDatos);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addHistorialChats(String mensaje, String ipDestino) {

        boolean contains = (usuarioDTOLogin.getListHistorialChatt().stream().anyMatch(historialChat -> historialChat.getIpReceptor().equals(ipDestino)));

        if((usuarioDTOLogin.getListHistorialChatt().isEmpty() || !contains)) { // Es nuevo no tiene historial
            HistorialChat historialChat = new HistorialChat(mensaje, ipDestino);
            usuarioDTOLogin.addHistorialChat(historialChat);
        }else{ //Obvio no es nuevo asi que se le aumenta el chat
            usuarioDTOLogin.setListHistorialChatt(usuarioDTOLogin.getListHistorialChatt().stream().map(historialChat -> {
                if(Objects.equals(historialChat.getIpReceptor(), ipDestino)){
                    historialChat.setHistorial(historialChat.getHistorial().concat(mensaje));
                    return historialChat;
                }
                return historialChat;
            }).toList());
            usuarioDTOLogin.getListHistorialChatt().forEach(System.out::println);
        }
    }

    @Override
    public void mostrarHistorial() {
        String ipReceptor = getIpDestino();
        chatClienteView.getTxaCampoChat().setText("");
        usuarioDTOLogin.getListHistorialChatt().forEach(historialChat -> {
            if(historialChat.getIpReceptor().equals(ipReceptor)){
                System.out.println(historialChat.getHistorial());
                chatClienteView.getTxaCampoChat().setText(historialChat.getHistorial());
            }
        });

        String username = chatClienteView.getLsContactosOnline().getSelectedValue();

        if(username != null){
            boolean status = username.contains("desconectado");

            chatClienteView.getLblEstatus().setText(status ? "En linea hace un momento" : "En linea");

            chatClienteView.getLblIconPerfilUsuario().setIcon(imageIconUserUtil.getPerfilUsuario(!status));

            username = username.substring(0, username.indexOf("-")).replaceAll(" ", "");

            System.out.println("desde mostrarHistorial: " + username);

            chatClienteView.getLblUsernameReceptor().setText(username);
        }
    }

    private String getIpDestino() {

        String username = chatClienteView.getLsContactosOnline().getSelectedValue();

        if(username == null){
            return "";
        }

        System.out.println(username);

        username = username.substring(0, username.indexOf("-")).replaceAll(" ", "");

        System.out.println("getIpDestino: " + username);

        String finalUsername = username;

        return ipUsuarios.stream().peek(System.out::println).filter(usuarioDTO -> usuarioDTO.getUsuario().equals(finalUsername)).findAny().orElseThrow().getIp();
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
                        if(ipUsuario.isOnline()){
                            listModel.addElement(ipUsuario.getUsuario() + " - conectado :D");
                        }else {
                            listModel.addElement(ipUsuario.getUsuario() + " - desconectado x");
                        }
                    }
                    chatClienteView.getLsContactosOnline().setModel(listModel);
                    int userIndex = 0;
                    for (int i = 0; i < ipUsuarios.size(); i++) {
                        if(ipUsuarios.get(i).equals(usuarioDTOrepetido)){
                            System.out.println( "i = " + i);
                            userIndex = i;
                        }
                    }

                    chatClienteView.getLsContactosOnline().setSelectedIndex(userIndex);

                    mostrarHistorial();
                }else{
                    //chatClienteView.getTxaCampoChat().append("\n" + flujoDatosRecibido.getUsername() + ": " + flujoDatosRecibido.getMensaje() + " para " + flujoDatosRecibido.getDirecIp());
                    String mensaje = flujoDatosRecibido.getUsername() + ": " + flujoDatosRecibido.getMensaje() + "\n";
                    chatClienteView.getTxaCampoChat().append(mensaje);
                    addHistorialChats(mensaje, flujoDatosRecibido.getUsuarioDTO().getIp());
                }
                objetoEntrada.close();

                socketRecibido.close();
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @Override
    public void recibirHistorial() {
        try(ServerSocket coneccionUsuario = SocketServidor.getServerSocketUsuario()){;
            Socket socketRecibidoUsuario;

            ObjectInput objetoRecibido;

            FlujoDatos flujoDatosRecibido;

            while(true) {
                socketRecibidoUsuario = coneccionUsuario.accept();

                objetoRecibido = new ObjectInputStream(socketRecibidoUsuario.getInputStream());

                flujoDatosRecibido = (FlujoDatos) objetoRecibido.readObject();

                usuarioDTOLogin = flujoDatosRecibido.getUsuarioDTO();

                objetoRecibido.close();

                socketRecibidoUsuario.close();
            }

        }catch (IOException | RuntimeException | ClassNotFoundException e ) {
            e.printStackTrace(System.out);
        }
    }

    @Override
    public void coneccionNotify() {
        try(Socket coneccionNotificar = SocketServidor.getSocketServidor();

            ObjectOutput flujoNotificacion = new ObjectOutputStream(coneccionNotificar.getOutputStream())) {

            FlujoDatos datosNotificacion = new FlujoDatos("online");

            datosNotificacion.setUsername(chatClienteView.getLblUsername().getText());

            flujoNotificacion.writeObject(datosNotificacion);

        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    @Override
    public void desconeccionNotify() {
        try(Socket coneccionNotificar = SocketServidor.getSocketServidor();

            ObjectOutput flujoNotificacion = new ObjectOutputStream(coneccionNotificar.getOutputStream())) {

            FlujoDatos datosNotificacion = new FlujoDatos("offline");

            datosNotificacion.setUsuarioDTOList(ipUsuarios);

            datosNotificacion.setUsername(chatClienteView.getLblUsername().getText());

            datosNotificacion.setUsuarioDTO(usuarioDTOLogin);

            flujoNotificacion.writeObject(datosNotificacion);

        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
}
