//Author: Michael Letter

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Drawing.Drawing2D;

namespace CustomWindower.CoreEngine {
    /// <summary>
    /// Manages a single Font Object So it can be instanced, loaded and unladed using the Resource Lubrary
    /// </summary>
    public class LoadableFont : LibraryResource{

        private static string DEFUALT_FONT_NAME = "Arial";
        private static float DEFUALT_FONT_SIZE = 20f;
        private static FontStyle DEFUALT_FONT_STYLE = FontStyle.Regular;

        /// <summary>
        /// Returns The ID that would be used by the LoadableFont created with the following properties 
        /// </summary>
        /// <param name="fontName">The Name of the Font that will be used, "Arial", "Times New Roman" Note The Fonts must already be installed on the computer to be put into use</param>
        /// <param name="size">The Size in points the Font will be, note Must be greater than zero</param>
        /// <param name="fontStyle">The Style, Bold, Italic,</param>
        /// <returns>ID that would be used by the LoadableFont created with the following properties </returns>
        public static string createFontID(string fontName, float size, FontStyle fontStyle){
            if(size <= 0){
                size = DEFUALT_FONT_SIZE;
            }
            if(fontName == null || fontName.Equals("")){
                fontName = DEFUALT_FONT_NAME;
            }
            return fontName + "," + size + "," + fontStyle;
        }
        /// <summary>The Font that is managed by this object </summary>
        protected Font managedFont = null;

        protected String name = DEFUALT_FONT_NAME;
        protected float size = DEFUALT_FONT_SIZE;
        protected FontStyle style = DEFUALT_FONT_STYLE;
        /// <summary>
        /// Will initialize a LoadableFont to manage a Font with the following properties
        /// </summary>
        /// <param name="fontName">The Name of the Font that will be used, "Arial", "Times New Roman" Note The Fonts must already be installed on the computer to be put into use</param>
        /// <param name="fontSize">The Size in points the Font will be, note Must be greater than zero</param>
        /// <param name="fontStyle">The Style, Bold, Italic,</param>
        public LoadableFont(string fontName, float fontSize, FontStyle fontStyle) : base(createFontID(fontName, fontSize, fontStyle), LibraryResource.convertResourceType(ResourceType.FONT)) {
            if(fontName != null && !fontName.Equals("")){
                name = fontName;
            }
            if(fontSize > 0){
                size = fontSize;
            }
            style = fontStyle;
        }
        /// <summary>
        /// Will return the name of this Font "Arial", "Times New Roman" etc...
        /// </summary>
        /// <returns>the name of this Font</returns>
        public String getFontName(){
            return name;
        }
        /// <summary>
        /// returns The Size of this Font
        /// </summary>
        /// <returns>The Size of this Font</returns>
        public float getFontSize() {
            return size;
        }
        /// <summary>
        /// Returns the Style of this Font, Bold, Italics etc...
        /// </summary>
        /// <returns>the Style of this Font</returns>
        public FontStyle getStyle() {
            return style;
        }
        /// <summary>
        /// Returns True if the Font has been successfullt loaded
        /// </summary>
        /// <returns></returns>
        public override bool isLoaded() {
            return managedFont != null;
        }
        /// <summary>
        /// Will 
        /// </summary>
        public override void loadResource() {
            managedFont = new Font(name, size, style);
        }
        /// <summary>
        /// Will Dispose of any resources currently in use
        /// </summary>
        public override void unloadResource() {
            managedFont.Dispose();
            managedFont = null;
        }
        /// <summary>
        /// Will Draw The Given Text using this Font to the Given Camera
        /// </summary>
        /// <param name="drawWindow">The Camera which this Text will be Dispayed in</param>
        /// <param name="text">The Text to be Drawn using this Font</param>
        /// <param name="textLocation">The location this Text will be Drawn to</param>
        /// <param name="textColor">The Color this Text will be drawn with</param>
        /// <param name="transformation">The Matrix Transformation that will be applied to this Text object When Drawn. Note this is not required and can be null</param>
        /// <returns>If True the text wqas successfully Drawn, if False Then There was an error drawing the text</returns>
        public bool paintText(Camera drawWindow, string text, PointF textLocation, Color textColor, Matrix transformation){
            if(drawWindow != null && isLoaded()){
                return drawWindow.paintString(text, managedFont, textColor, textLocation, transformation);
            }
            return false;
        }
    }
}
