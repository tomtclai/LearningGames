package corruptedUser;
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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import layers.ExteriorLayer;
import structures.IntVector;
import structures.TurnTimer;
import ui.*;
import ui.StatusScreen.StatusScreenType;
import Engine.GameObject;
import Engine.Vector2;
import corrupted.Game;

public class UserCodeCorruptedFull extends Game{
	
	//Game State stuff
//	private int FpsPull = 0;
//	private int FpsPullMax = 40;
	
	private static final int GAMEOVER_COUNTER_MAX = 160;
	private int gameOverCounter;
	private enum GameState {Splash, Playing, Win, Lose, Credits}
	private GameState state = GameState.Playing;
	private StatusScreen mGameOverScreen;
	private StatusScreen mWinScreen;
	private CreditsScreen mCreditsScreen;

	private TextObject rowsRemainingText;
	//protected ScoreSystem scoresystem;
	
	///debug members
	protected boolean debugMode = false;
	protected boolean pauseLine = false;
	private GameObject debugPosition,debugPosition2; 
	
	//game text
	String pauseLineString = "[P]Force Line Drop [Space]Pause Line Drop Timer";
	String[] consoleStrings= new String[]
	{
		"Game controls:",
		"[up/down]Move [right]Shoot Tile [left] Repair Protocol",
		"[Enter]Toggle Fullscreen [Z]Reset ",
		"[F1]Quit [F2]Toggle Debug [F3]Toggle Console",
		"Debug controls:",
		"[1]Red [2]Green [3]Blue [4]Cyan [5]Magenta [6]Yellow",
		"[Q]Burst [W]Fuse [E]Chain [R]Repair [T]Tile [Y]Corrupt",
		pauseLineString + pauseLine,
		"[[]]Adjust Lose Condition"
	};
	
	//exterior layers
	private int initialTileRows = 35;
	public ExteriorLayer CorBelow;
	public ExteriorLayer TilesAbove;

//	//shiftwarning
//	private int shiftWarningPosition = -1;
//	private Rectangle shiftWarningMarker;
//	
//	//viruswarning
//	private VirusWarning virusWarningMarker;
	
	private RepairMeter repairMeter;
	private RowMeter rowsLeftMeter;
	private	CorruptionBarMeter corruptionMeter;
	
	//splash
	private int splashTimer = 120;
	private GameObject splashRect;
	
	private debugSpawnStates mDebugSpawnState=debugSpawnStates.Idle;
	private enum debugSpawnStates{
		Idle,
		Burst,
		Chain,
		Fuse,
		Repair,
		Tile,
		Corruption
	}
	
