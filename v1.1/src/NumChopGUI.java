import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


public class NumChopGUI {

	// GUI components needed for the game
	private JFrame frame;
	private JPanel infoBar;
	private BoardPanel[][] boardPanel;
	private JPanel historyPanel;


	// Some statics constants we need for the GUI
	private static final int windowWidth = 610;
	private static final int windowHeight = 680;
	private static final int boardWidth = 500;
	private static final int boardHeight = 500;

	public NumChopGUI() {

		// Create the main Frame for the GUI, and do some basic operations on them
		buildFrame();

		// Create a menu
		buildMenu();
		
		// Create infoBar here
		buildInfoBar();

		// Create game board panel here
		buildBoard();

		// Create history panel here
		buildHistory();
		
		// Make sure the frame is visible
		frame.setVisible(true);
	}

	// This method creates the frame for the game
	private void buildFrame() {
		
		// Build a JFrame
		frame = new JFrame("NumChop");
		
		// Make it so you can't change the size of the game window
		frame.setResizable(false);
		
		// Allow the user to close the application on exit
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set the size of the application window to the constants declared above
		frame.setSize(windowWidth, windowHeight);
		
		// We're going to use the BorderLayout for the main frame
		frame.getContentPane().setLayout(new BorderLayout());
	}
	
	// This method builds the Menu for the game
	private void buildMenu() {

		// Build a menuBar
		JMenuBar menuBar = new JMenuBar();

		// Build the first drop down menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		// The new game option
		JMenuItem newGameMenuItem = new JMenuItem("New Game");
		newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		newGameMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//newGame();
			}

		});
		fileMenu.add(newGameMenuItem);

		// A separator
		fileMenu.addSeparator();

		// The exit option
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);

		// Add the menu to the menuBar
		menuBar.add(fileMenu);

		// Add the menu bar to the frame
		frame.setJMenuBar(menuBar);
		frame.pack();
	}

	// This method will create the info bar for the game
	private void buildInfoBar() {
		
	}
	
	// This method will create the Board using the BoardPanels
	private void buildBoard() {
		
	}
	
	// This method will create the history menu for the game
	private void buildHistory() {
		
	}
	
	public static void main(String[] args) {
		new NumChopGUI();
	}

	// This class will be a single panel in the 10x10 grid
	private class BoardPanel extends JPanel implements MouseListener{

		private int row, col;
		private boolean isSelected, consumed;
		private int value;

		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}

	}
}
