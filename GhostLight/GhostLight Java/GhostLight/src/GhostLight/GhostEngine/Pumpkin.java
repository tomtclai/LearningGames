
package GhostLight.GhostEngine;

import java.util.Random;

import Engine.BaseCode;
import Engine.Rectangle;
import Engine.ResourceHandler;
import Engine.Vector2;
import GhostLight.Interface.InteractableObject;

/**
 * Jack-o-Lantern: this is the most common character to appear, it has no special behaviors and includes 
 * random point values of 5, 10, 15 and 20. The Jack-o-Lantern is removed by either the the standard 
 * flashlight beam or the charge beam as shown in figures 9-12:
 * @author Michael Letter
 */

public class Pumpkin extends BaseEnemy{
	
	private static final String IDLE_GOLD_NAME = "monsters/pumpkin/Pumpkin_IdleGold_r.png";
	private static final String IDLE_SILVER_NAME = "monsters/pumpkin/Pumpkin_IdleSilver_r.png";
	private static final String IDLE_BRONZE_NAME = "monsters/pumpkin/Pumpkin_IdleBronze_r.png";
	private static final String IDLE_PURPLE_NAME = "monsters/pumpkin/Pumpkin_IdlePurple_r.png";
	
	private static final String DEATH_GOLD_NAME = "monsters/pumpkin/Pumpkin_DeathGold_r.png";
	private static final String DEATH_SILVER_NAME = "monsters/pumpkin/Pumpkin_DeathSilver_r.png";
	private static final String DEATH_BRONZE_NAME = "monsters/pumpkin/Pumpkin_DeathGold_r.png"; //TODO:Missing
	private static final String DEATH_PURPLE_NAME = "monsters/pumpkin/Pumpkin_DeathGold_r.png"; //TODO:MISSING
	
	public static void preLoadResources(ResourceHandler resources){
		
	}
	
	
	enum CoinType 
	{
		GOLD, SILVER, BRONZE, PURPLE
	}
	CoinType coinState;
	

	
	
  public Pumpkin(){
	super();
	
	coinState = CoinType.values()[BaseCode.random.nextInt(4)];
	
    type = InteractableObject.ObjectType.PUMPKIN;
    super.isHelpfull = true;
    unrevealType();
    health.setMaxSegments(InteractableObject.getDefualthealth(type));
	health.setFilledSegments(health.getMaxSegments());
    score = InteractableObject.getDefualtScore(type);
    super.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
    
    int bronzeScore = InteractableObject.getDefualtMinScore(type);
    int purpleScore = InteractableObject.getDefualtMaxScore(type);
    
    float scoreIncreaseRate = (purpleScore / bronzeScore) / 4;
    
    int silverScore = (int)(( bronzeScore * scoreIncreaseRate ) * 2);
    int goldScore = (int)((bronzeScore * scoreIncreaseRate) * 3);
    
    
	
    // Modify score and animation types
	if(coinState == CoinType.PURPLE)
	{
		animStates.addNewState(IDLE_STATE_NAME, IDLE_PURPLE_NAME,
				223, 237, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_WIDTH));
		animStates.addNewState(DEATH_STATE_NAME, DEATH_PURPLE_NAME,
				513, 229, 20, 0, 0, 19, 
				Rectangle.SpriteSheetAnimationMode.ANIMATE_FORWARD_STOP,
				new Vector2(DEFAULT_DEATH_WIDTH, DEFAULT_DEATH_HEIGHT));
		
		score = purpleScore;
	}
	else if(coinState == CoinType.GOLD)
	{
		animStates.addNewState(IDLE_STATE_NAME, IDLE_GOLD_NAME,
				223, 237, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_WIDTH));
		animStates.addNewState(DEATH_STATE_NAME, DEATH_GOLD_NAME,
				513, 229, 20, 0, 0, 19, 
				Rectangle.SpriteSheetAnimationMode.ANIMATE_FORWARD_STOP,
				new Vector2(DEFAULT_DEATH_WIDTH, DEFAULT_DEATH_HEIGHT));
		
		score = goldScore;
	}
	else if(coinState == CoinType.SILVER)
	{
		animStates.addNewState(IDLE_STATE_NAME, IDLE_SILVER_NAME,
				220, 237, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_WIDTH));
		animStates.addNewState(DEATH_STATE_NAME, DEATH_SILVER_NAME,
				509, 229, 20, 0, 0, 19, 
				Rectangle.SpriteSheetAnimationMode.ANIMATE_FORWARD_STOP,
				new Vector2(DEFAULT_DEATH_WIDTH, DEFAULT_DEATH_HEIGHT));
		
		score = silverScore;
	}
	else if(coinState == CoinType.BRONZE)
	{
		animStates.addNewState(IDLE_STATE_NAME, IDLE_BRONZE_NAME,
				220, 224, 40, 0, new Vector2(DEFAULT_WIDTH, DEFAULT_WIDTH));
		animStates.addNewState(DEATH_STATE_NAME, DEATH_BRONZE_NAME,
				513, 229, 20, 0, 0, 19, 
				Rectangle.SpriteSheetAnimationMode.ANIMATE_FORWARD_STOP,
				new Vector2(DEFAULT_DEATH_WIDTH, DEFAULT_DEATH_HEIGHT));
		
		score = bronzeScore;
	}
  }

  public StateChange revealType(int pauseTime){
	  if(isTypeRevealed && getInfectStatus() <= 0) {
		  return removeThis();
	  }
	  return super.revealType(pauseTime);
  }
  protected void shiftImageState()
  {
    if(isTypeRevealed)
    {
      animStates.changeState(IDLE_STATE_NAME);
    }
    else
    {
    	super.shiftImageState();
    }
  }
  @Override
  public void playWaitingIntroductions(int timeAllowed) 
  {
	  // None.
  }
@Override
public StateChange activateAbility(int timeAllowed) {
	// None
	return null;
}
protected void setDeathAnimation(int deathDuration) {
	animStates.changeState(DEATH_STATE_NAME, null);
	
}
}
