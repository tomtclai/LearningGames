

import java.awt.Color;
import java.awt.event.KeyEvent;

import Engine.BaseCode;
import GhostLight.Interface.FlashLight.lightType;
import GhostLight.Interface.InteractableObject;
import GhostLight.Interface.InteractableObject.ObjectType;
import GhostLight.Interface.MouseState.MouseButton;
import GhostLight.Interface.OnScreenButtons.ScreenButton;
import MenueSystem.Button;
import MenueSystem.Label;
import MenueSystem.Panel;
/**
 * Ghosts Pumpkins and Frankensteins
 * @author Michael Letter
 */
public class Level2 extends UserCode {
	
	Panel allPopUps = null;
	int targetPosition = -1;
	int instructionCounter = 0;
	boolean autoClosePopUp = false;
	Panel clickToContinue = null;
	
	float finalOpacity = 0.6f;
	float OpacityIncrement = 0.02f;
	
	InteractableObject frank = null;
	InteractableObject ghost1 = null;
	InteractableObject ghost2 = null;
	
	public Level2(){
		super();
		pointsToWin = 300;
	}
	public void initialize(){
		super.initialize();
		
		if(clickToContinue == null){
			clickToContinue = new Panel();
			clickToContinue.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			clickToContinue.getBackgroundSize().set(BaseCode.world.getWidth(), BaseCode.world.getHeight());
			clickToContinue.setBackgroundColor(new Color(0f,0f,0f,0f));
			
			Label clicker = new Label();
			clicker = new Button();
			clicker.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight() * 0.1f);
			clicker.getBackgroundSize().set(BaseCode.world.getWidth()/3, BaseCode.world.getHeight()/6);
			clicker.setIdleImage("menuArt/ButtonIdle.png");
			clicker.setSelectImage("menuArt/ButtonIdle.png");
			clicker.setText("Click to Continue");
			clicker.autoUpdateTextPositionScale();
			clickToContinue.addPanel(clicker);
			
		}
		clickToContinue.addToAutoDrawSet();
		
