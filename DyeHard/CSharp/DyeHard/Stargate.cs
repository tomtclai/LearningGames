using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;


namespace Dyehard
{
    class Stargate : GameWorldRegion
    {
        public static float width = GameWorld.rightEdge * 2.0f;
        public const int GATE_COUNT = 4;
        private Gate[] gates;
        private Platform[] platforms;
        private XNACS1Rectangle backdrop;
        
        public Stargate(Hero hero, List<Enemy> enemies, float leftEdge) : base()
        {

            List<Color> colors = Game.randomColorSet(GATE_COUNT);
            this.gates = new Gate[GATE_COUNT];
            for (int i = 0; i < this.gates.Length; i++)
            {
                this.gates[i] = new Gate(i, hero, enemies, leftEdge, colors[i]);
            }

            this.platforms = new Platform[GATE_COUNT + 1];
            for (int i = 0; i < this.platforms.Length; i++) {
                bool boundary = (i == 0) || (i == this.platforms.Length - 1);
                this.platforms[i] = new Platform(i, hero, enemies, leftEdge, boundary);
            }

            float height = GameWorld.topEdge;
            float Xposition = (width * 0.5f) + leftEdge;
            this.backdrop = new XNACS1Rectangle(new Vector2(Xposition, height/2), width, height);
            this.backdrop.Color = new Color(Color.Black, 130);
        }

        public override void remove()
        {
            backdrop.RemoveFromAutoDrawSet();

            foreach (Gate gate in gates)
            {
                gate.remove();
            }

            foreach (Platform platform in platforms)
            {
                platform.remove();
            }

        }

        public override void move()
        {
            backdrop.CenterX -= GameWorld.Speed;

            foreach (Gate g in gates)
            {
                g.move();
            }

            foreach (Platform p in platforms) {
                p.move();
            }
        }

        public override void draw()
        {
            backdrop.TopOfAutoDrawSet();

            foreach (Gate g in gates)
            {
                g.draw();
            }

            foreach (Platform p in platforms) {
                p.draw();
            }
        }

        public override void update()
        {
            foreach (Gate g in gates)
            {
                g.update();
            }

            foreach (Platform p in platforms) {
                p.update();
            }
        }

        public override bool isOffScreen()
        {
            return backdrop.MaxBound.X <= GameWorld.leftEdge;
        }

        public override float rightEdge()
        {
            return backdrop.MaxBound.X;
        }
    }
}
