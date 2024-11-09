 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import Juegos.PantallaCards;
import Juegos.PantallaCoins;
import Juegos.PantallaGato;
import Juegos.PantallaGuess;
import Juegos.PantallaMemory;
import Juegos.PantallaPath;
import Juegos.PantallaSopa;
import Tablero.Juego;
import Modelos.TipoCasilla;
import Modelos.Personaje;
import Tablero.DadosTurnoPrincipio;
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
public class ThreadCliente extends Thread{
	private DataInputStream entradaDatos;
	private DataOutputStream salidaDatos;
    private ObjectInputStream entradaObjetos;
    private ObjectOutputStream salidaObjetos;
	private PantallaCliente pantallaCliente;
	private DadosTurnoPrincipio dadosTurnoPrincipio;
	private String nombre;
	private int turnoActual;
	private boolean hayJuego;
	private boolean juegoWin;
	private ArrayList<Personaje> personajes;
	private ArrayList<TipoCasilla> casillas;
	private Juego juego;
	
	

    public ThreadCliente(DataInputStream entradaDatos,DataOutputStream salidaDatos, ObjectInputStream entradaObjetos, ObjectOutputStream salidaObjetos, PantallaCliente pantallaCliente, String nombre){
		this.entradaDatos = entradaDatos;
		this.salidaDatos = salidaDatos;
		this.entradaObjetos = entradaObjetos;
		this.salidaObjetos = salidaObjetos;
		this.pantallaCliente = pantallaCliente;
		this.nombre = nombre;
		this.hayJuego = false;
		this.juegoWin = false;
		
    }
    
    public void run(){
		
		try {inicioTirarDados();}
		catch (Exception ex) {System.out.println("Error en InicioTirarDados en ThreadCliente");}
		
		int evento = -1;
		
		while(true){
			try {
				evento = entradaDatos.readInt();
			} catch (IOException ex) {System.out.println("Error con entrada de evento en threadCliente");}
			switch(evento){
				case 0:
					try {
						crearTablero();
						break;
					} catch (Exception ex) {System.out.println("Error con caso crearTablero, señal 0 ThreadCliente");}
					
				case 1: 
					try {
						tirarDados();//espera a que la persona tire dados y luego hace la acción de la casilla
						break;
					} catch (Exception ex) {System.out.println("Error con caso tirarDados, señal 1 ThreadCliente");ex.printStackTrace();}
				case 2:
					try {
						moverContrincante();//espera a que la persona tire dados y luego hace la acción de la casilla
						break;
					} catch (Exception ex) {System.out.println("Error con caso moverContrincante, señal 2 ThreadCliente");}
				case 3:
					try {
						enemigoRepite();//Actualiza turno hasta tener el turno de la persona que recién le dio de nuevo, porque esa persona está repitiendo
						break;
					} catch (Exception ex) {System.out.println("Error con caso moverContrincante, señal 3 ThreadCliente");}
				case 4:
					try {
						alguienGano();//Termina el juego para la gente
						break;
					} catch (Exception ex) {System.out.println("Error con caso alguienGano, señal 4 ThreadCliente");}
				case 5:
					try {
						moverEnemigoFuego();//Mueve al principio al enemigo afectado por fuego
						break;
					} catch (Exception ex) {System.out.println("Error con caso moverEnemigoFuego, señal 5 ThreadCliente");}
				case 6:
					try {
						abrirGatoComoOponente();//Alguien más cayó en gato y fui escogido como oponente.
						break;
					} catch (Exception ex) {System.out.println("Error con caso abrirGatoComoOponente, señal 6 ThreadCliente");ex.printStackTrace();}
				case 7:
					try {
						abrirCardsComoOponente();//Alguien más cayó en cards y fui escogido como oponente.
						break;
					} catch (Exception ex) {System.out.println("Error con caso abrirCardsComoOponente, señal 7 ThreadCliente");}
			}
		}
        
    }
	
	private void inicioTirarDados() throws Exception{
		dadosTurnoPrincipio = new DadosTurnoPrincipio();
		while(true){
			JOptionPane.showInternalMessageDialog( null, dadosTurnoPrincipio.getPanel(), nombre, HIDE_ON_CLOSE);
			int resDados = dadosTurnoPrincipio.getResultado();
			if(resDados != -1){ //si tiró los dados va a ser algo más que -1
				pantallaCliente.write("El resultado de mis dados fue de " + resDados);
				salidaDatos.writeInt(0); //señal de evento para cambiar PUESTOCAMBIAR
				salidaDatos.writeInt(resDados); //resDados sería el resultado de los dados.
				break;
			}
		}
	}
    
