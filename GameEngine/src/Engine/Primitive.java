
package Engine;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;
import Engine.World.BoundCollidedStatus;

// note this class is private! to this package, will NOT be visible outside of GameEngine!! 
abstract class Primitive
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

  // the DrawingLayer that contains this object
  protected Primitive myDrawingLayer = null;
  
  
 
  
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
   * Moves this object to the end of the AutoDrawSet or the DrawingLayer it is in,
   * meaning it will be drawn last (And hence be on top of everything else).
   */
  public void moveToFront()
  {
	  if(myDrawingLayer == null)
	  {
		  removeFromAutoDrawSet();
		  addToAutoDrawSet();  
	  }
	  else
	  {
		  Primitive drawingLayerBackup = myDrawingLayer;
		  drawingLayerBackup.remove(this);
		  drawingLayerBackup.add(this);
	  }
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
   *
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
   *
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
   *
  public boolean isInsideWorldBound()
  {
    return visible && BaseCode.world.isInsideWorldBound(this);
  }
*/
  /**
   * Remove the object from drawing and updating.
   */
  public void destroy()
  {
    if(myDrawingLayer != null)
    {
    	myDrawingLayer.remove(this);
    }

    removeFromAutoDrawSet();
    
    texture = null;
    visible = false;
  }
  
  
  /**
   * If this object is in a DrawingLayer, removes
   * it from that DrawingLayer and places it back in
   * the AutoDrawSet.
   */
  public void removeFromDrawingLayer()
  {
	  if(myDrawingLayer != null)
	  {
		  myDrawingLayer.remove(this);
	  }
  }
  
  /**
   * This is only here so it can be overridden by extending class DrawingLayer,
   * so it can be used by the above destroy() method and moveToFront().
   * 
   * This is probably NOT the best way to do this, but since I am trying
   * to get DrawingLayers working without rewriting the Engine, it seems
   * like a good solution for now.
   * 
   * @param prim		Any primitive you want
   * @return			True
   */
  public boolean remove(Primitive prim)
  {
	  return true;
  }
  
  /**
   * This is only here so it can be overridden by extending class DrawingLayer,
   * so it can be used by the above moveToFront() method.
   * 
   * This is probably NOT the best way to do this, but since I am trying
   * to get DrawingLayers working without rewriting the Engine, it seems
   * like a good solution for now.
   * 
   * @param prim		The primitive we will do nothing to	
   */
  public void add(Primitive prim)
  {
  }

  
  
  
  
  
  
  
  /**
   * Draw the object.
   */
  public abstract void draw();
}
