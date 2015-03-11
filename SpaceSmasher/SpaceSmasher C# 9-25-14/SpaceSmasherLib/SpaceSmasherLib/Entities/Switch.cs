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
    public class Switch : CustomRectangle
    {
        public enum SwitchState
        { 
            Active, Inactive
        };

        private const string defaultActiveTexture = "Switch/state_changer_unlock_button";
        private const string defaultDeactiveTexture = "Switch/state_changer_unlock_button_idle";

        private SwitchState state; 
        
        public Switch()
            : this(XNACS1Base.World.WorldDimension.X * 0.5f, XNACS1Base.World.WorldDimension.Y - (XNACS1Base.World.WorldDimension.Y * .15f),
            XNACS1Base.World.WorldDimension.X * 0.25f, XNACS1Base.World.WorldDimension.Y * 0.05f)
        {

        }

        public Switch(float x, float y)
            : this(x, y, XNACS1Base.World.WorldDimension.X * 0.3f, XNACS1Base.World.WorldDimension.Y * 0.05f)
        {

        }

        public Switch(float x, float y, float width, float height): base(new Vector2(x,y), width, height)
        {
            initialize();
        }

        private void initialize()
        {
            ShouldTravel = true;
            Velocity = VelocityDirection = Vector2.Zero;
            state = SwitchState.Inactive;
            Texture = defaultDeactiveTexture;
            addtoGameobjects();
        }

        public virtual void update()
        { }

        protected void addtoGameobjects()
        {
            if (GameObjects.switchList == null)
                GameObjects.switchList = new List<Switch>();

            GameObjects.switchList.Add(this);
        }

        protected void removeFromGameObjects()
        {
            if (GameObjects.switchList != null)
                GameObjects.switchList.Remove(this);
        }

        public void remove()
        {
            state = SwitchState.Inactive;
            Visible = false;
            RemoveFromAutoDrawSet();
            Velocity = Vector2.Zero;
        }

        internal void destroy()
        {
            remove();
            removeFromGameObjects();
        }


        public new void activate()
        {
            Texture = defaultActiveTexture;
            state = SwitchState.Active;
        }

        public void deactivate()
        {
            Texture = defaultDeactiveTexture;
            state = SwitchState.Inactive;
        }

        public bool isActive()
        {
            return state == SwitchState.Active;
        }
    }
}
