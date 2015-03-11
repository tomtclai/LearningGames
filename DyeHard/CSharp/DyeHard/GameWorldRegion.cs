using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    abstract class GameWorldRegion : GameObject
    {
        abstract public void move(); // called before update

        abstract public bool isOffScreen();
        abstract public float rightEdge();
    }
}
