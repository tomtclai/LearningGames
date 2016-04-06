package Engine;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Maintains a segmented horizontal Segment bar
 * @author Michael Letter
 */
public class SegmentedBar extends Rectangle {
	private int maxSegments = 0;
	/** stores the Segment the bar will be representing must be less than SegmentBarDividers.length */
	private int filledSegments = 0;
	/** Stores the image used on a bar Segment if it is active */
	private String activeImage = "";
	/** Stores the image used on a bar Segment if it is not active */
	private String inactiveImage = "";
	/** if True the Bar will be Drawn Vertically. Otherwise it will be drawn horizontal */
	public boolean vertical = false;
	/** If True, when drawn will be drawn with the given textures rather than this rectangles color. If False, the bar will be drawn with the current color*/
	public boolean useImages = false;
	/**
	 * if "useImages" is True will set the texture drawn for the active segments of bar
	 * @param activeImageLocation the location of the target image
	 */
	public void setActiveImage(String activeImageLocation){
		if(activeImageLocation != null){
			activeImage = activeImageLocation;
		}
	}
	/**
	 * Sets the image to show when inactive.
	 * @param inactiveImageLocation the location of the target image
	 */
	public void setInactiveImage(String inactiveImageLocation){
		if(inactiveImageLocation != null){
			inactiveImage = inactiveImageLocation;
		}
	}
	/**
	 * will set the max Segment of the bar
	 * @param newmaxSegments maximum number of bars to show.
	 * @return whether or not changes were made
	 */
	public boolean setMaxSegments(int newmaxSegments){
		if(newmaxSegments >= 0){
			maxSegments = newmaxSegments;
			if(filledSegments > maxSegments){
				filledSegments = maxSegments;
			}
			return true;
		}
		return false;
	}
	/**
	 * Gets maximum number of bar that will be shown.
	 * @return the max number of bars that will be shown.
	 */
	public int getMaxSegments(){
		return maxSegments;
	}
	/**
	 * Will set the Segment displayed by the bar
	 * @param newfilledSegments number of the segments that will be filled.
	 * @return whether or not changes were made
	 */
	public boolean setFilledSegments(int newfilledSegments){
		if(newfilledSegments >= 0){
			filledSegments = newfilledSegments;
			if(filledSegments > maxSegments){
				filledSegments = maxSegments;
			}
			return true;
		}
		return false;
	}
	/**
	 * Gets the number of segments that are filled.
	 * @return The number of segments that are filled.
	 */
	public int getfilledSegments(){
		return filledSegments;
	}
	/**
	 * Will add as much of increment to the filledSegments as possible 
	 * @param increment amount to be added.
	 */
	public void incrementSegmentsFilled(int increment){
		filledSegments += increment;
		if(filledSegments < 0){
			filledSegments = 0;
		}
		else if(filledSegments > maxSegments){
			maxSegments = filledSegments;
		}
	}
	/** Will Draw the given Segment bar */
	public void draw(){
		//super.draw();
		if(BaseCode.resources != null && visible && maxSegments > 0){
			//Vertical
			if(vertical){
				//Images
				if(useImages){
					float segmentWidth = super.size.getY() / (float)maxSegments;
					float firstCorner = center.getY() - (size.getY()/2);
				
					//Empty Segments
					int loop = 0;
					super.setImage(inactiveImage);
					while(loop < (maxSegments - filledSegments) && texture != null){
						if(texture != null){
							BaseCode.resources.drawImage(texture, center.getX() - (size.getX()/2), firstCorner + (segmentWidth * loop), center.getX() + (size.getX()/2), firstCorner + (segmentWidth * (loop + 1)), rotate);
						}
						loop++;
					}
					//Filled Segments
					super.setImage(activeImage);
					while(loop < maxSegments){
						if(texture != null){
							BaseCode.resources.drawImage(texture, center.getX() - (size.getX()/2), firstCorner + (segmentWidth * loop), center.getX() + (size.getX()/2), firstCorner + (segmentWidth * (loop + 1)), rotate);
						}
						loop++;
					}
				}
				//Color
				else{
					float segmentHeight = super.size.getY() / (float)maxSegments;
					float firstCenter = super.center.getY() - (super.size.getY()/2f) + (segmentHeight/2);
					
					BaseCode.resources.setDrawingColor(color);
					if(filledSegments > 0){
						BaseCode.resources.drawFilledRectangle(center.getX(), firstCenter + ((segmentHeight * (filledSegments - 1))/2), size.getX()/2, (segmentHeight/2) * filledSegments, 0);
					}
					BaseCode.resources.setDrawingColor(Color.black);
					for(int loop = 0; loop < maxSegments; loop++){
						BaseCode.resources.drawOutlinedRectangle(center.getX(), firstCenter + (segmentHeight * loop), size.getX()/2, (segmentHeight/2), 0);
					}
				}
			}
			//Horizontal
			else{
				//Images
				if(useImages){
					float segmentWidth = super.size.getX() / (float)maxSegments;
					float firstCorner = center.getX() - (size.getX()/2);
					//Empty Segments
					int loop = 0;
					super.setImage(inactiveImage);
					while(loop < (maxSegments - filledSegments)){
						if(texture != null){
							BaseCode.resources.drawImage(texture, firstCorner + (segmentWidth * loop), center.getY() - (size.getY()/2), firstCorner + (segmentWidth * (loop + 1)), center.getY() + (size.getY()/2), rotate);
						}
						loop++;
					}
					//Filled Segments
					super.setImage(activeImage);
					while(loop < maxSegments){
						if(texture != null){
							BaseCode.resources.drawImage(texture, firstCorner + (segmentWidth * loop), center.getY() - (size.getY()/2), firstCorner + (segmentWidth * (loop + 1)), center.getY() + (size.getY()/2), rotate);
						}
						loop++;
					}
				}
				//Color
				else{
					float segmentWidth = super.size.getX() / (float)maxSegments;
					float firstCenter = super.center.getX() - (super.size.getX()/2f) + (segmentWidth/2);
					
					BaseCode.resources.setDrawingColor(color);
					if(filledSegments > 0){
						BaseCode.resources.drawFilledRectangle(firstCenter + ((segmentWidth * (filledSegments - 1))/2), center.getY(), (segmentWidth/2) * filledSegments, size.getY()/2, rotate);
					}
					BaseCode.resources.setDrawingColor(Color.black);
					for(int loop = 0; loop < maxSegments; loop++){
						BaseCode.resources.drawOutlinedRectangle(firstCenter + (segmentWidth * loop), center.getY(), (segmentWidth/2), size.getY()/2, rotate);
					}
				}
			}
		}
	}

