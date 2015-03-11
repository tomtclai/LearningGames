
package SpaceSmasher;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import Engine.BaseCode;
import Engine.LibraryCode;
import Engine.GameObject;
import Engine.Text;
import Engine.Vector2;
import SpaceSmasher.Ball.BallType;

/**
 * SpaceSmasher game base class.
 * Mapped keyboard controls:<p>
 *   
 *   F1: Toggle debug mode (show all block types)<br>
 *   F2: WSAD to move the Ball<br>
 *   F3: Force a new game immediately<br>
 *   F4: Assume game is won<br>
 *   F5: Close game window (force quit)<br>
 *   F6: Toggle skipping update frames<br>
 *   F11: Full Screen<br>
 *   F12: Toggle show debug information(to Console)<br>
 *   <br>
 *   V: Toggle audio loud/soft<br>
 *   M: Toggle sounds<br>
 *   <br>
 *   N: Sets the ball to Normal Ball<br>
 *   F: Sets the ball to Fire Ball<br>
 *   I: Sets the ball to Ice Ball<br>
 *   U: Toggle un-breakable blocks<br>
 *   <br>
 *   Q: Prints the coordinate of Block-0 to console<br>
 *   
 *   
 */
public abstract class SpaceSmasher extends LibraryCode
{
	
  private final String DEFAULT_BACKGROUND_MUSIC = "sounds/DigitalVortexTurnedDown";
  private final String WIN_SOUND = "sounds/YouWin!.wav";  

  // Gameover textures
  private final String GAMEOVER_EXIT_HOVER_IMAGE = "gameover/GameOver_PopUp_ExitHover.png";
  private final String GAMEOVER_REPLAY_HOVER_IMAGE = "gameover/GameOver_PopUp_ReplayHover.png";
  private final String GAMEOVER_STATIC_IMAGE = "gameover/GameOver_PopUp_Static.png";
  private final String GAMEOVER_BLUR_IMAGE = "gameover/GameOverBlur.png";
  
  private final String GAMEWIN_EXIT_HOVER_IMAGE = "gameover/YouWin_PopUp_Exit_Hover.png";
  private final String GAMEWIN_REPLAY_HOVER_IMAGE = "gameover/YouWin_PopUp_Replay_Hover.png";
  private final String GAMEWIN_STATIC_IMAGE = "gameover/YouWin_PopUp_Static.png"; 
  
  /** Offers access to the Paddle in the game*/
  protected PaddleSet paddleSet = null;
  /** Offers access to the Balls in the game*/
  protected BallSet ballSet = null;
  protected BlockSet blockSet = null;
  /** Offers access to the Bumpers on wither side of the screen*/
  protected TrapSet trapSet = null;
  /** Offers access to the bumper at the top of the screen*/
  protected Switch theSwitch = null;
  /** Offers access to the players Health meter*/
  protected LifeSet lifeSet = null;
  
  private boolean needCleanUp = false;
  	// this is for indicating cleanup is necessary after win/lost
  
  private static boolean showFlashScreen = true;
  /**
   * Switching the initial flash screen on/off. Switching off the
   * the initial flash screen is a good way to facilitate debugging 
   * rapid debugging process. 
   * 
   * Default shows the flash screen (you have to hit the start game button).
   *  
   * @param on	True (show flash screen) or False (start game immediately).
   */
  static public void SetShowFlashScreen(boolean on) {
	  showFlashScreen = on;
  }
  
  
  private DecorationSet decorations = null;

  private StartScreen startscreen = null; // Not abstract, make a general ui class when art is consistant.
  
  private String backgroundMusic = "";

  //private boolean breakBlocks = true;
  
  GameObject afterGameForeground1;
  GameObject afterGameForeground2;
  
  

  private boolean debugMode = false;
  private boolean debugMouseControl = false;
  private boolean allowDebug = true;

  private Vector2 debugVelocitySaved = new Vector2();
  
  private Text scoreText;
  private Text levelText;
  private Text lifeText;
  private int score;
  private int level;
  private boolean resourcesLoaded = false;

