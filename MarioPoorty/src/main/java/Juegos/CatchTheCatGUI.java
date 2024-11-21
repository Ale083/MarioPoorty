package Juegos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/*
* @author Alejandro Umaña y Luis Diego Rodriguez
*/

public class CatchTheCatGUI extends JFrame {
    private static final int SIZE = 11;
    private boolean[][] board;
    private int catX, catY;
    private int moveCount;
    private int maxMoves;
    private long startTime;
    private Timer gameTimer;
    private boolean gameOver;
	private boolean jugando;
	private boolean perdio;

    public CatchTheCatGUI() {
        setSize(600, 600);
        setLayout(new BorderLayout());
        
        // Inicialización de variables
        this.board = new boolean[SIZE][SIZE];
        this.catX = SIZE / 2;
        this.catY = SIZE / 2;
        this.moveCount = 0;
        this.maxMoves = SIZE * SIZE;
        this.gameOver = false;
		this.jugando = true;

        // Crear panel de juego
        JPanel gamePanel = new JPanel(new GridLayout(SIZE, SIZE));
        JButton[][] buttons = new JButton[SIZE][SIZE];

        // Crear botones para representar cada casilla
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
                int x = i, y = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        blockCell(x, y);
                        updateBoard(buttons);
                        moveCount++;
                        if (!moveCat()) {
                            JOptionPane.showMessageDialog(null, "¡El gato está atrapado! ¡Ganaste!");
							perdio = false;
                            endGame();
                        } else if (catX == 0 || catX == SIZE - 1 || catY == 0 || catY == SIZE - 1) {
                            JOptionPane.showMessageDialog(null, "¡El gato escapó! ¡Perdiste!");
							perdio = true;
                            endGame();
                        } else if (moveCount >= maxMoves) {
                            JOptionPane.showMessageDialog(null, "¡Se agotaron los movimientos! ¡Perdiste!");
							perdio = true;
                            endGame();
                        }
						updateBoard(buttons);
                    }
                });
                gamePanel.add(buttons[i][j]);
            }
        }

        add(gamePanel, BorderLayout.CENTER);
        add(createInfoPanel(), BorderLayout.SOUTH);
        updateBoard(buttons);
        startTimer();

        setVisible(true);
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        }, 1000, 1000);
    }

    private void updateTime() {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        setTitle("Catch the Cat - Tiempo: " + elapsedTime + "s");
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());

        JLabel moveCountLabel = new JLabel("Movimientos: " + moveCount + "/" + maxMoves);
        infoPanel.add(moveCountLabel);

        JButton restartButton = new JButton("Reiniciar");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        infoPanel.add(restartButton);

        return infoPanel;
    }

    private void blockCell(int x, int y) {
        if (x >= 0 && x < SIZE && y >= 0 && y < SIZE && !board[x][y]) {
            board[x][y] = true;
        }
    }

    private boolean moveCat() {
        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };
        int bestX = catX, bestY = catY;
        int shortestDistance = SIZE;

        for (int[] dir : directions) {
            int newX = catX + dir[0];
            int newY = catY + dir[1];

            if (newX >= 0 && newX < SIZE && newY >= 0 && newY < SIZE && !board[newX][newY]) {
                int distanceToEdge = Math.min(Math.min(newX, SIZE - 1 - newX), Math.min(newY, SIZE - 1 - newY));
                if (distanceToEdge < shortestDistance && !board[newX][newY]) {
                    bestX = newX;
                    bestY = newY;
                    shortestDistance = distanceToEdge;
                }
            }
        }

        if (bestX == catX && bestY == catY) {
            return false;
        }

        catX = bestX;
        catY = bestY;
        return true;
    }

    private void updateBoard(JButton[][] buttons) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (i == catX && j == catY) {
                    buttons[i][j].setBackground(Color.ORANGE); // Color del gato
                } else if (board[i][j]) {
                    buttons[i][j].setBackground(Color.BLACK); // Casillas bloqueadas
                } else {
                    buttons[i][j].setBackground(Color.LIGHT_GRAY); // Casillas libres
                }
            }
        }
    }

    private void endGame() {
		jugando = false;
        gameOver = true;
        gameTimer.cancel();
		this.setVisible(false);
    }

    private void restartGame() {
        this.board = new boolean[SIZE][SIZE];
        this.catX = SIZE / 2;
        this.catY = SIZE / 2;
        this.moveCount = 0;
        this.gameOver = false;
        this.startTime = System.currentTimeMillis();
        updateBoard(new JButton[SIZE][SIZE]);
        gameTimer.cancel();
        startTimer();
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
				new CatchTheCatGUI().setVisible(true);
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
