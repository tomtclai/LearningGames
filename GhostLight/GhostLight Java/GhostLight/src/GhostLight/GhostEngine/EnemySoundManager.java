package GhostLight.GhostEngine;

import Engine.BaseCode;
import Engine.ResourceHandler;


/**
 * Will handel Enemy Sounds and ensure that 
 * @author Michael Letter
 */
class EnemySoundManager {
    public enum SoundType { GHOST_EXPLODE, DRACULA_INTRO, DRACKULA_ATTACK, FRANKENSTIEN_INTRO, MUMMY_INTRO, ZOMBIE_INTRO, CAT_ATTACK, GHOST_INTRO };

    private static String soundsInOrder[] = null;
    private static SoundType enumsInOrder[] = null;
    private static boolean soundRequests[] = null;
    
    private final static String REMOVE_SOUND_FILE = "Sound/ghostlight_remove_tile.wav";
    private final static String DRACULA_INTRO_SOUND_FILE = "Sound/ghostlight_dracular_introduction.wav";
    private final static String DRACULA_ATTACK_SOUND_FILE = "Sound/ghostlight_dracula_attack.wav";
    private final static String FRANK_SOUND_FILE = "Sound/Frank1.wav";
    private final static String MUMMY_SOUND_FILE = "Sound/Mummy.wav";
    private final static String ZOMBIE_SOUND_FILE = "Sound/Zombi1.wav";
    private final static String CAT_ATTACK_SOUND_FILE = "Sound/Meow.wav";
    private final static String GHOST_SOUND_FILE = "Sound/ghostlight_ghost.wav";

    public static void preLoadResources(ResourceHandler resources){
    	resources.preloadSound(REMOVE_SOUND_FILE );
    	resources.preloadSound(DRACULA_INTRO_SOUND_FILE);
    	resources.preloadSound(DRACULA_ATTACK_SOUND_FILE);
        resources.preloadSound(FRANK_SOUND_FILE);
        resources.preloadSound(MUMMY_SOUND_FILE);
        resources.preloadSound(ZOMBIE_SOUND_FILE);
        resources.preloadSound(CAT_ATTACK_SOUND_FILE);
        resources.preloadSound(GHOST_SOUND_FILE);
        
        soundsInOrder = new String[] { REMOVE_SOUND_FILE, DRACULA_INTRO_SOUND_FILE, DRACULA_ATTACK_SOUND_FILE,
        		FRANK_SOUND_FILE, MUMMY_SOUND_FILE, ZOMBIE_SOUND_FILE, CAT_ATTACK_SOUND_FILE,
        		GHOST_SOUND_FILE };
        enumsInOrder = new SoundType[] { SoundType.GHOST_EXPLODE, SoundType.DRACULA_INTRO, SoundType.DRACKULA_ATTACK, SoundType.FRANKENSTIEN_INTRO, SoundType.MUMMY_INTRO, SoundType.ZOMBIE_INTRO, SoundType.CAT_ATTACK, SoundType.GHOST_INTRO };
        soundRequests = new boolean[soundsInOrder.length];
        for(int loop = 0; loop < soundRequests.length; loop++) {
            soundRequests[loop] = false;
        }
    
    }
    /// <summary>
    /// Will request a given sound to be played when ready
    /// </summary>
    /// <param name="targetSound"></param>
    public static void requestSound(SoundType targetSound){
        for (int loop = 0; loop < enumsInOrder.length; loop++) {
            if (enumsInOrder[loop] == targetSound) {
                if(!BaseCode.resources.isSoundPlaying(soundsInOrder[loop])){
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
        boolean soundPlaying = false;
        for(int loop = 0; loop < soundsInOrder.length; loop++) {
            soundPlaying = BaseCode.resources.isSoundPlaying(soundsInOrder[loop]) || soundPlaying;
        }
        if(!soundPlaying){
            for(int loop = 0; loop < soundRequests.length; loop++){
                if (soundRequests[loop]){
                    soundRequests[loop] = false;
                    BaseCode.resources.playSound(soundsInOrder[loop]);
                    break;
                }
            }
        }
    }
    /// <summary>
    /// Will Clear all requests
    /// </summary>
    public static void clear() {
         for(int loop = 0; loop < soundRequests.length; loop++){
             soundRequests[loop] = false;
         }
    }
}

