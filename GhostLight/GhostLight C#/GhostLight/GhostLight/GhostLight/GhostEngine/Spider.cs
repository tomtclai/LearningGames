using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;
using MenueSystem;

/**
 * Spider: when revealed, the spider immediately drops-down two rows. If the space is empty a new row will 
 * be created and populated with unrevealed characters to the left of the spider’s location. If the space 
 * is occupied the spider will “eat” the character and subtract whatever its point value was from the player’s
 * score (in the case of a spider eating an unrevealed character it will subtract 10 points from the player 
 * score).
 * @author Michael Letter
 */
namespace GhostFinder.GhostEngine{
    public class Spider : BaseEnemy{
	
	    public static void preLoadResources(ResourceLibrary library){
            if(library.getResource("GhostLight/GhostLight/resources/spider.png") == null){
                Sprite spiderMan = new Sprite("GhostLight/GhostLight/resources/spider.png");
                spiderMan.loadResource();
                library.addResource(spiderMan);
            }
	    }
	
      private static Random rand = new Random();

      private Sprite spider = null;

      protected int totalPauseTime = 0;					//the total time that the game will be paused for reveal
      protected static readonly int MAX_DROP_DISTANCE = 1;//the Total Distance the Spider will try to drop
      protected int dropDistance = MAX_DROP_DISTANCE+1;	//The number of Rows Down The spider will Drop down
      protected PointF hangTop = new PointF();			//The Position that Spiders Web will stretch From
      protected PointF dropTarget = new PointF();		//The target that the spider is moving towards during its drop
      protected PointF dropStart = new PointF();	    //The starting point that the spider is doping from
      protected bool dropping = false;
      protected int dropTime = 0;						//Used to mark the time total time available to drop and down 1 row
      protected int dropPos = 0;						//The Spiders current position in the drop
      protected DrawRectangle web = null;				//The Web the spider will hang from while Droping
  
