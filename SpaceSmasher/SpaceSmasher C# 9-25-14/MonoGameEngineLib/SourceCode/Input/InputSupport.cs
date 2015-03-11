//*******************************************
// PredefinedEmitter.cs
// Author:
// ChangeLog
// Li Yueqiao - Added the ability to have custom key bindings
// Samuel Cook - Made changes to custom key bindings support so that it had default settings rather than requiring the file
//*******************************************


using System;
using System.IO;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Content;
using System.Text;
using System.Collections;
using System.Runtime.InteropServices;

#if WINDOWS_PHONE
using XNACS1Lib.WP7InputSupport;
#else
using Microsoft.Xna.Framework.Storage;
#endif

namespace XNACS1Lib
{
    namespace XNACS1Lib.GamePadSupport
    {
        /// <summary>
        /// Records the states (pressed or release) of all the buttons on a XBOX 360 game pad.
        /// </summary>
        /// 
        public struct AllButtonsOnGamePad
        {
            /// <summary>
            /// State of the A-Button (pressed or released).
            /// </summary>
            public ButtonState A;

            /// <summary>
            /// State of the B-Button (pressed or released).
            /// </summary>
            public ButtonState B;

            /// <summary>
            /// State of the back-Button (pressed or released).
            /// </summary>
            public ButtonState Back;

            /// <summary>
            /// State of the leftShoulder-Button (pressed or released).
            /// </summary>
            public ButtonState LeftShoulder;

            /// <summary>
            /// State of the LeftStick-Button (pressed or released).
            /// </summary>
            public ButtonState LeftStick;

            /// <summary>
            /// State of the RightShoulder-Button (pressed or released).
            /// </summary>
            public ButtonState RightShoulder;

            /// <summary>
            /// State of the RightStick-Button (pressed or released).
            /// </summary>
            public ButtonState RightStick;

            /// <summary>
            /// State of the Start-Button (pressed or released).
            /// </summary>
            public ButtonState Start;

            /// <summary>
            /// State of the X-Button (pressed or released).
            /// </summary>
            public ButtonState X;

            /// <summary>
            /// State of the Y-Button (pressed or released).
            /// </summary>
            public ButtonState Y;
        }

        /// <summary>
        /// States (depressed or released) of the DPad-buttons (left, right, top, down)
        /// on the XBOX 360 game pad.
        /// </summary>
        public struct DPadOnGamePad
        {
            /// <summary>
            /// State of the DPad Down-Button (pressed or released).
            /// </summary>
            public ButtonState Down;

            /// <summary>
            /// State of the DPad Left-Button (pressed or released).
            /// </summary>
            public ButtonState Left;

            /// <summary>
            /// State of the DPad Right-Button (pressed or released).
            /// </summary>
            public ButtonState Right;


            /// <summary>
            /// State of the DPad Up-Button (pressed or released).
            /// </summary>
            public ButtonState Up;
        }

        /// <summary>
        /// Positions of the left and right thumbSticks on the XBOX 360 controller.
        /// </summary>
        public struct ThumbSticksOnGamePad
        {
            /// <summary>
            /// Position of the left thumbStick (X/Y values between -1 and +1).
            /// </summary>
            public Vector2 Left;


            /// <summary>
            /// Position of the right thumbStick (X/Y values between -1 and +1).
            /// </summary>
            public Vector2 Right;

        }

        /// <summary>
        /// Positions of the left and right triggers on the XBOX 360 controller.
        /// </summary>
        public struct TriggersOnGamePad
        {
            /// <summary>
            /// Position of the left trigger (value between 0 to 1).
            /// </summary>
            public float Left;

            /// <summary>
            /// Position of the right trigger (value betwen 0 to 1).
            /// </summary>
            public float Right;
        }

        /// <summary>
        /// 
        /// </summary>
        internal class ButtonClickOnGamePad
        {
            public bool m_ButtonPressed;
            public bool m_EventValid;

            internal ButtonClickOnGamePad()
            {
                m_ButtonPressed = false;
                m_EventValid = false;
            }
            internal void SetButtonEvent(ButtonState aButton)
            {
                m_EventValid = ((aButton == ButtonState.Pressed) && (!m_ButtonPressed));
                m_ButtonPressed = (aButton == ButtonState.Pressed);

            }
            internal bool ButtonHasClicked()
            {
                bool canUseEvent = m_EventValid;
                m_EventValid = false;
                return canUseEvent && m_ButtonPressed;
            }
        }
 

