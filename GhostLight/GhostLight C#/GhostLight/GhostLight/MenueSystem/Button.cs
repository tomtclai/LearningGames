using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
/**
 * Maintains a multiState button that uses the size of its background rectangle to determine if the mouse is placed over it 
 * @author Michael Letter
 */
namespace MenueSystem{
    public class Button : Label{
	
	    private Sprite ButtonDownTexture = null;
        private Sprite MouseOverSelectedTexture = null;
        private Sprite MouseOverIdleTexture = null;
	
	    public enum transitionState {DOWN, UP, MOUSEOVER};
	
	    protected transitionState subState = transitionState.UP;
	
	    /** Determines if when clicked the button alternates between a selected and idle state */
	    public bool selectable = true;
	
	    /**
	     * Will AutoUpdate the State if this Button and is subPanels based on the psotion of the mouse
	     * Note: Subpanels will be made invisible if the button is idle or inactive and when only be visible of the button is selected
	     * Unless selectable == false; Then subPanels will be updated the same way they are in Panel
	     * Otherwise 
	     * @param mouse
	     */
	    public override void autoUpdateState(Mouse mouse){
		    //inactive Buttons do not update
		    if(getState() != PanelState.INACTIVE){
			    if( mouse != null && isPointOver(mouse.getWorldPosition().X, mouse.getWorldPosition().Y)){
				    //MouseClick
				    if(mouse.leftMouseDown || mouse.rightMouseDown || mouse.middleMouseDown){
					    subState = transitionState.DOWN;
				    }
				    //MouseOver
				    else{
					    //Alternate state?
					    if(subState == transitionState.DOWN){
						    if(getState() == PanelState.IDLE && selectable){
							    setState(PanelState.SELECTED, true);
						    }
						    else{
							    setState(PanelState.IDLE, true);
							
						    }
					    }
					    subState = transitionState.MOUSEOVER;
				    }
			    }
			    //Idle
			    else{
				    //Alternate State?
				    if(subState == transitionState.DOWN){
					    if(getState() == PanelState.IDLE && selectable){
						    setState(PanelState.SELECTED, true);
					    }
					    else{
						    setState(PanelState.IDLE, true);
					    }
				    }
				    subState = transitionState.UP;
			    }
			    if(getState() == PanelState.SELECTED || !selectable){
				    autoUpdateSubPanels(mouse);
			    }
		    }
		    updateImage();
	    }
	    /**
	     * Will Set the current State of the Given Button:
	     * if button has been marked as selectable(Defualt):
	     * IDLE: All SupPanels are invisible, INACTIVE, and are not Updated.
	     * SELECTED: All SupPanels are visible, IDLE, and updated Normally.
	     * INACTIVE: All SupPanels are invisible and INACTIVE.
	     * if button has not been marked as selectable:
	     * IDLE: SupPanels States are updated Normally. 
	     * SELECTED: SupPanels States are updated Normally.  
	     * INACTIVE: All SupPanels are invisible INACTIVE, and not Updated. 
	     */
	    public override void setState(PanelState newState, bool setSubPanels){
		    //Idle
		    if(newState == PanelState.IDLE){
			    if(selectable){
				    base.setState(PanelState.INACTIVE, setSubPanels);
				    setVisibility(false,setSubPanels);
				    setVisibility(true,false);
			    }
			    base.setState(PanelState.IDLE, false);
		    }
		    //Selected
		    else if(newState == PanelState.SELECTED){
			    if(selectable){
				    base.setState(PanelState.IDLE, setSubPanels);
				    setVisibility(true,setSubPanels);
			    }
			    base.setState(PanelState.SELECTED, false);
		    }
		    //Inactive
		    else if(newState == PanelState.INACTIVE){
			    base.setState(PanelState.INACTIVE, setSubPanels);
			    setVisibility(false,setSubPanels);
			    setVisibility(true,false);
		    }
	    }
	    /**
	     * Will return whether or not the given world coordinants describe a point over this button
	     * @param pointX
	     * @param pointY
	     * @return
	     */
	    public bool isPointOver(float pointX, float pointY){
            return base.getBackGround().containsPoint(new PointF(pointX, pointY));
	    }
	    /**
	     * Returns the substate of the button, this includes whether the mouse is hovering over the button, whether the button is being held down, ir if the button is simply idle.
	     * Note, these states do not update until autoUpdateState() is called
	     * @return
	     */
	    public transitionState getSubState(){
		    return subState;
	    }
	    /**
	     * Will set this buttons current SubState to the given state
	     * @param newState
	     */
	    public void setSubState(transitionState newState){
		    subState = newState;
		    updateImage();
	    }
	    /**
	     * Will set the images this button will alternate between when using autoUpdateState and setState
	     * and will preload them if they are not already preloaded
	     * @param mouseOverSelectedImage used when the mouse is hovering over the button and the button is selected
	     * @param mouseOverIdleImage used when the mouse is hovering over the button and the button is idle, selected
	     * @param buttonDownImage used when the button is being clicked
	     */
	
