import java.awt.event.KeyEvent;

import TowerDefense.HTL;
import TowerDefense.Tile;
import TowerDefense.Tower;
import TowerDefense.TowerMedic;


/**
 * This game is the timeless classic of moving Bob around on the screen.
 * 
 * Can you stay on the path?  It really doesn't matter, but why not try?
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class UserGame02 extends HTL
{
	private Tower bob = null;
	
	public UserGame02()
	{
		super();
	}
	
	public void initializeWorld()
	{
		super.initializeWorld();
		
		Tile theTile = grid.getTile(3, 3);
		
		bob = new TowerMedic();
		bob.teleportTo(theTile);		
	}
	
	public void updateWorld()
	{
		super.updateWorld();
		
		bob.update();
		
		if(!bob.isMoving())
		{
			Tile newTile = null;
			
			if(keyboard.isButtonTapped(KeyEvent.VK_UP))
			{
				newTile = bob.getTile().getUp();
			}
			else if(keyboard.isButtonTapped(KeyEvent.VK_DOWN))
			{
				newTile = bob.getTile().getDown();
			}
			else if(keyboard.isButtonTapped(KeyEvent.VK_LEFT))
			{
				newTile = bob.getTile().getLeft();
			}
			else if(keyboard.isButtonTapped(KeyEvent.VK_RIGHT))
			{
				newTile = bob.getTile().getRight();
			}
			
			if(newTile != null)
			{
				bob.teleportTo(newTile);
			}
			
		}
	}
}
