package TowerDefense;
import java.awt.Color;
import java.util.LinkedList;

import Engine.BaseCode;
import Engine.DrawingLayer;
import Engine.GameObject;
import Engine.LibraryCode;
import Engine.Text;

/**
 * Extend this class and override initializeWorld() and updateWorld() to make a game.
 * This specific API is geared towards making a tower defense game, but you can
 * probably do a lot more with it.
 * 
 * This API was crafted over the course of 3 quarters from scratch by Rachel Horton
 * and Branden Drake (on top of the game engine API this extends from).
 * We have tried to do our best to make code readable, but
 * especially towards the end when deadlines were looming, we had to focus on
 * functionality over usability.  There are no doubt some things in this API that
 * could be better, and we sincerely apologize for any trouble they give you.
 * 
 * If you need anything, Kelvin can tell you how to reach us. :)
 * 
 * @author Rachel Horton
 * @author Branden Drake
 */
public class HTL extends LibraryCode
{	
	// all of this data has to do with the sizes of things both in the game world and on the screen,
	// due to the fact that GameObjects marry these two concepts together in a way.
	// So this is the size of all things, relative to each other.
	public static final float SCREEN_WIDTH = 20f;
	public static final float SCREEN_HEIGHT = SCREEN_WIDTH * INIT_HEIGHT/INIT_WIDTH;	// screen is 1280 x 720 right now
	
	// ratio of art assets size (INIT_WIDTH, in pixels) to convenient coordinate system game units (SCREEN_WIDTH)
	public static final float PIXELS_PER_GAME_UNIT = INIT_WIDTH / SCREEN_WIDTH;
	
	public static final float SCREEN_CENTER_X = SCREEN_WIDTH / 2f;
	public static final float SCREEN_CENTER_Y = SCREEN_HEIGHT / 2f;
	
	public static final float CHARACTER_WIDTH = SCREEN_WIDTH / 20f;
	public static final float CHARACTER_HEIGHT = SCREEN_WIDTH / 20f;
	
	// the smoke that appears when characters teleport
	public static final float SMOKE_WIDTH = CHARACTER_WIDTH * 1.6f;
	public static final float SMOKE_HEIGHT = CHARACTER_HEIGHT * 1.6f;
	
	public static final float PROJECTILE_WIDTH = CHARACTER_WIDTH / 4f;
	public static final float PROJECTILE_HEIGHT = CHARACTER_HEIGHT / 4f;
	
	public static final String IMAGE_BACKGROUND_WITH_FEATURES = "art/Background/HTL_InGame_Background_Path.png";
	public static final String IMAGE_BACKGROUND_EMPTY = "art/Background/Path-Background Pieces/HTL_BG_Grass2.png";
	public static final String IMAGE_HUD = "art/UI/HTL_InGame_UI.png";
	public static final String IMAGE_PAUSE_BUTTON = "art/UI/HTL_InGame_PauseButton.png";
	public static final String IMAGE_SCREEN_SHADE = "art/UI/HTL_Intro_Transparency.png";
		
	protected GameObject background = null;	

	protected SetOfTowers towerSet = new SetOfTowers();
	protected SetOfWalkers walkerSet = new SetOfWalkers();
	protected Grid grid = null;
	protected WaveSystem spawner = new WaveSystem();
	

	protected LinkedList<Projectile> projectileList = new LinkedList<Projectile>();
	

	
	
	// a layer for debug information
	protected DrawingLayer phaseLayerDebug;
		
	// these layers represent distinct phases of the game.
	protected DrawingLayer phaseLayerCredits;
	protected DrawingLayer phaseLayerTitleScreen;
	protected DrawingLayer phaseLayerLevelIntro;
	protected DrawingLayer phaseLayerRestartConfirm;
	protected DrawingLayer phaseLayerQuitConfirm;
	protected DrawingLayer phaseLayerPause;
	protected DrawingLayer phaseLayerWin;
	protected DrawingLayer phaseLayerLose;
	protected DrawingLayer phaseLayerGameplay;
	
	// shade effect that darkens phaseLayerGameplay	
	protected DrawingLayer layerScreenDarkener;	
	
