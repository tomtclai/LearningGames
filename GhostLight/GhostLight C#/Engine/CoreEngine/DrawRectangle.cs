//Author: Michael Letter

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing.Drawing2D;
using System.Drawing;
namespace CustomWindower.CoreEngine{
    public class DrawRectangle : Drawable {

        /// <summary>The Sprite the DrawRectangle will use</summary>
        protected Sprite drawImage = null;
        /// <summary> the Sprite frame that will be used</summary>
        protected int frame = 0;

        /// <summary>If True the Sprite will be drawn on top of the rectangle, If False, Vice Versa</summary>
        public bool spriteOnTop = true;
        /// <summary>If False the Sprite will not be Drawn</summary>
        public bool spriteVisible = true;
        /// <summary>If false the rectangle will not be drawn</summary>
        public bool rectangleVisible = true;
        /// <summary>If False none of this DrawRectangle will be drawn</summary>
        public bool visible = true;

        /// <summary>the edge Color of the Rectangle</summary>
        public Color fillColor = Color.Green;
        /// <summary>The color of this rectangles Edges</summary>
        public Color edgeColor = Color.White;
        /// <summary>The width of this rectangles Edges, Not if this value is zero or less the rectangle will not have edges</summary>
        public float edgeWidth = 3;

        /// <summary> Marks when rotation scale shear or porition have been changed</summary>
        private bool transformOutdated = true;
        private float rotation = 0;
        private PointF position = new PointF();
        private PointF scale = new PointF();
        private PointF shear = new PointF();

        /// <summary>The Matrix used to scale and rotate, postion the shear the drawRectangles </summary>
        private Matrix spriteTransoformation = new Matrix();
        /// <summary>Stored rectangle rotation position and shear</summary>
        private Matrix rectangleTransfromation = new Matrix();
        /// <summary>The Starting size of the Rectangle centered</summary>
        private RectangleF rectangleStart = new RectangleF();
        /// <summary>Stores the inverst Sprite Transformation This is will transform points from world space
        /// To a space where This Rectange is wholly contained by a 1x1 box centered at (0,0)</summary>
        private Matrix inversTransformation = new Matrix();
        /// <summary>Some Functions Transfrorm a single PointF often. this is to prevent constantly createing a new Array</summary>
        protected PointF[] publicPointArray = new PointF[1];

        public DrawRectangle() : base() {
            scale.X = scale.Y = 200;
        }
        /// <summary>
        /// Returns True if the Given Rectangle contains the given point
        /// </summary>
        /// <param name="point">The Point in space that will be checked</param>
        /// <returns></returns>
        public virtual bool containsPoint(PointF point) {
            if(point != null){
                updateTransformation();
                publicPointArray[0] = point;
                inversTransformation.TransformPoints(publicPointArray);
                return publicPointArray[0].X >= -0.5f && publicPointArray[0].X <= 0.5f && publicPointArray[0].Y >= -0.5f && publicPointArray[0].Y <= 0.5f;
            }
            return false;
        }
        //Size
        public virtual bool setSize(float width, float height) {
            return setWidth(width) & setHeight(height);
        }
        public virtual bool setWidth(float width) {
            if(width != 0 && scale.X != width){
                scale.X = width;
                transformOutdated = true;
                return true;
            }
            return false;
        }
        public virtual bool setHeight(float height){
            if(height != 0 && scale.Y != height){
                scale.Y = height;
                transformOutdated = true;
                return true;
            }
            return false;
        }
        public virtual float getWidth() {
            return scale.X;
        }
        public virtual float getHeight() {
            return scale.Y;
        }

        //Center
        public virtual void setCenter(float x, float y) {
            if(position.X != x || position.Y != y){
                position.X = x;
                position.Y = y;
                transformOutdated = true;
            }
        }
        public virtual void setCenterX(float x){
            if(position.X != x){
                position.X = x;
                transformOutdated = true;
            }
        }
        public virtual void setCenterY(float y){
            if(position.Y != y){
                position.Y = y;
                transformOutdated = true;
            }
        }
        public virtual float getCenterX(){
            return position.X;
        }
        public virtual float getCenterY() {
            return position.Y;
        }

        //Shear
        public virtual void setShear(float ShearX, float ShearY) {
            if((ShearX != shear.X || ShearY != shear.X) && (ShearX != 1 || ShearY != 1)){
                shear.X = ShearX;
                shear.Y = ShearY;
                transformOutdated = true;
            }
        }
        public virtual void setShearX(float ShearX){
            if(ShearX != shear.X){
                shear.X = ShearX;
                transformOutdated = true;
            }
        }
        public virtual void setShearY(float ShearY){
            if(ShearY != shear.Y){
                shear.Y = ShearY;
                transformOutdated = true;
            }
        }
        public virtual float getShearX(){
            return shear.X;
        }
        public virtual float getShearY() {
            return shear.Y;
        }

