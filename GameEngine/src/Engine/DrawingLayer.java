
package Engine;

import java.util.LinkedList;



/**
 * A DrawingLayer is a group of Primitives that should be drawn together.
 * This object is intended to organize like groups of objects to make sure
 * that when one object needs to be drawn over the other, they are drawn in
 * the correct order.
 * 
 * If you have any experience with drawing programs that use layers,
 * like Paint.net or Photoshop, you can think of a DrawingLayer like this.
 * 
 * There is no guarantee of what order the objects within a DrawingLayer
 * will be drawn in.
 * 
 * Let's be honest with ourselves: a DrawingLayer is not a Primitive.
 * So why are we extending it?  Because Kelvin Sung suggested we do this,
 * (and he was right to do so) and because there is no time left. <3
 * 
 * One really nasty thing about this class is that it extends Primitives.
 * Primitives, for some reason, have all of their members as public.
 * While users of the Engine do not have access to Primitives, they
 * do now have access to DrawingLayers, which means that any
 * DrawingLayer has all of its members just dangling out there were
 * anyone can mess with them.
 * 
 * Yup, this is scary.  Sadly, there's no time to fix it, as it would
 * involve changes throughout the entire Engine.
 * 
 * @author Branden Drake
 *
 */
public class DrawingLayer extends Primitive
{
	LinkedList<Primitive> primitives = new LinkedList<Primitive>();
  
	
	/**
	 * Constructor.
	 */
	public DrawingLayer()
	{
		this.addToAutoDrawSet();
	}
	
	/**
	 * Constructor that puts this DrawingLayer within another DrawingLayer.
	 * NOT A COPY CONSTRUCTOR.
	 */
	public DrawingLayer(DrawingLayer otherLayer)
	{
		this.moveToDrawingLayer(otherLayer);
	}
	



	  /**
	   * Remove all Primitives in the from the list,
	   * and stop them from drawing and updating.
	   * 
	   * Kind of a dangerous function to call considering
	   * that most of these Primitives are going to be
	   * tied to GameObjects, so use this function prudently.
	   */
	  public void destroy()
	  {
		  for(Primitive currentPrimitive : primitives.toArray(new Primitive[0]))
		  {
			  currentPrimitive.destroy();
		  }
		  
		  primitives.clear();
		  
		  super.destroy();
	  }
	  
	  
	  /**
	   * Removes the Primitive from the list,
	   * and puts it back in the default AutoDraw set
	   * @param prim		The Primitive to remove.
	   * @return			False if the Primitive was not in the list.
	   */
	  public boolean remove(Primitive prim)
	  {
		  if(prim == null)
		  {
			  return false;
		  }
		  
		  if(primitives.remove(prim))
		  {
			  prim.myDrawingLayer = null;
			  prim.addToAutoDrawSet();
			  return true;
		  }
		  else
		  {
			  return false;
		  }
	  }
	
	  
	  /**
	   * Adds a Primitive to the list.
	   * Also removes this Primitive from the default AutoDraw set,
	   * and any other DrawingLayers it is included in.
	   * @param prim	The Primitive to add to the list.
	   */
	  public void add(Primitive prim)
	  {	  
		  if(prim == null)
		  {
			  return;
		  }
		  
		  if(prim.myDrawingLayer != null)
		  {
			  prim.myDrawingLayer.remove(prim);
		  }
		  
		  prim.removeFromAutoDrawSet();
		  
		  prim.myDrawingLayer = this;
		  primitives.add(prim);
	  }
	  
	  
	  /**
	   * Update all Primitives in the list.
	   */
	  public void update()
	  {
		  for(Primitive currentPrimitive : primitives)
		  {
			  currentPrimitive.update();
		  }
	  }
	  
	  
	  /**
	   * Draw all Primitives in the list.
	   */
	  public void draw()
	  {
		  for(Primitive currentPrimitive : primitives)
		  {
			  if(currentPrimitive.visible)
			  {
				  currentPrimitive.draw();
			  }
		  }  
	  }
	  
	  
	  /**
	   * Sets whether to draw or not draw objects within this layer.
	   * Note that this does not change the individual visibility
	   * settings of any objects within the layer.
	   * @param isVisible		True if you want things to be drawn.
	   */
	  public void setVisibilityTo(boolean isVisible)
	  {
		  visible = isVisible;
	  }
  
  
  
  	/**
	 * Moves this DrawingLayer within another DrawingLayer, so that it will
	 * be drawn with all of the other GameObjects and DrawingLayers in that layer.
	 * Will remove this DrawingLayer from the DrawingLayer it
	 * is already in and/or the AutoDraw set.
	 * An argument of null will remove this DrawingLayer from
	 * any DrawingLayer it is in and move it back to the AutoDrawSet.
	 * @param drawingLayer		The layer to move this DrawingLayer to.
	 */
	public void moveToDrawingLayer(DrawingLayer drawingLayer)
	{
		if(drawingLayer != null)
		{
			drawingLayer.add(this);
		}
		else
		{
			myDrawingLayer.remove(this);
		}		
	}
}
