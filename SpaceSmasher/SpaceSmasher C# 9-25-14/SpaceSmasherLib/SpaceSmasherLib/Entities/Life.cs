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
    public class Life : XNACS1Rectangle
    {
        protected const string defaultTexture = "LifeIcon/Heart_Full";
        protected const string defaultInactiveTexture = "LifeIcon/Heart_Empty";

        protected enum LifeState
        {
            Alive, Dead
        }

        protected LifeState state;
        protected string normalTexture;
        protected string deadTexture;

        public Life()
            : base(Vector2.Zero, 1f, 1f, defaultTexture)
        {
            initialize();
        }

        public Life(float x, float y)
            : base(new Vector2(x, y), 1f , 1f, defaultTexture)
        {
            initialize();
        }

        public Life(float x, float y, float height, float width)
            : base(new Vector2(x, y), height, width, defaultTexture)
        {
            initialize();
        }

        private void initialize()
        {
            normalTexture = defaultTexture;
            deadTexture = defaultInactiveTexture;
            Velocity = VelocityDirection = Vector2.Zero;

            if (GameObjects.lifeList == null)
                GameObjects.lifeList = new List<Life>();

            GameObjects.lifeList.Add(this);
            renewLife();
        }

        public void renewLife()
        {
            Texture = normalTexture;
            state = LifeState.Alive;
        }

        public void update()
        {
   
        }

        public void remove()
        {
            Texture = deadTexture;
            state = LifeState.Dead;
        }

        /// <summary>
        /// To completely destroy a life object, also clear the lifeset.
        /// </summary>
        internal void destroy()
        {
            remove();
            RemoveFromAutoDrawSet();
        }

        public bool isNormal()
        {
            return state == LifeState.Alive;
        }

        public bool isDead()
        {
            return state == LifeState.Dead;
        }

        public string getNormalTexture()
        {
            return normalTexture;
        }

        public string getDeadTexture()
        {
            return deadTexture;
        }

        public void setNormalTexture(string value)
        {
            normalTexture = value;

            if (state == LifeState.Alive)
                Texture = normalTexture;
        }

        public void setDeadTexture(string value)
        {
            deadTexture = value;

            if (state == LifeState.Dead)
                Texture = deadTexture;
        }
    }
}
