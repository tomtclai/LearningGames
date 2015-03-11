using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class Gate : GameObject
    {
        private Color color;
        private XNACS1Rectangle path;
        private XNACS1Rectangle deathGate;
        private XNACS1Rectangle preview;
        private Hero hero;
        private List<Enemy> enemies;

        public Gate(int offset, Hero hero, List<Enemy> enemies, float leftEdge, Color color)
        {
            this.hero = hero;
            this.enemies = enemies;
            this.color = color;

            // set up pipe
            float position = (Stargate.width * 0.5f) + leftEdge;

            float drawHeight = GameWorld.topEdge / Stargate.GATE_COUNT;
            float drawOffset = drawHeight * (offset + 0.5f);
            
            this.path = new XNACS1Rectangle(new Vector2(position, drawOffset), Stargate.width, drawHeight - (Platform.height * 2));
            this.path.Color = new Color(color, 100);

            // gate is slightly set back from left edge to avoid killing when adjacent but not overlapping
            this.deathGate = new XNACS1Rectangle(new Vector2(leftEdge + 0.3f, path.CenterY), 0.5f, path.Height);
            this.deathGate.Color = Color.Maroon;
            deathGate.Visible = false;

            this.preview = new XNACS1Rectangle(new Vector2(GameWorld.rightEdge, drawOffset), 4f, 0f);
            this.preview.Color = this.path.Color;
            this.preview.Visible = false;
        }

        public override void remove()
        {
            path.RemoveFromAutoDrawSet();
            deathGate.RemoveFromAutoDrawSet();
            preview.RemoveFromAutoDrawSet();
        }

        public void move()
        {
            // called before update

            path.CenterX -= GameWorld.Speed;
            deathGate.CenterX -= GameWorld.Speed;
            preview.Visible = path.LowerLeft.X > (preview.LowerLeft.X + preview.Width) && (GameWorld.rightEdge + (Space.width * 0.7f)) > path.LowerLeft.X;
            if (preview.Visible)
            {
                preview.Height = ((path.Height + (Platform.height * 2)) * (1 - ((path.LowerLeft.X - (preview.LowerLeft.X + preview.Width)) / (Space.width * 0.7f))));
            }
        }

        public override void draw()
        {
            preview.TopOfAutoDrawSet();
            path.TopOfAutoDrawSet();
            deathGate.TopOfAutoDrawSet();
        }

        public override void update()
        {
            // kill the hero at the death wall
            if (hero.getColor() != color && deathGate.Collided(hero.getPosition()))
            {
                hero.kill();
            }

            // kill any enemies at the death wall
            foreach (Enemy e in enemies)
            {
                if (e.getColor() != color && deathGate.Collided(e.getPosition()))
                {
                    e.kill();
                }
            }

            // dye the hero
            if (contains(hero.getPosition()))
            {
                hero.setColor(color);
            }

            // dye any enemies
            foreach (Enemy e in enemies)
            {
                if (contains(e.getPosition()))
                {
                    e.setColor(color);
                }
            }
        }

        private bool contains(XNACS1Rectangle other)
        {
            float topEdge = path.MaxBound.Y;
            float bottomEdge = path.MinBound.Y;

            if (other.CenterY < topEdge && other.CenterY >= bottomEdge)
            {
                float leftEdge = path.LowerLeft.X;
                float rightEdge = leftEdge + path.Width;
                return other.CenterX < rightEdge && other.CenterX > leftEdge;
            }

            return false;
        }
    }
}
