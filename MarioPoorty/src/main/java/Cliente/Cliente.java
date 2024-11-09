/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.HIDE_ON_CLOSE;

/**
 *
 * @author diego
 */
public class Cliente {
	private PantallaCliente pantallaCliente;
	private ElegirPersonaje pantallaElegirPersonaje;
	private DataInputStream entradaDatos;
	private DataOutputStream salidaDatos;
    private ObjectInputStream entradaObjetos;
    private ObjectOutputStream salidaObjetos;
    private Socket socket; //Socket del cliente.
	private String nombreCliente;
	private ArrayList<Integer> personajesParaEscoger;
	private int personajeCliente;

    public Cliente(PantallaCliente pantallaCliente) {
        this.pantallaCliente = pantallaCliente;
    }

    public void run(){

		try {
			conectar();
			pantallaCliente.getTxaCliente().append("Conectado al servidor");
		} catch (Exception ex) {System.out.println("Error conectando al servidor");}
		
		try {
			if(entradaDatos.readInt()==1){ //si es el primero en conectarse
				esPrimero();
			}
		} catch (Exception ex) {System.out.println("Error en algoritmo de primero en conectarse");}
		
		try {
			escogerPersonaje();
		} catch (Exception ex) {System.out.println("Error en algoritmo de escogencia de personaje");}
		
		new ThreadCliente(entradaDatos,salidaDatos,entradaObjetos,salidaObjetos,pantallaCliente,nombreCliente).start();
	}
    
    private void conectar() throws IOException{
        socket = new Socket("localhost", 50000);
		entradaDatos = new DataInputStream(socket.getInputStream());
		salidaDatos = new DataOutputStream(socket.getOutputStream());
        salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
		entradaObjetos = new ObjectInputStream(socket.getInputStream());
		nombreCliente = JOptionPane.showInputDialog("Introduzca su nombre:");
        pantallaCliente.setTitle(nombreCliente);
		salidaDatos.writeUTF(nombreCliente); //le manda al servidor el nombre
    }
	
	private void esPrimero() throws IOException{
		String strCant;
		int num;
		while(true){   
			strCant=JOptionPane.showInputDialog("Cuantas personas van a jugar? [2-6]");
			if(esSoloNumeros(strCant)){
				num=Integer.parseInt(strCant);
				if(num>=2 && num<=6)
					break;
				else
					JOptionPane.showMessageDialog(null,"Error, el número debe estar entre 2 y 6","Error", JOptionPane.ERROR_MESSAGE);
			}
			JOptionPane.showMessageDialog(null,"Error, debe ingresar un número","Error", JOptionPane.ERROR_MESSAGE);
		}
		salidaDatos.writeInt(num); //Se lo manda al servidor
	}
    
	private void escogerPersonaje() throws ClassNotFoundException,IOException{
		personajesParaEscoger = (ArrayList<Integer>) entradaObjetos.readObject();
		pantallaElegirPersonaje = new ElegirPersonaje(personajesParaEscoger);
		while(true){
			JOptionPane.showInternalMessageDialog( null, pantallaElegirPersonaje, "Elija su personaje", HIDE_ON_CLOSE);
			if(pantallaElegirPersonaje.getElegido() != -1){ //si escogió un personaje va a ser algo más que -1
				salidaObjetos.writeObject(pantallaElegirPersonaje.getDisponibles()); //Manda la lista actualizada
				break;
			}
		}
		personajeCliente = pantallaElegirPersonaje.getElegido();
		salidaDatos.writeInt(personajeCliente);
	}
	
    private boolean esSoloNumeros(String str) {
		return str.matches("\\d+");  
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public ArrayList<Integer> getPersonajesParaEscoger() {
		return personajesParaEscoger;
	}

	public int getPersonajeCliente() {
		return personajeCliente;
	}
	
    
    
}
