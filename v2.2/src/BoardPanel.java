
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class BoardPanel extends JPanel implements MouseListener
{	
	private Board board;
	private Location pieceSelected;
	private AI.Move potentialMove;
	private BufferedImage image;
	
	private Image[] pieceIcons;
	private Image[] tileIcons;
	
	
	public BoardPanel (Board B)
	{
		board = B;
		pieceSelected = null;
		potentialMove = null;
		this.addMouseListener(this);
		image = new BufferedImage(500, 500,
				BufferedImage.TYPE_INT_ARGB);
		
		pieceIcons = new BufferedImage[4];
		tileIcons = new BufferedImage[3];
		
		try {
			pieceIcons[0] = ImageIO.read(getClass().getResourceAsStream("res/PieceRed.png"));
			pieceIcons[1] = ImageIO.read(getClass().getResourceAsStream("res/PieceSRed.png"));
			pieceIcons[2] = ImageIO.read(getClass().getResourceAsStream("res/PieceGreen.png"));
			pieceIcons[3] = ImageIO.read(getClass().getResourceAsStream("res/PieceSGreen.png"));
			tileIcons[0] = ImageIO.read(getClass().getResourceAsStream("res/Tile.png"));
			tileIcons[1] = ImageIO.read(getClass().getResourceAsStream("res/TileSRed.png"));
			tileIcons[2] = ImageIO.read(getClass().getResourceAsStream("res/TileSGreen.png"));
			
		} catch (IOException e) {}
		
	}
	
	@Override
	public void paintComponent(Graphics g2) 
	{
		super.paintComponent(g2);
		Graphics g = image.getGraphics();

		g.setColor(Color.BLACK);
		
		//Loop through all tiles on the board.
		for (int r = 0; r < Board.MAX_RAND_TBL_ROW; ++r)
		{
			for (int c = 0; c < Board.MAX_RAND_TBL_COL; ++c)
			{
				//Within moving distance, but the location of the selected piece.
				if(pieceSelected != null && board.checkValidMove(pieceSelected, new Location(r,c)))
				{
					//Draw red tiles for possible moves.
					if ((board.getTurn() == Board.PLAYER_1 && board.getPlayerColor(Board.PLAYER_1) == Board.RED) ||
						(board.getTurn() == Board.PLAYER_2 && board.getPlayerColor(Board.PLAYER_2) == Board.RED))
						g.drawImage(tileIcons[1], 50*c, 50*r, 50, 50, null);
					
					//Draw green tiles for possible moves.
					else if ((board.getTurn() == Board.PLAYER_1 && board.getPlayerColor(Board.PLAYER_1) == Board.GREEN) ||
							(board.getTurn() == Board.PLAYER_2 && board.getPlayerColor(Board.PLAYER_2) == Board.GREEN))
						g.drawImage(tileIcons[2], 50*c, 50*r, 50, 50, null);
				}
				
				//Draw potential AI's move
				else if(potentialMove != null &&
						((r == potentialMove.to.getRow() && c == potentialMove.to.getCol()))) {
								//|| (r == potentialMove.from.getRow() && c == potentialMove.from.getCol()))) {
					
					//g.drawImage(tileIcons[0], 50*c, 50*r, 50, 50, null);
					//Draw red tiles for possible moves.
					if ((board.getTurn() == Board.PLAYER_1 && board.getPlayerColor(Board.PLAYER_1) == Board.RED) ||
						(board.getTurn() == Board.PLAYER_2 && board.getPlayerColor(Board.PLAYER_2) == Board.RED))
						g.drawImage(tileIcons[1], 50*c, 50*r, 50, 50, null);
					
					//Draw green tiles for possible moves.
					else if ((board.getTurn() == Board.PLAYER_1 && board.getPlayerColor(Board.PLAYER_1) == Board.GREEN) ||
							(board.getTurn() == Board.PLAYER_2 && board.getPlayerColor(Board.PLAYER_2) == Board.GREEN))
						g.drawImage(tileIcons[2], 50*c, 50*r, 50, 50, null);
					
					
				}
				
				//Draw normal tile
				else
					g.drawImage(tileIcons[0], 50*c, 50*r, 50, 50, null);
			
				//Draw value if not already consumed.
				if (!board.checkConsumed(r,c))
					g.drawString("" +board.getValue(r, c), 50*c+20, 50*r+30);
			}
		}
		
		//Get player piece locations.
		Location[] player1pieces = board.getPlayer1Pieces();
		Location[] player2pieces = board.getPlayer2Pieces();
		
		//Loop through each piece.
		for (int currPiece = 0; currPiece < Board.NUM_PIECES_PER_PLAYER * 2; ++currPiece)
		{
			//Get current piece location.
			Location currLoc = null;
			
			//Getting a piece from the list of all pieces.
			if (currPiece < Board.NUM_PIECES_PER_PLAYER)
			{
				currLoc = player1pieces[currPiece];
			}
			else
			{
				currLoc = player2pieces[currPiece - Board.NUM_PIECES_PER_PLAYER];
			}
			
			if (currLoc != null)
			{
				int row = currLoc.getRow();
				int col = currLoc.getCol();
				
				int selectedOffset = 0;
				
				//Found the selected piece.
				if ((pieceSelected != null && currLoc.equals(pieceSelected))
						|| (potentialMove != null && currLoc.equals(potentialMove.from)))
				{
					//Increment offset to use selected icon.
					selectedOffset = 1;
				}
				
				//Draw red piece.
				if ((currPiece < Board.NUM_PIECES_PER_PLAYER && board.getPlayerColor(Board.PLAYER_1).equals(Board.RED)) ||
						((currPiece >= Board.NUM_PIECES_PER_PLAYER && currPiece < Board.NUM_PIECES_PER_PLAYER*2) 
							&& board.getPlayerColor(Board.PLAYER_2).equals(Board.RED)))
				{
					g.drawImage(pieceIcons[0+selectedOffset], 50*col, 50*row, 50, 50, null);
				}
				
				//Draw green piece.
				else if ((currPiece < Board.NUM_PIECES_PER_PLAYER && board.getPlayerColor(Board.PLAYER_1).equals(Board.GREEN)) ||
						((currPiece >= Board.NUM_PIECES_PER_PLAYER && currPiece < Board.NUM_PIECES_PER_PLAYER*2) 
						&& board.getPlayerColor(Board.PLAYER_2).equals(Board.GREEN)))
				{
					g.drawImage(pieceIcons[2+selectedOffset], 50*col, 50*row, 50, 50, null);
				}
			}
		}
		
		potentialMove = null;
		g2.drawImage(image, 0, 0, 500, 500, null);
	}
	
	
	public void mouseClicked(MouseEvent e) 
	{
		int x = e.getX();
		int y = e.getY();
		
		//System.out.println(x/50 +" " +y/50);
		
		Location loc = new Location(y/50, x/50);
		
		if (pieceSelected == null)
		{
			int turn = board.getTurn();

			Location[] pieces = null;
			
			if (turn == Board.PLAYER_1 && !board.getAI(Board.PLAYER_1))
			{
				pieces = board.getPlayer1Pieces();
			}
			else if (turn == Board.PLAYER_2 && !board.getAI(Board.PLAYER_2))
			{
				pieces = board.getPlayer2Pieces();
			}
			else	//invalid turn;
			{
				return;
			}
			
			//System.out.println(loc.getRow() +" " +loc.getCol());
				
			int currPiece;
			for (currPiece = 0; currPiece < Board.NUM_PIECES_PER_PLAYER; ++currPiece)
			{
				//System.out.println(pieces[currPiece].getRow() +" " +pieces[currPiece].getCol());
					
				if (pieces[currPiece].equals(loc))
				{
					break;
				}
			}
			
			if (currPiece < Board.NUM_PIECES_PER_PLAYER)
			{
				pieceSelected = loc;
			}
			
			//System.out.println(board.getTurn() +" " +currPiece +" " +currPiece/2);
		}
		else
		{
			board.move(pieceSelected, loc);
			pieceSelected = null;
		}
		repaint();
	}

	public void setPotentialMove(AI.Move move) {
		potentialMove = move;
	}
	
	public ImageIcon getRandomImageIcon() {
		int offset = (int)(Math.random()*2) *2;
		return new ImageIcon(pieceIcons[1 + offset]);
	}
	
	public ImageIcon getImageIcon(int player) {
		int offset = 0;
//		if ((player == Board.PLAYER_1 && board.getPlayerColor(Board.PLAYER_1).equals(Board.RED)) 
//				&& (player == Board.PLAYER_2 && board.getPlayerColor(Board.PLAYER_2).equals(Board.RED)))
//		{
//			
//		}
//		else 
			if ((player == Board.PLAYER_1 && board.getPlayerColor(Board.PLAYER_1).equals(Board.GREEN)) 
			|| (player == Board.PLAYER_2 && board.getPlayerColor(Board.PLAYER_2).equals(Board.GREEN)))
		{	
			offset = 2;
		}
		return new ImageIcon(pieceIcons[1 + offset]);
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