	// all of the below layers belong to phaseLayerGameplay 
	protected DrawingLayer layerHUD,    // Top and bottom HUD panels
			layerCharacterFeedback,		// Health Pips, +Health floaties, +Speed floaties.
			layerShadowsSky,			// cloud shadows
		    layerCharacterEffectsOver,  // Not sure if we have any of these right now <3
		    layerEnvironmentOver,   	// Wave Sign
		    layerWalkers,  				// Squibbles
		    layerSpellcastEffects, 		// Spellcast effects
		    layerTowers,				// Wizards
		    layerEnvironmentUnder,  	// Moving grass and such
		    layerPath,					// dynamic path
		    layerBackground;  			// static background
	
	
	// having a static variable and a static getter for pause status is
	// admittedly a bit hacky, but we're trying to make it so that every object
	// doesn't need a reference to this class
	protected static boolean paused = false;
	
	// makes the screen look darker
	protected GameObject screenShade = null;
	
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
	
	
	
	
	
	
	
	/**
	 * Constructor.
	 */
	public HTL()
	{	
		super();
	}
	
	
	
	
	
	///////////////////////////////
	//                           //
	//      INITIALIZATION       //
	//                           //
	///////////////////////////////
	
	
	
	
	
	
	/**
	* Initializes the world.
	* Override this method, but make sure to call this one using super.initializeWorld()
	* as the VERY FIRST thing you do in your override.
	*/
	public void initializeWorld()
	{	
		// this line NEEDS to be first
		// for more information, check JarResources
		resources.setClassInJar(new JarResources());
		
		// orders DrawingLayers
		initializeDrawingLayers();
		
		// initializes screen dimensions in the underlying game engine
		BaseCode.world.SetWorldCoordinate(SCREEN_WIDTH);		
		
		// projectiles currently aren't used, but it's cool...
		Projectile.setRepository(projectileList);
		
		// sets the list that new Walkers will automatically be placed in
		Walker.setRepository(walkerSet);
		

		spawner = new WaveSystem();
		spawner.setDrawingLayer(layerWalkers);
		
		
		// screen real estate set aside for the grid
		float xLowerLeft = 0;
		float yLowerLeft = 0;
		float mapWidth = SCREEN_WIDTH;
		float mapHeight = SCREEN_HEIGHT;
		
		// this will let you see what's off the screen for debugging purposes
		// because there are game objects that can exist off the screen (mainly Walkers)
		boolean debugMapEdges = false;
		if(debugMapEdges)
		{
			xLowerLeft = 1;
			yLowerLeft = 1;
			mapWidth = SCREEN_WIDTH - 2;
			mapHeight = SCREEN_HEIGHT - 2;
		}
		
		float xMapCenter = mapWidth/2 + xLowerLeft;
		float yMapCenter = mapHeight/2 +  yLowerLeft;
		
		if(grid == null)
		{
			grid = new Grid(xMapCenter, yMapCenter, mapWidth, mapHeight);
			grid.setDrawingLayer(layerPath);
		}
		

		if(background == null)
		{
			background = new GameObject(SCREEN_CENTER_X, SCREEN_CENTER_Y, SCREEN_WIDTH, SCREEN_HEIGHT);
			background.moveToDrawingLayer(layerBackground);
			background.setImage(IMAGE_BACKGROUND_EMPTY);
		}
		
		if(screenShade == null)
		{
			screenShade = new GameObject(SCREEN_CENTER_X, SCREEN_CENTER_Y, SCREEN_WIDTH, SCREEN_HEIGHT);
			screenShade.setImage(IMAGE_SCREEN_SHADE);
			screenShade.moveToDrawingLayer(layerScreenDarkener);
		}		
		
		initializeHUD();
		
		Tower.setDrawingLayer(layerTowers);
		Tower.setDrawingLayerForEffects(layerSpellcastEffects);
		Character.setDrawingLayerForHealth(layerCharacterFeedback);
		
		super.initializeWorld();		
	}
	

	
	
