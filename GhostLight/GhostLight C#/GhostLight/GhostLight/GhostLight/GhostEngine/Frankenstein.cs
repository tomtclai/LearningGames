using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;
using MenueSystem;
/**
 *  when revealed, Frankenstein will “clobber” the immediately adjacent characters on each side of himself
 *  and turn them into ghosts. If the character next to him is a ghost it will become an angry ghost after 
 *  getting clobbered, if the character is an angry ghost it will explode and remove one of the player’s 
 *  lives, if the space is blank nothing happens. Frankenstein also feeds on electricity – when revealed, 
 *  Frank will drain one segment of the player’s charge beam to feed his rage. Clobbering magnetized the chain 
 *  and pulls everything to Frnak, removing any spaces in the chain.
 * @author Michael Letter
 */

namespace  GhostFinder.GhostEngine{
    public class Frankenstein : BaseEnemy{
	
	    public static void preLoadResources(ResourceLibrary library){
            if(library.getResource("GhostLight/GhostLight/resources/frankenstein.png") == null){
                Sprite frank = new Sprite("GhostLight/GhostLight/resources/frankenstein.png");
                frank.loadResource();
                library.addResource(frank);
            }
	    }

	    Sprite frankenstien = null;

	    public Frankenstein(ResourceLibrary library) : base(library){
            frankenstien = (Sprite)library.getResource("GhostLight/GhostLight/resources/frankenstein.png");
		    type = InteractableObject.ObjectType.FRANKENSTEIN;
		    isTypeRevealed = false;
		    unrevealType();
		    health.setMaxSegments(InteractableObject.getDefualthealth(type));
		    health.setFilledSegments(health.getMaxSegments());
		    score = InteractableObject.getDefualtScore(type);
		    base.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
	    }
	    protected override void updateImage() {
		    if(isTypeRevealed){
                base.setSprite(frankenstien);
                base.setFrame(0);
		    }
		    else{
			    base.updateImage();
		    }
	    }
	    public override StateChange revealType(int pauseTime){
		    if(!isTypeRevealed){
                EnemySoundManager.requestSound(EnemySoundManager.SoundType.FRANKENSTIEN_INTRO);
			    base.revealType(pauseTime);
			    storedStateChangeObject.setToDefualts();
	    	    clobber(currentRow, currentCollumn - 1, pauseTime);
	    	    clobber(currentRow, currentCollumn + 1, pauseTime);
		        storedStateChangeObject.changInBatteryCharge = -0.5f;
		    }
		    return storedStateChangeObject;
	    }
	    //any changes to state as a result of clobber are added to storedStateChangeObject
	    private void clobber(int row, int collumn, int animationTime){
		    if(hostSet != null){
			    BaseEnemy target = hostSet.getEnemyAt(row, collumn);
			    if(target != null){
				    if(target.type == InteractableObject.ObjectType.ANGRY){
					    storedStateChangeObject.add(((Ghost)target).explode());
				    }
				    else{
					    if(target.type != InteractableObject.ObjectType.GHOST){
						    target.removeThis();
						    Ghost newGhost = new Ghost(BaseCode.activeLibrary);
                            newGhost.setCenter(target.getCenterX(), target.getCenterY());
						    newGhost.revealType(0);
						    hostSet.addEnemy(newGhost, row, collumn);
						    newGhost.setAnimationTarget(row, collumn);
						    newGhost.revealType(animationTime);
					    }
					    else{
						    target.revealType(animationTime);
					    }
				    }
			    }
		    }
	    }
    }
}
