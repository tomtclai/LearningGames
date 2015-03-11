using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;

/**
 * Maintains a segmented horizontal Segment bar
 * @author Michael Letter
 */
namespace MenueSystem{
    public class SegmentedBar : Drawable{
	    private int maxSegments = 0;
	    /** stores the Segment the bar will be representing must be less than SegmentBarDividers.length */
	    private int filledSegments = 0;
	    /** Stores the image used on a bar Segment if it is active */
	    private Sprite activeImage = null;
	    /** Stores the image used on a bar Segment if it is not active */
        private Sprite inactiveImage = null;
	    /** if True the Bar will be Drawn Vertically. Otherwise it will be drawn horizontal */
	    private bool vertical = false;
	    /** If True, when drawn will be drawn with the given textures rather than this rectangles color. If False, the bar will be drawn with the current color*/
	    private bool useImages = false;
        /// <summary>Dictates the X and Y position</summary>
        private PointF center = new PointF();
        /// <summary>Dictates the Width and Height</summary>
        private PointF size = new PointF();
        /// <summary>Marks the Color Bars Will be when they are filled</summary>
        private Color barColor = Color.Green;
        private Color emptyColor = Color.FromArgb(0, 0, 0, 0);
        /// <summary>Marks whether or not this bar is invisible or visible</summary>
        public bool visible = true;
        /// <summary>Marks whether or not the position or size of the List was changed</summary>
        private bool segmentsOutdated = true;
        /// <summary>Stores the Rectangles used to Draw the Bar</summary>
        List<DrawRectangle> barSegments = new List<DrawRectangle>();
	    /**
	     * if "useImages" is True will set the texture drawn for the active segments of bar
	     * @param activeImageLocation the location of the target image
	     */
	    public void setActiveImage(Sprite image){
		    if(image != null && image.isLoaded()){
			    activeImage = image;
                segmentsOutdated = true;
		    }
	    }
	    /**
	     * if "useImages" is True will set the texture drawn for the inactive segments of bar
	     * @param activeImageLocation the location of the target image
	     */
	    public void setInactiveImage(Sprite image){
		    if(image != null && image.isLoaded()){
                inactiveImage = image;
                segmentsOutdated = true;
		    }
	    }
	    /**
	     * will set the max Segment of the bar
	     * @param newmaxSegments
	     * @return whether or not changes were made
	     */
	    public bool setMaxSegments(int newmaxSegments){
		    if(newmaxSegments >= 0){
                if(maxSegments != newmaxSegments){
			        maxSegments = newmaxSegments;
			        if(filledSegments > maxSegments){
				        filledSegments = maxSegments;
			        }
                    else {
                        while(maxSegments > barSegments.Count){
                            DrawRectangle newSegment = new DrawRectangle();
                            newSegment.edgeColor = Color.Black;
                            newSegment.edgeWidth = 0.2f;
                            barSegments.Add(newSegment);
                        }   
                    }
                    segmentsOutdated = true;
                }
			    return true;
		    }
		    return false;
	    }
	    /**
	     * Does do you think this does
	     * @return
	     */
	    public int getMaxSegments(){
		    return maxSegments;
	    }
	    /**
	     * Will set the Segment displayed by the bar
	     * @param newfilledSegments
	     * @return whether or not changes were made
	     */
	    public bool setFilledSegments(int newfilledSegments){
		    if(newfilledSegments >= 0){
                if(filledSegments != newfilledSegments){
			        filledSegments = newfilledSegments;
			        if(filledSegments > maxSegments){
				        filledSegments = maxSegments;
			        }
                    segmentsOutdated = true;
                }
			    return true;
		    }
		    return false;
	    }
	    /**
	     * Does do you think this does
	     * @return
	     */
	    public int getfilledSegments(){
		    return filledSegments;
	    }
	    /**
	     * Will add as much of increment to the filledSegments as possible 
	     * @param increment
	     */
	    public void incrementSegmentsFilled(int increment){
		    filledSegments += increment;
		    if(filledSegments < 0){
			    filledSegments = 0;
		    }
		    else if(filledSegments > maxSegments){
			    maxSegments = filledSegments;
		    }
            segmentsOutdated = true;
	    }
        /// <summary>
        /// Sets the Center of the Bar
        /// </summary>
        /// <param name="X"></param>
        /// <param name="Y"></param>
        public void setCenter(float X, float Y){
            setCenterX(X);
            setCenterY(Y);
        }
        /// <summary>
        /// Sets the Center of the Bar
        /// </summary>
        /// <param name="X"></param>
        public void setCenterX(float X){
            if(X != center.X){
                center.X = X;
                segmentsOutdated = true;
            }
        }
        /// <summary>
        /// Sets the Center of the Bar
        /// </summary>
        /// <param name="Y"></param>
        public void setCenterY(float Y){
            if(Y != center.Y){
                center.Y = Y;
                segmentsOutdated = true;
            }
        }
        public float getCenterX()  {
            return center.X;
        }
        public float getCenterY() {
            return center.Y;
        }
        /// <summary>
        /// Sets the Size of the Bar
        /// </summary>
        /// <param name="newWidth"></param>
        /// <param name="newHeight"></param>
        /// <returns></returns>
        public bool setSize(float newWidth, float newHeight) {
            return setWidth(newWidth) & setHeight(newHeight);
        }
        /// <summary>
        /// Sets the Width of the Bar
        /// </summary>
        /// <param name="newWidth"></param>
        /// <returns></returns>
        public bool setWidth(float newWidth) {
            if(newWidth > 0){
                if(size.X != newWidth){
                    size.X = newWidth;
                    segmentsOutdated = true;
                }
            }
            return false;
        }
        /// <summary>
        /// Sets the Height of the Bar
        /// </summary>
        /// <param name="newHeight"></param>
        /// <returns></returns>
         public bool setHeight(float newHeight) {
            if(newHeight > 0){
                if(size.Y != newHeight){
                    size.Y = newHeight;
                    segmentsOutdated = true;
                }
            }
            return false;
        }
        public float getWidth() {
            return size.X;
        }
        public float getHeight() {
            return size.Y;
        }
        /// <summary>
        /// If Called will make it so this Bars Fills Vertically
        /// </summary>
        public void MakeVertical() {
            if(!vertical){
                vertical = true;
                segmentsOutdated = true;
            }
        }
        /// <summary>
        /// Will make the Bar Fill Horozontly
        /// </summary>
        public void makeHorizontle() {
            if(vertical){
                vertical = false;
                segmentsOutdated = true;
            }
        }
        /// <summary>
        /// Will Set the Color of the Bar Segments when they are Filled
        /// </summary>
        public void setColor(Color fillColor) {
            if(fillColor != this.barColor){
                barColor = fillColor;
                segmentsOutdated = true;
            }
        }
        /// <summary>
        /// Will return the Color used for
        /// </summary>
        /// <returns></returns>
        public Color getColor() {
            return barColor;
        }
       /// <summary>
       /// Will set the Sprite used for Segments which have been filled 
       /// </summary>
       /// <param name="targetSprite">Note, this Sprite must already be loaded</param>
        public void setActiveSprite(Sprite image) {
            if(image != null && image.isLoaded() && (activeImage != null || image != activeImage)){
                activeImage = image;
                segmentsOutdated = true;
            }
        }
        /// <summary>
        ///  Will return the Sprite used for Segments which have been filled 
        /// </summary>
        /// <returns></returns>
        public Sprite getActiveSprite() {
            return activeImage;
        }
        /// <summary>
        /// Will Set the Sprite used when a segment is not filled
        /// </summary>
        /// <param name="image">Note, this Sprite must already be loaded</param>
        public void setInactiveSprite(Sprite image){
            if(image != null && image.isLoaded() && (inactiveImage != null || image != inactiveImage)){
                inactiveImage = image;
                segmentsOutdated = true;
            }
        }
        /// <summary>
        /// Will Return the Sprite used when a segment is not filled
        /// </summary>
        /// <returns></returns>
        public Sprite setActiveImage() {
            return inactiveImage;
        }
        /// <summary>
        /// Will make it so Bar Segments use the Desigated Sprite verses a blank color
        /// </summary>
        public void useSprites() {
            if(!useImages){
                useImages = true;
                segmentsOutdated = true;
            }
        }
        /// <summary>
        /// If Called will mae it so the Bar uses a designated color over Sprites
        /// </summary>
        public void useColor() {
            if(useImages){
                useImages = false;
                segmentsOutdated = true;
            }
        }
        /// <summary>
        /// Will Update the Segments managed by this bar so that any changes made take effect
        /// </summary>
        protected virtual void updateSegments() {
            if(segmentsOutdated){
                if(vertical){
                    float segmentHeight = this.size.Y / (float)maxSegments;
                    float firstCenter = center.Y - (size.Y / 2f) + (segmentHeight / 2);

                    int loop = 0;
                    foreach(DrawRectangle rectangle in barSegments){
                        if(loop < maxSegments){
                            //orienting Segment
                            rectangle.setCenter(center.X, firstCenter + (segmentHeight * loop));
                            rectangle.setSize(size.X, segmentHeight);
                            //Draw Mode
                            if(useImages){
                                rectangle.spriteVisible = true;
                                rectangle.rectangleVisible = false;
                            }
                            else{
                                rectangle.rectangleVisible = true;
                                rectangle.spriteVisible = false;
                            }
                            //Filled or Empty Segment
                            if(loop < filledSegments){
                                rectangle.setSprite(activeImage);
                                rectangle.fillColor = barColor;
                            }
                            else{
                                rectangle.setSprite(inactiveImage);
                                rectangle.fillColor = emptyColor;
                            }
                        }
                        //Is Last Segment
                        else{
                            rectangle.visible = false;
                        }
                        loop++;
                    }
                }
                else {
                    float segmentWidth = size.X / (float)maxSegments;
                    float firstCenter = center.X - (size.X / 2f) + (segmentWidth / 2);

                    int loop = 0;
                    foreach(DrawRectangle rectangle in barSegments){
                        if(loop < maxSegments){
                            //orienting Segment
                            rectangle.setCenter(firstCenter + (segmentWidth * loop), center.Y);
                            rectangle.setSize(segmentWidth, size.Y);
                            //Draw Mode
                            if(useImages){
                                rectangle.spriteVisible = true;
                                rectangle.rectangleVisible = false;
                            }
                            else{
                                rectangle.rectangleVisible = true;
                                rectangle.spriteVisible = false;
                            }
                            //Filled or Empty Segment
                            if(loop < filledSegments){
                                rectangle.setSprite(activeImage);
                                rectangle.fillColor = barColor;
                            }
                            else{
                                rectangle.setSprite(inactiveImage);
                                rectangle.fillColor = emptyColor;
                            }
                        }
                        else{
                            rectangle.visible = false;
                        }
                        loop++;
                    }

                }
                segmentsOutdated = false;
            }
        }
	    /** Will Draw the given Segment bar */
        public override void paint(Camera drawLocation) {
		    if(visible && drawLocation != null){
                updateSegments();
                int loop = 0;
                foreach(DrawRectangle rectangle in barSegments){
                    rectangle.paint(drawLocation);
                    loop++;
                    if(loop >= maxSegments){
                        break;
                    }
                }
            }
	    }
    }
}