		if(allPopUps == null){
			allPopUps = new Panel();
			allPopUps.setVisibility(false, true);
			
			//1:This level starts with a popup and an arrow pointing to a tile in the 
			//	center of the row.
			Label popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.37f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("Well done! Unfortunately, ghosts aren’t" + '\n'
						+ "the only creatures prowling around the" + '\n'
						+ "haunted house. It’s time to meet a new" + '\n'
						+ "enemy! Click on the highlighted tile" + '\n'
						+ "to reveal it!");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.25f));
			allPopUps.addPanel(popUp);
			
			//2:When the tile is clicked, a Frankenstein is revealed. It pounds the tiles on 
			//	either side into Ghosts. The energy bar is highlighted to show the loss of energy from Frankenstein’s reveal.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()*0.35f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("When Frankenstein appears, he pounds" + '\n'
						+ "everything he can reach! Anything he" + '\n'
						+ "punches turns into a ghost. He also"  + '\n'
						+ "drains your energy! Luckily, the Wide" + '\n'
						+ "Beam can help you deal with him.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.25f));
			allPopUps.addPanel(popUp);
			
			//3:A highlight appears on the wide beam button and targets appear on the 
			//	Frankenstein and the ghosts surrounding it.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()*0.35f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("When tiles have already been revealed," + '\n'
					+ "you can shine the wide beam on them to " + '\n'
					+ "deal 1 damage. Click on the Frankenstein" + '\n'
					+ "and its ghost friends twice with the wide" + '\n'
					+ "beam to eliminate all three at once!");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.25f));
			allPopUps.addPanel(popUp);
			
			//4:When the ghosts and the Frankenstein are destroyed, a final popup appears.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()*0.5f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("Watch out for the deadly combo of" + '\n'
						+ "Frankenstein and an angry ghost. If" + '\n'
						+ "Frankenstein appears next to an angry" + '\n'
						+ "ghost and punches it, the ghost will" + '\n'
						+ "explode instantly and take away a heart!" + '\n'
						+ "Good luck! Get 300 points to reach the" + '\n'
						+ "next level.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.28f));
			allPopUps.addPanel(popUp);
		}
		
		allPopUps.addToAutoDrawSet();
		allPopUps.setVisibility(false, true);
		
		instructionCounter = -1;
		targetPosition = 7;
		
		autoClosePopUp = true;
		
		objectGrid.getObjectGrid()[1][targetPosition].setObjectType(ObjectType.FRANKENSTEIN, true);
		objectGrid.getObjectGrid()[1][targetPosition].setHighlight(true);
		
		frank = objectGrid.getObjectGrid()[1][targetPosition];
		
		//super.clickableButtons.highlightButton(ScreenButton.WIDEBUTTON);
		
		
		super.gameState.setMessage("get 300 points to Win");
	}
	public void update(){
		if(allPopUps != null){
			if(clickToContinue.getBackgroundColor().getAlpha() < finalOpacity * 255){
				clickToContinue.setBackgroundColor(new Color((float)(clickToContinue.getBackgroundColor().getRed()), (float)(clickToContinue.getBackgroundColor().getBlue()/255), (float)(clickToContinue.getBackgroundColor().getGreen()/255), ((float)clickToContinue.getBackgroundColor().getAlpha()/255) + OpacityIncrement));
			}
			//2:When the tile is clicked, a Frankenstein is revealed. It pounds the tiles on 
			//	either side into Ghosts. The energy bar is highlighted to show the loss of energy from Frankenstein’s reveal.
			if(instructionCounter == 1){
				if(autoClosePopUp){
					if(allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
						if(super.mouse.isButtonTapped(MouseButton.LEFT) || super.mouse.isButtonTapped(MouseButton.RIGHT) || super.mouse.isButtonTapped(MouseButton.MIDDLE) || 
								super.keyboard.isButtonTapped(KeyEvent.VK_ENTER) || super.keyboard.isButtonTapped(KeyEvent.VK_SPACE)){
							allPopUps.getPanel(instructionCounter).setVisibility(false, true);
							clickToContinue.setVisibility(false, true);
							super.clickableButtons.highlightButton(ScreenButton.WIDEBUTTON);
						}
					}
					else{
						this.incrementPopUp();
						((Label)clickToContinue.getPanel(0)).setText("Select Wide Beam");
					}
				}
				else {
					this.incrementPopUp();
					autoClosePopUp = true;
				}
			}
			//3:A highlight appears on the wide beam button and targets appear on the 
			//	Frankenstein and the ghosts surrounding it.
			else if(instructionCounter == 2){
				frank = objectGrid.getObjectGrid()[1][targetPosition];
				ghost1 = objectGrid.getObjectGrid()[1][targetPosition - 1];
				ghost2 = objectGrid.getObjectGrid()[1][targetPosition + 1];
				
				if(ghost1 != null){
					ghost1.setHighlight(true);
				}
				if(ghost2 != null){
					ghost2.setHighlight(true);
				}
				
				if(autoClosePopUp){
					if(allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
						if(clickableButtons.isButtonTapped(ScreenButton.WIDEBUTTON) || keyboard.isButtonTapped(KeyEvent.VK_V) || keyboard.isButtonTapped(KeyEvent.VK_2) ||
								(light.getLightType() == lightType.LASER && (keyboard.isButtonTapped(KeyEvent.VK_W) || keyboard.isButtonTapped(KeyEvent.VK_UP) || mouse.isButtonTapped(MouseButton.RIGHT))) ||
								(light.getLightType() == lightType.MEDIUM && (keyboard.isButtonTapped(KeyEvent.VK_S) || keyboard.isButtonTapped(KeyEvent.VK_DOWN) || mouse.isButtonTapped(MouseButton.MIDDLE)))){
							allPopUps.getPanel(instructionCounter).setVisibility(false, true);
							clickToContinue.setVisibility(false, true);
							clickableButtons.unHighlightButton(ScreenButton.WIDEBUTTON);
							light.setLightType(lightType.WIDE);
						}
					}
					else if(light.getLightType() != lightType.WIDE){
						allPopUps.getPanel(instructionCounter).setVisibility(true, true);
						clickToContinue.setVisibility(true, true);
						clickableButtons.highlightButton(ScreenButton.WIDEBUTTON);
						((Label)clickToContinue.getPanel(0)).setText("Select Wide Beam");
					}
					else{
						super.update();
					}
				}
				else {
					this.incrementPopUp();
					autoClosePopUp = true;
				}
			}
			//Default
			else if(instructionCounter >= 0){
				if(autoClosePopUp){
					if(allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
						if(super.mouse.isButtonTapped(MouseButton.LEFT) || super.mouse.isButtonTapped(MouseButton.RIGHT) || super.mouse.isButtonTapped(MouseButton.MIDDLE) || 
								super.keyboard.isButtonTapped(KeyEvent.VK_ENTER) || super.keyboard.isButtonTapped(KeyEvent.VK_SPACE)){
							allPopUps.getPanel(instructionCounter).setVisibility(false, true);
							clickToContinue.setVisibility(false, true);
						}
					}
					else{
						super.update();
					}
				}
				else {
					this.incrementPopUp();
					autoClosePopUp = true;
				}
			}
			else{
				incrementPopUp();
			}
		}
	}
	protected void activateLight(){
		//1:This level starts with a popup and an arrow pointing to a tile in the 
		//	center of the row.
		if(instructionCounter == 0){
			if(light.getPosition() == targetPosition && (light.getLightType() == lightType.LASER || light.getLightType() == lightType.MEDIUM)){
				targetPosition--;
				autoClosePopUp = false;
				super.activateLight();
				super.gameState.setPowerBarColor(Color.white);
			}
		}
		//2:When the tile is clicked, a Frankenstein is revealed. It pounds the tiles on 
		//	either side into Ghosts. The energy bar is highlighted to show the loss of energy from Frankenstein’s reveal.
		else if(instructionCounter == 2){
			if(light.getPosition() == targetPosition || light.getPosition() == targetPosition - 1 || light.getPosition() == targetPosition + 1){
				targetPosition--;
				super.activateLight();
				if((frank == null || frank.getHealth() <= 0) && (ghost1 == null || ghost1.getHealth() <= 0) && (ghost2 == null || ghost2.getHealth() <= 0)){
					autoClosePopUp = false;
				}
			}
		}
		//Default
		else if(instructionCounter != 1){
			super.activateLight();
		}
	}
	protected InteractableObject getRandomEnemy(){
		InteractableObject retVal = new InteractableObject();
		double percent = Math.random();
		//%50 Pumpkin
		if(percent <= 0.50){
			retVal.setObjectType(ObjectType.PUMPKIN, true);
		}
		//%25 Ghost
		else if(percent <= 0.75){
			retVal.setObjectType(ObjectType.GHOST, true);
		}
		//25% Frankenstein
		else {
			retVal.setObjectType(ObjectType.FRANKENSTEIN, true);
		}
		
		return retVal;
	}
	public void end(){
		super.end();
		if(allPopUps != null){
			allPopUps.destroy();
		}
		if(clickToContinue != null){
			clickToContinue.destroy();
		}
	}
	protected void updatePowerBarColor(){
		if(instructionCounter != 1){
			super.updatePowerBarColor();
		}
	}
	private void incrementPopUp(){
		if(instructionCounter >= 0){
			allPopUps.getPanel(instructionCounter).setVisibility(false, true);
		}
		instructionCounter++;
		if(allPopUps.getPanel(instructionCounter) != null){
			allPopUps.getPanel(instructionCounter).setVisibility(true, true);
			((Label)clickToContinue.getPanel(0)).setText("Click To Continue");
			clickToContinue.setVisibility(true, true);
			clickToContinue.setBackgroundColor(new Color(0f,0f,0f,0f));
		}
	}
}
