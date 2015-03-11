package MenueSystem;

import java.awt.Color;
import Engine.BaseCode;
import Engine.MouseInput;
import Engine.Rectangle;
import Engine.Vector2;

/**
 * Maintains a panel which draws maintains a simple background image and can also contain a series of sub panels with this one
 * Note this panel does not force any of the sub panels to actually reside inside this panel. 
 * It is there resposiblility to stay inside this panels boundaries if they choose too 
 * @author Michael Letter
 */
public class Panel {
	private String selectedTexture = "";
	private String idleTexture = "";
	private String inactiveTexture = "";
	
	private Rectangle Background = null;
	
	/** used to find this panel if it is a subPanel of another panel 
	 * A -1 indicates that the Panel exists on the level above
	 * Note ID[0] is used for communicating with other panels and is not part of the ID
	 */												
	// Levels by array index {Unused , this Panel, subRoot, Root, ...}
	private int[] ID = {-1,-1,-1,-1};
	/** stores the number of indices of the ID that is in use. If 0 then none are in use and this is a root Panel */
	private int IDLength = 0;
	/** References the Panel that is Directly above this one and can be used to access the rest of the system */ 
	private Panel localRoot = null;
	/** Stores all of this panels subPanels */
	private Panel[] subPanels = null;
	/** Stores the next position available in subPanels. Note, does not Garuntee that the index exists yet */
	private int nextAvailableIndex = 0;
	/** Stores the number of Panels that are direct subPanels of this Panel */
	private int numberOfPanels = 0;
	/** Determines the Margin how many extra spots are added if the ID array has to be grown.
	 * Note must Be Greater than zero */
	protected int defualtSubPanelSize = 3;
	
	/**
	 * Used to mark what operating mode this panel is in
	 * IDLE: all Sub panels are updated normally.
	 * SELECTED: all Sub panels are updated normally.
	 * INACTIVE: Sub Panels are not updated.
	 */
	public enum PanelState {IDLE, SELECTED, INACTIVE};
	
	private PanelState state = PanelState.IDLE;
	private boolean stateUnset = true;
	
	public Panel(){
		Background = new Rectangle();
		Background.removeFromAutoDrawSet();
	}
	
