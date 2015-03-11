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
    public abstract class BaseEnemy : DrawRectangle{

	    public static void preLoadResources(ResourceLibrary library){

            if(library.getResource("GhostLight/GhostLight/resources/unrevealed.png") == null){
                Sprite unrevealedIcons = new Sprite(new string[] {"GhostLight/GhostLight/resources/unrevealed.png",
                                                                  "GhostLight/GhostLight/resources/unrevealed-friendly.png",
                                                                  "GhostLight/GhostLight/resources/unrevealed-unfriendly.png"});
                unrevealedIcons.loadResource();
                library.addResource(unrevealedIcons);
            };
            if(library.getResource("GhostLight/GhostLight/resources/glow.png") == null){
                Sprite glow = new Sprite("GhostLight/GhostLight/resources/glow.png");
                glow.loadResource();
                library.addResource(glow);
            }
		    if(library.getResource("GhostLight/GhostLight/resources/infected.png") == null){
                Sprite infect = new Sprite("GhostLight/GhostLight/resources/infected.png");
                infect.loadResource();
                library.addResource(infect);
            }
		    if(library.getResource("GhostLight/GhostLight/resources/sparcle/sparcle1.png") == null){
                Sprite sparcle = new Sprite(new string[] {"GhostLight/GhostLight/resources/sparcle/sparcle1.png", "GhostLight/GhostLight/resources/sparcle/sparcle2.png", "GhostLight/GhostLight/resources/sparcle/sparcle3.png", "GhostLight/GhostLight/resources/sparcle/sparcle4.png", "GhostLight/GhostLight/resources/sparcle/sparcle5.png"});
                sparcle.loadResource();
                library.addResource(sparcle);

            }
		    if(library.getResource(LoadableFont.createFontID("Comic Sans MS", 1.5f, FontStyle.Italic)) == null){
                LoadableFont scoreFont = new LoadableFont("Comic Sans MS", 1.5f, FontStyle.Italic);
                scoreFont.loadResource();
                library.addResource(scoreFont);
            }
	    }
	
      public enum HiddenColor{
        WHITE, GREEN, RED
      };
      
  
      internal InteractableObject.ObjectType type = InteractableObject.ObjectType.PUMPKIN;
      internal bool isTypeRevealed = false;
      public bool setToReveal = false;
      internal bool isPartialRevealed = false;
      internal bool isHelpfull = false;
      protected bool isFalling = false;
      protected int score = 0;

      private Sprite unrevealedSprite = null;

      public EnemySubSetNode hostNode { get; internal set; }	//Stores the node that stores this Enemy if it is an EnemySubSet
      public EnemySet hostSet {get; internal set;}	            //The current Manager of currentEnemyRow, and currentEnemyGrid
        
      public int currentRow = 0;						//The Row which this Enemy Resides	
      public int currentCollumn = 0;					//THe Column which this enemy Resides
  
      protected PointF animPosStart = new PointF();	//The Position this animation is starting from
      protected PointF animPosEnd = new PointF();		//The Position this animation is Ending at 
      protected int animPos = 0;						//THe Current animation Position: Note animPos = 0 is a marker to move enemy to its target position
      protected int animTime = -1;						//The Number updates this animation has to travel from animPosStart to animPosEnd a -1 indicates that the time is unknown 
  
      protected StateChange storedStateChangeObject = new StateChange();//Stored to prevent extra new(s) durring each update
  
      protected int animDurationTotal = 20;
      protected int animDurationCurrent = -1;
  
      internal InteractableObject representative = null;

      protected HiddenColor hiddenColor = HiddenColor.WHITE;

      protected SegmentedBar health = new SegmentedBar();
      /** If TRUE this object will remove itself when (animPos == animTime)*/
      protected bool removeAfterAnimation = false;
  
      protected static EnemySubSet currentTargets = new EnemySubSet();
  
      private Text displayedScore = null; 
      private DrawRectangle selected = null;
      private DrawRectangle infectCloud = null;
  
      private DrawRectangle highLight = null;
  
      private int infectedTimer = 0;					//The Number of Turns the current this Enemy will Remain infected -1 is a marker that the enemy is permanently infected
      private SegmentedBar infectBar =  new SegmentedBar();
      private bool invulnerable = false;
      
  
      public BaseEnemy(ResourceLibrary library) : base() {
          hostSet = null;
          hostNode = null;

        base.setCenterY(-5);
        base.setSize(5f,5f);
        base.rectangleVisible = false;
        base.spriteVisible = true;
        base.visible = true;

        unrevealedSprite = (Sprite)BaseCode.activeLibrary.getResource("GhostLight/GhostLight/resources/unrevealed.png");

        selected = new DrawRectangle();
        selected.visible = false;
        selected.setSprite((Sprite)BaseCode.activeLibrary.getResource("GhostLight/GhostLight/resources/glow.png"));
        selected.rectangleVisible = false;
        updateSelected();
    
        highLight = new DrawRectangle();
        highLight.visible = true;
        highLight.setSprite((Sprite)BaseCode.activeLibrary.getResource("GhostLight/GhostLight/resources/sparcle/sparcle1.png"));
        highLight.rectangleVisible = false;
        updateHighLight();
        highLight.visible = false;
    
        displayedScore = new Text();
        displayedScore.targetFont = (LoadableFont)BaseCode.activeLibrary.getResource(LoadableFont.createFontID("Comic Sans MS", 1.5f, FontStyle.Italic));
        displayedScore.textColor = Color.White;
        displayedScore.visible = false;
        updateDisplayedScore();
    
        health.setMaxSegments(2);
	    health.setMaxSegments(health.getMaxSegments());
	    health.visible = false;
        health.setWidth(base.getWidth());
        health.setHeight(1.4f);
        health.setColor(Color.LightGreen);
    
        infectBar.setColor(Color.FromArgb(10,80,40));
        infectBar.visible = false;  
    
        infectCloud = new DrawRectangle();
        infectCloud.setSprite((Sprite)library.getResource("GhostLight/GhostLight/resources/infected.png"));
        infectCloud.rectangleVisible = false;
        infectCloud.visible = false;

        base.setPriority(2);
        updateImage();
      }
      /**
       * Updates the size and position of selected
       */
      private void updateSelected(){
          selected.setCenter(base.getCenterX(), base.getCenterY());
          selected.setSize(base.getWidth() * 1.3f, base.getHeight() * 1.3f);
      }
      /**
       * 
       */
      private void updateHighLight(){
	      if(highLight.visible){
              highLight.setCenter(base.getCenterX(), base.getCenterY());
              highLight.setSize(base.getWidth() * 2, base.getHeight() * 2);
              highLight.setFrame((highLight.getFrame() + 1) % highLight.getSprite().getTotalFrames());
	      }
      }
      /**
       * Will mark this Enemy as Selected
       */
      public void select(){
	      selected.visible = true;
      }
      /**
       * Will mark this enemy as Not selected
       */
      public void deselect(){
	      selected.visible = false;
      }
      public void circleEnemy(bool circled){
	      highLight.visible = circled;
          if (circled) {
              base.setPriority(8);
          }
          else if(isTypeRevealed){
              base.setPriority(3);
          }
          else{
              base.setPriority(2);
          }
      }
      public bool isEnemyCircled(){
	      return highLight.visible;
      }
      /**
       * Returns whether or not this enemy will die if its health falls to zero
       * @return True if this enemy will die if its health falls to zero
       */
      public bool isInvulnerable(){
	      return invulnerable;
      }
      /**
       * will set whether or not this enemy is invulnerable
       * @param makeInvulnerable
       */
      public void setInvulnerablility(bool makeInvulnerable){
	      invulnerable = makeInvulnerable;
	      if(invulnerable){
		      health.visible = false;
	      }
      }
      /**
       * updates the size and position of displayedScore
       */
      private void updateDisplayedScore(){
          displayedScore.textPosition.X = base.getCenterX() + (base.getWidth()/4);
          displayedScore.textPosition.Y = base.getCenterY() - (base.getHeight()*0.8f);
	      displayedScore.text = "" +score ;
      }
      private void updateHealth(){
	      if(health != null){
              health.setCenterX(base.getCenterX());
              health.setCenterY(base.getCenterY() + (base.getHeight()/2));
              health.setSize(base.getWidth(), 0.7f);
	      }
      }
      /**
       * Will Synk this objects representative with the state of this enemy
       * @param turnTime
       */
      internal void updateRepresentative(){
	      //Checking Representative 
	      if(representative != null){
		      //Repositioning if nessesary and able
		      if(hostSet!= null){
			      if(!hostSet.moveRepresentative(representative, currentRow, currentCollumn)){
				      removeThis();
			      }
		      }
	      }
	      //Creating Representative and moving into position
	      else if(hostSet != null){
		      representative = new InteractableObject();
		      if(!hostSet.moveRepresentative(representative, currentRow, currentCollumn)){
			      removeThis();
		      }
	      }
	      //Updating Representative Type reveal status etc..
	      ((Representative)representative).subject = this;
	      ((Representative)representative).setRevealStatus(isTypeRevealed);
	      ((Representative)representative).setPartialRevealStatus(isPartialRevealed);
	      ((Representative)representative).type = type;
	      ((Representative)representative).resetChanges();
	      ((Representative)representative).setScore(score);
	      ((Representative)representative).setMaxHealth(health.getMaxSegments());
	      ((Representative)representative).setCurrentHealth(health.getfilledSegments());
	      ((Representative)representative).setVulnerability(invulnerable);
	      ((Representative)representative).setInfectStatus(infectedTimer);
	      ((Representative)representative).setHighlight(isEnemyCircled());
      }
      /**
       * will set the current position and grid the enemy think  
       * @param row The Row the Enemy thinks it is at
       * @param collumn The Column the Enemy thinks it is at
       * @param EnemyGrid The grid in which this Enemy Resides
       */
      public void setPosition(int row, int collumn, EnemySet EnemyGrid){
	      if(EnemyGrid != null && row >= 0 && collumn >= 0){
		      hostSet = EnemyGrid;
		      currentRow = row;
		      currentCollumn = collumn;
		      if(hostNode != null){
			      hostNode.removeNode();
			      hostNode = null;
		      }
	      }
      }
      /**
       * Will set the Infect Status to the Given int
       * Giving a -1 will make this Enemy Permanently Infected
       */
      public void setInfectStatus(int newInfectStatus){
	      infectedTimer = newInfectStatus;
	      if(infectedTimer > 0){
		      infectCloud.visible = true;
		      infectBar.visible = true;
		      if(infectBar.getMaxSegments() < infectedTimer){
			      infectBar.setMaxSegments(infectedTimer);
			      infectBar.setFilledSegments(infectedTimer);
		      }
		      else{
			      infectBar.setFilledSegments(infectedTimer);
		      }
	      }
	      else{
		      infectCloud.visible = false;
		      infectBar.visible = false;
		      infectBar.setMaxSegments(1);
		      infectedTimer = 0;
	      }
      }
      /**
       * will return the current count on the infect time
       * @return the number of turns until the Enemy becomes uninfected
       * -1 indicates that it will never become uninfected
       */
      public int getInfectStatus(){
	      return infectedTimer;
      }
      /**
       * decrements infectedTimer and updates infectBar
       */
      private void updateInfectedStatus(){
	      //infect Bar
          infectBar.setWidth(base.getWidth());
          infectBar.setHeight(health.getHeight());
          infectBar.setCenterX(base.getCenterX());
          infectBar.setCenterY(health.getCenterY() - health.getHeight());
	  
	      //infectCloud
          infectCloud.setSize(base.getWidth(), base.getHeight());
          infectCloud.setCenter(base.getCenterX(), base.getCenterY());
      }
      public void setHiddenColor(HiddenColor value) {
        hiddenColor = value;
      }

      public bool isAlive() {
        return (visible && !isFalling);
      }

      public bool isPowerRevealed(){
        return isTypeRevealed;
      }
      /**
       * Will reveal whether or not this Enemy is an Enemy or useful power up
       */
      public void partialReveal(){
	      isPartialRevealed = true;
	      updateImage();
      }
      public StateChange revealIfSetToo(int pauseTime){
	      storedStateChangeObject.setToDefualts();
	      if(setToReveal && !isTypeRevealed){
		      setToReveal = false;
		      return revealType(pauseTime);
	      }
	      setToReveal = false;
	      return storedStateChangeObject;
      }
      /**
       * Will reveal this enemy and return the changes to the game state
       * @param pauseTime, the amount of time the game will be paused for the reveal
       * @return 
       */
      public virtual StateChange revealType(int pauseTime){
	    storedStateChangeObject.setToDefualts();
        isTypeRevealed = true;
        isPartialRevealed = true;
        updateImage();
        displayedScore.visible = true;
        base.setPriority(3);
        if(!invulnerable && !(isHelpfull && health.getMaxSegments() <= 1)){
    	    health.visible = true;
        }
        return storedStateChangeObject;
      }
      public virtual void unrevealType(){
	      health.visible = false;
	      isTypeRevealed = false;
	      isPartialRevealed = false;
	      isPartialRevealed = false;
	      displayedScore.visible = false;
          base.setPriority(2);
          updateImage();
      }
      /**
       * Will return the health object used by the enemy
       * @return
       */
      public SegmentedBar getHealth(){
	      return health;
      }
      /**
       * will remove this Enemy From the currentEnemyGrid
       * Note this does Reset any previous changes to the stored storedStateChangeObject
       * before making its own changes
       */
      public virtual StateChange removeThis(){
	      if(hostSet != null){
		      hostSet.removeEnemy(this);
	      }
	      if(hostNode != null){
		      hostNode.removeNode();
	      }
          base.removeFromDrawSet();
	      storedStateChangeObject.setToDefualts();
	      storedStateChangeObject.changeInPlayerScore = score;
	      return storedStateChangeObject;
      }
      /**
       * Trigger this object to escape out of the upper edge of the screen. then remove itself
       * @return
       */
      public virtual StateChange scare(){
	      setAnimationPosition(0);
	      setAnimationTarget(-2, currentCollumn);
          selected.visible = false;
	      health.visible = false;
	      infectBar.visible = false;
	      removeAfterAnimation = true;
          base.setPriority(2);
	      if(hostSet != null){
		      hostSet.moveEnemytoSubSet(this);
	      }
	      storedStateChangeObject.setToDefualts();
	      storedStateChangeObject.changeInPlayerScore = score;
	      return storedStateChangeObject;
      }
      public InteractableObject.ObjectType getType() {
        return type;
      }
      //targeted Enemy Stuff
      public EnemySubSetNode targetEnemy(BaseEnemy target){
	      if(currentTargets != null){
		      return currentTargets.addEnemy(target, false);
	      }
	      return null;
      }
      public bool isEnemyAlreadyTargeted(BaseEnemy target){
	      if(currentTargets != null){
		      if(currentTargets.findEnemy(target) != null){
			      return true;
		      }
	      }
	      return false;
      }
     protected virtual void updateImage() {
         if (!isTypeRevealed)  {
             //Update Sprite
             if (base.getSprite() != unrevealedSprite){
                base.setSprite(unrevealedSprite);
             }
             //Update Frame
             if (isPartialRevealed)  {
                 if (isHelpfull) {
                     base.setFrame(1);
                 }
                 else {
                     base.setFrame(2);
                 }
             }
             else{
                 base.setFrame(0);
            }
         } 
      }
  
      /**
       * Will set this objects position within the animation to the given position
       * @param animationPosition	the Desired Animation position
       */
      public void setAnimationPosition(int newAnimationPosition){
	      animPos = newAnimationPosition;
      }
      //Will move towards this target through out the turn and then move to this position at the End of the turn
      public void setAnimationTarget(int row, int collumn){
	      if(hostSet != null){
              animPosStart.X = base.getCenterX();
              animPosStart.Y = base.getCenterY();
              animPosEnd = hostSet.getWorldPosition(row, collumn);
	      }
      }
      /**
       * Will set the center of this objects position to the appropriate position in the animation and increment animPos
       */
      protected virtual void setCenterToAnimatedPosition(){
	      //t = current Time
	      //T = total time to animate
	      //D = Total Distance to Travel
	      //S = Starting point
	      //P = current Position
	      //P = (D / 2)(1 + Cos((((pi / 2) * t)-(T(pi / 2)))/(T / 2))) + S;
	      if(animTime > 0){
              setCenterX((float)(((animPosEnd.X - animPosStart.X) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)animPos) - ((Math.PI / 2) * (float)animTime)) / ((float)animTime / 2f))) + animPosStart.X));
              setCenterY((float)(((animPosEnd.Y - animPosStart.Y) / 2f) * (1f + Math.Cos((((Math.PI / 2f) * (float)animPos) - ((Math.PI / 2) * (float)animTime)) / ((float)animTime / 2f))) + animPosStart.Y));
	      }
	      else{
              base.setCenter(animPosEnd.X, animPosEnd.Y);
	      }
      }
      /**
       * Will Draw this Enemy 
       */
      public override void paint(Camera drawLocation){
	      if(visible){
              base.paint(drawLocation);
              infectCloud.paint(drawLocation);
              selected.paint(drawLocation);
              highLight.paint(drawLocation);
              displayedScore.paint(drawLocation);
              health.paint(drawLocation);
              infectBar.paint(drawLocation); 
	      }
      }
      /**
       * Get the point value that this enemy contains.
       * 
       * @return - The score of this enemy.
       */
      public int getScore() {
        return score;
      }

      public void setScore(int value){
        score = value;
      }
      /**
       *  Called Whenever a Turn has Passed
       *  Requires the current Enemy Set and this Enemies currentPosition in the Set
       *  @returns any change in payer state (By Default returns null)
       */
      public virtual StateChange updateTurn(){
	      //Decrementing Infect Status
	      if(infectedTimer > 0){
		      this.setInfectStatus(infectedTimer-1);
	      }

	      return null;
      }
      public virtual void update(int animationTimer) {
	      animTime = animationTimer;
	      //animating to position
	      setCenterToAnimatedPosition();
	      //Incrementing anim position
	      if(animPos < animTime){
		      animPos++; 
	      }
	      else if(removeAfterAnimation){
		      removeThis();
	      }
	      deselect();
	      updateSelected();
	      updateHighLight();
	      updateDisplayedScore();
	      updateHealth();
	      updateInfectedStatus();
	      if(hostSet != null && !invulnerable && health.getfilledSegments() <= 0){
		      scare();
	      }
      }
    }
}