    private void crearTablero() throws Exception{
		personajes = (ArrayList<Personaje>)entradaObjetos.readObject();
		casillas = (ArrayList<TipoCasilla>)entradaObjetos.readObject();
		juego = new Juego(personajes,casillas);
		juego.setVisible(true);
		juego.setTitle(nombre);
	}
    
    private void tirarDados() throws Exception{ //espera a que la persona de la que es el turno, tire los dados.
		turnoActual = entradaDatos.readInt(); //coge el turno de la persona
			if(juego.getPersonajeQueJuega() != turnoActual){ //si no es el turno de x persona
				return;
			}
			while(juego.btnDadosEnabled()){ //se queda esperando a que tire los dados
				sleep(500);
			}
			accionCasilla(); //ver que acción procede depende de la casilla que cayó.
	}
    
	private void accionCasilla() throws Exception{
		if(juego.isRepetirTurno()){
			moverParaLosDemas();
			juego.setRepetirTurno(false);
			salidaDatos.writeInt(2);
			salidaDatos.writeInt(turnoActual);
			ponerMiTurnoDeNuevo(turnoActual);
			salidaDatos.writeInt(3); //pone a la misma persona a jugar DeNuevo
			return; 
		}
		else if(juego.isTuboUsado()){ //solo está realmente porque el caso de juego es el default, entonces se ocupa poner todas las otras opciones.
			juego.setTuboUsado(false);
			siguienteTurno();
			return;
		}
		else if(juego.isCasillaFinal()){ //si alguien cayó en finish / ganó
			salidaDatos.writeInt(5);
			salidaDatos.writeUTF(nombre);
			juego.setVisible(false);
			JOptionPane.showMessageDialog(null, nombre + " ganó el juego", "Winning Screen", 1);
		} 
		else if(juego.isCasillaCola()){
			juego.setCasillaCola(false);
			siguienteTurno();
			return;
		}
		else if(juego.isCasillaFuego()){
			while(juego.getContrincante()==null){
				sleep(500);
			}
			salidaDatos.writeInt(6);  
			salidaDatos.writeInt(juego.getContrincante().getOrdenTurno());
			juego.finEventoFuego();
			juego.setContrincante(null);
		}
		else if(juego.isCasillaHielo()){
			juego.finEventoFuego();
			juego.setContrincante(null);
		}
		else if(juego.isJuegoEnProgreso()){ //si cayó en casilla de juego
			caiEnJuego(juego.getJuegoActual());
			juego.setJuegoEnProgreso(false);
		}
		siguienteTurno();
		
		
	}
	
	private void moverParaLosDemas() throws Exception{ 
		salidaDatos.writeInt(1);
		salidaDatos.writeInt(juego.getPersonaJugando().getNumDeCasilla());
		salidaDatos.writeInt(juego.getTurnoDelCliente());	
		
	}
	
	private void moverContrincante() throws Exception{
		int casillaEspecifica = entradaDatos.readInt();
		int turnoDePersonajeQueMueve = entradaDatos.readInt();
		juego.moverContrincante(casillaEspecifica, turnoDePersonajeQueMueve);
	}
	
	private void enemigoRepite() throws Exception{
		int repetirTurno = entradaDatos.readInt();
		while(juego.getPersonajeQueJuega() != repetirTurno){
			juego.siguienteRonda();
		}
		System.out.println("ESTO DEBERIA SIEMPRE IMRPIMIR FALSE" + juego.isRepetirTurno());
		//TODO: y bororar arriba: setRepetirTurno(false);
	}
	
	private void ponerMiTurnoDeNuevo(int repetirTurno) throws Exception{
		while(juego.getPersonajeQueJuega() != repetirTurno){
			juego.siguienteRonda();
		}
	}
	
	private void siguienteTurno() throws Exception{
		moverParaLosDemas();
		salidaDatos.writeInt(4);
	}
	
	private void alguienGano() throws Exception{
		String winnerNombre = entradaDatos.readUTF();
		JOptionPane.showMessageDialog(null, winnerNombre + " ganó el juego", "Winning Screen", 1);
		juego.setVisible(false);
	}
	
	private void moverEnemigoFuego() throws Exception{
		int turnoDeEnemigo = entradaDatos.readInt();
		juego.moverPersonajeAlPrincipio(turnoDeEnemigo);
	}
	
