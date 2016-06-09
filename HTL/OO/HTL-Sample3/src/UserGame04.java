import java.awt.event.KeyEvent;

import TowerDefense.HTL;
import TowerDefense.Path;


/**
 * This little "game" showcases how you can make your own path and map using the API.
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class UserGame04 extends HTL
{	
	public UserGame04()
	{
		super();
	}
	
	public void initializeWorld()
	{
		super.initializeWorld();
		
		this.setHUDVisibilityTo(false);
		createSpecialMap();
		
		spawner.addWave(1, 1, "b");
		spawner.startWaves();	
	}
	
	public void createSpecialMap()
	{
		
		/*
			OXXXXXXXXX
			OXXXXXXXXX
			OOOOOXXXXX
			XXXXOXXXXX
			OOOOOXXXXX	 
		 */
		
		// 1. Set grid size
		grid.setDimensions(10, 5);
		
		// 2. Set path tile placement
		grid.addPathDownLeft(0, 4);
		grid.addPathUpDown(0, 3);
		grid.addPathUpRight(0, 2);
		grid.addPathLeftRight(1, 2);
		grid.addPathLeftRight(2, 2);
		grid.addPathLeftRight(3, 2);
		grid.addPathDownLeft(4, 2);
		grid.addPathUpDown(4, 1);
		grid.addPathUpLeft(4, 0);
		grid.addPathLeftRight(3, 0);
		grid.addPathLeftRight(2, 0);
		grid.addPathLeftRight(1, 0);
		grid.addPathLeftRight(0, 0);
		
		grid.setPathTileVisibilityTo(true);
		super.updateWorld();
		
		// 3. Construct the path.
		boolean success = grid.constructPath(0, 4, 0, 0);
		Path thePath = grid.getPath();
		
		// 4. Pass the path to the spawner.
		spawner.setPath(thePath);
	}
	
	
	
	public void updateWorld()
	{
		super.updateWorld();
		
		spawner.update();
		
		walkerSet.update();
	}
}
