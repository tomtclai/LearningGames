package HTLFunctionalAPI;

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

public class HTLFunctionalAPI extends HTL {
	private static final long FIRED_TIME_NEVER = (long) 0;
	private float score = 0;
	private float scoreToWin = -1;
	private Text winScoreText = null;
	
	private static final String TOWER_PLURAL = "wizards";
	private static final String TILE_PLURAL = "tiles";
	
	private static final String MUSIC_TITLE = "audio/Music/Misty Sunshine.mp3";
	private static final String MUSIC_BACKGROUND = "audio/Music/It's a Wonderful World.mp3";
	private static final String MUSIC_WIN = "audio/Music/Together we Survive.mp3";
	private static final String MUSIC_LOSE = "audio/Music/Sea of Sadness.mp3";
	public static final String IMAGE_LOSE_POP_UP = "art/UI/HTL_PopUp_Lose.png";
	public static final String IMAGE_LOSE_BUTTON_RESTART = "art/UI/HTL_PopUp_ButtonRestart.png";
	public static final String IMAGE_LOSE_BUTTON_QUIT = "art/UI/HTL_PopUp_ButtonQuit.png";
	
	public static final String IMAGE_WIN_POP_UP = "art/UI/HTL_PopUp_Win.png";
	public static final String IMAGE_WIN_BUTTON_RESTART = "art/UI/HTL_PopUp_ButtonRestart.png";
	public static final String IMAGE_WIN_BUTTON_QUIT = "art/UI/HTL_PopUp_ButtonQuit.png";
	
	private GameObject winPopUp = null;
	private GameObject winButtonRestart = null;
	private GameObject winButtonQuit = null;
	
	private GameObject losePopUp = null;
	private GameObject loseButtonRestart = null;
	private GameObject loseButtonQuit = null;
	
	
	private Tower selectedTower = null;
	protected boolean towerSoundPlayed = false;
	ArrayList<Long> lastTimeTowersHaveFired = new ArrayList<Long>();
	protected WaveSystem spawner = new WaveSystem();
	
	
	// HUD stuff
	private GameObject hud = null;
	protected GameObject buttonPause = null;
	private Text textMovesRemaining = null;
	private Text textTowerMedicRemaining = null;
	private Text textTowerSpeedyRemaining = null;
	private Text textSurvivalsBasic = null;
	private Text textSurvivalsQuick = null;
	private Text textScore = null;
	private Text textTimeUntilNextWave = null;
	
	private enum gamePhase
	{
		GAMEPLAY, WIN, LOSE, PAUSE, RESTART_CONFIRM, QUIT_CONFIRM
	}
	private gamePhase currentGamePhase = gamePhase.GAMEPLAY;
	
	/**
	* Initializes the world
	* Calls buildGame, the method to be overridden by the student
	*/
	public void initializeWorld() {
		super.initializeWorld();
		grid.setPathTileVisibilityTo(true);
		spawner.setDrawingLayer(null);
		Walker.setRepository(walkerSet);
		enterGameplay();
	
		
	}

	private void initializeSettings() {
		TowerMedic.setCastHealthAdjust(10);
		TowerSpeedy.setCastSpeedAdjustDuration(1);
		TowerSpeedy.setCastSpeedAdjustMultiplier(3);
	}

