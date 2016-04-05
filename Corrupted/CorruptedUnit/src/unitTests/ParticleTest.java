package unitTests;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;

import gridElements.*;
import gridElements.GridElement.ColorEnum;

import org.junit.Test;

import Engine.Vector2;
import particles.*;
import corrupted.Game;
import runner.RunTests;
import structures.IntVector;

public class ParticleTest {

	@Test
	public void destroyParticleDoubleTest() {
		Game runee = RunTests.ct.getRunnee();
		Corruption corr = new Corruption(new IntVector(0,0), runee);
		DestroyedParticle dp = new DestroyedParticle(corr, new Vector2(1,1));
		DestroyedParticle dp2 = new DestroyedParticle(corr, new Vector2(1,1));
		
		for(int i = 0; i < 11; i++)
		{
			//just dont crash
			dp.update();
			dp2.update();
		}
	}
	@Test
	public void  moveParticleDoubleTest() {
		Game runee = RunTests.ct.getRunnee();
		Corruption corr = new Corruption(new IntVector(0,0), runee);
		MoveParticle mp = new MoveParticle(corr, new Vector2(1,1));
		MoveParticle mp2 = new MoveParticle(corr, new Vector2(1,1));
		assertFalse(corr.isVisible());
		for(int i = 0; i < 11; i++)
		{
			//just dont crash
			mp.update();
			mp2.update();
		}
		assertTrue(corr.isVisible());
	}

	@Test
	public void destroyTileParticleDoubleTest() {
		Game runee = RunTests.ct.getRunnee();
		Tile tile = new Tile(new IntVector(0,0), ColorEnum.RED, runee);
		DestroyedParticle dp = new DestroyedParticle(tile, new Vector2(1,1));
		DestroyedParticle dp2 = new DestroyedParticle(tile, new Vector2(1,1));
		
		for(int i = 0; i < 11; i++)
		{
			//just dont crash
			dp.update();
			dp2.update();
		}
	}
	@Test
	public void  moveTileParticleDoubleTest() {
		Game runee = RunTests.ct.getRunnee();
		Tile tile = new Tile(new IntVector(0,0), ColorEnum.RED, runee);
		MoveParticle mp = new MoveParticle(tile, new Vector2(1,1));
		MoveParticle mp2 = new MoveParticle(tile, new Vector2(1,1));
		assertFalse(tile.isVisible());
		for(int i = 0; i < 11; i++)
		{
			//just dont crash
			mp.update();
			mp2.update();
		}
		assertTrue(tile.isVisible());
	}
	
	@Test
	public void destroyParticleNullTest() {
		DestroyedParticle dp = new DestroyedParticle(null, new Vector2(1,1));
		DestroyedParticle dp2 = new DestroyedParticle(null, new Vector2(1,1));
		
		for(int i = 0; i < 11; i++)
		{
			//just dont crash
			dp.update();
			dp2.update();
		}
	}
	@Test
	public void  moveParticleNullTest() {
		MoveParticle mp = new MoveParticle(null, new Vector2(1,1));
		MoveParticle mp2 = new MoveParticle(null, new Vector2(1,1));
		for(int i = 0; i < 11; i++)
		{
			//just dont crash
			mp.update();
			mp2.update();
		}
	}

	@Test
	public void destroyTileParticleNullTest() {
		DestroyedParticle dp = new DestroyedParticle(null, new Vector2(1,1));
		DestroyedParticle dp2 = new DestroyedParticle(null, new Vector2(1,1));
		
		for(int i = 0; i < 11; i++)
		{
			//just dont crash
			dp.update();
			dp2.update();
		}
	}
	@Test
	public void  moveTileParticleNullTest() {
		MoveParticle mp = new MoveParticle(null, new Vector2(1,1));
		MoveParticle mp2 = new MoveParticle(null, new Vector2(1,1));
		for(int i = 0; i < 11; i++)
		{
			//just dont crash
			mp.update();
			mp2.update();
		}
	}

}
