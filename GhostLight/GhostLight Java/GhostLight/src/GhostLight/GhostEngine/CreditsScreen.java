package GhostLight.GhostEngine;

import java.awt.event.KeyEvent;

import Engine.BaseCode;
import Engine.KeyboardInput;
import Engine.Rectangle;
import Engine.Vector2;
import GhostLight.GhostEngine.GhostLight.updateState;

public class CreditsScreen 
{
  private KeyboardInput keyboard;
  private boolean creditsDone = false;
  
  private float yVelocity = .2f; 
  
  private Rectangle creditsBG;
  private Rectangle creditsScroll;
  
  private Rectangle catDance;
  private Rectangle spiderDance;
  private Rectangle draculaDance;
  private Rectangle mummyDance;
  private Rectangle batDance;
  
  private Vector2 defaultCatPos;
  private Vector2 defaultSpiderPos;
  private Vector2 defaultDraculaPos;
  private Vector2 defaultMummyPos;
  private Vector2 defaultBatPos;
  
  CreditsScreen(KeyboardInput keys)
  {
	  keyboard = keys;
	  
	  float screenWidth = BaseCode.world.getWidth();
	  float screenHeight = BaseCode.world.getHeight();

	  creditsBG = new Rectangle();
	  creditsBG.setImage("credits/CreditsBackground.png");
	  creditsBG.size.set(screenWidth, screenHeight);
	  creditsBG.center.set(screenWidth / 2, screenHeight /2);
	  creditsBG.visible = false;
	  
	  creditsScroll = new Rectangle();
	  creditsScroll.setImage("credits/Ghostlight_CreditsScroll.png");
	  creditsScroll.size.set(screenWidth / 2, screenHeight * 2);
	  creditsScroll.center.set(screenWidth / 2, screenHeight /2);
	  creditsScroll.visible = false;
	  
	  
	  // Characters
	  
	  Vector2 charSize = new Vector2(7f,10f);
	  defaultCatPos = new Vector2(screenWidth /2, creditsScroll.size.getY() * -1.2f);
	  defaultMummyPos = new Vector2(screenWidth * .7f, creditsScroll.size.getY() * -.25f);
	  defaultDraculaPos = new Vector2(screenWidth *.7f, creditsScroll.size.getY() * -.65f);
	  defaultSpiderPos = new Vector2(screenWidth *.3f, creditsScroll.size.getY() * -.5f);
	  defaultBatPos = new Vector2(screenWidth *.66f, creditsScroll.size.getY() * -.61f);
	  
	  catDance = new Rectangle();
	  catDance.setSpriteSheet("credits/catDance2.png", 207, 266,
			  48, 1);
	  catDance.setUsingSpriteSheet(true);
	  catDance.center.set(screenWidth /2, screenHeight / 2);
	  catDance.size.set(charSize);
	  catDance.visible = false;
	  
	  mummyDance = new Rectangle();
	  mummyDance.setSpriteSheet("credits/mummyDance.png", 216, 246, 24, 2);
	  mummyDance.setUsingSpriteSheet(true);
	  mummyDance.center.set(screenWidth * .6f, creditsScroll.size.getY() * -.7f);
	  mummyDance.size.set(charSize);
	  mummyDance.visible = false;
	  
	  draculaDance = new Rectangle();
	  draculaDance.setSpriteSheet("credits/draculaDance.png", 202, 240, 48, 1);
	  draculaDance.setUsingSpriteSheet(true);
	  draculaDance.center.set(screenWidth * .6f, creditsScroll.size.getY() * -.7f);
	  draculaDance.size.set(charSize);
	  draculaDance.visible = false;
	  
	  spiderDance = new Rectangle();
	  spiderDance.setSpriteSheet("credits/spiderDance.png", 386, 262, 24, 2);
	  spiderDance.setUsingSpriteSheet(true);
	  spiderDance.center.set(screenWidth * .6f, creditsScroll.size.getY() * -.7f);
	  spiderDance.size.set(charSize.getX()  * 1.3f, charSize.getY());
	  spiderDance.visible = false;
	  
	  batDance = new Rectangle();
	  batDance.setSpriteSheet("credits/batDance2.png", 418, 228, 24, 2);
	  batDance.setUsingSpriteSheet(true);
	  batDance.center.set(screenWidth * .6f, creditsScroll.size.getY() * -.7f);
	  batDance.size.set(charSize.mult(.7f));
	  batDance.visible = false;
	  
  }

public void startCredits() 
{
	  creditsDone = false;
	
	  BaseCode.resources.moveToBackOfDrawSet(creditsBG);	
	  setVisible(true);

	  
	  
	  creditsScroll.center.set(BaseCode.world.getWidth() /2,
			   (creditsScroll.size.getY() * -.6f));
	  
	  catDance.center.set(defaultCatPos);
	  mummyDance.center.set(defaultMummyPos);
	  batDance.center.set(defaultBatPos);
	  draculaDance.center.set(defaultDraculaPos);
	  spiderDance.center.set(defaultSpiderPos);
	  
	  creditsScroll.velocity.set(0, yVelocity);
	  catDance.velocity.set(0, yVelocity);
	  mummyDance.velocity.set(0, yVelocity);
	  batDance.velocity.set(0, yVelocity);
	  draculaDance.velocity.set(0, yVelocity);
	  spiderDance.velocity.set(0, yVelocity);
	
}
  
public void update()
{
	if(keyboard.isButtonDown(KeyEvent.VK_ESCAPE) ||
			creditsScroll.center.getY() >
			(creditsScroll.size.getY() * .75f) + BaseCode.world.getHeight())
	{
		creditsDone = true;
		setVisible(false);
	}
	creditsScroll.update();
	catDance.update();
	mummyDance.update();
	batDance.update();
	draculaDance.update();
	spiderDance.update();
}

public boolean isCreditsDone()
{
	return creditsDone;
}

private void setVisible(boolean isVisible)
{
	creditsScroll.visible = isVisible;
	creditsBG.visible = isVisible;
	catDance.visible = isVisible;
	mummyDance.visible = isVisible;
	batDance.visible = isVisible;
	draculaDance.visible = isVisible;
 	spiderDance.visible = isVisible;
}



  
  
	
}
