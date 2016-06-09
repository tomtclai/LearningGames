package HTLProceduralAPI;

/**
 * @author Tom Lai
 * @author Jeen Cherdchusilp
 */
public class Lab7 extends HTLProceduralAPI
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
		
		/* PRE-BUILT PATH USED FOR DEMONSTRATION
 		addPathLeftRight(0, 5);
		addPathLeftRight(1, 5);
		addPathLeftRight(2, 5);
		addPathLeftRight(3, 5);
		addPathLeftRight(4, 5);
		addPathLeftRight(5, 5);
		addPathLeftRight(6, 5);
		addPathLeftRight(7, 5);
		addPathLeftRight(8, 5);
		addPathLeftRight(9, 5);
		addPathLeftRight(10, 5);
		addPathLeftRight(11, 5);
		addPathLeftRight(12, 5);
		addPathLeftRight(13, 5);
		addPathLeftRight(14, 5);
		addPathLeftRight(15, 5);
		addPathLeftRight(16, 5);
		addPathLeftRight(17, 5);
		addPathLeftRight(18, 5);
		addPathLeftRight(19, 5);
		*/
		
		for (int currentNum = 0; currentNum < 20; currentNum = currentNum + 1) {
			addPathLeftRight(currentNum, 5);
		}
		preparePathForWalkers(0,5,19,5);
	}
	
	public void updateGame() {

		if (countdownFired()) {
			addBasicWalker();
		}
	}
}


