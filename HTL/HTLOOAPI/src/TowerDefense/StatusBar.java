package TowerDefense;

import java.awt.Color;

import Engine.GameObject;

/**
 * Status bars were used in the menu to display Towers' health and badFeels.
 * They haven't been used in forever, and hence may be out of date.
 * 
 *  @author Branden Drake
 *  @author Rachel Horton
 */

public class StatusBar extends GameObject
{	
	// These game objects are used to draw the status bar
	GameObject border = new GameObject(-5, -5, 2.1f, 0.3f);				// A border for the status bar
	GameObject background = new GameObject(-5, 5, 2f, 0.2f);			// The background of the status bar
	GameObject filling = new GameObject(0,0,0,0);						//	The inside of the status bar
	
	
	///////////////////////////////
	//                           //
	//       Constructors        //
	//                           //
	///////////////////////////////
	
	/**
	 * Constructor
	 * @param xLoc			X-coordinate of this object.
	 * @param yLoc			Y-coordinate of this object.
	 * @param fillColor		The color to fill the status bar with when it represents "full"
	 * @param emptyColor	The color to fill the status bar with when it represents "empty"
	 */
	public StatusBar(float xLoc, float yLoc, Color fillColor, Color emptyColor)
	{
		super(xLoc, yLoc, 0, 0);
		
		border.setCenter(xLoc, yLoc);
		border.setColor(Color.BLACK);
		border.draw();
		
		background.setCenter(xLoc, yLoc);
		background.setColor(emptyColor);
		background.draw();
		
		filling.setColor(fillColor);
		filling.draw();
	}
	
	
	////////////////////////
	//                    //
	//      UPDATE        //
	//                    //
	////////////////////////
	
	
	/**
	 * Updates the status bar
	 */
	public void update()
	{
		super.update();
	}
	
	/**
	 * Updates the colors filling the status bar, depending on the current level of the stat it represents
	 * @param proportion	Proportion representing the percentage of the stat the Tower still has
	 */
	public void updateFilling(float proportion)
	{
		float xCenter = border.getCenterX() -.5f;
		float yCenter = border.getCenterY();
		float xLeft = xCenter - 0.5f;
		
		float fillCenter = xLeft + proportion;
		
		filling.setCenter(fillCenter, yCenter);
		filling.setSize(proportion*2f , 0.2f);
	}
	
	
	
	
}
