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

namespace SpaceSmasherLib.Entities
{
    public class Block : CustomRectangle
    {
        private const string defaultTexture = "Blocks/Block_Normal";
        private const string defaultFrozenTexture = "Blocks/Block_Iced";
        protected const string defaultDieSound = "bounce2";
        private const string defaultDebugTexture = "Blocks/Block_Normal";
        protected bool debugMode;
        protected string debugTexture;
        protected string specialTexture;
        protected bool powerRevealed;

        public enum BlockType
        {
            NORMAL, EMPTY, FIRE, FREEZING, CAGE_ACTIVE, CAGE_INACTIVE, JOKER
        }
        public enum BlockState
        {
            Normal, Frozen, Dead
        }

        protected string normalTexture;
        protected string frozenTexture;
        protected string dieSound;
        protected BlockState blockState;
        protected BlockType type;
        protected int setRow;
        protected int setColumn;

        public Block(int row, int col)
            : base(Vector2.Zero, 4f, 1.5f, defaultTexture)
        {
            setRow = row;
            setColumn = col;

            initialize();
        }

        protected virtual void initialize()
        {
            debugTexture = defaultDebugTexture;
            ShouldTravel = true;
            Velocity = VelocityDirection = Vector2.Zero;
            Texture = normalTexture = defaultTexture;
            frozenTexture = defaultFrozenTexture;
            dieSound = defaultDieSound;
            type = BlockType.NORMAL;
            setDebugMode(GameObjects.debugMode);
            updateImage();

            if (GameObjects.blockList == null)
                GameObjects.blockList = new List<Block>();

            GameObjects.blockList.Add(this);
        }

        public void autoPosition(int blocksPerRow, float HorizontalMargin, float VerticalMargin)
        {
            int index = GameObjects.blockList.IndexOf(this);

            if (index >= 0 && index < GameObjects.blockList.Count)
            {
                if (index == 0)
                {
                    CenterX = HorizontalMargin + (Width * 0.2f);
                    CenterY = XNACS1Base.World.WorldDimension.Y - VerticalMargin - (Height * 2f);
                }
                else
                {
                    if (index % blocksPerRow == 0)
                    {
                        CenterX = HorizontalMargin + (Width * 0.2f);
                        CenterY = GameObjects.blockList[index - 1].CenterY - (Height * 1.15f);
                    }
                    else
                    {
                        CenterX = GameObjects.blockList[index - 1].CenterX + (Width * 1.15f);
                        CenterY = GameObjects.blockList[index - 1].CenterY;
                    }
                }
            }
        }

        public virtual void update()
        {

        }

        public BlockType getType()
        {
            return type;
        }

        public int getRow()
        {
            return setRow;
        }

        public int getColumn()
        {
            return setColumn;
        }

        public void remove()
        {
            Visible = false;
            RemoveFromAutoDrawSet();
            Velocity = Vector2.Zero;
            blockState = BlockState.Dead;
            updateImage();
        }

        internal void destroy()
        {
            remove();
            if (GameObjects.blockList != null)
                GameObjects.blockList.Remove(this);
        }

        public void freezeTheBlock()
        {
            blockState = BlockState.Frozen;

            updateImage();
        }

        public BlockState getState()
        {
            return blockState;
        }

        public void setState(BlockState newState)
        {
            blockState = newState;
            
            updateImage();
        }

        public string getNormalTexture()
        {
            return normalTexture;
        }

        public void setNormalTexture(string value)
        {
            normalTexture = value;

            updateImage();
        }

        public string getFrozenTexture()
        {
            return frozenTexture;
        }

        public void setFrozenTexture(string value)
        {
            frozenTexture = value;

            updateImage();
        }

        public string getDieSound()
        {
            return dieSound;
        }

        public void setDieSound(string value)
        {
            dieSound = value;
        }

        public bool getDebugMode()
        {
            return debugMode;
        }

        protected virtual void updateImage()
        {
            AddToAutoDrawSet();
            Visible = true;

            if (getState() == BlockState.Frozen)
            {
                Texture = frozenTexture;
            }
            else if (getType() == BlockType.NORMAL)
            {
                Texture = normalTexture;
            }

            if(getState() == BlockState.Dead)
            {
                RemoveFromAutoDrawSet();
                Visible = false;
            }
        }

        public void setDebugMode(bool value)
        {
            debugMode = value;

            updateImage();
        }

        public virtual void revealPower()
        {
            setIsPowerRevealed(true);
        }

        public virtual bool isPowerRevealed()
        {
            return powerRevealed;
        }

        public virtual void setIsPowerRevealed(bool value)
        {
            powerRevealed = value;
            updateImage();
        }

        public string getSpecialTexture()
        {
            return specialTexture;
        }

        public void setPowerTexture(string value)
        {
            specialTexture = value;

            updateImage();
        }
    }
}
