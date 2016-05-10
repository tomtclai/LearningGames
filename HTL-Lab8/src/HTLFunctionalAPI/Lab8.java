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

			Tile clickedTile = grid.getClickedTile();
			if (clickedTile == null)
				return;

			// if a Tower is selected, can it be moved to this Tile?
			if (selectedTower != null && clickedTile.isAvailable() && !clickedTile.hasPath()) {
				selectedTower.teleportTo(clickedTile);
				unselectTower();
				selectedTower.playSoundMove();
			}
			// otherwise, if there's no Tower selected, can we place a
			// Tower?
			else if (clickedTile.isAvailable() && !clickedTile.hasPath()) {
				towerSet.addTowerAt(clickedTile, true);
			}
			// otherwise, if there's a Tower on the tile, toggle selection
			// of the tower
			else if (clickedTile.hasOccupant()) {
				Tower clickedTower = clickedTile.getOccupant();

				if (clickedTower == selectedTower) {
					unselectTower();
				} else {
					selectTower(clickedTower);
				}
			}

		}
	}
}
