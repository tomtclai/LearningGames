package HTLFunctionalAPI;
import TowerDefense.*;

/**
 * @author Tom Lai
 * @author Jeen Cherdchusilp
 */
public class Lab5 extends HTLFunctionalAPI
{	
	public void updateGame() {
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
				// either speedy or medic, pick one
				// addMedicTowerAtClickedTile();
				addSpeedyTowerAtClickedTile();
			}

		}
		makeTowersFire();
		
		
	}
}
