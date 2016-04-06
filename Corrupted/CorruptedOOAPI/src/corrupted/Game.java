package corrupted;
import gridElements.BurstVirus;
import gridElements.ChainVirus;
import gridElements.FuseVirus;
import gridElements.FuseVirus.FuseVirusTypes;
import gridElements.GridElement;
import gridElements.GridElement.ColorEnum;
import gridElements.RepairVirus;
import gridElements.Tile;

import java.awt.Color;
import java.awt.Graphics;
import java.util.EnumMap;

import layers.CorruptionLogic;
import layers.Grid;
import layers.ParticleList;
import layers.PlayerLogic;
import layers.TileLogic;
import layers.VirusLogic;
import structures.IntVector;
import structures.TurnTimer;
import Engine.ConsoleWindow;
import Engine.GameObject;
import Engine.LibraryCode;
import Engine.Vector2;


public class Game extends LibraryCode
{	
	public boolean fullScreen = false;
	
	//game timers
	protected TurnTimer spawnTimer;
	protected TurnTimer shiftTimer;
	
	//Game stuff
	private Vector2 worldSize;
	private Vector2 worldCenterOffset;
	private GameObject backdrop;
	public static final int defaultGridWidth = 26;
	public static final int defaultGridHeight = 10;
	private int gridWidth = 0;
	private int gridHeight = 0;
	private static final float topBuffer = 2.5f;
	private static final float botBuffer = 2.5f;
	private float derivedTop = .2f;
	private float derivedBot = .3f;
	private float leftBuffer = 0.666666f;
	private float rightBuffer = 0;
	private final float centerOffset = -0.5f;
	
	//layer logic classes
	private boolean playerVisibility = true;
	private boolean playerInfiniteLaser = true;
	private PlayerLogic playerHelper;
	public TileLogic tileHelper;
	public CorruptionLogic corruptionHelper;
	public VirusLogic virusHelper;
	public VirusLogic repairHelper;

	//visible layers
	private Grid corruptedGrid;
	private Grid playerGrid;
	private Grid tileGrid;
	private Grid virusGrid;
	private Grid repairGrid;
	public ParticleList Particles;
	
	//firewall 
    protected int firewallPosition = -1;
    protected GameObject firewallMarker;
    protected GameObject firewallBorder;
	
    //resource constants
    //private static final String BGM = "bg.wav";
    private static final String BGM = "bg.mp3";
	
	//sound triggers
	public enum Sounds{
		shoot,
		corruption,
		clear,
		virusspawn,
		repairready,
		repairspawn,
		repairheal,
		win,
		lose,
	}
	
	private EnumMap<Sounds, Boolean> soundTriggers;
	
	/**
	 * initializes resources for the game.
	 * The initialize() method is called from here which allows users an opportunity to initialize the tilegrid
	 * if the tilegrid is not set, a default one is provided
	 * all other grids are instantiated after.
	 * @author Brian Chau
	 */
	public void initializeWorld()
	{
		super.initializeWorld();
	    resources.setClassInJar(new JarResources());
	    resources.preloadSound(BGM);
		//initialize sounds
		soundTriggers = new EnumMap<Sounds, Boolean>(Sounds.class);
		soundTriggers.put(Sounds.shoot, false);
		soundTriggers.put(Sounds.corruption, false);
		soundTriggers.put(Sounds.clear, false);
		soundTriggers.put(Sounds.virusspawn, false);
		soundTriggers.put(Sounds.win, false);
		soundTriggers.put(Sounds.lose, false);
		

		Particles = new ParticleList();

		shiftTimer = new TurnTimer(5);
		spawnTimer = new TurnTimer(5);
	
		initialize();
		
		//if tilegrid was not set, set it.
		if(getTileGrid() == null)
		{
			this.setTileGrid(null);
		}
		
		//firewall

	    int firewallHeight = gridHeight + gridHeight/2;
		firewallPosition = 2;
		firewallMarker = new GameObject(firewallPosition ,getHeight()/2f -.5f, 1f,firewallHeight);
		firewallMarker.setSpriteSheet("firewall.png", GridElement.PIXEL_SIZE, GridElement.PIXEL_SIZE*firewallHeight, 9, 1);
		firewallMarker.setUsingSpriteSheet(true);
		firewallMarker.setToInvisible();
		firewallMarker.setAutoDrawTo(false);

		firewallBorder = new GameObject(firewallMarker.getCenterX(), firewallMarker.getCenterY(),
				521f/72f * firewallMarker.getSize().getX(), firewallMarker.getSize().getY());
		firewallBorder.setImage("firewallBorder.png");
		firewallBorder.setAutoDrawTo(false);

		ConsoleWindow.setShowConsole(false);
		if(fullScreen){
			window.setScreenToFullscreen();
		}
		ConsoleWindow.bringToTop();
	}