	private void caiEnJuego(TipoCasilla casilla) throws Exception{
		Personaje personaje = personajes.get(turnoActual);
		this.hayJuego = true;
		switch(casilla){
			case GUESSWHO:
				juegoGuess(personaje);
				break;
			case PATH:
				juegoPath(personaje);
				break;
			case SOPA:
				juegoSopa(personaje);
				break;
			case MEMORIA:
//				juegoMemoria(personaje);
				hayJuego = false; //TODOQUITAR
				break;
			case CARDS:
				juegoCards(personaje);
				break;
			case COINS:
				juegoCoins(personaje);
				break;
			case GATO:
				juegoGato(personaje);
				System.out.println("aaa" + personaje.isRepetirJuego());
				break;
			case BOMBERMARIO:
				//
				break;
			case CATCHCAT:
				//
				break;
		}
	}
	
	
	
	
	//MÉTODOS PARA JUEGOS:
	private void juegoGuess(Personaje personaje) throws Exception{
		PantallaGuess pantalla = new PantallaGuess();
		pantalla.setVisible(true);
		pantalla.setTitle(nombre);
		pantalla.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		while(pantalla.isJugando()){
			sleep(500);
		}
		personaje.setRepetirJuego(pantalla.isPerdio());
		hayJuego = false;
	}
	
	private void juegoPath(Personaje personaje) throws Exception{
		PantallaPath pantalla = new PantallaPath();
		pantalla.setVisible(true);
		pantalla.setTitle(nombre);
		pantalla.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		while(pantalla.isJugando()){
			sleep(500);
		}
		personaje.setRepetirJuego(pantalla.isPerdio());
		hayJuego = false;
	}
	
	private void juegoSopa(Personaje personaje) throws Exception{
		PantallaSopa pantalla = new PantallaSopa();
		pantalla.setVisible(true);
		pantalla.setTitle(nombre);
		pantalla.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		while(pantalla.isJugando()){
			sleep(500);
		}
		personaje.setRepetirJuego(pantalla.isPerdio());
		hayJuego = false;
	}
	
	private void juegoCoins(Personaje personaje) throws Exception{
		PantallaCoins pantalla = new PantallaCoins();
		pantalla.setVisible(true);
		pantalla.setTitle(nombre);
		pantalla.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		while(pantalla.isJugando()){
			sleep(500);
		}
		personaje.setRepetirJuego(pantalla.isPerdio());
		hayJuego = false;
	}
	
	private void juegoGato(Personaje personaje) throws Exception{
		salidaDatos.writeInt(7);
		int turnoOponente = entradaDatos.readInt();
		String nombreOponente = "";
		for (Personaje p : personajes) {
			if(p.getOrdenTurno() == turnoOponente){
				nombreOponente = p.getNombre();
				break;
			}
		}
		PantallaGato pantalla = new PantallaGato();
		pantalla.setPersona(1);
		pantalla.setTitle(nombre);
		pantalla.setDefaultCloseOperation(HIDE_ON_CLOSE);
		pantalla.setVisible(true);
		
		int c;
		int f;
		boolean gane;
		
		while(pantalla.isJugando()){
			gatoJugar(pantalla, nombreOponente);
			if(pantalla.isLleno()){
				JOptionPane.showInternalMessageDialog(null, "Empate, cuenta como gane", nombre, 1);
				personaje.setRepetirJuego(false);
				pantalla.terminar();
				break;
			}
			gane = entradaDatos.readBoolean();
			c = entradaDatos.readInt();
			f = entradaDatos.readInt();
			if(gane){
				JOptionPane.showInternalMessageDialog(null, "Ganaste", nombre, 1);
				personaje.setRepetirJuego(false);
				pantalla.terminar();
				break;
			}
			System.out.println(nombre + " " + c + " " + f);
			pantalla.ponerEnGUI(c, f);
			if(pantalla.revisarGane()){//si gano el oponente
				JOptionPane.showInternalMessageDialog(null, "Perdiste", nombre, 0);
				personaje.setRepetirJuego(true);
				pantalla.terminar();
				salidaDatos.writeInt(8);
				salidaDatos.writeBoolean(true);
				salidaDatos.writeInt(pantalla.getColumnaJugada());
				salidaDatos.writeInt(pantalla.getFilaJugada());
				salidaDatos.writeUTF(nombreOponente);
				sleep(1000);
				break;
			}
		}
		hayJuego = false;
		pantalla.setVisible(false);
		System.out.println("JUEGO TERMINADO");
	}
	
	private void abrirGatoComoOponente() throws Exception{
		String nombreOponente = entradaDatos.readUTF();
		PantallaGato pantalla = new PantallaGato();
		pantalla.setPersona(2);
		pantalla.setTitle(nombre);
		pantalla.setDefaultCloseOperation(HIDE_ON_CLOSE);
		pantalla.setVisible(true);
		while(true){
			System.out.println("ca1");
			if(recibirTurnoOponente(pantalla,nombreOponente)){
				break;
			}
			System.out.println("ca2");
			gatoJugar(pantalla, nombreOponente);
			System.out.println("ca3");
			if(pantalla.isLleno()){
				break;
			}
			
		}
		System.out.println("terimno popoente");
		pantalla.setVisible(false);
	}
	
