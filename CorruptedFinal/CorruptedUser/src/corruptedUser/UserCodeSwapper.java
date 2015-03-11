package corruptedUser;

import gridElements.GridElement;
import gridElements.Tile;
import gridElements.GridElement.ColorEnum;

import java.awt.event.KeyEvent;

import corrupted.Game;
import structures.IntVector;

public class UserCodeSwapper extends Game{
	int readyToSwap = -1;
	static final int mainPosition = 10;
	static final int tempPosition = 11;
	GridElement[] mainLine, tempLine;
	
	public void initialize()
	{
		super.initialize();
		init();

	}
	
	public void init()
	{
		GridElement.ColorEnum[] colorArray = new GridElement.ColorEnum[] {GridElement.ColorEnum.RED, GridElement.ColorEnum.GREEN, GridElement.ColorEnum.BLUE};
		mainLine = new GridElement[10];
		for (int i = 0; i < 10; i++)
		{
			mainLine[i] = new Tile(colorArray[i%colorArray.length], this);
		}
		this.setTileColumn(mainLine, mainPosition);
		tempLine = new GridElement[10];
		this.setTileColumn(tempLine, tempPosition);
		readyToSwap = -1;

	}
	
	@Override
	public void update()
	{
		super.update();
		if(keyboard.isButtonTapped(KeyEvent.VK_DOWN))
		{
			movePlayerDown();
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_UP))
		{
			movePlayerUp();
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_LEFT))
		{
			init();
		}
		else if(keyboard.isButtonTapped(KeyEvent.VK_RIGHT))
		{
			//using the player position, shift an existing tile up,
			IntVector playerPos = getPlayerPosition();
			int y = playerPos.getY();

			int swapY  = readyToSwap;
			if(mainLine[y] != null)
			{
				//if nothing is ready to swap, prep one.
				if(readyToSwap == -1)
				{
					tempLine[y] = mainLine[y];
					mainLine[y] = null;
					//alternatively we can use: 
					//TL.moveElement(true, playerPos, new IntVector(x,1));
					
					readyToSwap = y;
				}
				//if somethign is ready to swap, swap
				else
				{
					//move current Tile to tempTile's orig location
					
					mainLine[swapY] = mainLine[y];
					mainLine[y] = tempLine[swapY];
					tempLine[swapY] = null;
					
					//alternatively we can use:
					//TL.moveElement(true, playerPos, new IntVector(swapx,0));
					//TL.moveElement(true, readyToSwap, playerPos);
					
					readyToSwap = -1;	//clear the swap holder
					this.destroy3InARow(y);
					this.destroy3InARow(swapY);
				}
			}
			//if nothing is here but we have something ready to swap, move it.
			else if (readyToSwap != -1)
			{
				mainLine[y] = tempLine[swapY];//swapper location y is always 1
				tempLine[swapY] = null;
				//alternatively we can use:
				//TL.moveElement(true, readyToSwap, playerPos);
				
				readyToSwap = -1;
				this.destroy3InARow(y);
				this.destroy3InARow(swapY);
			}
		}
	}
	public void destroy3InARow(int row)
	{
		//return if this location has nothing
		if(mainLine[row] == null) return;
		
		ColorEnum currentColor = mainLine[row].getColorEnum();
		int lowIndex = row;
		int highIndex = row;
		//get lowest contiguous matching index
		while (lowIndex > 0)
		{
			//CASTING IS BAD
			GridElement tile = mainLine[lowIndex-1];
			if(tile == null)
				break;
			if (tile.getColorEnum().equals(currentColor))
				lowIndex--;
			else
				break;
		}
		//get highest contiguous matching index
		while (highIndex < getHeight()-1)
		{
			//CASTING IS BAD
			GridElement tile = mainLine[highIndex+1];
			if(tile == null)
				break;
			if (tile.getColorEnum().equals(currentColor))
				highIndex++;
			else
				break;
		}
		//if the distance between indexes is greater or equal to 2, we have a match of 3
		if(highIndex - lowIndex >= 2)
		{
			//delete matching tiles
			for(int i = lowIndex; i <= highIndex; i++)
			{
				tileHelper.markForDelete(mainPosition, i);
				//mainLine[i].markForDelete();
			}
		}
	}
}
