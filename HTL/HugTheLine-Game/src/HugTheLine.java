import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import Engine.GameObject;
import Engine.Text;
import Engine.Vector2;
import TowerDefense.Button;
import TowerDefense.CooldownTimer;
import TowerDefense.Projectile;
import TowerDefense.TowerMedic;
import TowerDefense.TowerSpeedy;
import TowerDefense.Walker;
import TowerDefense.Tower;
import TowerDefense.HTL;
import TowerDefense.Path;
import TowerDefense.Tile;
import TowerDefense.WalkerBasic;
import TowerDefense.WalkerQuick;
/**
 * This game is Hug The Line.
 * We at the DFL spent 3 quarters designing, implementing, and iterating on it.
 * We're really proud of what we've accomplished, first and foremost being
 * a game that is cute and fun.
 * We're also proud of most of the code we've written.
 * We've tried to focus on readability above all else, but
 * sometimes had to compromise on this because time was in
 * short supply.  Hopefully it's clear enough that you can
 * get what you need from it.
 * If not, Kelvin knows how to get you in touch with us. :)
 * 
 * @author Rachel Horton
 * @author Branden Drake
 *
 */
public class HugTheLine extends HTL
{	
	public static final boolean SHOW_DEBUG_TEXT = false;		//set to false when not testing
	public static final boolean DEBUG_KEYS_ENABLED = true;
	private boolean debugWalkerSpeedBoostOn = false;
	private boolean debugWalkerDamageBoostOn = false;
	
	
	private Tower selectedTower = null;
	
	private float walkerSpeed = -1;
	private int movesRemaining = -1;
	private int towerMedicRemaining = -1;
	private int towerSpeedyRemaining = -1;
	private float scoreToWin = -1;
	
	private float towerMedicCastCooldownTime = -1;
	private float towerSpeedyCastCooldownTime = -1;
	private float towerSpeedyCastInitialDelay = -1;	// the amount of time after medic cooldown starts
													// before the speedy cooldown should start
													// some people might call this an offset
	
	
	private float towerMedicHealthAdjust = -1;
	private float towerSpeedyBoostDuration = -1;
	private float towerSpeedBoostMultiplier = -1;
	
	
	// game stats
	private int walkersDead = 0;
	private int walkersSavedBasic = 0;
	private int walkersSavedQuick = 0;
	private float healthSaved = 0;
	private float score = 0;
	
		
	
	
	
	// debug UI
	private Text textUnspawned = null;
	private Text textDeaths = null;
	private Text textTotalHP = null;
	private Text textWinMessage = null;
	private Text textCurrentWaveNumber = null;	
	private static final float TEMP_UI_OFFSET = -2;
	
	
	
	public static final String IMAGE_FONT = "art/Fonts/helsinki.ttf";
	
	
	// universal tower cooldowns
	private CooldownTimer towerMedicCooldown = new CooldownTimer(0);
	private CooldownTimer towerSpeedyCooldown = new CooldownTimer(0);
	private CooldownTimer towerSpeedyCooldownInitialDelay = null;
	private boolean towerSpeedyCooldownInitialDelayIsDone = false;
	
	
	
	
	
	
	// sound
	public static final String MUSIC_TITLE = "audio/Music/Misty Sunshine.mp3";
	public static final String MUSIC_BACKGROUND = "audio/Music/It's a Wonderful World.mp3";
	public static final String MUSIC_WIN = "audio/Music/Together we Survive.mp3";
	public static final String MUSIC_LOSE = "audio/Music/Sea of Sadness.mp3";
	
	////////////////////    UI stuff   //////////////
	
	public static final String IMAGE_TITLE_BACKGROUND = "art/Start Screen/HTL_StartScreen_Background.png";
	public static final String IMAGE_TITLE_BUTTON_PLAY = "art/Start Screen/HTL_StartScreen_ButtonPlay.png";
	public static final String IMAGE_TITLE_BUTTON_CREDITS = "art/Start Screen/HTL_StartScreen_ButtonCredits.png";
	
	public static final String IMAGE_LEVEL_INTRO_POP_UP = "art/UI/HTL_PopUp_Intro.png";
	public static final String IMAGE_LEVEL_INTRO_BUTTON_START = "art/UI/HTL_PopUp_ButtonPlay.png";
	
	public static final String IMAGE_WAVE_SIGN = "art/UI/HTL_InGame_UI_WaveSign.png";
	
	public static final String IMAGE_PAUSE_POP_UP = "art/UI/HTL_PopUp_Pause.png";
	public static final String IMAGE_PAUSE_BUTTON_RESUME = "art/UI/HTL_PopUp_ButtonResume.png";
	public static final String IMAGE_PAUSE_BUTTON_RESTART = "art/UI/HTL_PopUp_ButtonRestart.png";
	public static final String IMAGE_PAUSE_BUTTON_EXIT = "art/UI/HTL_PopUp_ButtonQuit.png";
	
	public static final String IMAGE_LOSE_POP_UP = "art/UI/HTL_PopUp_Lose.png";
	public static final String IMAGE_LOSE_BUTTON_RESTART = "art/UI/HTL_PopUp_ButtonRestart.png";
	public static final String IMAGE_LOSE_BUTTON_QUIT = "art/UI/HTL_PopUp_ButtonQuit.png";
	
	public static final String IMAGE_WIN_POP_UP = "art/UI/HTL_PopUp_Win.png";
	public static final String IMAGE_WIN_BUTTON_RESTART = "art/UI/HTL_PopUp_ButtonRestart.png";
	public static final String IMAGE_WIN_BUTTON_QUIT = "art/UI/HTL_PopUp_ButtonQuit.png";
	
	public static final String IMAGE_QUIT_CONFIRM_POP_UP = "art/UI/HTL_PopUp_Quit.png";
	public static final String IMAGE_QUIT_CONFIRM_BUTTON_YES = "art/UI/HTL_PopUp_ButtonYes.png";
	public static final String IMAGE_QUIT_CONFIRM_BUTTON_NO = "art/UI/HTL_PopUp_ButtonNo.png";
	
	public static final String IMAGE_RESTART_CONFIRM_POP_UP = "art/UI/HTL_PopUp_Restart.png";
	public static final String IMAGE_RESTART_CONFIRM_BUTTON_YES = "art/UI/HTL_PopUp_ButtonYes.png";
	public static final String IMAGE_RESTART_CONFIRM_BUTTON_NO = "art/UI/HTL_PopUp_ButtonNo.png";
	
