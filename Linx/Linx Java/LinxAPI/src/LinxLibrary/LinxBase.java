package LinxLibrary;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import Engine.BaseCode;
import Engine.GameObject;
import Engine.Text;
import Engine.Vector2;

/**
 * Linx game base class.
 * Mapped keyboard controls:<p>
 *   
 *   F1: Increase one life (cap at 5) <br>
 *   F2: Decrease one life (end game if less than 0) <br>
 *   F3: Force a new game immediately <br>
 *   F4: Assume game is won <br>
 *   F5: Assume game is lost <br>
 *   F11: Full Screen<br>
 *   Escape: To quit or skip credit showing <br>
 *   D: Toggle debug mode (show all block types)<br>
 *   <br>
 *   When in <b>Debug mode</b>: next ball follows the mouse<br>.
 *   <br>
 *   The follow keys are only defined when in debug mode.<br>
 *   Enter: to add new ball at the end of chain<br>
 *   R: Reverse the direction of the first chain (if only one chain, no effect) <br>
 *   S: Stop the ball chain<br>
 *   F: Toggle movement of ball chain<br>
 *   UP/Down arrow: increase/decrease chain speed<br>
 *   <br><b>Control next ball:</b>
 *   <i>color</i>
 *   Shift-1: Blue<br>
 *   Shift-2: Magenta<br>
 *   Shift-3: Green<br>
 *   Shift-4: Red<br>
 *   Shift-5: Yellow<br>
 *   <br>
 *   <i>Power-Up</i>
 *   1: Bomb <br>
 *   2: Slow <br>
 *   3: Stop <br>
 *   4: Wildcard <br>
 *   5: Speed <br>
 *   6: Invisible <br>
 *   <br>   
 * The base class for the Linx game.
 * Does most of the heavy lifting to get the game running including
 * ball movement, loading images and text, and winning/losing.
 * @author Brian
 */
public abstract class LinxBase extends Engine.LibraryCode {
    
    public enum GameState
    {
        Normal, Paused, Win, Lose, Restart, GameOver
    }

    public enum Power
    {
        None,        
        Bomb, Slow, Stop, Wildcard,
        Speed, Invisiball
    }
    
    public enum BallColor {
    	Blue, Magenta, Green, Red, Yellow, PowerUp
    }

    private boolean resourceLoaded = false;
    // debug stuff
    public boolean debugMode = false;
    private Text debugChains;
    private Text debugBalls;

    private final String BG_MUSIC = "BackgroundMusic.mp3";
    private final String PATH_FILE = "Path.txt";
    private final int LARGE_FONT = 60;
    private final int SMALL_FONT = 26;

    private Path mPath = null;
    private BallsInFlight inFlightBalls = null;
    private Vector<BallChainOnPath> mAllBallChains = null;
    private GameState gameState = null; 

    private GameObject startImage = null;
    private GameObject endImage = null;
    private GameObject stars = null;
    private GameObject gameOverBg = null;
    private GameObject gameOverImage = null;
    private GameObject gameOverRetry = null;
    private GameObject gameOverExit = null;
    private GameObject smiley = null;
    
    private GameObject[] lifeImages = null;
    private GameObject timerImage = null;
    private Text lives;
    private Text level;
    private Text scoreText;
    
    float worldWidth;
    float worldHeight;

    private final int MAX_LIVES = 5;
    private int mLives = MAX_LIVES;

    private int mScore = 0;

    // alarm variables
    private float mFlashTimer;
    private boolean fastPlayed;

    private float restartTimer;
    
    // Start screen variables
    private Vector2 mousePos = new Vector2();
    private boolean playedStart = false;
    private GameObject start = null;
    private GameObject credits = null;
    private GameObject background = null;
    private GameObject startBackground = null;
    private GameObject startText = null;
    private List<GameObject> mBgDecors = null; 
    
    private boolean creditsOn;
    private float worldX;
    private float worldY;

    // -- Public Variables for User Code ---
    protected Turret turret = null;
    protected ChainSet chains = null;
    protected Chain mainChain = null;
    protected BallSet mainChainBalls = null;
    protected BallSet shotBalls = null;

