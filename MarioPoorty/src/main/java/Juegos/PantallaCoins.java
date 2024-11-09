/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Juegos;

import Modelos.Random;
import Modelos.ThreadTiempoCoins;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Proyecto Diseño
 */
public class PantallaCoins extends javax.swing.JFrame implements ActionListener{
	private JButton[][] coins = new JButton[25][25];
	private int[][] vMonedas = new int[25][25];
	ImageIcon coinIcon = new ImageIcon(getClass().getResource("/Pics/coin.png"));
	private int total;
	private ThreadTiempoCoins threadTiempoCoins;
	private boolean jugando;
	private int pos = 313;
	private int neg = 312;
	private boolean perdio;
	
	public PantallaCoins() {
		initComponents();
		ponerBotones();
		jugando = false;
		threadTiempoCoins = new ThreadTiempoCoins(this);
		threadTiempoCoins.start();
	}
	
	public void setCronometroTiempo(String text){
		lblTiempo.setText(text);
		if(threadTiempoCoins.isTermino()){
			if(total>0){
				JOptionPane.showMessageDialog(null, "Ganaste!", "Winner",1);
				perdio = false;
			} else{
				JOptionPane.showMessageDialog(this, "Perdiste","Loser", 0);
				perdio = true;
			}
            jugando=false;
            this.setVisible(false);
		}
	}
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblPuntos = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblTiempo = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        pnlTablero = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblPuntos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setText("Puntos");

        lblTiempo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setText("Tiempo");

        pnlTablero.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnlTableroLayout = new javax.swing.GroupLayout(pnlTablero);
        pnlTablero.setLayout(pnlTableroLayout);
        pnlTableroLayout.setHorizontalGroup(
            pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlTableroLayout.setVerticalGroup(
            pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 519, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(145, 145, 145)
                                .addComponent(lblPuntos, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(178, 178, 178)
                                .addComponent(jLabel2)))
                        .addGap(113, 113, 113)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel4)))
                        .addGap(0, 154, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlTablero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTablero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPuntos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(PantallaCoins.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(PantallaCoins.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(PantallaCoins.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(PantallaCoins.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new PantallaCoins().setVisible(true);
			}
		});
	}

	private void ponerBotones(){
		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 25; j++) {
				coins[i][j] = new JButton(coinIcon);
				JButton boton = coins[i][j];
				pnlTablero.add(boton);
				boton.setBounds(5+20*i,5+20*j,20,20);
				boton.addActionListener(this);
				if(Random.randomBoolean()){
					if(pos==0){
						vMonedas[i][j] = Random.randomInt(-10, 0);
					} else{
						vMonedas[i][j] = Random.randomInt(0, 10);
						pos--;
					}
				} else{
					if(neg==0){
						vMonedas[i][j] = Random.randomInt(0, 10);
					} else{
						vMonedas[i][j] = Random.randomInt(-10, 0);
						neg--;
					}
				}
				vMonedas[i][j] = Random.randomInt(-10, 10);
			}
		}
	}

	public boolean isJugando() {
		return jugando;
	}

	public boolean isPerdio() {
		return perdio;
	}
	
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblPuntos;
    private javax.swing.JLabel lblTiempo;
    private javax.swing.JPanel pnlTablero;
    // End of variables declaration//GEN-END:variables

	@Override
	public void actionPerformed(ActionEvent evt) {
		JButton boton = (JButton)evt.getSource();
		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 25; j++) {
				if(boton == coins[i][j]){
					total += vMonedas[i][j];
					boton.setEnabled(false);
					boton.setBackground(null);
					lblPuntos.setText(total + "");
				}
			}
		}
	}
}
