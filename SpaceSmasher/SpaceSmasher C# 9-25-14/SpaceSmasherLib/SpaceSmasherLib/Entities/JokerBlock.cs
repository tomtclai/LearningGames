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
    public class JokerBlock : Block
    {
        private const string defaultTexture = "Blocks/Block_JokerSwap";

        public JokerBlock(int row, int col) : base(row, col) { }

        protected override void initialize()
        {
            base.initialize();
            Texture = debugTexture = defaultTexture;
            type = BlockType.JOKER;
            updateImage();
        }

        protected override void updateImage()
        {
            base.updateImage();

            Texture = defaultTexture;
        }
    }
}