	// Region Set/Get to because Rectangle is private (DO NOT, DO NOT make Rectangle a public class!!)
	// Region: get/set center position
		/**
		 * Access the center of the game object.
		 * @return the center position.
		 */
		public Vector2 getCenter() { return center; }
		
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
		public Vector2 getSize() { return size; }
		
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
		public Vector2 getVelocity() { return velocity; }
		
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
		public boolean isVisible() { return visible; }
		/**
		 * Sets the visibility of this object
		 * @param flag	True: visible, false: not visible
		 */
		public void setVisibilityTo(boolean flag) { visible = flag; }
		/**
		 * sets this object to be invisible.
		 */
		public void setToInvisible() { visible = false; }
		/**
		 * sets this object to be visible
		 */
		public void setToVisible() { visible = true; }
		/**
		 * toggles visibility of this object.
		 */
		public void toggleVisibility() { visible = !visible; }
		// EndRegion: get/set visibility
		
		// Region: get/set color
		/**
		 * Access the current color of the game object
		 * @return color of the game object
		 */
		public Color getColor() { return color; }
		/**
		 * sets the color of this object
		 * @param c the new color to be set to.
		 */
		public void setColor(Color c) { color = c; }
		// EndRegion: get/set Color
		
		// Region: get/set rotation
		/**
		 * Access the current rotation of the game object
		 * @return rotation of the game object (in radian)
		 */
		public float getRotation() { return rotate; }
		/**
		 * sets the rotation of this object
		 * @param r the new rotation for the object (in radian)
		 */
		public void setRotation(float r) { rotate = r; }
		/**
		 * changes the rotation of this object
		 * @param dr the amount of rotation to change (in radian)
		 */
		public void changeRotationBy(float dr) { rotate += dr; }
		// EndRegion
		
		// Region: get/set image and spriteSheet
		public BufferedImage getImage() { return texture; }
		/**
		 * sets an image to show for this object (texture over this object)
		 * @param imageFile filename to the image 
		 */
		public void setImage(String imageFile) { setImage(imageFile); }
		public void setImage(BufferedImage i) { texture = i; }
		
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
			setSpriteSheet(spriteFilename, width, height, totalFrames, ticksPerFrame);
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
			setSpriteSheet(image, width, height, totalFrames, ticksPerFrame);
		}
		/**
		 * pauses the sprite animation on this object.
		 * @param pause true to pause the animation.
		 */
		public void setAnimationPauseStatus(boolean pause)
		{
			setAnimationPauseStatus(pause);
		}
		/**
		 * Enable/disable the spritesheet animation on this object.
		 * @param on True: use sprite sheet animation, False: do not use sprite sheet animation
		 */
		public void setUsingSpriteSheet(boolean on)	{
			setUsingSpriteSheet(on);
		}
		/**
		 * controls the drawing of the GameObject to only drawing outline or not.
		 * Default: fills.
		 * @param onlyOutlined if true only draws outlined, otherwise draws regular (filled)
		 */
		public void setToDrawOutline(boolean onlyOutlined) {
			setDrawFilledRect(!onlyOutlined);
		}
		
		// EndRegion: get/set image and spriteSheet
		
		// Region: Auto drawing support
	// EndRegion
	
}
