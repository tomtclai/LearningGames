package ui;

import java.awt.Color;

import ui.StatusScreen.StatusScreenType;
import Engine.BaseCode;
import Engine.GameObject;
import Engine.Vector2;

public class CreditsScreen {
	private GameObject BackGround;
	private GameObject credits;
	private GameObject ForeGround;
	private float movementIncrement;

	public CreditsScreen(){
		float widthMod = 940f/1920f;
		float heightMod = 4614f/1080f;
		
		float ww = BaseCode.world.getWidth();
		float wh = BaseCode.world.getHeight();
		
		BackGround = new GameObject(BaseCode.world.getPositionX()+ww/2f, BaseCode.world.getWorldPositionY()+wh/2f,  // center
							ww,wh);		// size
		BackGround.setToInvisible();
		BackGround.setColor(Color.black);
		
		credits = new GameObject();
		credits.setSize(BaseCode.world.getWidth()*widthMod,BaseCode.world.getHeight()*heightMod);
		credits.setToInvisible();
		credits.setImage("Credits.png");
		
		ForeGround = new GameObject();
		ForeGround.setCenter(BackGround.getCenter());
		ForeGround.setSize(BackGround.getSize());
		ForeGround.setToInvisible();
		ForeGround.setImage("CreditsBorder.png");
		
				
		movementIncrement = ForeGround.getCenterY() / 90f;
		BackGround.setAutoDrawTo(false);
		credits.setAutoDrawTo(false);
		ForeGround.setAutoDrawTo(false);
		
		reset();		
	}
	
	/**
	 * Moves the credits to the starting position
	 */
	public void reset()
	{
		float x =BaseCode.world.getPositionX()+BaseCode.world.getWidth()/2f;
		float y =BaseCode.world.getWorldPositionY()-credits.getHeight()/2f;
		
		credits.setCenter(x, y);
	}
	
	/**
	 * slowly scrolls the Credits up
	 */
	public void update()
	{
		credits.setCenterY(credits.getCenterY() + movementIncrement);
	}
	
	public void setVisible(boolean isVisible){
		BackGround.setVisibilityTo(isVisible);
		ForeGround.setVisibilityTo(isVisible);		
		credits.setVisibilityTo(isVisible);
		if (isVisible) {
			BackGround.moveToFront();		
			credits.moveToFront();		
			ForeGround.moveToFront();
		}
	}
	
	public void draw()
	{
		if (BackGround.isVisible())
		{
			BackGround.draw();		
			credits.draw();		
			ForeGround.draw();
		}
	}
}
