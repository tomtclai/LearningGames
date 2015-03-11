package particles;

import gridElements.GridElement;
import gridElements.GridElement.ColorEnum;
import Engine.BaseCode;
import Engine.Vector2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Random;

import structures.ImageManip;

public class DestroyedParticle extends Particle {


	public DestroyedParticle(GridElement ge, Vector2 stopLoc)
	{
		super(ge, stopLoc, 10);
		if(getImage() != null){
			this.setSpriteSheet(getImage(), GridElement.PIXEL_SIZE, GridElement.PIXEL_SIZE, 1, 1);
			this.setUsingSpriteSheet(true);
		}
	}
	
	public void update()
	{
		this.setSize(getWidth()+.1f, getHeight()+.1f);
		super.update();
	}
}
