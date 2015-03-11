
package SpaceSmasher;

import Engine.ResourceHandler;

public class JokerBlock extends Block
{ 
  protected static final String JOKER_IMAGE = "blocks/Block_JokerSwap.png";
 /**
   * will load and sounds or images required for this class to function
   * @param resources handler to be used for pre-loading.
   */
  public static void preloadResources(ResourceHandler resources){
	  if(resources !=  null){
		  resources.preloadImage(JOKER_IMAGE);
	  }
  }
  public JokerBlock(int currentRow, int currentColumn)
  {
    setType(BlockType.JOKER);
    row = currentRow;
    column = currentColumn;
  }

  public void updateImage()
  {
    super.updateImage();

    setImage(JOKER_IMAGE);
  }
}
