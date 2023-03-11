package sinhilos;
import java.util.Scanner;

/**
 * Clase para probar los metodos
 * @author ernesto
 */
public class Main {

    public static void main(String[] args) {
        //"android", "android", "192.168.1.186", 2221
        boolean entrar=true;
        Scanner sc=new Scanner(System.in);
        System.out.println("Introduzca el usuario");
        String user=sc.next();
        System.out.println("Introduzca la contraseña");
        String pass=sc.next();
        System.out.println("Introduzca la IP del server");
        String server=sc.next();
        System.out.println("Introduza el puerto");
        int puerto=sc.nextInt();
        MethodsFTP ftp=new MethodsFTP(user, pass, server, puerto);
        while(entrar){

            System.out.println("¿Qué desea hacer?\n1.Conectar FTP" +
                    "\n2.Listar Archivos\n3.Buscar Archivo" +
                    "\n4.Descargar Archivo\n5.Subir Archivo\n6.Desconectar");
            int opcion=sc.nextInt();
            switch (opcion){
                case 1:

                    ftp.conectar();
                    break;
                case 2:
                    System.out.println("Introduzca la ruta");
                    String ruta=sc.next();
                    ftp.listFiles(ruta);
                    break;
                case 3:
                    System.out.println("Introduzca Archivo a buscar");
                    String file=sc.next();
                    System.out.println("Introduzca la ruta");
                    String path=sc.next();
                    ftp.buscar(file, path);
                    break;
                case 4:
                    System.out.println("Introduzca la ruta al archivo");
                    path=sc.next();
                    System.out.println("Introduzca la ruta donde guardar el archivo");
                    String pathGuardar=sc.next();
                    ftp.descargarFile(path, pathGuardar);
                    break;
                case 5:
                    System.out.println("Introduzca la ruta del archivo local que quiere subir");
                    String localDir=sc.next();
                    System.out.println("Introduzca la ruta donde desea guardar el archivo, debe especificar tambien en la ruta el nombre del archivo");
                    String remoteDir=sc.next();
                    ftp.subir(remoteDir, localDir);
                    break;
                case 6:
                    ftp.desconectar();
                    entrar=false;
                    sc.close();
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
