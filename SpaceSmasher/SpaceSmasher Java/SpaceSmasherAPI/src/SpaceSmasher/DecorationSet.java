package SpaceSmasher;

import Engine.BaseCode;
import Engine.GameObject;
import Engine.ResourceHandler;
import Engine.SetOfGameObjects;
import Engine.Vector2;

class DecorationSet extends SetOfGameObjects<GameObject> 
{
	  private static boolean BACKGROUND_STATIC = true;
	
	
	  private static final String BARRIER_FIELD = "decorations/barrier_full.png";
	  private static final String ROTATING_JOINT = "decorations/SideBumper_RotatingJoint.png";
	  private static final String BUMPER_TRACK_LEFT = "decorations/SideBumper_Track.png";
	  private static final String BUMPER_TRACK_RIGHT = "decorations/BumperTrack_Right.png";
	  private static final String BOUNDING_BOX = "background/BG_BoundingBox.png";
	  
	  //private static final String BACKGROUND_BOUNDING_BOX = "background/BG_BoundingBox.png";
	  
	  private static final String STATIC_BACKGROUND = "background/Bg_StaticGuide.png";
	  private static final String BACKGROUND_LAYER1 = "background/BG_1.png";
	  private static final String BACKGROUND_LAYER2 = "background/BG_2.png";
	  private static final String BACKGROUND_LAYER4 = "background/BG_4.png";
	  
	  private static final String BOTTOM_LAYER_1 = "background/Bot1.png";
	  private static final String BOTTOM_LAYER_2 = "background/Bot2.png";
	  private static final String BOTTOM_LAYER_3 = "background/Bot3.png";
	  
	  private static final String TOP_LAYER_1 = "background/Top1.png";
	  private static final String TOP_LAYER_2 = "background/Top2.png";
	  private static final String TOP_LAYER_3 = "background/Top3.png";
	  
	  private static final String LEFT_LAYER_1 = "background/Side1 L.png";
	  private static final String LEFT_LAYER_2 = "background/Side2 L.png";
	  private static final String LEFT_LAYER_3 = "background/Side3 L.png";
	  private static final String LEFT_LAYER_4 = "background/Side4 L.png";
	  private static final String LEFT_LAYER_5 = "background/Side5 L.png";
	  
	  private static final String RIGHT_LAYER_1 = "background/Side1 R.png";
	  private static final String RIGHT_LAYER_2 = "background/Side2 R.png";
	  private static final String RIGHT_LAYER_3 = "background/Side3 R.png";
	  private static final String RIGHT_LAYER_4 = "background/Side4 R.png";
	  private static final String RIGHT_LAYER_5 = "background/Side5 R.png";

	  /**
	   * will load and sounds or images required for this class to function
	   * @param resources the handler to be used for pre-loading.
	   */
	  public static void preloadResources(ResourceHandler resources)
	  {
		  if(resources !=  null)
		  {
			  resources.preloadImage(ROTATING_JOINT);
			  resources.preloadImage(BUMPER_TRACK_LEFT);
			  resources.preloadImage(BUMPER_TRACK_RIGHT);
			  resources.preloadImage(BOUNDING_BOX);
			  resources.preloadImage(BARRIER_FIELD);
			  resources.preloadImage(BACKGROUND_LAYER1);
			  
			  if(BACKGROUND_STATIC)
			  {
				  resources.preloadImage(BACKGROUND_LAYER2);
			  }
			  else
			  {
				  resources.preloadImage(BACKGROUND_LAYER2);
				  resources.preloadImage(BACKGROUND_LAYER4);
				  
				  resources.preloadImage(TOP_LAYER_1);
				  resources.preloadImage(TOP_LAYER_2);
				  resources.preloadImage(TOP_LAYER_3);
				  
				  resources.preloadImage(BOTTOM_LAYER_1);
				  resources.preloadImage(BOTTOM_LAYER_2);
				  resources.preloadImage(BOTTOM_LAYER_3);
				  
				  resources.preloadImage(LEFT_LAYER_1);
				  resources.preloadImage(LEFT_LAYER_2);
				  resources.preloadImage(LEFT_LAYER_3);
				  resources.preloadImage(LEFT_LAYER_4);
				  resources.preloadImage(LEFT_LAYER_5);
				  
				  resources.preloadImage(RIGHT_LAYER_1);
				  resources.preloadImage(RIGHT_LAYER_2);
				  resources.preloadImage(RIGHT_LAYER_3);
				  resources.preloadImage(RIGHT_LAYER_4);
				  resources.preloadImage(RIGHT_LAYER_5);
			  }
		  }
	  }
	  
	  
	  private static float TRACK_HEIGHT_SCREEN_RATIO = 0.975f;
	  private static float TRACK_WIDTH = 5f;
	  private static float TRACK_CENTER_HEIGHT_SCREEN_RATIO = 0.445f;
	  private static float TRACK_OFFSET_FROM_SIDE_SCREEN_RATIO = .02f;
	  
	  
	  public void init()
	  {
		  initBackground();
		  initForeground();
	  }
	  /**
	   * Adds a joint to a location.
	   */
	  private void addJoint(Vector2 location)
	  {
		  GameObject joint = new GameObject();
		  joint.setSize(new Vector2(BaseCode.world.getHeight() * .04f,
				  BaseCode.world.getHeight() * .04f));
		  joint.setCenter(location);
		  joint.setSpriteSheet(ROTATING_JOINT, 36, 36, 36, 5);
		  joint.setUsingSpriteSheet(true);
		  this.add(joint);
	  }
	  