	/**
	 * Initializes an empty console and sizes it correctly
	 */
	protected void initializeConsole() {
		ConsoleWindow.setPosition(new Vector2(getWidth()*.5f,getHeight()).add(new Vector2(centerOffset,centerOffset)));
		ConsoleWindow.setWidth(getWidth()/2);
		ConsoleWindow.setHeight(getHeight());
		ConsoleWindow.setBackgroundTexture("Console.png");

		ConsoleWindow.bringToTop();
	}
	
	/**
	 * set the text console strings
	 * Only the first 10 strings will be used
	 * @author Brian Chau
	 */
	public void setConsole(String[] strings)
	{
		ConsoleWindow.clear();
		for (int i = 0; i < strings.length; i++)
			ConsoleWindow.writeLine(strings[i]);
	}
	
	/**
	 * Shows the text console
	 * @author Brian Chau
	 */
	public void showConsole()
	{
		ConsoleWindow.setShowConsole(true);

		ConsoleWindow.bringToTop();
	}
	
	/**
	 * Hides the text console
	 * @author Brian Chau
	 */
	public void hideConsole()
	{
		ConsoleWindow.setShowConsole(false);
	}
	
	/**
	 * toggles visibility for text console on and off
	 * @author Brian Chau
	 */
	public void toggleConsoleVisibiltiy()
	{
		ConsoleWindow.toggleShowConsole();

		ConsoleWindow.bringToTop();
	}
	
	/**
	 * gets the corruptedgrid
	 * @author Brian Chau
	 * @return 2D array from the corruptedGrid (same dimensions as tileGrid)
	 */
	public GridElement[][] getCorruptedGrid()
	{
		if (tileGrid == null)
		{
			this.setTileGrid(null);
		}
		return corruptedGrid.getGrid();
	}
	
	/**
	 * gets the tilegrid
	 * @author Brian Chau
	 * @return 2D array from the tilegrid
	 */
	public GridElement[][] getTileGrid()
	{
		if (tileGrid == null)
		{
			this.setTileGrid(null);
		}
		return tileGrid.getGrid();
	}
	/**
	 * gets the virusgrid
	 * @author Brian Chau
	 * @return 2D array from the virusGrid (same dimensions as tileGrid)
	 */
	public GridElement[][] getVirusGrid()
	{
		if (tileGrid == null)
		{
			this.setTileGrid(null);
		}
		return virusGrid.getGrid();
	}
	/**
	 * gets the repairgrid
	 * @author Brian Chau
	 * @return 2D array from the repairGrid (same dimensions as tileGrid)
	 */
	public GridElement[][] getRepairGrid()
	{
		if (tileGrid == null)
		{
			this.setTileGrid(null);
		}
		return repairGrid.getGrid();
	}
	

	
	/**
	 * Users should Override this method with their own initialization.
	 * in this method, they should call setTileGrid to initialize the grids
	 * otherwise, it will be set to a default empty 25x10 grid.
	 * @author Brian Chau
	 */
	protected void initialize(){}
	
	/**
	 * This method puts a user provided Tile column into the grid at a certain horizontal position.
	 * If hPosition exceeds existing bounds or the length of the array is incorrect, it will fail out.
	 * If there is already existing data in this column, it is overwritten.
	 * @author Brian Chau
	 * @param tline array of GridElements to add 
	 * @param hPosition horizontal position to add the column
	 */
	public void setTileColumn(GridElement[] tline, int hPosition)
	{
		if(tline == null || tline.length == 0)
		{
			return;	// do nothing if null
		}
		//if existing grid is null set a new one
		if(this.tileGrid == null)
		{
			gridHeight = tline.length;
			if(gridWidth <= 0)
			{
				gridWidth = defaultGridWidth;
			}
			if(hPosition > gridWidth)
			{
				gridWidth = hPosition+1;
			}
			GridElement[][] tempgrid = new GridElement[gridWidth][gridHeight];
			setTileGrid(tempgrid);
		}
		//drop the new column into the grid. we will overwrite existing data
		// any size issues should be handled here
		tileGrid.setColumn(tline, hPosition);
	}
	
