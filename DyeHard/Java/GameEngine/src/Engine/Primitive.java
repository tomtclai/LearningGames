
package Engine;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;
import Engine.World.BoundCollidedStatus;

public abstract class Primitive
{
  public static ResourceHandler resources = null;

  public Vector2 center = new Vector2();
  public Vector2 size = new Vector2(50.0f, 50.0f);
  public Color color = Color.GREEN;
  public BufferedImage texture = null;
  public Vector2 velocity = new Vector2();
  public Vector2 acceleration = new Vector2();
  public Vector2 terminalVelocity = new Vector2();
  public boolean hasTerminalVelocity = false;
  public boolean shouldTravel = true;
  public boolean alwaysOnTop = false;

  public float rotate = 0.0f;

  public boolean visible = true;

  public Primitive()
  {
    Random rand = new Random();
    color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

    addToAutoDrawSet();
  }

  public Primitive(Primitive other)
  {
    center = other.center.clone();
    size = other.size.clone();
    color = other.color;
    texture = other.texture;
    velocity = other.velocity.clone();
    acceleration = other.acceleration.clone();
    terminalVelocity = other.terminalVelocity.clone();
    hasTerminalVelocity = other.hasTerminalVelocity;
    shouldTravel = other.shouldTravel;
    alwaysOnTop = other.alwaysOnTop;
    rotate = other.rotate;
    visible = other.visible;

    addToAutoDrawSet();
  }

  /**
   * Set this object to automatically be drawn every frame.
   */
  public void addToAutoDrawSet()
  {
    BaseCode.resources.addToAutoDrawSet(this);
  }

  /**
   * Remove this object from being drawn every frame.
   */
  public void removeFromAutoDrawSet()
  {
    BaseCode.resources.removeFromAutoDrawSet(this);
  }

  /**
   * Move this object to the back.
   */
  /*
  public void moveToBackOfDrawSet()
  {
    ResourceHandler.obj.moveToBackOfDrawSet(this);
  }
  */

  /**
   * Move this object to the front.
   */
  /*
  public void moveToFrontOfDrawSet()
  {
    ResourceHandler.obj.moveToFrontOfDrawSet(this);
  }
  */

  /**
   * Set the image for this object.
   * 
   * @param fileName
   *          - The file name of the image.
   */
  public void setImage(String fileName)
  {
    texture = BaseCode.resources.loadImage(fileName);
  }

  /**
   * Draw the object. Will be called automatically by default until removed from
   * draw set.
   */
  public void drawPrimitive()
  {
    if(visible)
    {
      draw();
    }
  }

  /**
   * Update the object.
   */
  public void update()
  {
    if(shouldTravel)
    {
      if(hasTerminalVelocity)
      {
        velocity.setX(Math.min(velocity.getX(), terminalVelocity.getX()));
        velocity.setY(Math.min(velocity.getY(), terminalVelocity.getY()));
      }

      velocity.add(acceleration);

      center.add(velocity);
    }
  }

  /**
   * Check if the primitive is touching or outside of any side of the world.
   * 
   * @return - The side of the world that the object is overlapping.
   */
  public BoundCollidedStatus collideWorldBound()
  {
    if(!visible)
    {
      return BoundCollidedStatus.INSIDEBOUND;
    }

    return BaseCode.world.collideWorldBound(this);
  }

  /**
   * Check if the given object is not inside of the given world bound.
   * 
   * @return - True if touching or outside of any side of the world, otherwise
   *         false.
   */
  public boolean checkWorldBound(BoundCollidedStatus whichBound)
  {
    if(!visible)
    {
      return false;
    }

    return BaseCode.world.checkWorldBound(this, whichBound);
  }

  /**
   * Check if any part of the primitive is inside the world.
   * 
   * @return - True if any part of the primitive is inside the world, otherwise
   *         false.
   */
  public boolean isInsideWorldBound()
  {
    return visible && BaseCode.world.isInsideWorldBound(this);
  }

  /**
   * Remove the object from drawing and updating.
   */
  public void destroy()
  {
    removeFromAutoDrawSet();
    visible = false;
  }

  /**
   * Draw the object.
   */
  public abstract void draw();
}
