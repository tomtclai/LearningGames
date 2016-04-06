package Engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.imageio.ImageIO;

public class ResourceHandler {
	private final int MAX_SAME_SOUND_INSTANCES = 50;

	private Graphics currentGFX = null;
	private Graphics2D currentGFX2D = null;
	private AffineTransform tempTrans = new AffineTransform();

	private Map<String, BufferedImage> textures = new HashMap<String, BufferedImage>();
	private Map<String, ArrayList<Sound>> sounds = new HashMap<String, ArrayList<Sound>>();

	private boolean isMuted = false;
	private float soundVolume = 1.0f;

	private Vector<Primitive> drawSet = new Vector<Primitive>();

	// Path to the working directory, will be combined with
	// resourcePath to get the full path to a resource.
	public String basePath = "";

	// Possible locations that resources will be located.
	// Working directory for running applet with Eclipse is different than
	// running an application.
	private final String resourcePath[] = { "resources/", "../resources/", "bin/resources/" };

	private World world = null;

	private Vector<Font> fonts = new Vector<Font>();
	private Font activeFont = new Font("Times New Roman", Font.BOLD, 24);

	private Object classInJar = null;

	private Color textBack = Color.WHITE;
	private Color textFront = Color.BLACK;

	/**
	 * In order to load resources from inside a jar file, a class will be needed
	 * from inside the jar.
	 * 
	 * @param theObj
	 *            - A dummy object that is from inside the desired jar.
	 */
	public void setClassInJar(Object theObj) {
		classInJar = theObj;
	}

	/**
	 * Adds the given primitive to the draw set to be automatically drawn each
	 * frame. The primitive will be drawn in front of all other primitives if it
	 * is marked to be always on top. If not marked to be always on top, it will
	 * be on top of all primitives except those marked as always on top.
	 * 
	 * @param addPrimitive
	 *            - A primitive to draw automatically each frame.
	 */
	public void addToAutoDrawSet(Primitive addPrimitive) {
		if (addPrimitive != null && !drawSet.contains(addPrimitive)) {
			boolean foundSpot = false;

			// If the primitive doesn't request to be drawn on top of everything
			if (!addPrimitive.alwaysOnTop) {
				// Find the first element that doesn't request to be on top
				for (int i = drawSet.size() - 1; i >= 0; i--) {
					if (!drawSet.get(i).alwaysOnTop) {
						foundSpot = true;

						drawSet.add(i + 1, addPrimitive);

						break;
					}
				}

				// If no elements existed that don't request to be on top
				if (!foundSpot) {
					drawSet.add(0, addPrimitive);
				}
			} else {
				// Add to the top of the draw set
				drawSet.add(addPrimitive);
			}
		}
	}

	/**
	 * Removes the given primitive from the draw set. Primitives in the draw set
	 * are automatically drawn each frame.
	 * 
	 * @param addPrimitive
	 *            - Primitive to remove from the draw set.
	 */
	public void removeFromAutoDrawSet(Primitive addPrimitive) {
		drawSet.remove(addPrimitive);
	}

	/**
	 * Moves the primitive to be drawn behind all other primitives in the draw
	 * set.
	 * 
	 * @param thePrimitive
	 *            - The primitive to move to the back.
	 */
	public void moveToBackOfDrawSet(Primitive thePrimitive) {
		removeFromAutoDrawSet(thePrimitive);
		drawSet.add(0, thePrimitive);
	}

	/**
	 * Move the primitive to be drawn in front of all other primitives. The
	 * primitive will be drawn in front of all other primitives if it is marked
	 * to be always on top. If not marked to be always on top, it will be on top
	 * of all primitives except those marked as always on top.
	 * 
	 * @param thePrimitive the primitive to be moved to the front of drawset.
	 */
	public void moveToFrontOfDrawSet(Primitive thePrimitive) {
		removeFromAutoDrawSet(thePrimitive);
		addToAutoDrawSet(thePrimitive);
	}

	/**
	 * Draws all primitives in the draw set.
	 */
	public void drawDrawSet() {
		Primitive current = null;

		for (int i = 0; i < drawSet.size(); i++) {
			current = drawSet.get(i);

			if (current != null) {
				current.drawPrimitive();
			}
		}
	}

	/**
	 * Set the graphics which resources should be drawn with.
	 * 
	 * @param newGraphics
	 *            - The graphics to draw with.
	 */
	public void setGraphics(Graphics newGraphics) {
		currentGFX = newGraphics;
		currentGFX2D = (Graphics2D) currentGFX;
	}

	/**
	 * Set the world to use to convert from world space to pixel space.
	 * 
	 * @param aWorld
	 *            - The world to use for conversions internally.
	 */
	public void setWorld(World aWorld) {
		world = aWorld;
	}

	/**
	 * Get an input stream to the given file name. Files not inside a jar will
	 * have priority over those inside a jar.
	 * 
	 * @param fileName
	 *            - The file name to get the stream for.
	 * @return - An input stream to the given resource. Will be null is there
	 *         was a problem.
	 */
	private InputStream getResourceStream(String fileName) {
		InputStream toReturn = null;

	/*
	 * This code commented out here was refactored to remove URL-based file IO in favor of 
	 * InputStreams ( FileInputStreams )
	 * 
	 * 	URL url = checkResourcePathOutsideJar(fileName);

		// Try loading from a URL outside the jar
		if (url != null) {
			try {
				// Try opening the stream
				toReturn = url.openStream();
			} catch (IOException e) {
				// e.printStackTrace();

				toReturn = null;
			}
		}*/
		toReturn = checkResourcePathOutsideJar(fileName);
		
		// If a stream was not created, check inside the jar
		if (toReturn == null) {
			toReturn = checkResourcePathInJar(fileName);
		}
		
		return toReturn;
	}
	
	
	
