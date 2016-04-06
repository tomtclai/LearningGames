package ui;

import Engine.Vector2;

public class RepairMeter extends AnimMeter{

	/**
	 * instantiates a new RepairMeter
	 * @author Brian Chau
	 * @param position	position on screen to place the RepairMeter
	 * @param size size of the RepairMeter
	 * @param maxValue maximum representable value (Meter can represent values between 0 and maxValue)
	 */
	public RepairMeter(Vector2 position, Vector2 size, int maxValue)
	{
		//calculate preferred position based on World size
		//TODO: i could prob make this pull world directly and not need coords as input
		super(position,size, "RepairMeter", 11, maxValue);
	}
	



}
