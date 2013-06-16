
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;


public class Game implements Observer, KeyListener
{
	private JFrame frame;
	private JLabel P1Score;
	private JLabel P2Score;
	private BoardPanel boardP;
	private Board board;
	private JPanel scorePanel;
	private JLabel scoreLabel;
	private JLabel turnIndicator;

	private static final int windowWidth = 610;
	private static final int windowHeight = 745;
	private static final int boardWidth = 500;
	private static final int boardHeight = 500;
	private JPanel Info_bar;
	private GamePanel panel;
	//	private JPanel turnIndicator;

	private static int difference = 25;

	private AI ai0;
	private AI ai1;
	private JPanel gameInfoPanel;
	private JLabel movesMadeLabel;
	private JLabel piecesLeftLabel;
	private JLabel pointsLeftLabel;
	private JPanel gameHistory;
	private JLabel lastMoveLabel;

	public Game ()
	{
		frame = new JFrame("NumChop");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(windowWidth, windowHeight);
		frame.getContentPane().setLayout(null);

		board = new Board();
		board.addObserver(this);

		//		Info_bar = new JPanel();
		//		Info_bar.setBounds(5, 6, 600, 44);
		//		frame.getContentPane().add(Info_bar);
		//		Info_bar.setLayout(null);

		Info_bar = new JPanel();
		Info_bar.setBounds(5, 6, 598, 72);
		Info_bar.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.getContentPane().add(Info_bar);
		Info_bar.setLayout(null);

		turnIndicator = new JLabel("Turn Indicator");
		turnIndicator.setBounds(250, 25, 222, 16);
		Info_bar.add(turnIndicator);

		scorePanel = new JPanel();
		scorePanel.setBounds(6, 6, 150, 56);
		Info_bar.add(scorePanel);
		scorePanel.setLayout(null);

		scoreLabel = new JLabel("Score:");
		scoreLabel.setBounds(6, 3, 61, 16);
		scorePanel.add(scoreLabel);

		//		P1Score = new JLabel("Player 1 Score: 00");
		//		P1Score.setForeground(Color.BLACK);
		//		P1Score.setFont(new Font("Arial", Font.BOLD, 16));
		//		P1Score.setHorizontalAlignment(SwingConstants.CENTER);
		//		P1Score.setBounds(6, 5, 150, 33);

		P1Score = new JLabel("Player 1: 000");
		P1Score.setBounds(6, 20, 120, 16);
		scorePanel.add(P1Score);

		//		Info_bar.add(P1Score);

		//		P2Score = new JLabel("Player 2 Score: 00");
		//		P2Score.setForeground(Color.BLACK);
		//		P2Score.setFont(new Font("Arial", Font.BOLD, 16));
		//		P2Score.setHorizontalAlignment(SwingConstants.CENTER);
		//		P2Score.setBounds(444, 5, 150, 33);

		P2Score = new JLabel("Player 2: 000");
		P2Score.setBounds(6, 38, 120, 16);
		scorePanel.add(P2Score);

		//		Info_bar.add(P2Score);

		//		turnIndicator = new JPanel();
		//		turnIndicator.setBackground(Color.WHITE);
		//		turnIndicator.setBounds(275, 0, 50, 44);
		//		Info_bar.add(turnIndicator);

		gameInfoPanel = new JPanel();
		gameInfoPanel.setBounds(474, 6, 118, 59);
		Info_bar.add(gameInfoPanel);
		gameInfoPanel.setLayout(null);

		movesMadeLabel = new JLabel("Moves Made: 0");
		movesMadeLabel.setBounds(6, 6, 109, 16);
		gameInfoPanel.add(movesMadeLabel);

		piecesLeftLabel = new JLabel("Tiles Left: 0");
		piecesLeftLabel.setBounds(6, 22, 109, 16);
		gameInfoPanel.add(piecesLeftLabel);

		pointsLeftLabel = new JLabel("Points Left: 0");
		pointsLeftLabel.setBounds(6, 38, 109, 16);
		gameInfoPanel.add(pointsLeftLabel);


		boardP = new BoardPanel(board);
		boardP.setBackground(Color.WHITE);
		boardP.setBounds(55, 100 + difference, boardWidth, boardHeight);

		frame.getContentPane().add(boardP);

		panel = new GamePanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(5, 80, 600, 600);

		frame.getContentPane().add(panel);

		gameHistory = new JPanel();
		gameHistory.setBounds(5, windowHeight - 58, 599, 26);
		gameHistory.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.getContentPane().add(gameHistory);
		gameHistory.setLayout(null);

		lastMoveLabel = new JLabel("Last Move Made");
		lastMoveLabel.setBounds(6, 6, 587, 16);
		gameHistory.add(lastMoveLabel);

		frame.setVisible(true);

		ai1 = new AI();
		ai0 = new AI();

		init();

	}