	@Override
	public void initialize()
	{
		//this.fullScreen = true;
		shiftTimer = new TurnTimer(5);
		spawnTimer = new TurnTimer(5);

		int width = 26;
		int height = 10;

		//reinitialize grid here to draw it over firewall;
		this.setTileGrid(new GridElement[width][height]);
		CorBelow = new ExteriorLayer(this, false);
		TilesAbove = new ExteriorLayer(this, true);
		TilesAbove.addTileRows(initialTileRows);
		this.setPlayerLaserInfiniteDrawingMode(false);
		
		//console stuff
		initializeConsole();
		setConsole(consoleStrings);
		showConsole();
		
		//virus timer 
//		virusTimeText = new TextObject("Turns until Virus Spawn: "+ spawnTimer.howLong(), 
//				new Vector2(0f, 1.05f*getHeight()), 18, Color.WHITE, Color.BLACK);


		//debug position markers
		debugPosition = new GameObject(-100,-100, 1,1);
		debugPosition.setColor(new Color(125,125,125,125));
		
		debugPosition2 = new GameObject(-100,-100, 1,1);
		debugPosition2.setColor(new Color(125,125,125,125));

		//repairMeter
		initializeRepairMeter();
		//rowsLeftMeter
		initializeRowsLeftMeter();
		//corruptionMeter
		initializeCorruptionMeter();
		//scoresystem = new ScoreSystem(this);
		
//		//shiftWarning
//		shiftWarningPosition = getWidth()-1;
//		shiftWarningMarker = new GameObject();
//		shiftWarningMarker.center = new Vector2( shiftWarningPosition,getHeight()/2 -.5f);
//		shiftWarningMarker.size = new Vector2(1f,getHeight());
//		shiftWarningMarker.setImage("firewall.png");
//		shiftWarningMarker.visible = false;
//		//virusWarning
//		Vector2 vMarkerCenter = new Vector2(world.getPositionX()+world.getWidth()/2f, world.getWorldPositionY()+world.getHeight()/30f);
//		Vector2 vMarkerSize = new Vector2(world.getHeight()/15f,world.getHeight()/15f);
//		virusWarningMarker = new VirusWarning(vMarkerCenter, vMarkerSize);

		for(int i=0;i<8;i++) shiftGridsDownAndHandleExteriors();

		updateScore();	
		//TL.connectAllTiles();
	
		//game state screens
		mGameOverScreen = new StatusScreen(StatusScreenType.LOSE);
		mWinScreen = new StatusScreen(StatusScreenType.WIN);
		mCreditsScreen = new CreditsScreen();
		
		splashRect = new GameObject();
		splashRect.setSize(new Vector2(world.getWidth(), world.getHeight()));
		splashRect.setCenter(new Vector2(world.getPositionX()+splashRect.getSize().getX()/2f, world.getWorldPositionY()+splashRect.getSize().getY()/2f));
		splashRect.setImage("Splash.png"); //TODO:magic numbers
		this.setPlayerLaserInfiniteDrawingMode(false);
		setState(GameState.Splash);
		
		//this.setPlayerLaserInfiniteDrawingMode(true);
	}
	
	/**
	 * sets the gamestate (playing, win, lose)
	 * @author Brian Chau
	 * @param newstate state to be set
	 */
	public void setState(GameState newstate)
	{
		state = newstate;
		if(state.equals(GameState.Lose) || state.equals(GameState.Win))
		{
			stopBackgroundMusic();
		}
		if(state.equals(GameState.Splash))
		{
			splashRect.setVisibilityTo(true);
			splashRect.moveToFront();
			splashTimer = 120;
		}
		else if(state.equals(GameState.Playing))
		{
			splashRect.setVisibilityTo(false);
			playBackgroundMusic();
		}
		else if (state.equals(state.Lose))
		{
			this.triggerSound(Sounds.lose);
			gameOverCounter = GAMEOVER_COUNTER_MAX;
		}
		else if (state.equals(state.Win))
		{
			this.triggerSound(Sounds.win);
			gameOverCounter = GAMEOVER_COUNTER_MAX;
		}

		else if (state.equals(state.Credits))
		{
			this.mCreditsScreen.reset();
		}
			
	}
	
	private void updateScore()
	{
		//scoresystem.updateScores();
		int corruptionLevel = corruptionHelper.count()+CorBelow.count();
		corruptionMeter.setValue(corruptionLevel);
		if(corruptionMeter.getPercentFull()>corruptionMeter.getLosePercent())
		{
			setState(GameState.Lose);
		}
		if(tileHelper.count()+TilesAbove.count() == 0)
		{
			setState(GameState.Win);
		}
	}
	
	protected void setDebugTextVisibility(boolean vis)
	{
		//virusTimeText.visible =vis;
//		msgDebug.visible =vis;
//		msgDebug2.visible =vis;
//		msgDebug3.visible =vis;
//		msgDebug4.visible =vis;
		debugPosition.setVisibilityTo(vis && (mDebugSpawnState != debugSpawnStates.Idle));
		debugPosition2.setVisibilityTo(vis && (mDebugSpawnState != debugSpawnStates.Idle));
	}
	
