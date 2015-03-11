package ui;

import Engine.Vector2;

public class RowMeter extends AnimMeter{

	
	/**
	 * instantiates a new RowMeter
	 * @author Brian Chau
	 * @param position	position on screen to place the RowMeter
	 * @param size size of the RowMeter
	 * @param maxValue maximum representable value (Meter can represent values between 0 and maxValue)
	 */
	public RowMeter(Vector2 position, Vector2 size, int maxValue)
	{
		//calculate preferred position based on World size
		//TODO: i could prob make this pull world directly and not need coords as input
		super(position,size, "RowMeter", 11, maxValue);
	}
	



}