	/**
	 * gets a 1D GridElement array from the tileGrid based on a column position
	 * @author Brian Chau
	 * @param hPosition horizontal column position
	 * @return a 1D GridElement array containing the desired Column
	 */
	public GridElement[] getTileColumn(int hPosition)
	{
		if(tileGrid == null)
		{
			int tempHeight = gridHeight;
			if(tempHeight <= 0)
			{
				tempHeight = defaultGridHeight;
			}
			GridElement[] tline = new GridElement[tempHeight];
			this.setTileColumn(tline, hPosition);
		}
		return this.tileGrid.getColumn(hPosition);
	}
	
	/**
	 * Sets the tileGrid using a player provided 2D GridElement array.
	 * The length of the outer array becomes the gridWidth
	 * The length of the inner arrays becomes the gridHeight
	 * If the array is jagged (inconsistent inner array lengths), a default gridHeight of 25 is provided
	 * if no array is provided or the array has invalid size in either dimension, a default 25x10 grid is provided
	 * @author Brian Chau
	 * @param tgrid player provided grid
	 */
	public void setTileGrid(GridElement[][] tgrid)
	{
		//verify dimensions
		if(tgrid == null || tgrid.length == 0)
		{
			//if gridwith is null or too small, define a default grid
			gridWidth = defaultGridWidth;
			gridHeight = defaultGridHeight;
			tgrid = new GridElement[gridWidth][gridHeight];
		}
		else
		{
			gridWidth = tgrid.length;
			int tempGridHeight = tgrid == null ? 0 : tgrid[0].length;
			for(int i = 0; i < gridWidth; i++)
			{
				if(tgrid[i].length != tempGridHeight)
				{
					//if we found jagged, we deny and provide default grid
					setTileGrid(null);
					return;
				}
			}
			if(tempGridHeight == 0)
			{
				//if gridHeight is null, too small or inconsistent (jagged 2d array), define a default grid
				setTileGrid(null);
				return;
			}
			else
			{
				//otherwise, everything is ok.
				gridHeight = tempGridHeight;
				
			}
		}
		//draw backdrop under everything
		derivedTop = gridHeight * topBuffer/defaultGridHeight;
		derivedBot = gridHeight * botBuffer/defaultGridHeight;
		worldSize = new Vector2(leftBuffer+gridWidth+rightBuffer,derivedBot + gridHeight + derivedTop);
		worldCenterOffset = new Vector2(centerOffset - leftBuffer, centerOffset - derivedBot);
		Vector2 worldCenter = new Vector2(worldSize.getX()/2f + worldCenterOffset.getX(),worldSize.getY()/2f + worldCenterOffset.getY());
		//backdrop
		backdrop = new GameObject(worldCenter.getX(), worldCenter.getY(), worldSize.getX(), worldSize.getY());
		backdrop.setColor(Color.MAGENTA);
		backdrop.setSpriteSheet("bg.png", 1920, 1080, 10, 5);
		backdrop.setUsingSpriteSheet(true);
		backdrop.setAutoDrawTo(false);	
		//set the Tilegrid as the input grid and generate other grids to be the same size
		initializeLayerStuff(tgrid);
		//skullIcon = new SkullIndicator(worldSize, worldCenter);
		//skullIcon.setGreen();
		initializeConsole();
	}

	/**
	 * gets the gridWidth.
	 * if called before grids were initialized, they will be forced to initialize with default values
	 * @author Brian Chau
	 * @return width of the grids (all grids should be the same dimensions)
	 */
	public int getWidth()
	{
		if(gridWidth <= 0)
		{
			
			setTileGrid(null);
		}
		return gridWidth;
	}
	
	/**
	 * gets the gridHeight.
	 * if called before grids were initialized, they will be forced to initialize with default values
	 * @author Brian Chau
	 * @return height of the grids (all grids should be the same dimensions)
	 */
	public int getHeight()
	{
		if(gridHeight <= 0)
		{
			setTileGrid(null);
		}
		return gridHeight;
	}
	
