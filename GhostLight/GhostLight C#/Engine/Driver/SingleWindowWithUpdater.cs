//Author: Michael Letter

using System;
using System.Threading;
using System.Drawing;
//using System.Windows;
using System.Windows.Forms;
using System.Windows.Media.Animation;
using CustomWindower.CoreEngine;

namespace CustomWindower.Driver {
    /// <summary>
    /// Will Create single window With a camera contained in the window
    /// Will Rescale Camera as the window Resizes
    /// </summary>
    public class SingleWindow : Form {
        private Camera drawWindow = null;
        private SingleWindowUpdater updater = null;
    
        /// <summary>
        /// Will start open a windows form and start it using the updater given
        /// Note this is a blocking call. this function will not return until the form has exited
        /// </summary>
        /// <param name="custumUpdater">The updater that will be used to build and update the drawSet overTime</param>
        /// <param name="Width">The desired pixel width of the form</param>
        /// <param name="height">The desired pixel height of the form</param>
        /// <returns>if the form was created successfully</returns>
        public static bool start(SingleWindowUpdater custumUpdater, int width, int height){
            if (custumUpdater != null && width > 0 && height > 0) {
                SingleWindow forme = new SingleWindow();
                forme.updater = custumUpdater;
                forme.Width = width + 16;
                forme.Height = height + 38;
                forme.initialize();
                Application.Run(forme);
                return true;
            }
            return false;
        }

        public SingleWindow() : base(){
            //initializing size
            this.Width = 816;
            this.Height = 518;
        }
        public virtual void initialize() {
            //initializing Camera
            drawWindow = new Camera();
            drawWindow.Width = Width - 16;
            drawWindow.Height = Height - 38;

            drawWindow.setWorldCenter(new PointF(drawWindow.Width / 2, drawWindow.Height / 2));
            drawWindow.setCameraScale(new PointF(0.9f, 0.9f));
            Controls.Add(drawWindow);

            //initializing updater
            if (updater != null) {
                updater.setIncrement(20);
                updater.initialize(this, drawWindow);
                updater.start(false);
            }
            base.FormClosing += new FormClosingEventHandler(CloseWindow);
            base.Resize += new EventHandler(resize);
        }
        /// <summary>
        ///  Closing Method Ussed to clean up anything that need be cleaned up
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void CloseWindow(object sender, EventArgs e) {
            if(updater != null){
                updater.Dispose();
            }
        }
        /// <summary>
        /// If Window Is Resized 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        protected virtual void resize(object sender, EventArgs e){
            drawWindow.scaleToFit(base.Width - 16, base.Height - 32);
            drawWindow.Location = new Point((int)(((base.Width - 16) / 2) - (drawWindow.Width / 2)), (int)(((base.Height - 32) / 2) - (drawWindow.Height / 2)));
        }

    }
}
