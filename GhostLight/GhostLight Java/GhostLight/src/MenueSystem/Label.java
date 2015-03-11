package MenueSystem;

import java.awt.Color;

import Engine.Text;
import Engine.Vector2;

/**
 * Will maintain a label which can display a string of text at a given position
 * @author Michael Letter
 */
public class Label extends Panel {
	private TextWithNewLines lableText = new TextWithNewLines();
	
	public Label(){
		super();
		lableText.removeFromAutoDrawSet();
		lableText.setFontName("Serif");
		lableText.setBackColor(Color.BLACK);
		lableText.setFrontColor(Color.WHITE);
		lableText.setText("");
	}
	/**
	 * will set the Text that appear on the lable
	 */
	public void setText(String text){
		if(text != null){
			lableText.setText(text);
		}
		else{
			lableText.setText("");
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
		return lableText.getText();
	}
	/**
	 * Will set the Test size and position to match the size of the Label
	 * Note Depending on the Width of the label and the amount of text this function may not work completely 
	 */
	public void autoUpdateTextPositionScale(){
		lableText.size.set(super.getBackgroundSize());
		lableText.center.setX(super.getBackgroundCenter().getX() - (super.getBackgroundSize().getX()/3) - 0.2f);
		lableText.center.setY(super.getBackgroundCenter().getY());
	}
	/**
	 * Will return the Position of of the Text in World Space
	 * @return the Position of of the Text in World Space
	 */
	public Vector2 getTextPosition(){
		return lableText.center;
	}
	/**
	 * Will return the Position of of the Text in World Space
	 * @return the Position of of the Text in World Space
	 */
	public Vector2 getTextSize(){
		return lableText.size;
	}
	/**
	 * Will set whether or not the text on the Label is visible
	 * @param visible If TRUE, this Text will be made visible. If FALSE, this Text will be made invisible
	 */
	public void getTextVisibility(boolean visible){
		lableText.visible = visible;
	}
	/**
	  * Will return whether or not the Text is currenrly Visible
	 * @return if TRUE, Text is visible, if False, Text is invisible
	 */
	public boolean getTextVisibility(){
		return lableText.visible;
	}
	/**
	 * Will set whether or not this Label is Visible 
	 * @param visable If TRUE, this Label will be made visible. If FALSE, this Label will be made invisible
	 * @param affectSubPanels If TRUE, all of this Label's subPanels visibility settings will be set to match this Label's visibility settings.
	 * If FALSE, only this Label's visibility setting will be affcted
	 */
	public void setVisibility(boolean visible, boolean effectSubPanels){
		getTextVisibility(visible);
		super.setVisibility(visible, effectSubPanels);
	}
	/**
	 * Will scale this Button by the given amount in world coordinates
	 * @param move the amount that this Button will be moved by
	 * @param affectSubPanels If TRUE, all of this Button's subPanels position settings will be set to match this Button's position settings.
	 */
	public void movePanel(Vector2 move, boolean effectSubPanels){
		if(move != null){
			lableText.center.add(move);
			super.movePanel(move, effectSubPanels);
		}
	}
	/**
	 * Will scale this panel by the given amount in world coordinates
	 * @param scale the amount that will be scaled by 
	 * @param affectSubPanels If TRUE, all of this panels subPanels size settings will be set to match this Panels size settings.
	 */
	public void scalePanel(Vector2 scale, boolean effectSubPanels){
		if(scale != null){
			lableText.size.setX(lableText.size.getX() * scale.getX());
			lableText.size.setY(lableText.size.getY() * scale.getY());
			super.scalePanel(scale, effectSubPanels);
		}
	}
	/**
	 * Will set this Label and all of its subPanels to be drawn automatically
	 */
	public void addToAutoDrawSet(){
		lableText.addToAutoDrawSet();
		super.addToAutoDrawSet();
	}
	/**
	 * Will set this Label and all of its subPanels to not be drawn automatically
	 */
	public void removeFromAutoDrawSet(){
		lableText.removeFromAutoDrawSet();
		super.removeFromAutoDrawSet();
	}
	/**
	 * Will set this Label and all of its subPanels to be not be drawn nor updated automatically
	 */
	public void destroy(){
		lableText.destroy();
		super.destroy();
	}
	/**
	 * Will Draw this Label
	 * Note This function is not called if you use addToAutoDrawSet();
	 */
	public void drawThisPanel(){
		super.drawThisPanel();
		if(lableText.visible){
			lableText.draw();
		}
	}
}
