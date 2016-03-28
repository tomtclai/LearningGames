package TowerDefense;

import java.awt.Color;

/**
 * Status bar representing a Tower's health
 * Hasn't been used in forever, and may be out of date.
 * 
 *  @author Branden Drake
 *  @author Rachel Horton
 */

public class StatusBarHealth extends StatusBar
{	
	Tower tower = null;  			//The Tower whose stats are being displayed
	
	
	///////////////////////////////
	//                           //
	//       Constructors        //
	//                           //
	///////////////////////////////
	
	
	/**
	 * Constructor
	 * @param xLoc			X-coordinate of this object.
	 * @param yLoc			Y-coordinate of this object.
	 * @param fillColor		The color representing "Full"
	 * @param emptyColor	The color representing "Empty"
	 * @param deffy			The tower whose stats are being displayed
	 */
	public StatusBarHealth(float xLoc, float yLoc, Color fillColor, Color emptyColor, Tower deffy)
	{
		super(xLoc, yLoc, fillColor, emptyColor);

		tower = deffy;
		float hp = tower.getHealthProportion();
		updateFilling(hp);
	}
	
	
	///////////////////////////////
	//                           //
	//          Update           //
	//                           //
	///////////////////////////////
	
	/**
	 * Updates the badFeels status bar
	 */
	public void update()
	{
		float hp = tower.getHealthProportion();
		updateFilling(hp);
		
		super.update();
	}
	
	

	
	
	
}
