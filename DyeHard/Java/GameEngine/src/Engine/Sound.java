package Engine;

import java.io.ByteArrayInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    // Max of 4MB
    private final int MAX_STORED_BYTES = 4194304;

    public Clip theClip = null;
    private byte[] dataBuffer = null;
    private long pauseTime = -1;
    private float volume = 0.0f;
    private boolean isLooping = false;

    /**
     * Load a sound from the given file name.
     * 
     * @param filename
     *            - The file name to load from.
     * @return - True if the sound was loaded, false otherwise.
     */
    public boolean loadSound(String filename) {
        try {
            theClip = null;

            dataBuffer = BaseCode.resources.readFileBytes(filename);

            if (dataBuffer != null) {
                theClip = initFromBytes(dataBuffer);

                // If the sound is too long to remember
                if (dataBuffer.length > MAX_STORED_BYTES) {
                    dataBuffer = null;
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            System.err.println("Error loading '" + filename + "'");
        }

        return (theClip != null);
    }

    /**
     * Check if the sound loaded.
     * 
     * @return True if the sound loaded, false otherwise.
     */
    public boolean didLoad() {
        return (theClip != null);
    }

    /**
     * Check if the sound data is stored with the sound.
     * 
     * @return True if the sound data was kept loaded, false otherwise.
     */
    public boolean hasDataBuffer() {
        return (dataBuffer != null);
    }

    /**
     * Start playing. Can loop if wanted.
     * 
     * @param looping
     *            - True if the sound should loop, false otherwise.
     */
    public void start(boolean looping) {
        if (theClip != null) {
            // theClip.stop();
            theClip.flush();
            // theClip.setMicrosecondPosition(0);
            theClip.setFramePosition(0);

            isLooping = looping;

            if (isLooping) {
                theClip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            theClip.start();
        }
    }

    /**
     * Starts or restarts the audio clip.
     */
    public void start() {
        start(false);
    }

    /**
     * Starts or restarts the audio clip as looping.
     */
    public void startLooping() {
        start(true);
    }

    /**
     * Pause playing the sound.
     */
    public void pause() {
        if (theClip != null) {
            if (pauseTime == -1) {
                pauseTime = theClip.getLongFramePosition();
                // pauseTime = theClip.getMicrosecondPosition();
            }

            stop();
        }
    }

    /**
     * Resume playing the sound.
     */
    public void resume() {
        if (theClip != null) {
            if (pauseTime != -1) {
                theClip.setFramePosition((int) pauseTime);
                // theClip.setMicrosecondPosition(pauseTime);

                pauseTime = -1;
            }

            if (isLooping) {
                theClip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            theClip.start();
        }
    }

    /**
     * Mute sound volume.
     */
    public void mute() {
        setVolume(0.0f, false);
    }

    /**
     * Unmute sound volume.
     */
    public void unmute() {
        setVolume(volume, false);
    }

    /**
     * Set the volume to play the sound at.
     * 
     * @param value
     *            - The volume to play at. Should be between 0.0f and 1.0f
     *            inclusively.
     * @param saveValue
     *            - Should the volume be saved (used for muting and unmuting,
     *            use true otherwise).
     */
    private void setVolume(float value, boolean saveValue) {
        if (theClip != null) {
            if (value < 0.0f) {
                value = 0.0f;
            }

            if (value > 1.0f) {
                value = 1.0f;
            }

            if (saveValue) {
                volume = value;
            }

            FloatControl control = (FloatControl) theClip
                    .getControl(FloatControl.Type.MASTER_GAIN);

            // final float max = control.getMaximum();
            final float max = 1.0f;
            final float min = control.getMinimum();

            final float range = max - min;

            control.setValue((value * range) + min);

            // theClip.flush();

            // pause();
            // resume();
        }
    }

    /**
     * Set the volume to play the sound at.
     * 
     * @param value
     *            - The volume to play at. Should be between 0.0f and 1.0f
     */
    public void setVolume(float value) {
        setVolume(value, true);
    }

    /**
     * Stop playing the sound.
     */
    public void stop() {
        if (theClip != null) {
            theClip.stop();
            theClip.setFramePosition(0);
        }
    }

    /**
     * Check if the audio clip is in use.
     * 
     * @return - True if in use, false otherwise.
     */
    public boolean isInUse() {
        // return (theClip != null && theClip.isActive());
        return (theClip != null && theClip.isRunning());
    }

    /**
     * Duplicates the current sound
     * 
     * @return - The duplicate sound. Will be null if the sound data was not
     *         kept previously or if there was a problem.
     */
    public Sound duplicate() {
        Sound toReturn = new Sound();
        toReturn.dataBuffer = dataBuffer;
        toReturn.theClip = initFromBytes(toReturn.dataBuffer);

        if (toReturn.theClip == null) {
            return null;
        }

        return toReturn;
    }

    /**
     * Takes in file data for a sound and initializes the sound with it.
     * 
     * @param data
     *            - The data to initialize with.
     * @return The clip that was created from the data.
     */
    private static Clip initFromBytes(byte[] data) {
        // Get ready for the new data
        Clip toReturn = null;

        // If there isn't new data to use
        if (data != null) {
            try {
                toReturn = AudioSystem.getClip();

                // Load the audio data into the clip
                ByteArrayInputStream dataStream = new ByteArrayInputStream(data);
                AudioInputStream theStream = AudioSystem
                        .getAudioInputStream(dataStream);

                toReturn.open(theStream);
            } catch (Exception e) {
                // e.printStackTrace();
                toReturn = null;

                System.err.println("Error initializing sound");
            }
        }

        return toReturn;
    }
}
