package MenueSystem;

import Engine.Vector2;

/**
 * Will Manage and maintain a list of buttons in addition to Panels
 * @author Michael Letter
 */
public class Menue extends Panel{
	private Vector2 borderPadding = new Vector2();

	private boolean[] menuButtons = null;
	private int numberOfButtons = 0;
	
	
	/**
	 * Will add a given button as member of this Menu
	 * This will resize and reposition the button to match to fit the menue.
	 * This will also call autoUpdateTextScale().
	 * This Button can be retrieved with getSubPanel()
	 * @param targetButton the button to be added
	 * @return if positive: represents the current button number, if negative: operation failed nothing is changed.
	 */
	public int addButton(Button targetButton){
		int retVal = addPanel(targetButton);
		if(retVal >= 0){
			markButtonPosition(retVal);
			numberOfButtons++;
		}
		return retVal;
	}
	public boolean removePanel(int targetPanelLocation){
		boolean retVal = super.removePanel(targetPanelLocation);
		if(retVal && isSubPanelMenueButton(targetPanelLocation)){
			unmarkButtonPosition(targetPanelLocation);
			numberOfButtons--;
		}
		return retVal;
	}
	/**
	 * The Vector describing  space between the buttons and the outer edges of the Panel is returned by this function
	 * @return paddings around the button.
	 */
	public Vector2 getButtonPadding(){
		return borderPadding;
	}
	/**
	 * Will initialize the menuButtons array to defualtSubPanelSize
	 */
	private void initializeMenuButtons(int baseSize){
		if(menuButtons == null){
			if(baseSize > 0){
				menuButtons = new boolean[baseSize + defualtSubPanelSize];
			}
			else{
				menuButtons = new boolean[defualtSubPanelSize];
			}
			for(int loop = 0; loop < menuButtons.length; loop++){
				menuButtons[loop] = false;
			}
		}
	}
	/**
	 * Will Reposition and Rescale all Buttons added using addButton() to fit within this Menue
	 * @param vertical if TRUE, Buttons will be placed in a vertical line. if FALSE, buttons wil be placed in a horizontal line 
	 */
	public void setButtonSizesPositions(boolean vertical){
		if(menuButtons != null){
			int found = 0;
			for(int loop = 0; loop < menuButtons.length && found < numberOfButtons; loop++){
				if(menuButtons[loop]){
					repositionAndResizeButton(found, loop, vertical);
					found++;
				}
			}
		}
	}
	/**
	 * Will reset the buttons position and scale to match the other buttons in the menu
	 * @param buttonPosition the Button Panels SubPanel Position
	 * @param ButtonNumber The position this button exitst in in the order of buttons in the list
	 */
	protected void repositionAndResizeButton(int buttonPosition, int ButtonNumber, boolean verticalMenue){
		if(isSubPanelMenueButton(buttonPosition)){
			Button target = (Button)getPanel(buttonPosition);
			//Extend Down
			if(verticalMenue){ 
				//Size
				target.getBackgroundSize().setX(getBackgroundSize().getX() - (borderPadding.getX()*2));
				target.getBackgroundSize().setY((getBackgroundSize().getY() - (borderPadding.getY()*2))/numberOfButtons);
				//Position
				target.getBackgroundCenter().setX(getBackgroundCenter().getX());
				target.getBackgroundCenter().setY((((getBackgroundSize().getY()/2) - borderPadding.getY() - (target.getBackgroundSize().getY()/2)) + getBackgroundCenter().getY()) - (ButtonNumber * target.getBackgroundSize().getY()));
				//FirstCenter - (ButtonNumber * incrementAmount)
				//incrementAmount = buttonSize
				//totalDistance = MenueSize - padding 
				//FirstCenter = (totalDistance/2) + MenuCenter + buttonSize/2
			}
			//Extend Right
			else{
				//Size
				target.getBackgroundSize().setX((getBackgroundSize().getX() - (borderPadding.getX()*2))/numberOfButtons);
				target.getBackgroundSize().setY(getBackgroundSize().getY() - (borderPadding.getY()*2));
				//Position
				target.getBackgroundCenter().setX((((getBackgroundSize().getX()/2) - borderPadding.getX() - (target.getBackgroundSize().getX()/2)) + getBackgroundCenter().getX()) - (ButtonNumber * target.getBackgroundSize().getX()));
				target.getBackgroundCenter().setY(getBackgroundCenter().getY());
			}
			target.autoUpdateTextPositionScale();
		}
	}
	/**
	 * Will return true if the subPanl at the given position is indeed a button in this menu
	 * @param buttonPosition subPanl in question
	 * @return if TRUE, if the subPanl at the given position is indeed a button in this menu. otherwise FALSE
	 */
	public boolean isSubPanelMenueButton(int buttonPosition){
		if(menuButtons != null && buttonPosition < menuButtons.length && menuButtons[buttonPosition]){
			return true;
		}
		return false;
	}
	/**
	 * Will mark a button position in menuButtons
	 * @param buttonPosition
	 */
	private void markButtonPosition(int buttonPosition){
		if(buttonPosition >= 0){
			if(menuButtons == null){
				initializeMenuButtons(buttonPosition);
			}
			else if(menuButtons.length <= buttonPosition){
				expandMenueButtons((buttonPosition - menuButtons.length + 1) + defualtSubPanelSize);
			}
			menuButtons[buttonPosition] = true;
		}
	}
	/**
	 * Will unmark a button in the menuButtons array
	 * @param buttonPosition
	 */
	private void unmarkButtonPosition(int buttonPosition){
		if(menuButtons != null && buttonPosition >= 0 && buttonPosition < menuButtons.length){
			menuButtons[buttonPosition] = false;
		}
	}
	/**
	 * Will expand the menuButtons array by the given amount
	 * @param expandBy
	 */
	private void expandMenueButtons(int expandBy){
		if(expandBy > 0){
			boolean[] newMenueButtons = new boolean[menuButtons.length + expandBy];
			int loop = 0;
			while(loop < menuButtons.length){
				newMenueButtons[loop] = menuButtons[loop];
				loop++;
			}
			while(loop < newMenueButtons.length){
				newMenueButtons[loop] = false;
				loop++;
			}
			menuButtons = newMenueButtons;
		}
	}
}
