
package SpaceSmasher;

import Engine.ResourceHandler;

public class FreezingBlock extends Block
{
  protected static final String ICED_IMAGE = "blocks/Block_Iced.png";
  protected static final String ICE_POWERUP_IMAGE = "blocks/Block_IcePowerDown.png";
  protected static final String NORMAL_BLOCK_IMAGE = "blocks/Block_Normal.png";
/**
   * will load and sounds or images required for this class to function
   * @param resources handler to be used for pre-loading.
   */
  public static void preloadResources(ResourceHandler resources){
	  if(resources !=  null){
		  resources.preloadImage(ICED_IMAGE);
		  resources.preloadImage(ICE_POWERUP_IMAGE);
		  resources.preloadImage(NORMAL_BLOCK_IMAGE);
	  }
  }
  public FreezingBlock(int currentRow, int currentColumn)
  {
    setType(BlockType.FREEZING);
    row = currentRow;
    column = currentColumn;
  }

  public void updateImage()
  {
    super.updateImage();

    if(getState() == BlockState.FROZEN)
    {
      setImage(ICED_IMAGE);
    }
    else if(getShowType() || isPowerRevealed())
    {
      setImage(ICE_POWERUP_IMAGE);
    }
    else
    {
      setImage(NORMAL_BLOCK_IMAGE);
    }
  }
}
