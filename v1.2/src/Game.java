import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;


public class Game implements Observer
{
	private JFrame frame;
	private static BufferedImage image;
	private JLabel P1Score;
	private JLabel P2Score;
	private BoardPanel boardP;
	private Board board;

	private static final int windowWidth = 610;
	private static final int windowHeight = 680;
	private static final int boardWidth = 500;
	private static final int boardHeight = 500;
	private JPanel Info_bar;
	private JPanel panel;
	private JPanel turnIndicator;
	
	private AI ai0;
	private AI ai1;
	
	
	public Game ()
	{
		frame = new JFrame("NumChop");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(windowWidth, windowHeight);
		frame.getContentPane().setLayout(null);
		
		board = new Board();
		board.addObserver(this);
		
		Info_bar = new JPanel();
		Info_bar.setBounds(5, 6, 600, 44);
		frame.getContentPane().add(Info_bar);
		Info_bar.setLayout(null);
		
		P1Score = new JLabel("Player 1 Score: 00");
		P1Score.setForeground(Color.BLACK);
		P1Score.setFont(new Font("Arial", Font.BOLD, 16));
		P1Score.setHorizontalAlignment(SwingConstants.CENTER);
		P1Score.setBounds(6, 5, 150, 33);
		
		Info_bar.add(P1Score);
		
		P2Score = new JLabel("Player 2 Score: 00");
		P2Score.setForeground(Color.BLACK);
		P2Score.setFont(new Font("Arial", Font.BOLD, 16));
		P2Score.setHorizontalAlignment(SwingConstants.CENTER);
		P2Score.setBounds(444, 5, 150, 33);
		
		Info_bar.add(P2Score);
		
		turnIndicator = new JPanel();
		turnIndicator.setBackground(Color.WHITE);
		turnIndicator.setBounds(275, 0, 50, 44);
		Info_bar.add(turnIndicator);
		
		
		boardP = new BoardPanel(board);
		boardP.setBackground(Color.WHITE);
		boardP.setBounds(55, 100, boardWidth, boardHeight);
		
		frame.getContentPane().add(boardP);
		
		panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(5, 50, 600, 600);
		
		frame.getContentPane().add(panel);
		
		frame.setVisible(true);
		
		ai1 = new AI(board);
		ai0 = new AI(board);
		init();
		
	}
	
	public void init ()
	{
		P1Score.setText("Player 1 Score: 00");
		P2Score.setText("Player 2 Score: 00");
		
		board.init();
		drawPanel(panel, frame);
		boardP.repaint();
		
		long s = 0L;
		while (true) 
		{
			String seed = JOptionPane.showInputDialog(frame, "Enter the value for seed:", "0");
			
			try {
				Long.parseLong(seed);
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
	
	
	public static void main(String[] args) 
	{
		new Game();
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
		P1Score.setText(String.format("Player 1 Score: %02d", scores[0]));
		P2Score.setText(String.format("Player 2 Score: %02d",scores[1]));
		
		turnIndicator.setBackground(board.getPlayerColor(board.getTurn()));
		
		
		boardP.repaint();
		
		if (b.getConsumed() == 100 || b.getMoves() == 200)
		{
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
				init();
			else
				b.setTurn(-1);
		}
		/*
		if (board.getTurn() == 1)
			ai1.move();
		
		if (board.getTurn() == 0)
			ai0.move();
		*/
		boardP.repaint();
		
	}
}
