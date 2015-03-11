using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;

/**
 * Jack-o-Lantern: this is the most common character to appear, it has no special behaviors and includes 
 * random point values of 5, 10, 15 and 20. The Jack-o-Lantern is removed by either the the standard 
 * flashlight beam or the charge beam as shown in figures 9-12:
 * @author Michael Letter
 */
namespace GhostFinder.GhostEngine{
    public class Pumpkin : BaseEnemy{
	
	    public static void preLoadResources(ResourceLibrary library){
            if(library.getResource("GhostLight/GhostLight/resources/pumpkin.png") == null){
                Sprite pumpkin = new Sprite("GhostLight/GhostLight/resources/pumpkin.png");
                pumpkin.loadResource();
                library.addResource(pumpkin);
            }
	    }
        private Sprite pumpkin = null;

      public Pumpkin(ResourceLibrary library) : base(library){
          pumpkin = (Sprite)library.getResource("GhostLight/GhostLight/resources/pumpkin.png");
        type = InteractableObject.ObjectType.PUMPKIN;
        base.isHelpfull = true;
        unrevealType();
        health.setMaxSegments(InteractableObject.getDefualthealth(type));
	    health.setFilledSegments(health.getMaxSegments());
        score = InteractableObject.getDefualtScore(type);
        base.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
      }

      public override StateChange revealType(int pauseTime){
	      if(isTypeRevealed && getInfectStatus() <= 0) {
		      return removeThis();
	      }
          return base.revealType(pauseTime);
      }
      protected override void updateImage() {
        if(isTypeRevealed){
            base.setSprite(pumpkin);
            base.setFrame(0);
        }
        else{
            base.updateImage();
        }
      }
    }
}
