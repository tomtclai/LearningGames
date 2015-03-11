package SpaceSmasher;

import Engine.BaseCode;
import Engine.GameObject;
import Engine.Vector2;

class Background extends GameObject {
	
	/** Stores the %distance of the total size the Background should Move*/
	public float movePercentage = 0.2f;
	/** Stores the Number Updates this will use to reach the target Position */
	public int animTime = 400;
	
	/** the current Frame in the animation */
	private int animPos = 0;
	/** The Starting position of the Animation */
	private Vector2 animPosStart = new Vector2();
	/** The Ending position of the Animation */
	private Vector2 animPosEnd = new Vector2();
	
	public void initialize(){
		animPosStart.set(getCenter());
		animPosEnd.set(getCenter());
		choseNextEndPosition();
	}
	/**
	 * Will update the backgrounds 
	 */
	public void updatePosition(){
		if(animPos < animTime){
			setCenterToAnimatedPosition();
			animPos++;
		}
		else{
			choseNextEndPosition();
			setCenterToAnimatedPosition();
		}
	}
	/**
	 * Will select a new animPosEnd and set the current animPosStart to the old animPosEnd
	 */
	public void choseNextEndPosition(){
		animPos = 0;
		animPosStart.set(animPosEnd);
		
		//Setting new Center
		double percnent = Math.random();
		float h = getHeight();
		float w = getWidth();
		float halfH = h/2.0f;
		float halfW = w/2.0f;
		//Up
		if(percnent <= 0.25){
			animPosEnd.setY(animPosEnd.getY() + (h * movePercentage));
		}
		//Down
		else if(percnent <= 0.5){
			animPosEnd.setY(animPosEnd.getY() - (h * movePercentage));
		}
		//Left
		else if(percnent <= 0.75){
			animPosEnd.setX(animPosEnd.getX() - (w * movePercentage));
		}
		//Right
		else{
			animPosEnd.setX(animPosEnd.getX() + (w * movePercentage));
		}
		//Reseting Center if Result is Off Screen
		//X
		if(animPosEnd.getX() - halfW > 0){
			animPosEnd.setX(halfW);
		}
		else if(animPosEnd.getX() + (halfW) <= BaseCode.world.getWidth()){
			animPosEnd.setX(BaseCode.world.getWidth() - halfW);
		}
		//Y
		if(animPosEnd.getY() - (halfH) > 0){
			animPosEnd.setY(halfH);
		}
		else if(animPosEnd.getY() + (halfH) <= BaseCode.world.getHeight()){
			animPosEnd.setY(BaseCode.world.getHeight() - halfH);
		}
		//checking of should try for second attempt
		if(animPosStart.getX() == animPosEnd.getX() && animPosStart.getY() == animPosEnd.getY()){
			animPos = animTime;
		}
	}
	/**
	 * Will Set the the center to the current Animated Position
	 */
	protected void setCenterToAnimatedPosition(){
		if(animTime > 0){
			//t = current Time
			//T = total time to animate
			//D = Total Distance to Travel
			//S = Starting point
			//P = current Position
			//P = (D / 2)(1 + Cos((((pi / 2) * t)-(T(pi / 2)))/(T / 2))) + S;
			setCenterX((float)(((animPosEnd.getX()-animPosStart.getX())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)animPos) - ((Math.PI/2)*(float)animTime)) / ((float)animTime/2f)))+animPosStart.getX()));
			setCenterY((float)(((animPosEnd.getY()-animPosStart.getY())/2f) * (1f + Math.cos((((Math.PI/2f)*(float)animPos) - ((Math.PI/2)*(float)animTime)) / ((float)animTime/2f)))+animPosStart.getY()));
		}
		else{
			setCenter(animPosEnd);
		}
	}
}
