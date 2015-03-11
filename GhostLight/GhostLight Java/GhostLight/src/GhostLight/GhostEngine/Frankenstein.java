package GhostLight.GhostEngine;

import Engine.ResourceHandler;
import Engine.Vector2;
import GhostLight.Interface.InteractableObject;
/**
 *  when revealed, Frankenstein will “clobber” the immediately adjacent characters on each side of himself
 *  and turn them into ghosts. If the character next to him is a ghost it will become an angry ghost after 
 *  getting clobbered, if the character is an angry ghost it will explode and remove one of the player’s 
 *  lives, if the space is blank nothing happens. Frankenstein also feeds on electricity – when revealed, 
 *  Frank will drain one segment of the player’s charge beam to feed his rage. Clobbering magnetized the chain 
 *  and pulls everything to Frnak, removing any spaces in the chain.
 * @author Michael Letter
 */

public class Frankenstein extends BaseEnemy{
	
	public static void preLoadResources(ResourceHandler resources){
		resources.loadImage(INTRO_FILENAME);
		resources.loadImage(IDLE_FILENAME);
		resources.loadImage(ABILITY_FILENAME);
		resources.loadImage(DEATH_FILENAME);
		resources.loadImage(BLINK_FILENAME);
	}
	
	private static final String INTRO_FILENAME = "monsters/frank/Frankenstein_Intro_r.png";
	private static final String IDLE_FILENAME = "monsters/frank/Frankenstein_Idle_r.png";
	private static final String ABILITY_FILENAME = "monsters/frank/Frankenstein_SpecialAbility_r.png";
	private static final String DEATH_FILENAME = "monsters/frank/Frankenstein_Death_r.png";
	private static final String BLINK_FILENAME = "monsters/frank/Frankenstein_IdleBlink_r.png";
	
	private boolean allowAbility = false;
	
	public Frankenstein(){
		super();
		
		// Setup animation states
		animStates.addNewState(INTRO_STATE_NAME, INTRO_FILENAME,
				235, 306, 40, 0, new Vector2(DEFAULT_WIDTH * 1.25f, DEFAULT_HEIGHT * 1.1f));
		animStates.addNewState(IDLE_STATE_NAME, IDLE_FILENAME,
				187, 305, 40, 0, new Vector2(DEFAULT_WIDTH * 1.1f, DEFAULT_HEIGHT * 1.1f));
		animStates.addNewState(ABILITY_STATE_NAME, ABILITY_FILENAME,
				451, 305, 40, 0, new Vector2(DEFAULT_WIDTH * 2.7f, DEFAULT_HEIGHT * 1.1f));
		animStates.addNewState(DEATH_STATE_NAME, DEATH_FILENAME,
				559, 339, 20, 1, 
				new Vector2(DEFAULT_DEATH_WIDTH * 1.5f, DEFAULT_DEATH_HEIGHT));
		animStates.addNewState(BLINK_STATE_NAME, BLINK_FILENAME,
				187, 305, 40, 0, new Vector2(DEFAULT_WIDTH  * 1.1f, DEFAULT_HEIGHT  * 1.1f));
		
		type = InteractableObject.ObjectType.FRANKENSTEIN;
		isTypeRevealed = false;
		unrevealType();
		health.setMaxSegments(InteractableObject.getDefualthealth(type));
		health.setFilledSegments(health.getMaxSegments());
		score = InteractableObject.getDefualtScore(type);
		 super.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
	}
	public BaseEnemy clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void shiftImageState(){
		if(isTypeRevealed){
	      	animStates.setCycleState(IDLE_STATE_NAME,
	      			BLINK_STATE_NAME,
	      			DEFAULT_IDLE_TIME,
	      			DEFAULT_BLINK_TIME);
		}
		else{
			super.shiftImageState();
		}
	}
	public StateChange revealType(int pauseTime){
		if(!isTypeRevealed){
			EnemySoundManager.requestSound(EnemySoundManager.SoundType.FRANKENSTIEN_INTRO);
			super.revealType(pauseTime);
			storedStateChangeObject.setToDefualts();
		    storedStateChangeObject.changInBatteryCharge = -0.5f;
		    allowAbility = true;
		}
		return storedStateChangeObject;
	}
	//any changes to state as a result of clobber are added to storedStateChangeObject
	private void clobber(int row, int collumn, int animationTime){
		if(hostSet != null){
			BaseEnemy target = hostSet.getEnemyAt(row, collumn);
			if(target != null){
				if(target.type == InteractableObject.ObjectType.ANGRY){
					storedStateChangeObject.add(((Ghost)target).explode());
				}
				else{
					if(target.type != InteractableObject.ObjectType.GHOST){
						target.removeThis();
						Ghost newGhost = new Ghost();
						newGhost.center.set(target.center);
						newGhost.revealType(0);
						hostSet.addEnemy(newGhost, row, collumn);
						newGhost.setAnimationTarget(row, collumn);
						newGhost.revealType(animationTime);
					}
					else{
						
						if(target.isTypeRevealed)
						{
							((Ghost)target).MakeAngry();
						}
						else
						{
							target.revealType(animationTime);
						}
					}
				}
			}
		}
	}
	@Override
	public void playWaitingIntroductions(int timeAllowed) 
	{
		if(justRevealed)
		{
			animStates.shiftTimerStateThenCycle(INTRO_STATE_NAME,
	  				IDLE_STATE_NAME,
	  				timeAllowed,
	  				DEFAULT_IDLE_TIME,
	  				DEFAULT_BLINK_TIME,
	  				BLINK_STATE_NAME);
			justRevealed = false;
		}
	}
	@Override
	public StateChange activateAbility(int timeAllowed) 
	{
		if(allowAbility)
		{
	    	clobber(currentRow, currentCollumn - 1, timeAllowed);
	    	clobber(currentRow, currentCollumn + 1, timeAllowed);
	    	
			animStates.shiftTimerStateThenCycle(ABILITY_STATE_NAME,
	  				IDLE_STATE_NAME,
	  				timeAllowed,
	  				DEFAULT_IDLE_TIME,
	  				DEFAULT_BLINK_TIME,
	  				BLINK_STATE_NAME);
	    	
	    	allowAbility = false;
		}
    	
    	return null;
	}
	protected void setDeathAnimation(int deathDuration) {
		animStates.changeState(DEATH_STATE_NAME, null);
		
	}
}
