using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CustomWindower.CoreEngine;

namespace GhostFinder.GhostEngine{
    /// <summary>
    /// Will handel Enemy Sounds and ensure that 
    /// </summary>
    class EnemySoundManager {
        public enum SoundType { GHOST_EXPLODE, DRACULA_INTRO, DRACKULA_ATTACK, FRANKENSTIEN_INTRO, MUMMY_INTRO, ZOMBIE_INTRO, CAT_ATTACK, GHOST_INTRO };

        private static Sound ghostExplode = null;
        private static Sound draculaIntro = null;
        private static Sound draculaAttack = null;
        private static Sound frankenstienIntro = null;
        private static Sound mummyIntro = null;
        private static Sound zombieIntro = null;
        private static Sound catAttack = null;
        private static Sound ghostIntro = null;


        private static Sound[] soundsInOrder = null;
        private static SoundType[] enumsInOrder = null;
        private static bool[] soundRequests = null;


        public static void preLoadResources(ResourceLibrary library){
            //GhostExplode
            if(library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_remove_tile.wav") == null){
                ghostExplode = new Sound("GhostLight/GhostLight/resources/Sounds/ghostlight_remove_tile.wav");
                ghostExplode.loadResource();
                library.addResource(ghostExplode);
            }
            else{
                ghostExplode = (Sound)library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_remove_tile.wav");
            }
            //DraculaIntro
            if(library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_dracular_introduction.wav") == null){
                draculaIntro = new Sound("GhostLight/GhostLight/resources/Sounds/ghostlight_dracular_introduction.wav");
                draculaIntro.loadResource();
                library.addResource(draculaIntro);
            }
            else{
                draculaIntro = (Sound)library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_dracular_introduction.wav");
            }
            //DraculaAttack
            if(library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_dracula_attack.wav") == null){
                draculaAttack = new Sound("GhostLight/GhostLight/resources/Sounds/ghostlight_dracula_attack.wav");
                draculaAttack.loadResource();
                library.addResource(draculaAttack);
            }
            else{
                draculaAttack = (Sound)library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_dracula_attack.wav");
            }
            //FrankenstienIntro
            if (library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_frankenstein.wav") == null) {
                frankenstienIntro = new Sound("GhostLight/GhostLight/resources/Sounds/ghostlight_frankenstein.wav");
                frankenstienIntro.loadResource();
                library.addResource(frankenstienIntro);
            }
            else {
                frankenstienIntro = (Sound)library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_frankenstein.wav");
            }
            //MummyIntro
            if (library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_mummy.wav") == null) {
                mummyIntro = new Sound("GhostLight/GhostLight/resources/Sounds/ghostlight_mummy.wav");
                mummyIntro.loadResource();
                library.addResource(mummyIntro);
            }
            else {
                mummyIntro = (Sound)library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_mummy.wav");
            }
            //ZombieIntro
            if (library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_zombie.wav") == null) {
                zombieIntro = new Sound("GhostLight/GhostLight/resources/Sounds/ghostlight_zombie.wav");
                zombieIntro.loadResource();
                library.addResource(zombieIntro);
            }
            else {
                zombieIntro = (Sound)library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_zombie.wav");
            }
            //CatAttack
            if (library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_cat_attack.wav") == null) {
                catAttack = new Sound("GhostLight/GhostLight/resources/Sounds/ghostlight_cat_attack.wav");
                catAttack.loadResource();
                library.addResource(catAttack);
            }
            else {
                catAttack = (Sound)library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_cat_attack.wav");
            }
            //GhostIntro
            if (library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_ghost.wav") == null) {
                ghostIntro = new Sound("GhostLight/GhostLight/resources/Sounds/ghostlight_ghost.wav");
                ghostIntro.loadResource();
                library.addResource(ghostIntro);
            }
            else {
                ghostIntro = (Sound)library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_ghost.wav");
            }

            soundsInOrder = new Sound[] { ghostExplode, draculaIntro, draculaAttack, frankenstienIntro, mummyIntro, zombieIntro, catAttack, ghostIntro };
            enumsInOrder = new SoundType[] { SoundType.GHOST_EXPLODE, SoundType.DRACULA_INTRO, SoundType.DRACKULA_ATTACK, SoundType.FRANKENSTIEN_INTRO, SoundType.MUMMY_INTRO, SoundType.ZOMBIE_INTRO, SoundType.CAT_ATTACK, SoundType.GHOST_INTRO };
            soundRequests = new bool[soundsInOrder.Length];
            for(int loop = 0; loop < soundRequests.Length; loop++) {
                soundRequests[loop] = false;
            }
        
        }
        /// <summary>
        /// Will request a given sound to be played when ready
        /// </summary>
        /// <param name="targetSound"></param>
        public static void requestSound(SoundType targetSound){
            for (int loop = 0; loop < enumsInOrder.Length; loop++) {
                if (enumsInOrder[loop] == targetSound) {
                    if(!soundsInOrder[loop].isPlaying()){
                        soundRequests[loop] = true;
                        update();
                    }
                    break;
                }
            }
        }
        /// <summary>
        /// Will play the next sound to be played if the previous sound has finished
        /// </summary>
        public static void update() {
            bool soundPlaying = false;
            for(int loop = 0; loop < soundsInOrder.Length; loop++) {
                soundPlaying = soundsInOrder[loop].isPlaying() || soundPlaying;
            }
            if(!soundPlaying){
                for(int loop = 0; loop < soundRequests.Length; loop++){
                    if (soundRequests[loop]){
                        soundRequests[loop] = false;
                        soundsInOrder[loop].playSound();
                        break;
                    }
                }
            }
        }
        /// <summary>
        /// Will Clear all requests
        /// </summary>
        public static void clear() {
             for(int loop = 0; loop < soundRequests.Length; loop++){
                 soundRequests[loop] = false;
             }
        }
    }
}
