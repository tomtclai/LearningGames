using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework.Input;
using XNACS1Lib;

namespace SpaceSmasherLib.Entities
{
    public static class MouseDevice
    {
        private static MouseState current = Mouse.GetState();
        private static MouseState previous = Mouse.GetState();
        private static int windowWidth = 1;
        private static int windowHeight = 1;

        public static void setWindowWidth(int value)
        {
            windowWidth = value;
        }

        public static void setWindowHeight(int value)
        {
            windowHeight = value;
        }

        public static bool IsLeftButtonDown()
        {
            return current.LeftButton == ButtonState.Pressed;
        }

        public static bool IsRightButtonDown()
        {
            return current.RightButton == ButtonState.Pressed;
        }

        public static bool IsLeftButtonUp()
        {
            return current.LeftButton == ButtonState.Released;
        }

        public static bool IsRightButtonUp()
        {
            return current.RightButton == ButtonState.Released;
        }

        public static bool IsLeftButtonDown(MouseState mouse)
        {
            return mouse.LeftButton == ButtonState.Pressed;
        }

        public static bool IsRightButtonDown(MouseState mouse)
        {
            return mouse.RightButton == ButtonState.Pressed;
        }

        public static bool IsLeftButtonUp(MouseState mouse)
        {
            return mouse.LeftButton == ButtonState.Released;
        }
        
        public static bool IsRightButtonUp(MouseState mouse)
        {
            return mouse.RightButton == ButtonState.Released;
        }

        public static bool IsLeftTapped()
        {
            if (IsLeftButtonUp(previous) && IsLeftButtonDown(current))
                return true;
            return false;
        }

        public static bool IsRightTapped()
        {
            if (IsRightButtonUp(previous) && IsRightButtonDown(current))
                return true;
            return false;
        }

        public static float getMouseX()
        {
            float toReturn = current.X;

            // Convert to world coordinates
            toReturn = XNACS1Base.World.WorldMax.X *
                (current.X / (float)windowWidth);

            return toReturn;
        }

        public static float getMouseY()
        {
            float toReturn = current.Y;

            // Convert to world coordinates
            // Flip the axis
            toReturn = XNACS1Base.World.WorldMax.Y *
                (1.0f - ((current.Y + 1) / (float)windowHeight));

            return toReturn;
        }

        public static bool MouseOnScreen()
        {
            if (current.X > 0 && current.X < windowWidth &&
                    current.Y > 0 && current.Y < windowHeight)
                return true;
            return false;
        }

        public static void Update()
        {
            previous = current;
            current = Mouse.GetState();
        }
    
    }
}
