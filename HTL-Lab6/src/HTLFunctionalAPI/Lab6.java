package HTLFunctionalAPI;
import TowerDefense.*;

/**
 * @author Tom Lai
 * @author Jeen Cherdchusilp
 */
public class Lab6 extends HTLFunctionalAPI
{
	
	public Lab6()
	{
		super();
	}
	
	public void initializeWorld()
	{
		super.initializeWorld();
		
		TowerMedic.preloadResources();
		WalkerBasic.preloadResources();
		
		createPlaygroundPath();
//		
//		wizard = new TowerMedic();
//		wizard.setTile(grid.getTile(5, 5));
//		
//		cutiepie = new WalkerBasic(grid.getPath());
//		cutiepie.setDamageTakenPerSecond(5);
	}
	
	public void updateWorld()
	{
		super.updateWorld();
		
	}
}
