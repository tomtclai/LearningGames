
package SpaceSmasher;

import Engine.ResourceHandler;

public class FireBlock extends Block
{	
  protected static final String ICED_IMAGE = "blocks/Block_Iced.png";
  protected static final String FIRE_IMAGE = "blocks/Block_FirePowerUp.png";
  protected static final String NORMAL_BLOCK_IMAGE = "blocks/Block_Normal.png";
  /**
   * will load and sounds or images required for this class to function
   * @param resources handler to be used for pre-loading.
   */
  public static void preloadResources(ResourceHandler resources){
	  if(resources !=  null){
		  resources.preloadImage(ICED_IMAGE);
		  resources.preloadImage(FIRE_IMAGE);
		  resources.preloadImage(NORMAL_BLOCK_IMAGE);
	  }
  }
  protected FireBlock(int currentRow, int currentColumn)
  {
    setType(BlockType.FIRE);
    row = currentRow;
    column = currentColumn;
  }

  protected void updateImage()
  {
    super.updateImage();

    if(getState() == BlockState.FROZEN)
    {
      setImage(ICED_IMAGE);
    }
    else if(getShowType() || isPowerRevealed())
    {
      setImage(FIRE_IMAGE);
    }
    else
    {
      setImage(NORMAL_BLOCK_IMAGE);
    }
  }
}
