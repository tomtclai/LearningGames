
package Engine;

import java.awt.image.BufferedImage;

// Sprite sheet code ported from the C# engine, which
// is credited to Samuel Cook and Ron Cook for adding support for that.
public class Rectangle extends Primitive
{
  private boolean drawImage = true;
  private boolean drawFilledRect = true;

  public enum SpriteSheetAnimationMode
  {
    ANIMATE_FORWARD, ANIMATE_BACKWARD, ANIMATE_SWING
  };

  private boolean usingSpriteSheet = false;
  /**
   * Sprite tolerance is for letting spritesheets with odd widths/heights
   * to include outermost sprites. This is because the spritemapping
   * algorithm does not include any part of a spritesheet that is
   * bigger than the sum of the sprites dimension (width or height).
   */
  
  
  /**
   * frameCoords holds the lower left and upper right coordinates of each
   * frame of the sprite sheet.
   * frameCoords[][0] = lower left x
   * frameCoords[][1] = lower left y
   * frameCoords[][2] = upper right x
   * frameCoords[][3] = upper right y
   * 
   */
  private int[][] frameCoords;
  private int currentFrame;
  private int totalFrames;
  
  private int ticksPerFrame;
  private int currentTick;
  public int rowsInSpriteSheet = 1;
  public int colsInSpriteSheet = 1;
  

  public Rectangle()
  {
    super();
  }

  public Rectangle(Rectangle other)
  {
    super(other);

    drawImage = other.drawImage;
    drawFilledRect = other.drawFilledRect;
  }
  
  public boolean usingSpriteSheet()
  {
	  return usingSpriteSheet;
  }
  public void setUsingSpriteSheet(boolean usingSpriteSheet)
  {
	  this.usingSpriteSheet = usingSpriteSheet;
  }
  /**
   * Sets up the spritesheet animation.
   * 
   * Will do nothing if width, height, or totalFrame are <= 0.
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
	  
	  frameCoords = new int[totalFrames][4];
	  this.totalFrames = totalFrames;
	  currentFrame = 0;
	  
	  currentTick = 0;
	  this.ticksPerFrame = ticksPerFrame;
	  
	  int currentFrameX = 0;
	  int currentFrameY = 0;
	  
	  colsInSpriteSheet = 1;
	  rowsInSpriteSheet = 1;
	  int colCounter = 0;
	  
	  for(int frame = 0; frame < totalFrames; frame++)
	  {
		  if(currentFrameY + height > texture.getHeight())
		  {
			  break;
		  }
		  if(currentFrameX + width > texture.getWidth() ||
				  isSubimageTransparent(texture,
						  currentFrameX,
						  currentFrameY,
						  width,
						  height))
		  {
			  currentFrameX = 0;
			  currentFrameY += height;
			  rowsInSpriteSheet++;
			  colCounter = 0;
		  }
		  else
			  colCounter++;
		  
		  if(colCounter > colsInSpriteSheet)
		  {
			  colsInSpriteSheet++;
		  }
		  
		  frameCoords[frame][0] = currentFrameX + width;
		  frameCoords[frame][1] = currentFrameY + height;
		  frameCoords[frame][2] = currentFrameX;
		  frameCoords[frame][3] = currentFrameY;
		  
		  currentFrameX += width;
		  
	  }
	  
  }
  
  private boolean isSubimageTransparent(BufferedImage texture, int xLoc,
		  int yLoc, int width, int height) 
  {
	int rgbArray[] = new int[width * height]; 
	texture.getRGB(xLoc, yLoc, width, height, rgbArray, 0, width);
	for(int i = 0; i < rgbArray.length; i++)
	{
		if((rgbArray[i] >> 24) != 0x00)
		{
			return false;
		}
	}
	return true;
}

public void setSpriteSheet(BufferedImage texture, int width, int height, int totalFrames, int ticksPerFrame)
  {
	  if(totalFrames <= 0 || width <= 0 || height <= 0)
	  {
		  return;
	  }
	  this.texture = texture;
	  
	  frameCoords = new int[totalFrames][4];
	  this.totalFrames = totalFrames;
	  currentFrame = 0;
	  
	  currentTick = 0;
	  this.ticksPerFrame = ticksPerFrame;
	  
	  int currentFrameX = 0;
	  int currentFrameY = 0;
	  
	  colsInSpriteSheet = 1;
	  rowsInSpriteSheet = 1;
	  int colCounter = 0;
	  
	  for(int frame = 0; frame < totalFrames; frame++)
	  {
		  if(currentFrameY + height > texture.getHeight())
		  {
			  break;
		  }
		  if(currentFrameX + width > texture.getWidth() ||
				  isSubimageTransparent(texture,
						  currentFrameX,
						  currentFrameY,
						  width,
						  height))
		  {
			  currentFrameX = 0;
			  currentFrameY += height;
			  rowsInSpriteSheet++;
			  colCounter = 0;
		  }
		  else
			  colCounter++;
		  
		  if(colCounter > colsInSpriteSheet)
		  {
			  colsInSpriteSheet++;
		  }
		  
		  frameCoords[frame][0] = currentFrameX + width;
		  frameCoords[frame][1] = currentFrameY + height;
		  frameCoords[frame][2] = currentFrameX;
		  frameCoords[frame][3] = currentFrameY;
		  
		  currentFrameX += width;
	  }
  }
  /**
   * Updates the currently drawn sprite of the spritesheet.
   */
  private void updateSpriteSheetAnimation()
  {
	  if(currentTick < ticksPerFrame)
	  {
		  currentTick++;
		  return;
	  }
	  
	  currentTick = 0;
	  
	  if(currentFrame >= totalFrames -1)
	  {
		  currentFrame = 0;
		  return;
	  }
	  ++currentFrame;
	  
  }  

  // Helper getters for drawing a portion of an image(the spritesheet)
  private int getSpriteUpperX()
  {
	  return frameCoords[currentFrame][0];
  }
  private int getSpriteUpperY()
  {
	  return frameCoords[currentFrame][1];
  }
  private int getSpriteLowerX()
  {
	  return frameCoords[currentFrame][2];
  }
  private int getSpriteLowerY()
  {
	  return frameCoords[currentFrame][3];
  }
  
  public void draw()
  {
    if(drawImage && texture != null)
    {
      if(usingSpriteSheet)
      {
    	  BaseCode.resources.drawImage(texture,
            		center.getX() - (size.getX() * 0.5f),
            		center.getY() - (size.getY() * 0.5f),
            		center.getX() + (size.getX() * 0.5f),
                    center.getY() + (size.getY() * 0.5f),
    	    	    getSpriteLowerX(),
    			    getSpriteLowerY(),
    			    getSpriteUpperX(),
    			    getSpriteUpperY(),
    			    rotate);
    	  updateSpriteSheetAnimation();
    	  
      }
      else
      {
        BaseCode.resources.drawImage(texture,
        		center.getX() - (size.getX() * 0.5f),
        		center.getY() - (size.getY() * 0.5f),
                center.getX() + (size.getX() * 0.5f),
                center.getY() + (size.getY() * 0.5f),
                rotate);
      }
    }
    else
    {
      BaseCode.resources.setDrawingColor(color);

      if(drawFilledRect)
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

  public void setDrawImage(boolean value)
  {
    drawImage = value;
  }

  public void setDrawFilledRect(boolean value)
  {
    drawFilledRect = value;
  }

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
