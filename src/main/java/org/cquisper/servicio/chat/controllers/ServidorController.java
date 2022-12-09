package org.cquisper.servicio.chat.controllers;

import org.cquisper.servicio.chat.models.FlujoDatos;
import org.cquisper.servicio.chat.models.UsuarioDTO;
import org.cquisper.servicio.chat.views.FromServidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServidorController implements Runnable{

    private FromServidor fromServidor;

    private Set<UsuarioDTO> ipUsuarios;

    public ServidorController() {
        this.fromServidor = new FromServidor();

        this.fromServidor.setVisible(true);

        this.ipUsuarios = new HashSet<>();

        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            ServerSocket servidor = new ServerSocket(9999);

            String username, ip, mensaje;

            FlujoDatos flujoDatos;

            List<UsuarioDTO> usuarioDTOList;

            while(true){
                Socket miSocket = servidor.accept();

                ObjectInputStream objetoEntrada = new ObjectInputStream(miSocket.getInputStream());

                flujoDatos = (FlujoDatos) objetoEntrada.readObject();

                if(flujoDatos.getMensaje().equals("online")){ //Obtenemos los usuarios online

                    String ipRemoteUsuario = getIpCliente(miSocket);

                    UsuarioDTO usuarioDTO = new UsuarioDTO();

                    usuarioDTO.setIp(ipRemoteUsuario);

                    usuarioDTO.setUsuario(flujoDatos.getUsername());

                    usuarioDTO.setOnline(true);

                    ipUsuarios.add(usuarioDTO); //Agrega los usuarios con su ip pero sin ser duplicados

                    flujoDatos.setDirecIp(ipRemoteUsuario);

                    usuarioDTOList = new ArrayList<>(ipUsuarios);

                    flujoDatos.setUsuarioDTOList(usuarioDTOList);

                    for (UsuarioDTO ipUsuario : usuarioDTOList) {
                        if (ipUsuario.equals(usuarioDTO)) {
                            ipUsuario.setUsuario(usuarioDTO.getUsuario());
                        }
                        envioMensaje(ipUsuario.getIp(), flujoDatos);
                        System.out.println(ipUsuario.getUsuario() + " : " + ipUsuario.getIp() + " - conectado :D");
                    }

                } else if(flujoDatos.getMensaje().equals("offline")){

                    String ipRemoteUsuario = getIpCliente(miSocket);

                    UsuarioDTO usuarioDTO = new UsuarioDTO();

                    usuarioDTO.setIp(ipRemoteUsuario);

                    usuarioDTO.setUsuario(flujoDatos.getUsername());

                    usuarioDTOList = flujoDatos.getUsuarioDTOList().stream().map(userDto -> {
                        if(userDto.equals(usuarioDTO)){
                            userDto.setOnline(false);
                            return userDto;
                        }else {
                            return userDto;
                        }
                    }).toList();

                    flujoDatos.setUsuarioDTOList(usuarioDTOList);

                    for (UsuarioDTO ipUsuario : usuarioDTOList) {
                        envioMensaje(ipUsuario.getIp(), flujoDatos);
                        if(!ipUsuario.isOnline()){
                            System.out.println(ipUsuario.getUsuario() + " : " + ipUsuario.getIp() + " - desconectado x");
                        }
                    }
                } else {
                    username = flujoDatos.getUsername();

                    ip = flujoDatos.getDirecIp();

                    mensaje = flujoDatos.getMensaje();

                    fromServidor.getTxaRegistro().append("\n" + username + ": " + mensaje + " para " + ip);

                    envioMensaje(ip, flujoDatos);
                }

                miSocket.close();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void envioMensaje(String ipDestinatario, FlujoDatos flujoDatos) throws IOException {
        Socket enviaDestinatario = new Socket(ipDestinatario, 9090);

        ObjectOutputStream objetoReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());

        objetoReenvio.writeObject(flujoDatos);

        objetoReenvio.close();

        enviaDestinatario.close();
    }

    private String getIpCliente(Socket socket){
        InetAddress localizacion = socket.getInetAddress(); //Obtiene la direccion ip del cliente conectado

        return localizacion.getHostAddress();
    }

    public static void main(String[] args) {
        System.out.println("Iniciando servidor");
        new ServidorController();
    }
}
