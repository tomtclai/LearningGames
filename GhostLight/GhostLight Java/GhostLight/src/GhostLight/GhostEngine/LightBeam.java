
package GhostLight.GhostEngine;

import java.awt.Color;

import Engine.BaseCode;
import Engine.Rectangle;
import Engine.ResourceHandler;
import Engine.Vector2;

public class LightBeam extends Rectangle{
	
	private static final String LIGHTBEAM_CONE_SPRITE = ("light/LightBeam_Cone_r.png");
	private static final String LIGHTBEAM_LASER_SPRITE = ("light/LightBeam_Laser_r.png");
	private static final String LIGHTBEAM_WIDE_SPRITE = ("light/LightBeam_Wide_r.png");
	
	private static final String LIGHTBEAM_CONE_STATIC_OUTLINE = ("light/LightBeam_ConeOutline.png");
	private static final String LIGHTBEAM_LASER_STATIC_OUTLINE = ("light/LightBeam_LaserOutline.png");
	private static final String LIGHTBEAM_WIDE_STATIC_OUTLINE = ("light/LightBeam_WideOutline.png");
	
	private static final String LASER_SOUND = "Sound/ghostlight_laser_beam_fire.wav";
	
	public static void preLoadResources(ResourceHandler resources){
		resources.loadImage(LIGHTBEAM_CONE_STATIC_OUTLINE);
		resources.loadImage(LIGHTBEAM_LASER_STATIC_OUTLINE);
		resources.loadImage(LIGHTBEAM_WIDE_STATIC_OUTLINE);
		resources.loadImage(LIGHTBEAM_CONE_SPRITE);
		resources.loadImage(LIGHTBEAM_LASER_SPRITE);
		resources.loadImage(LIGHTBEAM_WIDE_SPRITE);
		
		resources.preloadSound(LASER_SOUND);
	}
	
  public enum BeamType {
    REVEAL, WIDE, LASER
  };

  private BeamType type;
  
  private AnimationStateHandler animationStates;
  
  private float WIDE_WIDTH = 25f;
  private float WIDE_LENGTH = 15f;
  private float LASER_WIDTH = 1f;
  private float LASER_ACTIVATED_WIDTH = 8f;
  private float LASER_LENGTH = 37f;
  private float REVEAL_WIDTH = 8f;
  private float REVEAL_LENGTH = 25;
  private int ANIMATION_SPEED = 1;
  private float centerXOffset = 0f;
  private float centerYOffset = 10f;
  
  private boolean active = false;	//Marks whether the light Draws as if it is on or Off
  
  
  //Stored to prevent extra new(s) durring each update
  protected StateChange storedStateChangeObject = new StateChange();
  
  
  public LightBeam(EnemySet hostSet) {
	setupAnimationStates();
    color = new Color(255, 255, 160, 100);
    makeLaser();
    this.addToAutoDrawSet();
    alwaysOnTop = true;
  }

  public BeamType getType(){
    return type;
  }

