package HTLFunctionalAPI;
import java.awt.event.KeyEvent;
import java.util.*;
import TowerDefense.*;

/**
 * This is a functional API for the game Hug The Line (HTL).
 * It is designed for use the introductory computer science lessons.
 * 
 * @author Kunlakan (Jeen) Cherdchusilp
 * @author Tom Lai
 *
 */

public class HTLFunctionalAPI extends HTL
{
	public void initializeWorld()
	{
		super.initializeWorld();
		setHUDVisibilityTo(false);
		buildWorld();
	}

	public void updateWorld()
	{
		super.updateWorld();
		towerSet.update();
	}

	/**
	 * The function to be overridden by the student. This method should be used
	 * to "build the world" of the game by drawing key elements.
	 */
	public void buildWorld(){}
	
	//-------------------------------------------------------------------------
	
	/**
	 * Draws a medic tower at a random tile.
	 */
	public void drawMedicWizard()
	{		
		Random random = new Random();
		int x = random.nextInt((int)SCREEN_WIDTH - 1);
		int y = random.nextInt((int)SCREEN_HEIGHT);

		drawMedicWizard(x, y);
	}
	
	/**
	 * Draws a medic tower at the given x and y coordinate.
	 * @param x		The x coordinate of the position of the medic wizard
	 * @param y		The y coordinate of the position of the medic wizard
	 */
	public void drawMedicWizard(int x, int y)
	{
		Tile position = grid.getTile(x, y);
		towerSet.addTowerAt(position, true);
	}

	/**
	 * Draws a speedy tower at a random tile.
	 */
	public void drawSpeedyWizard()
	{
		Random random = new Random();
		int x = random.nextInt((int)SCREEN_WIDTH - 1);
		int y = random.nextInt((int)SCREEN_HEIGHT);

		drawSpeedyWizard(x, y);
	}
	
	/**
	 * Draws a speedy tower at the given x and y coordinate.
	 * @param x		The x coordinate of the position of the speedy wizard
	 * @param y		The y coordinate of the position of the speedy wizard
	 */
	public void drawSpeedyWizard(int x, int y)
	{
		Tile position = grid.getTile(x, y);
		towerSet.addTowerAt(position, false);
	}
	
	/**
	 * @return true if the user is pressing the up arrow
	 */
	public boolean keyboardIsPressingUp() {
		return keyboard.isButtonTapped(KeyEvent.VK_UP);
	}

	/**
	 * @return true if the user is pressing the down arrow
	 */
	public boolean keyboardIsPressingDown() {
		return keyboard.isButtonTapped(KeyEvent.VK_DOWN);
	}

	/**
	 * @return true if the user is pressing the left arrow
	 */
	public boolean keyboardIsPressingLeft() {
		return keyboard.isButtonTapped(KeyEvent.VK_LEFT);
	}

	/**
	 * @return true if the user is pressing the right arrow
	 */
	public boolean keyboardIsPressingRight() {
		return keyboard.isButtonTapped(KeyEvent.VK_RIGHT);
	}
}
