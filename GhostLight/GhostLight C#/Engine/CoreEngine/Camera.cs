//Author: Michael Letter

using System;
using System.Collections;
using System.Windows.Forms;
using System.Drawing;
using System.Drawing.Drawing2D;

namespace CustomWindower.CoreEngine {
    /// <summary>
    /// Maintains the Paint actions to a single Control
    /// </summary>
    public class Camera : Panel{
        /// <summary> The Color the Background of this window will be cleared too </summary>
        public System.Drawing.Color backGroundColor = Color.Blue;

        private Mouse attachedMouse = null;
        
        /// <summary> Stores the aspect Ratio of this panel (Width/Height)</summary>
        public double aspectRatio { get; private set; }

        //Draw Buffers
        //Due to Flickering All items are Drawn to an image that is then drawn to the screen
        /// <summary> The actual buffer images</summary>
        private System.Drawing.Bitmap[] drawBuffers = null;
        /// <summary> the Graphics objects to draw to each image </summary>
        private System.Drawing.Graphics[] bufferGraphics = null;
        /// <summary> The Buffer that will be pushed to the screen next</summary>
        private int targetBuffer = 0;

        //Camera to world Transform stuff
        private bool cameraTransformChanged = true;
        /// <summary> Stores the Transform that is used to Define the comver</summary>
        private System.Drawing.Drawing2D.Matrix cameraTransform = new Matrix();
        /// <summary> Used to Determine to map points from pixelSpace to woldSpace </summary>
        private System.Drawing.Drawing2D.Matrix inverseCameraTransform = new Matrix();
        /// <summary> Stores the proint in the world that will be drawn at the center of the image produced by the camera </summary>
        private System.Drawing.PointF worldCenter = new PointF();
        /// <summary> Stores the scale of the world in the X and Y directions </summary>
        private System.Drawing.PointF worldScale = new PointF();
        /// <summary> Stores the Shear of the World in the X and Y dirrections </summary>
        private System.Drawing.PointF worldShear = new PointF();
        /// <summary> Stores the amount number of radians the world will be rotated by </summary>
        private float worldRotation = 0;

        /// <summary> If not null, this DrawSet will be Drawn to this camera every tim onPain() is envoked </summary>
        public DrawSet drawSet = null;

        /// <summary> The Total Width of the Screen in world Coordinates
        /// Note this value does not take into acount rotation or shearing</summary>
        public float worldWidth { get {
            if(drawBuffers == null){
                return (float)base.Width / worldScale.X;
            }
            else{
                return (float)drawBuffers[0].Width / worldScale.X;
            }
        }
            private set{ } 
        }
        /// <summary> The Total Height of the Screen in world Coordinates
        /// Note this value does not take into acount rotation or shearing</summary>
        public float worldHeight { get {
            if (drawBuffers == null){
                return (float)base.Height / worldScale.Y;
            }
            else{
                return (float)drawBuffers[0].Height / worldScale.Y;
            }
        }
            private set { }
        }

        //Public Storage 
        /// <summary>Used to ensure only one draw happens at a time when a draw is conducted paintLock becomes true
        /// All paints will wait for paintLock to become false to draw again</summary>
        private bool paintLock = false;
        /// <summary>The Number of miliseconds a draw will wait for paintLock before the paint is considered failed</summary>
        protected TimeSpan giveUpTime = new TimeSpan(0, 0, 0, 200);
        /// <summary> A reference to the currentDraw Object that draws are being directed too,
        /// Note, if 0 buffers are in use this is the actual Graphics object to the panel that is being drawn too</summary>
        private System.Drawing.Graphics currentGraphicsObject = null;
        /// <summary> Used by majority of the Draw Functions, storred to prevent creating a new one repeatedly </summary>
        private System.Drawing.Pen publicPen = new System.Drawing.Pen(Color.DarkBlue, 5);
        /// <summary> used by some of the draw functions to fill in a selection</summary>
        private System.Drawing.SolidBrush publicBrush = new System.Drawing.SolidBrush(Color.Chartreuse);
        /// <summary>Some Draw Functions required transforming a sieries of points This array is used to prevent unessesry news allong the way</summary>
        private System.Drawing.PointF[] publicPoints = new System.Drawing.PointF[1];
        /// <summary>  Used by majority of the Draw Functions, storred to prevent creating a new one repeatedly </summary>
        private System.Drawing.Drawing2D.Matrix publicMatrix = new System.Drawing.Drawing2D.Matrix();

