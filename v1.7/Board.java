
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;


public class Board extends Observable
{
	public static final int MAX_RAND_VALUE = 6;
	public static final int MAX_RAND_TBL_ROW = 10;
	public static final int MAX_RAND_TBL_COL = 10;
	public static final int NUM_PIECES_PER_PLAYER = 2;
	public static final int NUM_PIECES_TOTAL = 100;
	
	public static final int PLAYER_1 = 0;
	public static final int PLAYER_2 = 1;
	public static final int INVALID_PLAYER = -1;
	
	public static final String NO_MOVES_MADE_YET = "No moves made yet!";
	
	private int[][] rand_tbl = new int[MAX_RAND_TBL_ROW][MAX_RAND_TBL_COL];
	private boolean[][] consumed = new boolean[MAX_RAND_TBL_ROW][MAX_RAND_TBL_COL];
	
	private Location[] player1pieces;
	private Location[] player2pieces;
	
	private Color P1color;
	private Color P2color;
	
	private int turn;
	private int plays;
	private int numConsumed;
	private int pointsLeft;
	private int[] scores = new int[2];
	
	private ArrayList<String> moveHistory;
	
	
	public Board ()
	{
		init ();
	}
	
	//Copy constructor
	public Board (Board b)
	{
		init();
		for (int i = 0; i < MAX_RAND_TBL_ROW; ++i)
		{
			for (int j = 0; j < MAX_RAND_TBL_ROW; ++j)
			{
				rand_tbl[i][j] = b.rand_tbl[i][j];
				consumed[i][j] = b.consumed[i][j];
			}
		}
		
		for (int i = 0; i < NUM_PIECES_PER_PLAYER; ++i)
		{
			player1pieces[i] = b.player1pieces[i];
			player2pieces[i] = b.player2pieces[i];
		}
		
		P1color = b.P1color;
		P2color = b.P2color;
		
		turn = b.turn;
		plays = b.plays;
		numConsumed = b.numConsumed;
		pointsLeft = b.pointsLeft;
		
		for (int i = 0; i < 2; ++i)
			scores[i] = b.scores[i];
	}
	
	public void init ()
	{
		turn = INVALID_PLAYER;
		plays = 0;
		numConsumed = 0;
		
		for (int i = 0; i < MAX_RAND_TBL_ROW; ++i)
		{
			Arrays.fill(rand_tbl[i], 0);
			Arrays.fill(consumed[i], false);
		}
		
		pointsLeft = 0;
		
		player1pieces = new Location[NUM_PIECES_PER_PLAYER];
		Arrays.fill(player1pieces, null);
		player2pieces = new Location[NUM_PIECES_PER_PLAYER];
		Arrays.fill(player2pieces, null);
		
		
		P1color = Color.BLACK;
		P2color = Color.BLACK;
		
		Arrays.fill(scores, 0);
		
		moveHistory = new ArrayList<String>(200);
	}
	
	public String getLastMove() {
		return moveHistory.size() > 0 ? moveHistory.get(moveHistory.size() - 1) : NO_MOVES_MADE_YET;
	}
	
	public ArrayList<String> getMoveHistory() {
		return moveHistory;
	}
	
	public int getValue(int r, int c)
	{
		return rand_tbl[r][c];
	}
	
	public int getValue(Location loc)
	{
		return getValue(loc.getRow(), loc.getCol());
	}
	
	public boolean checkConsumed (int r, int c)
	{
		return consumed[r][c];
	}
	
	public boolean checkConsumed (Location loc)
	{
		return consumed[loc.getRow()][loc.getCol()];
	}
	
	public void setSeed (long seed)
	{
		rand_tbl = RandGen.Generate(seed);
		for (int i = 0; i < MAX_RAND_TBL_ROW; i++) {
			for (int j = 0; j < MAX_RAND_TBL_COL; j++) {
				pointsLeft += rand_tbl[i][j];
			}
		}
	}
	
	public void setTurn (int t)
	{
		turn = t;
	}
	
	public int getTurn ()
	{
		return turn;
	}
	
