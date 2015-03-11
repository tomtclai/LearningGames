//Author: Michael Letter

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CustomWindower.CoreEngine{
    /// <summary>
    /// Defines an object that can be added and drawn as a part of the draw set
    /// Note a single Drawable Object can only be a part of one drawSet
    /// </summary>
    public abstract class Drawable {
        /// <summary>Stores the next DrawableObject</summary>
        private Drawable nextDrawable = null;
        /// <summary>Stores the last DrawableObject</summary>
        private Drawable previousDrawable = null;
        /// <summary> The Draw set this Drawable is currently a part of</summary>
        private DrawSet currentDrawSet = null;
        /// <summary> Determines the order of the  </summary>
        private int priority = 0;

        /// <summary>Will add this Drawable Object to que governed by the given nullItem</summary>
        /// <param name="firstItem"> The FirstDrawable Item in the Que that does not actualy Draw anything</param>
        internal void addToQueue(Drawable previousItem)  {
            if(previousItem != null){
                if(nextDrawable != null || previousDrawable != null){
                    removeFromQue();
                }
                //update self
                nextDrawable = previousItem.nextDrawable;
                if(nextDrawable != null){
                    nextDrawable.previousDrawable = this;
                }
                previousItem.nextDrawable = this;
                previousDrawable = previousItem;
                currentDrawSet = previousItem.currentDrawSet;
                priority = previousItem.priority;
                movedInDrawSet();
            }
        }
        /// <summary>
        /// Will Add this Drawable and all of the Drawables that come after it
        /// to the queue directly after the specified item
        /// </summary>
        /// <param name="previousItem"></param>
        internal void addListToQueue(Drawable previousItem){
            if(previousItem != null){
                //Find LastNode and update nodes all nodes in list
                Drawable lastNode = this;
                while (lastNode.nextDrawable != null){
                    lastNode = lastNode.nextDrawable;
                    lastNode.currentDrawSet = previousItem.currentDrawSet;
                    lastNode.priority = previousItem.priority;
                    movedInDrawSet();
                }
                //disconnect from previous node
                if(previousDrawable != null){
                    previousDrawable.nextDrawable = null;
                }
                //update self
                previousDrawable = previousItem;
                lastNode.nextDrawable = previousItem.nextDrawable;
                currentDrawSet = previousItem.currentDrawSet;
                priority = previousItem.priority;
            }
            movedInDrawSet();
        }
        /// <summary>
        /// Gets the next DrawableObject in the list
        /// </summary>
        /// <returns></returns>
        internal Drawable getNextDrawable() {
            return nextDrawable;
        }
        /// <summary> Will remove this Drawable from whatever que it is a member of</summary>
        internal void removeFromQue() {
            if(nextDrawable != null){
                //middle of que
                if (previousDrawable != null) {
                    nextDrawable.previousDrawable = this.previousDrawable;
                    previousDrawable.nextDrawable = this.nextDrawable;
                }
                //start of que
                else{
                    nextDrawable.previousDrawable = previousDrawable;
                }
            }
            //end of que
            else if(previousDrawable != null){
                previousDrawable.nextDrawable = nextDrawable;
            }
            previousDrawable = null;
            nextDrawable = null;
            currentDrawSet = null;
            movedInDrawSet();
        }
        /// <summary>
        /// Will reset this Drawables currentDrawSet and priority to the following
        /// Note, No error checks are made by this function
        /// </summary>
        /// <param name="newDrawSet"></param>
        /// <param name="priority"></param>
        protected internal void setDrawSet(DrawSet newDrawSet, int newPriority){
            currentDrawSet = newDrawSet;
            this.priority = newPriority;
        }
        /// <summary>
        /// Will attempt to add this Drawable to the given DrawSet.
        /// </summary>
        /// <param name="targetDrawSet">The DrawSet this object will be added to.
        /// Note, if this objects priority is not within the targetDrawSet's range of available priorities it will be adjusted to to the closest one</param>
        /// <returns>If True, this was successfully added to the drawSet, if false no changes were made</returns>
        public bool addToDrawSet(DrawSet targetDrawSet) {
            if(targetDrawSet != null && (currentDrawSet == null || targetDrawSet != currentDrawSet)){
                if(currentDrawSet != null){
                    removeFromDrawSet();
                }
                return targetDrawSet.addToDrawSet(this);
            }
            return false;
        }
        /// <summary>
        /// Is Called when this object has been successfully added to a DrawSet Or when it has been moved within the DrawSet
        /// Be careFull changing priorities or moving objects within a drawSet will call this function repeatedly and may cause Stack Overlfow
        /// </summary>
        protected virtual void movedInDrawSet() {

        }
        /// <summary>
        /// Will remove this DrawableObject from the drawSet it resides in. if it is not in a drawset then no changes are made
        /// </summary>
        public virtual void removeFromDrawSet() {
             removeFromQue();
        }
        /// <summary>
        /// Will return the DrawSet thes Drawable Resides within.
        /// If it not in any Draw set it will return null
        /// </summary>
        /// <returns>Will return the DrawSet thes Drawable Resides within.</returns>
        public DrawSet getDrawSet(){
            return currentDrawSet;
        }
        /// <summary>
        /// Will Return this objects current Priority.
        /// Priority is used to determine draw order, higher priority items are drawn first, lower priority items are drawn last
        /// </summary>
        /// <returns>current Priority</returns>
        public int getPriority() {
            return priority;
        }
        /// <summary>
        /// Will set this object priority.
        /// Priority is used to determine draw order, higher priority items are drawn first, lower priority items are drawn last.
        /// Priority is represented by an inteteger value ranging from zero to the max priority
        /// maxpriority is determined by the 
        /// </summary>
        /// <returns>If True the prioirity was changed successfully, otherwise returns false</returns>
        public bool setPriority(int newPriority) {
            if(newPriority >= 0 && (currentDrawSet == null || newPriority < currentDrawSet.getAvailablePriorities())){
                priority = newPriority;
                if(currentDrawSet != null){
                    DrawSet drawSet = currentDrawSet;
                    removeFromDrawSet();
                    drawSet.addToDrawSet(this);
                }
                return true;
            }
            return false;
        }
        /// <summary> Responsible for Drawing this Drawable </summary>
        /// <param name="drawLocation">The Camera that that this object will Draw too</param>
        public abstract void paint(Camera drawLocation);
    }
}