	/**
	 * Will automatically update the state of this panel and all contained panels
	 * @param mouse
	 */
	public void autoUpdateState(MouseInput mouse){
		if(state != PanelState.INACTIVE){
			autoUpdateSubPanels(mouse);
		}
	}
	/**
	 * Will auto update this panels subPanels.
	 * Note this function is automatically called by Panel.autoUpdateState(); if the Panel is not INACTIVE
	 * @param mouse
	 */
	protected void autoUpdateSubPanels(MouseInput mouse){
		if(subPanels != null){
			int panelsFound = 0;
			for(int loop = 0; loop < subPanels.length && panelsFound < numberOfPanels; loop++){
				if(subPanels[loop] != null){
					panelsFound++;
					subPanels[loop].autoUpdateState(mouse);
				}
			}
		}
	}
	/**
	 * Will set the image used by the panel when the panel is idle
	 * @param imageLocation the location of the image to be used by the panel when the panel is idle
	 */
	public void setIdleImage(String imageLocation){
		if(imageLocation != null && !imageLocation.equals("")){
			idleTexture = imageLocation;
			BaseCode.resources.loadImage(idleTexture);
			updateImage();
		}
	}
	/**
	 * will return the image used by the panel when the panel is idle
	 * @return the image Location used by the panel when the panel is idle
	 */
	public String getIdleImage(){
		return idleTexture;
	}
	/**
	 * Will set the image used by the panel when the panel is idle
	 * @param imageLocation the location of the image to be used by the panel when the panel is idle
	 */
	public void setSelectImage(String imageLocation){
		if(imageLocation != null && !imageLocation.equals("")){
			selectedTexture = imageLocation;
			BaseCode.resources.loadImage(selectedTexture);
			updateImage();
		}
	}
	/**
	 * will return the image used by the panel when the panel is Selected
	 * @return the image Location used by the panel when the panel is Selected
	 */
	public String getSelectImage(){
		return selectedTexture;
	}
	/**
	 * Will set the image used by the panel when the panel is idle
	 * @param imageLocation the location of the image to be used by the panel when the panel is idle
	 */
	public void setInactiveImage(String imageLocation){
		if(imageLocation != null && !imageLocation.equals("")){
			inactiveTexture = imageLocation;
			BaseCode.resources.loadImage(inactiveTexture);
			updateImage();
		}
	}
	/**
	 * will return the image used by the panel when the panel is Inactive
	 * @return the image Location used by the panel when the panel is Inactive
	 */
	public String getInactiveImage(){
		return inactiveTexture;
	}
	/**
	 * will set the currentState of the Panel to the given PanelState
	 * IDLE: all Sub panels are updated normally.
	 * SELECTED: all Sub panels are updated normally.
	 * INACTIVE: Sub Panels are not updated.
	 * @param newState the desired State
	 * @param setSubPanels if True all SupPanels of this Panel will also be set to the given state in addition to this Panel. otherwise only this Panel is affected
	 */
	public void setState(PanelState newState, boolean setSubPanels){
		//if(state != newState || stateUnset){
			stateUnset = false;
			state = newState;
			updateImage();
			if(setSubPanels && subPanels != null){
				int panelsFound = 0;
				for(int loop = 0; loop < subPanels.length && panelsFound < numberOfPanels; loop++){
					if(subPanels[loop] != null){
						panelsFound++;
						subPanels[loop].setState(newState, setSubPanels);
					}
				}
			}
		//}
	}
	/**
	 * Will return the current state of the panel.
	 * IDLE: all Sub panels are updated normally.
	 * SELECTED: all Sub panels are updated normally.
	 * INACTIVE: Sub Panels are not updated.
	 * @return
	 */
	public PanelState getState(){
		return state;
	}
	/**
	 * Will set the image that is currently be used for the panels draw
	 * @param image the location of the desired image
	 */
	public void setImage(String imageLocation){
		if(imageLocation != null && !imageLocation.equals("")){
			Background.setImage(imageLocation);
		}
	}
	/**
	 * Will return the center of the panel
	 * @return
	 */
	public Vector2 getBackgroundCenter(){
		return Background.center;
	}
	/**
	 * The current Width and height of the of the panel
	 * @return
	 */
	public Vector2 getBackgroundSize(){
		return Background.size;
	}
	/**
	 * the current color used by the panel if no image isin use
	 * @return
	 */
	public Color getBackgroundColor() {
		return Background.color;
	}
	/**
	 * Will set the color of the Panel to the given color
	 * @param color
	 * @return if TRUE: the change was successful, if FALSE no changes were made
	 */
	public boolean setBackgroundColor(Color color){
		if(color != null){
			Background.color = color;
			return true;
		}
		return false;
	}
	/**
	 * Will return whether or not the background is currenrly Visible
	 * @return if TRUE, background is visible, if False, background is invisible
	 */
	public boolean getBackGroundVisibility(){
		return Background.visible;
	}
	/**
	 * Will set whether or not the background is visible
	 * @param visible If TRUE, this background will be made visible. If FALSE, this background will be made invisible
	 */
	public void setBackgroundVisiblility(boolean visible){
		Background.visible = visible;
	}
	/**
	 * If this Panel is another Panel's subPanel this function will return that panel
	 * @return this SubPanels rootPanel
	 */
	public Panel getHostPanel(){
		return localRoot;
	}
	/**
	 * If this Panel is another Panel's subPanel this function will return the int ID that can be used to access this panel from the hostPanel
	 * @return this Panels ID from the HostPanel, not if this ID is negative then this Panel is not any Panels subPanel and has no ID
	 */
	public int getID(){
		if(IDLength > 0){
			return ID[1];
		}
		return -1;
	}
	/**
	 * Will return how many Panels this panel is a SubPanel too
	 * @return how many Panels this panel is a SubPanel too
	 */
	public int getPanelDepth(){
		return IDLength;
	}
	/**
	 * will return the number of SubPanels that are directly contained this one
	 * @return the number of SubPanels that are directly contained this one
	 */
	public int getNumberOfSubPanels(){
		return numberOfPanels;
	}
	/**
	 * Will set whether or not this Panel is Visible 
	 * @param visable If TRUE, this panel will be made visible. If FALSE, this panel will be made invisible
	 * @param affectSubPanels If TRUE, all of this panels subPanels visibility settings will be set to match this Panels visibility settings.
	 * If FALSE, only this panels visibility setting will be affcted
	 */
	public void setVisibility(boolean visible, boolean effectSubPanels){
		setBackgroundVisiblility(visible);
		if(effectSubPanels && subPanels != null){
			int panelsFound = 0;
			for(int loop = 0; loop < subPanels.length && panelsFound < numberOfPanels; loop++){
				if(subPanels[loop] != null){
					panelsFound++;
					subPanels[loop].setVisibility(visible, effectSubPanels);
				}
			}
		}
	}
	/**
	 * Will scale this panel by the given amount in world coordinates
	 * @param move the amount that this panel will be moved by
	 * @param affectSubPanels If TRUE, all of this panels subPanels position settings will be set to match this Panels position settings.
	 */
	public void movePanel(Vector2 move, boolean effectSubPanels){
		if(move != null){
			getBackgroundCenter().add(move);
			if(effectSubPanels && subPanels != null){
				int panelsFound = 0;
				for(int loop = 0; loop < subPanels.length && panelsFound < numberOfPanels; loop++){
					if(subPanels[loop] != null){
						panelsFound++;
						subPanels[loop].movePanel(move, effectSubPanels);
					}
				}
			}
		}
	}
	/**
	 * Will scale this panel by the given amount in world coordinates
	 * @param scale the amount that will be scaled by 
	 * @param affectSubPanels If TRUE, all of this panels subPanels size settings will be set to match this Panels size settings.
	 */
	public void scalePanel(Vector2 scale, boolean effectSubPanels){
		if(scale != null){
			getBackgroundSize().setX(Background.size.getX() * scale.getX());
			getBackgroundSize().setY(Background.size.getY() * scale.getY());
			if(effectSubPanels && subPanels != null){
				int panelsFound = 0;
				for(int loop = 0; loop < subPanels.length && panelsFound < numberOfPanels; loop++){
					if(subPanels[loop] != null){
						panelsFound++;
						subPanels[loop].movePanel(scale, effectSubPanels);
					}
				}
			}
		}
	}
	/**
	 * Will set this panel and all of its subPanels to be drawn automatically
	 */
	public void addToAutoDrawSet(){
		Background.addToAutoDrawSet();
		if(subPanels != null){
			int panelsFound = 0;
			for(int loop = 0; loop < subPanels.length && panelsFound < numberOfPanels; loop++){
				if(subPanels[loop] != null){
					panelsFound++;
					subPanels[loop].addToAutoDrawSet();
				}
			}
		}
	}
	/**
	 * Will set this panel and all of its subPanels to not be drawn automatically
	 */
	public void removeFromAutoDrawSet(){
		Background.removeFromAutoDrawSet();
		if(subPanels != null){
			int panelsFound = 0;
			for(int loop = 0; loop < subPanels.length && panelsFound < numberOfPanels; loop++){
				if(subPanels[loop] != null){
					panelsFound++;
					subPanels[loop].removeFromAutoDrawSet();
				}
			}
		}
	}
	/**
	 * Will set this panel and all of its subPanels to be not be drawn nor updated automatically
	 */
	public void destroy(){
		Background.destroy();
		if(subPanels != null){
			int panelsFound = 0;
			for(int loop = 0; loop < subPanels.length && panelsFound < numberOfPanels; loop++){
				if(subPanels[loop] != null){
					panelsFound++;
					subPanels[loop].destroy();
				}
			}
		}
	}
	/**
	 * Will Draw this panel
	 * Note This funtion is not called if you use addToAutoDrawSet();
	 */
	public void draw(){
		drawThisPanel();
		if(subPanels != null){
			int panelsFound = 0;
			for(int loop = 0; loop < subPanels.length && panelsFound < numberOfPanels; loop++){
				if(subPanels[loop] != null){
					panelsFound++;
					subPanels[loop].draw();
				}
			}
		}
	}
	/**
	 * Will draw this panel and only this panel. None of this panels subPanels will be drawn. by this function
	 */
	public void drawThisPanel(){
		if(Background.visible){
			Background.draw();
		}
	}
	/**
	 * Will make a given panel a subPanel of this panel and update 
	 * note this panel can not be in any other Panels at this point other wise the add will fail
	 * use remove panel to remove it if necessary
	 * @return If >= 0 represents the int ID the newSubPanel can be referenced by from this Panel
	 * 			If negative then the add was unsuccessful and No changes were made
	 */
	public int addPanel(Panel newSubPanel){
		if(newSubPanel != null && newSubPanel.localRoot == null){
			newSubPanel.localRoot = this;
			int retVal = addElementToSubSet(newSubPanel);
			ID[0] = retVal;
			newSubPanel.incrememtID(ID, IDLength + 1, 0);
			numberOfPanels++;
			return retVal;
		}
		return -1;
	}
	/**
	 * Will attempt to remove the given Panel and all of its sub Panels from whatever tree it is currently part of
	 * @return if TRUE: removeTarget was removed successfully, if FALSE: removeTarget was not removed and no changes were made
	 */
	public boolean removePanel(){
		if(localRoot != null){
			localRoot.removePanel(ID[1]);
			return true;
		}
		return false;
	}
	/**
	 * Will attempt to remove the given Panel and all of its sub Panels from this Tree
	 * @param removeTarget the target Panel to be removed
	 * @return if TRUE: removeTarget was removed successfully, if FALSE: removeTarget was not removed and no changes were made
	 */
	public boolean removePanel(Panel removeTarget){
		if(removeTarget != null && removeTarget.localRoot != null && removeTarget.IDLength > IDLength && subPanels != null){
			//direct Sub Child
			if(removeTarget.IDLength - IDLength == 1){
				if(removeTarget == getPanel(removeTarget.ID[1])){
					return removePanel(removeTarget.ID[1]);
				}
				else{
					return false;
				}
			}
			//Indirect Sub Child
			else if(subPanels.length > removeTarget.ID[removeTarget.IDLength - IDLength]){
				if(subPanels[removeTarget.ID[removeTarget.IDLength - IDLength]] != null){
					return subPanels[removeTarget.ID[removeTarget.IDLength - IDLength]].removePanel(removeTarget);
				}
			}
		}
		return false;
	}
	/**
	 * Will attempt to remove the given Panel and all of its sub Panels from this Tree
	 * @param the location of the target Panel relative to this Panel
	 * @return if TRUE: removeTarget was removed successfully, if FALSE: removeTarget was not removed and no changes were made
	 */
	public boolean removePanel(int targetPanelLocation){
		if(targetPanelLocation >= 0 && targetPanelLocation < subPanels.length){
			Panel target = subPanels[targetPanelLocation];
			if(target != null){
				removeElement(targetPanelLocation);
				target.localRoot = null;
				target.decrementID(target.IDLength);
				numberOfPanels--;
				return true;	
			}
		}
		return false;
	}
	/**
	 * Will attempt to find the given panel under this panel
	 * targetID the location of the Panel under this Panel relative to this Panel
	 * @return if null no Panel was found at this location other wise returns a pointer to the Panel at this location
	 */
	public Panel getPanel(int targetID){
		if(targetID >= 0 && subPanels != null && targetID < subPanels.length){
			return subPanels[targetID];
		}
		return null;
	}
	/**
	 * will update the image in use by the panel based on the current State
	 */
	protected void updateImage(){
		if(state == PanelState.IDLE){
			setImage(idleTexture);
		}
		else if(state == PanelState.SELECTED){
			setImage(selectedTexture);
		}
		else if(state == PanelState.INACTIVE){
			setImage(inactiveTexture);
		}
	}
	/**
	 * Will return the current rectangle used for the background
	 * @return the current rectangle used for the background
	 */
	protected Rectangle getBackgroundRectangle(){
		return Background;
	}
	/**
	 * Will set the Rectangle currently used for the background to the given Rectangle
	 * @param newBackGround this paramiter can not be null
	 */
	protected void setBackgroundRectangle(Rectangle newBackGround){
		if(newBackGround != null){
			Background = newBackGround;
		}
	}
	/**
	 * Will initialize subPanels if they are null
	 */
	private void initializeSubPanelList(){
		if(subPanels == null){
			subPanels = new Panel[defualtSubPanelSize];
			int panelsFound = 0;
			for(int loop = 0; loop < subPanels.length && panelsFound < numberOfPanels; loop++){
				if(subPanels[loop] != null){
					panelsFound++;
					subPanels[loop] = null;
				}
			}
		}
	}
	/**
	 * Will add the given path/ID to the front top of this Panels ID as well as the ID of all of this panels subPanels
	 * @param newUpperLevels 
	 * @param lastReleventIndex the last index in newUpperLevels that this function will care about
	 * @param firstReleventIndex the first Index in newUpperLevels that this function will care about
	 */
	private void incrememtID(int[] newUpperLevels, int lastReleventIndex, int firstReleventIndex){
		if(newUpperLevels != null && firstReleventIndex >= 0 && lastReleventIndex >= 0 && newUpperLevels.length > lastReleventIndex && newUpperLevels.length > firstReleventIndex){			
			//updating own ID
			if(ID.length <= IDLength + lastReleventIndex - firstReleventIndex + 1){
				expandID((IDLength + (lastReleventIndex - firstReleventIndex) + 2 + defualtSubPanelSize) - ID.length);
			}
			for(int loop = firstReleventIndex; loop <= lastReleventIndex; loop++){
				ID[IDLength + loop - firstReleventIndex + 1] = newUpperLevels[loop];
			}
			//incrementing Children
			IDLength = IDLength + (lastReleventIndex - firstReleventIndex);
			if(subPanels != null){
				int panelsFound = 0;
				for(int loop = 0; loop < subPanels.length && panelsFound < numberOfPanels; loop++){
					if(subPanels[loop] != null){
						panelsFound++;
						subPanels[loop].incrememtID(newUpperLevels, lastReleventIndex, firstReleventIndex);
					}
				}
			}
		}
	}
	/**
	 * Will Decrement the ID of this Panel and all of its subPanels 
	 * Note update LocalRoot
	 * @param decrementAmount the number of levels this Panel is no longer burried under
	 */
	private void decrementID(int decrementAmount){
		if(decrementAmount > 0 && IDLength > 0){
			//This is the new Root
			if(IDLength <= decrementAmount){
				decrementAmount = IDLength;
				IDLength = 0;
			}
			//This is not the new Root aww
			else{
				IDLength -= decrementAmount;
			}
			//updating subPanels
			if(subPanels != null){
				int panelsFound = 0;
				for(int loop = 0; loop < subPanels.length && panelsFound < numberOfPanels; loop++){
					if(subPanels[loop] != null){
						panelsFound++;
						subPanels[loop].decrementID(decrementAmount);
					}
				}
			}
		}
	}
	/**
	 * will expand the length of the ID array by the given amount
	 * Any existing ID elements will be remain at the current Index
	 * @param expandBy
	 */
	private void expandID(int expandBy){
		if(expandBy > 0){
			int[] newID = new int[ID.length + expandBy];
			int loop = 0;
			while(loop < ID.length){
				newID[loop] = ID[loop];
				loop++;
			}
			while(loop < newID.length){
				newID[loop] = -1;
				loop++;
			}
			ID = newID;
		}
	}
	/**
	 * Will add the given Panel to the subPanels Vector 
	 * @param newElement
	 */
	//Becuase Vector.Add is not smart enough,
	//Grows Vector like this
	//defualtSubPanelSize = 3
	//{null, null, null }
	//{new1, null, null }
	//{new1, new2, null }
	//{new1, new2, new3 } defualtSubPanelSize = 3
	//{new1, new2, new3 ,new4, null, null, null}
	private int addElementToSubSet(Panel newElement){
		if(newElement != null){
			initializeSubPanelList();
			subPanels[nextAvailableIndex] = newElement;
			int retVal = nextAvailableIndex;
			findNextSubPanelSlot();
			return retVal;
		}
		return -1;
	}
	/**
	 * Will remove the element at the given location
	 * @param ElementLocation
	 */
	//Used to make sure nextAvailableIndex is updated
	private void removeElement(int ElementLocation){
		if(ElementLocation >= 0 && ElementLocation < nextAvailableIndex && subPanels != null){
			subPanels[ElementLocation] = null;
			if(ElementLocation < nextAvailableIndex){
				nextAvailableIndex = ElementLocation;
			}
		}
	}
	/**
	 * Will increase the size of the subPanel set by the given amount
	 * @param expandBy
	 */
	private void expandSubPanelSet(int expandBy){
		if(expandBy > 0){
			Panel[] newSub = new Panel[subPanels.length + expandBy];
			int loop = 0;
			while(loop < subPanels.length){
				newSub[loop] = subPanels[loop];
				loop++;
			}
			while(loop < newSub.length){
				newSub[loop] = null;
				loop++;
			}
			subPanels = newSub;
		}
	}
	/**
	 * Will attempt to find the next available place to move a subPanel in the SubPanel array
	 * and will expand the subPanel array if nessesary
	 */
	private void findNextSubPanelSlot(){
		boolean found = false;
		for(int loop = nextAvailableIndex; loop < subPanels.length && !found; loop++){
			if(subPanels[loop] == null){
				found = true;
				nextAvailableIndex = loop;
			}
		}
		if(!found){
			nextAvailableIndex = subPanels.length;
			expandSubPanelSet(defualtSubPanelSize);
		}
	}
}