  public enum GameState
  {
    NORMAL, START, PAUSED, WIN, GAMEOVER
  };

  
  private GameState state = GameState.START;

  
  /**
   * This is called when it is time to create and set starting values for data.
   */
  public void initializeWorld()
  {
    super.initializeWorld();
    resources.setClassInJar(new JarResources());
    
    /* 
     * Takes too long: defer to let used decide if they want to pre-load
     * Look at preloadResources() function.
     * 
    Ball.preloadResources(resources);
    Block.preloadResources(resources);
    CageBlock.preloadResources(resources);
    FireBlock.preloadResources(resources);
    FreezingBlock.preloadResources(resources);
    JokerBlock.preloadResources(resources);
    LifeSet.preloadResources(resources);
    Paddle.preloadResources(resources);
    Switch.preloadResources(resources);
    Trap.preloadResources(resources);
    DecorationSet.preloadResources(resources);
    */
    
    resources.preloadSound(DEFAULT_BACKGROUND_MUSIC + ".mp3");
    backgroundMusic = DEFAULT_BACKGROUND_MUSIC;
    
    
 //   resources.preloadFont("fonts/Graviton_MensuraRegular.ttf");
    
    //Decorations
    decorations = new DecorationSet();
    decorations.init();
    
    //StartScreen
    startscreen = new StartScreen(mouse, keyboard, world);
    
    // Gameover components
    afterGameForeground1 = new GameObject(world.getWidth() / 2, world.getHeight() / 2, 40f, 35f);
    afterGameForeground1.setImage(GAMEOVER_STATIC_IMAGE);
    afterGameForeground1.setToInvisible();
    
    afterGameForeground2 = new GameObject(world.getWidth() / 2, world.getHeight() / 2, world.getWidth(), world.getHeight());
    afterGameForeground2.setImage(GAMEOVER_BLUR_IMAGE);
    afterGameForeground2.setToInvisible();
    
    //Topbar variables
    float topbarHeight = world.getHeight() - 4.8f;
    
    //Score
    score = 0;    
    scoreText = new Text();
    scoreText.center = new Vector2((world.getWidth()/2) - 11f,  topbarHeight);
    scoreText.setText("SCORE: " + score);
    scoreText.setFontName("Mensura Regular");
    scoreText.setFrontColor(new Color(220,220,220));
    scoreText.setBackColor(Color.black);
    scoreText.setFontSize((int) (window.getHeight() / 15));
    
    // Level
    level = 0;
    levelText = new Text();
    levelText.center = new Vector2(world.getWidth()/12 - 3f,  topbarHeight);
    levelText.setText("LEVEL: " + level);
    levelText.setFontName("Mensura Regular");
    levelText.setFrontColor(new Color(220,220,220));
    levelText.setBackColor(Color.black);
    levelText.setFontSize((int) (window.getHeight() / 15));

    // LifeBar
    lifeSet = new LifeSet();
//lifeSet.setSize(BaseCode.world.getWidth()/9, (window.getHeight() / 120));
// lifeSet.setCenter(BaseCode.world.getWidth() * (15f/16f)  - 4f, topbarHeight + 1.5f);
    lifeSet.size.set(BaseCode.world.getWidth()/9, (window.getHeight() / 120));
    lifeSet.center.set(BaseCode.world.getWidth() * (15f/16f)  - 4f, topbarHeight + 1.5f);
    
    // Life Text
    lifeText = new Text();
    lifeText.center = new Vector2(world.getWidth() - 30f,  topbarHeight);
    lifeText.setText("LIVES: ");
    lifeText.setFontName("Mensura Regular");
    lifeText.setFrontColor(new Color(220,220,220));
    lifeText.setBackColor(Color.black);
    lifeText.setFontSize((int) (window.getHeight() / 15));
    
    theSwitch = new Switch();
    theSwitch.setCenterY(topbarHeight - 5f);
    blockSet = new BlockSet();
    trapSet = new TrapSet();
    paddleSet = new PaddleSet();
    
    ballSet = new BallSet();
    ballSet.upperBound = topbarHeight - 3f;
    ballSet.setBlockSet(blockSet);
    
    debugMode = false;
    paddleSet.maxX = world.getWidth();

    goToStartScreen();
  }
    
  /**
   * Forces the loading of all resources. This would allow smooth game play but
   * may take a while to return.
   */
  public void preloadResources()
  {
	  if (!resourcesLoaded) {
		  resourcesLoaded = true;
		  Ball.preloadResources(resources);
		  Block.preloadResources(resources);
		  CageBlock.preloadResources(resources);
		  FireBlock.preloadResources(resources);
		  FreezingBlock.preloadResources(resources);
		  JokerBlock.preloadResources(resources);
		  LifeSet.preloadResources(resources);
		  Paddle.preloadResources(resources);
		  Switch.preloadResources(resources);
		  Trap.preloadResources(resources);
		  DecorationSet.preloadResources(resources);
	  }
  }
  
