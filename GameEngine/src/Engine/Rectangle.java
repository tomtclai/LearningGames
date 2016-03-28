
package Engine;

import java.awt.image.BufferedImage;

// Sprite sheet code ported from the C# engine, which
// is credited to Samuel Cook and Ron Cook for adding support for that.
class Rectangle extends Primitive
{
	// Note: this class is package private, we DO NOT want anyone to see this outside of this class

  public enum SpriteSheetAnimationMode
  {
    ANIMATE_FORWARD,         // Animates from first frame to last frame.
    
    ANIMATE_BACKWARD,        // Animates from last frame to first.
    
    ANIMATE_SWING,           // Animates from first to last then to first
                             //   again and so forth.
    
    ANIMATE_FORWARD_STOP,    // Animates from first to last frame then
                             //   stops on final frame.
    
    ANIMATE_BACKWARD_STOP   // Animates from last to first frame then
                             //   stops on first frame.
  };
  
  /**
   * frameCoords holds the lower left and upper right coordinates of each
   * frame of the sprite sheet.
   * frameCoords[][0] = lower left x
   * frameCoords[][1] = lower left y
   * frameCoords[][2] = upper right x
   * frameCoords[][3] = upper right y
   */
  private int[][] mFrameCoords;
  private int mCurrentFrame;
  private int mTotalFrames;
  private int mFirstFrame;
  private int mLastFrame;
  
  private int mTicksPerFrame;
  private int mCurrentTick;
  private boolean mSwingDirectionLeft = false; // For swing animation, if false, go from
                                               // first to last frame, if true go from
                                               // last to first frame.
  
  private boolean mPauseAnimation = false;     // If set to true, spritesheet will not advance
  
  private boolean mDrawImage = true;
  private boolean mDrawFilledRect = true;
  
  private SpriteSheetAnimationMode mAnimationMode = SpriteSheetAnimationMode.ANIMATE_FORWARD;
  private boolean mUsingSpriteSheet = false;
  
  private boolean mHorizontalFlip = false;     // Flips on horizontal axis. (Up and Down swapped)
  private boolean mVerticalFlip = false;       // Flips on vertical axis (Left and Right swapped)
  
  private boolean mNonLoopingAnimationInProgress = false;	// This is used to tell whether or not an ANIMATE_FORWARD_STOP
  															// or ANIMATED_BACKWARD_STOP is finished yet.
  private boolean mLoopingAnimationInProgress = false;		// Used to tell if we are animation a looping animation.

  public Rectangle()
  {
    super();
  }

  public Rectangle(Rectangle other)
  {
    super(other);

    mDrawImage = other.mDrawImage;
    mDrawFilledRect = other.mDrawFilledRect;
  }
  
  public boolean hasNonLoopingAnimationInProgress()
  {
	  return mNonLoopingAnimationInProgress;
  }
  
  public boolean hasLoopingAnimationInProgress()
  {
	  return mLoopingAnimationInProgress;
  }
  
  public boolean hasAnimationInProgress()
  {
	  return mLoopingAnimationInProgress || mNonLoopingAnimationInProgress;
  }
  
  
  public boolean usingSpriteSheet()
  {
	  return mUsingSpriteSheet;
  }
  public void setUsingSpriteSheet(boolean usingSpriteSheet)
  {
	  this.mUsingSpriteSheet = usingSpriteSheet;
  }
  public boolean getHorizontalFlip()
  {
	  return mHorizontalFlip;
  }
  public void setHorizontalFlip(boolean horizontalFlip)
  {
	  mHorizontalFlip = horizontalFlip;
  }
  public boolean getVerticalFlip()
  {
	  return mVerticalFlip;
  }
  public void setVerticalFlip(boolean verticalFlip)
  {
	  mVerticalFlip = verticalFlip;
  }
  
  public void setDrawImage(boolean value)
  {
    mDrawImage = value;
  }

