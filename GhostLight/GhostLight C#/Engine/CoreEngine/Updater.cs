//Author: Michael Letter

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Diagnostics;

namespace CustomWindower.CoreEngine{
    /// <summary>
    /// When Started will call update automatically on aseperate thread every at a given Frame rate until stopped
    /// </summary>
    public abstract class Updater {
        /// <summary> Used to call begine Update after a specified amount of time</summary>
        private System.Threading.Timer updateTimer = null;
        /// <summary> The amount of time between ach increment </summary>
        private int updateIncrement = 1500;
        /// <summary> Used to time update calls </summary>
        private Stopwatch watch = null;
        /// <summary> Used as aflag to determine if another update is nessesary </summary>
        private bool running = false;
        /// <summary> Used by end to determine if the last update has completed to determine when it is safe to dispose of resorces </summary>
        private bool confirmedStopped = true; 

        /// <summary> </summary>
        public Updater() {

        }
        /// <summary> Will attempt to stop the updater and deolocate any resources it heald.
        /// Note, This is a blocking call, if the updater can not be stopped this function will hold indefinatly</summary>
        public virtual void Dispose() {
            stop();
            while (!confirmedStopped) {
            }
            if(updateTimer != null){
                updateTimer.Dispose();
            }
        }
        /// <summary>
        /// Sets the number of miliseconds the Updater will space between each update
        /// </summary>
        /// <param name="miliseconds"></param>
        public void setIncrement(int miliseconds){
            updateIncrement = miliseconds;
        }
        /// <summary>
        /// gets the number of miliseconds the Updater will space between each update
        /// </summary>
        /// <returns></returns>
        public int getIncrement(){
            return updateIncrement;
        }
        /// <summary>
        /// Will start the thread managed by this class to call update at the specified intervalled
        /// To stop this cycle stop() can be called
        /// <param name="startImmediatly">if True, the firstUpdate will be called immidiatly after the conclusion of this function,
        /// If false it will be called after the designated timeslice has passed</param>
        /// </summary>
        public void start(bool startImmediatly) {
            running = true;
            confirmedStopped = false;
            int startAt = updateIncrement;
            if(startImmediatly){
                startAt = 0;
            }
            if(watch == null){
                watch = new Stopwatch();
            }
            if(updateTimer == null){
                updateTimer = new System.Threading.Timer(begineUpdate, null, startAt, Timeout.Infinite);
            }
            else{
                updateTimer.Change(startAt, Timeout.Infinite);
            }

        }
        /// <summary>
        /// If the updater has been started this thread will stop it
        /// </summary>
        public void stop(){
            running = false;
        }
        /// <summary>
        /// Used to time update calls and reset the updateTimer to call begineUpdate again if nessisary
        /// </summary>
        /// <param name="state">IDK</param>
        private void begineUpdate(Object state) {
            while (running) {
                //Timing Update
                watch.Reset();
                watch.Start();
                update(state);
                watch.Stop();
                //Desiding how to start next update depending on aloted time
                if(running &&watch.ElapsedMilliseconds < updateIncrement){
                    updateTimer.Change(updateIncrement - watch.ElapsedMilliseconds, Timeout.Infinite);
                    break;
                }
            }
            //Last Update confirm that the updater has stopped
            if(!running) {
                confirmedStopped = true;
            }
        }
        /// <summary> 
        /// Is called at the specified intervals once start() has been called until end(); has been called
        /// </summary>
        public abstract void update(Object state);
    }
}
