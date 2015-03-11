using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;

/**
 * Dracula: When revealed, Dracula will immediately jump to a random space somewhere in the
 * current chain (Dracula cannot jump to a space on the game board further than the leading 
 * occupied spot, although he can jump to an empty space within the current chain), bite 
 * whatever is in that space (revealed or unrevealed) and turn it into an angry ghost, then 
 * return to his original location. If the space is occupied by an unrevealed character there 
 * is no point loss; if the character is revealed, the player loses however many points that 
 * character was worth. If the space is empty, Dracula will remain in it after jumping. If 
 * the space is filled with an Angry Ghost, Dracula will explode that ghost upon biting it 
 * and the player will lose a life. Dracula repeats this behavior every other turn until he 
 * is removed. Dracula can only be removed by the charge beam. Dracula is worth 50 points.
 * @author Michael Letter
 */
namespace GhostFinder.GhostEngine{
    public class Vampire : BaseEnemy{
	
	    public static void preLoadResources(ResourceLibrary library){
            if(library.getResource("GhostLight/GhostLight/resources/dracula.png") == null){
                Sprite vampire = new Sprite("GhostLight/GhostLight/resources/dracula.png");
                vampire.loadResource();
                library.addResource(vampire);
            }
	    }
        private Sprite dracula = null;
        private static Random rand = new Random();

	    protected int targetEnemyCollumn = -1;
	    protected BaseEnemy byteTarget = null;
	    protected int byteTime = 0;
	    protected int bytePos = 0;
	
	    protected EnemySubSetNode currentTarget = null;
	
      public Vampire(ResourceLibrary library) :base(library){
          dracula = (Sprite)library.getResource("GhostLight/GhostLight/resources/dracula.png");
	      type = InteractableObject.ObjectType.VAMPIRE;
	      unrevealType();
	      health.setMaxSegments(InteractableObject.getDefualthealth(type));
	      health.setFilledSegments(health.getMaxSegments());
	      score = InteractableObject.getDefualtScore(type);
	      base.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
      }

