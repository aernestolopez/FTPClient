package hilosFTP;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Clase con los metodos de FTP
 * @author ernesto
 */
public class FTP {
    private final String user;
    private final String pass;
    private final String server;
    private final int port;
    private FTPClient ftpClient = new FTPClient();

    /**
     * Constructor del FTP
     * @param user
     * @param pass
     * @param server
     * @param port
     */
    public FTP(String user, String pass, String server, int port) {
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
    public void conectar(){
        try {
            ftpClient.connect(server, port);
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server  replyCode: " + replyCode);
                return;
            }
            boolean success = ftpClient.login(user, pass);
            showServerReply(ftpClient);
            if (!success) {
                System.out.println("Could not login to the server");
                return;
            } else {
                System.out.println("LOGGED IN SERVER");
            }
        } catch (IOException ex) {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para desconectarse del servidor
     */
    public void desconectar(){
        try {
            ftpClient.logout();
            ftpClient.disconnect();
            if(!ftpClient.isConnected()){
                System.out.println("SERVER: DISCONECTED");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo para listar los archivos
     * @param path
     */
    public void listFiles(String path){
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
    public void buscar(String busca,String path){
        FTPFileFilter filter = ftpFile -> (ftpFile.getName().contains(busca));
        FTPFile[] result;
        try {
            result = ftpClient.listFiles(path,filter);
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
    public void descargarFile(String pathRemoto,String  saveDirPath) {
        try {
            ftpClient.enterLocalPassiveMode();
            File downloadFile = new File(saveDirPath);
            File parentDir = downloadFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdir();
            }
            OutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(downloadFile));
            try {
                ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                System.out.println("Descargando el archivo...");
                ftpClient.retrieveFile(pathRemoto, outputStream);
            } catch (IOException ex) {
                throw ex;
            } finally {
                System.out.println("Archivo Descargado");
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo para subir un archivo
     * @param remoteDirPath
     * @param localDirPath
     */
    public void subirFile(String remoteDirPath,String localDirPath) {
        try {
            ftpClient.enterLocalPassiveMode();
            File localFile = new File(localDirPath);
            InputStream inputStream = new FileInputStream(localFile);
            try {
                System.out.println("Subiendo archivo");
                if(ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE)){
                    System.out.println("Archivo Subido");
                }
                ftpClient.storeFile(remoteDirPath, inputStream);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
