package hilosFTP;

import java.util.Scanner;

/**
 * Clase para crear mas de una conexion mediante hilos
 * @author ernesto
 */
public class TestFTP {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean salir=true;
        ThreadGroup tg=new ThreadGroup("ftps");
        int opcion;
        while(salir){
            System.out.println("Pulse lo que desee\n1.Nueva Conexion\n2.Salir");
            opcion=sc.nextInt();
            if(opcion==1){
                new FTPThread(tg,"hiloftp");
            }else{
                while(tg.activeCount()>0){}
                break;
            }
        }
        sc.close();
    }
}
