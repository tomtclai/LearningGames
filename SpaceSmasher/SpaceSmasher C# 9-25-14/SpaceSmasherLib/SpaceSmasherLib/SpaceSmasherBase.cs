using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;
using XNACS1Lib;
using SpaceSmasherLib.Entities;

namespace SpaceSmasherLib
{
    public abstract class SpaceSmasherBase : XNACS1Base
    {
        private const string defaultBackgroundMusic = "DigitalVortexTurnedDown";
        private const int defaultTotalLife = 3;
        
        protected KeyBoardDevice keyboard;
        protected BallSet ballSet;
        protected BlockSet blockSet;
        protected LifeSet lifeSet;
        protected TrapSet trapSet;
        protected PaddleSet paddleSet;
        protected Switch theSwitch;
        protected Decorations allDecorations;
        protected int totalLife;
        protected bool fullScreen;
        private string backgroundMusic;
        private int score = 0;

        private float gameVolume;

        private bool allowDebug = true;

        private Vector2 debugVelocitySaved = new Vector2();

        // StartScreen Variables
        private bool useStartScreen = false;
        private StartScreen startScreen;
        public bool UseStartScreen { get { return useStartScreen; } set { useStartScreen = value; } }

        private EndScreen endScreen;

        private enum GameState
        {
            Normal, StartScreen, Paused, Win, Gameover
        }

        private GameState state;

        protected override void InitializeWorld()
        {
            // These lines need to run before the code below in
            // order for the window and world size to be set to
            // the correct value.
            fullScreen = false;
            preInit();
            setUpWorld();
            keyboard = new KeyBoardDevice();

            if (useStartScreen == true)
            {
                startScreen = new StartScreen(this);
                state = GameState.StartScreen;
            }
            else
            {
                initializeGame();
            }
        }
        /// <summary>
        /// This is called before the neccessary initializations
        /// to manipulate content in the game.
        /// 
        /// One use is to call activate startscreen to display a
        /// startscreen before the game begins. (UseStartScreen)
        /// </summary>
        protected virtual void preInit() { }

        protected void initializeGame()
        {
            // initialize background music
            backgroundMusic = defaultBackgroundMusic;
            gameVolume = 1f;
            PlayBackgroundAudio(backgroundMusic, gameVolume);
            SetTopEchoColor(Color.White);

            theSwitch = new Switch();
            ballSet = new BallSet();
            blockSet = new BlockSet();
            lifeSet = new LifeSet();
            trapSet = new TrapSet();
            paddleSet = new PaddleSet();
            allDecorations = new Decorations();
            allDecorations.initializeBackground();
            GameObjects.debugMode = false;
            GameObjects.score = 0;
            GameObjects.displayScore();

            totalLife = defaultTotalLife;
            state = GameState.Normal;

            paddleSet.initialize();
            ballSet.initialize();
            trapSet.initialize();
            blockSet.initialize();
            lifeSet.initialize();

            allDecorations.initializeForeground();
            initialize();
        }

        protected abstract void initialize();

        protected override void UpdateWorld()
        {
            keyboard.update();
            MouseDevice.Update();
            

            if (state == GameState.Normal)
            {
                GameObjects.update();
                update();

                if (keyboard.isKeyTapped(Keys.Escape))
                {
                    state = GameState.Paused;
                    GameObjects.pause();
                    PlayBackgroundAudio(backgroundMusic, 0);
                }

                if (keyboard.isKeyTapped(Keys.F1))
                {
                    setDebugMode(!getDebugMode());
                }
            }
            else if (state == GameState.Gameover || state == GameState.Win)
            {
                if (endScreen.readyToReplay())
                {
                    GameObjects.destroy();
                    allDecorations.destroy();
                    InitializeWorld();
                }
                else
                {
                    endScreen.update();
                }
            }
            else if (state == GameState.Paused)
            {
                if (keyboard.isKeyTapped(Keys.Escape))
                {
                    state = GameState.Normal;
                    GameObjects.resume();
                    PlayBackgroundAudio(backgroundMusic, gameVolume);
                }
            }
            else if (state == GameState.StartScreen)
            {
                if (startScreen.isReady())
                {
                    initializeGame();
                }
                else
                {
                    startScreen.update();
                }
            }
        }

        protected abstract void update();

        protected void gameLost()
        {
            state = GameState.Gameover;
            endScreen = new EndScreen(this, EndScreen.EndState.GAMEOVER);
        }

        protected void gameWin()
        {
            state = GameState.Win;
            endScreen = new EndScreen(this, EndScreen.EndState.WIN);
        }

        public new void SetAppWindowPixelDimension(bool fullScreen, int widthInPixel, int heightInPixel)
        {
            XNACS1Base.SetAppWindowPixelDimension(fullScreen, widthInPixel, heightInPixel);

            MouseDevice.setWindowWidth(GraphicsDeviceManager.DefaultBackBufferWidth);
            MouseDevice.setWindowHeight(GraphicsDeviceManager.DefaultBackBufferHeight);
        }

        private void setUpWorld()
        {
            // initialize world
            SetAppWindowPixelDimension(fullScreen, GraphicsDeviceManager.DefaultBackBufferWidth, GraphicsDeviceManager.DefaultBackBufferHeight);
            World.SetWorldCoordinate(new Vector2(0f, 0f), 100f);
        }

        protected string getBackgroundMusic()
        {
            return backgroundMusic;
        }

        protected void setBackgroundMusic(string value)
        {
            backgroundMusic = value;

            if (state == GameState.Normal)
                PlayBackgroundAudio(value, gameVolume);
        }

        protected float getGameVolume()
        {
            return gameVolume;
        }

        protected void setGameVolume(float value)
        {
            gameVolume = value;

            if (state == GameState.Normal)
                PlayBackgroundAudio(backgroundMusic, value);
        }

        protected bool getDebugMode()
        {
            return GameObjects.debugMode;
        }

        public void setAllowDebug(bool value)
        {
            if (!value)
            {
                setDebugMode(false);
            }

            allowDebug = value;
        }

        protected void setDebugMode(bool value)
        {
            if (allowDebug)
            {
                if (value != GameObjects.debugMode)
                {
                    if (GameObjects.blockList != null)
                    {
                        foreach (Block block in GameObjects.blockList)
                            block.setDebugMode(GameObjects.debugMode);
                    }

                    if (value)
                    {
                        debugVelocitySaved = GameObjects.ballList[0].Velocity;
                        GameObjects.ballList[0].VelocityX = 0.0f;
                        GameObjects.ballList[0].VelocityY = 0.0f;
                    }
                    else
                    {
                        GameObjects.ballList[0].Velocity = debugVelocitySaved;
                    }
                }

                GameObjects.debugMode = value;
            }
        }
    }
}
