package GhostLight.GhostEngine;
import java.util.HashMap;

import Engine.Rectangle;
import Engine.Vector2;

/**
 * Handles animations so that when triggered from anywhere in the code,
 * the changes in the animation are only caught on the edge.
 * 
 * An important note is that the timeing here does not reflect the number of
 * draw ticks, because update and draw are not always subsequent.
 * 
 * @author Fernando Arnez
 *
 * */
public class AnimationStateHandler 
{
	// Contains info for a spritesheet
	private class SpriteInfo
	{
		public String filename;
		public boolean isSprite;
		public int spriteWidth;
		public int spriteHeight;
		public int spriteFrames;
		public int spriteTicks;
		public int firstFrame;
		public int lastFrame;
		public Vector2 positionOffset = null;
		public Rectangle.SpriteSheetAnimationMode animationMode;
		public Vector2 imageSize;
	}
	private Rectangle sprite;
	private String currentState;
	private Vector2 defaultPosition = null;
	private StateRule stateRule = null;
	
	private HashMap<String, SpriteInfo > states;
	
	AnimationStateHandler(Rectangle sprite)
	{
		states = new HashMap<String, SpriteInfo>();
		this.sprite = sprite;
		currentState = "";
	}

	/**
	 * Adds state as a static image.
	 * @param state
	 * @param filename
	 */
	public void addNewState(String state, String filename, Vector2 size)
	{
		SpriteInfo temp = new SpriteInfo();
		temp.filename = filename;
		temp.isSprite = false;
		temp.imageSize = size;
		states.put(state, temp);
	}
	
	/**
	 * Adds a animated state
	 * Uses default animationMode (Forward)
	 * @param state
	 * @param filename
	 * @param spriteWidth
	 * @param spriteHeight
	 * @param spriteFrames
	 * @param spriteTicks
	 */
	public void addNewState(String state, String filename,
					   int spriteWidth, int spriteHeight, int spriteFrames,
					   int spriteTicks, Vector2 size)
	{
		SpriteInfo temp = new SpriteInfo();
		temp.filename = filename;
		temp.spriteWidth = spriteWidth;
		temp.spriteHeight = spriteHeight;
		temp.spriteFrames = spriteFrames;
		temp.spriteTicks = spriteTicks;
		temp.firstFrame = 0;
		temp.lastFrame = spriteFrames -1;
		temp.animationMode = Rectangle.SpriteSheetAnimationMode.ANIMATE_FORWARD;
		temp.isSprite = true;
		temp.imageSize = size;
		states.put(state, temp);
	}
	
	/**
	 * Adds a animated state
	 * @param state
	 * @param filename
	 * @param spriteWidth
	 * @param spriteHeight
	 * @param spriteFrames
	 * @param spriteTicks
	 */
	public void addNewState(String state, String filename,
					   int spriteWidth, int spriteHeight, int spriteFrames,
					   int spriteTicks,
					   int firstFrame, int lastFrame,
					   Rectangle.SpriteSheetAnimationMode animationMode,
					   Vector2 size)
	{
		SpriteInfo temp = new SpriteInfo();
		temp.filename = filename;
		temp.spriteWidth = spriteWidth;
		temp.spriteHeight = spriteHeight;
		temp.spriteFrames = spriteFrames;
		temp.firstFrame = firstFrame;
		temp.lastFrame = lastFrame;
		temp.animationMode = animationMode;
		temp.spriteTicks = spriteTicks;
		temp.isSprite = true;
		temp.imageSize = size;
		states.put(state, temp);
	}
	
	/**
	 * Sets the default position for all states.
	 * This must be set if there are any states with offsets.
	 * State positions offset from this.
	 * @param position
	 */
	public void setDefaultPosition(Vector2 position)
	{
		defaultPosition = position;
	}
	/**
	 * Returns the current default position. If it is not set,
	 * will return null.
	 * @return
	 */
	public Vector2 getDefaultPosition()
	{
		return defaultPosition;
	}
	
