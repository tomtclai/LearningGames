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
using SpaceSmasherLib.Entities;
namespace SpaceSmasherLib.Entities
{
    public class KeyBoardDevice
    {
        private KeyboardState current;
        private KeyboardState previous;

        public KeyBoardDevice()
        {
            current = Keyboard.GetState();
            previous = current;
        }

        public bool isKeyDown(Keys key)
        {
            return current.IsKeyDown(key);
        }

        public bool isKeyUp(Keys key)
        {
            return current.IsKeyUp(key);
        }

        public bool isKeyTapped(Keys key)
        {
            if (previous.IsKeyUp(key) && current.IsKeyDown(key))
                return true;
            return false;
        }

        public void update()
        {
            previous = current;
            current = Keyboard.GetState();
        }
    }
}
