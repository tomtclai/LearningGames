
package GhostLight.GhostEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Vector;

import Engine.BaseCode;
import Engine.LibraryCode;
import Engine.Rectangle;
import Engine.ResourceHandler;
import Engine.Text;
import GhostLight.Interface.*;
import GhostLight.JarResources;
import GhostLight.Interface.FlashLight;
import GhostLight.Interface.GameState;
import GhostLight.Interface.GhostLightInterface;
import GhostLight.Interface.InteractableObject;
import GhostLight.Interface.MouseState;
import GhostLight.Interface.OnScreenButtons;
import GhostLight.Interface.FlashLight.lightType;
import GhostLight.Interface.GameState.EndState;
import MenueSystem.Button;
import MenueSystem.Label;
import MenueSystem.Panel;
import MenueSystem.Panel.PanelState;
import MenueSystem.Menue;

public class GhostLight extends LibraryCode{
	
	/**
	 * Will load images and other recourses required for the game
	 * @param resources
	 */
	public static void preLoadResources(ResourceHandler resources){
		  BaseEnemy.preLoadResources(resources);
		  Frankenstein.preLoadResources(resources);
		  Cat.preLoadResources(resources);
		  Ghost.preLoadResources(resources);
		  LightBeam.preLoadResources(resources);
		  Mummy.preLoadResources(resources);
		  Pumpkin.preLoadResources(resources);
		  Spider.preLoadResources(resources);
		  Vampire.preLoadResources(resources);
		  Zombie.preLoadResources(resources);
		  LightButton.preLoadResources(resources);
		  EnemySoundManager.preLoadResources(resources);
		  resources.loadImage("life.png");
		  resources.preloadSound("Sound/ghostlight_lose_life.wav");
	  }
	
  protected Player player = null;
  public static EnemySet enemies = null;
  protected int animationDelay = 30;
  protected float powerFillRate = 0.25f;
  protected int animationPauseTimer;

  protected final float FALLOFF_POSITION = 75.0f;
  protected final float FALLDOWN_POSITION = 10.0f;

  protected LightButton laserButton = null;
  protected LightButton mediumButton = null;
  protected LightButton wideButton = null;
  
  private static final int numLevelButtonImgs = 5;
  
  private final static float HEART_X_OFFSET = 5f;
  private final static float HEART_Y_OFFSET = 3.2f;
  
  protected PowerBar powerBar = null;
  
  private boolean updateTurn = true;
  
  protected final int STARTING_ENEMIES = 8;

  protected enum updateState {INGAME, MAINMENUE, ESC, TRANSITION, CREDITS};
  
  protected GhostLightInterface currentLevel = null;
  private EndState state = EndState.CONTINUE;
  private updateState active = updateState.INGAME;

  private Text typeSelectedText = null;
  private Text userText = null;
  
  int LevelNumber = 0;
  
  /** Main allows for picking a level to play and starting that level*/
  private Menue mainMenue = null;
  /** Displayed at the End of a Level and handles Progressing to the next Level at the conclusion of the currentLevel */
  private Menue transitionMenue = null;
  /** Handles exiting or resuming a level, mid Level */
  private Menue escapeMenue = null;
  
  private LinkedList<GhostLightInterface> tempList = new LinkedList<GhostLightInterface>();
  private LinkedList<String> tempNameList = new LinkedList<String>(); 
  private LinkedList<String> tempWinList = new LinkedList<String>(); 
  private LinkedList<String> tempLoseList = new LinkedList<String>(); 
  
  
  protected Vector<Life> hearts = new Vector<Life>();
  protected boolean healthHighlighted = false;
  protected int lifeCounter = 0;
  
  private Background background;
  
  private CreditsScreen credits;
  
