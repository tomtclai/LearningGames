import java.awt.event.KeyEvent;

import Engine.World.BoundCollidedStatus;
import SpaceSmasher.Ball;
import SpaceSmasher.Paddle;
import SpaceSmasher.SpaceSmasher;

/*
 * Empty project for SpaceSmasher 
 *  
 * Authors: Kelvin Sung, Mike Panitz, Rob Nash
 * 
 * To get proper code folding (of Region and EndRegion):
 * 		1. download from: http://themindstorms.blogspot.com/2006/11/my-eclipse-code-folding-plugin.html
 *      2. look at: http://stackoverflow.com/questions/6940199/how-to-use-coffee-bytes-code-folding 
 */

public class MySpaceSmasherGame extends SpaceSmasher {

	final int kMaxBallOnScreen = 10;
	int numBallOnScreen = 0;
	
	// This function is called once when the game first begins
	protected void initialize() {
	
		numBallOnScreen = 0;
		
		// this.preloadResources();
		
		// Region: Initialize the ball and the paddle
		lifeSet.add(5);
		paddleSet.add(1);
		ballSet.add(kMaxBallOnScreen);
		for (int i=0; i<kMaxBallOnScreen; i++) {
			ballSet.get(i).setToInvisible();
		}
		// EndRegion

		// Region: Define how many blocks per row and what are the blocks
		blockSet.setBlocksPerRow(6);

		for (int i = 0; i < 2; i++) {
			blockSet.addNormalBlock(1);
			blockSet.addFireBlock(1);
			blockSet.addNormalBlock(1);
			blockSet.addFireBlock(1);
			blockSet.addActiveCageBlock(1);
			blockSet.addFreezingBlock(1);
		}
		// EndRegion

	}

	// This method is called continuously about 24 to 60 times per second
	protected void update() {

		Paddle paddle = paddleSet.get(0);

		// check for left mouse button clicked
		boolean mouseLeftClicked = mouse.MouseOnScreen()
				&& mouse.isButtonTapped(1);

		// notice how to check for keyboard
		if (keyboard.isButtonTapped(KeyEvent.VK_SPACE) || mouseLeftClicked) {
			if (numBallOnScreen < kMaxBallOnScreen) {
				int c = 0;
				boolean once = false;
				while ((c<kMaxBallOnScreen) && (!once)) {
					Ball b = ballSet.get(c);
					if (!b.isVisible()) {
						b.spawn(paddle);
						numBallOnScreen++;
						once = true;
					}
					c++;
				}
			}
		}
		
		if (keyboard.isButtonDown(KeyEvent.VK_L))
			paddle.setImage("paddles/P2.png");
		if (keyboard.isButtonDown(KeyEvent.VK_N))
			paddle.setImage("paddles/Paddle_Normal.png");

		if (keyboard.isButtonDown(KeyEvent.VK_UP))
			paddle.moveUp();
		if (keyboard.isButtonDown(KeyEvent.VK_DOWN))
			paddle.moveDown();
		if (keyboard.isButtonDown(KeyEvent.VK_LEFT))
			paddle.moveLeft();
		if (keyboard.isButtonDown(KeyEvent.VK_RIGHT))
			paddle.moveRight();

		for (int i = 0; i<kMaxBallOnScreen; i++) {
			Ball b = ballSet.get(i);
			if (b.isVisible())
				processBall(b, paddle);
		}
				
	}

	
	private boolean processBall(Ball ball, Paddle paddle) {// Region: bounce the ball with the
											// window bounds
		if (ball != null && ball.isVisible()) {
			BoundCollidedStatus status = ball.collideWorldBound();

			if (paddle.collided(ball))
				paddle.reflect(ball);

			/*
			 * Bouncing the ball against the window bounds
			 */
			switch (status) {
			case TOP: {
				ball.reflectTop();
				ball.playBounceSound();
				break;
			}

			case BOTTOM: {
				ball.setToInvisible();
				ball.playDieSound();
				numBallOnScreen--;

				lifeSet.remove();

				if (lifeSet.getCount() < 1) {
					gameLost();
					return false;
				}

				break;
			}

			case LEFT: {
				ball.reflectLeft();
				ball.playBounceSound();
				break;
			}

			case RIGHT: {
				ball.reflectRight();
				ball.playBounceSound();
				break;
			}
			// Catch the case where the ball is inside the
			// world and not hitting any bounds. A warning is
			// given if all cases are not handled.
			default:
				break;
			}
		}
		return true;
		// EndRegion
	}
}