	    /**
	    * Will set the image this button will alternate between when using autoUpdateState and setState
	    * and will preload them if they are not already preloaded
	    * @param mouseOverSelectedImage used when the mouse is hovering over the button and the button is selected
	    */
	    public void setMouseOverSelectImage(Sprite image){
		    if(image != null && image.isLoaded()){
                MouseOverSelectedTexture = image;
			    updateImage();
		    }
	    }
	    /**
	     * Will return the Image used when the Button is Selected and the mouse is hovering over it
	     * @return the Image used when the Button is Selected and the mouse is hovering over it
	     */
	    public Sprite getMouseOverSelectImage(){
		    return MouseOverSelectedTexture;
	    }
	    /**
	     * Will set the image this button will alternate between when using autoUpdateState and setState
	     * and will preload them if they are not already preloaded
	     * @param mouseOverIdleImage used when the mouse is hovering over the button and the button is idle, selected
	     */
	    public void setMouseOverIdleImage(Sprite image){
		    if(image != null && image.isLoaded()){
                MouseOverIdleTexture = image;
			    updateImage();
		    }
	    }
	    /**
	     * Will return the Image used when the Button is Idle and the mouse is hovering over it
	     * @return the Image used when the Button is Idle and the mouse is hovering over it
	     */
	    public Sprite getMouseOverIdleImage(){
		    return MouseOverIdleTexture;
	    }
	    /**
	     * Will set the image this button will alternate between when using autoUpdateState and setState
	     * and will preload them if they are not already preloaded
	     * @param buttonDownImage used when the button is being clicked
	     */
	    public void setButtonDownImage(Sprite image){
		    if(image != null && image.isLoaded()){
                ButtonDownTexture = image;
			    updateImage();
		    }
	    }
	    /**
	     * Will return the Image used when the Button is being held Down
	     * @return the Image used when when the Button is being held Down
	     */
	    public Sprite getMouseDownImage(){
		    return ButtonDownTexture;
	    }
	    /**
	     * Will set the buttons Background image to the appropriate image based on the current state and transition state
	     */
	    protected void updateImage(){
		    //button inactive
		    if(getState() == PanelState.INACTIVE){
			    setImage(getInactiveImage());
		    }
		    //Button Down
		    else if(subState == transitionState.DOWN){
			    setImage(ButtonDownTexture);
		    }
		    //Button Selected
		    else if(getState() == PanelState.SELECTED){
			    //MouseOver Select
			    if(subState == transitionState.MOUSEOVER){
			        setImage(MouseOverSelectedTexture);
			    }
			    //Select Idle
			    else {
				    setImage(getSelectImage());
			    }
		    }
		    //Idle
		    else {
			    //MouseOver Idle
			    if(subState == transitionState.MOUSEOVER){
				    setImage(MouseOverIdleTexture);
			    }
			    //Idle Idle
			    else{
				    setImage(getIdleImage());
			    }
		    }
	    }
    }
}