  public void setDrawFilledRect(boolean value)
  {
    mDrawFilledRect = value;
  }
  /**
   * Sets up the spritesheet animation. Will not do anything if width, height, or totalFrame are less than 0.
   * 
   * @param textureFilename Filename of the spritesheet image
   * @param width Width of individual sprite.
   * @param height Height of individual sprite.
   * @param totalFrames Total sprites in spritesheet
   * @param ticksPerFrame How long each sprite remains before the next is drawn.
   */
  public void setSpriteSheet(String textureFilename, int width, int height, int totalFrames, int ticksPerFrame)
  {
	  if(totalFrames <= 0 || width <= 0 || height <= 0)
	  {
		  return;
	  }
	  setImage(textureFilename);
	  initializeSpriteSheet(width,height,totalFrames,ticksPerFrame);
  }
  /**
   * Sets up the spritesheet animation. Will not do anything if width, height, or totalFrame are less than 0.
   * 
   * @param texture the spritesheet image
   * @param width Width of individual sprite.
   * @param height Height of individual sprite.
   * @param totalFrames Total sprites in spritesheet
   * @param ticksPerFrame How long each sprite remains before the next is drawn.
   */
  public void setSpriteSheet(BufferedImage texture, int width, int height, int totalFrames, int ticksPerFrame)
  {
	  if(totalFrames <= 0 || width <= 0 || height <= 0)
	  {
		  return;
	  }
	  this.texture = texture;
	  initializeSpriteSheet(width,height,totalFrames,ticksPerFrame);
  }
  
  private void initializeSpriteSheet(int width, int height, int totalFrames, int ticksPerFrame)
  {
	  mFrameCoords = new int[totalFrames][4];
	  this.mTotalFrames = totalFrames;
	  mCurrentFrame = 0;
	  
	  mCurrentTick = 0;
	  this.mTicksPerFrame = ticksPerFrame;
	  
	  int currentFrameX = 0;
	  int currentFrameY = 0;
	  
	  for(int frame = 0; frame < totalFrames; frame++)
	  {
		  if(currentFrameX + width > texture.getWidth())
		  {
			  currentFrameX = 0;
			  currentFrameY += height;
		  }
		  if(currentFrameY + height > texture.getHeight())
		  {
			  break;
		  }
		  mFrameCoords[frame][0] = currentFrameX + width;
		  mFrameCoords[frame][1] = currentFrameY + height;
		  mFrameCoords[frame][2] = currentFrameX;
		  mFrameCoords[frame][3] = currentFrameY;
		  
		  currentFrameX += width;
	  }
	  mFirstFrame = 0;
	  mLastFrame = mTotalFrames - 1;
  }
  /**
   * Sets the mode of animation for the spritesheet animation.
   * Will do nothing if firstFrame or lastFrame is less than 0.
   * 
   * During the animation, if LastFrame exceeds mTotalFrames,
   *  an error will occur.
   * 
   * @param firstFrame First frame in the spritesheet to animate.
   * @param lastFrame  Last frame in the spritesheet to animate.
   * @param animationMode The type of animation that will be followed.
   */
  public void setAnimationMode(int firstFrame, int lastFrame,
		  SpriteSheetAnimationMode animationMode)
  {
	  if(firstFrame < 0 || lastFrame < 0)
		  return;
	  
	  mFirstFrame = firstFrame;
	  mLastFrame = lastFrame;
	  mAnimationMode = animationMode;
	  
	  if(animationMode == SpriteSheetAnimationMode.ANIMATE_BACKWARD ||
			  animationMode == SpriteSheetAnimationMode.ANIMATE_BACKWARD_STOP)
	  {
		  mCurrentFrame = mLastFrame;
	  }
  }
  
 

  // Helper getters for drawing a portion of an image(the spritesheet)
  private int getSpriteUpperX()
  {
	  return mFrameCoords[mCurrentFrame][0];
  }
  private int getSpriteUpperY()
  {
	  return mFrameCoords[mCurrentFrame][1];
  }
  private int getSpriteLowerX()
  {
	  return mFrameCoords[mCurrentFrame][2];
  }
  private int getSpriteLowerY()
  {
	  return mFrameCoords[mCurrentFrame][3];
  }
  
