using Microsoft.Xna.Framework.Input;

namespace Dyehard
{
    public static class KeyboardDevice
    {
        private static KeyboardState current = Keyboard.GetState();
        private static KeyboardState previous = Keyboard.GetState();

        public static bool isKeyDown(Keys key)
        {
            return current.IsKeyDown(key);
        }

        public static bool IsKeyUp(Keys key)
        {
            return current.IsKeyUp(key);
        }

        public static bool isKeyTapped(Keys key)
        {
            if (previous.IsKeyUp(key) && current.IsKeyDown(key))
            {
                return true;
            }
            return false;
        }

        public static void update()
        {
            previous = current;
            current = Keyboard.GetState();
        }
    }
}