	public static final String IMAGE_CREDITS_BACKGROUND = "art/Credits/HTL_CreditsScreen_Background.png";
	public static final String IMAGE_CREDITS_BUTTON_BACK = "art/Credits/HTL_CreditsScreen_ButtonBack.png";
	
	private GameObject creditsBackground = null;
	private GameObject creditsButtonBack = null;
	
	private GameObject restartConfirmPopUp = null;
	private GameObject restartConfirmButtonYes = null;
	private GameObject restartConfirmButtonNo = null;
	
	private GameObject quitConfirmPopUp = null;
	private GameObject quitConfirmButtonYes = null;
	private GameObject quitConfirmButtonNo = null;
	
	private GameObject winPopUp = null;
	private GameObject winButtonRestart = null;
	private GameObject winButtonQuit = null;
	
	private GameObject losePopUp = null;
	private GameObject loseButtonRestart = null;
	private GameObject loseButtonQuit = null;
	
	private GameObject pausePopUp = null;
	private GameObject pauseButtonResume = null;
	private GameObject pauseButtonRestart = null;
	private GameObject pauseButtonExit = null;
	
	private GameObject titleBackground = null;
	private GameObject titleButtonPlay = null;
	private GameObject titleButtonCredits = null;	
			
	private GameObject levelIntroPopUp = null;		
	private GameObject levelIntroButtonStart = null;
	
	private Button buttonMedic = null;
	private Button buttonSpeedy = null;
	
	private GameObject waveSign = null;
	
	private Text winScoreText = null;
	


	
	private enum gamePhase
	{
		TITLE, CREDITS, LEVEL_INTRO, GAMEPLAY,
		WIN, LOSE, PAUSE, RESTART_CONFIRM, QUIT_CONFIRM
	}
	
	private gamePhase currentGamePhase = gamePhase.TITLE;
	private gamePhase previousGamePhase = null;
	
	private boolean fullscreenMode = false;
	
	
	
	/**
	 * Constructor.
	 */
	public HugTheLine()
	{
		super();
	}
	
	
	
	
	/////////////////////////////////////////////////////
	//                                                 //
	//                                                 //
	//                                                 //
	//                 INITIALIZATION                  //
	//                                                 //
	//                                                 //
	//                                                 //
	/////////////////////////////////////////////////////
	
	
	
	
	
	
	/**
	 * Initializes everything the game needs to be ready, as well as kicking off
	 * the title screen.
	 */
	public void initializeWorld()
	{
		super.initializeWorld();
			
		preloadResources();	
		createDefaultPath();
		setBlockingTilesForSignAndHUD();
		
		initializeButtonsOnHUD();
		initializeMiscUI();
		
		initializeTitleScreenAssets();
		initializeLevelIntroAssets();
		initializePauseScreenAssets();
		initializeWinScreenAssets();
		initializeLoseScreenAssets();
		initializeRestartConfirmAssets();
		initializeQuitConfirmAssets();
		initializeCreditsAssets();
		
		enterPhaseTitle();	
	}
	
	
	
	/**
	 * Initializes assets for credit screen.
	 */
	private void initializeCreditsAssets()
	{
		if(creditsBackground == null)
		{
			creditsBackground = new GameObject(SCREEN_CENTER_X, SCREEN_CENTER_Y, SCREEN_WIDTH, SCREEN_HEIGHT);
			creditsBackground.setImage(IMAGE_CREDITS_BACKGROUND);
			creditsBackground.moveToDrawingLayer(phaseLayerCredits);
			
			float buttonWidth = gameUnitsFromPixels(92); 
			float buttonHeight = gameUnitsFromPixels(95);					
			creditsButtonBack = new GameObject(19f, 10f, buttonWidth, buttonHeight);
			creditsButtonBack.setImage(IMAGE_CREDITS_BUTTON_BACK);
			creditsButtonBack.moveToDrawingLayer(phaseLayerCredits);
		}
	}
	
	/**
	 * Initializes assets for Restart Confirmation screen.
	 */
	private void initializeRestartConfirmAssets()
	{
		if(restartConfirmPopUp == null)
		{
			float popUpWidth = gameUnitsFromPixels(430);
			float popUpHeight = gameUnitsFromPixels(312);
			restartConfirmPopUp = new GameObject(SCREEN_CENTER_X, SCREEN_CENTER_Y, popUpWidth, popUpHeight);
			restartConfirmPopUp.setImage(IMAGE_RESTART_CONFIRM_POP_UP);
			restartConfirmPopUp.moveToDrawingLayer(phaseLayerRestartConfirm);
			
			float buttonWidth = gameUnitsFromPixels(154); 
			float buttonHeight = gameUnitsFromPixels(85);		
			float xDif = buttonWidth * 0.6f;
			restartConfirmButtonYes = new GameObject(SCREEN_CENTER_X - xDif, SCREEN_CENTER_Y - 1, buttonWidth, buttonHeight);
			restartConfirmButtonYes.setImage(IMAGE_RESTART_CONFIRM_BUTTON_YES);
			restartConfirmButtonYes.moveToDrawingLayer(phaseLayerRestartConfirm);
			
			restartConfirmButtonNo = new GameObject(SCREEN_CENTER_X + xDif, SCREEN_CENTER_Y - 1, buttonWidth, buttonHeight);
			restartConfirmButtonNo.setImage(IMAGE_RESTART_CONFIRM_BUTTON_NO);
			restartConfirmButtonNo.moveToDrawingLayer(phaseLayerRestartConfirm);
		}
	}
	
	/**
	 * Initializes assets for Quit Confirmation screen.
	 */
	private void initializeQuitConfirmAssets()
	{
		if(quitConfirmPopUp == null)
		{
			float popUpWidth = gameUnitsFromPixels(430);
			float popUpHeight = gameUnitsFromPixels(312);
			quitConfirmPopUp = new GameObject(SCREEN_CENTER_X, SCREEN_CENTER_Y, popUpWidth, popUpHeight);
			quitConfirmPopUp.setImage(IMAGE_QUIT_CONFIRM_POP_UP);
			quitConfirmPopUp.moveToDrawingLayer(phaseLayerQuitConfirm);
			
			float buttonWidth = gameUnitsFromPixels(154); 
			float buttonHeight = gameUnitsFromPixels(85);		
			float xDif = buttonWidth * 0.6f;
			quitConfirmButtonYes = new GameObject(SCREEN_CENTER_X - xDif, SCREEN_CENTER_Y - 1, buttonWidth, buttonHeight);
			quitConfirmButtonYes.setImage(IMAGE_QUIT_CONFIRM_BUTTON_YES);
			quitConfirmButtonYes.moveToDrawingLayer(phaseLayerQuitConfirm);
			
			quitConfirmButtonNo = new GameObject(SCREEN_CENTER_X + xDif, SCREEN_CENTER_Y - 1, buttonWidth, buttonHeight);
			quitConfirmButtonNo.setImage(IMAGE_QUIT_CONFIRM_BUTTON_NO);
			quitConfirmButtonNo.moveToDrawingLayer(phaseLayerQuitConfirm);
		}
	}
	
