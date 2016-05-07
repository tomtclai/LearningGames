package HTLFunctionalAPI;
import TowerDefense.*;

/**
 * @author Tom Lai
 * @author Jeen Cherdchusilp
 */
public class Lab8 extends HTLFunctionalAPI
{	
	public void buildWorld()
	{
		super.buildWorld();
		
		
		for (int currentNum = 0; currentNum < 20; currentNum = currentNum + 1) {
			addPathLeftRight(currentNum, 5);
		}

		makePathVisible();
		
		preparePathForWalkers(0,5,19,5);
		
		
		addBasicWalker();
	}
	
	public void updateWorld() {
		updateWalkers();
	}
}
