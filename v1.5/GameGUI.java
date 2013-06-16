import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.KeyStroke;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JLabel;


public class GameGUI implements Observer {

	private JFrame frame;
	private BoardPanel boardP;
	private Board board;
	
	private JPanel infoBar;
	private JPanel gameInfoPanel;
	private JPanel gameBoard;
	private JPanel gameHistory;
	private JPanel scorePanel;

	private JLabel turnIndicator;
	private JLabel scoreLabel;
	private JLabel player1ScoreLabel;
	private JLabel player2ScoreLabel;
	private JLabel movesMadeLabel;
	private JLabel piecesLeftLabel;
	private JLabel pointsLeftLabel;
	private JLabel lastMoveLabel;

	private AI ai0;
	private AI ai1;

	private static final int windowWidth = 610;
	private static final int windowHeight = 686;
	private static final int boardWidth = 500;
	private static final int boardHeight = 500;
	private static BufferedImage image;
	

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
		
		ai1 = new AI();
		ai0 = new AI();
		
		initializeGame();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("NumChop");
		frame.setResizable(false);
		frame.setSize(windowWidth, windowHeight);
		frame.getContentPane().setLayout(null);
		
		board = new Board();
		board.addObserver(this);
		
		infoBar = new JPanel();
		infoBar.setBounds(6, 6, 598, 72);
		infoBar.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.getContentPane().add(infoBar);
		infoBar.setLayout(null);
		
		turnIndicator = new JLabel("Turn Indicator");
		turnIndicator.setBounds(184, 46, 222, 16);
		infoBar.add(turnIndicator);
		
		scorePanel = new JPanel();
		scorePanel.setBounds(6, 6, 91, 56);
		infoBar.add(scorePanel);
		scorePanel.setLayout(null);
		
		scoreLabel = new JLabel("Score:");
		scoreLabel.setBounds(6, 3, 61, 16);
		scorePanel.add(scoreLabel);
		
		player1ScoreLabel = new JLabel("Player 1: 000");
		player1ScoreLabel.setBounds(6, 20, 81, 16);
		scorePanel.add(player1ScoreLabel);
		
		player2ScoreLabel = new JLabel("Player 2: 000");
		player2ScoreLabel.setBounds(6, 38, 81, 16);
		scorePanel.add(player2ScoreLabel);
		
		gameInfoPanel = new JPanel();
		gameInfoPanel.setBounds(474, 6, 118, 59);
		infoBar.add(gameInfoPanel);
		gameInfoPanel.setLayout(null);
		
		movesMadeLabel = new JLabel("Moves Made: 000");
		movesMadeLabel.setBounds(6, 6, 109, 16);
		gameInfoPanel.add(movesMadeLabel);
		
		piecesLeftLabel = new JLabel("Pieces Left: 000");
		piecesLeftLabel.setBounds(6, 22, 109, 16);
		gameInfoPanel.add(piecesLeftLabel);
		
		pointsLeftLabel = new JLabel("Points Left: 000");
		pointsLeftLabel.setBounds(6, 38, 109, 16);
		gameInfoPanel.add(pointsLeftLabel);
		
		/* ****** Copy and Pasted code here, need to test ******/
		boardP = new BoardPanel(board);
		boardP.setBackground(Color.WHITE);
		boardP.setBounds(55, 100, boardWidth, boardHeight);
		
		frame.getContentPane().add(boardP);
		
		/* ****************************************************/
		
		gameBoard = new JPanel();
		gameBoard.setBounds(6, 90, 598, 531);
		gameBoard.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.getContentPane().add(gameBoard);
		gameBoard.setLayout(null);
		
		gameHistory = new JPanel();
		gameHistory.setBounds(6, 633, 599, 26);
		gameHistory.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.getContentPane().add(gameHistory);
		gameHistory.setLayout(null);
		
