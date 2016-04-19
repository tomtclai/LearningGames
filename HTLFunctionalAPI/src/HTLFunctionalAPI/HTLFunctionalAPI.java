package HTLFunctionalAPI;
import java.util.*;
import TowerDefense.*;

/**
 * 
 * @author Kunlakan (Jeen) Cherdchusilp
 * @author Tom Lai
 *
 */

public class HTLFunctionalAPI extends HTL {
	Vector<TowerDefense.Character> characters = new Vector<TowerDefense.Character>();
	public void initializeWorld() {
		super.initializeWorld();
		buildWorld();
	}
	public void buildWorld(){}
	
	public void updateWorld(){
		for (TowerDefense.Character c : characters) {
			c.update();
		}
	}
	
	/*
	 * Draws a medic tower at a random tile
	 */
	public void drawMedicWizard()
	{		

		Random random = new Random();
		int x = random.nextInt((int)SCREEN_WIDTH);
		int y = random.nextInt((int)SCREEN_HEIGHT);
		
		Tile position = grid.getTile(3, 3);
		
		Tower medicWizard = new TowerMedic();
		medicWizard.teleportTo(position);
		characters.add(medicWizard);
	}
	
	/*
	 * Draws a speedy tower at a random tile
	 */
	public void drawSpeedyWizard(){}
}
