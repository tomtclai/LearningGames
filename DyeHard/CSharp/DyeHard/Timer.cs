using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class Timer
    {
        private float initialTicks;
        private float ticks;

        public Timer(float seconds)
        {
            ticks = seconds * XNACS1Base.World.TicksInASecond;
            initialTicks = ticks;
        }

        public void update()
        {
            ticks -= 1;
        }

        public bool isDone()
        {
            return ticks <= 0;
        }

        public void reset()
        {
            ticks = initialTicks;
        }

        public float currentTime()
        {
            return ticks / XNACS1Base.World.TicksInASecond;
        }
    }
}
