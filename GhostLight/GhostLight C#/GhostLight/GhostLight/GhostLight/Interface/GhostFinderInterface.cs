using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
namespace GhostFinder.Interface{
    /// <summary>
    /// Used to interface with the GhostFinder Game extend this class to define the initialize and update within the game
    /// </summary>
    public abstract class GhostFinderInterface {


        /// <summary> Stores the users knowledge of the gameState </summary>
	    public GameState gameState = new GameState();
        /// <summary>Current Enemy Grid used to Dictate Enemy positions and of the InteractableObjects in play</summary>
	    public ObjectSet objectGrid = new ObjectSet();
        /// <summary> Current Enemy Grid used to Dictate Enemy positions of the InteractableObjects in play </summary>
	    public PrimitiveSet primitiveGrid = new PrimitiveSet();
        /// <summary> Stores the user information for the flashlight </summary>
	    public FlashLight light = new FlashLight();
        /// <summary> Offers access to the Keybords current State</summary>
        public Keyboard keyboard = null;
        /// <summary> Offers Access to the Mouses currentState. this includes mouse movement, which buttons on the mouse have been pressed tapped etc.. </summary>
	    public MouseState mouse = null;
        /// <summary> Offers access to the buttons in the window that can be clicked by the user </summary>
	    public OnScreenButtons clickableButtons = null;

        //User Functions
	    /// <summary>
        /// Is called at the start prior to the start of the game and can be used to initialize game state brior ot running
	    /// </summary>
	    public abstract void initialize();
	    /// <summary>
        /// Is called at Each update in game and allows you to define the progression of the game state
	    /// </summary>
	    public abstract void update();
	    /// <summary>
        /// Is called right before the End of the level and can be used to clean up and data or resources this level is using
	    /// </summary>
	    public abstract void end();
    }
}