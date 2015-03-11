package ui;

import java.awt.image.BufferedImage;

import Engine.BaseCode;
import Engine.GameObject;
import Engine.Vector2;

abstract class AnimMeter extends GameObject {
	private BufferedImage[] textures;

	
	public final float MaxValue;
	public final float ValueThreshold;
	float value = 0;	
	
	/**
	 * instantiates a new Meter.
	 * This constructor will look for textures named [textureBaseName] appended with numbers from 0 to numTextures
	 * to use as textures where 0 is empty and numTextures is full (must be png). These textures will be displayed
	 * in accordance to the Meter's value between 0 and maxValue (which may not necessarily be equal to numTextures)
	 * values supported by Meter range from0 to maxValue
	 * 
	 * @author Brian Chau
	 * @param position	position on screen to place the Meter
	 * @param size size of the Meter
	 * @param textureBaseName base name of the texture (without numeral counter or file extension)
	 * @param numTextures number of textures
	 * @param maxValue maximum value of this meter
	 */
	public AnimMeter(Vector2 position, Vector2 theSize, String textureBaseName, int numTextures, float maxValue)
	{
		super();
		if(theSize != null)setSize(theSize);
		if(position != null)setCenter(position);
		if(maxValue <= 0) maxValue = 1;
		MaxValue = maxValue;
		ValueThreshold = maxValue/(numTextures-1);
		textures =new BufferedImage[numTextures];
		for(int i = 0; i < numTextures; i++)
		{
			textures[i] = BaseCode.resources.loadImage(textureBaseName+i+".png");
		}
		setValue(0);
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
		if (value > MaxValue)
		{
			value = MaxValue;
		}
	}
	
	/**
	 * sets the level of the RepairMeter
	 * @param newValue level to be set
	 */
	public void setValue(float newValue)
	{
		value = newValue;
		updateTexture();
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
		return MaxValue;
	}
	
	/**
	 * increments the level of the RepairMeter
	 * @param inc amount to be incremented
	 */
	public void incrementValue(int inc)
	{
		value += inc;
		updateTexture();
	}
	
	/**
	 * resets the RepairMeter to 0;
	 */
	public void reset()
	{
		setValue(0);
	}
	
	/**
	 * checks if the RepairMeter is fully charged
	 * @return true if fully charged
	 */
	public boolean isFull()
	{
		return value >= MaxValue;
	}
	
	/**
	 * This method forces the value within bounds and then sets the texture accordingly
	 */
	private void updateTexture()
	{
		verifyValue();
		int index = 0;
		if(value >= MaxValue)
		{
			index = textures.length - 1;
		}
		else{
			index = (int)(value/ValueThreshold);
		}
		setImage(textures[index]);	
	}
}
