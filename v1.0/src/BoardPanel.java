import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class BoardPanel extends JPanel implements MouseListener
{	
	private Board board;
	private int pieceSelected;
	private BufferedImage image;
	
	
	public BoardPanel (Board B)
	{
		board = B;
		pieceSelected = -1;
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
		
		String[] pieces = board.getPositions();
		
		for (int i = 0; i < pieces.length; ++i)
		{
			String piece = pieces[i];
			if (!piece.equals(""))
			{
				int row = (int)piece.charAt(0) - (int)'A';
				int col = (int)piece.charAt(1) - (int)'0';
				
				if (i == pieceSelected)
				{
					g.setColor(Color.BLACK);
					g.fillOval(50*col+5, 50*row+5, 40, 40);
				}
				
				if ((i == 0 || i == 1) && !board.getPlayerColor(0).equals(Color.BLACK))
				{
					g.setColor(board.getPlayerColor(0));
				}
				else if ((i == 2 || i == 3) && !board.getPlayerColor(1).equals(Color.BLACK))
				{
					g.setColor(board.getPlayerColor(1));
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
		
		String pos = "" +(char)((y/50) +'A') +(x/50);
		
		//System.out.println(pos);
		
		if (pieceSelected == -1)
		{
			String[] pieces = board.getPositions();
			
			int i;
			for (i = 0; i < pieces.length; ++i)
			{
				if (pieces[i].equals(pos))
					break;
			}
			
			//System.out.println(board.getTurn() +" " +i +" " +i/2);
			
			if (i != 4 && (i/2) == board.getTurn())
			{
				pieceSelected = i;
			}
			repaint();
		}
		else
		{
			board.move(pieceSelected, pos);
			pieceSelected = -1;
			repaint();
		}
		
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
