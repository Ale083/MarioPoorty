 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

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
					} catch (Exception ex) {System.out.println("Error con caso tirarDados, señal 1 ThreadCliente");}
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
		//TODO: poner en el constructor de juego, set visible y ponerle nombre
	}
    
    private void tirarDados() throws Exception{ //espera a que la persona de la que es el turno, tire los dados.
		turnoActual = entradaDatos.readInt(); //coge el turno de la persona
			if(juego.getPersonajeQueJuega() != turnoActual){ //si no es el turno de x persona
				return;
			}
			//tablero set resultado dados TODO:
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
			return; //TODO QUITAR
		}
		else if(juego.isTuboUsado()){ //solo está realmente porque el caso de juego es el default, entonces se ocupa poner todas las otras opciones.
			juego.setTuboUsado(false);
			siguienteTurno();
			return;//TODO QUITAR
		}
		else if(juego.isCasillaFinal()){ //si alguien cayó en finish
			//TODO: lógica de gane.
		} 
		else if(juego.isJuegoEnProgreso()){ //si cayó en casilla de juego
			//TODO: lógica de juego.
		}
		else if(juego.isCasillaCola()){
			//TODO:
		}
		else if(juego.isCasillaFuego()){
			//TODO:
		}
		else if(juego.isCasillaHielo()){
			//TODO:
		}
//		else if TODO: Lo del tubo ver como sirve.
		siguienteTurno();
		
		
	}
	
	private void moverParaLosDemas() throws Exception{ //TODO: quitar actualizarJuego
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
	
	
	
	
	
	
	
	
	
	
	
}
