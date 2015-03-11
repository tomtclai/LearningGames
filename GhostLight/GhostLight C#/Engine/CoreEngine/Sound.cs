using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Controls;
using System.Windows;
using System.Windows.Media;

namespace CustomWindower.CoreEngine{

    /// <summary>
    /// Can load and play a specific sound
    /// </summary>
    public class Sound : LibraryResource{

        /// <summary> The location of the target sound </summary>
        private Uri soundLocation = null;
        /// <summary>The Sounds managed by this resource</summary>
        private MediaPlayer[] soundPlayers = null;
        /// <summary> When a sound is Started the time it is started is recorded</summary>
        //Note, DateTime.MinValue is used as a null value indicating the sound is not currently running
        private DateTime[] startTime = null;
        /// <summary> Stores the number of instances of the target sound can be played simultaniously</summary>
        private int targetPlayerInstances = 1;
        /// <summary> Returns the number of players that are currently playing sound </summary>
        private int numberOfActivePlayers = 0;


        private delegate void updateSoundPlayer(MediaPlayer target);
        private delegate void updateSoundPlayerDouble(MediaPlayer target, double var);

        /// <summary>
        /// Will create a loadable sound managing the given sound
        /// </summary>
        /// <param name="fileLocation"></param>
        public Sound(string fileLocation) : base(fileLocation, LibraryResource.convertResourceType(ResourceType.SOUND)){
            if(fileLocation != null){
                try{
                    soundLocation = new Uri(AppDomain.CurrentDomain.BaseDirectory + fileLocation);
                }
                catch (UriFormatException e) {
                    Console.Error.WriteLine("ERROR Locating fileLocation: " + e.Message);
                }
            }
        }
        /// <summary>
        /// will set the number of sounds this object can play concurrently
        /// Note, this function does not load the resources nessesary to make this possible
        /// Call loadResource() to do this.
        /// </summary>
        /// <param name="targetNumber"> Must be 1 or greater</param>
        // Yes the function name is giant but we have autocomplete. Wooo!!
        public void setNumberOfConcurrentSounds(int targetNumber){
            if(targetNumber > 1){
                targetPlayerInstances = targetNumber;
            }
        }
        /// <summary>
        /// Will return the number of sounds this object is capable of playing simultainiously
        /// </summary>
        /// <returns></returns>
        public int getPossibleConcurrentSounds(){
            if(soundPlayers != null){
                if(soundPlayers.Length < targetPlayerInstances){
                    return soundPlayers.Length;
                }
                else{
                    return targetPlayerInstances;
                }
            }
            return 0;
        }
        /// <summary>
        /// Returns the number of instances of sound that are currently playing
        /// </summary>
        /// <returns></returns>
        public int getNumberOfInstancesplaying() {
            return numberOfActivePlayers;
        }
        /// <summary>
        /// Will return true if the sound managed by this resource is loaded successfully
        /// </summary>
        /// <returns></returns>
        public override bool isLoaded(){
            return soundPlayers != null;
        }
        /// <summary>
        /// Will attempt to load this resource
        /// </summary>
        public override void loadResource() {
            if (soundLocation != null) {
                int loop = 0;   //Stores which elements have already been looked at
                //Resizing array
                if(soundPlayers == null){
                    soundPlayers = new MediaPlayer[targetPlayerInstances];
                    startTime = new DateTime[targetPlayerInstances];
                }
                else if(soundPlayers.Length < targetPlayerInstances){
                    MediaPlayer[] newPlayerArray = new MediaPlayer[targetPlayerInstances];
                    DateTime[] newPlayingArray = new DateTime[targetPlayerInstances];
                    //Migrating old Players
                    while(loop < soundPlayers.Length){
                        newPlayerArray[loop] = soundPlayers[loop];
                        newPlayingArray[loop] = startTime[loop];
                        loop++;
                    }
                    startTime = newPlayingArray;
                    soundPlayers = newPlayerArray;
                }
                //Creating new players
                while(loop < soundPlayers.Length){
                    if(soundPlayers[loop] == null){
                        soundPlayers[loop] = new MediaPlayer();
                        soundPlayers[loop].Open(soundLocation);
                        soundPlayers[loop].MediaEnded += new EventHandler(updateCurrentlyPlaying);
                        startTime[loop] = DateTime.MinValue;
                    }
                    loop++;
                }
            }
        }
        /// <summary>
        /// Will attempt unload this resource 
        /// </summary>
        public override void unloadResource(){
            if(soundLocation != null){
                for(int loop = 0; loop < soundPlayers.Length; loop++){
                    soundPlayers[loop].MediaEnded -= new EventHandler(updateCurrentlyPlaying);
                    stopSound(loop);
                    soundPlayers[loop].Close();
                    soundPlayers[loop] = null;
                }
            }
            numberOfActivePlayers = 0;
            startTime = null;
            soundPlayers = null;
        }
        /// <summary>
        /// Will return true if at least one instance of this sound is playing
        /// </summary>
        /// <returns></returns>
        public bool isPlaying() {
            return numberOfActivePlayers > 0;
        }
        /// <summary>
        /// Will return whether or not the sound instance in question is currently playing
        /// </summary>
        /// <param name="targetSound">The identifier associated with the sound instace in question</param>
        /// <returns></returns>
        public bool isPlaying(int targetSound){
            if (isLoaded() && targetSound >= 0 && targetSound < getPossibleConcurrentSounds()) {
                return startTime[targetSound] != DateTime.MinValue;
            }
            return false;
        }
        /// <summary>
        /// Will set the Volume of all instances of this sound to the given Level
        /// </summary>
        /// <param name="newVolume">Must be a number btween 0 and 1 representing a value between  0% and 100%</param>
        /// <returns>If True, the Volumn was set. If False no changes were made</returns>
        public bool setVolume(double newVolume) {
            if(isLoaded() && newVolume >= 0 && newVolume<= 1){
                Object[] args = new Object[] { null, newVolume };
                for(int loop = 0; loop < soundPlayers.Length; loop++){
                    updateSoundPlayerDouble volumndelegate = setVolumn;
                    args[0] = soundPlayers[loop];
                    soundPlayers[loop].Dispatcher.Invoke(volumndelegate, args);
                }
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will sey the Volumn of the givn sound instance
        /// </summary>
        /// <param name="newVolume">Must be a number btween 0 and 1 representing a value between  0% and 100%</param>
        /// <param name="targetSound">The identifier associated with the sound instance in question</param>
        /// <returns>If True, the Volumn was set. If False no changes were made</returns>
        public bool setVolume(double newVolume, int targetSound){
            if(isLoaded() && newVolume >= 0 && newVolume <= 1 && targetSound >= 0 && targetSound < soundPlayers.Length){
                updateSoundPlayerDouble volumndelegate = setVolumn;
                soundPlayers[targetSound].Dispatcher.Invoke(volumndelegate, new Object[] {soundPlayers[targetSound], targetSound});
                return true;
            }
            return false;
        }
        /// <summary>
        /// Used to set the volun of a given MediaPlayer
        /// </summary>
        /// <param name="targetPlayer"></param>
        /// <param name="volume"></param>
        private void setVolumn(MediaPlayer targetPlayer, double volume) {
            targetPlayer.Volume = volume;
        }
        /// <summary>
        /// Will play the given sound if the sound is not already playing
        /// If multiple concurrent instances of this sound can be played,
        /// this will start another concurrent instance. If all concurrent Instances are 
        /// currently in use the instance clossest to conclustion will be stopped then restarted
        /// </summary>
        /// <returns>Will return an identifier for the sound instance that was targeted by this fucntion, If, -1 then no new Instances of the sound were started</returns>
        /// <remarks>Note, this function does not assing sound instances, whenever play is called, a sound is played and that does mean that sounds may be restarted prematuraly.
        /// This means it is possible for multiple playSound() to return the same identifire. In fact, this will happen whenever getNumberOfInstancesplaying() == getPossibleConcurrentSounds()</remarks>
        public int playSound(){
            if (isLoaded()){
                DateTime currentMin = DateTime.MaxValue;
                int target = -1;
                for(int loop = 0; loop < soundPlayers.Length; loop++){
                    if(startTime[loop].Equals(DateTime.MinValue)){
                        playSound(loop);
                        return loop;
                    }
                    else if(startTime[loop] < currentMin){
                        currentMin = startTime[loop];
                        target = loop;
                    }
                }
                playSound(target);
                return target;
            }
            return -1;
        }
        /// <summary>
        /// Will Stop the given sound and play it from the begining
        /// </summary>
        /// <param name="targetSound">the identifier of the sound in question</param>
        /// <returns>If True, the operation was successfull, if False, no changes were made</returns>
        public bool playSound(int targetSound) {
            if (isLoaded() && targetSound >= 0 && targetSound < getPossibleConcurrentSounds()) {
                updateSoundPlayer targetDelegate;
                //Stopping sound if nessesary
                if(startTime[targetSound] != DateTime.MinValue){
                    targetDelegate = stopSound;
                    soundPlayers[targetSound].Dispatcher.Invoke(targetDelegate, new Object[] { soundPlayers[targetSound] });
                }
                //starting/restarting sound
                targetDelegate = playSound;
                soundPlayers[targetSound].Dispatcher.Invoke(targetDelegate, new Object[] { soundPlayers[targetSound] });
                //Updating meta data
                startTime[targetSound] = DateTime.Now;
                numberOfActivePlayers++;
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will Stop all sounds that are currently playing from this object
        /// </summary>
        public void stopSound(){
            if (isLoaded()){
                for(int loop = 0; loop < soundPlayers.Length; loop++){
                    stopSound(loop);
                }
            }
        }
        /// <summary>
        /// Will stop the given sound from playing
        /// </summary>
        /// <param name="targetSound">the identifier of the sound in question</param>
        public void stopSound(int targetSound){
            if (isLoaded() && targetSound >= 0 && ((targetSound < getPossibleConcurrentSounds()) || (targetSound < soundPlayers.Length && startTime[targetSound] != DateTime.MinValue))) {
                updateSoundPlayer targetDelegate = stopSound;
                soundPlayers[targetSound].Dispatcher.Invoke(targetDelegate, new Object[] { soundPlayers[targetSound] });
            }
        }
        /// <summary>
        /// Will play the sound used by the given sound player
        /// </summary>
        /// <param name="targetMediaPlayer"></param>
        private void playSound(MediaPlayer targetMediaPlayer){
            targetMediaPlayer.Position = TimeSpan.MinValue;
            targetMediaPlayer.Play();
            
        }
        /// <summary>
        /// Wil stop the sound on the given sound player
        /// </summary>
        /// <param name="targetMediaPlayer"></param>
        private void stopSound(MediaPlayer targetMediaPlayer){
            targetMediaPlayer.Stop();
        }
        /// <summary>
        /// Called when a player is Stopped 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void updateCurrentlyPlaying(object sender, EventArgs e){
            for (int loop = 0; loop < soundPlayers.Length; loop++) {
                if(soundPlayers[loop] == sender){
                    startTime[loop] = DateTime.MinValue;
                    numberOfActivePlayers--;
                    break;
                }
            }
        }
    }
}