	/**
	 * initializes each grid and provides their reference to their respective layer logic wrappers
	 * if a tileGrid was provided, it will be used. all other grids are replaced with new grids with the same dimensions as tileGrid
	 * 
	 * this is used internally and assumes tgrid has been validated and is not jagged or null
	 * @author Brian Chau
	 * param tgrid tileGrid to initialize with.
	 */
	private void initializeLayerStuff(GridElement[][] tgrid)
	{
		if (gridWidth <= 0) gridWidth = defaultGridWidth;
		if (gridHeight <= 0) gridHeight = defaultGridHeight;
		

		//destroy any existing grids to prevent drawing errors
		/*
		if(playerGrid != null)
			playerGrid.destroy();
		if(tileGrid != null) 
			tileGrid.destroy();
		if(virusGrid != null) 
			virusGrid.destroy();
		if(corruptedGrid != null) 
			corruptedGrid.destroy();
		if(repairGrid != null) 
			repairGrid.destroy();
		if(Particles != null)
			Particles.destroy();
			*/
		
		playerGrid = new Grid(this,new GridElement[gridWidth][gridHeight]);

		tileGrid = new Grid(this, tgrid);
		virusGrid = new Grid(this,new GridElement[gridWidth][gridHeight]);
		repairGrid = new Grid(this,new GridElement[gridWidth][gridHeight]);

		corruptedGrid = new Grid(this,new GridElement[gridWidth][gridHeight]);
		//order of the definition of the layers determines draw order
		playerHelper = new PlayerLogic(this,playerGrid.getGrid());
		playerHelper.setPlayerVisibility(playerVisibility);
		playerHelper.setPlayerInfiniteLaserDrawing(playerInfiniteLaser);

		
		tileHelper = new TileLogic(this);

		corruptionHelper = new CorruptionLogic(this);
		virusHelper = new VirusLogic(this, false);

		repairHelper = new VirusLogic(this, true);
		
		//set camera view
		world.SetWorldCoordinate(worldSize.getX(),worldSize.getY());
		world.setPosition(worldCenterOffset.getX(), worldCenterOffset.getY());
	}

	
	/**
	 * Game update loop
	 * This calls the update() method which provides the user code to implement their own logic
	 * after update() GridElement positions are verified and forced to match their 2D array grid indexes
	 * @author Brian Chau
	 */
	public void updateWorld()
	{
		super.updateWorld();
		update();
		
		syncPositions();
		Particles.update();
		if(tileHelper != null){
		tileHelper.connectAllTiles();
		}
		this.playTriggeredSounds();
	}

	/**
	 * Users can Override this method with their own update logic. This method is called each frame.
	 * in this method, they can use keyboard or mouse events to trigger 2D array manipulation and other desired game logic.
	 * @author Brian Chau
	 */
	public void update() 
	{
	}
	
	/**
	 * This method updates the Virus and repair layers with the canned corruption logic
	 * (corruptions and tiles are have no active actions)
	 * @author Brian Chau
	 */
	public void runLayerUpdateSequence()
	{
		//return if helpers are not initialized;\
		if (virusHelper == null) return;
		//update Virus and corruption and tile destruction (mark for delete only)
		virusHelper.TurnBasedUpdate();

		repairHelper.TurnBasedUpdate();
		//update Virus spreading (additional corruption and tile destruction can be done here if necessary too)
		virusHelper.spreadUpdate();

		repairHelper.spreadUpdate();
		//update tiles destroyed by viruses (must be done after previous loop is done to avoid prematurely removing tiles that are yet to be updated)
		deleteAllMarked();
		//spawn new Viruses if there arent any other viruses
		if(virusHelper.isEmpty()){
			if(incrementSpawnTimer())
			{
				virusHelper.spawnUpdate();
				this.triggerSound(Sounds.virusspawn);
			}
		}
		//DO NOT SPAWN REPAIR LAYER, WE DO THIS MANNUALLY
	}
	
	/**
	 * increments the timer from shifting the grids and checks if it is time to shift the grids
	 * @author Brian Chau
	 * @return true if it is time to shift the grids
	 */
	protected boolean incrementShiftTimer()
	{
		boolean rtn = shiftTimer.tick();
		return rtn;
	}
	
	/**
	 * increments the timer from shifting the grids and checks if it is time to shift the grids
	 * @author Brian Chau
	 * @return true if it is time to shift the grids
	 */
	protected boolean incrementSpawnTimer()
	{
		return spawnTimer.tick();
	}

