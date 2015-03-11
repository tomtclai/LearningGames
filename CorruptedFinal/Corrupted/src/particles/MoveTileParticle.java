package particles;

import structures.IntVector;
import gridElements.GridElement;
import gridElements.Tile;
import gridElements.TileConnector;
import Engine.Vector2;

public class MoveTileParticle extends Particle {
	Tile parent;
	
	TileConnector[] connectors;
	
	public MoveTileParticle(Tile ge, Vector2 stopLoc)
	{
		super(ge,stopLoc, 10);
		if(ge != null){
			ge.setToInvisible();
			parent = ge;
		}

		
		//import connectors from parent
		connectors = new TileConnector[8];
		{
			for (int i = 0; i < connectors.length; i++)
			{
				if( ge != null && ge.isConnected(i))
				{
					connectors[i] = new TileConnector(parent, i);
					connectors[i].connect();
				}
			}
		}
	}
	
	/**
	 * During the update, if this Move particle is destroyed we makethe gridelement visible again, 
	 * Also, we trigger connector update for this tile and all around it
	 * @author Brian Chau
	 */
	@Override
	public void update()
	{
		super.update();
		
		for(TileConnector tc : connectors)
		{
			if (tc != null){
				tc.setCenter(this.getCenter()) ; 
			} 
		}
		
		if(destroyed  && parent != null)
		{
			parent.setToVisible();
			parent = null;
		}
	}
	
	/**
	 * When drawing, draw the connectors too
	 */
	@Override
	public void draw()
	{
		if(isVisible()){
			super.draw();
			for(TileConnector tc : connectors)
			{
				if (tc != null){
					tc.draw();
				}
			}
		}
	}
	
	
}