        /// <summary>
    /// State information for all input mechanisms on the XBOX 360 game pad controller.
    /// </summary>
        public class GamePadStruct
    {
        internal GamePadStruct(float v)
        {
            m_VibrationA = v;
            m_VibrationB = v;

            Buttons = new XNACS1Lib.GamePadSupport.AllButtonsOnGamePad();
            Dpad = new XNACS1Lib.GamePadSupport.DPadOnGamePad();
            ThumbSticks = new XNACS1Lib.GamePadSupport.ThumbSticksOnGamePad();
            Triggers = new XNACS1Lib.GamePadSupport.TriggersOnGamePad();
            aButton = new XNACS1Lib.GamePadSupport.ButtonClickOnGamePad();
            bButton = new XNACS1Lib.GamePadSupport.ButtonClickOnGamePad();
            xButton = new XNACS1Lib.GamePadSupport.ButtonClickOnGamePad();
            yButton = new XNACS1Lib.GamePadSupport.ButtonClickOnGamePad();
            startButton = new XNACS1Lib.GamePadSupport.ButtonClickOnGamePad();
            backButton = new XNACS1Lib.GamePadSupport.ButtonClickOnGamePad();
        }

        /// <summary>
        /// States of all the buttons on the XBOX 360 game pad.
        /// </summary>
        public XNACS1Lib.GamePadSupport.AllButtonsOnGamePad Buttons;

        /// <summary>
        /// States of all the buttons on the DPad on the XBOX 360 game pad.
        /// </summary>
        public XNACS1Lib.GamePadSupport.DPadOnGamePad Dpad;

        /// <summary>
        /// Positions of the left and right thumbsticks on the XBOX 360 game pad.
        /// </summary>
        public XNACS1Lib.GamePadSupport.ThumbSticksOnGamePad ThumbSticks;

        /// <summary>
        /// Positions of the left and right triggers on the XBOX 360 game pad.
        /// </summary>
        public XNACS1Lib.GamePadSupport.TriggersOnGamePad Triggers;

        internal float m_VibrationA, m_VibrationB;

        internal XNACS1Lib.GamePadSupport.ButtonClickOnGamePad aButton, bButton, xButton, yButton, startButton, backButton;

        internal void UpdateButtonClick()
        {
            aButton.SetButtonEvent(Buttons.A);
            bButton.SetButtonEvent(Buttons.B);
            xButton.SetButtonEvent(Buttons.X);
            yButton.SetButtonEvent(Buttons.Y);
            startButton.SetButtonEvent(Buttons.Start);
            backButton.SetButtonEvent(Buttons.Back);
        }

        /// <summary>
        /// If Button-A has been clicked.
        /// </summary>
        /// <returns>T/F on if the button has been clicked.</returns>
        public bool ButtonAClicked() { return aButton.ButtonHasClicked(); }

        /// <summary>
        /// If Button-B has been clicked.
        /// </summary>
        /// <returns>T/F on if the button has been clicked.</returns>
        public bool ButtonBClicked() { return bButton.ButtonHasClicked(); }

        /// <summary>
        /// If Button-X has been clicked.
        /// </summary>
        /// <returns>T/F on if the button has been clicked.</returns>
        public bool ButtonXClicked() { return xButton.ButtonHasClicked(); }

        /// <summary>
        /// If Button-Y has been clicked.
        /// </summary>
        /// <returns>T/F on if the button has been clicked.</returns>
        public bool ButtonYClicked() { return yButton.ButtonHasClicked(); }

        /// <summary>
        /// If the start button has been clicked.
        /// </summary>
        /// <returns>T/F on if the button has been clicked.</returns>
        public bool ButtonStartClicked() { return startButton.ButtonHasClicked(); }

        /// <summary>
        /// If the back button has been clicked.
        /// </summary>
        /// <returns>T/F on if the button has been clicked.</returns>
        public bool ButtonBackClicked() { return backButton.ButtonHasClicked(); }
    }
    }


    // Keyboard-input-manager can open CS1setting.ini and read [keyboard] section to make keyboard mapping.
    //   Li Yueqiao, 2009.9.1.
    internal class KeyboardInputManager
    {
#if WINDOWS_PHONE || XBOX
        internal string[] KeyID = {
                "A-key",
                "B-key",
                "X-key",
                "Y-key",
                "LeftShoulder",
                "RightShoulder",
                "TriggerLeft",
                "TriggerRight",
                "TriggerBack",
                "Start",
                "ThumbLeft-Up",
                "ThumbLeft-Down",
                "ThumbLeft-Left",
                "ThumbLeft-Right",
                "ThumbRight-Up",
                "ThumbRight-Down",
                "ThumbRight-Left",
                "ThumbRight-Right",
                "Dpad-Up",
                "Dpad-Down",
                "Dpad-Left",
                "Dpad-Right",
                "LeftStickButton",
                "RightStickButton", 
                null};

