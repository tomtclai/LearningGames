import java.awt.event.KeyEvent;

import TowerDefense.HTL;


/**
 * This game lets you control whether the cute animal moves by holding SPACE BAR
 * 
 * If you imagine obstacles, you can time your movement and avoid them!
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class UserGame03 extends HTL
{	
	public UserGame03()
	{
		super();
	}
	
	public void initializeWorld()
	{
		super.initializeWorld();
		
		createDefaultPath();
		
		spawner.addWave(1, 1, "b");
		spawner.startWaves();	
	}
	
	public void updateWorld()
	{
		super.updateWorld();
		
		spawner.update();
		
		if(keyboard.isButtonDown(KeyEvent.VK_SPACE))
		{
			walkerSet.update();
		}
	}
}
