package Juegos;

import Modelos.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
* @author Alejandro Umaña y Luis Diego Rodriguez
*/

public class BomberMarioGUI extends JFrame {
    private static final int MAX_SIZE = 20;  // Tamaño máximo del tablero
    private static final int MIN_SIZE = 10;  // Tamaño mínimo del tablero
    private static final int BOMBS = 7;  // Cantidad inicial de bombas
    private static final int MAX_TREASURE = 4; // Número de tesoros en el tablero
    private int boardSize;
    private int[][] board;
    private int[][] treasurePositions;
    private int bombsLeft;
    private boolean gameOver;
	private boolean jugando;
	private boolean perdio;

    private JButton[][] buttons;

    private int score;
    private long startTime;
    private Timer gameTimer;
    
    public BomberMarioGUI() {
        // Configuración inicial del juego
        this.boardSize = Random.randomInt(MIN_SIZE, MAX_SIZE);
        this.board = new int[boardSize][boardSize];
        this.treasurePositions = new int[MAX_TREASURE][2];  // El tesoro ocupa 4 casillas
        this.bombsLeft = BOMBS;
        this.gameOver = false;
        this.score = 0;
		this.jugando = true;

        // Configuración de la ventana principal
        setTitle("Bomber Mario");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de juego
        JPanel gamePanel = new JPanel(new GridLayout(boardSize, boardSize));
        buttons = new JButton[boardSize][boardSize];

        // Creación de los botones de la interfaz
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
                final int x = i, y = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!gameOver) {
                            placeBomb(x, y);
                            updateBoard();
                            checkForTreasure();
                        }
                    }
                });
                gamePanel.add(buttons[i][j]);
            }
        }

        add(gamePanel, BorderLayout.CENTER);
        add(createInfoPanel(), BorderLayout.SOUTH);
        placeTreasure();

        // Iniciar cronómetro
        startTime = System.currentTimeMillis();
        startGameTimer();

        setVisible(true);
    }

    // Método para colocar el tesoro en el tablero de forma aleatoria
    private void placeTreasure() {
        Random rand = new Random();
        for (int i = 0; i < MAX_TREASURE; i++) {
            int x =	Random.randomInt(0, boardSize-1);
            int y = Random.randomInt(0, boardSize-1);
            treasurePositions[i][0] = x;
            treasurePositions[i][1] = y;
            board[x][y] = 1;  // Marcar casillas con tesoro
        }
    }

    // Método para colocar una bomba en una casilla
    private void placeBomb(int x, int y) {
        if (bombsLeft > 0) {
            bombsLeft--;
            board[x][y] = 2;  // Marca la casilla con bomba
            score += 10; // Aumenta la puntuación al colocar una bomba
            JOptionPane.showMessageDialog(this, "¡Bomba colocada en (" + x + ", " + y + ")");
        } else {
            JOptionPane.showMessageDialog(this, "No tienes más bombas.");
        }
    }

    // Verificar si el tesoro ha sido descubierto
    private void checkForTreasure() {
        boolean treasureFound = true;
        for (int i = 0; i < MAX_TREASURE; i++) {
            int x = treasurePositions[i][0];
            int y = treasurePositions[i][1];
            if (board[x][y] != 2) {  // Si una casilla del tesoro no ha sido marcada con bomba
                treasureFound = false;
            }
        }

        if (treasureFound) {
            gameOver = true;
            long endTime = System.currentTimeMillis();
            long elapsedTime = (endTime - startTime) / 1000;  // Tiempo en segundos
            JOptionPane.showMessageDialog(this, "¡Has encontrado todo el tesoro! Ganaste. Tiempo: " + elapsedTime + " segundos.");
        }
    }

    // Actualizar el tablero con los elementos del juego (bombas, tesoro)
    private void updateBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 1) {
                    buttons[i][j].setBackground(Color.YELLOW);  // Casillas del tesoro
                } else if (board[i][j] == 2) {
                    buttons[i][j].setBackground(Color.RED);  // Casillas con bomba
                } else {
                    buttons[i][j].setBackground(Color.LIGHT_GRAY);  // Casillas vacías
                }
            }
        }
    }

    // Crear el panel de información en la parte inferior (con estadísticas del juego)
    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        JLabel bombsLabel = new JLabel("Bombas restantes: " + bombsLeft);
        infoPanel.add(bombsLabel);

        JLabel scoreLabel = new JLabel("Puntuación: " + score);
        infoPanel.add(scoreLabel);

        JLabel timerLabel = new JLabel("Tiempo: 0");
        infoPanel.add(timerLabel);

        return infoPanel;
    }

    // Iniciar el temporizador del juego
    private void startGameTimer() {
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimeDisplay();
            }
        });
        gameTimer.start();
    }
    private void updateTimeDisplay() {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        ((JLabel) ((JPanel) getContentPane().getComponent(1)).getComponent(2)).setText("Tiempo: " + elapsedTime);
    }
	
    private void showGameDetails() {
        JOptionPane.showMessageDialog(this, "Bienvenido a Bomber Mario\n" +
                "Objetivo: Coloca bombas en el tablero para encontrar el tesoro oculto.\n" +
                "Las bombas y el tesoro son generados aleatoriamente.");
    }
    private void animateExplosion(int x, int y) {
        buttons[x][y].setBackground(Color.RED);
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttons[x][y].setBackground(Color.LIGHT_GRAY);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
	
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
			java.util.logging.Logger.getLogger(PantallaCards.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(PantallaCards.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(PantallaCards.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(PantallaCards.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new BomberMarioGUI().setVisible(true);
			}
		});
	}

	public boolean isJugando() {
		return jugando;
	}
	
	
	public boolean isPerdio(){
		return perdio;
	}
}