	@Override
	public void update()
	{
		
//		FpsPull = (FpsPull+1)%FpsPullMax;
//		if(FpsPull == 0)
//		{
//			System.out.println(getFPS());
//		}
		if(state == GameState.Splash)
		{
			SplashStateUpdate();
		}
		
		if(state == GameState.Playing)
		{
			if (debugMode)
			{
				debugUpdateSection();
			}
			PlayingStateUpdate();
		}
		
		if(state == GameState.Lose)
		{
			LoseStateUpdate();
		}
		if(state == GameState.Win)
		{
			WinStateUpdate();
		}
		if(state == GameState.Credits)
		{
			CreditsStateUpdate();
		}
	}
	
	private void SplashStateUpdate()
	{
		this.mWinScreen.setVisible(false);
		this.mGameOverScreen.setVisible(false);
		this.mCreditsScreen.setVisible(false);
		if(splashTimer > 0)
		{
			splashTimer--;
		}
		else
		{
			setState(GameState.Playing);
			Restart();
		}
	}
	
	private void CreditsStateUpdate()
	{
		this.mWinScreen.setVisible(false);
		this.mGameOverScreen.setVisible(false);
		this.mCreditsScreen.setVisible(true);
		mCreditsScreen.update();
		if(keyboard.isButtonTapped(KeyEvent.VK_LEFT)||
		keyboard.isButtonTapped(KeyEvent.VK_RIGHT)||
		keyboard.isButtonTapped(KeyEvent.VK_UP)||
		keyboard.isButtonTapped(KeyEvent.VK_DOWN)||
		keyboard.isButtonTapped(KeyEvent.VK_SPACE)||
		keyboard.isButtonTapped(KeyEvent.VK_ESCAPE))
		{
			setState(GameState.Playing);
			this.Restart();
		}
	}
	
