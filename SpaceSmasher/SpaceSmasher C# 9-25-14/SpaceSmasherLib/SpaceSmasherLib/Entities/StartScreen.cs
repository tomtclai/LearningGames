using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using XNACS1Lib;

namespace SpaceSmasherLib.Entities
{
    public class StartScreen
    {
        protected enum StartScreenState
        {
            START, CREDITS, TRANS_INTO_GAME, TRANS_INTO_START, TRANS_INTO_CREDITS, READY_FOR_GAME
        };

        StartScreenState state;

        protected const string backgroundTexture = "StartScreen/StartScreenBG";

	    private const string START_BUTTON_PRESSED = "StartScreen/StartButton_Press";
	    private const string START_BUTTON_HOVER = "StartScreen/StartButton_Hover";
	    private const string START_BUTTON_STATIC = "StartScreen/StartButton_Static";

        XNACS1Rectangle startButton;

        SpaceSmasherBase gameBase;

        public StartScreen(SpaceSmasherBase game)
        {
            gameBase = game;
            gameBase.IsMouseVisible = true;

            XNACS1Lib.XNACS1Base.World.SetBackgroundTexture(backgroundTexture);

            state = StartScreenState.START;

            startButton = new XNACS1Rectangle(
                new Vector2(
                    XNACS1Lib.XNACS1Base.World.WorldDimension.X * 0.4f,
                    XNACS1Lib.XNACS1Base.World.WorldDimension.Y * 0.4f),
                14f,
                6f,
                START_BUTTON_STATIC
                );
        }

        /// <summary>
        /// If true, the player has selected to play the game.
        /// </summary>
        /// <returns>If the game should start.</returns>
        public bool isReady()
        {
            return state == StartScreenState.READY_FOR_GAME;
        }

        public void update()
        {
            switch (state)
            {
                case StartScreenState.START:
                    startScreenButtons();
                    break;
                case StartScreenState.TRANS_INTO_GAME:
                    endStartScreen();
                    break;
            }
        }
        private void startScreenButtons()
        {
            // Update start button
            buttonStateChanger(startButton,
                    START_BUTTON_STATIC,
                    START_BUTTON_HOVER,
                    START_BUTTON_PRESSED,
                    StartScreenState.TRANS_INTO_GAME);
        }

        private void endStartScreen()
        {
            startButton.RemoveFromAutoDrawSet();
            startButton = null;
            gameBase.IsMouseVisible = false;
            state = StartScreenState.READY_FOR_GAME;
        }

        private void buttonStateChanger(XNACS1Rectangle button,
        String staticFilename, String hoverFilename,
        String pressedFilename, StartScreenState exitState)
        {
            // Inefficent, optimize later.
            Vector2 mousePos = new Vector2(MouseDevice.getMouseX(), MouseDevice.getMouseY());

            // Check Start Button
            if (button.Contains(mousePos))
            {
                button.Texture = hoverFilename;
                if (MouseDevice.IsLeftButtonDown())
                {
                    button.Texture = pressedFilename;
                    state = exitState;
                }
            }
            else
            {
                button.Texture = staticFilename;
            }
        }


    }
}