  /**
   * Increases the score text.
   * @param increment amount to be incremented.
   */
  public void increaseScore(int increment)
  {
	  score += increment;
	  
	  scoreText.setText("SCORE: " + score);
  }
  /**
   * Decreases the score text.
   * @param decrement amount to be decremented.
   */
  public void decreaseScore(int decrement)
  {
	  score -= decrement;
	  
	  scoreText.setText("SCORE: " + score);
  }
  /**
   * Set the level text to input.
   * @param level level to be set to.
   */
  public void setLevel(int level)
  {
	  this.level = level;
	  levelText.setText("SCORE: " + level);
	  
  }

  /**
   * Ends the currently running game.
   */
  private void endGame()
  {
    lifeSet.setFilledSegments(0);
    theSwitch.deactivate();
    theSwitch.setToInvisible();
    trapSet.clear();
    paddleSet.clear();
    blockSet.clear();
    ballSet.clear();
    scoreText.visible = false;
    levelText.visible = false;
    lifeText.visible = false;
//lifeSet.setVisibilityTo(false); 
 lifeSet.visible = false;
    
    score = 0;
    level = 0;
    resources.stopSound(backgroundMusic + ".mp3");
         
    window.resetCursor();
    startscreen.setShowStartScreen(showFlashScreen);
  }

  /**
   * Set the game state to lost.
   */
  public void gameLost()
  {
    state = GameState.GAMEOVER;
    needCleanUp = true;
  }
  /**
   * Enables the overlay for the gameover
   */
  private void enableGameoverBackground()
  {
	  afterGameForeground2.setToVisible();
	  afterGameForeground2.moveToFront();
	  
	  afterGameForeground1.setToVisible();
	  afterGameForeground1.moveToFront();
  }
  
  private void disableGameoverBackground()
  {
	  afterGameForeground1.setToInvisible();
	  afterGameForeground2.setToInvisible();
  }
  
  /**
   * Set the game state to win.
   */
  public void gameWin()
  {
    state = GameState.WIN;
	resources.playSound(WIN_SOUND);
    needCleanUp = true;
  }

  /**
   * Starts a new game. This calls endGame() internally before executing.
   */
  public void newGame()
  {
    endGame();
    needCleanUp = false;
    theSwitch.setToVisible();

    // Turn off sound while testing
    //resources.setMuteSound(true);

    ballSet.upperBound = 53f;
    ballSet.leftBound = 2f;
    ballSet.rightBound = world.getWidth() - 2f;
// lifeSet.setVisibilityTo(true);
lifeSet.visible = true;
    
    
    resources.stopSound(backgroundMusic + ".mp3");
    resources.playSoundLooping(backgroundMusic + ".mp3");

    state = GameState.NORMAL;
    
    scoreText.visible = true;
    levelText.visible = true;
    lifeText.visible = true;
    scoreText.setText("SCORE: " + score);
    levelText.setText("LEVEL: " + level);

    // Initialize the child
    initialize();
  }
  
  public void goToStartScreen()
  {
	  endGame();
	  state = GameState.START;
	  
  }

  /**
   * Set if debug mode is allowed to be enabled and disables it if false is give
   * and debug mode is currently enabled.
   * 
   * @param value
   *          - True if debug mode should be allowed, false otherwise.
   */
  public void setAllowDebug(boolean value)
  {
    if(!value)
    {
      setDebugMode(false);
    }

    allowDebug = value;
  }

  /**
   * Set debug mode features to be enabled or disabled.
   * 
   * @param mode
   *          - Set the state of debug mode.
   */
  public void setDebugMode(boolean mode)
  {
    if(allowDebug)
    {
      if(mode != debugMode)
      {
        if(mode)
        {
          blockSet.showTypes(true);
          ballSet.setSpeedMod(0.3f);
        }
        else
        {
          blockSet.showTypes(false);
          ballSet.setSpeedMod(1.0f);
        }
      }

      debugMode = mode;
    }
  }

  /**
   * Set debug mouse control to be enabled or disabled.
   * 
   * @param mode
   *          - Set the state of debug mouse control.
   */
  public void setDebugMouseControl(boolean mode)
  {
    if(allowDebug)
    {
      if(mode != debugMouseControl)
      {
        if(mode)
        {
          debugVelocitySaved.set(ballSet.get(0).getVelocity());
          ballSet.get(0).setVelocity(0.0f, 0.0f);
        }
        else
        {
          ballSet.get(0).setVelocity(debugVelocitySaved);
        }
      }

      debugMouseControl = mode;
    }
  }

