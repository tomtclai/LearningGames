using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using XNACS1Lib;
using Microsoft.Xna.Framework;


namespace Dyehard
{
    class Explosion : GameObject
    {
        private static float splashRadius = 2.5f;
        private static float duration = 1.5f;
        private ExplosionEmitter emitter;
        private XNACS1Circle splash;
        private Timer timer;


        public Explosion(Hero h, Enemy e)
        {
            splash = new XNACS1Circle(e.getPosition().Center, splashRadius);
            timer = new Timer(duration);
            splash.Color = h.getColor();
            splash.Visible = false;
            splash.RemoveFromAutoDrawSet();

            emitter = new ExplosionEmitter(e.getPosition().Center, duration, 0.6f, h.getColor());
            emitter.DrawHalo(20);
            emitter.AutoRemoveDeadParticles = true;
        }

        public override void remove()
        {
            throw new NotImplementedException();
        }

        public override void draw()
        {
            throw new NotImplementedException();
        }

        public override void update()
        {
            timer.update();
            emitter.TopOfAutoDrawSet();
        }



        public bool isDone()
        {
            return timer.isDone();
        }

        public void interactEnemy(List<Enemy> enemies){
            foreach (Enemy e in enemies)
            {
                if (e.getPosition().Collided(splash)){
                    e.gotShot(splash.Color);
                }
            }
        }
    }

    public class ExplosionEmitter : XNACS1ParticleEmitter
    {
        public ExplosionEmitter(Vector2 center, float particleLife, float particleSize, Color color)
            : base(center, (int) particleLife * XNACS1Base.World.TicksInASecond, particleSize, "particle", color)
        {
            AutoEmitEnabled = false;
        }

        public void DrawHalo(int numParticles)
        {
            for (int i = 0; i < numParticles; i++)
            {
                Emit();
            }
        }

        protected override void NewParticle(XNACS1Particle particle)
        {
            float theta = XNACS1Base.RandomFloat(0, (float)(2 * Math.PI));
            particle.CenterX = CenterX;
            particle.CenterY = CenterY;
            Vector2 randomVelocity = new Vector2(XNACS1Base.RandomFloat(-1, 1), XNACS1Base.RandomFloat(-1, 1));
            randomVelocity *= 0.2f;
            particle.Velocity = randomVelocity;
            particle.ShouldTravel = true;
        }

        protected override void UpdateParticle(XNACS1Particle particle)
        {
            int temp = (255 * particle.Life) / InitialLife;
            if (temp > 255)
            {
                temp = 255;
            }
            else if (temp < 0)
            {
                temp = 0;
            }
            Color tempColor = particle.TextureTintColor;
            tempColor.A = (byte)temp;
            particle.TextureTintColor = tempColor;
        }
    }
}
