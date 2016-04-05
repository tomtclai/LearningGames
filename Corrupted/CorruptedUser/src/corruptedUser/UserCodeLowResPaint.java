package corruptedUser;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import structures.IntVector;
import Engine.Vector2;
import gridElements.GridElement;
import gridElements.GridElement.ColorEnum;
import gridElements.Tile;
import corrupted.Game;

public class UserCodeLowResPaint extends Game{
	
	GridElement[][] myTileGrid;
	public void initialize()
	{
		myTileGrid = new GridElement[20][10];
		setTileGrid(myTileGrid);

	}
	
	@Override
	public void update()
	{
		super.update();
		if(mouse.isButtonDown(MouseEvent.BUTTON1)){
			//create a tile on position
			int x = getMouseCenter().getX();
			int y = getMouseCenter().getY();
			myTileGrid[x][y] = new Tile(this.getPlayerColorEnum(), this);
			
		}
		if(mouse.isButtonDown(MouseEvent.BUTTON3)){
			//kill all horizontal tiles
			int x = getMouseCenter().getX();
			int y = getMouseCenter().getY();
			if(myTileGrid[x][y] != null)
			{
				myTileGrid[x][y].markForDelete();;
			}
		}
		if(keyboard.isButtonTapped(KeyEvent.VK_SPACE))
		{
			myTileGrid = new GridElement[20][10];
			setTileGrid(myTileGrid);
		}
		
		if(keyboard.isButtonTapped(KeyEvent.VK_1))
		{
			this.setPlayerColorEnum(ColorEnum.RED);
		}		
		if(keyboard.isButtonTapped(KeyEvent.VK_2))
		{
			this.setPlayerColorEnum(ColorEnum.YELLOW);
		}		
		if(keyboard.isButtonTapped(KeyEvent.VK_3))
		{
			this.setPlayerColorEnum(ColorEnum.GREEN);
		}		
		if(keyboard.isButtonTapped(KeyEvent.VK_4))
		{
			this.setPlayerColorEnum(ColorEnum.CYAN);
		}		
		if(keyboard.isButtonTapped(KeyEvent.VK_5))
		{
			this.setPlayerColorEnum(ColorEnum.BLUE);
		}		
		if(keyboard.isButtonTapped(KeyEvent.VK_6))
		{
			this.setPlayerColorEnum(ColorEnum.MAGENTA);
		}
	}
}
