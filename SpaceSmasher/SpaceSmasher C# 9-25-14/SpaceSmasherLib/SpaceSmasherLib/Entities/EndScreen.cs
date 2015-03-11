using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using XNACS1Lib;

namespace SpaceSmasherLib.Entities
{
    public class EndScreen
    {
        private const String GAMEOVER_EXIT_HOVER_IMAGE = "EndScreen/GameOver_PopUp_ExitHover";
        private const String GAMEOVER_REPLAY_HOVER_IMAGE = "EndScreen/GameOver_PopUp_ReplayHover";
        private const String GAMEOVER_STATIC_IMAGE = "EndScreen/GameOver_PopUp_Static";
        private const String GAMEOVER_BLUR_IMAGE = "EndScreen/GameOverBlur";
  
        private const String GAMEWIN_EXIT_HOVER_IMAGE = "EndScreen/YouWin_PopUp_Exit_Hover";
        private const String GAMEWIN_REPLAY_HOVER_IMAGE = "EndScreen/YouWin_PopUp_Replay_Hover";
        private const String GAMEWIN_STATIC_IMAGE = "EndScreen/YouWin_PopUp_Static"; 

        public enum EndState
        {
            GAMEOVER, WIN
        }
        EndState state;

        XNACS1Rectangle afterGameForeground1;
        XNACS1Rectangle afterGameBackground;

        private bool replay = false;

        SpaceSmasherBase gameBase;
        public EndScreen(SpaceSmasherBase game, EndState gameState)
        {
            gameBase = game;
            state = gameState;
            gameBase.IsMouseVisible = true;

            float worldWidth = XNACS1Lib.XNACS1Base.World.WorldDimension.X;
            float worldHeight = XNACS1Lib.XNACS1Base.World.WorldDimension.Y;

            afterGameBackground = new XNACS1Rectangle(
                new Vector2(worldWidth / 2, worldHeight / 2),
                worldWidth,
                worldHeight,
                GAMEOVER_BLUR_IMAGE);

            if (state == EndState.GAMEOVER)
            {
                afterGameForeground1 = new XNACS1Rectangle(
                    new Vector2(worldWidth / 2, worldHeight / 2),
                    40f,
                    35f,
                    GAMEOVER_STATIC_IMAGE);
            }
            else
            {
                afterGameForeground1 = new XNACS1Rectangle(
                    new Vector2(worldWidth / 2, worldHeight / 2),
                    40f,
                    35f,
                    GAMEWIN_STATIC_IMAGE);
            }
            
        }

        public bool readyToReplay()
        {
            return replay;
        }

        public void update()
        { 
            if(state == EndState.GAMEOVER)
            {
    	        afterGameState(GAMEOVER_STATIC_IMAGE, GAMEOVER_EXIT_HOVER_IMAGE,
    			        GAMEOVER_REPLAY_HOVER_IMAGE);
            }
            else if( state == EndState.WIN)
            {
    	        afterGameState(GAMEWIN_STATIC_IMAGE, GAMEWIN_EXIT_HOVER_IMAGE,
    			        GAMEWIN_REPLAY_HOVER_IMAGE);
            }
        }

        private void afterGameState(String normalImg, String exitHoverImg,
        String replayHoverImg)
        {
            float worldWidth = XNACS1Lib.XNACS1Base.World.WorldDimension.X;
            float worldHeight = XNACS1Lib.XNACS1Base.World.WorldDimension.Y;

            // Focuses the highlightable area to a column in the center.
            if (MouseDevice.getMouseX() < (worldWidth / 2) + 7f &&
                    MouseDevice.getMouseX() > (worldWidth / 2) - 7f)
            {
                // Focuses the exit highlight area to a row within the column
                // so the highlight area is rectangular like the image.
                if (MouseDevice.getMouseY() > (worldHeight / 2) - 6f &&
                        MouseDevice.getMouseY() < (worldHeight / 2) - 2f)
                {
                    afterGameForeground1.Texture = exitHoverImg;
                    if (MouseDevice.IsLeftButtonDown())
                    {
                        gameBase.Exit();
                    }
                }
                // The replay highlight area.
                else if (MouseDevice.getMouseY() > (worldHeight / 2) - 12f &&
                        MouseDevice.getMouseY() < (worldHeight / 2) - 8f)
                {
                    afterGameForeground1.Texture = replayHoverImg;
                    if (MouseDevice.IsLeftButtonDown())
                    {
                        afterGameForeground1.RemoveFromAutoDrawSet();
                        afterGameForeground1 = null;
                        afterGameBackground.RemoveFromAutoDrawSet();
                        afterGameBackground = null;

                        gameBase.IsMouseVisible = false;
                        replay = true;
                    }
                }
                else
                {
                    afterGameForeground1.Texture = normalImg;
                }
            }
            else
            {
                afterGameForeground1.Texture = normalImg;
            }
        }

    }
}
