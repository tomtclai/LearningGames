

import java.awt.Color;
import java.awt.event.KeyEvent;

import Engine.BaseCode;
import GhostLight.Interface.InteractableObject;
import GhostLight.Interface.FlashLight.lightType;
import GhostLight.Interface.InteractableObject.ObjectType;
import GhostLight.Interface.MouseState.MouseButton;
import GhostLight.Interface.OnScreenButtons.ScreenButton;
import MenueSystem.Button;
import MenueSystem.Label;
import MenueSystem.Panel;
/**
 * Example Level
 *  Ghosts Frankenstiens, Vampires and Cats
 * @author Michael Letter
 */
public class Level3 extends UserCode {

	Panel allPopUps = null;
	int targetPosition = -1;
	int instructionCounter = 0;
	boolean autoClosePopUp = false;
	Panel clickToContinue = null;
	
	float finalOpacity = 0.6f;
	float OpacityIncrement = 0.02f;
	
	public Level3(){
		super();
		pointsToWin = 500;
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
			
			//1:When the level starts, there is a target on one of the unrevealed tiles in the center.
			Label popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.24f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("From the depths of the haunted house" + '\n'
						+ "comes a bloodsucking menace. Click on" + '\n'
						+ "this tile to reveal him!");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.23f));
			allPopUps.addPanel(popUp);
			
			//2:When the player clicks, Dracula is revealed!
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.5f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("Dracula is a powerful enemy who preys" + '\n'
					+ "on weaker monsters. Every turn, he’ll" + '\n'
					+ "jump to a new location and bite whatever" + '\n'
					+ "he finds there, turning it into a ghost!" + '\n'
					+ "If he ever jumps on an angry ghost, the" + '\n'
					+ "ghost will explode instantly and take" + '\n'
					+ "away a heart.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.28f));
			allPopUps.addPanel(popUp);
			
			//3:The wide beam and the leftmost set of tiles are highlighted.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.24f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("Luckily, you have a friend on your" + '\n'
						+ "side. Use the wide beam to search" + '\n'
						+ "for it!");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.38f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.18f));
			allPopUps.addPanel(popUp);
			
			//4:When the player clicks the highlighted tiles, four are red 
			//	and one is green. Dracula can jump to any tile in the row 
			//	except the green hint. The green tile is now highlighted.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.24f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("Pumpkins aren’t the only helpful tiles." + '\n'
						+ "Click on the green tile now to reveal" + '\n'
						+ "an ally!");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.38f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.18f));
			allPopUps.addPanel(popUp);
			
			//5:When the player clicks on the highlighted green tile, a cat is revealed.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.4f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("Cats are Dracula’s worst enemy! Cats" + '\n'
						+ "hate ghosts, and can jump on any ghost" + '\n'
						+ "in the same row to frighten it away." + '\n'
						+ "Cats can even chase away angry ghosts," + '\n'
						+ "although the cat will disappear too.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.23f));
			allPopUps.addPanel(popUp);
			
			//6:The player can click anywhere at this point. When they do, a new popup appears.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.5f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("Be careful. Your flashlight beam will" + '\n'
						+ "frighten away cats as well, so be sure" + '\n'
						+ "not to target them unless you really" + '\n'
						+ "have to! With the cat’s help, try to" + '\n'
						+ "reach 500 points. Once you do, you’ll" + '\n'
						+ "have beaten this demo. Thanks for" + '\n'
						+ "playing!");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.3f));
			allPopUps.addPanel(popUp);
		}
		allPopUps.addToAutoDrawSet();
		allPopUps.setVisibility(false, true);
		
		targetPosition = 6;
		instructionCounter = -1;
		autoClosePopUp = true;
		
		objectGrid.getObjectGrid()[1][targetPosition].setObjectType(ObjectType.VAMPIRE, true);
		objectGrid.getObjectGrid()[1][targetPosition].setHighlight(true);
		
		objectGrid.getObjectGrid()[0][objectGrid.getObjectGrid()[0].length - 1].setObjectType(ObjectType.CAT, true);
		
		
		super.gameState.setMessage("get 500 points to Win");
	}
	public void update(){
		if(clickToContinue.getBackgroundColor().getAlpha() < finalOpacity * 255){
			clickToContinue.setBackgroundColor(new Color((float)(clickToContinue.getBackgroundColor().getRed()), (float)(clickToContinue.getBackgroundColor().getBlue()/255), (float)(clickToContinue.getBackgroundColor().getGreen()/255), ((float)clickToContinue.getBackgroundColor().getAlpha()/255) + OpacityIncrement));
		}
		//2:When the player clicks, Dracula is revealed!
		if(instructionCounter == 1){
			if(autoClosePopUp){
				if(allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
					if(super.mouse.isButtonTapped(MouseButton.LEFT) || super.mouse.isButtonTapped(MouseButton.RIGHT) || super.mouse.isButtonTapped(MouseButton.MIDDLE) || 
							super.keyboard.isButtonTapped(KeyEvent.VK_ENTER) || super.keyboard.isButtonTapped(KeyEvent.VK_SPACE)){
						allPopUps.getPanel(instructionCounter).setVisibility(false, true);
						clickToContinue.setVisibility(false, true);
					}
				}
				else{
					clickableButtons.highlightButton(ScreenButton.WIDEBUTTON);
					objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 1].setHighlight(true);
					objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 2].setHighlight(true);
					objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 3].setHighlight(true);
					objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 4].setHighlight(true);
					objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 5].setHighlight(true);
					
					objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 1].setObjectType(ObjectType.CAT, true);
					if(objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 2].getType() == ObjectType.PUMPKIN ||
							objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 2].getType() == ObjectType.CAT){
						objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 2].setObjectType(ObjectType.GHOST, true);
					}
					if(objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 3].getType() == ObjectType.PUMPKIN ||
							objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 3].getType() == ObjectType.CAT){
						objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 3].setObjectType(ObjectType.GHOST, true);
					}
					if(objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 4].getType() == ObjectType.PUMPKIN ||
							objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 4].getType() == ObjectType.CAT){
						objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 4].setObjectType(ObjectType.GHOST, true);
					}
					if(objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 5].getType() == ObjectType.PUMPKIN ||
							objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 5].getType() == ObjectType.CAT){
						objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 5].setObjectType(ObjectType.GHOST, true);
					}
					//forceCat(1, objectGrid.getObjectGrid()[1].length - 1, false);
					this.incrementPopUp();
					((Label)clickToContinue.getPanel(0)).setText("Select Wide Beam");
				}
			}
			else {
				this.incrementPopUp();
				autoClosePopUp = true;
			}
		}
		//3:The wide beam and the leftmost set of tiles are highlighted..
		else if(instructionCounter == 2){			
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
					((Label)clickToContinue.getPanel(0)).setText("Select Wide Beam");
					clickableButtons.highlightButton(ScreenButton.WIDEBUTTON);
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
		//4:When the player clicks the highlighted tiles, four are red 
		//	and one is green. Dracula can jump to any tile in the row 
		//	except the green hint. The green tile is now highlighted.
		else if(instructionCounter == 3){
			//forceCat(1, objectGrid.getObjectGrid()[1].length - 2, true);
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
		//5:When the player clicks on the highlighted green tile, a cat is revealed.
		else if(instructionCounter == 4){
			if(autoClosePopUp){
				if(allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
					if(super.mouse.isButtonTapped(MouseButton.LEFT) || super.mouse.isButtonTapped(MouseButton.RIGHT) || super.mouse.isButtonTapped(MouseButton.MIDDLE) || 
							super.keyboard.isButtonTapped(KeyEvent.VK_ENTER) || super.keyboard.isButtonTapped(KeyEvent.VK_SPACE)){
						allPopUps.getPanel(instructionCounter).setVisibility(false, true);
						clickToContinue.setVisibility(false, true);
					}
				}
				else{
					this.incrementPopUp();
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
		else {
			this.incrementPopUp();
		}
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
	protected void activateLight(){
		//1:When the level starts, there is a target on one of the unrevealed tiles in the center.
		if(instructionCounter == 0){
			if(light.getPosition() == targetPosition && (light.getLightType() == lightType.LASER || light.getLightType() == lightType.MEDIUM)){
				objectGrid.getObjectGrid()[1][targetPosition].setHighlight(false);
				autoClosePopUp = false;
				super.activateLight();
				targetPosition = objectGrid.getObjectGrid()[1].length - 3;
				gameState.setAnimationTime(100);
			}
		}
		//3:The wide beam and the leftmost set of tiles are highlighted..
		else if(instructionCounter == 2){
			if(light.getPosition() == targetPosition){
				autoClosePopUp = false;
				objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 2].setHighlight(false);
				objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 3].setHighlight(false);
				objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 4].setHighlight(false);
				objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 5].setHighlight(false);
				targetPosition = objectGrid.getObjectGrid()[1].length - 2;
				super.activateLight();
				gameState.setAnimationTime(30);
			}
		}
		//4:When the player clicks the highlighted tiles, four are red 
		//	and one is green. Dracula can jump to any tile in the row 
		//	except the green hint. The green tile is now highlighted.
		else if(instructionCounter == 3){
			if(light.getPosition() == targetPosition && (light.getLightType() == lightType.LASER || light.getLightType() == lightType.MEDIUM)){
				autoClosePopUp = false;
				objectGrid.getObjectGrid()[1][objectGrid.getObjectGrid()[1].length - 2].setHighlight(false);
				super.activateLight();
				super.gameState.setAnimationTime(100);
			}
		}
		//Default
		else if(instructionCounter != 1){
			super.activateLight();
			gameState.setAnimationTime(30);
		}
	}
	protected InteractableObject getRandomEnemy(){
		InteractableObject retVal = new InteractableObject();
		double percent = Math.random();
		//%30 Pumpkin
		if(percent <= 0.35){
			retVal.setObjectType(ObjectType.PUMPKIN, true);
		}
		//%15 Ghost
		else if(percent <= 0.50){
			retVal.setObjectType(ObjectType.GHOST, true);
		}
		//%15 Frankenstein
		else if(percent <= 0.65){
			retVal.setObjectType(ObjectType.FRANKENSTEIN, true);
		}
		//%15 Cat
		else if(percent <= 0.90){
			retVal.setObjectType(ObjectType.CAT, true);
		}
		//%15 Dracula
		else{
			retVal.setObjectType(ObjectType.VAMPIRE, true);
		}
		return retVal;
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
