package org.example;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
    MethodsFTP ftp=new MethodsFTP("android", "android", "192.168.1.17", 2221);

    ftp.conectar();
    //ftp.listFiles("/");
    //ftp.buscar("", "/Download");
    ftp.descargarFile("Download/image-4.png");
    ftp.subir();
    ftp.desconectar();


    }


}