	public void init ()
	{
		P1Score.setText("Player 1 Score: 0");
		P2Score.setText("Player 2 Score: 0");


		board.init();

		//panel.repaint();
		boardP.repaint();

		lastMoveLabel.setForeground(Color.BLACK);
		lastMoveLabel.setText("Last Move Made");


		movesMadeLabel.setText(String.format("Moves Made: %3d", board.getMoves()));
		piecesLeftLabel.setText(String.format("Tiles Left: %2d", Board.NUM_PIECES_TOTAL - board.getConsumed()));
		pointsLeftLabel.setText(String.format("Points Left: %3d", board.getPointsLeft()));


//		long s = 0L;
//		while (true) 
//		{
//			String seed = JOptionPane.showInputDialog(frame, "Enter the value for seed:", "0");
//
//			try {
//				s = Long.parseLong(seed);
//			}
//			catch (NumberFormatException ex) 
//			{ continue;}
//			break;
//		}
//
//		//Get seed
//		board.setSeed(s);
//		boardP.repaint();

		//		// Ask user whether or not each player is a computer or human
		//		String[] playerType = {"Human", "Computer"};
		//		int player1Human = JOptionPane.showOptionDialog(frame,
		//				"Is Player 1 going to be a human or a computer?",
		//				"Player 1",
		//				JOptionPane.YES_NO_OPTION,
		//				JOptionPane.QUESTION_MESSAGE,
		//				null,
		//				playerType,
		//				playerType[0]);
		//
		//		// If user wants human, turn the AI 'off'
		//		if (0 == player1Human) {
		//			ai0 = null;
		//		}
		//		else {
		//			ai0 = new AI();
		//			ai0.set(board, Board.PLAYER_1);
		//		}
		//
		//		int player2Human = JOptionPane.showOptionDialog(frame,
		//				"Is Player 2 going to be a human or a computer?",
		//				"Player 2",
		//				JOptionPane.YES_NO_OPTION,
		//				JOptionPane.QUESTION_MESSAGE,
		//				null,
		//				playerType,
		//				playerType[0]);
		//
		//		// If user wants human, turn the AI 'off'
		//		if (0 == player2Human) {
		//			ai1 = null;
		//		}
		//		else {
		//			ai1 = new AI();
		//			ai1.set(board, Board.PLAYER_2);
		//		}
		//
		//		//Set turn
		//		String[] turnValues = { "Random", "Player 2", "Player 1"};
		//		int selectedValue = JOptionPane.showOptionDialog(frame,
		//				"Pick who will go first:",
		//				"Turn selection",
		//				JOptionPane.YES_NO_CANCEL_OPTION,
		//				JOptionPane.QUESTION_MESSAGE,
		//				null,
		//				turnValues, turnValues[2]);
		//
		//		int turn = 0;
		//
		//		if (selectedValue == 0)
		//			turn = (int)(Math.random()*2);
		//		else if (selectedValue == 1)
		//			turn = 1;
		//
		//		board.setTurn(turn);

//		//Set colors
//		String[] colorValues = {"Green", "Red"};
//		int selectedValue = JOptionPane.showOptionDialog(frame,
//				"Player " +(board.getTurn()+1) +", pick your color:",
//				"Select your color",
//				JOptionPane.YES_NO_CANCEL_OPTION,
//				JOptionPane.QUESTION_MESSAGE,
//				null,
//				colorValues, colorValues[1]);
//
//		board.setColors(1-selectedValue);

		/* *************************************************************** */

		GameStartPanel gsp = new GameStartPanel();

		long s = 0L;
		while (true) {
			JOptionPane.showOptionDialog(frame, gsp, "Game Info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

			String seed = gsp.getSeed();

			try {
				s = Long.parseLong(seed);
			}
			catch (NumberFormatException ex) 
			{ continue;}
			break;
		}

		// Set seed
		board.setSeed(s);
		boardP.repaint();

		boolean[] gameStartData = gsp.getData();

		// If player one is a human
		if (gameStartData[0]) {
			ai0 = null;
		}
		else {
			ai0 = new AI();
			ai0.set(board, Board.PLAYER_1);
		}

		// If player two is a human
		if (gameStartData[1]) {
			ai1 = null;
		}
		else {
			ai1 = new AI();
			ai1.set(board, Board.PLAYER_2);
		}

		// If player one is going first
		if (gameStartData[2]) {
			board.setTurn(Board.PLAYER_1);
		}
		else {
			board.setTurn(Board.PLAYER_2);
		}
		turnIndicator.setText("Player " + (board.getTurn() + 1) + "'s Turn");

		
		// Set colors
		if (gameStartData[3]) {
			board.setColors(0);
		}
		else {
			board.setColors(1);
		}
		/* *************************************************************** */


		turnIndicator.setForeground(board.getPlayerColor(board.getTurn()));

		int selectedValue;
		if (ai0 != null && ai0.getPlayer() == board.getTurn())
		{
			selectedValue = ai0.findBestStart();
			board.pickStart(selectedValue);
		}

		else if (ai1 != null && ai1.getPlayer() == board.getTurn())
		{
			selectedValue = ai1.findBestStart();
			board.pickStart(selectedValue);
		}

		//Notify user of AI's corner selection.

		else 
		{
			//pick corners 
			String[] cornerValues = {"A10 and J1", "A1 and J10"};
			selectedValue = JOptionPane.showOptionDialog(frame,
					"Player "+ (board.getTurn()+1) +", pick a set of corners:",
					"Select a corner",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					cornerValues, cornerValues[1]);

			board.pickStart(1-selectedValue);
		}

		boardP.repaint();

	}


	public static void main(String[] args) 
	{
		new Game();
	}


	@Override
	public void update(Observable o, Object arg) 
	{
		//Board b = (Board) o;

		boardP.repaint();

		int[] scores = board.getScores();
		P1Score.setText(String.format("Player 1 Score: %2d", scores[0]));
		P2Score.setText(String.format("Player 2 Score: %2d",scores[1]));

		movesMadeLabel.setText(String.format("Moves Made: %3d", board.getMoves()));
		piecesLeftLabel.setText(String.format("Tiles Left: %2d", Board.NUM_PIECES_TOTAL - board.getConsumed()));
		pointsLeftLabel.setText(String.format("Points Left: %3d", board.getPointsLeft()));

		turnIndicator.setForeground(board.getPlayerColor(board.getTurn()));
		turnIndicator.setText("Player " + (board.getTurn() + 1) + "'s Turn");

		if (board.getLastMove().equals(Board.NO_MOVES_MADE_YET)) {
			lastMoveLabel.setForeground(Color.BLACK);
		}
		else {
			lastMoveLabel.setForeground(board.getPlayerColor(1 - board.getTurn()));
		}
		lastMoveLabel.setText(board.getLastMove());

		//boardP.repaint();

		if (board.endOfGame())
		{
			board.setTurn(-1);
			turnIndicator.setForeground(Color.BLACK);
			turnIndicator.setText("Game Over");

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
			if (n == JOptionPane.YES_OPTION) {

				if (ai1 != null)
				{
					ai1.endThread();
				}

				if (ai0 != null)
				{
					ai0.endThread();
				}

				init();
				return;
			}
		}

		if (ai1 != null && (board.getTurn() == Board.PLAYER_2 || board.endOfGame()))
		{
			ai1.signalReady();
		}

		if (ai0 != null && (board.getTurn() == Board.PLAYER_1 || board.endOfGame()))
		{
			ai0.signalReady();
		}
	}

	private class GamePanel extends JPanel { 
		private BufferedImage image;

		@Override 
		public void paintComponent(Graphics g) { 
			super.paintComponents(g);

			if (image == null)
			{
				image = new BufferedImage(600, 600,
						BufferedImage.TYPE_INT_ARGB);
			}

			Graphics imageGraphics = image.getGraphics();
			imageGraphics.setColor(Color.BLACK);
			imageGraphics.setFont(new Font("Arial", Font.BOLD, 20));

			for (int r = 0; r < Board.MAX_RAND_TBL_ROW; ++r)
			{
				int x = 70;
				if (r == 9)
					x = 65;
				imageGraphics.drawString(""+(r+1), x + 50*r, 10 + difference);
				imageGraphics.drawString(""+(r+1), x + 50*r, 545 + difference);

			}

			for (int c = 0; c < Board.MAX_RAND_TBL_COL; ++c)
			{
				imageGraphics.drawString(""+(char)(c+'A'), 20, 50 + 50*c + difference);
				imageGraphics.drawString(""+(char)(c+'A'), 565, 50 + 50*c + difference);
			}


			g.drawImage(image, 0, 0, 600, 600, frame);
			setVisible(true);
		}
	}

	public void waitForPlayer(Location from, Location to) {
		lastMoveLabel.setForeground(board.getPlayerColor(board.getTurn()));
		lastMoveLabel.setText("Player " + (board.getTurn() + 1) + " wants to move from " + from + " to " + to + ". Press any key to continue.");
		for (int i = 0; i < (1 << 32); i++) 
			;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
