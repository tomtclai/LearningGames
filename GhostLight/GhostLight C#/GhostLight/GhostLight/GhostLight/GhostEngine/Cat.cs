using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;
using MenueSystem;
/**
 * @author Michael Letter
 */
namespace GhostFinder.GhostEngine{
    public class Cat : BaseEnemy{
	
	    public static void preLoadResources(ResourceLibrary library){
            if(library.getResource("GhostLight/GhostLight/resources/cat.png") == null){
                cat = new Sprite("GhostLight/GhostLight/resources/cat.png");
                cat.loadResource();
                library.addResource(cat);
            }
            else{
                cat = (Sprite)library.getResource("GhostLight/GhostLight/resources/cat.png");
            }

	    }

        private static Random rand = new Random();

        private static  Sprite cat = null;

	    protected int targetEnemyCollumn = -1;
	    protected BaseEnemy byteTarget = null;
	    protected int byteTime = 0;
	    protected int bytePos = 0;
	
	    protected List<BaseEnemy> targtableGhosts = null;
	    protected EnemySubSetNode currentTarget = null;
	
	    public Cat(ResourceLibrary library) : base(library) {
		    type = InteractableObject.ObjectType.CAT;
		    health.setMaxSegments(InteractableObject.getDefualthealth(type));
		    health.setFilledSegments(health.getMaxSegments());
		    score = InteractableObject.getDefualtScore(type);
		    base.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
		    unrevealType();
		    isHelpfull = true;
	    }
	    protected override void updateImage() {
		    if(isTypeRevealed){
                base.setSprite(cat);
                base.setFrame(0);
		    }
            else{
                base.updateImage();
            }
	    }
	     public override StateChange revealType(int pauseTime){
		      storedStateChangeObject.setToDefualts();
		      base.revealType(pauseTime);
		      setByteTarget();
		      if(pauseTime == 0){
			      ByteTarget(byteTarget);
		      }
		      byteTime = pauseTime;
		      bytePos = 0;
		      storedStateChangeObject.puaseForAnimation = true;
		      return storedStateChangeObject;
	    
	      }
	     public override StateChange updateTurn(){
              base.updateTurn();
		      byteTime = 0;
		      if(isTypeRevealed){
			      return setByteTarget();
		      }
		      return null;  
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
                          setCenterX((float)(((targetEnemyPosition.X - animPosStart.X) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)bytePos) - ((Math.PI / 4f) * (float)byteTime)) / ((float)byteTime / 4f))) + animPosStart.X));
                          setCenterY((float)(((targetEnemyPosition.Y - animPosStart.Y) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)bytePos) - ((Math.PI / 4f) * (float)byteTime)) / ((float)byteTime / 4f))) + animPosStart.Y));
				      }
				      //Reached Enemy
				      //From TargetEnemy to EndPos in (animTime/2)
				      else if(bytePos == (byteTime/2)){
                          setCenterX((float)(((targetEnemyPosition.X - animPosEnd.X) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)bytePos) - ((Math.PI / 4f) * (float)byteTime)) / ((float)byteTime / 4f))) + animPosEnd.X));
                          setCenterY((float)(((targetEnemyPosition.Y - animPosEnd.Y) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)bytePos) - ((Math.PI / 4f) * (float)byteTime)) / ((float)byteTime / 4f))) + animPosEnd.Y));
					      ByteTarget(byteTarget);
				      }
				      //From TargetEnemy to EndPos in (animTime/2)
				      else{
                          setCenterX((float)(((targetEnemyPosition.X - animPosEnd.X) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)bytePos) - ((Math.PI / 4f) * (float)byteTime)) / ((float)byteTime / 4f))) + animPosEnd.X));
                          setCenterY((float)(((targetEnemyPosition.Y - animPosEnd.Y) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)bytePos) - ((Math.PI / 4f) * (float)byteTime)) / ((float)byteTime / 4f))) + animPosEnd.Y));
				      }
				      bytePos++;
			      }
			      //Biting Enemy during normal Turn
			      else if(byteTime <= 0){
				      //Moving Two Enemy
				      if(animPos < (animTime/2)){
                          setCenterX((float)(((targetEnemyPosition.X - animPosStart.X) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)animPos) - ((Math.PI / 4f) * (float)animTime)) / ((float)animTime / 4f))) + animPosStart.X));
                          setCenterY((float)(((targetEnemyPosition.Y - animPosStart.Y) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)animPos) - ((Math.PI / 4f) * (float)animTime)) / ((float)animTime / 4f))) + animPosStart.Y));
				      }
				      //Reached Enemy
				      //From TargetEnemy to EndPos in (animTime/2)
				      else if(animPos == (animTime/2)){
                          setCenterX((float)(((targetEnemyPosition.X - animPosEnd.X) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)animPos) - ((Math.PI / 4f) * (float)animTime)) / ((float)animTime / 4f))) + animPosEnd.X));
                          setCenterY((float)(((targetEnemyPosition.Y - animPosEnd.Y) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)animPos) - ((Math.PI / 4f) * (float)animTime)) / ((float)animTime / 4f))) + animPosEnd.Y));
					      ByteTarget(byteTarget);	
				      }
				      //From TargetEnemy to EndPos in (animTime/2)
				      else{
                          setCenterX((float)(((targetEnemyPosition.X - animPosEnd.X) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)animPos) - ((Math.PI / 4f) * (float)animTime)) / ((float)animTime / 4f))) + animPosEnd.X));
                          setCenterY((float)(((targetEnemyPosition.Y - animPosEnd.Y) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)animPos) - ((Math.PI / 4f) * (float)animTime)) / ((float)animTime / 4f))) + animPosEnd.Y));
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
			      //initializing targtableGhosts
			      if(targtableGhosts == null){
				      targtableGhosts = new List<BaseEnemy>();
			      }
			      targtableGhosts.Clear();
			      if(targtableGhosts.Capacity < hostSet.getNumberOfCollumns(currentRow)){
				      targtableGhosts.Capacity = hostSet.getNumberOfCollumns(currentRow);
			      }
			      //Finding Ghosts
			      BaseEnemy target = null;
			      for(int loop = 0; loop < hostSet.getNumberOfCollumns(currentRow); loop++){
				      target = hostSet.getEnemyAt(currentRow, loop);
				      if(target != null && target.isTypeRevealed && (target.type == InteractableObject.ObjectType.ANGRY || target.type == InteractableObject.ObjectType.GHOST)){
					      targtableGhosts.Add(target);
				      }
			      }
			      //Choosing Ghost to byte
			      if(targtableGhosts.Count == 0){
				      targetEnemyCollumn = -1;
				      byteTarget = null;
			      }
			      //single target
			      else if(targtableGhosts.Count == 1){
				      byteTarget = targtableGhosts[0];
				      targetEnemyCollumn = byteTarget.currentCollumn;
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
			      //Multiple targets
			      else{
				      byteTarget = targtableGhosts[((int)(rand.NextDouble() * (targtableGhosts.Count)))];
				      targetEnemyCollumn = byteTarget.currentCollumn;
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
		      }
		      return storedStateChangeObject;
	      }
	      private void ByteTarget(BaseEnemy target){
		      if(target != null && hostSet != null){
                  EnemySoundManager.requestSound(EnemySoundManager.SoundType.CAT_ATTACK);
			      if(target.type == InteractableObject.ObjectType.ANGRY){
				      ((Ghost)target).explode();
				      removeThis();
			      }
			      else{
				      target.removeThis();
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