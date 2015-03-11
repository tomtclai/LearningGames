//Author: Michael Letter

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using CustomWindower.CoreEngine;

namespace CustomWindower.Driver{
    /// <summary>
    /// Will will update on a specified interval on the Thread of the window
    /// </summary>
    public abstract class SingleWindowUpdater : Updater {
        private Camera hostWindow = null;
        private Mouse mouse = null;
        private Keyboard keybord = null;
        private ResourceLibrary localLibrary = new ResourceLibrary();
        private DrawSet drawSet = new DrawSet();
        private Form parentForm = null;

        /// <summary>
        /// Initializes this updater use start to start the updater
        /// </summary>
        /// <param name="hostCamera"></param>
        public virtual void initialize(Form ParentForm, Camera hostCamera)  {
            if(hostCamera != null){
                parentForm = ParentForm;
                mouse = new CoreEngine.Mouse();
                keybord = new CoreEngine.Keyboard();
                hostWindow = hostCamera;
                hostWindow.attachMouse(mouse);
                keybord.attachToCamera(hostCamera);
                hostCamera.drawSet = drawSet;
                hostCamera.MouseEnter += new EventHandler(mouseEnter);
            }
        }
        /// <summary>
        /// Is Called when the mouse enters the Screen
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void mouseEnter(object sender, EventArgs e){
            hostWindow.Focus();
        }
        /// <summary>
        /// Is called at each update, designed to be overriden
        /// Note The Form is Invaledated at the conclusion of this funtion. As a result 
        /// base.update(state) should be called at the end of your update
        /// </summary>
        /// <param name="state"></param>
        public override void update(Object state){
            hostWindow.Invalidate();
        }
        /// <summary>
        /// Will return the Mouse attached to The Draw Window
        /// </summary>
        /// <returns></returns>
        public CoreEngine.Mouse getActiveMouse(){
            return mouse;
        }
        /// <summary>
        /// Will return the Keyboard attached to the Draw Window
        /// </summary>
        /// <returns></returns>
        public CoreEngine.Keyboard getActiveKeyBoard(){
            return keybord;
        }
        /// <summary>
        /// Will return the Local Library
        /// </summary>
        /// <returns></returns>
        public ResourceLibrary getLocalLibrary(){
            return localLibrary;
        }
        /// <summary>
        /// Will return the Draw Set the Draw window is repsonsible for drawing
        /// </summary>
        /// <returns></returns>
        public DrawSet getActiveDrawSet(){
            return drawSet;
        }
        /// <summary>
        /// Will return the current Draw window responsible for drawing the DrawSet
        /// </summary>
        /// <returns></returns>
        public Camera getDrawWindow(){
            return hostWindow;
        }
        /// <summary>
        /// Will close this window and Dispose of any resources the windo uses
        /// </summary>
        public virtual void closeWindow(){
            if(parentForm != null){
                parentForm.Close();
            }
        }
        /// <summary>
        /// Must Be Called when the Window is closed
        /// Will unload all resources used by this updater.
        /// </summary>
        public override void Dispose() {
            base.stop();
            localLibrary.unloadAll(false);
            hostWindow.detachMouse();
            hostWindow.MouseEnter -= new EventHandler(mouseEnter);
            base.Dispose();
        }
    }
}
