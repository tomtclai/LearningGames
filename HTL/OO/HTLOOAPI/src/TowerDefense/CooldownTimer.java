package TowerDefense;
import java.util.Date;
import java.util.LinkedList;


/**
 * CooldownTimer represents a timer that counts down from a certain number of seconds to zero.
 * Users can then check the timer to see if it is done or not.
 * The timer can be reset and used again!
 * This class used to be pretty straightforward, but then we added the ability to pause the game...
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class CooldownTimer
{	
	private long cooldownDuration = 1000;	// number of milliseconds this timer takes to finish
	
	private long cooldownStartTime;			// timestamp of when the most recent cooldown began	
	
	private long pauseStartTime = -1;		// timestamp of when the most recent pause began
											// -1 means we're not paused now
	
	private long pausedDuration = 0;		// Amount of time the timer has been paused total, if any.
											// Is updated each time the game unpauses
	
	// the list of timers is used so that if an application is paused, all timers can
	// easily be paused or unpaused
	static LinkedList<CooldownTimer> timerList = new LinkedList<CooldownTimer>();
	
	
	/**
	 * Indiscriminately pauses every CooldownTimer.
	 */
	static public void pauseAll()
	{
		for(CooldownTimer timer : timerList)
		{
			timer.pause();
		}	
	}
	
	
	/**
	 * Indiscriminately unpauses every CooldownTimer.
	 */
	static public void unpauseAll()
	{
		for(CooldownTimer timer : timerList)
		{
			timer.unpause();
		}	
	}
	
	
	
	
	/**
	 * Constructor.
	 * Initializes the number of seconds to countdown from and starts the timer counting down.
	 * @param seconds	The number of seconds to start the countdown from.
	 */
	public CooldownTimer(float seconds)
	{
		timerList.add(this);
		this.start(seconds);
	}
	
	
	
	
	
	
	
	
	/**
	 * @return	True if this timer is in a paused state.
	 */
	public boolean isPaused()
	{
		return pauseStartTime != -1;
	}
	
	
	/**
	 * @return	The amount of time this timer has been in its current pause state.
	 */
	public long getCurrentPausedDuration()
	{
		if(isPaused())
		{
			long currentTime = new Date().getTime();
			return currentTime - pauseStartTime;
		}
		else
		{
			return 0;
		}
	}
	
	
	/**
	 * Pauses this timer.
	 * @return		False if the timer was already paused.
	 */
	public boolean pause()
	{
		if(isPaused()) return false;
		
		pauseStartTime = new Date().getTime();		
		return true;		
	}
	
	
	/**
	 * Unpauses this timer.
	 * @return		False if this timer wasn't paused.
	 */
	public boolean unpause()
	{
		if(!isPaused()) return false;
		
		long currentTime = new Date().getTime();		
		long lastPauseDuration = currentTime - pauseStartTime;

		long durationToCutOff = 0;
		
		// if this timer started while it was paused...
		if(cooldownStartTime > pauseStartTime)
		{
			// remove the chunk of the pause duration that this timer was inactive 
			durationToCutOff = cooldownStartTime - pauseStartTime;
		}
		
		lastPauseDuration -= durationToCutOff;		
		pausedDuration += lastPauseDuration;	
		
		pauseStartTime = -1;		
		return true;
	}

	
	
	
	
	
	


	
	/**
	 * Set the number of seconds to countdown from next time.
	 * Note that this does not affect any currently active timers.
	 * @param seconds		The number of seconds to start the next countdown from.
	 */
	public void setDuration(float seconds)
	{
		cooldownDuration = (long)(seconds * 1000);
	}

	
	/**
	 * Start the timer from a set number of seconds.
	 * If the timer is already being used, tosses out the old countdown and replaces it with this new one.
	 * @param seconds		The number of seconds to start the countdown from.
	 */
	public void start(float seconds)
	{		
		this.setDuration(seconds);
		start();
	}
	
	
	/**
	 * Start the timer from the last set number of seconds.
	 * If the timer is already being used, tosses out the old countdown and replaces it with this new one.
	 */
	public void start()
	{
		this.pausedDuration = 0;
		cooldownStartTime = new Date().getTime();
	}
	
	
	/**
	 * Checks whether the timer has finished its countdown.
	 * @return		True if the timer has reached zero.
	 */
	public boolean isReady()
	{		
		long currentPausedDuration = getCurrentPausedDuration();		
		long timeWhenFinished = cooldownStartTime + cooldownDuration + pausedDuration + currentPausedDuration;
		
		long currentTime = new Date().getTime();
		
		return timeWhenFinished <= currentTime;		
	}
	
	
	
	
	/**
	 * Gets the number of seconds until this timer will be done.
	 * @return		The amount of seconds left until this timer will be done.
	 */
	public long getSecondsRemaining()
	{
		long currentPausedDuration = getCurrentPausedDuration();		
		long timeWhenFinished = cooldownStartTime + cooldownDuration + pausedDuration + currentPausedDuration;

		long currentTime = new Date().getTime();
		
		long durationUntilFinished = timeWhenFinished - currentTime;
		
		durationUntilFinished /= 1000;	// convert from milliseconds to seconds		
		
		if (durationUntilFinished <= 0){
			return 0;
		}
		else
		{
			return durationUntilFinished;
		}
	}
	
}