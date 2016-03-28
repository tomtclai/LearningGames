package TowerDefense;
/**
 * Barricades are objects that stand on the path to the end goal.
 * They block Walkers from progressing along their paths, and must be destroyed by the walkers.
 * 
 * NOTE:	This object has not been used in the game for a long time,
 * 			and it has not been maintained.  It may need some tweaking
 *			to use.
 * 
 * NOTE:  	No professional art assets were created for this, so they will be needed if this
 * 			class is ever to be used.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class Barricade extends Destructible
{

	/**
	 * Constructor
		 * @param xLoc			X-coordinate of location.
		 * @param yLoc			Y-coordinate of location.
	 */
	public Barricade(float xLoc, float yLoc)
	{
		super(xLoc, yLoc, HTL.CHARACTER_WIDTH * 2f, HTL.CHARACTER_HEIGHT *2f);
		
		this.setTeam("Tower");
		this.setUsingSpriteSheet(false);
		this.updateTexture();
		this.feedbackHealthUp.setImage("transparent.png");
		this.feedbackHealthDown.setImage("transparent.png");
		this.hasHealthAnimations = false;
		draw();
	}
	
	
	/**
	 * Updates the barricade's texture/appearance.
	 */
	public void update()
	{
		this.updateTexture();
		super.update();
	}
	
	
	/**
	 * @return 	True if the barricade has not yet been destroyed.
	 */
	public boolean isValidTarget()
	{
		return !isDead();
	}
	
	
	/////////////////////////
	//                     //
	//       PRIVATE       //
	//                     //
	/////////////////////////

	
	/**
	 * Updates the appearance of the barricade based on its health.
	 */
	private void updateTexture()
	{		
		if(this.getHealthProportion() <= 0)
		{
			this.setVisibilityTo(false);
		}
		else if(this.getHealthProportion() <= 0.25f)
		{
			this.setImage("barricade_025.png");
		}
		else if(this.getHealthProportion() <= 0.50f)
		{
			this.setImage("barricade_050.png");
		}
		else if(this.getHealthProportion() <= 0.75f)
		{
			this.setImage("barricade_075.png");
		}
		else
		{
			this.setImage("barricade_100.png");
		}
	}
}
