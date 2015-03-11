using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

/**
 * Will Manage and maintain a list of buttons in addition to Panels
 * @author Michael Letter
 */
namespace MenueSystem{
    public class Menue : Panel{
        private PointF borderPadding = new PointF();

	    private bool[] menuButtons = null;
	    private int numberOfButtons = 0;
	
	
	    /**
	     * Will add a given button as member of this Menu
	     * This will resize and reposition the button to match to fit the menue.
	     * This will also call autoUpdateTextScale().
	     * This Button can be retrieved with getSubPanel()
	     * @param targetButton
	     * @return
	     */
	    public int addButton(Button targetButton){
		    int retVal = addPanel(targetButton);
		    if(retVal >= 0){
			    markButtonPosition(retVal);
			    numberOfButtons++;
		    }
		    return retVal;
	    }
	    public override bool removePanel(int targetPanelLocation){
		    bool retVal = base.removePanel(targetPanelLocation);
		    if(retVal && isSubPanelMenueButton(targetPanelLocation)){
			    unmarkButtonPosition(targetPanelLocation);
			    numberOfButtons--;
		    }
		    return retVal;
	    }
        /// <summary>
        /// Will set the amount of padding on the sides of the menue
        /// </summary>
        /// <param name="padWidth"></param>
        /// <param name="padHeight"></param>
        /// <returns></returns>
        public bool setPadding(float padWidth, float padHeight){
            return setPaddingHeight(padHeight) & setPaddingWidth(padWidth);
        }
        /// <summary>
        /// Will set the amount of padding on the top and bottom of the menue
        /// </summary>
        /// <param name="padding"></param>
        /// <returns></returns>
        public bool setPaddingHeight(float padding){
            if(!float.IsNaN(padding) && padding >= 0){
                borderPadding.Y = padding;
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will set the amount of padding on the left and right sides of the menue
        /// </summary>
        /// <param name="padding"></param>
        /// <returns></returns>
        public bool setPaddingWidth(float padding){
            if(!float.IsNaN(padding) && padding >= 0){
                borderPadding.X = padding;
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will return the amount of Padding the panel will have on the left and right sides
        /// </summary>
        /// <returns></returns>
	    public float getPaddingWidth(){
            return borderPadding.X;
        }
        /// <summary>
        /// Return the amount of padding the panel will have on the top and bottom
        /// </summary>
        /// <returns></returns>
        public float getPaddingHeight() {
            return borderPadding.Y;
        }
	    
	    /**
	     * Will initialize the menuButtons array to defualtSubPanelSize
	     */
	    private void initializeMenuButtons(int baseSize){
		    if(menuButtons == null){
			    if(baseSize > 0){
				    menuButtons = new bool[baseSize + defualtSubPanelSize];
			    }
			    else{
				    menuButtons = new bool[defualtSubPanelSize];
			    }
			    for(int loop = 0; loop < menuButtons.Length; loop++){
				    menuButtons[loop] = false;
			    }
		    }
	    }
	    /**
	     * Will Reposition and Rescale all Buttons added using addButton() to fit within this Menue
	     * &param vertical if TRUE, Buttons will be placed in a vertical line. if FALSE, buttons wil be placed in a horizontal line 
	     */
	    public void setButtonSizesPositions(bool vertical){
		    if(menuButtons != null){
			    int found = 0;
			    for(int loop = 0; loop < menuButtons.Length && found < numberOfButtons; loop++){
				    if(menuButtons[loop]){
					    repositionAndResizeButton(loop, found, vertical);
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
	    protected void repositionAndResizeButton(int buttonPosition, int ButtonNumber, bool verticalMenue){
		    if(isSubPanelMenueButton(buttonPosition)){
			    Button target = (Button)getPanel(buttonPosition);
			    //Extend Down
			    if(verticalMenue){ 
				    //Size
                    target.getBackGround().setWidth(getBackGround().getWidth() - (borderPadding.X * 2));
                    target.getBackGround().setHeight((getBackGround().getHeight() - (borderPadding.Y * 2)) / numberOfButtons);
				    //Position
                    target.getBackGround().setCenterX(getBackGround().getCenterX());
                    target.getBackGround().setCenterY((((-getBackGround().getHeight() / 2) - borderPadding.Y + (target.getBackGround().getHeight() / 2)) + getBackGround().getCenterY()) + (ButtonNumber * target.getBackGround().getHeight()));
				    //FirstCenter - (ButtonNumber * incrementAmount)
				    //incrementAmount = buttonSize
				    //totalDistance = MenueSize - padding 
				    //FirstCenter = (totalDistance/2) + MenuCenter + buttonSize/2
			    }
			    //Extend Right
			    else{
				    //Size
                    target.getBackGround().setWidth((getBackGround().getWidth() - (borderPadding.X * 2)) / numberOfButtons);
                    target.getBackGround().setHeight(getBackGround().getHeight() - (borderPadding.Y * 2));
				    //Position
                    target.getBackGround().setCenterX((((-getBackGround().getWidth() / 2) - borderPadding.X + (target.getBackGround().getWidth() / 2)) + getBackGround().getCenterX()) + (ButtonNumber * target.getBackGround().getWidth()));
                    target.getBackGround().setCenterY(getBackGround().getCenterY());
			    }
			    target.autoUpdateTextPositionScale();
		    }
	    }
	    /**
	     * Will return true if the subPanl at the given position is indeed a button in this menu
	     * @param buttonPosition subPanl in question
	     * @return if TRUE, if the subPanl at the given position is indeed a button in this menu. otherwise FALSE
	     */
	    public bool isSubPanelMenueButton(int buttonPosition){
		    if(menuButtons != null && buttonPosition < menuButtons.Length && menuButtons[buttonPosition]){
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
			    else if(menuButtons.Length <= buttonPosition){
				    expandMenueButtons((buttonPosition - menuButtons.Length + 1) + defualtSubPanelSize);
			    }
			    menuButtons[buttonPosition] = true;
		    }
	    }
	    /**
	     * Will unmark a button in the menuButtons array
	     * @param buttonPosition
	     */
	    private void unmarkButtonPosition(int buttonPosition){
		    if(menuButtons != null && buttonPosition >= 0 && buttonPosition < menuButtons.Length){
			    menuButtons[buttonPosition] = false;
		    }
	    }
	    /**
	     * Will expand the menuButtons array by the given amount
	     * @param expandBy
	     */
	    private void expandMenueButtons(int expandBy){
		    if(expandBy > 0){
			    bool[] newMenueButtons = new bool[menuButtons.Length + expandBy];
			    int loop = 0;
			    while(loop < menuButtons.Length){
				    newMenueButtons[loop] = menuButtons[loop];
				    loop++;
			    }
			    while(loop < newMenueButtons.Length){
				    newMenueButtons[loop] = false;
				    loop++;
			    }
			    menuButtons = newMenueButtons;
		    }
	    }
    }
}