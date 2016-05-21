package HTLFunctionalAPI;

import java.awt.event.KeyEvent;
import java.util.*;

import Engine.Text;
import Engine.Vector2;
import TowerDefense.*;

/**
 * This is a functional API for the game Hug The Line (HTL). It is designed for
 * use the introductory computer science lessons.
 * 
 * @author Kunlakan (Jeen) Cherdchusilp
 * @author Tom Lai
 *
 */

public class HTLFunctionalAPI extends HTL {
	private static final long FIRED_TIME_NEVER = (long) 0;
	private float score = 0;
	private float scoreToWin = -1;
	private Text winScoreText = null;
	private static final String MUSIC_TITLE = "audio/Music/Misty Sunshine.mp3";
	private static final String MUSIC_BACKGROUND = "audio/Music/It's a Wonderful World.mp3";
	private static final String MUSIC_WIN = "audio/Music/Together we Survive.mp3";
	private static final String MUSIC_LOSE = "audio/Music/Sea of Sadness.mp3";
	private Tower selectedTower = null;
	protected boolean towerSoundPlayed = false;
	ArrayList<Long> lastTimeTowersHaveFired = new ArrayList<Long>();
	protected WaveSystem spawner = new WaveSystem();;
	private enum gamePhase
	{
		GAMEPLAY, WIN, LOSE
	}
	private gamePhase currentGamePhase = gamePhase.GAMEPLAY;
	private gamePhase previousGamePhase = null;
	
	/**
	* Initializes the world
	* Calls buildGame, the method to be overridden by the student
	*/
	public void initializeWorld() {
		super.initializeWorld();
		setHUDVisibilityTo(false);
		buildGame();
		initializeSettings();
		initializeWinScreenAssets();
		spawner.setDrawingLayer(null);
		Walker.setRepository(walkerSet);
		
	}

	private void initializeSettings() {
		TowerMedic.setCastHealthAdjust(10);
		TowerSpeedy.setCastSpeedAdjustDuration(1);
		TowerSpeedy.setCastSpeedAdjustMultiplier(3);
	}

	private void initializeWinScreenAssets() {
		winScoreText = makeText(SCREEN_CENTER_X - .95f, SCREEN_CENTER_Y + 1.3f);
		winScoreText.setFontSize(38);
		winScoreText.moveToDrawingLayer(phaseLayerWin);
	}

	/**
	 * Update everything Calls updateGame, the method to be overridden by the
	 * student
	 */
	public void updateWorld() {
		super.updateWorld();
		towerSet.update();
		spawner.update();
		walkerSet.update();
		updateGame();
		

	}

	/**
	 * The function to be overridden by the student. This method should be used
	 * to "build the game" by building key elements.
	 */
	public void buildGame() {

	}

	/**
	 * The function to be overridden by the student. This method should be used
	 * to "update the game" by updating key elements.
	 */
	public void updateGame() {

	}

	// -------------------------------------------------------------------------

	/**
	 * Draws a medic tower at a random tile.
	 */
	public void drawMedicWizard() {
		Random random = new Random();
		int x = random.nextInt((int) SCREEN_WIDTH - 1);
		int y = random.nextInt((int) SCREEN_HEIGHT);

		drawMedicWizard(x, y);
	}

	/**
	 * Draws a medic tower at the given x and y coordinate.
	 * 
	 * @param x
	 *            The x coordinate of the position of the medic wizard
	 * @param y
	 *            The y coordinate of the position of the medic wizard
	 */
	public void drawMedicWizard(int x, int y) {
		Tile position = grid.getTile(x, y);
		towerSet.addTowerAt(position, true);
	}

	/**
	 * Draws a speedy tower at a random tile.
	 */
	public void drawSpeedyWizard() {
		Random random = new Random();
		int x = random.nextInt((int) SCREEN_WIDTH - 1);
		int y = random.nextInt((int) SCREEN_HEIGHT);

		drawSpeedyWizard(x, y);
	}

