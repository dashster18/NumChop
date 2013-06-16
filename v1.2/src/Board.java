import java.awt.Color;
import java.util.Arrays;
import java.util.Observable;


public class Board extends Observable
{
	public static final int MAX_RAND_VALUE = 6;
	public static final int MAX_RAND_TBL_ROW = 10;
	public static final int MAX_RAND_TBL_COL = 10;
	
	private int[][] rand_tbl = new int[MAX_RAND_TBL_ROW][MAX_RAND_TBL_COL];
	private boolean[][] consumed = new boolean[MAX_RAND_TBL_ROW][MAX_RAND_TBL_COL];
	
	private String[] pieces = new String[4];
	private Color P1color;
	private Color P2color;
	
	private int turn;
	private int plays;
	private int numConsumed;
	private int[] scores = new int[2];
	
	
	public Board ()
	{
		init ();
	}
	
	public void init ()
	{
		turn = 0;
		plays = 0;
		numConsumed = 0;
		
		for (int i = 0; i < MAX_RAND_TBL_ROW; ++i)
		{
			Arrays.fill(rand_tbl[i], 0);
			Arrays.fill(consumed[i], false);
		}
		
		Arrays.fill(pieces, "");
		
		P1color = Color.BLACK;
		P2color = Color.BLACK;
		
		Arrays.fill(scores, 0);
	}
	
	public int getValue(int r, int c)
	{
		return rand_tbl[r][c];
	}
	
	public int getValue(String loc)
	{
		int row = (int)loc.charAt(0) - (int)'A';
		int col = (int)loc.charAt(1) - (int)'0';
		
		return getValue(row, col);
	}
	
	public boolean checkConsumed (int r, int c)
	{
		return consumed[r][c];
	}
	
	public void setSeed (long seed)
	{
		rand_tbl = RandGen.Generate(seed);
	}
	
	public void setTurn (int t)
	{
		turn = t;
	}
	
	public int getTurn ()
	{
		return turn;
	}
	
	public boolean move (int piece, String to)
	{
		if (!checkValidMove(piece, to))
			return false;
		
		pieces[piece] = to;
		
		int row = (int)to.charAt(0) - (int)'A';
		int col = (int)to.charAt(1) - (int)'0';
		
		if (!consumed[row][col])
		{
			scores[turn] += rand_tbl[row][col];
			consumed[row][col] = true;
			++numConsumed;
		}
		
		
		turn = 1 - turn;
		++plays;
		
		//Notify Game that the score and turn has changed.
		setChanged();  
		notifyObservers();
		return true;
	}
	
	public boolean checkValidMove (int piece, String to)
	{
		boolean ret = true;
		
		int rowP = (int)pieces[piece].charAt(0) - (int)'A';
		int colP = (int)pieces[piece].charAt(1) - (int)'0';
		
		int rowT = (int)to.charAt(0) - (int)'A';
		int colT = (int)to.charAt(1) - (int)'0';
		
		if ((Math.abs(rowP - rowT) > 1 || Math.abs(colP - colT) > 1) ||
			(rowP == rowT && colP == colT))
			ret = false;
		
		for (int i = 0; i < pieces.length; ++i)
		{
			if (i == piece)
				continue;
			rowP = (int)pieces[i].charAt(0) - (int)'A';
			colP = (int)pieces[i].charAt(1) - (int)'0';
			
			if (rowP == rowT && colP == colT)
				ret = false;
		}
		
		return ret;
	}
	
	public void pickStart(int corner)
	{
		consumed[0][0] = true;
		consumed[0][9] = true;
		consumed[9][0] = true;
		consumed[9][9] = true;
		numConsumed = 4;
		
		if ((turn == 0 && (corner == 0 || corner == 2)) || (turn == 1 && ((corner == 1 || corner == 3))))
		{
			pieces[0] = "A0";
			pieces[1] = "J9";
			scores[0] += rand_tbl[0][0] +rand_tbl[9][9];
			
			pieces[2] = "A9";
			pieces[3] = "J0";
			scores[1] += rand_tbl[9][0] +rand_tbl[0][9];
			
		}
		else
		{
			pieces[0] = "A9";
			pieces[1] = "J0";
			scores[0] += rand_tbl[9][0] +rand_tbl[0][9];
			
			pieces[2] = "A0";
			pieces[3] = "J9";
			scores[1] += rand_tbl[0][0] +rand_tbl[9][9];
		}
		
		//Notify Game to update score.
		setChanged();  
		notifyObservers();
	}
	
	public String[] getPositions ()
	{
		return pieces;
	}
	
	public void setColors(int selection)
	{
		if ((selection == 0 && turn == 0) || (selection == 1 && turn == 1))
		{
			P1color = Color.RED;
			P2color = Color.GREEN;
		}
		else
		{
			P2color = Color.RED;
			P1color = Color.GREEN;
		}
	}
	
	public Color getPlayerColor(int player)
	{
		if (player == 0)
			return P1color;
		return P2color;
	}
	
	public int[] getScores ()
	{
		return scores;
	}
	
	public int getMoves()
	{
		return plays;
	}
	
	public int getConsumed()
	{
		return numConsumed;
	}
}
