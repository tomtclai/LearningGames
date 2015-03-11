package GhostLight.GhostEngine;

import Engine.BaseCode;
import Engine.MouseInput;
import Engine.Rectangle;
import GhostLight.Interface.GhostLightInterface;
import MenueSystem.Button;
import MenueSystem.Button.transitionState;

public class PostGameScreen 
{
	private MouseInput mouse;
	private static String BACKGROUND_IMG_NAME = "postGame/GameOverBlur.png";
	private static String PANEL_LOSE_NAME = "postGame/GameOver_PopUp.png";
	private static String PANEL_WIN_NAME = "postGame/YouWin_PopUp.png";
	private static String BUTTON_EXIT_HOVER_NAME = "postGame/Exit_Button_Hover.png";
	private static String BUTTON_EXIT_STATIC_NAME = "postGame/Exit_Button_Static.png";
	private static String BUTTON_REPLAY_HOVER_NAME = "postGame/Replay_Button_Hover.png";
	private static String BUTTON_REPLAY_STATIC_NAME = "postGame/Replay_Button_Static.png";
	
	private Rectangle backgroundImage;
	private Rectangle panelImage;
	private LevelButton replayButton;
	private ExitButton exitButton;
	private GhostLight gManager;
	
	PostGameScreen(MouseInput mouseInput, GhostLight manager, GhostLightInterface replayLevel, int levelNumber)
	{
		mouse = mouseInput;
		gManager = manager;
		
		backgroundImage = new Rectangle();
		backgroundImage.setImage(BACKGROUND_IMG_NAME);
		backgroundImage.size.set(BaseCode.world.getWidth(), BaseCode.world.getHeight());
		backgroundImage.center.set(BaseCode.world.getWidth() / 2, BaseCode.world.getHeight() / 2);	
		backgroundImage.visible = false;
		
		panelImage = new Rectangle();
		panelImage.setImage(PANEL_WIN_NAME);
		panelImage.size.set(30f, 30f);
		panelImage.center.set(BaseCode.world.getWidth() / 2, BaseCode.world.getHeight() / 2);	
		panelImage.visible = false;
		
		replayButton = new LevelButton();
		replayButton.setLevel(manager, replayLevel, levelNumber);
		replayButton.getBackgroundCenter().set(BaseCode.world.getWidth() / 2, (BaseCode.world.getHeight() / 2) - 4.5f);
		replayButton.getBackgroundSize().set(12f, 4.5f);
		replayButton.setVisibility(false, true);
		
		exitButton = new ExitButton();
		exitButton.setGameManager(manager);
		exitButton.getBackgroundCenter().set(BaseCode.world.getWidth() / 2, (BaseCode.world.getHeight() / 2) - 9f);
		exitButton.getBackgroundSize().set(12f, 4f);
		exitButton.setVisibility(false, true);
		
		
	}
	public void enablePostScreen(boolean hasWon)
	{
		// Decide which image to put up.
		if(hasWon)
		{
			panelImage.setImage(PANEL_WIN_NAME);
		}
		else
		{
			panelImage.setImage(PANEL_LOSE_NAME);
		}
		
		backgroundImage.visible = true;
		panelImage.visible = true;
		replayButton.setVisibility(true, true);
		exitButton.setVisibility(true, true);
		exitButton.setIdleImage(BUTTON_EXIT_STATIC_NAME);
		exitButton.setMouseOverIdleImage(BUTTON_EXIT_HOVER_NAME);
		replayButton.addToAutoDrawSet();
		exitButton.addToAutoDrawSet();
		
		gManager.setUserTextVisiblity(false);
		gManager.setScoreTextVisiblity(false);
	}
	public void disablePostScreen()
	{
		backgroundImage.visible = false;
		panelImage.visible = false;
		replayButton.setVisibility(false, true);
		replayButton.setIdleImage(BUTTON_REPLAY_STATIC_NAME);
		replayButton.setMouseOverIdleImage(BUTTON_REPLAY_HOVER_NAME);
		replayButton.removeFromAutoDrawSet();
		exitButton.removeFromAutoDrawSet();
		
		gManager.setUserTextVisiblity(true);
		gManager.setScoreTextVisiblity(true);
	}
	public void update()
	{
		replayButton.autoUpdateState(mouse);
		exitButton.autoUpdateState(mouse);
	}
	
	/*
	 * A simple class to allow for the game to end via clicking the button.
	 */
	private class ExitButton extends Button
	{
		GhostLight manager = null;
		
		public ExitButton() 
		{
			super();
			super.setIdleImage("menuArt/ButtonIdle.png");
			super.setButtonDownImage("menuArt/ButtonDown.png");
			super.setMouseOverIdleImage("menuArt/ButtonIdleMouseOver.png");
			super.setMouseOverSelectImage("menuArt/ButtonIdleMouseOver.png");
			super.setSelectImage("menuArt/ButtonIdle.png");
			super.selectable = false;
		}
		/**
		 * Sets which game it will terminate.
		 */
		public void setGameManager(GhostLight gameManager)
		{
			manager = gameManager;
		}
		
		public void autoUpdateState(MouseInput mouse){
			transitionState oldState = super.getSubState();
			super.autoUpdateState(mouse);
			if(super.getSubState() == transitionState.DOWN && super.getSubState() != oldState){
				if(manager != null)
					manager.terminateProgram();
			}
		}
		
	}
	
	
}
