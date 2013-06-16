

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class AI implements Runnable
{
	private Semaphore ready;
	private Thread moveThread;
	private boolean continueGame;

	private int maxDepth = 10;
	private Board board;
	private int player;
	
	public AI ()
	{
		moveThread = new Thread(this);
	}

	public void set(Board B, int p)
	{
		board = B;
		player = p;
		ready = new Semaphore(0);
		continueGame = true;

		moveThread.start();
	}

	public void getBestMove()
	{
		Board newBoard = new Board (board);
		newBoard.deleteObservers();

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

						newBoard.move(from, to, currPiece);
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

						newBoard.unMove(from, to, consumed,currPiece);
					}
				}
			}
			//System.out.println("Best score: " +bestscore +"  for Player " +player +"'s " +currPiece);

		}

		HashMap<Location, Location> consumedLocs = new HashMap<Location, Location>();
		HashMap<Location, Location> notConsumedLocs = new HashMap<Location, Location>();
		
		
		for (Location loc : bestMoves.keySet()) {
			if (board.checkConsumed(loc))
			{
				consumedLocs.put(loc, bestMoves.get(loc));
				//bestMoves.remove(loc);
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
		//Make move with Bestmove
		//System.out.println("Player " + (player+1) +" From: " +bestFrom +" To: " +bestTo);

		//System.out.println(bestscore +" " +board.getValue(bestTo) +" " +board.getConsumed());
		board.move(bestFrom, bestTo);
	}


	private int min (Board newBoard, int alpha, int beta, int depth, Location prev)
	{
		if (newBoard.endOfGame() || depth == maxDepth)
		{
			return eval(newBoard);
			//int score = newBoard.getScores()[player];// - newBoard.getScores()[1-player];
			//System.out.println(score);
			//return score;
		}

		int bestscore = beta;

		Location[] pieces;

		if (player == Board.PLAYER_1) {
			pieces = newBoard.getPlayer2Pieces();
		}
		else {
			pieces = newBoard.getPlayer1Pieces();
		}


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
						boolean consumed = newBoard.checkConsumed(to);

						newBoard.move(from, to, currPiece);
						int score = max(newBoard, alpha, bestscore, depth+1, from);

						if (score < bestscore)
						{
							bestscore = score;
						}

						newBoard.unMove(from, to, consumed, currPiece);

						//Alpha prune
						if (score <= alpha)
						{
							return bestscore;
						}
					}
				}
			}
		}

		return bestscore;
	}


	private int max (Board newBoard, int alpha, int beta, int depth, Location prev)
	{
		if (newBoard.endOfGame() || depth == maxDepth)
		{
			return eval(newBoard);
			//int score = newBoard.getScores()[player];// - newBoard.getScores()[1-player];
			//System.out.println(score);
			//return score;
		}

		int bestscore = alpha;

		Location[] pieces;

		if (player == Board.PLAYER_1)
		{
			pieces = newBoard.getPlayer1Pieces();
		}
		else
		{
			pieces = newBoard.getPlayer2Pieces();
		}


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
						boolean consumed = newBoard.checkConsumed(to);

						newBoard.move(from, to, currPiece);
						int score = min(newBoard, bestscore, beta, depth+1, from);

						if (score > bestscore)
						{
							bestscore = score;
						}

						newBoard.unMove(from, to, consumed, currPiece);

						//Beta prune
						if (score >= beta)
						{
							return bestscore;
						}
					}
				}
			}
		}

		return bestscore;
	}

	public int eval(Board newBoard)
	{
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
				
				calculateMaxDepth();
				getBestMove();


			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.out.println((player+1) +" Out of loop");
	}


	private void calculateMaxDepth()
	{
		maxDepth = 8;// (int) Math.floor(board.getConsumed() / 100.0 *5) +8;
		//System.out.println(maxDepth);
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


}
