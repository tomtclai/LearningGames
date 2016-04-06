package Engine;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * GameObject, base class of all game objects that encapsulate all common behaviors
 * of objects in the game. 
 */
public class GameObject {
	
	private Rectangle mTheObject;
	
	/**
	 * Default constructor
	 */
	public GameObject()
	{
		mTheObject = new Rectangle();
	}
	
	public GameObject(GameObject other) {
		mTheObject = new Rectangle(other.mTheObject);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param posX		Center position's X
	 * @param posY		Center position's Y
	 * @param width 	Width of the object
	 * @param height	Height of the object
	 */
	public GameObject(float posX, float posY, float width, float height) {
		mTheObject = new Rectangle();
		mTheObject.size.setX(width);
		mTheObject.size.setY(height);
		mTheObject.center.setX(posX);
		mTheObject.center.setY(posY);
	}
	// Region: general utilities 
	/**
	 * Tests if two game object has collided.
	 * 
	 * @param otherObject	The other object to be tested for collision.
	 * @return Returns the status of the collision (true or false).
	 */
	public boolean collided(GameObject otherObject) {
		return mTheObject.collided(otherObject.mTheObject);
	}
	
	/**
	 * Test if the given position is within the bounds of the GameObject
	 * @param pos (x,y) position of the point 
	 * @return true: if pos is in the bounds of game object, false otherwise
	 */
	public boolean containsPoint(Vector2 pos) {
		return mTheObject.containsPoint(pos);
	}
	
	/**
	 * Updates this game object
	 */
	public void update() {
		mTheObject.update();
	}
	
	/**
	 * Destroy this game object. Call when ready to dispose of this object.
	 */
	public void destroy() {
		mTheObject.destroy();
	}
	
	/**
	 * Move this game object to the front so that it will be drawn over all other objects
	 */
	public void moveToFront() {
		mTheObject.removeFromAutoDrawSet();
		mTheObject.addToAutoDrawSet();
	}
	/**
	 * Attempts will be made (no promise!) to always draw this GameObject in front of all other objects
	 * @param inFront True: set this object to (try to) be drawn in front of every other GameObject; false otherwise
	 */
	public void setAlwaysInFront(boolean inFront) {
		mTheObject.alwaysOnTop = inFront;
	}
	
	
	/**
	 * Test if the textures of two GameObject overlaps. **WARNING**: this can be a very expensive function to call. Make sure the
	 * parameter is the smaller texuter is making the call:
	 *      smallerTextureGameObject.pixelTouches(largerTextureGameObject);
	 * @param other the other GameObject to be tested for intersection (should be the object with larger texture)
	 * @return true if the two textures has any non-transparent region that overlaps, false otherwise
	 */
	public boolean pixelTouches(GameObject other) {
		return mTheObject.pixelTouches(other.mTheObject);
	}
	// EndRegion
	
	// Region: get/set center position
	/**
	 * Access the center of the game object.
	 * @return the center position.
	 */
	public Vector2 getCenter() { return mTheObject.center; }
	
	/**
	 * @return x-coordinate of the center position
	 */
	public float getCenterX() { return getCenter().getX(); }
	
	/**
	 * 
	 * @return y-coordinate of the center position
	 */
	public float getCenterY() { return getCenter().getY(); }
	
	/**
	 * sets the x-coordinate of the center position
	 * @param cx the x-coordinate  
	 */
	public void setCenterX(float cx) { getCenter().setX(cx); }
	
	/**
	 * sets the y-coordinate of the center position
	 * @param cy the y-coordinate
	 */
	public void setCenterY(float cy) { getCenter().setY(cy); }
	
	/**
	 * sets the (cx, cy) to be the new center position of the object.
	 * @param cx x-coordinate to be set
	 * @param cy y-coordinate to be set
	 */
	public void setCenter(float cx, float cy) { setCenterX(cx); setCenterY(cy); }
	
	/**
	 * sets the center position
	 * @param newC (x,y) of the new center position
	 */
	public void setCenter(Vector2 newC) { setCenter(newC.getX(), newC.getY()); }
	// EndRegion: center get/set
	
	// Region: get/set size of object
	/**
	 * Access the size of the game object.
	 * @return the width and height of the object.
	 */
	public Vector2 getSize() { return mTheObject.size; }
	
	/**
	 * 
	 * @return width of this game object
	 */
	public float getWidth() { return getSize().getX(); }
	
	/**
	 * 
	 * @return height of this game object
	 */
	public float getHeight() { return getSize().getY(); }
	
	/**
	 * sets the width of this game object
	 * @param width new width to be set
	 */
	public void setWidth(float width) { getSize().setX(width); }
	
	/**
	 * sets the height of this game object
	 * @param height new height to be set
	 */
	public void setHeight(float height) { getSize().setY(height); }
	
	/**
	 * sets new width and height for this game object
	 * @param width new width to be set
	 * @param height new height to be set
	 */
	public void setSize(float width, float height) { setWidth(width); setHeight(height); }
	/**
	 * Sets the width and height to the same value.
	 * @param newSize the new width and height of the game object
	 */
	public void setSize(float newSize) { setWidth(newSize); setHeight(newSize); }
	/**
	 * sets the size of this game object
	 * @param newSize (width, height) to be set for this object
	 */
	public void setSize(Vector2 newSize) { setSize(newSize.getX(), newSize.getY()); }
	// EndRegion: get/set size of object
	
	// Region: get/set velocity
	/**
	 * Access the velocity of the game object.
	 * @return the current velocity.
	 */
	public Vector2 getVelocity() { return mTheObject.velocity; }
	
	/**
	 * 
	 * @return x-component of the velocity
	 */
	public float getVelocityX() { return getVelocity().getX(); }
	
	/**
	 * 
	 * @return y-component of the velocity
	 */
	public float getVelocityY() { return getVelocity().getY(); }
	
	/**
	 * sets the x-component of the velocity
	 * @param vx the x-component to be set to
	 */
	public void setVelocityX(float vx) { getVelocity().setX(vx); }
	/**
	 * sets the y-component of the velocity
	 * @param vy the y-component to be set to
	 */
	public void setVelocityY(float vy) { getVelocity().setY(vy); }
	/**
	 * sets the velocity to be (vx, vy)
	 * @param vx the x-component to be set to
	 * @param vy the y-component to be set to
	 */
	public void setVelocity(float vx, float vy) { setVelocityX(vx); setVelocityY(vy); }
	/**
	 * sets the velocity to be newV
	 * @param newV the (x,y) values to set the velocty
	 */
	public void setVelocity(Vector2 newV) { setVelocity(newV.getX(), newV.getY()); }
	// EndRegion: get/set velocity
	
	// Region: get/set visibility
	/**
	 * Access the visibility of the game object.
	 * @return true (visible on the game window), or false (not visible)
	 */
	public boolean isVisible() { return mTheObject.visible; }
	/**
	 * Sets the visibility of this object
	 * @param flag	True: visible, false: not visible
	 */
	public void setVisibilityTo(boolean flag) { mTheObject.visible = flag; }
	/**
	 * sets this object to be invisible.
	 */
	public void setToInvisible() { mTheObject.visible = false; }
	/**
	 * sets this object to be visible
	 */
	public void setToVisible() { mTheObject.visible = true; }
	/**
	 * toggles visibility of this object.
	 */
	public void toggleVisibility() { mTheObject.visible = !mTheObject.visible; }
	// EndRegion: get/set visibility
	
	// Region: get/set color
	/**
	 * Access the current color of the game object
	 * @return color of the game object
	 */
	public Color getColor() { return mTheObject.color; }
	/**
	 * sets the color of this object
	 * @param c the new color to be set to.
	 */
	public void setColor(Color c) { mTheObject.color = c; }
	// EndRegion: get/set Color
	
	// Region: get/set rotation
	/**
	 * Access the current rotation of the game object
	 * @return rotation of the game object (in radian)
	 */
	public float getRotation() { return mTheObject.rotate; }
	/**
	 * sets the rotation of this object
	 * @param r the new rotation for the object (in radian)
	 */
	public void setRotation(float r) { mTheObject.rotate = r; }
	/**
	 * changes the rotation of this object
	 * @param dr the amount of rotation to change (in radian)
	 */
	public void changeRotationBy(float dr) { mTheObject.rotate += dr; }
	// EndRegion
	
	// Region: get/set image and spriteSheet
	public BufferedImage getImage() { return mTheObject.texture; }
	/**
	 * sets an image to show for this object (texture over this object)
	 * @param imageFile filename to the image 
	 */
	public void setImage(String imageFile) { mTheObject.setImage(imageFile); }
	public void setImage(BufferedImage i) { mTheObject.texture = i; }
	
	/**
	   * Sets up the spritesheet animation. Will not do anything if width, height, or totalFrame are less than 0.
	   * 
	   * @param spriteFilename Filename of the spritesheet image
	   * @param width Width of individual sprite.
	   * @param height Height of individual sprite.
	   * @param totalFrames Total sprites in spritesheet
	   * @param ticksPerFrame How long each sprite remains before the next is drawn.
	   */
	public void setSpriteSheet(String spriteFilename, int width, int height, int totalFrames, int ticksPerFrame) {
		mTheObject.setSpriteSheet(spriteFilename, width, height, totalFrames, ticksPerFrame);
	}
	
	/**
	   * Sets up the spritesheet animation. Will not do anything if width, height, or totalFrame are less than 0.
	   * 
	   * @param image The bufferedTexture (image) of the spritesheet
	   * @param width Width of individual sprite.
	   * @param height Height of individual sprite.
	   * @param totalFrames Total sprites in spritesheet
	   * @param ticksPerFrame How long each sprite remains before the next is drawn.
	   */
	public void setSpriteSheet(BufferedImage image, int width, int height, int totalFrames, int ticksPerFrame) {
		mTheObject.setSpriteSheet(image, width, height, totalFrames, ticksPerFrame);
	}
	/**
	 * pauses the sprite animation on this object.
	 * @param pause true to pause the animation.
	 */
	public void setAnimationPauseStatus(boolean pause)
	{
		mTheObject.setAnimationPauseStatus(pause);
	}
	/**
	 * Enable/disable the spritesheet animation on this object.
	 * @param on True: use sprite sheet animation, False: do not use sprite sheet animation
	 */
	public void setUsingSpriteSheet(boolean on)	{
		mTheObject.setUsingSpriteSheet(on);
	}
	/**
	 * controls the drawing of the GameObject to only drawing outline or not.
	 * Default: fills.
	 * @param onlyOutlined if true only draws outlined, otherwise draws regular (filled)
	 */
	public void setToDrawOutline(boolean onlyOutlined) {
		mTheObject.setDrawFilledRect(!onlyOutlined);
	}
	
	// EndRegion: get/set image and spriteSheet
	
	// Region: Auto drawing support
	/**
	 * Default behavior of GameObject is that it will be drawn automatically for each frame. 
	 * You can switch this off by setting autodraw to false. 
	 * @param autoDraw true: draw automatically, false: do not draw this game object
	 */
	public void setAutoDrawTo(boolean autoDraw) {
		if (autoDraw)
			mTheObject.addToAutoDrawSet();
		else
			mTheObject.removeFromAutoDrawSet();
	}
	/**
	 * Draws this game object. if setAutoDrawTo(true), then, this object will be drawn twice 
	 * per frame.
	 * 
	 * GameObject's draw should not be called explicitly (AutoDraw will take care of drawing!)
	 */
	public void draw() {	mTheObject.draw();	}
	// EndRegion

	// Region: protected operations Not visible to the world
	/**
	 * Push the input GameObject out of the circular bounds of this object
	 * @param other the input GameObject.
	 * @return the position between the center of the two objects
	 */
	protected Vector2 pushOutCircle(GameObject other) {
		return mTheObject.pushOutCircle(other.mTheObject);
	}
	// EndRegion: protected
		
}
