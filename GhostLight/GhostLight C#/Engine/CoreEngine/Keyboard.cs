//Author: Michael Letter

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace CustomWindower.CoreEngine{
    /// <summary> Using The is not  </summary>
    public class Keyboard {
        private Camera attachedCamera = null;
        /// <summary> Marks whether or not this Keybord is currently traking Keybord input from the given Camera</summary>
        public bool active { get; private set; }

        private bool[] keysDown = new bool[Enum.GetValues(typeof(Keys)).Length];


        public Keyboard() {
            for(int loop = 0; loop < keysDown.Length; loop++){
                keysDown[loop] = false;
            }
            active = false;
        }
        /// <summary>
        /// Will Detach this Keybord from any Cameras it is currenly attached too
        /// And then Attach it to the given Camera if possible
        /// </summary>
        /// <returns>If True, Connection was successfull, if False Keybord is no currently connected</returns>
        public bool attachToCamera(Camera targetCamera){
            if (attachedCamera != null){
                detachFromCamera();
            }
            attachedCamera = targetCamera;
            attachedCamera.KeyDown += new KeyEventHandler(keyPressed);
            attachedCamera.KeyUp += new KeyEventHandler(keyReleased);
            active = true;
            return true;
        }
        /// <summary>
        /// Will Detach this Keybord from the given Camera
        /// </summary>
        public void detachFromCamera(){
            if(active && attachedCamera != null){
                attachedCamera.KeyDown -= new KeyEventHandler(keyPressed);
                attachedCamera.KeyUp -= new KeyEventHandler(keyReleased);
                attachedCamera = null;
                active = false;
            }
        }
        /// <summary>
        /// Will return True if the Key is currently held Down
        /// </summary>
        /// <param name="targetKey">The Keybord Key in question</param>
        /// <returns>If True the targetKey is currently Down. If False the target Key is currently not pressed</returns>
        public bool isKeyDown(Keys targetKey){
            return keysDown[(int)targetKey];
        }
        private void keyPressed(object sender, KeyEventArgs e) {
            if((int)e.KeyData >= 0 && (int)e.KeyData < keysDown.Length){
                keysDown[(int)e.KeyData] = true;
            }
        }
        private void keyReleased(object sender, KeyEventArgs e) {
            if((int)e.KeyData >= 0 && (int)e.KeyData < keysDown.Length){
                keysDown[(int)e.KeyData] = false;
            }
        }
    }
}
