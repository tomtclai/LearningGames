package HTLFunctionalAPI;
import TowerDefense.*;

/**
 * @author Tom Lai
 * @author Jeen Cherdchusilp
 */
public class Lab6 extends HTLFunctionalAPI
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
		for (int currentNum = 0; currentNum < 20; currentNum = currentNum + 1) {
			addPathLeftRight(currentNum, 5);
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
		for (int currentNum = 0; currentNum < 20; currentNum = currentNum + 1) {
			addPathLeftRight(currentNum, 2);
		}
		// 2. Make path visible
		makePathsVisible();
	}
}
