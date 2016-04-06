
package Engine;

import java.util.Vector;

public class ButtonsInput
{
  public enum ButtonState
  {
    UP, TAPPED, PRESSED, RELEASED
  };

  protected class ButtonData
  {
    public ButtonData(int theKey)
    {
      key = theKey;
    }

    public int key = 0;
    public ButtonState state = ButtonState.TAPPED;
  }

  protected Vector<ButtonData> buttonStates = new Vector<ButtonData>();

  protected ButtonData getButtonData(int key)
  {
    for(int i = 0; i < buttonStates.size(); i++)
    {
      if(buttonStates.get(i).key == key)
      {
        return buttonStates.get(i);
      }
    }

    return null;
  }

  public boolean isButtonTapped(int button)
  {
    ButtonData data = getButtonData(button);

    return (data != null && data.state == ButtonState.TAPPED);
  }

  public boolean isButtonDown(int button)
  {
    ButtonData data = getButtonData(button);

    return (data != null && (data.state == ButtonState.TAPPED || data.state == ButtonState.PRESSED));
  }

  public void pressButton(int button)
  {
    ButtonData data = getButtonData(button);

    if(data == null)
    {
      buttonStates.add(new ButtonData(button));
    }
    else if(data.state == ButtonState.UP)
    {
      data.state = ButtonState.TAPPED;
    }
  }

  public void releaseButton(int button)
  {
    ButtonData data = getButtonData(button);

    if(data != null &&
        (data.state == ButtonState.TAPPED || data.state == ButtonState.PRESSED))
    {
      data.state = ButtonState.RELEASED;
    }
  }

  public void update()
  {
    ButtonData current;

    // Update key states
    for(int i = 0; i < buttonStates.size(); i++)
    {
      current = buttonStates.get(i);

      // Tapped to pressed
      if(current.state == ButtonState.TAPPED)
      {
        current.state = ButtonState.PRESSED;
      }
      // Released to up
      else if(current.state == ButtonState.RELEASED)
      {
        //current.state = ButtonState.UP;

        buttonStates.remove(i);
        i -= 1;
      }
    }
  }
}