    // Region: Create UI deco elements
    private GameObject allocDecor(float posX, float posY, float w, float h, String imageName, boolean visible) {
    	GameObject obj = new GameObject(posX, posY, w, h);
    	obj.setImage(imageName);
    	if (!visible)
    		obj.setToInvisible();
    	mBgDecors.add(obj);
    	return obj;
    }
    /**
     * Initializes the start screen of the game, with play and credits buttons.
     */
    private void InitStart()
    {        
        creditsOn = false;

        worldX = world.getWidth();
        worldY = world.getHeight();
        float hw = worldX / 2;
        float hh = worldY / 2;
        
        // Game flash screen
        	background = allocDecor(hw, hh, worldX, worldY, "StartScreen_BG.png", false);
        
	        // Create the start/credit image and use static version initially (update will handle mouse hover/click)
	        start   = allocDecor(hw - 9f, hh - 8.25f, 16f, 5f, "StartButton_Static.png", false);        
	        credits = allocDecor(hw + 9f, hh - 8.25f, 16f, 5f, "CreditsButton_Static.png", false);
                
        // Credit stuff
	        // Create the blurred background
	        startBackground = allocDecor(hw, hh, worldX, worldY, "Linx_Credits_BG.png", false);
	        // credits roll up from the bottom of screen
	        startText = allocDecor(hw, -hh, worldY, worldX, "Linx_Credits_Scroll.png", false);
        
	   // Game Over stuff
	        smiley = allocDecor(hw, hh, 34, 22, "YouWin_PopUp_Static.png", false);
        
	        gameOverBg = allocDecor(hw, hh, worldX, worldY, "GameOverBlur_PopUpPlacement.png", false);
        
	        // Create the gameover image and use static version initially (update will handle mouse hover/click)  
	        gameOverRetry = allocDecor(hw - 4.5f, hh - 7.5f, 8f, 2f, "", false);
	        gameOverRetry.setToDrawOutline(true);
        
	        gameOverExit = allocDecor(hw + 4f, hh - 7.5f, 8f, 2f, "", false);
	        gameOverExit.setToDrawOutline(true);
        
	        // draw image last so buttons do not draw on top
	        gameOverImage = allocDecor(hw, hh, 34, 22, "GameOver_PopUp_Static.png", false);
    }
    // EndRegion
    
    // Region: Show/Hide Game Decor/Start/End screens
    
    private void showEndScreen(boolean win)
    {
    	hideGameDecors();
    	
        gameOverBg.setToVisible();
        gameOverBg.moveToFront();
        gameOverRetry.setToVisible();
        gameOverRetry.moveToFront();
        gameOverExit.setToVisible();
        gameOverExit.moveToFront();
        gameOverImage.setToVisible();
        gameOverImage.moveToFront();
     
        if (win) {
        	smiley.setToVisible();
        	smiley.moveToFront();
        }
    }
    
    private void hideEndGameScreen()
    {
    	smiley.setToInvisible();
        gameOverBg.setToInvisible();
        gameOverRetry.setToInvisible();
        gameOverExit.setToInvisible();
        gameOverImage.setToInvisible();
        showGameDecors();
    }

    private void hideGameDecors(){
    	startImage.setToInvisible();
    	endImage.setToInvisible();
    	for (int i = 0; i < mLives; i++)
			lifeImages[i].setToInvisible();
    	
    	// texts
    	level.setText("");
    	lives.setText("");
    	scoreText.setText("");
    	debugChains.setText("");
        debugBalls.setText("");
    }
    
    private void showGameDecors(){    	
    	debugChains.setText("");
        debugBalls.setText("");
        // 
        level.setText("LEVEL: 1");
        scoreText.setText("SCORE: 0000000");
        lives.setText("LIVES:");
        for (int i = 0; i < mLives; i++) {
			lifeImages[i].setImage("LifeCounter_Full.png");
			lifeImages[i].setToVisible();
			lifeImages[i].setAlwaysInFront(true);
		}
        
        // start and end point of the chain
        startImage.setToVisible();
        startImage.moveToFront();
        startImage.setAlwaysInFront(true);
        endImage.setToVisible();
        endImage.moveToFront();
    	endImage.setAlwaysInFront(true);
    }
    
    private void showInitUIScreen(){
    	background.setToVisible();
		start.setToVisible();
		credits.setToVisible();
		
		hideGameDecors();
		background.moveToFront();
		start.moveToFront();
		credits.moveToFront();
    }
    
    private void hideInitUIScreen(){
    	showGameDecors();
    	start.setToInvisible();
        credits.setToInvisible();
        background.setToInvisible();
    }
    // EndRegion
    
    // Region: set score
    /**
     * Increases the current score by the input amount.
     * @param increment this is the amount the score will be incremented by. (Negative is acceptable)
     */
    public void increaseScoreBy(int increment) {
    	mScore += increment;
    }
    /**
     * Sets the current score to the input value.
     * @param newScore this is the new score that will be displayed.
     */
    public void setScoreTo(int newScore) {
    	mScore = newScore;
    }
    // EndRegion: score
    
