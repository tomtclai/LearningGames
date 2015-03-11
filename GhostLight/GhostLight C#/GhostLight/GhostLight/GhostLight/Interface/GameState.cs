using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

/**
 * Offers Access to view and change the various Elements of the Games current State. This includes
 * The players HEALTH(heart Count),
 * The Players LIGHT Power(power supply that the flash light draws from),
 * the Player SCORE,
 * the time given for Enemies to animate when revealed, and
 * the time given for Enemies to animate them sleves when the shift positions.
 * @author Michael Letter
 */
namespace GhostFinder.Interface{
    public class GameState {
        /// <summary> Used to mark generalize the games overall state </summary>
	    public enum EndState {WIN, LOSE, CONTINUE};
	
	    private int health = 0;
	    private float lightPower = 0;
	    private int score = 0;
	    private bool healthHighlighted = true;
	
	    private bool turnEnd = false;	//Marks whether or not this is the last update in the turn
	    private int AnimationTime = 30; 	//the number of updates that will occur while reveal animations continue
	
	
	    private String message = "";
	    private Color powerBarColor = Color.Green;
	    private EndState gameState = EndState.CONTINUE;
	
	    private bool ObjectSetPriority = true;

        //Health
	    /// <summary>
        /// Will return the health (hearts) the player currently has
	    /// </summary>
        /// <returns>The number of hearts the player has</returns>
	    public int getHealth(){
		    return health;
	    }
	    /// <summary>
        /// Will set the players health (hearts) to the given amount, Note player health can not be less then zero
	    /// </summary>
        /// <param name="newHealth">The health that the player will have at the conclusion of this function</param>
        /// <returns>Whether or not player health was successfully changed</returns>
	    public bool setHealth(int newHealth){
		    if(newHealth >= 0){
			    health = newHealth;
			    return true;
		    }
		    return false;
	    }
	    /// <summary>
        /// Will highLight or Unhighlight the players health
	    /// </summary>
        /// <param name="highlight">If TRUE the players Health will be highLighted. If FALSE, the players health will be unhighlighted</param>
	    public void setHealthHighLight(bool highlight){
		    healthHighlighted = highlight;
	    }
	    /// <summary>
        /// Will return whether or not the players health is highLighted
	    /// </summary>
        /// <returns>If TRUE the players Health is highLighted. If FALSE, the players health is not.</returns>
	    public bool isHealthHighlighted(){
		    return healthHighlighted;
	    }
        //power
	    /// <summary>
        /// Will return the Percentage of light Power that is currently available
	    /// </summary>
        /// <returns>the percent of light power the player has with 0 representing 0% and 1 representing %100</returns>
	    public float getLightPower(){
		    return lightPower;
	    }
	    /// <summary>
        /// Will set the players lightPower to the given percentage, Note the given percentage must be between 0 and 1
	    /// </summary>
        /// <param name="newPower">the percentage of lightPower the player is to have at the conclusion of this function</param>
        /// <returns>whether or not light power was successfully changed</returns>
	    public bool setLightPower(float newPower){
		    if(newPower >= 0f && newPower <= 1f){
			    lightPower = newPower;
			    return true;
		    }
		    return false;
	    }
        //score
	    /// <summary>
        /// Will return the players current score
	    /// </summary>
        /// <returns>current score</returns>
	    public int getScore(){
		    return score;
	    }
	    /// <summary>
        /// Will set the players current score to the given amount Note, score must be greater then zero
	    /// </summary>
        /// <param name="newScore">the desired player score</param>
        /// <returns> whether or not the players score was successfully changed</returns>
	    public bool setScore(int newScore){
		    if(newScore >= 0){
			    score = newScore;
			    return true;
		    }
		    return false;
	    }
        //revealAnimation
	    /// <summary>
        /// Will return the amount of time objects have to be revealed
	    /// </summary>
        /// <returns>current score</returns>
	    public int getAnimationTime(){
		    return AnimationTime;
	    }
	    /// <summary>
        /// Will set amount of time objects have to be revealed. Note, the desired RevealAnimationTime must be either zero, or greater then 6
	    /// </summary>
        /// <param name="newAnimationTime">the desired RevealAnimationTime</param>
        /// <returns>whether or not the RevealAnimationTime successfully changed</returns>
	    public bool setAnimationTime(int newAnimationTime){
		    if(newAnimationTime == 0 || newAnimationTime >= 6){
			    AnimationTime = newAnimationTime;
			    return true;
		    }
		    return false;
	    }
        //Turn End
	    /// <summary>
	    /// Will Mark or unmark this update as the last Update in the current turn. Marking this update
        /// as the last update in the turn will allow all of the InteractableObjects to update there state with
        /// the passing of the turn. Note, this change will not occur until after the the conclusion of this update 
        /// and its effects will not be visible until the next update
	    /// </summary>
        /// <param name="isTurnEnd">If true this update will be marked as the the last update of the turn. 
        /// If false will be marked as not the last update of the turn</param>
	    public void markTurnEnd(bool isTurnEnd){
		    turnEnd = isTurnEnd;
	    }
	    /// <summary>
	    /// Will return whether or not this update is marked as the last update of the turn. if this update was marked 
	    /// as the last update in the turn. all active InteractableObjects will be allowed to update there state with
	    /// the passing of the turn. Note, this change will not occur until after the the conclusion of this update 
        /// and its effects will not be visible until the next update 
	    /// </summary>
        /// <returns>TRUE indicates this update has been marked as the last update in the turn. FALSE indicates that this updates has not</returns>
	    public bool isTurnEnd(){
		    return turnEnd; 
	    }
        //Game end
	    /// <summary>
	    /// Will mark or unmark this update as the last update in the game and mark whether or not the game has been one or lost. Note this function will not do anything if called from interface.end()
        /// At this point the game is already over.
	    /// </summary>
	    /// <param name="gameState">if WIN or LOSE, will mark this update as the last update of the turn of the game and will bring up either the Win screen or the loose screen
        /// If CONTINUE, then this update will be unmarked as the last update of the game and will ensure the game continues at normal</param>
	    public void setGameEnd(EndState gameState){
		    this.gameState = gameState;
	    }
	    /// <summary>
	    /// Will return whether or not this this update has been marked or unmarked as
	    /// the last update in the game and mark whether or not the game has been one or lost
	    /// </summary>
	    /// <returns>if WIN or LOSE, this update has been marked as the last update of the turn of the game and will bring up either the Win screen or the loose screen at the conclusion of this update
	    /// If CONTINUE, then the game and updates and will ensure the game continue at normal </returns>
	    public EndState getGameEndState(){
		    return gameState;
	    }
        //Color
	    /// <summary>
        /// Will set the color of the displayed power bar to the given color
	    /// </summary>
        /// <param name="newColor">the given color. Note cannot be null</param>
        /// <returns>If TRUE the color has been successfully changed. If False then no changes were made</returns>
	    public bool setPowerBarColor(Color newColor){
		    if(newColor != null){
			    powerBarColor = newColor;
			    return true;
		    }
		    return false;
	    }
	    /// <summary>
        /// Will return the current Color of the power Bar 
	    /// </summary>
        /// <returns>the current Color of the power Bar </returns>
	    public Color getPowerBarColor(){
		    return powerBarColor;
	    }
        //Message
	    /// <summary>
        /// Will set the current Message displayed at the top of the screen to the given string
	    /// </summary>
        /// <param name="newMessage">the desired message</param>
	    public void setMessage(String newMessage){
		    if(newMessage != null){
			    message = newMessage;
		    }
		    else{
			    message = "";
		    }
	    }
	    /// <summary>
        /// The current Message displayed at the top of the screen
	    /// </summary>
        /// <returns>The current Message displayed at the top of the screen</returns>
	    public String getMessage(){
		    return message;
	    }
        //priority
	    /// <summary>
	    /// Will return whether or not the current ObjectGrid has priority
        /// This means if the current objectGrid's state is different than the current primitiveGrid's state, objectGrid's state will take priority
	    /// </summary>
        /// <returns>if TRUE, objectGrid has priority. if FALSE, primitiveGrid has priority.</returns>
	    public bool objectGridHasPriority(){
		    return ObjectSetPriority;
	    }
	    /// <summary>
	    /// Will return whether or not the current primitiveGrid has priority
	    /// This means if the current primitiveGrid's state is different than the current primitiveGrid's state, primitiveGrid's state will take priority
	    /// </summary>
        /// <returns>if TRUE, primitiveGrid has priority. if FALSE, objectGrid has priority.</returns>
	    public bool primitiveGridHasPriority(){
		    return !ObjectSetPriority;
	    }
	    /// <summary> 
	    /// Will give the current ObjectGrid priority
	    /// This means if the current objectGrid's state is different than the current primitiveGrid's state, objectGrid's state will take priority
	    /// </summary>
	    public void giveObjectGridPriority(){
		    ObjectSetPriority = true;
	    }
	    /// <summary>
	    /// Will give the current primitiveGrid priority
	    /// This means if the current primitiveGrid's state is different than the current primitiveGrid's state, primitiveGrid's state will take priority
	    /// </summary>
	    public void givePrimitiveGridPriority(){
		    ObjectSetPriority = false;
	    }
    }
}
