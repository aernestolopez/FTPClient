package hilosFTP;

import java.util.Scanner;

/**
 * Hilo para comprobar el funcionamiento del cliente FTP mediante mensajes por consola
 * @author ernesto
 */
public class FTPThread extends Thread{
    public FTPThread(ThreadGroup group, String name) {
        super(group, name);
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el usuario ");
        String user = sc.next();
        System.out.println("Introduce la contraseña");
        String contra = sc.next();
        System.out.println("Introduce el servidor");
        String servidor = sc.next();
        System.out.println("Introduce el puerto");
        int puerto = sc.nextInt();
        FTP ftp = new FTP(user, contra, servidor, puerto);
        int opcion;
        boolean entrar = true;
        while (entrar) {
            System.out.println("Elige una opcion\n" + "1.Conectar\n2.Listar Ficheros y Directorios\n3.Buscar fichero o directorio" +
                    "\n4.Descargar un fichero\n5.Subir un fichero\n6.Desconectar");
            opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                    ftp.conectar();
                    break;
                case 2:
                    System.out.println("Introduce la ruta a listar");
                    String path = sc.next();
                    ftp.listFiles(path);
                    break;
                case 3:
                    System.out.println("Introduce la ruta donde buscar");
                    String pathb = sc.next();
                    System.out.println("Introduce el termino a buscar");
                    String tb = sc.next();
                    ftp.buscar(tb, pathb);
                    break;
                case 4:
                    System.out.println("Introduce la ruta del archivo a descargar");
                    String pathd = sc.next();
                    System.out.println("Introduce la ruta donde guardar el archivo");
                    String pathlocal = sc.next();
                    ftp.descargarFile(pathd, pathlocal + "/" + pathd);
                    break;
                case 5:
                    System.out.println("Introduce la ruta del archivo a subir");
                    String paths = sc.next();
                    System.out.println("Introduce la ruta de destino");
                    String pathr = sc.next();
                    ftp.subirFile(pathr, paths);
                    break;
                case 6:
                    ftp.desconectar();
                    break;
                default:
                    ftp.desconectar();
                    entrar=false;
                    sc.close();
                    break;
            }
        }

    }
}
