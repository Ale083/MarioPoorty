package Servidor;

import Modelos.TipoCasilla;
import Modelos.Random;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
* @author Alejandro Umaña y Luis Diego Rodriguez
*/

public class Servidor {
    private PantallaServidor pantallaServidor;
	private ServerSocket servidor;
	private ArrayList<Socket> clientes; //Sockets para cada usuario / cliente.
	private ArrayList<ThreadServidor> threadsServidor;  //Thread de conexión con servidor para cada cliente.
	private ArrayList<Integer> personajesParaEscoger;
	private ArrayList<TipoCasilla> tablero;
	private int conectados;
	private int maxJugadores;
	
    public Servidor(PantallaServidor pantallaServidor){
        this.pantallaServidor = pantallaServidor;
		this.clientes = new ArrayList<Socket>();
		this.threadsServidor= new ArrayList<ThreadServidor>();
		this.personajesParaEscoger = new ArrayList<Integer>();
		ponerPersonajesDisponibles();
		this.tablero= new ArrayList<TipoCasilla>();
		inicializarCasillas();
		conectados = 0;
		maxJugadores = 6;
    }
	
	public void run(){
		try {
			servidor = new ServerSocket(50000);
		} catch (IOException ex) {System.out.println("Error levantando el servidor");}
		pantallaServidor.write("Servidor levantado");
		pantallaServidor.write("Esperando usuarios");
		esperarConexiones(); //Se queda aquí hasta tener la cantidad necesaria de jugadores conectados.
		esperarPersonajesIniciados(); //Se queda aquí hasta que todos tengan personaje escogido.
		
		ponerEnemigos(); //Para cada threadServidor pone en el ArrayList contrincantes los otros threadServidor
		pantallaServidor.write("Empieza el juego");
		
		//En threadCliente les abre un form para tirar dados.
		ordenDeTurnos();//Se queda esperando a que ya todos tiraron los dados del principio para escoger orden
		Collections.sort(threadsServidor); //Poner threadsServidor en orden por el resultado de los dados del principio.
		
		ponerEnCadaPersonajeSuTurno();
		iniciarTableroParaCadaCliente();
	}
    
	
	private void ponerPersonajesDisponibles(){
        for (int i = 0; i < 10; i++) {
            personajesParaEscoger.add(i); //Pone del 0-9 en el ArrayList de disponibles, su respectivo numero de posicion, es decir [0,1,2,3,4...]
        }
    }
	
	private void inicializarCasillas(){
        for (int i = 0; i < 9; i++) { //para apuntar a cada juego
            tablero.add(getTipoDeCasilla(i)); 
            tablero.add(getTipoDeCasilla(i)); //Mete 2 de gato, 2 de sopa ...
        }
        for(int i=9;i<17;i++)                
            tablero.add(getTipoDeCasilla(i)); //Mete 1 de las demás casillas
        
        Collections.shuffle(tablero);
        int numDeTubo=1;
        for (int i = 0; i < tablero.size(); i++) { // recorre cada casilla
            if(tablero.get(i).equals(TipoCasilla.TUBO1)){
				if(numDeTubo == 1){
					tablero.remove(i); 
					tablero.add(i, TipoCasilla.TUBO1); 
					numDeTubo++;
				} else if(numDeTubo == 2){
					tablero.remove(i); 
					tablero.add(i, TipoCasilla.TUBO2); 
					numDeTubo++;
				} else if(numDeTubo == 3){
					tablero.remove(i); 
					tablero.add(i, TipoCasilla.TUBO3); 
					numDeTubo++;
				}
				
            }
        }
		//ya hay 26 ahí.
		tablero.addFirst(TipoCasilla.INICIO);
		tablero.addLast(TipoCasilla.FINAL);
		for (TipoCasilla tipoCasilla : tablero) {
			System.out.println(tipoCasilla.name());
		}
    }
	
	private void esperarConexiones(){
		while(conectados<maxJugadores){ //hasta que no hayan max personajes
			try {
				clientes.add(servidor.accept()); //aqui se queda esperando a que alguien se meta y lo mete al arraylist de clientes
			} catch (IOException ex) {System.out.println("Error conectando cliente");}
			
			pantallaServidor.write("Cliente número " + (conectados+1) + " conectado");
			threadsServidor.add(new ThreadServidor(clientes.get(conectados),this,++conectados));
			threadsServidor.get(conectados-1).start();
		}
	}
	
