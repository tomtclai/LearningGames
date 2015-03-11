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
    public class FireBlock : Block
    {
        private const string defaultSpecialTexture = "Blocks/Block_FirePowerUP";

        public FireBlock(int row, int col) : base(row, col) { }

        protected override void initialize()
        {
            base.initialize();
            specialTexture = defaultSpecialTexture;
            type = BlockType.FIRE;
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
        public override void activateSpecial()
        {
            specialActive = true;

            if (!getDebugMode())
                Texture = specialTexture;
        }
        */
    }
}
