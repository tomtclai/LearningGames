using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;

/**
 * Will maintain a label which can display a string of text at a given position
 * @author Michael Letter
 */
namespace MenueSystem{
    public class Label : Panel {
	    private TextWithNewLines lableText = new TextWithNewLines();
	    /**
	     * will set the Text that appear on the lable
	     */
	    public void setText(String text){
		    if(text != null){
			    lableText.text = text;
		    }
		    else{
                lableText.text = text;
		    }
	    }
	    /**
	     * Will return the Text object employed by this label
	     * @return Text object employed by this label
	     */
	    public Text getFont(){
		    return lableText;
	    }
	    /**
	     * Returns the Text Displayed By this Label
	     * @return the String Text Displayed By this Label
	     */
	    public String getText(){
		    return lableText.text;
	    }
	    /**
	     * Will set the Test size and position to match the size of the Label
	     * Note Depending on the Width of the label and the amount of text this function may not work completely 
	     */
	    public void autoUpdateTextPositionScale(){
		    lableText.textPosition.X = (base.getBackGround().getCenterX() - (base.getBackGround().getWidth() / 2));
            lableText.textPosition.Y = (base.getBackGround().getCenterY() - (base.getBackGround().getHeight() / 3));
        }
	    /**
	     * Will return the Position of of the Text in World Space
	     * @return the Position of of the Text in World Space
	     */
	    public PointF getTextPosition(){
            return lableText.textPosition;
	    }
	    /**
	     * Will return the Position of of the Text in World Space
	     * @return the Position of of the Text in World Space
	     */
	    public LoadableFont getFontType(){
		    return lableText.targetFont;
	    }
	    /**
	     * Will set whether or not the text on the Label is visible
	     * @param visible If TRUE, this Text will be made visible. If FALSE, this Text will be made invisible
	     */
	    public void setTextVisibility(bool visible){
		    lableText.visible = visible;
	    }
	    /**
	      * Will return whether or not the Text is currenrly Visible
	     * @return if TRUE, Text is visible, if False, Text is invisible
	     */
	    public bool getTextVisibility(){
		    return lableText.visible;
	    }
	    /**
	     * Will set whether or not this Label is Visible 
	     * @param visable If TRUE, this Label will be made visible. If FALSE, this Label will be made invisible
	     * @param affectSubPanels If TRUE, all of this Label's subPanels visibility settings will be set to match this Label's visibility settings.
	     * If FALSE, only this Label's visibility setting will be affcted
	     */
	    public override void setVisibility(bool visible, bool effectSubPanels){
            setTextVisibility(visible);
		    base.setVisibility(visible, effectSubPanels);
	    }
	    /**
	     * Will scale this Button by the given amount in world coordinates
	     * @param move the amount that this Button will be moved by
	     * @param affectSubPanels If TRUE, all of this Button's subPanels position settings will be set to match this Button's position settings.
	     */
	    public override void movePanel(float Xincrement, float Yincrement, bool effectSubPanels){
            lableText.textPosition.X += Xincrement;
            lableText.textPosition.Y += Yincrement;
            base.movePanel(Xincrement, Yincrement, effectSubPanels);
	    }
	    /**
	     * Will Draw this Label
	     * Note This function is not called if you use addToAutoDrawSet();
	     */
	    public override void drawThisPanel(Camera drawLocation){
            base.drawThisPanel(drawLocation);
            lableText.paint(drawLocation);
	    }
    }
}