  // Helper getters for drawing onto the screen.
  // Swapping these values allows vertical and horizontal flips.
  private float getOnScreenLowerX()
  {
	  if(mVerticalFlip)
		  return center.getX() + (size.getX() * 0.5f);
	  return center.getX() - (size.getX() * 0.5f);
  }
  private float getOnScreenLowerY()
  {
	  if(mHorizontalFlip)
		  return center.getY() + (size.getY() * 0.5f);
	  return center.getY() - (size.getY() * 0.5f);
  }
  private float getOnScreenUpperX()
  {
	  if(mVerticalFlip)
		  return center.getX() - (size.getX() * 0.5f);
	  return center.getX() + (size.getX() * 0.5f);
  }
  private float getOnScreenUpperY()
  {
	  if(mHorizontalFlip)
		  return center.getY() - (size.getY() * 0.5f);
	  return center.getY() + (size.getY() * 0.5f);
  }

  
  public void draw()
  {
    if(mDrawImage && texture != null)
    {
      if(mUsingSpriteSheet)
      {
    	  BaseCode.resources.drawImage(texture,
    			  	getOnScreenLowerX(),
          			getOnScreenLowerY(),
          			getOnScreenUpperX(),
          			getOnScreenUpperY(),
    	    	    getSpriteLowerX(),
    			    getSpriteLowerY(),
    			    getSpriteUpperX(),
    			    getSpriteUpperY(),
    			    rotate);
    	  if(!mPauseAnimation)
    	  {
    	  updateSpriteSheetAnimation();
    	  }
      }
      else
      {
        BaseCode.resources.drawImage(texture,
        		getOnScreenLowerX(),
        		getOnScreenLowerY(),
        		getOnScreenUpperX(),
        		getOnScreenUpperY(),
                rotate);
      }
    }
    else
    {
      BaseCode.resources.setDrawingColor(color);

      if(mDrawFilledRect)
      {
        BaseCode.resources.drawFilledRectangle(center.getX(), center.getY(),
            size.getX() * 0.5f, size.getY() * 0.5f, rotate);
      }
      else
      {
        BaseCode.resources.drawOutlinedRectangle(center.getX(), center.getY(),
            size.getX() * 0.5f, size.getY() * 0.5f, rotate);
      }
    }
  }
  
  //***************************************************************************
  // Animation Modes
  //***************************************************************************
  
  /**
   * Updates the currently drawn sprite of the spritesheet.
   * The type of update depends on the current mAnimationMode
   * and the first and last frames.
   * 
   * This is called after the actual draw, as are all related
   * methods.
   */
  private void updateSpriteSheetAnimation()
  {
	  switch(mAnimationMode)
	  {
	  case ANIMATE_FORWARD:
		  updateForwardAnimation();
		  break;
	  case ANIMATE_BACKWARD:
		  updateBackwardAnimation();
		  break;
	  case ANIMATE_BACKWARD_STOP:
		  updateBackwardStopAnimation();
		  break;
	  case ANIMATE_FORWARD_STOP:
		  updateForwardStopAnimation();
		  break;
	  case ANIMATE_SWING:
		  updateSwingAnimation();
		  break;
	  default:
		  break;
	  }
  }
  private void updateForwardAnimation()
  {
	  mNonLoopingAnimationInProgress = false;
	  mLoopingAnimationInProgress = true;
	  
	  // Waits for as many ticks per draw opportunity.
	  if(mCurrentTick < mTicksPerFrame)
	  {
		  mCurrentTick++;
		  return;
	  }
	  
	  mCurrentTick = 0;
	  
	  // Updates frames, loops back if at final frame.
	  if(mCurrentFrame >= mLastFrame)
	  {
		  mCurrentFrame = mFirstFrame;
		  return;
	  }
	  ++mCurrentFrame;
  }
  private void updateForwardStopAnimation()
  {
	  mNonLoopingAnimationInProgress = true;
	  mLoopingAnimationInProgress = false;
	  
	  // Waits for as many ticks per draw opportunity.
	  if(mCurrentTick < mTicksPerFrame)
	  {
		  mCurrentTick++;
		  return;
	  }
	  
	  mCurrentTick = 0;
	  
	  // Updates frames, stop animation if at final frame.
	  if(mCurrentFrame >= mLastFrame)
	  {
		  mNonLoopingAnimationInProgress = false;
		  return;
	  }
	  
	  ++mCurrentFrame;
  }
  private void updateBackwardAnimation()
  {
	  mNonLoopingAnimationInProgress = false;
	  mLoopingAnimationInProgress = true;
	  
	  // Waits for as many ticks per draw opportunity.
	  if(mCurrentTick < mTicksPerFrame)
	  {
		  mCurrentTick++;
		  return;
	  }
	  
	  mCurrentTick = 0;
	  
	  // Updates frames, loops back if at final frame.
	  if(mCurrentFrame <= mFirstFrame)
	  {
		  mCurrentFrame = mLastFrame;
		  return;
	  }
	  --mCurrentFrame;
  }
  