	  /**
	   * Initializes the background to:
	   * 
	   * From front-most layer to furthest back layer:
		BG_3 (sprite sheets of tile rings)
		Order for tile rings from front-most layers are as follows:
			1) Top Rings (Top1, 2, 3)
			2) Side Rings (Side1 L, Side 1 R, Side2 L... etc.)
			3) Bottom Rings (Bot1, 2, 3)
		BG_4
	   */
	  private void initBackground()
	  {
		  float width = BaseCode.world.getWidth();
		  float height = BaseCode.world.getHeight();
		  float cx = width / 2f;
		  float cy = height / 2f;
	  
		  if(BACKGROUND_STATIC)
		  {
			  GameObject staticBg = new GameObject(cx, cy, width, height);
			  staticBg.setImage(STATIC_BACKGROUND);
			  this.add(staticBg); 
			  
			return;  
		  }
		  
		  // background layer 4
		  GameObject backgroundlayer4 = new GameObject(cx, cy, width, height);
		  backgroundlayer4.setImage(BACKGROUND_LAYER4);
		  this.add(backgroundlayer4); 
		
		  // background layer 3
		  initBackgroundLayer3();
		  
		  // background layer 2
		  GameObject backgroundlayer2 = new GameObject(cx, cy, width, height);
		  backgroundlayer2.setImage(BACKGROUND_LAYER2);
		  this.add(backgroundlayer2);
		  
		  // background layer 1
		  GameObject backgroundlayer1 = new GameObject(cx, cy, width, height);
		  backgroundlayer1.setImage(BACKGROUND_LAYER1);
		  this.add(backgroundlayer1); 
		  
	  }
	  /**
	   * Handles the layer in the background with moving panels.
	   */
	  private void initBackgroundLayer3() 
	  {  
		  // Locations and sizes are based on world coord ratios.
		  float width = BaseCode.world.getWidth();
		  float height = BaseCode.world.getHeight();
		  
		  float scaleTopBottomWidthBy = .0525f;
		  float scaleTopBottomHeightBy = .0525f;
		  
		  float scaleRightLeftWidthBy = .0545f;
		  float scaleRightLeftHeightBy = .0545f;
		  
		  int frames = 36;
		  int buggedFrames = 33; // TODO: WTF is going on!
		  int ticksPerFrame = 1;
		  
		  //----------------------------
		  //-------- Layer 3 -----------
          //----------------------------
		  
		  // Top Layer 3
		  addPanel(930,85,scaleTopBottomWidthBy,scaleTopBottomHeightBy,
				  width * .5f, height * .665f,TOP_LAYER_3,frames,ticksPerFrame);
		  
		  // Bottom Layer 3
		  addPanel(923,83,scaleTopBottomWidthBy,scaleTopBottomHeightBy,
				  width * .5f, height * .212f,BOTTOM_LAYER_3,frames,ticksPerFrame);
		  
		  // Right Layer 5
		  addPanel(98,511,scaleRightLeftWidthBy,scaleRightLeftHeightBy,
				  width * .720f, height * .435f,RIGHT_LAYER_5,frames,ticksPerFrame);

		  // Left Layer 5
		  addPanel(103,511,scaleRightLeftWidthBy,scaleRightLeftHeightBy,
				  width * .285f, height * .435f,LEFT_LAYER_5,buggedFrames,ticksPerFrame);
		  
          //----------------------------
		  //-------- Layer 2 -----------
          //----------------------------

		  // Top Layer 2
		  addPanel(1225,137,scaleTopBottomWidthBy,scaleTopBottomHeightBy,
				  width * .5f, height * .715f,TOP_LAYER_2,frames,ticksPerFrame);
		  
		  // Top Layer 2
		  addPanel(1225,149,scaleTopBottomWidthBy,scaleTopBottomHeightBy,
				  width * .5f, height * .162f,BOTTOM_LAYER_2,frames,ticksPerFrame);
		  
		  //-------- Lower Layer 2 -----------

		  // Right Layer 4
		  addPanel(119,605,scaleRightLeftWidthBy,scaleRightLeftHeightBy,
				  width * .747f, height * .435f,RIGHT_LAYER_4,frames,ticksPerFrame);
		  
		  // Left Layer 4
		  addPanel(116,606,scaleRightLeftWidthBy,scaleRightLeftHeightBy,
				  width * .253f, height * .435f,LEFT_LAYER_4,frames,ticksPerFrame);
		  
		  
		  //--------- Upper Layer 2 -----------
		  		  
		  // Right Layer 3
		  addPanel(118,714,scaleRightLeftWidthBy,scaleRightLeftHeightBy,
				  width * .797f, height * .435f,RIGHT_LAYER_3,frames,ticksPerFrame);
		  
		  // Left Layer 3
		  addPanel(125,713,scaleRightLeftWidthBy,scaleRightLeftHeightBy,
				  width * .203f, height * .435f,LEFT_LAYER_3,frames,ticksPerFrame);
		  
		  //-----------------------------
		  //-------- Layer 1 ------------
		  //-----------------------------
		  
		  // Top Layer 1
		  addPanel(1579,163,scaleTopBottomWidthBy,scaleTopBottomHeightBy,
				  width * .5f, height * .805f,TOP_LAYER_1,frames,ticksPerFrame);
		  
		  // Bottom Layer 1
		  addPanel(1584,149,scaleTopBottomWidthBy,scaleTopBottomHeightBy,
				  width * .5f, height * .065f,BOTTOM_LAYER_1,frames,ticksPerFrame);
		  
		  //---------- Lower Layer 1 ------------

		  // Right Panel Layer 2
		  addPanel(137,814,scaleRightLeftWidthBy,scaleRightLeftHeightBy,
				  width * .847f, height * .435f,RIGHT_LAYER_2,buggedFrames,ticksPerFrame);
		  
		  // Left Panel Layer 2
		  addPanel(143,810,scaleRightLeftWidthBy,scaleRightLeftHeightBy,
				  width * .159f, height * .435f,LEFT_LAYER_2,frames,ticksPerFrame);
		  
		  
		  //----------- Upper Layer 1 ------------
		  
		  // Right Panel Layer 1
		  addPanel(147,937,scaleRightLeftWidthBy,scaleRightLeftHeightBy,
				  width * .897f, height * .435f,RIGHT_LAYER_1,frames,ticksPerFrame);
		  
		  // Left Panel Layer 1
		  addPanel(153,932,scaleRightLeftWidthBy,scaleRightLeftHeightBy,
				  width * .103f, height * .435f,LEFT_LAYER_1,frames,ticksPerFrame);
		  
      }
	  
