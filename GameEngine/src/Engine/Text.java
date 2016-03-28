
package Engine;

import java.awt.Color;
import java.awt.Font;

public class Text extends Primitive
{
  private String text = "UNSET_TEXT";
  private boolean drawInWorldCoords = true;
  private String fontName = "Times New Roman";
  private int fontSize = 24;

  private Color colorBack = Color.WHITE;
  private Color colorFront = Color.BLACK;

  private Font secondaryFontAccess;
  
  public Text()
  {
    alwaysOnTop = true;
  }

  public Text(String value)
  {
    alwaysOnTop = true;

    setText(value);
  }

  public Text(String value, float x, float y)
  {
    center.set(x, y);

    alwaysOnTop = true;

    setText(value);
  }

  public void setBackColor(Color value)
  {
    colorBack = value;
  }

  public void setFrontColor(Color value)
  {
    colorFront = value;
  }

  public void setText(String value)
  {
    text = value;
  }

  public String getText()
  {
    return text;
  }

  public void setFontName(String value)
  {
    fontName = value;
  }
  
  public void setFont(Font font)
  {
	secondaryFontAccess = font;
  }

  public void setFontSize(int value)
  {
    fontSize = value;
  }

  /**
   * Set whether the text should be positioned in world coordinates or pixel
   * coordinates.
   * 
   * @param value
   *          - True to draw in world coordinates, false to draw in pixel
   *          coordinates.
   */
  public void setDrawInWorldCoords(boolean value)
  {
    drawInWorldCoords = value;
  }

  /**
   * Get the width of the text in world coordinates.
   * 
   * @return - Width of the text in world coordinates.
   */
  public float getWidth()
  {
    return BaseCode.resources.getTextWidth(text);
  }

  /**
   * Get the width of the text in pixel coordinates.
   * 
   * @return - Width of the text in pixel coordinates.
   */
  public float getWidthABS()
  {
    return BaseCode.resources.getTextWidth(text);
  }

  public void draw()
  {
	if(secondaryFontAccess != null)
		BaseCode.resources.setFont(secondaryFontAccess, fontSize);
	else
		BaseCode.resources.setFont(fontName, fontSize);

    BaseCode.resources.setTextBackColor(colorBack);
    BaseCode.resources.setTextFrontColor(colorFront);

    if(drawInWorldCoords)
    {
      BaseCode.resources.drawText(text, center.getX(), center.getY(), rotate);
    }
    else
    {
      BaseCode.resources
          .drawTextABS(text, center.getX(), center.getY(), rotate);
    }
  }

  public void update()
  {
  }
  
  
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
	 * Moves this Text to a DrawingLayer, so that it will
	 * be drawn with all of the other Text in that layer.
	 * Will remove this Text from the DrawingLayer it
	 * is already in and/or the AutoDraw set.
	 * An argument of null will remove this Text from
	 * any DrawingLayer it is in and move it back to
	 * the AutoDrawSet.
	 * Note that Text already tries to draw on top of everything,
	 * so this is mainly useful for trying to manage Text associated
	 * with other Text or GameObjects within a DrawingLayer.
	 * @param drawingLayer		The layer to move this Text to.
	 */
	public void moveToDrawingLayer(DrawingLayer drawingLayer)
	{
		if(drawingLayer != null)
		{
			drawingLayer.add(this);

		}
		else
		{
			this.removeFromDrawingLayer();
		}
	}
}
