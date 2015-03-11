using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using XNACS1Lib;
using SpaceSmasherLib.Entities;

namespace SpaceSmasherLib.Entities
{
    /// <summary>
    /// Holds the items that are only decoration within the game like background
    /// and foreground items. The foreground is only in the front for all existing
    /// objects in the drawset after when initiailze foreground.
    /// </summary>
    public class Decorations
    {
        private const float TRACK_HEIGHT_SCREEN_RATIO = 0.95f;
        private const float TRACK_WIDTH = 5f;
        private const float TRACK_CENTER_HEIGHT_SCREEN_RATIO = 0.445f;
        private const float TRACK_OFFSET_FROM_SIDE_SCREEN_RATIO = .02f;
        private const float TRACK_RIGHT_Y_POS_OFFSET = -.5f; // an additional offset for the right track.

        private const string defaultBackgroundTexture = "Decorations/Bg_StaticGuide";
        private const string defaultBoundingBoxTexture = "Decorations/BG_BoundingBox";
        private const string defaultLeftRailTexture = "Decorations/SideBumper_Track";
        private const string defaultRightRailTexture = "Decorations/BumperTrack_Right";
        private const string defaultJointTexture = "Decorations/SideBumper_RotatingJoint";
        private const string defaultBarrierTexture = "Decorations/barrier_full";

        private string backgroundTexture;

        private List<XNACS1Rectangle> foregroundItems;

        public virtual void initializeBackground()
        {
            backgroundTexture = defaultBackgroundTexture;
            XNACS1Lib.XNACS1Base.World.SetBackgroundTexture(backgroundTexture);
        }

        public virtual void initializeForeground()
        {
            foregroundItems = new List<XNACS1Rectangle>();

            float worldWidth = XNACS1Lib.XNACS1Base.World.WorldDimension.X;
            float worldHeight = XNACS1Lib.XNACS1Base.World.WorldDimension.Y;

            #region Side rails
            XNACS1Rectangle leftRail = new XNACS1Rectangle(
                    new Vector2(worldWidth * TRACK_OFFSET_FROM_SIDE_SCREEN_RATIO,
                        worldHeight * TRACK_CENTER_HEIGHT_SCREEN_RATIO),
                    TRACK_WIDTH,
                    worldHeight * TRACK_HEIGHT_SCREEN_RATIO);
            leftRail.SetTextureSpriteSheet(
                defaultLeftRailTexture,
                76,
                1024);
            leftRail.SetTextureSpriteAnimationFrames(
                0, 0,
                5, 0,
                5,
                SpriteSheetAnimationMode.AnimateForward);

            leftRail.UseSpriteSheet = true;
            leftRail.UseSpriteSheetAnimation = true;
            foregroundItems.Add(leftRail);

            XNACS1Rectangle rightRail = new XNACS1Rectangle(
                new Vector2(worldWidth * (1 - TRACK_OFFSET_FROM_SIDE_SCREEN_RATIO),
                    worldHeight * TRACK_CENTER_HEIGHT_SCREEN_RATIO + TRACK_RIGHT_Y_POS_OFFSET),
                TRACK_WIDTH,
                worldHeight * TRACK_HEIGHT_SCREEN_RATIO);
            rightRail.SetTextureSpriteSheet(
                defaultRightRailTexture,
                76,
                1024);
            rightRail.SetTextureSpriteAnimationFrames(
                0, 0,
                5, 0,
                5,
                SpriteSheetAnimationMode.AnimateForward);

            rightRail.UseSpriteSheet = true;
            rightRail.UseSpriteSheetAnimation = true;
            foregroundItems.Add(rightRail);
            #endregion rails

            XNACS1Rectangle bottomBarrier = new XNACS1Rectangle(
                new Vector2(worldWidth / 2, worldHeight * 0.045f),
                worldWidth * 0.95f,
                worldHeight * 0.09f);

            bottomBarrier.SetTextureSpriteSheet(
                defaultBarrierTexture,
                1248,
                70);

            bottomBarrier.SetTextureSpriteAnimationFrames(
                0, 0,
                1, 35,
                3,
                SpriteSheetAnimationMode.AnimateForward);

            bottomBarrier.UseSpriteSheet = true;
            bottomBarrier.UseSpriteSheetAnimation = true;
            foregroundItems.Add(bottomBarrier);

            XNACS1Rectangle boundingBox = new XNACS1Rectangle(
                    new Vector2(worldWidth / 2, worldHeight / 2),
                    worldWidth,
                    worldHeight,
                    defaultBoundingBoxTexture);
            foregroundItems.Add(boundingBox);

            // Bottom Joints
            addJoint(new Vector2(worldWidth * .015f, worldHeight * .025f));
            addJoint(new Vector2(worldWidth * .98f, worldHeight * .025f));

            // Top Joints
            addJoint(new Vector2(worldWidth * .015f, worldHeight * .86f));
            addJoint(new Vector2(worldWidth * .98f, worldHeight * .86f));
        }
        /// <summary>
        /// Adds a joint a location.
        /// </summary>
        private void addJoint(Vector2 location)
        {
            XNACS1Rectangle joint = new XNACS1Rectangle(
                location,
                XNACS1Lib.XNACS1Base.World.WorldDimension.Y * .04f,
                XNACS1Lib.XNACS1Base.World.WorldDimension.Y * .04f);

            joint.SetTextureSpriteSheet(defaultJointTexture, 36, 36);

            joint.SetTextureSpriteAnimationFrames(
                0, 0,
                5, 5,
                5,
                SpriteSheetAnimationMode.AnimateForward);

            joint.UseSpriteSheet = true;
            joint.UseSpriteSheetAnimation = true;

            foregroundItems.Add(joint);
        }

        protected string getBackgroundTexture()
        {
            return backgroundTexture;
        }

        protected void setBackgroundTexture(string value)
        {
            backgroundTexture = value;
            XNACS1Lib.XNACS1Base.World.SetBackgroundTexture(backgroundTexture);
        }

        public void destroy()
        {
            foreach (XNACS1Rectangle rect in foregroundItems)
            {
                rect.RemoveFromAutoDrawSet();
            }
            foregroundItems.Clear();
            foregroundItems = null;
        }
    }
}
