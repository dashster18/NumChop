

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
	
	public boolean equals(Location o)
	{
		return (row == o.row && col == o.col);
	}
	
	public String toString()
	{
		return String.format("%d %d", row, col);
	}
}
