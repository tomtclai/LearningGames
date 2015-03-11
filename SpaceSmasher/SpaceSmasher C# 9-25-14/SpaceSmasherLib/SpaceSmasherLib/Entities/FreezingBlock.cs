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
    public class FreezingBlock : Block
    {
        private const string defaultSpecialTexture = "Blocks/Block_IcePowerDown";

        public FreezingBlock(int row, int col) : base(row, col) { }

        protected override void initialize()
        {
            base.initialize();
            specialTexture = defaultSpecialTexture;
            type = BlockType.FREEZING;
            debugTexture = defaultSpecialTexture;
            updateImage();
        }

        protected override void updateImage()
        {
            base.updateImage();

            if (getState() == BlockState.Frozen)
            {
                Texture = frozenTexture;
            }
            else if (debugMode || powerRevealed)
            {
                Texture = defaultSpecialTexture;
            }
            else
            {
                Texture = normalTexture;
            }
        }

        /*
        public void reflect(Ball ball)
        {
            // ball collision with bricks
            if (ball != null && blockState != BlockState.Dead && !ball.isDead())
            {
                if (ball.isBurning())
                {
                    destroy();
                }
                else
                {
                    pushOutCircle(ball);
                    reflectHelper(ball);

                    if (!specialActive)
                    {
                        specialActive = true;
                        Texture = specialTexture;
                        ball.freezeTheBall();
                    }
                    else
                    {
                        Destroy();
                    }
                }
            }
         
        }
        */

        /*
        public override void activateSpecial()
        {
            specialActive = true;

            if(!getDebugMode())
                Texture = specialTexture;
        }
        */
    }
}
