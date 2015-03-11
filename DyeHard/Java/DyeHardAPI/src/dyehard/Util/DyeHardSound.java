package dyehard.Util;

import Engine.BaseCode;
import dyehard.Configuration;

public class DyeHardSound {
    private static boolean soundPlay = true;
    private static boolean musicPlay = true;
    private static boolean bgStoppedOnce = false;
    // Music/Sound path strings
    public final static String bgMusicPath = "Audio/BgMusic.wav";
    public final static String pickUpSound = "Audio/PickupSound.wav";
    public final static String powerUpSound = "Audio/Powerup.wav";
    public final static String paintSpraySound = "Audio/PaintSpraySound.wav";
    public final static String enemySpaceship1 = "Audio/EnemySpaceship1.wav";
    public final static String loseSound = "Audio/DyeLose.wav";
    public final static String winSound = "Audio/DyeWin.wav";
    public final static String lifeLostSound = "Audio/LifeLost.wav";
    public final static String portalEnter = "Audio/PortalEnter.wav";
    public final static String portalExit = "Audio/PortalExit.wav";
    public final static String portalLoop = "Audio/PortalLoop.wav";
    public final static String shieldSound = "Audio/ShieldSound.wav";

    static {
        // load sounds for later interactions
        BaseCode.resources.preloadSound(bgMusicPath);
        BaseCode.resources.preloadSound(pickUpSound);
        BaseCode.resources.preloadSound(powerUpSound);
        BaseCode.resources.preloadSound(paintSpraySound);
        BaseCode.resources.preloadSound(enemySpaceship1);
        BaseCode.resources.preloadSound(loseSound);
        BaseCode.resources.preloadSound(winSound);
        BaseCode.resources.preloadSound(lifeLostSound);
        BaseCode.resources.preloadSound(portalEnter);
        BaseCode.resources.preloadSound(portalExit);
        BaseCode.resources.preloadSound(portalLoop);
        BaseCode.resources.preloadSound(shieldSound);

        BaseCode.resources.setSoundVolume(pickUpSound,
                Configuration.pickUpSound);
        BaseCode.resources.setSoundVolume(powerUpSound,
                Configuration.powerUpSound);
        BaseCode.resources.setSoundVolume(paintSpraySound,
                Configuration.paintSpraySound);
        BaseCode.resources.setSoundVolume(enemySpaceship1,
                Configuration.enemySpaceship1);
        BaseCode.resources.setSoundVolume(loseSound, Configuration.loseSound);
        BaseCode.resources.setSoundVolume(winSound, Configuration.winSound);
        BaseCode.resources.setSoundVolume(lifeLostSound,
                Configuration.lifeLostSound);
        BaseCode.resources.setSoundVolume(portalEnter,
                Configuration.portalEnter);
        BaseCode.resources.setSoundVolume(portalExit, Configuration.portalExit);
        BaseCode.resources.setSoundVolume(portalLoop, Configuration.portalLoop);
        BaseCode.resources.setSoundVolume(shieldSound,
                Configuration.shieldSound);
    }

    public static void play(String path) {
        if ((soundPlay) && (!BaseCode.resources.isSoundPlaying(path))) {
            BaseCode.resources.playSound(path);
        }
    }

    public static void playMulti(String path) {
        if (soundPlay) {
            BaseCode.resources.playSound(path);
            BaseCode.resources.setSoundVolume(paintSpraySound,
                    Configuration.paintSpraySound);
        }
    }

    public static void playLoop(String path) {
        if ((soundPlay) && (!BaseCode.resources.isSoundPlaying(path))) {
            BaseCode.resources.playSoundLooping(path);
        }
    }

    public static void stopSound(String path) {
        BaseCode.resources.stopSound(path);
    }

    public static void playBgMusic() {
        if (musicPlay) {
            BaseCode.resources.setSoundVolume(bgMusicPath,
                    Configuration.bgMusicPath);
            if (!bgStoppedOnce) {
                BaseCode.resources.playSoundLooping(bgMusicPath);
            }
        }
    }

    public static void stopBgMusic() {
        if (!musicPlay) {
            BaseCode.resources.setSoundVolume(bgMusicPath, 0f);
            bgStoppedOnce = true;
        }
    }

    public static void setSound(boolean bool) {
        soundPlay = bool;
        if (!bool) {
            DyeHardSound.stopSound(DyeHardSound.portalLoop);
            DyeHardSound.stopSound(DyeHardSound.shieldSound);
            DyeHardSound.stopSound(DyeHardSound.enemySpaceship1);
        }
    }

    public static boolean getSound() {
        return soundPlay;
    }

    public static void setMusic(boolean bool) {
        musicPlay = bool;
    }

    public static boolean getMusic() {
        return musicPlay;
    }
}
