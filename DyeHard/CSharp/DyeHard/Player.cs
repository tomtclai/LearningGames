using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

// Player takes input from the current game player to manipulate the Hero object.
// Player represents essentially the joystick commands.

namespace Dyehard
{
    class Player
    {
        private Hero hero;

        public Player(Hero hero)
        {
            this.hero = hero;
        }

        public void update()
        {
            // update hero per input
            hero.push(XNACS1Base.GamePad.ThumbSticks.Right);
        }
    } 
}
