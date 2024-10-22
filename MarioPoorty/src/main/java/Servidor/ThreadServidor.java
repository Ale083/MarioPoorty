/*\
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
//Este es el thread que maneja in/out del servidor, es uno para cada cliente / socket
public class ThreadServidor extends Thread implements Comparable<ThreadServidor>,Serializable{ 
    public Socket socket;
    private Servidor servidor;
	private DataInputStream entradaDatos;
	private DataOutputStream salidaDatos;
    private ObjectInputStream entradaObjetos;
    private ObjectOutputStream salidaObjetos;
//    private Personaje personajeEnTablero; //Información del cliente como tal jugador.
    private String nombreCliente;
	private ArrayList<ThreadServidor> contrincantes;
//	private ArrayList<Personaje> personajes;
	private int numeroCliente;
	private int turno;

    public ThreadServidor(Socket socket, Servidor servidor, int ordenConexion) {
        this.socket = socket;
        this.servidor = servidor;
		this.numeroCliente = ordenConexion;
		this.nombreCliente = "";
		this.contrincantes = new ArrayList<ThreadServidor>();
//		this.personajes = new ArrayList<Personaje>();
        try {
			entradaDatos = new DataInputStream(socket.getInputStream());
			salidaDatos = new DataOutputStream(socket.getOutputStream());
            entradaObjetos = new ObjectInputStream(socket.getInputStream());
            salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) { System.out.println("Error en entrada/salida de datos");}
    }

    @Override
    public void run() {
        recibirNombreCliente();
        mandarPrimerJugador();
        
    }

	@Override
	public int compareTo(ThreadServidor o) { //Para ordenarlos por turnos
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}
	
	private void recibirNombreCliente(){
		try { 
			setNombreCliente(entradaDatos.readUTF());
			servidor.getPantallaServidor().write("El jugador " + numeroCliente + " se llama "+ nombreCliente); //Lo muestra en el servidor.
		} catch (IOException ex) {System.out.println("Error leyendo el nombre del cliente");}
	}
	
	private void mandarPrimerJugador(){
		try {
			salidaDatos.writeInt(numeroCliente);
			if(this.numeroCliente==1){
				servidor.setMaxJugadores(entradaDatos.readInt());
			}
		} catch (IOException ex) {System.out.println("Error con el algoritmo de primer jugador (ThreadServidor)");}
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
    
	
	
	
    
    
    
}
