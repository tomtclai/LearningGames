using System;
using System.IO;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Content;

namespace XNACS1Lib
{
    internal  class TextureTable : ResourceTable
    {
        private ContentManager m_ContentManager;        // GraphicsSystem should allocate this
        private List<Texture2D> m_Texture;
        private List<Color[]> m_TextureColor;
        private String[] mResourceFileExtension = { @".png", @".jpg", @".xnb" };

        override protected String FolderName { get { return @"Textures"; } }
        override protected String[] ResourceFileExtension { get { return mResourceFileExtension; } }

        public TextureTable(ContentManager manager)
        {
            m_ContentManager = manager;
            m_Texture = new List<Texture2D>();
            m_TextureColor = new List<Color[]>();
        }

        public void LoadTexture(String name)
        {
            String useName = FindResourceFile(name);
            if (null != useName)
            {
                Texture2D t = m_ContentManager.Load<Texture2D>(useName);
                Color[] tColor = new Color[t.Width * t.Height];
                t.GetData(tColor);
                m_Texture.Add(t);
                m_TextureColor.Add(tColor);
            }
        }

        public Texture2D FindTexture(String name)
        {
            int index = FindResourceIndex(name);
            Texture2D foundTex = null;

            if (index >= 0)
                foundTex = m_Texture[index];

            return foundTex;
        }

        public Color[] FindTextureColor(String name)
        {
            int index = FindResourceIndex(name);
            Color[] foundColor = null;

            if (index >= 0)
                foundColor = m_TextureColor[index];

            return foundColor;
        }
    }
}