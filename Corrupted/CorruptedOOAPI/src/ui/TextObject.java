package ui;

import java.awt.Color;

import Engine.Text;
import Engine.Vector2;

public class TextObject extends Text{
	
	public TextObject(String text, Vector2 position)
	{
		super(text);
		alwaysOnTop = false;
		setSize(18);
		if(position == null)position = new Vector2(0,0);
		setPosition(position);
		setColor(Color.WHITE, Color.BLACK);
	}
	
	
	public TextObject(String text, Vector2 position, int fontSize, Color foreColor, Color backColor)
	{
		this(text, position);
		setSize(fontSize);
		if(foreColor == null) foreColor = Color.WHITE;
		if(backColor == null) backColor = Color.BLACK;
		setColor(foreColor, backColor);
	}
	
	
	public void setPosition(Vector2 position)
	{
		center = position;
	}
	
	public void setColor(Color foreColor, Color backColor)
	{

		setFrontColor(foreColor);
		setBackColor(backColor);
	}
	public void setSize(int fontSize)
	{
		setFontSize(fontSize);
	}
}
