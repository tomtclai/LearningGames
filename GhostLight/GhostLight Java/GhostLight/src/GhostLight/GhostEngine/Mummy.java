package GhostLight.GhostEngine;

import Engine.BaseCode;
import Engine.ResourceHandler;
import Engine.Vector2;
import GhostLight.Interface.InteractableObject;

/**
 * when revealed, the mummy turns everything in the current row back to the unraveled 
 * state  except for itself.
 * @author Michael Letter
 */
public class Mummy extends BaseEnemy{
	
	private static final String INTRO_FILENAME = "monsters/mummy/Mummy_Intro_r.png";
	private static final String IDLE_FILENAME = "monsters/mummy/Mummy_IdleBlink_r.png"; //TODO: Missing
	private static final String ABILITY_FILENAME = "monsters/mummy/Mummy_SpecialAction_r.png";
	private static final String DEATH_FILENAME = "monsters/mummy/Mummy_Death_r.png";
	private static final String BLINK_FILENAME = "monsters/mummy/Mummy_IdleBlink_r.png";
	
	
	public static void preLoadResources(ResourceHandler resources){
		resources.loadImage(INTRO_FILENAME);
		resources.loadImage(IDLE_FILENAME);
		resources.loadImage(ABILITY_FILENAME);
		resources.loadImage(DEATH_FILENAME);
		resources.loadImage(BLINK_FILENAME);
	}
	
	private boolean allowAbility = false;
	
  public Mummy() {
	  super();
	  
		// Setup animation states
		animStates.addNewState(INTRO_STATE_NAME, INTRO_FILENAME,
				255, 302, 40, 0, new Vector2(DEFAULT_WIDTH * 1.5f, DEFAULT_HEIGHT));
		animStates.addNewState(IDLE_STATE_NAME, IDLE_FILENAME,
				203, 302, 40, 0, new Vector2(DEFAULT_WIDTH * 1.1f, DEFAULT_HEIGHT));
		animStates.addNewState(ABILITY_STATE_NAME, ABILITY_FILENAME,
				762, 301, 40, 0, new Vector2(DEFAULT_WIDTH * 4.5f, DEFAULT_HEIGHT));
		animStates.addNewState(DEATH_STATE_NAME, DEATH_FILENAME,
				559, 339, 20, 0, new Vector2(DEFAULT_DEATH_WIDTH, DEFAULT_DEATH_HEIGHT));
		animStates.addNewState(BLINK_STATE_NAME, BLINK_FILENAME,
				203, 302, 40, 0, new Vector2(DEFAULT_WIDTH * 1.1f, DEFAULT_HEIGHT));
	  
	  type = InteractableObject.ObjectType.MUMMY;
	  unrevealType();
	  super.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
	  health.setMaxSegments(InteractableObject.getDefualthealth(type));
		health.setFilledSegments(health.getMaxSegments());
	    score = InteractableObject.getDefualtScore(type);
  }
  protected void shiftImageState(){
    if(isTypeRevealed) {
      	animStates.setCycleState(IDLE_STATE_NAME,
      			BLINK_STATE_NAME,
      			DEFAULT_IDLE_TIME * BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
  				DEFAULT_BLINK_TIME + BaseCode.random.nextInt(DEFAULT_BLINK_TIME));
    }
    else
    {
    	super.shiftImageState();
    }
  }
  public StateChange revealType(int pauseTime){
	  if(!isTypeRevealed){
		  EnemySoundManager.requestSound(EnemySoundManager.SoundType.MUMMY_INTRO);
		  super.revealType(pauseTime);
		  allowAbility = true;
	  }
    return storedStateChangeObject;
  }
  @Override
  public void playWaitingIntroductions(int timeAllowed) 
  {
  	if(justRevealed)
  	{
		animStates.shiftTimerStateThenCycle(INTRO_STATE_NAME,
  				IDLE_STATE_NAME,
  				timeAllowed,
  				DEFAULT_IDLE_TIME * BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
  				DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME),
  				BLINK_STATE_NAME);
  		justRevealed = false;
  	}
  }
@Override
public StateChange activateAbility(int timeAllowed) {
	if(allowAbility)
	{
	  if(hostSet != null){
		  for(int loop = 0; loop < hostSet.getNumberOfCollumns(currentRow); loop++){
			  BaseEnemy target = hostSet.getEnemyAt(currentRow, loop);
			  if(target != null && target != this){
				  target.unrevealType();
			  }
		  }
	  }
	  allowAbility = false;
	  animStates.shiftTimerStateThenCycle(ABILITY_STATE_NAME,
  				IDLE_STATE_NAME,
  				timeAllowed,
  				DEFAULT_IDLE_TIME * BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
  				DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME),
  				BLINK_STATE_NAME);
	}
	return null;
}
protected void setDeathAnimation(int deathDuration) {
	animStates.changeState(DEATH_STATE_NAME, null);
	
}
}
