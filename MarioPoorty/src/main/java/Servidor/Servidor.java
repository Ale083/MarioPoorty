package Servidor;

import Modelos.TipoCasilla;
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
	private ArrayList<TipoCasilla> casillas;
	private int conectados;
	private int maxJugadores;
	
    public Servidor(PantallaServidor pantallaServidor){
        this.pantallaServidor = pantallaServidor;
		this.clientes = new ArrayList<Socket>();
		this.threadsServidor= new ArrayList<ThreadServidor>();
		this.personajesParaEscoger = new ArrayList<Integer>();
		ponerPersonajesDisponibles();
		this.casillas= new ArrayList<TipoCasilla>();
		cargarCasillas();
		conectados = 0;
		maxJugadores = 6;
    }
	
	public void run(){
		try {
			servidor = new ServerSocket(50000);
		} catch (IOException ex) {System.out.println("Error levantando el servidor");}
		
		pantallaServidor.write("Servidor levantado");
		pantallaServidor.write("Esperando usuarios");
		
		while(conectados<maxJugadores){ //hasta que no hayan max personajes
			try {
				clientes.add(servidor.accept()); //aqui se queda esperando a que alguien se meta y lo mete al arraylist de clientes
			} catch (IOException ex) {System.out.println("Error conectando cliente");}
			
			pantallaServidor.write("Cliente número " + (conectados+1) + " conectado");
			threadsServidor.add(new ThreadServidor(clientes.get(conectados),this,++conectados));
			threadsServidor.get(conectados).start();
			
		}
	}
    
	private void ponerPersonajesDisponibles(){
        for (int i = 0; i < 10; i++) {
            personajesParaEscoger.add(i); //Pone del 0-9 en el ArrayList de disponibles, su respectivo numero de posicion, es decir [0,1,2,3,4...]
        }
    }
	
	private void cargarCasillas(){
        for (int i = 0; i < 9; i++) { //para apuntar a cada juego
            casillas.add(getTipoDeCasilla(i)); 
            casillas.add(getTipoDeCasilla(i)); //Mete 2 de gato, 2 de sopa ...
        }
        for(int i=9;i<17;i++)                
            casillas.add(getTipoDeCasilla(i)); //Mete 1 de las demás casillas
        
        Collections.shuffle(casillas);
        int numDeTubo=1;
        for (int i = 0; i < casillas.size(); i++) { // recorre cada casilla
            if(casillas.get(i).equals(TipoCasilla.TUBO1)){
				if(numDeTubo == 1){
					casillas.remove(i); 
					casillas.add(i, TipoCasilla.TUBO1); 
					numDeTubo++;
				} else if(numDeTubo == 2){
					casillas.remove(i); 
					casillas.add(i, TipoCasilla.TUBO2); 
					numDeTubo++;
				} else if(numDeTubo == 3){
					casillas.remove(i); 
					casillas.add(i, TipoCasilla.TUBO3); 
					numDeTubo++;
				}
				
            }
        }
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
			case 9:return TipoCasilla.TUBO1;//CORREGIR ESTE CODIGO
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
    
	
	
}
