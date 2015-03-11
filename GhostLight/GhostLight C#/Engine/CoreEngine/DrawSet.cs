//Author: Michael Letter

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CustomWindower.CoreEngine {
    /// <summary>
    /// Stores all of the information needed to draw the screen from any camera
    /// and manages the drawing of all of th items required
    /// </summary>
    public class DrawSet {
        /// <summary> Stores the priority que that defines the DrawSet</summary>
        private Drawable[] priorityQue = null;

        /// <summary>
        /// Inititializes the Drawset to have the given number of priorities available
        /// </summary>
        public DrawSet(){
            priorityQue = new Drawable[2];
            priorityQue[0] = new Line();
            priorityQue[0].setDrawSet(this, 0);
            priorityQue[1] = new Line();
            priorityQue[1].setDrawSet(this, 1);
        }
        public int getAvailablePriorities() {
            return priorityQue.Length;
        }
        public bool setAvailablePriorities(int newAvailablePriorities) {
            //No Resize
            if (newAvailablePriorities == priorityQue.Length) {
                return true;
            }
            //Resize
            else if(newAvailablePriorities > 0){
                Drawable[] newPriorityQue = new Drawable[newAvailablePriorities];
                //Shrink
                if(newPriorityQue.Length < newPriorityQue.Length - 1){
                    for(int loop = 0; loop < newPriorityQue.Length - 1; loop++){
                        newPriorityQue[loop] = priorityQue[loop];
                    }
                    newPriorityQue[newPriorityQue.Length - 1] = new Line();
                    newPriorityQue[newPriorityQue.Length - 1].setDrawSet(this, newPriorityQue.Length - 1);
                    for (int loop = priorityQue.Length - 1; loop >= newPriorityQue.Length - 1; loop--) {
                        priorityQue[loop].addListToQueue(newPriorityQue[newPriorityQue.Length - 1].getNextDrawable());
                    }
                }
                //Expand
                else{
                    int loop = 0;
                    while(loop < priorityQue.Length){
                        newPriorityQue[loop] = priorityQue[loop];
                        loop++;
                    }
                    while(loop < newPriorityQue.Length){
                        newPriorityQue[loop] = new Line();
                        newPriorityQue[loop].setDrawSet(this, loop);
                        loop++;
                    }
                }
                priorityQue = newPriorityQue;
                return true;
            }

            return false;


        }
        /// <summary>
        /// Will add the given Drawable Oject to the DrawSet and remove it from any other Drawsets it may be a part of
        /// </summary>
        /// <param name="newDrawable">The Drawable Object to be added</param>
        /// <returns>If True the object was successfully added to the drawset, If False, no changes were made </returns>
        public bool addToDrawSet(Drawable newDrawable){
            if(newDrawable != null){
                if (newDrawable.getPriority() >= priorityQue.Length) {
                    newDrawable.addToQueue(priorityQue[priorityQue.Length - 1]);
                }
                else if (newDrawable.getPriority() < 0) {
                    newDrawable.addToQueue(priorityQue[0]);
                }
                else{
                    newDrawable.addToQueue(priorityQue[newDrawable.getPriority()]);
                }
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will attempt to remove the given object from this DrawSet
        /// </summary>
        /// <param name="removeTarget">the target Object ot be removed</param>
        /// <returns>If True the object was remove successfully, if false the object did not reside in this DrawSet to bgin with</returns>
        public bool removeFromDrawSet(Drawable removeTarget){
            if(removeTarget != null && removeTarget.getDrawSet() == this){
                removeTarget.removeFromDrawSet();
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will Draw All of the objects in the Set in order of priority, 
        /// Note, This function is called automaticly by camera. It is not recomended that you call this function manually, you may interupt a draw in progress
        /// </summary>
        /// <param name="drawTarget">The view everything will b drawn too</param>
        public void paintSet(Camera drawTarget) {
            for (int loop = 0; loop < priorityQue.Length; loop++) {
                Drawable nextDrawable = priorityQue[loop].getNextDrawable();
                while(nextDrawable != null){
                    nextDrawable.paint(drawTarget);
                    nextDrawable = nextDrawable.getNextDrawable();
                }
            }
        }
    }
}
