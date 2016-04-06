package structures;

/**
 * This is a turn based timer. Set a max interval and it will count down when you call tick
 * tick returns true and resets the timer to the original max interval
 * @author Lappy
 *
 */
public class TurnTimer {
	int currentValue;
	int interval;
	
	public TurnTimer(int max)
	{
		setInterval(max);
		reset();
	}
	
	public void reset()
	{
		currentValue = interval;
	}
	
	public void setInterval(int max)
	{
		interval = max;
	}
	
	public int howLong()
	{
		return currentValue;
	}
	
	public boolean tick()
	{
		currentValue--;
		if (currentValue <= 0)
		{
			reset();
			return true;
		}
		return false;
	}
}
