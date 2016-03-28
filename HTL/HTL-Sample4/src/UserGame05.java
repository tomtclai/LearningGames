
import Engine.Vector2;
import TowerDefense.HTL;
import TowerDefense.Tile;
import TowerDefense.Tower;
import TowerDefense.TowerMedic;
import TowerDefense.Walker;
import TowerDefense.WalkerBasic;


/**
 * This little "game" shows how to have a Tower that heals a Walker
 * 
 * @author Branden Drake
 * @author Rachel Horton
 */
public class UserGame05 extends HTL
{	
	private Tower wizard = null;
	private Walker cutiepie = null;
	
	
	public UserGame05()
	{
		super();
	}
	
	
	public void initializeWorld()
	{
		super.initializeWorld();
		
		TowerMedic.preloadResources();
		WalkerBasic.preloadResources();
		
		createDefaultPath();
		
		wizard = new TowerMedic();
		wizard.setTile(grid.getTile(5, 5));
		
		cutiepie = new WalkerBasic(grid.getPath());
		cutiepie.setDamageTakenPerSecond(5);
	}

	
	
	
	public void updateWorld()
	{
		super.updateWorld();
		
		updateShoot();
		
		cutiepie.update();
		wizard.update();
	}
	
	
	
	private void updateShoot()
	{		
		// if this Tower is of the tower type that is currently ready to fire...
		if(wizard.cooldownIsReady())
		{							
			if(cutiepie.isDead()) 
			{
				return;
			}

			if(walkerIsInTowerSpellcastArea(cutiepie, wizard))
			{		
				wizard.playEffectSpellcast();
				wizard.playSoundSpellcast();				
				
				cutiepie.addHealth(10);
				
				wizard.cooldownStart(1);
			}					
		}			
	}
	
	
	/**
	 * If the Walker is within spell range of the Tower, return true.
	 * Current range is a 3x3 Tile grid with the Tower in the center.
	 * @param tower		The Tower.
	 * @param walker	The Walker.
	 * @return			True if the Walker is in range.
	 */
	private boolean walkerIsInTowerSpellcastArea(Walker walker, Tower tower)
	{
		Tile towerTile = tower.getTile();
		if(towerTile == null)
		{
			return false;
		}
		
		int mapColumnOfTower = towerTile.getGridColumn();
		int mapRowOfTower = towerTile.getGridRow();		
		Vector2 walkerPos = walker.getCenter();

		// search surrounding tiles in 3x3 for walker
		for(int row = mapRowOfTower-1; row <= mapRowOfTower+1; row++)
		{
			for(int column = mapColumnOfTower-1; column <= mapColumnOfTower+1; column++)
			{
				Tile testTile = grid.getTile(column, row);
				if(testTile != null && testTile.containsPoint(walkerPos))
				{								
					return true;
				}
			}
		}
		
		return false;
	}
}