  private PostGameScreen afterGameScreen;
  

  
  /**
   * Will add the given GhostLightInterface as a Level to come after all of the previous levels.
   * Note this Function will only function. prior to GameWindow.setRunner() being called. calling afterwards will have no affect. 
   * @param nextLevel The Level to Be added
   * @param name the Level's name
   */
  public void addLevel(GhostLightInterface nextLevel, String name){
	  addLevel(nextLevel, name, null, null);
  }
  /**
   * Will add the given GhostLightInterface as a Level to come after all of the previous levels
   * Note this Function will only function. prior to GameWindow.setRunner() being called. calling afterwards will have no affect.
   * @param nextLevel The Level to Be added
   * @param name the Level's name
   * @param WinMessage
   * @param LoseMessage
   */
  public void addLevel(GhostLightInterface nextLevel, String name, String WinMessage, String LoseMessage){
	  if(tempList != null){
		  tempList.add(nextLevel);
		  //name
		  if(name != null){
			  tempNameList.add(name);
		  }
		  else{
			  tempNameList.add("Level " + tempNameList.size());
		  }
		  //Win
		  if(WinMessage != null){
			  tempWinList.add(WinMessage);
		  }
		  else{
			  tempWinList.add("You Win");
		  }
		  //Lose
		  if(LoseMessage != null){
			  tempLoseList.add(LoseMessage);
		  }
		  else{
			  tempLoseList.add("You Lose");
		  }
		  //Setting first Level
		  if(currentLevel == null){
			  currentLevel = nextLevel;
		  }
	  }
  }
  /**
   * Will initialize GhostLight Menu and Gameplay Systems
   */
  public void initializeWorld() {
	  /*super.window.setSize(1600, 900);
	  super.world.centerPosition(1600/2,900/2);
	  super.*/
	  
    super.initializeWorld();
    BaseCode.resources.setClassInJar(new JarResources());
    preLoadResources(BaseCode.resources);
    initializeMenues();
    initializeGamePlay();
    setUpdateState(updateState.MAINMENUE);
    resources.playSoundLooping("Sound/ghotstlight_background_music.wav");
    
    background = new Background(); 
    
    credits = new CreditsScreen(keyboard);
    
    
  }

/**
   * Called when the currentLevel has indicated that the user has won the game
   */
  protected void gameWin() {
	  if (currentLevel != null) // Unknown if null check needed.
	  {
			currentLevel.end();
	  }
	  enemies.moveAllEnemiestoVerticalPosition(-2);
	  afterGameScreen.enablePostScreen(true);

  }
  /**
   * Called when the currentLevel has indicated that the user has lost the game
   */
  protected void gameLose() {
	  if (currentLevel != null)
	  {
			currentLevel.end();
	  }
	  enemies.moveAllEnemiestoVerticalPosition(enemies.getNumberOfRows() + 3);
	  afterGameScreen.enablePostScreen(false);
  }
  /**
   * Will change the update state to the given update state
   * This will Determine what objects receive updates and which objects are visible
   * @param newState
   */
  protected void setUpdateState(updateState newState){
	  if(active != newState){
		  //In Game 
		  if(newState == updateState.INGAME){
			  player.visible = true;
			  player.getLight().visible = true;
			  powerBar.setVisibility(true);
			  laserButton.setVisibility(true, true);
			  wideButton.setVisibility(true, true);
			  mediumButton.setVisibility(true, true);
			  userText.visible = true;
			  typeSelectedText.visible = true;
			  
			  mainMenue.setState(PanelState.INACTIVE, true);
			  mainMenue.setVisibility(false, true);
			  escapeMenue.setState(PanelState.INACTIVE, true);
			  escapeMenue.setVisibility(false, true);
			  transitionMenue.setState(PanelState.INACTIVE, true);
			  transitionMenue.setVisibility(false, true);
			  EnemySoundManager.clear();
			  active = newState;
		  }
		  //Main Menue
		  else if(newState == updateState.MAINMENUE){
			  player.visible = false;
			  player.getLight().visible = false;
			  powerBar.setVisibility(false);
			  laserButton.setVisibility(false, true);
			  wideButton.setVisibility(false, true);
			  mediumButton.setVisibility(false, true);
			  userText.visible = false;
			  typeSelectedText.visible = false;
			  enemies.RemoveAllEnemies();
			  mediumButton.setSparcleVisiblity(false);
			  wideButton.setSparcleVisiblity(false);
			  laserButton.setSparcleVisiblity(false);
			  if(currentLevel != null){
				  currentLevel.end();
			  }
			  setHeartCount(0);
			  
			  mainMenue.setVisibility(true, true);
			  mainMenue.setState(PanelState.IDLE, true);
			  
			  
			  escapeMenue.setState(PanelState.INACTIVE, true);
			  escapeMenue.setVisibility(false, true);
			  transitionMenue.setState(PanelState.INACTIVE, true);
			  transitionMenue.setVisibility(false, true);
			  active = newState;
		  }
		  //Transition Menue
		  else if(newState == updateState.TRANSITION){
			  if(state == EndState.LOSE || state == EndState.WIN){
				  transitionMenue.setVisibility(true, true);
				  transitionMenue.setState(PanelState.IDLE, true);
				  
				  mainMenue.setState(PanelState.INACTIVE, true);
				  mainMenue.setVisibility(false, true);
				  escapeMenue.setState(PanelState.INACTIVE, true);
				  escapeMenue.setVisibility(false, true);
				  active = newState;

				  if(state == EndState.LOSE){
					  transitionMenue.getPanel(1).setState(PanelState.INACTIVE, true);
					  // TODO: This needs fixing too
					  //((Label)transitionMenue.getPanel(2)).setText(((LevelButton)mainMenue.getPanel(2).getPanel(0).getPanel(LevelNumber)).loseMessage);
				  }
				  else{
					  if(((LevelButton)transitionMenue.getPanel(1)).gameLevel != null){
						  transitionMenue.getPanel(1).setState(PanelState.IDLE, true); 
					  }
					  else{
						  transitionMenue.getPanel(1).setState(PanelState.INACTIVE, true);
					  }
					  ((Label)transitionMenue.getPanel(2)).setText(((LevelButton)mainMenue.getPanel(2).getPanel(0).getPanel(LevelNumber)).winMessage);
				  }
			  }
		  }
		  //Credits
		  else if(newState == updateState.CREDITS){
			  
			  transitionMenue.setVisibility(false, true);
			  transitionMenue.setState(PanelState.INACTIVE, true);
			  
			  mainMenue.setState(PanelState.INACTIVE, true);
			  mainMenue.setVisibility(false, true);
			  escapeMenue.setState(PanelState.INACTIVE, true);
			  escapeMenue.setVisibility(false, true);
			  active = newState;

			  credits.startCredits();
			  

			  
		  }
		  //Escape Menu
		  else if(newState == updateState.ESC){
			  escapeMenue.setVisibility(true, true);
			  escapeMenue.setState(PanelState.IDLE, true);
			  
			  mainMenue.setState(PanelState.INACTIVE, true);
			  mainMenue.setVisibility(false, true);
			  transitionMenue.setState(PanelState.INACTIVE, true);
			  transitionMenue.setVisibility(false, true);
			  active = newState;
		  }
		  else{
			  setUpdateState(updateState.ESC);
		  }
	  }
  }
  /**
   * Will initialize the various Menues and Buttons that are used by the game
   * This includes the Main Menue, Transition Menue, and Escape Menue
   */
  protected void initializeMenues(){
	  //Main Menue
	  mainMenue = new Menue();
	  mainMenue.getBackgroundCenter().set(world.getWidth()/2, world.getHeight()/2);
	  mainMenue.getBackgroundSize().set(world.getWidth()/4, world.getHeight()/3f);
	  mainMenue.setIdleImage("menuArt/ButtonIdle.png");
	  
	  //Title
	  Panel title = new Panel();
	  title.getBackgroundSize().set(world.getWidth(),world.getHeight());
	  title.getBackgroundCenter().set(world.getWidth()/2, world.getHeight() / 2);
	  title.setIdleImage("startscreen/GhostlightStartScreenBG.png");
	  mainMenue.addPanel(title);
	  
	  //Bottombar
	  Panel bbar = new Panel();
	  bbar.getBackgroundSize().set(world.getWidth() * .65f, world.getHeight() * .76f);
	  bbar.getBackgroundCenter().set(world.getWidth() / 2, world.getHeight() * .4f);
	  bbar.setIdleImage("startscreen/BarStartMenuDown.png");
	  mainMenue.addPanel(bbar);
	  
	  //Credit Button
	  Button credits = new CreditsButton(this);
	  credits.getBackgroundSize().set(world.getWidth() * .25f, world.getHeight() * .15f);
	  credits.getBackgroundCenter().set(world.getWidth() * .67f , world.getHeight() * .125f);
	  credits.setIdleImage("startscreen/CreditsButton_Static.png");
	  credits.setButtonDownImage("startscreen/CreditsButton_Press.png");
	  credits.setMouseOverIdleImage("startscreen/CreditsButton_Hover.png");
	  credits.setMouseOverSelectImage("startscreen/CreditsButton_Press.png");
	  mainMenue.addButton(credits);
	  
	  if (tempList != null)
	  {
		  // Only added the panel of level choices if there is more than one level:
		  // If there is only one level, the play button is the goes directly to the first level.
		  // Else, the playbutton opens a list of all possible levels.
		  // If the there are no levels, don't put a play button.
		  if(tempList.size() == 1)
		  {
			  LevelButton  onlyLevel = new LevelButton();
			  onlyLevel.setIdleImage("startscreen/PlayButton_Static.png");
			  onlyLevel.setButtonDownImage("startscreen/PlayButton_Press.png");
			  onlyLevel.setMouseOverIdleImage("startscreen/PlayButton_Hover.png");
			  onlyLevel.setMouseOverSelectImage("startscreen/PlayButton_Press.png");
		
			  onlyLevel.getBackgroundSize().set(world.getWidth() * .25f, world.getHeight() * .15f);
			  onlyLevel.getBackgroundCenter().set(world.getWidth() * .33f , world.getHeight() * .125f);
			  
			  onlyLevel.setLevel(this, tempList.remove());
			  onlyLevel.winMessage = tempWinList.remove();
			  onlyLevel.loseMessage = tempLoseList.remove();
			  mainMenue.addButton(onlyLevel);
		  }
		  else if (tempList.size() > 1)
		  {
			  //choseLevel, AKA the play button
			  Button chooseLevel = new Button();
			  chooseLevel.selectable = true;
			  chooseLevel.setIdleImage("startscreen/PlayButton_Static.png");
			  chooseLevel.setButtonDownImage("startscreen/PlayButton_Press.png");
			  chooseLevel.setMouseOverIdleImage("startscreen/PlayButton_Hover.png");
			  chooseLevel.setMouseOverSelectImage("startscreen/PlayButton_Press.png");
			  chooseLevel.setSelectImage("startscreen/PlayButton_Static.png");
			  chooseLevel.getBackgroundSize().set(world.getWidth() * .25f, world.getHeight() * .15f);
			  chooseLevel.getBackgroundCenter().set(world.getWidth() * .33f , world.getHeight() * .125f);
			  mainMenue.addButton(chooseLevel);		  
			 
			  // Popup background
			  Panel bpop = new Panel();
			  bpop.getBackgroundSize().set(world.getWidth() * .28f,world.getHeight() * .47f);
			  bpop.getBackgroundCenter().set(world.getWidth() * .33f, world.getHeight() * .47f);
			  bpop.setIdleImage("startscreen/BarStartMenuUpv2.png");
			  chooseLevel.addPanel(bpop);
			  

			  //LevelMenue
			  Menue LevelMenue = new Menue();
			  LevelMenue = new Menue();
			  LevelMenue.getBackgroundCenter().set(world.getWidth() * .33f,world.getHeight() * .47f);
			  LevelMenue.getBackgroundSize().set(world.getWidth() * .25f, world.getHeight() * .42f);
			  LevelMenue.setIdleImage("menuArt/ButtonIdle.png");
			  chooseLevel.addPanel(LevelMenue);
			  
			  //Adding Levels

			  int imgCounter = 1;
			  while(tempList.size() > 0){
				  LevelButton  newLevel = new LevelButton();
				  
				  if(imgCounter <= numLevelButtonImgs)
				  {
					  tempNameList.remove();
					  newLevel.setIdleImage("startscreen/LevelButton" + imgCounter + "_Static.png");
					  newLevel.setButtonDownImage("startscreen/LevelButton" + imgCounter + "_Press.png");
					  newLevel.setMouseOverIdleImage("startscreen/LevelButton" + imgCounter + "_Hover.png");
					  newLevel.setMouseOverSelectImage("startscreen/LevelButton" + imgCounter + "_Press.png");
				  }
				  else
				  {
					  newLevel.setText(tempNameList.remove());
					  newLevel.getFont().setBackColor(new Color(0,0,0,0));
					  newLevel.getFont().setFrontColor(Color.BLACK);
				  }

				  //newLevel.getBackgroundSize().set(world.getWidth() * .25f, world.getHeight() * 0.1f);
				  //newLevel.getBackgroundCenter().set(world.getWidth()* .33f,
					//	  world.getHeight()* 2f + ((imgCounter - 1) * newLevel.getBackgroundSize().getY() ));
				  
				  newLevel.setLevel(this, tempList.remove());
				  newLevel.winMessage = tempWinList.remove();
				  newLevel.loseMessage = tempLoseList.remove();
				  LevelMenue.addButton(newLevel);
				  imgCounter++;
			  }
			  LevelMenue.setButtonSizesPositions(true);
		  }
	  }
	  tempList = null;
	  tempNameList = null;
	  tempWinList = null;
	  tempLoseList = null;
	  
	  mainMenue.setState(PanelState.IDLE, true);
	  
	  
	  
	  //Transition Menu
	  transitionMenue = new Menue();
	  transitionMenue.getBackgroundCenter().set(world.getWidth()/2, world.getHeight()/2);
	  transitionMenue.getBackgroundSize().set(world.getWidth()/4, world.getHeight()/4);
	  transitionMenue.setIdleImage("menuArt/ButtonIdle.png");
	  //replay Level
	  LevelButton replay = new LevelButton();
	  replay.setText("Replay Level");
	  replay.getFont().setBackColor(new Color(0,0,0,0));
	  replay.getFont().setFrontColor(Color.BLACK);
	  transitionMenue.addButton(replay);
	  //Next Level
	  LevelButton next = new LevelButton();
	  next.setText("Next Level");
	  next.getFont().setBackColor(new Color(0,0,0,0));
	  next.getFont().setFrontColor(Color.BLACK);
	  transitionMenue.addButton(next);
	  
	  Label WinLose = new Label();
	  WinLose.getTextPosition().set(world.getWidth()*0.40f, world.getHeight() * 0.65f);
	  WinLose.getBackgroundSize().set(0,0);
	  WinLose.getTextSize().set(world.getWidth() * 0.75f, world.getHeight() * 0.2f);
	  WinLose.setText("");
	  WinLose.getFont().setBackColor(new Color(0,0,0,0));
	  WinLose.getFont().setFrontColor(Color.BLACK);
	  transitionMenue.addPanel(WinLose);
	  
	  transitionMenue.setButtonSizesPositions(true);
	  transitionMenue.setState(PanelState.IDLE, true);
	  
	  //Escape Menu
	  escapeMenue = new Menue();
	  escapeMenue.getBackgroundCenter().set(world.getWidth()/2, world.getHeight()/2);
	  escapeMenue.getBackgroundSize().set(world.getWidth()/4, world.getHeight()/4);
	  escapeMenue.setIdleImage("menuArt/ButtonIdle.png");
	  //Resume Game
	  ChangeGameStateButton resume = new ChangeGameStateButton();
	  resume.setText("Resume Game");
	  resume.getFont().setBackColor(new Color(0,0,0,0));
	  resume.getFont().setFrontColor(Color.BLACK);
	  resume.setTargetState(this, updateState.INGAME);
	  escapeMenue.addButton(resume);
	  //To Main Menue
	  ChangeGameStateButton toMain = new ChangeGameStateButton();
	  toMain.setText("Main Menu");
	  toMain.getFont().setBackColor(new Color(0,0,0,0));
	  toMain.getFont().setFrontColor(Color.BLACK);
	  toMain.setTargetState(this, updateState.MAINMENUE);
	  escapeMenue.addButton(toMain);
	  
	  escapeMenue.setButtonSizesPositions(true);
	  escapeMenue.setState(PanelState.IDLE, true);
  }
  /**
   * Will add a button to the given menu labeled "Controls" and set it to open a sub panel describing the controls of the game
   * Note does not auto scale the menue and by defualt sets the button state to IDLE and the subpanel state to INACTIVE
   * Not in use becuase this class can not possible know the controls in use
   */
  private void createControlsMenue(Menue targetMenue){
	  
	  
	  if(targetMenue != null){
		  //Controls Button
		  Button controls = new Button();
		  controls.setIdleImage("menuArt/ButtonIdle.png");
		  controls.setButtonDownImage("menuArt/ButtonDown.png");
		  controls.setMouseOverIdleImage("menuArt/ButtonIdleMouseOver.png");
		  controls.setMouseOverSelectImage("menuArt/ButtonIdleMouseOver.png");
		  controls.setSelectImage("menuArt/ButtonIdle.png");
		  controls.setText("Controls");
		  controls.setState(PanelState.IDLE, false);
		  //Controls Panel S
		  Label infoBox = new Label();
		  infoBox.setIdleImage("menuArt/ButtonIdle.png");
		  infoBox.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getWidth()/2);
		  infoBox.getBackgroundSize().set(BaseCode.world.getWidth()/3, BaseCode.world.getWidth() * 0.66f);
		  infoBox.setText("Move Left: 'A', Left Arrow Key, Move Mouse Left" + '\n'
		  				+ "Move Right: 'D', Right Arrow Key, Move Mouse Right" + '\n'
		  				+ "Shine Light: Space, Enter, Left Mouse Button" + '\n'
		  				+ "Cycle Light Type Up: 'W', Up Arrow Key, Left Arrow Key" + '\n'
		  				+ "Cycle Light Type Down: 'S', Down Arrow Key, Middle Mouse Button" + '\n'
		  				+ "Select Normal Beam: 'N', Click Normal Beam Button" + '\n'
		  				+ "Select Wide Beam: 'V', Click Wide Beam Button" + '\n'
		  				+ "Select Laser Beam: 'B', Click Laser Beam Button");
		  infoBox.getTextPosition().set(infoBox.getBackgroundCenter().getX() - (infoBox.getBackgroundSize().getX()* 0.42f) ,infoBox.getBackgroundCenter().getY() + (infoBox.getBackgroundSize().getY()* 0.25f));
		  infoBox.setState(PanelState.INACTIVE, false);
		  infoBox.setVisibility(false, false);
		  controls.addPanel(infoBox);
		  targetMenue.addButton(controls);
	  }
  }
  /**
   * Will initialize the objects used in game play
   * This includes the player, Light Buttons Power Bar, enemySet, and various text Objects
   */
  protected void initializeGamePlay(){
	  //enemySet
	  enemies = new EnemySet();
	  enemies.setSetPadding(12f, 20f, 13f, 17f);
	  //player
	  player = new Player();
	  player.color = Color.blue;
	  player.removeFromAutoDrawSet();
	  player.getLight().removeFromAutoDrawSet();
	  //power bar
	  powerBar = new PowerBar();
	  powerBar.removeFromAutoDrawSet();
	  
	  // Text
	  resources.preloadFont("fonts/Grind Zero.ttf");
	  resources.preloadFont("fonts/Grind Zero Bold Italic.ttf");
	  //score text
	  typeSelectedText = new Text();
	  typeSelectedText.setFontName("Grind Zero");
	  typeSelectedText.removeFromAutoDrawSet();
	  typeSelectedText.setFrontColor(Color.WHITE);
	  typeSelectedText.setBackColor(Color.BLACK);
	  typeSelectedText.setFontSize(24);
	  //user text
	  userText = new Text();
	  userText.setFontName("Grind Zero");
	  userText.setFrontColor(Color.WHITE);
	  userText.setBackColor(Color.BLACK);
	  userText.setFontSize(24);
	  //light type indicator buttons
	  mediumButton = new LightButton();
	  mediumButton.setResponsibility(player.getLight(), LightBeam.BeamType.REVEAL);
	  mediumButton.removeFromAutoDrawSet();
	  
	  wideButton = new LightButton();
	  wideButton.setResponsibility(player.getLight(), LightBeam.BeamType.WIDE);
	  wideButton.removeFromAutoDrawSet();
	  
	  laserButton = new LightButton();
	  laserButton.setResponsibility(player.getLight(), LightBeam.BeamType.LASER);
	  laserButton.removeFromAutoDrawSet();
  }
  /**
   * Conducts final initialization of gameplay objects including the current level
   * Required to begine a game
   */
  protected void newGame(GhostLightInterface userCode, int LevelNum) {
    if(getBackgroundImage() != null) {
      getBackgroundImage().alwaysOnTop = false;
      BaseCode.resources.moveToBackOfDrawSet(getBackgroundImage());
    }
    resources.setSoundVolume("Sound/ghotstlight_background_music.wav", 0.8f);
    
    this.setUpdateState(updateState.INGAME);
    currentLevel = userCode;
    LevelNumber = LevelNum;
    
    //TODO: Needs refactoring
//    ((LevelButton)transitionMenue.getPanel(0)).setLevel(this, currentLevel, LevelNum); // Switches a button with this data.
//    if(mainMenue.getPanel(2).getPanel(0).getPanel(LevelNum+1) != null){ // Checks if level button exists.
//    	((LevelButton)transitionMenue.getPanel(1)).setLevel(this, ((LevelButton)mainMenue.getPanel(2).getPanel(0).getPanel(LevelNumber+1)).gameLevel, LevelNumber+1);
//    }
//    else{
//    	((LevelButton)transitionMenue.getPanel(1)).setLevel(this, null);
//    }
//    ((LevelButton)mainMenue.getPanel(0)).setLevel(this, userCode, LevelNum);
    //endGame();

    // Turn off sound while testing
    //resources.setMuteSound(true);

    //resources.stopSound(backgroundMusic + ".wav");
    //resources.playSoundLooping(backgroundMusic + ".wav");

    setHeartHighlight(false);
    setHeartCount(4);
    
    //Enemies
    enemies.RemoveAllEnemies();
    
    //Player
    player.center.set(0.7f, 5.0f);
    player.setCollumn(1);
    player.setEnemySet(enemies);
    player.visible = true;
    player.getLight().visible = true;
    player.setHurtState(false);
    currentLevel.light.setHurtState(false);
    
    //powerBar
    powerBar.size.set(4.0f, 20.0f);
    powerBar.center.set(world.getWidth() - (powerBar.size.getX()/2) - 5f, world.getHeight() - (powerBar.size.getY()/2) - 10f);
    powerBar.size.set(6.0f, 24.0f);
    powerBar.updateBar();
    powerBar.setMaxScore(10.0f);
    powerBar.setScore(0.0f);
    powerBar.setToVertical();
    powerBar.setVisibility(true);
    
    animationPauseTimer = 30;
    
    //typeSelectedText
    typeSelectedText.center.set(5.0f, world.getHeight() - 6.5f);
    typeSelectedText.setText("Score: "+ player.getScore());
    typeSelectedText.visible = true;
    
    userText.center.set(30f, world.getHeight() - 6.5f);
    userText.setText("");
    userText.visible = true;   
    
    //buttons
    //Reveal
    mediumButton.getBackgroundSize().set(4f, 4f);
    mediumButton.getBackgroundCenter().set(world.getWidth() - (mediumButton.getBackgroundSize().getX()/2) - 5f, (world.getHeight()/2) - 5);
    mediumButton.setVisibility(true, true);
    mediumButton.setState(PanelState.IDLE, true);
    mediumButton.setSparcleVisiblity(false);
    
    //Wide
    wideButton.getBackgroundSize().set(4f, 4f);
    wideButton.getBackgroundCenter().set(world.getWidth() - (mediumButton.getBackgroundSize().getX()/2) - 5f, (world.getHeight()/2) - 10f);
    wideButton.setVisibility(true, true);
    wideButton.setState(PanelState.IDLE, true);
    wideButton.setSparcleVisiblity(false);
    //Laser
    laserButton.getBackgroundSize().set(4f, 4f);
    laserButton.getBackgroundCenter().set(world.getWidth() - (mediumButton.getBackgroundSize().getX()/2) - 5f, (world.getHeight()/2) - 15f);  
    laserButton.setVisibility(true, true);
    laserButton.setState(PanelState.IDLE, true);
    laserButton.setSparcleVisiblity(false);
    
    player.setCollumn(0);
    
    if(userCode != null){
    	InteractableObject.setDefualtsToDefualts();
    	player.turnLightOnorOff(false);
    	userCode.objectGrid.setObjectGrid(enemies.getGrid());
    	synkPlayerStateWithTrueState();
    	if(userCode.objectGrid != null){
    		userCode.objectGrid.reset();
    	}
    	if(userCode.primitiveGrid != null){
    		userCode.primitiveGrid.reset();
    	}
    	userCode.initialize();
    	synkTrueStateWithPlayerState();
    	userCode.light.updateMaxPosition(userCode.objectGrid.getObjectGrid());
    	StateChange change = enemies.synkGrid(userCode.objectGrid.getObjectGrid(), animationDelay);
    	if(change != null && change.puaseForAnimation){
    		animationPauseTimer = animationDelay;
    		enemies.setEnemyAnimations(0);
    	}
    }
    
    // Null check differentiates the first time the game is started, and every replay.
    if (afterGameScreen == null)
    {
    	afterGameScreen = new PostGameScreen(mouse, this, currentLevel, LevelNum);
    }
    afterGameScreen.disablePostScreen();
    
    setEndGameState( EndState.CONTINUE);   
  }
  /**
   * Will set the number of hearts drawn
   * @param num
   */
  protected void setHeartCount(int num) {
	  if(num != lifeCounter){
		if(num != 0){
			if(num < lifeCounter){
				BaseCode.resources.playSound("Sound/ghostlight_lose_life.wav");
				
			    // Remove hearts
			    while(lifeCounter > num) {
			      decreaseLife();
			    }
			}
			else
			{
				// Regain hearts
				for(; lifeCounter < hearts.size() && lifeCounter < num; lifeCounter++)
				{
					hearts.get(lifeCounter).regain();
				}
				
			    // Add new hearts
			    for(int i = lifeCounter; i < num; i++) {
			      Life heart = new Life();
			      heart.center.set(world.getWidth() - HEART_X_OFFSET - ((i + 1) * heart.size.getX()) -
			          (i * 1.25f), world.getHeight() - HEART_Y_OFFSET - (heart.size.getY() * 0.5f) - 1.0f);
			      hearts.add(heart);
		    	  heart.Sparcle = healthHighlighted;
		    	  lifeCounter++;
			    }
			}
		}
		else{
			while(hearts.size() > 0) {
				//decreaseLife();
				hearts.clear();
			}
		}
	  }
	  lifeCounter = num;
  }
  /**
   * Will decrease life by 1
   */
  protected void decreaseLife() {
    if(lifeCounter > 0) {
      Life heart = hearts.get(lifeCounter - 1);
      heart.lose();
      lifeCounter--;
    }
  }
  /**
   * Will add a number of hearts from the set of hearts drawn
   * @param increment the number of hearts to be added
   */
  protected void incrementHeartCount(int increment){
	  setHeartCount(increment + lifeCounter);  
  }
  /**
   * Will return the number of hearts drawn
   * @return
   */
  protected int getLivesRemaining(){
    return lifeCounter;
  }
  /**
   * will highlight or unhighlight the hearst drawn
   * @param highlight
   */
  protected void setHeartHighlight(boolean highlight){
	  if(hearts.size() > 0 && healthHighlighted != highlight) {
		  healthHighlighted = highlight;
		  for(int loop = 0; loop < hearts.size(); loop++){
			  if(hearts.get(loop) != null){
				  hearts.get(loop).Sparcle = highlight;
			  }
		  }
	  }
  }
  /**
   * will return the light beam currently employed by the player
   * @return
   */
  protected LightBeam.BeamType getLightBeamType() {
    return player.getLight().getType();
  }
  /**
   * Will update the objects actve detrmined by the updates state
   */
  public void updateWorld() {
    super.updateWorld();
    
    // Update enemy movement (position changes, not animation).
    enemies.update(animationDelay); //TODO: Temporally coupled. Lose lighting if after statemachine.
    
    // State Machine for Most of the game. The EndState is also used.
    switch (this.active)
    {
    case INGAME:
    	ingameStateSelection();
    	break;
    	
    case MAINMENUE:
    	mainMenue.autoUpdateState(mouse);
    	break;
    	
    case TRANSITION:
    	transitionMenue.autoUpdateState(mouse);
    	break;
    	
    case ESC:
    	escapeMenue.autoUpdateState(mouse);
    	break;
    case CREDITS:
    	if(credits.isCreditsDone())
    	{
    		setUpdateState(updateState.MAINMENUE);
    	}
    	else
    	{
        	credits.update(); 		
    	}
    	break;
    }
    

  }
  /**
   * Progresses the game state set by the current
   * EndState state
   */
  private void ingameStateSelection()
  {
	switch (state) {
	case CONTINUE:
		updateContinue();
		break;
	case LOSE:
		afterGameScreen.update();
		break;
	case WIN:
		afterGameScreen.update();
		break;
	}
  }
  
