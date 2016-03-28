package TowerDefense;
import Engine.GameObject;

/**
 * This class simply extends GameObject with some additional functionality.
 * Adds some persistence to the concept of animations, and some shortcuts to interact with them.
 * @author Branden Drake
 */
public class GameObjectPlus extends GameObject
{	
	private String currentAnim = null;

	
	/**
	 * Constructor
	 * 
	 * @param xLoc		x-axis position
	 * @param yLoc		y-axis position
	 * @param width		In-game width.
	 * @param height	In-game height.
	 */
	public GameObjectPlus(float xLoc, float yLoc, float width, float height)
	{
		super(xLoc, yLoc, width, height);
		draw();
		
		this.setUsingSpriteSheet(true);		// use sprite sheet as sprite, not texture
	}
	
	
	
	/**
	 * Returns filename of the current animation as a String.
	 */
	public String getCurrentAnim()
	{
		return currentAnim;
	}	
	
	
	/**
	 * Returns true if the object's current animation isn't null.
	 */
	public boolean hasCurrentAnim()
	{
		return currentAnim != null;
	}
	
	
	/**
	 * Returns true if anim is the animation currently being displayed by the object.
	 */
	public boolean currentAnimIs(String anim)
	{
		if(currentAnim == null)
		{
			return false;
		}
		
		return this.currentAnim.equals(anim);
	}


	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * The 10 public functions below are used to initiate animations.
	 * There are 5 different types of animation.
	 * There are 2 ways to initiate animation:
	 * 		- start:	if a single animation (an animation intended to play only once)
	 * 					is in progress, the animation will not be interrupted
	 * 
	 * 		- force:	even if a single animation is in progress, the new animation will begin
	 * 
	 * A looping animation will never interrupt itself; this will result in returning false, as
	 * a new animation has not begun. 
	 */	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Start an animation that plays once, but only if another single animation isn't already playing.
	 * @param animFile			Filename of the animation to play.
	 * @param numFrames			Number of frames in the animation's spritesheet.
	 * @param ticksPerFrame		How long to stay on each frame of the animation.
	 * @return					True if this animation was played.
	 */
	public boolean startSingleAnim(AnimationData animData)
	{
		return setCurrentAnim(animData, false, AnimationMode.ANIMATE_FORWARD_STOP);
	}
	
	
	/**
	 * Start an animation that plays once, even if another single animation is already playing.
	 * @param animFile			Filename of the animation to play.
	 * @param numFrames			Number of frames in the animation's spritesheet.
	 * @param ticksPerFrame		How long to stay on each frame of the animation.
	 * @return					True if this animation was played.
	 */
	public boolean forceSingleAnim(AnimationData animData)
	{
		return setCurrentAnim(animData, true, AnimationMode.ANIMATE_FORWARD_STOP);
	}
	
	
	
	/**
	 * Start an animation that loops forever, but only if a single animation isn't currently playing.
	 * @param animFile			Filename of the animation to play.
	 * @param numFrames			Number of frames in the animation's spritesheet.
	 * @param ticksPerFrame		How long to stay on each frame of the animation.
	 * @return					True if this animation was played.
	 */
	public boolean startLoopingAnim(AnimationData animData)
	{
		return setCurrentAnim(animData, false, AnimationMode.ANIMATE_FORWARD);
	}
	
	/**
	 * Start an animation that loops forever, even if a single animation is currently playing.
	 * @param animFile			Filename of the animation to play.
	 * @param numFrames			Number of frames in the animation's spritesheet.
	 * @param ticksPerFrame		How long to stay on each frame of the animation.
	 * @return					True if this animation was played.
	 */
	public boolean forceLoopingAnim(AnimationData animData)
	{
		return setCurrentAnim(animData, true, AnimationMode.ANIMATE_FORWARD);
	}
	
	/**
	 * Start an animation that reverses when it finishes and loops forever,
	 * but only if a single animation isn't currently playing.
	 * @param animFile			Filename of the animation to play.
	 * @param numFrames			Number of frames in the animation's spritesheet.
	 * @param ticksPerFrame		How long to stay on each frame of the animation.
	 * @return					True if this animation was played.
	 */
	public boolean startSwingAnim(AnimationData animData)
	{
		return setCurrentAnim(animData, false, AnimationMode.ANIMATE_SWING);
	}
	