  private void updateBackwardStopAnimation()
  {
	  mNonLoopingAnimationInProgress = true;
	  mLoopingAnimationInProgress = false;
	  
	  // Waits for as many ticks per draw opportunity.
	  if(mCurrentTick < mTicksPerFrame)
	  {
		  mCurrentTick++;
		  return;
	  }
	  
	  mCurrentTick = 0;
	  
	  // Updates frames, stops on firstFrame
	  if(mCurrentFrame <= mFirstFrame)
	  {
		  mNonLoopingAnimationInProgress = false;
		  return;
	  }
	  --mCurrentFrame;
  }
  
  private void updateSwingAnimation()
  {
	  mNonLoopingAnimationInProgress = false;
	  mLoopingAnimationInProgress = true;
	  
	  // Waits for as many ticks per draw opportunity.
	  if(mCurrentTick < mTicksPerFrame)
	  {
		  mCurrentTick++;
		  return;
	  }
	  
	  mCurrentTick = 0;
	  
	  if(mSwingDirectionLeft)
	  {
		  // Updates frames, loops back if at final frame.
		  if(mCurrentFrame <= mFirstFrame)
		  {
			  mSwingDirectionLeft = false;
			  return;
		  }
		  --mCurrentFrame;
	  }
	  else
	  {
		  // Updates frames, loops back if at final frame.
		  if(mCurrentFrame >= mLastFrame)
		  {
			  mSwingDirectionLeft = true;
			  return;
		  }
		  ++mCurrentFrame;
	  }
  }
  
  /**
   * sets the paused status of the animation
   * @param animate if true, animation will be paused
   */
  public void setAnimationPauseStatus(boolean pause)
  {
	  mPauseAnimation = pause;
  }
  
  /**
   * Gets the animation pause state
   * @return True if animation is paused
   */
  public boolean isAnimationPaused()
  {
	  return mPauseAnimation;
  }
  //***************************************************************************
  // Collision and related methods
  //***************************************************************************
  
  public boolean containsPoint(Vector2 point)
  {
    if(rotate != 0.0f)
    {
      // Rotate the point to match this object's rotation
      point = point.clone().sub(center).rotate(-rotate).add(center);
    }

    return (point.getX() >= (center.getX() - (size.getX() * 0.5f))) &&
        (point.getX() < (center.getX() + (size.getX() * 0.5f))) &&
        (point.getY() >= (center.getY() - (size.getY() * 0.5f))) &&
        (point.getY() < (center.getY() + (size.getY() * 0.5f)));
  }

  public boolean pixelTouches(Rectangle otherPrim)
  {
    return pixelTouches(otherPrim, null);
  }

