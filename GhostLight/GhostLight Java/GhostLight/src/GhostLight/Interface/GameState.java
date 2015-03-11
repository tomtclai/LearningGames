package GhostLight.Interface;

import java.awt.Color;

/**
 * Offers Access to view and change the various Elements of the Games current State. This includes
 * The players HEALTH(heart Count),
 * The Players LIGHT Power(power supply that the flash light draws from),
 * the Player SCORE,
 * the time given for Enemies to animate when revealed, and
 * the time given for Enemies to animate them sleves when the shift positions.
 * @author Michael Letter
 */
public class GameState {
	/** Used to mark generalize the games overall state */
	public enum EndState {WIN, LOSE, CONTINUE};
	
	private int health = 0;
	private float lightPower = 0;
	private int score = 0;
	private boolean healthHighlighted = true;
	
	private boolean turnEnd = false;	//Marks whether or not this is the last update in the turn
	private int AnimationTime = 30; 	//the number of updates that will occur while reveal animations continue
	
	
	private String message = "";
	private Color powerBarColor = Color.green;
	private EndState gameState = EndState.CONTINUE;
	
	private boolean ObjectSetPriority = true;
	
	//Health
	/**
	 * Will return the health (hearts) the player currently has
	 * @return The number of hearts the player has
	 */
	public int getHealth(){
		return health;
	}
	/**
	 * Will set the players health (hearts) to the given amount, Note player health can not be less then zero
	 * @param newHealth The health that the player will have at the conclusion of this function
	 * @return	Whether or not player health was successfully changed
	 */
	public boolean setHealth(int newHealth){
		if(newHealth >= 0){
			health = newHealth;
			return true;
		}
		return false;
	}
	/**
	 * Will highLight or Unhighlight the players health
	 * @param highlight If TRUE the players Health will be highLighted. If FALSE, the players health will be unhighlighted
	 */
	public void setHealthHighLight(boolean highlight){
		healthHighlighted = highlight;
	}
	/**
	 * Will return whether or not the players health is highLighted
	 * @return highlight If TRUE the players Health is highLighted. If FALSE, the players health is not.
	 */
	public boolean isHealthHighlighted(){
		return healthHighlighted;
	}
	//power
	/**
	 * Will return the Percentage of light Power that is currently available
	 * @return the percent of light power the player has with 0 representing 0% and 1 representing %100
	 */
	public float getLightPower(){
		return lightPower;
	}
	/**
	 * Will set the players lightPower to the given percentage, Note the given percentage must be between 0 and 1
	 * @param newHealth the percentage of lightPower the player is to have at the conclusion of this function
	 * @return	whether or not light power was successfully changed
	 */
	public boolean setLightPower(float newPower){
		if(newPower >= 0f && newPower <= 1f){
			lightPower = newPower;
			return true;
		}
		return false;
	}
	//score
	/**
	 * Will return the players current score
	 * @return current score
	 */
	public int getScore(){
		return score;
	}
	/**
	 * Will set the players current score to the given amount Note, score must be greater then zero
	 * @param newScore the desired player score
	 * @return whether or not the players score was successfully changed
	 */
	public boolean setScore(int newScore){
		if(newScore >= 0){
			score = newScore;
			return true;
		}
		return false;
	}
	//revealAnimation
	/**
	 * Will return the amount of time objects have to be revealed
	 * @return current score
	 */
	public int getAnimationTime(){
		return AnimationTime;
	}
	/**
	 * Will set amount of time objects have to be revealed. Note, the desired RevealAnimationTime must be either zero, or greater then 6
	 * @param newAnimationTime the desired RevealAnimationTime
	 * @return whether or not the RevealAnimationTime successfully changed
	 */
	public boolean setAnimationTime(int newAnimationTime){
		if(newAnimationTime == 0 || newAnimationTime >= 6){
			AnimationTime = newAnimationTime;
			return true;
		}
		return false;
	}
	//Turn End
	/**
	 * Will Mark or unmark this update as the last Update in the current turn. Marking this update
	 * as the last update in the turn will allow all of the InteractableObjects to update there state with
	 * the passing of the turn. Note, this change will not occur until after the the conclusion of this update 
	 * and its effects will not be visible until the next update
	 * @param isTurnEnd If true this update will be marked as the the last update of the turn. 
	 * If false will be marked as not the last update of the turn
	 */
	public void markTurnEnd(boolean isTurnEnd){
		turnEnd = isTurnEnd;
	}
	/**
	 * Will return whether or not this update is marked as the last update of the turn. if this update was marked 
	 * as the last update in the turn. all active InteractableObjects will be allowed to update there state with
	 * the passing of the turn. Note, this change will not occur until after the the conclusion of this update 
	 * and its effects will not be visible until the next update 
	 * @return TRUE indicates this update has been marked as the last update in the turn. FALSE indicates that this updates has not
	 */
	public boolean isTurnEnd(){
		return turnEnd; 
	}
	//Game end
	/**
	 * Will mark or unmark this update as the last update in the game and mark whether or not the game has been one or lost. Note this function will not do anything if called from interface.end()
	 * At this point the game is already over.
	 * @param gameState if WIN or LOSE, will mark this update as the last update of the turn of the game and will bring up either the Win screen or the loose screen
	 * 		If CONTINUE, then this update will be unmarked as the last update of the game and will ensure the game continues at normal
	 */
	public void setGameEnd(EndState gameState){
		this.gameState = gameState;
	}
	/**
	 * Will return whether or not this this update has been marked or unmarked as
	 * the last update in the game and mark whether or not the game has been one or lost
	 * @return if WIN or LOSE, this update has been marked as the last update of the turn of the game and will bring up either the Win screen or the loose screen at the conclusion of this update
	 * 		If CONTINUE, then the game and updates and will ensure the game continue at normal 
	 */
	public EndState getGameEndState(){
		return gameState;
	}
	//Color
	/**
	 * Will set the color of the displayed power bar to the given color
	 * @param newColor the given color. Note cannot be null
	 * @return If TRUE the color has been successfully changed. If False then no changes were made
	 */
	public boolean setPowerBarColor(Color newColor){
		if(newColor != null){
			powerBarColor = newColor;
			return true;
		}
		return false;
	}
	/**
	 * Will return the current Color of the power Bar 
	 * @return the current Color of the power Bar 
	 */
	public Color getPowerBarColor(){
		return powerBarColor;
	}
	//Message
	/**
	 * Will set the current Message displayed at the top of the screen to the given string
	 * @param newMessage the desired message
	 */
	public void setMessage(String newMessage){
		if(newMessage != null){
			message = newMessage;
		}
		else{
			message = "";
		}
	}
	/**
	 * The current Message displayed at the top of the screen
	 * @return The current Message displayed at the top of the screen
	 */
	public String getMessage(){
		return message;
	}
	//priority
	/**
	 * Will return whether or not the current ObjectGrid has priority
	 * This means if the current objectGrid's state is different than the current primitiveGrid's state, objectGrid's state will take priority
	 * @return if TRUE, objectGrid has priority. if FALSE, primitiveGrid has priority.
	 */
	public boolean objectGridHasPriority(){
		return ObjectSetPriority;
	}
	/**
	 * Will return whether or not the current primitiveGrid has priority
	 * This means if the current primitiveGrid's state is different than the current primitiveGrid's state, primitiveGrid's state will take priority
	 * @return if TRUE, primitiveGrid has priority. if FALSE, objectGrid has priority.
	 */
	public boolean primitiveGridHasPriority(){
		return !ObjectSetPriority;
	}
	/**
	 * Will give the current ObjectGrid priority
	 * This means if the current objectGrid's state is different than the current primitiveGrid's state, objectGrid's state will take priority
	 */
	public void giveObjectGridPriority(){
		ObjectSetPriority = true;
	}
	/**
	 * Will give the current primitiveGrid priority
	 * This means if the current primitiveGrid's state is different than the current primitiveGrid's state, primitiveGrid's state will take priority
	 */
	public void givePrimitiveGridPriority(){
		ObjectSetPriority = false;
	}
	public void addToHealth(int i) {
		// TODO Auto-generated method stub
		
	}
}
