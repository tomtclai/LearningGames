package MenueSystem;

import Engine.Text;

/**
 * Is Identical to Text in every way
 * Except when you put '\n' into a string It makes the rest of the String a new Line
 * @author Michael Letter
 */
//Gahh!!
public class TextWithNewLines extends Text{

	public void draw(){
	    String totalText = super.getText();
	    float startHeight = super.center.getY();
	    float lineWidth = 3f;
	    int lastNewLine = 0;
	    while(!super.getText().equals("")){
	    	int newLineIndex = totalText.indexOf('\n', lastNewLine + 1);
	    	if(newLineIndex >= 0){
	    		super.setText(totalText.substring(lastNewLine, newLineIndex));
	    		super.draw();
	    		super.center.setY(super.center.getY() - lineWidth);
	    		lastNewLine = newLineIndex;
	    	}
	    	else if(lastNewLine > 0 && lastNewLine != totalText.length()){
	    		super.setText(totalText.substring(lastNewLine, totalText.length()));
	    		super.draw();
	    		break;
	    	}
	    	else{
	    		 super.draw();
	    		 break;
	    	}
	    }
	    super.center.setY(startHeight);
	    super.setText(totalText);
	}
}
