package GhostLight.GhostEngine;

import Engine.MouseInput;
import Engine.Rectangle;
import Engine.ResourceHandler;
import Engine.Vector2;
import MenueSystem.Button;
import MenueSystem.Panel.PanelState;
/**
 * @author Michael Letter
 */
public class LightButton extends Button{
	
	public static String INACTIVE_BUTTON = "lightButtons/circleBlank.png";
	public static String CONE_IDLE_BUTTON = "lightButtons/Flashlight Cone.png";
	public static String WIDE_IDLE_BUTTON = "lightButtons/Flashlight Wide.png";
	public static String LASER_IDLE_BUTTON = "lightButtons/Flashlight Laser.png";
	
	public static String CONE_SELECTED_BUTTON = "lightButtons/Flashlight Cone_SelectedGlow.png";
	public static String WIDE_SELECTED_BUTTON = "lightButtons/Flashlight Wide_SelectedGlow.png";
	public static String LASER_SELECTED_BUTTON = "lightButtons/Flashlight Laser_SelectedGlow.png";
	
	public static void preLoadResources(ResourceHandler resources){
		
		resources.loadImage(INACTIVE_BUTTON);
		
		resources.loadImage(CONE_IDLE_BUTTON);
		resources.loadImage(WIDE_IDLE_BUTTON);
		resources.loadImage(LASER_IDLE_BUTTON);
		
		resources.loadImage(CONE_SELECTED_BUTTON);
		resources.loadImage(WIDE_SELECTED_BUTTON);
		resources.loadImage(LASER_SELECTED_BUTTON);
		
		resources.loadImage("sparcle/sparcle1.png");
		resources.loadImage("sparcle/sparcle2.png");
		resources.loadImage("sparcle/sparcle3.png");
		resources.loadImage("sparcle/sparcle4.png");
		resources.loadImage("sparcle/sparcle5.png");
	}
	private LightBeam.BeamType type = LightBeam.BeamType.REVEAL;
	private LightBeam localLight = null;
	private int sparcleFrameNumber = 0;
	private static String[] sparcleFrames = {"sparcle/sparcle1.png", "sparcle/sparcle2.png",
								"sparcle/sparcle3.png", "sparcle/sparcle4.png", "sparcle/sparcle5.png"};
	private Rectangle sparcler = new Rectangle();
	
	private boolean sizeChangeOnSelect = true;
	private float idleXSize = -1;
	private float idleYSize = -1;
	private float selcXSize = 10f;
	private float selcYSize = 10f;
	
	public LightButton(){
		super();
		sparcler.removeFromAutoDrawSet();
		sparcler.visible = false;
		type = LightBeam.BeamType.REVEAL;
		setIdleImage(CONE_IDLE_BUTTON);
		setSelectImage(CONE_SELECTED_BUTTON);
		setInactiveImage(INACTIVE_BUTTON);
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
					setIdleImage(CONE_IDLE_BUTTON);
					setSelectImage(CONE_SELECTED_BUTTON);
					setInactiveImage(INACTIVE_BUTTON);
				}
				else if(lightType == LightBeam.BeamType.WIDE){
					type = lightType;
					setIdleImage(WIDE_IDLE_BUTTON);
					setSelectImage(WIDE_SELECTED_BUTTON);
					setInactiveImage(INACTIVE_BUTTON);
				}
				else if(lightType == LightBeam.BeamType.LASER){
					type = lightType;
					setIdleImage(LASER_IDLE_BUTTON);
					setSelectImage(LASER_SELECTED_BUTTON);
					setInactiveImage(INACTIVE_BUTTON);
				}
			}
			updateImage();
		}
	}
	/**
	 * Will automatically update the state of this panel and all contained panels
	 * @param mouse
	 */
	public void autoUpdateState(MouseInput mouse){
		if(localLight != null && super.getState() != PanelState.INACTIVE){
			if(localLight.getType() == type){
				super.setState(PanelState.SELECTED, true);
				if(sizeChangeOnSelect)
				{
					if(idleXSize < 0)
					{
						idleXSize = getBackgroundSize().getX();
						idleYSize = getBackgroundSize().getY();
					}
					getBackgroundSize().set(selcXSize, selcYSize);
				}
			}
			else{
				super.setState(PanelState.IDLE, true);
				if(sizeChangeOnSelect && idleXSize > 0)
				{
					getBackgroundSize().set(idleXSize, idleYSize);
				}
			}
		}
		else{
			super.autoUpdateState(mouse);
		}
		if(sparcler.visible){
			sparcleFrameNumber++;
			if(sparcleFrameNumber >= sparcleFrames.length * 3){
				sparcleFrameNumber = 0;
			}
			setSparclePositionScale(super.getBackgroundCenter(), super.getBackgroundSize());
			sparcler.size.mult(1.3f);
			sparcler.setImage(sparcleFrames[sparcleFrameNumber/3]);
		}
	}

	
	/**
	 * Will set whether or not this buttons sparcle is visible
	 * @param visible
	 */
	public void setSparcleVisiblity(boolean visible){
		sparcler.visible = visible;
		if(visible){
			setSparclePositionScale(super.getBackgroundCenter(), super.getBackgroundSize());
			sparcler.setImage(sparcleFrames[sparcleFrameNumber/3]);
		}
	}
	/**
	 * Will return true if this button is sparcling
	 * @return
	 */
	public boolean isSparcling(){
		return sparcler.visible;
	}
	/**
	 * Will set the Sparcles size and position based on the given vectors
	 * @param center
	 * @param size
	 */
	protected void setSparclePositionScale(Vector2 center, Vector2 size){
		sparcler.center.set(center);
		sparcler.size.set(size);
	}
	/**
	 * Will set this button and all of its subPanels to be drawn automatically
	 */
	public void addToAutoDrawSet(){
		super.addToAutoDrawSet();
		sparcler.addToAutoDrawSet();
	}
	/**
	 * Will set this button and all of its subPanels to not be drawn automatically
	 */
	public void removeFromAutoDrawSet(){
		sparcler.removeFromAutoDrawSet();
		super.removeFromAutoDrawSet();
	}
	/**
	 * Will set this button and all of its subPanels to be not be drawn nor updated automatically
	 */
	public void destroy(){
		sparcler.destroy();
		super.destroy();
	}
	/**
	 * Will Draw this button
	 * Note This function is not called if you use addToAutoDrawSet();
	 */
	public void drawThisPanel(){
		super.drawThisPanel();
		if(sparcler.visible){
			sparcler.draw();
		}
	}
}
