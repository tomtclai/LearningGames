import java.awt.image.BufferedImage;

import Engine.LibraryCode;
import Engine.Rectangle;

public class SpriteDisplayer extends LibraryCode 
{
	Rectangle spriteSheet = null;
	Rectangle sprite = null;
	
	public SpriteDisplayer()
	{

	}
	
	public void initializeWorld() 
	{
		super.initializeWorld();
		spriteSheet = new Rectangle();
		spriteSheet.size.set(40, 40);
		spriteSheet.center.set(80, 40);
		
		sprite = new Rectangle();
		sprite.size.set(20,20);
		sprite.center.set(10, 10);
	}
	
	public void loadSpriteSheet(BufferedImage image,
			int width, int height, int frames, int ticks)
	{
		spriteSheet.texture = image;
		try
		{
			sprite.setSpriteSheet(image, width, height, frames, ticks);
			sprite.setUsingSpriteSheet(true);
		}
		catch(Exception e) { } // Invalid data, do nothing.
	}
	
	public void updateSprite(int width, int height, int frames, int ticks)
	{
		try
		{
			sprite.setSpriteSheet(sprite.texture, width, height, frames, ticks);
		}
		catch(Exception e) { } // Invalid data, do nothing.
	}
	public int getRows()
	{
		if(sprite == null) 
			return -1;
		return sprite.rowsInSpriteSheet;
	}
	public int getColumns()
	{
		if(sprite == null)
			return -1;
		return sprite.colsInSpriteSheet;
	}
	
	public void updateWorld()
	{
		super.updateWorld();
	}

}