      public Spider(ResourceLibrary library) : base(library){
            spider = (Sprite)library.getResource("GhostLight/GhostLight/resources/spider.png");
            
  	        type = InteractableObject.ObjectType.SPIDER;
  	        unrevealType();
  	        health.setMaxSegments(InteractableObject.getDefualthealth(type));
	        health.setFilledSegments(health.getMaxSegments());
            score = InteractableObject.getDefualtScore(type);
            base.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
      }
      public override StateChange updateTurn(){
	      return base.updateTurn();
      }
      /**
       * Will deduct points for each Enemy to be eaten and set first target for the spider to drop
       * and will Determine how many Rows the Spider will Drop 
       */
      public override StateChange revealType(int pauseTime){
	      //checking if currentEnemyGrid and position are up to date
	      storedStateChangeObject.setToDefualts();
	      if(!isTypeRevealed){
		      base.revealType(pauseTime);
		      //setting targets to move Towards
		      dropDistance = 0;
		      setMoveTarget();
		      initializeWeb();
		      //Deducting points as a result of Drop
		      if(hostSet != null){
			      base.revealType(pauseTime);
			      //checking if two rows below the spider exist
			      /*if(hostSet.doesGridPositionExist(currentRow, currentRow + 2)){
				      //Jumping Down 1 row
				      BaseEnemy target = hostSet.getEnemyAt(currentRow + 1, currentCollumn);
				      if(target != null){
					      storedStateChangeObject.changeInPlayerScore -= target.getScore();
				      }
				      //Jumping Down 2 row
				      target = hostSet.getEnemyAt(currentRow + 2, currentCollumn);
				      if(target != null){
					      storedStateChangeObject.changeInPlayerScore -= target.getScore();
				      }		
				      storedStateChangeObject.puaseForAnimation = true;
				      dropTime = pauseTime/3;
			      }
			      //checking if one rows below the spider exist
			      else */if(hostSet.doesGridPositionExist(currentRow, currentRow + 1)){
				      //Jumping Down 1 row
				      BaseEnemy target = hostSet.getEnemyAt(currentRow + 1, currentCollumn);
				      if(target != null){
					      storedStateChangeObject.changeInPlayerScore -= target.getScore();
				       }
				      storedStateChangeObject.puaseForAnimation = true;
				      dropTime = pauseTime/2;
			      }
			      if(dropTime == 0){
				      hostSet.moveEnemy(this, currentRow + dropDistance, currentCollumn);
				      fillRowWithEnemies(currentRow + dropDistance);
				      this.setAnimationTarget(currentRow, currentCollumn);
			      }
		      }
	      }
	      else if(getInfectStatus() <= 0){
		      return removeThis();
	      }
	      return storedStateChangeObject;
      }
      /**
       * Will set the center of this objects position to the appropriate position in the animation and increment animPos
       * Changes animation to move to drop down two rows eating the Enemies in the way when revealed
       */
      protected override void setCenterToAnimatedPosition(){
          if (dropping && dropTime > 0) {
		      //Moving Spider
		      //if moved down one row successfully
		      if(dropPos >= dropTime){
			      EatVictem(base.currentRow + dropDistance, currentCollumn);
			      setMoveTarget();			  
		      }
		      //Updating Position if target was reset successfully
		      if(dropping){
			      base.setCenterX((float)(((dropTarget.X - dropStart.X)/2f) * (1f + Math.Cos((((Math.PI/2f)*(float)dropPos) - ((Math.PI/2)*(float)dropTime)) / ((float)dropTime/2f)))+dropStart.X));
			      base.setCenterY((float)(((dropTarget.Y - dropStart.Y)/2f) * (1f + Math.Cos((((Math.PI/2f)*(float)dropPos) - ((Math.PI/2)*(float)dropTime)) / ((float)dropTime/2f)))+dropStart.Y));
			      dropPos++;
			      //Updating Web
			      //creating new web if nessesary
			      if(web != null && hangTop != null){
				    //updating web position and length
				      setWebPositionSize();
			      }
		      }
		      //Moving to position and filling row
		      else{
			      hostSet.moveEnemy(this, currentRow + dropDistance, currentCollumn);
			      fillRowWithEnemies(currentRow);
			      this.setAnimationTarget(currentRow, currentCollumn);
		      }
	      }
	      else{
		      dropping = false;
		      if(web != null){
			      web.visible = false;
		      }
		      web = null;
		      base.setCenterToAnimatedPosition();
	      }
      }
      protected override void updateImage() {
        if(isTypeRevealed){
            base.setSprite(spider);
            base.setFrame(0);
        }
        else{
            base.updateImage();
        }
      }
      /**
       * Will set the target of the Animation and the Time for it to take place for a Drop
       */
      protected void setMoveTarget(){
	    //checking if currentEnemyGrid and position are up to date
	      if(hostSet != null){
		      //Will set spider to drop down one row
		      if(dropDistance < MAX_DROP_DISTANCE && hostSet.getNumberOfRows() > (currentRow + dropDistance + 1)){
			      dropping = true;
                  dropStart.X = base.getCenterX();
                  dropStart.Y = base.getCenterY();
			      dropDistance++;
			      BaseEnemy target = hostSet.getEnemyAt(currentRow+dropDistance, currentCollumn);
			      if(target != null && target != this){
				      dropTarget.X = target.getCenterX();
                      dropTarget.Y = target.getCenterY();
			      }
			      else{
                      dropTarget.X = base.getCenterX();
                      dropTarget.Y = base.getCenterY();
				      dropTarget = hostSet.getWorldPosition(currentRow + dropDistance, currentCollumn);
			      }
			      //setting Web hang point and initializing web
			      setWebPositionSize();;
		      }
		      else{
			      dropping = false;
		      }
	      }
      }
      /**
       * Will eat the Victem at the Given location
       * @param row	the row the Victem the victem lies in
       * @param column the column the Victem the victem lies in
       * @return The change in score as a result of the meal
       */
      protected void EatVictem(int row, int column){
	      if(hostSet != null){
		      BaseEnemy victem = hostSet.getEnemyAt(row, column);
		      if(victem != null && victem != this){
			      victem.removeThis();
		      }
	      }
      }
      /**
       * Will Fill a given Row with a random Set of Enemies
       * @param row the given Row
       */
      protected void fillRowWithEnemies(int row){
	      if(hostSet != null){
		      for(int loop = 0; loop < hostSet.getNumberOfCollumns(row); loop++){
			      if(hostSet.getEnemyAt(row, loop) == null){
				      BaseEnemy temp = getRandomEnemy();
                      temp.setCenterX(this.getCenterX());
                      temp.setCenterY(this.getCenterY());
				      hostSet.addEnemy(temp, row, loop);
			      }
		      }
	      }
      }
      protected void initializeWeb(){
	  
	      if(web == null){
		      web = new DrawRectangle();
		      web.fillColor = Color.Black;
		      web.visible = false;
	      }
		  hangTop.X = base.getCenterX();
          hangTop.Y = base.getCenterY();

      }
      protected void setWebPositionSize(){
	      if(hostSet != null && web != null && hangTop != null){
		      //updating web position and length
              web.setCenterX((hangTop.X + base.getCenterX())/2);
              web.setCenterY((hangTop.Y + base.getCenterY())/2);
              web.setWidth(1f);
		      web.setHeight(dropStart.Y - base.getCenterY() + (hostSet.getRowSpacing() * (dropDistance-1)));
		      web.visible = true;
	      }
      }
      /**
       * Will return a random Enemy based on the Following Probabilities
       * 11% Angry Ghost
       * 11% Cat
       * 11% Frankenstien
       * 11% Ghost
       * 11% Mummy
       * 12% Pumpkin
       * 11% Spider
       * 11% Vampire
       * 11% Zombie
       * 
       * @return The Randomly Generated Enemy
       */
      protected BaseEnemy getRandomEnemy(){
          double percent = rand.NextDouble();
	      //11% Angry Ghost
	      if(percent <= 0.11){
		      Ghost retVal = new Ghost(BaseCode.activeLibrary);
		      retVal.MakeAngry();
		      return retVal;
	      }
	      //11% Cat
	      else if(percent <= 0.22){
              return new Cat(BaseCode.activeLibrary);
	      }
	      //11% Frankenstien
	      else if(percent <= 0.33){
              return new Frankenstein(BaseCode.activeLibrary);
	      }
	      //11% Ghost
	      else if(percent <= 0.44){
              return new Ghost(BaseCode.activeLibrary);
	      }
	      //11% Mummy
	      else if(percent <= 0.55){
              return new Mummy(BaseCode.activeLibrary);
	      }
	      //12% Pumpkin
	      else if(percent <= 0.67){
              return new Pumpkin(BaseCode.activeLibrary);
	      }
	      //11% Spider
	      else if(percent <= 0.78){
              return new Spider(BaseCode.activeLibrary);
	      }
	      //11% Vampire
	      else if(percent <= 0.89){
              return new Vampire(BaseCode.activeLibrary);
	      }
	      //11% Zombie
	      else {
              return new Zombie(BaseCode.activeLibrary);
	      }
      }
    }
}
