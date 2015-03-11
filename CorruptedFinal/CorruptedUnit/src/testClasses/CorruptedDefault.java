package testClasses;

import gridElements.*;
import gridElements.GridElement.ColorEnum;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import Engine.GameWindow;
import corrupted.Game;
import corruptedUser.UserCodeCorruptedFull;


public class CorruptedDefault extends GameWindow {
	private UserCodeCorruptedFull mRunnee;
	
	public Game getRunnee()
	{
		return mRunnee;
	}
	
	public CorruptedDefault()
	{
		mRunnee = new UserCodeCorruptedFull();
		setRunner(mRunnee);
		startProgram();
	}
	
		public class CorruptedDefaultRunnee extends Game{
		GridElement[][] myTileArray;
		
		@Override
		public void initialize()
		{
		}
	
	
		@Override
		public void update()
		{
		}
	}
}
