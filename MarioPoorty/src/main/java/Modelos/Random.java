package Modelos;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Proyecto Diseño
 */
public class Random {
	
	public static int randomInt(int min, int max){
		return (min + (int)(Math.random() * ((max - min) + 1)));
	}
	
	public static boolean randomBoolean(){
		if(Math.random() > 0.5){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean probabilidad(int prob){
		if (randomInt(0,100) < prob){
			return true;
		} else {
			return false;
		}
	}
}
