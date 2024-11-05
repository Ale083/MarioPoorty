/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mariopoorty;
import Cliente.PantallaCliente;
import Servidor.PantallaServidor;
import javax.swing.SwingUtilities;
/**
 *
 * @author Proyecto Diseño
 */
public class MarioPoorty {

    public static void main(String[] args) {
		//funciones lambda para hacer threads que inician las pantallas
		
		new Thread(() -> {
            PantallaServidor pantalla = new PantallaServidor();
			pantalla.setVisible(true);
			pantalla.servidor.run();
        }).start();
		
		
		try {Thread.sleep(500);} catch (InterruptedException ex) {}
		
		for (int i = 0; i < 2; i++) {
			new Thread(() -> {
				new PantallaCliente().setVisible(true);
			}).start();
			try {Thread.sleep(6000);} catch (InterruptedException ex) {}
		}
    }
}
