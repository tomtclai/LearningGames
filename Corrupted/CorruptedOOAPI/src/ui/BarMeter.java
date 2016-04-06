package ui;

import java.awt.Color;

import Engine.GameObject;
import Engine.Vector2;

public class BarMeter extends GameObject {
	protected static final float INNER_BAR_SIZE = .75f;
	protected Vector2 valueBarFullSize;
	GameObject valueBar;
	GameObject backBar;
	
	public final float maxValue;
	float value = 0;	
	
	/**
	 * instantiates a new BarMeter
	 * 
	 * @author Brian Chau
	 * @param position	position on screen to place the Meter
	 * @param theSize size of the Meter
	 * @param max maximum value of this meter
	 */
	public BarMeter(Vector2 position, Vector2 theSize, float max)
	{
		super();
		if(theSize != null)setSize(theSize);
		if(position != null)setCenter(position);
		setImage("BarMeter.png");
		
		backBar = new GameObject(position.getX(), position.getY(), theSize.getX(), theSize.getY());
		backBar.setColor(new Color(45,5,5));
        backBar.setAutoDrawTo(false);
		valueBarFullSize = new Vector2(theSize.getX()*INNER_BAR_SIZE, theSize.getY());
		maxValue = max;
		
		valueBar = new GameObject();
		valueBar.setColor(new Color(191,22,28));
		valueBar.setAutoDrawTo(false);
		setValue(0);
		
	}
	
	/**
	 * sets the level of the RepairMeter
	 * @author Brian Chau
	 * @param newValue level to be set
	 */
	public void setValue(float newValue)
	{
		value = newValue;
		updateBar();
	}
	
	/**
	 * Returns the value inside this meter
	 * @return the current value
	 */
	public float getValue()
	{
		return value;
	}
	
	/**
	 * Returns the maximum value supported by this meter
	 * @return the maximum value
	 */
	public float getMaxValue()
	{
		return maxValue;
	}
	
	/**
	 * increments the level of the RepairMeter
	 * @author Brian Chau
	 * @param inc amount to be incremented
	 */
	public void incrementValue(int inc)
	{
		value += inc;
		updateBar();
	}
	
	/**
	 * resets the BarMeter to 0;
	 */
	public void reset()
	{
		setValue(0);
	}
	
	/**
	 * Checks if the BarMeter is full
	 * @author Brian Chau
	 * @return true if the BarMeter is full
	 */
	public boolean isFull()
	{
		return value == maxValue;
	}	
	
	/**
	 * verifies that the value is within bounds
	 */
	private void verifyValue()
	{
		if (value < 0f)
		{
			value = 0f;
		}
		if (value > maxValue)
		{
			value = maxValue;
		}
	}
	
	
	/**
	 * This method forces the value within bounds and then sets the texture accordingly
	 * @author Brian Chau
	 */
	protected void updateBar()
	{
		verifyValue();
		float percentageFull = value/maxValue;
		Vector2 leftSide = new Vector2(getCenterX()- valueBarFullSize.getX()/2f, getCenterY());
		Vector2 barSize = new Vector2(valueBarFullSize.getX()*percentageFull, getHeight());
		Vector2 barCenter = new Vector2(leftSide.getX()+barSize.getX()/2f, getCenterY());
		valueBar.setCenter(barCenter);
		valueBar.setSize(barSize);
	}
	/**
	 * gets the current percentage of the bar that is filled
	 * @author Brian Chau
	 * @return value between 0 and 1 representing the percent of the bar that is filled
	 */
	public float getPercentFull()
	{
		return value/maxValue;
	}
	
	
	/**
	 * draw the BarMeter
	 */
	public void draw()
	{
		backBar.draw();
		valueBar.draw();
		super.draw();
	} 
}