	/**
	 * shifts down each of the game grids
	 * @author Brian Chau
	 */
	public void shiftGridsDown() {
		shiftGridDown(this.getCorruptedGrid());
		shiftGridDown(this.getTileGrid());
		shiftGridDown(this.getVirusGrid());
		shiftGridDown(this.getRepairGrid());
	}
	
	/**
	 * Shifts all elements to the left (towards the player).
	 * This makes assumptions that the grid size is equal to gridWidth and gridHeight
	 * This leaves the rightmost line empty.
	 * @author Brian Chau
	 * @param grid to advance
	 * @return the leftmost line that was shifted off the grid
	 */
	private GridElement[] shiftGridDown(GridElement[][] mgrid) {
		GridElement[] passedElements = mgrid[0];

		for (int x = 1; x < getWidth(); x++) {
			mgrid[x-1] = mgrid[x];
		}
		//make sure to null out the rightmost. 
		//this is redundant if we are going to immediately add a new row after.
		//this is a safety precaution
		mgrid[gridWidth-1] = new GridElement[gridHeight];
		for (int y = 0; y < gridHeight; y++){
			mgrid[gridWidth-1][y] = null;
		}	
		
		return passedElements;
	}
	
	/**
	 * this method checks the position of all GridElements in each layer and 
	 * if the primitive positions are not in the correct place (grid index representation),
	 * it moves them to the correct place and spawns a movement particle.
	 */
	public void syncPositions()
	{
		//if one is null they are all null
		if(tileHelper != null){
		corruptionHelper.syncPositions();
		playerHelper.syncPositions();
		tileHelper.syncPositions();
		virusHelper.syncPositions();
		repairHelper.syncPositions();
		updateTileStates();
		}
	}
	
