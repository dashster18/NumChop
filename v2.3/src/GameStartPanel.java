
import java.awt.Dimension;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * NumChop Game
 * Spring 2013
 * 
 * GameStartPanel is the initial options
 * menu that pops up when you start the
 * NumChop Game.
 * 
 * @author Joshua Linge and Nikesh Srivastava
 * 
 */
public class GameStartPanel extends JPanel {

	// Booleans used to store game state
	// at the beginning of the game
	private boolean playerOneIsHuman;
	private boolean playerTwoIsHuman;
	private boolean playerOneIsFirst;
	private boolean playerOneIsRed;
	
	// GUI components
	private JRadioButton playerOneHuman;
	private JRadioButton playerOneComputer;
	private JRadioButton playerTwoHuman;
	private JRadioButton playerTwoComputer;
	private JRadioButton player1Turn;
	private JRadioButton player2Turn;
	private JRadioButton randomTurn;
	private JRadioButton redColorSelection;
	private JRadioButton greenColorSelection;
	private JTextField textField;
	private JLabel lblSelectColor;
	
	/**
	 * @return Returns the user entered seed.
	 */
	public String getSeed() {
		return textField.getText();
	}
	
	
	/**
	 * @return Returns the preliminary data
	 * 		   that is needed to start the game.
	 */
	public boolean[] getData() {
		
		// Determine if player 1
		// is a human or a computer.
		if (playerOneHuman.isSelected()) {
			playerOneIsHuman = true;
		}
		else {
			playerOneIsHuman = false;
		}
		
		// Similarily, do so for
		// player 2.
		if (playerTwoHuman.isSelected()) {
			playerTwoIsHuman = true;
		}
		else {
			playerTwoIsHuman = false;
		}
		
		// Determine who's moving
		// first.
		if (player1Turn.isSelected()) {
			playerOneIsFirst = true;
		}
		else if (player2Turn.isSelected()) {
			playerOneIsFirst = false;
		}
		else {
			// Randomly pick someone
			// to move first.
			int player = (int)(Math.random()*2);
			if (player == 0) {
				playerOneIsFirst = true;
			}
			else {
				playerOneIsFirst = false;
			}
		}
		
		// Determine which player is
		// red and which player is
		// green.
		if (playerOneIsFirst && redColorSelection.isSelected()) {
			playerOneIsRed = true;
		}
		else if (!playerOneIsFirst && greenColorSelection.isSelected()) { 
			playerOneIsRed = true;
		}
		else {
			playerOneIsRed = false;
		}
		
		// Return the data that
		// represents all the 
		// preliminary info about
		// players.
		return new boolean[] {playerOneIsHuman, playerTwoIsHuman, playerOneIsFirst, playerOneIsRed};
	}
	
	/**
	 * Constructor to initialize the panel
	 * that has all the game info.
	 */
	public GameStartPanel() {
		
		// Player 1's options to choose
		// if he/she is a human or computer.
		playerOneHuman = new JRadioButton("Human");
		playerOneHuman.setBounds(16, 24, 77, 23);
		playerOneComputer = new JRadioButton("Computer");
		playerOneComputer.setBounds(105, 24, 94, 23);
		
		ButtonGroup player1Group = new ButtonGroup();
		player1Group.add(playerOneHuman);
		player1Group.add(playerOneComputer);
		playerOneHuman.setSelected(true);
		
		
		// Player 2's options to choose if
		// he/she is a human or computer.
		playerTwoHuman = new JRadioButton("Human");
		playerTwoHuman.setBounds(16, 76, 77, 23);
		playerTwoComputer = new JRadioButton("Computer");
		playerTwoComputer.setBounds(105, 76, 94, 23);
		
		ButtonGroup player2Group = new ButtonGroup();
		player2Group.add(playerTwoHuman);
		player2Group.add(playerTwoComputer);
		playerTwoHuman.setSelected(true);
		
		
		// Option to select who moves first
		player1Turn = new JRadioButton("Player 1");
		player1Turn.setBounds(16, 128, 81, 23);
		player2Turn = new JRadioButton("Player 2");
		player2Turn.setBounds(105, 128, 81, 23);
		randomTurn = new JRadioButton("Random");
		randomTurn.setBounds(198, 128, 83, 23);
		
		ButtonGroup turnSelectionGroup = new ButtonGroup();
		turnSelectionGroup.add(player1Turn);
		turnSelectionGroup.add(player2Turn);
		turnSelectionGroup.add(randomTurn);
		player1Turn.setSelected(true);
		
		
		// Add Player 1's options to the Panel
		setLayout(null);
		JLabel label_1 = new JLabel("Player 1");
		label_1.setBounds(6, 6, 49, 16);
		this.add(label_1);
		this.add(playerOneHuman);
		this.add(playerOneComputer);
		
		// Add Player 2's options to the Panel
		JLabel label_2 = new JLabel("Player 2");
		label_2.setBounds(6, 59, 49, 16);
		this.add(label_2);
		this.add(playerTwoHuman);
		this.add(playerTwoComputer);
		this.add(player1Turn);
		this.add(player2Turn);
		
		// Add the "who moves first" option to the Panel
		JLabel label = new JLabel("Select who will go first");
		label.setBounds(6, 111, 143, 16);
		this.add(label);
		this.add(randomTurn);
		
		// Add the seed option the Panel		
		JLabel lblSeed = new JLabel("Seed");
		lblSeed.setBounds(6, 214, 49, 23);
		add(lblSeed);
		
		// Randomly populate the seed field with a number
		textField = new JTextField();
//		textField.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//				textField.setText(textField.getText().replaceAll("[^0-9]", ""));
//			}
//		});
		textField.setBounds(52, 211, 134, 28);
		textField.setText("" + Math.abs(new Random().nextInt()));
		add(textField);
		textField.setColumns(10);
		
		// Color options for players
		lblSelectColor = new JLabel("Select Color");
		lblSelectColor.setBounds(6, 163, 104, 16);
		add(lblSelectColor);
		
		redColorSelection = new JRadioButton("Red");
		redColorSelection.setBounds(16, 179, 81, 23);
		add(redColorSelection);
		
		greenColorSelection = new JRadioButton("Green");
		greenColorSelection.setBounds(105, 179, 141, 23);
		add(greenColorSelection);
		
		ButtonGroup colorSelectionGroup = new ButtonGroup();
		colorSelectionGroup.add(redColorSelection);
		colorSelectionGroup.add(greenColorSelection);
		redColorSelection.setSelected(true);
		
		// Size the window
		this.setPreferredSize(new Dimension(280, 245));
	}
	
}
