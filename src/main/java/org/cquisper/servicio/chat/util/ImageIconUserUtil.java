package org.cquisper.servicio.chat.util;

import javax.swing.*;
import java.awt.*;

public class ImageIconUserUtil {
    private static String FILE_ICON_USER_ONLINE = "/static/img/user-online.png";
    private static String FILE_ICON_USER_OFFLINE = "/static/img/user-offline.png";
    public ImageIcon getPerfilUsuario(boolean online){
        String fileIcon = online ? FILE_ICON_USER_ONLINE : FILE_ICON_USER_OFFLINE;
        System.out.println(fileIcon);
        ImageIcon iconUser = new ImageIcon(getClass().getResource(fileIcon));
        return new ImageIcon(iconUser.getImage().getScaledInstance( 50, 50, Image.SCALE_DEFAULT));
    }
}