	/**
	 * this changes the texture of tiles when corruption is on them
	 */
	private void updateTileStates()
	{
		GridElement[][] tgrid = getTileGrid();
		GridElement[][] cgrid = getCorruptedGrid();
		try{
			for(int x = 0; x < getWidth(); x++)
			{
				for (int y = 0; y < getHeight(); y++)
				{
					if(tgrid[x][y] != null)
					{
						//if this tile has a corruption on it, make it use special graphic
						try
						{
							Tile currentTile = (Tile)tgrid[x][y];
							currentTile.useSpecialTexture(cgrid[x][y] != null);
						}
						catch(ClassCastException e)
						{
							ErrorHandler.printErrorAndQuit(ErrorHandler.EXPECTED_TILE);
						}
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			ErrorHandler.printErrorAndQuit(ErrorHandler.OUT_OF_BOUNDS);
		}
	}
	
	/**
	 * deletes all marked tiles
	 * @author Brian Chau
	 */
	public void deleteAllMarked()
	{
		//if one is null, they are all null
		if(tileHelper != null){
		tileHelper.deleteAllMarked();
		virusHelper.deleteAllMarked();
		repairHelper.deleteAllMarked();
		corruptionHelper.deleteAllMarked();
		}

	}
	
	/**
	 * Creates a tile based on the player location color.
	 * The center of the returned tile will be set to the player's current location
	 * @author Brian Chau
	 * @return Tile generated by player.
	 */
	public Tile generatePlayerTile()
	{
		if(playerHelper != null){
		this.triggerSound(Sounds.shoot);
		return playerHelper.generatePlayerTile();
		}
		return new Tile(this);
	}
	
	/**
	 * gets the location of the player
	 * @author Brian Chau
	 * @return IntVector representing the player's location
	 */
	public IntVector getPlayerPosition()
	{
		IntVector rtn  = new IntVector(0,0);
		if(playerHelper != null){
		rtn = playerHelper.getPlayerPosition();
		}
		return rtn;
	}
	
	/**
	 * gets the height of the player
	 * @author Brian Chau
	 * @return the height of the player as an int.
	 */
	public int getPlayerHeight()
	{
		int rtn = 0;
		if(playerHelper != null){
		rtn = getPlayerPosition().getY();
		}
		return rtn;
	}
	
	/**
	 * sets the player's height position to pos.
	 * If pos is greater or equal to the game's set height, it is set to height-1.
	 * if pos is less than 0, it is set to 0.
	 * 
	 * @author Brian Chau
	 * @param pos height position to set the player
	 */
	public void setPlayerHeight(int pos)
	{
		if(playerHelper != null){
		playerHelper.setPlayerHeight(pos);
		}
	}
	
	/**
	 * gets the current color of the player
	 * @author Brian Chau
	 * @return ColorEnum representing the color of the player
	 */
	public ColorEnum getPlayerColorEnum()
	{
		ColorEnum rtn = ColorEnum.RED;
		if(playerHelper != null){
		rtn = playerHelper.getPlayerColor();
		}
		return rtn;
	}
	
	/**
	 * sets the color of the player
	 * @author Brian Chau
	 * @param col ColorEnum to set the player
	 */
	public void setPlayerColorEnum(ColorEnum col)
	{
		if(playerHelper != null){
		playerHelper.setPlayerColorEnum(col);
		}
	}
	
	/**
	 * Sets the targeting laser mode for the player (this affects drawing only).
	 * InfiniteLaserMode true: laser will draw to the end of the screen if there is a clear path.
	 * InfiniteLaserMode false: laser will stop on the last column that has tiles on it
	 * @param mode if true, InfiniteLasermode will be on.
	 */
	public void setPlayerLaserInfiniteDrawingMode(boolean mode)
	{
		playerInfiniteLaser = mode;
		if(playerHelper != null){
			playerHelper.setPlayerInfiniteLaserDrawing(mode);
		}
	}
	
	/**
	 * Sets the visibility of the player cannon. Some games may not want to use the cannon functionality. 
	 * @param visible if true, the player will be drawn. If false, the player will be invisible
	 */
	public void setPlayerVisibility(boolean visible)
	{
		playerVisibility = visible;
		if(playerHelper != null){
			playerHelper.setPlayerVisibility(visible);
		}
	}
	/**
	 * moves the player up one position
	 * @author Brian Chau
	 */
	public void movePlayerUp()
	{
		if(playerHelper != null){
			playerHelper.movePlayerUp();
		}
	}
	/**
	 * moves the player down one position
	 * @author Brian Chau
	 */
	public void movePlayerDown()
	{
		if(playerHelper != null){
			playerHelper.movePlayerDown();
		}
	}
	
	/**
	 * gets the mouse position and translates it into world coordinates
	 * 
	 * @author Samuel Kim
	 * @return IntVector containing mouse position
	 */
	public IntVector getMouseCenter(){
		
		int x,y;
		
		if(mouse.getWorldX()-(int)mouse.getWorldX()>= 0.5f ){
			x = (int)mouse.getWorldX()+1;
		}else{
			x = (int)mouse.getWorldX();
		}

		if(mouse.getWorldY()-(int)mouse.getWorldY()>= 0.5f ){
			y = (int)mouse.getWorldY()+1;
		}else{
			y = (int)mouse.getWorldY();
		}
		
		if(x < 0) x = 0;
		if(y < 0) y = 0;
		
		if(x > getWidth()-1) x = getWidth()-1; 
		if(y > getHeight()-1) y = getHeight()-1;
		
		Vector2 MC = new Vector2(x,y);
		
		return new IntVector(MC);
	}
	
	/**
	 * Plays the background music
	 */
	public void playBackgroundMusic()
	{
		if(!resources.isSoundPlaying(BGM))
		{
			resources.playSoundLooping(BGM);
		}
	}
	
	public void stopBackgroundMusic()
	{
		if(resources.isSoundPlaying(BGM))
		{
			resources.stopSound(BGM);
		}
	}
	
	/**
	 * trigger a given sound
	 * @author Brian Chau
	 * @param key sound to be triggered
	 */
	public void triggerSound(Sounds key)
	{
		this.soundTriggers.put(key, true);
	}
	
	/**
	 * play any sounds that have been triggered since the last time this method was called
	 * @author Brian Chau
	 */
	private void playTriggeredSounds()
	{
		for(EnumMap.Entry<Sounds,Boolean> entry : soundTriggers.entrySet())
		{
			if(entry.getValue()){
				resources.playSound(entry.getKey().toString() + ".wav");
				soundTriggers.put(entry.getKey(), false);
			}
		}
	
	}
	
	@Override
	public void draw(Graphics gfx) 
	{
		super.draw(gfx);
		backdrop.draw();
		corruptedGrid.draw();
		
		if(firewallMarker.isVisible()){
			firewallMarker.draw();
		}
		firewallBorder.draw();

		playerGrid.draw();
		tileGrid.draw();
		virusGrid.draw();
		repairGrid.draw();
		Particles.draw();
		ConsoleWindow.forceDraw();
	}
	
}
