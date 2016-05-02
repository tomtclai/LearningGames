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

	}
	
	public void updateWorld()
	{
		super.updateWorld();
		
	}
}
