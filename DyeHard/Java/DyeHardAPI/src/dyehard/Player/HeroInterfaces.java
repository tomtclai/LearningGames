package dyehard.Player;

import Engine.Primitive;
import dyehard.Collidable;

public class HeroInterfaces {
    public interface HeroCollision {
        public abstract void collideWithHero(Hero hero, Collidable other);
    }

    public interface HeroDamage {
        public void damageHero(Hero hero, Primitive who);
    }
}
