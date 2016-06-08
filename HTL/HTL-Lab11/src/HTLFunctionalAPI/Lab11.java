package HTLFunctionalAPI;

import TowerDefense.*;

/**
 * @author Tom Lai
 * @author Jeen Cherdchusilp
 */
public class Lab11 extends HTLFunctionalAPI {
	String defaultWizardType = "medic";
	String defaultWalkerType = "basic";
	public void buildGame() {
		drawToolbars();
		for (int currentNum = 0; currentNum < 20; currentNum = currentNum + 1) {
			addPathLeftRight(currentNum, 5);
		}

		preparePathForWalkers(0, 5, 19, 5);
		setWalkerDamagePerSecond(5);
		setCountdownFrom(1);
		setScoreToWin(1);
		
		// Settings
		setSpeedyTimeBetweenSpellcasts(0.0); 
		setMedicTimeBetweenSpellcasts(3.0);
		setMedicWizardHealthAdjust(10.0);
		setSpeedyWizardSpeedBoostDuration(10.0);
		setSpeedyWizardSpeedBoostMultipler(3.0);
	}

	public void updateGame() {

		if (countdownFired()) {
			addWalker(defaultWalkerType);
		}

		// in-game

		if (userWon()) {
			enterWin();
		}

		if (mouseClicked()) {
			int clickedRow = getClickedRow();
			int clickedColumn = getClickedColumn();
			if (userWon()) { // if user won, check for button clicks
				if (winRestartButtonSelected()) { // did user click on the
													// restart button?
					enterGameplay();
				} else if (winQuitButtonSelected()) { // or did user click on
														// the quit button?
					exitGame();
				}
			} else {
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
					// either speedy or medic
					drawWizard(clickedColumn, clickedRow, defaultWizardType);
				}
			}

		}

		if (keyboardIsPressingLeft()) {
			defaultWizardType = "medic";
		} else if (keyboardIsPressingRight()) {
			defaultWizardType = "speedy";
		} else if (keyboardIsPressingUp()) {
			defaultWalkerType = "quick";
		} else if (keyboardIsPressingUp()) {
			defaultWalkerType = "basic";
		}

		// heal walkers or make walkers faster
		for (int i = 0; i < numOfWizards(); i++) {
			if (towerIsReady(i)) {
				for (int j = 0; j < numOfWalkers(); j++) {
					if (walkerIsInRange(i, j)) {
						if (wizardIsMedic(i)) {
							medicWizardCastSpellOnWalker(i, j);
						} else {
							speedyWizardCastSpellOnWalker(i, j);
						}
					}
				}
			}
		}

		setScore(getNumOfWalkersSaved() * getHealthSaved());

	}
}