  /**
   * Checks for and reacts to the ball colliding with a switch.
   * 
   * @param ball
   *          - The ball to check collision with.
   */
  protected void ballSwitchCollision(Ball ball)
  {
    if(theSwitch.isActive() && theSwitch.collided(ball))
    {
      theSwitch.reflect(ball);
      theSwitch.deactivate();
      trapSet.deactivate();
      blockSet.toggleUnbreakables();
      ball.playBounceSound();
    }
  }

  /**
   * Updates the game state by one time cycle.
   */
  public void updateWorld()
  {
    super.updateWorld();

    if(state == GameState.NORMAL)
    {
      ballSet.update();
      paddleSet.update();
      decorations.update();
      blockSet.update();
      trapSet.update();
      
      // Update child code
      update();

      if(keyboard.isButtonTapped(KeyEvent.VK_ESCAPE))
      {
        // TODO: C# version calls GameObjects.pause();
        // instead of paused = true;
        state = GameState.PAUSED;
        resources.pauseSound();
      }
    }
    else if(state == GameState.START)
    {
    	startScreenButtons();
    }
    else if(state == GameState.GAMEOVER)
    {
    	if (needCleanUp) {
    		endGame();
    	    enableGameoverBackground();
    	    needCleanUp = false;
    	}
    	afterGameState(GAMEOVER_STATIC_IMAGE, GAMEOVER_EXIT_HOVER_IMAGE,
    			GAMEOVER_REPLAY_HOVER_IMAGE);
    }
    else if( state == GameState.WIN)
    {
    	if (needCleanUp) {
    		endGame();
    	    enableGameoverBackground();
    	    needCleanUp = false;
    	}
    	afterGameState(GAMEWIN_STATIC_IMAGE, GAMEWIN_EXIT_HOVER_IMAGE,
    			GAMEWIN_REPLAY_HOVER_IMAGE);
    }
    else if(state == GameState.PAUSED)
    {
      if(keyboard.isButtonTapped(KeyEvent.VK_ESCAPE))
      {
        // TODO: C# version  callsGameObjects.resume();
        // instead of paused = false;
        state = GameState.NORMAL;
        resources.resumeSound();
      }
    }

    if(ballSet.size() > 0)
    {
      Ball curBall = ballSet.get(0);

      if(curBall != null && curBall.isVisible())
      {
        if(debugMouseControl)
        {
          //curBall.center.set(mouse.getWorldX(), mouse.getWorldY());

          boolean pressedW = keyboard.isButtonDown(KeyEvent.VK_W);
          boolean pressedS = keyboard.isButtonDown(KeyEvent.VK_S);
          boolean pressedA = keyboard.isButtonDown(KeyEvent.VK_A);
          boolean pressedD = keyboard.isButtonDown(KeyEvent.VK_D);

          float moveX = 0.0f;
          float moveY = 0.0f;

          float speed = 24.0f / 40.0f;

          if(pressedW)
          {
            moveY += speed *2;
          }

          if(pressedS)
          {
            moveY -= speed *2;
          }

          if(pressedA)
          {
            moveX -= speed *2;
          }

          if(pressedD)
          {
            moveX += speed *2;
          }

          curBall.setVelocity(moveX, moveY);
        }
      }
    }

    if(state == GameState.NORMAL) {
	    if(keyboard.isButtonTapped(KeyEvent.VK_F1))
	    {
	      setDebugMode(!debugMode);
	    }
	
	    if(keyboard.isButtonTapped(KeyEvent.VK_F2))
	    {
	      setDebugMouseControl(!debugMouseControl);
	    }
	
	    if(keyboard.isButtonTapped(KeyEvent.VK_F3))
	    {
	      goToStartScreen();
	    }
	
	    if(keyboard.isButtonTapped(KeyEvent.VK_F4))
	    {
	      gameWin();
	    }
	
	    if(keyboard.isButtonTapped(KeyEvent.VK_F5))
	    {
	      window.close();
	
	      return;
	    }
	
	    if(keyboard.isButtonTapped(KeyEvent.VK_F6))
	    {
	      window.setShouldSkipFrames(!window.getShouldSkipFrames());
	      System.out.println("Frame skipping is now: " +
	          window.getShouldSkipFrames());
	    }
	    
	    if(keyboard.isButtonTapped(KeyEvent.VK_Q))
	    {
	      Block b = blockSet.getBlockAt(0, 0);
	      System.out.println("Block X: " + b.getCenterX());
	      System.out.println("Block Y: " + b.getCenterY());
	      //b.setType(Block.BlockType.);
	    }
	    
	
	    if(keyboard.isButtonTapped(KeyEvent.VK_F11))
	    {
	      System.out.println("F11: Key pressed: toggle full screen");
	      window.toggleFullscreen();
	      scoreText.setFontSize((int) (window.getHeight() / 15));
	      levelText.setFontSize((int) (window.getHeight() / 15));
	      lifeText.setFontSize((int) (window.getHeight() / 15));
	      
	      // remove the cursor if in full screen mode
	      if (window.isFullscreen())
	    	  window.removeCursor();
	      else
	    	  window.resetCursor();
	      
	    }
	    
	    if(keyboard.isButtonTapped(KeyEvent.VK_F12))
	    {
	      toggleShowDebugInfo();
	    }
	
	    if(keyboard.isButtonTapped(KeyEvent.VK_M))
	    {
	      resources.toggleMuteSound();
	    }
	
	    if(keyboard.isButtonTapped(KeyEvent.VK_V))
	    {
	      if(resources.getSoundVolume() == 1.0f)
	      {
	        resources.setSoundVolume(0.8f);
	      }
	      else
	      {
	        resources.setSoundVolume(1.0f);
	      }
	    }
	
	    if(keyboard.isButtonTapped(KeyEvent.VK_N) && ballSet.size() > 0)
	    {
	      ballSet.get(0).setType(BallType.NORMAL);
	      paddleSet.get(0).setToNormal();
	    }
	    if(keyboard.isButtonTapped(KeyEvent.VK_F) && ballSet.size() > 0)
	    {
	      ballSet.get(0).setType(BallType.FIRE);
	      paddleSet.get(0).startFire();
	    }
	
	    if(keyboard.isButtonTapped(KeyEvent.VK_I) && ballSet.size() > 0)
	    {
	      ballSet.get(0).setType(BallType.ICE);
	      paddleSet.get(0).startIce();
	    }
	
	    if(keyboard.isButtonTapped(KeyEvent.VK_U))
	    {
	      blockSet.toggleUnbreakables();
	    }
    }
  }
  
