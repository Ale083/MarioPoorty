/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Tablero;

import Modelos.Personaje;

/**
 *
 * @author Proyecto Diseño
 */
public class ElegirCasillasNum extends javax.swing.JPanel {
	Personaje personajeJugando;
	/**
	 * Creates new form ElegirCasillasNum
	 */
	public ElegirCasillasNum(Personaje personajeJugando) {
		initComponents();
		this.personajeJugando = personajeJugando;
		insertarACbx(personajeJugando);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(137, 137, 137)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(156, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void insertarACbx(Personaje personajeJugando){
		int casillaActual = personajeJugando.getNumDeCasilla();
		int index = 0;
		if(casillaActual <= 3){
			for (int i = casillaActual*-1; i <= 3; i++) {
				jComboBox1.addItem(Integer.toString(i));
				if(i==0)
					jComboBox1.setSelectedIndex(index);
				index++;
			}
		}
		else if(casillaActual >= 24){
			for (int i = -3; i <= 27-casillaActual; i++) {
				jComboBox1.addItem(Integer.toString(i));
				if(i==0)
					jComboBox1.setSelectedIndex(3);
			}
		}
		else{
			for (int i = -3; i <= 3; i++) {
				jComboBox1.addItem(Integer.toString(i));
				if(i==0)
					jComboBox1.setSelectedIndex(3);
			}
		}
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox1;
    // End of variables declaration//GEN-END:variables
	
	public int getNumEscogido(){
		return Integer.parseInt((String)jComboBox1.getSelectedItem());
	}


}


