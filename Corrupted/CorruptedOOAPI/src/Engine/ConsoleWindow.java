package Engine;

import java.awt.Color;
import java.util.ArrayList;


/**
 * A console window that can be repositioned and can have text printed to it.
 * It is a singleton.
 * @author Brian Chau
 *
 */
public class ConsoleWindow extends Rectangle{
	
	private static ConsoleWindow instance = new ConsoleWindow();
	
	private ArrayList<Text> TOList;
	
	private ConsoleWindow()
	{
		super();
		//this.center = center;
		//this.size = size;
		this.color = color.black;
		removeFromAutoDrawSet();
		TOList = new ArrayList<Text>();
		
	}
		
	/**
	 * Sets the width of the window (any text that is longer than the width will extend out of the window)
	 * @param width
	 */
	public static void setWidth(float width)
	{
		//since position is measured from the upper left instead of center, 
		//we need to reposition it after resizing it
		Vector2 tempPosition = instance.getPosition();
		instance.size.setX(width);
		instance.setPosition(tempPosition);
	}
	
	/**
	 * Gets the width of the window.
	 * @return The width of the window.
	 */
	public static float getWidth()
	{
		return instance.size.getX();
	}
	
	/**
	 * Sets the height of the window (if the number of lines on the console would cause 
	 * any of the lines to be drawn off screen, older text lines will be omitted)
	 * @param height
	 */
	public static void setHeight(float height)
	{
		//since position is measured from the upper left instead of center, 
		//we need to reposition it after resizing it
		Vector2 tempPosition = getPosition();
		instance.size.setY(height);
		setPosition(tempPosition);
	}
	
	/**
	 * Gets the height of the window.
	 * @return The height of the window.
	 */
	public float getHeight()
	{
		return this.size.getY();
	}
	
	/**
	 * Gets the upper left corner position of the window.
	 * @return Vector 2 containing coordinates to the upper left corner of the window.
	 */
	public static Vector2 getPosition()
	{
		return new Vector2(instance.center.getX()-instance.size.getX()/2, instance.center.getY()+instance.size.getY()/2);
	}
	
	/**
	 * Moves the upper left corner of the console window to a given position
	 * @param position position to move the console
	 */
	public static void setPosition(Vector2 position)
	{
		if (position == null) position = new Vector2(0,0);
		position.setX(position.getX() + instance.size.getX()*.5f);
		position.setY(position.getY() + instance.size.getY()*-.5f);
		instance.center = position;

		instance.updateTextPosition();
	}
	
	/**
	 * Sets the position and size of the ConsoleWindow.
	 * @param position Vector2 for the upper left corner of the window.
	 * @param theSize Vector2 containing the width and height of the window.
	 */
	public static void setConsoleLocation(Vector2 position, Vector2 theSize)
	{
		if (position == null) position = new Vector2(0,0);
		if (theSize == null) theSize = new Vector2(1,1);
		
		instance.size = theSize;
		setPosition(position);
	}
	
	/**
	 * Sets the background texture for the window.
	 * @param filename Filename of the background texture.
	 */
	public static void setBackgroundTexture(String filename)
	{
		if(filename == null) filename = "";
		instance. setImage(filename);
	}
	
	/**
	 * Writes text to the console window and  adds a new line.
	 * @param value text to write. the latest line will always appear on the bottom.
	 */
	public static void write(String value)
	{
		if (value == null) value = "";
		if (instance.TOList.isEmpty()){
			addBlankLine();
		}
		instance.TOList.get(0).setText(instance.TOList.get(0).getText() + value);
		
		instance.updateTextPosition();
	}
	
	/**
	 * Writes text to the console window and  adds a new line.
	 * @param value text to write. the latest line will always appear on the bottom.
	 */
	public static void writeLine(String value)
	{
		write(value);
		addBlankLine();
		
		instance.updateTextPosition();
	}
	
	private static void addBlankLine()
	{
		Text newTO = new Text("");
		newTO.setFontSize(18);
		newTO.setFrontColor(Color.WHITE);
		newTO.setBackColor(Color.BLACK);
		newTO.removeFromAutoDrawSet();
		instance.TOList.add(0,newTO);
	}
	
	/**
	 * Deletes all lines from the console.
	 */
	public static void clear()
	{
		instance.TOList.clear();
	}
	
	/**
	 * Brings the console window to the front of the screen (draw order)
	 */
	public static void bringToTop()
	{
		instance.removeFromAutoDrawSet();
		instance.addToAutoDrawSet();
	}
	
	/**
	 * sets the visibility of the console window
	 * @param visibility
	 */
	public static void setShowConsole(boolean visibility)
	{
		instance.visible = visibility;
	}
	
	/**
	 * toggles the visibility of the console window
	 * @param visibility
	 */
	public static void toggleShowConsole()
	{
		instance.visible = !instance.visible;
	}
	
	@Override
	/**
	 * draws the console to screen
	 */
	public void draw() {
		if(!this.visible)
			return;
		
		super.draw();
		if(TOList != null)
		{
			int max = TOList.size();
			for(int i = 0; i < max; i++)
			{
				float textLevel =TOList.get(i).center.getY();
				float maxHeight = this.center.getY() + this.size.getY()/2f;
				if(textLevel < maxHeight)
				{
					TOList.get(i).draw();
				}
			}
		}
	}
	
	public static void forceDraw()
	{
		instance.draw();
	}
	
	private void updateTextPosition() {
		Vector2 temp = new Vector2(center.getX()-size.getX()*.45f, center.getY() - (size.getY()/2));
		for(int i = 0; i < TOList.size(); i++)
		{
			//recalculate position for each line
			Vector2 lineNumber = BaseCode.world.scaleVectorScreenToWorld(new Vector2(0, 20f * (i + 1f)));
			
			TOList.get(i).center = lineNumber.add(temp);
		}
	}
}
