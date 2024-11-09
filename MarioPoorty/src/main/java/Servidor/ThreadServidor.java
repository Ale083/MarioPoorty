/*\
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Modelos.Personaje;
import Modelos.Random;
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
					try {resultadoDados = entradaDatos.readInt(); }//Recibe como puesto el número que le salió en los dados.
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
				case 5:
					try {
						alguienGano(); //Si alguien gana
						break;
					} catch (Exception ex) {System.out.println("Error con caso alguienGano, señal 5 threadServidor");}
				case 6:
					try {
						fuegoUsado(); //Actualiza para los demás que el enemigo lo devuelva a 0 gráficamente.
						break;
					} catch (Exception ex) {System.out.println("Error con caso fuegoUsado, señal 6 threadServidor");}
				case 7:
					try {
						gatoConectarJugadores(); //Es el caso por si alguien empieza a jugar gato, abre la ventana para el oponente también.
						break;
					} catch (Exception ex) {System.out.println("Error con caso gatoConectarJugadores, señal 7 threadServidor"); System.out.println(ex.getLocalizedMessage());}
				case 8:
					try {
						mandarJugadaGato(); //Para mandar al oponente la jugada que hice
						break;
					} catch (Exception ex) {System.out.println("Error con caso mandarJugadaGato, señal 8 threadServidor");}
				case 9:
					try {
						abrirCardsTodos(); //Para mandar a los demas a abrir cards
						break;
					} catch (Exception ex) {System.out.println("Error con caso abrirCardsTodos, señal 9 threadServidor");}
				case 10:
					try {
						mandarValoresCartas(); //Para mandar a la persona que cayó, los valores de las otras cartas.
						break;
					} catch (Exception ex) {System.out.println("Error con caso mandarValoresCartas, señal 10 threadServidor");}
				case 11:
					try {
						memoryConectarOponente(); // para mandar a que el oponente le abra memory
						break;
					} catch (Exception ex) {System.out.println("Error con caso memoryConectarOponente, señal 11 threadServidor");}
				case 12:
					try {
						mandarParejasMemory(); // para mandar a que el oponente le abra memory
						break;
					} catch (Exception ex) {System.out.println("Error con caso mandarParejasMemory, señal 12 threadServidor");}
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
	
	private void alguienGano() throws Exception{
		String nombreWinner = entradaDatos.readUTF();
		for (ThreadServidor contrincante : contrincantes) {
			contrincante.salidaDatos.writeInt(4);
			contrincante.salidaDatos.writeUTF(nombreWinner);
		}
	}
	
	private void fuegoUsado() throws Exception{
		int turnoDeEnemigo = entradaDatos.readInt();
		for (ThreadServidor contrincante : contrincantes) {
			contrincante.salidaDatos.writeInt(5);
			contrincante.salidaDatos.writeInt(turnoDeEnemigo);
		}
	}
	
	private void gatoConectarJugadores() throws Exception{
		ThreadServidor oponente = contrincantes.get(Random.randomInt(0, contrincantes.size()-1));
		salidaDatos.writeInt(oponente.getPersonajeEnTablero().getOrdenTurno());
		oponente.getSalidaDatos().writeInt(6);
		oponente.getSalidaDatos().writeUTF(nombreCliente);
	}
	
	private void mandarJugadaGato() throws Exception{
		boolean gana = entradaDatos.readBoolean();
		int col = entradaDatos.readInt();
		int fila = entradaDatos.readInt();
		String nombreOponente = entradaDatos.readUTF();
		for (ThreadServidor contrincante : contrincantes) {
			if(contrincante.getPersonajeEnTablero().getNombre().equals(nombreOponente)){
				contrincante.salidaDatos.writeBoolean(gana);
				contrincante.salidaDatos.writeInt(col);
				contrincante.salidaDatos.writeInt(fila);
				break;
			}
		}
	}
	
	private void abrirCardsTodos() throws Exception{
		for (ThreadServidor contrincante : contrincantes) {
			contrincante.getSalidaDatos().writeInt(7);
			contrincante.getSalidaDatos().writeUTF(nombreCliente);
		}
	}
	
	private void mandarValoresCartas() throws Exception{
		String contrincante = entradaDatos.readUTF();
		int cartaNum = entradaDatos.readInt();
		int cartaType = entradaDatos.readInt();
		for (ThreadServidor c : contrincantes) {
			if(c.getNombreCliente().equals(contrincante)){
				c.getSalidaDatos().writeInt(cartaNum);
				c.getSalidaDatos().writeInt(cartaType);
				break;
			}
		}
	}
	
	private void memoryConectarOponente() throws Exception{
		ThreadServidor oponente = contrincantes.get(Random.randomInt(0, contrincantes.size()-1));
		salidaDatos.writeInt(oponente.getPersonajeEnTablero().getOrdenTurno());
		oponente.getSalidaDatos().writeInt(8);
		oponente.getSalidaDatos().writeUTF(nombreCliente);
	}
	
	private void mandarParejasMemory() throws Exception{
		int correctas = entradaDatos.readInt();
		String contrincante = entradaDatos.readUTF();
		for (ThreadServidor c : contrincantes) {
			if(c.getNombreCliente().equals(contrincante)){
				c.getSalidaDatos().writeInt(correctas);
				break;
			}
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
