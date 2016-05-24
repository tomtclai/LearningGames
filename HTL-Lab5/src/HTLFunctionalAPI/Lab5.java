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
			int clickedRow = getClickedRow();
			int clickedColumn = getClickedColumn();
			
			// if a Tower is selected, can it be moved to this Tile?
			if (aTowerIsSelected()) {
				moveTowerTo(clickedColumn, clickedRow);
			}
			// otherwise, if there's a Tower on the tile, toggle selection
			// of the tower
			else if (tileHasTower(clickedRow,clickedColumn)) {

				if (towerIsSelected(clickedColumn, clickedRow)) {
					unselectTower();
				} else {
					selectTower(clickedColumn, clickedRow);
				}
			}
			// otherwise, place a Tower
			else {
				// either speedy or medic, pick one
				// drawMedicWizard(clickedColumn, clickedRow);
				drawSpeedyWizard(clickedColumn, clickedRow);
				
			}

		}
		makeTowersFire();
		
		
	}
}
