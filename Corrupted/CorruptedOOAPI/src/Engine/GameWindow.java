package Engine;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public abstract class GameWindow extends Applet implements Runnable {
	//
	// Configuration
	//

	// Number of times per second to try to update and draw at
	private final long TARGET_UPDATES_PER_SECOND = 40;

	// Maximum number of frames to skip between drawing a new frame
	private int MAX_FRAME_SKIP = 10;

	// How much longer a frame can take before skipping drawing a frame
	private float FRAME_SKIP_RATE = 1.5f;

	// Game loop (Timer = 1, loop = 2, or Scheduled thread = 3)
	private static int LOOP_OPTION = 2;

	//
	//
	//

	private boolean didInit = false;

	private int clientWidth = 200;
	private int clientHeight = 200;

	private boolean isFullscreen = false;

	// Double buffer
	private Image screenBuffer = null;
	protected Graphics2D screenBufferGraphics = null;

	// OPTION_2
	private Thread updateThread = null;

	// If true, only draw frames if an update has
	// occurred since the last draw frame. If false, draw frames even if
	// no updates have occured since the last draw frame.
	private boolean syncFPS = true;

	// If true, frames can be skipped as needed. If false, frames will not be
	// skipped and possibly cause slower performance.
	private boolean skipFrames = true;

	private Semaphore isRunning = new Semaphore(1);
	private final long FRAME_DELAY = (1000 / TARGET_UPDATES_PER_SECOND);

	private long lastUpdateTime = 0;
	private int numFramesSkipped = 0;
	private int FRAME_SKIP_THRESHOLD = (int) (FRAME_SKIP_RATE * FRAME_DELAY);

	private boolean shouldClose = false;
	private boolean didCleanExit = false;

	// Used by ResourceHandler to locate external files
	private String basePath = "";

	// Used when running as an application to contain the applet
	private JFrame theFrame = null;

	//
	//
	//

	// Automatically called when running as an applet
	public void init() {
		// Since running as an applet, this shouldn't crash internally
		setBasePath(getCodeBase().toString());

		myInit();

		// getParent().validate();
	}

	public void setShouldSkipFrames(boolean value) {
		skipFrames = value;
		lastUpdateTime = (System.nanoTime() / 1000000);
	}

	public boolean getShouldSkipFrames() {
		return skipFrames;
	}

	public void setBasePath(String value) {
		basePath = value;
	}

	public String getBasePath() {
		return basePath;
	}

	// Call this one from main
	private void myInit() {

		setIgnoreRepaint(true);

		initConfig(this);  // config the runner: will set the size of the Frame!

		// Set the window
		setBackground(new Color(100, 100, 100));
		setSize(getWidth(), getHeight());
		setPreferredSize(new Dimension(getWidth(), getHeight()));
		
		resizeDoubleBuffer();

		initializeWorld();

		didInit = true;

		lastUpdateTime = (System.nanoTime() / 1000000);

		try {
		
			updateThread = new Thread(this);
			updateThread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Update thread
	public void run() {
		long curTime = (System.nanoTime() / 1000000);
		lastUpdateTime = curTime;

		try {
			isRunning.acquire();
		} catch (InterruptedException e) {
			System.err.println("Semaphore error while starting to run!");
			e.printStackTrace();
		}

		boolean didUpdate = false;

		numFramesSkipped = -1;

		// Infinite loop instead of using a timer due to Java timer relying on
		// the system timer resolution, which may not be as precise as desired.
		while (!shouldClose) {
			curTime = (System.nanoTime() / 1000000);

			while ((curTime - lastUpdateTime) >= FRAME_DELAY
					&& numFramesSkipped < MAX_FRAME_SKIP) {
				updateWorld();
				updateInput();

				lastUpdateTime += FRAME_DELAY;

				didUpdate = true;
				numFramesSkipped += 1;

				if (!skipFrames) {
					break;
				}
			}

			if (didUpdate || !syncFPS) {
				didUpdate = false;
				numFramesSkipped = -1;

				// 
				update(getGraphics());
			}

			// Sleep to allow input state to be processed and changed
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		}

		isRunning.release();

		cleanExit();
	}

	// Paint the screen
	public void paint(Graphics theGraphics) {
		if (didInit && theGraphics != null) {
			
screenBufferGraphics.dispose();
screenBufferGraphics = (Graphics2D) screenBuffer.getGraphics();
addRenderingHints(screenBufferGraphics);			

			// Clear the buffer
			screenBufferGraphics.clearRect(0, 0, screenBuffer.getWidth(null),
					screenBuffer.getHeight(null));

			// Draw the scene
			draw(screenBufferGraphics);

			// Draw the screen buffer to the screen
			theGraphics.drawImage(screenBuffer, 0, 0, null);
		}
	}

	// Called when the screen should be repainted
	public void update(Graphics theGraphics) {
		paint(theGraphics);
	}

	//
	//
	//

	public void setClientSize(int width, int height) {
		clientWidth = width;
		clientHeight = height;
	}

	public void setSize(int width, int height) {
		setClientSize(width, height);

		super.setSize(clientWidth, clientHeight);
	}

	public int getWidth() {
		return clientWidth;
	}

	public int getHeight() {
		return clientHeight;
	}

	private void cleanExit() {
		if (didCleanExit) {
			return;
		}

		didCleanExit = true;

		didInit = false;

		// Make sure things are set to close
		close();

		// Wait for the update loop to stop
		try {
			isRunning.acquire();
		} catch (InterruptedException e) {
			System.err.println("Semaphore error while trying to stop!");
			e.printStackTrace();
		}

		clean();  // tell runner to clean up

		// Close the containing frame
		if (theFrame != null) {
			// Close the frame
			theFrame.setVisible(false);
			if (screenBufferGraphics != null) {
				screenBufferGraphics.dispose();
				screenBufferGraphics = null;
			}
			theFrame.dispose();
			theFrame = null;
			// System.out.println("clean exist disposing the Frame");
		}
		
		// If there wasn't a frame, then try closing as an
		// applet that is using the applet viewer
		else if (this.getParent() != null) {
			// Close
			Window parentFrame = (Window) this.getParent().getParent();

			if (parentFrame != null) {
				WindowEvent wev = new WindowEvent(parentFrame,
						WindowEvent.WINDOW_CLOSING);
				Toolkit.getDefaultToolkit().getSystemEventQueue()
						.postEvent(wev);
			}
		}

		updateThread = null;
		if (runner != null) {
			runner.clean();
			runner = null;
		}
		
		// now destroy the applet
		destroy();
		
		// 
		System.gc();
	}

	// closing the game window: shutting down!
	public void close() {
		shouldClose = true;
	}

	//
	//
	//

	public void startProgram() {
		// String current = System.getProperty("user.dir");
		// System.out.println("Current working directory in Java : " + current);

		

		CreateTheFrame(true, // should decorate (windowed)
				JFrame.NORMAL); // windowed (or normal mode)

		// Since running as an application, this
		// shouldn't throw a security warning
//		setBasePath("file:" + System.getProperty("user.dir") + "\\");

		
		//note the system independent use of separatorChar
		setBasePath( System.getProperty("user.dir") +  File.separatorChar );
		
		
		myInit();

		theFrame.getContentPane().setPreferredSize(
				new Dimension(getWidth(), getHeight()));
		theFrame.pack();

	}

	//
	//
	//

	/**
	 * Sets the JFrame's state to fullscreen if normal, or normal if fullscreen.
	 * 
	 */
	public void toggleFullscreen() {
		if (theFrame != null) {
			if (isFullscreen) {
				isFullscreen = false;
				setScreenToWindowed();
			} else {
				isFullscreen = true;
				setScreenToFullscreen();
			}
			theFrame.toFront();
		}
	}

	/**
	 * Checks whether or not the state of the gameWindow is fullscreen.
	 * 
	 * @return Whether this is fullscreen or not.
	 */
	public boolean isFullscreen() {
		return isFullscreen;
	}

	/**
	 * Reinstanciates theFrame to a fullscreen size.
	 * 
	 * Must be instancaited as a new Frame because setUndecorated() (removes
	 * topbar) will fail once theFrame becomes displayable.
	 */
	public void setScreenToFullscreen() {

		CreateTheFrame(false, // should NOT decorate (full screen)
				JFrame.MAXIMIZED_BOTH); // MAX SIZE

		// Sets the gamewindows size to the frame's size.
		this.setSize(theFrame.getWidth(), theFrame.getHeight());
		resizeDoubleBuffer();
	}

	/**
	 * Reinstanciates theFrame to windowed size.
	 */
	public void setScreenToWindowed() {
		
		CreateTheFrame(true, 	// should decorate (Window mode)
				JFrame.NORMAL); // normal means windowed
		
		// Sets the gamewindow's size to the default values set in library code.
		this.setSize(runner.getInitWidth(), runner.getInitHeight());

		theFrame.getContentPane().setPreferredSize(
				new Dimension(getWidth(), getHeight()));
		
		resizeDoubleBuffer();
		theFrame.pack();
	}

	/**
	 * Resizes the double buffer to theFrame's current size.
	 */
	private void resizeDoubleBuffer() {
		// Recreate double buffer for new size
		if (screenBufferGraphics != null)
			screenBufferGraphics.dispose();
		
		screenBuffer = createImage(getWidth(), getHeight());
		screenBufferGraphics = (Graphics2D) screenBuffer.getGraphics();
		addRenderingHints(screenBufferGraphics);
	}

	/**
	 * Adds the rendering hints to a graphics display.
	 * 
	 * @param g2
	 *            - Graphics to be modified.
	 */
	private void addRenderingHints(Graphics2D g2) {
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		RenderingHints rh2 = new RenderingHints(
				RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		// RenderingHints rh3 = new RenderingHints(
		// RenderingHints.KEY_INTERPOLATION,
		// RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		g2.addRenderingHints(rh);
		g2.addRenderingHints(rh2);
		// g2.addRenderingHints(rh3);
	}

	/*
	 * public static void main(String[] args) {
	 * System.err.println("NEED MAIN METHOD!"); }
	 */

	//
	// Misc fucntions
	//
	/**
	 * Removes the cursor from the window.
	 */
	public void removeCursor() {
		BufferedImage blankImg = new BufferedImage(16, 16,
				BufferedImage.TYPE_INT_ARGB);

		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				blankImg, new Point(0, 0), "Blank Cursor");

		theFrame.getContentPane().setCursor(blankCursor);
	}

	/**
	 * Sets cursor to system default.
	 */
	public void resetCursor() {
		theFrame.getContentPane().setCursor(Cursor.getDefaultCursor());
	}

	//
	//
	//

	private LibraryCode runner = null;

	public void setRunner(LibraryCode theRunner) {
		runner = theRunner;
	}

	private void initConfig(GameWindow theWindow) {
		runner.initConfig(this);
	}

	private void initializeWorld() {
		runner.initializeWorld();
	}

	private void updateWorld() {
		runner.updateWorld();
	}

	private void updateInput() {
		runner.updateInput();
	}

	private void clean() {
		runner.clean();
	}

	private void draw(Graphics gfx) {
		runner.draw(gfx);
	}

	//
	//
	//

	/*
	 * public abstract void initConfig(GameWindow theWindow);
	 * 
	 * public abstract void initializeWorld();
	 * 
	 * public abstract void updateWorld();
	 * 
	 * public abstract void updateInput();
	 * 
	 * public abstract void clean();
	 * 
	 * public abstract void draw(Graphics gfx);
	 */

	//
	//
	//



	//
	private void CreateTheFrame(boolean shouldDecorate, int extendedState) {

		if (theFrame != null)
			theFrame.dispose();

		JFrame.setDefaultLookAndFeelDecorated(shouldDecorate);
		final JFrame frame = new JFrame();

		frame.setExtendedState(extendedState);
		if (!shouldDecorate)
			frame.setUndecorated(true); // this is for full screen
		frame.setResizable(false);
		frame.setTitle("Window");
		frame.setVisible(true);
		frame.getContentPane().add(this);

		// System.out.println("CreateTheFrame called");
		// Detect when the window wants to close
	    frame.addWindowListener(new WindowAdapter()
	    {
		      public void windowClosing(WindowEvent we)
		      {
		    	  shouldClose = true;
		    	  // System.out.println("windowClosing called");
		      }
	    });

		theFrame = frame;
		
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //so we exit
	}
}
