package corruptedUser;

import java.awt.event.KeyEvent;

import corrupted.*;
import structures.*;
import gridElements.*;
import gridElements.GridElement.ColorEnum;

public class UserCodeOneRowMatchThree extends Game{
	
	private GridElement[] tutorialArray; 
			
	public void initialize()
	{
		tutorialArray = new GridElement[10];
		this.setTileColumn(tutorialArray, 5);
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
		else if(keyboard.isButtonTapped(KeyEvent.VK_RIGHT))
		{
			int playerPos = getPlayerPosition().getY();
			
			tutorialArray[playerPos] = new Tile(getPlayerPosition(), getPlayerColorEnum(), this);
			//TL.putTile(playerPos, getPlayerColor());
			destroy3InARow(playerPos);
			
			setPlayerColorEnum(GridElement.getRandomColorEnum());
			
		}
	}
	public void destroy3InARow(int position)
	{
		ColorEnum currentColor = tutorialArray[position].getColorEnum();
		int matchCount = 1;
		int lowIndex = position;
		int highIndex = position;
		//get lowest contiguous matching index
		while (lowIndex > 0)
		{
			//CASTING IS BAD
			Tile tile = (Tile)tutorialArray[lowIndex-1];
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
			GridElement tile = tutorialArray[highIndex+1];
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
				tutorialArray[i].markForDelete();
			}
		}
	}
}
