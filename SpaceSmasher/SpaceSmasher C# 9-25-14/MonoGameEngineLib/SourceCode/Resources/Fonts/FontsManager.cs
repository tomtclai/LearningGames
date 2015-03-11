using System;
using System.IO;
//using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
//using Microsoft.Xna.Framework.Input;
// using Microsoft.Xna.Framework.Content;

namespace XNACS1Lib
{
    /// <summary>
    /// This is the base class for programming assignments.
    /// </summary>
    internal class FontManager
    {      
        private const float InitFontSpace = 10.0f;
        private const float FontHeight = 16.0f;
        private const float FontWidth = 10.5f;
        private float BottomFontY;

        private SpriteFont m_DefaultFont =null;
        private float m_SpaceFillerWidth;
        private XNACS1LibDrawHelper m_DrawHelper = null;
        private Color m_LabelColor, m_TopEchoColor, m_BottomEchoColor;
        private Microsoft.Xna.Framework.Content.ContentManager mContentManager;

        private String mUsePath = null;
        private String kDefaultFont = "Arial";

        private SpriteFont LoadFont(String fontName)
        {
            SpriteFont f = null;
            if (null != fontName)
            {
                String fullPath = String.Concat(mUsePath, fontName);

#if !WINDOWS_PHONE
                if (File.Exists(string.Concat(fullPath, ".xnb")))
#endif
                    f = mContentManager.Load<SpriteFont>(fullPath);
            }

            m_SpaceFillerWidth = 0;
            if (null == f)
            {
                f = m_DefaultFont;
            }
            if (null != f)
            {
                Vector2 spaceSize = f.MeasureString(" ");
                m_SpaceFillerWidth = spaceSize.X;
            }
            return f;
        }

        public FontManager(GraphicsDevice graphicsDevice, Microsoft.Xna.Framework.Content.ContentManager content, int appWinHeight)
        {
            mContentManager = content;
            m_LabelColor = Color.Red;
            m_TopEchoColor = Color.Black;
            m_BottomEchoColor = Color.Black;
            ComputeFontPositions(appWinHeight);

            m_DefaultFont = null;
            m_DrawHelper = null;

            const String path1 = @"Resources/Fonts/";
            const String path2 = @"Content/Resources/Fonts/";

#if WINDOWS_PHONE
            mUsePath = path2;
#else
            if (File.Exists(string.Concat(path1, kDefaultFont, ".xnb")))
                mUsePath = path1;
            else if (File.Exists(string.Concat(path2, kDefaultFont, ".xnb")))
                mUsePath = path2;
#endif

            if (null!=mUsePath) {
                String usePath = string.Concat(mUsePath, kDefaultFont);
                m_DefaultFont = LoadFont(kDefaultFont);
            }
        }

        public void ComputeFontPositions(int h)
        {
            BottomFontY = h - 25.0f;
        }

        public void SetDrawHelper(XNACS1LibDrawHelper d) { m_DrawHelper = d; }

        public Color TopEchoFontColor
        {
            set { m_TopEchoColor = value; }
        }

        public Color BottomEchoFontColor
        {
            set { m_BottomEchoColor = value; }
        }

        
        public void DrawFonts(String top, String bottom, String fontName)
        {
            SpriteFont useFont = LoadFont(fontName);
            if (null != useFont)
            {
                if (null != top)
                {
                    m_DrawHelper.DrawFont(useFont, @"Status:", new Vector2(InitFontSpace, 0.0f), m_LabelColor);
                    m_DrawHelper.DrawFont(useFont, top, new Vector2(InitFontSpace + (FontWidth * 7.0f), 0.0f), m_TopEchoColor);
                }

                if (null != bottom)
                {
                    m_DrawHelper.DrawFont(useFont, @"Status:", new Vector2(InitFontSpace, BottomFontY), m_LabelColor);
                    m_DrawHelper.DrawFont(useFont, bottom, new Vector2(InitFontSpace + (FontWidth * 7.0f), BottomFontY), m_BottomEchoColor);
                }
            }
        }

        public void DrawFontsAt(Vector2 dcAt, String msg, Color c, String fontName)
        {
            SpriteFont useFont = LoadFont(fontName);
            if ((null != useFont) && (null != msg))
            {

                    Vector2 fontAt = dcAt;
                    String [] lines = msg.Split(new Char[] {'\n'});
                    Vector2[] msgSize = new Vector2[lines.Length];
                    float totalHeight = 0;
                    float maxWidth = 0;
                    int l = 0;
                foreach (String s in lines)
                {
                    msgSize[l] = useFont.MeasureString(s);
                        if (msgSize[l].X > maxWidth)
                            maxWidth = msgSize[l].X;
                        totalHeight += msgSize[l].Y;
                        l++;
                    }
                    fontAt.Y -= (totalHeight/2.0f);
                    fontAt.X -= (maxWidth / 2.0f);
                    //
                    // Try to align center ...
                    // 
                    msg = "";
                    for (l = 0; l < lines.Length; l++)
                    {
                        int n = (int) ((0.5f*(maxWidth - msgSize[l].X)) / m_SpaceFillerWidth);
                        for (int i = 0; i < n; i++)
                            msg += " ";
                        msg += lines[l];
                        msg += "\n";
                    }
                m_DrawHelper.DrawFont(useFont, msg, fontAt, c);
            }
        }
    }
}