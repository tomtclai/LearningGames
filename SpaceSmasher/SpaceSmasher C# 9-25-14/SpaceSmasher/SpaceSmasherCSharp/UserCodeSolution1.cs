#region Reference to system libraries
using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;

using Microsoft.Xna.Framework.Storage;
#endregion
using SpaceSmasherLib;
using SpaceSmasherLib.Entities;

namespace BlockBreakerGame
{
public class UserCodeSolution1 : SpaceSmasherBase {
	//BUG: lowercase MouseOnScreen()
	//BUG: use enums or constants for 1 in isButtonTapped
	//BUG: ball can never be null here: if(ball != null && ball.visible == true)
	//BUG: OSX renders block sizes incorrectly	
	
	//ROB: add a method header here
    protected override void initialize() {
        lifeSet.add(5);
        paddleSet.add(1);
        ballSet.add(1);
        ballSet.get(0).Visible = false;
        blockSet.setBlocksPerRow(6);
        
        for (int i = 0; i < 5; i++) {
	        blockSet.addNormalBlock(1);
	        blockSet.addFireBlock(1);
	        blockSet.addNormalBlock(1);
	        blockSet.addFireBlock(1);
	        blockSet.addActiveCageBlock(1);
	        blockSet.addFreezingBlock(1);
	      //  blockSet.addActiveCageBlock(1);
        }
        
        for (int iRow = 0; iRow < blockSet.getNumRows(); iRow++) {
            for (int iCol = 0; iCol < blockSet.getNumColumns(); iCol++) {
                Block nextBlock = blockSet.getBlockAt(iRow, iCol);
              //  if (nextBlock.getType() == Block.BlockType.FIRE)
                nextBlock.revealPower();
            }
        }
        
        
        //blockSet.addFireBloc(4);
        // setDebugMode(true);
        // setAllowDebug(false);
    }

    //ROB: Comment this more
    private bool areAllBlocksRemoved() {     //check for winning condition with this method  
    	 bool retVal = true;
         int nextColumn = 0;

         while (nextColumn < blockSet.getNumColumns()) {
             Block nextBlock = blockSet.getBlockAt(0, nextColumn);
             if (nextBlock != null ) { //ROB!!! This is a temporary change for testing fire and ice blocks!
                     //&& nextBlock.getType() == BlockType.NORMAL) {
                 retVal = false;
                 break;
             }
             nextColumn++;
         }
         return retVal;
    }
    
