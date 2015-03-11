using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CustomWindower.CoreEngine;


namespace GhostFinder.GhostEngine{
    public class LightBeam : DrawRectangle{

        private static Sprite lazer = null;
        private static Sprite light = null;
        private static Sound LazerSound = null;

	    public static void preLoadResources(ResourceLibrary library){
            //Lazer
            if(library.getResource("GhostLight/GhostLight/resources/lazer.png") == null){
                lazer = new Sprite(new string[] { "GhostLight/GhostLight/resources/lazer.png", "GhostLight/GhostLight/resources/lazerGuide.png" });
                lazer.loadResource();
                library.addResource(lazer);
            }
            else{
                lazer = (Sprite)library.getResource("GhostLight/GhostLight/resources/lazer.png");
            }
            //Light
            if(library.getResource("GhostLight/GhostLight/resources/light.png") == null){
                light = new Sprite(new string[] { "GhostLight/GhostLight/resources/light.png", "GhostLight/GhostLight/resources/lightGuide.png" });
                light.loadResource();
                library.addResource(light);
            }
            else{
                light = (Sprite)library.getResource("GhostLight/GhostLight/resources/light.png");
            }
            //Sound
            if(library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_laser_beam_fire.wav") == null){
                LazerSound = new Sound("GhostLight/GhostLight/resources/Sounds/ghostlight_laser_beam_fire.wav");
                LazerSound.loadResource();
                library.addResource(LazerSound);
            }
            else{
                LazerSound = (Sound)library.getResource("GhostLight/GhostLight/resources/Sounds/ghostlight_laser_beam_fire.wav");
            }
            LazerSound.setVolume(0.5);
	    }
	
      public enum BeamType {
        REVEAL, WIDE, LASER
      };

      private BeamType type;
  
      private float WIDE_WIDTH = 25f;
      private float WIDE_LENGTH = 15f;
      private float LASER_WIDTH = 1f;
      private float LASER_LENGTH = 100;
      private float REVEAL_WIDTH = 8f;
      private float REVEAL_LENGTH = 25;
  
      private bool active = false;	//Marks whether the light Draws as if it is on or Off
  
      protected StateChange storedStateChangeObject = new StateChange();//Stored to prevent extra new(s) durring each update
  
  
      public LightBeam(EnemySet hostSet) {
          base.rectangleVisible = false;
          makeLaser();
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
			      makeLaser();
                  LazerSound.playSound();
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
			      makeLaser();
                  LazerSound.stopSound();
		      }
	      }
      }
      /**
       * Will Make the Current Light a Wide Light
       */
      public void makeWide() {
	      if(type != BeamType.WIDE){
		      type = BeamType.WIDE;
		      base.setWidth(WIDE_WIDTH);
              base.setHeight(WIDE_LENGTH);
              base.setSprite(light);
              base.setRotation(0);
	      }
	      if(active){
		      base.setFrame(0);
	      }
	      else{
              base.setFrame(1);
	      }
    
      }
      /**
       * Will Make the Current Light a Laser Light
       */
      public void makeLaser() {	  
	      if(type != BeamType.LASER){
		      type = BeamType.LASER;
              base.setWidth(LASER_WIDTH);
              base.setHeight(LASER_LENGTH);
              base.setSprite(lazer);
              base.setRotation(180);
	      }
	      if(active){
              base.setFrame(0);
	      }
	      else{
              base.setFrame(1);
	      }
      }
      /**
       * Will Make the Current Light a Revealing Light
       */
      public void makeReveal(){
	      if(type != BeamType.REVEAL){
		      type = BeamType.REVEAL;
              base.setWidth(REVEAL_WIDTH);
              base.setHeight(REVEAL_LENGTH);
              base.setSprite(light);
              base.setRotation(0);
	      }
	      if(active){
              base.setFrame(0);
	      }
	      else{
              base.setFrame(1);
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
      public bool isActive(){
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
	    /**
	     * Will rescale and reposition the light so that it will stretce from its base to the given marker
	     * Note newDistance must be positive {@link #activate(int)}greater than zero
	     * @param newDistance the given marker as a Y row in the world space
	     */
	    private void rescaleLight(float newDistance){
		    if(newDistance > 0){
                base.setHeight(base.getCenterY() - base.getHeight() + newDistance);
		    }
	    }
    }
}
