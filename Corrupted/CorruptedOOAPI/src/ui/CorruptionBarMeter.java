package ui;

import java.awt.image.BufferedImage;

import Engine.BaseCode;
import Engine.GameObject;
import Engine.Vector2;

public class CorruptionBarMeter extends BarMeter {

	private float losePercent = 1f;
	private GameObject loseMarker;
	private static final int numTex = 4;
	
	private static BufferedImage[] textureList;
	static{
		textureList = new BufferedImage[numTex];
		for (int i = 0; i < numTex; i++)
		{
			textureList[i]= BaseCode.resources.loadImage("CorruptionMarker"+i+".png");
		}
	}
	
	/**
	 * Instatiates a new CorruptionBarMeter
	 * @author Brian Chau
	 * @param position position to put the Meter
	 * @param theSize size of the meter
	 * @param max maximum value (possible values represented by meter is 0-max)
	 * @param losepercent this percent (0-1f) on the bar will have a skull marker placed there.
	 */
	public CorruptionBarMeter(Vector2 position, Vector2 theSize, float max, float losepercent)
	{
		super(position, theSize, max);
		loseMarker = new GameObject();
		loseMarker.setSize(getHeight());
		setLosePercent(losepercent);
		updateBar();
	}
	
	/**
	 * This method forces the value within bounds and then sets the texture of the skull icon accordingly.
	 * at 33% of losePercent, line turns yellow
	 * at 66% of losePercent, line turns orange
	 * at 100% of losePercent, like turns red
	 * @author Brian Chau
	 */
	@Override
	public void updateBar()
	{
		//handle tex swap
		super.updateBar();
		float incrementfactor = losePercent/(numTex-1);
	    int index = (int)(this.getPercentFull()/incrementfactor);
	    if(index >= numTex)
	    {
	    	index = numTex-1;
	    }
	    if(loseMarker != null){
	    	loseMarker.setImage(textureList[index]);
	    }
	}
	
	/**
	 * sets the losePercent marker
	 * @author Brian Chau
	 * @param losepercent percentace point to place the marker
	 */
	public void setLosePercent(float losepercent)
	{
		//set value
		if(losepercent > 1f)
		{
			losepercent = 1f;
		}
		if(losepercent < 0f)
		{
			losepercent = 0f;
		}
		losePercent = losepercent;
		
		//handle geometry
		loseMarker.setCenter(getCenterX()-valueBarFullSize.getX()/2f + valueBarFullSize.getX()*losePercent, getCenterY());
		
	}
	
	/**
	 * gets the losePercent
	 * @return the percentage setting of the lose percent marker (0-1f)
	 */
	public float getLosePercent()
	{
		return losePercent;
	}
	
	/**
	 * draws the BarMeter
	 */
	public void draw()
	{
		super.draw();
		loseMarker.draw();
	}
}