	/**
	 * Start an animation that reverses when it finishes and loops forever,
	 * even if a single animation is currently playing.
	 * @param animFile			Filename of the animation to play.
	 * @param numFrames			Number of frames in the animation's spritesheet.
	 * @param ticksPerFrame		How long to stay on each frame of the animation.
	 * @return					True if this animation was played.
	 */
	public boolean forceSwingAnim(AnimationData animData)
	{
		return setCurrentAnim(animData, true, AnimationMode.ANIMATE_SWING);
	}	
	
	/**
	 * Start an animation that plays backwards once, but only if another single animation isn't already playing.
	 * @param animFile			Filename of the animation to play.
	 * @param numFrames			Number of frames in the animation's spritesheet.
	 * @param ticksPerFrame		How long to stay on each frame of the animation.
	 * @return					True if this animation was played.
	 */
	public boolean startSingleReverseAnim(AnimationData animData)
	{
		return setCurrentAnim(animData, false, AnimationMode.ANIMATE_BACKWARD_STOP);
	}
	
	/**
	 * Start an animation that plays backwards once, even if another single animation is already playing.
	 * @param animFile			Filename of the animation to play.
	 * @param numFrames			Number of frames in the animation's spritesheet.
	 * @param ticksPerFrame		How long to stay on each frame of the animation.
	 * @return					True if this animation was played.
	 */
	public boolean forceSingleReverseAnim(AnimationData animData)
	{
		return setCurrentAnim(animData, true, AnimationMode.ANIMATE_BACKWARD_STOP);
	}
	
	/**
	 * Start an animation that plays backwards and loops forever, but only if a single animation isn't currently playing.
	 * @param animFile			Filename of the animation to play.
	 * @param numFrames			Number of frames in the animation's spritesheet.
	 * @param ticksPerFrame		How long to stay on each frame of the animation.
	 * @return					True if this animation was played.
	 */
	public boolean startLoopingReverseAnim(AnimationData animData)
	{
		return setCurrentAnim(animData, false, AnimationMode.ANIMATE_BACKWARD);
	}
	
	/**
	 * Start an animation that plays backwards and loops forever, even if a single animation is currently playing.
	 * @param animFile			Filename of the animation to play.
	 * @param numFrames			Number of frames in the animation's spritesheet.
	 * @param ticksPerFrame		How long to stay on each frame of the animation.
	 * @return					True if this animation was played.
	 */
	public boolean forceLoopingReverseAnim(AnimationData animData)
	{
		return setCurrentAnim(animData, true, AnimationMode.ANIMATE_BACKWARD);
	}
	
	
	//////////////////////
	//                  //
	//    THE GUTS      //
	//                  //
	//////////////////////
	
	
	/**
	 * 
	 * @param animFile				Filename of the animation to play.
	 * @param numFrames				Number of frames in the animation's spritesheet.
	 * @param ticksPerFrame			How long to stay on each frame of the animation.
	 * @param interruptSingleAnims	True if this animation should interrupt single animations.
	 * @param animMode				The type of animation this is (check AnimationMode).
	 * @return						True if this animation was played.
	 */
	private boolean setCurrentAnim(AnimationData animData, boolean interruptSingleAnims, AnimationMode animMode)
	{
		// skip if a non-looping animation we don't want to skip is in progress
		if(!interruptSingleAnims && this.hasNonLoopingAnimationInProgress()) return false;
		
		// skip if we are already animating that exact animation in a loop (otherwise the anim would start over)
		String animFile = animData.getFilename();
		if(this.hasLoopingAnimationInProgress() && currentAnimIs(animFile)) return false;
		
		// 0 is first frame index, numFrames-1 is last frame index
		this.setAnimationMode(0, animData.getNumberOfFrames()-1, animMode);
		
		this.currentAnim = animFile;
		this.setSpriteSheet(animFile, animData.getFrameWidth(), animData.getFrameHeight(),
							animData.getNumberOfFrames(), animData.getTicksPerFrame());
		return true;
	}
	

	
}
