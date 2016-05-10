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
	
	public void updateGame() {
		
		updateWalkers();
		// in-game
		if (mouseClicked()) {

			// if a Tower is selected, can it be moved to this Tile?
			if (aTowerIsSelected()) {
				moveTowerToClickedTile();
			}
			// otherwise, if there's a Tower on the tile, toggle selection
			// of the tower
			else if (clickedTileHasTower()) {

				if (clickedTowerIsSelected()) {
					unselectTower();
				} else {
					selectClickedTower();
				}
			}
			// otherwise, place a Tower
			else {
				addTowerAtClickedTile();
			}

		}
	}
}
