package TowerDefense;
import Engine.GameObject;

/**
 * This is a basic button that can be pressed or not pressed.
 * We made this class because the Engine Button class had far more stuff in it we needed.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class Button extends GameObject
{		
	private boolean isPressed = false;		// whether the button is currently in a state of being pressed

	
	// images of the button in various combinations of being up or down, with or without a mouse hovering over it
	private String picDown = null;
	private String picUp = null;
	

	
	
	
	/**
	 * Constructor.
	 * @param xLoc					X-location of this button.
	 * @param yLoc					Y-location of this button.
	 * @param width					Width of the button.
	 * @param height				Height of the button.
	 */
	public Button(float xLoc, float yLoc, float width, float height)
	{
		super(xLoc, yLoc, width, height);
		this.draw();
	}

	
	

	
	/**
	 * Set whether the button is pressed or not pressed.
	 * Note that by default, buttons will stay pressed
	 * once pressed.
	 * @param isPressed		True if the button should be pressed.
	 */
	public void setPressed(boolean isPressed)
	{		
		this.isPressed = isPressed;		
		
		this.update();
	}
	
	
	
	/**
	 * @return		True if the button is currently pressed.
	 */
	public boolean isPressed()
	{
		return isPressed;
	}
	
	
	
	
	/**
	 * Specifies which image should be used for this button when it is pressed.
	 * @param filename		The pressed button image.
	 */
	public void setImagePressed(String filename)
	{
		picDown = filename;
		if(isPressed)
		{
			this.update();
		}
	}
	
	
	/**
	 * Specifies which image should be used for this button when it is not pressed.
	 * @param filename		The non-pressed button image.
	 */
	public void setImageUnpressed(String filename)
	{
		picUp = filename;
		if(!isPressed)
		{
			this.update();
		}
	}
	
	
	/**
	 * Update the button.
	 */
	public void update()
	{
		if(isPressed)
		{
			this.setImage(picDown);
		}
		else
		{
			this.setImage(picUp);
		}
		
		super.update();
	}
}
