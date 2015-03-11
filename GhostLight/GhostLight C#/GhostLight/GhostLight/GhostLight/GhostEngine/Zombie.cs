using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;
using MenueSystem;

/**
 * Zombie: when revealed, the Zombie “infects” the two adjacent spaces on each side of his character 
 * for 3 turns, making anything in those spaces invulnerable to any beam except the charge beam. The 
 * Zombie is worth 40 points.
 *
 * @author Michael Letter
 */
namespace GhostFinder.GhostEngine{
    public class Zombie : BaseEnemy{
	    public static void preLoadResources(ResourceLibrary library){
            if(library.getResource("GhostLight/GhostLight/resources/zombie.png") == null){
                Sprite zombie = new Sprite("GhostLight/GhostLight/resources/zombie.png");
                zombie.loadResource();
                library.addResource(zombie);
            }
	    }
        private Sprite zombie = null;

      public Zombie(ResourceLibrary library) : base(library){
          zombie = (Sprite)library.getResource("GhostLight/GhostLight/resources/zombie.png");
	      type = InteractableObject.ObjectType.ZOMBIE;
	      unrevealType();
	      health.setMaxSegments(InteractableObject.getDefualthealth(type));
	      health.setFilledSegments(health.getMaxSegments());
	      score = InteractableObject.getDefualtScore(type);
	      base.setInvulnerablility(InteractableObject.getDefualtInvulnerability(type));
      }

      protected override void updateImage() {
        if(isTypeRevealed) {
            base.setSprite(zombie);
            base.setFrame(0);
        }
        else{
            base.updateImage();
        }
      }
      public override StateChange revealType(int pauseTime){
	      base.revealType(pauseTime);
	      if(hostSet != null){
              EnemySoundManager.requestSound(EnemySoundManager.SoundType.ZOMBIE_INTRO);
		      infect(currentRow, currentCollumn + 2, 3);
		      infect(currentRow, currentCollumn + 1, 3);
		      infect(currentRow, currentCollumn - 1, 3);
		      infect(currentRow, currentCollumn - 2, 3);
	      }
	      return null;
      }
      /**
       * Will Infect target Enemy for the give timeInfected
       */
      public void infect(int row, int collumn, int timeInfected){
	      if(hostSet != null){
		      BaseEnemy target = hostSet.getEnemyAt(row, collumn);
		      if(target != null){
			      target.setInfectStatus(timeInfected);
		      }
	      }
      }
    }
}