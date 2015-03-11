/**
 * used to ferry requests to any change in the players state
 * Simply has a sieries of public varialbels that will be used eventuallty by Ghostfinder to increment the players scores
 * Defualt Values will have no effect on the players state
 * @author Michael Letter
 */
namespace GhostFinder.GhostEngine{
    public class StateChange {
	    public int changeInPlayerHealth = 0;	//Will increment the players health by the given amount
	    public int changeInPlayerScore = 0;		//Will increment the players score by the given amount
	    public float changInBatteryCharge = 0;	//Will increment the players battery by the given percentage.
	    public bool puaseForAnimation = false;//Stores whether or not changes were made that require the game to puase for animation
	
	    /**
	     * Will set given variables to defualts 
	     */
	    public void setToDefualts(){
		    changeInPlayerHealth = 0;
		    changeInPlayerScore = 0;
		    changInBatteryCharge = 0;
		    puaseForAnimation = false;
	    }
	    /**
	     * Will increment all of this rgis objects paramiters and 
	     * @param other
	     */
	    public StateChange add(StateChange other){
		    if(other != null){
			    changeInPlayerHealth += other.changeInPlayerHealth;
			    changeInPlayerScore += other.changeInPlayerScore;
			    changInBatteryCharge += other.changInBatteryCharge;
			    puaseForAnimation = puaseForAnimation || other.puaseForAnimation;
		    }
		    return this;
	    }
	    public StateChange copy(StateChange other){
		    if(other != null){
			    changeInPlayerHealth = other.changeInPlayerHealth;
			    changeInPlayerScore = other.changeInPlayerScore;
			    changInBatteryCharge = other.changInBatteryCharge;
			    puaseForAnimation = other.puaseForAnimation;
		    }
		    return this;
	    }
    }
}