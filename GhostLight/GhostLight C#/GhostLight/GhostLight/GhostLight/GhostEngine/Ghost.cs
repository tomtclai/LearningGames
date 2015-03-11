using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;
using MenueSystem;

/**
 * Ghost: when revealed, the ghost must be removed within 3 turns or it will turn into an angry ghost. The ghost is worth 30 points.
 * 	The ghost can only be removed by the charge beam.
 * Angry Ghost: when a regular ghost becomes an angry ghost, if it is not removed in 5 turns it explodes and takes one of the player’s 
 * 	lives. The angry ghost can only be removed by the charge beam. The angry ghost is worth 50 points.
 * import GhostFinder.Interface.InteractableObject.ObjectType;
 * @author Michael Letter
 */
namespace GhostFinder.GhostEngine{
    public class Ghost : BaseEnemy {
	
	    public static void preLoadResources(ResourceLibrary library){
            if(library.getResource("GhostLight/GhostLight/resources/angry-ghost1.png") == null){
                Sprite angry = new Sprite(new string[] { "GhostLight/GhostLight/resources/angry-ghost1.png",
                                                         "GhostLight/GhostLight/resources/angry-ghost2.png",
                                                         "GhostLight/GhostLight/resources/angry-ghost3.png",
                                                         "GhostLight/GhostLight/resources/angry-ghost4.png",
                                                         "GhostLight/GhostLight/resources/angry-ghost5.png"});
                angry.loadResource();
                library.addResource(angry);
            }
		    if(library.getResource("GhostLight/GhostLight/resources/ghost1.png") == null){
                Sprite complacent = new Sprite(new string[] {"GhostLight/GhostLight/resources/ghost1.png",
                                                             "GhostLight/GhostLight/resources/ghost2.png",
                                                             "GhostLight/GhostLight/resources/ghost3.png"});
                complacent.loadResource();
                library.addResource(complacent);
            }
            if(library.getResource("GhostLight/GhostLight/resources/explode/explode1.png") == null){
                Sprite explode = new Sprite(new string[] {"GhostLight/GhostLight/resources/explode/explode1.png",
                                                          "GhostLight/GhostLight/resources/explode/explode2.png",
                                                          "GhostLight/GhostLight/resources/explode/explode3.png",
                                                          "GhostLight/GhostLight/resources/explode/explode4.png",
                                                          "GhostLight/GhostLight/resources/explode/explode5.png",
                                                          "GhostLight/GhostLight/resources/explode/explode6.png",
                                                          "GhostLight/GhostLight/resources/explode/explode7.png",
                                                          "GhostLight/GhostLight/resources/explode/explode8.png",
                                                          "GhostLight/GhostLight/resources/explode/explode9.png",
                                                          "GhostLight/GhostLight/resources/explode/explode10.png",
                                                          "GhostLight/GhostLight/resources/explode/explode11.png"});
                explode.loadResource();
                library.addResource(explode);
            }
	    }
        private static Random rand = new Random();
	
	    protected bool angry;									    //Marks whether the Ghost is angry
	    private int angryTimer;									//Marks the number of turns until the Ghost transforms from Ghost -> AngryGhost: Transforms when == 0
	    protected bool vibrate = false;
	    protected SegmentedBar explodeTimer = new SegmentedBar();

        private Sprite explodeSprite = null;
        private Sprite angrySprite = null;
        private Sprite complacentSprite = null;

	    //Defialt Constructor
	    //Sets image to new
        public Ghost(ResourceLibrary library) : base(library) {

            explodeSprite = (Sprite)library.getResource("GhostLight/GhostLight/resources/explode/explode1.png");
            angrySprite = (Sprite)library.getResource("GhostLight/GhostLight/resources/angry-ghost1.png");
            complacentSprite = (Sprite)library.getResource("GhostLight/GhostLight/resources/ghost1.png");

		    type = InteractableObject.ObjectType.GHOST;
		    MakeNotAngry();
		    unrevealType();
		    health.setMaxSegments(InteractableObject.getDefualthealth(type));
		    health.setFilledSegments(health.getMaxSegments());
	        score = InteractableObject.getDefualtScore(type);
	        base.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));

            health.setHeight(0.35f);