	/*
	 * 
	 * This method was evolved to use FileInputStreams over URIs/URLs
	 * due to a path issue 
	 * 
	 */
	private InputStream checkResourcePathOutsideJar(String fileName) {
		// Get the path to the desired file
		FileInputStream fio= null;

		for (int i = 0; i < resourcePath.length; i++) {
			try {		
				String pathAndFile = basePath + resourcePath[i] + fileName;
				fio = new FileInputStream(pathAndFile);
				
			} catch (FileNotFoundException e1) {  //skip this on purpose
			} catch (IOException e) {
				System.out.println( "Died in checkResource");
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}

		return fio;
	}

	
	

	/**
	 * (Refactored out so we use FIOs in place of URIs)
	 * Checks if the given file exists outside of a jar.  
	 * 
	 * @param fileName
	 *            - The file to check if it exists.
	 * @return - URL to the file. Will be null if there was a problem.
	 */
	/*private URL checkResourcePathOutsideJar(String fileName) {
		// Get the path to the desired file
		URL url;

		for (int i = 0; i < resourcePath.length; i++) {
			try {
				url = new URL(basePath + resourcePath[i] + fileName);

				// Check that the file exists
				URLConnection in = url.openConnection();

				if (in.getContentLengthLong() > 0) {
					return url;
				}
			} catch (MalformedURLException e1) {
			} catch (IOException e) {
			}
		}

		return null;
	}*/

	/**
	 * Checks if the given file exists inside of a jar.
	 * 
	 * @param fileName
	 *            - The file to check if it exists.
	 * @return - InputStream for the file. Will be null if there was a problem.
	 */
	private InputStream checkResourcePathInJar(String fileName) {
		if (classInJar != null) {
			for (int i = 0; i < resourcePath.length; i++) {
				InputStream inStream = classInJar.getClass()
						.getResourceAsStream(resourcePath[i] + fileName);

				if (inStream != null) {
					return inStream;
				}
			}
		}

		return null;
	}

	/**
	 * Read the given file name and return the contents as bytes.
	 * 
	 * @param filename
	 *            - The file to read.
	 * @return - The contents of the given file. Will be null if there was a
	 *         problem.
	 */
	public byte[] readFileBytes(String filename) {
		InputStream inStream = getResourceStream(filename);

		if (inStream != null) {
			return readFileBytes(inStream);
		}

		System.err.println("Error accessing file: '" + filename + "'");
		MessageOnce.showAlert("Error accessing file: '" + filename + "'");

		return null;
	}

	/**
	 * Read all the data from the given InputStream and return the contents as
	 * bytes.
	 * 
	 * @param inStream
	 *            - The stream to read from.
	 * @return - The contents of the given InputStream. Will be null if there
	 *         was a problem.
	 */
	private byte[] readFileBytes(InputStream inStream) {
		// Buffer size of 4MB
		final int READ_SIZE = 1024 * 1024 * 4;

		byte[] toReturn = null;

		if (inStream != null) {
			final ByteArrayOutputStream dupeBuffer = new ByteArrayOutputStream();
			final byte[] dupeData = new byte[READ_SIZE];
			int loadedAmount = 0;
			try {
				while ((loadedAmount = inStream.read(dupeData)) != -1) {
					dupeBuffer.write(dupeData, 0, loadedAmount);
				}
				dupeBuffer.flush();
				toReturn = dupeBuffer.toByteArray();
				dupeBuffer.reset();
				
				inStream.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}

		return toReturn;
	}

	/**
	 * Cleans any resources that should not exists if the program were to close.
	 * Stops and clears all loaded sounds.
	 */
	public void clean() {
		while (drawSet.size() > 0) {
			drawSet.remove(0).destroy();
		}

		// Audio support clean up
		Iterator<Entry<String, ArrayList<Sound>>> iter = sounds.entrySet()
				.iterator();

		while (iter.hasNext()) {
			ArrayList<Sound> soundCollection = iter.next().getValue();

			for (int i = 0; i < soundCollection.size(); i++) {
				soundCollection.get(i).stop();
				soundCollection.get(i).destroy();
			}
		}
		sounds.clear();

		// File texture clean up ...
		Iterator<Entry<String, BufferedImage>> itTex = textures.entrySet().iterator();
		while (itTex.hasNext()) {
			BufferedImage b = itTex.next().getValue();
			if (b != null)
				b.getGraphics().dispose();
		}
		textures.clear();

		fonts.clear();
	}

	/**
	 * Loads and stores a sound to be played later.
	 * 
	 * @param fileName
	 *            - The file name of the sound to be loaded.
	 * @param numberToPreload
	 *            - How many duplicates of the sound should be loaded. This is
	 *            basically an estimate of the maximum number of this sound that
	 *            might play at the same time. If the estimate was not high
	 *            enough, duplicates will be created as needed.
	 */
	public void preloadSound(String fileName, int numberToPreload) {
		// Need to preload at least one
		if (numberToPreload < 1) {
			numberToPreload = 1;
		}

		// Check if the sound has been loaded before
		ArrayList<Sound> soundCollection = sounds.get(fileName);

		int numLoaded = 0;

		// If it wasn't loaded before
		if (soundCollection == null) {
			// Create a new collection for the sound
			soundCollection = new ArrayList<Sound>();
			sounds.put(fileName, soundCollection);

			// Only create sounds if under the limit
			if (MAX_SAME_SOUND_INSTANCES > 0) {
				// Create the new sound
				Sound newSound = new Sound();

				// Load the sound data
				newSound.loadSound(fileName);

				soundCollection.add(newSound);

				numLoaded += 1;
			}
		} else {
			// Uncomment if "numberToPreload" should be the
			// total number loaded rather than additional amount to load
			// numLoaded = soundCollection.size();
		}

		Sound dupeFrom = null;

		// Check if any instances have the sound data to duplicate from
		for (int i = 0; i < soundCollection.size(); i++) {
			if (soundCollection.get(i).hasDataBuffer()) {
				dupeFrom = soundCollection.get(i);

				break;
			}
		}

		// Duplicate the sound the requested number of times
		for (; numLoaded < numberToPreload; numLoaded++) {
			// Only create sounds if under the limit
			if (soundCollection.size() >= MAX_SAME_SOUND_INSTANCES) {
				break;
			}

			// If there is data to duplicate from
			if (dupeFrom != null) {
				// Duplicate the sound
				Sound newSound = dupeFrom.duplicate();
				soundCollection.add(newSound);
			} else {
				// Create the new sound
				Sound newSound = new Sound();

				// Load the sound data
				newSound.loadSound(fileName);

				soundCollection.add(newSound);
			}
		}
	}

	/**
	 * Loads and stores a sound to be played later. Default to only loading one
	 * instance of the sound.
	 * 
	 * @param fileName
	 *            - The file to load the sound from.
	 */
	public void preloadSound(String fileName) {
		preloadSound(fileName, 1);
	}

	/**
	 * Will return true if the given sound is currently playing
	 * @param fileName: filename of the audio file to be tested.
	 * @return true: sounds is playing, false otherwise
	 */
	public boolean isSoundPlaying(String fileName) {
		boolean retVal = false;
		ArrayList<Sound> soundCollection = sounds.get(fileName);
		if (soundCollection != null) {
			for (int loop = 0; loop < soundCollection.size(); loop++) {
				retVal = soundCollection.get(loop).isInUse() || retVal;
			}
		}
		return retVal;
	}

	/**
	 * Get an available instance of the given sound. Load more copies of the
	 * sound if there were no available instances.
	 * 
	 * @param fileName
	 *            - The file name of the sound to get an available instance for.
	 * @return - An available instance of the given sound. Will be null if no
	 *         more instances of the sound may exist (Hit the preset limit on
	 *         number of instances) or there was a problem.
	 */
	private Sound getAvailableSound(String fileName) {
		// Check if the sound has been loaded before
		ArrayList<Sound> soundCollection = sounds.get(fileName);

		// If it hasn't been loaded before
		if (soundCollection == null) {
			// Load it in
			preloadSound(fileName);

			soundCollection = sounds.get(fileName);
		}

		//
		// Check if any sounds in the collection are available
		//

		Sound soundWithData = null;
		Sound curSound = null;

		// Check if any instances of the sound are not in use
		for (int i = 0; i < soundCollection.size(); i++) {
			curSound = soundCollection.get(i);

			// If not in use
			if (!curSound.isInUse()) {
				return soundCollection.get(i);
			}
			// If this instance has a copy of the sound data
			else if (soundWithData == null && curSound.hasDataBuffer()) {
				// Remember this sound instance
				soundWithData = curSound;
			}
		}
		//
		// If there was a sound instance with the sound data then copy it.
		// If not, then load from disk if the sound was loaded successfully
		// before.
		//

		curSound = null;

		// Only create sounds if under the limit
		if (soundCollection.size() < MAX_SAME_SOUND_INSTANCES) {
			// If there is a sound instance with the sound data
			if (soundWithData != null) {
				// Duplicate the sound
				curSound = soundWithData.duplicate();
				soundCollection.add(curSound);
			} else {
				// If the sound was able to load previously
				if (soundCollection.get(0).didLoad()) {
					// Load another copy of the sound
					preloadSound(fileName);

					curSound = soundCollection.get(soundCollection.size() - 1);

					// If the sound failed to load this time, then there is a
					// problem
					if (curSound != null && !curSound.didLoad()) {
						curSound = null;
					}
				}
			}
		}

		return curSound;
	}

	/**
	 * Plays the sound at the given file name. If the sound has loaded
	 * previously, it will not be loaded from disk again.
	 * 
	 * @param fileName
	 *            - The file name of the sound to play.
	 * @return - Return true if the sound was told to start playing. Will return
	 *         false if there was a problem or the sound could not be played
	 *         (possibly for internal reasons).
	 */
	public boolean playSound(String fileName) {
		return playSound(fileName, false);
	}

	/**
	 * Plays the sound at the given file name. If the sound has loaded
	 * previously, it will not be loaded from disk again. The sound will restart
	 * after it reaches the end of the sound clip.
	 * 
	 * @param fileName
	 *            - The file name of the sound to play.
	 * @return - Return true if the sound was told to start playing. Will return
	 *         false if there was a problem or the sound could not be played
	 *         (possibly for internal reasons).
	 */
	public boolean playSoundLooping(String fileName) {
		return playSound(fileName, true);
	}

	/**
	 * Plays the sound at the given file name. If the sound has loaded
	 * previously, it will not be loaded from disk again. The sound may be set
	 * to repeat itself.
	 * 
	 * @param fileName
	 *            - The file name of the sound to play.
	 * @param playInLoop
	 *            - Set to true if the sound should repeat after it plays.
	 * @return - Return true if the sound was told to start playing. Will return
	 *         false if there was a problem or the sound could not be played
	 *         (possibly for internal reasons).
	 */
	public boolean playSound(String fileName, boolean playInLoop) {
		Sound theSound = getAvailableSound(fileName);

		if (theSound != null) {
			// System.out.println("Start: " + fileName);

			theSound.setVolume(soundVolume);

			if (isMuted) {
				theSound.mute();
			}

			theSound.start(playInLoop);
		} else {
			System.out
					.println("Too many of the same sound playing at the same time!");
		}

		return true;
	}

	/**
	 * Stops all instances of the given sound.
	 * 
	 * @param fileName
	 *            - The file name of the sound to stop.
	 * @return - True if the sound was at least loaded at some point, false
	 *         otherwise.
	 */
	public boolean stopSound(String fileName) {
		// Check if the sound has been loaded before
		ArrayList<Sound> soundCollection = sounds.get(fileName);

		// If it hasn't been loaded before
		if (soundCollection != null) {
			// Check if any instances of the sound are not in use
			for (int i = 0; i < soundCollection.size(); i++) {
				soundCollection.get(i).stop();
			}

			return true;
		}

		return false;
	}

	/**
	 * Pauses or resumes all instances of the given sound.
	 * 
	 * @param fileName
	 *            - The file name of the sound to pause or resume.
	 * @param mute
	 *            - True if the sound should paused. False if the sound should
	 *            resume.
	 * @return - True if the sound has been loaded before, false otherwise.
	 */
	private boolean pauseSound(String fileName, boolean paused) {
		// Check if the sound has been loaded before
		ArrayList<Sound> soundCollection = sounds.get(fileName);

		// If it hasn't been loaded before
		if (soundCollection != null) {
			// Check if any instances of the sound are not in use
			for (int i = 0; i < soundCollection.size(); i++) {
				if (paused) {
					soundCollection.get(i).pause();
				} else {
					soundCollection.get(i).resume();
				}
			}

			return true;
		}

		return false;
	}

	/**
	 * Pauses or resumes all instances of all sounds.
	 * 
	 * @param value
	 *            - True if sounds should pause, false to resume.
	 */
	public void setPauseSound(boolean value) {
		Iterator<Entry<String, ArrayList<Sound>>> iter = sounds.entrySet()
				.iterator();

		while (iter.hasNext()) {
			pauseSound((String) iter.next().getKey(), value);
		}
	}

	/**
	 * Pause all sounds.
	 */
	public void pauseSound() {
		setPauseSound(true);
	}

	/**
	 * Resume all sounds.
	 */
	public void resumeSound() {
		setPauseSound(false);
	}

	/**
	 * Mutes or unmutes all instances of the given sound.
	 * 
	 * @param fileName
	 *            - The file name of the sound to mute.
	 * @param mute
	 *            - True if the sound should mute. False if the sound should
	 *            unmute.
	 * @return - True if the sound has been loaded before, false otherwise.
	 */
	private boolean muteSound(String fileName, boolean mute) {
		// Check if the sound has been loaded before
		ArrayList<Sound> soundCollection = sounds.get(fileName);

		// If it hasn't been loaded before
		if (soundCollection != null) {
			// Check if any instances of the sound are not in use
			for (int i = 0; i < soundCollection.size(); i++) {
				if (mute) {
					soundCollection.get(i).mute();
				} else {
					soundCollection.get(i).unmute();
				}
			}

			return true;
		}

		return false;
	}

	/**
	 * Mutes or unmutes all instances of all sounds. Any sounds loaded after
	 * calling this will be set to the same mute value.
	 * 
	 * @param value
	 *            - True if sounds should mute, false otherwise.
	 */
	public void setMuteSound(boolean value) {
		isMuted = value;

		Iterator<Entry<String, ArrayList<Sound>>> iter = sounds.entrySet()
				.iterator();

		while (iter.hasNext()) {
			muteSound((String) iter.next().getKey(), isMuted);
		}
	}

	/**
	 * Mute all sounds.
	 */
	public void muteSound() {
		setMuteSound(true);
	}

	/**
	 * Unmute all sounds.
	 */
	public void unmuteSound() {
		setMuteSound(false);
	}

	/**
	 * Toggle if all sounds are muted.
	 */
	public void toggleMuteSound() {
		setMuteSound(!isMuted);
	}

	/**
	 * Check if sounds are set to be muted.
	 * 
	 * @return - True if sounds are set as mute, false otherwise.
	 */
	public boolean getMuteSound() {
		return isMuted;
	}

	/**
	 * Set the volume for all instances of the given sound.
	 * 
	 * @param fileName
	 *            - File name of the sound to change volume for.
	 * @param volume
	 *            - The volume to change to, should be between 0.0f amd 1.0f
	 *            inclusively.
	 * @return - True if the sound has been loaded before, false otherwise.
	 */
	public boolean setSoundVolume(String fileName, float volume) {
		// Check if the sound has been loaded before
		ArrayList<Sound> soundCollection = sounds.get(fileName);

		// If it hasn't been loaded before
		if (soundCollection != null) {
			// Check if any instances of the sound are not in use
			for (int i = 0; i < soundCollection.size(); i++) {
				soundCollection.get(i).setVolume(volume);
			}

			return true;
		}

		return false;
	}

	/**
	 * Set the volume for all instances of all sounds. Sounds played after this
	 * has been set will also have the same volume.
	 * 
	 * @param volume
	 *            - The volume to set all sounds to. Should be between 0.0f amd
	 *            1.0f inclusively.
	 */
	public void setSoundVolume(float volume) {
		soundVolume = volume;

		Iterator<Entry<String, ArrayList<Sound>>> iter = sounds.entrySet()
				.iterator();

		while (iter.hasNext()) {
			setSoundVolume((String) iter.next().getKey(), volume);
		}
	}

	/**
	 * Get the volume that sounds are set to.
	 * 
	 * @return - The volume of all sounds.
	 */
	public float getSoundVolume() {
		return soundVolume;
	}

	/**
	 * Sleep for a set number of miliseconds.
	 * 
	 * @param miliseconds
	 *            - Number of miliseconds to sleep.
	 */
	public void sleep(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
		}
	}

	/*
	 * public void drawImage(BufferedImage texture, float dstX, float dstY) {
	 * if(currentGFX == null || texture == null) { return; }
	 * 
	 * currentGFX.drawImage(texture, (int)dstX, (int)dstY, null); }
	 */

	/**
	 * Draw an image with the given lower left and upper right coordinates and
	 * with the given rotation. Coordinates should be in world space.
	 * 
	 * @param texture
	 *            - The image to draw.
	 * @param x1
	 *            - Lower left x coordinate.
	 * @param y1
	 *            - Lower left y coordinate.
	 * @param x2
	 *            - Upper right x coordinate.
	 * @param y2
	 *            - Upper right y coordinate.
	 * @param degrees
	 *            - Number of degrees to rotate.
	 */
	public void drawImage(BufferedImage texture, float x1, float y1, float x2,
			float y2, float degrees) {
		if (currentGFX == null || texture == null) {
			return;
		}

		// Convert coords
		{
			// Convert from world coords to screen coords
			x1 = world.worldToScreenX(x1);
			y1 = world.worldToScreenY(y1);
			x2 = world.worldToScreenX(x2);
			y2 = world.worldToScreenY(y2);

			// Swap upper and lower Y values
			float temp = y1;
			y1 = y2;
			y2 = temp;
		}

		float halfW = (x2 - x1) * 0.5f;
		float halfH = (y2 - y1) * 0.5f;

		AffineTransform savedTrans = currentGFX2D.getTransform();

		tempTrans.setTransform(savedTrans);
		tempTrans.translate(x1 + halfW, y1 + halfH);
		tempTrans.rotate(Math.toRadians(-degrees));
		currentGFX2D.transform(tempTrans);

		currentGFX2D.drawImage(texture, -(int) halfW, -(int) halfH,
				(int) halfW, (int) halfH, 0, 0, texture.getWidth(),
				texture.getHeight(), null);

		// currentGFX.drawImage(texture, -(int)halfW, -(int)halfH, (int)halfW,
		// (int)halfH, 0, 0, texture.getWidth(), texture.getHeight(), null);

		currentGFX2D.setTransform(savedTrans);
	}

	/**
	 * Draw an image with the given lower left and upper right coordinates and
	 * with the given rotation. Coordinates should be in world space. Also
	 * provided are the coordinates of the source image to draw from.
	 * 
	 * @param texture
	 *            - The image to draw.
	 * @param x1
	 *            - Lower left x coordinate.
	 * @param y1
	 *            - Lower left y coordinate.
	 * @param x2
	 *            - Upper right x coordinate.
	 * @param y2
	 *            - Upper right y coordinate.
	 * @param srcX1
	 *            - Source image x start position.
	 * @param srcY1
	 *            - Source image y start position.
	 * @param srcX2
	 *            - Source image x end position.
	 * @param srcY2
	 *            - Source image y end position.
	 * @param degrees
	 *            - Number of degrees to rotate.
	 */
	public void drawImage(BufferedImage texture, float x1, float y1, float x2,
			float y2, int srcX1, int srcY1, int srcX2, int srcY2, float degrees) {
		if (currentGFX == null || texture == null) {
			return;
		}

		// Convert coords
		{
			// Convert from world coords to screen coords
			x1 = world.worldToScreenX(x1);
			y1 = world.worldToScreenY(y1);
			x2 = world.worldToScreenX(x2);
			y2 = world.worldToScreenY(y2);

			// Swap upper and lower Y values
			float temp = y1;
			y1 = y2;
			y2 = temp;
		}

		float halfW = (x2 - x1) * 0.5f;
		float halfH = (y2 - y1) * 0.5f;

		AffineTransform savedTrans = currentGFX2D.getTransform();

		tempTrans.setTransform(savedTrans);
		tempTrans.translate(x1 + halfW, y1 + halfH);
		tempTrans.rotate(Math.toRadians(-degrees));
		currentGFX2D.transform(tempTrans);

		currentGFX2D.drawImage(texture, -(int) halfW, -(int) halfH,
				(int) halfW, (int) halfH, srcX1, srcY1, srcX2, srcY2, null);

		// currentGFX.drawImage(texture, -(int)halfW, -(int)halfH, (int)halfW,
		// (int)halfH, srcX1, srcY1, srcX2, srcY2, null);

		currentGFX2D.setTransform(savedTrans);
	}

	/**
	 * Draw an image with the given lower left and upper right coordinates and
	 * with the given rotation. Coordinates should be in world space. Also
	 * provided are the coordinates (as a percentage of the dimensions) of the
	 * source image to draw from.
	 * 
	 * @param texture
	 *            - The image to draw.
	 * @param x1
	 *            - Lower left x coordinate.
	 * @param y1
	 *            - Lower left y coordinate.
	 * @param x2
	 *            - Upper right x coordinate.
	 * @param y2
	 *            - Upper right y coordinate.
	 * @param srcX1
	 *            - Source image x start percent.
	 * @param srcY1
	 *            - Source image y start percent.
	 * @param srcX2
	 *            - Source image x end percent.
	 * @param srcY2
	 *            - Source image y end percent.
	 * @param degrees
	 *            - Number of degrees to rotate.
	 */
	public void drawImage(BufferedImage texture, float x1, float y1, float x2,
			float y2, float srcX1, float srcY1, float srcX2, float srcY2,
			float degrees) {
		if (currentGFX == null || texture == null) {
			return;
		}

		// Convert coords
		{
			// Convert from world coords to screen coords
			x1 = world.worldToScreenX(x1);
			y1 = world.worldToScreenY(y1);
			x2 = world.worldToScreenX(x2);
			y2 = world.worldToScreenY(y2);

			// Swap upper and lower Y values
			float temp = y1;
			y1 = y2;
			y2 = temp;
		}

		float halfW = (x2 - x1) * 0.5f;
		float halfH = (y2 - y1) * 0.5f;

		AffineTransform savedTrans = currentGFX2D.getTransform();

		tempTrans.setTransform(savedTrans);
		tempTrans.translate(x1 + halfW, y1 + halfH);
		tempTrans.rotate(Math.toRadians(-degrees));
		currentGFX2D.transform(tempTrans);

		currentGFX2D.drawImage(texture, -(int) halfW, -(int) halfH,
				(int) halfW, (int) halfH, (int) (srcX1 * texture.getWidth()),
				(int) (srcY1 * texture.getHeight()),
				(int) (srcX2 * texture.getWidth()),
				(int) (srcY2 * texture.getHeight()), null);

		// currentGFX.drawImage(texture, -(int)halfW, -(int)halfH, (int)halfW,
		// (int)halfH, (int)(srcX1 * texture.getWidth()),
		// (int)(srcY1 * texture.getHeight()), (int)(srcX2 *
		// texture.getWidth()),
		// (int)(srcY2 * texture.getHeight()), null);

		currentGFX2D.setTransform(savedTrans);
	}

	/**
	 * Set the RGB color to draw with.
	 * 
	 * @param r
	 *            - The red value.
	 * @param g
	 *            - The green value.
	 * @param b
	 *            - The blue value.
	 */
	public void setDrawingColor(int r, int g, int b) {
		if (currentGFX == null) {
			return;
		}

		currentGFX.setColor(new Color(r, g, b));
	}

	/**
	 * Set the color to draw with.
	 * 
	 * @param theColor
	 *            - The color to draw with.
	 */
	public void setDrawingColor(Color theColor) {
		if (currentGFX == null) {
			return;
		}

		currentGFX.setColor(theColor);
	}

	/**
	 * Draw a textureless rectangle with the given lower left and upper right
	 * coordinates. Coordinates should be in world space.
	 * 
	 * @param x1
	 *            - Lower left x coordinate.
	 * @param y1
	 *            - Lower left y coordinate.
	 * @param x2
	 *            - Upper right x coordinate.
	 * @param y2
	 *            - Upper right y coordinate.
	 */
	public void drawRectangle(float x1, float y1, float x2, float y2) {
		if (currentGFX == null) {
			return;
		}

		// Convert coords
		{
			// Convert from world coords to screen coords
			x1 = world.worldToScreenX(x1);
			y1 = world.worldToScreenY(y1);
			x2 = world.worldToScreenX(x2);
			y2 = world.worldToScreenY(y2);

			// Swap upper and lower Y values
			float temp = y1;
			y1 = y2;
			y2 = temp;
		}

		currentGFX2D.drawRect((int) x1, (int) y1, (int) (x2 - x1),
				(int) (y2 - y1));
		// currentGFX.drawRect((int)x1, (int)y1, (int)(x2 - x1), (int)(y2 -
		// y1));

	}

	/*
	 * public void drawRectangleABS(float left, float top, float width, float
	 * height) { if(currentGFX == null) { return; }
	 * 
	 * currentGFX.drawRect((int)left, (int)top, (int)width, (int)height); }
	 */

	/**
	 * Draw a textureless rectangle with the given center, size, and rotation.
	 * Coordinates should be in world space.
	 * 
	 * @param centerX
	 *            - Center x coordinate.
	 * @param centerY
	 *            - Center y coordinate.
	 * @param width
	 *            - Width of the rectangle.
	 * @param height
	 *            - Height of the rectangle.
	 * @param degrees
	 *            - Number of degrees to rotate.
	 */
	public void drawFilledRectangle(float centerX, float centerY, float width,
			float height, float degrees) {
		if (currentGFX == null) {
			return;
		}

		float x1 = centerX - width;
		float y1 = centerY - height;
		float x2 = centerX + width;
		float y2 = centerY + height;

		// Convert coords
		{
			// Convert from world coords to screen coords
			x1 = world.worldToScreenX(x1);
			y1 = world.worldToScreenY(y1);
			x2 = world.worldToScreenX(x2);
			y2 = world.worldToScreenY(y2);

			// Swap upper and lower Y values
			float temp = y1;
			y1 = y2;
			y2 = temp;

			centerX = world.worldToScreenX(centerX);
			centerY = world.worldToScreenY(centerY);
			width = x2 - x1;
			height = y2 - y1;
		}

		AffineTransform savedTrans = currentGFX2D.getTransform();

		tempTrans.setTransform(savedTrans);
		tempTrans.translate(centerX, centerY);
		tempTrans.rotate(Math.toRadians(-degrees));
		currentGFX2D.transform(tempTrans);

		// currentGFX.fillRect(-(int)(width * 0.5f), -(int)(height * 0.5f),
		// (int)width, (int)height);

		currentGFX2D.fillRect(-(int) (width * 0.5f), -(int) (height * 0.5f),
				(int) width, (int) height);

		currentGFX2D.setTransform(savedTrans);
	}

	/**
	 * Draw a textureless rectangle with the given center, size, and rotation.
	 * Coordinates should be in world space.
	 * 
	 * @param centerX
	 *            - Center x coordinate.
	 * @param centerY
	 *            - Center y coordinate.
	 * @param width
	 *            - Width of the rectangle.
	 * @param height
	 *            - Height of the rectangle.
	 * @param degrees
	 *            - Number of degrees to rotate.
	 */
	public void drawOutlinedRectangle(float centerX, float centerY,
			float width, float height, float degrees) {
		if (currentGFX == null) {
			return;
		}

		float x1 = centerX - width;
		float y1 = centerY - height;
		float x2 = centerX + width;
		float y2 = centerY + height;

		// Convert coords
		{
			// Convert from world coords to screen coords
			x1 = world.worldToScreenX(x1);
			y1 = world.worldToScreenY(y1);
			x2 = world.worldToScreenX(x2);
			y2 = world.worldToScreenY(y2);

			// Swap upper and lower Y values
			float temp = y1;
			y1 = y2;
			y2 = temp;

			centerX = world.worldToScreenX(centerX);
			centerY = world.worldToScreenY(centerY);
			width = x2 - x1;
			height = y2 - y1;
		}

		AffineTransform savedTrans = currentGFX2D.getTransform();

		tempTrans.setTransform(savedTrans);
		tempTrans.translate(centerX, centerY);
		tempTrans.rotate(Math.toRadians(-degrees));
		currentGFX2D.transform(tempTrans);

		currentGFX.drawRect(-(int) (width * 0.5f), -(int) (height * 0.5f),
				(int) width, (int) height);

		currentGFX2D.setTransform(savedTrans);
	}

	/**
	 * Draw a textureless rectangle with the given center, size, and rotation.
	 * Coordinates should be in pixel space.
	 * 
	 * @param centerX
	 *            - Center x coordinate.
	 * @param centerY
	 *            - Center y coordinate.
	 * @param width
	 *            - Width of the rectangle.
	 * @param height
	 *            - Height of the rectangle.
	 * @param degrees
	 *            - Number of degrees to rotate.
	 */
	public void drawRectangleABS(int centerX, int centerY, int width,
			int height, float degrees) {
		if (currentGFX == null) {
			return;
		}

		AffineTransform savedTrans = currentGFX2D.getTransform();

		tempTrans.setTransform(savedTrans);
		tempTrans.translate(centerX, centerY);
		tempTrans.rotate(Math.toRadians(degrees));
		currentGFX2D.transform(tempTrans);

		currentGFX2D.drawRect(-width / 2, -height / 2, width, height);

		currentGFX2D.setTransform(savedTrans);
	}

	/**
	 * Draw a line between the given coordinates. Coordinates should be in world
	 * space.
	 * 
	 * @param x1
	 *            - x coordinate of the first point.
	 * @param y1
	 *            - y coordinate of the first point.
	 * @param x2
	 *            - x coordinate of the second point.
	 * @param y2
	 *            - y coordinate of the second point.
	 */
	public void drawLine(float x1, float y1, float x2, float y2) {
		if (currentGFX == null) {
			return;
		}

		// Convert coords
		{
			// Convert from world coords to screen coords
			x1 = world.worldToScreenX(x1);
			y1 = world.worldToScreenY(y1);
			x2 = world.worldToScreenX(x2);
			y2 = world.worldToScreenY(y2);
		}

		currentGFX.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}

	/**
	 * Draw a line between the given coordinates. Coordinates should be in pixel
	 * space.
	 * 
	 * @param x1
	 *            - x coordinate of the first point.
	 * @param y1
	 *            - y coordinate of the first point.
	 * @param x2
	 *            - x coordinate of the second point.
	 * @param y2
	 *            - y coordinate of the second point.
	 */
	public void drawLineABS(float x1, float y1, float x2, float y2) {
		if (currentGFX == null) {
			return;
		}

		currentGFX.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}

	/**
	 * Draw text.Coordinates should be in world space.
	 * 
	 * @param text
	 *            - The text to draw.
	 * @param x
	 *            - x coordinate to draw at.
	 * @param y
	 *            - y coordinate to draw at.
	 */
	public void drawText(String text, float x, float y) {
		drawText(text, x, y, 0.0f);
	}

	/**
	 * Draw text.Coordinates should be in world space.
	 * 
	 * @param text
	 *            - The text to draw.
	 * @param x
	 *            - x coordinate to draw at.
	 * @param y
	 *            - y coordinate to draw at.
	 * @param degrees
	 *            - Number of degrees to rotate.
	 */
	public void drawText(String text, float x, float y, float degrees) {
		x = world.worldToScreenX(x);
		y = world.worldToScreenY(y);

		drawTextABS(text, (int) x, (int) y, degrees);
	}

	/**
	 * Draw text.Coordinates should be in pixel space.
	 * 
	 * @param text
	 *            - The text to draw.
	 * @param x
	 *            - x coordinate to draw at.
	 * @param y
	 *            - y coordinate to draw at.
	 */
	public void drawTextABS(String text, float x, float y) {
		drawTextABS(text, x, y, 0.0f);
	}

	/**
	 * Draw text.Coordinates should be in pixel space.
	 * 
	 * @param text
	 *            - The text to draw.
	 * @param x
	 *            - x coordinate to draw at.
	 * @param y
	 *            - y coordinate to draw at.
	 * @param degrees
	 *            - Number of degrees to rotate.
	 */
	public void drawTextABS(String text, float x, float y, float degrees) {
		// currentGFX.setFont(activeFont);

		AffineTransform savedTrans = currentGFX2D.getTransform();

		// int width = currentGFX.getFontMetrics().stringWidth(text);

		tempTrans.setTransform(savedTrans);
		tempTrans.translate(x, y);
		tempTrans.rotate(Math.toRadians(-degrees));
		currentGFX2D.transform(tempTrans);

		currentGFX.setColor(textBack);
		currentGFX.drawString(text, -1, 1);
		// currentGFX.drawString(text, (int)(-width * 0.5f) - 1, 1);

		currentGFX.setColor(textFront);
		currentGFX.drawString(text, 0, 0);
		// currentGFX.drawString(text, (int)(-width * 0.5f), 0);

		currentGFX2D.setTransform(savedTrans);
	}

	/**
	 * Get the width of the text in world coordinates.
	 * 
	 * @param text
	 *            - The text to measure.
	 * @return - The width of the given text.
	 */
	public float getTextWidth(String text) {
		return world.screenToWorldX(currentGFX.getFontMetrics().stringWidth(
				text));
	}

	/**
	 * Get the width of the text in pixel coordinates.
	 * 
	 * @param text
	 *            - The text to measure.
	 * @return - The width of the given text.
	 */
	public float getTextWidthABS(String text) {
		return currentGFX.getFontMetrics().stringWidth(text);
	}

	// Region: Support for Font loading and caching

	private Font findFontInCache(String name, int size) {
		Font inCache = null;
		Font currentFont = null;

		for (int i = 0; i < fonts.size(); i++) {
			currentFont = fonts.get(i);

			if (currentFont.getName().compareToIgnoreCase(name) == 0
					&& currentFont.getSize() == size) {
				inCache = currentFont;
				break;
			}
		}
		return inCache;
	}

	/**
	 * Loads a font into the current graphics system. Font must be TrueType.
	 * 
	 * @param fileName
	 *            - Name of the file to load from. -
	 * @return - The Font that was loaded. Returns null if there was a problem.
	 */
	public Font preloadFont(String fileName) {
		// Check for obvious faults
		if (fileName == null || fileName.compareTo("") == 0) {
			return null;
		}

		InputStream input = getResourceStream(fileName);

		if (input != null) {
			try {
				Font font = Font.createFont(Font.TRUETYPE_FONT, input);
				GraphicsEnvironment ge = GraphicsEnvironment
						.getLocalGraphicsEnvironment();
				ge.registerFont(font);
				setFont(font, 16);
				input.close();
				return font;
			} catch (Exception ex) {
			}
		} else {
			System.out.println(":(");
		}
		return null;
	}

	// Checks the cached font map, if found, return, otherwise, create a new one
	// and add to font cache
	public void setFont(String name, int size) {
		Font theFont = findFontInCache(name, size);

		if (theFont == null) {
			theFont = new Font(name, Font.BOLD, size);

			fonts.add(theFont);
		}

		activeFont = theFont;
		currentGFX.setFont(activeFont);
	}

	public void setFont(Font theFont, int size) {
		if (findFontInCache(theFont.getName(), size) == null)
			fonts.add(theFont);

		activeFont = theFont;
		currentGFX.setFont(activeFont);
	}

	public void setTextBackColor(Color value) {
		textBack = value;
	}

	public void setTextFrontColor(Color value) {
		textFront = value;
	}

	// EndRegion For Font support

	// Region: Image loading support
	public BufferedImage findImageInCache(String name) {
		BufferedImage image = null;
		// If there is an entry for the image already
		if (textures.containsKey(name)) {
			image = textures.get(name);
		}
		return image;
	}

	/**
	 * Loads an image from the given file name to be used later.
	 * 
	 * @param fileName
	 *            - The name of the file to load from.
	 * @return - The image that was loaded. Will be null if there was a problem.
	 */
	public BufferedImage preloadImage(String fileName) {
		// System.out.println("Preloaded Image: " + fileName);

		if (fileName == null || fileName.compareTo("") == 0) {
			return null;
		}

		BufferedImage img = findImageInCache(fileName);
		if (img != null)
			return img;

		
		InputStream input = getResourceStream(fileName);

		if (input != null) {
			try {
				img = ImageIO.read(input);
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (img == null) {
			System.err.println("Error loading image: '" + basePath + " and file:" + fileName + "'");
			MessageOnce.showAlert("Error loading image: '" + fileName + "'");
		}

		// Cache the result, even if it failed to read
		textures.put(fileName, img);

		return img;
	}

	/**
	 * Loads the given file name as an image.
	 * 
	 * @param fileName
	 *            - The file name to read from.
	 * @return - The image that was loaded. Will be null if there was a problem.
	 */
	public BufferedImage loadImage(String fileName) {
		return preloadImage(fileName);
	}
	// EndRegion: support for loading images
}