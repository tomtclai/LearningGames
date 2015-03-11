using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;

namespace GhostFinder.GhostEngine{

    public class Player : DrawRectangle {

        private static Sprite player = null;
        public static void preLoadResources(ResourceLibrary library) {
            if(library.getResource("GhostLight/GhostLight/resources/flashlightBot.png") == null){
                player = new Sprite("GhostLight/GhostLight/resources/flashlightBot.png");
                player.loadResource();
                library.addResource(player);
            }
            else{
                player = (Sprite)library.getResource("GhostLight/GhostLight/resources/flashlightBot.png");
            }
        }

      private LightBeam light = null;
      private int score = 0;			//The Players current score
      protected int currentCollumn = 0;	//The index of the Column the Player Currently Occupies	
      protected int collumnNum = 10;	//The total number of columns available to the
      protected EnemySet hostSet = null;//The Enemy set the player will affecting
 

      public Player() {
        base.setSize(10.0f, 10.0f);
        light = new LightBeam(hostSet);
        base.setSprite(player);
        base.rectangleVisible = false;
      }
      /**
       * Will Increment the Players X and Y positions by the given Increments
       * @param Xincrement The Distance and Direction moved Horizontally 
       * @param Yincrement The Distance and Direction moved Vertically 
       */
      public void incrementPosition(float Xincrement, float Yincrement){
          base.setCenterX(base.getCenterX() + Xincrement);
          base.setCenterY(base.getCenterY() + Yincrement);
      }
      /**
       * Will set the amount of space traveled when the Player moves left or right
       * @param newMoveSpeed the Desired Distance for the player to move
       */
      public void setEnemySet(EnemySet targetSet){
	      hostSet = targetSet;
	      movePlayer(0);
      }
      /**
       * Will set the collumn the player will occupy and the corrospoding world position
       * @param currentCollumnIndex the given column
       */
      public void setCollumn(int currentCollumnIndex){
	      currentCollumn = currentCollumnIndex;
	      if(hostSet != null){
		      //fixing currentCollumn
		      if(currentCollumn >= hostSet.getNumberOfCollumns()){
			      currentCollumn = hostSet.getNumberOfCollumns() - 1;
		      }
		      if(currentCollumn < 0){
			      currentCollumn = 0;
		      }
		      //Calculating knew world position
		      PointF pos = hostSet.getWorldPosition( 0, currentCollumn);
              base.setCenterX(pos.X);
	      }
	      else{
              base.setCenterX(BaseCode.worldWidth/2);
		  
	      }
      }
      /**
       * Will return the collumn the player is currently in
       * @return  the collumn index the player resides
       */
      public int getCurrentCollumn(){
	      return currentCollumn;
      }
      /**
       * Will attempt to move the player by the given amount if this amount 
       * moves the player to a collumn that is outside the bounds of the defined
       * collumn indices then the player will be moved as far as is possible while
       * remaining within the bounds 
       * @param collumnMovement	the number of indecies the player should be moved over
       */
      public void movePlayer(int collumnMovement){
	      //movement breaks lower bound
	      if(hostSet != null){
		      //Calculating ideal collumn
		      currentCollumn += collumnMovement;
		      //fixing currentCollumn
		      if(currentCollumn >= hostSet.getNumberOfCollumns()){
			      currentCollumn = hostSet.getNumberOfCollumns() - 1;
		      }
		      if(currentCollumn < 0){
			      currentCollumn = 0;
		      }
		      //Calculating knew world position
		      PointF pos = hostSet.getWorldPosition(0, currentCollumn);
              base.setCenterX(pos.X);
	      }
	      else{
              base.setCenterX(BaseCode.worldWidth / 2);
	      }
	  
      }
      public bool isLightActive(){
	      return light.isActive();
      }
      public void turnLightOnorOff(bool onOff){
	      if(onOff){
		      light.activate(hostSet); 
	      }
	      else{
		      light.Deactivate(hostSet);
	      }
      }
      /**
       *  Will select Enemies within the current Enemy Set that would be affected by the light
       */
      public void selectTargetableEnemies(){
	      if(hostSet != null){
		      light.selectTargetableEnemies(hostSet, currentCollumn);
	      }
      }
      public int getScore(){
	      return score;
      }
      public void setScore(int newScore){
	      if(newScore >= 0){
		      score = newScore;
	      }
      }
      public void incrementScore(int increment){
	      score += increment;
	      if(score < 0){
		      score = 0;
	      }
      }
      public LightBeam getLight(){
        return light;
      }
      private void updateLight(){
          light.setCenter(base.getCenterX(), base.getCenterY() - (base.getHeight()/2) - (light.getHeight()/2));
      }
      public void update(){
        updateLight();
      }
      public override void paint(Camera drawLocation) {
          base.paint(drawLocation);
          light.paint(drawLocation);
      }
    }
}
