using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;

/**
 * Maintains a panel which draws maintains a simple background image and can also contain a series of sub panels with this one
 * Note this panel does not force any of the sub panels to actually reside inside this panel. 
 * It is there resposiblility to stay inside this panels boundaries if they choose too 
 * @author Michael Letter
 */
namespace MenueSystem{
    public class Panel : Drawable{
        private Sprite selectedTexture = null;
        private Sprite idleTexture = null;
        private Sprite inactiveTexture = null;

	    private DrawRectangle Background = null;

	    /** used to find this panel if it is a subPanel of another panel 
	     * A -1 indicates that the Panel exists on the level above
	     * Note ID[0] is used for communicating with other panels and is not part of the ID
	     */												
        /// <summary>Levels by array index {Unused , this Panel, subRoot, Root, ...} </summary>
	    private int[] ID = {-1,-1,-1,-1};

        /// <summary>stores the number of indices of the ID that is in use. If 0 then none are in use and this is a root Panel </summary>
        private int IDLength = 0;
        /// <summary>References the Panel that is Directly above this one and can be used to access the rest of the system </summary>
	    private Panel localRoot = null;
        /// <summary>Stores all of this panels subPanels </summary>
	    private Panel[] subPanels = null;
        /// <summary>Stores the next position available in subPanels. Note, does not Garuntee that the index exists yet </summary>
	    private int nextAvailableIndex = 0;
        /// <summary>Stores the number of Panels that are direct subPanels of this Panel </summary>
	    private int numberOfPanels = 0;
        /// <summary>Stores the number of Panels that are direct subPanels of this Panel </summary>
	    protected int defualtSubPanelSize = 3;
	
	    

        /// <summary>
	    /// * Used to mark what operating mode this panel is in
	    /// * IDLE: all Sub panels are updated normally.
	    /// * SELECTED: all Sub panels are updated normally.
	    ///* INACTIVE: Sub Panels are not updated.
	    /// </summary>
	    public enum PanelState {IDLE, SELECTED, INACTIVE};
	
	    private PanelState state = PanelState.IDLE;
	    private bool stateUnset = true;
	
	    public Panel(){
		    Background = new DrawRectangle();
	    }
	