	/**
	 * Preloads art and sound resources so that they don't slow the game down
	 * the first time they are used.
	 */
	protected void preloadResources()
	{
		WalkerBasic.preloadResources();
		WalkerQuick.preloadResources();
		TowerMedic.preloadResources();
		TowerSpeedy.preloadResources();
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
	public void createDefaultPath()
	{
		// 1. Set grid size
		grid.setDimensions(20, 10);
		
		// 2. Set path tile placement
		placePathTiles();
		
		// 3. Construct the path.
		grid.constructPath(0, 6, 18, 1);
		Path thePath = grid.getPath();
		
		// 4. Pass the path to the spawner.
		spawner.setPath(thePath);
		
		// 5. Set up blocking zones
		placeBlockingTiles();
		
		background.setImage(IMAGE_BACKGROUND_WITH_FEATURES);
		grid.setPathTileVisibilityTo(false);
	}
	
	
	
	
	/**
	 * Turns the Tiles that align with the path on the
	 * default background image into Path Tiles.
	 * This is used to create a path,
	 * and the Tiles use it so that they know to draw differently (if tiles are being drawn).
	 */
	protected void placePathTiles()
	{
		// begin
		grid.addPathLeftRight(0, 6);
		grid.addPathLeftRight(1, 6);
		grid.addPathLeftRight(2, 6);
		grid.addPathLeftRight(3, 6);
		
		grid.addPathUpLeft(4, 6);
		
		grid.addPathUpDown(4, 7);
		
		grid.addPathDownRight(4, 8);
		
		grid.addPathLeftRight(5, 8);
		
		grid.addPathDownLeft(6, 8);
		
		grid.addPathUpDown(6, 7);
		grid.addPathUpDown(6, 6);
		grid.addPathUpDown(6, 5);

		grid.addPathUpLeft(6, 4);
		
		grid.addPathLeftRight(5, 4);
		grid.addPathLeftRight(4, 4);
		
		grid.addPathDownRight(3, 4);
		
		grid.addPathUpDown(3, 3);
		
		grid.addPathUpRight(3, 2);
		
		// bottom horizontal strip
		grid.addPathLeftRight(4, 2);
		grid.addPathLeftRight(5, 2);
		grid.addPathLeftRight(6, 2);
		grid.addPathLeftRight(7, 2);
		grid.addPathLeftRight(8, 2);
		grid.addPathLeftRight(9, 2);

		grid.addPathUpLeft(10, 2);
		
		grid.addPathUpDown(10, 3);
		grid.addPathUpDown(10, 4);
		grid.addPathUpDown(10, 5);
		
		grid.addPathDownRight(10, 6);
		
		grid.addPathLeftRight(11, 6);
		grid.addPathLeftRight(12, 6);
		grid.addPathLeftRight(13, 6);
		grid.addPathLeftRight(14, 6);
		
		grid.addPathUpLeft(15, 6);
		
		grid.addPathDownRight(15, 7);
		
		grid.addPathLeftRight(16, 7);
		grid.addPathLeftRight(17, 7);
		
		grid.addPathDownLeft(18, 7);
		
		grid.addPathUpDown(18, 6);
		grid.addPathUpDown(18, 5);
		grid.addPathUpDown(18, 4);
		grid.addPathUpDown(18, 3);
		grid.addPathUpDown(18, 2);
		grid.addPathUpDown(18, 1);
		//grid.addPathUpDown(18, 0);	// removing this tile because it is blocked by UI
	}

	
	
	/**
	 * Sets up the Blocking Tiles for the default grid.
	 * Blocking Tiles are places where Towers can't move to.
	 */
	protected void placeBlockingTiles()
	{		
		// bottom right mushroom
		for(int y = 0; y < 4; y++)
		{
			for(int x = 12; x < 17; x++)
			{
				grid.setTileBlockedTo(x, y, true);
			}
		}
		
		// upper mushroom and sticks and rocks
		for(int x = 7; x < 18; x++)
		{
			grid.setTileBlockedTo(x, 9, true);
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
	 * Make a new Text object at the specified location.
	 * All Text objects made with this method will have the
	 * same settings (size, color, font, etc).
	 * Which is the same settings that the HUD text uses.
	 * @param x		X location of the Text 
	 * @param y		Y location of the Text
	 * @return		The newly-constructed Text object.
	 */
	protected Text makeText(float x, float y)
	{
		Text newText = new Text("", x, y);
		newText.setFontSize(24);
		newText.setBackColor(Color.GRAY);
		newText.setFrontColor(Color.WHITE);
		newText.setFontName("helsinki");
		return newText;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/////////////////////////////////
	//                             //
	//           UPDATES           //
	//                             //
	/////////////////////////////////
	
	
	
	
	
	
	
	/**
	 * Update everything!
	 */
	public void updateWorld()
	{
		grid.update(mouse);
		super.updateWorld();
	}
	
	
	
	
	
	
	/**
	 * @return		True if the game is paused.
	 */
	public static boolean gameIsPaused()
	{
		return paused;
	}
	
	
	
	
	/**
	 * Converts a distance measurement in pixels to a
	 * distance measurement in game units, based on the
	 * scale of the game's current convenient coordinate system.
	 * @param pixels		The number of pixels
	 * @return				The equivalent number of game units
	 */
	public static float gameUnitsFromPixels(int pixels)
	{
		return pixels / PIXELS_PER_GAME_UNIT;
	}
	
	
	/**
	 * Converts a distance measurement in game units
	 * to a distance measurement in pixels, based on
	 * the scale of the game's current convenient
	 * coordinate system.	  
	 * Note that while pixels are generally measured in
	 * whole numbers, we return a float so that the
	 * user can make any rounding choices themselves.
	 * @param gameUnits		The number of game units
	 * @return				The equivalent number of game units
	 */
	public static float pixelsFromGameUnits(float gameUnits)
	{
		return gameUnits * PIXELS_PER_GAME_UNIT;
	}
	
	
	
	////////////////////////////
	//                        //
	//  Default UI Interface  //
	//                        //
	////////////////////////////
	
	
	
	/**
	 * Turns the entire default HUD either
	 * visible or invisible, in accordance with your wishes.
	 * @param isVisible		True if you want a visible HUD.
	 */
	public void setHUDVisibilityTo(boolean isVisible)
	{
		layerHUD.setVisibilityTo(isVisible);
	}	
	
	
	/**
	 * Displays the corresponding number in the
	 * default HUD for Time.
	 * If the value is below zero, will show an X instead.
	 * @param num		The number to display.
	 */
	public void setHUDTime(int num)
	{
		if(num >= 0)
		{
			textTimeUntilNextWave.setText("" + num);
		}
		else
		{
			textTimeUntilNextWave.setText("X");
		}
	}
	
	/**
	 * Displays the corresponding text in the
	 * default HUD for Time.
	 * @param num		The number to display.
	 */
	public void setHUDTime(String time)
	{
		textTimeUntilNextWave.setText(time);
	}
	
	/**
	 * Displays the corresponding number in the
	 * default HUD for Moves.
	 * @param num		The number to display.
	 */
	public void setHUDNumberOfMoves(int num)
	{
		textMovesRemaining.setText("" + num);
	}
	
	/**
	 * Displays the corresponding number in the
	 * default HUD for Score.
	 * @param num		The number to display.
	 */
	public void setHUDScore(int num)
	{
		textScore.setText("" + num);
	}
	
	/**
	 * Displays the corresponding number in the
	 * default HUD for Medic Towers.
	 * @param num		The number to display.
	 */
	public void setHUDNumberOfTowersMedic(int num)
	{
		textTowerMedicRemaining.setText("" + num);
	}	
	
	/**
	 * Displays the corresponding number in the
	 * default HUD for Speedy Towers.
	 * @param num		The number to display.
	 */
	public void setHUDNumberOfTowersSpeedy(int num)
	{
		textTowerSpeedyRemaining.setText("" + num);
	}
	
	/**
	 * Displays the corresponding number in the
	 * default HUD for Basic Walkers.
	 * @param num		The number to display.
	 */
	public void setHUDNumberOfWalkersBasic(int num)
	{
		textSurvivalsBasic.setText("" + num);
	}
	
	
	/**
	 * Displays the corresponding number in the
	 * default HUD for Quick Walkers.
	 * @param num		The number to display.
	 */
	public void setHUDNumberOfWalkersQuick(int num)
	{
		textSurvivalsQuick.setText("" + num);
	}
}
