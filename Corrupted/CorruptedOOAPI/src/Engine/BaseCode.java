
package Engine;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public abstract class BaseCode// extends GameWindow
{
  protected GameWindow window = null;
  public static ResourceHandler resources = null;
  public static World world = null;
  public static Random random = null;

  protected KeyboardInput keyboard = null;
  protected MouseInput mouse = null;

  // FPS
  private int framesPerSecond = 0;
  private int framesPerSecondPrev = 0;
  private long prevFPSTime = (System.nanoTime() / 1000000);
  private long curFPSTime = prevFPSTime;

  // UPS
  private int updatesPerSecond = 0;
  private int updatesPerSecondPrev = 0;
  private long prevUPSTime = (System.nanoTime() / 1000000);
  private long curUPSTime = prevUPSTime;

  private Text textFPS = null;
  private Text textUPS = null;
  private Text textLastKey = null;
  private Text textClientSize = null;

  private boolean showDebugInfo = false;

  private Text textStatusTop = null;
  private Text textStatusBottom = null;

  /**
   * Toggle whether debug information should be shown on screen.
   */
  public void toggleShowDebugInfo()
  {
    setShowDebugInfo(!showDebugInfo);
  }

  /**
   * Set whether debug information should be shown on screen.
   * 
   * @param value
   *          - True if debug information should be shown, false otherwise.
   */
  public void setShowDebugInfo(boolean value)
  {
    showDebugInfo = value;

    if(showDebugInfo)
    {
      textFPS.addToAutoDrawSet();
      textUPS.addToAutoDrawSet();
      textLastKey.addToAutoDrawSet();

      if(textClientSize != null)
      {
        textClientSize.addToAutoDrawSet();
      }
    }
    else
    {
      textFPS.removeFromAutoDrawSet();
      textUPS.removeFromAutoDrawSet();
      textLastKey.removeFromAutoDrawSet();

      if(textClientSize != null)
      {
        textClientSize.removeFromAutoDrawSet();
      }
    }
  }

  public void initConfig(GameWindow theWindow)
  {
    window = theWindow;

    world = new World(window);

    resources = new ResourceHandler();
    resources.basePath = window.getBasePath();
    resources.setWorld(world);

    // Start listening for keyboard input
    keyboard = new KeyboardInput();
    window.addKeyListener(keyboard);

    random = new Random();

    // Start listening for mouse input
    mouse = new MouseInput();
    mouse.setWorld(world);
    window.addMouseListener(mouse);
    window.addMouseMotionListener(mouse);
  }

  protected void echoToTopStatus(String text)
  {
    textStatusTop.setText("Status: " + text);
    textStatusTop.visible = true;
  }

  protected void echoToBottomStatus(String text)
  {
    textStatusBottom.setText("Status: " + text);
    textStatusBottom.visible = true;
  }

  public void initializeWorld()
  {
    textFPS = new Text();
    textFPS.center.set(window.getWidth() - 120, 20);
    textFPS.setDrawInWorldCoords(false);
    textFPS.setText("FPS: " + framesPerSecondPrev);

    textUPS = new Text();
    textUPS.center.set(window.getWidth() - 120, 40);
    textUPS.setDrawInWorldCoords(false);
    textUPS.setText("UPS: " + updatesPerSecondPrev);

    textLastKey = new Text();
    textLastKey.center.set(window.getWidth() - 160, 60);
    textLastKey.setDrawInWorldCoords(false);
    textLastKey.setText("Last key: " + keyboard.getLastKey());

    /*
    textClientSize = new Text();
    textClientSize.center.set(window.getWidth() - 180, 80);
    textClientSize.setDrawInWorldCoords(false);
    textClientSize.setText("Client: " + window.getWidth() + ", " +
        window.getHeight());
    */

    textStatusTop = new Text();
    textStatusTop.center.set(10.0f, 30.0f);
    textStatusTop.setDrawInWorldCoords(false);
    textStatusTop.alwaysOnTop = true;
    textStatusTop.visible = false;
    textStatusTop.setBackColor(Color.BLACK);
    textStatusTop.setFrontColor(Color.RED);
    textStatusTop.setFontSize(28);
    textStatusTop.setText("Status:");

    textStatusBottom = new Text();
    textStatusBottom.center.set(10.0f, window.getHeight() - 20.0f);
    textStatusBottom.setDrawInWorldCoords(false);
    textStatusBottom.alwaysOnTop = true;
    textStatusBottom.visible = false;
    textStatusBottom.setBackColor(Color.BLACK);
    textStatusBottom.setFrontColor(Color.RED);
    textStatusBottom.setFontSize(28);
    textStatusBottom.setText("Status:");

    setShowDebugInfo(showDebugInfo);
  }

  public void updateWorld()
  {
    textStatusTop.visible = false;
    textStatusBottom.visible = false;

    // UPS
    {
      curUPSTime = (System.nanoTime() / 1000000);

      // Calculate FPS
      if(curUPSTime - prevUPSTime >= 1000)
      {
        prevUPSTime = curUPSTime;

        updatesPerSecondPrev = updatesPerSecond;
        updatesPerSecond = 0;

        if(textFPS != null)
        {
          textUPS.setText("UPS: " + updatesPerSecondPrev);
        }
      }

      updatesPerSecond += 1;
    }
  }

  public void updateInput()
  {
    keyboard.update();
    mouse.update();

    if(textLastKey != null)
    {
      textLastKey.setText("Last key: " + keyboard.getLastKey());
    }

    if(textClientSize != null)
    {
      textClientSize.setText("Client: " + window.getWidth() + ", " +
          window.getHeight());
    }
  }

  public void draw(Graphics gfx)
  {
    resources.setGraphics(gfx);

    resources.drawDrawSet();

    // FPS
    {
      curFPSTime = (System.nanoTime() / 1000000);

      // Calculate FPS
      if(curFPSTime - prevFPSTime >= 1000)
      {
        prevFPSTime = curFPSTime;

        framesPerSecondPrev = framesPerSecond;
        framesPerSecond = 0;

        if(textFPS != null)
        {
          textFPS.setText("FPS: " + framesPerSecondPrev);
        }
      }

      framesPerSecond += 1;
    }
  }

  public void clean()
  {
    resources.clean();
  }
}
