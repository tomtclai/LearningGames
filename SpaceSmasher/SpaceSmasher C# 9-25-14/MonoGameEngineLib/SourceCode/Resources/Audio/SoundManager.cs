#region Using Statements
using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
#endregion

namespace XNACS1Lib
{

    /// <summary>
    /// Abstracts away the sounds for a simple interface using the Sounds enum
    /// </summary>
    internal class SoundManager : ResourceTable
    {

        private ContentManager mContentManager = null;
        private List<SoundEffect> mAudioClips;

        private SoundEffectInstance mBgAudio = null;
        private String mBgAudioName = null;
        private String[] mResourceFileExtension = { @".wav", @".mp3", @".xnb" };

        override protected String FolderName { get { return @"Audio"; } }
        override protected String[] ResourceFileExtension { get { return mResourceFileExtension; } }

        public SoundManager(ContentManager m)
        {
            mContentManager = m;
            mBgAudio = null;
            mAudioClips = new List<SoundEffect>();
        }

        private int LoadAudioClip(String name)
        {
            int index = -1;
            String useName = FindResourceFile(name);
            if (null != useName)
            {
                mAudioClips.Add(mContentManager.Load<SoundEffect>(useName));
                index = mAudioClips.Count - 1;
            }
            return index;
        }

        private SoundEffect FindAudioClip(String name)
        {
            int index = FindResourceIndex(name);
            if (index < 0)
                index = LoadAudioClip(name);

            SoundEffect foundClip = null;

            if (index >= 0)
                foundClip = mAudioClips[index];

            return foundClip;
        }
        /// <summary>
        /// Playas the sound with name for lengthInSec
        /// </summary>
        /// <param name="name"></param>
        public void PlaySound(String name)
        {
            SoundEffect s = FindAudioClip(name);
            if (null != s)
            {
                try
                {
                    s.Play();
                }
                catch (InstancePlayLimitException e) { }
            }
        }

        private void StopBg()
        {
            if (null != mBgAudio)
            {
                mBgAudio.Pause();
                mBgAudio.Stop();
                mBgAudio.Volume = 0f;

                mBgAudio.Dispose();
            }
            
            mBgAudio = null;
            mBgAudioName = null;
        }

        /// <summary>
        ///  start without checking!!
        /// </summary>
        /// <param name="name"></param>
        /// <param name="level"></param>
        private void StarBg(String name, float level)
        {
            SoundEffect bg = FindAudioClip(name);
            mBgAudio = bg.CreateInstance();
            mBgAudio.IsLooped = true;
            mBgAudio.Volume = level;
            mBgAudio.Play();
            mBgAudioName = name;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="name"></param>
        /// <param name="level"></param>
        public void PlayBackgroundAudio(String name, float level)
        {
            if ((null == name) || (""==name))
            {
                StopBg();
            }
            else
            {
                level = MathHelper.Clamp(level, 0f, 1f);
                if (String.Compare(mBgAudioName, name) != 0)
                {
                    // new bg ... 
                    StopBg();
                    StarBg(name, level);
                }
                else
                {
                    mBgAudio.Volume = level;
                }
            }
        }
    }
}