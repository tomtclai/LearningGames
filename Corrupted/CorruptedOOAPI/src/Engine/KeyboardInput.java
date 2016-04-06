
package Engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput extends ButtonsInput implements KeyListener
{
  private String lastKey = "";

  /**
   * Get the most recent key that was pressed.
   * 
   * @return - The name of the key that was pressed last.
   */
  public String getLastKey()
  {
    return lastKey;
  }

  //
  // Listener methods
  //

  public void keyPressed(KeyEvent e)
  {
    pressButton(e.getKeyCode());
    lastKey = KeyEvent.getKeyText(e.getKeyCode());
  }

  public void keyReleased(KeyEvent e)
  {
    releaseButton(e.getKeyCode());
  }

  public void keyTyped(KeyEvent e)
  {
  }
}
