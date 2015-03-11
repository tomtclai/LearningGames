using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;


/**
 * Can be used as a Label is used in MenueSystem 
 * However also Manages a Segmented Bar in addition
 * @author Michael Letter
 *
 */
namespace MenueSystem{
    public class LabeledBar : Label{
	    /**
	     * Stores the Segmented Bar Maintained by this class
	     */
	    //2 good friends walk into a bar
	    //You would think one of them would have saw it coming!! lol
	    private SegmentedBar localBar = new SegmentedBar();
	
	    /**
	     * Initializes local variables to default values
	     */
	    public LabeledBar() : base(){
		    localBar.setMaxSegments(1);
		    localBar.setFilledSegments(1);
		    localBar.setSize(1f,1f);
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
	    public bool setSegmentedBar(SegmentedBar newBar){
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
	    public void setVisibility(bool visible, bool effectSubPanels){
		    localBar.visible = visible;
		    base.setVisibility(visible, effectSubPanels);
	    }
	    /**
	     * Will move this LabeledBar by the given amount in world coordinates
	     * @param move the amount that this LabeledBar will be moved by
	     * @param affectSubPanels If TRUE, all of this LabeledBar's subPanels position settings will be set to match this Button's position settings.
	     */
	    public override void movePanel(float Xincrement, float Yincrement, bool effectSubPanels){
		    if(!float.IsNaN(Xincrement) && !float.IsNaN(Yincrement)){
			    localBar.setCenter(localBar.getCenterX() + Xincrement, localBar.getCenterY() + Yincrement);
			    base.movePanel(Xincrement, Yincrement, effectSubPanels);
		    }
	    }
	    /**
	     * Will scale this LabeledBar by the given amount in world coordinates
	     * @param scale the amount that will be scaled by 
	     * @param affectSubPanels If TRUE, all of this panels subPanels size settings will be set to match this LabeledBar size settings.
	     */
	    public virtual void scalePanel(float XScale, float YScale, bool effectSubPanels){
		    if(!float.IsNaN(XScale) && !float.IsNaN(YScale)){
			    localBar.setWidth(localBar.getWidth() * XScale);
			    localBar.setHeight(localBar.getHeight() * YScale);
			    base.scalePanel(XScale, YScale, effectSubPanels);
		    }
	    }
	    /**
	     * Will Draw this LabeledBar
	     * Note This function is not called if you use addToAutoDrawSet();
	     */
	    public override void drawThisPanel(Camera drawLocation){
		    base.drawThisPanel(drawLocation);
		    if(localBar.visible){
                localBar.paint(drawLocation);
		    }
	    }
    }
}
