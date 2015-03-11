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
    public class UnbreakableBlock : Block
    {
        private const string defaultClosingTexture = "Blocks/Block_Unbreakable_Closing";

        protected override void initialize()
        {
            base.initialize();
            type = Type.Unbreakable_Open;

            SetTextureSpriteSheet(defaultClosingTexture, 320, 136);
            SetTextureSpriteAnimationFrames(0, 0, 0, 0, 2, SpriteSheetAnimationMode.AnimateForwardThenStop);
            UseSpriteSheet = true;
            UseSpriteSheetAnimation = true;
        }

        public override bool isPowerRevealed()
        {
            return powerRevealed;
        }

        public override void setIsPowerRevealed(bool value)
        {
            if(value)
            {
                type = Type.Unbreakable_Closed;
            }
            else
            {
                type = Type.Unbreakable_Open;
            }
            powerRevealed = value;
            updateImage();
        }

        protected override void updateImage()
        {
            if (type == Type.Unbreakable_Closed)
            {
                // Includes current frames to allow switching animation mid frames.
                SetTextureSpriteAnimationFrames(
                    CurrentSpriteSheetFrameX, CurrentSpriteSheetFrameY,
                    2, 7,
                    2, SpriteSheetAnimationMode.AnimateForwardThenStop);
                UseSpriteSheet = true;
                UseSpriteSheetAnimation = true;
            }
            else if(type == Type.Unbreakable_Open)
            {
                SetTextureSpriteAnimationFrames(
                    0, 0,
                    CurrentSpriteSheetFrameX, CurrentSpriteSheetFrameY,
                    2, SpriteSheetAnimationMode.AnimateBackwardThenStop);
                UseSpriteSheet = true;
                UseSpriteSheetAnimation = true;
            }
            
        }
    }
}
