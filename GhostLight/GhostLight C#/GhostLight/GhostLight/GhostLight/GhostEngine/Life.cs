using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;

/**
 * @author Michael Letter
 */
namespace GhostFinder.GhostEngine{
    public class Life : DrawRectangle{

        private static Sprite sparcle = null;
        private static Sprite heart = null;

        public static void preLoadResources(ResourceLibrary library){
            if(library.getResource("GhostLight/GhostLight/resources/sparcle/sparcle1.png") == null){
                sparcle = new Sprite(new string[] {"GhostLight/GhostLight/resources/sparcle/sparcle1.png", "GhostLight/GhostLight/resources/sparcle/sparcle2.png", "GhostLight/GhostLight/resources/sparcle/sparcle3.png", "GhostLight/GhostLight/resources/sparcle/sparcle4.png", "GhostLight/GhostLight/resources/sparcle/sparcle5.png"});
                sparcle.loadResource();
                library.addResource(sparcle);
            }
            else{
                sparcle = (Sprite)library.getResource("GhostLight/GhostLight/resources/sparcle/sparcle1.png");
            }
            if(library.getResource("GhostLight/GhostLight/resources/life.png") == null){
                heart = new Sprite("GhostLight/GhostLight/resources/life.png");
                heart.loadResource();
                library.addResource(heart);
            }
            else{
                heart = (Sprite)library.getResource("GhostLight/GhostLight/resources/life.png");
            }
        }

	    public bool Sparcle = false;
        int sparcleFrame = 0;
	
	    public Life() {
		    base.setSize(3.0f, 3.0f);
            base.rectangleVisible = false;
            base.setSprite(heart);
	    }
        protected override void movedInDrawSet() {
            base.movedInDrawSet();

        }
	    public override void paint(Camera drawLocation){
		    base.paint(drawLocation);
		    if(Sparcle){
                base.setSprite(sparcle);
                sparcleFrame = (sparcleFrame + 1)%sparcle.getTotalFrames();
                base.setFrame(1);
                base.paint(drawLocation);
                base.setSprite(heart);
		    }
	    }
    }
}