        //Rotation
        /// <summary>
        /// Will set the amount in degrees this rectangle will be rotated by
        /// </summary>
        /// <param name="rotation">degrees</param>
        public virtual bool setRotation(float rotation) {
            if (!float.IsNaN(rotation) && this.rotation != rotation){
                this.rotation = rotation%360;
                transformOutdated = true;
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will return the number of degrees this rectangle is rotated by
        /// </summary>
        /// <returns>degrees</returns>
        public virtual float getRotation() {
            return rotation;
        }

        //Frame
        /// <summary>
        /// Will set this Rectangles Frame to the given frame index
        /// Note, this Value can only be set to a frame index that exists in the Sprite and can only 
        /// </summary>
        /// <returns>Frame Index</returns>
        public bool setFrame(int frameIndex) {
            if (drawImage != null && frameIndex >= 0 && frameIndex < drawImage.getTotalFrames()) {
                frame = frameIndex;
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will return the frame index that will be be used in the sprite
        /// </summary>
        /// <returns>frame index</returns>
        public int getFrame(){
            return frame;
        }
        /// <summary>
        /// Will Set the sprite in use by this rectangle to the given sprite if it can or already has been loaded
        /// Note This does not unload this resources, if this resource is not in a library it may not be unloaded
        /// If this is the case be sure to use getSprite() to unload it prior to calling this function
        /// </summary>
        /// <returns>If True, the rectangle is using the given sprite, if false, no changes made</returns>
        public bool setSprite(Sprite newSprite) {
            if(newSprite != null){
                if(!newSprite.isLoaded()){
                    newSprite.loadResource();
                }
                if(newSprite.isLoaded()){
                    drawImage = newSprite;
                    return true;
                }
            }
            return false;
        }
        /// <summary>
        /// Will return the Sprite currently in use by this DrawRectangle
        /// </summary>
        /// <returns>the Sprite currently in use by this DrawRectangle</returns>
        public Sprite getSprite(){
            return drawImage;
        }
        /// <summary>
        /// If transformOutdated will update spriteTransoformation, rectangleTransfromation and rectangleStart 
        /// based on rotation, scale, shear, and positon
        /// </summary>
        private void updateTransformation() {
            if(transformOutdated){
                //Sprite
                spriteTransoformation.Reset();
                if (scale.X != 0 && scale.Y != 0) {
                    spriteTransoformation.Scale(scale.X, scale.Y, MatrixOrder.Append);
                }
                if(shear.X != 1 || shear.Y != 1){
                    spriteTransoformation.Shear(shear.X, shear.Y, MatrixOrder.Append);
                }
                spriteTransoformation.Rotate(rotation, MatrixOrder.Append);
                spriteTransoformation.Translate(position.X, position.Y, MatrixOrder.Append);

                //inverse Transform
                inversTransformation = spriteTransoformation.Clone();
                inversTransformation.Invert();

                //Rectangle
                if (scale.X > 0){
                    rectangleStart.Width = scale.X;
                }
                else if(scale.X != 0){
                    rectangleStart.Width = -scale.X;
                }
                if(scale.Y > 0){
                    rectangleStart.Height = scale.Y;
                }
                else if(scale.Y != 0){
                    rectangleStart.Height = -scale.Y;
                }
                rectangleStart.X = -rectangleStart.Width / 2;
                rectangleStart.Y = -rectangleStart.Height / 2;

                //Rectangle Transform
                rectangleTransfromation.Reset();
                if(shear.X != 1 || shear.Y != 1){
                    rectangleTransfromation.Shear(shear.X, shear.Y, MatrixOrder.Append);
                }
                rectangleTransfromation.Rotate(rotation, MatrixOrder.Append);
                rectangleTransfromation.Translate(position.X, position.Y, MatrixOrder.Append);

                transformOutdated = false;
            }
        }
        /// <summary>
        /// Will Draw the Rectangle
        /// </summary>
        /// <param name="drawLocation"></param>
        public override void paint(Camera drawLocation) {
           if(visible){
               updateTransformation();
               if (spriteOnTop) {
                   if (rectangleVisible)  {
                       drawLocation.paintRectangle(this.rectangleStart, fillColor, edgeColor, edgeWidth, rectangleTransfromation);
                   }
                   if (spriteVisible && drawImage != null && drawImage.isLoaded()) {
                       drawImage.paint(drawLocation, frame, spriteTransoformation);
                   }
               }
               else{
                    if (spriteVisible && drawImage != null && drawImage.isLoaded()) {
                        drawImage.paint(drawLocation, frame, spriteTransoformation);
                    }
                    if (rectangleVisible)  {
                        drawLocation.paintRectangle(this.rectangleStart, fillColor, edgeColor, edgeWidth, rectangleTransfromation);
                    }
               }
           }
        }
    }
}