/**
 * Updates when state = EndState.CONTINUE;
 */
  protected void updateContinue(){
      typeSelectedText.setText("Score: "+ player.getScore());
      
      EnemySoundManager.update();
      
      player.update();
      player.selectTargetableEnemies();
      

      //Animation is playing user does not receive update
      if(animationPauseTimer > 0){
    	  partitionAnimationTime();
      }
      //No Animation currently playing user receives update
      else{
    	  if(currentLevel != null){
    		  // Sync between interface (user) and engine.
    		  currentLevel.objectGrid.setObjectGrid(enemies.getGrid());
    		  synkPlayerStateWithTrueState();
    		  currentLevel.update();
    		  synkTrueStateWithPlayerState();
    		  StateChange change = enemies.synkGrid(
    				  currentLevel.objectGrid.getObjectGrid(),
    				  animationDelay);
    		  changeState(change);
    		  
    		  // Check if user clicked.
    		  if(change != null && change.puaseForAnimation){
    			  animationPauseTimer = animationDelay;
    			  //enemies.setEnemyAnimations(0); //TODO: Seemly useless code. Check with Micheal.
    		  }
    	  }
      }

      updateKeyInput();
  }
  
  /**
   * Divides up the animation pause time into sections
   * of animations for enemies.
   * 
   * Decrements the pause timer every time its called.
   */
  private void partitionAnimationTime(){
      //updating turn 
      if(this.updateTurn){
    	  changeState(enemies.updateTurn());
    	  updateTurn = false;
      }
      
      //TODO: Move to top as Static variables when finished.
      int introAnimationTime = (int) (animationDelay/3f);
      int specialAbilityTIme = (int) (animationDelay/3f);
      int moveTime = (int) (animationDelay/3f);
      
      // Trigger animations in enemies depending on
      // partitioned time of animationDelay;
      if(animationPauseTimer == animationDelay)
      {
    	  enemies.playIntroductions(introAnimationTime);
      }
      if(animationPauseTimer == animationDelay - introAnimationTime)
      {
    	  enemies.activateSpecialAbilities(specialAbilityTIme);
      }
      if(animationPauseTimer == animationDelay - introAnimationTime
    		  - specialAbilityTIme)
      {
    	  enemies.allowMovement(moveTime);
      }

	  animationPauseTimer--;
  }
  
  /**
   * Updates the keyboard inputs for system outside of user control.
   */
  private void updateKeyInput() {
      //Esc Menue
      if(keyboard.isButtonTapped(KeyEvent.VK_ESCAPE)){
    	  setUpdateState(updateState.ESC);
      }
      //Close
      if(keyboard.isButtonTapped(KeyEvent.VK_F5)) {
    	  window.close();
    	  return;
      }
      //Mute
      if(keyboard.isButtonTapped(KeyEvent.VK_M)) {
    	  BaseCode.resources.toggleMuteSound();
      }
      //Fullscreen
      if(keyboard.isButtonTapped(KeyEvent.VK_F11)) {
    	  window.toggleFullscreen();
    	  if(window.isFullscreen())
    	  {
    		  typeSelectedText.setFontSize(48);
    		  userText.setFontSize(48);
    	  }
    	  else
    	  {
    		  typeSelectedText.setFontSize(24);
    		  userText.setFontSize(24);
    	  }
      }
      /*
      //Dev Keys
      //set Score to zero  
      if(keyboard.isButtonTapped(KeyEvent.VK_BACK_SPACE)){
    	  player.setScore(0);
    	  System.out.println("Score = " + player.getScore());
      }
      //increase Score  
      if(keyboard.isButtonTapped(KeyEvent.VK_ADD)){
    	  player.incrementScore(5);
    	  System.out.println("Score = " + player.getScore());
      }
      //Decrease Score
      if(keyboard.isButtonTapped(KeyEvent.VK_SUBTRACT)){
    	  player.incrementScore(-5);
    	  System.out.println("Score = " + player.getScore());
      }
      //increase Health
      if(keyboard.isButtonTapped(KeyEvent.VK_0)){
    	  this.incrementHeartCount(1);
    	  System.out.println("Health = " + hearts.size());
      }
      //Decrease Health
      if(keyboard.isButtonTapped(KeyEvent.VK_9)){
    	  this.incrementHeartCount(-1);
    	  System.out.println("Health = " + hearts.size());
      }
      //MaxPower
      if(keyboard.isButtonTapped(KeyEvent.VK_8)){
    	  this.powerBar.setPercent(1f);
    	  System.out.println("Power = 100%");
      }
      //MinPower
      if(keyboard.isButtonTapped(KeyEvent.VK_7)){
    	  this.powerBar.setPercent(0f);
    	  System.out.println("Power = 0%");
      }
      //increase Power Fill Rate
      if(keyboard.isButtonTapped(KeyEvent.VK_6)){
    	  this.powerFillRate += 0.1;
    	  System.out.println("Recharge Rate = " + powerFillRate);
      }
      //Decrease Power Fill Rate
      if(keyboard.isButtonTapped(KeyEvent.VK_5)){
    	  this.powerFillRate -= 0.1;
    	  System.out.println("Recharge Rate = " + powerFillRate);
      }
      //Kill All Enemies
      if(keyboard.isButtonTapped(KeyEvent.VK_4)){
    	  enemies.RemoveAllEnemies();
    	  System.out.println("Poof");
      }
      */
	
}
/**
   * Will update the health, power and score based on a given StateChange object
   * @param changeState
   * @return
   */
  protected boolean changeState(StateChange changeState){
	  if(changeState != null){
		  player.incrementScore(changeState.changeInPlayerScore);
		  powerBar.setPercent(powerBar.getPercent()+changeState.changInBatteryCharge);
		  this.incrementHeartCount(changeState.changeInPlayerHealth);
		  return true;
	  }
	  return false;
  }
  /**
   * Will Synk the users gameState with the true GameState
   */
  protected void synkPlayerStateWithTrueState(){
	  if(currentLevel != null){
		  if(currentLevel.gameState == null){
			  currentLevel.gameState = new GameState();
		  }
		  currentLevel.gameState.setHealth(getLivesRemaining());
		  currentLevel.gameState.setLightPower(powerBar.getPercent());
		  currentLevel.gameState.setScore(player.getScore());
		  currentLevel.gameState.setAnimationTime(animationDelay);
		  currentLevel.gameState.markTurnEnd(updateTurn);
		  currentLevel.gameState.setGameEnd(state);
		  currentLevel.gameState.setPowerBarColor(powerBar.color);
		  currentLevel.gameState.setMessage(userText.getText());
		  currentLevel.gameState.setHealthHighLight(healthHighlighted);
		  
		  //Setting Player light
		  if(currentLevel.light == null){
			  currentLevel.light = new FlashLight(); 
		  }
		  currentLevel.light.updateMaxPosition(currentLevel.objectGrid.getObjectGrid());
		  currentLevel.light.setPosition(player.getCurrentCollumn());
		  if(currentLevel.light.isLightActive() != player.isLightActive()){
			  if(!player.isLightActive()){
				  currentLevel.light.deactivateLight();
			  }
		  }
		  if(currentLevel.objectGrid != null){
			  currentLevel.objectGrid.synkPrimitiveSet(currentLevel.primitiveGrid);
		  }
		  //Setting On Screen Buttons
		  mediumButton.autoUpdateState(mouse);
		  wideButton.autoUpdateState(mouse);
		  laserButton.autoUpdateState(mouse);	
		  
		  currentLevel.keyboard = super.keyboard;
		  if(currentLevel.mouse == null){
			  currentLevel.mouse = new MouseState();
		  }
		  ((MouseInfo)currentLevel.mouse).mouse = super.mouse;
		  ((MouseInfo)currentLevel.mouse).screenWidth = super.window.getWidth();
		  ((MouseInfo)currentLevel.mouse).screenHeight = super.window.getHeight();
		  if(currentLevel.clickableButtons == null){
			  currentLevel.clickableButtons = new OnScreenButtons();
		  }
		  ((ButtonAccess)currentLevel.clickableButtons).mouse = mouse;
		  ((ButtonAccess)currentLevel.clickableButtons).MediumButton = mediumButton;
		  ((ButtonAccess)currentLevel.clickableButtons).WideButton = wideButton;
		  ((ButtonAccess)currentLevel.clickableButtons).LaserButton = laserButton;
	  }
  }
  /**
   * Will Synk the true GameState with the users gameState
   */
  protected void synkTrueStateWithPlayerState(){
	  if(currentLevel != null){
		  if(currentLevel.gameState != null){
			  setHeartCount(currentLevel.gameState.getHealth());
			  powerBar.setPercent(currentLevel.gameState.getLightPower());
			  player.setScore(currentLevel.gameState.getScore());
			  animationDelay = currentLevel.gameState.getAnimationTime();
			  updateTurn = currentLevel.gameState.isTurnEnd();
			  setEndGameState(currentLevel.gameState.getGameEndState());
			  powerBar.color = currentLevel.gameState.getPowerBarColor();
			  userText.setText(currentLevel.gameState.getMessage());
			  setHeartHighlight(currentLevel.gameState.isHealthHighlighted());
		  
			  if(currentLevel.gameState.primitiveGridHasPriority() && currentLevel.primitiveGrid != null){
				  currentLevel.primitiveGrid.synkObjectSet(currentLevel.objectGrid);
			  }
		  }
		  if(currentLevel.light != null){
			  player.setCollumn(currentLevel.light.getPosition());
			  player.getLight().updateBeamSizes(enemies);
			  player.setHurtState(currentLevel.light.getHurtState());
			  if(currentLevel.light.getLightType() == lightType.WIDE){
				  player.getLight().makeWide();
			  }
			  else if(currentLevel.light.getLightType() == lightType.LASER){
				  player.getLight().makeLaser();
			  }
			  else{
				  player.getLight().makeReveal();
			  }
			  player.turnLightOnorOff(currentLevel.light.isLightActive());
			  player.update();
		  }
		  if(currentLevel.objectGrid != null){
			  currentLevel.objectGrid.synkPrimitiveSet(currentLevel.primitiveGrid);
		  }
	  }
  }
  // Allows changing of the EndState for the game.
  // If a win or lose state is passed in, it will activate
  // the appropriate end game screen.
  private void setEndGameState(EndState gameState)
  {
	  if(gameState == EndState.WIN)
	  {
		  gameWin();
	  }
	  else if(gameState == EndState.LOSE)
	  {
		  gameLose();
	  }
	  state = gameState;
  }
