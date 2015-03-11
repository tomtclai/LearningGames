using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class ScoreTracker : GameObject
    {
        private Hero hero;
        private Vector2 startPoint;
        private float accumulatedDistance;
        private float factor;
        private XNACS1Rectangle scoreBox;

        public ScoreTracker(Hero hero)
        {
            this.hero = hero;
            startPoint = hero.getPosition().Center;
            factor = hero.getPosition().Width;
            accumulatedDistance = 0.0f;

            float height = GameWorld.panelSize - 1;
            float width = 20f;
            Vector2 position = new Vector2((GameWorld.rightEdge / 2) + width, GameWorld.topEdge + (GameWorld.panelSize / 2));            
            scoreBox = new XNACS1Rectangle(position, width, height);
            scoreBox.Color = Color.Transparent;
            scoreBox.LabelColor = Color.White;
        }

        public override void remove()
        {
            scoreBox.RemoveFromAutoDrawSet();
        }

        public override void update()
        {
            // update distance
            accumulatedDistance += GameWorld.Speed;
            float heroOffset = hero.getPosition().CenterX - startPoint.X;

            float dyepackBonus = 50f * hero.dyepacksCollected();
            float powerupBonus = 50f * hero.powerupsCollected();

            float score = ((accumulatedDistance + heroOffset) / factor) + dyepackBonus + powerupBonus;

            // update textbox
            scoreBox.Label = (score).ToString("00000000.0");
        }

        public override void draw()
        {
            scoreBox.TopOfAutoDrawSet();
        }
    }
}
