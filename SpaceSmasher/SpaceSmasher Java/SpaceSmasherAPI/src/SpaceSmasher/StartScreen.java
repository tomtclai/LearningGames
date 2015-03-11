package SpaceSmasher;

import java.awt.event.KeyEvent;

import Engine.KeyboardInput;
import Engine.MouseInput;
import Engine.GameObject;
import Engine.Vector2;
import Engine.World;
import SpaceSmasher.SpaceSmasher.GameState;
/**
 * Manages the StartScreen components before the game starts
 * 
 * @author Fernando Arnez
 *
 */
class StartScreen 
{
	private boolean isRunning; // If start screen is on.
	private MouseInput mouse;
	private KeyboardInput keyboard;
	private World world;

	GameObject background;
	GameObject startButton;
	GameObject creditsButton;
	GameObject creditsScroll;
	
	private final static float SCROLL_SPEED = .1f;

	private final String START_SCREEN = "startscreen/StartScreenBG.png";
	private final String CREDITS_SCREEN = "credits/SpaceSmasher_CreditsBG.png";
	
	private final String CREDITS_SCROLL = "credits/SpaceSmasher_CreditsScroll.png";

	private final String START_BUTTON_PRESSED = "startscreen/StartButton_Press.png";
	private final String START_BUTTON_HOVER = "startscreen/StartButton_Hover.png";
	private final String START_BUTTON_STATIC = "startscreen/StartButton_Static.png";

	private final String CREDITS_BUTTON_STATIC = "startscreen/CreditsButton_Static.png";
	private final String CREDITS_BUTTON_HOVER = "startscreen/CreditsButton_Hover.png";
	private final String CREDITS_BUTTON_PRESSED = "startscreen/CreditsButton_Press.png";
	
	private enum StartScreenState
	{
		START, CREDITS, TRANS_INTO_GAME, TRANS_INTO_START, TRANS_INTO_CREDITS
	};

	private StartScreenState state = StartScreenState.START;
	private boolean ShowStartScreen = true;

	public StartScreen(MouseInput mouse, KeyboardInput keyboard, World world)
	{
		this.mouse = mouse;
		this.world = world;
		this.keyboard = keyboard;
		isRunning = true;
		

		// Background
		background = new GameObject(world.getWidth() / 2, world.getHeight() / 2, world.getWidth(), world.getHeight());
		background.setImage( START_SCREEN );
		
		// Buttons
		startButton = new GameObject(world.getWidth() * .4f, world.getHeight() * .4f, 14f, 6f);
		startButton.setImage( START_BUTTON_STATIC );
		
		creditsButton = new GameObject(world.getWidth() * .6f, world.getHeight() * .4f, 14f, 6f);
		creditsButton.setImage( CREDITS_BUTTON_STATIC );
		
		creditsScroll = new GameObject(world.getWidth() * .5f, world.getHeight() * -0.5f , 30f, 80f);
		creditsScroll.setImage( CREDITS_SCROLL );
		creditsScroll.setToInvisible();
	}
	
	public void setShowStartScreen(boolean f) {
		ShowStartScreen = f;
	}

	public void update()
	{
		switch(state)
		{
		case START:
			if (ShowStartScreen)
				startScreenButtons();
			else
				endStartScreen();
			break;
		case CREDITS:
			creditScreenButtons();
			break;
		case TRANS_INTO_GAME:
			endStartScreen();
			break;
		case TRANS_INTO_CREDITS:
			background.setImage(CREDITS_SCREEN);
			startButton.setToInvisible();
			creditsButton.setToInvisible();
			creditsScroll.setToVisible();
			creditsScroll.setVelocity(0,SCROLL_SPEED);
			state = StartScreenState.CREDITS;
			break;
		case TRANS_INTO_START:
			background.setImage(START_SCREEN);
			beginStartScreen();
			creditsScroll.setToInvisible();
			creditsScroll.setVelocity(0,0);
			creditsScroll.setCenter( world.getWidth() * .5f, creditsScroll.getHeight() * -0.5f );
			state = StartScreenState.START;
			break;
		}
	}

	private void startScreenButtons() 
	{
		// Update start button
		buttonStateChanger(startButton,
				START_BUTTON_STATIC,
				START_BUTTON_HOVER,
				START_BUTTON_PRESSED,
				StartScreenState.TRANS_INTO_GAME);
		
		// Update start button
		buttonStateChanger(creditsButton,
				CREDITS_BUTTON_STATIC,
				CREDITS_BUTTON_HOVER,
				CREDITS_BUTTON_PRESSED,
				StartScreenState.TRANS_INTO_CREDITS);
		
		
	}
	
	private void buttonStateChanger(GameObject button, 
			String staticFilename, String hoverFilename,
			String pressedFilename, StartScreenState exitState)
	{
		// Inefficent, optimize later.
		Vector2 mousePos = new Vector2(mouse.getWorldX(), mouse.getWorldY()); 
		
		// Check Start Button
		if(button.containsPoint(mousePos))
		{
			button.setImage(hoverFilename);
			if(mouse.isButtonDown(1))
			{
				button.setImage(pressedFilename);
				state = exitState;
			}
		}
		else
		{
			button.setImage(staticFilename);
		}
	}
	

	private void creditScreenButtons() 
	{
		if(keyboard.isButtonTapped(KeyEvent.VK_ESCAPE))
		{
			state = StartScreenState.TRANS_INTO_START;
		}
		// Loop the Credits.
		if( creditsScroll.getCenterY() - ( creditsScroll.getHeight() / 1.5 ) > world.getHeight() )
		{
			state = StartScreenState.TRANS_INTO_START;
		}
		// Move Credits
		creditsScroll.update();
	}

	public boolean startScreenRunning()
	{	
		return isRunning;
	}
	
	public void beginStartScreen()
	{
		isRunning = true;
		background.setToVisible();
		creditsButton.setToVisible();
		startButton.setToVisible();
	}
	
	public void endStartScreen()
	{
		isRunning = false;
		background.setToInvisible();
		creditsButton.setToInvisible();
		startButton.setToInvisible();
		state = StartScreenState.TRANS_INTO_START;
	}
	
}