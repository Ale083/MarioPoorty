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

/**
 *
 * @author diego
 */
public class Cliente {
    private final String IP = "localhost";
    private final int PORT = 50000;
	private PantallaCliente pantallaCliente;
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
		
		try {
			conectar();
			pantallaCliente.getTxaCliente().append("Conectado al servidor");
		} catch (IOException ex) {System.out.println("Error conectando al servidor");}
		
		try {
			if(entradaDatos.readInt()==1){ //si es el primero en conectarse
				esPrimero();
			}
		} catch (IOException ex) {System.out.println("Error en algoritmo de primero en conectarse");}
    }

    
    
    private void conectar() throws IOException{
        socket = new Socket("localhost", 50000);
		entradaDatos = new DataInputStream(socket.getInputStream());
		salidaDatos = new DataOutputStream(socket.getOutputStream());
        entradaObjetos = new ObjectInputStream(socket.getInputStream());
        salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
		
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
				if(num<=1 || num>=7)
					break;
				else
					JOptionPane.showMessageDialog(null,"Error, el número debe estar entre 2 y 6","Error", JOptionPane.ERROR_MESSAGE);
			}
			JOptionPane.showMessageDialog(null,"Error, debe ingresar un número","Error", JOptionPane.ERROR_MESSAGE);
		}
		salidaDatos.writeInt(num); //Se lo manda al servidor
	}
    
    private boolean esSoloNumeros(String str) {
		return str.matches("\\d+");  
	}
	
    
    
}
