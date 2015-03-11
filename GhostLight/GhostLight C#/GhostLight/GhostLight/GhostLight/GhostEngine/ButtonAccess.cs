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
    public class ButtonAccess {
	    internal Mouse mouse = null;
        internal LightButton LaserButton = null;
        internal LightButton MediumButton = null;
        internal LightButton WideButton = null;
	
	    protected bool isLaserDown(){
		    if(mouse != null && LaserButton != null &&
                    (mouse.leftMouseDown || mouse.rightMouseDown || mouse.middleMouseDown) &&
				    LaserButton.isPointOver(mouse.getWorldPosition().X, mouse.getWorldPosition().Y)){
			    return true;
			
		    }
		    return false;
	    }
	    protected void setLaserActivity(bool active){
		    if(LaserButton != null){
			    if(!active){
				    LaserButton.setState(Panel.PanelState.INACTIVE, false);
			    }
			    else if(LaserButton.getState() == Panel.PanelState.INACTIVE){
				    LaserButton.setState(Panel.PanelState.IDLE, false);
			    }
		    }
	    }
	    protected bool getLaserActivity(){
		    if(LaserButton != null){
			    if(LaserButton.getState() != Panel.PanelState.INACTIVE){
				    return true;
			    }
		    }
		    return false;
	    }
	    protected void setLaserHighLight(bool highlighted){
		    if(LaserButton != null){
			    LaserButton.setSparcleVisiblity(highlighted);
		    }
	    }
	    protected bool isLaserHighlighted(){
		    if(LaserButton != null){
			    LaserButton.isSparcling();
		    }
		    return false;
	    }
	    protected bool isMediumDown(){
		    if(mouse != null && MediumButton != null &&
                    (mouse.leftMouseDown || mouse.rightMouseDown || mouse.middleMouseDown) &&
				    MediumButton.isPointOver(mouse.getWorldPosition().X, mouse.getWorldPosition().Y)){
			    return true;
			
		    }
		    return false;
	    }
	    protected void setRevealActivity(bool active){
		    if(MediumButton != null){
			    if(!active){
                    MediumButton.setState(Panel.PanelState.INACTIVE, false);
			    }
                else if (MediumButton.getState() == Panel.PanelState.INACTIVE)
                {
                    MediumButton.setState(Panel.PanelState.IDLE, false);
			    }
		    }
	    }
	    protected bool getRevealActivity(){
		    if(MediumButton != null){
			    if(MediumButton.getState() != Panel.PanelState.INACTIVE){
				    return true;
			    }
		    }
		    return false;
	    }
	    protected void setMediumHighLight(bool highlighted){
		    if(MediumButton != null){
			    MediumButton.setSparcleVisiblity(highlighted);
		    }
	    }
	    protected bool isMediumHighlighted(){
		    if(MediumButton != null){
			    MediumButton.isSparcling();
		    }
		    return false;
	    }
	    //Wide
	    protected bool isWideDown(){
		    if(mouse != null && WideButton != null &&
                    (mouse.leftMouseDown || mouse.rightMouseDown || mouse.middleMouseDown) &&
                    WideButton.isPointOver(mouse.getWorldPosition().X, mouse.getWorldPosition().Y))
                {
			    return true;
			
		    }
		    return false;
	    }
	    protected void setWideActivity(bool active){
		    if(WideButton != null){
			    if(!active){
				    WideButton.setState(Panel.PanelState.INACTIVE, false);
			    }
			    else if(WideButton.getState() == Panel.PanelState.INACTIVE){
                    WideButton.setState(Panel.PanelState.IDLE, false);
			    }
		    }
	    }
	    protected bool getWideActivity(){
		    if(WideButton != null){
			    if(WideButton.getState() != Panel.PanelState.INACTIVE){
				    return true;
			    }
		    }
		    return false;
	    }
	    protected void setWideHighLight(bool highlighted){
		    if(WideButton != null){
			    WideButton.setSparcleVisiblity(highlighted);
		    }
	    }
	    protected bool isWideHighlighted(){
		    if(WideButton != null){
			    WideButton.isSparcling();
		    }
		    return false;
	    }
    }
}