            explodeTimer.setHeight(0.7f);
	        explodeTimer.visible = false;
            explodeTimer.setCenterX(base.getCenterX());
            explodeTimer.setCenterY(health.getCenterY() - (base.getHeight() / 2) + 0.35f);
		    explodeTimer.setWidth(base.getWidth());
		    explodeTimer.setColor(Color.Yellow);
            explodeTimer.setMaxSegments(angrySprite.getTotalFrames() + complacentSprite.getTotalFrames());
		    explodeTimer.setFilledSegments(explodeTimer.getMaxSegments());
	    }
        protected override void updateImage() {
            if(isTypeRevealed){
                if (explodeTimer.getfilledSegments() == 0 && base.getSprite() != explodeSprite){
                    base.setSprite(explodeSprite);
                    base.setFrame(base.getSprite().getTotalFrames() - angryTimer);
                }
                else if (angry && base.getSprite() != angrySprite){
                    base.setSprite(angrySprite);
                    base.setFrame(base.getSprite().getTotalFrames() - angryTimer);
                }
                else if(base.getSprite() != complacentSprite){
                    base.setSprite(complacentSprite);
                    base.setFrame(base.getSprite().getTotalFrames() - angryTimer);
                }
            }
            else{
                base.updateImage();
            }
        }
	    /**
	     * Will Make this Ghost Angry
	     */
	    public void MakeAngry(){
		    angry = true;
		    type = InteractableObject.ObjectType.ANGRY;
            if(isTypeRevealed){
                base.setSprite(angrySprite);
            }
            base.setFrame(0);
            angryTimer = angrySprite.getTotalFrames();
		    explodeTimer.setFilledSegments(angryTimer);
		    health.setMaxSegments(InteractableObject.getDefualthealth(type));
		    health.setFilledSegments(health.getMaxSegments());
	        score = InteractableObject.getDefualtScore(type);
	        base.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
		    vibrate = false;
	    }
	    /**
	     * Will Make this Ghost Not Angry
	     */
	    public void MakeNotAngry(){
		    angry = false;
		    type = InteractableObject.ObjectType.GHOST;
            if(isTypeRevealed){
                base.setSprite(complacentSprite);
            }
            base.setFrame(0);
            angryTimer = complacentSprite.getTotalFrames();
		    explodeTimer.setFilledSegments(angrySprite.getTotalFrames() + angryTimer);
		    health.setMaxSegments(InteractableObject.getDefualthealth(type));
		    health.setFilledSegments(health.getMaxSegments());
	        score = InteractableObject.getDefualtScore(type);
	        base.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
		    vibrate = false;
	    }
	    /**
	     * Will Make the Ghost Explode and return the changes to the player state
	     * @return any changes to player state as a result of the explotion
	     */
	    public StateChange explode(){
            EnemySoundManager.requestSound(EnemySoundManager.SoundType.GHOST_EXPLODE);
		    if(!isTypeRevealed){
			    revealType(0);
		    }
		    if(!angry){
			    MakeAngry();
		    }
            base.setSprite(explodeSprite);
            explodeTimer.visible = false;
		    angryTimer = (explodeSprite.getTotalFrames() * 2);
		    explodeTimer.setFilledSegments(0);
		    killEnemy(currentRow - 1, currentCollumn + 1);
		    killEnemy(currentRow - 1, currentCollumn - 1);
		    killEnemy(currentRow - 1, currentCollumn);
		    killEnemy(currentRow + 1, currentCollumn + 1);
		    killEnemy(currentRow + 1, currentCollumn - 1);
		    killEnemy(currentRow + 1, currentCollumn);
		    killEnemy(currentRow, currentCollumn + 1);
		    killEnemy(currentRow, currentCollumn - 1);
		
		    if(hostSet != null){
                base.setWidth(hostSet.getCollumnSpacing() * 3);
			    base.setHeight(hostSet.getRowSpacing() * 3);
			    hostSet.moveEnemytoSubSet(this);
		    }
		    else{
                base.setSize(base.getWidth() * 5, base.getHeight() * 5);
		    }
		    vibrate = false;
		    return getExplodeEffects(); 
	    }
	    /**
	     * Will return the effects of this Ghost Exploding if it were to explode
	     */
	    public StateChange getExplodeEffects(){
		    storedStateChangeObject.setToDefualts();
		    storedStateChangeObject.changeInPlayerHealth = -1;
		    return storedStateChangeObject; 
	    }
	    /**
	     * Will set the number of turns this ghost take to evolve
	     * @param target
	     * @return
	     */
	    public bool setAngryTimer(short target){
		    if(target > 0){
			    angryTimer = target;
			    updateAngryness();
			    return true;
		    }
		    return false;
	    }
	    /**
	     * 
	     * @return Returns True if the Ghost is Angry. Returns False if the Ghost is not
	     */
	    public bool isAngry(){
		    return angry;
	    }
	    //Overides updateTurn
	    /**
	     * Call Each time a Turn Concludes 
	     */
	    public override StateChange updateTurn(){
		    base.updateTurn();
		    return updateAngryness();
	    }
	    /**
	     * Overrides normal updated to update explode timer
	     */
	    public override void update(int animationTimer) {
		    base.update(animationTimer);
		    if(isTypeRevealed && explodeTimer.getfilledSegments() == 0){
			    //incrementing animation
			    angryTimer--;
			    //setting animation frame
			    if((angryTimer/2) > 0 && (angryTimer/2) <= explodeSprite.getTotalFrames()){
                    base.setFrame(explodeSprite.getTotalFrames() - (angryTimer / 2));
                    explodeTimer.visible = false;
			    }
			    else{
				    removeThis();
			    }
		    }
            explodeTimer.setWidth(base.getWidth());
            explodeTimer.setHeight(0.7f);
            explodeTimer.setCenterX(base.getCenterX());
            explodeTimer.setCenterY(health.getCenterY() + health.getHeight());
		
	    }
	    public override StateChange revealType(int pauseTime){
            EnemySoundManager.requestSound(EnemySoundManager.SoundType.GHOST_INTRO);
		    explodeTimer.visible = true;
		    return base.revealType(pauseTime);
	    }
	    public override void unrevealType(){
		    if(explodeTimer.getfilledSegments() > 0){
			    explodeTimer.visible = false;
                base.unrevealType();
		    }
	    }
	    public override void paint(Camera drawLocation){
            base.paint(drawLocation);
            explodeTimer.paint(drawLocation);
	    }
	    public override StateChange removeThis(){
		    return base.removeThis();
	    }
	    /**
	     * Called once per turn updates the angry state of the ghost and the image
	     */
	    protected override void setCenterToAnimatedPosition(){
		    base.setCenterToAnimatedPosition();
		    if(isTypeRevealed && vibrate){
                base.setCenterX((float)(base.getCenterX() + rand.NextDouble()));
                base.setCenterY((float)(base.getCenterY() + rand.NextDouble()));
		    }
	    }
	    /**
	     * Will attempt to Remove an enemy at the given location 
	     * Note Does not check if hostSet is set
	     * @param targetRow
	     * @param targetCollumn
	     */
	    private void killEnemy(int targetRow, int targetCollumn){
		    if(hostSet != null){
			    BaseEnemy target = hostSet.getEnemyAt(targetRow, targetCollumn);
			    if(target != null){
				    target.scare();
			    }
		    }
	    }
	    private StateChange updateAngryness(){
		    base.storedStateChangeObject.setToDefualts();
		    if(isTypeRevealed && explodeTimer.getfilledSegments() > 0){
			    angryTimer--; 
			    if(!angry){
				    if(angryTimer == 0){
					    MakeAngry();
				    }
				    else{
					    if(angryTimer > complacentSprite.getTotalFrames()){
                            angryTimer = complacentSprite.getTotalFrames();
					    }
					    explodeTimer.setFilledSegments(angrySprite.getTotalFrames() + angryTimer);
                        base.setSprite(complacentSprite);
                        base.setFrame(complacentSprite.getTotalFrames() - angryTimer);
				    }
			    }
			    else{
				    if(angryTimer == 0){
					    return explode();
				    }
				    else{
					    if(angryTimer > angrySprite.getTotalFrames()){
                            angryTimer = angrySprite.getTotalFrames();
					    }
					    explodeTimer.setFilledSegments(angryTimer);
                        base.setSprite(angrySprite);
                        base.setFrame(angrySprite.getTotalFrames() - angryTimer + 1);
					    if(angryTimer == 1){
						    vibrate = true;
					    }
				    }
			    }
		    }
		    return storedStateChangeObject;
	    }
    }
}
