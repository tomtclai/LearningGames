package ui;

import java.awt.Color;

import Engine.*;

public class StatusScreen{
	
	private GameObject BackGround;
	public enum  StatusScreenType{ WIN, LOSE}
	
	public StatusScreen(StatusScreenType status){
		String textureString = "WinScreen.png";
		if (status.equals(StatusScreenType.LOSE))
		{
			textureString = "LoseScreen.png";
		}
		
		float ww = BaseCode.world.getWidth();
		float wh = BaseCode.world.getHeight();
		BackGround = new GameObject(BaseCode.world.getPositionX()+ww/2f, BaseCode.world.getWorldPositionY()+wh/2f, // pos
			ww, wh); // size
		BackGround.setToInvisible();
		BackGround.setImage(textureString);
		
	}
	
	public void setVisible(boolean isVisible){
		BackGround.setVisibilityTo(isVisible);
		if (isVisible)
			BackGround.moveToFront();
	}
	
	public void draw()
	{
		if(BackGround.isVisible())
		{
			BackGround.draw();
		}
	}
}
