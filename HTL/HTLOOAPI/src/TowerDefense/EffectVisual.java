package TowerDefense;
/**
 * A visual effect.  In general, it should be hiding, and when someone tells it to,
 * it comes out to play before disappearing back into the shadows.
 * 
 * Like Batman.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class EffectVisual extends GameObjectPlus
{		
	private AnimationData animationData;
	
	/**
	 * Constructor.  The messy version.
	 * @param filename			File containing the animation.
	 * @param imageHeight		Height of the entire animation file.
	 * @param imageWidth		Width of the entire animation file.
	 * @param frameHeight		Height of each frame of animation within the file.
	 * @param frameWidth		Width of each frame of animation within the file.
	 * @param numberOfFrames	Number of animation frames in the file.
	 * @param framesPerSecond	Intended framerate of this animation.
	 */
	public EffectVisual(String filename, int imageHeight, int imageWidth,
						int frameHeight, int frameWidth, int numberOfFrames, int framesPerSecond,
						float xLoc, float yLoc, float width, float height)
	{
		this(new AnimationData(filename, imageHeight, imageWidth, frameHeight, frameWidth, numberOfFrames, framesPerSecond),
				xLoc, yLoc, width, height);	
	}
	
	
	/**
	 * Constructor.  So clean.
	 * @param myAnimationData		The animation data object for this visual effect.
	 */
	public EffectVisual(AnimationData animationData, float xLoc, float yLoc, float width, float height)
	{
		super(xLoc, yLoc, width, height);
		this.animationData = animationData;
	}
	
	
	/**
	 * Plays the effect once.  If effect is already playing, this will restart it.
	 */
	public void play()
	{
		this.forceSingleAnim(animationData);
	}
	
	
	/**
	 * Updates anything necessary, which is nothing.
	 */
	public void update()
	{	
	}	
}