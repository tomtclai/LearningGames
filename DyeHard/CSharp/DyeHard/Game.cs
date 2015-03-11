using System;
using System.Linq;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Storage;
using Microsoft.Xna.Framework.GamerServices;
using XNACS1Lib;

namespace Dyehard
{
    public class Game : XNACS1Base
    {
        private static bool FULLSCREEN = true;

        // Dyehard Dye Colors
        public static int colorCount = 6;
        public static Color Green = new Color(38, 153, 70);
        public static Color Red = new Color(193, 24, 30);
        public static Color Yellow = new Color(228, 225, 21);
        public static Color Teal = new Color(90, 184, 186);
        public static Color Pink = new Color(215, 59, 148);
        public static Color Blue = new Color(50, 75, 150);

        private enum State
        {
            BEGIN,
            PAUSED,
            PLAYING,
            GAMEOVER
        };

        // screen objects
        private Background background;
        private Screen startScreen;
        private Window pauseScreen;
        private Window gameOverScreen;

        // game objects
        private GameWorld world;

        // game state
        private State state;
        
        // constructor
        public Game() {
        }

        // Initialize the game world
        protected override void InitializeWorld()
        {
            //SetAppWindowPixelDimension(FULLSCREEN, 1920, 1080);
            World.SetWorldCoordinate(new Vector2(0f, 0f), 100);
            preloadTexturedObjects();
            loadControllerObjects();

            state = State.BEGIN;
            world = new GameWorld();
        }

        protected override void UpdateWorld()
        {
            checkControl();
            background.update();
            
            switch (state)
            {
                case State.BEGIN:
                    startScreen.draw();

                    break;

                case State.PAUSED:
                    world.draw();
                    pauseScreen.draw();
                    break;

                case State.PLAYING:
                    world.update();
                    world.draw();

                    break;

                case State.GAMEOVER:
                    world.draw();
                    gameOverScreen.draw();
                    break;
            }
        }

        private void checkControl()
        {
            KeyboardDevice.update();

            if (KeyboardDevice.isKeyDown(Keys.Escape))
            {
                Exit();
            }

            switch (state)
            {
                case State.BEGIN:
                    if (KeyboardDevice.isKeyTapped(Keys.A))
                    {
                        state = State.PLAYING;
                        startScreen.remove();
                    }
                    break;

                case State.PAUSED:
                    if (KeyboardDevice.isKeyTapped(Keys.A))
                    {
                        state = State.PLAYING;
                        pauseScreen.remove();
                    }
                    if (KeyboardDevice.isKeyTapped(Keys.Q))
                    {
                        state = State.BEGIN;
                        world.remove();
                        world = new GameWorld();
                        pauseScreen.remove();
                    }
                    break;

                case State.PLAYING:
                    if (KeyboardDevice.isKeyTapped(Keys.A))
                    {
                        state = State.PAUSED;
                    }
                    else if (world.gameOver())
                    {
                        state = State.GAMEOVER;
                    }
                    break;

                case State.GAMEOVER:
                    if (KeyboardDevice.isKeyTapped(Keys.A))
                    {
                        state = State.BEGIN;
                        world.remove();
                        world = new GameWorld();
                        gameOverScreen.remove();
                    }
                    break;
            }
        }

        public static List<Color> randomColorSet(int count)
        {
            // get a random and unique subset of the available colors

            List<int> range = Enumerable.Range(0, colorCount).ToList();
            List<int> sample = new List<int>();

            // set up the indexes in the sample list
            for (int i = 0; i < count; i++)
            {
                int choice = XNACS1Base.RandomInt(range.Count);
                sample.Add(range.ElementAt(choice));
                range.RemoveAt(choice);
            }

            // get the colors from the indexes in the sample list
            List<Color> colors = new List<Color>();
            foreach (int i in sample)
            {
                colors.Add(colorPicker(i));
            }

            return colors;
        }

        public static Color randomColor()
        {
            // get a single random color
            return colorPicker(XNACS1Base.RandomInt(6));
        }

        private static Color colorPicker(int choice)
        {
            switch (choice)
            {
                case 0: return Green;
                case 1: return Red;
                case 2: return Yellow;
                case 3: return Teal;
                case 4: return Pink;
                case 5: return Blue;
            }
            return Color.Black;
        }

        // game text
        private const string startText = "Press 'A' to begin.";
        private const string pauseText = "Paused.\nResume game:    'A'\nRestart game:     'Q'";
        private const string deathText = "YOU HAVE DIED...\n\n'A' to continue.";
        private const string controls = "\n\nControls:\n" +
                                        "Move:        arrow keys\n" +
                                        "Fire dye gun:          'F'\n" +
                                        "Pause game:          'A'\n" +
                                        "Quit game:          'ESC'\n" +
                                        "Stop the world:       'W'";


        // preload any game objects that have textures
        private static void preloadTexturedObjects()
        {
            // dont save references to any preloaded objects
            Hero preloadHero = new Hero();
            preloadHero.remove();

            Enemy preloadEnemy = new BrainEnemy(new Vector2(-100f, -100f), 0, preloadHero);
            preloadEnemy.remove();

            (new RedBeamEnemy(new Vector2(-100f, -100f), 0, preloadHero)).remove();
            (new SpiderEnemy(new Vector2(-100f, -100f), 0, preloadHero)).remove();

            (new DyePack(preloadHero, -200f, -100f, Blue)).remove();
            (new DyePack(preloadHero, -200f, -100f, Green)).remove();
            (new DyePack(preloadHero, -200f, -100f, Yellow)).remove();
            (new DyePack(preloadHero, -200f, -100f, Red)).remove();
            (new DyePack(preloadHero, -200f, -100f, Pink)).remove();
            (new DyePack(preloadHero, -200f, -100f, Teal)).remove();
            
            (new SpeedUp(preloadHero, -200f, -100f)).remove();
            (new Ghost(preloadHero, -200f, -100f)).remove();
            (new Overload(preloadHero, -200f, -100f)).remove();
            (new Invincibility(preloadHero, -200f, -100f)).remove();

            new Explosion(preloadHero, preloadEnemy);
        }

        private void loadControllerObjects()
        {
            background = new Background();
            startScreen = new Screen(startText + controls);
            pauseScreen = new Window(pauseText + controls);
            gameOverScreen = new Window(deathText);
        }

    }
}
