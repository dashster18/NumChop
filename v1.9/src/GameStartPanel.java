import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class GameStartPanel extends JPanel {

	private boolean playerOneIsHuman;
	private boolean playerTwoIsHuman;
	private boolean playerOneIsFirst;
	
	private JRadioButton playerOneHuman;
	private JRadioButton playerOneComputer;
	private JRadioButton playerTwoHuman;
	private JRadioButton playerTwoComputer;
	private JRadioButton player1Turn;
	private JRadioButton player2Turn;
	private JRadioButton randomTurn;
	
	
	public boolean[] getData() {
		
		if (playerOneHuman.isSelected()) {
			playerOneIsHuman = true;
		}
		else {
			playerOneIsHuman = false;
		}
		
		if (playerTwoHuman.isSelected()) {
			playerTwoIsHuman = true;
		}
		else {
			playerTwoIsHuman = false;
		}
		
		if (player1Turn.isSelected()) {
			playerOneIsFirst = true;
		}
		else if (player2Turn.isSelected()) {
			playerOneIsFirst = false;
		}
		else {
			int player = (int)(Math.random()*2);
			if (player == 0) {
				playerOneIsFirst = true;
			}
			else {
				playerOneIsFirst = false;
			}
		}
		
		return new boolean[] {playerOneIsHuman, playerTwoIsHuman, playerOneIsFirst};
	}
	
	public GameStartPanel() {
		playerOneHuman = new JRadioButton("Human");
		playerOneHuman.setBounds(16, 24, 77, 23);
		playerOneComputer = new JRadioButton("Computer");
		playerOneComputer.setBounds(105, 24, 94, 23);
		
		ButtonGroup player1Group = new ButtonGroup();
		player1Group.add(playerOneHuman);
		player1Group.add(playerOneComputer);
		playerOneHuman.setSelected(true);
		
		
		playerTwoHuman = new JRadioButton("Human");
		playerTwoHuman.setBounds(16, 76, 77, 23);
		playerTwoComputer = new JRadioButton("Computer");
		playerTwoComputer.setBounds(105, 76, 94, 23);
		
		ButtonGroup player2Group = new ButtonGroup();
		player2Group.add(playerTwoHuman);
		player2Group.add(playerTwoComputer);
		playerTwoHuman.setSelected(true);
		
		
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
		randomTurn.setSelected(true);
		
		
		setLayout(null);
		JLabel label_1 = new JLabel("Player 1");
		label_1.setBounds(6, 6, 49, 16);
		this.add(label_1);
		this.add(playerOneHuman);
		this.add(playerOneComputer);
		
		JLabel label_2 = new JLabel("Player 2");
		label_2.setBounds(6, 59, 49, 16);
		this.add(label_2);
		this.add(playerTwoHuman);
		this.add(playerTwoComputer);
		this.add(player1Turn);
		this.add(player2Turn);
		
		JLabel label = new JLabel("Select who will go first");
		label.setBounds(6, 111, 143, 16);
		this.add(label);
		this.add(randomTurn);
		
		this.setPreferredSize(new Dimension(280, 160));
	}
}
