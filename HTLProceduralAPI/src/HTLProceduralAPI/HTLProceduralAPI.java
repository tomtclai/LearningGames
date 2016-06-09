package HTLProceduralAPI;

import java.awt.event.KeyEvent;
import java.util.*;

import Engine.DrawingLayer;
import Engine.GameObject;
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

public class HTLProceduralAPI extends HTL {
	// Paths
	private static final String MUSIC_TITLE = "audio/Music/Misty Sunshine.mp3";
	private static final String MUSIC_BACKGROUND = "audio/Music/It's a Wonderful World.mp3";
	private static final String MUSIC_WIN = "audio/Music/Together we Survive.mp3";
	public static final String IMAGE_WIN_POP_UP = "art/UI/HTL_PopUp_Win.png";
	public static final String IMAGE_WIN_BUTTON_RESTART = "art/UI/HTL_PopUp_ButtonRestart.png";
	public static final String IMAGE_WIN_BUTTON_QUIT = "art/UI/HTL_PopUp_ButtonQuit.png";

	// User Interface
	private GameObject winPopUp = null;
	private GameObject winButtonRestart = null;
	private GameObject winButtonQuit = null;

	// Game Data
	private Tower selectedTower = null;
	private boolean towerSoundPlayed = false;
	ArrayList<Long> lastTimeTowersHaveFired = new ArrayList<Long>();
	private int numOfMedicsCreated = 0;
	private int numOfSpeedysCreated = 0;
	private int numOfDeadWalkers = 0;
	private int numOfWalkersCreated = 0;
	private int numOfWalkerSaved = 0;
	private int numOfBasicWalkersOnScreen = 0;
	private int numOfQuickWalkersOnScreen = 0;
	private float score = 0;
	private float scoreToWin = -1;
	private int countdownFrom = 3;
	private int countdownCurrent = 0;
	private long lastTimeTimerWasUpdated = 0;
	private long lastTimeTimerWasFired = 0;
	private Text winScoreText = null;
	Vector<Walker> walkers = new Vector<Walker>();

	// Settings
	private int medicTimeBetweenSpellcastsInMS = 3000;
	private int speedyTimeBetweenSpellcastsInMS = 3000;

	// Constants
	private static final long FIRED_TIME_NEVER = (long) 0;
	private static final String TOWER_PLURAL = "wizards";
	private static final String TILE_PLURAL = "tiles";

	private enum GamePhase {
		GAMEPLAY, WIN, PAUSE, RESTART_CONFIRM, QUIT_CONFIRM
	}

	private GamePhase currentGamePhase = GamePhase.GAMEPLAY;
	private float healthSaved;

