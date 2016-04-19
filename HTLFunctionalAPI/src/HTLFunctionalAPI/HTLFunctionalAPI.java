package HTLFunctionalAPI;
import java.util.Random;
import TowerDefense.*;

/**
 * 
 * @author Kunlakan (Jeen) Cherdchusilp
 * @author Tom Lai
 *
 */

public class HTLFunctionalAPI extends HTL {
	
	public void buildGame(){}
	
	public void updateGame(){}
	
	/*
	 * 
	 */
	public void drawMedicWizard()
	{		
		Random random = new Random();
		int x = random.nextInt((int)SCREEN_WIDTH);
		int y = random.nextInt((int)SCREEN_HEIGHT);
		
		Tile position = grid.getTile(3, 3);
		
		Tower medicWizard = null;
		medicWizard.teleportTo(position);
	}
	
	public void drawSpeedyWizard(){}
}