    // Region: Lifes
    /**
     * Remove one life from the user.
     */
    public void decreaseOneLife() {
    	// subtract a life and remove the life image        
    	mLives -= 1;
    	if (mLives < 0) 
    		mLives = 0;
    	lifeImages[mLives].setToInvisible();
    }
    /**
     * Increase one life for the user.
     */
    public void increaseOneLife() {
    	if (mLives >= MAX_LIVES)
    		return;
    	
    	lifeImages[mLives].setToVisible();
    	++mLives;
    }
    // EndRegion: life
    /**
     * Updates the start screen for button presses and credits. Must be called
     * in updateWorld() so it runs each frame.
     */
    private void UpdateStart()
    {
        // Check for user trying to exit game
    	if (!background.isVisible())
    		showInitUIScreen();
    	
        if (keyboard.isButtonTapped(KeyEvent.VK_ESCAPE))
        {
            if (creditsOn)
                Credits(false); // turn them off
            else {
            	clearToQuit();
            	return;
            }
        }
        // Check for rolling credits
        if (creditsOn)
        {
            if (startText.getCenterY() > worldY + worldX/2)
                Credits(false); // over
            else
                startText.getCenter().offset(0, .1f);
            return;
        }

        // get mouse position as a primitive to check collision
        mousePos.set(mouse.getWorldX(), mouse.getWorldY());

        if (start.containsPoint(mousePos))
        {
            if (mouse.isButtonTapped(MouseEvent.BUTTON1))
            {
                playedStart = true; // restart game, but don't show start screen
                hideInitUIScreen();
                initializeWorld();
            }
            else if (mouse.isButtonDown(MouseEvent.BUTTON1))
                start.setImage("StartButton_Press.png");
            else
                start.setImage("StartButton_Hover.png");
        }
        else if (credits.containsPoint(mousePos))
        {
            if (mouse.isButtonTapped(MouseEvent.BUTTON1))
            {
                Credits(true);
            }
            else if (mouse.isButtonDown(MouseEvent.BUTTON1))
                credits.setImage("CreditsButton_Press.png");
            else
                credits.setImage("CreditsButton_Hover.png");
        }
        else
        {
            start.setImage("StartButton_Static.png");
            credits.setImage("CreditsButton_Static.png");
        }
    }
    /**
     * Sets the credits to start or stop, and will reset image positions.
     * @param show whether or not to show the credits screen
     */
    private void Credits(boolean show)
    {
        if (show)
        {
            startBackground.setToVisible();
            startBackground.moveToFront();
            startText.setToVisible();
            startText.moveToFront();
            creditsOn = true;
        }
        else
        {
            // Hide everything
            startBackground.setToInvisible();
            startText.setToInvisible();
            creditsOn = false;
        }
    }
    