	/**
	 * The function returns true if 1) countdown finishes and 2) the function
	 * has not returned true in the last 1 second
	 */
	public boolean countdownFired() {
		if (countdownCurrent < 0 && System.currentTimeMillis() - lastTimeTimerWasFired > 1000) {
			lastTimeTimerWasFired = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	private void updateTimer() {
		if (countdownCurrent < 0) {
			countdownCurrent = getCountdownMax();
			lastTimeTimerWasUpdated = System.currentTimeMillis();
		} else if (System.currentTimeMillis() - lastTimeTimerWasUpdated > 1000) {
			countdownCurrent--;
			lastTimeTimerWasUpdated = System.currentTimeMillis();
		}
	}

	/**
	 * @return the number to countdown from
	 */
	public int getCountdownMax() {
		return countdownFrom;
	}

	/**
	 * set the integer to count down from
	 * 
	 * @param the
	 *            number to countdown from
	 */
	public void setCountdownFrom(int countdownFrom) {
		this.countdownFrom = countdownFrom;
	}

	/**
	 * Initializes the world Calls buildGame, the method to be overridden by the
	 * student
	 */
	public void initializeWorld() {
		super.initializeWorld();
		grid.setPathTileVisibilityTo(true);
		setHUDVisibilityTo(false);
		Walker.setRepository(walkerSet);
		Smoke.setTransitionDelay(0);
		enterGameplay();

	}

	private void initializeStates() {
		numOfWalkerSaved = 0;
		lastTimeTimerWasUpdated = 0;
		lastTimeTimerWasFired = 0;
		score = 0;
		numOfMedicsCreated = 0;
		numOfSpeedysCreated = 0;
		numOfDeadWalkers = 0;
		numOfWalkersCreated = 0;
		numOfBasicWalkersOnScreen = 0;
		numOfQuickWalkersOnScreen = 0;
		healthSaved = 0;
		towerSet.removeAll();
		walkerSet.removeAll();

	}

	/**
	 * Set the settings to defaults
	 */
	private void initializeSettings() {
		TowerMedic.setCastHealthAdjust(10);
		TowerSpeedy.setCastSpeedAdjustDuration(1);
		TowerSpeedy.setCastSpeedAdjustMultiplier(3);
	}

	private void initializeWinScreenAssets() {
		if (winPopUp == null) {
			float popUpWidth = gameUnitsFromPixels(445);
			float popUpHeight = gameUnitsFromPixels(515);
			winPopUp = new GameObject(SCREEN_CENTER_X, SCREEN_CENTER_Y, popUpWidth, popUpHeight);
			winPopUp.setImage(IMAGE_WIN_POP_UP);
			winPopUp.moveToDrawingLayer(phaseLayerWin);

			float buttonWidth = gameUnitsFromPixels(224);
			float buttonHeight = gameUnitsFromPixels(85);
			winButtonRestart = new GameObject(SCREEN_CENTER_X, 5.4f, buttonWidth, buttonHeight);
			winButtonRestart.setImage(IMAGE_WIN_BUTTON_RESTART);
			winButtonRestart.moveToDrawingLayer(phaseLayerWin);

			winButtonQuit = new GameObject(SCREEN_CENTER_X, 3.9f, buttonWidth, buttonHeight);
			winButtonQuit.setImage(IMAGE_WIN_BUTTON_QUIT);
			winButtonQuit.moveToDrawingLayer(phaseLayerWin);

			winScoreText = makeText(SCREEN_CENTER_X - .95f, SCREEN_CENTER_Y + 1.3f);
			winScoreText.setFontSize(38);
			winScoreText.moveToDrawingLayer(phaseLayerWin);
		}
	}

	/**
	 * Update everything Calls updateGame, the method to be overridden by the
	 * student
	 */
	public void updateWorld() {
		super.updateWorld();
		if (currentGamePhase == GamePhase.GAMEPLAY) {
			towerSet.update();
			walkerSet.update();
			updateTimer();
			updateStats();
		}
		updateGame();
		updateUI();
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
		drawWizard(x, y, true);
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
		drawWizard(x, y, false);
	}

	/**
	 * Draws a tower at the given x and y coordinate.
	 * 
	 * @param x
	 *            The x coordinate of the position of the speedy wizard
	 * @param y
	 *            The y coordinate of the position of the speedy wizard
	 * @param type
	 *            Either "medic" or "speedy", case insensitive.
	 */
	public void drawWizard(int x, int y, String type) {
		if (type.toLowerCase().equals("medic")) {
			drawWizard(x, y, true);
		} else if (type.toLowerCase().equals("speedy")) {
			drawWizard(x, y, false);
		}
	}

	/**
	 * Draws a speedy tower at the given x and y coordinate.
	 * 
	 * @param x
	 *            The x coordinate of the position of the speedy wizard
	 * @param y
	 *            The y coordinate of the position of the speedy wizard
	 * @param isMedic
	 *            If a medic tower should be drawn (true) or speedy tower
	 *            (false)
	 */
	private void drawWizard(int x, int y, boolean isMedic) {
		Tile position = grid.getTile(x, y);

		if (position == null || position.isBlocked()) {
			return;
		}
		towerSet.addTowerAt(position, isMedic);
		if (isMedic) {
			numOfMedicsCreated++;
		} else {
			numOfSpeedysCreated++;
		}
		lastTimeTowersHaveFired.add(FIRED_TIME_NEVER);
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
		grid.setTileBlockedTo(x, y, true);
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
		grid.setTileBlockedTo(x, y, true);
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
		grid.setTileBlockedTo(x, y, true);
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
		grid.setTileBlockedTo(x, y, true);
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
		grid.setTileBlockedTo(x, y, true);
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
		grid.setTileBlockedTo(x, y, true);
		return grid.addPathDownRight(x, y);
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
	protected boolean preparePathForWalkers(int startColumn, int startRow, int endColumn, int endRow) {
		// TODO: try to hide this function from students
		if (grid.constructPath(startColumn, startRow, endColumn, endRow)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Add a walker
	 * 
	 * @param type
	 *            Either "basic" or "quick", case insensitive.
	 */
	public void addWalker(String type) {
		if (type.toLowerCase().equals("basic")) {
			addBasicWalker();
		} else if (type.toLowerCase().equals("quick")) {
			addQuickWalker();
		}
	}

	/**
	 * Add a basic walker
	 */
	public void addBasicWalker() {
		Walker w = new WalkerBasic(grid.getPath());
		w.moveToDrawingLayer(layerWalkers);
		walkers.add(w);
		numOfBasicWalkersOnScreen++;
		numOfWalkersCreated++;
	}

	/**
	 * Add a fast walker
	 */
	public void addQuickWalker() {
		Walker w = new WalkerQuick(grid.getPath());
		w.moveToDrawingLayer(layerWalkers);
		walkers.add(w);
		numOfQuickWalkersOnScreen++;
		numOfWalkersCreated++;
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
	 * Returns true if a wizard is selected
	 * 
	 * @return true
	 */
	public boolean aWizardIsSelected() {
		return selectedTower != null;
	}

	/**
	 * Moves the selected Tower to the clicked Tile via in-game movement.
	 */
	public void moveWizardTo(int x, int y) {
		Tile tile = getTileAt(x, y);
		if (tile == null) {
			printNoObjectAtCoordinateMessage(TILE_PLURAL, x, y);
			return;
		}
		if (!tile.isBlocked() && selectedTower.teleportTo(tile)) {
			selectedTower.playSoundMove();
		}
		unselectWizard();

	}

	private Tile getTileAt(int x, int y) {
		Tile tile = grid.getTile(x, y);
		if (tile == null) {
			printNoObjectAtCoordinateMessage(TILE_PLURAL, x, y);
		}
		return tile;
	}

	private void printNoObjectAtCoordinateMessage(String objPlural, int x, int y) {
		System.out.println("There are no " + objPlural + " at " + coordinatesToString(x, y));
	}

	private String coordinatesToString(int x, int y) {
		return "x = " + x + " y = " + y;
	}

	/**
	 * @return true if the clicked wizard is selected; false otherwise
	 */
	public boolean wizardIsSelected(int x, int y) {
		Tile tile = getTileAt(x, y);
		if (tile == null) {
			return false;
		}
		Tower tower = tile.getOccupant();
		if (tower == null) {

			printNoObjectAtCoordinateMessage(TOWER_PLURAL, x, y);
		}
		return tile.getOccupant() == selectedTower;
	}

	public boolean wizardIsMedic(int wizardIndex) {
		Tower t = towerSet.getArrayOfTowers()[wizardIndex];
		return t.getTowerType() == Tower.Type.MEDIC;
	}

	/**
	 * @return true if the clicked tile is occupied; false otherwise
	 */
	public boolean tileHasWizard(int x, int y) {
		Tile tile = getTileAt(x, y);

		if (tile != null) {
			return tile.hasOccupant();
		} else {
			printNoObjectAtCoordinateMessage(TILE_PLURAL, x, y);
		}
		return false;
	}

	/**
	 * Selects the wizard that was on the clicked tile
	 */
	public void selectWizard(int x, int y) {

		Tile tile = getTileAt(x, y);

		if (tile == null) {
			printNoObjectAtCoordinateMessage(TILE_PLURAL, x, y);
			return;
		}

		Tower tower = tile.getOccupant();
		if (tower == null) {
			printNoObjectAtCoordinateMessage(TOWER_PLURAL, x, y);
		}
		selectTower(grid.getClickedTile().getOccupant());

	}

	/**
	 * Sets a tower to be the currently selected tower. Used when the player
	 * clicks a tower.
	 * 
	 * @param tower
	 *            The tower to select.
	 */
	private void selectTower(Tower tower) {
		if (tower != null) {
			unselectWizard();
			selectedTower = tower;
			selectedTower.setSelectedTo(true);
		}
	}

	/**
	 * If there is a selected tower, it is no longer selected.
	 */
	protected void unselectWizard() {
		if (selectedTower != null) {
			selectedTower.setSelectedTo(false);
		}
		selectedTower = null;
	}

	/**
	 * 
	 * @return the number of wizards
	 */
	protected int numOfWizards() {
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
	protected void makeWizardsFire() {
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

	protected boolean wizardIsReady(int towerIndex) {
		long lastSoundTime = lastTimeTowersHaveFired.get(towerIndex);
		// So, turns out we have a CooldownTimer class...
		// This function could use some refactoring :)
		int delay = 0;

		if (towerIsMedic(towerIndex)) {
			delay = medicTimeBetweenSpellcastsInMS;
		} else {
			delay = speedyTimeBetweenSpellcastsInMS;
		}

		if (System.currentTimeMillis() - lastSoundTime > delay) {
			towerSoundPlayed = false;
			lastTimeTowersHaveFired.set(towerIndex, System.currentTimeMillis());

			return true;
		}

		return false;
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
	protected boolean walkerIsInRange(int towerIndex, int walkerIndex) {

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
	protected void medicWizardCastSpellOnWalker(int towerIndex, int walkerIndex) {
		Tower t = towerSet.getArrayOfTowers()[towerIndex];
		Walker w = walkerSet.getArrayOfWalkers()[walkerIndex];
		if (!towerSoundPlayed) {
			t.playSoundSpellcast();
			towerSoundPlayed = true;
		}
		t.playEffectSpellcast();
		w.addHealth(t.getCastHealthAdjust());
	}

	/**
	 * Make tower fire at the walker, walker should walk faster as a result
	 * 
	 * @param towerIndex
	 *            index for a given tower
	 * @param walkerIndex
	 *            index for a given walker
	 */
	protected void speedyWizardCastSpellOnWalker(int towerIndex, int walkerIndex) {
		Tower t = towerSet.getArrayOfTowers()[towerIndex];
		Walker w = walkerSet.getArrayOfWalkers()[walkerIndex];
		if (!towerSoundPlayed) {
			t.playSoundSpellcast();
			towerSoundPlayed = true;
		}
		w.applySpeedBuff(t.getCastSpeedAdjustMultiplier(), t.getCastSpeedAdjustDuration());
		t.playEffectSpellcast();
	}

	protected boolean userWon() {
		return getScore() >= getScoreToWin();
	}

	protected float getScoreToWin() {
		return scoreToWin;
	}

	protected void setScoreToWin(float scoreToWin) {
		this.scoreToWin = scoreToWin;
	}

	/**
	 * Creates the DrawingLayers in order from bottom to top, which makes sure
	 * things in upper layers draw last, and hence on top.
	 */
	private void initializeDrawingLayers() {
		if (layerBackground == null) {
			// ORDER MATTERS - later ones are on top of the others in the same
			// layer
			phaseLayerGameplay = new DrawingLayer();
			phaseLayerWin = new DrawingLayer();
			phaseLayerPause = new DrawingLayer();

			layerBackground = new DrawingLayer(phaseLayerGameplay);
			layerPath = new DrawingLayer(phaseLayerGameplay);
			layerEnvironmentUnder = new DrawingLayer(phaseLayerGameplay);
			layerTowers = new DrawingLayer(phaseLayerGameplay);
			layerSpellcastEffects = new DrawingLayer(phaseLayerGameplay);
			layerWalkers = new DrawingLayer(phaseLayerGameplay);
			layerEnvironmentOver = new DrawingLayer(phaseLayerGameplay);
			layerCharacterEffectsOver = new DrawingLayer(phaseLayerGameplay);
			layerShadowsSky = new DrawingLayer(phaseLayerGameplay);
			layerCharacterFeedback = new DrawingLayer(phaseLayerGameplay);
			layerHUD = new DrawingLayer(phaseLayerGameplay);
			layerScreenDarkener = new DrawingLayer(phaseLayerGameplay);
			layerScreenDarkener.setVisibilityTo(false);
		}
	}

	/**
	 * When the game transitions from the pre-level message to the actual game
	 * level, do this.
	 */
	protected void enterGameplay() {
		resources.stopSound(MUSIC_WIN);

		if (currentGamePhase != GamePhase.PAUSE) {
			initializeStates();
			initializeSettings();
			initializeWinScreenAssets();
			initializeDrawingLayers();
			buildGame();
			// logic
			phaseLayerWin.setVisibilityTo(false);
		}

		// logic
		paused = false;
		CooldownTimer.unpauseAll();
		// setAnimationPauseStatus(false);

		// music
		// resources.resumeSound();

		// visuals
		phaseLayerGameplay.setVisibilityTo(true);
		phaseLayerPause.setVisibilityTo(false);
		phaseLayerWin.setVisibilityTo(false);
		layerScreenDarkener.setVisibilityTo(false);

		// done
		currentGamePhase = GamePhase.GAMEPLAY;
	}

	/**
	 * Transition to the win screen.
	 */
	protected void enterWin() {
		if (currentGamePhase == GamePhase.GAMEPLAY) {
			resources.stopSound(MUSIC_TITLE);
			resources.stopSound(MUSIC_BACKGROUND);
			resources.playSound(MUSIC_WIN);

			winScoreText.setText("" + (int) getScore());

			// visuals
			phaseLayerWin.setVisibilityTo(true);
			layerScreenDarkener.setVisibilityTo(true);
			phaseLayerGameplay.setVisibilityTo(true);
			phaseLayerTitleScreen.setVisibilityTo(false);
			phaseLayerCredits.setVisibilityTo(false);
			phaseLayerRestartConfirm.setVisibilityTo(false);
			phaseLayerQuitConfirm.setVisibilityTo(false);
			phaseLayerLevelIntro.setVisibilityTo(false);
			phaseLayerPause.setVisibilityTo(false);

			setCurrentGamePhase(GamePhase.WIN);
		}
	}

	/**
	 * Set the current game phase
	 * @param currentGamePhase
	 */
	private void setCurrentGamePhase(GamePhase currentGamePhase) {

		this.currentGamePhase = currentGamePhase;
	}

	/**
	 * Set Walker's damage per second
	 * @param damage
	 */
	protected void setWalkerDamagePerSecond(int damage) {
		Walker.setDamageTakenPerSecond(damage);
	}

	/**
	 * @return true if the restart button was selected
	 */
	protected boolean winRestartButtonSelected() {
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();

		return winButtonRestart.containsPoint(mouseX, mouseY);
	}

	/**
	 * @return true if the quit button was selected
	 */
	protected boolean winQuitButtonSelected() {
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();
		return winButtonQuit.containsPoint(mouseX, mouseY);
	}

	/**
	 * Exit the game
	 */
	protected void exitGame() {
		System.exit(0);
	}

	/**
	 * Draw the HUD toolbars
	 */
	protected void drawToolbars() {
		setHUDVisibilityTo(true);
	}

	/**
	 * @return the clicked column # in the grid 
	 */
	protected int getClickedColumn() {
		Tile clickedTile = grid.getClickedTile();
		if (clickedTile == null) {
			return -1;
		}
		return clickedTile.getGridColumn();
	}

	/**
	 * @return  the clicked row # in the grid 
	 */
	protected int getClickedRow() {
		Tile clickedTile = grid.getClickedTile();
		if (clickedTile == null) {
			return -1;
		}
		return clickedTile.getGridRow();
	}

	/**
	 * Updates everything on the HUD, the wave sign, and some optional debug
	 * output.
	 */
	private void updateUI() {

		// top row
		// countdownCurrent will hit -1 for a frame, but we don't want it to
		// flicker
		if (countdownCurrent >= 0) {
			setHUDTime(countdownCurrent);
		}
		// commented out because we don't know what to put on that spot.
		// setHUDNumberOfMoves( ??? );
		setHUDScore((int) getScore());

		// bottom row
		setHUDNumberOfTowersMedic(numOfMedicsCreated);
		setHUDNumberOfTowersSpeedy(numOfSpeedysCreated);
		setHUDNumberOfWalkersBasic(numOfBasicWalkersOnScreen);
		setHUDNumberOfWalkersQuick(numOfQuickWalkersOnScreen);

		// win screen
		winScoreText.setText("" + (int) getScore());

	}

	/**
	 * Update the stats if a Walker makes it to the end of the path or dies.
	 * Also gets rid of Walkers who made it to the end of the path.
	 */
	private void updateStats() {
		for (Walker walker : walkerSet.getArrayOfWalkers()) {
			if (walker.hasJustDied()) {
				numOfDeadWalkers = getNumOfDeadWalkers() + 1;
				walker.playSoundDeath();
			} else if (walker.isAtPathEnd()) {
				walker.playSoundSurvival();
				numOfWalkerSaved++;
				float healthToAdd = walker.getHealth();
				if (walker.getWalkerType() == Walker.Type.BASIC) {
					numOfBasicWalkersOnScreen--;
				} else {
					numOfQuickWalkersOnScreen--;
					healthToAdd *= 2;
				}
				healthSaved += healthToAdd;
				walker.destroy();
			}
		}
	}

	// Game Stats
	
	protected float getScore() {
		return score;
	}

	protected void setScore(float score) {
		this.score = score;
	}

	protected int getNumOfDeadWalkers() {
		return numOfDeadWalkers;
	}

	protected int getNumOfWalkersCreated() {
		return numOfWalkersCreated;
	}

	protected int getNumOfWalkersSaved() {
		return numOfWalkerSaved;
	}

	protected float getHealthSaved() {
		return healthSaved;
	}

	/**
	 * if the function is never called, we will use the default, which is 10.0
	 * 
	 * @param h
	 *            the health to add to the walkers, if h is negative, we will
	 *            subtract from the walker's health
	 */
	protected void setMedicWizardHealthAdjust(double h) {
		TowerMedic.setCastHealthAdjust((float) h);
	}

	/**
	 * if the function is never called, we will use the default, which is 3.0
	 * 
	 * @param multiplier
	 *            how much to multiply the speed with, you can use negative to
	 *            make it go backwards. Multiplier cannot be higher than 25x or
	 *            lower than -25x
	 */
	protected void setSpeedyWizardSpeedBoostMultipler(double multiplier) {
		if (Math.abs(multiplier) <= 25.0) {
			TowerSpeedy.setCastSpeedAdjustMultiplier((float) multiplier);
		} else {
			System.out.println("Multiplier cannot be higher than 25x or lower than -25x");
		}
	}

	/**
	 * if the function is never called, we will use the default, which is 1.0
	 * 
	 * @param duration
	 *            how long does the speedboost last, this cannot be negative or
	 *            0
	 */
	protected void setSpeedyWizardSpeedBoostDuration(double duration) {
		if (duration > 0) {
			TowerSpeedy.setCastSpeedAdjustDuration((float) duration);
		} else {
			System.out.println("Duration cannot be lower or equal than 0");
		}
	}

	/**
	 * if the function is never called, we will use the default, which is 3.0
	 * 
	 * @param duration
	 *            the time between spellcasts to set, this cannot be negative or
	 *            0
	 */
	protected void setSpeedyTimeBetweenSpellcasts(double duration) {
		if (duration > 0.0) {
			this.speedyTimeBetweenSpellcastsInMS = (int) (duration * (double) 1000);
		} else {
			System.out.println("Duration cannot be lower or equal than 0");
		}
	}

	/**
	 * if the function is never called, we will use the default, which is 3.0
	 * 
	 * @param duration
	 *            the time between spellcasts to set, this cannot be negative or
	 *            0
	 */
	protected void setMedicTimeBetweenSpellcasts(double duration) {
		if (duration > 0.0) {
			this.medicTimeBetweenSpellcastsInMS = (int) (duration * (double) 1000);
		} else {
			System.out.println("Duration cannot be lower or equal than 0");
		}
	}

}
