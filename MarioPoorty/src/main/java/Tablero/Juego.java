/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Tablero;

import Modelos.Personaje;
import Modelos.Random;
import Modelos.TipoCasilla;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.HIDE_ON_CLOSE;

/**
 *
 * @author Proyecto Diseño
 */
public class Juego extends javax.swing.JFrame {
	private final int TAMANOBOTONPIXELES = 60;
	private final int CASILLASENTABLERO = 28;
	private	JButton casillasGUI[] = new JButton[CASILLASENTABLERO];
	private int personajeQueJuega; //Indicador del turno de quien es.
	private int casillasAvanzar;
	private int turnoDelCliente; //es el num de turno de la persona del juego por decir asi.
	private boolean tuboUsado;
	private ArrayList<Personaje> personajes;
	private ArrayList<TipoCasilla> tablero;
	private Personaje contrincante;
	private boolean repetirTurno;
	private boolean casillaHielo;
	private boolean casillaFuego;
	private boolean casillaCola;
	private boolean casillaFinal;
	private Personaje personaJugando;
	private boolean juegoEnProgreso;
	private TipoCasilla juegoActual;
	
	/**
	 * Creates new form Juego
	 */
	public Juego(ArrayList<Personaje> personajes, ArrayList<TipoCasilla> tablero) {
		initComponents();
		this.personajes = personajes;
		this.tablero = tablero;
		iniciarFichasPersonajes();
		iniciarTablero();
	}

	private void iniciarFichasPersonajes(){
		for (int i = 0; i < personajes.size(); i++) {
			Personaje get = personajes.get(i);
			turnoDelCliente = get.getOrdenTurno();
			JButton ficha = new JButton(String.valueOf(i+1));
			ficha.setIcon(get.getImagenFicha());
			pnlTablero.add(ficha);
			if(i<3){
				ficha.setBounds(5,10*i+5,30,20);
			} else{
				ficha.setBounds(35,10*(i-3)+5,30,20);
			}
			get.setFichaPersonaje(ficha);
		}
	}
	
	private void iniciarTablero(){
		for (int i = 0; i < casillasGUI.length; i++) {
			casillasGUI[i] = new JButton();
			JButton casillaGUI = casillasGUI[i];
			casillaGUI.setFont(new Font("Arial", Font.PLAIN, 8));
			pnlTablero.add(casillaGUI);
			casillaGUI.setEnabled(false);
			
			if(i < 9){ //crea 9, arriba 
				casillaGUI.setBounds((TAMANOBOTONPIXELES * i) + 5, 5, TAMANOBOTONPIXELES,TAMANOBOTONPIXELES);
				casillaGUI.setText(tablero.get(i).name());
			}
			
			else if(i>=9 && i<17){ //crea 8 más a la derecha
				casillaGUI.setBounds(485, (TAMANOBOTONPIXELES * (i-8))+5,TAMANOBOTONPIXELES,TAMANOBOTONPIXELES);
				casillaGUI.setText(tablero.get(i).name());
			}
			
			else if(i>=17 && i<25){//crea 7 más abajo
				casillaGUI.setBounds(485 - (TAMANOBOTONPIXELES * (i-16)) , 485, TAMANOBOTONPIXELES,TAMANOBOTONPIXELES);
				casillaGUI.setText(tablero.get(i).name());
			}
			
			else{
				casillaGUI.setBounds(5,485-(TAMANOBOTONPIXELES * (i-24)),TAMANOBOTONPIXELES,TAMANOBOTONPIXELES);
				casillaGUI.setText(tablero.get(i).name());
			}
		}
		
		for (Personaje personaje : personajes) {
			if(personaje.getOrdenTurno() == personajeQueJuega){
				btnTirarDados.setEnabled(true);
			} else {
				btnTirarDados.setEnabled(false);
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTablero = new javax.swing.JPanel();
        lblTurno = new javax.swing.JLabel();
        txfSenaladorTurno = new javax.swing.JTextField();
        txfResultadoDados = new javax.swing.JTextField();
        btnTirarDados = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlTablero.setBackground(new java.awt.Color(255, 102, 204));
        pnlTablero.setBorder(new javax.swing.border.MatteBorder(null));

        javax.swing.GroupLayout pnlTableroLayout = new javax.swing.GroupLayout(pnlTablero);
        pnlTablero.setLayout(pnlTableroLayout);
        pnlTableroLayout.setHorizontalGroup(
            pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );
        pnlTableroLayout.setVerticalGroup(
            pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );

        lblTurno.setBackground(new java.awt.Color(0, 204, 204));
        lblTurno.setFont(new java.awt.Font("Courier New", 0, 18)); // NOI18N
        lblTurno.setForeground(new java.awt.Color(0, 0, 0));
        lblTurno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTurno.setText("Turno de:");
        lblTurno.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTurno.setOpaque(true);

        txfSenaladorTurno.setEditable(false);
        txfSenaladorTurno.setFont(new java.awt.Font("Courier New", 0, 18)); // NOI18N
        txfSenaladorTurno.setForeground(new java.awt.Color(0, 0, 0));
        txfSenaladorTurno.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfSenaladorTurno.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txfSenaladorTurno.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txfSenaladorTurno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfSenaladorTurnoActionPerformed(evt);
            }
        });

