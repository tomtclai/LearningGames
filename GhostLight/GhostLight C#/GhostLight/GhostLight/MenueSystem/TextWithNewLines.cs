using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;

/**
 * Is Identical to Text in every way
 * Except when you put '\n' into a string It makes the rest of the String a new Line
 * @author Michael Letter
 */
namespace MenueSystem{
    public class TextWithNewLines : Text{
        public override void paint(Camera drawLocation) {
            String[] totalText = null;
            string oldText = "";
            if(visible && drawLocation != null && text != null && !text.Equals("")){
                if(!oldText.Equals(base.text) || totalText == null){
                    totalText = base.text.Split(new Char[] { '\n' });
                    oldText = text;
                }
	            float startHeight = base.textPosition.Y;
	            float lineWidth = 3f;
                updateTransform();

                for(int loop = 0; loop < totalText.Length; loop++){
                    textPosition.Y = textPosition.Y + (lineWidth * loop);
                    if (targetFont != null && targetFont.isLoaded()){
                        targetFont.paintText(drawLocation, totalText[loop], textPosition, textColor, textTransform);
                    }
                }
                textPosition.Y = startHeight;
            }
	    }
    }
}