		lastMoveLabel = new JLabel("Last Move Made");
		lastMoveLabel.setBounds(6, 6, 587, 16);
		gameHistory.add(lastMoveLabel);
	}

	public void initializeGame()
	{
		//ai1.set(board, Board.PLAYER_2);
		//ai0.set(board, Board.PLAYER_1);
		
		board.init();
		drawPanel(gameBoard, frame);
		boardP.repaint();
		
		long s = 0L;
		while (true) 
		{
			String seed = JOptionPane.showInputDialog(frame, "Enter the value for seed:", "0");
			
			try {
				s = Long.parseLong(seed);
			}
			catch (NumberFormatException ex) 
			{ continue;}
			break;
		}
		
		//Get seed
		board.setSeed(s);
		boardP.repaint();
		
		//Set turn
		String[] turnValues = { "Random", "Player 2", "Player 1"};
		int selectedValue = JOptionPane.showOptionDialog(frame,
				"Pick who will go first:",
				"Turn selection",
				JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    turnValues, turnValues[2]);
		
		int turn = 0;
		
		if (selectedValue == 0)
			turn = (int)(Math.random()*2);
		else if (selectedValue == 1)
			turn = 1;
		
		board.setTurn(turn);
		

		//Set colors
		String[] colorValues = {"Green", "Red"};
		selectedValue = JOptionPane.showOptionDialog(frame,
				"Player " +(board.getTurn()+1) +", pick your color:",
				"Select your color",
				JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    colorValues, colorValues[1]);
		
		board.setColors(1-selectedValue);
		
		turnIndicator.setBackground(board.getPlayerColor(board.getTurn()));
		
		
		//pick corners 
		String[] cornerValues = {"A10 and J1", "A1 and J10"};
		selectedValue = JOptionPane.showOptionDialog(frame,
				"Player "+ (turn+1) +", pick a set of corners:",
				"Select a corner",
				JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    cornerValues, cornerValues[1]);
		
		board.pickStart(1-selectedValue);
		boardP.repaint();
		
	}
	
	public static void drawPanel(JPanel panel, JFrame frame)
	{
		if (image == null)
		{
			image = new BufferedImage(600, 600,
					BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.getGraphics();
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			
			for (int r = 0; r < Board.MAX_RAND_TBL_ROW; ++r)
			{
				int x = 70;
				if (r == 9)
					x = 65;
				g.drawString(""+(r+1), x + 50*r, 35);
				g.drawString(""+(r+1), x + 50*r, 580);
				
			}
			
			for (int c = 0; c < Board.MAX_RAND_TBL_COL; ++c)
			{
				g.drawString(""+(char)(c+'A'), 20, 80 + 50*c);
				g.drawString(""+(char)(c+'A'), 565, 80 + 50*c);
					
			}
			panel.getGraphics().drawImage(image, 0, 0, 600, 600, frame);
		}
	}
	

	public void update(Observable o, Object arg) 
	{
		Board b = (Board) o;
		int[] scores = b.getScores();
		player1ScoreLabel.setText(String.format("Player 1 Score: %02d", scores[0]));
		player2ScoreLabel.setText(String.format("Player 2 Score: %02d",scores[1]));
		
		turnIndicator.setBackground(board.getPlayerColor(board.getTurn()));
		
		
		boardP.repaint();
		
		if (b.endOfGame())
		{
			b.setTurn(-1);
			
			String print = "";
			if (scores[0] > scores[1])
			{
				print = "Player 1 wins with a score of " +scores[0];
			}
			else if (scores[1] > scores[0])
			{
				print = "Player 2 wins with a score of " +scores[1];
			}
			else
			{
				print = "It is a draw!";
			}
			
			int n = JOptionPane.showConfirmDialog(frame,
				    print +"\nWould you like to play again?",
				    "Game Over!",
				    JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION)
				initializeGame();
		}
		
		if (board.getTurn() == Board.PLAYER_2)
		{
			//ai1.ready.release();
		}
		else if (board.getTurn() == Board.PLAYER_1)
		{
			//ai0.ready.release();
		}
		
		boardP.repaint();
		
	}
}