        //PaintEventHandler paintEcentHandeler = null;
        public Camera() {
            worldCenter.X = 0;
            worldCenter.Y = 0;

            worldScale.X = 1f;
            worldScale.Y = 1f;

            worldShear.X = 0f;
            worldShear.Y = 0f;

            worldRotation = 0;

            SetStyle(ControlStyles.DoubleBuffer, true);
            //SetStyle(ControlStyles.ResizeRedraw, true);
            SetStyle(ControlStyles.AllPaintingInWmPaint, true);
            SetStyle(ControlStyles.UserPaint, true);

            aspectRatio = (double)Width / (double)Height;


            this.HandleCreated += new EventHandler(load);
            this.Disposed += new EventHandler(dispose);
            this.Resize += new EventHandler(updateAspectRatio);
        }
        /// <summary>
        /// Will load resources nessesary for thisCamera to be created
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        protected virtual void load(object sender, EventArgs e) {
            aspectRatio = (double)Width / (double)Height;
        }

        /// <summary>
        /// Will dispose of resourses that need be disposed off
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        protected virtual void dispose(object sender, EventArgs e){
            //Disposing Old Buffers
            if (drawBuffers != null) {
                for (int loopDispose = 0; loopDispose < drawBuffers.Length; loopDispose++) {
                    bufferGraphics[loopDispose].Dispose();
                    drawBuffers[loopDispose].Dispose();
                }
                bufferGraphics = null;
                drawBuffers = null;
                publicPen.Dispose();
                publicBrush.Dispose();
            }
        }
        /// <summary>
        /// Is called whenever the window is resized
        /// </summary>
        protected virtual void updateAspectRatio(object sender, EventArgs e) {
            aspectRatio = (double)Width / (double)Height;
        }
        /// <summary>
        /// Will resize this camera to fit within the given Space and rescale the world coordinates to match size
        /// </summary>
        /// <param name="targetWidth">The width of aria the camera has to fit within</param>
        /// <param name="targetHeight">The height of aria the camera has to fit within</param>
        public virtual void scaleToFit(int targetWidth, int targetHeight){
            if(targetWidth > 0 &&  targetWidth != base.Width && targetHeight > 0 && targetHeight != base.Height){
                //Finding scale Factor
                double currentAspectRatio = aspectRatio;
                double newWidth = targetWidth;
                double newHeight = (int)(newWidth / currentAspectRatio);
                if(newHeight > targetHeight){
                    newHeight = targetHeight;
                    newWidth = (int)(newHeight * currentAspectRatio);
                }
                //Scaleing world
                setCameraScaleX(worldScale.X * ((float)newWidth / (float)base.Width));
                setCameraScaleY(worldScale.Y * ((float)newHeight / (float)base.Height));
                //Scaling Draw Area
                Width = (int)newWidth;
                Height = (int)newHeight;
                aspectRatio = currentAspectRatio;
            }
        }
        /// <summary>
        /// Will attach a mouse object to this view to keep track of the mouses position and state
        /// </summary>
        /// <param name="newMouse"></param>
        /// <returns></returns>
        public bool attachMouse(Mouse newMouse){
            if(this.attachedMouse == null){
                newMouse.connectToCamera(this);
                attachedMouse = newMouse;
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will detach any mouse that is attached to this camera
        /// </summary>
        public void detachMouse() {
            if(attachedMouse != null){
                attachedMouse.disconnectFromCamera();
                attachedMouse = null;
            }
        }
        /// <summary>
        /// Will convert the given array of Points from points in PixelSpace to points in WorldSpace
        /// </summary>
        /// <param name="targetPoints">Points to be converted</param>
        public void convertToWorldCoordinates(Point[] targetPoints) {
            if(targetPoints != null){
                updateCameraTransform();
                if(drawBuffers != null){
                    for(int loop = 0; loop < targetPoints.Length; loop++){
                        targetPoints[loop].X = (int)((float)targetPoints[loop].X * ((float)drawBuffers[0].Width) / (float)base.Width);
                        targetPoints[loop].Y = (int)((float)targetPoints[loop].Y * ((float)drawBuffers[0].Height) / (float)base.Height);
                    }
                }
                inverseCameraTransform.TransformPoints(targetPoints);
            }
        }
        /// <summary>
        /// Will convert the given array of Points from points in PixelSpace to points in WorldSpace
        /// </summary>
        /// <param name="targetPoints">Points to be converted</param>
        public void convertToWorldCoordinates(PointF[] targetPoints) {
            if(targetPoints != null){
                updateCameraTransform();
                if(drawBuffers != null){
                    for(int loop = 0; loop < targetPoints.Length; loop++){
                        targetPoints[loop].X = (int)((float)targetPoints[loop].X * ((float)drawBuffers[0].Width / (float)base.Width));
                        targetPoints[loop].Y = (int)((float)targetPoints[loop].Y * ((float)drawBuffers[0].Height / (float)base.Height));
                    }
                }
                inverseCameraTransform.TransformPoints(targetPoints);
            }
        }
        /// <summary>
        /// Will convert the given array of Points from points in WorldSpace to points in PixelSpace
        /// </summary>
        /// <param name="targetPoints">Points to be converted</param>
        public void convertToPixelCoordinates(Point[] targetPoints) {
            if(targetPoints != null){
                updateCameraTransform();
                cameraTransform.TransformPoints(targetPoints);
                if(drawBuffers != null){
                    for(int loop = 0; loop < targetPoints.Length; loop++){
                        targetPoints[loop].X = (int)((float)targetPoints[loop].X * ((float)base.Width) / (float)drawBuffers[0].Width);
                        targetPoints[loop].Y = (int)((float)targetPoints[loop].Y * ((float)base.Height) / (float)drawBuffers[0].Height);
                    }
                }
            }
        }
        /// <summary>
        /// Will convert the given array of Points from points in WorldSpace to points in PixelSpace
        /// </summary>
        /// <param name="targetPoints">Points to be converted</param>
        public void convertToPixelCoordinates(PointF[] targetPoints) {
            if(targetPoints != null){
                updateCameraTransform();
                cameraTransform.TransformPoints(targetPoints);
                if(drawBuffers != null){
                    for(int loop = 0; loop < targetPoints.Length; loop++){
                        targetPoints[loop].X = (int)((float)targetPoints[loop].X * ((float)base.Width) / (float)drawBuffers[0].Width);
                        targetPoints[loop].Y = (int)((float)targetPoints[loop].Y * ((float)base.Height) / (float)drawBuffers[0].Height);
                    }
                }
            }
        }
        /// <summary>
        /// When this Camera draws it draws to a buffer of a given size independent from the size of the control
        /// this function will
        /// </summary>
        /// <param name="width">The width this camera will draw in</param>
        /// <param name="height">The width this camera will draw in</param>
        /// <param name="bufferCount">The number of buffers this Camera will use, If you don't know what this is give 0</param>
        /// <returns>If True the disconection was successfull, otherwise no changes were made</returns>
        public bool setDrawBufferSize(int width, int height, int bufferCount) {
            if (width > 0 && height > 0 && (drawBuffers == null || drawBuffers[0].Width != width || drawBuffers[0].Height != height)) {
                //Disposing Old Buffers
                if (drawBuffers != null) {
                    for (int loopDispose = 0; loopDispose < drawBuffers.Length; loopDispose++) {
                        bufferGraphics[loopDispose].Dispose();
                        drawBuffers[loopDispose].Dispose();
                    }
                    bufferGraphics = null;
                    drawBuffers = null;
                }
                //Creating new Buffers
                if (bufferCount > 0) {
                    drawBuffers = new Bitmap[bufferCount];
                    bufferGraphics = new Graphics[bufferCount];
                    for (int loopInitialize = 0; loopInitialize < drawBuffers.Length; loopInitialize++) {
                        drawBuffers[loopInitialize] = new Bitmap(width, height);
                        bufferGraphics[loopInitialize] = Graphics.FromImage(drawBuffers[loopInitialize]);
                        bufferGraphics[loopInitialize].InterpolationMode = InterpolationMode.High;
                        bufferGraphics[loopInitialize].CompositingQuality = CompositingQuality.HighSpeed;
                        bufferGraphics[loopInitialize].SmoothingMode = SmoothingMode.AntiAlias;
                    }
                }
                return true;
            }
            return false;
        }

        //Functions Tied to events
        /// <summary>
        /// If connected to a control this function will be called each time the control needs to be redrawn
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        protected override void OnPaint(System.Windows.Forms.PaintEventArgs e) {
            Graphics graphicsObject = e.Graphics;
            currentGraphicsObject = null;
            graphicsObject.InterpolationMode = InterpolationMode.Bilinear;
            graphicsObject.CompositingQuality = CompositingQuality.HighSpeed;
            graphicsObject.SmoothingMode = SmoothingMode.AntiAlias;
               
            if(bufferGraphics != null){
                targetBuffer = targetBuffer % drawBuffers.Length;
                currentGraphicsObject = bufferGraphics[targetBuffer];
            }
            else{
                currentGraphicsObject = graphicsObject;
            }
            updateCameraTransform();
            currentGraphicsObject.Clear(backGroundColor);

            if(drawSet != null){
                drawSet.paintSet(this);
            }
            if(drawBuffers != null){
                graphicsObject.DrawImage(drawBuffers[targetBuffer], new System.Drawing.RectangleF(0, 0, Width, Height));
            }
        }

        //Paint functions>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        /// <summary>
        /// Will Clear the entire DrawSpace with the given Color
        /// </summary>
        /// <param name="clearColor">The Color the screen will be cleared too</param>
        /// <returns>If True the screen was succeessfully Cleared. OtherWise False</returns>
        public bool clear(System.Drawing.Color clearColor) {
            if(clearColor != null){
                if(aquirePaintLock()) {
                    if (currentGraphicsObject != null) {
                        currentGraphicsObject.Clear(clearColor);
                        releasePaintLock();
                        return true;
                    }
                }
                releasePaintLock();
            }
            return false;
        }
        /// <summary>
        /// Will Draw a rectangle in this view
        /// </summary>
        /// <param name="rectangle">The size and scale of the rectangle</param>
        /// <param name="edgeColor">The Color of the rectangle edge</param>
        /// <param name="edgeWidth">The width of the rectangels outer edge</param>
        /// <param name="tranformation">The Affine Transformation that will be applied to this line. Note this member is not required and can be null</param>
        /// <returns>If True the line was drawn successfully, If False the draw was abourted due to an error</returns>
        public bool paintRectangle(System.Drawing.RectangleF rectangle, System.Drawing.Color fillColor, System.Drawing.Color edgeColor, float edgeWidth, Matrix tranformation)  {
           //Can only Draw one Rectangle at a time, sorry
            if (rectangle != null && edgeColor != null && edgeWidth >= 0){
                if(aquirePaintLock()) {
                    if (currentGraphicsObject != null) {
                        //Transform
                        publicMatrix.Reset();
                        if (tranformation != null) {
                            publicMatrix.Multiply(tranformation, MatrixOrder.Append);
                        }
                        publicMatrix.Multiply(cameraTransform, MatrixOrder.Append);
                        currentGraphicsObject.Transform = publicMatrix;
                        //Fill Brush
                        if (fillColor != null && fillColor.A > 0) {
                            publicBrush.Color = fillColor;
                            currentGraphicsObject.FillRectangle(publicBrush, rectangle);
                        }
                        //Edge Pen
                        if (edgeColor != null && edgeColor.A > 0) {
                            publicPen.Color = edgeColor;
                            publicPen.Width = edgeWidth;
                            currentGraphicsObject.DrawRectangle(publicPen, rectangle.X, rectangle.Y, rectangle.Width, rectangle.Height);
                        }
                        releasePaintLock();
                        return true;
                    }
                    releasePaintLock();
                }
            }
            return false;
        }
        /// <summary>
        /// Will Draw a given image in this camera
        /// </summary>
        /// <param name="drawImage">The image to be drawn</param>
        /// <param name="srcRectangle">Defines the peice of the image that will be drawn</param>
        /// <param name="locationRectangle">Defines the location and Scale of the image prior to the image transformation</param>
        /// <param name="tranformation">The Affine Transformation that will be applied to this line. Note this member is not required and can be null</param>
        /// <returns>If True the line was drawn successfully, If False the draw was abourted due to an error</returns>
        public bool paintImage(System.Drawing.Image drawImage, System.Drawing.RectangleF srcRectangle, System.Drawing.RectangleF locationRectangle, Matrix tranformation) {
            if (drawImage != null && srcRectangle != null && locationRectangle != null){
                if(aquirePaintLock()) {
                    if (currentGraphicsObject != null) {
                        //Transform
                        publicMatrix.Reset();
                        if(tranformation != null){
                            publicMatrix.Multiply(tranformation, MatrixOrder.Append);
                        }
                        publicMatrix.Multiply(cameraTransform, MatrixOrder.Append);
                        currentGraphicsObject.Transform = publicMatrix;
                        //Draw
                        currentGraphicsObject.DrawImage(drawImage,locationRectangle, srcRectangle, GraphicsUnit.Pixel);
                        releasePaintLock();
                        return true;
                    }
                    releasePaintLock();
                }
            }
            return false;
        }
        /// <summary>
        /// Will Draw a lineSegment in this camera
        /// </summary>
        /// <param name="LinePoints">specifies The Points That Define The Line Segments When Drawn will Draw Line Segments between each point given
        /// ie if LinePoints.length == 3 a line will be drawn between LinePoints[0] and LinePoints[1], as well as between LinePoints[1] and LinePoints[2].
        /// Note, if LinePoints containes fewer than 2 elements this function will fail</param>
        ///<param name="activePoints">Specifies how many points within LinePoints should be used to draw the Line.
        ///ei if active Points ware 3 only the first 3 points will be used to draw the line
        ///Note, if activePoints is greater than the LinePoints.length this function will fail</param>
        /// <param name="lineColor">specifies the color of the line</param>
        /// <param name="thickness">specifies Thickness of the line</param>
        /// <param name="tranformation">The Affine Transformation that will be applied to this line. Note this member is not required and can be null</param>
        /// <returns>If True the line was drawn successfully, If False the draw was abourted due to an error</returns>
        public bool paintLine(PointF[] LinePoints, int activePoints,  Color lineColor, float thickness, Matrix tranformation)  {
            if(LinePoints != null && LinePoints.Length > 1 && activePoints > 1 && activePoints <= LinePoints.Length && thickness > 0 && lineColor != null && lineColor.A > 0){
                if(aquirePaintLock()) {
                    if (currentGraphicsObject != null) {
                        //Transform
                        publicMatrix.Reset();
                        publicMatrix.Multiply(cameraTransform, MatrixOrder.Append);
                        if (tranformation != null) {
                            publicMatrix.Multiply(tranformation, MatrixOrder.Prepend);
                        }
                      
                        currentGraphicsObject.Transform = publicMatrix;
                        /*//Transforming Points
                        if (publicPoints.Length < activePoints || publicPoints.Length > activePoints + 20) {
                            publicPoints = new PointF[activePoints];
                        }
                        for (int loop = 0; loop < activePoints; loop++) {
                            publicPoints[loop].X = LinePoints[loop].X;
                            publicPoints[loop].Y = LinePoints[loop].Y;
                        }
                        publicMatrix.Invert();
                        publicMatrix.TransformPoints(publicPoints);*/

                        //Pen
                        publicPen.Width = thickness;
                        publicPen.Color = lineColor;
                        //Draw
                        for(int loop = 1; loop < activePoints; loop++){
                            currentGraphicsObject.DrawLine(publicPen, LinePoints[loop - 1], LinePoints[loop]);
                        }
                        releasePaintLock();
                        return true;
                    }
                    releasePaintLock();
                }
            }
            return false;
        }
        /// <summary>
        /// Will Draw the given String starting from the Specified point
        /// </summary>
        /// <param name="text">The Text to be Drawn</param>
        /// <param name="targetFont">The Font that will be used to draw the line</param>
        /// <param name="fontColor">The Color that the Text will be drawn in</param>
        /// <param name="textPosition">The position the text will be drawn at</param>
        /// <param name="tranformation">The Affine Transformation that will be applied to this line. Note this member is not required and can be null</param>
        /// <returns>If True the String was drawn successfully, If False the draw was abourted due to an error</returns>
        public bool paintString(String text, System.Drawing.Font targetFont , Color fontColor, PointF textPosition, Matrix tranformation) {
            if(text != null && targetFont != null && textPosition != null && fontColor != null && fontColor.A > 0){
                if (aquirePaintLock()) {
                    if (currentGraphicsObject != null){
                        //Transform
                        publicMatrix.Reset();
                        if (tranformation != null)   {
                            publicMatrix.Multiply(tranformation, MatrixOrder.Append);
                        }
                        publicMatrix.Multiply(cameraTransform, MatrixOrder.Append);
                        currentGraphicsObject.Transform = publicMatrix;
                        //Brush
                        publicBrush.Color = fontColor;
                        //Draw
                        currentGraphicsObject.DrawString(text, targetFont, publicBrush, textPosition);
                        releasePaintLock();
                        return true;
                    }
                    releasePaintLock();
                }
            }
            return false;
        }
        /// <summary>
        /// Will wait until paintLock is aquired or time runs out
        /// </summary>
        /// <returns>If True, Lock aquired, if False, lock Aqusition failed</returns>
        private bool aquirePaintLock() {
            TimeSpan startTime = DateTime.Now.TimeOfDay;
            bool aquired = false;
            while (!aquired && startTime.Subtract(DateTime.Now.TimeOfDay) < giveUpTime) {
                if(!paintLock){
                    paintLock = true;
                    aquired = true;
                }
            }
            return aquired;
        }
        /// <summary> If the paint lock is locked will unlock it. 
        /// Note, this functin does not check to see if this thread actually aquired the lock So be carful </summary>
        private void releasePaintLock() {
            paintLock = false;
        }

        //Camra Transform functions>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        //Rotation
        
        /// <summary>
        /// Will set the set the amount, in radians, the world be roptated by in this view
        /// </summary>
        /// <returns>he set the amount, in radians, the world be roptated by in this view</returns>
        public float getRotation() {
            return worldRotation;
        }
        /// <summary>
        /// Will set the set the amount, in radians, the world be rotated by in this view
        /// </summary>
        /// <param name="newRotation"></param>
        /// <returns>If True the disconection was successfull, otherwise no changes were made</returns>
        public bool setRotation(float newRotation) {
            worldRotation = newRotation % 360;
            cameraTransformChanged = true;
            return true;
        }

        //Scale

        /// <summary>
        /// Will Set the ratios the world will be scaled by in the X and Y directions in this view 
        /// </summary>
        /// <param name="scaleFactor">Stores the desires Scale Factors. Note, the Scale Factors in both the X and Y directions can not be 0</param>
        /// <returns>If True the disconection was successfull, otherwise no changes were made</returns>
        public bool setCameraScale(PointF scaleFactor) {
            if (scaleFactor != null && scaleFactor.X != 0 && scaleFactor.Y != 0) {
                worldScale.X = scaleFactor.X;
                worldScale.Y = scaleFactor.Y;
                cameraTransformChanged = true;
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will Set the ratios the world will be scaled by in the X direction in this view 
        /// </summary>
        /// <param name="scaleFactor">Note, the Scale Factor can not be 0</param>
        /// <returns>If True the disconection was successfull, otherwise no changes were made</returns>
        public bool setCameraScaleX(float XScale) {
            if (XScale != 0 && XScale != worldScale.X) {
                worldScale.X = XScale;
                cameraTransformChanged = true;
                return true;
            }
            return false;
        }

        /// <summary>
        /// Will Set the ratios the world will be scaled by in the Y direction in this view 
        /// </summary>
        /// <param name="scaleFactor">Note, the Scale Factor can not be 0</param>
        /// <returns>If True the disconection was successfull, otherwise no changes were made</returns>
        public bool setCameraScaleY(float YScale) {
            if (YScale != 0 && YScale != worldScale.Y) {
                worldScale.Y = YScale;
                cameraTransformChanged = true;
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will return the amount the world is be scaled by in the X direction by this view
        /// </summary>
        /// <returns>the amount the world is be scaled by in the X direction by this view</returns>
        public float getCameraScaleX() {
            return worldScale.X;
        }
        /// <summary>
        /// Will return the amount the world is be scaled by in the Y direction by this view
        /// </summary>
        /// <returns>the amount the world is be scaled by in the Y direction by this view</returns>
        public float getCameraScaleY() {
            return worldScale.Y;
        }

        //Shear

        /// <summary>
        /// Will Set the amount the world will be sheared by in the X and Y directions in this view 
        /// </summary>
        /// <param name="scaleFactor">Stores the desires sheared Factors. Note, the sheared Factors in both the X and Y directions can not be the same </param>
        /// <returns>If True the disconection was successfull, otherwise no changes were made</returns>
        public bool setCameraShear(PointF shearFactor) {
            if (shearFactor != null && shearFactor.X != shearFactor.Y) {
                worldShear.X = shearFactor.X;
                worldShear.Y = shearFactor.Y;
                cameraTransformChanged = true;
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will Set the amount the world will be sheared by in the X direction in this view 
        /// </summary>
        /// <param name="scaleFactor">Stores the desires sheared Factors. Note, the sheared Factors in both the X and Y directions can not be the same</param>
        /// <returns>If True the disconection was successfull, otherwise no changes were made</returns>
        public bool setCameraShearX(float XShear) {
            worldShear.X = XShear;
            cameraTransformChanged = true;
            return true;
        }

        /// <summary>
        /// Will Set the amount the world will be sheared by in the Y direction in this view  
        /// </summary>
        /// <param name="scaleFactor">Note, the Scale Factors can not be 0</param>
        /// <returns>If True the disconection was successfull, otherwise no changes were made</returns>
        public bool setCameraShearY(float YShear) {
            worldShear.Y = YShear;
            cameraTransformChanged = true;
            return true;
        }
        /// <summary>
        /// Will return the amount the world will be sheared by in the X direction in this view  
        /// </summary>
        /// <returns>the amount the world will be sheared by in the X direction in this view  </returns>
        public float getCameraShearX() {
            return worldShear.X;
        }
        /// <summary>
        /// Will return the amount the world will be sheared by in the Y direction in this view  
        /// </summary>
        /// <returns>the amount the world will be sheared by in the Y direction in this view  </returns>
        public float getCameraShearY() {
            return worldShear.Y;
        }

        //Center

        /// <summary>
        /// Will Set the postion in the world that will be drawn at the center of the screen in this view
        /// </summary>
        /// <param name="scaleFactor">Stores the centers X and Y coordinates </param>
        /// <returns>If True the disconection was successfull, otherwise no changes were made</returns>
        public bool setWorldCenter(PointF center) {
            if (center != null) {
                worldCenter.X = center.X;
                worldCenter.Y = center.Y;
                cameraTransformChanged = true;
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will Set the X postion in the world that will be drawn at the center of the screen in this view
        /// </summary>
        /// <param name="scaleFactor">X postion in the world that will be drawn at the center of the screen in this view</param>
        /// <returns>If True the disconection was successfull, otherwise no changes were made</returns>
        public bool setWorldCenterX(float Xcenter) {
            worldCenter.X = Xcenter;
            cameraTransformChanged = true;
            return true;
        }

        /// <summary>
        /// Will Set the Y postion in the world that will be drawn at the center of the screen in this view
        /// </summary>
        /// <param name="scaleFactor">Y postion in the world that will be drawn at the center of the screen in this view</param>
        /// <returns>If True the disconection was successfull, otherwise no changes were made</returns>
        public bool setWorldCenterY(float Ycenter) {
            worldCenter.Y = Ycenter;
            cameraTransformChanged = true;
            return true;
        }
        /// <summary>
        /// Will return the X postion in the world that will be drawn at the center of the screen in this view  
        /// </summary>
        /// <returns>the X postion in the world that will be drawn at the center of the screen in this view  </returns>
        public float getworldCenterX() {
            return worldCenter.X;
        }
        /// <summary>
        /// the Y postion in the world that will be drawn at the center of the screen in this view 
        /// </summary>
        /// <returns>the Y postion in the world that will be drawn at the center of the screen in this view </returns>
        public float getworldCenterY() {
            return worldCenter.Y;
        }
        /// <summary>
        /// Will return a copy of the Transform currently used to map the world Space of the draw Set to the pixel Space of the screen
        /// </summary>
        /// <returns></returns>
        public void setInverseCameraTransform() {
            inverseCameraTransform = cameraTransform.Clone();
            inverseCameraTransform.Invert();
            //adjusting for any warping as a result of differently sized buffers
            if (drawBuffers != null && (drawBuffers[0].Width != base.Width || drawBuffers[0].Height != base.Height)){
                inverseCameraTransform.Scale(base.Width / drawBuffers[0].Width, base.Height / drawBuffers[0].Height, MatrixOrder.Append);
            }
        }
        /// <summary>
        /// If the any of the would to cameraSpace variables are updated will recalculate the cameraTransform
        /// </summary>
        private void updateCameraTransform() {
            if (cameraTransformChanged) {
                //Reseting camera Transform
                cameraTransform.Reset();
                //Translation to origin 
                cameraTransform.Translate(-worldCenter.X, -worldCenter.Y, MatrixOrder.Append); ;
                //Scale
                if (worldScale.X != 0 && worldScale.Y != 0 && (worldScale.X != 1 || worldScale.Y != 1)) {
                    cameraTransform.Scale(worldScale.X, worldScale.Y, MatrixOrder.Append);
                }
                //Shear
                if ((worldShear.X != 1 || worldShear.Y != 1) && (worldShear.X != 0 || worldShear.Y != 0)) {
                    cameraTransform.Shear(worldShear.X, worldShear.Y, MatrixOrder.Append);
                }
                //Rotation
                if(worldRotation != 0){
                    cameraTransform.Rotate(worldRotation, MatrixOrder.Append);
                }
                //Moving Center to Centr
                if(drawBuffers != null){
                    cameraTransform.Translate(drawBuffers[0].Width / 2, drawBuffers[0].Height / 2, MatrixOrder.Append);
                }
                else{
                    cameraTransform.Translate(base.Width / 2, base.Height / 2, MatrixOrder.Append);
                }
                cameraTransformChanged = false;

                //invers Transform
                setInverseCameraTransform();
                if(attachedMouse != null){
                    attachedMouse.setTransform(inverseCameraTransform);
                }
            }
        }
    }
}
