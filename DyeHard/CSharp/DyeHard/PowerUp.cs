using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    delegate void DeactivateDelegate();

    abstract class PowerUp : GameObject
    {

        public const float Duration = 5f;

        private const float width = 5f;
        protected Hero hero;
        protected XNACS1Rectangle box;
        public PowerUp(Hero hero, float minX, float maxX)
        {
            this.hero = hero;
            float padding = hero.getPosition().Width * 2;
            float randomX = XNACS1Base.RandomFloat(minX + padding, maxX - padding);
            float randomY = XNACS1Base.RandomFloat(GameWorld.bottomEdge + padding, GameWorld.topEdge - padding);
            box = new XNACS1Rectangle(new Vector2(randomX, randomY), width, width * 0.39f);
        }

        public override void remove()
        {
            box.RemoveFromAutoDrawSet();
        }

        public void move()
        {
            box.CenterX -= GameWorld.Speed;
        }

        public override void update()
        {
            if (box.Collided(hero.getPosition()) && box.Visible)
            {
                hero.collect(this);
            }
        }

        public override void draw()
        {
            box.TopOfAutoDrawSet();
        }

        public virtual void activate()
        {
            box.Visible = false;
        }
        
        public static PowerUp randomPowerUp(Hero hero, float minX, float maxX)
        {
            switch (XNACS1Base.RandomInt(4))
            {
                case 0:
                    return new SpeedUp(hero, minX, maxX);
                case 1:
                    return new Ghost(hero, minX, maxX);
                case 2:
                    return new Invincibility(hero, minX, maxX);
                default:
                    return new Overload(hero, minX, maxX);
            }
        }
    }


    class SpeedUp : PowerUp
    {
        public static PowerUpMeter meter = new PowerUpMeter(0, Game.Green);

        public SpeedUp(Hero hero, float minX, float maxX)
            : base(hero, minX, maxX)
        {
            box.Texture = "PowerUp_Green";
        }

        public override void activate()
        {
            hero.increaseSpeed();
            meter.reset(Duration, hero.normalizeSpeed);
            base.activate();
        }

    }


    class Ghost : PowerUp
    {
        public static PowerUpMeter meter = new PowerUpMeter(1, Game.Blue);

        public Ghost(Hero hero, float minX, float maxX)
            : base(hero, minX, maxX)
        {
            box.Texture = "PowerUp_Blue";
        }

        public override void activate()
        {
            hero.setInvisible();
            meter.reset(Duration, hero.setVisible);
            base.activate();
        }
    }

    class Invincibility : PowerUp
    {
        public static PowerUpMeter meter = new PowerUpMeter(2, Game.Pink);

        public Invincibility(Hero hero, float minX, float maxX)
            : base(hero, minX, maxX)
        {
            box.Texture = "PowerUp_Pink";
        }

        public override void activate()
        {
            meter.reset(Duration, null);
            base.activate();
        }
    }


    class Overload : PowerUp
    {
        public static PowerUpMeter meter = new PowerUpMeter(3, Game.Red);

        public Overload(Hero hero, float minX, float maxX)
            : base(hero, minX, maxX)
        {
            box.Texture = "PowerUp_Red";
        }

        public override void activate()
        {
            meter.reset(Duration, null);
            base.activate();
        }
    }



    class PowerUpMeter
    {
        private XNACS1Rectangle box;
        private XNACS1Rectangle meter;
        private Timer timer;
        private float initialTime;
        private float initialMeterHeight;
        private DeactivateDelegate deactivate;

        public PowerUpMeter(int sequenceNumber, Color color)
        {
            initialTime = 0f;
            timer = new Timer(0);
            float padding = 1f;
            float height = GameWorld.panelSize;
            float width = height;
            float startingPoint = GameWorld.leftEdge + 25f;
            float offset = startingPoint + (sequenceNumber + 1) * (padding) + sequenceNumber * width + width / 2;

            box = new XNACS1Rectangle(new Vector2(offset, GameWorld.topEdge + (GameWorld.panelSize / 2)), width, height);
            box.Texture = "PowerUp_Box1";

            initialMeterHeight = height / 1.8f;
            meter = new XNACS1Rectangle(box.Center, width / 1.8f, initialMeterHeight);
            meter.Color = color;
            deactivate = null;
        }

        public void update()
        {
            if (timer.isDone())
            {
                meter.Visible = false;
                if (deactivate != null)
                {
                    deactivate();
                    deactivate = null;
                }
            }
            else
            {
                timer.update();
                meter.Visible = true;
                meter.Height = initialMeterHeight * Math.Max(0, timer.currentTime() / initialTime);
                meter.CenterY = box.CenterY - ((initialMeterHeight - meter.Height) / 2);
            }
        }

        public void reset(float time, DeactivateDelegate d)
        {
            initialTime = time;
            timer = new Timer(time);
            deactivate = d;
        }

        public void draw()
        {
            box.TopOfAutoDrawSet();
            meter.TopOfAutoDrawSet();
        }
    }
}
