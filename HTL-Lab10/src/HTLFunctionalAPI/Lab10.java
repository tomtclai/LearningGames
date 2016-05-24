package HTLFunctionalAPI;
import TowerDefense.*;

/**
 * @author Tom Lai
 * @author Jeen Cherdchusilp
 */
public class Lab10 extends HTLFunctionalAPI
{	
	public void buildGame()
	{			
		for (int currentNum = 0; currentNum < 20; currentNum = currentNum + 1) {
			addPathLeftRight(currentNum, 5);
		}
		
		preparePathForWalkers(0,5,19,5);
		// either Walker or QuickWalker, pick one  
//		addWalkers();
		addQuickWalkers();
		drawMedicWizard(19,6);
		setHUDVisibilityTo(true);
		setWalkerDamagePerSecond(1);
	}
	
	public void updateGame() {
		
		// in-game
		
		if (gameIsOver()) {
			if (userWon()) {
				enterWin();
			} else {
				enterLose();
			}
		} 
		
		if (mouseClicked()) {
			int clickedRow = getClickedRow();
			int clickedColumn = getClickedColumn();
			if(gameIsOver()) { // if game is over, check for button clicks
				if (userWon()) {
					if (winRestartButtonSelected()) { // did user click on the restart button?
						enterGameplay();
					} else if (winQuitButtonSelected()) { // or did user click on the quit button?
						exitGame();
					}
				} else {
					if (loseRestartButtonSelected()) {
						enterGameplay();
					} else if (loseQuitButtonSelected()) {
						exitGame();
					}
				}
			} else {
				// if a Tower is selected, can it be moved to this Tile?
				if (aTowerIsSelected()) {
					moveTowerTo(clickedColumn, clickedRow);
				}
				// otherwise, if there's a Tower on the tile, toggle selection
				// of the tower
				else if (tileHasTower(clickedColumn, clickedRow)) {

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

		}
		
		
		// heal walkers or make walkers faster
		for (int i = 0; i < numOfTowers(); i++) {
			for (int j = 0; j < numOfWalkers(); j++) {
				if (towerShouldFire(i, j)) {
					// either speedy or medic, pick one
					// towerCastMedicSpellOnWalker(i, j);
					speedyWizardCastSpellOnWalker(i, j);
				}
			}
		}


	}
}