	private void initializeWinScreenAssets() {
		if(winPopUp == null)
		{
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
	 * Initializes assets for Lose screen.
	 */
	private void initializeLoseScreenAssets()
	{
		if(losePopUp == null)
		{
			float popUpWidth = gameUnitsFromPixels(445);
			float popUpHeight = gameUnitsFromPixels(515);
			losePopUp = new GameObject(SCREEN_CENTER_X, SCREEN_CENTER_Y, popUpWidth, popUpHeight);
			losePopUp.setImage(IMAGE_LOSE_POP_UP);
			losePopUp.moveToDrawingLayer(phaseLayerLose);
			
			float buttonWidth = gameUnitsFromPixels(224); 
			float buttonHeight = gameUnitsFromPixels(85);			
			loseButtonRestart = new GameObject(SCREEN_CENTER_X, 5.4f, buttonWidth, buttonHeight);
			loseButtonRestart.setImage(IMAGE_LOSE_BUTTON_RESTART);
			loseButtonRestart.moveToDrawingLayer(phaseLayerLose);
			
			loseButtonQuit = new GameObject(SCREEN_CENTER_X, 3.9f, buttonWidth, buttonHeight);
			loseButtonQuit.setImage(IMAGE_LOSE_BUTTON_QUIT);
			loseButtonQuit.moveToDrawingLayer(phaseLayerLose);
		}
	}
	/**
	 * Initialize the Default HUD
	 */
	private void initializeHUD()
	{	
		// HUD should probably become its own object type someday
		if(hud == null)
		{
			// panes
			hud = new GameObject(SCREEN_CENTER_X, SCREEN_CENTER_Y, SCREEN_WIDTH, SCREEN_HEIGHT);
			hud.moveToDrawingLayer(layerHUD);		
			hud.setImage(IMAGE_HUD);
					
			// pause button
			float pauseButtonSize = gameUnitsFromPixels(31);
			buttonPause = new GameObject(0.95f, 10.67f, pauseButtonSize, pauseButtonSize);
			buttonPause.setImage(IMAGE_PAUSE_BUTTON);
			buttonPause.moveToDrawingLayer(layerHUD);
			
			float topRowHeight = 10.53f;			
			
			textTimeUntilNextWave = makeText(3.62f, topRowHeight);
			textMovesRemaining = makeText(6.1f, topRowHeight);
			textScore = makeText(18.2f, topRowHeight);			
			
			float bottomRowHeight = 0.2f;
			
			textTowerMedicRemaining = makeText(0.55f, bottomRowHeight);
			textTowerSpeedyRemaining = makeText(2.4f, bottomRowHeight);	
			textSurvivalsQuick = makeText(16.6f, bottomRowHeight);
			textSurvivalsBasic = makeText(18.46f, bottomRowHeight);

			textTimeUntilNextWave.moveToDrawingLayer(layerHUD);
			textMovesRemaining.moveToDrawingLayer(layerHUD);
			textScore.moveToDrawingLayer(layerHUD);
			textTowerMedicRemaining.moveToDrawingLayer(layerHUD);
			textTowerSpeedyRemaining.moveToDrawingLayer(layerHUD);
			textSurvivalsQuick.moveToDrawingLayer(layerHUD);
			textSurvivalsBasic.moveToDrawingLayer(layerHUD);
		}
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
		lastTimeTowersHaveFired.add(FIRED_TIME_NEVER);
		
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
		
		spawner.addWave((float) 0.1, (float) 0.1, "b");
		spawner.startWaves();
	}

	/**
	 * Add a fast walker
	 */
	public void addQuickWalker() {
		spawner.addWave((float) 0.1, (float) 0.1, "q");
		spawner.startWaves();
	}

	/**
	 * Add a basic walker
	 */
	public void addWalkers() {
		
		spawner.addWave((float) 0.1, (float) 0.1, "bbbbbb");
		spawner.startWaves();
	}

	/**
	 * Add a fast walker
	 */
	public void addQuickWalkers() {
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
	public void moveTowerTo(int x, int y) { //TODO: look into move tower to (i, X, Y) // i out of bounds?
		Tile tile = getTileAt(x, y);
		selectedTower.teleportTo(tile);
		unselectTower();
		selectedTower.playSoundMove();
	}

	private Tile getTileAt(int x, int y) {
		Tile tile = grid.getTile(x, y);
		if (tile == null) {
			printNoObjectAtCoordinateMessage(TILE_PLURAL, x, y);
		}
		return tile;
	}

	private void printNoObjectAtCoordinateMessage(String objPlural, int x, int y) {
		System.out.println("There are no " + objPlural + " at "+ coordinatesToString(x,y));
	}
	private String coordinatesToString(int x, int y) {
		return  "x = "+ x + " y = " + y;
	}
	/**
	 * @return true if the clicked tower is selected; false otherwise
	 */
	public boolean towerIsSelected(int x, int y) {
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

	/**
	 * @return true if the clicked tile is occupied; false otherwise
	 */
	public boolean tileHasTower(int x, int y) {
		Tile tile = getTileAt(x, y);
		
		if ( tile != null){ 
			return tile.hasOccupant();
		} else {
			printNoObjectAtCoordinateMessage(TILE_PLURAL, x, y);
		}
		return false;
	}

	/**
	 * Selects the tower that was on the clicked tile
	 */
	public void selectTower(int x, int y) {
		
		Tile tile = getTileAt(x, y);
		
		if ( tile == null){ 
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
					// medicWizardCastSpellOnWalker(i, j);
					speedyWizardCastSpellOnWalker(i, j);
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
	protected void medicWizardCastSpellOnWalker(int towerIndex, int walkerIndex) {
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
	protected void speedyWizardCastSpellOnWalker(int towerIndex, int walkerIndex) {
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

		if (walkerSet.isEmpty()) {
			return false;
		}
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
	 * Creates the DrawingLayers in order from bottom to top,
	 * which makes sure things in upper layers draw last,
	 * and hence on top.
	 */
	private void initializeDrawingLayers()
	{
		if(layerBackground == null)
		{
			// ORDER MATTERS - later ones are on top of the others in the same layer
			phaseLayerGameplay = new DrawingLayer();			
			phaseLayerLose = new DrawingLayer();
			phaseLayerWin = new DrawingLayer();
			phaseLayerPause = new DrawingLayer();
			phaseLayerRestartConfirm = new DrawingLayer();
			phaseLayerQuitConfirm = new DrawingLayer();
			phaseLayerLevelIntro = new DrawingLayer();
			phaseLayerTitleScreen = new DrawingLayer();
			phaseLayerCredits = new DrawingLayer();
			phaseLayerDebug = new DrawingLayer();

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
	 * When the game transitions from the pre-level message to the actual game level,
	 * do this.
	 */
	protected void enterGameplay()
	{
		resources.stopSound(MUSIC_WIN);
		resources.stopSound(MUSIC_LOSE);
		if(currentGamePhase != gamePhase.PAUSE)
		{
			spawner.clearWaves();
			buildGame();
			initializeSettings();
			initializeWinScreenAssets();
			initializeLoseScreenAssets();
			initializeDrawingLayers();
			initializeHUD();
			spawner.setDrawingLayer(null);
			Walker.setRepository(walkerSet);
	
			
			// logic
			spawner.startWaves();
			phaseLayerLose.setVisibilityTo(false);
			phaseLayerWin.setVisibilityTo(false);
		}
		
		// logic
		paused = false;
		CooldownTimer.unpauseAll();
		//setAnimationPauseStatus(false);
		
		// music
		//resources.resumeSound();
		
		// visuals
		phaseLayerGameplay.setVisibilityTo(true);
		
		phaseLayerTitleScreen.setVisibilityTo(false);		
		phaseLayerCredits.setVisibilityTo(false);		
		phaseLayerRestartConfirm.setVisibilityTo(false);
		phaseLayerQuitConfirm.setVisibilityTo(false);
		phaseLayerPause.setVisibilityTo(false);
		phaseLayerWin.setVisibilityTo(false);
		phaseLayerLose.setVisibilityTo(false);
		phaseLayerLevelIntro.setVisibilityTo(false);
		layerScreenDarkener.setVisibilityTo(false);
		
		// done
		currentGamePhase = gamePhase.GAMEPLAY;
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
		
		this.currentGamePhase = currentGamePhase;
	}
	
	protected void setWalkerDamagePerSecond(int damage) {
		Walker.setDamageTakenPerSecond(damage);
	}
	protected boolean winRestartButtonSelected() {
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();
		return winButtonRestart.containsPoint(mouseX, mouseY);
	}
	
	protected boolean winQuitButtonSelected() {
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();
		return winButtonQuit.containsPoint(mouseX, mouseY);
	}
	protected boolean loseRestartButtonSelected() {
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();
		return loseButtonRestart.containsPoint(mouseX, mouseY);
	}
	
	protected boolean loseQuitButtonSelected() {
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();
		return loseButtonQuit.containsPoint(mouseX, mouseY);
	}
	protected void exitGame() {
		System.exit(0);
	}
	protected void makeToolbarVisible() {
		setHUDVisibilityTo(true);
	}
	protected int getClickedColumn() {
		Tile clickedTile = grid.getClickedTile();
		if (clickedTile == null){
			return -1;
		}
		return clickedTile.getGridColumn();
	} 
	protected int getClickedRow() {
		Tile clickedTile = grid.getClickedTile();
		if (clickedTile == null){
			return -1;
		}
		return clickedTile.getGridRow();
	} 
}
