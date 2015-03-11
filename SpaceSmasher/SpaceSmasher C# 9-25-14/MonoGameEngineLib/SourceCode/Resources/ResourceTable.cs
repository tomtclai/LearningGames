using System;
using System.IO;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
// using Microsoft.Xna.Framework.Content;

namespace XNACS1Lib
{
    internal abstract class ResourceTable
    {
        private List<String> m_ResourceTable;
        private String[] kPaths = new String[]
        {   @"Resources",
            @"Content/Resources"
        };

        public ResourceTable()
        {
            m_ResourceTable = new List<String>();
        }

        abstract protected String FolderName { get; }
        abstract protected String[] ResourceFileExtension { get; }

        protected String FindResourceFile(String name)
        {
            if (FindResourceIndex(name) >= 0)     // Already loaded, don't worry about loading again
                return null;

            String fname = null;
            String useName = FolderName + "/" + name;

#if WINDOWS_PHONE
            if ((null!=name) && (!name.Equals("")))
                fname = kPaths[1] + "/" + useName;
#else       

            bool found = false;
            int e = 0;
            String[] extendFildName = ResourceFileExtension;

            while ((e < extendFildName.Length) && (found == false))
            {
                String checkName = useName + extendFildName[e];
                int i = 0;
                while ((found == false) && (i < kPaths.Length))
                {
                    String search = kPaths[i] + "/" + checkName;
                    string[] derp = Directory.GetFiles("Resources/Textures");
                    if (File.Exists(search))
                    {
                        fname = kPaths[i] + "/" + useName;
                        found = true;
                    }
                    i++;
                }
                e++;
            }
#endif

            if (null != fname)
            {
                m_ResourceTable.Add(name);
            }
            return fname;
        }

        protected int FindResourceIndex(String name)
        {
            // for now, simple linear search
            bool found = false;
            int i = 0;
            while ((i < m_ResourceTable.Count) && (!found))
            {
                found = String.Equals(name, m_ResourceTable[i], StringComparison.OrdinalIgnoreCase);
                i++;
            }

            if (found)
                i--;
            else
                i = -1;

            return i;
        }
    }
}