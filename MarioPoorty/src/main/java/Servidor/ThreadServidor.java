/*\
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Modelos.Personaje;
import Modelos.TipoCasilla;
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
    private Personaje personajeEnTablero; //Información del cliente como tal jugador.
    private String nombreCliente;
	private ArrayList<ThreadServidor> contrincantes;
	private ArrayList<Personaje> personajes;
	private int numeroCliente;
	private int resultadoDados;

    public ThreadServidor(Socket socket, Servidor servidor, int ordenConexion) {
        this.socket = socket;
        this.servidor = servidor;
		this.numeroCliente = ordenConexion;
		this.nombreCliente = "";
		this.contrincantes = new ArrayList<ThreadServidor>();
		this.personajes = new ArrayList<Personaje>();
        try {
			entradaDatos = new DataInputStream(socket.getInputStream());
			salidaDatos = new DataOutputStream(socket.getOutputStream());
            salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
			entradaObjetos = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) { System.out.println("Error en entrada/salida de datos");}
		this.resultadoDados = -1;
    }

    @Override
    public void run() {
        recibirNombreCliente();
        mandarPrimerJugador();
        escogerPersonaje();
		juegoEmpieza();
		
		
    }

	@Override
	public int compareTo(ThreadServidor o) { //Para ordenarlos por turnos, ordena de mayor a menor
		//hasta donde entiendo si da negativo, entonces este objeto, va antes que el objeto "o" y viceversa.
		return o.getResultadoDados() - resultadoDados;
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
			if(numeroCliente==1){
				servidor.setMaxJugadores(entradaDatos.readInt());
				System.out.println("alskdja" + servidor.getMaxJugadores());
			}
		} catch (IOException ex) {System.out.println("Error con el algoritmo de primer jugador (ThreadServidor)");}
	}

	private void escogerPersonaje(){
		try {
			salidaObjetos.writeObject(servidor.getPersonajesParaEscoger()); //manda al cliente el arraylist de los personajes disponibles.
			servidor.getPantallaServidor().write("Se mandaron los personajes a " + nombreCliente);
			servidor.setPersonajesParaEscoger((ArrayList<Integer>)entradaObjetos.readObject());
			servidor.getPantallaServidor().write("Se devolvió al servidor los personajes disponibles actualizados.");
			this.personajeEnTablero = new Personaje(nombreCliente, entradaDatos.readInt()); //Espera a que el cliente mande el número de personaje escogido, y crea un personaje.
		} catch (Exception ex) {System.out.println("Error escogiendo personaje para algún cliente");}
	}
	
	private void juegoEmpieza(){
		int evento = -1;
		while(true){
			try {
				evento = entradaDatos.readInt();
			} catch (IOException ex) {System.out.println("Error con entrada de evento en threadServidor");}
			
			switch(evento){
				case 0: //caso para poner que num le salio en los dados del principio
					try {resultadoDados = entradaDatos.readInt();}//Recibe como puesto el número que le salió en los dados.
					catch (Exception ex) {System.out.println("Error en evento 0 en threadServidor");}
					break;
				case 1: 
					try {
						pasarAEnemigosMovimientos(); //Pasa a los enemigos la cantidad de casillas que se movio esta persona
						break;
					} catch (Exception ex) {System.out.println("Error con caso pasarAEnemigosMovimientos, señal 1 threadServidor");}
				case 2:
					try {
						turnoTableroEnemigosRepetir(); //actualiza a los enemigos para que tengan el turno de nuevo de la misma persona, se usa cuando una persona repite
						break;
					} catch (Exception ex) {System.out.println("Error con caso turnoTableroEnemigosRepetir, señal 2 threadServidor");}
				case 3:
					try {
						jugarDeNuevo(); //Llama a la señal 1 del threadCliente, y le da el personaje de este thread, hace que repita el turno, se usa despues de toda la serie de señales para repetir.
						break;
					} catch (Exception ex) {System.out.println("Error con caso jugarDeNuevo, señal 3 threadServidor");}
				case 4:
					try {
						siguienteTurno(); //Pone a la siguiente persona a tirar los dados.
						break;
					} catch (Exception ex) {System.out.println("Error con caso siguienteTurno, señal 4 threadServidor");}
			}
		}
	}
	
	
	public void crearTablero(ArrayList<TipoCasilla>listaTablero){
		try{
			salidaDatos.writeInt(0); //manda a threadCliente la señal de creación de tablero.
			for (ThreadServidor contrincante : contrincantes) {
				personajes.add(contrincante.getPersonajeEnTablero());
			}
			personajes.add(personajeEnTablero); //De último está el del cliente especifico.
			salidaObjetos.writeObject(personajes); //manda los personajes, el cliente está en evento crearTablero, que es señal 1.
			salidaObjetos.writeObject(listaTablero); //manda las casillas, por lo mismo que arriba.
			
		}catch(Exception e){System.out.println("Error en crearTablero en threadServidor");}
	}

	private void pasarAEnemigosMovimientos() throws Exception{ //señal 1
		int casillaEspecifica = entradaDatos.readInt();
		int turnoDePersonajeQueMueve = entradaDatos.readInt();
		for (ThreadServidor contrincante : contrincantes) {
			contrincante.salidaDatos.writeInt(2);
			contrincante.salidaDatos.writeInt(casillaEspecifica);
			contrincante.salidaDatos.writeInt(turnoDePersonajeQueMueve);
		}
	}
	
	private void turnoTableroEnemigosRepetir() throws Exception{ //señal 2
		int turnoActual = entradaDatos.readInt();
		for (ThreadServidor contrincante : contrincantes) {
			contrincante.salidaDatos.writeInt(3); 
			contrincante.salidaDatos.writeInt(turnoActual);
		}
	}
	
	private void jugarDeNuevo() throws Exception{
		salidaDatos.writeInt(1);
		salidaDatos.writeInt(personajeEnTablero.getOrdenTurno());
	}
	
	private void siguienteTurno() throws Exception{
		for (ThreadServidor contrincante : contrincantes) {
			contrincante.salidaDatos.writeInt(1);
			contrincante.salidaDatos.writeInt(contrincante.getPersonajeEnTablero().getOrdenTurno());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	//GETTERS Y SETTERS
	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public Personaje getPersonajeEnTablero() {
		return personajeEnTablero;
	}

	public ArrayList<ThreadServidor> getContrincantes() {
		return contrincantes;
	}    
	
	public int getResultadoDados() {
		return resultadoDados;
	}

	public DataOutputStream getSalidaDatos() {
		return salidaDatos;
	}
}
