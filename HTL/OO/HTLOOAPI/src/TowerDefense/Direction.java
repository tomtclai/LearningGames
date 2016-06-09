package TowerDefense;
/**
 * Simple little enum that represents four directions.
 * @author Branden
 */
public enum Direction
{
	UP, DOWN, LEFT, RIGHT;

	public static Direction oppositeOf(Direction direction)
	{
		switch(direction)
		{
			case UP:		return DOWN;
			case DOWN:		return UP;
			case LEFT:		return RIGHT;
			case RIGHT:		return LEFT;
			default:		return null;
		}
	}
	
	/**
	 * Mods your input by 4, then returns the consistent, corresponding Direction.
	 * Intended to allow easy use of the 4 directions in loops that want to use all directions.
	 * @param number		The number to be modded.
	 * @return				The Direction associated with the mod result.
	 */
	public static Direction getOrderedDirection(int number)
	{
		number = number%4;
		
		switch(number)
		{
			case 0:		return Direction.UP;
			case 1:		return Direction.DOWN;
			case 2:		return Direction.LEFT;
			default:	return Direction.RIGHT;
		}		
	}
}
