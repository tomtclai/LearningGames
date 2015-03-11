using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MenueSystem;
using CustomWindower.CoreEngine;
using CustomWindower.Driver;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Windows.Forms;
using GhostFinder.Interface;
using System.Windows.Input;


namespace GhostFinder.GhostEngine{
    public class GhostFinder : SingleWindowUpdater{
	
	    /// <summary>
	    ///  Will load images and other recourses required for the game
	    /// </summary>
	    /// <param name="resources"></param>
        /// <remarks>Note this function is called automatically once the game has started. and should not b called manualy</remarks>
	    public static void preLoadResources(ResourceLibrary resources){
              Life.preLoadResources(resources);
		      BaseEnemy.preLoadResources(resources);
		      Frankenstein.preLoadResources(resources);
		      Cat.preLoadResources(resources);
		      Ghost.preLoadResources(resources);
              Player.preLoadResources(resources);
		      LightBeam.preLoadResources(resources);
		      Mummy.preLoadResources(resources);
		      Pumpkin.preLoadResources(resources);
		      Spider.preLoadResources(resources);
		      Vampire.preLoadResources(resources);
		      Zombie.preLoadResources(resources);
		      LightButton.preLoadResources(resources);
              EnemySoundManager.preLoadResources(resources);
	      }

        private Sprite buttonDown = null;
        private Sprite buttonUp = null;
        private Sprite buttonMouseOver = null;
        private Sprite Logo = null;
        private LoadableFont defaultFont = null;
        private Sound backGroundMusic = null;
        private Sound loseHeart = null;

      protected Player player = null;
      public static EnemySet enemies = null;
      protected int animationDelay = 30;
      protected float powerFillRate = 0.25f;
      protected int animationPuaseTimer = 0;

      protected readonly float FALLOFF_POSITION = 75.0f;
      protected readonly float FALLDOWN_POSITION = 10.0f;

      protected LightButton laserButton = null;
      protected LightButton mediumButton = null;
      protected LightButton wideButton = null;
  
      protected PowerBar powerBar = null;
  
      private bool updateTurn = true;

      public enum updateState {INGAME, MAINMENUE, ESC, TRANSITION};
  
      protected GhostFinderInterface currentLevel = null;
      private GameState.EndState state = GameState.EndState.CONTINUE;
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

      private Queue<GhostFinderInterface> tempList = new Queue<GhostFinderInterface>();
      private Queue<String> tempNameList = new Queue<String>();
      private Queue<String> tempWinList = new Queue<String>();
      private Queue<String> tempLoseList = new Queue<String>();


      protected List<Life> hearts = new List<Life>();
      protected bool healthHighlighted = false;

