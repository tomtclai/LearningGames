package layers;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import particles.Particle;
import Engine.GameObject;

public class ParticleList extends GameObject {

	ArrayList<Particle> particleList;
	
	public ParticleList()
	{
		particleList = new ArrayList<Particle>();
	}
	
	@Override
	public void draw() {
		for(int i = 0; i < particleList.size(); i++)
		{
			particleList.get(i).draw();
		}
	}
	
	@Override
	public void update()
	{
		//update in reverse order so we can remove if we have to
		//if we do it in normal order, the indexes will be messed up when an object is removed
		for (int i = particleList.size()-1; i >= 0; i--)
		{
			Particle currentParticle = particleList.get(i);
			currentParticle.update();
			if(currentParticle.destroyed)
			{
				currentParticle.setImage((BufferedImage)null);
				particleList.remove(i);
			}
		}
	}
	
	public void add(Particle p)
	{
		if (p == null) return;
		p.setAutoDrawTo(false);
		particleList.add(p);
	}
}
