package particles;
import gridElements.GridElement;

import java.awt.Color;
import java.awt.image.BufferedImage;

import Engine.*;

public abstract class Particle extends GameObject {

	protected Vector2 target;
	protected int duration = 10; //number of frames before the particle should die
	public boolean destroyed = false;
	
	/**
	 * Initializes a new particle using the texture, size and start position of a gridElement
	 * @param ge Grid element to get texture, size and position
	 * @param stopLoc stopping position of teh particle
	 * @param dur duration of the particle
	 */
	public Particle (GridElement ge, Vector2 stopLoc, int dur)
	{
		BufferedImage tex = null;
		Vector2 siz = new Vector2(1,1);
		Vector2 startLoc = new Vector2(0,0);
		
		if (ge != null)
		{
			tex = ge.getImage();
			siz = ge.getSize();
			startLoc = ge.getCenter();
		}
		init(tex, siz, startLoc, stopLoc, dur);
	}
	
	/**
	 * Initializes a new Particle
	 * 
	 * @author Brian Chau
	 * @param tex texture to set to the particle
	 * @param siz size of the particle
	 * @param startLoc starting location of the particle
	 * @param stopLoc stopping location of the particle
	 * @param dur duration of the particle
	 */
//	public Particle(BufferedImage tex, Vector2 siz, Vector2 startLoc, Vector2 stopLoc, int dur)
//	{
//		init(tex, siz, startLoc, stopLoc, dur);
//	}

	/**
	 * this is basically a constructor, but extracted so that it can be reused for multiple constructors
	 * this essentially initializes a new particle with default values put in if things are null or invalid
	 * 
	 * @author Brian Chau
	 * @param tex texture to set to the particle
	 * @param siz size of the particle
	 * @param startLoc starting location of the particle
	 * @param stopLoc stopping location of the particle
	 * @param dur duration of the particle
	 */
	protected void init(BufferedImage tex, Vector2 siz, Vector2 startLoc,
			Vector2 stopLoc, int dur) {
		if (dur <= 0)
			dur = 1;
		
		duration = dur;
		//copy things from GridElement
		if(siz != null)
			setSize(siz);
		if(startLoc != null)
			setCenter(startLoc);
		if(tex != null)
			setImage(tex);
		setAutoDrawTo(false); 
		//calculate velocity
		if(stopLoc == null)
			stopLoc = startLoc;
		float velx = (stopLoc.getX() - getCenterX())/duration;
		float vely = (stopLoc.getY() - getCenterY())/duration;
		setVelocity(velx,vely);
	}
	
	/* @Override
	public void draw()
	{
		super.draw();
	}
	*/
	
	@Override 
	public void update()
	{
		super.update();
		duration--;
		if(duration <= 0)
		{
			destroy();
		}
	}
	
	@Override
	public void destroy()
	{
		destroyed = true;
		super.destroy();
	}
}