/**
   * Draws objects
   */
  public void draw(Graphics gfx) {

	if(active != updateState.MAINMENUE && state == EndState.CONTINUE){
	    BaseCode.resources.setDrawingColor(Color.BLACK);
	    BaseCode.resources.drawLine(0.0f, FALLDOWN_POSITION, world.getWidth(), FALLDOWN_POSITION);
	    
	    background.draw();
	    
		enemies.drawUnselected();
		
		player.draw();
		player.getLight().draw();
		powerBar.draw();  
		typeSelectedText.draw(); 
		userText.draw();  
		
		if(!mediumButton.isSparcling()){
    		mediumButton.draw();
    	}
    	if(!wideButton.isSparcling()){
    		wideButton.draw();
    	}
    	if(!laserButton.isSparcling()){
    		laserButton.draw();
    	}
    	if(!healthHighlighted){
    		for(int loop = 0; loop < hearts.size(); loop++){
    			if(hearts.get(loop) != null){
    				hearts.get(loop).draw();
    			}
    		}
    	}
	}

    
//    if(active != updateState.MAINMENUE){
//    	enemies.drawSelected();
//    	
//    	if(mediumButton.isSparcling()){
//    		mediumButton.draw();
//    	}
//    	if(wideButton.isSparcling()){
//    		wideButton.draw();
//    	}
//    	if(laserButton.isSparcling()){
//    		laserButton.draw();
//    	}
//    	if(healthHighlighted){
//    		for(int loop = 0; loop < hearts.size(); loop++){
//    			if(hearts.get(loop) != null){
//    				hearts.get(loop).draw();
//    			}
//    		}
//    	}
//    }
    
    
    mainMenue.draw();
    transitionMenue.draw();
    escapeMenue.draw();
    
    super.draw(gfx);
    
  }
public void transitionIntoCredits() 
{
	setUpdateState(updateState.CREDITS);	
}

public void setUserTextVisiblity(boolean isVisible)
{
	userText.visible = isVisible;
}
public boolean getUserTextVisibility()
{
	return userText.visible;
}
public void setScoreTextVisiblity(boolean isVisible)
{
	typeSelectedText.visible = isVisible;
}
public boolean getScoreTextVisibility()
{
	return typeSelectedText.visible;
}


/*
 * Terminates the application.
 */
public void terminateProgram()
{
	window.close();
}
}
