//Author: Michael Letter

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CustomWindower.CoreEngine;

namespace CustomWindower.Driver {
    /// <summary>
    /// Will start all of the requred resources and offer an interface through Camara to interact
    /// does not use a standard update loop update is called prior to invalidate
    /// </summary>
    public abstract class CameraLockStepUpdater : Camera {
        protected ResourceLibrary library = new ResourceLibrary();

        protected override void load(object sender, EventArgs e) {
            base.drawSet = new DrawSet();
        }
        protected override void dispose(object sender, EventArgs e) {
            library.unloadAll(false);
            base.dispose(sender, e);
        }
    }
}