	/**
	 * Initializes assets for Victory screen.
	 */
	private void initializeWinScreenAssets()
	{
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
	 * Initializes assets for Title screen.
	 */
	private void initializeTitleScreenAssets()
	{
		if(titleBackground == null)
		{
			titleBackground = new GameObject(SCREEN_CENTER_X, SCREEN_CENTER_Y, SCREEN_WIDTH, SCREEN_HEIGHT);
			titleBackground.setImage(IMAGE_TITLE_BACKGROUND);
			titleBackground.moveToDrawingLayer(phaseLayerTitleScreen);
			
			float playButtonWidth = HTL.gameUnitsFromPixels(372);
			float playButtonHeight = HTL.gameUnitsFromPixels(138);			
			titleButtonPlay = new GameObject(SCREEN_CENTER_X, SCREEN_CENTER_Y - .5f, playButtonWidth, playButtonHeight);
			titleButtonPlay.setImage(IMAGE_TITLE_BUTTON_PLAY);
			titleButtonPlay.moveToDrawingLayer(phaseLayerTitleScreen);

			float creditsButtonWidth = HTL.gameUnitsFromPixels(93);
			float creditsButtonHeight = HTL.gameUnitsFromPixels(95);
			titleButtonCredits = new GameObject(19, 1, creditsButtonWidth, creditsButtonHeight);
			titleButtonCredits.setImage(IMAGE_TITLE_BUTTON_CREDITS);
			titleButtonCredits.moveToDrawingLayer(phaseLayerTitleScreen);
		}		
	}
	
	/**
	 * Initializes assets for Level Intro screen.
	 */
	private void initializeLevelIntroAssets()
	{
		if(levelIntroPopUp == null)
		{						
			levelIntroPopUp = new GameObject();
			levelIntroPopUp.moveToDrawingLayer(phaseLayerLevelIntro);
			levelIntroButtonStart = new GameObject();
			levelIntroButtonStart.moveToDrawingLayer(phaseLayerLevelIntro);
			
			// message box over the shade
			float messageBoxWidth = gameUnitsFromPixels(609);
			float messageBoxHeight = gameUnitsFromPixels(487);
			levelIntroPopUp.setCenter(SCREEN_CENTER_X, SCREEN_CENTER_Y);
			levelIntroPopUp.setSize(messageBoxWidth, messageBoxHeight);
			levelIntroPopUp.setImage(IMAGE_LEVEL_INTRO_POP_UP);
			
			// and finally, a button!
			float buttonStartWidth = gameUnitsFromPixels(255);
			float buttonStartHeight = gameUnitsFromPixels(97);
			float buttonStartX = SCREEN_CENTER_X;
			float buttonStartY = SCREEN_CENTER_Y - 4.5f;
			levelIntroButtonStart.setCenter(buttonStartX, buttonStartY);
			levelIntroButtonStart.setSize(buttonStartWidth, buttonStartHeight);
			levelIntroButtonStart.setImage(IMAGE_LEVEL_INTRO_BUTTON_START);
		}
	}
	
	/**
	 * Initializes assets for Pause screen.
	 */
	private void initializePauseScreenAssets()
	{
		if(pausePopUp == null)
		{
			float popUpWidth = gameUnitsFromPixels(446);
			float popUpHeight = gameUnitsFromPixels(515);
			pausePopUp = new GameObject(SCREEN_CENTER_X, SCREEN_CENTER_Y, popUpWidth, popUpHeight);
			pausePopUp.setImage(IMAGE_PAUSE_POP_UP);
			pausePopUp.moveToDrawingLayer(phaseLayerPause);
			
			float buttonWidth = gameUnitsFromPixels(224); 
			float buttonHeight = gameUnitsFromPixels(85);
			pauseButtonResume = new GameObject(SCREEN_CENTER_X, 6.9f, buttonWidth, buttonHeight);
			pauseButtonResume.setImage(IMAGE_PAUSE_BUTTON_RESUME);
			pauseButtonResume.moveToDrawingLayer(phaseLayerPause);
			
			pauseButtonRestart = new GameObject(SCREEN_CENTER_X, 5.4f, buttonWidth, buttonHeight);
			pauseButtonRestart.setImage(IMAGE_PAUSE_BUTTON_RESTART);
			pauseButtonRestart.moveToDrawingLayer(phaseLayerPause);
			
			pauseButtonExit = new GameObject(SCREEN_CENTER_X, 3.9f, buttonWidth, buttonHeight);
			pauseButtonExit.setImage(IMAGE_PAUSE_BUTTON_EXIT);
			pauseButtonExit.moveToDrawingLayer(phaseLayerPause);
		}
	}
	
	
	/**
	 * If buttons haven't been created, make them.
	 */
	private void initializeButtonsOnHUD()
	{
		if(buttonMedic == null)
		{						
			buttonMedic = new Button(1f, 0.5f, 2, 1);
			buttonMedic.moveToDrawingLayer(layerHUD);
			buttonMedic.setVisibilityTo(false);
			//buttonMedic.setImagePressed("button_medic_down.png");
			//buttonMedic.setImageUnpressed("button_medic_up.png");

			buttonSpeedy = new Button(3f, 0.5f, 2, 1);
			buttonSpeedy.moveToDrawingLayer(layerHUD);
			buttonSpeedy.setVisibilityTo(false);
			//buttonSpeedy.setImagePressed("button_speedy_down.png");
			//buttonSpeedy.setImageUnpressed("button_speedy_up.png");			
		}			
	}
	
	
	/**
	 * This method puts blocking tiles in the areas where Wizards
	 * shouldn't be able to stand that would either be obscured
	 * by the HUD or in the way of neat level art objects.
	 */
	private void setBlockingTilesForSignAndHUD()
	{
		// wave sign
		grid.setTileBlockedTo(0, 5, true);
		grid.setTileBlockedTo(1, 5, true);
		grid.setTileBlockedTo(0, 4, true);
		grid.setTileBlockedTo(1, 4, true);
		
		// bottom left HUD
		for(int x = 0; x < 4; x++)
		{
			grid.setTileBlockedTo(x, 0, true);
		}
		
		// bottom right HUD
		for(int x = 16; x < 20; x++)
		{
			grid.setTileBlockedTo(x, 0, true);
		}
	}
	
	
	/**
	 * This function sets the speed at which walkers move at,
	 * and uses that speed to calculate how much damage Walkers should
	 * take every second in order to kill them approximately halfway
	 * through the path.
	 * The current function is a lot messy, and we never perfectly
	 * achieved the functionality described above.  However, the designers
	 * ended up satisfied with the state of the game, so we stopped iterating
	 * on this.
	 * We left our calculations behind in case it helps anyone.
	 */
	private void calculateWalkerSpeedAndDamageRate()
	{		
		// 6. Determine meatball damage per second
		float tileWidth = grid.getTileWidth();	
		int tilesInPath = grid.getNumberOfTilesInPath() + 1; // we add one because the Walker actually starts off the path
		float distanceToWalk = tilesInPath * tileWidth;
		float secondsToWalk = distanceToWalk / walkerSpeed;
		float secondsToDie = secondsToWalk / 2;
		float damagePerSecond = 100 / secondsToDie;
		float theActualNumber = damagePerSecond*25 * 40f / 48f * 2;

		// 0.04255319 dps
				
		// This is the math for the above.  It's awful.
		// 47
		// currentDPS * 41 tiles = 100
		// desiredDPS * 47 tiles = 100
		// 
		
		// gameObject velocity units = X every 40th of a second  (95% accuracy)  + or - 5%
		
		// okay, there's something with the speed of movement that just isn't right.
		// need to look into it - use the debugger to confirm math!
		
		// UPDATE NOTE: not looking into the above because everyone seems happy with the
		//				current rates of movement and death
	
		Walker.setDamageTakenPerSecond(theActualNumber);
			
		Walker.setDefaultSpeed(walkerSpeed);
		
		
		// 7. Set projectile speed based on meatballs speed.
		//		Projectiles currently aren't used, but leaving this here because
		//		it doesn't affect anything.
		Projectile.setSpeed(walkerSpeed * 3);
	}
	
	
	
	/**
	 * Loads the game's resources in advance so that the game doesn't
	 * slow down in real time when the resource is first used.
	 */
	protected void preloadResources()
	{
		super.preloadResources();
		resources.preloadSound(MUSIC_BACKGROUND);
		resources.preloadSound(MUSIC_TITLE);
		resources.preloadSound(MUSIC_LOSE);
		resources.preloadSound(MUSIC_WIN);
		resources.preloadFont(IMAGE_FONT);
		// don't need to preload most of the visuals because they are
		// loaded on initialization
	}
	
	
	
	/**
	 * Designers can tweak things in the text file,
	 * and we load those tweaks into the game to
	 * affect the gameplay.
	 */
	private void loadSettingsFromFile()
	{
		Scanner scan;
		
		File file = new File("settings.txt");
		scan = null;
		
		try
		{
			scan = new Scanner(file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		walkerSpeed = scanNextFloat(scan);
		towerMedicCastCooldownTime = scanNextFloat(scan);
		towerMedicHealthAdjust = scanNextFloat(scan);
		towerSpeedyCastInitialDelay = scanNextFloat(scan);
		towerSpeedyCastCooldownTime = scanNextFloat(scan);
		towerSpeedyBoostDuration = scanNextFloat(scan);
		towerSpeedBoostMultiplier = scanNextFloat(scan);
		movesRemaining = (int)scanNextFloat(scan);
		towerMedicRemaining = (int)scanNextFloat(scan);
		towerSpeedyRemaining = (int)scanNextFloat(scan);
		scoreToWin = scanNextFloat(scan);
		
		TowerMedic.setCastHealthAdjust(towerMedicHealthAdjust);
		TowerSpeedy.setCastSpeedAdjustDuration(towerSpeedyBoostDuration);
		TowerSpeedy.setCastSpeedAdjustMultiplier(towerSpeedBoostMultiplier);
	}
	

	
	/**
	 * Used by loadSettingsFromFile()
	 * @param scan		The scanner being used.
	 * @return			The float parsed from the scanner.
	 */
	private float scanNextFloat(Scanner scan)
	{
		float val = scan.nextFloat();
		scan.nextLine();
		return val;
	}
	

	
	/**
	 * Initializes the Wave Sign and debug text.
	 */
	private void initializeMiscUI()
	{			
		if(waveSign == null)
		{				
			// the wave sign			
			float waveSignWidth = gameUnitsFromPixels(122);
			float waveSignHeight = gameUnitsFromPixels(152);
			waveSign = new GameObject(0.7f, 5f, waveSignWidth, waveSignHeight);
			waveSign.setImage(IMAGE_WAVE_SIGN);
			waveSign.moveToDrawingLayer(layerEnvironmentOver);
			textCurrentWaveNumber = makeText(0.6f, 5.14f);	
			textCurrentWaveNumber.setFontSize(18);
			textCurrentWaveNumber.moveToDrawingLayer(layerEnvironmentOver);
			
			// debug
			if(SHOW_DEBUG_TEXT)
			{
				textUnspawned = makeText(8.5f, 10.8f);
				textTotalHP = makeText(8.5f, 10.5f);
				textDeaths = makeText(8.5f, 10.2f);		
			
				textWinMessage = makeText(8.5f, 1);
				textWinMessage.setFrontColor(Color.YELLOW);
			}
		}
	}

	
	
	
	
	
	
	
	/////////////////////////////////////////////////////
	//                                                 //
	//                                                 //
	//                                                 //
	//               PHASE TRANSITIONS                 //
	//                                                 //
	//                                                 //
	//                                                 //
	/////////////////////////////////////////////////////
	
	// worth noting that these phase transitions methods are NOT designed to be bulletproof
	// they make assumptions about the possible phases that they can be coming from.
	
	
	
	
	
	

	
	/**
	 * Transition to the Tile screen.
	 */
	private void enterPhaseTitle()
	{
		// music
		if(currentGamePhase != gamePhase.CREDITS)
		{
			resources.stopSound(MUSIC_BACKGROUND);
			resources.stopSound(MUSIC_WIN);
			resources.stopSound(MUSIC_LOSE);
			resources.playSoundLooping(MUSIC_TITLE);
		}		
		
		// visuals
		phaseLayerTitleScreen.setVisibilityTo(true);
		
		phaseLayerCredits.setVisibilityTo(false);
		phaseLayerLevelIntro.setVisibilityTo(false);
		phaseLayerRestartConfirm.setVisibilityTo(false);
		phaseLayerQuitConfirm.setVisibilityTo(false);
		phaseLayerPause.setVisibilityTo(false);
		phaseLayerWin.setVisibilityTo(false);
		phaseLayerLose.setVisibilityTo(false);
		layerScreenDarkener.setVisibilityTo(false);
		phaseLayerGameplay.setVisibilityTo(false);
		
		// done
		previousGamePhase = currentGamePhase;
		currentGamePhase = gamePhase.TITLE;	
	}
	
	
	
	/**
	 * Transition to the Credits Screen
	 */
	private void enterPhaseCredits()
	{		
		// visuals
		phaseLayerCredits.setVisibilityTo(true);
		
		phaseLayerTitleScreen.setVisibilityTo(false);		
		phaseLayerLevelIntro.setVisibilityTo(false);
		phaseLayerRestartConfirm.setVisibilityTo(false);
		phaseLayerQuitConfirm.setVisibilityTo(false);
		phaseLayerPause.setVisibilityTo(false);
		phaseLayerWin.setVisibilityTo(false);
		phaseLayerLose.setVisibilityTo(false);
		layerScreenDarkener.setVisibilityTo(false);
		phaseLayerGameplay.setVisibilityTo(false);
		
		// done
		previousGamePhase = currentGamePhase;
		currentGamePhase = gamePhase.CREDITS;	
	}
	
	
	
	/**
	 * Transition to the Level Intro screen.
	 */
	private void enterPhaseLevelIntro()
	{	
		// logic - prepare the level
		walkerSet.removeAll();
		towerSet.removeAll();
		loadSettingsFromFile();
		
		// this logic chunk wipes the grid so that the old game's stuff is not there - there is definitely a cheaper, cleaner way to do this
		grid.setDimensions(1, 1);
		grid.setDimensions(20, 10);
		createDefaultPath();
		setBlockingTilesForSignAndHUD();
		spawner.addPath(grid.getPath());		
		
		// more logic
		calculateWalkerSpeedAndDamageRate();
		spawner.clearWaves();
		spawner.addWavesFromFile("level001.txt");
		walkersDead = 0;
		walkersSavedBasic = 0;
		walkersSavedQuick = 0;
		healthSaved = 0;
		score = 0;
		selectedTower = null;
		buttonMedic.setPressed(false);
		buttonSpeedy.setPressed(false);
		updateScore();
		
		// music
		resources.stopSound(MUSIC_TITLE);
		resources.stopSound(MUSIC_WIN);
		resources.stopSound(MUSIC_LOSE);
		resources.stopSound(MUSIC_BACKGROUND);
		resources.playSoundLooping(MUSIC_BACKGROUND);		
		
		// visuals
		phaseLayerLevelIntro.setVisibilityTo(true);
		layerScreenDarkener.setVisibilityTo(true);
		phaseLayerGameplay.setVisibilityTo(true);
		
		phaseLayerTitleScreen.setVisibilityTo(false);		
		phaseLayerCredits.setVisibilityTo(false);		
		phaseLayerRestartConfirm.setVisibilityTo(false);
		phaseLayerQuitConfirm.setVisibilityTo(false);
		phaseLayerPause.setVisibilityTo(false);
		phaseLayerWin.setVisibilityTo(false);
		phaseLayerLose.setVisibilityTo(false);
		
		// done
		previousGamePhase = currentGamePhase;
		currentGamePhase = gamePhase.LEVEL_INTRO;
	}
	
	
	
	/**
	 * Transition to the Pause screen.
	 */
	private void enterPhasePause()
	{	
		// logic
		paused = true;
		CooldownTimer.pauseAll();
		//setAnimationPauseStatus(true);
		
		// music
		//resources.pauseSound();
		
		// visuals
		phaseLayerPause.setVisibilityTo(true);
		layerScreenDarkener.setVisibilityTo(true);
		phaseLayerGameplay.setVisibilityTo(true);
		
		phaseLayerTitleScreen.setVisibilityTo(false);		
		phaseLayerCredits.setVisibilityTo(false);		
		phaseLayerRestartConfirm.setVisibilityTo(false);
		phaseLayerQuitConfirm.setVisibilityTo(false);
		phaseLayerWin.setVisibilityTo(false);
		phaseLayerLose.setVisibilityTo(false);
		phaseLayerLevelIntro.setVisibilityTo(false);
		
		// done
		previousGamePhase = currentGamePhase;
		currentGamePhase = gamePhase.PAUSE;
	}
	
	
	
	/**
	 * Transition to the win screen.
	 */
	private void enterPhaseWin()
	{			
		// music
		if(currentGamePhase == gamePhase.GAMEPLAY)
		{
			resources.stopSound(MUSIC_TITLE);
			resources.stopSound(MUSIC_LOSE);
			resources.stopSound(MUSIC_BACKGROUND);
			resources.playSound(MUSIC_WIN);
			
			winScoreText.setText("" + (int)score);
		}
		
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
		
		// done
		previousGamePhase = currentGamePhase;
		currentGamePhase = gamePhase.WIN;
	}
	
	
	
	/**
	 * Transition to the Lose screen.
	 */
	private void enterPhaseLose()
	{			
		// music
		if(currentGamePhase == gamePhase.GAMEPLAY)
		{
			resources.stopSound(MUSIC_TITLE);
			resources.stopSound(MUSIC_WIN);
			resources.stopSound(MUSIC_BACKGROUND);
			resources.playSound(MUSIC_LOSE);
		}
		
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
		
		// done
		previousGamePhase = currentGamePhase;
		currentGamePhase = gamePhase.LOSE;
	}
	
	
	
	/**
	 * Transition to the Quit confirmation screen.
	 */
	private void enterPhaseQuitConfirm()
	{			
		// visuals		
		phaseLayerQuitConfirm.setVisibilityTo(true);
		layerScreenDarkener.setVisibilityTo(true);
		phaseLayerGameplay.setVisibilityTo(true);		
		
		phaseLayerTitleScreen.setVisibilityTo(false);		
		phaseLayerCredits.setVisibilityTo(false);		
		phaseLayerRestartConfirm.setVisibilityTo(false);
		phaseLayerWin.setVisibilityTo(false);
		phaseLayerLose.setVisibilityTo(false);
		phaseLayerLevelIntro.setVisibilityTo(false);
		phaseLayerPause.setVisibilityTo(false);
		
		// done
		previousGamePhase = currentGamePhase;
		currentGamePhase = gamePhase.QUIT_CONFIRM;
	}
	
	
	
	/**
	 * Transition to the Restart confirmation screen.
	 */
	private void enterPhaseRestartConfirm()
	{			
		// visuals	
		phaseLayerRestartConfirm.setVisibilityTo(true);
		layerScreenDarkener.setVisibilityTo(true);
		phaseLayerGameplay.setVisibilityTo(true);		
		
		phaseLayerTitleScreen.setVisibilityTo(false);		
		phaseLayerCredits.setVisibilityTo(false);		
		phaseLayerQuitConfirm.setVisibilityTo(false);
		phaseLayerWin.setVisibilityTo(false);
		phaseLayerLose.setVisibilityTo(false);
		phaseLayerLevelIntro.setVisibilityTo(false);
		phaseLayerPause.setVisibilityTo(false);
		
		// done
		previousGamePhase = currentGamePhase;
		currentGamePhase = gamePhase.RESTART_CONFIRM;
	}
	
	
	
	
	/**
	 * When the game transitions from the pre-level message to the actual game level,
	 * do this.
	 */
	private void enterPhaseGameplay()
	{
		if(currentGamePhase != gamePhase.PAUSE)
		{	
			// logic
			spawner.startWaves();
			
			towerMedicCooldown.start(towerMedicCastCooldownTime);
			// there is a delay before the tower Speedy is allowed to be active
			towerSpeedyCooldownInitialDelay = new CooldownTimer(towerSpeedyCastInitialDelay);
			towerSpeedyCooldown.start(Float.MAX_VALUE);
			towerSpeedyCooldownInitialDelayIsDone = false;	
			
			debugWalkerDamageBoostOn = false;
			debugWalkerSpeedBoostOn = false;
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
	
	
	
	
	
	
	
	
	
	/////////////////////////////////////////////////////
	//                                                 //
	//                                                 //
	//                                                 //
	//                     UPDATES                     //
	//                                                 //
	//                                                 //
	//                                                 //
	/////////////////////////////////////////////////////
	
	
	
	
	
	
	
	
	/**
	 * Updates everything in the game, once per game loop.
	 */
	public void updateWorld()
	{
		super.updateWorld();		
		
		// toggle fullscreen
		if(keyboard.isButtonTapped(KeyEvent.VK_W))
		{
			if(fullscreenMode)
			{
				window.setScreenToWindowed();
			}
			else
			{
				window.setScreenToFullscreen();
			}
			fullscreenMode = !fullscreenMode;
		}
		
		// update the game according to current phase
		switch(currentGamePhase)
		{
			case TITLE: 			updatePhaseTitle(); break;
			case CREDITS:			updatePhaseCredits(); break;
			case LEVEL_INTRO:		updatePhaseLevelIntro(); break;
			case GAMEPLAY:			updatePhaseGameplay(); break;
			case PAUSE:				updatePhasePause(); break;
			case RESTART_CONFIRM:	updateRestartConfirm(); break;
			case QUIT_CONFIRM:		updateQuitConfirm(); break;
			case WIN:				updatePhaseWin(); break;
			case LOSE:				updatePhaseLose(); break;
		}		
	}
	
	
	
	/**
	 * Update player input for Title screen.
	 */
	private void updatePhaseTitle()
	{
		if(!mouse.isButtonTapped(1))
		{
			return;
		}
		
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();
		
		if(titleButtonPlay.containsPoint(mouseX, mouseY))
		{
			enterPhaseLevelIntro();
		}
		else if(titleButtonCredits.containsPoint(mouseX, mouseY))
		{
			enterPhaseCredits();
		}
	}
	
	
	
	/**
	 * Update player input for credit screen
	 */
	private void updatePhaseCredits()
	{
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();
		
		if(mouse.isButtonTapped(1) && creditsButtonBack.containsPoint(mouseX, mouseY))
		{
			enterPhaseTitle();
		}
	}
	
	
	
	/**
	 * Update player input for Level introduction screen
	 */
	private void updatePhaseLevelIntro()
	{
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();
		
		if(mouse.isButtonTapped(1) && levelIntroButtonStart.containsPoint(mouseX, mouseY))
		{
			enterPhaseGameplay();
		}
	}
	
	
	
	/**
	 * Update player input for pause screen.
	 */
	private void updatePhasePause()
	{
		if(!mouse.isButtonTapped(1))
		{
			return;
		}
		
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();
		
		if(pauseButtonResume.containsPoint(mouseX, mouseY))
		{
			enterPhaseGameplay();
		}
		else if(pauseButtonRestart.containsPoint(mouseX, mouseY))
		{
			enterPhaseRestartConfirm();
		}
		else if(pauseButtonExit.containsPoint(mouseX, mouseY))
		{
			enterPhaseQuitConfirm();
		}
	}
	
	
	
	/**
	 * Update player input for win screen
	 */
	private void updatePhaseWin()
	{
		if(!mouse.isButtonTapped(1))
		{
			return;
		}
		
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();
		
		if(winButtonRestart.containsPoint(mouseX, mouseY))
		{
			enterPhaseLevelIntro();
		}
		else if(winButtonQuit.containsPoint(mouseX, mouseY))
		{
			enterPhaseQuitConfirm();
		}
	}
	
	
	
	/**
	 * Update player input for lose screen
	 */
	private void updatePhaseLose()
	{
		if(!mouse.isButtonTapped(1))
		{
			return;
		}
		
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();
		
		if(loseButtonRestart.containsPoint(mouseX, mouseY))
		{
			enterPhaseLevelIntro();
		}
		else if(loseButtonQuit.containsPoint(mouseX, mouseY))
		{
			enterPhaseQuitConfirm();
		}
	}
	
	
	
	/**
	 * Update player input for restart confirmation screen
	 */
	private void updateRestartConfirm()
	{
		if(!mouse.isButtonTapped(1))
		{
			return;
		}
		
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();		
		
		if(restartConfirmButtonYes.containsPoint(mouseX, mouseY))
		{
			enterPhaseLevelIntro();
		}
		else if(restartConfirmButtonNo.containsPoint(mouseX, mouseY))
		{
			if(previousGamePhase == gamePhase.PAUSE)
			{
				enterPhasePause();
			}
			else if(previousGamePhase == gamePhase.WIN)
			{
				enterPhaseWin();
			}
			else if(previousGamePhase == gamePhase.LOSE)
			{
				enterPhaseLose();
			}
		}
	}
	
	
	
	/**
	 * Update player input for quit confirmation screen.
	 */
	private void updateQuitConfirm()
	{
		if(!mouse.isButtonTapped(1))
		{
			return;
		}
		
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();		
		
		if(quitConfirmButtonYes.containsPoint(mouseX, mouseY))
		{
			System.exit(0);
		}
		else if(quitConfirmButtonNo.containsPoint(mouseX, mouseY))
		{
			if(previousGamePhase == gamePhase.PAUSE)
			{
				enterPhasePause();
			}
			else if(previousGamePhase == gamePhase.WIN)
			{
				enterPhaseWin();
			}
			else if(previousGamePhase == gamePhase.LOSE)
			{
				enterPhaseLose();
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Update the gameplay phase, including all objects and player input.
	 */
	private void updatePhaseGameplay()
	{
		updateGameplayClicks();
		
		if(gameIsOver())
		{
			if(score >= scoreToWin)
			{
				enterPhaseWin();
			}
			else
			{
				enterPhaseLose();
			}
		}
		
		spawner.update();
		walkerSet.update();
		towerSet.update();
		updateTowerShoot();		
		updateScore();
		updateUI();
		updateDebugKeys();
	}
	

	
	/**
	 * Returns true if the game is considered over.
	 * @return		Whether the game is considered over.
	 */
	private boolean gameIsOver()
	{
		if(!spawner.isDone())
		{
			return false;
		}
		
		for(Walker walker : walkerSet.getArrayOfWalkers())
		{
			if(!walker.isDead())
			{
				return false;
			}
		}		
		return true;
	}
	
	
	
	/**
	 * Update debug stuff based on player input.
	 */
	private void updateDebugKeys()
	{
		if(!DEBUG_KEYS_ENABLED)
		{
			return;
		}
		
		// toggle speed boost
		if(keyboard.isButtonTapped(KeyEvent.VK_S))
		{
			debugWalkerSpeedBoostOn = !debugWalkerSpeedBoostOn;
			float oldSpeed = Walker.getDefaultSpeed();
			
			if(debugWalkerSpeedBoostOn)
			{
				float newSpeed = oldSpeed * 10;
				Walker.setDefaultSpeed(newSpeed);
			}
			else
			{
				float newSpeed = oldSpeed / 10;
				Walker.setDefaultSpeed(newSpeed);
			}
		}

		// toggle damage boost
		if(keyboard.isButtonTapped(KeyEvent.VK_D))
		{
			debugWalkerDamageBoostOn = !debugWalkerDamageBoostOn;
			float oldDamage = Walker.getDamageTakenPerSecond();
			
			if(debugWalkerDamageBoostOn)
			{
				float newDamage = oldDamage * 10;
				Walker.setDamageTakenPerSecond(newDamage);
			}
			else
			{
				float newDamage = oldDamage / 10;
				Walker.setDamageTakenPerSecond(newDamage);
			}
		}
		
		// force move to next wave (didn't get it working yet)
		if(keyboard.isButtonTapped(KeyEvent.VK_N))
		{
			//spawner.forceNextSpawn();
		}
	}
	
	
	
	/**
	 * Sets a tower to be the currently selected tower.
	 * Used when the player clicks a tower.
	 * @param tower		The tower to select.
	 */
	private void selectTower(Tower tower)
	{
		if(tower != null)
		{
			unselectTower();
			selectedTower = tower;
			selectedTower.setSelectedTo(true);
		}
		
	}
	
	
	
	/**
	 * If there is a selected tower, it is no longer selected.
	 */
	private void unselectTower()
	{
		if(selectedTower != null)
		{
			selectedTower.setSelectedTo(false);
		}
		selectedTower = null;
	}
	
	
	
	/**
	 * For the gameplay phase.
	 * Checks to see if anything important has been clicked,
	 * and reacts accordingly.
	 * 
	 * Needs refactoring because it's too big; should probably be several tinier methods.
	 */
	private void updateGameplayClicks()
	{	
		// get mouse coordinates
		float mouseX = mouse.getWorldX();
		float mouseY = mouse.getWorldY();
		
		// in-game
		if(mouse.isButtonTapped(1))
		{		
			// press pause button
			if(buttonPause.containsPoint(mouseX, mouseY))
			{
				enterPhasePause();
			}
			// press Medic Tower button
			else if(buttonMedic.containsPoint(mouseX, mouseY))
			{
				if(!buttonMedic.isPressed())
				{
					buttonMedic.setPressed(true);
					buttonSpeedy.setPressed(false);
					unselectTower();
				}
			}
			// press Speed Tower button
			else if(buttonSpeedy.containsPoint(mouseX, mouseY))
			{
				if(!buttonSpeedy.isPressed())
				{
					buttonSpeedy.setPressed(true);
					buttonMedic.setPressed(false);					
					unselectTower();
				}
			}
			// otherwise, did you click a tile?
			else
			{
				Tile clickedTile = grid.getClickedTile();		
				if(clickedTile == null) return;				

				// if a Tower is selected, can it be moved to this Tile?
				if(selectedTower != null && clickedTile.isAvailable()
					&& !clickedTile.hasPath() && movesRemaining > 0)
				{
					selectedTower.teleportTo(clickedTile);
					unselectTower();
					movesRemaining--;
					
					selectedTower.playSoundMove();
				}	
				// otherwise, if there's no Tower selected, can we place a Tower?
				else if(clickedTile.isAvailable() && !clickedTile.hasPath())
				{	
					if(towerMedicRemaining > 0 && buttonMedic.isPressed())
					{
						towerSet.addTowerAt(clickedTile, true);
						towerMedicRemaining--;
					}
					else if (towerSpeedyRemaining > 0 && buttonSpeedy.isPressed())
					{
						towerSet.addTowerAt(clickedTile, false);
						towerSpeedyRemaining--;
					}
				}
				// otherwise, if there's a Tower on the tile, toggle selection of the tower
				else if (clickedTile.hasOccupant())
				{			
					Tower clickedTower = clickedTile.getOccupant();
					
					if(clickedTower == selectedTower)
					{
						unselectTower();
					}
					else
					{
						selectTower(clickedTower);
					}
					buttonMedic.setPressed(false);
					buttonSpeedy.setPressed(false);
				}
				// otherwise, if the space is unavailable, play an error sound
				else if (!clickedTile.isAvailable() || clickedTile.hasPath())
				{
					Tower.playSoundMoveFail();		
				}
			}
		}
	}
	
	
	
	/**
	 * Controls whether any given Tower should shoot its spell.
	 * 
	 * Should be refactored because it's too big.
	 */
	private void updateTowerShoot()
	{
		// implements the delay between spellcasts of different Tower types, if any
		if(!towerSpeedyCooldownInitialDelayIsDone && towerSpeedyCooldownInitialDelay.isReady())
		{
			towerSpeedyCooldownInitialDelayIsDone = true;
			towerSpeedyCooldown.start(towerSpeedyCastCooldownTime);
		}
		
		// if nobody is ready to spellcast, why bother?
		if(!towerSpeedyCooldown.isReady() && !towerMedicCooldown.isReady())
		{
			return;
		}
		
		boolean towerMedicSoundPlayed = false;
		boolean towerSpeedySoundPlayed = false;
		
		// for all Towers...
		for (Tower tower : towerSet.getArrayOfTowers())
		{		
			// if this Tower is of the tower type that is currently ready to fire...
			if(towerSpeedyCooldown.isReady() && tower.getTowerType() == Tower.Type.SPEEDY ||
				towerMedicCooldown.isReady() && tower.getTowerType() == Tower.Type.MEDIC) 
			{
				Tile towerTile = tower.getTile();
				if(towerTile == null) continue;
				
				boolean mySpellcastEffectHasPlayed = false;
				
				// if there is a Walker on a tile within range, do spellcast
				for (Walker walker : walkerSet.getArrayOfWalkers())
				{
					if(walker.isDead()) 
					{
						continue;
					}

					if(walkerIsInTowerSpellcastArea(walker, tower))
					{
						// visual and audio effects
						if(!mySpellcastEffectHasPlayed)
						{
							mySpellcastEffectHasPlayed = true;									
							tower.playEffectSpellcast();
						}
						
						if(!towerMedicSoundPlayed && tower.getTowerType() == Tower.Type.MEDIC)
						{
							towerMedicSoundPlayed = true;
							tower.playSoundSpellcast();
						}
						else if(!towerSpeedySoundPlayed && tower.getTowerType() == Tower.Type.SPEEDY)
						{
							towerSpeedySoundPlayed = true;
							tower.playSoundSpellcast();
						}						
						
						//tower.shootAt(walker);
						float healthAdjust = tower.getCastHealthAdjust();
						float speedMultiplier = tower.getCastSpeedAdjustMultiplier();
						float speedTime = tower.getCastSpeedAdjustDuration();
						
						walker.addHealth(healthAdjust);
						walker.applySpeedBuff(speedMultiplier, speedTime);
					}	
				}
			}
		}
		
		// restart timers if necessary
		
		if(towerSpeedyCooldown.isReady())
		{
			towerSpeedyCooldown.start(towerSpeedyCastCooldownTime);
		}			
			
		if(towerMedicCooldown.isReady())
		{
			towerMedicCooldown.start(towerMedicCastCooldownTime);
		}		
	}
	
	
	
	
	/**
	 * If the Walker is within spell range of the Tower, return true.
	 * Current range is a 3x3 Tile grid with the Tower in the center.
	 * @param tower		The Tower.
	 * @param walker	The Walker.
	 * @return			True if the Walker is in range.
	 */
	private boolean walkerIsInTowerSpellcastArea(Walker walker, Tower tower)
	{
		Tile towerTile = tower.getTile();
		if(towerTile == null)
		{
			return false;
		}
		
		int mapColumnOfTower = towerTile.getGridColumn();
		int mapRowOfTower = towerTile.getGridRow();		
		Vector2 walkerPos = walker.getCenter();

		// search surrounding tiles in 3x3 for walker
		for(int row = mapRowOfTower-1; row <= mapRowOfTower+1; row++)
		{
			for(int column = mapColumnOfTower-1; column <= mapColumnOfTower+1; column++)
			{
				Tile testTile = grid.getTile(column, row);
				if(testTile != null && testTile.containsPoint(walkerPos))
				{								
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	
	
	/**
	 * Update the score if a Walker makes it to the end of the path or dies.
	 * Also gets rid of Walkers who made it to the end of the path.
	 */
	private void updateScore()
	{
		for (Walker walker : walkerSet.getArrayOfWalkers())
		{
			if(walker.hasJustDied())
			{
				walkersDead++;
				walker.playSoundDeath();
			}
			else if(walker.isAtPathEnd())
			{
				walker.playSoundSurvival();
				
				float healthToAdd = walker.getHealth();
				if(walker.getWalkerType() == Walker.Type.BASIC)
				{
					walkersSavedBasic++;
				}
				else
				{
					walkersSavedQuick++;
					healthToAdd *= 2;
				}
				healthSaved += healthToAdd;
				
				int totalWalkersSaved = walkersSavedBasic + walkersSavedQuick;
				score = totalWalkersSaved * healthSaved;
				walker.destroy();
			}
		}
	}
	
	
	

	
	
	/**
	 * Updates everything on the HUD, the wave sign,
	 * and some optional debug output.
	 */
	private void updateUI()
	{	
		int secondsUntilNextWave = (int)(spawner.getSecondsUntilNextWave());
		
		// top row
		setHUDTime(secondsUntilNextWave);
		setHUDNumberOfMoves(movesRemaining);
		setHUDScore((int)score);
	
		// bottom row
		setHUDNumberOfTowersMedic(towerMedicRemaining);
		setHUDNumberOfTowersSpeedy(towerSpeedyRemaining);
		setHUDNumberOfWalkersBasic(walkersSavedBasic);
		setHUDNumberOfWalkersQuick(walkersSavedQuick);
		
		// sign - shows currentWave / totalWaves
		String signString = "" + spawner.getCurrentWaveNumber() + "/" + spawner.getTotalWaves();
		textCurrentWaveNumber.setText(signString);	
		

		// debug		
		if(SHOW_DEBUG_TEXT)	
		{
			textUnspawned.setText("Walkers remaining: " + spawner.getWalkersThisWaveRemaining());
			textDeaths.setText("Deaths: " + walkersDead);		
			textTotalHP.setText("Health saved: " + healthSaved);
			
			if(score >= scoreToWin)
			{
				textWinMessage.setText("YOU WIN!");
			}
			else
			{
				textWinMessage.setText("");
			}
		}
	}
	
	
}
