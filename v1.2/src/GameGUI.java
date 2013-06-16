import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.KeyStroke;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JLabel;


public class GameGUI {

	private JFrame frame;
	private BoardPanel boardP;
	private Board board;


	private static final int windowWidth = 610;
	private static final int windowHeight = 686;
	private static final int boardWidth = 500;
	private static final int boardHeight = 500;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameGUI window = new GameGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GameGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("NumChop");
		frame.setResizable(false);
		frame.setSize(windowWidth, windowHeight);
		frame.getContentPane().setLayout(null);
		
		JPanel infoBar = new JPanel();
		infoBar.setBounds(6, 6, 598, 72);
		infoBar.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.getContentPane().add(infoBar);
		infoBar.setLayout(null);
		
		JLabel turnIndicator = new JLabel("Turn Indicator");
		turnIndicator.setBounds(184, 46, 222, 16);
		infoBar.add(turnIndicator);
		
		JPanel scorePanel = new JPanel();
		scorePanel.setBounds(6, 6, 91, 56);
		infoBar.add(scorePanel);
		scorePanel.setLayout(null);
		
		JLabel scoreLabel = new JLabel("Score:");
		scoreLabel.setBounds(6, 3, 61, 16);
		scorePanel.add(scoreLabel);
		
		JLabel player1ScoreLabel = new JLabel("Player 1: 000");
		player1ScoreLabel.setBounds(6, 20, 81, 16);
		scorePanel.add(player1ScoreLabel);
		
		JLabel player2ScoreLabel = new JLabel("Player 2: 000");
		player2ScoreLabel.setBounds(6, 38, 81, 16);
		scorePanel.add(player2ScoreLabel);
		
		JPanel gameInfoPanel = new JPanel();
		gameInfoPanel.setBounds(474, 6, 118, 59);
		infoBar.add(gameInfoPanel);
		gameInfoPanel.setLayout(null);
		
		JLabel movesMadeLabel = new JLabel("Moves Made: 000");
		movesMadeLabel.setBounds(6, 6, 109, 16);
		gameInfoPanel.add(movesMadeLabel);
		
		JLabel piecesLeftLabel = new JLabel("Pieces Left: 000");
		piecesLeftLabel.setBounds(6, 22, 109, 16);
		gameInfoPanel.add(piecesLeftLabel);
		
		JLabel pointsLeftLabel = new JLabel("Points Left: 000");
		pointsLeftLabel.setBounds(6, 38, 109, 16);
		gameInfoPanel.add(pointsLeftLabel);
		
		JPanel gameBoard = new JPanel();
		gameBoard.setBounds(6, 90, 598, 531);
		gameBoard.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.getContentPane().add(gameBoard);
		gameBoard.setLayout(null);
		
		JPanel gameHistory = new JPanel();
		gameHistory.setBounds(6, 633, 599, 26);
		gameHistory.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.getContentPane().add(gameHistory);
		gameHistory.setLayout(null);
		
		JLabel lastMoveLabel = new JLabel("Last Move Made");
		lastMoveLabel.setBounds(6, 6, 587, 16);
		gameHistory.add(lastMoveLabel);
	}

	
}