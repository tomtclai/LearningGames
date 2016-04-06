
package Engine;

import java.io.ByteArrayInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound
{
  // Max of 4MB
  private boolean isMp3 = false;
  private int MAX_STORED_BYTES = 4194304;

  public Clip theClip = null;
  private byte[] dataBuffer = null;
  private long pauseTime = -1;
  private float volume = 0.0f;
  private boolean isLooping = false;

  /* (non-Javadoc)
 * @see Engine.ISound#loadSound(java.lang.String)
 */
public boolean loadSound(String filename)
  {
	if(filename.toLowerCase().endsWith(".mp3"))
	{
		isMp3 = true;
	}
    try
    {
      theClip = null;

      dataBuffer = BaseCode.resources.readFileBytes(filename);

      if(dataBuffer != null)
      {
        theClip = initFromBytes(dataBuffer,isMp3);

        // If the sound is too long to remember
        if(dataBuffer.length > MAX_STORED_BYTES)
        {
          dataBuffer = null;
        }
      }
    }
    catch(Exception e)
    {
      //e.printStackTrace();
      System.err.println("Error loading '" + filename + "'");
    }

    return (theClip != null);
  }

  /* (non-Javadoc)
 * @see Engine.ISound#didLoad()
 */
  
public boolean didLoad()
  {
    return (theClip != null);
  }

  /* (non-Javadoc)
 * @see Engine.ISound#hasDataBuffer()
 */
  
public boolean hasDataBuffer()
  {
    return (dataBuffer != null);
  }

  /* (non-Javadoc)
 * @see Engine.ISound#start(boolean)
 */
  
public void start(boolean looping)
  {
    if(theClip != null)
    {
      //theClip.stop();
      theClip.flush();
      //theClip.setMicrosecondPosition(0);
      theClip.setFramePosition(0);

      isLooping = looping;

      if(isLooping)
      {
        theClip.loop(Clip.LOOP_CONTINUOUSLY);
      }

      theClip.start();
    }
  }

  /* (non-Javadoc)
 * @see Engine.ISound#start()
 */
  
public void start()
  {
    start(false);
  }

  /* (non-Javadoc)
 * @see Engine.ISound#startLooping()
 */
  
public void startLooping()
  {
    start(true);
  }

  /* (non-Javadoc)
 * @see Engine.ISound#pause()
 */
  
public void pause()
  {
    if(theClip != null)
    {
      if(pauseTime == -1)
      {
        pauseTime = theClip.getLongFramePosition();
        //pauseTime = theClip.getMicrosecondPosition();
      }

      stop();
    }
  }

  /* (non-Javadoc)
 * @see Engine.ISound#resume()
 */
  
public void resume()
  {
    if(theClip != null)
    {
      if(pauseTime != -1)
      {
        theClip.setFramePosition((int)pauseTime);
        //theClip.setMicrosecondPosition(pauseTime);

        pauseTime = -1;
      }

      if(isLooping)
      {
        theClip.loop(Clip.LOOP_CONTINUOUSLY);
      }

      theClip.start();
    }
  }

  /* (non-Javadoc)
 * @see Engine.ISound#mute()
 */
  
public void mute()
  {
    setVolume(0.0f, false);
  }

  /* (non-Javadoc)
 * @see Engine.ISound#unmute()
 */
  
public void unmute()
  {
    setVolume(volume, false);
  }

  /**
   * Set the volume to play the sound at.
   * 
   * @param value
   *          - The volume to play at. Should be between 0.0f and 1.0f
   *          inclusively.
   * @param saveValue
   *          - Should the volume be saved (used for muting and unmuting, use
   *          true otherwise).
   */
  private void setVolume(float value, boolean saveValue)
  {
    if(theClip != null)
    {
      if(value < 0.0f)
      {
        value = 0.0f;
      }

      if(value > 1.0f)
      {
        value = 1.0f;
      }

      if(saveValue)
      {
        volume = value;
      }

      FloatControl control =
          (FloatControl)theClip.getControl(FloatControl.Type.MASTER_GAIN);

      //final float max = control.getMaximum();
      final float max = 0.0f;
      final float min = control.getMinimum();

      final float range = max - min;

      control.setValue((value * range) + min);

      //theClip.flush();

      //pause();
      //resume();
    }
  }

  /* (non-Javadoc)
 * @see Engine.ISound#setVolume(float)
 */
  
public void setVolume(float value)
  {
    setVolume(value, true);
  }

  /* (non-Javadoc)
 * @see Engine.ISound#stop()
 */
  
public void stop()
  {
    if(theClip != null)
    {
      theClip.stop();
      theClip.setFramePosition(0);
    }
  }
  
  /* (non-Javadoc)
 * @see Engine.ISound#destroy()
 */

public void destroy()
  {
	  stop();
	  if (theClip != null)
		  theClip.close();
  }

  /* (non-Javadoc)
 * @see Engine.ISound#isInUse()
 */
public boolean isInUse()
  {
    //return (theClip != null && theClip.isActive());
    return (theClip != null && theClip.isRunning());
  }

  /* (non-Javadoc)
 * @see Engine.ISound#duplicate()
 */
public Sound duplicate()
  {
    Sound toReturn = new Sound();
    toReturn.dataBuffer = dataBuffer;
    toReturn.theClip = initFromBytes(toReturn.dataBuffer, isMp3);

    if(toReturn.theClip == null)
    {
      return null;
    }

    return toReturn;
  }
  
  /**
   * Takes in file data for a sound and initializes the sound with it.
   * 
   * @param data
   *          - The data to initialize with.
   * @return The clip that was created from the data.
   */
  private static Clip initFromBytes(byte[] data, boolean mp3)
  {
    // Get ready for the new data
    Clip toReturn = null;

    // If there isn't new data to use
    if(data != null)
    {
      try
      {
        toReturn = AudioSystem.getClip();

        // Load the audio data into the clip
        ByteArrayInputStream dataStream = new ByteArrayInputStream(data);
        AudioInputStream theStream =
            AudioSystem.getAudioInputStream(dataStream);
        if(mp3)
        {
        	//http://stackoverflow.com/questions/938304/how-to-get-audio-data-from-a-mp3
        	AudioInputStream decodedStream = null;
        	AudioFormat baseFormat = theStream.getFormat();
        	AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);
        	decodedStream = AudioSystem.getAudioInputStream(decodedFormat,theStream);
        	toReturn.open(decodedStream);
        	decodedStream.close();
        }else{
        	toReturn.open(theStream);
        }
        dataStream.close();
        theStream.close();
      }
      catch(Exception e)
      {
        //e.printStackTrace();
        toReturn = null;

        System.err.println("Error initializing sound");
      }
    }

    return toReturn;
  }
}
