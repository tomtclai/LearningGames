package HTLFunctionalAPI;
import TowerDefense.*;

/**
 * @author Tom Lai
 * @author Jeen Cherdchusilp
 */
public class Lab7 extends HTLFunctionalAPI
{	
	public void initializeWorld()
	{
		super.initializeWorld();
		
		// 1. Make paths
		/*      9| 
		 *      8| 
		 *      7| 
		 *      6|               
		 * rows 5| X X X X X X X X X X X
		 *      4|                    
		 *      3|                
		 *      2|                
		 *      1|                
		 *      0|______________________
		 *         0 1 2 3 4 5 6 7 8 9 ...
		 *            columns
		 */
		
		int currentNum1 = 0;
		while (currentNum1 < 20) {
			addPathLeftRight(currentNum1, 5);
			currentNum1 = currentNum1 + 1;
		}

		/*      9| 
		 *      8| 
		 *      7| 
		 *      6|               
		 * rows 5| X X X X X X X X X X X
		 *      4|                    
		 *      3|                
		 *      2| X X X X X X X X X X X               
		 *      1|                
		 *      0|______________________
		 *         0 1 2 3 4 5 6 7 8 9 ...
		 *            columns
		 */
		int currentNum2 = 0;
		while (currentNum2 < 20) {
			addPathLeftRight(currentNum2, 2);
			currentNum2 = currentNum2 + 1;
		}
		
		// 2. Make path visible
		makePathsVisible();
	}
}