	/**
	 * Draws a speedy tower at the given x and y coordinate.
	 * 
	 * @param x
	 *            The x coordinate of the position of the speedy wizard
	 * @param y
	 *            The y coordinate of the position of the speedy wizard
	 */
	public void drawSpeedyWizard(int x, int y) {
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
	 * ability to connect to Tiles to the left and top. This means that when the
	 * Path is created using constructPath(), this Tile can be included in the
	 * Path.
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
	 * Make paths in the grid visible. Note that Tiles without paths are never
	 * drawn, because it ends up being really expensive.
	 * 
	 * @param isVisible
	 *            True if Path Tiles should be visible.
	 */
	public void makePathVisible() {
		grid.setPathTileVisibilityTo(true);
	}

	/**
	 * It is necessary to call this method so that walkers can walk on a custom
	 * path.
	 * 
	 * @param startColumn
	 *            The column of the Tile where the Path begins.
	 * @param startRow
	 *            The row of the Tile where the Path begins.
	 * @param endColumn
	 *            The column of the Tile where the Path ends.
	 * @param endRow
	 *            The row of the Tile where the Path ends.
	 * @return True if the Path was successfully prepared.
	 */
	public boolean preparePathForWalkers(int startColumn, int startRow, int endColumn, int endRow) {
		if (grid.constructPath(startColumn, startRow, endColumn, endRow)) {
			System.out.println(spawner);
			spawner.setPath(grid.getPath());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Add a basic walker
	 */
	public void addWalker() {
		spawner.addWave((float) 0.1, (float) 0.1, "bbbbbb");
		spawner.startWaves();
	}

	/**
	 * Add a fast walker
	 */
	public void addQuickWalker() {
		spawner.addWave((float) 0.1, (float) 0.1, "qqqqqq");
		spawner.startWaves();
	}

	/**
	 * Returns true if the user left clicks
	 * 
	 * @return true if the user left clicks
	 */
	public boolean mouseClicked() {
		return mouse.isButtonTapped(1);
	}

	/**
	 * Returns true if a tower is selected
	 * 
	 * @return true
	 */
	public boolean aTowerIsSelected() {
		return selectedTower != null;
	}

	/**
	 * Moves the selected Tower to the clicked Tile via in-game movement.
	 */
	public void moveTowerToClickedTile() {
		selectedTower.teleportTo(grid.getClickedTile());
		unselectTower();
		selectedTower.playSoundMove();
	}

	/**
	 * @return true if the clicked tower is selected; false otherwise
	 */
	public boolean clickedTowerIsSelected() {
		return grid.getClickedTile().getOccupant() == selectedTower;
	}

	/**
	 * @return true if the clicked tile is occupied; false otherwise
	 */
	public boolean clickedTileHasTower() {
		if ( grid.getClickedTile() != null){ 
			return grid.getClickedTile().hasOccupant();
		}
		return false;
	}

	/**
	 * Selects the tower that was on the clicked tile
	 */
	public void selectClickedTower() {
		selectTower(grid.getClickedTile().getOccupant());
	}

	/**
	 * Adds a Medic tower at the clicked tile
	 */
	public void addMedicTowerAtClickedTile() {
		towerSet.addTowerAt(grid.getClickedTile(), true);
		lastTimeTowersHaveFired.add(FIRED_TIME_NEVER); // The tower has never
														// fired
	}

	/**
	 * Adds a Speedy tower at the clicked tile
	 */
	public void addSpeedyTowerAtClickedTile() {
		towerSet.addTowerAt(grid.getClickedTile(), false);
		lastTimeTowersHaveFired.add(FIRED_TIME_NEVER); // The tower has never
														// fired
	}

	/**
	 * Sets a tower to be the currently selected tower. Used when the player
	 * clicks a tower.
	 * 
	 * @param tower
	 *            The tower to select.
	 */
	protected void selectTower(Tower tower) {
		if (tower != null) {
			unselectTower();
			selectedTower = tower;
			selectedTower.setSelectedTo(true);
		}
	}

	/**
	 * If there is a selected tower, it is no longer selected.
	 */
	protected void unselectTower() {
		if (selectedTower != null) {
			selectedTower.setSelectedTo(false);
		}
		selectedTower = null;
	}

	/**
	 * 
	 * @return the number of towers
	 */
	protected int numOfTowers() {
		return towerSet.getArrayOfTowers().length;
	}

	/**
	 * 
	 * @return the number of walkers
	 */
	protected int numOfWalkers() {
		return walkerSet.getArrayOfWalkers().length;
	}

	/**
	 * returns true if the tower of a given index is speedy; false otherwise
	 * 
	 * @param index
	 * @return true if the tower of a given index is speedy; false otherwise
	 */
	protected boolean towerIsSpeedy(int index) {
		return towerSet.getArrayOfTowers()[index].getTowerType() == Tower.Type.SPEEDY;
	}

	/**
	 * returns true if the tower of a given index is medic; false otherwise
	 * 
	 * @param index
	 * @return true if the tower of a given index is medic; false otherwise
	 */
	protected boolean towerIsMedic(int index) {
		return towerSet.getArrayOfTowers()[index].getTowerType() == Tower.Type.MEDIC;
	}

	/**
	 * Make towers fire appropriately
	 */
	protected void makeTowersFire() {
		// heal walkers or make walkers faster
		for (int i = 0; i < numOfTowers(); i++) {
			for (int j = 0; j < numOfWalkers(); j++) {
				if (towerShouldFire(i, j)) {
					// either speedy or medic, pick one
					// towerCastMedicSpellOnWalker(i, j);
					towerCastSpeedySpellOnWalker(i, j);
				}
			}
		}

	}

	/**
	 * returns true if the tower should fire; false otherwise
	 * 
	 * @param towerIndex
	 *            index for a given tower
	 * @param walkerIndex
	 *            index for a given walker
	 * @return true if the tower should fire; false otherwise
	 */
	protected boolean towerShouldFire(int towerIndex, int walkerIndex) {
		long lastSoundTime = lastTimeTowersHaveFired.get(towerIndex);
		if (System.currentTimeMillis() - lastSoundTime > 5000) { //todo: check with branden how often they should fire
			towerSoundPlayed = false;
			lastTimeTowersHaveFired.set(towerIndex, System.currentTimeMillis());
		}

		Walker walker = walkerSet.getArrayOfWalkers()[walkerIndex];
		Tower tower = towerSet.getArrayOfTowers()[towerIndex];

		if (!towerSet.getArrayOfTowers()[towerIndex].cooldownIsReady()) {
			return false;
		}
		if (walker.isDead()) {
			return false;
		}
		Tile towerTile = tower.getTile();
		if (towerTile == null) {
			return false;
		}

		int mapColumnOfTower = towerTile.getGridColumn();
		int mapRowOfTower = towerTile.getGridRow();
		Vector2 walkerPos = walker.getCenter();

		// search surrounding tiles in 3x3 for walker
		for (int row = mapRowOfTower - 1; row <= mapRowOfTower + 1; row++) {
			for (int column = mapColumnOfTower - 1; column <= mapColumnOfTower + 1; column++) {
				Tile testTile = grid.getTile(column, row);
				if (testTile != null && testTile.containsPoint(walkerPos)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Make tower fire at the walker, walker should gain health as a result
	 * 
	 * @param towerIndex
	 *            index for a given tower
	 * @param walkerIndex
	 *            index for a given walker
	 */
	protected void towerCastMedicSpellOnWalker(int towerIndex, int walkerIndex) {
		Tower t = towerSet.getArrayOfTowers()[towerIndex];
		if (!towerSoundPlayed) {
			Walker w = walkerSet.getArrayOfWalkers()[walkerIndex];
			t.playEffectSpellcast();
			t.playSoundSpellcast();
			towerSoundPlayed = true;
			w.addHealth(t.getCastHealthAdjust());
		}
	}

	/**
	 * Make tower fire at the walker, walker should walk faster as a result
	 * 
	 * @param towerIndex
	 *            index for a given tower
	 * @param walkerIndex
	 *            index for a given walker
	 */
	protected void towerCastSpeedySpellOnWalker(int towerIndex, int walkerIndex) {
		Tower t = towerSet.getArrayOfTowers()[towerIndex];
		if (!towerSoundPlayed) {
			Walker w = walkerSet.getArrayOfWalkers()[walkerIndex];
			t.playEffectSpellcast();
			t.playSoundSpellcast();
			towerSoundPlayed = true;
			w.applySpeedBuff(t.getCastSpeedAdjustMultiplier(), t.getCastSpeedAdjustDuration());
		}
	}

	/**
	 * Returns true if the game is considered over.
	 * 
	 * @return Whether the game is considered over.
	 */
	protected boolean gameIsOver() {

		for (Walker walker : walkerSet.getArrayOfWalkers()) {
			if (!walker.isAtPathEnd()) { //expected isDead to return true; but it is false
				return false;
			}
		}
		return true;
	}

	protected boolean userWon() {
		return score >= getScoreToWin();
	}

	protected float getScoreToWin() {
		return scoreToWin;
	}

	protected void setScoreToWin(float scoreToWin) {
		this.scoreToWin = scoreToWin;
	}

	/**
	 * Transition to the win screen.
	 */
	protected void enterWin() {
		if (currentGamePhase == gamePhase.GAMEPLAY) {
			resources.stopSound(MUSIC_TITLE);
			resources.stopSound(MUSIC_LOSE);
			resources.stopSound(MUSIC_BACKGROUND);
			resources.playSound(MUSIC_WIN);

			winScoreText.setText("" + (int) score);

			// visuals
			phaseLayerWin.setVisibilityTo(true);
			layerScreenDarkener.setVisibilityTo(true);
			phaseLayerGameplay.setVisibilityTo(true);

			phaseLayerTitleScreen.setVisibilityTo(false);
			phaseLayerCredits.setVisibilityTo(false);
			phaseLayerRestartConfirm.setVisibilityTo(false);
			phaseLayerQuitConfirm.setVisibilityTo(false);
			phaseLayerLose.setVisibilityTo(false);
			phaseLayerLevelIntro.setVisibilityTo(false);
			phaseLayerPause.setVisibilityTo(false);
			
			setCurrentGamePhase(gamePhase.WIN);
		}
	}

	/**
	 * Transition to the Lose screen.
	 */
	protected void enterLose() {
		if (currentGamePhase == gamePhase.GAMEPLAY) {
			resources.stopSound(MUSIC_TITLE);
			resources.stopSound(MUSIC_WIN);
			resources.stopSound(MUSIC_BACKGROUND);
			resources.playSound(MUSIC_LOSE);

			// visuals
			phaseLayerLose.setVisibilityTo(true);
			layerScreenDarkener.setVisibilityTo(true);
			phaseLayerGameplay.setVisibilityTo(true);

			phaseLayerTitleScreen.setVisibilityTo(false);
			phaseLayerCredits.setVisibilityTo(false);
			phaseLayerRestartConfirm.setVisibilityTo(false);
			phaseLayerQuitConfirm.setVisibilityTo(false);
			phaseLayerWin.setVisibilityTo(false);
			phaseLayerLevelIntro.setVisibilityTo(false);
			phaseLayerPause.setVisibilityTo(false);
			
			setCurrentGamePhase(gamePhase.LOSE);
		}
	}

	private void setCurrentGamePhase(gamePhase currentGamePhase) {
		previousGamePhase = this.currentGamePhase;
		this.currentGamePhase = currentGamePhase;
	}
}
