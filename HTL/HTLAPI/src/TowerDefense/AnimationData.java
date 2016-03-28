package TowerDefense;
/**
 * Contains all of the data required for the engine to use an animation.
 * That way you don't have to keep track of it all in a zillion variables.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class AnimationData
{	
	// the Engine currently pushes 30 frames per second; used to calculate ticks per frame
	// there is probably a better place to store and access this constant from
	public static final int ENGINE_FRAME_RATE = 30;
	
	private String filename = null;	// filename of animation
	
	private int imageHeight = -1;				// height of the entire image
	private int imageWidth = -1;				// width of the entire image
	
	private int frameHeight = -1;				// the height of each frame in the animation
	private int frameWidth = -1;				// the width of each frame in the animation
	
	private int numberOfFrames = -1;			// number of frames in the animation		
	
	private int framesPerSecond = -1;			// rate at which frames should be played
	
	
	/**
	 * Constructor.  Deal with it.
	 * @param filename			File containing the animation.
	 * @param imageHeight		Height of the entire animation file.
	 * @param imageWidth		Width of the entire animation file.
	 * @param frameHeight		Height of each frame of animation within the file.
	 * @param frameWidth		Width of each frame of animation within the file.
	 * @param numberOfFrames	Number of animation frames in the file.
	 * @param framesPerSecond	Intended framerate of this animation.
	 */
	public AnimationData(String filename, int imageHeight, int imageWidth,
						int frameHeight, int frameWidth, int numberOfFrames, int framesPerSecond)
	{
		this.filename = filename;
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
		this.frameHeight = frameHeight;
		this.frameWidth = frameWidth;
		this.numberOfFrames = numberOfFrames;
		this.framesPerSecond = framesPerSecond;
	}

	/**
	 * @return	File containing the animation.
	 */
	public String getFilename()
	{
		return filename;
	}

	
	/**
	 * @return	Height of the entire animation file.
	 */
	public int getImageHeight()
	{
		return imageHeight;
	}


	/**
	 * @return	Width of the entire animation file.
	 */
	public int getImageWidth()
	{
		return imageWidth;
	}


	/**
	 * @return	Height of each frame of animation within the file.
	 */
	public int getFrameHeight()
	{
		return frameHeight;
	}


	/**
	 * @return	Width of each frame of animation within the file.
	 */
	public int getFrameWidth()
	{
		return frameWidth;
	}


	/**
	 * @return	Number of animation frames in the file.
	 */
	public int getNumberOfFrames()
	{
		return numberOfFrames;
	}


	/**
	 * @return	Intended framerate of this animation.
	 */
	public int getFramesPerSecond()
	{
		return framesPerSecond;
	}
	
	
	
	
	/**
	 * Returns the best approximation of how many ticks each frame of this animation should be drawn.
	 * Each game loop, animations are drawn.  Every time this happens, consider it one tick.
	 * So, how many game frames in a row should each frame of the animation be drawn?
	 * That's the number of ticks this returns.
	 * 
	 * @return		The number of ticks each frame of this animation should be drawn.
	 */
	public int getTicksPerFrame()
	{
		return (int) (framesPerSecond / ENGINE_FRAME_RATE);
	}
	
	
	/**
	 * Gives the width in in-game units that an object
	 * using this animation should be in order to take
	 * up the exact amount of screen real-estate as the
	 * image.
	 * @return		The width.
	 */
	public float getGameWidthFromImageWidth()
	{
		return frameWidth/HTL.PIXELS_PER_GAME_UNIT;
	}
	
	
	/**
	 * Gives the height in in-game units that an object
	 * using this animation should be in order to take
	 * up the exact amount of screen real-estate as the
	 * image.
	 * @return		The height.
	 */
	public float getGameHeightFromImageHeight()
	{
		return frameHeight/HTL.PIXELS_PER_GAME_UNIT;
	}
}
