

public class RandGen 
{
	private static final int MAX_RAND_VALUE = 6;
	private static final int MAX_RAND_TBL_ROW = 10;
	private static final int MAX_RAND_TBL_COL = 10;
	
	private static int[][] rand_tbl = new int[MAX_RAND_TBL_ROW][MAX_RAND_TBL_COL];
	
	public static int[][] Generate (long seed) 
	{
		//Scanner stdin = new Scanner(System.in);
		long modulus, multiplier, increment;//, seed;
		int k, r,c;
		int[] freq_count = new int[MAX_RAND_VALUE+1];
							/* freq_count[0] is not used */
		
		for (k = 1; k <= MAX_RAND_VALUE; ++k)
			freq_count[k] = 0;
		
		/* initialize modulus to 2^32 */
		modulus = 1;
		for ( k = 1; k <= 32; ++k)
			modulus *= 2;
		
		multiplier = 1103515245L;
		increment = 12345L;
		
		//System.out.println("Enter the value for seed ( 0 - " + (modulus -1) + ")");
		
		//seed = stdin.nextLong();
		
		//System.out.println();
		//System.out.println();
		
		for ( r = 0; r < MAX_RAND_TBL_ROW; ++r)
		{
			for ( c = 0; c < MAX_RAND_TBL_COL; ++c)
			{
				rand_tbl[r][c] = (int) (seed % MAX_RAND_VALUE) + 1;
				++freq_count[ rand_tbl[r][c] ];
				//System.out.printf("%2d", rand_tbl[r][c]);
				seed = ((multiplier * seed) + increment) % modulus;
			}/* end for (columns) */
			//System.out.println();
		}/* end for (rows) */

		//System.out.println();
		//System.out.println();
		
		//System.out.print("freq_count[1:" + MAX_RAND_VALUE + "] =");
		//for ( k = 1; k <= MAX_RAND_VALUE; ++k)
		//	System.out.printf(" %d", freq_count[k]);
		

		//System.out.println();
		//System.out.println();
		
		return rand_tbl;
	}/* end main */
}/* end public class RandGen */