	  private void addPanel(int spriteWidth, int spriteHeight, float scaleWidthBy,
			  float scaleHeightBy, float centerX, float centerY, String fileName,
			  int frames, int ticksPerFrame)
	  {
		  GameObject panel = new GameObject(
				  	spriteWidth * scaleWidthBy, 
				  	spriteHeight * scaleHeightBy,			  
				  	centerX, centerY);
		  panel.setSpriteSheet(fileName, spriteWidth, spriteHeight, frames, ticksPerFrame);
		  panel.setUsingSpriteSheet(true);
		  this.add(panel);
	  }
	  
	/**
	   * Initializes the background to:
	   * 
	   * From front-most layer to furthest back layer:

		BG_1
			Left/Right Bumper Track, Left/Right/Top Bumpers,
			Side Bumper Indicators, Bottom Generator, Paddle, Ball, Blocks
		BG_BoundingBox
		BG_2, Rotating Track Joints
	   */
	  private void initForeground()
	  {
		  // Locations and sizes are based on world coord ratios.
		  float width = BaseCode.world.getWidth();
		  float height = BaseCode.world.getHeight();
		  
		  GameObject leftBumperTrack = new GameObject(width * TRACK_OFFSET_FROM_SIDE_SCREEN_RATIO,
								height * TRACK_CENTER_HEIGHT_SCREEN_RATIO,
								TRACK_WIDTH,
				  				height * TRACK_HEIGHT_SCREEN_RATIO);
		  leftBumperTrack.setSpriteSheet(BUMPER_TRACK_LEFT, 76, 1024, 6, 5);
		  leftBumperTrack.setUsingSpriteSheet(true);
		  this.add(leftBumperTrack);
		  
		  GameObject rightBumperTrack = new GameObject(width * (1 - TRACK_OFFSET_FROM_SIDE_SCREEN_RATIO),
				  	height * TRACK_CENTER_HEIGHT_SCREEN_RATIO,
				  	TRACK_WIDTH,
				  	height * TRACK_HEIGHT_SCREEN_RATIO);
		  rightBumperTrack.setSpriteSheet(BUMPER_TRACK_RIGHT, 76, 1024, 6, 5);
		  rightBumperTrack.setUsingSpriteSheet(true);
		  this.add(rightBumperTrack);
		  
		  GameObject barrierField = new GameObject(width / 2f, height * 0.045f, width * 0.95f, height * 0.09f);
		  barrierField.setSpriteSheet(BARRIER_FIELD, 1248, 70, 72, 3);
		  barrierField.setUsingSpriteSheet(true);
		  this.add(barrierField);
		  
		  // Correct center and height for topbar.
		  GameObject foregroundBoundingBox = new GameObject(width / 2f,height / 2f, width, height);
		  foregroundBoundingBox.setImage(BOUNDING_BOX);
		  this.add(foregroundBoundingBox);
		  
		  // Bottom Joints
		  addJoint(new Vector2(width * .015f, height * .025f));
		  addJoint(new Vector2(width * .98f, height * .025f));
		  
		  // Top Joints
		  addJoint(new Vector2(width * .015f, height * .86f));
		  addJoint(new Vector2(width * .98f, height * .86f));
	  }
}