      protected override void updateImage() {
        if(isTypeRevealed){
            base.setSprite(dracula);
            base.setFrame(0);
        }
        else{
            base.updateImage();
        }
      }
      public override StateChange updateTurn(){
	      base.updateTurn();
	      byteTime = 0;
	      if(isTypeRevealed){
		      return setByteTarget();
	      }
	      return null;  
      }
      public override StateChange revealType(int pauseTime){
          EnemySoundManager.requestSound(EnemySoundManager.SoundType.DRACULA_INTRO);
	      storedStateChangeObject.setToDefualts();
	      base.revealType(pauseTime);
	      setByteTarget();
	      if(pauseTime == 0){
		      BiteTarget(byteTarget);
	      }
	      byteTime = pauseTime;
	      bytePos = 0;
	      storedStateChangeObject.puaseForAnimation = true;
	      return storedStateChangeObject;
    
      }
      /**
       * Will set the center of this objects position to the appropriate position in the animation and increment animPos
       * Changes animation to move to TargetPosition then move to the set EndPosition
       */
      protected override void setCenterToAnimatedPosition(){
	      //From StartPos to TargetEnemy in (animTime/2)
	      if(isTypeRevealed && byteTarget != null && !removeAfterAnimation){
              PointF targetEnemyPosition = new PointF();
              targetEnemyPosition.X = byteTarget.getCenterX();
              targetEnemyPosition.Y = byteTarget.getCenterY();
		      //IfBiting at Reveal vs during normal Update
		      if(byteTime > 0){
			    //Moving Two Enemy
			      if(bytePos < (byteTime/2)){
				      base.setCenterX((float)(((targetEnemyPosition.X - animPosStart.X) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)bytePos) - ((Math.PI / 4f) * (float)byteTime)) / ((float)byteTime / 4f))) + animPosStart.X));
                      base.setCenterY((float)(((targetEnemyPosition.Y - animPosStart.Y) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)bytePos) - ((Math.PI / 4f) * (float)byteTime)) / ((float)byteTime / 4f))) + animPosStart.Y));
			      }
			      //Reached Enemy
			      //From TargetEnemy to EndPos in (animTime/2)
			      else if(bytePos == (byteTime/2)){
                      base.setCenterX((float)(((targetEnemyPosition.X - animPosEnd.X) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)bytePos) - ((Math.PI / 4f) * (float)byteTime)) / ((float)byteTime / 4f))) + animPosEnd.X));
                      base.setCenterY((float)(((targetEnemyPosition.Y - animPosEnd.Y) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)bytePos) - ((Math.PI / 4f) * (float)byteTime)) / ((float)byteTime / 4f))) + animPosEnd.Y));
				      BiteTarget(byteTarget);
			      }
			      //From TargetEnemy to EndPos in (animTime/2)
			      else{
                      base.setCenterX((float)(((targetEnemyPosition.X - animPosEnd.X) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)bytePos) - ((Math.PI / 4f) * (float)byteTime)) / ((float)byteTime / 4f))) + animPosEnd.X));
                      base.setCenterY((float)(((targetEnemyPosition.Y - animPosEnd.Y) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)bytePos) - ((Math.PI / 4f) * (float)byteTime)) / ((float)byteTime / 4f))) + animPosEnd.Y));
			      }
			      bytePos++;
		      }
		      //Biting Enemy durring normal Turn
		      else if(byteTime <= 0){
			      //Moving Two Enemy
			      if(animPos < (animTime/2)){
                      base.setCenterX((float)(((targetEnemyPosition.X - animPosStart.X) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)animPos) - ((Math.PI / 4f) * (float)animTime)) / ((float)animTime / 4f))) + animPosStart.X));
                      base.setCenterY((float)(((targetEnemyPosition.Y - animPosStart.Y) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)animPos) - ((Math.PI / 4f) * (float)animTime)) / ((float)animTime / 4f))) + animPosStart.Y));
			      }
			      //Reached Enemy
			      //From TargetEnemy to EndPos in (animTime/2)
			      else if(animPos == (animTime/2)){
                      base.setCenterX((float)(((targetEnemyPosition.X - animPosEnd.X) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)animPos) - ((Math.PI / 4f) * (float)animTime)) / ((float)animTime / 4f))) + animPosEnd.X));
                      base.setCenterY((float)(((targetEnemyPosition.Y - animPosEnd.Y) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)animPos) - ((Math.PI / 4f) * (float)animTime)) / ((float)animTime / 4f))) + animPosEnd.Y));
				      BiteTarget(byteTarget);	
			      }
			      //From TargetEnemy to EndPos in (animTime/2)
			      else{
                      base.setCenterX((float)(((targetEnemyPosition.X - animPosEnd.X) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)animPos) - ((Math.PI / 4f) * (float)animTime)) / ((float)animTime / 4f))) + animPosEnd.X));
                      base.setCenterY((float)(((targetEnemyPosition.Y - animPosEnd.Y) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)animPos) - ((Math.PI / 4f) * (float)animTime)) / ((float)animTime / 4f))) + animPosEnd.Y));
			      }
		      }
	      }
	      //Normal Unrevealed Movement
	      else{
              base.setCenterToAnimatedPosition();
	      }
      }
      /**
       * will remove this Enemy From the currentEnemyGrid
       * Note this does Reset any previous changes to the stored storedStateChangeObject
       * before making its own changes
       */
      public override StateChange removeThis(){
	      if(currentTarget != null){
		      currentTarget.removeNode();
		      currentTarget = null;
	      }
	      byteTarget = null;
	      this.targetEnemyCollumn = -1;
          return base.removeThis();
      }
      /**
       * Will set the Target of the Byte and will Deduct any points that will need to be Deducted as a result of the byte
       * Will Also Set the Animation to move towards the Byte target within the Fist half of the animation ime
       * @return The super.storedStateChangeObject as the change game state
       */
      private StateChange setByteTarget(){
	      storedStateChangeObject.setToDefualts();
	      bytePos = 0;
	      if(currentTarget != null){
		      currentTarget.removeNode();
	      }
	      if(hostSet != null){
		      //choosing Target Enemy
		      targetEnemyCollumn = getRandomCollomn();
		      byteTarget = hostSet.getEnemyAt(currentRow, targetEnemyCollumn);
		      //Deducting Points
		      if(byteTarget != this){
			      //Byte
			      if(byteTarget != null && byteTarget.type != InteractableObject.ObjectType.CAT){
				      if(byteTarget.isTypeRevealed){
					      storedStateChangeObject.changeInPlayerScore =- byteTarget.getScore();
				      }
				      if(byteTarget.type == InteractableObject.ObjectType.ANGRY){
					      storedStateChangeObject.add(((Ghost)byteTarget).getExplodeEffects());
				      }
			      }
			      //Move
			      else if(byteTarget == null){
				      hostSet.moveEnemy(this, currentRow, targetEnemyCollumn);
                      base.setAnimationTarget(currentRow, targetEnemyCollumn);
			      }
			      //Idle
			      else{
				      targetEnemyCollumn = -1;
				      byteTarget = null;
			      }
			      //is enemy already targeted
			      if(isEnemyAlreadyTargeted(byteTarget)){
				      targetEnemyCollumn = -1;
				      byteTarget = null;
			      }
			      //targeting enemy
			      else{
				      currentTarget = targetEnemy(byteTarget);
			      }
		      }
		      else{
			      byteTarget = null; 
		      }  
	      }
	      return storedStateChangeObject;
      }
      /**
       * Will Bite a Random Enemy on the current Row
       * @return the Score Decrement as a result of the Bite
       */
      private void BiteTarget(BaseEnemy target){
	      if(hostSet != null && target != null){
              EnemySoundManager.requestSound(EnemySoundManager.SoundType.DRACKULA_ATTACK);
	    	    //Target is Ghost
	    	    //Making Angry
	    	    if(target.type == InteractableObject.ObjectType.GHOST){
	    		    //if not revealed
	    		    if(!target.isTypeRevealed){
	    			    target.revealType(0);
	    		    }
	    		    ((Ghost)target).MakeAngry();
	    	    }
	    	    //Target is already an angry Ghost
	    	    else if(target.type == InteractableObject.ObjectType.ANGRY){
	    		    //if not revealed
	    		    if(!target.isTypeRevealed){
	    			    target.revealType(0);
	    		    }
	    		    ((Ghost)target).explode();
	    		    removeThis();
	    	    }
	    	    //Target is not an Angry Ghost
	    	    //Removing Target and Making it an angry Ghost
	    	    else if(target != this && target.type != InteractableObject.ObjectType.CAT){
	    		    target.removeThis();
	    		    Ghost angryGhost = new Ghost(BaseCode.activeLibrary);
	    		    angryGhost.revealType(0);
	    		    hostSet.addEnemy(angryGhost, currentRow, targetEnemyCollumn);
                    angryGhost.setCenter(base.getCenterX(), base.getCenterY());
	    		    angryGhost.setAnimationTarget(currentRow, targetEnemyCollumn);
	    	    }
	      }
	      if(currentTarget != null){
		     currentTarget.removeNode();
		     currentTarget = null;
	      }
	      targetEnemyCollumn = -1;
      }
      //will return a random column in the current Enemy Grid
      private int getRandomCollomn(){
	    if(hostSet != null && hostSet.getNumberOfCollumns(currentRow) > 0){	
		    int MaxAttempts = 5;
		    int retVal = (int)(rand.NextDouble() * (hostSet.getNumberOfCollumns(currentRow) - 0.2));
		    while(retVal == currentCollumn && MaxAttempts >= 0){
                retVal = (int)(rand.NextDouble() * (hostSet.getNumberOfCollumns(currentRow) - 0.2));
			    MaxAttempts--;
		    }
		    return retVal;
	    }
	    return 0;
      }
    }
}
