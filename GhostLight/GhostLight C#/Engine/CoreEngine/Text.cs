//Author: Michael Letter

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Drawing.Drawing2D;

namespace CustomWindower.CoreEngine{
    public class Text : Drawable {

        /// <summary> If False The Text Object will not Paint itself </summary>
        public bool visible = true;
        /// <summary> The Text that will be Drawn </summary>
        public string text = "Hello World!";
        /// <summary> Th Color of the Text when Drawn</summary>
        public Color textColor = Color.Black;
        /// <summary> The Position will Draw itself too</summary>
        public PointF textPosition = new PointF();

        /// <summary> Stores this Texts Rotation in Degrees</summary>
        private float TextRotation = 0;
        /// <summary> Becuase Why Not </summary>
        private PointF textShear = new PointF();

        /// <summary> The Font that will be employed by this object </summary>
        public LoadableFont targetFont = null;

        protected bool transformOutdated = false;
        protected Matrix textTransform = new Matrix();

        /// <summary>
        /// Will Draw this Text to the given Camera
        /// </summary>
        /// <param name="drawLocation">The camera the text will be drawn to</param>
        public override void paint(Camera drawLocation) {
            if(targetFont != null && visible){
                updateTransform();
                targetFont.paintText(drawLocation, text, textPosition, textColor, textTransform);
            }
        }
        /// <summary>
        /// Will set the Amount the Text will be rotated by when Drawn in Degrees 
        /// </summary>
        /// <param name="Rotation">the desired Rotation</param>
        public void setRotation(float Rotation) {
            TextRotation = Rotation % 360;
        }
        /// <summary>
        /// Will Return the amount the Text will be rotated by when Drawn in Degrees 
        /// </summary>
        /// <returns>This Texts Rotation</returns>
        public float getRotation() {
            return TextRotation;
        }
        /// <summary>
        /// Will Set the Text shear in the X and Y directions. 
        /// Note the Shear in the Shear X and Y direction can not be 1 at the same time
        /// </summary>
        /// <param name="shearX">The desired Shear in the X direction</param>
        /// <param name="shearY">The desired Shear in the Y Direction</param>
        /// <returns>If True the Shear was changed successfully. If False, no changes were made</returns>
        public bool setShear(float shearX, float shearY){
            if(shearX != 1 || shearY != 1){
                textShear.X = shearX;
                textShear.Y = shearY;
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will Set the Text shear in the X directions. 
        /// Note the Shear in the Shear X and Y direction can not be 1 at the same time
        /// </summary>
        /// <param name="shearX">The desired Shear in the X direction</param>
        public void setShearX(float shearX) {
             textShear.X = shearX;
        }
        /// <summary>
        /// Will Set the Text shear in the Y directions. 
        /// Note the Shear in the Shear X and Y direction can not be 1 at the same time
        /// </summary>
        /// <param name="shearY">The desired Shear in the Y direction</param>
        public void setShearY(float shearY) {
            textShear.Y = shearY;
        }
        /// <summary>
        /// Will return the Text's currentShear in the X direction
        /// </summary>
        /// <returns> currentShear in the X direction</returns>
        public float getShearX() {
            return textShear.X;
        }
        /// <summary>
        /// Will return the Text's currentShear in the Y direction
        /// </summary>
        /// <returns>currentShear in the Y direction</returns>
        public float getShearY() {
            return textShear.Y;
        }
        /// <summary>
        /// Will update the Transform in use if it is marked as outdated
        /// </summary>
        protected virtual void updateTransform() {
            if(transformOutdated){
                textTransform.Reset();
                textTransform.Translate(-textPosition.X, -textPosition.Y, MatrixOrder.Append);
                if(textShear.X != 1 || textShear.Y != 1){
                    textTransform.Shear(textShear.X, textShear.Y, MatrixOrder.Append);
                }
                textTransform.Rotate(TextRotation, MatrixOrder.Append);
                textTransform.Translate(textPosition.X, textPosition.Y, MatrixOrder.Append);
                transformOutdated = false;
            }
        }
    }
}
