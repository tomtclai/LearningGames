using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;
using MenueSystem;


/**
 * when revealed, the mummy turns everything in the current row back to the unraveled 
 * state  except for itself.
 * @author Michael Letter
 */
namespace GhostFinder.GhostEngine{
    public class Mummy : BaseEnemy{
	
	 public static void preLoadResources(ResourceLibrary library){
            if(library.getResource("GhostLight/GhostLight/resources/mummy.png") == null){
                Sprite mum = new Sprite("GhostLight/GhostLight/resources/mummy.png");
                mum.loadResource();
                library.addResource(mum);
            }
	    }
     private Sprite mummy = null;
      public Mummy(ResourceLibrary library) : base(library){
          mummy = (Sprite)library.getResource("GhostLight/GhostLight/resources/mummy.png");

	      type = InteractableObject.ObjectType.MUMMY;
	      unrevealType();
	      base.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
	      health.setMaxSegments(InteractableObject.getDefualthealth(type));
		    health.setFilledSegments(health.getMaxSegments());
	        score = InteractableObject.getDefualtScore(type);
      }
      protected override void updateImage() {
        if(isTypeRevealed) {
            base.setSprite(mummy);
            base.setFrame(0);
        }
        else{
            base.updateImage();
        }
      }
      public override StateChange revealType(int pauseTime){
	      if(!isTypeRevealed){
              EnemySoundManager.requestSound(EnemySoundManager.SoundType.MUMMY_INTRO);
		      base.revealType(pauseTime);
		      if(hostSet != null){
			      for(int loop = 0; loop < hostSet.getNumberOfCollumns(currentRow); loop++){
				      BaseEnemy target = hostSet.getEnemyAt(currentRow, loop);
				      if(target != null && target != this){
					      target.unrevealType();
				      }
			      }
		      }
	      }
        return storedStateChangeObject;
      }
    }
}