    protected override void update() {
        Paddle paddle = paddleSet.get(0);
        Ball ball = ballSet.get(0);

        /* EXAMPLE 1
         * Single IF Statement  
         * 
         * Notice how this if statement seeks to 
         * determine if the game logic should continue
         * or if we should jump to the game winning code
         * in gameWin()
         */
        bool playerWonGame = areAllBlocksRemoved();
        if (playerWonGame) {  // The player has won when all the normal blocks are removed
            gameWin();
        } 
        
        /* EXAMPLE 2
         * Sequential, Single IF Statement Demonstration
         * 
         * The above if statement is followed by a 
         * second if statement in sequential order.
         * Since we omitted the "else" statement here
         * these two ifs are unrelated to one another
         * and their code blocks are not mutually exclusive
         * We'll improve this code once we reach if/else
         * statements in the lab
         */
        if(!playerWonGame) {
        	
        	/* TODO 5
        	 * Compound Boolean Expressions using Logical AND and Logical OR 
        	 * 
        	 * Here, we'll combine the mouse and keyboard if statements into one 
        	 * longer, compound if statement using AND and OR.
        	 * 
        	 * The if statement below should ask: "Is the spacebar pressed
        	 * OR is BOTH the mouse onscreen AND the left button clicked?"
        	 * Note that the OR is "||", the AND is "&&" and the BOTH is accomplished
        	 * by adding parenthesis around MouseOnScreen() and isButtonTapped()
        	 *
        	 * If either of these (or both) are true, then this will execute the 
        	 * code you already wrote in the TODO1 section below.
        	 * How is this question change if we swapped all ANDs and ORs? 
        	 * This would produce the question: "Is the spacebar pressed
        	 * AND EITHER the mouse onscreen OR the the left button is clicked?" 
        	 * 
        	 * before: Only pressing the space key would cause the ball to spawn.
        	 * after:  Now, using the mouse or the space key causes balls to spawn.
        	 * 
        	 */
        
        	/*      Starting code:
        	 *            if (keyboard.isButtonTapped(KeyEvent.VK_SPACE)) {
        	 */
         
           if ( keyboard.isKeyTapped(Keys.Space) || (MouseDevice.MouseOnScreen() && MouseDevice.IsRightTapped())) {  /** SOLN */    	
            	/*  TODO 1
            	 *  Single IF Statement 
            	 * 
            	 *   Write a single If statement here that asks:
            	 * 
            	 *  "Is the ball invisible?".  If it is invisible, then  
            	 *   put it on the screen near the paddle by calling "ball.spawn(paddle)".
            	 *  
            	 *  See the EXAMPLE1 and EXAMPLE1B for a sample single IF statement.
            	 *  
            	 *  Transform this pseuducode into the if statement below:
            	 *  Step 1: if the ball is invisible, then tell the ball to spawn near the paddle.
            	 *  Step 2: fill in the code gaps below to obtain working Java code
            	 *  
            	 *   if( <your question goes here> ) {
            	 *	    <your "then" code goes here to spawn the paddle near the ball>
            	 *   }
            	 *   
            	 *   before: hitting space or left-clicking does nothing in our game
            	 *   after: hitting space or left-clicking spawns a ball and starts the game
            	 *   
            	 */   
                 if (ball.Visible == false) {	/**   SOLN */
                    ball.spawn(paddle);
                }
            }
            
            /* TODO 4
             * Use Logical AND to Enable Mouse Support for Ball Spawning
             * 
             * Rewrite the three IF statements below as one compound IF statement using Java's logical
             * AND operator "&&".  Start by commenting out the 3 if statements, and build your own in 
             * its place.
             * 
             * Notice the IF statements and what they accomplish; in order, they ask the following:
             * (1) Is the mouse is onscreen? (i.e. connected to your computer)
             * (2) Is the mouse's left-button is pressed?
             * (3) Is the ball already spawned? (i.e. is the ball already visible?)
             *  
             * before: Three if statements, with one nested inside the other.
             * after: One compound if statement to rule the spawning of the ball.
             */
            if(MouseDevice.MouseOnScreen()) {  //is a mouse connected to the computer?
                if (MouseDevice.IsRightTapped())
                {	//is the mouse's left-button pressed?
            		if (ball.Visible == false) {  //is there no ball on the scree currently
                        ball.spawn(paddle);
                    }
            	}
            }
                    
            if(MouseDevice.MouseOnScreen() && MouseDevice.IsRightTapped() && ball.Visible == true) {   /** SOLN */
                    ball.spawn(paddle);        
            }
            
            
            
            /*  TODO 2
             *  Sequential, Single If statements. 
             * 
             *  These will have the form as outlined in the lab, and will ask:
             * 
        	 *  "Is the user pressing the left key? If so, move the paddle left."
        	 *  "Is the user pressing the right key? If so, move the paddle right."
        	 *  "Is the user pressing the up key? If so, move the paddle up."
        	 *  "Is the user pressing the down key? If so, move the paddle down."  
        	 *  
        	 *  Transform this pseuducode above into the if statement below:
        	 *  by replacing "LEFT button" with "KeyEvent.VK_LEFT" and
        	 *  by replacing "keyboard is pressing down" with "keyboard.isButtonDown()"
        	 *  by replacing "Tell the paddle to move left" with "paddle.moveLeft()"
        	 *
	         *	 if (keyboard is pressing down the LEFT button) {
	         *       Tell the paddle to move left
	         *   }
	 		 *
	         *   if (keyboard is pressing down the RIGHT button) {
	         *       Tell the paddle to move right
	         *   }
	         *  
	         *   if (the keyboard is pressing UP) { 
	         *   	Tell the paddle to move up 
	         *   }
	         *   
	         *   if pressing down {  
	         *   	move paddle down. 
	         *   }
	         *   
	         *   before:
	         *   after:
	         *   
	         *   Take a look at the animated gif in the lab for a pictorial example of the before and after.
	         *   
             */
            
            //BUG: NOTE: the mouse check must come first here, or the if's need to be joined as an if-else clause for the keyboard code to function correctly  
            if (keyboard.isKeyDown(Keys.Left)) {          /** SOLN */  
                paddle.moveLeft();
                //mouse.setX()  //solution to bug 1001
            } else if (keyboard.isKeyDown(Keys.Right)) {  /** SOLN */
                paddle.moveRight();
                //mouse.setY() //solution to bug 1001
            }
            if (MouseDevice.MouseOnScreen()) {					        /** SOLN */
                paddle.CenterX = MouseDevice.getMouseX();
                paddle.clampAtWorldBounds(); 
            }  
            
            
   
            
            /* EXAMPLE 4
             * Compound Boolean Expressions using Logical AND
             * 
             * Notice how we check the ball first for null before we try to use it.
             * By ordering the null check first and using the double '&', we perform a short-circuit 
             * evaulation of the clauses in the if statement.  This means that the && will
             * block execution of the second Boolean expression if the first expression is false.
             *
             * This is based on the following observations:
             * FALSE AND X is always FALSE, regardless of X.
             * TRUE OR Y is always TRUE, regardless of Y.
			 * 
			 * Also observe how reversing the expressions
             * is a logic error and could result in NullPointerExceptions being thrown.
             * So we can see order of boolean expressions is important inside an 
             * if statement, just like the order of the statements in an if/else chain matters as well.
             */
            if ( ball != null && ball.Visible) {  
                BoundCollidedStatus status = ball.collideWorldBound();
                switch (status) {
                    case BoundCollidedStatus.CollidedTop: {
                        ball.reflectTop();
                        ball.playBounceSound();
                        break;
                    }

                    case BoundCollidedStatus.CollideBottom: {
                        ball.Visible = false;
                        ball.playDieSound();

                        lifeSet.remove();

                        if (lifeSet.getCount() < 1) {
                            gameLost();
                        }

                        break;
                    }

                    case BoundCollidedStatus.CollidedLeft: {
                        ball.reflectLeft();
                        ball.playBounceSound();
                        break;
                    }

                    case BoundCollidedStatus.CollidedRight: {
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

               /* TODO Z
                * another good single if (without it, the ball goes through the paddle!)
                * 
                * before: The ball goes right through the paddle!
                * after: The ball reflects off the paddle and plays a bounce sound
                * 
                * Take a look at the animated gif in the lab for a pictorial example of the before and after.
                */
                if (paddle.Collided(ball)) {
                    paddle.reflect(ball);
                    ball.playBounceSound();
                }

                
                //ROB/KELVIN; consider undoing the import so the students know PaddleStates live inside Paddles?
                //ROB/KELVIN Q2: unite Ball and Paddle Types/States respectively, to reduce logic to:
                //	if(ball.getState() != paddle.getState() ) ball.setState(paddle.getState());
                
          
                /* EXAMPLE 1B
                 * Single IF Statement
                 * 
                 *  The following if statement checks to see if the paddle and ball state/types
                 *  are synchronized.  If the paddle is in a NORMAL state, the ball should be 
                 *  set to a NORMAL type/state as well, which draws a standard ball (with no 
                 *  fire or ice) on the screen.
                 *  
                 */
                if(paddle.getState()==Paddle.PaddleState.NORMAL) ball.setType(Ball.BallType.NORMAL);
                
                /* TODO Y
                 * IF-ELSE Chain
                 * 
                 * Write a chain of 2 IF statements joined by an ELSE clause to determine if
                 * the ball needs to be transformed into a fireball or iceball, respectively, based
                 * on the current state of the paddle.
                 * 
                 * See the example above for how this is done for the NORMAL state and NORMAL ball type
                 * 
                 * before: Balls don't change to fire or ice as they should when they collide with such blocks
                 * after: Balls will visibly change appearance to match the paddle's fire or ice state.
                 * 
                 * Take a look at the animated gif in the lab for a pictorial example of the before and after.
                 */ 
                if(paddle.getState()==Paddle.PaddleState.FIRE) ball.setType(Ball.BallType.FIRE); /** SOLN */
                else if(paddle.getState()==Paddle.PaddleState.ICE) ball.setType(Ball.BallType.ICE); /** SOLN */
                
                
                //logic reduction here to see ball and paddle states are synchronized
                /** ball.setState(paddle.getState());  REDUCE to this!  */
                
               
                //ROB/KELVIN: where does a paddle get its state set?
                //ROB/KELVIN: where does a ball get its state set? In response to the paddle, and in the update, right?
                if (blockSet.isBallCollidingWithABlock(ball)) {
                    handleBlockBallCollision(ball, paddle);
                }
            }
        }
    }
    
    private void removeNeighboringBlocks(int row, int col, Block.BlockType type) {//todo: refactor type to state
    	for (int iRow = row - 2; iRow < row + 2; iRow++) {
			if (iRow < 0 || iRow >= blockSet.getNumRows()) continue;  //skip certain cases (a non-lab IF)

			for (int iCol = col - 2; iCol < col+2; iCol++) {
				if (iCol < 0 || iCol >= blockSet.getNumColumns()) continue;  //skip certain cases (a non-lab IF)

				Block nextBlock = blockSet.getBlockAt(iRow, iCol);
				
				/* TODO X 
				 * Compound IF statements using logical AND (&&)
				 * 
				 * To get started, if you have yet to see an example of a compound AND statement, 
				 * see EXAMPLE4 above. 
				 * 
				 * Below, we need to remove a block from the set of blocks onscreen depending on 
				 * two conditions:
				 * (1) Does the next block exist? (i.e., is the nextBlock == null or not?)
				 * (2) Does the block match the type (FIRE, ICE) we're looking for?
				 * If both of these conditions are true, then we need to remove the nextBlock from
				 * the blockSet using the .remove() function
				 * 
				 * before: No fire or ice block chain reactions for block removal
				 * after: A destroyed fire or ice block will also destroy neighboring fire and ice blocks
				 *
				 * Take a look at the animated gif in the lab for a pictorial example of the before and after.
				 */
				if (nextBlock != null && nextBlock.getType() == type) /** SOLN */
					blockSet.remove(nextBlock);
				/*
				 *  editors note for before: no code below; after: see solution below
				 */
			}

		}
    }

	private void handleBlockBallCollision(Ball ball, Paddle paddle) {
		 Block block = blockSet.getCollidedBlock(ball);	//find our collision target
         block.reflect(ball);						    //reverse ball direction relative to the target
		
		int row = block.getRow();
		int col = block.getColumn();
		
		/* TODO X
		 * Single IF Statement
		 * Write a single if statement that will determine whether to remove a block that 
		 * collided with our ball.  Some blocks, called CAGES, should be indestructable
		 * but currently, all blocks are removed when they are crashed into with the ball.
		 *  
		 * Write a single if statement that will guard the "blockSet.remove(block);" statement below.
		 * This if statement makes sure the block is NOT an active CAGE block, 
		 * and only then does the block get removed from the blockSet.
		 *  
		 *  before: All blocks, including CAGES are removed when hit in our game
		 *  after:  CAGES are unbreakable
		 *  
		 *  Take a look at the animated gif in the lab for a pictorial example of the before and after.
		 */
		if(block.getType() != Block.BlockType.CAGE_ACTIVE)  /** SOLN */ 
			blockSet.remove(block);//except for CAGE blocks, remove collided block 
	
		
		ball.playBounceSound();  //ROB: Added this
		
		switch(block.getType()) {  //TODO: collapse switch when type refactored to state
			case Block.BlockType.FIRE:
				removeNeighboringBlocks(row, col, Block.BlockType.FIRE);  
				paddle.startFire(); //attempt at solution for bug 1002
				break;

            case Block.BlockType.FREEZING:
				removeNeighboringBlocks(row, col, Block.BlockType.FREEZING);
				paddle.startIce();
                break;

            case Block.BlockType.NORMAL:  //do nothing for a normal block	
                break;
			default:	  //and do nothing here
                break;
		}
	}
	
	
    
    /* ROB: Return to this code piece and cut it or fix it depending on answers to question 2 above
     * 
     * PaddleState paddleState = paddle.getState();
    
    if(ball.isBurning() && paddleState != PaddleState.FIRE )   {  //if the ball is burning but the paddle is not, sync the ball with the paddle
    	if(paddleState == PaddleState.ICE) {
    		ball.setType(Ball.BallType.ICE);
    	} else {  //
    		
    	}
    }
    else if(ball.isFrozen() && paddleState == PaddleState.NORMAL || paddleState == PaddleState.FIRE )) {
    	
    }
    */
}
}