        internal Keys[] KeyValue = {
                Keys.K,         // A
                Keys.L,         // B
                Keys.J,         // X
                Keys.I,         // Y
                Keys.U,         // LeftShoulder
                Keys.O,         // RightShoulder
                Keys.M,         // TriggerLeft
                Keys.OemPeriod, // TriggerRight
                Keys.F1,        // TriggerBack
                Keys.F2,         // TriggerStart
                Keys.W,         // LeftThumbStick-Up
                Keys.S,         // LeftThumbStick-Down
                Keys.A,         // LeftThumbStick-Left
                Keys.D,         // LeftThumbStick-Right
                Keys.Up,        // RightThumbStick-Up
                Keys.Down,      // RightThumbStick-Down
                Keys.Left,      // RightThumbStick-Left
                Keys.Right,     // RightThumbStick-Right
                Keys.F,         // Dpad-Up
                Keys.V,         // Dpad-Down
                Keys.C,         // Dpad-Left
                Keys.B,         // Dpad-Right
                Keys.LeftShift, // LeftStickButton
                Keys.RightControl};        // RightStickButton

        public KeyboardInputManager(string FileName)
        {
        }

        public Keys this[string key]
        {
            get {
                bool found = false;
                string result = null;
                int i = 0;
                while ( (!found) && (null == result)) {
                    if (string.Compare(KeyID[i], key) == 0)
                        found = true;
                    else
                        i++;
                }   
                return KeyValue[i]; 
            }
        }
#else
        [ DllImport ( "kernel32" ) ]
        private static extern int GetPrivateProfileString ( 
            string section, string key, string def, 
            StringBuilder retVal, int size , string filePath );

