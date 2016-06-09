package HTLFunctionalAPI;
import HTLProceduralAPI.HTLProceduralAPI;
import TowerDefense.*;

/**
 * @author Tom Lai
 * @author Jeen Cherdchusilp
 */
public class Lab9 extends HTLProceduralAPI
{	
	public void buildGame()
	{			
		for (int currentNum = 0; currentNum < 20; currentNum = currentNum + 1) {
			addPathLeftRight(currentNum, 5);
		}
		
		preparePathForWalkers(0,5,19,5);
		setCountdownFrom(3);
	}
	
	public void updateGame() {
		
		if (countdownFired()) {
			addQuickWalker();
		}

		// in-game
		if (mouseClicked()) {
			int clickedRow = getClickedRow();
			int clickedColumn = getClickedColumn();
			
			// if a Tower is selected, can it be moved to this Tile?
			if (aWizardIsSelected()) {
				moveWizardTo(clickedColumn, clickedRow);
			}
			// otherwise, if there's a Tower on the tile, toggle selection
			// of the tower
			else if (tileHasWizard(clickedColumn, clickedRow)) {

				if (wizardIsSelected(clickedColumn, clickedRow)) {
					unselectWizard();
				} else {
					selectWizard(clickedColumn, clickedRow);
				}
			}
			// otherwise, place a Tower
			else {
				// either speedy or medic, pick one
				// drawMedicWizard(clickedColumn, clickedRow);
				drawSpeedyWizard(clickedColumn, clickedRow);
				
			}

		}
		
		
		// heal walkers or make walkers faster
		for (int i = 0; i < numOfWizards(); i++) {
			for (int j = 0; j < numOfWalkers(); j++) {
				if (walkerIsInRange(i, j)) {
					// either speedy or medic, pick one
					// medicWizardCastSpellOnWalker(i, j);
					speedyWizardCastSpellOnWalker(i, j);
				}
			}
		}

		
	}
}
