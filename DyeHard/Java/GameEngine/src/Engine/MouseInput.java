
package Engine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInput extends ButtonsInput implements MouseListener,
    MouseMotionListener
{
  private Vector2 mousePos = new Vector2();
  
  private boolean mouseOnScreen = false;
  private World world = null;

  //
  //
  //

  /**
   * Set the world that will be used to convert between pixel space and world
   * space.
   * 
   * @param aWorld
   *          - The world to use.
   */
  public void setWorld(World aWorld)
  {
    world = aWorld;
  }

  /**
   * Get the world x coordinate of the mouse.
   * 
   * @return The x world coordinate of the mouse.
   */
  public float getWorldX()
  {
    if(world != null)
    {
      return world.screenToWorldX(mousePos.getX());
    }

    return 0.0f;
  }

  /**
   * Get the world y coordinate of the mouse.
   * 
   * @return The y world coordinate of the mouse.
   */
  public float getWorldY()
  {
    if(world != null)
    {
      return world.screenToWorldY(mousePos.getY());
    }

    return 0.0f;
  }

  /**
   * Get the pixel x coordinate of the mouse.
   * 
   * @return The x pixel coordinate of the mouse.
   */
  public float getPixelX()
  {
    return mousePos.getX();
  }

  /**
   * Get the pixel y coordinate of the mouse.
   * 
   * @return The y pixel coordinate of the mouse.
   */
  public float getPixelY()
  {
    return mousePos.getY();
  }
  public boolean MouseOnScreen(){
	  return mouseOnScreen;
  }

  //
  // Listener methods
  //

  public void mouseClicked(MouseEvent e)
 {
  }

  public void mouseEntered(MouseEvent e){
	  mouseOnScreen = true;
  }

  public void mouseExited(MouseEvent e){
	  mouseOnScreen = false;
  }

  public void mousePressed(MouseEvent e)
  {
    int button = e.getButton();

    pressButton(button);
  }

  public void mouseReleased(MouseEvent e)
  {
    int button = e.getButton();

    releaseButton(button);
  }

  public void mouseDragged(MouseEvent e)
  {
    mousePos.setX(e.getX());
    mousePos.setY(e.getY());
  }

  public void mouseMoved(MouseEvent e)
  {
    mousePos.setX(e.getX());
    mousePos.setY(e.getY());
  }
}
