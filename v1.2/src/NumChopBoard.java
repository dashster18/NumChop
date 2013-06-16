import java.awt.Color;
import java.util.Arrays;


public class NumChopBoard {

	/**
	 * MAX_RAND_VALUE determines the highest
	 * possible value that can show up on the
	 * board in any square.
	 */
	public static final int MAX_RAND_VALUE = 6;


	/**
	 * NUM_ROWS determines how many
	 * rows the game board will have.
	 */
	public static final int NUM_ROWS = 10;


	/**
	 * NUM_COLS determines how many
	 * columns the game board will
	 * have.
	 */
	public static final int NUM_COLS = 10;


	/**
	 * This object shows that it's
	 * player one's turn.
	 */
	public static final Object PLAYER_ONE_TURN = new Object();


	/**
	 * This object shows that it's
	 * player two's turn.
	 */
	public static final Object PLAYER_TWO_TURN = new Object();

	// Need this value to signify that 
	// the piece is currently in an 
	// invalid location.
	private static final int INVALID_LOCATION = -1;


	// These variables are used to keep
	// track of what color each player is.
	private Color playerOneColor = null;
	private Color playerTwoColor = null;


	// These variables used to track the
	// state of the game.
	private int[][] board;
	private boolean[][] locationUsed;
	private Location[] playerOneLocation;
	private Location[] playerTwoLocation;


	// The variables are being used to
	// keep track of some relevant game
	// data which the user might find
	// useful.
	private Object turn;
	private int playerOneScore;
	private int playerTwoScore;
	private int numMovesMade;
	private int numPiecesUsed;
	private int numPointsLeft;


