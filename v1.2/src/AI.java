
public class AI 
{
	Board board;
	
	public AI (Board B)
	{
		board = B;
	}
	
	public void move ()
	{
		String[] pieces = board.getPositions();
		
		int turn = board.getTurn();
		
		String loc1 = findBest(pieces[turn*2]);
		String loc2 = findBest(pieces[turn*2+1]);
		
		if (board.getValue(loc1) > board.getValue(loc2))
		{
			board.move(turn*2, loc1);
			//if (!board.move(turn*2, loc1))
				//System.out.println("Bloop!");
			//System.out.println(pieces[turn*2] +" " +loc1);
			
		}
		else
		{
			board.move(turn*2+1, loc2);
			//if (!board.move(turn*2+1, loc2))	
			//	System.out.println("Bloop!");
			//System.out.println(pieces[turn*2+1] +" " +loc2);
			
		}
	}
	
	private String findBest(String pos)
	{
		int row = (int)pos.charAt(0) - (int)'A';
		int col = (int)pos.charAt(1) - (int)'0';
		
		boolean u = false;
		boolean d = false;
		boolean l = false;
		boolean r = false;
		
		if (row == 0)
			u = true;
		if (row == 9)
			d = true;
		if (col == 0)
			l = true;
		if (col == 9)
			r = true;
		
		int maxR = -1, maxC = -1;
		
		for (int i = -1; i < 2; ++i)
		{
			for (int j = -1; j < 2; ++j)
			{
				if (i == 0 && j == 0)
					continue;
				
				if (!(i == -1 && u) && !(i == 1 && d) && !(j == -1 && l) && !(j == 1 && r) 
						&& !board.checkConsumed(row+i, col+j))
				{
					int curVal = board.getValue(row+i, col+j);
					
					if ((maxR == -1 && maxC == -1) || curVal > board.getValue(maxR, maxC))
					{
						maxR = row+i;
						maxC = col+j;
					}
				}
				
			}
		}
		
		String loc = "" +(char)(maxR +'A') +(maxC);
		
		if (maxR != -1 && maxC != -1)
			return loc;
		
		int i = 0;
		int j = 0;
		
		boolean found = false;
		
		while (!found)
		{
			do
			{
				i = (int) (Math.random() * 3)-1;
				j = (int) (Math.random() * 3)-1;
			} while (i == 0 && j == 0);
			
					
			if (!(i == -1 && u) && !(i == 1 && d) && !(j == -1 && l) && !(j == 1 && r))
			{
				String[] pieces = board.getPositions();
				loc =  "" +(char)(row+i +'A') +(col+j);
				
				int k;
				for (k = 0; k < pieces.length; ++k)
				{
					if (pieces[k] == pos)
						break;
				}
				
				if (!board.checkValidMove(k, loc))
					continue;
				
				found = true;
			}
			
		}

		return loc;
	}
}