  // From the C# code "TexturedPrimitivePixelCollide.cs"
  public boolean pixelTouches(Rectangle otherPrim, Vector2 collidePoint)
  {
    if(texture == null || otherPrim.texture == null)
    {
      return false;
    }

    boolean touches = primitivesTouches(otherPrim);
    //collidePoint.set(center);

    if(touches)
    {
      boolean pixelTouch = false;

      Vector2 myXDir =
          Vector2.rotateVectorByAngle(Vector2.unitX,
              (float)Math.toRadians(rotate));
      Vector2 myYDir =
          Vector2.rotateVectorByAngle(Vector2.unitY,
              (float)Math.toRadians(rotate));

      Vector2 otherXDir =
          Vector2.rotateVectorByAngle(Vector2.unitX,
              (float)Math.toRadians(otherPrim.rotate));
      Vector2 otherYDir =
          Vector2.rotateVectorByAngle(Vector2.unitY,
              (float)Math.toRadians(otherPrim.rotate));

      if(collidePoint == null)
      {
        collidePoint = new Vector2();
      }

      int i = 0;
      while((!pixelTouch) && (i < texture.getWidth()))
      {
        int j = 0;

        while((!pixelTouch) && (j < texture.getHeight()))
        {
          collidePoint.set(indexToCameraPosition(i, j, myXDir, myYDir));
          int myColor = ((texture.getRGB(i, j) >> 24) & 0xff);

          if(myColor > 0)
          {
            Vector2 otherIndex =
                otherPrim.cameraPositionToIndex(collidePoint, otherXDir,
                    otherYDir);
            int xMin = (int)otherIndex.getX();
            int yMin = (int)otherIndex.getY();

            if((xMin >= 0) && (xMin < otherPrim.texture.getWidth()) &&
                (yMin >= 0) && (yMin < otherPrim.texture.getHeight()))
            {
              pixelTouch =
                  (((otherPrim.texture.getRGB(xMin, yMin) >> 24) & 0xff) > 0);
            }
          }

          j++;
        }

        i++;
      }

      touches = pixelTouch;
    }

    return touches;
  }

  private Vector2
      indexToCameraPosition(int i, int j, Vector2 xDir, Vector2 yDir)
  {
    // Translate from percent across image to percent across size
    float x = i * size.getX() / (float)(texture.getWidth() - 1);
    float y = j * size.getY() / (float)(texture.getHeight() - 1);

    Vector2 r =
        center.clone().add(xDir.clone().mult(x - (size.getX() * 0.5f)))
            .sub(yDir.clone().mult(y - (size.getY() * 0.5f)));

    return r;
  }

  private Vector2 cameraPositionToIndex(Vector2 p, Vector2 xDir, Vector2 yDir)
  {
    Vector2 delta = p.clone().sub(center);

    float xOffset = Vector2.dot(delta, xDir);
    float yOffset = Vector2.dot(delta, yDir) - 1;

    float i = texture.getWidth() * (xOffset / size.getX());
    float j = texture.getHeight() * (yOffset / size.getY());
    i += texture.getWidth() / 2;
    j = (texture.getHeight() / 2) - j;

    return new Vector2(i, j);
  }

  private boolean primitivesTouches(Rectangle otherPrim)
  {
    final float epsilon = 0.0001f;

    if(Math.abs(Math.toRadians(rotate)) < epsilon &&
        Math.abs(Math.toRadians(otherPrim.rotate)) < epsilon)
    {
      return ((center.getX() - (size.getX() * 0.5f)) < (otherPrim.center.getX() + (otherPrim.size
          .getX() * 0.5f))) &&
          ((center.getX() + (size.getX() * 0.5f)) > (otherPrim.center.getX() - (otherPrim.size
              .getX() * 0.5f))) &&
          ((center.getY() - (size.getY() * 0.5f)) < (otherPrim.center.getY() + (otherPrim.size
              .getY() * 0.5f))) &&
          ((center.getY() + (size.getY() * 0.5f)) > (otherPrim.center.getY() - (otherPrim.size
              .getY() * 0.5f)));
    }
    else
    {
      // From the C# version:
      // one of both are rotated ... use radius ... be conservative
      // Use the larger of the Width/Height and approx radius
      //   Sqrt(1/2)*x Approx = 0.71f * x;
      float r1 = 0.71f * Math.max(size.getX(), size.getY());
      float r2 = 0.71f * Math.max(otherPrim.size.getX(), otherPrim.size.getY());
      return (otherPrim.center.clone().sub(center).length() < (r1 + r2));
    }
  }

  public boolean collided(Rectangle otherPrim)
  {
    return (visible && otherPrim.visible && primitivesTouches(otherPrim));
  }