	private void gatoJugar(PantallaGato pantalla, String nombreOponente) throws Exception{
		while(pantalla.getColumnaJugada() == -1 || pantalla.getFilaJugada() == -1){
			sleep(500);
		} //espera a que presione algo.
		System.out.println("pantalla.getColumnaJugada()" + pantalla.getColumnaJugada());
		System.out.println("pantalla.getFilaJugada()" + pantalla.getFilaJugada());
		salidaDatos.writeInt(8);
		salidaDatos.writeBoolean(false);
		salidaDatos.writeInt(pantalla.getColumnaJugada());
		salidaDatos.writeInt(pantalla.getFilaJugada());
		salidaDatos.writeUTF(nombreOponente);
		pantalla.reiniciarColFila();
	}
	
	private boolean recibirTurnoOponente(PantallaGato pantalla, String nombreOponente) throws Exception{
		boolean gano;
		int f;
		int c;
		gano = entradaDatos.readBoolean();
		c = entradaDatos.readInt();
		f = entradaDatos.readInt();
		if(gano) return true;
		System.out.println(c + " caca " + f);
		pantalla.ponerEnGUI(c,f);//375
		pantalla.reiniciarColFila();
		if(pantalla.revisarGane()){ //revisar si después de esa jugada de P1 ganó
			salidaDatos.writeInt(8);
			salidaDatos.writeBoolean(true);
			salidaDatos.writeInt(pantalla.getColumnaJugada());
			salidaDatos.writeInt(pantalla.getFilaJugada());
			salidaDatos.writeUTF(nombreOponente);
			return true;
		} else {
			return false;
		}
	}
	
	private void juegoMemoria(Personaje personaje) throws Exception{
		salidaDatos.writeInt(7);
		int turnoOponente = entradaDatos.readInt();
		String nombreOponente = "";
		for (Personaje p : personajes) {
			if(p.getOrdenTurno() == turnoOponente){
				nombreOponente = p.getNombre();
				break;
			}
		}
		PantallaMemory pantalla = new PantallaMemory();
//		pantalla.setPersona(1);
		pantalla.setTitle(nombre);
		pantalla.setDefaultCloseOperation(HIDE_ON_CLOSE);
		pantalla.setVisible(true);
	}
	
	private void juegoCards(Personaje personaje) throws Exception{
		PantallaCards pantalla = new PantallaCards();
		pantalla.setVisible(true);       
        pantalla.setTitle(nombre);
        pantalla.setDefaultCloseOperation(HIDE_ON_CLOSE);
        
        while(!pantalla.cartaElegida()){
            sleep(500);
        }
		salidaDatos.writeInt(9);
		ArrayList<Integer> numeros = new ArrayList<Integer>();
		ArrayList<Integer> tipos = new ArrayList<Integer>();
		for (int i = 0; i < personajes.size()-1; i++) {
			numeros.add(entradaDatos.readInt());
			tipos.add(entradaDatos.readInt());
		}
		sleep(2000);
		if(checkCartas(numeros, tipos, pantalla.getCartaNum(), pantalla.getCartaType())){
			JOptionPane.showMessageDialog(null, "ganaste", "", 1);
			personaje.setRepetirJuego(false);
		} else {
			JOptionPane.showMessageDialog(null, "perdiste", "", 0);
			personaje.setRepetirJuego(true);
		}
		hayJuego = false;
	}
	
	private void abrirCardsComoOponente() throws Exception{
		String contrincanteNombre = entradaDatos.readUTF();
		PantallaCards pantalla = new PantallaCards();
		pantalla.setVisible(true);       
        pantalla.setTitle(nombre);
        pantalla.setDefaultCloseOperation(HIDE_ON_CLOSE);
        
        while(!pantalla.cartaElegida()){
            sleep(500);
        }
		
		salidaDatos.writeInt(10);
		salidaDatos.writeUTF(contrincanteNombre);
		salidaDatos.writeInt(pantalla.getCartaNum());
		salidaDatos.writeInt(pantalla.getCartaType());
	}
	
	private boolean checkCartas(ArrayList<Integer> numL, ArrayList<Integer> typeL, int num, int type){
		for (int i = 0; i < numL.size(); i++) {
			if(numL.get(i) > num){
				return false;
			} else if ( numL.get(i) == num){
				if(typeL.get(i) == type){
					return false;
				}
			}
		}
		return true;
	}
}