  public void activate(EnemySet hostSet) {
	  if(!active){
		  active = true;
		  if(type == BeamType.WIDE){
			  makeWide();
		  }
		  else if(type == BeamType.REVEAL){
			  makeReveal();
		  }
		  else if(type == BeamType.LASER){
			  BaseCode.resources.playSound(LASER_SOUND);
			  makeLaser();
		  }
	  }
  }
  public void Deactivate(EnemySet hostSet){
	  if(active){
		  active = false;
		  if(type == BeamType.WIDE){
			  makeWide();
		  }
		  else if(type == BeamType.REVEAL){
			  makeReveal();
		  }
		  else if(type == BeamType.LASER){
			  BaseCode.resources.stopSound(LASER_SOUND);
			  makeLaser();
		  }
	  }
  }
  /**
   * Will Make the Current Light a Wide Light
   */
  public void makeWide() {
	  if(type != BeamType.WIDE){
		  type = BeamType.WIDE;
	  }
	  if(active){
		  animationStates.shiftTimerState(LIGHTBEAM_WIDE_SPRITE,
				  LIGHTBEAM_WIDE_STATIC_OUTLINE, 40);
	  }
	  else{
		  animationStates.changeState(LIGHTBEAM_WIDE_STATIC_OUTLINE);
	  }
    
  }
  /**
   * Will Make the Current Light a Laser Light
   */
  public void makeLaser() {	  
	  if(type != BeamType.LASER){
		  type = BeamType.LASER;
	  }
	  if(active){
		  animationStates.shiftTimerState(LIGHTBEAM_LASER_SPRITE,
				  LIGHTBEAM_LASER_STATIC_OUTLINE, 40);
	  }
	  else{
		  animationStates.changeState(LIGHTBEAM_LASER_STATIC_OUTLINE);
	  }
  }
  /**
   * Will Make the Current Light a Revealing Light
   */
  public void makeReveal(){
	  if(type != BeamType.REVEAL){
		  type = BeamType.REVEAL;
	  }
	  if(active){
		  animationStates.shiftTimerState(LIGHTBEAM_CONE_SPRITE,
				  LIGHTBEAM_CONE_STATIC_OUTLINE, 40);
	  }
	  else{
		  animationStates.changeState(LIGHTBEAM_CONE_STATIC_OUTLINE);
	  }
  }
  public void updateBeamSizes(EnemySet hostSet){
	  if(hostSet != null){
		  if((hostSet.getCollumnSpacing() * 4) != WIDE_WIDTH){
			  WIDE_WIDTH = hostSet.getCollumnSpacing() * 4;
			  if(type == BeamType.WIDE){
				  type = BeamType.LASER;
			  		makeWide();
			  }
		  }
		  if((hostSet.getCollumnSpacing() * 2) != REVEAL_WIDTH){
			  REVEAL_WIDTH = hostSet.getCollumnSpacing() * 2;
			  if(type == BeamType.REVEAL){
				  type = BeamType.LASER;
				  makeReveal();	  
			  }
		  }
	  }
  }
  /*public void setTypeDuration(int value) {
    typeRemaining = value;
  }*/
  /**
   * Will Turn off the Light
   */
  public void stopType(){
	  this.visible = false;
  }
  public boolean isActive(){
	  return active;
  }
  /**
   * Will select Enemies within the current Enemy Set that would be affected by the light
   * @param targetSet The current Enemy Set
   * @param currentCollumn the Collumn within the Set that the light is on
   */
  public void selectTargetableEnemies(EnemySet targetSet, int currentCollumn){
	  if(targetSet != null){
		  BaseEnemy currentTarget = null;
		  //Reveal
		  if(type == BeamType.REVEAL){
			  for(int loop = currentCollumn - 1; loop <= currentCollumn + 1 && loop < targetSet.getNumberOfCollumns(); loop++){
				  currentTarget = getClosestEnemy(targetSet, loop);
				  if(currentTarget != null){
					  currentTarget.select();
				  }
			  }
		  }
		  //Wide
		  else if(type == BeamType.WIDE){
			  for(int loop = currentCollumn - 2; loop <= currentCollumn + 2 && loop < targetSet.getNumberOfCollumns(); loop++){
				  currentTarget = getClosestEnemy(targetSet, loop);
				  if(currentTarget != null){
					  currentTarget.select();
				  }
			  }
		  }
		  //Laser
		  else{
			  for(int loopRow = 0; loopRow < targetSet.getNumberOfRows(); loopRow++){
				  currentTarget = targetSet.getEnemyAt(loopRow, currentCollumn);
				  if(currentTarget != null){
					  currentTarget.select();
				  }
			  }
		  }
	  }
  }
  /**
	 * Will seach a given Enemy grid and will return the Enemy at the greatest index within that collumm
	 * @param Column The target collumn within the enemyGrid to be searched
	 * @param hostSet The set of Enemies to be searched
	 * @return The Enemy within the target collumn at the deapest row. Not if this is null no Enemy was found
	 */
	private BaseEnemy getClosestEnemy(EnemySet hostSet, int column){
		if(hostSet != null && column >= 0){
			for(int loopRow = hostSet.getNumberOfRows() - 1; loopRow >= 0; loopRow--){
				  if(column < hostSet.getNumberOfCollumns(loopRow)){
					  BaseEnemy retVal = hostSet.getEnemyAt(loopRow, column);
					  if(retVal != null){
						  return retVal;
					  }
				  }
			  }
		}
		return null;
	}
//	/**
//	 * Will rescale and reposition the light so that it will stretce from its base to the given marker
//	 * Note newDistance must be positive {@link #activate(int)}greater than zero
//	 * @param newDistance the given marker as a Y row in the world space
//	 */
//	private void rescaleLight(float newDistance){
//		if(newDistance > 0){
//			super.size.setY(super.center.getY() - super.size.getY() + newDistance);
//		}
//	}

