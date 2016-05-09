package HTLFunctionalAPI;
import TowerDefense.*;

/**
 * @author Tom Lai
 * @author Jeen Cherdchusilp
 */
public class Lab8 extends HTLFunctionalAPI
{	
	public void buildGame()
	{			
		for (int currentNum = 0; currentNum < 20; currentNum = currentNum + 1) {
			addPathLeftRight(currentNum, 5);
		}

		makePathVisible();
		
		preparePathForWalkers(0,5,19,5);
		
		addWalker();
	
	}
	
	public void updateWorld() {
		
		updateWalkers();
		
		if(mouse.isButtonTapped(1)) {
			Tile clickedTile = grid.getClickedTile();
			if(clickedTile == null) return;		
			towerSet.addTowerAt(clickedTile, true);
		}
	}
}
