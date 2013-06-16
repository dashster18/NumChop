/**
 * NumChop Game
 * Spring 2013
 * 
 * This class is used to keep
 * track of the location of a
 * piece on the board.
 * 
 * @author Joshua Linge and Nikesh Srivastava
 * 
 */
public class Location {

	private int row;
	private int col;

	public Location (int r, int c)
	{
		row = r; 
		col = c;
	}

	public int getRow ()
	{
		return row;
	}

	public int getCol()
	{
		return col;
	}
	
	public void setRow(int r) 
	{
		row = r;
	}

	public void setCol(int c)
	{
		col = c;
	}
	
	public boolean equals(Location o)
	{
		return (row == o.row && col == o.col);
	}

	public String toString()
	{
		return String.format("%c%d", row + 'A', col + 1);
	}
}
