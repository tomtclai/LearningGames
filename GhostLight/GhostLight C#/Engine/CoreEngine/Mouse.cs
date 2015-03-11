//Author: Michael Letter

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing;
using System.Drawing.Drawing2D;

namespace CustomWindower.CoreEngine{

    /// <summary>
    /// Once attached to a control will convay the mouses state when the mouse is in the control
    /// </summary>
    public class Mouse {
        private Camera mouseControl = null;
        private Matrix inversCameraTransform = null;

        public bool active { get; private set; }
        public bool onSceen {get; private set;}
        private Point pixelPosition = new Point();
        private PointF worldPosition = new Point();
        private PointF[] worldPos = { new PointF() };

        public bool rightMouseDown { get; private set; }
        public bool leftMouseDown { get; private set; }
        public bool middleMouseDown { get; private set; }

        public Mouse(){
            rightMouseDown = false;
            leftMouseDown = false;
            middleMouseDown = false;
            onSceen = false;
            active = false;
        }
        /// <summary>
        /// Will connect this mouse to a given camra this will link the properties of this object
        /// to the priperties of the mouse while it is within the given Camera
        /// Note, only one mouse object may be connected to a single Camera at any given time. 
        /// If there is another mouse Attached to the Camera this function will fail
        /// </summary>
        /// <param name="?">the Camera to get MouseEvents from</param>
        /// <returns>If True, the function executed succesfully, if false no changes were made</returns>
        internal bool connectToCamera(Camera targetCamera){
            if(targetCamera != null){
                if(active){
                    disconnectFromCamera();
                }
                active = true;
                mouseControl = targetCamera;
                mouseControl.MouseEnter += new EventHandler(MouseEntered);
                mouseControl.MouseLeave += new EventHandler(MouseExited);
                mouseControl.MouseDown += new MouseEventHandler(MouseButtonPressed);
                mouseControl.MouseUp += new MouseEventHandler(MouseButtonReleased);
                mouseControl.MouseMove += new MouseEventHandler(MouseMoved);
            }
            return true;
        }
        /// <summary>
        /// Will disconect this mouse from whatever camera it is attached to
        /// </summary>
        internal void disconnectFromCamera(){
            mouseControl.MouseEnter -= new EventHandler(MouseEntered);
            mouseControl.MouseLeave -= new EventHandler(MouseExited);
            mouseControl.MouseDown -= new MouseEventHandler(MouseButtonPressed);
            mouseControl.MouseUp -= new MouseEventHandler(MouseButtonReleased);
            mouseControl.MouseMove -= new MouseEventHandler(MouseMoved);
            active = false;
        }
        /// <summary>
        /// Will set the Matrix used as this Mouses Invers Camra Transform to the given Matrix
        /// This is used to convert mouse pixel positions to the coordinate system used by the 
        /// </summary>
        internal void setTransform(Matrix newInversCamraTransform) {
            if(newInversCamraTransform != null){
                inversCameraTransform = newInversCamraTransform;
                inversCameraTransform.TransformPoints(worldPos);
                worldPosition = worldPos[0];
            }
        }
        //Event Deligates
        private void MouseEntered(object sender, EventArgs e) {
            onSceen = true;
        }
        private void MouseExited(object sender, EventArgs e) {
            onSceen = false;
            leftMouseDown = false;
            middleMouseDown = false;
            rightMouseDown = false;
        }
        private void MouseButtonPressed(object sender, MouseEventArgs e) {
            switch(e.Button){
                case MouseButtons.Left:
                    leftMouseDown = true;
                    break;
                case MouseButtons.Middle:
                    middleMouseDown = true;
                    break;
                case MouseButtons.Right:
                    rightMouseDown = true;
                    break;
            }
        }
        private void MouseButtonReleased(object sender, MouseEventArgs e) {
            switch(e.Button){
                case MouseButtons.Left:
                    leftMouseDown = false;
                    break;
                case MouseButtons.Middle:
                    middleMouseDown = false;
                    break;
                case MouseButtons.Right:
                    rightMouseDown = false;
                    break;
            }
        }
        private void MouseMoved(object sender, MouseEventArgs e) {
            worldPos[0].X = pixelPosition.X = e.Location.X;
            worldPos[0].Y = pixelPosition.Y = e.Location.Y;
            if(inversCameraTransform != null){
                inversCameraTransform.TransformPoints(worldPos);
            }
            worldPosition = worldPos[0];
        }
        /// <summary>
        /// Will return a point Describing the current Pixel position of the mouse in this window
        /// </summary>
        /// <returns>Pixel Position of the mouse</returns>
        public Point getPixelPosition(){
            return pixelPosition;
        }
        /// <summary>
        /// Will return a point Describing the current World position of the mouse used by the DrawSet
        /// </summary>
        /// <returns>World Position</returns>
        public PointF getWorldPosition(){
            return worldPosition;
        }
    }
}
