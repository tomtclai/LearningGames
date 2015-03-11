
package GhostLight.GhostEngine;

import Engine.Rectangle;

public class TimedRectangle extends Rectangle
{
  protected int timeLeft = 0;

  public TimedRectangle()
  {
    super();

    visible = false;
  }

  public void setTimeLeft(int value)
  {
    timeLeft = value;

    if(timeLeft > 0)
    {
      visible = true;
    }
  }

  public void update()
  {
    super.update();

    if(visible && timeLeft > 0)
    {
      timeLeft -= 1;

      if(timeLeft <= 0)
      {
        visible = false;
      }
    }
  }
}
