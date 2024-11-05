/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Servidor;

/*
* @author Alejandro Umaña y Luis Diego Rodriguez
*/
public class PantallaServidor extends javax.swing.JFrame {
	public Servidor servidor;

	public PantallaServidor() {
		initComponents();
		servidor=new Servidor(this);
	}


	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txaServidor = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txaServidor.setEditable(false);
        txaServidor.setBackground(new java.awt.Color(0, 0, 0));
        txaServidor.setColumns(20);
        txaServidor.setForeground(new java.awt.Color(255, 255, 255));
        txaServidor.setRows(5);
        jScrollPane1.setViewportView(txaServidor);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
	
	
	public void write(String text){
        this.txaServidor.append(text + "\n");
    }
	
	
	public static void main(String args[]) {
		PantallaServidor pantalla = new PantallaServidor();
		pantalla.setVisible(true);
		pantalla.servidor.run();
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txaServidor;
    // End of variables declaration//GEN-END:variables
}