	//Move function. Given two locations, checks if the move 
	//is valid and then moves the pieces.
	public boolean move (Location from, Location to)
	{
		if (!checkValidMove(from, to))
			return false;
		
		moveHistory.add("Player " + (turn + 1) + " moved from " + from.toString() + " to " + to.toString());
		
		//Move current piece to new location.
		for (int currPiece = 0; currPiece < NUM_PIECES_PER_PLAYER; ++currPiece)
		{
			if (turn == PLAYER_1 && player1pieces[currPiece].equals(from))
			{
				player1pieces[currPiece] = to;
				break;
			}
			else if (turn == PLAYER_2 && player2pieces[currPiece].equals(from))
			{
				player2pieces[currPiece] = to;
				break;
			}
		}
		
		int row = to.getRow();
		int col = to.getCol();
		
		//If tile has not been consumed, consume it and and it to the player's score.
		if (!consumed[row][col])
		{
			scores[turn] += rand_tbl[row][col];
			pointsLeft -= rand_tbl[row][col];
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
	
	
	//Move function used by AI. Move is checked before call is made.
	public void moveAI (Location from, Location to, int pieceNum, int depth, int maxDepth)
	{
		if (turn == PLAYER_1)
		{
			player1pieces[pieceNum] = to;
		}
		else if (turn == PLAYER_2)
		{
			player2pieces[pieceNum] = to;
		}
		
		int row = to.getRow();
		int col = to.getCol();
		
		//If tile has not been consumed, consume it and and it to the player's score.
		if (!consumed[row][col])
		{
			scores[turn] += rand_tbl[row][col] * (maxDepth - depth +1);
			consumed[row][col] = true;
			++numConsumed;
		}
		
		turn = 1 - turn;
		++plays;
	}
	
	//Check whether moving from one location to another is valid.
	public boolean checkValidMove (Location from, Location to)
	{
		int rowF = from.getRow();
		int colF = from.getCol();
		
		int rowT = to.getRow();
		int colT = to.getCol();
		
			//If move is farther than one square or
		if ((Math.abs(rowF - rowT) > 1 || Math.abs(colF - colT) > 1) ||
			//If locations are the same, not a valid move.
			(from.equals(to)))
		{
			return false;
		}
		
		return !doesPieceExist(to);
	}
	
	//Returns true if any piece exists on the given location.
	public boolean doesPieceExist(Location loc)
	{
		//Loop through all game pieces and check to make sure that
		//the destination location is not already taken.	
		for (int currPiece = 0; currPiece < NUM_PIECES_PER_PLAYER * 2; ++currPiece)
		{
			Location currLoc = null;
			
			//Getting a piece from the list of all pieces.
			if (currPiece < NUM_PIECES_PER_PLAYER) {
				currLoc = player1pieces[currPiece];
			}
			else {
				currLoc = player2pieces[currPiece-NUM_PIECES_PER_PLAYER];
			}
			
			//If another piece is located at the destination, not a valid move.
			if (currLoc.equals(loc))
				return true;
		}
		return false;
	}
	
	
	//Unmoves a given move. Only used by the AI.
	public void unMoveAI (Location to, Location from, boolean con, int pieceNum, int depth, int maxDepth)
	{
		turn = 1 - turn;
		
		int row = from.getRow();
		int col = from.getCol();
		
		//If tile was not consumed, unconsume it and subtract it from the player's score.
		if (!con)
		{
			scores[turn] -= rand_tbl[row][col] * (maxDepth - depth +1);
			consumed[row][col] = false;
			--numConsumed;
		}
				
		//Unmove current piece from new location.
		if (turn == PLAYER_1)
		{
			player1pieces[pieceNum] = to;
		}
		else if (turn == PLAYER_2)
		{
			player2pieces[pieceNum] = to;
		}
		
		--plays;
	}
	
	
	public void pickStart(int corner)
	{
		consumed[0][0] = true;
		consumed[0][9] = true;
		consumed[9][0] = true;
		consumed[9][9] = true;
		numConsumed = 4;
		
		if ((turn == PLAYER_1 && (corner == 0 || corner == 2)) || (turn == PLAYER_2 && ((corner == 1 || corner == 3))))
		{
			scores[0] += rand_tbl[0][0] +rand_tbl[9][9];
			pointsLeft -= (rand_tbl[0][0] + rand_tbl[9][9]);
			
			player1pieces[0] = new Location(0,0);
			player1pieces[1] = new Location(9,9);
			
			scores[1] += rand_tbl[9][0] +rand_tbl[0][9];
			pointsLeft -= (rand_tbl[9][0] + rand_tbl[0][9]);
			
			player2pieces[0] = new Location(0,9);
			player2pieces[1] = new Location(9,0);
		}
		else
		{
			scores[0] += rand_tbl[9][0] +rand_tbl[0][9];
			pointsLeft -= (rand_tbl[9][0] + rand_tbl[0][9]);

			player1pieces[0] = new Location(0,9);
			player1pieces[1] = new Location(9,0);
			
			scores[1] += rand_tbl[0][0] +rand_tbl[9][9];
			pointsLeft -= (rand_tbl[0][0] + rand_tbl[9][9]);
			
			player2pieces[0] = new Location(0,0);
			player2pieces[1] = new Location(9,9);
		}
		
		//Notify Game to update score.
		setChanged();  
		notifyObservers();
	}
	
	public Location[] getPlayer1Pieces()
	{
		return player1pieces;
	}
	
	public Location[] getPlayer2Pieces()
	{
		return player2pieces;
	}
	
	public void setColors(int selection)
	{
		if ((selection == 0 && turn == PLAYER_1) || (selection == 1 && turn == PLAYER_2))
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
		if (player == PLAYER_1)
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
	
	public int getPointsLeft()
	{
		return pointsLeft;
	}
	
	public boolean endOfGame()
	{
		return (numConsumed == 100 ||plays == 200);
	}

	
}
