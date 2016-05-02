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
	
	/**
	 * Initializes the game's Grid.
	 * 	1. Sets the Grid size.
	 * 	2. Makes specific Tiles into Path Tiles.
	 * 	3. Connects the Path Tiles into a Path.
	 * 	4. Hooks the WaveSystem (spawner) up to use this Path.
	 *  5. Sets the background to a custom background that is pretty and not empty
	 *  6. Sets Tiles with paths to be invisible, since the background already contains a visual path.
	 * 
	 * Precondition: spawner object has been initialized
	 * 
	 * The specific path that this method makes is one that aligns
	 * with the default background image of this game.  Because of
	 * this design decision, we don't actually draw the path by default.
	 * However, this is only the default initialization.
	 * If you want, you can reshape this grid to be whatever you want,
	 * even after calling this method.
	 */
	public void createPlaygroundPath()
	{
		// 1. Set grid size
		grid.setDimensions(20, 10);
		
		// 2. Set path tile placement
		placePlaygroundPathTiles();
		
		// 3. Construct the path.
		grid.constructPath(0, 5, 7, 0); 
		Path thePath = grid.getPath();
		
		// 4. Pass the path to the spawner.
		spawner.setPath(thePath);
		
		// 5. Set up blocking zones
		placeBlockingTiles();
		
		background.setImage(IMAGE_BACKGROUND_EMPTY);
		grid.setPathTileVisibilityTo(true);
	}
	/**
	 * Turns the Tiles that align with the path on the
	 * default background image into Path Tiles.
	 * This is used to create a path,
	 * and the Tiles use it so that they know to draw differently (if tiles are being drawn).
	 */
	private void placePlaygroundPathTiles() {
		/*      9| 
		 *      8| 
		 *      7| 
		 *      6|               X X X
		 * rows 5| X X X X X X X X   X
		 *      4|               X X X 
		 *      3|               X
		 *      2|               X
		 *      1|               X
		 *      0|_______________X______
		 *         0 1 2 3 4 5 6 7 8 9 ...
		 *            columns
		 */
		
		// Starting from left
		for (int i = 0; i < 7; i++) {
			grid.addPathLeftRight(i, 5);
		}
		grid.addPathUpLeft(7, 5);
		grid.addPathDownRight(7, 6);
		grid.addPathLeftRight(8, 6);
		grid.addPathDownLeft(9, 6);
		grid.addPathUpDown(9, 5);
		grid.addPathUpLeft(9, 4);
		grid.addPathLeftRight(8, 4);
		grid.addPathDownRight(7, 4);
		for (int i = 3; i >= 0; i--) {
			grid.addPathUpDown(7, i);
		}
		grid.setPathTileVisibilityTo(true);
	}
}
