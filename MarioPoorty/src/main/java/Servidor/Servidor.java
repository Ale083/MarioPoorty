//package Servidor;
//
//import Modelos.Mensaje;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class Servidor {
//    private final int PORT = 8084;
//    ServerSocket server;
//    public PantallaServidor pantalla;
//    
//     ArrayList<ThreadServidor> clientesAceptados;
//    ServerConnectionsThread conexionsThread;
//    
//    public Servidor(PantallaServidor pantalla){
//        this.pantalla = pantalla;
//        connect();
//        clientesAceptados = new ArrayList<ThreadServidor>();
//        conexionsThread = new ServerConnectionsThread(this);
//        conexionsThread.start();
//        
//    }
//    
//    public void connect(){
//        try {
//            server = new ServerSocket(PORT);
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//        }
//    }
//    
//    public void broadcoast(Mensaje mensaje){
//        
//        for (ThreadServidor cliente : clientesAceptados) {
//            try {
//                cliente.salida.writeObject(mensaje);
//            } catch (IOException ex) {
//            
//            }
//        }
//        this.pantalla.write("Enviado " + clientesAceptados.size() +" veces: " + mensaje);
//        
//    }
//    
//    public void privateMessage(Mensaje mensaje){
//        
//        for (ThreadServidor cliente : clientesAceptados) {
//            try {
//                if(mensaje.getReceptor().equals(cliente.nombre)){
//                    cliente.salida.writeObject(mensaje);
//                    break;
//                }
//            } catch (IOException ex) {
//            
//            }
//        }
//        this.pantalla.write("Enviado " + clientesAceptados.size() +" veces: " + mensaje);
//        
//    }
//    
//    
//}
