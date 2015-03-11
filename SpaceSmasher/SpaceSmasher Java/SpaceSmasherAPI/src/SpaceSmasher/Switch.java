
package SpaceSmasher;

import Engine.BaseCode;
import Engine.GameObject;
import Engine.ResourceHandler;
import Engine.Vector2;
import Engine.World;

public class Switch extends GameObject
{
  private static final String ACTIVE_TEXTURE = "StateChangeSwitch_On.png";
  private static final String INACTIVE_TEXTURE = "state_changer_unlock_button_idle.png";
  private static final String ACTIVATION_SOUND = "sounds/Button1SpaceSmasher.wav";
  private static final String DEACTIVATION_SOUND = "sounds/Button2SpaceSmasher.wav";
  
  private final float SWITCH_HEIGHT = 3.5f;

  boolean active = false;
  
  
  /**
   * will load and sounds or images required for this class to function
   * @param resources handler to be used for pre-loading.
   */
  public static void preloadResources(ResourceHandler resources){
	  if(resources !=  null){
		  resources.preloadImage(ACTIVE_TEXTURE);
		  resources.preloadImage(INACTIVE_TEXTURE);
		  resources.preloadSound(ACTIVATION_SOUND);
		  resources.preloadSound(DEACTIVATION_SOUND);
	  }
  }
  public Switch()
  {
    setup(BaseCode.world);
  }

  /**
   * Setup the switch to a preset location in the world.
   * 
   * @param world
   *          - The world to be used to locate the switch with.
   */
  private void setup(World world)
  {
    /*
    center.set(world.getWidth() * 0.5f, world.getHeight() -
        (world.getHeight() * 0.06f));
    size.set(world.getWidth() * 0.3f, world.getHeight() * 0.1f);
    */

    setCenter(world.getWidth() * 0.5f, world.getHeight() -
        (SWITCH_HEIGHT));
    setSize(20.0f, SWITCH_HEIGHT);
  }

  /**
   * Bounce the ball off the paddle.
   * 
   * @param ball
   *          - The ball to bounce.
   */
  public void reflect(Ball ball)
  {
    if(ball != null)
    {
      Vector2 dir = pushOutCircle(ball);

      ball.bounce(dir);
    }
  }

  /**
   * Check if the switch is active and in visible.
   * 
   * @return - True if the switch is active and visible.
   */
  public boolean isActive()
  {
    return active;
  }

  /**
   * The switch will become active.
   */
  public void activate()
  {
    setImage(ACTIVE_TEXTURE);
    setToVisible();
    active = true;
  }
  /**
   * Will play the sound associated with the switch becoming active
   */
  public void playActivationSound(){
	  BaseCode.resources.playSound(ACTIVATION_SOUND);
  }
  /**
   * The switch is no longer active
   */
  public void deactivate()
  {
	  setImage(INACTIVE_TEXTURE);
	  setToVisible();
	  active = false;
  }
  /**
   * Will play the sound associated with the switch turning off
   */
  public void playDeactivationSound(){
	  BaseCode.resources.playSound(DEACTIVATION_SOUND);
  }
}
