
package SpaceSmasher;

import Engine.SetOfGameObjects;

@SuppressWarnings("serial")
public class SwitchSet extends SetOfGameObjects<Switch>
{
  /**
   * Show all switches.
   */
  public void activate()
  {
    for(int i = 0; i < size(); i++)
    {
      get(i).activate();
    }
  }

  /**
   * Hide all switches.
   */
  public void deactivate()
  {
    for(int i = 0; i < size(); i++)
    {
      get(i).deactivate();
    }
  }
}
