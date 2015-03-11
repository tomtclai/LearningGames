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
    public class Trap : CustomRectangle
    {
        private const string defaultInactiveTexture = "state_changer_not_active";
        private const string defaultActiveTexture = "state_changer_locked";

        private const string leftInactiveTexture = "Trap/SideBumper_Deactivated_Left";
        private const string leftActiveTexture = "Trap/SideBumper_Ready_Left";

        private const string rightInactiveTexture = "Trap/SideBumper_Deactivated_Right";
        private const string rightActiveTexture = "Trap/SideBumper_Ready_Right";

        private const string leftActivatedIndicatorTexture = "Trap/Indicators/activated_indicator_left";
        private const string rightActivatedIndicatorTexture = "Trap/Indicators/activated_indicator_right";

        private const string leftDeactivatingIndicatorTexture = "Trap/Indicators/deactivating_indicator_left";
        private const string leftReactivatingIndicatorTexture = "Trap/Indicators/reactivating_indicator_left";

        private const string rightDeactivatingIndicatorTexture = "Trap/Indicators/deactivating_indicator_right";
        private const string rightReactivatingIndicatorTexture = "Trap/Indicators/reactivating_indicator_right";

        protected enum WallState
        {
            Active, Inactive, Activating, Deactivating, Dead
        }

        public enum ScreenSide
        { 
            Left, Right
        }

        protected string inactiveTexture;
        protected string activeTexture;
        protected string activeIndicatorTexture;
        protected string deactivatingIndicatorTexture;
        protected string reactivatingIndicatorTexture;
        protected XNACS1Rectangle indicator;


        // Indicator values
        protected float leftIndicatorXOffset = -2f;
        protected float rightIndicatorXOffset = 1.75f;
        protected float indicatorWidth = 1f;
        protected float indicatorHeight = 13f;

        protected WallState wallState;
        protected ScreenSide screenSide;
         public Trap()
            : base(Vector2.Zero, XNACS1Base.World.WorldDimension.X * 0.05f, XNACS1Base.World.WorldDimension.Y * 0.25f, defaultInactiveTexture)
        {
            initialize();
        }

        public Trap(float x, float y)
             : base(new Vector2(x, y), XNACS1Base.World.WorldDimension.X * 0.05f, XNACS1Base.World.WorldDimension.Y * 0.25f, defaultInactiveTexture)
        {
            initialize();
        }

        public Trap(float x, float y, float width, float height)
            : base(new Vector2(x, y), width, height, defaultInactiveTexture)
        {
            initialize();
        }

        private void initialize()
        {
            ShouldTravel = true;
            Velocity = VelocityDirection = Vector2.Zero;
            wallState = WallState.Inactive;
            inactiveTexture = defaultInactiveTexture;
            activeTexture = defaultActiveTexture;
            addtoGameobjects();

            // Set up the indicator, default left active indicator
            indicator = new XNACS1Rectangle(new Vector2(CenterX, CenterY),
                indicatorWidth, indicatorHeight);
            indicator.SetTextureSpriteSheet(leftDeactivatingIndicatorTexture, 24, 198);
            indicator.SetTextureSpriteAnimationFrames(
                13, 0,
                13, 0,
                3,
                SpriteSheetAnimationMode.AnimateForwardThenStop);
            indicator.UseSpriteSheet = true;
            indicator.UseSpriteSheetAnimation = true;

        }

        /// <summary>
        /// Sets the textures and sets up the indicator.
        /// </summary>
        /// <param name="side">The selection of textures for the side of the screen.</param>
        public void setTrapTextures(ScreenSide side)
        {
            screenSide = side;

            if (side == ScreenSide.Left)
            {
                // Set the bumper textures
                inactiveTexture = leftInactiveTexture;
                activeTexture = leftActiveTexture;

                // set the indicator textures
                activeIndicatorTexture = leftActivatedIndicatorTexture;
                deactivatingIndicatorTexture = leftDeactivatingIndicatorTexture;
                reactivatingIndicatorTexture = leftReactivatingIndicatorTexture;

                // set indicator location.
                indicator.CenterX = this.CenterX + leftIndicatorXOffset;
            }
            else
            {
                // Set the bumper textures
                inactiveTexture = rightInactiveTexture;
                activeTexture = rightActiveTexture;

                // set the indicator textures
                activeIndicatorTexture = rightActivatedIndicatorTexture;
                deactivatingIndicatorTexture = rightDeactivatingIndicatorTexture;
                reactivatingIndicatorTexture = rightReactivatingIndicatorTexture;

                // set indicator location.
                indicator.CenterX = this.CenterX + rightIndicatorXOffset;
            }

            Texture = inactiveTexture;

            indicator.SetTextureSpriteSheet(deactivatingIndicatorTexture, 24, 198);
            indicator.SetTextureSpriteAnimationFrames(
                13, 0,
                13, 0,
                3,
                SpriteSheetAnimationMode.AnimateForwardThenStop);
            indicator.CenterY = this.CenterY;
        }

        public void update()
        {
            if (wallState == WallState.Activating)
            {
                if (indicator.CurrentSpriteSheetFrameX == 29 &&
                    indicator.CurrentSpriteSheetFrameY == 1)
                {
                    // Bumper
                    Texture = activeTexture;
                    wallState = WallState.Active;

                    // Indicator
                    indicator.SetTextureSpriteSheet(activeIndicatorTexture, 24, 198);
                    indicator.SetTextureSpriteAnimationFrames(
                        0, 0,
                        17, 1,
                        3,
                        SpriteSheetAnimationMode.AnimateForward);
                }
            }
            else if (wallState == WallState.Deactivating)
            {
                if (indicator.CurrentSpriteSheetFrameX == 13 &&
                        indicator.CurrentSpriteSheetFrameY == 0)
                {
                    Texture = inactiveTexture;
                    wallState = WallState.Inactive;
                }
            }
        }

        protected virtual void removeFromGameObjects()
        {
            if (GameObjects.trapList != null)
                GameObjects.trapList.Remove(this);
        }

        protected virtual void addtoGameobjects()
        {
            if (GameObjects.trapList == null)
                GameObjects.trapList = new List<Trap>();

            GameObjects.trapList.Add(this);
        }

        public void remove()
        {
            wallState = WallState.Dead;
            Visible = false;
            RemoveFromAutoDrawSet();
            Velocity = Vector2.Zero;

            indicator.Visible = false;
            indicator.RemoveFromAutoDrawSet();
            indicator.Velocity = Vector2.Zero;
        }

        internal void destroy()
        {
            remove();
            removeFromGameObjects();
        }

        public void activate()
        {
            if(wallState == WallState.Inactive)
            {
                indicator.SetTextureSpriteSheet(reactivatingIndicatorTexture, 24, 198);
                indicator.SetTextureSpriteAnimationFrames(
                    0, 0,
                    29, 1,
                    3,
                    SpriteSheetAnimationMode.AnimateForwardThenStop);

                wallState = WallState.Activating;
            }
        }

        public void deActivate()
        {
            if (wallState == WallState.Active)
            {
                indicator.SetTextureSpriteSheet(deactivatingIndicatorTexture, 24, 198);
                indicator.SetTextureSpriteAnimationFrames(
                    0, 0,
                    13, 0,
                    3,
                    SpriteSheetAnimationMode.AnimateForwardThenStop);

                wallState = WallState.Deactivating;
            }
        }

        public bool isDead()
        {
            return wallState == WallState.Dead;
        }

        public bool isActive()
        {
            return wallState == WallState.Active;
        }

        public bool isInactive()
        {
            return wallState == WallState.Inactive;
        }

        public void reflect(Ball ball)
        {
            if (ball != null && wallState != WallState.Dead)
            {
               pushOutCircle(ball);
               reflectionVelocityResolution(ball);
            }
        }

        public string getNormalTexture()
        {
            return inactiveTexture;
        }

        public void setInativeTexture(string value)
        {
            inactiveTexture = value;

            if (wallState == WallState.Inactive)
                Texture = inactiveTexture;
        }

        public string getActiveTexture()
        {
            return activeTexture;
        }

        public void setActiveTexture(string value)
        {
            activeTexture = value;

            if (wallState == WallState.Active)
                Texture = activeTexture;
        }
    }
}
