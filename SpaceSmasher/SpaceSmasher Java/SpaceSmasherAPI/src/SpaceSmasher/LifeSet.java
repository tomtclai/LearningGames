
package SpaceSmasher;

import Engine.ResourceHandler;
import Engine.SegmentedBar;

 public class LifeSet extends SegmentedBar {
	public final static String HEALTH_ACTIVE = "Heart_Empty.png";
	public final static String HEALTH_INACTIVE = "Heart_Full.png";
	
	/**
	   * will load and sounds or images required for this class to function
	   * @param resources handler to be used for pre-loading.
	   */
	  public static void preloadResources(ResourceHandler resources){
		  if(resources !=  null){
			  resources.preloadImage(HEALTH_ACTIVE);
			  resources.preloadImage(HEALTH_INACTIVE);
		  }
	  }
	public LifeSet(){
		super();
		setActiveImage(HEALTH_ACTIVE);
		setInactiveImage(HEALTH_INACTIVE);
		useImages = true;
 //setVisibilityTo(true);
 		visible = true;
	}
	/**
	 * Will increment the amount of Health Shown by 1 and will increase MaxSegments to accommodate
	 * the increased amount of health if necessary  
	 */
	public void add(){
		add(1);
	}
	/**
	 * Will increment the amount of Health Shown by the given amount will increase MaxSegments to accommodate
	 * the increased amount of health if necessary  
	 * @param amount the amount health will be incremented by
	 */
	public void add(int amount){
		if(getfilledSegments() + amount > getMaxSegments()){
			setMaxSegments(getfilledSegments() + amount);
		}
		setFilledSegments(getfilledSegments() + amount);
	}
	/**
	 * Will Decrement the amount of health shown by 1
	 */
	public void remove(){
		remove(1);
	}
	/**
	 * Will Decrement the amount of health shown by the given amount if the new Health levels 
	 * is greater then the current MaxSegments then the health level will be decreased to MaxSegments
	 * @param amount the given amount
	 */
	public void remove(int amount){
		setFilledSegments(getfilledSegments() - amount);
	}
	/**
	 * Will return the amount of health available
	 * @return amount of health available
	 */
	public int getCount(){
		return getfilledSegments();
	}
}