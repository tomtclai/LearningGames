using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Input;

namespace Dyehard
{
    abstract class GameObject
    {
        abstract public void remove();
        abstract public void draw();
        abstract public void update();
    }
}