    /**
     * If the game will show initial start screen.
     * @param show if true the game will not show the initial start screen (facilitate debugging).
     */
    public void setShowStartScreen(boolean show) {
    	if (show) {
    		playedStart = false;
    		showInitUIScreen();
    	} else {
    		hideInitUIScreen();
    		playedStart = true;
    	}
    }
    
    
    /**
     * Initializes the game world, places images and sets up student variables.
     * Resets game variables and initializes start screen if it has not played
     * yet.
     */
    @Override
    public void initializeWorld()
    {
    	if(mBgDecors == null) {
    		// first time, must allocate all ...
    	
			super.initializeWorld();
			resources.setClassInJar(new JarResources());
			setUpWorld();
    	
			mBgDecors = new Vector<GameObject>();	
			
			InitStart();

			gameState = GameState.Normal;
			restartTimer = 0; // timer to wait after losing a life

			// ***** Background images *****
			Vector2 worldCenter = new Vector2(BaseCode.world.getWidth() / 2,
					BaseCode.world.getHeight() / 2);
			worldWidth = BaseCode.world.getWidth();
			worldHeight = BaseCode.world.getHeight();

			stars = new GameObject(worldCenter.getX(), worldCenter.getY(),
					worldWidth * .8f, worldHeight * .8f);
			stars.getCenter().offset(stars.getWidth() / 4, 0);
			stars.setImage("Stars_Static.png");
			mBgDecors.add(stars);
			GameObject starsWindow = new GameObject(worldCenter.getX(),
					worldCenter.getY(), worldWidth * .8f, worldHeight * .8f);
			starsWindow.setImage("StarWindow_Static.png");
			mBgDecors.add(starsWindow);
			GameObject gameBackground = new GameObject(worldCenter.getX(),
					worldCenter.getY(), worldWidth, worldHeight);
			gameBackground.setImage("Background_noProps.png");
			mBgDecors.add(gameBackground);

			// Blue orb tanks
			GameObject blueOrb_BL = new GameObject(13, 14.5f, 14, 7);
			// Java animation is (image name, width, height, total frames, ticks
			// per frame)
			blueOrb_BL.setSpriteSheet("BlueTankLeft.png", 272, 128, 96, 1);
			blueOrb_BL.setUsingSpriteSheet(true);
			mBgDecors.add(blueOrb_BL);

			GameObject blueOrb_BR = new GameObject(86, 13.5f, 14, 7);
			blueOrb_BR.setSpriteSheet("BlueTankRight.png", 273, 128, 96, 1);
			blueOrb_BR.setUsingSpriteSheet(true);
			mBgDecors.add(blueOrb_BR);

			GameObject blueOrb_TR = new GameObject(86, 41, 14, 7);
			blueOrb_TR.setSpriteSheet("BlueTankLeft.png", 272, 128, 96, 1);
			blueOrb_TR.setUsingSpriteSheet(true);
			blueOrb_TR.setRotation(180);
			mBgDecors.add(blueOrb_TR);

			GameObject blueOrb_TL = new GameObject(13, 42, 14, 7);
			blueOrb_TL.setSpriteSheet("BlueTankRight.png", 273, 128, 96, 1);
			blueOrb_TL.setUsingSpriteSheet(true);
			blueOrb_TL.setRotation(180);
			mBgDecors.add(blueOrb_TL);

			// Conveyor belts
			GameObject conveyor_BL = new GameObject(21, 14.5f, 7.5f, 11.5f);
			conveyor_BL.setSpriteSheet("ConveyorBeltLeft.png", 148, 224, 8, 2);
			conveyor_BL.setUsingSpriteSheet(true);
			mBgDecors.add(conveyor_BL);

			GameObject conveyor_BR = new GameObject(79, 14.5f, 7.5f, 11.5f);
			conveyor_BR.setSpriteSheet("ConveyorBeltRight.png", 148, 224, 8, 2);
			conveyor_BR.setUsingSpriteSheet(true);
			mBgDecors.add(conveyor_BR);

			GameObject conveyor_TR = new GameObject(79, 41.5f, 7.5f, 11.5f);
			conveyor_TR.setSpriteSheet("ConveyorBeltLeft.png", 148, 224, 8, 2);
			conveyor_TR.setUsingSpriteSheet(true);
			conveyor_TR.setRotation(180);
			mBgDecors.add(conveyor_TR);
			GameObject conveyor_TL = new GameObject(21, 41.5f, 7.5f, 11.5f);
			conveyor_TL.setSpriteSheet("ConveyorBeltRight.png", 148, 224, 8, 2);
			conveyor_TL.setUsingSpriteSheet(true);
			conveyor_TL.setRotation(180);
			mBgDecors.add(conveyor_TL);

			// Make the start and end images
			startImage = new GameObject(8.5f, 42, 15.5f, 7);
			startImage.setSpriteSheet("EntranceTube.png", 504, 228, 13, 2);
			startImage.setUsingSpriteSheet(true);
			mBgDecors.add(startImage);

			endImage = new GameObject(93.8f, 16, 7.5f, 11);
			endImage.setSpriteSheet("ExitTube.png", 244, 355, 13, 2);
			endImage.setUsingSpriteSheet(true);
			endImage.setRotation(-90);
			mBgDecors.add(endImage);

			// Turret baseplate
			GameObject turretBase = new GameObject(54, 12, 14, 22);
			turretBase.setImage("Turret_Base.png");
			mBgDecors.add(turretBase);

			// UI border
			GameObject UI = new GameObject(worldCenter.getX(),
					worldCenter.getY(), worldWidth, worldHeight);
			UI.setImage("LinxBorderTop.png");
			mBgDecors.add(UI);

			float UIcenterY = worldHeight - 4.4f; // center for UI elements

			// setup font
			resources.preloadFont("Mensura Regular.ttf");

			// display level
			level = new Text("", worldCenter.getX() * .1f,
					UIcenterY - 1);
			level.setFrontColor(Color.white);
			level.setFontName("Mensura Regular");
			level.setFontSize(SMALL_FONT);

			// display score
			scoreText = new Text("", worldCenter.getX() * .7f,
					UIcenterY - 1);
			scoreText.setFontName("Mensura Regular");
			scoreText.setFrontColor(Color.white);
			scoreText.setFontSize(SMALL_FONT);

			// display lives
			lives = new Text("", worldCenter.getX() * 1.5f, UIcenterY - 1);
			lives.setFontName("Mensura Regular");
			lives.setFrontColor(Color.white);
			lives.setFontSize(SMALL_FONT);

			// life images
			lifeImages = new GameObject[mLives];
			float lifeWidth = 1.25f;
			float lifeHeight = 3.1f;
			float lifeX = lives.center.getX() + lifeWidth * 8.1f;
			float lifeY = UIcenterY + .2f;
			for (int i = 1; i <= mLives; i++) {
				lifeImages[i - 1] = new GameObject(lifeX + (lifeWidth * 1.5f)
						* i, lifeY, lifeWidth, lifeHeight);
				lifeImages[i - 1].setImage("LifeCounter_Full.png");
				lifeImages[i - 1].setAlwaysInFront(true);
			}

			// debug text
			debugChains = new Text("", 5, 14);
			debugBalls = new Text("", 5, 18);
			
			
			// ********** Initialize objects ***********
	        mPath = new Path();
	        try {
	        	mPath.loadPath(PATH_FILE);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        // setup variables for student's to use
	        mAllBallChains = new Vector<BallChainOnPath>();

	        inFlightBalls = new BallsInFlight();
	        
	        // load the turret using the path file for coordinates
	        try {
	                turret = new Turret(PATH_FILE, inFlightBalls, mouse, mAllBallChains);
	        } catch (IOException e) {
	                e.printStackTrace();
	        }
	        
    	}

        // Play background music
    	if (resources.isSoundPlaying(BG_MUSIC))
    		resources.stopSound(BG_MUSIC);
    	resources.playSoundLooping(BG_MUSIC);
    	
    	
    	if (mainChainBalls != null)
        	mainChainBalls.clear();

    	if (shotBalls != null)
    		shotBalls.clear();
    	       
		gameState = GameState.Normal;
        restartTimer = 0; // timer to wait after losing a life
        
        // initialize world state
        showGameDecors();
        
        for (int i=mAllBallChains.size()-1; i>=0; i--) {
        	BallChainOnPath p = mAllBallChains.remove(i);
            p.destroyBallChain();
        }
        mAllBallChains.clear();
        mAllBallChains.add(new BallChainOnPath(mPath));
        
        inFlightBalls.clear();
        
        // ******* Protected User Object Initialization *******            
        mainChain = new Chain(mAllBallChains.get(0));
        chains = new ChainSet(mAllBallChains, inFlightBalls);        
        mainChainBalls = new BallSet(mAllBallChains.get(0));
        shotBalls = new BallSet(inFlightBalls);
        
        // student's initialize
        initialize();
    }
           

    /**
     * 
     * Updates the game world every frame and calls the student update().
     * Handles key presses, score, ball warnings, and cleans up when necessary.
     */
    @Override
    public void updateWorld()
    {   
        super.updateWorld();
        
        // check for fullscreen switch
        if (keyboard.isButtonTapped(KeyEvent.VK_F11)) {
            window.toggleFullscreen();
            // shrink or grow text size to match screen
            if (window.isFullscreen()) {
                lives.setFontSize(LARGE_FONT);
                scoreText.setFontSize(LARGE_FONT);
                level.setFontSize(LARGE_FONT);
            }
            else {
                lives.setFontSize(SMALL_FONT);
                scoreText.setFontSize(SMALL_FONT);
                level.setFontSize(SMALL_FONT);
            }
        }

        // check for start screen
        if (playedStart == false) {
            UpdateStart();
            return;
        }
            
        // check for user tyring to exit game
        if (keyboard.isButtonTapped(KeyEvent.VK_ESCAPE)) {
        	clearToQuit();
        	return;
        } else if (keyboard.isButtonTapped(KeyEvent.VK_F1)) {
        	increaseOneLife();
        } else if (keyboard.isButtonTapped(KeyEvent.VK_F2)) {
        	decreaseOneLife();
        } else if (keyboard.isButtonTapped(KeyEvent.VK_F3)) {
        	playedStart = true; // restart game, but don't show start screen
        	mLives = MAX_LIVES;
        	mScore = 0;
        	restartWorld();
        } else if (keyboard.isButtonTapped(KeyEvent.VK_F4)) {
        	win();
        } else if (keyboard.isButtonTapped(KeyEvent.VK_F5)) {
        	gameOver();
        } 

        // check for gameover screen, handle switching gameover image on mouse hover and clicks
        if (gameState == GameState.GameOver || gameState == GameState.Win)
        {   
            // get mouse position as a primitive to check collision
            mousePos.set(mouse.getWorldX(), mouse.getWorldY());
            if (gameOverExit.containsPoint(mousePos))
            {
                if (mouse.isButtonTapped(MouseEvent.BUTTON1)) {
                	clearToQuit();
                	return;
                } else if (mouse.isButtonDown(MouseEvent.BUTTON1))//.isLeftButtonDown())
                    gameOverImage.setImage("GameOver_PopUp_ExitPress.png");
                else
                    gameOverImage.setImage("GameOver_PopUp_ExitHover.png");
            }
            else if (gameOverRetry.containsPoint(mousePos))
            {
                if (mouse.isButtonTapped(MouseEvent.BUTTON1))//isLeftReleased())
                {
                    // reset lives and then reset game
                    mLives = MAX_LIVES;
                    mScore = 0;
                    restartWorld();
                }
                else if (mouse.isButtonDown(MouseEvent.BUTTON1))
                    gameOverImage.setImage("GameOver_PopUp_RetryPress.png");
                else
                    gameOverImage.setImage("GameOver_PopUp_RetryHover.png");
            }
            else
                gameOverImage.setImage("GameOver_PopUp_Static.png");

            return;
        }

        // if the player lost, destroy all the balls before reset/gameover
        if (gameState == GameState.Restart || gameState == GameState.Lose)
        {
            cascadeDestroyBalls();
            for (BallChainOnPath ballChain : mAllBallChains) {
                for (Ball ball : ballChain) {
                    ball.update();
                }    
            }
            return;
        }

        // move the stars in the background
        if (stars.getCenterX() + stars.getWidth()/4 > worldWidth/2)
            stars.getCenter().offset(-.05f, 0);
        else
            stars.setCenterX(worldWidth/2 + stars.getWidth()/4);

        // move balls shot by turret
        inFlightBalls.update();

        // check for debug update
        if (keyboard.isButtonTapped(KeyEvent.VK_D))
            debugMode = !debugMode;            
        debug();

        for (BallChainOnPath mBallChain : mAllBallChains) {
            mBallChain.clearMarks();
        }

        // delete any empty chains left over from matching the last 3 balls etc.
        for (int i = mAllBallChains.size()-1; i>=0; i--)
        {
            if (mAllBallChains.get(i).size() == 0)
            {
                // remove it; count is lower
                BallChainOnPath p = mAllBallChains.remove(i);
                p.destroyBallChain();
            }
        }
        
        // update all the balls that are being destroyed
        BallsInFlight.cleanRemovedBalls();
        

        // double check that the furthest back chain is not moving backwards
        // from a lucky magnetize+match causing chain to never stop moving backward
        if (mAllBallChains.size() > 0)
            if (mAllBallChains.get(mAllBallChains.size() - 1).getDirection() == Path.PathTravelDirection.ePathTravelReverse)
                mAllBallChains.get(mAllBallChains.size() - 1).setChainDirection(Path.PathTravelDirection.ePathTravelForward);

        // **** Run the students update function
        update();
        // ****

        // update the score text
        scoreText.setText(String.format("SCORE: %07d", mScore));

        // check for warning if ball is close to end
        // Entrance/Exit tube's can't flash in Java - no image tint or color
        if (mAllBallChains.size() > 0)
        {
            if (mAllBallChains.get(mAllBallChains.size() - 1).size() > 0)
            {
                float t = mAllBallChains.get(mAllBallChains.size() - 1).getFirst().getT();
                // if farthest ball is at least 70%
                if (t > .7f)
                {
                    mFlashTimer += 1;
                    if (t > .9f)
                    {
                        if (mFlashTimer > 10)
                        {
                            if (!fastPlayed)
                            {
                                // Only play fast alarm once
                                resources.playSound("AlarmFast.wav");
                                fastPlayed = true;
                            }
                            mFlashTimer = 0;
                            if (endImage.getColor() == Color.red)
                                endImage.setColor(Color.white);
                            else
                                endImage.setColor(Color.red);
                        }
                    }
                    else if (t > .8f)
                    {
                        if (mFlashTimer > 25)
                        {
                            fastPlayed = false;
                            mFlashTimer = 0;
                            if (endImage.getColor() == Color.red)
                                endImage.setColor(Color.white);
                            else
                            {
                                endImage.setColor(Color.red);
                                // Play alarm sound only when color switches to red
                                resources.playSound("AlarmMedium.wav");
                            }
                        }
                    }
                    // flash while > .75f
                    else
                    {
                        if (mFlashTimer > 50)
                        {
                            fastPlayed = false;
                            mFlashTimer = 0;
                            if (endImage.getColor() == Color.red)
                                endImage.setColor(Color.white);
                            else
                            {
                                endImage.setColor(Color.red);
                                // Play alarm sound only when color switches to red
                                resources.playSound("AlarmSlow.wav");
                            }
                        }
                    }
                }
                // check for losing a life
                if ((t >= 1) || (mLives <= 0)) 
                {
                    // if there are more lives
                    if (mLives > 1)
                    {
                    	decreaseOneLife();
                        // restart game
                        gameState = GameState.Restart;
                    }
                    else
                    {
                        gameState = GameState.Lose;
                    }
                    // Play a lose sound
                    resources.playSound("DeathExplosion2.wav");
                }
            }
        }
        else // no chains left, check for win!
        {
            win();
        }            
    }

    /**
     * The student's game world update function that is called each frame.
     */
    protected abstract void update();
    
    protected abstract void initialize();

    /**
     * Destroys each remaining ball in a cascading fashion; called after a loss.
     */
    private void cascadeDestroyBalls()
    {
        // Destroy one ball at a time until they are all gone
        if (mAllBallChains.size() > 0)
        {
            if (mAllBallChains.get(0).size() > 0)
            {
                for (int i = 0; i<mAllBallChains.get(0).size(); i++)
                {
                    // Remove the ball and wait till next frame
                    if (!mAllBallChains.get(0).get(i).isRemoved())
                    {
                        mAllBallChains.get(0).get(i).remove();
                        break;
                    }
                    // Or, if finished exploding, remove from list
                    else if (!mAllBallChains.get(0).get(i).isAlive())
                    {
                        Ball b = mAllBallChains.get(0).remove(i);
                        b.destroy();
                        i--;
                    }
                }
            }
            else {
                BallChainOnPath p = mAllBallChains.remove(0);
                p.destroyBallChain();
            }
        }
        // Complete the game over or restart
        else if (restartTimer < 1.5f) // pause for effect!
            restartTimer += .025f;
        else if (gameState == GameState.Lose)
            gameOver();            
        else
            restartWorld();
    }

    /**
     * Restarts and initializes the game world for use after a lost life.
     */
    protected void restartWorld()
    {
        // BaseCode.resources.clean();
        hideEndGameScreen();
        hideInitUIScreen();
        showGameDecors();
        initializeWorld();
    }
    
    /**
     * Sets the game state to GameOver and draws the game over screen.
     */
    protected void gameOver()
    {
        // Set the gamestate to gameover so update will run gameover UI code
        gameState = GameState.GameOver;        
        // Draw a game over screen
        showEndScreen(false);
    }

    /**
     * Sets the game state to win and draws the win screen.
     */
    protected void win()
    {
        gameState = GameState.Win;
        // Play a win sound
        resources.playSound("YouWinFull.mp3");
        showEndScreen(true);
    }

    /**
     * Sets the world coordinates for the game world.
     */
    private void setUpWorld()
    {
        // initialize world
        world.SetWorldCoordinate(100);
    }

    /**
     * Calculates the remaining number of balls across all chains.
     * @return the total number of balls remaining
     */
    private int getNumbBallsInGame()
    {
        int totalBalls = 0;
        for (int i = 0; i < mAllBallChains.size(); i++)
        {
            totalBalls += mAllBallChains.get(i).size();
        }

        return totalBalls;
    }

    /**
     * Returns the number of chains remaining in the game.
     * @return the total number of chains
     */
    private int getNumbBallChainsInGame()
    {
        return mAllBallChains.size();
    }

    private void clearToQuit() {
    	if(mBgDecors != null) {
    		for (int i = mBgDecors.size()-1; i>0; i--) {
    			GameObject o = mBgDecors.remove(i);
    			o.destroy();
    		}
    		mBgDecors.clear();
    		mBgDecors = null;
    		
    		BaseCode.resources.clean();
    		
    		for (int i = 0; i < mLives; i++) {
				lifeImages[i].destroy();
				lifeImages[i] = null;
			}
    		lifeImages = null;
    		
    		for (int i = mAllBallChains.size()-1; i>=0; i--) {
    			BallChainOnPath p = mAllBallChains.remove(i);
    			p.clear();
    		}
    		
    		mPath.clearPath();
    		mPath = null;
    		
	        mAllBallChains.clear();
	        inFlightBalls.clear();
	        turret.destroy();
	        
	        mAllBallChains = null;
	        inFlightBalls = null;
	        turret = null;
    	}

    	if (mainChainBalls != null)
        	mainChainBalls.clear();

    	if (shotBalls != null)
    		shotBalls.clear();
    	
        BallsInFlight.cleanRemovedBalls();
        mainChainBalls = null;
        shotBalls = null;
        window.close();
    }
    
    /**
     * Updates debugging text/variables based on the debugMode boolean.
     * Displays the number of chains and balls remaining, and allows the
     * use of special debugging keys to spawn powerups or specific colors
     * and allow the turret's ball to move to the mouse position.
     */
    private void debug()
    {
        if (debugMode)
        {
            debugChains.visible = true;
            debugBalls.visible = true;
            debugChains.setText("Chains: " + getNumbBallChainsInGame());
            debugBalls.setText("Balls: " + getNumbBallsInGame());

            turret.debugMode(true);

            // check for special keyboard presses            
            if ((keyboard.isButtonTapped(KeyEvent.VK_ENTER)))
                mAllBallChains.get(0).AddNewBallToEnd();

            if (keyboard.isButtonTapped(KeyEvent.VK_R))
            {
                for (BallChainOnPath c : mAllBallChains)
                    c.reverseChainDirection();
            }

            if (keyboard.isButtonTapped(KeyEvent.VK_S))
            {
                for (BallChainOnPath c : mAllBallChains)
                    c.setChainToMovement(false);
            }

            if (keyboard.isButtonTapped(KeyEvent.VK_F))
            {
                for (BallChainOnPath c : mAllBallChains)
                    c.toggleChainMovement();
            }

            if (keyboard.isButtonTapped(KeyEvent.VK_UP))
                chains.setSpeed(chains.getSpeed() + 1);

            if (keyboard.isButtonTapped(KeyEvent.VK_DOWN))
                chains.setSpeed(chains.getSpeed() - 1);

            // Adding Powerups
            if ((keyboard.isButtonTapped(KeyEvent.VK_1)))
            {
                //turret.setCurrentBallPower(Power.Bomb);                    
                if ((keyboard.isButtonDown(KeyEvent.VK_SHIFT)))
                    turret.setCurrentBallColor(LinxBase.BallColor.Blue);
                else
                    chains.spawnPowerBall(Power.Bomb);
            }

            if ((keyboard.isButtonTapped(KeyEvent.VK_2)))
            {
                //turret.setCurrentBallPower(Power.Slow);
                if ((keyboard.isButtonDown(KeyEvent.VK_SHIFT)))
                    turret.setCurrentBallColor(LinxBase.BallColor.Magenta);
                else
                    chains.spawnPowerBall(Power.Slow);
            }

            if ((keyboard.isButtonTapped(KeyEvent.VK_3)))
            {
                //turret.setCurrentBallPower(Power.Stop);
                if ((keyboard.isButtonDown(KeyEvent.VK_SHIFT)))
                    turret.setCurrentBallColor(LinxBase.BallColor.Green);
                else
                    chains.spawnPowerBall(Power.Stop);
            }

            if ((keyboard.isButtonTapped(KeyEvent.VK_4)))
            {
                //turret.setCurrentBallPower(Power.Wildcard);
                if ((keyboard.isButtonDown(KeyEvent.VK_SHIFT)))
                    turret.setCurrentBallColor(LinxBase.BallColor.Red);
                else
                    chains.spawnPowerBall(Power.Wildcard);
            }

            if ((keyboard.isButtonTapped(KeyEvent.VK_5)))
            {
                //turret.setCurrentBallPower(Power.Speed);
                if ((keyboard.isButtonDown(KeyEvent.VK_SHIFT)))
                    turret.setCurrentBallColor(LinxBase.BallColor.Yellow);
                else
                    chains.spawnPowerBall(Power.Speed);
            }

            if ((keyboard.isButtonTapped(KeyEvent.VK_6)))
            {
                //turret.setCurrentBallPower(Power.Invisiball);
                chains.spawnPowerBall(Power.Invisiball);                    
            }
            
        }
        else
        {
            debugChains.visible = false;
            debugBalls.visible = false;
            turret.debugMode(false);
        }
    }
    
    /**
     * preloads all resources. This may take a while (2-3 seconds), but it will ensure
     * smooth game play. 
     * <br>
	 * **NOTICE**:preload during debugging will slow down the run/test cycle.
     */
    public void   preloadResources()
    {
    	if (resourceLoaded)
    		return;
    	
    	resourceLoaded = true;
    	resources.preloadSound("AlarmFast.wav");
    	resources.preloadSound("AlarmMedium.wav");
    	resources.preloadSound("AlarmSlow.wav");
    	resources.preloadSound("BackgroundMusic.mp3");
    	resources.preloadSound("BallDisappear.wav");
    	resources.preloadSound("BallHit.wav");
    	resources.preloadSound("BallShoot.wav");
    	resources.preloadSound("WildCard.wav");
    	resources.preloadSound("YouWinFull.mp3");
    	resources.preloadSound("Bomb.wav");
    	resources.preloadSound("Freeze.wav");
    	resources.preloadSound("Invisiball.wav");
    	resources.preloadSound("SlowDown.wav");
    	resources.preloadSound("SpeedUp.wav");
    	resources.preloadSound("DeathExplosion2.wav");
    	
    	resources.preloadImage("Background_noProps.png");
    	resources.preloadImage("BlueTankLeft.png");
    	resources.preloadImage("BlueTankRight.png");
    	resources.preloadImage("ConveyorBeltLeft.png");
    	resources.preloadImage("ConveyorBeltRight.png");
    	resources.preloadImage("CreditsButton_Hover.png");
    	resources.preloadImage("CreditsButton_Press.png");
    	resources.preloadImage("CreditsButton_Static.png");
    	resources.preloadImage("EntranceTube.png");
    	resources.preloadImage("ExitTube.png");
    	resources.preloadImage("Explosion.png");
    	resources.preloadImage("GameOverBlur_PopUpPlacement.png");
    	resources.preloadImage("GameOver_PopUp_ExitHover.png");
    	resources.preloadImage("GameOver_PopUp_ExitPress.png");
    	resources.preloadImage("GameOver_PopUp_RetryHover.png");
    	resources.preloadImage("GameOver_PopUp_RetryPress.png");
    	resources.preloadImage("GameOver_PopUp_Static.png");
    	resources.preloadImage("LifeCounter_Full.png");
    	resources.preloadImage("LinxBorderTop.png");
    	resources.preloadImage("Linx_Credits_BG.png");
    	resources.preloadImage("Linx_Credits_Scroll.png");
    	resources.preloadImage("Orb_Blue.png");
    	resources.preloadImage("Orb_Green.png");
    	resources.preloadImage("Orb_Neutral.png");
    	resources.preloadImage("Orb_Purple.png");
    	resources.preloadImage("Orb_Red.png");
    	resources.preloadImage("Orb_Special_Bomb.png");
    	resources.preloadImage("Orb_Special_Freeze.png");
    	resources.preloadImage("Orb_Special_Invisiball.png");
    	resources.preloadImage("Orb_Special_Slow.png");
    	resources.preloadImage("Orb_Special_Speed.png");
    	resources.preloadImage("Orb_Special_Wildcard.png");
    	resources.preloadImage("Orb_Yellow.png");
    	resources.preloadImage("Rail_Mid.png");
    	resources.preloadImage("StarWindow_Static.png");
    	resources.preloadImage("Stars_Static.png");
    	resources.preloadImage("StartButton_Hover.png");
    	resources.preloadImage("StartButton_Press.png");
    	resources.preloadImage("StartButton_Static.png");
    	resources.preloadImage("StartScreen_BG.png");
    	resources.preloadImage("Turret_Base.png");
    	resources.preloadImage("Turret_BodyPlacement.png");
    	resources.preloadImage("Turret_ColorIndicator_Blue.png");
    	resources.preloadImage("Turret_ColorIndicator_Green.png");
    	resources.preloadImage("Turret_ColorIndicator_Neutral.png");
    	resources.preloadImage("Turret_ColorIndicator_Purple.png");
    	resources.preloadImage("Turret_ColorIndicator_Red.png");
    	resources.preloadImage("Turret_ColorIndicator_Yellow.png");
    	resources.preloadImage("YouWin_PopUp_Static.png");
    }
    
}