package GhostLight.GhostEngine;

import java.util.Vector;

import Engine.BaseCode;
import Engine.Rectangle;

public class Background 
{
	private static final String BACKGROUND_IMG = "background/GhostLight_BG.png";
	private static final String GROUNDHOUSE_IMG = "background/GhostLight_GroundHouse.png";
	private static final String CLOUD1_IMG = "background/Cloud 1.png";
	private static final String CLOUD2_IMG = "background/Cloud 2.png";
	
	Vector<Rectangle> bgItems;
	Vector<Rectangle> animatedItems;
	
	public Background()
	{
		bgItems = new Vector<Rectangle>();
		animatedItems = new Vector<Rectangle>();
		
		addItem(BACKGROUND_IMG,
				BaseCode.world.getWidth(),
				BaseCode.world.getHeight(),
				BaseCode.world.getWidth() / 2,
				BaseCode.world.getHeight() /2 );
		
		addWrappedMovingImage(CLOUD1_IMG,
				40,
				10,
				BaseCode.world.getWidth() + 4 / 2 + 4,
				BaseCode.world.getHeight() /2  + 10,
				-.05f,
				0);
		
		addWrappedMovingImage(CLOUD2_IMG,
				30,
				10,
				BaseCode.world.getWidth() - 14 / 2,
				BaseCode.world.getHeight() /2,
				.07f,
				0);
		
		addItem(GROUNDHOUSE_IMG,
				BaseCode.world.getWidth(),
				BaseCode.world.getHeight(),
				BaseCode.world.getWidth() / 2,
				BaseCode.world.getHeight() /2 );
		

	}
	
	/**
	 * Draw the background and update animation.
	 */
	public void draw()
	{
		for( Rectangle r : bgItems)
		{
			r.draw();
		}
		for( Rectangle r : animatedItems)
		{
			r.draw();
			r.update();
		}
	}
	
	private Rectangle addItem(String fileName, float sizeX, float sizeY, float xLoc, float yLoc)
	{
		Rectangle item = new Rectangle();
		item.size.set(sizeX, sizeY);
		item.setImage(fileName);
		item.center.set(xLoc, yLoc);
		bgItems.add(item);
		item.alwaysOnTop = false;
		BaseCode.resources.removeFromAutoDrawSet(item);
		return item;
	}
	
	private Rectangle addWrappedMovingImage(String fileName,
			float sizeX, float sizeY, float xLoc, float yLoc, float xVel, float yVel)
	{
		WrappedMovingSprite item = new WrappedMovingSprite(fileName,
				sizeX, sizeY, xLoc, yLoc, xVel, yVel);
		animatedItems.add(item);
		item.alwaysOnTop = false;
		BaseCode.resources.removeFromAutoDrawSet(item);
		return item;
	}
	
	private class WrappedMovingSprite extends Rectangle
	{
		
		public WrappedMovingSprite(String fileName,float sizeX, float sizeY,
				float xLoc, float yLoc, float xVel, float yVel )
		{
			size.set(sizeX, sizeY);
			setImage(fileName);
			center.set(xLoc, yLoc);
			velocity.set(xVel, yVel);
		}
		
		public void update()
		{
			super.update();
			//wrap from left to right
			if( center.getX() + (size.getX() / 2) < 0 )
			{
				center.setX(BaseCode.world.getWidth() + (size.getX() / 2));
			}
			// wrap from right to left
			else if( center.getX() - (size.getX() / 2) > BaseCode.world.getWidth() )
			{
				center.setX(0 - (size.getX() / 2));
			}
			//wrap from bottom to top
			else if( center.getY() + (size.getY() / 2) < 0 )
			{
				center.setX(BaseCode.world.getHeight() + (size.getY() / 2));
			}
			// wrap from top to bottom
			else if( center.getY() - (size.getY() / 2) > BaseCode.world.getHeight() )
			{
				center.setX( 0 - (size.getY() / 2));
			}
		}
		
	}
	
}
