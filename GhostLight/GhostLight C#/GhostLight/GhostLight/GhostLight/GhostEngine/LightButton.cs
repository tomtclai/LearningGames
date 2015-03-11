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
    public class LightButton : Button{

        private static Sprite sparcle = null;

        private static Sprite circleBlank = null;

        private static Sprite revealIdle = null;
        private static Sprite revealSelected = null;
        private static Sprite revealMouseOver = null;
        private static Sprite revealMouseOverSelected = null;

        private static Sprite wideIdle = null;
        private static Sprite wideSelected = null;
        private static Sprite wideMouseOver = null;
        private static Sprite wideMouseOverSelected = null;

        private static Sprite lazerIdle = null;
        private static Sprite lazerSelected = null;
        private static Sprite lazerMouseOver = null;
        private static Sprite lazerMouseOverSelected = null;

	    public static void preLoadResources(ResourceLibrary library){
            //Sparle
            if(library.getResource("GhostLight/GhostLight/resources/sparcle/sparcle1.png") == null){
                sparcle = new Sprite(new string[] {"GhostLight/GhostLight/resources/sparcle/sparcle1.png", "GhostLight/GhostLight/resources/sparcle/sparcle2.png", "GhostLight/GhostLight/resources/sparcle/sparcle3.png", "GhostLight/GhostLight/resources/sparcle/sparcle4.png", "GhostLight/GhostLight/resources/sparcle/sparcle5.png"});
                sparcle.loadResource();
                library.addResource(sparcle);
            }
            else{
                sparcle = (Sprite)library.getResource("GhostLight/GhostLight/resources/sparcle/sparcle1.png");
            }
            //Circle Blank
		    if(library.getResource("GhostLight/GhostLight/resources/lightButtons/circleBlank.png") == null){
                circleBlank = new Sprite("GhostLight/GhostLight/resources/lightButtons/circleBlank.png");
                circleBlank.loadResource();
                library.addResource(circleBlank);
            }
            else{
                circleBlank = (Sprite)library.getResource("GhostLight/GhostLight/resources/lightButtons/circleBlank.png");
            }
		    //Reveal
            if(library.getResource("GhostLight/GhostLight/resources/lightButtons/RevealIdle.png") == null){
                revealIdle = new Sprite("GhostLight/GhostLight/resources/lightButtons/RevealIdle.png");
                revealIdle.loadResource();
                library.addResource(revealIdle);
            }
            else{
                revealIdle = (Sprite)library.getResource("GhostLight/GhostLight/resources/lightButtons/RevealIdle.png");
            }
            if(library.getResource("GhostLight/GhostLight/resources/lightButtons/RevealSelected.png") == null){
                revealSelected = new Sprite("GhostLight/GhostLight/resources/lightButtons/RevealSelected.png");
                revealSelected.loadResource();
                library.addResource(revealSelected);
            }
            else{
                revealSelected = (Sprite)library.getResource("GhostLight/GhostLight/resources/lightButtons/RevealSelected.png");
            }
		    if(library.getResource("GhostLight/GhostLight/resources/lightButtons/RevealMouseOver.png") == null){
                revealMouseOver = new Sprite("GhostLight/GhostLight/resources/lightButtons/RevealMouseOver.png");
                revealMouseOver.loadResource();
                library.addResource(revealMouseOver);
            }
            else{
                revealMouseOver = (Sprite)library.getResource("GhostLight/GhostLight/resources/lightButtons/RevealMouseOver.png");
            }
		    if(library.getResource("GhostLight/GhostLight/resources/lightButtons/RevealMouseOverSelected.png") == null){
                revealMouseOverSelected = new Sprite("GhostLight/GhostLight/resources/lightButtons/RevealMouseOverSelected.png");
                revealMouseOverSelected.loadResource();
                library.addResource(revealMouseOverSelected);
            }
            else{
                revealMouseOverSelected = (Sprite)library.getResource("GhostLight/GhostLight/resources/lightButtons/RevealMouseOverSelected.png");
            }
		    
            //Wide
            if(library.getResource("GhostLight/GhostLight/resources/lightButtons/WideIdle.png") == null){
                wideIdle = new Sprite("GhostLight/GhostLight/resources/lightButtons/WideIdle.png");
                wideIdle.loadResource();
                library.addResource(wideIdle);
            }
            else{
                wideIdle = (Sprite)library.getResource("GhostLight/GhostLight/resources/lightButtons/WideIdle.png");
            }
            if(library.getResource("GhostLight/GhostLight/resources/lightButtons/WideSelected.png") == null){
                wideSelected = new Sprite("GhostLight/GhostLight/resources/lightButtons/WideSelected.png");
                wideSelected.loadResource();
                library.addResource(wideSelected);
            }
            else{
                wideSelected = (Sprite)library.getResource("GhostLight/GhostLight/resources/lightButtons/WideSelected.png");
            }
		    if(library.getResource("GhostLight/GhostLight/resources/lightButtons/WideMouseOver.png") == null){
                wideMouseOver = new Sprite("GhostLight/GhostLight/resources/lightButtons/WideMouseOver.png");
                wideMouseOver.loadResource();
                library.addResource(wideMouseOver);
            }
            else{
                wideMouseOver = (Sprite)library.getResource("GhostLight/GhostLight/resources/lightButtons/WideMouseOver.png");
            }
		    if(library.getResource("GhostLight/GhostLight/resources/lightButtons/WideMouseOverSelected.png") == null){
                wideMouseOverSelected = new Sprite("GhostLight/GhostLight/resources/lightButtons/WideMouseOverSelected.png");
                wideMouseOverSelected.loadResource();
                library.addResource(wideMouseOverSelected);
            }
            else{
                wideMouseOverSelected = (Sprite)library.getResource("GhostLight/GhostLight/resources/lightButtons/WideMouseOverSelected.png");
            }
		    
            //Lazer
            if(library.getResource("GhostLight/GhostLight/resources/lightButtons/LaserIdle.png") == null){
                lazerIdle = new Sprite("GhostLight/GhostLight/resources/lightButtons/LaserIdle.png");
                lazerIdle.loadResource();
                library.addResource(lazerIdle);
            }
            else{
                lazerIdle = (Sprite)library.getResource("GhostLight/GhostLight/resources/lightButtons/LaserIdle.png");
            }
            if(library.getResource("GhostLight/GhostLight/resources/lightButtons/LaserSelected.png") == null){
                lazerSelected = new Sprite("GhostLight/GhostLight/resources/lightButtons/LaserSelected.png");
                lazerSelected.loadResource();
                library.addResource(lazerSelected);
            }
            else{
                lazerSelected = (Sprite)library.getResource("GhostLight/GhostLight/resources/lightButtons/LaserSelected.png");
            }
		    if(library.getResource("GhostLight/GhostLight/resources/lightButtons/LaserMouseOver.png") == null){
                lazerMouseOver = new Sprite("GhostLight/GhostLight/resources/lightButtons/LaserMouseOver.png");
                lazerMouseOver.loadResource();
                library.addResource(lazerMouseOver);
            }
            else{
                lazerMouseOver = (Sprite)library.getResource("GhostLight/GhostLight/resources/lightButtons/LaserMouseOver.png");
            }
		    if(library.getResource("GhostLight/GhostLight/resources/lightButtons/LaserMouseOverSelected.png") == null){
                lazerMouseOverSelected = new Sprite("GhostLight/GhostLight/resources/lightButtons/LaserMouseOverSelected.png");
                lazerMouseOverSelected.loadResource();
                library.addResource(lazerMouseOverSelected);
            }
            else{
                lazerMouseOverSelected = (Sprite)library.getResource("GhostLight/GhostLight/resources/lightButtons/LaserMouseOverSelected.png");
            }
	    }

	    private LightBeam.BeamType type = LightBeam.BeamType.REVEAL;
	    private LightBeam localLight = null;
        private DrawRectangle sparcler = new DrawRectangle();
	
	
	    public LightButton(): base(){
		    sparcler.visible = false;
		    type = LightBeam.BeamType.REVEAL;
            base.getBackGround().rectangleVisible = false;

            sparcler.setSprite(sparcle);
            sparcler.rectangleVisible = false;


            setIdleImage(revealIdle);
            setSelectImage(revealSelected);
            setInactiveImage(circleBlank);
		    setMouseOverIdleImage(revealMouseOver);
		    setMouseOverSelectImage(revealMouseOverSelected);
	    }
	    /**
	     * Will set the lightBam this light will moniter and what  BeamType this button will respond too
	     */
	    public void setResponsibility(LightBeam light, LightBeam.BeamType lightType){
		    if(light != null){
			    localLight = light;
			    selectable = true;
			    if(lightType != type){
				    if(lightType == LightBeam.BeamType.REVEAL){
					    type = lightType;
                        setIdleImage(revealIdle);
                        setSelectImage(revealSelected);
                        setInactiveImage(circleBlank);
                        setMouseOverIdleImage(revealMouseOver);
                        setMouseOverSelectImage(revealMouseOverSelected);
				    }
				    else if(lightType == LightBeam.BeamType.WIDE){
					    type = lightType;
					    setIdleImage(wideIdle);
					    setSelectImage(wideSelected);
                        setInactiveImage(circleBlank);
					    setMouseOverIdleImage(wideMouseOver);
                        setMouseOverSelectImage(wideMouseOverSelected);
				    }
				    else if(lightType == LightBeam.BeamType.LASER){
					    type = lightType;
					    setIdleImage(lazerIdle);
					    setSelectImage(lazerSelected);
                        setInactiveImage(circleBlank);
                        setMouseOverIdleImage(lazerMouseOver);
					    setMouseOverSelectImage(lazerMouseOverSelected);
				    }
			    }
			    updateImage();
		    }
	    }
	    /**
	     * Will automatically update the state of this panel and all contained panels
	     * @param mouse
	     */
	    public override void autoUpdateState(Mouse mouse){
		    if(localLight != null && base.getState() != PanelState.INACTIVE){
			    if(localLight.getType() == type){
                    base.setState(PanelState.SELECTED, true);
			    }
			    else{
                    base.setState(PanelState.IDLE, true);
			    }
		    }
		    else{
                base.autoUpdateState(mouse);
		    }
		    if(sparcler.visible){
                sparcler.setFrame((sparcler.getFrame() + 1) % sparcler.getSprite().getTotalFrames());
		    }
	    }
	    /**
	     * Will set whether or not this buttons sparcle is visible
	     * @param visible
	     */
	    public void setSparcleVisiblity(bool visible){
		    sparcler.visible = visible;
		    if(visible){
                setSparclePositionScale();
		    }
	    }
	    /**
	     * Will return true if this button is sparcling
	     * @return
	     */
	    public bool isSparcling(){
		    return sparcler.visible;
	    }
	    /**
	     * Will set the Sparcles size and position based on the given vectors
	     * @param center
	     * @param size
	     */
	    protected void setSparclePositionScale(){
            sparcler.setCenter(base.getBackGround().getCenterX(), base.getBackGround().getCenterY());
            sparcler.setSize(base.getBackGround().getWidth(), base.getBackGround().getHeight());
	    }
	    /**
	     * Will Draw this button
	     * Note This function is not called if you use addToAutoDrawSet();
	     */
	    public override void drawThisPanel(Camera drawLocation){
		    base.drawThisPanel(drawLocation);
		    if(sparcler.visible){
			    sparcler.paint(drawLocation);
		    }
	    }
    }
}
