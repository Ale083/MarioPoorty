/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import Juegos.PantallaCoins;

/**
 *
 * @author Proyecto Diseño
 */
public class ThreadTiempoCoins extends Thread{
	private PantallaCoins pantalla;
	private boolean corriendo;
    private int sec;
    private int min;
	private boolean termino;
	
	public ThreadTiempoCoins(PantallaCoins pantalla){
		this.pantalla = pantalla;
		this.sec = elegirSegundos();
		this.min = 0;
		this.corriendo = true;
		this.termino = false;
	}
	
	public void run(){
		while(corriendo){
			sec-=1;
			if(sec==0 && min==0){
				corriendo = false;
				termino = true;
			} 
			else if(sec<0 && min>0){
				min-=1;
				sec=59;
			}
			pantalla.setCronometroTiempo(formatear(min) + ":" + formatear(sec));
			try {Thread.sleep(1000);} catch (InterruptedException ex) {}
		}
	}

	private String formatear(int num){
		if(num<10){
			return "0" + num;
		} else{
			return Integer.toString(num);
		}
	}
	
	public boolean isTermino() {
		return termino;
	}

	public void parar() {
		this.corriendo = false;
	}
	
	private int elegirSegundos(){
		int num = Random.randomInt(1, 3);
		if(num==1){
			return 30;
		} else if(num==2){
			return 45;
		} else{
			return 60;
		}
	}
}
