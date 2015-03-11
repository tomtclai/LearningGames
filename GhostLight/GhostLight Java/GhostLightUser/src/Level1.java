

import java.awt.Color;
import java.awt.event.KeyEvent;

import GhostLight.Interface.FlashLight.lightType;
import GhostLight.Interface.InteractableObject;
import GhostLight.Interface.InteractableObject.ObjectType;
import GhostLight.Interface.MouseState.MouseButton;
import GhostLight.Interface.OnScreenButtons.ScreenButton;
import Engine.BaseCode;
import MenueSystem.Button;
import MenueSystem.Panel;
import MenueSystem.Label;
/**
 * Example Level
 * @author Michael Letter
 */
public class Level1 extends UserCode {
	
	int instructionCounter = 0;
	int targetPosition = -1;
	int tempCounter = 0;
	Panel allPopUps = null;
	Panel clickToContinue = null;
	
	float finalOpacity = 0.6f;
	float OpacityIncrement = 0.02f;
	
	int autoClosePopUp = -1;
	
	public Level1(){
		super();
		pointsToWin = 200;
	}
	public void initialize(){
		super.initialize();
		
		super.gameState.setMessage("");
		
		if(clickToContinue == null){
			clickToContinue = new Panel();
			clickToContinue.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			clickToContinue.getBackgroundSize().set(BaseCode.world.getWidth(), BaseCode.world.getHeight());
			
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
		clickToContinue.setVisibility(false, true);
		clickToContinue.setBackgroundColor(new Color(0f,0f,0f,0f));
		clickToContinue.addToAutoDrawSet();
		
		
		if(allPopUps == null){
			//Building Pop Up Messages
			allPopUps = new Panel();
			allPopUps.setVisibility(false, true);
			
			//1: When the player starts this level, a popup appears with text:
			Label popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()*0.3f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("You’ve entered the haunted house, with " + '\n'
						+ "only a flashlight. Fortunately, the " + '\n'
						+ "monsters within are frightened of" + '\n'
						+ "the light…");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.25f));
			allPopUps.addPanel(popUp);
			
			//2: When the player clicks the mouse, this popup disappears and a target appears on one of the unrevealed tiles (a pumpkin). A smaller popup appears adjacent to the targeted tile, reading:
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()*0.24f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("The house is full of shadowy figures." + '\n' 
						+ "Use the beam to target this one and click" + '\n'
						+ "the left mouse button to reveal it!");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.18f));
			allPopUps.addPanel(popUp);
			
			//3: If the player tries to target a different tile, nothing happens. If they target the correct tile and click, the pumpkin is revealed. There is a green hint on one side of the pumpkin and a red hint on the other side. 
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.37f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("Just a Pumpkin! After you’ve revealed a" + '\n'
						+ "pumpkin, you can shine the flashlight on" + '\n'
						+ "it again to collect it and earn points." + '\n'
						+ " This pumpkin is worth 10 points. " + '\n'
						+ "Try clicking it now");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.25f));
			allPopUps.addPanel(popUp);
			
			//4: When the player clicks the pumpkin and removes it, another popup appears to explain the hints. 
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.44f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("When you shine your beam to reveal one"  + '\n'
						+ "tile, you get a hint for the tiles on either"  + '\n'
						+ "side. Green means the tile will be"  + '\n'
						+ "something helpful, like a pumpkin. Red" + '\n'
						+ "means the tile is an enemy. Try clicking" + '\n'
						+ "on the red tile now to reveal it!");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.25f));
			allPopUps.addPanel(popUp);
			
			//5: When the player clicks on the red tile, they reveal a ghost
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.18f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText(" A ghost! Ghosts are enemies, so they’re" + '\n'
						+ "made of sterner stuff than pumpkins.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.16f));
			allPopUps.addPanel(popUp);
			
			//6 An arrow points to the green health bar under the ghost.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.4f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("The green health bar underneath an" + '\n'
						+ "enemy indicates how many times you need" + '\n'
						+ "to shine your flashlight on it before" + '\n'
						+ "it’ll be frightened away. " + '\n'
						+ "Try clicking on this ghost now.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.23f));
			allPopUps.addPanel(popUp);
			
			//7: When the player clicks on the ghost, another popup appears.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight() * 0.3f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("One more click would finish that ghost" + '\n'
						+ "off, but first, let’s collect some more" + '\n'
						+ "points." + '\n'
						+ "Click on this green tile to reveal it.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.25f));
			allPopUps.addPanel(popUp);
			
			//8: When the player clicks on the green hint, they reveal another pumpkin, worth 5 points.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.18f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("Another pumpkin!" + '\n'
						+ "Collect its points by clicking on it.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.17f));
			allPopUps.addPanel(popUp);
			
			//9: When the player clicks on the pumpkin, the ghost begins to transform into an angry ghost and regenerates its lost health, so it now has 3 health again. (Player score: 15)
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight() * 0.18f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("Uh oh. If you don’t eliminate ghosts right" + '\n'
						+ "away, they get angry.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.18f));
			allPopUps.addPanel(popUp);
			
			//10: An arrow points to the red counter underneath the angry ghost’s health bar.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.52f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("A ghost will start to transform into an" + '\n'
						+ "angry ghost three turns after you reveal" + '\n'
						+ "it, and will get stronger, regaining any" + '\n'
						+ "health it previously lost. Luckily, angry" + '\n'
						+ "ghosts are also worth more points! Click" + '\n'
						+ "on this angry ghost three times to remove" + '\n'
						+ "it.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.27f));
			allPopUps.addPanel(popUp);
			
			//11: After the player has removed the angry ghost, another popup appears, along with a target on the wide beam button. (Player score: 65)
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.30f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText(" Good job! Your flashlight has special" + '\n'
						+ " beams that can help you find enemies" + '\n'
						+ " and get rid of them faster. Select the" + '\n'
						+ " Wide Beam now.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.25f));
			allPopUps.addPanel(popUp);
			
			//12: The wide beam is now activated. When the wide beam is selected, the leftmost five tiles are highlighted and a popup appears:
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.37f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("The Wide Beam won’t remove tiles from" + '\n'
						+ "the field, but it gives you a helpful" + '\n'
						+ "hint without revealing or activating" + '\n'
						+ "enemies. Try using it over here for"  + '\n'
						+ "a hint at these five tiles.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.25f));
			allPopUps.addPanel(popUp);
			
			//13: Two of the tiles are red, three green. At least one of the red tiles should be situated so that there are two tiles in the vertical column above it; this tile is highlighted.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()*0.45f);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()* 0.18f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("Switch back to your regular" + '\n'
						+ "beam and let’s reveal this enemy.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.2f));
			allPopUps.addPanel(popUp);
			
			//14: When player clicks the red tile, a ghost is revealed.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()*0.24f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("Another ghost! Let’s get rid of" + '\n'
						+ "this one quickly with the Laser" + '\n'
						+ "Beam.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.22f));
			allPopUps.addPanel(popUp);
			
			//15: The laser beam button is highlighted.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.4f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("The Laser uses a lot of energy, but" + '\n'
						+ "it packs a punch. It can remove any" + '\n'
						+ "enemy with just one click, and will" + '\n'
						+ "remove or reveal every tile in a" + '\n'
						+ "vertical line.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.25f));
			allPopUps.addPanel(popUp);
			
			//16: The laser beam is now activated. When the player uses the laser beam to destroy the
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()* 0.4f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("Uh oh. Two more ghosts, and you don’t" + '\n'
						+ "have enough energy to remove them both" + '\n'
						+ "with the Laser Beam. Try to get rid of" + '\n'
						+ "of them! Just remember, angry ghosts" + '\n'
						+ "have an explosive temper.");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.25f));
			allPopUps.addPanel(popUp);
			
			//17: One of the ghosts revealed last turn becomes an angry ghost about to explode. An arrow points to the red bar underneath the health bar.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()/3);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("If you don’t eliminate an angry ghost" + '\n'
						+ "within the eight turns displayed on " + '\n'
						+ "the yellow bar, it erupts and destroys" + '\n'
						+ "everything nearby!");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.2f));
			allPopUps.addPanel(popUp);
			
			//18: The angry ghost explodes.
			popUp = new Label();
			popUp.getBackgroundCenter().set(BaseCode.world.getWidth()/2, BaseCode.world.getHeight()/2);
			popUp.getBackgroundSize().set(BaseCode.world.getWidth() * 0.6f, BaseCode.world.getHeight()*0.37f);
			popUp.setIdleImage("menuArt/ButtonIdle.png");
			popUp.setText("When an angry ghost explodes, it takes" + '\n'
					+ "away one of your hearts. Lose all four" + '\n'
					+ "hearts and you’ll have to restart the" + '\n'
					+ "level. Get 200 points to advance to the" + '\n'
					+ "next round!");
			popUp.getTextPosition().set(popUp.getBackgroundCenter().getX() - (popUp.getBackgroundSize().getX()* 0.42f) ,popUp.getBackgroundCenter().getY() + (popUp.getBackgroundSize().getY()* 0.25f));
			allPopUps.addPanel(popUp);		
		}
		
		allPopUps.addToAutoDrawSet();
		allPopUps.setVisibility(false, true);
		instructionCounter = -1;
		clickableButtons.setButtonActivity(ScreenButton.LASERBUTTON, false);
		clickableButtons.setButtonActivity(ScreenButton.WIDEBUTTON, false);
		light.setLightType(lightType.MEDIUM);
		autoClosePopUp = 1;
		
		//setting up enemies that must exist
		this.targetPosition = 2;
		//setting up enemies for Reveal Beam intro
		objectGrid.getObjectGrid()[1][targetPosition - 1].setObjectType(ObjectType.GHOST, true);
		objectGrid.getObjectGrid()[1][targetPosition + 1].setObjectType(ObjectType.PUMPKIN, true);
		objectGrid.getObjectGrid()[1][targetPosition + 1].setScore(5);
		objectGrid.getObjectGrid()[1][targetPosition].setObjectType(ObjectType.PUMPKIN, true);
		objectGrid.getObjectGrid()[1][targetPosition].setHighlight(true);	
		objectGrid.getObjectGrid()[1][targetPosition].setScore(10);	
		
		//setting up enemies for 
		objectGrid.getObjectGrid()[1][targetPosition + 2].setObjectType(ObjectType.GHOST, true);
		objectGrid.getObjectGrid()[1][targetPosition + 3].setObjectType(ObjectType.PUMPKIN, true);
		objectGrid.getObjectGrid()[1][targetPosition + 4].setObjectType(ObjectType.PUMPKIN, true);
		objectGrid.getObjectGrid()[1][targetPosition + 5].setObjectType(ObjectType.GHOST, true);
		objectGrid.getObjectGrid()[1][targetPosition + 6].setObjectType(ObjectType.PUMPKIN, true);
		
	}
	public void update(){
		if(allPopUps != null){
			if(autoClosePopUp < 0){ 
				updateInstruction();
				if(allPopUps.getPanel(instructionCounter).getBackGroundVisibility() &&
						super.mouse.isButtonTapped(MouseButton.LEFT) || super.mouse.isButtonTapped(MouseButton.RIGHT) || super.mouse.isButtonTapped(MouseButton.MIDDLE) || 
						super.keyboard.isButtonTapped(KeyEvent.VK_ENTER) || super.keyboard.isButtonTapped(KeyEvent.VK_SPACE)){
					allPopUps.getPanel(instructionCounter).setVisibility(false, true);
					clickToContinue.setVisibility(false, true);
				}
			}
			else {
				this.incrementPopUp(autoClosePopUp);
				autoClosePopUp = -1;
			}
			if(clickToContinue.getBackgroundColor().getAlpha() < finalOpacity * 255){
				clickToContinue.setBackgroundColor(new Color((float)(clickToContinue.getBackgroundColor().getRed()), (float)(clickToContinue.getBackgroundColor().getBlue()/255), (float)(clickToContinue.getBackgroundColor().getGreen()/255), ((float)clickToContinue.getBackgroundColor().getAlpha()/255) + OpacityIncrement));
			}
		}
	}
	
	
	protected InteractableObject getRandomEnemy(){
		InteractableObject retVal = new InteractableObject();
		double percent = Math.random();
		//%65 Pumpkin
		if(percent <= 0.65){
			retVal.setObjectType(ObjectType.PUMPKIN, true);
		}
		//%35 Ghost
		else{
			retVal.setObjectType(ObjectType.GHOST, true);
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
	protected void updateInstruction(){
		if(allPopUps != null){
			//1: When the player starts this level, a popup appears with text:
			if(instructionCounter == 0){
				if(!allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
					incrementPopUp(1);
				}
			}
			//6: When the player clicks on the red tile, they reveal a ghost
			else if(instructionCounter == 4){
				if(!allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
					incrementPopUp(1);
				}
			}
			//9: When the player clicks on the pumpkin, the ghost begins to transform 
			//into an angry ghost and regenerates its lost health, so it now has 3 health again.
			//(Player score: 15)
			else if(instructionCounter == 8){
				if(!allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
					incrementPopUp(1);
				}
			}
			//10: When the player clicks on the pumpkin, the ghost begins to transform 
			//	into an angry ghost and regenerates its lost health, so it now has 3 
			//	health again. (Player score: 15)
			else if(instructionCounter == 9){
				if(!allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
					super.update();
					objectGrid.getObjectGrid()[2][targetPosition].setHighlight(true);
				}
			}
			//11: After the player has removed the angry ghost, another popup appears, 
			//	along with a target on the wide beam button. (Player score: 65)
			else if(instructionCounter == 10){
				if(keyboard.isButtonTapped(KeyEvent.VK_V) || keyboard.isButtonTapped(KeyEvent.VK_2) || keyboard.isButtonTapped(KeyEvent.VK_S) || keyboard.isButtonTapped(KeyEvent.VK_DOWN)
							|| keyboard.isButtonTapped(KeyEvent.VK_W) || keyboard.isButtonTapped(KeyEvent.VK_UP) 
							|| mouse.isButtonTapped(MouseButton.MIDDLE) || mouse.isButtonTapped(MouseButton.RIGHT)
							|| clickableButtons.isButtonTapped(ScreenButton.WIDEBUTTON)){
					light.setLightType(lightType.WIDE);
					clickableButtons.unHighlightButton(ScreenButton.WIDEBUTTON);
					autoClosePopUp = 1;
				}
				else if(!allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
					allPopUps.getPanel(instructionCounter).setVisibility(true, true);
					this.clickToContinue.setVisibility(true, true);
				}
			}
			else if(instructionCounter == 11){
				if(!allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
					objectGrid.getObjectGrid()[2][0].setHighlight(true);
					objectGrid.getObjectGrid()[2][1].setHighlight(true);
					objectGrid.getObjectGrid()[2][2].setHighlight(true);
					objectGrid.getObjectGrid()[2][3].setHighlight(true);
					objectGrid.getObjectGrid()[2][4].setHighlight(true);
					targetPosition = 2;
					super.update();
				}
			}
			//13: Two of the tiles are red, three green. At least one of the red 
			//	tiles should be situated so that there are two tiles in the vertical
			//	column above it; this tile is highlighted.
			else if(instructionCounter == 12){
				if(light.getLightType() != lightType.MEDIUM){
					if(keyboard.isButtonTapped(KeyEvent.VK_N) || keyboard.isButtonTapped(KeyEvent.VK_1) || keyboard.isButtonTapped(KeyEvent.VK_S) || keyboard.isButtonTapped(KeyEvent.VK_DOWN)
							|| keyboard.isButtonTapped(KeyEvent.VK_W) || keyboard.isButtonTapped(KeyEvent.VK_UP) 
							|| mouse.isButtonTapped(MouseButton.MIDDLE) || mouse.isButtonTapped(MouseButton.RIGHT)
							|| clickableButtons.isButtonTapped(ScreenButton.MEDIUMBUTTON)){
						light.setLightType(lightType.MEDIUM);
						clickableButtons.unHighlightButton(ScreenButton.MEDIUMBUTTON);
						super.update();
					}
					else if(!allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
						allPopUps.getPanel(instructionCounter).setVisibility(true, true);
						this.clickToContinue.setVisibility(true, true);
					}
				}
				else{
					super.update();
				}
			}
			//14: When player clicks the red tile, a ghost is revealed.
			else if(instructionCounter == 13){
				if(!allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
					incrementPopUp(4);
				}
			}
			//15: When player clicks the red tile, a ghost is revealed. The laser beam
			//	button is highlighted.
			else if(instructionCounter == 14){
				if(light.getLightType() != lightType.LASER){
					if(clickableButtons.isButtonTapped(ScreenButton.LASERBUTTON) || keyboard.isButtonTapped(KeyEvent.VK_B)  || keyboard.isButtonTapped(KeyEvent.VK_3)
							|| keyboard.isButtonTapped(KeyEvent.VK_W) || keyboard.isButtonTapped(KeyEvent.VK_UP) || mouse.isButtonTapped(MouseButton.RIGHT)){
						light.setLightType(lightType.LASER);
						clickableButtons.unHighlightButton(ScreenButton.LASERBUTTON);
						allPopUps.getPanel(instructionCounter).setVisibility(false, true);
						clickToContinue.setVisibility(false, true);
						objectGrid.getObjectGrid()[2][targetPosition].setHighlight(true);
					}
					else if(!allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
						allPopUps.getPanel(instructionCounter).setVisibility(true, true);
						clickToContinue.setVisibility(true, true);
						clickableButtons.highlightButton(ScreenButton.LASERBUTTON);
						((Label)clickToContinue.getPanel(0)).setText("Select Laser Beam");
					}
				}
				else{
					super.update();
				}	
			}
			//17: The angry ghost explodes.
			else if(instructionCounter == 16){
				if(!allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
					super.update();
					super.gameState.setMessage("get 200 points to Win");
				}
			}
			else if(instructionCounter == 17){
				if(!allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
					super.update();
					super.gameState.setHealthHighLight(false);
				}
			}
			//18:
			else {
				if(!allPopUps.getPanel(instructionCounter).getBackGroundVisibility()){
					super.update();
				}
			}
		}
	}
	protected void setLightPosition(int newLightPosition){
		//2: When the player clicks the mouse, this popup disappears and a
		//	target appears on one of the unrevealed tiles (a pumpkin). A smaller 
		//	popup appears adjacent to the targeted tile, reading:
		if(instructionCounter == 1){
			if(newLightPosition == 0){
				super.setLightPosition(1);
			}
			else if(newLightPosition == super.objectGrid.getObjectGrid()[0].length - 1){
				super.setLightPosition(newLightPosition - 1);
			}
			else{
				super.setLightPosition(newLightPosition);
			}
		}
		else{
			super.setLightPosition(newLightPosition);
		}
	}
	protected void activateLight(){
		//2: When the player clicks the mouse, this popup disappears and a
		//	target appears on one of the unrevealed tiles (a pumpkin). A smaller 
		//	popup appears adjacent to the targeted tile, reading:
		if(instructionCounter == 1){
			if(light.getPosition() == targetPosition){	
				targetPosition--;
				autoClosePopUp = 1;
				super.activateLight();
			}
		}
		//3: If the player tries to target a different tile, nothing happens. 
		//	If they target the correct tile and click, the pumpkin is revealed.
		//	There is a green hint on one side of the pumpkin and a red hint on 
		//	the other side.
		else if(instructionCounter == 2){
			if(light.getPosition() == targetPosition){
				objectGrid.getObjectGrid()[1][targetPosition].setHighlight(false);	
				targetPosition = 0;
				autoClosePopUp = 1;
				super.activateLight();
				objectGrid.getObjectGrid()[2][targetPosition].setHighlight(true);
			}
		}
		//4: When the player clicks the pumpkin and removes it, another popup 
		//	appears to explain the hints.
		else if(instructionCounter == 3){
			if(light.getPosition() == targetPosition){
				targetPosition++;
				autoClosePopUp = 1;
				super.activateLight();
			}
		}
		//6 An arrow points to the green health bar under the ghost.
		else if(instructionCounter == 5){
			if(light.getPosition() == targetPosition){
				objectGrid.getObjectGrid()[2][targetPosition].setHighlight(false);
				targetPosition--;
				autoClosePopUp = 1;
				super.activateLight();
				objectGrid.getObjectGrid()[2][targetPosition].setHighlight(true);
			}
		}
		//7: When the player clicks on the ghost, another popup appears.
		else if(instructionCounter == 6){
			if(light.getPosition() == targetPosition){	
				targetPosition++;
				autoClosePopUp = 1;
				super.activateLight();
			}
		}
		//8: When the player clicks on the green hint, they reveal another
		//	pumpkin, worth 5 points.
		else if(instructionCounter == 7){
			if(light.getPosition() == targetPosition){	
				autoClosePopUp = 1;
				objectGrid.getObjectGrid()[2][targetPosition].setHighlight(false);
				targetPosition += 3;
				super.activateLight();
			}
		}
		//10: When the player clicks on the pumpkin, the ghost begins to transform 
		//	into an angry ghost and regenerates its lost health, so it now has 3 
		//	health again. (Player score: 15)
		else if(instructionCounter == 9){
			if(light.getPosition() == targetPosition){
				if(objectGrid.getObjectGrid()[2][targetPosition].getHealth() == 1){
					autoClosePopUp = 2;
					clickableButtons.setButtonActivity(ScreenButton.WIDEBUTTON, true);
					clickableButtons.highlightButton(ScreenButton.WIDEBUTTON);
				}
				else{
					System.out.println(objectGrid.getObjectGrid()[2][targetPosition].getHealth());
				}
				super.activateLight();
				targetPosition++;
			}
		}
		//11: After the player has removed the angry ghost, another popup appears, 
		//	along with a target on the wide beam button. (Player score: 65)
		else if(instructionCounter == 10){
			super.activateLight();
		}
		//12: The wide beam is now activated. When the wide beam is selected,
		//	the leftmost five tiles are highlighted and a popup appears:
		else if(instructionCounter == 11){
			if(light.getPosition() == targetPosition && light.getLightType() == lightType.WIDE){
				autoClosePopUp = 3;
				clickableButtons.highlightButton(ScreenButton.MEDIUMBUTTON);
				objectGrid.getObjectGrid()[2][0].setHighlight(false);
				objectGrid.getObjectGrid()[2][1].setHighlight(false);
				objectGrid.getObjectGrid()[2][2].setHighlight(false);
				objectGrid.getObjectGrid()[2][3].setHighlight(false);
				targetPosition = 5;
				super.activateLight();
			}
		}
		//13: Two of the tiles are red, three green. At least one of the red 
		//	tiles should be situated so that there are two tiles in the vertical
		//	column above it; this tile is highlighted.
		else if(instructionCounter == 12){
			if(light.getPosition() == targetPosition && light.getLightType() == lightType.MEDIUM){
				autoClosePopUp = 1;
				targetPosition++;
				super.activateLight();
				objectGrid.getObjectGrid()[2][targetPosition].setHighlight(false);
				clickableButtons.setButtonActivity(ScreenButton.LASERBUTTON, true);
				clickableButtons.highlightButton(ScreenButton.LASERBUTTON);
			}
		}
		//15: When player clicks the red tile, a ghost is revealed. The laser beam
		//	button is highlighted.
		else if(instructionCounter == 14){
			if(light.getPosition() == targetPosition && light.getLightType() == lightType.LASER){
				objectGrid.getObjectGrid()[0][targetPosition].setObjectType(ObjectType.ANGRY, true);
				objectGrid.getObjectGrid()[0][targetPosition].setGhostTimer((short)3);
				objectGrid.getObjectGrid()[1][targetPosition].setObjectType(ObjectType.ANGRY, true);
				objectGrid.getObjectGrid()[1][targetPosition].setGhostTimer((short)3);
				super.activateLight();
				super.gameState.setLightPower(0.25f);
				autoClosePopUp = 1;
				tempCounter = 1;
			}
		}
		//16: One of the ghosts revealed last turn becomes an angry ghost about
		//	to explode. An arrow points to the red bar underneath the health bar.
		else if(instructionCounter == 15){
			System.out.print("Test ");
			super.activateLight();
			tempCounter++;
			targetPosition += tempCounter;
			autoClosePopUp = 1;
		}
		//17: One of the ghosts revealed last turn becomes an angry ghost about
		//	to explode. An arrow points to the red bar underneath the health bar.
		else if(instructionCounter == 16){
			super.activateLight();
			autoClosePopUp = 1;
			gameState.setHealthHighLight(true);
		}
		else{
			super.activateLight();
		}
	}
	/**
	 * 1 = Click To Continue,
	 * 2 = Select Wide Beam,
	 * 3 = Select Normal Beam,
	 * 4 = Select Laser Beam,
	 */
	private void incrementPopUp(int transitionText){
		if(instructionCounter >= 0){
			allPopUps.getPanel(instructionCounter).setVisibility(false, true);
		}
		instructionCounter++;
		if(allPopUps.getPanel(instructionCounter) != null){
			allPopUps.getPanel(instructionCounter).setVisibility(true, true);
			//Select Wide Beam
			if(transitionText == 2){
				((Label)clickToContinue.getPanel(0)).setText("Select Wide Beam");
			}
			//Select Normal Beam
			else if(transitionText == 3){
				((Label)clickToContinue.getPanel(0)).setText("Select Normal Beam");
			}
			//Select Laser Beam
			else if(transitionText == 4){
				((Label)clickToContinue.getPanel(0)).setText("Select Laser Beam");
			}
			//Click To Continue
			else{
				((Label)clickToContinue.getPanel(0)).setText("Click To Continue");
			}
			clickToContinue.setVisibility(true, true);
			clickToContinue.setBackgroundColor(new Color(0f,0f,0f,0f));
		}
	}
	
}
