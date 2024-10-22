/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mariopoorty;
import Cliente.PantallaCliente;
import Servidor.PantallaServidor;
/**
 *
 * @author Proyecto Diseño
 */
public class MarioPoorty {

    public static void main(String[] args) {
        System.out.println("Hello World!");
		System.out.println("Adios mundo");
		new PantallaServidor().setVisible(true);
		new PantallaCliente().setVisible(true);
		new PantallaCliente().setVisible(true);
		
    }
}