	/**
	 * this method is called by Game's update method if state is GameState.Lose
	 * it can be overridden with update logic that should be run during the lose state
	 * @author Brian Chau
	 */
	private void LoseStateUpdate()
	{

		
		this.mWinScreen.setVisible(false);
		this.mGameOverScreen.setVisible(true);
		this.mCreditsScreen.setVisible(false);
		gameOverCounter--;
		if (gameOverCounter <= 0)
		{
			setState(GameState.Credits);
		}
			
			
	}
	/**
	 * this method is called by Game's update method if state is GameState.Win
	 * it can be overridden with update logic that should be run during the win state
	 * @author Brian Chau
	 */
	private void WinStateUpdate()
	{
		this.mWinScreen.setVisible(true);
		this.mGameOverScreen.setVisible(false);
		this.mCreditsScreen.setVisible(false);
		gameOverCounter--;
		if (gameOverCounter <= 0)
		{
			setState(GameState.Credits);
		}
	}
	/**
	 * this method is called by Game's update method if state is GameState
	 * @author Brian Chau
	 */
	private void PlayingStateUpdate()
	{
		this.mWinScreen.setVisible(false);
		this.mGameOverScreen.setVisible(false);
		this.mCreditsScreen.setVisible(false);
		
		setDebugTextVisibility(debugMode);
		
		if(keyboard.isButtonTapped(KeyEvent.VK_F1))
		{
			System.exit(0);
		}		
		
		if(keyboard.isButtonTapped(KeyEvent.VK_F2))
		{
			debugMode = !debugMode;
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_F3))
		{
			this.toggleConsoleVisibiltiy();
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_DOWN))
		{
			movePlayerDown();
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_UP))
		{
			movePlayerUp();
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_RIGHT))
		{
			turnBasedUpdate();
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_LEFT))
		{
			if(repairMeter.isFull() && !(corruptionHelper.isEmpty() && virusHelper.isEmpty()))
			{
				spawnRepairProtocol();
				repairMeter.reset();
			}
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_ENTER) ){
			if(fullScreen == false){
				window.setScreenToFullscreen();
				fullScreen = true;
				
			}else{
				window.setScreenToWindowed();
				fullScreen = false;
			}
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_Z))
		{
			Restart();
		}
	}
	
	
	/**
	 * creates a tile based on the player location color and 
	 * places it on the last empty spot before an occupied tile space
	 * @author Brian Chau
	 * @return location of the tile that was placed
	 */
	public IntVector placePlayerTile()
	{
		this.triggerSound(Sounds.shoot);
		Tile tile = generatePlayerTile();
		IntVector pos = placePlayerTile(tile);
		return pos;
	}
	
	
	
	/**
	 * using a player-generated tile, this method traces a horizontal-rightward path until it hits another tile.
	 * this tile is then placed in the last empty spot
	 * @author Brian Chau
	 * @param tile to be placed (this should be a tile located at the player's location. 
	 * source position and color is derived from here)
	 * @return ending position of the tile
	 */
	public IntVector placePlayerTile(Tile tile)
	{
		int maxColumn = -1;
		//if rows not finished, shoot until the end
		if(TilesAbove.rowCount() > 0)
		{
			maxColumn = this.getWidth();
		}
		//otherwise set max at first non-null column
		else{
			maxColumn = tileHelper.getFurthestColumn();
		}
		int x = (int)tile.getCenter().getX();
		int y = (int)tile.getCenter().getY();
		//find the last empty y position
		while ( tileHelper.withinBounds(x+1,y) && tileHelper.getElement(x+1, y) == null && x < maxColumn)
		{
			x++;
		}
		//move the geometry
		tile.moveTo(true, new IntVector(x,y));
		tileHelper.putTile(new IntVector(x,y), tile);
		
		return new IntVector(x,y);
	}
	
	public void turnBasedUpdate()
	{
		//update player shooting. this will mark tiles and viruses for deletion
		IntVector tilepos = placePlayerTile();
		int numTilesCleared = this.handlePlayerTileCollision(tilepos);
		
		//increment repairMeter
		float previousRepairValue = repairMeter.getValue();
		repairMeter.incrementValue(numTilesCleared);
		float currentRepairValue = repairMeter.getValue();
		if (previousRepairValue < repairMeter.getMaxValue() && currentRepairValue >= repairMeter.getMaxValue())
		{
			this.triggerSound(Sounds.repairready);
		}
		
		
		//delete tiles that are marked
		deleteAllMarked();
		
		runLayerUpdateSequence();

		setPlayerColorEnum(tileHelper.getRandomExistingColor());
		//tiles shift down
		if(!pauseLine)
		{
			if(incrementShiftTimer())
			{
				shiftGridsDownAndHandleExteriors();
			}
		}
		//activate firewall
		firewallClear();
		//update scores

		//TL.connectAllTiles();
		updateScore();
	}
	
	protected void shiftGridsDownAndHandleExteriors()
	{
		pushEndLineForCL();
		shiftGridsDown();

		if(!TilesAbove.isEmpty()){
			generateStartLineForTL();
		}
		rowsRemainingText.setText(""+ TilesAbove.rowCount());
		rowsLeftMeter.setValue(TilesAbove.rowCount());
	}
	
	protected void pushEndLineForCL()
	{
		//take the last row of CL and put it into the ExternalLayer
		CorBelow.pushRow(getCorruptedGrid()[0]);
	}
	
	/**
	 * generates a random set of tiles on the top row
	 * @author Brian Chau
	 */
	protected void generateStartLineForTL()
	{
		GridElement[] newLine = TilesAbove.popRow();

		if (newLine == null) return;
		getTileGrid()[getWidth()-1] = newLine;
	}

	@Override
	protected boolean incrementShiftTimer()
	{
		boolean rtn = super.incrementShiftTimer();
//		int shiftwait =shiftTimer.howLong();
//		if(shiftwait == 1)
//		{
//			shiftWarningMarker.visible = true;
//		}
//		else
//		{
//			shiftWarningMarker.visible = false;
//		}
		return rtn;
	}

	@Override
	protected boolean incrementSpawnTimer()
	{
		boolean rtn = super.incrementSpawnTimer();
		//update text
		//virusTimeText.setText("Turns until Virus Spawn: "+ spawnTimer.howLong());
//		this.virusWarningMarker.setTexture(spawnTimer.howLong()-1);
		return rtn;
	}
	
	public void firewallClear()
	{
		boolean showFirewall = false;
		for(int x = 0; x <= firewallPosition; x++)
		{
			for (int y = 0; y < getHeight(); y++)
			{
				boolean tile = tileHelper.markForDelete(x, y);				
				boolean virus = virusHelper.markForDelete(x, y);
				boolean repair = repairHelper.markForDelete(x, y);
				if(tile || virus || repair)
				{
					showFirewall = true;
					GridElement[][] corrGrid = getCorruptedGrid();
					if(tile && corrGrid[x][y] == null)
					{
						corruptionHelper.addCorruption(new IntVector(x,y));
					}
				}
			}
		}
		if(showFirewall && !firewallMarker.isVisible())
		{
			firewallMarker.setToVisible();
		}
	}
	
	
	
	/**
	 * this method is called by the player object. It places the given tile into the TileLayer
	 * and attempts 
	 * @author Brian Chau
	 * @param tile
	 * @return number of tiles marked for deletion
	 */
	public int handlePlayerTileCollision(IntVector pos)
	{
		ArrayList<IntVector> matches = tileHelper.getContiguousTiles(pos);
		if (matches.size() >= 3)
		{
			tileHelper.markForDelete(matches);
		}
		virusHelper.markForDelete(matches);
		repairHelper.markForDelete(matches);
		//if not enough to match return 0
		if(matches == null || matches.size() < 3) return 0;
		//otherwise return number of matched tiles
		return matches.size();
	}
	
	public void spawnRepairProtocol()
	{
		repairHelper.spawnUpdate();
		this.triggerSound(Sounds.repairspawn);
	}

	public void Restart(){
		TilesAbove.clear();
		TilesAbove.addTileRows(initialTileRows);
		tileHelper.clear();
		corruptionHelper.clear();
		CorBelow.clear();
		virusHelper.clear();
		repairHelper.clear();
		
		repairMeter.reset();
		//initializeCorruptionMeter();
		for(int i=0;i<8;i++) shiftGridsDownAndHandleExteriors();
		firewallMarker.setToInvisible();
		//setState(GameState.Playing);

		//TL.connectAllTiles();
		updateScore();
		
	}
	private void initializeRepairMeter()
	{
		Vector2 position = new Vector2(world.getPositionX() + (world.getHeight()*(1/10f)), world.getWorldPositionY()+world.getHeight()*(1/10f));
		Vector2  size = new Vector2(world.getHeight()*(1/5f),world.getHeight()*(1/5f));
		//repairMeter = new AnimMeter(position, size, "RepairMeter", 11, 40);//(new Vector2(getWidth()*.04f, 1.05f*getHeight()));
		repairMeter = new RepairMeter(position, size, 40);//(new Vector2(getWidth()*.04f, 1.05f*getHeight()));
		repairMeter.setAutoDrawTo(false);		
	}
	
	private void initializeRowsLeftMeter()
	{
		Vector2 position = new Vector2(world.getPositionX() + world.getWidth() - (world.getHeight()*(1/10f)), world.getWorldPositionY()+world.getHeight()*(1/10f));
		Vector2  size = new Vector2(world.getHeight()*(1/5f),world.getHeight()*(1/5f));
		//rowsLeftMeter = new AnimMeter(position, size, "RowMeter", 11, initialTileRows);//(new Vector2(getWidth()*.04f, 1.05f*getHeight()));
		rowsLeftMeter = new RowMeter(position, size, 40);//(new Vector2(getWidth()*.04f, 1.05f*getHeight()));
		rowsLeftMeter.setValue(TilesAbove.rowCount());
		//rows remaining
		rowsRemainingText = new TextObject(""+TilesAbove.rowCount(), 
				new Vector2(world.getHeight()/120f,-world.getHeight()/30f).add(position), 18, Color.WHITE, Color.BLACK);
		rowsLeftMeter.setAutoDrawTo(false);
		rowsRemainingText.removeFromAutoDrawSet();
	}
	
	private void initializeCorruptionMeter()
	{
		Vector2 position = new Vector2(world.getPositionX() + world.getWidth()/2f, world.getWorldPositionY() + world.getHeight()*(29/30f));
		Vector2  size = new Vector2 (world.getWidth(), world.getHeight()*(1/15f));
		int totalTiles = TilesAbove.count()+tileHelper.count();
		if(corruptionMeter != null)
		{
			corruptionMeter.destroy();
		}
		corruptionMeter = new CorruptionBarMeter(position, size, totalTiles, .5f);
		corruptionMeter.setAutoDrawTo(false);
		
	}
	
	private void debugUpdateSection()
	{
		debugPosition.setCenter(getMouseFloatCenter());
		debugPosition2.setCenter(getMouseFloatCenter());
		
		//[1]Red [2]Green [3]Blue [4]Cyan [5]Magenta [6]Yellow
		if(keyboard.isButtonTapped(KeyEvent.VK_1)){
			
			setPlayerColorEnum(ColorEnum.RED);
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_2)){
			setPlayerColorEnum(ColorEnum.GREEN);
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_3)){
			setPlayerColorEnum(ColorEnum.BLUE);
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_4)){
			setPlayerColorEnum(ColorEnum.CYAN);
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_5)){
			setPlayerColorEnum(ColorEnum.MAGENTA);
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_6)){
			setPlayerColorEnum(ColorEnum.YELLOW);
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_OPEN_BRACKET)){
			this.corruptionMeter.setLosePercent(corruptionMeter.getLosePercent()-.05f);
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_CLOSE_BRACKET)){
			this.corruptionMeter.setLosePercent(corruptionMeter.getLosePercent()+.05f);
		}
		
		if(keyboard.isButtonTapped(KeyEvent.VK_SPACE)){
			if(pauseLine) {pauseLine = false;}
			else { pauseLine = true;}
			
			//TODO: this is a bit messy
			//this.consoleStrings[6] = pauseLineString + pauseLine;
		}

		if(keyboard.isButtonTapped(KeyEvent.VK_P)){
			this.shiftGridsDownAndHandleExteriors();
			//activate firewall
			firewallClear();
			//update scores

			updateScore();
		}
		
		if(mDebugSpawnState == debugSpawnStates.Idle){
			if(keyboard.isButtonTapped(KeyEvent.VK_Q)){
				mDebugSpawnState = debugSpawnStates.Burst;
			}
			if(keyboard.isButtonTapped(KeyEvent.VK_W)){
				mDebugSpawnState = debugSpawnStates.Fuse;
			}
			if(keyboard.isButtonTapped(KeyEvent.VK_E)){
				mDebugSpawnState = debugSpawnStates.Chain;
			}
			if(keyboard.isButtonTapped(KeyEvent.VK_R)){
				mDebugSpawnState = debugSpawnStates.Repair;
			}
			if(keyboard.isButtonTapped(KeyEvent.VK_T)){
				mDebugSpawnState = debugSpawnStates.Tile;
			}
			if(keyboard.isButtonTapped(KeyEvent.VK_Y)){
				mDebugSpawnState = debugSpawnStates.Corruption;
			}
		}
		
		
		
		if(keyboard.isButtonTapped(KeyEvent.VK_ESCAPE)){
			mDebugSpawnState = debugSpawnStates.Idle;
		}
		
		switch(mDebugSpawnState){
		case Idle:
			//msgDebug.setText(strDebugSpawnText);
			debugPosition.setToInvisible();
			debugPosition2.setToInvisible();
			break;
			
		case Burst:
			//msgDebug.setText("Click to put [Burst] Virus. [ESC] to Cancel");
			debugPosition.setToVisible();
			debugPosition2.setToInvisible();
			
			if(mouse.isButtonTapped(MouseEvent.BUTTON1)){
				
				BurstVirus newBV = new BurstVirus(ColorEnum.RED,this); //Color will be randomized
				
				newBV.spawnAtLocation(new IntVector(debugPosition.getCenter()));
				
				mDebugSpawnState = debugSpawnStates.Idle;
				newBV = null;
			}
			
			break;
		case Fuse:
			//msgDebug.setText("Click to put [Fuse] Virus. [ESC] to Cancel");
			debugPosition.setToVisible();
			debugPosition2.setToVisible();
			debugPosition.setCenterY(0);
			debugPosition2.setCenterY(getHeight()-1);
			
			if(mouse.isButtonTapped(MouseEvent.BUTTON1)){						
				FuseVirus newFV = new FuseVirus(ColorEnum.RED, this,FuseVirusTypes.BOTFUSED); //Color will be randomized
				newFV.spawnAtLocation(new IntVector(debugPosition.getCenter()));
				newFV = null;
				
				mDebugSpawnState = debugSpawnStates.Idle;					
			}
			
			break;
		case Chain:
			//msgDebug.setText("Click to put [Chain] Virus. [ESC] to Cancel");
			debugPosition.setToVisible();
			debugPosition2.setToInvisible();
			
			if(mouse.isButtonTapped(MouseEvent.BUTTON1)){
				
				ChainVirus newCV = new ChainVirus(ColorEnum.RED,this); //Color will be randomized
				
				newCV.spawnAtLocation(new IntVector(debugPosition.getCenter()));
				
				mDebugSpawnState = debugSpawnStates.Idle;
				newCV = null;
			}
			
			break;
		case Repair:
			//msgDebug.setText("Click to put [Repair] Protocol. [ESC] to Cancel");
			debugPosition.setToVisible();
			debugPosition2.setToInvisible();
			
			if(mouse.isButtonTapped(MouseEvent.BUTTON1)){
				
				RepairVirus newRV = new RepairVirus(this);
				
				newRV.spawnAtLocation(new IntVector(debugPosition.getCenter()));
				
				mDebugSpawnState = debugSpawnStates.Idle;
				newRV = null;
			}
			
			break;
		case Tile:
			//msgDebug.setText("Click to put [Tile]. [ESC] to Cancel");
			debugPosition.setToVisible();
			debugPosition2.setToInvisible();
			
			if(mouse.isButtonTapped(MouseEvent.BUTTON1)){
				ColorEnum col = getPlayerColorEnum();
				tileHelper.putTile(new IntVector(debugPosition.getCenter()),col);
				mDebugSpawnState = debugSpawnStates.Idle;
			}
			
			break;
		case Corruption:
			//msgDebug.setText("Click to put [Corruption]. [ESC] to Cancel");
			debugPosition.setToVisible();
			debugPosition2.setToInvisible();
			
			if(mouse.isButtonTapped(MouseEvent.BUTTON1)){
				corruptionHelper.addCorruption(new IntVector(debugPosition.getCenter()));
				mDebugSpawnState = debugSpawnStates.Idle;
			}
			
			break;
		default:
			//msgDebug.setText(strDebugSpawnText);
			debugPosition.setToInvisible();
			debugPosition2.setToInvisible();
			break;
		}
	}
	
	private Vector2 getMouseFloatCenter(){
		
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
		
		if(x > getWidth()-1) x = getWidth()-1; 
		if(y > getHeight()-1) y = getHeight()-1;
		
		Vector2 MC = new Vector2(x,y);
		
		return MC;
	}
	
	@Override
	public void draw(Graphics gfx) 
	{
		super.draw(gfx);

		//meters
		repairMeter.draw();
		rowsLeftMeter.draw();
		corruptionMeter.draw();

		if(debugPosition.isVisible())
		{
			debugPosition.draw();
		}

		if(debugPosition2.isVisible())
		{
			debugPosition2.draw();
		}
		rowsRemainingText.draw();
		
		mGameOverScreen.draw();
		mWinScreen.draw();
		mCreditsScreen.draw();
		if(splashRect.isVisible())
		{
			splashRect.draw();
		}
	}

}
