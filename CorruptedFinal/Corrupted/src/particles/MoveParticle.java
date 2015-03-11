package particles;

import gridElements.GridElement;
import Engine.Vector2;

public class MoveParticle extends Particle {
	GridElement parent;
	public MoveParticle(GridElement ge, Vector2 stopLoc)
	{
		super(ge,stopLoc, 10);
		if(ge != null){
			ge.setToInvisible();
			parent = ge;
			if (getImage() != null){
			this.setSpriteSheet(getImage(), GridElement.PIXEL_SIZE, GridElement.PIXEL_SIZE, 1, -1);
			this.setUsingSpriteSheet(true);
			}
		}
	}
	
	/**
	 * During the update, if this Move particle is destroyed, trigger connector update for this tile and all around it
	 * @author Brian Chau
	 */
	@Override
	public void update()
	{
		super.update();
		if(destroyed  && parent != null)
		{
			parent.setToVisible();
			parent = null;
		}
	}
	
	
}