        private static Hashtable KeyMapping_NameContent = new Hashtable(120);
        private static void KeyMappingInitialize()
        {
			KeyMapping_NameContent.Clear();
			KeyMapping_NameContent.Add("Q", Keys.Q);
			KeyMapping_NameContent.Add("W", Keys.W);
			KeyMapping_NameContent.Add("E", Keys.E);
			KeyMapping_NameContent.Add("R", Keys.R);
			KeyMapping_NameContent.Add("T", Keys.T);
			KeyMapping_NameContent.Add("Y", Keys.Y);
			KeyMapping_NameContent.Add("U", Keys.U);
			KeyMapping_NameContent.Add("I", Keys.I);
			KeyMapping_NameContent.Add("O", Keys.O);
			KeyMapping_NameContent.Add("P", Keys.P);
			KeyMapping_NameContent.Add("A", Keys.A);
			KeyMapping_NameContent.Add("S", Keys.S);
			KeyMapping_NameContent.Add("D", Keys.D);
			KeyMapping_NameContent.Add("F", Keys.F);
			KeyMapping_NameContent.Add("G", Keys.G);
			KeyMapping_NameContent.Add("H", Keys.H);
			KeyMapping_NameContent.Add("J", Keys.J);
			KeyMapping_NameContent.Add("K", Keys.K);
			KeyMapping_NameContent.Add("L", Keys.L);
			KeyMapping_NameContent.Add("Z", Keys.Z);
			KeyMapping_NameContent.Add("X", Keys.X);
			KeyMapping_NameContent.Add("C", Keys.C);
			KeyMapping_NameContent.Add("V", Keys.V);
			KeyMapping_NameContent.Add("B", Keys.B);
			KeyMapping_NameContent.Add("N", Keys.N);
			KeyMapping_NameContent.Add("M", Keys.M);
			KeyMapping_NameContent.Add("Backspace", Keys.Back);
			KeyMapping_NameContent.Add("Tab", Keys.Tab);
			KeyMapping_NameContent.Add("Enter", Keys.Enter);
			KeyMapping_NameContent.Add("Pause", Keys.Pause);
			KeyMapping_NameContent.Add("CapsLock", Keys.CapsLock);
			KeyMapping_NameContent.Add("Escape", Keys.Escape);
			KeyMapping_NameContent.Add("Space", Keys.Space);
			KeyMapping_NameContent.Add("PageUp", Keys.PageUp);
			KeyMapping_NameContent.Add("PageDown", Keys.PageDown);
			KeyMapping_NameContent.Add("End", Keys.End);
			KeyMapping_NameContent.Add("Home", Keys.Home);
			KeyMapping_NameContent.Add("Left", Keys.Left);
			KeyMapping_NameContent.Add("Up", Keys.Up);
			KeyMapping_NameContent.Add("Right", Keys.Right);
			KeyMapping_NameContent.Add("Down", Keys.Down);
			KeyMapping_NameContent.Add("PrintScreen", Keys.PrintScreen);
			KeyMapping_NameContent.Add("Insert", Keys.Insert);
			KeyMapping_NameContent.Add("Delete", Keys.Delete);
			KeyMapping_NameContent.Add("NumPad0", Keys.NumPad0);
			KeyMapping_NameContent.Add("NumPad1", Keys.NumPad1);
			KeyMapping_NameContent.Add("NumPad2", Keys.NumPad2);
			KeyMapping_NameContent.Add("NumPad3", Keys.NumPad3);
			KeyMapping_NameContent.Add("NumPad4", Keys.NumPad4);
			KeyMapping_NameContent.Add("NumPad5", Keys.NumPad5);
			KeyMapping_NameContent.Add("NumPad6", Keys.NumPad6);
			KeyMapping_NameContent.Add("NumPad7", Keys.NumPad7);
			KeyMapping_NameContent.Add("NumPad8", Keys.NumPad8);
			KeyMapping_NameContent.Add("NumPad9", Keys.NumPad9);
			KeyMapping_NameContent.Add("Multiply", Keys.Multiply);
			KeyMapping_NameContent.Add("Add", Keys.Add);
			KeyMapping_NameContent.Add("Separator", Keys.Separator);
			KeyMapping_NameContent.Add("Subtract", Keys.Subtract);
			KeyMapping_NameContent.Add("Decimal", Keys.Decimal);
			KeyMapping_NameContent.Add("Divide", Keys.Divide);
			KeyMapping_NameContent.Add("F1", Keys.F1);
			KeyMapping_NameContent.Add("F2", Keys.F2);
			KeyMapping_NameContent.Add("F3", Keys.F3);
			KeyMapping_NameContent.Add("F4", Keys.F4);
			KeyMapping_NameContent.Add("F5", Keys.F5);
			KeyMapping_NameContent.Add("F6", Keys.F6);
			KeyMapping_NameContent.Add("F7", Keys.F7);
			KeyMapping_NameContent.Add("F8", Keys.F8);
			KeyMapping_NameContent.Add("F9", Keys.F9);
			KeyMapping_NameContent.Add("F10", Keys.F10);
			KeyMapping_NameContent.Add("F11", Keys.F11);
			KeyMapping_NameContent.Add("F12", Keys.F12);
			KeyMapping_NameContent.Add("F13", Keys.F13);
			KeyMapping_NameContent.Add("F14", Keys.F14);
			KeyMapping_NameContent.Add("F15", Keys.F15);
			KeyMapping_NameContent.Add("F16", Keys.F16);
			KeyMapping_NameContent.Add("F17", Keys.F17);
			KeyMapping_NameContent.Add("F18", Keys.F18);
			KeyMapping_NameContent.Add("F19", Keys.F19);
			KeyMapping_NameContent.Add("F20", Keys.F20);
			KeyMapping_NameContent.Add("F21", Keys.F21);
			KeyMapping_NameContent.Add("F22", Keys.F22);
			KeyMapping_NameContent.Add("F23", Keys.F23);
			KeyMapping_NameContent.Add("F24", Keys.F24);
			KeyMapping_NameContent.Add("NumLock", Keys.NumLock);
			KeyMapping_NameContent.Add("Scroll", Keys.Scroll);
			KeyMapping_NameContent.Add("LeftShift", Keys.LeftShift);
			KeyMapping_NameContent.Add("RightShift", Keys.RightShift);
			KeyMapping_NameContent.Add("LeftControl", Keys.LeftControl);
			KeyMapping_NameContent.Add("RightControl", Keys.RightControl);
			KeyMapping_NameContent.Add("LeftAlt", Keys.LeftAlt);
			KeyMapping_NameContent.Add("RightAlt", Keys.RightAlt);
			KeyMapping_NameContent.Add("0", Keys.D0);
			KeyMapping_NameContent.Add("1", Keys.D1);
			KeyMapping_NameContent.Add("2", Keys.D2);
			KeyMapping_NameContent.Add("3", Keys.D3);
			KeyMapping_NameContent.Add("4", Keys.D4);
			KeyMapping_NameContent.Add("5", Keys.D5);
			KeyMapping_NameContent.Add("6", Keys.D6);
			KeyMapping_NameContent.Add("7", Keys.D7);
			KeyMapping_NameContent.Add("8", Keys.D8);
			KeyMapping_NameContent.Add("9", Keys.D9);
			KeyMapping_NameContent.Add("OemSemicolon", Keys.OemSemicolon);
			KeyMapping_NameContent.Add("OemPlus", Keys.OemPlus);
			KeyMapping_NameContent.Add("OemComma", Keys.OemComma);
			KeyMapping_NameContent.Add("OemMinus", Keys.OemMinus);
			KeyMapping_NameContent.Add("OemPeriod", Keys.OemPeriod);
			KeyMapping_NameContent.Add("OemQuestion", Keys.OemQuestion);
			KeyMapping_NameContent.Add("OemTilde", Keys.OemTilde);
			KeyMapping_NameContent.Add("OemOpenBrackets", Keys.OemOpenBrackets);
			KeyMapping_NameContent.Add("OemPipe", Keys.OemPipe);
			KeyMapping_NameContent.Add("OemCloseBrackets", Keys.OemCloseBrackets);
			KeyMapping_NameContent.Add("OemQuotes", Keys.OemQuotes);
			KeyMapping_NameContent.Add("OemBackslash", Keys.OemBackslash);
			KeyMapping_NameContent.Add("none", null);
		}