  private void startScreenButtons() 
  {
	startscreen.update();
	if(!startscreen.startScreenRunning())
	{
		state = GameState.NORMAL;
		newGame();
	}
  }
/**
   * Called during the update function in order to simulate the
   * aftergame splashscreen.
   * 
   * This function is dependent on the actual images.
   * 
   * @param normalImg The image that has no highlighted buttns.
   * @param exitHoverImg The exit button is highlighted
   * @param replayHoverImg The replaybutton is highlighted.
   */
  private void afterGameState(String normalImg, String exitHoverImg,
		  String replayHoverImg) 
  {
	  // Focuses the highlightable area to a column in the center.
	  if(mouse.getWorldX() < (world.getWidth() / 2) + 7f &&
			  mouse.getWorldX() > (world.getWidth() / 2) - 7f)
	  {
		  // Focuses the exit highlight area to a row within the column
		  // so the highlight area is rectangular like the image.
		  if(mouse.getWorldY() > (world.getHeight() / 2) - 6f &&
				  mouse.getWorldY() < (world.getHeight() / 2) - 2f)
		  {
		      afterGameForeground1.setImage(exitHoverImg); 
		      if(mouse.isButtonDown(1))
		      {
		    	  window.close();
		      }
		  }
		  // The replay highlight area.
		  else if(mouse.getWorldY() > (world.getHeight() / 2) - 12f &&
				  mouse.getWorldY() < (world.getHeight() / 2) - 8f)
		  {
			  afterGameForeground1.setImage(replayHoverImg);
		      if(mouse.isButtonDown(1))
		      {
		          disableGameoverBackground();
		    	  goToStartScreen();
		      }
		  }
		  else
		  {
			  afterGameForeground1.setImage(normalImg); 
		  }
	  }
	  else
	  {
		 afterGameForeground1.setImage(normalImg); 
	  }
  }

  @Override
  public void clean() {
	  if (paddleSet != null)
		  paddleSet.clear();
	  if (ballSet != null)
		  ballSet.clear();
	  if (blockSet != null)
		  blockSet.clear();
	  if (trapSet != null)
		  trapSet.clear();
	  if (decorations != null)
		  decorations.clear();
	  
	  paddleSet = null;
	  ballSet = null;
	  blockSet = null;
	  trapSet = null;
	  decorations = null;
	  super.clean();
  }
  
  
/*public void draw(Graphics gfx) {
	  //this.bacgroundAnimations.draw();
	  super.draw(gfx);
  }*/
  /**
   * Initialize game data in this method.
   */
  protected abstract void initialize();

  /**
   * Modify game data every game cycle in this method.
   */
  protected abstract void update();
  
}