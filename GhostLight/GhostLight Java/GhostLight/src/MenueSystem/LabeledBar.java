package MenueSystem;

import Engine.Vector2;


/**
 * Can be used as a Label is used in MenueSystem 
 * However also Manages a Segmented Bar in addition
 * @author Michael Letter
 *
 */
public class LabeledBar extends Label{
	/**
	 * Stores the Segmented Bar Maintained by this class
	 */
	//2 good friends walk into a bar
	//You would think one of them would have saw it coming!! lol
	private SegmentedBar localBar = new SegmentedBar();
	
	/**
	 * Initializes local variables to default values
	 */
	public LabeledBar(){
		super();
		localBar.removeFromAutoDrawSet();
		localBar.setMaxSegments(1);
		localBar.setFilledSegments(1);
		localBar.size.set(1f,1f);
		localBar.visible = true;
	}
	/**
	 * Will return the activeSegmentedBar
	 * @return
	 */
	public SegmentedBar getSegmentedBar(){
		return localBar;
	}
	/**
	 * Will through away the existing SegmentedBar to use the given one 
	 * @param newBar the new Segmented bar to be managed by this LabeledBar, Note the Old bar is not destroyed
	 * at the conclusion of this function. So do remember to use getSegmentedBar() to clean it up if necessary
	 * @return If TRUE, the old SegmentedBar was successfully replaced. otherwise if FALSE, no changes were made
	 */
	public boolean setSegmentedBar(SegmentedBar newBar){
		if(newBar != null){
			localBar = newBar;
		}
		return false;
	}
	/**
	 * Will set whether or not this LabeledBar is Visible 
	 * @param visable If TRUE, this LabeledBar will be made visible. If FALSE, this LabeledBar will be made invisible
	 * @param affectSubPanels If TRUE, all of this LabeledBar's subPanels visibility settings will be set to match this LabeledBar's visibility settings.
	 * If FALSE, only this LabeledBar's visibility setting will be affcted
	 */
	public void setVisibility(boolean visible, boolean effectSubPanels){
		localBar.visible = visible;
		super.setVisibility(visible, effectSubPanels);
	}
	/**
	 * Will move this LabeledBar by the given amount in world coordinates
	 * @param move the amount that this LabeledBar will be moved by
	 * @param affectSubPanels If TRUE, all of this LabeledBar's subPanels position settings will be set to match this Button's position settings.
	 */
	public void movePanel(Vector2 move, boolean effectSubPanels){
		if(move != null){
			localBar.center.add(move);
			super.movePanel(move, effectSubPanels);
		}
	}
	/**
	 * Will scale this LabeledBar by the given amount in world coordinates
	 * @param scale the amount that will be scaled by 
	 * @param affectSubPanels If TRUE, all of this panels subPanels size settings will be set to match this LabeledBar size settings.
	 */
	public void scalePanel(Vector2 scale, boolean effectSubPanels){
		if(scale != null){
			localBar.size.setX(localBar.size.getX() * scale.getX());
			localBar.size.setY(localBar.size.getY() * scale.getY());
			super.scalePanel(scale, effectSubPanels);
		}
	}
	/**
	 * Will set this LabeledBar and all of its subPanels to be drawn automatically
	 */
	public void addToAutoDrawSet(){
		localBar.addToAutoDrawSet();
		super.addToAutoDrawSet();
	}
	/**
	 * Will set this LabeledBar and all of its subPanels to not be drawn automatically
	 */
	public void removeFromAutoDrawSet(){
		localBar.removeFromAutoDrawSet();
		super.removeFromAutoDrawSet();
	}
	/**
	 * Will set this Label and all of its subPanels to be not be drawn nor updated automatically
	 */
	public void destroy(){
		localBar.destroy();
		super.destroy();
	}
	/**
	 * Will Draw this LabeledBar
	 * Note This function is not called if you use addToAutoDrawSet();
	 */
	public void drawThisPanel(){
		super.drawThisPanel();
		if(localBar.visible){
			localBar.draw();
		}
	}
}