		internal Hashtable KeyboardInputMap = new Hashtable(26);

        public KeyboardInputManager(string FileName)
        {
            KeyMappingInitialize();
            KeyboardInputMap.Clear();
            if (File.Exists(FileName)) {
                StringBuilder strTemp = new StringBuilder(255);
                GetPrivateProfileString("Keyboard", "A-key", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("A-key", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "B-key", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("B-key", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "X-key", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("X-key", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "Y-key", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("Y-key", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "LeftShoulder", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("LeftShoulder", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "RightShoulder", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("RightShoulder", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "TriggerLeft", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("TriggerLeft", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "TriggerRight", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("TriggerRight", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "TriggerBack", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("TriggerBack", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "Start", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("Start", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "ThumbLeft-Up", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("ThumbLeft-Up", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "ThumbLeft-Down", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("ThumbLeft-Down", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "ThumbLeft-Left", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("ThumbLeft-Left", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "ThumbLeft-Right", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("ThumbLeft-Right", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "ThumbRight-Up", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("ThumbRight-Up", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "ThumbRight-Down", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("ThumbRight-Down", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "ThumbRight-Left", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("ThumbRight-Left", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "ThumbRight-Right", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("ThumbRight-Right", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "Dpad-Up", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("Dpad-Up", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "Dpad-Down", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("Dpad-Down", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "Dpad-Left", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("Dpad-Left", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "Dpad-Right", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("Dpad-Right", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "LeftStickButton", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("LeftStickButton", KeyMapping_NameContent[strTemp.ToString()]);
                GetPrivateProfileString("Keyboard", "RightStickButton", "none", strTemp, 255, FileName);
                KeyboardInputMap.Add("RightStickButton", KeyMapping_NameContent[strTemp.ToString()]);
            }
            else {
                KeyboardInputMap.Add("A-key", KeyMapping_NameContent["K"]);
                KeyboardInputMap.Add("B-key", KeyMapping_NameContent["L"]);
                KeyboardInputMap.Add("X-key", KeyMapping_NameContent["J"]);
                KeyboardInputMap.Add("Y-key", KeyMapping_NameContent["I"]);
                KeyboardInputMap.Add("LeftShoulder", KeyMapping_NameContent["U"]);
                KeyboardInputMap.Add("RightShoulder", KeyMapping_NameContent["O"]);
                KeyboardInputMap.Add("TriggerLeft", KeyMapping_NameContent["M"]);
                KeyboardInputMap.Add("TriggerRight", KeyMapping_NameContent["OemPeriod"]);
                KeyboardInputMap.Add("TriggerBack", KeyMapping_NameContent["F1"]);
                KeyboardInputMap.Add("Start", KeyMapping_NameContent["F2"]);
                KeyboardInputMap.Add("ThumbLeft-Up", KeyMapping_NameContent["W"]);
                KeyboardInputMap.Add("ThumbLeft-Down", KeyMapping_NameContent["S"]);
                KeyboardInputMap.Add("ThumbLeft-Left", KeyMapping_NameContent["A"]);
                KeyboardInputMap.Add("ThumbLeft-Right", KeyMapping_NameContent["D"]);
                KeyboardInputMap.Add("ThumbRight-Up", KeyMapping_NameContent["Up"]);
                KeyboardInputMap.Add("ThumbRight-Down", KeyMapping_NameContent["Down"]);
                KeyboardInputMap.Add("ThumbRight-Left", KeyMapping_NameContent["Left"]);
                KeyboardInputMap.Add("ThumbRight-Right", KeyMapping_NameContent["Right"]);
                KeyboardInputMap.Add("Dpad-Up", KeyMapping_NameContent["F"]);
                KeyboardInputMap.Add("Dpad-Down", KeyMapping_NameContent["V"]);
                KeyboardInputMap.Add("Dpad-Left", KeyMapping_NameContent["C"]);
                KeyboardInputMap.Add("Dpad-Right", KeyMapping_NameContent["B"]);
                KeyboardInputMap.Add("LeftStickButton", KeyMapping_NameContent["LeftShift"]);
                KeyboardInputMap.Add("RightStickButton", KeyMapping_NameContent["RightControl"]);
            }
		}

        public Keys this[string key]
		{
			get { return (Keys)KeyboardInputMap[key]; }
		}
#endif
    }


    internal static class XNACS1LibInputManager
    {

        /// <summary>
        /// Set the vibration level on the game pad. With keyboard only, this function does not
        /// do anything.
        /// </summary>
        /// <param name="index">game pad index (1 to 4)</param>
        /// <param name="a">motor-a: value between 0.0f to 1.0f</param>
        /// <param name="b">motor-b: value between 0.0f to 1.0f</param>
        /// <returns></returns>
        public static void SetGamePadVibration(PlayerIndex index, float a, float b)
        {
            // for now, gamepad index-i is ignored.
            //
            m_MyGamePad.m_VibrationA = a;
            m_MyGamePad.m_VibrationB = b;
        }

        public static void UpdateGamePadState(GameTime gameTime, DisplayOrientation currentOrientation)
        {
#if WINDOWS_PHONE
            mWP7Input.UpdateWP7InputState(gameTime, currentOrientation);
#endif
            // Only get game controller input if there actually is one :)
            if (Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).IsConnected)
            {
                // Buttons
                m_MyGamePad.Buttons.A = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).Buttons.A;
                m_MyGamePad.Buttons.B = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).Buttons.B;
                m_MyGamePad.Buttons.X = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).Buttons.X;
                m_MyGamePad.Buttons.Y = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).Buttons.Y;

                m_MyGamePad.Buttons.Back = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).Buttons.Back;
                m_MyGamePad.Buttons.Start = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).Buttons.Start;

                m_MyGamePad.Buttons.LeftShoulder = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).Buttons.LeftShoulder;
                m_MyGamePad.Buttons.RightShoulder = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).Buttons.RightShoulder;

                m_MyGamePad.Buttons.LeftStick = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).Buttons.LeftStick;
                m_MyGamePad.Buttons.RightStick = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).Buttons.RightStick;

                // DPad
                m_MyGamePad.Dpad.Up = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).DPad.Up;
                m_MyGamePad.Dpad.Down = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).DPad.Down;
                m_MyGamePad.Dpad.Left = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).DPad.Left;
                m_MyGamePad.Dpad.Right = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).DPad.Right;

                // ThumbSticks
                m_MyGamePad.ThumbSticks.Left = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).ThumbSticks.Left;
                m_MyGamePad.ThumbSticks.Right = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).ThumbSticks.Right;

                // Triggers
                m_MyGamePad.Triggers.Left = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).Triggers.Left;
                m_MyGamePad.Triggers.Right = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).Triggers.Right;

                // Vibration
                // Microsoft.Xna.Framework.Input.GamePad.SetVibration(PlayerIndex.One, m_MyGamePad.m_VibrationA, m_MyGamePad.m_VibrationB);
            }
            else
            {
                m_MyGamePad.Buttons.A = ButtonState.Released;
                m_MyGamePad.Buttons.B = ButtonState.Released;
                m_MyGamePad.Buttons.X = ButtonState.Released;
                m_MyGamePad.Buttons.Y = ButtonState.Released;

                m_MyGamePad.Buttons.Back = ButtonState.Released;
                m_MyGamePad.Buttons.Start = ButtonState.Released;

                m_MyGamePad.Buttons.LeftShoulder = ButtonState.Released;
                m_MyGamePad.Buttons.RightShoulder = ButtonState.Released;

                m_MyGamePad.Buttons.LeftStick = ButtonState.Released;
                m_MyGamePad.Buttons.RightStick = ButtonState.Released;

                // DPad
                m_MyGamePad.Dpad.Up = ButtonState.Released;
                m_MyGamePad.Dpad.Down = ButtonState.Released;
                m_MyGamePad.Dpad.Left = ButtonState.Released;
                m_MyGamePad.Dpad.Right = ButtonState.Released;

                // ThumbSticks
                m_MyGamePad.ThumbSticks.Left = new Vector2(0, 0);
                m_MyGamePad.ThumbSticks.Right = new Vector2(0, 0);

                // Triggers
                m_MyGamePad.Triggers.Left = 0f;
                m_MyGamePad.Triggers.Right = 0f;
            }

            //  update from the keyboard, so that if either kdb or controller is being pressed, 
            //      the game gets the 'pressed' message.  We only grab keyboard input if the
            //      controller hasn't done anything yet, so that the controller has 'precedence'
            // 
            //  use keyboard mapping to allow user-defined keyboards.
            //     -- modified by Li Yueqiao, 2009.9.1.
            if (m_MyGamePad.Buttons.A == ButtonState.Released)
#if WINDOWS_PHONE
                m_MyGamePad.Buttons.A = mWP7Input.HasTap() ? ButtonState.Pressed : ButtonState.Released;
#else
                m_MyGamePad.Buttons.A = Keyboard.GetState().IsKeyDown(m_Input["A-key"]) ? ButtonState.Pressed : ButtonState.Released;
#endif

            if (m_MyGamePad.Buttons.B == ButtonState.Released)
#if WINDOWS_PHONE
                m_MyGamePad.Buttons.B = mWP7Input.HasTapTwo() ? ButtonState.Pressed : ButtonState.Released;
#else
                m_MyGamePad.Buttons.B = Keyboard.GetState().IsKeyDown(m_Input["B-key"]) ? ButtonState.Pressed : ButtonState.Released;
#endif

            if (m_MyGamePad.Buttons.X == ButtonState.Released)
                m_MyGamePad.Buttons.X = Keyboard.GetState().IsKeyDown(m_Input["X-key"]) ? ButtonState.Pressed : ButtonState.Released;
            if (m_MyGamePad.Buttons.Y == ButtonState.Released)
                m_MyGamePad.Buttons.Y = Keyboard.GetState().IsKeyDown(m_Input["Y-key"]) ? ButtonState.Pressed : ButtonState.Released;

            if (m_MyGamePad.Buttons.Back == ButtonState.Released)
#if WINDOWS_PHONE
                m_MyGamePad.Buttons.Back = Microsoft.Xna.Framework.Input.GamePad.GetState(PlayerIndex.One).Buttons.Back;
#else
                m_MyGamePad.Buttons.Back = Keyboard.GetState().IsKeyDown(m_Input["TriggerBack"]) ? ButtonState.Pressed : ButtonState.Released;
#endif
            if (m_MyGamePad.Buttons.Start == ButtonState.Released)
                m_MyGamePad.Buttons.Start = Keyboard.GetState().IsKeyDown(m_Input["Start"]) ? ButtonState.Pressed : ButtonState.Released;

            if (m_MyGamePad.Buttons.LeftShoulder == ButtonState.Released)
                m_MyGamePad.Buttons.LeftShoulder = Keyboard.GetState().IsKeyDown(m_Input["LeftShoulder"]) ? ButtonState.Pressed : ButtonState.Released;
            if (m_MyGamePad.Buttons.RightShoulder == ButtonState.Released)
                m_MyGamePad.Buttons.RightShoulder = Keyboard.GetState().IsKeyDown(m_Input["RightShoulder"]) ? ButtonState.Pressed : ButtonState.Released;

            if (m_MyGamePad.Buttons.LeftStick == ButtonState.Released)
                m_MyGamePad.Buttons.LeftStick = Keyboard.GetState().IsKeyDown(m_Input["LeftStickButton"]) ? ButtonState.Pressed : ButtonState.Released;
            if (m_MyGamePad.Buttons.RightStick == ButtonState.Released)
                m_MyGamePad.Buttons.RightStick = Keyboard.GetState().IsKeyDown(m_Input["RightStickButton"]) ? ButtonState.Pressed : ButtonState.Released;

            // DPad
            if (m_MyGamePad.Dpad.Up == ButtonState.Released)
                m_MyGamePad.Dpad.Up = Keyboard.GetState().IsKeyDown(m_Input["Dpad-Up"]) ? ButtonState.Pressed : ButtonState.Released;
            if (m_MyGamePad.Dpad.Down == ButtonState.Released)
                m_MyGamePad.Dpad.Down = Keyboard.GetState().IsKeyDown(m_Input["Dpad-Down"]) ? ButtonState.Pressed : ButtonState.Released;

            if (m_MyGamePad.Dpad.Left == ButtonState.Released)
                m_MyGamePad.Dpad.Left = Keyboard.GetState().IsKeyDown(m_Input["Dpad-Left"]) ? ButtonState.Pressed : ButtonState.Released;
            if (m_MyGamePad.Dpad.Right == ButtonState.Released)
                m_MyGamePad.Dpad.Right = Keyboard.GetState().IsKeyDown(m_Input["Dpad-Right"]) ? ButtonState.Pressed : ButtonState.Released;

            // ThumbSticks
            if (m_MyGamePad.ThumbSticks.Left.X == 0 && m_MyGamePad.ThumbSticks.Left.Y == 0)
#if WINDOWS_PHONE
            {
                m_MyGamePad.ThumbSticks.Left = new Vector2(mWP7Input.HDelta(), mWP7Input.VDelta());
            }
#else
            {
                Vector2 left = new Vector2();
                left.X = Keyboard.GetState().IsKeyDown(m_Input["ThumbLeft-Right"]) ? 0.75f : 0.0f;
                left.X -= Keyboard.GetState().IsKeyDown(m_Input["ThumbLeft-Left"]) ? 0.75f : 0.0f;
                left.Y = Keyboard.GetState().IsKeyDown(m_Input["ThumbLeft-Up"]) ? 0.75f : 0.0f;
                left.Y -= Keyboard.GetState().IsKeyDown(m_Input["ThumbLeft-Down"]) ? 0.75f : 0.0f;
                m_MyGamePad.ThumbSticks.Left = left;
            }
#endif

            if (m_MyGamePad.ThumbSticks.Right.X == 0 && m_MyGamePad.ThumbSticks.Right.Y == 0)
#if WINDOWS_PHONE
            {
                Vector3 a = mWP7Input.GetAcc();
                m_MyGamePad.ThumbSticks.Right = new Vector2(a.X, a.Y);
            }
#else
            {
                Vector2 right = new Vector2();
                right.X = Keyboard.GetState().IsKeyDown(m_Input["ThumbRight-Right"]) ? 0.75f : 0.0f;
                right.X -= Keyboard.GetState().IsKeyDown(m_Input["ThumbRight-Left"]) ? 0.75f : 0.0f;
                right.Y = Keyboard.GetState().IsKeyDown(m_Input["ThumbRight-Up"]) ? 0.75f : 0.0f;
                right.Y -= Keyboard.GetState().IsKeyDown(m_Input["ThumbRight-Down"]) ? 0.75f : 0.0f;
                m_MyGamePad.ThumbSticks.Right = right;
            }
#endif

            // Triggers
            if (m_MyGamePad.Triggers.Left == 0)
                m_MyGamePad.Triggers.Left = Keyboard.GetState().IsKeyDown(m_Input["TriggerLeft"]) ? 0.5f : 0.0f;
            if (m_MyGamePad.Triggers.Right == 0)
                m_MyGamePad.Triggers.Right = Keyboard.GetState().IsKeyDown(m_Input["TriggerRight"]) ? 0.5f : 0.0f;

            m_MyGamePad.UpdateButtonClick();
        }


#if WINDOWS_PHONE
        private static WP7InputSupport.WP7Input mWP7Input = new WP7Input();
#endif
        private static XNACS1Lib.GamePadSupport.GamePadStruct m_MyGamePad = new XNACS1Lib.GamePadSupport.GamePadStruct(0.0f);
        public static XNACS1Lib.GamePadSupport.GamePadStruct GamePad { get { return m_MyGamePad; } }
		public static KeyboardInputManager m_Input = new KeyboardInputManager("./Content/Resources/CS1Setting.ini");
    }
}