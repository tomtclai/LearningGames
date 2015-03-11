
package GhostLight.GhostEngine;

import Engine.Rectangle;
/**
 * @author Michael Letter
 */
public class Life extends Rectangle{
	
	private static String HEALTH_DRAIN = "life/Health_Drain_r.png";
	private static String HEALTH_FULL = "life/HeartCounter_Full.png";
	private static String HEALTH_EMPTY = "life/HeartCounter_Empty.png";
	
	public boolean Sparcle = false;
	private int sparcleFrameNumber = (int)(Math.random() * sparcleFrames.length);
	private static String[] sparcleFrames = {"sparcle/sparcle1.png", "sparcle/sparcle2.png",
								"sparcle/sparcle3.png", "sparcle/sparcle4.png", "sparcle/sparcle5.png"};
	private AnimationStateHandler animState;
	private static int DRAIN_FRAMES = 30;
	
	public Life() {
		size.set(1.5f, 3.0f); 
		animState = new AnimationStateHandler(this);
		animState.addNewState(HEALTH_FULL, HEALTH_FULL, size);
		animState.addNewState(HEALTH_EMPTY, HEALTH_EMPTY, size);
		animState.addNewState(HEALTH_DRAIN, HEALTH_DRAIN, 65, 140, DRAIN_FRAMES, 0, size);
		
		animState.changeState(HEALTH_FULL);
		super.removeFromAutoDrawSet();
	}
	
	
	public void draw(){
		super.draw();
		animState.update();
		if(Sparcle){
			setImage(sparcleFrames[sparcleFrameNumber/3]);
			super.draw();
			setImage("life.png");
			sparcleFrameNumber++;
			if(sparcleFrameNumber >= sparcleFrames.length * 3){
				sparcleFrameNumber= 0;
			}
		}
	}
	public void lose() 
	{
		animState.shiftTimerState(HEALTH_DRAIN, HEALTH_EMPTY, DRAIN_FRAMES);
	}
	public void regain() 
	{
		animState.changeState(HEALTH_FULL);
		
	}
}
