
package SpaceSmasher;

import Engine.ResourceHandler;

public class CageBlock extends Block
{
	int transitionFrame = 0;
	public static final int CLOSE_ANIMATION_FRAME_COUNT = 24; 
	protected static final String ICED_IMAGE = "blocks/Block_Iced.png";
	protected static final String NORMAL_BLOCK_IMAGE = "blocks/Block_Unbreakable_Open.png";
	protected static final String UNBREAKABLE_CLOSED = "blocks/Block_Unbreakable_Closed.png";
	  
	
	
	int transition = 0;
	
	
/**
   * will load and sounds or images required for this class to function
   * @param resources the handler to be used for pre-loading.
   */
  public static void preloadResources(ResourceHandler resources){
	  if(resources !=  null){
		  resources.preloadImage(ICED_IMAGE);
		  resources.preloadImage(UNBREAKABLE_CLOSED);
		  resources.preloadImage(NORMAL_BLOCK_IMAGE);
		  for(int loop = 0; loop < CLOSE_ANIMATION_FRAME_COUNT; loop++){
			  resources.preloadImage("blocks/closing/Block_Closing (" + (loop + 1) + ").png");
		  }
	  }
  }
  public CageBlock(boolean activity, int currentRow, int currentColumn){
	  
	  row = currentRow;
	  column = currentColumn;
	  
	  if(activity){
		  super.setType(BlockType.CAGE_ACTIVE);
		  transitionFrame = CLOSE_ANIMATION_FRAME_COUNT-1;
	  }
	  else{
		  super.setType(BlockType.CAGE_INACTIVE);
		  transitionFrame = 0;
	  }
  }
  public void begineTransition(BlockType targetType){
	  if(targetType != null){
		  if(targetType == BlockType.CAGE_ACTIVE){
			  transition = 1;
		  }
		  else if(targetType == BlockType.CAGE_INACTIVE){
			  transition = -1;
		  }
	  }
  }
  public void update(){
	  if(getState() != BlockState.FROZEN && (transitionFrame + transition >= 0) && (transitionFrame + transition < CLOSE_ANIMATION_FRAME_COUNT)){
		  transitionFrame += transition;
		  if(transitionFrame + 1 < CLOSE_ANIMATION_FRAME_COUNT/2){
			  super.setType(BlockType.CAGE_INACTIVE);
		  }
		  else{
			  super.setType(BlockType.CAGE_ACTIVE);
		  }
		  updateImage();
	  }
	  else {
		  transition = 0;
	  }
  }
  public void updateImage() {
    super.updateImage();
    if(super.getState() == BlockState.FROZEN){
    	setImage(ICED_IMAGE);
    }
    else{
    	setImage("blocks/closing/Block_Closing (" + (transitionFrame + 1) + ").png");
    }
  }
  /*
   * 
  WHY are these two functions here? THis looks wrong!!
  
  public void addToAutoDrawSet(){
	  super.addToAutoDrawSet();
	  super.destroy();
  }
  
  public void removeFromAutoDrawSet(){
	  
  }
  */
}
