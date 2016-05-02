package HTLFunctionalAPI;
import TowerDefense.*;

/**
 * @author Tom Lai
 * @author Jeen Cherdchusilp
 */
public class Lab6 extends HTLFunctionalAPI
{
	
	public Lab6()
	{
		super();
	}
	
	public void initializeWorld()
	{
		super.initializeWorld();
		// 1. Set dimension
		setDimension(20,10);
		// 2. Make a path
		/*      9| 
		 *      8| 
		 *      7| 
		 *      6|               X X X
		 * rows 5| X X X X X X X X   X
		 *      4|               X X X 
		 *      3|               X
		 *      2|               X
		 *      1|               X
		 *      0|_______________X______
		 *         0 1 2 3 4 5 6 7 8 9 ...
		 *            columns
		 */
		for (int i = 0; i < 7; i++) {
			addPathLeftRight(i, 5);
		}
		addPathUpLeft(7, 5);
		addPathDownRight(7, 6);
		addPathLeftRight(8, 6);
		addPathDownLeft(9, 6);
		addPathUpDown(9, 5);
		addPathUpLeft(9, 4);
		addPathLeftRight(8, 4);
		addPathDownRight(7, 4);
		for (int i = 3; i >= 0; i--) {
			addPathUpDown(7, i);
		}
		// 3. Make path visible
		makePathVisible();
	}
	
	public void updateWorld()
	{
		super.updateWorld();
		
	}
}
