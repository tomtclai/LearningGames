using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CustomWindower.CoreEngine;

namespace GhostFinder.GhostEngine{
    public class BaseCode {
        public static float worldWidth { get; internal set; }
        public static float worldHeight { get; internal set; }

        public static DrawSet activeDrawSet { get; internal set; }
        public static ResourceLibrary activeLibrary { get; internal set; }

        public static Keyboard activeKeyboard { get; internal set; }
        public static Mouse activeMouse { get; internal set; }

    }
}
