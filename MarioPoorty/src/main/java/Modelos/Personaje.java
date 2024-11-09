/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Proyecto Diseño
 */
public class Personaje implements Serializable {
	private boolean repetirTurno;
	private String nombre;
	private int numDeCasilla;
	private int ordenTurno;
	private int turnosPerdidos;
	private JButton fichaPersonaje;
	private ImageIcon imagenFicha;
	private boolean repetirJuego;
	
	public Personaje(String nombre, int numPersonaje){
		this.repetirTurno = false;
		this.nombre = nombre;
		this.numDeCasilla = 0; //casilla actual
		this.ordenTurno = -1; //Define el lugar de orden de turno que le tocó
		this.turnosPerdidos = 0; 
		this.imagenFicha = ponerImagen(numPersonaje);
	}
	
	
	public void avanzar(int cantidad){
		numDeCasilla += cantidad;
	}
	
	private ImageIcon ponerImagen(int numPersonaje){
		switch(numPersonaje){
			case 0: 
				return new ImageIcon(getClass().getResource("/Pics/PaperMarioTransparente78x100.png"));
			case 1:
				return new ImageIcon(getClass().getResource("/Pics/MrLTransparente67x100.png"));
			case 2: 
				return new ImageIcon(getClass().getResource("/Pics/PeachTransparente95x100.png"));
			case 3: 
				return new ImageIcon(getClass().getResource("/Pics/ToadTransparente62x100.png"));
			case 4: 
				return new ImageIcon(getClass().getResource("/Pics/YoshiTransparente86x100.png"));
			case 5: 
				return new ImageIcon(getClass().getResource("/Pics/LumaTransparente98x100.png"));
			case 6: 
				return new ImageIcon(getClass().getResource("/Pics/BowserTransparente100x100.png"));
			case 7: 
				return new ImageIcon(getClass().getResource("/Pics/DaisyTransparente84x100.png"));
			case 8: 
				return new ImageIcon(getClass().getResource("/Pics/RosalinaTransparente58x100.png"));
			case 9: 
				return new ImageIcon(getClass().getResource("/Pics/DimentioTransparente100x87.png"));
		}
		return null;
	}

	public int getOrdenTurno() {
		return ordenTurno;
	}

	public void setOrdenTurno(int ordenTurno) {
		this.ordenTurno = ordenTurno;
	}

	public ImageIcon getImagenFicha() {
		return imagenFicha;
	}

	public void setFichaPersonaje(JButton fichaPersonaje) {
		this.fichaPersonaje = fichaPersonaje;
	}

	public int getTurnosPerdidos() {
		return turnosPerdidos;
	}

	public void sumarTurnosPerdidos(int turnosPerdidos) {
		this.turnosPerdidos += turnosPerdidos;
	}
	
	public void restarTurnosPerdidos(int num){
		this.turnosPerdidos -= num;
	}

	public String getNombre() {
		return nombre;
	}

	public int getNumDeCasilla() {
		return numDeCasilla;
	}

	public void setNumDeCasilla(int numDeCasilla) {
		this.numDeCasilla = numDeCasilla;
	}

	public JButton getFichaPersonaje() {
		return fichaPersonaje;
	}

	public boolean isRepetirJuego() {
		return repetirJuego;
	}

	public void setRepetirJuego(boolean repetirJuego) {
		this.repetirJuego = repetirJuego;
	}
	
	
	
	
	
	
	
}


