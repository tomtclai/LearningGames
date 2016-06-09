package HTLProceduralAPI;
import HTLProceduralAPI.HTLProceduralAPI;
import TowerDefense.*;

/**
 * @author Tom Lai
 * @author Jeen Cherdchusilp
 */
public class Lab8 extends HTLProceduralAPI
{	
	public void buildGame()
	{
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
		
		int currentNum = 0;
		while (currentNum < 20) {
			addPathLeftRight(currentNum, 5);
			currentNum = currentNum + 1;
		}

	}
	
	public void updateGame() {

		if (countdownFired()) {
			addBasicWalker();
		}
	}
}