	public NumChopBoard(long seed, Object turn, Color p1, Color p2) {

		// Create the array to hold
		// the location for two pieces
		playerOneLocation = new Location[2];
		playerTwoLocation = new Location[2];

		// Create the locations for
		// both pieces of both players
		playerOneLocation[0] = new Location(INVALID_LOCATION, INVALID_LOCATION);
		playerOneLocation[1] = new Location(INVALID_LOCATION, INVALID_LOCATION);

		playerTwoLocation[0] = new Location(INVALID_LOCATION, INVALID_LOCATION);
		playerTwoLocation[1] = new Location(INVALID_LOCATION, INVALID_LOCATION);

		// Initialize these values
		playerOneScore = 0;
		playerTwoScore = 0;
		this.turn = turn;
		numMovesMade = 0;
		numPiecesUsed = 0;
		numPointsLeft = 0;
		playerOneColor = p1;
		playerTwoColor = p2;
	

		// Make a board of pieces used
		// and initially set it so
		// none are used.
		locationUsed = new boolean[NUM_ROWS][NUM_COLS];
		for (boolean[] b : locationUsed) {
			Arrays.fill(b, false);
		}
		
		// Generate the board
		createBoard(seed);
		
		// Iterate through the board
		// and sum up all of the points
		// on the board.
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				numPointsLeft += board[i][j];
			}
		}
	}


	/**
	 * This method makes the move specified
	 * move from 'source' to 'destination'.
	 * @param source The initial position
	 * 			your piece is.
	 * @param destination The position you
	 * 			want to move your piece to.
	 * @return Returns True if the move was
	 *			successful. False if it failed.
	 */
	public boolean makeMove(Location source, Location destination) {

		// Check whether the move can
		// be made. If it can't, then
		// return false.
		if (!isValidMove(source, destination) || !checkLocationHasPiece(source)) {
			return false;
		}


		if (turn == PLAYER_ONE_TURN) {

			// If it's Player One's turn,
			// then make sure that (s)he's
			// not trying to move Player
			// Two's piece.
			if ((source.getRow() == playerTwoLocation[0].getRow() && source.getCol() == playerTwoLocation[0].getCol()) || 
					(source.getRow() == playerTwoLocation[1].getRow() && source.getCol() == playerTwoLocation[1].getCol())) {

				return false;
			}

			// Figure out which piece
			// your are trying to move.
			Location currentPiece = null;

			// If you're moving piece 0,
			// then set currentPiece to
			// that.
			if (source.getRow() == playerOneLocation[0].getRow() && source.getCol() == playerOneLocation[0].getCol()) {
				currentPiece = playerOneLocation[0];
			}

			// If you're moving piece 1,
			// then set currentPiece to
			// that.
			else if (source.getRow() == playerOneLocation[1].getRow() && source.getCol() == playerOneLocation[1].getCol()) {
				currentPiece = playerOneLocation[1];
			}

			// You should never reach
			// this case, but if you
			// magically do, then just
			// return false.
			else {
				return false;
			}

			// Update the piece's location
			currentPiece.setRow(destination.getRow());
			currentPiece.setCol(destination.getCol());

			// If the piece hasn't been
			// used yet, add the score
			// to the player's score
			// Update the number of 
			// pieces used, and the number
			// of points left on the board
			if (!checkLocationUsed(destination)) {
				playerOneScore += getBoardValue(destination);
				numPointsLeft -= getBoardValue(destination);
				locationUsed[destination.getRow()][destination.getCol()] = true;
				numPiecesUsed++;

			}

			// Update the turn
			turn = PLAYER_TWO_TURN;
		}


		else if (turn == PLAYER_TWO_TURN) {

			// Similiarly, if it's Player
			// Two's turn, make sure (s)he's
			// not trying to move Player
			// One's piece.
			if ((source.getRow() == playerOneLocation[0].getRow() && source.getCol() == playerOneLocation[0].getCol()) || 
					(source.getRow() == playerOneLocation[1].getRow() && source.getCol() == playerOneLocation[1].getCol())) {

				return false;
			}

			// Figure out which piece
			// your are trying to move.
			Location currentPiece = null;

			// If you're moving piece 0,
			// then set currentPiece to
			// that.
			if (source.getRow() == playerTwoLocation[0].getRow() && source.getCol() == playerTwoLocation[0].getCol()) {
				currentPiece = playerTwoLocation[0];
			}

			// If you're moving piece 1,
			// then set currentPiece to
			// that.
			else if (source.getRow() == playerTwoLocation[1].getRow() && source.getCol() == playerTwoLocation[1].getCol()) {
				currentPiece = playerTwoLocation[1];
			}

			// You should never reach
			// this case, but if you
			// magically do, then just
			// return false.
			else {
				return false;
			}

			// Update the piece's location
			currentPiece.setRow(destination.getRow());
			currentPiece.setCol(destination.getCol());

			// If the piece hasn't been
			// used yet, add the score
			// to the player's score
			// Update the number of 
			// pieces used, and the number
			// of points left on the board
			if (!checkLocationUsed(destination)) {
				playerOneScore += getBoardValue(destination);
				numPointsLeft -= getBoardValue(destination);
				locationUsed[destination.getRow()][destination.getCol()] = true;
				numPiecesUsed++;

			}

			// Update the turn
			turn = PLAYER_ONE_TURN;
		}
		
		// If it's neither Player
		// One's turn or Player
		// Two's turn, well you
		// fucked something up.
		else {
			return false;
		}

		// If you get here, then
		// you did everything 
		// right and made a correct
		// move. Yay!
		return true;
	}

	
	/**
	 * Checks whether the move from 'source'
	 * to 'destination' is a valid move.
	 * @param source Location of the piece.
	 * @param destination Location the piece
	 * 			wishes to move to.
	 * @return Returns True if it is a valid
	 * 			move. Returns False otherwise.
	 */
	public boolean isValidMove(Location source, Location destination) {

		// If there is a piece at the
		// destination, then you can't
		// move there, so return false.
		if (checkLocationHasPiece(destination)) {
			return false;
		}

		// If the move tries to go more
		// than one square away, you
		// can't move there, so return
		// false.
		if (Math.abs(source.getRow() - destination.getRow()) > 1 ||
				Math.abs(source.getCol() - destination.getCol()) > 1) {

			return false; 
		}

		// Otherwise, the move is fine
		// so return true.
		return true;
	}


	/**
	 * Checks if 'loc' contains a piece on it.
	 * @param loc The location you want to check.
	 * @return Returns True if the location contains
	 * 			a piece. Returns False otherwise.
	 */
	public boolean checkLocationHasPiece(Location loc) {
		return (playerOneLocation[0].getRow() == loc.getRow() && playerOneLocation[0].getCol() == loc.getCol())
				|| (playerOneLocation[1].getRow() == loc.getRow() && playerOneLocation[1].getCol() == loc.getCol())
				|| (playerTwoLocation[0].getRow() == loc.getRow() && playerTwoLocation[0].getCol() == loc.getCol())
				|| (playerTwoLocation[1].getRow() == loc.getRow() && playerTwoLocation[1].getCol() == loc.getCol());

	}

	// TODO: Implement this
	// Ask Joshua whether or not we
	// should give the player who
	// moves second the option to
	// pick which corner to start
	// in
	public void setStartingPositions() {
		
	}
	
	/**
	 * Generates a random board, as per
	 * the specifications
	 * @param seed The initial value you want
	 * 				to start the random number
	 * 				generator at.
	 */
	public void createBoard(long seed) { 
		board = RandGen.Generate(seed);
	}


	/**
	 * Gets the value of 'loc' on the board.
	 * @param loc The location you want to observe.
	 * @return Returns the value at 'loc'.
	 */
	public int getBoardValue(Location loc) { 
		return board[loc.getRow()][loc.getCol()]; 
	}


	/**
	 * Check if location 'loc' has already been
	 * used.
	 * @param loc The location you want to check.
	 * @return Returns true if the 'loc' has been used,
	 * 			false if 'loc' has not been used. 
	 */
	public boolean checkLocationUsed(Location loc) { 
		return locationUsed[loc.getRow()][loc.getCol()];	
	}


	// These are all getters for the
	// variables we have
	public int getPlayerOneScore() { return playerOneScore; }
	public int getPlayerTwoScore() { return playerTwoScore; }
	public Object getTurn() { return turn; }
	public int getNumMovesMade() { return numMovesMade; }
	public int getNumPiecesUsed() { return numPiecesUsed; }
	public int getNumPointsLeft() { return numPointsLeft; }


	/*
	 * This class is used to keep
	 * track of the location of a
	 * piece on the board.
	 */
	private static class Location {
		private int row, col;

		public Location(int r, int c) {
			row = r;
			col = c;
		}

		public int getRow() { return row; }
		public int getCol() { return col; }

		public void setRow(int r) { row = r; }
		public void setCol(int c) { col = c; }
	}
}
