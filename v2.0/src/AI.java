

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class AI implements Runnable
{
	private Semaphore ready;
	private Thread moveThread;
	private boolean continueGame;

	private int maxDepth = 9;
	private Board board;
	private int player;
	//private long totalTime;
	
	
	public AI ()
	{
		moveThread = new Thread(this);
	}

	
	public void set(Board B, int p)
	{
		//totalTime = 0;
		board = B;
		player = p;
		ready = new Semaphore(0);
		continueGame = true;

		moveThread.start();
	}
	

	public int findBestStart()
	{
		int bestCornerValue = -1;
		int corner = -1;
		
		//Loop through both corner selections
		//and test to see which will yield a 
		//better score.
		for (int currCorner = 0; currCorner < 2; ++currCorner)
		{
			Board newBoard = new Board (board);
			newBoard.deleteObservers();

			newBoard.pickStart(currCorner);
			
			calculateMaxDepth();
			
			Move move = getBestMove(newBoard);
			
			if (move.value > bestCornerValue)
			{
				bestCornerValue = move.value;
				corner = currCorner;
			}
		}
		
		return corner;
	}
	
	
	public Move getBestMove(Board newBoard)
	{
		int bestscore = Integer.MIN_VALUE;

		Location[] pieces;

		if (player == Board.PLAYER_1)
		{
			pieces = newBoard.getPlayer1Pieces();
		}
		else
		{
			pieces = newBoard.getPlayer2Pieces();
		}

		
		HashMap<Location, Location> bestMoves = new HashMap<Location, Location>();
	
		for (int currPiece = 0; currPiece < Board.NUM_PIECES_PER_PLAYER; ++currPiece)
		{
			Location from = pieces[currPiece];
			int row = from.getRow();
			int col = from.getCol();

			boolean u = row == 0;
			boolean d = row == 9;
			boolean l = col == 0;
			boolean r = col == 9;

			for (int currRow = -1; currRow < 2; ++currRow)
			{
				for (int currCol = -1; currCol < 2; ++currCol)
				{
					if (currRow == 0 && currCol == 0)
						continue;

					Location to = new Location(from.getRow()+currRow, from.getCol()+currCol);

					//If valid move.
					if (!(currRow == -1 && u) && !(currRow == 1 && d) && !(currCol == -1 && l) && !(currCol == 1 && r) 
							&& !newBoard.doesPieceExist(to))
					{
						/*
						 * save if tile was consumed
						 * make move m (from, to) in board.
						 * score = min(Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
						 * if (score > bestscore)
						 * {
						 * 		bestscore = score
						 * 		bestmove = m
						 * }
						 * unmake move m in board, passing if tile was consumed.
						 */

						boolean consumed = newBoard.checkConsumed(to);

						newBoard.moveAI(from, to, currPiece, 0, maxDepth);
						int score = min(newBoard, Integer.MIN_VALUE, Integer.MAX_VALUE, 1, from);

						
						if (score == bestscore)
						{
							bestMoves.put(to, from);
						}
						
						if (score > bestscore)
						{
							bestscore = score;

							bestMoves.clear();
							bestMoves.put(to, from);
						}

						//System.out.println("Player " + (player+1) +" From: " +from +" To: " +to +" Score: " +score);

						newBoard.unMoveAI(from, to, consumed,currPiece, 0, maxDepth);
					}
				}
			}
			//System.out.println("Best score: " +bestscore +"  for Player " +player +"'s " +currPiece);
		}

		HashMap<Location, Location> consumedLocs = new HashMap<Location, Location>();
		HashMap<Location, Location> notConsumedLocs = new HashMap<Location, Location>();
		
		
		for (Location loc : bestMoves.keySet()) 
		{
			if (board.checkConsumed(loc))
			{
				consumedLocs.put(loc, bestMoves.get(loc));
			}
			else
			{
				notConsumedLocs.put(loc, bestMoves.get(loc));
			}
		}
		

		Location bestFrom = null;
		Location bestTo = null;

		Random random = new Random();
		
		if(!notConsumedLocs.isEmpty())
		{
			ArrayList<Location> keys = new ArrayList<Location>(notConsumedLocs.keySet());
			bestTo = keys.get(random.nextInt(keys.size()));
			bestFrom = notConsumedLocs.get(bestTo);
		}
		else 
		{
			ArrayList<Location> keys = new ArrayList<Location>(consumedLocs.keySet());
			bestTo = keys.get(random.nextInt(keys.size()));
			bestFrom = consumedLocs.get(bestTo);
		}

		//Display bestmove to user
		
		//System.out.println("Player " + (player+1) +" From: " +bestFrom +" To: " +bestTo);
		//System.out.println(bestscore +" " +(board.checkConsumed(bestTo)? 0: board.getValue(bestTo)) +" " +board.getConsumed());
		
		return new Move(bestFrom, bestTo, bestscore);
	}


	private int min (Board newBoard, int alpha, int beta, int depth, Location prev)
	{
		if (newBoard.endOfGame() || depth == maxDepth)
		{
			return eval(newBoard);
		}

		int bestscore = beta;
		
		Location[] pieces;

		//Get opponent's pieces.
		if (player == Board.PLAYER_1) {
			pieces = newBoard.getPlayer2Pieces();
		}
		else {
			pieces = newBoard.getPlayer1Pieces();
		}

		//Get a list of all possible moves.
		Move[] allMoves = getAllMoves(newBoard, pieces, prev);
		
		for (Move currMove: allMoves)
		{
			boolean consumed = newBoard.checkConsumed(currMove.to);
	
			newBoard.moveAI(currMove.from, currMove.to, currMove.currPiece, depth, maxDepth);
			int score = max(newBoard, alpha, bestscore, depth+1, currMove.from);
	
			if (score < bestscore)
			{
				bestscore = score;
			}
	
			newBoard.unMoveAI(currMove.from, currMove.to, consumed, currMove.currPiece, depth, maxDepth);
	
			//Alpha prune
			if (score <= alpha)
			{
				return bestscore;
			}
		}
		
		return bestscore;
	}


	private int max (Board newBoard, int alpha, int beta, int depth, Location prev)
	{
		if (newBoard.endOfGame() || depth == maxDepth)
		{
			return eval(newBoard);
		}

		int bestscore = alpha;

		Location[] pieces;

		//Get AI's game pieces.
		if (player == Board.PLAYER_1) {
			pieces = newBoard.getPlayer1Pieces();
		}
		else {
			pieces = newBoard.getPlayer2Pieces();
		}
		
		//Get a list of all possible moves.
		Move[] allMoves = getAllMoves(newBoard, pieces, prev);
		
		for (Move currMove: allMoves)
		{
			boolean consumed = newBoard.checkConsumed(currMove.to);
	
			newBoard.moveAI(currMove.from, currMove.to, currMove.currPiece, depth, maxDepth);
			
			int score = min(newBoard, bestscore, beta, depth+1, currMove.from);
	
			if (score > bestscore)
			{
				bestscore = score;
			}
	
			newBoard.unMoveAI(currMove.from, currMove.to, consumed, currMove.currPiece, depth, maxDepth);
	
			//Beta prune
			if (score >= beta)
			{
				return bestscore;
			}
		}
		
		return bestscore;
	}
	
	
	private Move[] getAllMoves(Board newBoard, Location[] pieces, Location prev)
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for (int currPiece = 0; currPiece < Board.NUM_PIECES_PER_PLAYER; ++currPiece)
		{
			Location from = pieces[currPiece];
			int row = from.getRow();
			int col = from.getCol();

			boolean u = row == 0;
			boolean d = row == 9;
			boolean l = col == 0;
			boolean r = col == 9;

			for (int currRow = -1; currRow < 2; ++currRow)
			{
				for (int currCol = -1; currCol < 2; ++currCol)
				{
					if (currRow == 0 && currCol == 0)
						continue;

					Location to = new Location(from.getRow()+currRow, from.getCol()+currCol);

					if (to.equals(prev))
						continue;

					//If valid move.
					if (!(currRow == -1 && u) && !(currRow == 1 && d) && !(currCol == -1 && l) && !(currCol == 1 && r) 
							&& !newBoard.doesPieceExist(to))
					{
						Move curr = new Move(from, to, newBoard.checkConsumed(to)? 0 : newBoard.getValue(to), currPiece);
						
						//Never move Horizontally or Vertically to consumed tiles.
						if (curr.value != 0 || curr.isCornerMove())
							moves.add(curr);
					}
				}
			}
		}
		
		Move[] allMoves = new Move[moves.size()];
		moves.toArray(allMoves);
		
		Arrays.sort(allMoves);
		
		return allMoves;
	}
	

	private int eval(Board newBoard)
	{
		//AI score is calculated during each move.
		//The score is weighted by distance traveled to 
		//get the sum of pieces.
		return newBoard.getScores()[player];
	}

	
	public void run() {
		while (!board.endOfGame() || continueGame)
		{
			try {
				ready.acquire();
				
				if (board.endOfGame() || !continueGame)
				{
					break;
				}
				
				//long st = System.nanoTime();
				
				calculateMaxDepth();
				
				Board newBoard = new Board (board);
				newBoard.deleteObservers();

				Move move = getBestMove(newBoard);
				
				// Insert the 'stall' here
				//waitForPlayer(move.from, move.to);
				
				board.move(move.from, move.to);
				
				//long et = System.nanoTime();
				//totalTime += et -st;
				
				//System.out.println("Player" +(player+1) +": " +(et - st)/1000000000 +"s, Total: " + (totalTime/1000000000)/60 +":"+String.format("%02d", (totalTime/1000000000)%60));
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	private void calculateMaxDepth()
	{
		//Even numbers means eval is called in max.
		//Odd numbers means eval is called in min.
		
		//We need a smoother function.
		// 9 when < 50 tiles consumed, 11 >= 50 tiles consumed.
		//keep this time. a depth of 13 takes too long and we will pass the 3 minute mark.
		
		int add = (int) Math.floor(board.getConsumed() / 100.0 * 4);
		add += 1 - add % 2;
		maxDepth =  add + 8;
		//System.out.println(maxDepth +" => " +board.getConsumed());
	}
	
	
	public void endThread()
	{
		continueGame = false;
		ready.release();
	}
	
	
	public void signalReady()
	{
		ready.release();
	}
	
	
	public int getPlayer()
	{
		return player;
	}

	
	private class Move implements Comparable<Move>
	{
		Location from;
		Location to;
		int value;
		int currPiece;
		
		public Move (Location f, Location t, int v, int cp)
		{
			from = f;
			to = t;
			value = v;
			currPiece = cp;
		}
		
		
		public Move (Location f, Location t, int v)
		{
			from = f;
			to = t;
			value = v;
			currPiece = -1;
		}
		
		
		public boolean isCornerMove()
		{
			return (from.getRow() != to.getRow() && from.getCol() != to.getCol());
		}

		
		@Override
		public int compareTo(Move lhs) {
			return lhs.value - value;
		}
	}
	
}