	private void esperarPersonajesIniciados(){
		while(true){
			try {Thread.sleep(500);} catch (InterruptedException ex) {}
			int personajesIniciados = 0;
			for (ThreadServidor threadServidor : threadsServidor) {
				if(threadServidor.getPersonajeEnTablero() != null){
					personajesIniciados++;
				}
			}
			if(personajesIniciados == threadsServidor.size()){
				break;
			}
		}
	}
	
	private void ponerEnemigos(){
		for (ThreadServidor cliente : threadsServidor) {
			for (ThreadServidor enemigo : threadsServidor) {
				if(cliente != enemigo){
					cliente.getContrincantes().add(enemigo);
				}
			}
		}
	}
	
	
	private void ordenDeTurnos(){
		while(true){ //Se queda esperando a que ya todos tiraron los dados del principio para escoger orden
			try {Thread.sleep(500);} catch (InterruptedException ex) {}
			int personasConOrden=0;
			for (ThreadServidor threadServidor : threadsServidor) {
				if(threadServidor.getResultadoDados() != -1){
					personasConOrden++;
				}
			}
			if(personasConOrden == threadsServidor.size()){
				break;
			}
		}
	}
	
	private void ponerEnCadaPersonajeSuTurno(){
		pantallaServidor.write("Orden de jugadores:");
		for (int i = 0; i < threadsServidor.size(); i++) {
			ThreadServidor get = threadsServidor.get(i);
			get.getPersonajeEnTablero().setOrdenTurno(i);
			pantallaServidor.write(get.getNombreCliente());
		}
	}
	
	private void iniciarTableroParaCadaCliente(){
		try {
			for (ThreadServidor threadServidor : threadsServidor) {
				threadServidor.crearTablero(tablero); //crea tablero y poner a la primer persona con boton activado.
				threadServidor.getSalidaDatos().writeInt(1);
				threadServidor.getSalidaDatos().writeInt(threadServidor.getPersonajeEnTablero().getOrdenTurno());	
			}
		} catch (IOException ex) {System.out.println("Error en iniciarTableroParaCadaCliente en Servidor");}
	}
	
	private TipoCasilla getTipoDeCasilla(int num){
        switch (num){
            case 0:return  TipoCasilla.GATO;
            case 1:return  TipoCasilla.SOPA;
            case 2:return  TipoCasilla.PATH;
            case 3:return  TipoCasilla.MEMORIA;
            case 4:return  TipoCasilla.CATCHCAT;//"CAT";
            case 5:return  TipoCasilla.BOMBERMARIO;//"BOMBER";
            case 6:return  TipoCasilla.GUESSWHO;
            case 7:return  TipoCasilla.COINS;
            case 8:return  TipoCasilla.CARDS;
			case 9:return TipoCasilla.TUBO1;
            case 10:return TipoCasilla.TUBO1;
            case 11:return TipoCasilla.TUBO1;
            case 12:return  TipoCasilla.CARCEL;
            case 13:return TipoCasilla.ESTRELLA;
            case 14:return TipoCasilla.FUEGO;
            case 15:return TipoCasilla.HIELO;
            case 16:return TipoCasilla.COLA;
        }
        return null;
    }

	public PantallaServidor getPantallaServidor() {
		return pantallaServidor;
	}

	public void setPantallaServidor(PantallaServidor pantallaServidor) {
		this.pantallaServidor = pantallaServidor;
	}

	public int getMaxJugadores() {
		return maxJugadores;
	}

	public void setMaxJugadores(int maxJugadores) {
		this.maxJugadores = maxJugadores;
	}

	public ArrayList<Integer> getPersonajesParaEscoger() {
		return personajesParaEscoger;
	}

	public void setPersonajesParaEscoger(ArrayList<Integer> personajesParaEscoger) {
		this.personajesParaEscoger = personajesParaEscoger;
	}
    
	
	
	
}