	    /// <summary>
        /// Will automatically update the state of this panel and all contained panels
	    /// </summary>
	    /// <param name="mouse"></param>
	    public virtual void autoUpdateState(Mouse mouse){
		    if(state != PanelState.INACTIVE){
			    autoUpdateSubPanels(mouse);
		    }
	    }
	    /// <summary>
	    /// Will auto update this panels subPanels.
        /// Note this function is automatically called by Panel.autoUpdateState(); if the Panel is not INACTIVE
	    /// </summary>
	    /// <param name="mouse"></param>
        protected void autoUpdateSubPanels(Mouse mouse) {
		    if(subPanels != null){
			    int panelsFound = 0;
			    for(int loop = 0; loop < subPanels.Length && panelsFound < numberOfPanels; loop++){
				    if(subPanels[loop] != null){
					    panelsFound++;
					    subPanels[loop].autoUpdateState(mouse);
				    }
			    }
		    }
	    }
	    /// <summary>
        /// will set the image used by the panel when the panel is idle
        /// Note Requires the image to be already loaded
	    /// </summary>
        /// <param name="image">the location of the image to be used by the panel when the panel is idle</param>
	    public virtual void setIdleImage(Sprite image){
		    if(image != null && image.isLoaded()){
			    idleTexture = image;
			    updateImage();
		    }
	    }
	    /// <summary>
        /// will return the image used by the panel when the panel is idle
	    /// </summary>
        /// <returns>the image Location used by the panel when the panel is idle</returns>
	    public virtual Sprite getIdleImage(){
		    return idleTexture;
	    }
	    /// <summary>
        /// will set the image used by the panel when the panel is idle
	    /// </summary>
        /// <param name="image">the image to be used by the panel when the panel is idle</param>
	    public virtual void setSelectImage(Sprite image){
		    if(image != null && image.isLoaded()){
                selectedTexture = image;
			    updateImage();
		    }
	    }
        /// <summary>
        /// will return the image used by the panel when the panel is idle
        /// </summary>
        /// <returns>the image used by the panel when the panel is idle</returns>
	    public virtual Sprite getSelectImage(){
		    return selectedTexture;
	    }
        /// <summary>
        /// Will set the image used by the panel when the panel is idle
        /// Requires the image to be already loaded
        /// </summary>
        /// <param name="image"></param>
	    public virtual void setInactiveImage(Sprite image){
		    if(image != null && !image.isLoaded()){
                inactiveTexture = image;
			    updateImage();
		    }
	    }
	    /// <summary>
	    /// Returns the Image used when this panel is inactive
	    /// </summary>
	    /// <returns></returns>
	    public virtual Sprite getInactiveImage(){
		    return inactiveTexture;
	    }
        /// <summary>
        /// will set the currentState of the Panel to the given PanelState
        /// * IDLE: all Sub panels are updated normally.
        /// * SELECTED: all Sub panels are updated normally.
        /// * INACTIVE: Sub Panels are not updated.
        /// </summary>
        /// <param name="newState">the desired State</param>
        /// <param name="setSubPanels">if True all SupPanels of this Panel will also be set to the given state in addition to this Panel. otherwise only this Panel are affected</param>
	    public virtual void setState(PanelState newState, bool setSubPanels){
		    //if(state != newState || stateUnset){
			    stateUnset = false;
			    state = newState;
			    updateImage();
			    if(setSubPanels && subPanels != null){
				    int panelsFound = 0;
				    for(int loop = 0; loop < subPanels.Length && panelsFound < numberOfPanels; loop++){
					    if(subPanels[loop] != null){
						    panelsFound++;
						    subPanels[loop].setState(newState, setSubPanels);
					    }
				    }
			    }
		    //}
	    }
	    /// <summary>
	    /// * Will return the current state of the panel.
	    /// * IDLE: all Sub panels are updated normally.
	    /// * SELECTED: all Sub panels are updated normally.
	    /// * INACTIVE: Sub Panels are not updated.
	    /// </summary>
	    /// <returns></returns>
	    public virtual PanelState getState(){
		    return state;
	    }
	    /**
	     * Will set the image that is currently be used for the panels draw
	     * @param image the location of the desired image
	     */
	    public virtual void setImage(Sprite image){
		    if(image != null && image.isLoaded()){
                Background.setSprite(image);
		    }
	    }
        /// <summary>
        /// Will Return the Background Rectangle employed by this panel
        /// </summary>
        /// <returns></returns>
        public DrawRectangle getBackGround() {
            return Background;
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
        /// <summary>
        /// will return the number of SubPanels that are directly contained this one
        /// </summary>
        /// <returns></returns>
	    public int getNumberOfSubPanels(){
		    return numberOfPanels;
	    }
	    /// <summary>
        /// Will set whether or not this Panel is Visible 
	    /// </summary>
        /// <param name="visible">If TRUE, this panel will be made visible. If FALSE, this panel will be made invisible</param>
	    /// <param name="effectSubPanels">If TRUE, all of this panels subPanels visibility settings will be set to match 
        /// this Panels visibility settings. If FALSE, only this panels visibility setting will be affcted</param>
	    public virtual void setVisibility(bool visible, bool effectSubPanels){
            Background.visible = visible;
		    if(effectSubPanels && subPanels != null){
			    int panelsFound = 0;
			    for(int loop = 0; loop < subPanels.Length && panelsFound < numberOfPanels; loop++){
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
	    public virtual void movePanel(float Xincrement, float Yincrement, bool effectSubPanels){
            Background.setCenterX(Background.getCenterX() + Xincrement);
            Background.setCenterY(Background.getCenterY() + Yincrement);
			if(effectSubPanels && subPanels != null){
				int panelsFound = 0;
				for(int loop = 0; loop < subPanels.Length && panelsFound < numberOfPanels; loop++){
					if(subPanels[loop] != null){
						panelsFound++;
                        subPanels[loop].movePanel(Xincrement, Yincrement, effectSubPanels);
					}
			    }
		    }
	    }
	    /**
	     * Will scale this panel by the given amount in world coordinates
	     * @param scale the amount that will be scaled by 
	     * @param affectSubPanels If TRUE, all of this panels subPanels size settings will be set to match this Panels size settings.
	     */
	    public virtual void scalePanel(float XScale, float YScale, bool effectSubPanels){
            Background.setWidth(Background.getWidth() * XScale);
            Background.setHeight(Background.getHeight() * YScale);
			if(effectSubPanels && subPanels != null){
				int panelsFound = 0;
				for(int loop = 0; loop < subPanels.Length && panelsFound < numberOfPanels; loop++){
					if(subPanels[loop] != null){
						panelsFound++;
                        subPanels[loop].movePanel(XScale, YScale, effectSubPanels);
					}
				}
		    }
	    }
	    /**
	     * Will Draw this panel
	     * Note This funtion is not called if you use addToAutoDrawSet();
	     */
        public override void paint(Camera drawLocation) {
            drawThisPanel(drawLocation);
		    if(subPanels != null){
			    int panelsFound = 0;
			    for(int loop = 0; loop < subPanels.Length && panelsFound < numberOfPanels; loop++){
				    if(subPanels[loop] != null){
					    panelsFound++;
                        subPanels[loop].paint(drawLocation);
				    }
			    }
		    }
	    }
	    /**
	     * Will draw this panel and only this panel. None of this panels subPanels will be drawn. by this function
	     */
	    public virtual void drawThisPanel(Camera drawLocation){
			Background.paint(drawLocation);
	    }
	    /**
	     * Will make a given panel a subPanel of this panel and update 
	     * note this panel can not be in any other Panels at this point other wise the add will fail
	     * use remove panel to remove it if necessary
	     * @return If >= 0 represents the int ID the newSubPanel can be referenced by from this Panel
	     * 			If negative then the add was unsuccessful and No changes were made
	     */
	    public virtual int addPanel(Panel newSubPanel){
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
	    public virtual bool removePanel(){
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
	    public virtual bool removePanel(Panel removeTarget){
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
			    else if(subPanels.Length > removeTarget.ID[removeTarget.IDLength - IDLength]){
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
	    public virtual bool removePanel(int targetPanelLocation){
		    if(targetPanelLocation >= 0 && targetPanelLocation < subPanels.Length){
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
		    if(targetID >= 0 && subPanels != null && targetID < subPanels.Length){
			    return subPanels[targetID];
		    }
		    return null;
	    }
	    /**
	     * will update the image in use by the panel based on the current State
	     */
	    protected virtual void updateImage(){
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
	     * Will set the Rectangle currently used for the background to the given Rectangle
	     * @param newBackGround this paramiter can not be null
	     */
	    protected void setBackgroundRectangle(DrawRectangle newBackGround){
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
			    for(int loop = 0; loop < subPanels.Length && panelsFound < numberOfPanels; loop++){
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
		    if(newUpperLevels != null && firstReleventIndex >= 0 && lastReleventIndex >= 0 && newUpperLevels.Length > lastReleventIndex && newUpperLevels.Length > firstReleventIndex){			
			    //updating own ID
			    if(ID.Length <= IDLength + lastReleventIndex - firstReleventIndex + 1){
				    expandID((IDLength + (lastReleventIndex - firstReleventIndex) + 2 + defualtSubPanelSize) - ID.Length);
			    }
			    for(int loop = firstReleventIndex; loop <= lastReleventIndex; loop++){
				    ID[IDLength + loop - firstReleventIndex + 1] = newUpperLevels[loop];
			    }
			    //incrementing Children
			    IDLength = IDLength + (lastReleventIndex - firstReleventIndex);
			    if(subPanels != null){
				    int panelsFound = 0;
				    for(int loop = 0; loop < subPanels.Length && panelsFound < numberOfPanels; loop++){
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
				    for(int loop = 0; loop < subPanels.Length && panelsFound < numberOfPanels; loop++){
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
			    int[] newID = new int[ID.Length + expandBy];
			    int loop = 0;
			    while(loop < ID.Length){
				    newID[loop] = ID[loop];
				    loop++;
			    }
			    while(loop < newID.Length){
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
			    Panel[] newSub = new Panel[subPanels.Length + expandBy];
			    int loop = 0;
			    while(loop < subPanels.Length){
				    newSub[loop] = subPanels[loop];
				    loop++;
			    }
			    while(loop < newSub.Length){
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
		    bool found = false;
		    for(int loop = nextAvailableIndex; loop < subPanels.Length && !found; loop++){
			    if(subPanels[loop] == null){
				    found = true;
				    nextAvailableIndex = loop;
			    }
		    }
		    if(!found){
			    nextAvailableIndex = subPanels.Length;
			    expandSubPanelSet(defualtSubPanelSize);
		    }
	    }
    }
}