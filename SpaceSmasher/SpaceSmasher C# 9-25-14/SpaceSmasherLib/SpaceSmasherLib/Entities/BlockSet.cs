using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;
using XNACS1Lib;
using XNACS1Lib.XNACS1Lib;
using System.Diagnostics;

namespace SpaceSmasherLib.Entities
{
    public class BlockSet: Set
    {
        //For grid based interface
        private const float TOLERANCE = .1f;
        private Vector2 por; // Point of reference for grid.

        // A value of -1 means no blocks
        private int numBlocksPerRow = -1;
        private int numRows = 0;
        private int columnCounter = 0; // Used for calculating numRows.
        private int currentColumn = 0;// can probably be combined with columnCounter
        private int currentRow = 0; // Can probably be combined with numRows


        private float horizontalMargin;
        private float verticalMargin;

        public Type BlockType;

        public override void initialize()
        {
            if (GameObjects.blockList == null)
                GameObjects.blockList = new List<Block>();

            horizontalMargin = XNACS1Base.World.WorldDimension.X * 0.20f;
            verticalMargin = XNACS1Base.World.WorldDimension.Y * 0.15f;
        }

        public Block getBlockAt(int row, int column)
        {
            foreach (Block b in GameObjects.blockList)
            {
                if (b.getRow() == row && b.getColumn() == column)
                {
                    return b;
                }
            }
            return null;
	        
        }

        public override void add()
        {
            addNormalBlock(1);
        }

        public void setBlocksPerRow(int num)
        {
            if (num < 1)
            {
                num = 1;
                currentColumn = numBlocksPerRow - 1; // So that one added block = new row.
            }

            numBlocksPerRow = num;
        }

        public bool allBlocksAreDead()
        {
            if (GameObjects.blockList != null)
            {
                foreach (Block block in GameObjects.blockList)
                {
                    if (block.getState() != Block.BlockState.Dead)
                        return false;
                }
            }
            return (numBlocksPerRow > 0);
        }

        public void addNormalBlock(int num)
        {
            if (num < 1)
                return;

            for (int i = 0; i < num; i++)
            {
                Block block = new Block(currentRow, currentColumn);
                addBlock(block);
            }
        }

        public void addFireBlock(int num)
        {
            if (num < 1)
                return;
            for (int i = 0; i < num; i++)
            {
                FireBlock block = new FireBlock(currentRow, currentColumn);
                addBlock(block);
            }
        }

        public void addEmptyBlock(int num)
        {
            if (num < 1)
                return;
            for (int i = 0; i < num; i++)
            {
                EmptyBlock block = new EmptyBlock(currentRow, currentColumn);
                addBlock(block);
            }
        }

        public void addFreezingBlock(int num)
        {
            if (num < 1)
                return;
            for (int i = 0; i < num; i++)
            {
                FreezingBlock block = new FreezingBlock(currentRow, currentColumn);
                addBlock(block);
            }
        }

        public void addJokerBlock(int num)
        {
            if (num < 1)
                return;
            for (int i = 0; i < num; i++)
            {
                JokerBlock block = new JokerBlock(currentRow, currentColumn);
                addBlock(block);
            }
        }

        private void addBlock(Block block)
        {
            float width = ((XNACS1Base.World.WorldDimension.X - (horizontalMargin * 2f)) / numBlocksPerRow);
            float height = XNACS1Base.World.WorldDimension.Y * 0.075f;

            block.Width = width;
            block.Height = height;
            block.autoPosition(numBlocksPerRow, horizontalMargin, verticalMargin);

            columnCounter++;
            currentColumn++;
            if (columnCounter >= numBlocksPerRow)
            {
                columnCounter = 0;
                currentColumn = 0;
                numRows++;
                currentRow++;
            }
        }

        public Block getCollidedBlock(Ball ball)
        {
            if (ball != null || GameObjects.blockList != null)
            {
                foreach (Block block in GameObjects.blockList)
                {
                    if (block.Visible && block.Collided(ball))
                        return block;
                }
            }
            return null;
        }

        public bool isBallCollidingWithABlock(Ball ball)
        {
            return (getCollidedBlock(ball) != null);
        }

        public void activateUnbreakableBlocks()
        {
            foreach (Block block in GameObjects.blockList)
            {
                if (block.getState() != Block.BlockState.Dead &&
                    block.getType() == Block.BlockType.CAGE_ACTIVE)
                    block.setIsPowerRevealed(true);
            }
        }

        public void deactivateUnbreakableBlocks()
        {
            foreach (Block block in GameObjects.blockList)
            {
                if (block.getState() != Block.BlockState.Dead &&
                    block.getType() == Block.BlockType.CAGE_INACTIVE)
                    block.setIsPowerRevealed(false);
            }
        }

        public void destroyAllBlocks()
        {
            if (GameObjects.blockList != null)
            {
                foreach (Block block in GameObjects.blockList)
                    block.destroy();

                GameObjects.blockList.Clear();
            }

            if (GameObjects.blockList != null)
                GameObjects.blockList.Clear();
        }

        public void remove(Block block)
        {
            if (block != null)
            {
                Debug.WriteLine("Row: " + block.getRow() + " Column: " + block.getColumn());

                GameObjects.blockList.Remove(block);
                block.destroy();
            }
        }

        public void addActiveCageBlock(int num)
        {
            if (num < 1)
                return;
            for (int i = 0; i < num; i++)
            {
                CageBlock block = new CageBlock(currentRow, currentColumn);
                block.setIsPowerRevealed(true);
                addBlock(block);
            }
        }

        public void addInactiveCageBlock(int num)
        {
            if (num < 1)
                return;
            for (int i = 0; i < num; i++)
            {
                CageBlock block = new CageBlock(currentRow, currentColumn);
                block.setIsPowerRevealed(false);
                addBlock(block);
            }
        }

        public int getNumColumns()
        {
            return numBlocksPerRow;
        }

        public int getNumRows()
        {
            return numRows;
        }
    }
}