        txfResultadoDados.setEditable(false);
        txfResultadoDados.setFont(new java.awt.Font("Courier New", 0, 36)); // NOI18N
        txfResultadoDados.setForeground(new java.awt.Color(0, 0, 0));
        txfResultadoDados.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfResultadoDados.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txfResultadoDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfResultadoDadosActionPerformed(evt);
            }
        });

        btnTirarDados.setBackground(new java.awt.Color(255, 51, 51));
        btnTirarDados.setFont(new java.awt.Font("Courier New", 0, 18)); // NOI18N
        btnTirarDados.setForeground(new java.awt.Color(0, 0, 0));
        btnTirarDados.setText("Tirar Dados");
        btnTirarDados.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnTirarDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTirarDadosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlTablero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTurno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txfSenaladorTurno))
                        .addGap(9, 9, 9))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(txfResultadoDados, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(btnTirarDados, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lblTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txfSenaladorTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(162, 162, 162)
                .addComponent(txfResultadoDados, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTirarDados, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(102, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlTablero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txfSenaladorTurnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfSenaladorTurnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfSenaladorTurnoActionPerformed

    private void txfResultadoDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfResultadoDadosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfResultadoDadosActionPerformed

    private void btnTirarDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTirarDadosActionPerformed
        int num1;
		int num2;
		
		for (Personaje personaje : personajes) { //itera en la lista de personajes y busca el turno de quien es, lo guarda en personaJugando.
			if(personaje.getOrdenTurno()==personajeQueJuega){
				personaJugando = personaje;
				break;
			}
		}
		System.out.println("repetir " + personaJugando.isRepetirJuego());
		if(personaJugando.isRepetirJuego()){
			personaJugando.setRepetirJuego(false);
			txfResultadoDados.setText("" + 0);
			jugarCasilla(personaJugando);
			siguienteRonda();
			return;
		}
		
		if(personaJugando.getTurnosPerdidos()> 0){ //Si tiene x cantidad de turnos perdidos:
			personaJugando.restarTurnosPerdidos(1);
			casillasAvanzar = 0;
			txfResultadoDados.setText("P" + personaJugando.getTurnosPerdidos());
			siguienteRonda(); // actualiza el turno, pone el turno de quien es y le pone el boton activado.
			return;
		}
		
		num1 = Random.randomInt(1, 6);
		num2 = Random.randomInt(1, 6);
		
		if(num1 == 6 && num2 == 6){
			personaJugando.sumarTurnosPerdidos(2);
			casillasAvanzar = 0;
			txfResultadoDados.setText("P" + personaJugando.getTurnosPerdidos());
			siguienteRonda();
			return;
		}
		
		if(num1 == 6 || num2 == 6){
			personaJugando.sumarTurnosPerdidos(1);
			casillasAvanzar = 0;
			txfResultadoDados.setText("P" + personaJugando.getTurnosPerdidos());
			siguienteRonda();
			return;
		}
		
		//else
		casillasAvanzar = num1+num2;
		txfResultadoDados.setText("" + casillasAvanzar);
		moverPersonajeLogico(true, personaJugando, casillasAvanzar);
		moverEnGUI(personaJugando);
		siguienteRonda();
		
    }//GEN-LAST:event_btnTirarDadosActionPerformed

	public void siguienteRonda(){
		personajeQueJuega++; //actualizar el turno como tal
		if(personajeQueJuega == personajes.size()){
			personajeQueJuega = 0;
		}
		
		for (Personaje personaje : personajes) { //Como el personaje del cliente está al final de "personajes", entonces es la ultima iteración la que determina si el boton está enabled o no.
			//Las demás iteraciones son para poner en el textbox de quien es el turno.
			if(personaje.getOrdenTurno()==personajeQueJuega){
				btnTirarDados.setEnabled(true);
				txfSenaladorTurno.setText(personaje.getNombre());
			}else{
				btnTirarDados.setEnabled(false);
			}
		}
	}
	
	private void moverPersonajeLogico(boolean jugarCasilla, Personaje personaJugando, int casillasAvanzar){
		int casilla = personaJugando.getNumDeCasilla() + casillasAvanzar;
		if(casilla > CASILLASENTABLERO){ //osea si se pasa.
			casilla = CASILLASENTABLERO - (casilla - CASILLASENTABLERO)-2;
		}
		if(casilla == 28){
			casilla = 26;
		}
		personaJugando.setNumDeCasilla(casilla);
		if(jugarCasilla){
			jugarCasilla(personaJugando);
		}
	}
	
	private void moverEnGUI(Personaje personaJugando){
		Point coords = casillasGUI[personaJugando.getNumDeCasilla()].getLocation();
		for (int i = 0; i < personajes.size(); i++) {
			if(personajes.get(i) == personaJugando){
				if(i<3){
					personaJugando.getFichaPersonaje().setLocation((int)coords.getX(), ((int)coords.getY()) + 10*i);
				} else{
					personaJugando.getFichaPersonaje().setLocation((int)coords.getX()+30, ((int)coords.getY()) + 10*(i-3));
				}
				return;
			}	
		}
	}
	
	private void jugarCasilla(Personaje personaJugando){
		TipoCasilla eventoDeCasilla = tablero.get(personaJugando.getNumDeCasilla());
		System.out.println(eventoDeCasilla.name());
		casillasGUI[personaJugando.getNumDeCasilla()].setBackground(Color.green);
		switch(eventoDeCasilla){
			case TipoCasilla.HIELO:
				casillaHielo = true;
				btnTirarDados.setEnabled(false);
				contrincante = elegirContrincante();
				contrincante.sumarTurnosPerdidos(2);
				break;
			case TipoCasilla.FUEGO:
				casillaFuego = true;
				btnTirarDados.setEnabled(false);
				contrincante = elegirContrincante();
				contrincante.setNumDeCasilla(0);
				moverEnGUI(contrincante);
				break;
			case TipoCasilla.ESTRELLA:
				repetirTurno = true;
				break;
			case TipoCasilla.COLA:
				casillaCola = true;
				btnTirarDados.setEnabled(false);
				int avance = elegirCasillasAvanzarRetroceder();
				personaJugando.setNumDeCasilla(personaJugando.getNumDeCasilla() + avance);
				casillasAvanzar += avance;
				break;
			case TipoCasilla.CARCEL:
				personaJugando.sumarTurnosPerdidos(2);
				break;
			case TipoCasilla.TUBO1:
				tuboUsado=true;
				personaJugando.setNumDeCasilla(encontrarNumCasilla(TipoCasilla.TUBO2));
				break;
			case TipoCasilla.TUBO2:
				tuboUsado=true;
				personaJugando.setNumDeCasilla(encontrarNumCasilla(TipoCasilla.TUBO3));
				break;
			case TipoCasilla.TUBO3:
				tuboUsado=true;
				personaJugando.setNumDeCasilla(encontrarNumCasilla(TipoCasilla.TUBO1));
				break;
			case TipoCasilla.INICIO:
				break; 
			case TipoCasilla.FINAL:
				casillaFinal = true;
				break;
			default: //en caso de un juego/
				if(personaJugando.getTurnosPerdidos() > 0){ //Si la persona no movió, (porque perdió turno) entonces no juega el juego en el que está.
					break; 
				}
				juegoEnProgreso = true;
				juegoActual = eventoDeCasilla;
				break;
		}
	}
	
	private int encontrarNumCasilla(TipoCasilla casilla){
		for (int i = 0; i < tablero.size(); i++) {
			if(tablero.get(i) == casilla){
				return i;
			}
		}
		return 0;
	}
	
	public void moverContrincante(int casillaEspecifica, int turnoDePersonajeQueMueve){
		for (Personaje personaje : personajes) {
			if(personaje.getOrdenTurno() == turnoDePersonajeQueMueve){
				personaje.setNumDeCasilla(casillaEspecifica);
				moverEnGUI(personaje);
				siguienteRonda();
				return;
			}
		}
	}
	
	public void moverPersonajeAlPrincipio(int turnoDePersonajeQueMueve){
		for (Personaje personaje : personajes) {
			if(personaje.getOrdenTurno() == turnoDePersonajeQueMueve){
				personaje.setNumDeCasilla(0);
				moverEnGUI(personaje);
				return;
			}
		}
	}
	
	private Personaje elegirContrincante(){
		ElegirPersonajeAtacar pantallaElegirAtacar = new ElegirPersonajeAtacar(personaJugando,personajes);
		
		while(pantallaElegirAtacar.getEnemigoElegido() == null){
			JOptionPane.showInternalMessageDialog(null, pantallaElegirAtacar, "Elija el personaje a atacar", HIDE_ON_CLOSE);
			try {Thread.sleep(500);} catch (InterruptedException ex) {}
		}
		return pantallaElegirAtacar.getEnemigoElegido();
	}
	
	private int elegirCasillasAvanzarRetroceder(){
		ElegirCasillasNum pantallaElegirNum = new ElegirCasillasNum(personaJugando);
		JOptionPane.showInternalMessageDialog(null, pantallaElegirNum, "Elija las casillas para retroceder/avanzar", HIDE_ON_CLOSE); //no hace falta while, el num por default es 0.
		
		return pantallaElegirNum.getNumEscogido();
	}

	public void setRepetirTurno(boolean repetirTurno) {
		this.repetirTurno = repetirTurno;
	}

	public void setCasillaHielo(boolean casillaHielo) {
		this.casillaHielo = casillaHielo;
	}

	public void setCasillaFuego(boolean casillaFuego) {
		this.casillaFuego = casillaFuego;
	}

	public void setCasillaCola(boolean casillaCola) {
		this.casillaCola = casillaCola;
	}

	public void setCasillaFinal(boolean casillaFinal) {
		this.casillaFinal = casillaFinal;
	}
	
	

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTirarDados;
    private javax.swing.JLabel lblTurno;
    private javax.swing.JPanel pnlTablero;
    private javax.swing.JTextField txfResultadoDados;
    private javax.swing.JTextField txfSenaladorTurno;
    // End of variables declaration//GEN-END:variables

	public int getPersonajeQueJuega() {
		return personajeQueJuega;
	}

	public boolean btnDadosEnabled() {
		return btnTirarDados.isEnabled();
	}

	public boolean isCasillaHielo() {
		return casillaHielo;
	}

	public boolean isCasillaFuego() {
		return casillaFuego;
	}

	public boolean isCasillaCola() {
		return casillaCola;
	}

	public boolean isCasillaFinal() {
		return casillaFinal;
	}

	public boolean isRepetirTurno() {
		return repetirTurno;
	}

	public boolean isJuegoEnProgreso() {
		return juegoEnProgreso;
	}

	public int getCasillasAvanzar() {
		return casillasAvanzar;
	}

	public int getTurnoDelCliente() {
		return turnoDelCliente;
	}

	public boolean isTuboUsado() {
		return tuboUsado;
	}

	public void setTuboUsado(boolean tuboUsado) {
		this.tuboUsado = tuboUsado;
	}

	public Personaje getPersonaJugando() {
		return personaJugando;
	}

	public Personaje getContrincante() {
		return contrincante;
	}

	public void setContrincante(Personaje contrincante) {
		this.contrincante = contrincante;
	}
	
	public void finEventoFuego(){
		this.casillaFuego = false;
	}
	
	public void finEventoHielo(){
		this.casillaHielo = false;
	}

	public TipoCasilla getJuegoActual() {
		return juegoActual;
	}

	public void setJuegoEnProgreso(boolean juegoEnProgreso) {
		this.juegoEnProgreso = juegoEnProgreso;
	}

}
