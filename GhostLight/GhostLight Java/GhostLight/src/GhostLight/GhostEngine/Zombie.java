
package GhostLight.GhostEngine;

import Engine.BaseCode;
import Engine.ResourceHandler;
import Engine.Vector2;
import GhostLight.Interface.InteractableObject;

/**
 * Zombie: when revealed, the Zombie “infects” the two adjacent spaces on each side of his character 
 * for 3 turns, making anything in those spaces invulnerable to any beam except the charge beam. The 
 * Zombie is worth 40 points.
 *
 * @author Michael Letter
 */
public class Zombie extends BaseEnemy{
	
	private static final String INTRO_FILENAME = "monsters/zombie/Zombie_Intro_r.png";
	private static final String IDLE_FILENAME = "monsters/zombie/Zombie_Idle_r.png";
	private static final String ABILITY_FILENAME = "monsters/zombie/Zombie_SpecialAction_r.png";
	private static final String DEATH_FILENAME = "monsters/zombie/Zombie_Death_r.png";
	private static final String BLINK_FILENAME = "monsters/zombie/Zombie_IdleBlink_r.png";
	
	private boolean allowAbility = false;
	
	
	public static void preLoadResources(ResourceHandler resources){
		resources.loadImage(INTRO_FILENAME);
		resources.loadImage(IDLE_FILENAME);
		resources.loadImage(ABILITY_FILENAME);
		resources.loadImage(DEATH_FILENAME);
		resources.loadImage(BLINK_FILENAME);
	}
	
  public Zombie() {
	  super();
	  
		// Setup animation states
		animStates.addNewState(INTRO_STATE_NAME, INTRO_FILENAME,
				217, 304, 40, 1, new Vector2(DEFAULT_WIDTH  * 1.2f, DEFAULT_HEIGHT));
		animStates.addNewState(IDLE_STATE_NAME, IDLE_FILENAME,
				176, 304, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		animStates.addNewState(ABILITY_STATE_NAME, ABILITY_FILENAME,
				543, 304, 40, 0, new Vector2(DEFAULT_WIDTH * 3f, DEFAULT_HEIGHT));
		animStates.addNewState(DEATH_STATE_NAME, DEATH_FILENAME,
				559, 339, 20, 0, new Vector2(DEFAULT_DEATH_WIDTH, DEFAULT_DEATH_HEIGHT));
		animStates.addNewState(BLINK_STATE_NAME, BLINK_FILENAME,
				176, 304, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	  
	  type = InteractableObject.ObjectType.ZOMBIE;
	  unrevealType();
	  health.setMaxSegments(InteractableObject.getDefualthealth(type));
	  health.setFilledSegments(health.getMaxSegments());
	  score = InteractableObject.getDefualtScore(type);
	  super.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
  }

  protected void shiftImageState() {
    if(isTypeRevealed) {
      	animStates.setCycleState(IDLE_STATE_NAME,
      			BLINK_STATE_NAME,
  				DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
  				DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME));
    }
    else
    {
        super.shiftImageState();
    }

  }
  public StateChange revealType(int pauseTime){
	  super.revealType(pauseTime);
	  EnemySoundManager.requestSound(EnemySoundManager.SoundType.ZOMBIE_INTRO);
	  allowAbility = true;
	  
	  return null;
  }
  /**
   * Will Infect target Enemy for the give timeInfected
   */
  public void infect(int row, int collumn, int timeInfected){
	  if(hostSet != null){
		  BaseEnemy target = hostSet.getEnemyAt(row, collumn);
		  if(target != null){
			  target.setInfectStatus(timeInfected);
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
  				DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
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
		  infect(currentRow, currentCollumn + 2, 3);
		  infect(currentRow, currentCollumn + 1, 3);
		  infect(currentRow, currentCollumn - 1, 3);
		  infect(currentRow, currentCollumn - 2, 3);
	  }
		animStates.shiftTimerStateThenCycle(ABILITY_STATE_NAME,
  				IDLE_STATE_NAME,
  				timeAllowed,
  				DEFAULT_IDLE_TIME + BaseCode.random.nextInt(DEFAULT_IDLE_TIME),
  				DEFAULT_BLINK_TIME +BaseCode.random.nextInt(DEFAULT_BLINK_TIME),
  				BLINK_STATE_NAME);
	  allowAbility = false;
	}
	return null;
}
protected void setDeathAnimation(int deathDuration) {
	animStates.changeState(DEATH_STATE_NAME, null);
	
}
}
