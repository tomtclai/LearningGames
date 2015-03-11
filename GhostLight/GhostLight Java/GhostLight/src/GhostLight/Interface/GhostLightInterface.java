package GhostLight.Interface;

import Engine.KeyboardInput;

/**
 * Used to interface with the GhostLight Game extend this class to define the initialize and update within the game
 * @author Michael Letter
 */
public abstract class GhostLightInterface {
	
	
	/** Stores the users knowledge of the gameState */
	public GameState gameState = new GameState();
	/** Current Enemy Grid used to Dictate Enemy positions and of the InteractableObjects in play */
	public ObjectSet objectGrid = new ObjectSet();
	/** Current Enemy Grid used to Dictate Enemy positions of the InteractableObjects in play */ 
	public PrimitiveSet primitiveGrid = new PrimitiveSet();
	/** Stores the user information for the flashlight */
	public FlashLight light = new FlashLight();
	/** Offerers access to the keyboards state and whether or not a specific key is up, down or tapped*/
	public KeyboardInput keyboard = null;
	/** Offers Access to the Mouses currentState. this includes mouse movement, which buttons on the mouse have been pressed tapped etc.. */
	public MouseState mouse = null;
	/** Offers access to the buttons in the window that can be clicked by the user */
	public OnScreenButtons clickableButtons = null;
	
	//User Functions
	/**
	 * Is called at the start prior to the start of the game and can be used to initialize game state brior ot running
	 */
	public abstract void initialize();
	/**
	 * Is called at Each update in game and allows you to define the progression of the game state
	 */
	public abstract void update();
	/**
	 * Is called right before the End of the level and can be used to clean up and data or resources this level is using
	 */
	public abstract void end();
}