	public void setCenterPosition(float x, float y) 
	{
		animationStates.updatePosition(x, y);;
	}
	
	public void update()
	{
		super.update();
		animationStates.update();
	}
	
	private void setupAnimationStates()
	{
	    animationStates = new AnimationStateHandler(this);
	    animationStates.setDefaultPosition(new Vector2());
	    
	    animationStates.addNewState(LIGHTBEAM_CONE_SPRITE, LIGHTBEAM_CONE_SPRITE,
	    		139, 306, 20, ANIMATION_SPEED, 0, 19,
	    		Rectangle.SpriteSheetAnimationMode.ANIMATE_FORWARD_STOP,
	    		new Vector2(REVEAL_WIDTH,REVEAL_LENGTH + 1f));
	 
	    animationStates.addNewState(LIGHTBEAM_WIDE_SPRITE, LIGHTBEAM_WIDE_SPRITE,
	    		311, 131, 20, ANIMATION_SPEED, 0, 19,
	    		Rectangle.SpriteSheetAnimationMode.ANIMATE_FORWARD_STOP,
	    		new Vector2(WIDE_WIDTH,WIDE_LENGTH));
	    
	    animationStates.addNewState(LIGHTBEAM_LASER_SPRITE, LIGHTBEAM_LASER_SPRITE,
	    		82, 349, 20, ANIMATION_SPEED, 0, 19,
	    		Rectangle.SpriteSheetAnimationMode.ANIMATE_FORWARD_STOP,
	    		new Vector2(LASER_ACTIVATED_WIDTH,LASER_LENGTH + 2.5f));
	    
	    animationStates.addNewState(LIGHTBEAM_CONE_STATIC_OUTLINE,
	    		LIGHTBEAM_CONE_STATIC_OUTLINE, 
	    		new Vector2(REVEAL_WIDTH,REVEAL_LENGTH -1f));
	    
	    animationStates.addNewState(LIGHTBEAM_WIDE_STATIC_OUTLINE,
	    		LIGHTBEAM_WIDE_STATIC_OUTLINE, 
	    		new Vector2(WIDE_WIDTH,WIDE_LENGTH));
	    
	    animationStates.addNewState(LIGHTBEAM_LASER_STATIC_OUTLINE,
	    		LIGHTBEAM_LASER_STATIC_OUTLINE, 
	    		new Vector2(LASER_WIDTH,LASER_LENGTH));
	    
	    animationStates.addPositionOffsetToState(LIGHTBEAM_CONE_SPRITE,
	    		new Vector2(centerXOffset, centerYOffset ));
	    
	    animationStates.addPositionOffsetToState(LIGHTBEAM_WIDE_SPRITE,
	    		new Vector2(centerXOffset, centerYOffset - 4f));
	    
	    animationStates.addPositionOffsetToState(LIGHTBEAM_LASER_SPRITE,
	    		new Vector2(centerXOffset, centerYOffset + 7.25f));
	    
	    animationStates.addPositionOffsetToState(LIGHTBEAM_CONE_STATIC_OUTLINE,
	    		new Vector2(centerXOffset, centerYOffset + 1f));
	    
	    animationStates.addPositionOffsetToState(LIGHTBEAM_WIDE_STATIC_OUTLINE,
	    		new Vector2(centerXOffset, centerYOffset - 4f));
	    
	    animationStates.addPositionOffsetToState(LIGHTBEAM_LASER_STATIC_OUTLINE,
	    		new Vector2(centerXOffset, centerYOffset + 8f));
	}
}

