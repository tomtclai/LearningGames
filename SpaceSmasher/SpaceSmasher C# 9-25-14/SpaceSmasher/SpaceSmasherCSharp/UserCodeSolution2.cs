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
    class UserCodeSolution2 : SpaceSmasherBase
    {
        protected override void initialize()
        {
            lifeSet.add(5);
            paddleSet.add(1);
            ballSet.add(1);
            ballSet.get(0).Visible = false;
            blockSet.setBlocksPerRow(6);



            /* TODO 1
             * Single For Loop
             * 
             * for( <one_time_initialization> ; <someBooleanQuestion>; <increment_expression> ) {
             *   //loop on this code if the question resulted in a true value
             * } //or skip just past this line if the question resulted in a false value
             * 
             * 
             * Notice the similarities to the if statement in the for loop structure below.
             * Both have a boolean expression (the question) that determines if the code
             * in the block (or body of the loop or if) should be executed.  The primary difference
             * is that a loop may execute its code body 0, 1, or n(>1) times, whereas an if statement
             * will execute its code body (or block) 0 or 1 times exactly.
             * 
             * Question: Can you completely skip over a loop, not doing the code in the body even once?
             * Answer: If the loop's boolean expression results in false it will be skipped, even if
             * it's the first loop iteration.  For example "for(int i = 100; i < 10; i++) {" won't execute
             * even one loop iteration.
             * 
             * This for loop adds blocks to each row of our game, one row at a time.  
             * Since the blocks per row is configured above as 6, we add 6 blocks per loop
             * iteration, and our engine behind the scenes takes us to the next row.
             * 
             * In pseudocode, the IF asks: "If we've won, call the gameWin() function"
             * Notice that the IF says nothing about what to do if we haven't won; 
             * to make such a structure we'd need to introduce an IF/ELSE.  
             * 
             * If we removed this for statement:
             * before: The game will build only one row of blocks, as indicated by the statements inside
             * the loop, but will not repeat those block creation steps for rows two or three.
             * after: The game builds multiple rows of blocks, controlled by the loop variable 'i'.
             * 
             * TODO 1b: Draw a flowchart of the single for loop.
             */
            for (int i = 0; i < 1; i++)
            {		/** SOLN */
                blockSet.addNormalBlock(1);
                blockSet.addFireBlock(1);
                blockSet.addNormalBlock(1);
                blockSet.addFireBlock(1);
                blockSet.addFreezingBlock(1);//blockSet.addActiveCageBlock(1);
                blockSet.addFreezingBlock(1);
            }
            /* TODO 3
             * The While Loop
             * 
             * Take the above for loop and convert it to the logically equivalent while loop.
             * Note that all for loops can be converted to while loops, and vice-versa.
             * Consider the pseudocode first...
             * 
             * 
             * for( <expression1> ; <expression2>; <expression3> ) {
             *   	print "Hello";
             * } 
             * 
             * <expression1>;
             * while( <expression2> ) {
             * 		print "Hello";
             * 		<expression3>;
             * }
             * 
             * 
             */



            /* TODO 4
             * Nested For Loops 
             * A.k.a. loops inside loops, loops for use with multi-dimensional arrays, etc.
             * 
             * for( <variable_initializationA> ; <someBooleanQuestionA>; <increment_expressionA> ) {
             *   //only execute the code here (another loop) if the questionA resulted in a true value
             *   for( <variable_initializationB> ; <someBooleanQuestionB>; <increment_expressionB> ) {
             *   	//loop on this code if the questionB resulted in a true value
             *   	//and implicit at this point is also that questionA resulted in a true value.
             *   }  //or, skip just past this line if the questionB resulted in a false value
             * }    //similarly, skip just past this line if the questionA resulted in a false value
             * 
             * 
             * Question: In this example, we put a loop inside a loop and called this a nested loop. How
             * many times can we nest one loop inside another?
             * Answer: Loops can be nested one inside another until the computational limits of your
             * finite computer have been reached. 
             * 
             * This set of for loops iterate over the blocks in our game; one loop controls getting 
             * blocks in the x-axis and the second loop controls movement in the y-axis.  
             * Notice we ask the game for the number of rows and columns found in the blockSet, rather
             * than try to manage that and remember it ourselves.
             * 
             * In pseudocode, the outer for structure loops over every possible row value from 0 to n-1
             * where n is the number of rows in the blockSet.  For any given row, the inner for structure
             * starts the column count at 0 and loops the innermost code m times (where m == #ofColumns).
             * Thus, the total number of times the two lines are executed below is n*m (which is 6*3 in 
             * this example. 
             * 
             * If we removed this set of nested for loops:
             * before: The game will not reveal whether a block is a fire or ice block.
             * after: The game builds multiple rows of blocks, controlled by the loop variable 'i'.
             * 
             * TODO 4b: Draw a flowchart of the nested for loops.
             */
            int nextRow = 0, nextColumn = 0;
            for (nextRow = 0; nextRow < blockSet.getNumRows(); nextRow++)
            {   /** SOLN */
                for (nextColumn = 0; nextColumn < blockSet.getNumColumns(); nextColumn++)
                {
                    Block nextBlock = blockSet.getBlockAt(nextRow, nextColumn);
                    nextBlock.revealPower();
                }
            }

            //setAllowDebug(true);
            //setDebugMode(true);
        }

        private bool areAllBlocksRemoved()
        {     //check for winning condition with this method  
            bool retVal = true;
            /* TODO 2
            * Single While Loop
            * 
            * <variable_initializationA> ;
            * while( <someBooleanQuestionA> ) {
            *   //loop on this code if the question resulted in a true value
            *   <increment_expressionA>;  //forgetting this line of code results in an infinite loop 
            * } //or skip just past this line if the questionA resulted in a false value
            * 
            * 
            * Notice the similarities to the for structure found in the while structure above.
            * Both have a boolean expression (the question) that determines if the code
            * in the block (or body of the loop) should be executed.  The primary differences
            * are that the while header includes only one of the three expressions found in the for
            * loop, and may look like a simpler structure.  In fact, the while solves any and all of 
            * the problems a for loop can, but is more distributed with respect to its variable
            * initialization and increment.
            * 
            * 
            * This for loop adds blocks to each row of our game, one row at a time.  
            * Since the blocks per row is configured above as 6, we add 6 blocks per loop
            * iteration, and our engine behind the scenes takes us to the next row.
            * 
            * In pseudocode, the while seeks to loop over all of the blocks in the first row(==0)
            * and if even one block still exists (ie, != null) we break out of the loop after
            * setting the return value to false, meaning the game is stil ongoing.
            * 
            * If we removed this while statement: 
            * 
            * before: The game wont ever end unless the block at row 0 and column 0 has been destroyed.
            * after: The game ends when all of the blocks in the topmost row (0) have been destroyed.
            * 
            * 
            */

            int nextRow = 0;
            while (nextRow < blockSet.getNumRows())
            {  /** SOLN TODO 5*/
                int nextColumn = 0;
                while (nextColumn < blockSet.getNumColumns())
                {    /** SOLN TODO 2*/
                    Block nextBlock = blockSet.getBlockAt(nextRow, nextColumn);  /**ROB: SOLN TBA add loop */
                    if (nextBlock != null && nextBlock.getType() != Block.BlockType.EMPTY)
                    {
                        //&& nextBlock.getType() == BlockType.NORMAL) {
                        retVal = false;
                        break;
                    }
                    nextColumn++;
                }
                nextRow++;
            }
            /* TODO 5
             * Loops Inside Loops 
             * A.K.A. Nested Loops
             * 
             * Add a second layer of looping to the code above by nesting the while loop completely inside 
             * of a new for loop that you will build.  This new loop should wrap the existing code (which 
             * checks the blocks in a given column) and extend this so the new outer loop varies the row selected, 
             * rather than always using row 0 as the code does currently.
             * 
             * If you didn't have the outer for loop:
             * before: The game's winning condition is to destroy bricks in all columns, for row 0 only
             * after:  The game's winning condition is to destroy bricks in all columns, for all rows.  
             * 
             */


            return retVal;
        }

        protected override void update()
        {
            Paddle paddle = paddleSet.get(0);
            Ball ball = ballSet.get(0);


            bool playerWonGame = areAllBlocksRemoved();
            if (playerWonGame)
            {  //EXAMPLE 1 asks if playerWonGame == true
                gameWin();		  //and then calls the gameWin() function
            }

            if (!playerWonGame)
            {
                if (keyboard.isKeyTapped(Keys.Space) || (MouseDevice.MouseOnScreen() && MouseDevice.IsLeftTapped()))
                {
                    if (ball.Visible) ball.spawn(paddle);
                }

                //Editors note: we revert sections of future labs to their starting point where it
                //doesn't affect the future labs to block newer labs providing all of the answers
                //to previous labs, and the below unduly nested if statement is one such example
                if (MouseDevice.MouseOnScreen())
                {  //is a MouseDevice connected to the computer?
                    if (MouseDevice.IsLeftTapped())
                    {	//is the MouseDevice's left-button pressed?
                        if (ball.Visible == false)
                        {  //is there no ball on the screen currently
                            ball.spawn(paddle);
                        }
                    }
                }
                if (keyboard.isKeyDown(Keys.Left))
                {          //SOLN  
                    paddle.moveLeft();
                    //MouseDevice.setX()  //solution to bug 1001
                }
                if (keyboard.isKeyDown(Keys.Right))
                { 		 //SOLN
                    paddle.moveRight();
                    //MouseDevice.setX() //solution to bug 1001
                    // MouseDevice.setWorld(new World());
                }
                if (MouseDevice.MouseOnScreen())
                {					       //SOLN
                    paddle.CenterX = MouseDevice.getMouseX();
                    paddle.clampAtWorldBounds();
                }

                if (ball != null && ball.Visible)
                {
                    BoundCollidedStatus status = ball.collideWorldBound();

                    switch (status)
                    {
                        case BoundCollidedStatus.CollidedTop:
                            {
                                ball.reflectTop();
                                ball.playBounceSound();
                                break;
                            }

                        case BoundCollidedStatus.CollideBottom:
                            {
                                ball.Visible = false;
                                ball.playDieSound();

                                lifeSet.remove();

                                if (lifeSet.getCount() < 1)
                                {
                                    gameLost();
                                }

                                break;
                            }

                        case BoundCollidedStatus.CollidedLeft:
                            {
                                ball.reflectLeft();
                                ball.playBounceSound();
                                break;
                            }

                        case BoundCollidedStatus.CollidedRight:
                            {
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

                    if (paddle.Collided(ball))
                    {
                        paddle.reflect(ball);  //method pushes this ball out a bit

                        //BUG: paddle.reflect(ball) and ball.reflect(paddle) do different things
                        //issue: using paddle.reflect(ball) pushes out in the y where ball.reflect paddle doesnt.
                        //solutionToBug: have both methods do the algorithm below (pushout and then call superclass?)
                        //instead of only one.
                        //solution: have the pushout amount be relative to the inverse of the current
                        //direction of motion, so if the ball was coming down from the top you move it -5 
                        //in the y (or up) and if the ball is coming from the right to the left, you move
                        //it +5 in the x (where 5 is the speed)
                        ball.playBounceSound();
                    }

                    //ROB/KELVIN; consider undoing the import so the students know PaddleStates live inside Paddles?
                    //ROB/KELVIN Q2: unite Ball and Paddle Types/States respectively, to reduce logic to:
                    //	if(ball.getState() != paddle.getState() ) ball.setState(paddle.getState());

                    if (paddle.getState() == Paddle.PaddleState.NORMAL) ball.setType(Ball.BallType.NORMAL);
                    if (paddle.getState() == Paddle.PaddleState.FIRE) ball.setType(Ball.BallType.FIRE); //soln
                    else if (paddle.getState() == Paddle.PaddleState.ICE) ball.setType(Ball.BallType.ICE); //soln


                    //logic reduction here to see ball and paddle states are synchronized
                    /** ball.setState(paddle.getState());  REDUCE to this!  */

                    if (blockSet.isBallCollidingWithABlock(ball))
                    {
                        handleBlockBallCollision(ball, paddle);
                    }
                }
            }
        }

        private void removeNeighboringBlocks(int row, int col, Block.BlockType type)
        {//todo: refactor type to state
            /* TODO 6
             * Nested For Loops
             * A.k.a. "Loop inside a loop"
             * 
             * The code in this section will add special behaviors to special block types such as 
             * FIRE and ICE.  If a fire block is destroyed, all neighboring fire blocks should be removed
             * as far away as 2 blocks, in both the x and y axis.  Since there are two axes to consider
             * here, we'll have 2 loops - one for checking blocks in the x axis (the two neighbors to the
             * left and the two neighbors to the right), and the second loop surrounding the first to 
             * adjust the y-axis so we also look at the neighbors above and below the block in question.  
             * 
             * 
             * Note that this effect is relative to only one block (the destroyed block) and not cumulative
             * in that if a destroyed fire block removes a neighboring block, you don't reapply this algorithm
             * for that neighbor too; she simply disappears without removing her neighbors as well.
             * 
             * before: When a fire or ice block is destroyed, it is the only block removed from the set
             * after: When a fire or ice block is destroyed, all blocks of that type that are 2 blocks or
             * less away will also be destroyed.
             *  
             * Start by uncommenting out the starter code below and transforming the "for" statements 
             * from pseudocode to actual code.  Some lines of code (like the ones that skip certain cases)
             * have already been written for you, and you just need to wrap these lines inside your loops.
             *  
             *  Green lines of code need to be uncommented below and transformed into working for loops
             *  Blue lines of code below need to be uncommented and left as is.
             */

            //for (every value of y starting at row-2 and ending at row+2) {  //TODO
            /** if (y < 0 || y >= blockSet.getNumRows()) continue;  //skip certain cases */
            //for(every value of x starting at col-2 and ending at col+2) {  //TODO
            /** if (x < 0 || x >= blockSet.getNumColumns()) continue;  //skip certain cases 
    	 
            Block nextBlock = blockSet.getBlockAt(iRow, iCol);  //get the block to examine
            if (nextBlock != null && nextBlock.getType() == type) blockSet.remove(nextBlock); */
            //}
            //}


            for (int iRow = row - 2; iRow < row + 2; iRow++)
            {				  /** SOLN */
                if (iRow < 0 || iRow >= blockSet.getNumRows()) continue;  //skip certain cases 

                for (int iCol = col - 2; iCol < col + 2; iCol++)
                {		 /** SOLN */
                    if (iCol < 0 || iCol >= blockSet.getNumColumns()) continue;  //skip certain cases 

                    Block nextBlock = blockSet.getBlockAt(iRow, iCol);  //get the block to examine
                    if (nextBlock != null && nextBlock.getType() == type) blockSet.remove(nextBlock);
                }
            }
        }

        private void handleBlockBallCollision(Ball ball, Paddle paddle)
        {
            Block block = blockSet.getCollidedBlock(ball);	//find our collision target
            block.reflect(ball);						    //reverse ball direction relative to the target

            int row = block.getRow();
            int col = block.getColumn();


            if (block.getType() != Block.BlockType.CAGE_ACTIVE)
                blockSet.remove(block); //except for CAGE blocks, remove collided block 

            ball.playBounceSound();

            switch (block.getType())
            {  //TODO: collapse switch when type refactored to state
                case Block.BlockType.FIRE:
                    removeNeighboringBlocks(row, col, Block.BlockType.FIRE);
                    paddle.startFire(); //attempt at solution for bug 1002
                    break;

                case Block.BlockType.FREEZING:
                    removeNeighboringBlocks(row, col, Block.BlockType.FREEZING);
                    paddle.startIce(); //attempt at solution for bug 1002
                    break;

                case Block.BlockType.NORMAL:  //do nothing for a normal block	
                    break;

            }
        }
    }
}