	/**
	 * Give a new position to default position
	 * and recalculates the sprite's offset position;
	 * Will give error if defaultPosition is not set.
	 * @param x
	 * @param y
	 */
	public void updatePosition(float x, float y)
	{
		defaultPosition.set(x, y);
		Vector2 offset = states.get(currentState).positionOffset;
		if(offset != null)
		{
			sprite.center.set(defaultPosition.getX() + offset.getX(),
					defaultPosition.getY() + offset.getY());
		}
		else
		{
			sprite.center.set(defaultPosition);
		}
	}
	
	/**
	 * Adds an offset to position that the state will put the sprite in
	 * when the state switched to.
	 * The defaultPosition must be set, or this won't have an effect.
	 * 
	 * @param state
	 * @param offset
	 */
	public void addPositionOffsetToState(String state, Vector2 offset)
	{
		SpriteInfo info = states.get(state);
		if(info != null)
			info.positionOffset = offset;
	}
	
	/**
	 * Change state only activates when the state is a different one.
	 * Will remove any state rule.
	 * @param state
	 */
	public void changeState(String state)
	{
		stateRule = null;
		changeStateWithStateRule(state);
	}
	
	/*
	* Change state only activates when the state is a different one.
	* Will keep any state rule currently in the system.
	*/
	private void changeStateWithStateRule(String state)
	{
		if(!currentState.equals(state))
		{
			SpriteInfo info = states.get(state);
			if(info != null)
			{
				if(info.isSprite)
				{
					sprite.setSpriteSheet(info.filename, info.spriteWidth,
							info.spriteHeight, info.spriteFrames,
							info.spriteTicks);
					sprite.setAnimationMode(info.firstFrame, info.lastFrame,
							info.animationMode);
					sprite.setUsingSpriteSheet(true);
				}
				else
				{
					sprite.setImage(info.filename);
					sprite.setUsingSpriteSheet(false);
				}
				sprite.size = info.imageSize;
				
				if(defaultPosition != null)
				{
					if(info.positionOffset == null)
					{
						sprite.center.set(defaultPosition);
					}
					else
					{
						sprite.center.set(defaultPosition.getX() + info.positionOffset.getX(),
								defaultPosition.getY() + info.positionOffset.getY());
					}
				}
				currentState = state;
			}
		}
	}

	/**
	 * Will set the state to the newStateName, then, in timeAllowed updated ticks,
	 * the state will change to nextStateName.
	 */
	public void shiftTimerState(String newStateName, String nextStateName,
			int timeAllowed) 
	{
		if(states.get(newStateName) != null && states.get(nextStateName) != null)
		{
			stateRule = new SetThenShiftStateRule(this, newStateName,
					nextStateName, timeAllowed );
		}
	}
	/**
	 * Sets the state rule to shift state, then sets to cycle rule.
	 * @param newStateName       The state immediately shifted to.
	 * @param nextStateName		 The state that will be after timeAllowed
	 * @param timeAllowed        The amount of ticks the state will be newStateName
	 * @param cycleInterval      The amount of ticks the state will be the main cycle state
	 * 								(in this case, nextStateName)
	 * @param otherStateInterval The amount of ticks the state will be in the non-main cycle
	 * 								state.
	 * @param cycleState         The state that will be cycled to during the Cycle rule.
	 */
	public void shiftTimerStateThenCycle(String newStateName, String nextStateName,
			int timeAllowed, int cycleInterval, int otherStateInterval, String cycleState ) 
	{
		if(states.get(newStateName) != null && states.get(nextStateName) != null)
			stateRule = new SetThenShiftStateRule(
							this, 
							newStateName,
							nextStateName, 
							timeAllowed, 
								new CycleStateRule(
										this,
										nextStateName, 
										cycleState,
										cycleInterval,
										otherStateInterval) );
	}
	/**
	 *	Updates a single tick in animation manager. Allows for
	 *	timed switches in animation.
	 */
	public void update()
	{
		if(stateRule != null)
		{
			stateRule.update();
		}
	}
	
