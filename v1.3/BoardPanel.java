
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class BoardPanel extends JPanel implements MouseListener
{	
	private Board board;
	private Location pieceSelected;
	private BufferedImage image;
	
	
	public BoardPanel (Board B)
	{
		board = B;
		pieceSelected = null;
		this.addMouseListener(this);
		image = new BufferedImage(500, 500,
				BufferedImage.TYPE_INT_ARGB);
	}
	
	@Override
	public void paintComponent(Graphics g2) 
	{
		super.paintComponent(g2);
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 500, 500);
		
		g.setColor(Color.BLACK);
		
		for (int r = 0; r <= Board.MAX_RAND_TBL_ROW; ++r)
		{
			g.fillRect(0, 50*r-2, 500, 4);
		}
		
		for (int c = 0; c <= Board.MAX_RAND_TBL_COL; ++c)
		{
			g.fillRect(50*c-2, 0, 4, 500);
		}
		
		for (int r = 0; r < Board.MAX_RAND_TBL_ROW; ++r)
		{
			for (int c = 0; c < Board.MAX_RAND_TBL_COL; ++c)
			{
				if (!board.checkConsumed(r,c))
					g.drawString("" +board.getValue(r, c), 50*c+20, 50*r+30);
			}
		}
		
		
		Location[] player1pieces = board.getPlayer1Pieces();
		Location[] player2pieces = board.getPlayer2Pieces();
		
		for (int currPiece = 0; currPiece < Board.NUM_PIECES_PER_PLAYER * 2; ++currPiece)
		{
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
				
				if (pieceSelected != null && currLoc.equals(pieceSelected))
				{
					g.setColor(Color.BLACK);
					g.fillOval(50*col+5, 50*row+5, 40, 40);
				}

				if ((currPiece < Board.NUM_PIECES_PER_PLAYER) && !board.getPlayerColor(Board.PLAYER_1).equals(Color.BLACK))
				{
					g.setColor(board.getPlayerColor(Board.PLAYER_1));
				}
				else if ((currPiece >= Board.NUM_PIECES_PER_PLAYER && currPiece < Board.NUM_PIECES_PER_PLAYER*2) 
						&& !board.getPlayerColor(Board.PLAYER_2).equals(Color.BLACK))
				{
					g.setColor(board.getPlayerColor(Board.PLAYER_2));
				}
				
				g.fillOval(50*col+10, 50*row+10, 30, 30);
			}
		}
		
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
			
			if (turn == Board.PLAYER_1)
			{
				pieces = board.getPlayer1Pieces();
			}
			else if (turn == Board.PLAYER_2)
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

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
