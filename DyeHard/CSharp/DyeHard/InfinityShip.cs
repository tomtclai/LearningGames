using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNACS1Lib;
using Microsoft.Xna.Framework;

namespace Dyehard
{
    class InfinityShip
    {
        public static float Speed = 0f;
        private Queue<ShipTile> shipTiles;
        private Queue<ShipTile> pool;

        public InfinityShip(float speed)
        {
            Speed = speed;
            this.shipTiles = new Queue<ShipTile>();
            this.pool = new Queue<ShipTile>();

            shipTiles.Enqueue(new ShipTile(GameWorld.rightEdge));
        }

        public void update()
        {
            // move stars, and redraw
            foreach (ShipTile tile in shipTiles)
            {
                tile.update();
            }

            if (shipTiles.First().isOffScreen())
            {
                pool.Enqueue(shipTiles.Dequeue());
            }

            if (shipTiles.Last().rightEdge() <= GameWorld.rightEdge)
            {
                if (pool.Count > 0)
                {
                    // draw a tile from the pool
                    ShipTile tile = pool.Dequeue();
                    tile.setLeftEdgeAt(shipTiles.Last().rightEdge());
                    shipTiles.Enqueue(tile);
                }
                else
                {
                    // pool is empty, create new ship tile
                    shipTiles.Enqueue(
                        new ShipTile(shipTiles.Last().rightEdge())
                    );
                }
            }

            foreach (ShipTile tile in shipTiles)
            {
                tile.draw();
            }
        }
    }

    class ShipTile
    {
        private const float width = 40;
        private const float height = 25f;
        private float gap = 1.5f; // force gap between ship tiles to show overlap
        private XNACS1Rectangle tile;
        private XNACS1Rectangle window;

        public ShipTile(float leftEdge)
        {
            float Xpos = leftEdge + (width * 0.5f);
            float Ypos = (Background.height / 2);
            Vector2 position = new Vector2(Xpos, Ypos);
            this.tile = new XNACS1Rectangle(position, width - gap, height);
            this.tile.Color = Color.DimGray;

            this.window = new XNACS1Rectangle(position, 1, 1);
            this.window.Color = Color.Gray;
        }

        public void update()
        {
            tile.CenterX -= InfinityShip.Speed;
            window.CenterX -= InfinityShip.Speed;
        }

        public void setLeftEdgeAt(float leftEdge)
        {
            tile.CenterX = leftEdge + ((tile.Width + gap) / 2);
            window.CenterX = tile.CenterX;
        }

        public float rightEdge()
        {
            return tile.MaxBound.X + (gap / 2);
        }

        public bool isOffScreen()
        {
            return rightEdge() < GameWorld.leftEdge;
        }

        public void draw()
        {
            tile.TopOfAutoDrawSet();
            window.TopOfAutoDrawSet();
        }
    }
}
