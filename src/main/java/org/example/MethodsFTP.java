package org.example;

import org.apache.commons.net.ftp.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MethodsFTP {
    private String user;
    private String pass;
    private String server;
    private int port;
    private FTPClient ftpClient = new FTPClient();

    //constructor
    public MethodsFTP(String user, String pass, String server, int port) {
        this.user = user;
        this.pass = pass;
        this.server = server;
        this.port = port;
    }

    /**
     * Obtener la respuesta del servidor
     * @param ftpClient
     */
    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    /**
     * Metodo para conectarse al servidor
     */
    public void conectar() {
        try {
            ftpClient.connect(server, port);
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operacion Fallida, respuesta: " + replyCode);
                return;
            }
            boolean success = ftpClient.login(user, pass);
            showServerReply(ftpClient);
            if (!success) {
                System.out.println("No se pudo conectar al servidor");
                return;
            } else {
                System.out.println("CONECTADO AL SERVIDOR");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para desconectarse del servidor
     */
    public void desconectar() {
        try {
            System.out.println("Desconectandose del Servidor");
            ftpClient.logout();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo para listar los archivos
     * @param path
     */
    public void listFiles(String path) {

        FTPFile[] files = new FTPFile[0];
        try {
            files = ftpClient.listFiles(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (FTPFile file : files) {
            String details = file.getName();
            if (file.isDirectory()) {
                details = "[" + details + "]";
            }
            details += "\t\t" + file.getSize();
            details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());
            System.out.println(details);
        }
    }

    /**
     * Metodo para buscar un archivo
     * @param busca
     * @param path
     */
    public void buscar(String busca, String path) {
        FTPFileFilter filter = ftpFile -> (ftpFile.getName().contains(busca));
        FTPFile[] result;
        try {
            result = ftpClient.listFiles(path, filter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (result != null && result.length > 0) {
            System.out.println("SEARCH RESULT:");
            for (FTPFile aFile : result) {
                System.out.println(aFile.getName());
            }
        }
    }

    /**
     * Metodo para descargar un archivo
     * @param pathRemoto
     * @param saveDirPath
     */
    public void descargarFile(String pathRemoto, String saveDirPath) {
        try {
            ftpClient.enterLocalPassiveMode();
            FTPUtil.downloadSingleFile(ftpClient, pathRemoto, saveDirPath  + pathRemoto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo para subir un archivo
     * @param remoteDirPath
     * @param localDirPath
     */
    public void subir(String remoteDirPath, String localDirPath) {
        try {
            ftpClient.enterLocalPassiveMode();
            FTPUtil.uploadSingleFile(ftpClient, localDirPath, remoteDirPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}