	/**
	 * Changes the state and the rule.
	 * Set rule to null to stop it.
	 * @param stateName
	 * @param rule
	 */
	public void changeState(String stateName, StateRule rule) 
	{
		changeState(stateName);
		stateRule = rule;
	}
	
	/*
	 * Sets the animation to cycle between two states
	 * Will ignore if already in the same cycle. (Sameness is judged only on
	 * mainState and otherState, not the intervals)
	 */
	public void setCycleState(String mainState, String otherState,
			int mainInterval, int otherStateInterval)
	{
		StateRule cycleRule = new CycleStateRule(
				this,
				mainState,
				otherState,
				mainInterval,
				otherStateInterval);
		if(stateRule == null || !stateRule.equals(cycleRule))
		{	
			changeState(mainState, cycleRule);
		}
	}
	
	
	//***********************************************************************
	// State Rules
	//***********************************************************************
	
	
	/**
	 * A interface to allow any type of state rule to be added.
	 * @author Fernando Arnez
	 *
	 */
	private interface StateRule
	{
		public abstract void update();
		public abstract boolean equals(Object object);
	}
	
	/**
	 * Will first set the state, then wait a duration, then 
	 * switch to the next state. Will set set staterule to null
	 * or set a new state rule afterwords.
	 * 
	 * @author Fernando Arnez
	 *
	 */
	private class SetThenShiftStateRule implements StateRule
	{
		AnimationStateHandler stateMachine;
		String nextState;
		int waitTime;
		StateRule newStateRule = null;
		
		SetThenShiftStateRule(AnimationStateHandler sMachine,
							  String setToState, String nextState,
							  int waitTime)
		{
			stateMachine = sMachine;
			sMachine.changeStateWithStateRule(setToState);
			this.nextState = nextState;
			this.waitTime = waitTime;
		}
		SetThenShiftStateRule(AnimationStateHandler sMachine,
				  String setToState, String nextState, int waitTime,
				  StateRule newSRule)
		{
			stateMachine = sMachine;
			sMachine.changeStateWithStateRule(setToState);
			this.nextState = nextState;
			this.waitTime = waitTime;
			newStateRule = newSRule;
		}
		
		public void update()
		{
			// An animation is going to expire.
			if(waitTime > 0)
			{
				waitTime--;
				if(waitTime == 0)
				{
					stateMachine.changeStateWithStateRule(nextState);
					stateMachine.stateRule = newStateRule;
				}
			}
		}
		
		public boolean equals(Object object)
		{
			if(object instanceof SetThenShiftStateRule)
			{
				SetThenShiftStateRule other = (SetThenShiftStateRule) object;
				if(this.nextState.equals(other.nextState) &&
						this.waitTime == other.waitTime)
					return true;
			}
			return false;
		}
	}
	
	private class CycleStateRule implements StateRule
	{
		private String mainState;
		private String otherState;
		private AnimationStateHandler stateMachine;
		private int cycleTime;
		private int otherStateTime;
		private int counter;
		
		CycleStateRule(AnimationStateHandler sMachine,
				String mainState, String otherState, int cycleTime,
				int otherStateTime)
		{
			stateMachine = sMachine;
			this.mainState = mainState;
			this.otherState = otherState;
			this.cycleTime = cycleTime;
			this.otherStateTime = otherStateTime;
			counter = 0;
		}


		public void update() 
		{
			if(counter <= 0)
			{
				if(stateMachine.currentState.equals(mainState))
				{
					counter = otherStateTime;
					stateMachine.changeStateWithStateRule(otherState);
				}
				else
				{
					counter = cycleTime;
					stateMachine.changeStateWithStateRule(mainState);
				}
			}
			else
			{
				counter--;
			}
		}
		
		public boolean equals(Object object)
		{
			if(object instanceof CycleStateRule)
			{
				CycleStateRule other = (CycleStateRule) object;
				if(this.mainState.equals(other.mainState) &&
						this.otherState.equals(other.otherState))// &&
						//this.cycleTime == other.cycleTime &&
						//this.otherStateTime == other.otherStateTime)
					return true;
			}
			return false;
		}
		
	}

	
}
