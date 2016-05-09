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
	Vector<Walker> walkers = new Vector<Walker>();
	public void initializeWorld()
	{
		super.initializeWorld();
		setHUDVisibilityTo(false);
		buildGame();
	}

	public void updateWorld()
	{
		super.updateWorld();
		towerSet.update();
		updateGame();
		
	}

	/**
	 * The function to be overridden by the student. This method should be used
	 * to "build the world" of the game by drawing key elements.
	 */
	public void buildGame(){
		
	}
	
	public void updateGame() {
	
	}
	
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
	 * Marks the corresponding Tile as being a part of the Path, with the
	 * ability to connect to Tiles to the top and bottom. This means that when
	 * the Path is created using constructPath(), this Tile can be included in
	 * the Path.
	 * 
	 * @param column
	 *            Column of the Tile to include in the Path
	 * @param row
	 *            Row of the Tile to include in the Path
	 * @return True if Tile was successfully marked
	 */
	public boolean addPathUpDown(int x, int y) {
		return grid.addPathUpDown(x, y);
	}

	/**
	 * Marks the corresponding Tile as being a part of the Path, with the
	 * ability to connect to Tiles to the left and right. This means that when
	 * the Path is created using constructPath(), this Tile can be included in
	 * the Path.
	 * 
	 * @param column
	 *            Column of the Tile to include in the Path
	 * @param row
	 *            Row of the Tile to include in the Path
	 * @return True if Tile was successfully marked
	 */
	public boolean addPathLeftRight(int x, int y) {
		return grid.addPathLeftRight(x, y);
	}

	/**
	 * Marks the corresponding Tile as being a part of the Path, with the
	 * ability to connect to Tiles to the left and top. This means that when
	 * the Path is created using constructPath(), this Tile can be included in
	 * the Path.
	 * 
	 * @param column
	 *            Column of the Tile to include in the Path
	 * @param row
	 *            Row of the Tile to include in the Path
	 * @return True if Tile was successfully marked
	 */
	public boolean addPathUpLeft(int x, int y) {
		return grid.addPathUpLeft(x, y);
	}

	/**
	 * Marks the corresponding Tile as being a part of the Path, with the
	 * ability to connect to Tiles to the right and top. This means that when
	 * the Path is created using constructPath(), this Tile can be included in
	 * the Path.
	 * 
	 * @param column
	 *            Column of the Tile to include in the Path
	 * @param row
	 *            Row of the Tile to include in the Path
	 * @return True if Tile was successfully marked
	 */
	public boolean addPathUpRight(int x, int y) {
		return grid.addPathUpRight(x, y);
	}

	/**
	 * Marks the corresponding Tile as being a part of the Path, with the
	 * ability to connect to Tiles to the left and bottom. This means that when
	 * the Path is created using constructPath(), this Tile can be included in
	 * the Path.
	 * 
	 * @param column
	 *            Column of the Tile to include in the Path
	 * @param row
	 *            Row of the Tile to include in the Path
	 * @return True if Tile was successfully marked
	 */
	public boolean addPathDownLeft(int x, int y) {
		return grid.addPathDownLeft(x, y);
	}

	/**
	 * Marks the corresponding Tile as being a part of the Path, with the
	 * ability to connect to Tiles to the right and bottom. This means that when
	 * the Path is created using constructPath(), this Tile can be included in
	 * the Path.
	 * 
	 * @param column
	 *            Column of the Tile to include in the Path
	 * @param row
	 *            Row of the Tile to include in the Path
	 * @return True if Tile was successfully marked
	 */
	public boolean addPathDownRight(int x, int y) {
		return grid.addPathDownRight(x, y);
	}

	
	/**
	 * Make paths in the grid visible.
	 * Note that Tiles without paths are never drawn,
	 * because it ends up being really expensive.
	 * @param isVisible		True if Path Tiles should be visible.
	 */
	public void makePathVisible() {
		grid.setPathTileVisibilityTo(true);
	}
	
	/**
	 * It is necessary to call this method so that walkers can walk on a custom path.
	 * @param startColumn		The column of the Tile where the Path begins.
	 * @param startRow			The row of the Tile where the Path begins.
	 * @param endColumn			The column of the Tile where the Path ends.
	 * @param endRow			The row of the Tile where the Path ends.
	 * @return					True if the Path was successfully prepared.
	 */
	public boolean preparePathForWalkers(int startColumn, int startRow, int endColumn, int endRow) {
		return grid.constructPath(startColumn,startRow,endColumn,endRow);
	}
	
	public void addWalker() {
		walkers.add(new WalkerBasic(grid.getPath()));
	}
	public void updateWalkers() {
		for (Walker w: walkers) {
			w.update();
		}
	}
}