  // Pushes the other object out of the current object
  public Vector2 pushOutCircle(Rectangle other)
  {
    Vector2 resolved = null;

    if(other != null)
    {
      float topD = 0f, bottomD = 0f, leftD = 0f, rightD = 0f;

      // Flying upwards
      if(other.velocity.getY() > 0.0f)
      {
        // Check for bottom penetration
        topD =
            (other.center.getY() + (other.size.getY() * 0.5f)) -
                (center.getY() - (size.getY() * 0.5f));
      }
      // Flying downwards
      else
      {
        // Check for top penetration
        bottomD =
            (center.getY() + (size.getY() * 0.5f)) -
                (other.center.getY() - (other.size.getY() * 0.5f));
      }

      // Flying towards right
      if(other.velocity.getX() > 0)
      {
        // Check for left penetration
        leftD =
            (other.center.getX() + (other.size.getX() * 0.5f)) -
                (center.getX() - (size.getX() * 0.5f));
      }
      // Flying towards left
      else
      {
        // Check for right penetration
        rightD =
            (center.getX() + (size.getX() * 0.5f)) -
                (other.center.getX() - (other.size.getX() * 0.5f));
      }

      if(topD > 0)
      {
        if(leftD > 0)
        {
          if(topD < leftD)
          {
            // Push up from top
            resolved = new Vector2();
            resolved.setY((center.getY() - (other.size.getY() * 0.5f) - (size
                .getY() * 0.5f)) - other.center.getY());
            other.center.setY(other.center.getY() + resolved.getY());
          }
          else
          {
            // Push towards left
            resolved = new Vector2();
            resolved.setX((center.getX() - (other.size.getX() * 0.5f) - (size
                .getX() * 0.5f)) - other.center.getX());
            other.center.setX(other.center.getX() + resolved.getX());
          }
        }
        else if(rightD > 0)
        {
          if(topD < rightD)
          {
            // Push up from top
            resolved = new Vector2();
            resolved.setY((center.getY() - (other.size.getY() * 0.5f) - (size
                .getY() * 0.5f)) - other.center.getY());
            other.center.setY(other.center.getY() + resolved.getY());
          }
          else
          {
            // Push towards right
            resolved = new Vector2();
            resolved.setX((center.getX() + (other.size.getX() * 0.5f) + (size
                .getX() * 0.5f)) - other.center.getX());
            other.center.setX(other.center.getX() + resolved.getX());
          }
        }
      }
      else if(bottomD > 0)
      {
        if(leftD > 0)
        {
          if(bottomD < leftD)
          {
            // Push up from bottom
            resolved = new Vector2();
            resolved.setY((center.getY() + (other.size.getY() * 0.5f) + (size
                .getY() * 0.5f)) - other.center.getY());
            other.center.setY(other.center.getY() + resolved.getY());
          }
          else
          {
            // Push towards left
            resolved = new Vector2();
            resolved.setX((center.getX() - (other.size.getX() * 0.5f) - (size
                .getX() * 0.5f)) - other.center.getX());
            other.center.setX(other.center.getX() + resolved.getX());
          }
        }
        else if(rightD > 0)
        {
          if(bottomD < rightD)
          {
            // Push up from bottom
            resolved = new Vector2();
            resolved.setY((center.getY() + (other.size.getY() * 0.5f) + (size
                .getY() * 0.5f)) - other.center.getY());
            other.center.setY(other.center.getY() + resolved.getY());
          }
          else
          {
            // Push towards right
            resolved = new Vector2();
            resolved.setX((center.getX() + (other.size.getX() * 0.5f) + (size
                .getX() * 0.5f)) - other.center.getX());
            other.center.setX(other.center.getX() + resolved.getX());
          }
        }
      }

      /*
      Vector2 otherCenter = new Vector2(other.center.x, other.center.y);
      float left = center.x - size.x;
      float right = center.x + size.x;
      float top = center.y + size.y;
      float bottom = center.y - size.y;
      otherCenter.x = clamp(otherCenter.x, left, right);
      otherCenter.y = clamp(otherCenter.y, top, bottom);
      Vector2 direction = other.center.sub(otherCenter);
      float dist = other.size.x - direction.length();
      direction.normalize();
      other.center.set(other.center.add(direction.mult(dist)));
      */
    }

    return resolved;
  }

  /*
  private float clamp(float val, float min, float max)
  {
    return (val < min ? val = min : (val > max ? val = max : val));
  }
  */
}