      /// <summary>
      /// Will Start the program. Note 
      /// </summary>
      public void start() {
          SingleWindow.start(this, 800, 480);
      }
      /// <summary>
      /// Will add the given GhostFinderInterface as a Level to come after all of the previous levels.
      /// Note this Function will only function. prior to GameWindow.setRunner() being called. calling afterwards will have no affect. 
      /// </summary>
      /// <param name="nextLevel">The Level to Be added</param>
      /// <param name="name">the Level's name</param>
      public void addLevel(GhostFinderInterface nextLevel, String name){
	      addLevel(nextLevel, name, null, null);
      }
      /// <summary>
      /// Will add the given GhostFinderInterface as a Level to come after all of the previous levels
      /// Note this Function will only function. prior to GameWindow.setRunner() being called. calling afterwards will have no affect.
      /// </summary>
      /// <param name="nextLevel">The Level to Be added</param>
      /// <param name="name">the Level's name</param>
      /// <param name="WinMessage"></param>
      /// <param name="LoseMessage"></param>
      public void addLevel(GhostFinderInterface nextLevel, string name, string WinMessage, string LoseMessage){
	      if(tempList != null){
		      tempList.Enqueue(nextLevel);
		      //name
		      if(name != null){
                  tempNameList.Enqueue(name);
		      }
		      else{
                  tempNameList.Enqueue("Level " + tempNameList.Count());
		      }
		      //Win
		      if(WinMessage != null){
                  tempWinList.Enqueue(WinMessage);
		      }
		      else{
                  tempWinList.Enqueue("You Win");
		      }
		      //Lose
		      if(LoseMessage != null){
                  tempLoseList.Enqueue(LoseMessage);
		      }
		      else{
                  tempLoseList.Enqueue("You Lose");
		      }
		      //Setting first Level
		      if(currentLevel == null){
			      currentLevel = nextLevel;
		      }
	      }
      }
      /// <summary>
      /// Will initialize GhostFinder Menu and Gameplay Systems
      /// </summary>
      /// <param name="ParentForm"></param>
      /// <param name="hostCamera"></param>
      /// <remarks>Note this Function is called automatically by start. and should not be called manually</remarks>
      public override void initialize(Form ParentForm, Camera hostCamera) {
          hostCamera.backGroundColor = Color.FromArgb(90,90,90);
          //loading menue art
          buttonDown = new Sprite("GhostLight/GhostLight/resources/menuArt/ButtonDown.png");
          buttonDown.loadResource();
          base.getLocalLibrary().addResource(buttonDown);

          buttonUp = new Sprite("GhostLight/GhostLight/resources/menuArt/ButtonIdle.png");
          buttonUp.loadResource();
          base.getLocalLibrary().addResource(buttonUp);

          buttonMouseOver = new Sprite("GhostLight/GhostLight/resources/menuArt/ButtonIdleMouseOver.png");
          buttonMouseOver.loadResource();
          base.getLocalLibrary().addResource(buttonMouseOver);

          Logo = new Sprite("GhostLight/GhostLight/resources/Logo Clear.png");
          Logo.loadResource();
          base.getLocalLibrary().addResource(Logo);

          defaultFont = new LoadableFont("Arial", 2.2f, FontStyle.Regular);
          defaultFont.loadResource();
          base.getLocalLibrary().addResource(defaultFont);

          backGroundMusic = new Sound("GhostLight/GhostLight/resources/Sounds/ghotstlight_background_music.wav");
          backGroundMusic.loadResource();
          base.getLocalLibrary().addResource(backGroundMusic);
          backGroundMusic.setVolume(1);
          backGroundMusic.playSound();

          loseHeart = new Sound("GhostLight/GhostLight/resources/Sounds/ghostlight_lose_life.wav");
          loseHeart.setNumberOfConcurrentSounds(3);
          loseHeart.loadResource();
          loseHeart.setVolume(0.3);
          base.getLocalLibrary().addResource(loseHeart);


          //Scaleing world such that the World Width is 100 
          hostCamera.setCameraScaleX(((float)hostCamera.Width) / 100f);
          hostCamera.setCameraScaleY(hostCamera.getCameraScaleX());
          hostCamera.setWorldCenterX(hostCamera.worldWidth/2);
          hostCamera.setWorldCenterY(hostCamera.worldHeight/2);

          //initializing BaseCode
          base.initialize(ParentForm, hostCamera);
          BaseCode.worldWidth = hostCamera.worldWidth;
          BaseCode.worldHeight = hostCamera.worldHeight;
          BaseCode.activeDrawSet = getActiveDrawSet();
          BaseCode.activeLibrary = base.getLocalLibrary();
          BaseCode.activeMouse = base.getActiveMouse();
          BaseCode.activeKeyboard = base.getActiveKeyBoard();
          getActiveDrawSet().setAvailablePriorities(10);

          preLoadResources(base.getLocalLibrary());
          initializeMenues();
          initializeGamePlay();
          setUpdateState(updateState.MAINMENUE);
      }
      /**
       * Called when the currentLevel has indicated that the user has won the game
       */
      protected void gameWin() {
	    typeSelectedText.text = ("Score: "+ player.getScore());
	    enemies.moveAllEnemiestoVerticalPosition(-2);
        state = GameState.EndState.WIN;
      }
      /**
       * Called when the currentLevel has indicated that the user has lost the game
       */
      protected void gameLose() {
	    typeSelectedText.text = ("Score: "+ player.getScore());
	    enemies.moveAllEnemiestoVerticalPosition(enemies.getNumberOfRows() + 3);
        state = GameState.EndState.LOSE;
      }
      /**
       * Will change the update state to the given update state
       * This will Determine what objects receive updates and which objects are visible
       * @param newState
       */
      internal void setUpdateState(updateState newState){
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
			  
			      mainMenue.setState(MenueSystem.Panel.PanelState.INACTIVE, true);
			      mainMenue.setVisibility(false, true);
                  escapeMenue.setState(MenueSystem.Panel.PanelState.INACTIVE, true);
			      escapeMenue.setVisibility(false, true);
                  transitionMenue.setState(MenueSystem.Panel.PanelState.INACTIVE, true);
			      transitionMenue.setVisibility(false, true);
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
                  EnemySoundManager.clear();
			  
			      mainMenue.setVisibility(true, true);
                  mainMenue.setState(MenueSystem.Panel.PanelState.IDLE, true);

                  escapeMenue.setState(MenueSystem.Panel.PanelState.INACTIVE, true);
			      escapeMenue.setVisibility(false, true);
                  transitionMenue.setState(MenueSystem.Panel.PanelState.INACTIVE, true);
			      transitionMenue.setVisibility(false, true);
			      active = newState;
                  setHeartCount(0);
		      }
		      //Transition Menue
		      else if(newState == updateState.TRANSITION){
			      if(state == GameState.EndState.LOSE || state == GameState.EndState.WIN){
				      transitionMenue.setVisibility(true, true);
                      transitionMenue.setState(MenueSystem.Panel.PanelState.IDLE, true);

                      mainMenue.setState(MenueSystem.Panel.PanelState.INACTIVE, true);
				      mainMenue.setVisibility(false, true);
                      escapeMenue.setState(MenueSystem.Panel.PanelState.INACTIVE, true);
				      escapeMenue.setVisibility(false, true);
				      active = newState;

				      if(state == GameState.EndState.LOSE){
                          transitionMenue.getPanel(1).setState(MenueSystem.Panel.PanelState.INACTIVE, true);
                          ((MenueSystem.Label)transitionMenue.getPanel(2)).setText(((LevelButton)mainMenue.getPanel(2).getPanel(0).getPanel(LevelNumber)).loseMessage);
				      }
				      else{
					      if(((LevelButton)transitionMenue.getPanel(1)).gameLevel != null){
                              transitionMenue.getPanel(1).setState(MenueSystem.Panel.PanelState.IDLE, true); 
					      }
					      else{
                              transitionMenue.getPanel(1).setState(MenueSystem.Panel.PanelState.INACTIVE, true);
					      }
                          ((MenueSystem.Label)transitionMenue.getPanel(2)).setText(((LevelButton)mainMenue.getPanel(2).getPanel(0).getPanel(LevelNumber)).winMessage);
				      }
			      }
			      else{
				      setUpdateState(updateState.ESC);
			      }
		      }
		      //Escape Menu
		      else if(newState == updateState.ESC){
			      escapeMenue.setVisibility(true, true);
                  escapeMenue.setState(MenueSystem.Panel.PanelState.IDLE, true);

                  mainMenue.setState(MenueSystem.Panel.PanelState.INACTIVE, true);
			      mainMenue.setVisibility(false, true);
                  transitionMenue.setState(MenueSystem.Panel.PanelState.INACTIVE, true);
			      transitionMenue.setVisibility(false, true);
			      active = newState;
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
	      mainMenue.getBackGround().setCenter(BaseCode.worldWidth / 2, BaseCode.worldHeight / 2);
          mainMenue.getBackGround().setSize(BaseCode.worldWidth / 4f, BaseCode.worldHeight/ 3f);
          mainMenue.getBackGround().rectangleVisible = false;
	      mainMenue.setIdleImage(buttonUp);

	      //Play Button
	      LevelButton play = new LevelButton();
	      play.setText("Play");
          play.getFont().targetFont = defaultFont;
          play.getFont().textColor = Color.Black;
          play.getBackGround().rectangleVisible = false;
	      play.setLevel(this, currentLevel, LevelNumber);
	      mainMenue.addButton(play);
	  
	      //Tidal
	      MenueSystem.Panel tital = new MenueSystem.Panel();
          tital.getBackGround().setCenter(BaseCode.worldWidth / 2, BaseCode.worldHeight * 0.25f);
          tital.getBackGround().setSize(BaseCode.worldWidth / 2, BaseCode.worldHeight/2);
	      tital.setIdleImage(Logo);
          tital.getBackGround().rectangleVisible = false;
	      mainMenue.addPanel(tital);
	  
	      //choseLevel
          MenueSystem.Button choseLevel = new MenueSystem.Button();
          choseLevel.getBackGround().rectangleVisible = false;
	      choseLevel.setText("Choose Level");
          choseLevel.getFont().targetFont = defaultFont;
          choseLevel.getFont().textColor = Color.Black;
	      choseLevel.selectable = true;
	      choseLevel.setIdleImage(buttonUp);
	      choseLevel.setButtonDownImage(buttonDown);
	      choseLevel.setMouseOverIdleImage(buttonMouseOver);
          choseLevel.setMouseOverSelectImage(buttonMouseOver);
          choseLevel.setSelectImage(buttonUp);
          choseLevel.getBackGround().rectangleVisible = false;
	      mainMenue.addButton(choseLevel);		  
	  
	      //LevelMenue
          MenueSystem.Menue LevelMenue = new MenueSystem.Menue();
	      LevelMenue = new Menue();
          LevelMenue.getBackGround().rectangleVisible = false;
          LevelMenue.getBackGround().setCenter(BaseCode.worldWidth * 0.75f, BaseCode.worldHeight / 2);
          LevelMenue.getBackGround().setSize(BaseCode.worldWidth / 4, BaseCode.worldHeight * 0.8f);
          LevelMenue.setIdleImage(buttonUp);
	      choseLevel.addPanel(LevelMenue);
	      //Adding Levels
	      if(tempList != null){
		      while(tempList.Count > 0){
			      LevelButton  newLevel = new LevelButton();
                  newLevel.getFont().targetFont = defaultFont;
			      newLevel.setText(tempNameList.Dequeue());
                  newLevel.getBackGround().rectangleVisible = false;
                  newLevel.getFont().textColor = Color.Black;
                  newLevel.setLevel(this, tempList.Dequeue());
                  newLevel.winMessage = tempWinList.Dequeue();
                  newLevel.loseMessage = tempLoseList.Dequeue();
			      LevelMenue.addButton(newLevel);
		      }
		      LevelMenue.setButtonSizesPositions(true);
		      tempList = null;
		      tempNameList = null;
		      tempWinList = null;
		      tempLoseList = null;
	  
	      }
	      mainMenue.setButtonSizesPositions(true);
	      mainMenue.setState(MenueSystem.Panel.PanelState.IDLE, true);
          BaseCode.activeDrawSet.addToDrawSet(mainMenue);
          mainMenue.setPriority(9);
	  
	      //Transition Menu
	      transitionMenue = new Menue();
          transitionMenue.getBackGround().setCenter(BaseCode.worldWidth / 2, BaseCode.worldHeight / 2);
          transitionMenue.getBackGround().setSize(BaseCode.worldWidth / 4, BaseCode.worldHeight / 4);
          transitionMenue.getBackGround().rectangleVisible = false;
	      transitionMenue.setIdleImage(buttonUp);
	      //replay Level
	      LevelButton replay = new LevelButton();
	      replay.setText("Replay Level");
          replay.getFont().targetFont = defaultFont;
	      replay.getFont().textColor = Color.Black;
          replay.getBackGround().rectangleVisible = false;
	      transitionMenue.addButton(replay);
	      //Next Level
	      LevelButton next = new LevelButton();
          next.getFont().targetFont = defaultFont;
	      next.setText("Next Level");
          next.getFont().textColor = Color.Black;
          next.getBackGround().rectangleVisible = false;
	      transitionMenue.addButton(next);
	  
	      MenueSystem.Label WinLose = new MenueSystem.Label();
          WinLose.getFont().textPosition.X = BaseCode.worldWidth * 0.4f;
          WinLose.getFont().textPosition.Y = BaseCode.worldHeight * 0.3f;
          WinLose.getFont().targetFont = defaultFont;
	      WinLose.setText("");
	      WinLose.getFont().textColor = Color.Black;
          WinLose.getBackGround().rectangleVisible = false;
	      transitionMenue.addPanel(WinLose);
	  
	      transitionMenue.setButtonSizesPositions(true);
          transitionMenue.setState(MenueSystem.Panel.PanelState.IDLE, true);

          transitionMenue.addToDrawSet(BaseCode.activeDrawSet);
          transitionMenue.setPriority(9);
	  
	      //Escape Menu
          escapeMenue = new MenueSystem.Menue();
          escapeMenue.getBackGround().setCenter(BaseCode.worldWidth / 2, BaseCode.worldHeight / 2);
          escapeMenue.getBackGround().setSize(BaseCode.worldWidth / 4, BaseCode.worldHeight / 4);
          escapeMenue.getBackGround().rectangleVisible = false;
	      escapeMenue.setIdleImage(buttonUp);
	      //Resume Game
	      ChangeGameStateButton resume = new ChangeGameStateButton();
          resume.getFont().targetFont = defaultFont;
	      resume.setText("Resume Game");
	      resume.getFont().textColor = Color.Black;
	      resume.setTargetState(this, updateState.INGAME);
          resume.getBackGround().rectangleVisible = false;
	      escapeMenue.addButton(resume);
	      //To Main Menue
	      ChangeGameStateButton toMain = new ChangeGameStateButton();
          toMain.getFont().targetFont = defaultFont;
	      toMain.setText("Main Menu");
          toMain.getFont().textColor = Color.Black;
	      toMain.setTargetState(this, updateState.MAINMENUE);
          toMain.getBackGround().rectangleVisible = false;
	      escapeMenue.addButton(toMain);
	  
	      escapeMenue.setButtonSizesPositions(true);
	      escapeMenue.setState(MenueSystem.Panel.PanelState.IDLE, true);
          escapeMenue.addToDrawSet(BaseCode.activeDrawSet);
          escapeMenue.setPriority(9);
      }
      /**
       * Will initialize the objects used in game play
       * This includes the player, Light Buttons Power Bar, enemySet, and various text Objects
       */
      protected void initializeGamePlay(){

	      //enemySet
	      enemies = new EnemySet(base.getLocalLibrary(), BaseCode.worldWidth, BaseCode.worldHeight);
	      enemies.setSetPadding(8f, 15f,5f, 9f);
	      //player
	      player = new Player();
	      player.fillColor = Color.Blue;

	      //power bar
	      powerBar = new PowerBar();

	      //score text
	      typeSelectedText = new Text();
          BaseCode.activeDrawSet.addToDrawSet(typeSelectedText);
          typeSelectedText.setPriority(9);
	      typeSelectedText.textColor = Color.White;
	      typeSelectedText.targetFont = defaultFont;

	      //user text
	      userText = new Text();
          BaseCode.activeDrawSet.addToDrawSet(userText);
          userText.setPriority(9);
          userText.textColor = Color.White;
          userText.targetFont = defaultFont;

	      //light type indicator buttons
	      mediumButton = new LightButton();
	      mediumButton.setResponsibility(player.getLight(), LightBeam.BeamType.REVEAL);
          mediumButton.removeFromDrawSet();
	  
	      wideButton = new LightButton();
	      wideButton.setResponsibility(player.getLight(), LightBeam.BeamType.WIDE);
          wideButton.removeFromDrawSet();
	  
	      laserButton = new LightButton();
	      laserButton.setResponsibility(player.getLight(), LightBeam.BeamType.LASER);
          laserButton.removeFromDrawSet();
      }
      /**
       * Conducts final initialization of gameplay objects including the current level
       * Required to begine a game
       */
      internal void newGame(GhostFinderInterface userCode, int LevelNum) {
          //Background Music
          backGroundMusic.setVolume(0.1);

        this.setUpdateState(updateState.INGAME);
        currentLevel = userCode;
        LevelNumber = LevelNum;
    
        ((LevelButton)transitionMenue.getPanel(0)).setLevel(this, currentLevel, LevelNum);
        if(mainMenue.getPanel(2).getPanel(0).getPanel(LevelNum+1) != null){
    	    ((LevelButton)transitionMenue.getPanel(1)).setLevel(this, ((LevelButton)mainMenue.getPanel(2).getPanel(0).getPanel(LevelNumber+1)).gameLevel, LevelNumber+1);
        }
        else{
    	    ((LevelButton)transitionMenue.getPanel(1)).setLevel(this, null);
        }
        ((LevelButton)mainMenue.getPanel(0)).setLevel(this, userCode, LevelNum);
        //endGame();

        setHeartHighlight(false);
        setHeartCount(4);
    
        //Enemies
        enemies.RemoveAllEnemies();
    
        //Player
        player.setWidth(player.getHeight() / 2);
        player.setCenterY(BaseCode.worldHeight - (player.getHeight()/2));
        player.setCollumn(1);
        player.setEnemySet(enemies);
        player.visible = true;
        player.getLight().visible = true;
        BaseCode.activeDrawSet.addToDrawSet(player);
        player.setPriority(7);
    
        //powerBar
        powerBar.setSize(4.0f, 20.0f);
        powerBar.setCenter((BaseCode.worldWidth - (powerBar.getWidth()) / 2), (powerBar.getHeight() / 2) + 5f);
        powerBar.setMaxScore(10.0f);
        powerBar.setScore(0.0f);
        powerBar.setToVertical();
        powerBar.setVisibility(true);
        BaseCode.activeDrawSet.addToDrawSet(powerBar);
        powerBar.setPriority(7);

        //Animation timer
        animationDelay = 30;
    
        //typeSelectedText
        typeSelectedText.textPosition.X = 1.0f;
        //typeSelectedText.textPosition.Y = 3.0f;
        typeSelectedText.text = ("Score: "+ player.getScore());
        typeSelectedText.visible = true;
        typeSelectedText.setPriority(7);

        userText.textPosition.X = 20f;
        //userText.textPosition.Y = 3.0f;
        userText.text = ("");
        userText.visible = true;   
    
        //buttons
        //Reveal
        mediumButton.getBackGround().setSize(4f, 4f);
        mediumButton.getBackGround().setCenter(BaseCode.worldWidth - (mediumButton.getBackGround().getWidth() / 2), (BaseCode.worldHeight / 2));
        mediumButton.setVisibility(true, true);
        mediumButton.setState(MenueSystem.Panel.PanelState.IDLE, true);
        mediumButton.setSparcleVisiblity(false);
        mediumButton.setPriority(7);
        BaseCode.activeDrawSet.addToDrawSet(mediumButton);
    
        //Wide
        wideButton.getBackGround().setSize(4f, 4f);
        wideButton.getBackGround().setCenter(BaseCode.worldWidth - (mediumButton.getBackGround().getWidth() / 2), (BaseCode.worldHeight / 2) + 5f);
        wideButton.setVisibility(true, true);
        wideButton.setState(MenueSystem.Panel.PanelState.IDLE, true);
        wideButton.setSparcleVisiblity(false);
        wideButton.setPriority(7);
        BaseCode.activeDrawSet.addToDrawSet(wideButton);

        //Laser
        laserButton.getBackGround().setSize(4f, 4f);
        laserButton.getBackGround().setCenter(BaseCode.worldWidth - (mediumButton.getBackGround().getWidth() / 2), (BaseCode.worldHeight / 2) + 10f);  
        laserButton.setVisibility(true, true);
        laserButton.setState(MenueSystem.Panel.PanelState.IDLE, true);
        laserButton.setSparcleVisiblity(false);
        laserButton.setPriority(7);
        BaseCode.activeDrawSet.addToDrawSet(laserButton);
    
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
    		    animationPuaseTimer = animationDelay;
    		    enemies.setEnemyAnimations(0);
    	    }
        }
        state = GameState.EndState.CONTINUE;   
      }
      /**
       * Will set the number of hearts drawn
       * @param num
       */
      protected void setHeartCount(int num) {
	      if(num != hearts.Count){
            if(num < hearts.Count && this.active == updateState.INGAME){
                loseHeart.playSound();
            }
		    if(num != 0){
		        // Remove old hearts
                foreach (Life heart in hearts){
                    heart.removeFromDrawSet();
                }
                hearts.Clear();
		        // Add new hearts
		        for(int i = 0; i < num; i++) {
		          Life heart = new Life();
                  BaseCode.activeDrawSet.addToDrawSet(heart);
                  heart.setPriority(7);
                  heart.setCenter(BaseCode.worldWidth - ((i + 1) * heart.getWidth()) -
                      (i * 1.25f), (heart.getHeight() * 0.5f) + 1.0f);
		          hearts.Add(heart);
	    	      heart.Sparcle = healthHighlighted;
		        }
		    }
		    else{
			    while(hearts.Count > 0) {
				    removeHeart();
			    }
		    }
	      }
      }
      /**
       * Will remove a heart from the set of hearts drawn
       */
      protected void removeHeart() {
        if(hearts.Count > 0) {
            Life Heart = hearts[hearts.Count - 1];
            Heart.removeFromDrawSet();
            hearts.RemoveAt(hearts.Count - 1);

        }
      }
      /**
       * Will add a number of hearts from the set of hearts drawn
       * @param increment the number of hearts to be added
       */
      protected void incrementHeartCount(int increment){
	      setHeartCount(increment + hearts.Count);  
      }
      /**
       * Will return the number of hearts drawn
       * @return
       */
      protected int getLivesRemaining(){
        return hearts.Count;
      }
      /**
       * will highlight or unhighlight the hearst drawn
       * @param highlight
       */
      protected void setHeartHighlight(bool highlight){
	      if(hearts.Count > 0 && healthHighlighted != highlight) {
		      healthHighlighted = highlight;
              foreach (Life heart in hearts) {
                  heart.Sparcle = highlight;
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
      /// <summary>
      /// Will update the objects actve detrmined by the updates state
      /// </summary>
      /// <param name="updateInfo"></param>
      /// <remarks>Note, this function is called automatically while the game is in progress and should not ba called manually</remarks>
      public override void update(Object updateInfo){

        //forcing Background Music to Loop
        if(!backGroundMusic.isPlaying()){
            backGroundMusic.playSound();
        }

        enemies.update(animationDelay);
        //In Game
        if(this.active == updateState.INGAME){
	        if(this.state == GameState.EndState.CONTINUE){
	    	    updateContinue();
	        }
        }
        //In Main Menue  
        else if(this.active == updateState.MAINMENUE ){
    	    mainMenue.autoUpdateState(BaseCode.activeMouse);
        }
        //In Transition Menue
        else if(this.active == updateState.TRANSITION ){
            transitionMenue.autoUpdateState(BaseCode.activeMouse);
        }
        //In Escape Menue
        else if(this.active == updateState.ESC ){
            escapeMenue.autoUpdateState(BaseCode.activeMouse);
        }
        base.update(updateInfo);
      }
    /**
     * Updates when state = EndState.CONTINUE;
     */
      protected void updateContinue(){
          typeSelectedText.text = ("Score: "+ player.getScore());

          EnemySoundManager.update();
      
          //updating turn 
          if(this.updateTurn){
    	      changeState(enemies.updateTurn());
    	      updateTurn = false;
          }
          player.update();
          player.selectTargetableEnemies();
          //No Animation currently playing user receives update
          if(animationPuaseTimer <= 0){
    	      if(currentLevel != null){
    		      currentLevel.objectGrid.setObjectGrid(enemies.getGrid());
    		      synkPlayerStateWithTrueState();
    		      currentLevel.update();
    		      synkTrueStateWithPlayerState();
    		      StateChange change = enemies.synkGrid(currentLevel.objectGrid.getObjectGrid(), animationDelay);
    		      changeState(change);
    		      if(change != null && change.puaseForAnimation){
    			      animationPuaseTimer = animationDelay;
    			      enemies.setEnemyAnimations(0); 
    		      }
    	      }
    	      if(state == GameState.EndState.LOSE){
    		      if(currentLevel != null){
    			      currentLevel.end();
    		      }
    		      gameLose();
    		      setUpdateState(updateState.TRANSITION);
    	      }
    	      else if(state == GameState.EndState.WIN){
    		      if(currentLevel != null){
    			      currentLevel.end();
    		      }
    		      gameWin();
    		      setUpdateState(updateState.TRANSITION);
    	      }
          }
          //Animation is playing user does not receive update
          else{
    	      animationPuaseTimer--;
          }
          //Esc Menue
          if (BaseCode.activeKeyboard.isKeyDown(Keys.Escape) && active != updateState.ESC) {
    	      setUpdateState(updateState.ESC);
          }
          //Close
          if (BaseCode.activeKeyboard.isKeyDown(Keys.F5)) {
              base.closeWindow();
    	      return;
          }
      }
      /**
       * Will update the health, power and score based on a given StateChange object
       * @param changeState
       * @return
       */
      protected bool changeState(StateChange changeState){
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
		      currentLevel.gameState.setHealth(hearts.Count);
		      currentLevel.gameState.setLightPower(powerBar.getPercent());
		      currentLevel.gameState.setScore(player.getScore());
		      currentLevel.gameState.setAnimationTime(animationDelay);
		      currentLevel.gameState.markTurnEnd(updateTurn);
		      currentLevel.gameState.setGameEnd(state);
		      currentLevel.gameState.setPowerBarColor(powerBar.fillColor);
		      currentLevel.gameState.setMessage(userText.text);
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
		      mediumButton.autoUpdateState(BaseCode.activeMouse);
              wideButton.autoUpdateState(BaseCode.activeMouse);
              laserButton.autoUpdateState(BaseCode.activeMouse);	
		  
		      if(currentLevel.mouse == null){
			      currentLevel.mouse = new MouseState();
		      }
              currentLevel.keyboard = base.getActiveKeyBoard();
              ((MouseInfo)currentLevel.mouse).mouse = BaseCode.activeMouse;
		      ((MouseInfo)currentLevel.mouse).screenWidth = base.getDrawWindow().Width;
              ((MouseInfo)currentLevel.mouse).screenHeight = base.getDrawWindow().Height;
		      if(currentLevel.clickableButtons == null){
			      currentLevel.clickableButtons = new OnScreenButtons();
		      }
              ((ButtonAccess)currentLevel.clickableButtons).mouse = BaseCode.activeMouse;
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
			      state = currentLevel.gameState.getGameEndState();
			      powerBar.fillColor = currentLevel.gameState.getPowerBarColor();
			      userText.text = (currentLevel.gameState.getMessage());
			      setHeartHighlight(currentLevel.gameState.isHealthHighlighted());
		  
			      if(currentLevel.gameState.primitiveGridHasPriority() && currentLevel.primitiveGrid != null){
				      currentLevel.primitiveGrid.synkObjectSet(currentLevel.objectGrid);
			      }
		      }
		      if(currentLevel.light != null){
			      player.setCollumn(currentLevel.light.getPosition());
			      player.getLight().updateBeamSizes(enemies);
			      if(currentLevel.light.getLightType() == FlashLight.lightType.WIDE){
				      player.getLight().makeWide();
			      }
			      else if(currentLevel.light.getLightType() == FlashLight.lightType.LASER){
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
    }
}
