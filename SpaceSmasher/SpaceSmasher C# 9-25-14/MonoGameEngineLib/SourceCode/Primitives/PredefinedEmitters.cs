//*******************************************
// PredefinedEmitter.cs
// Author:   Samuel Cook and Ron Cook
//*******************************************


using System;
using System.IO;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Content;

namespace XNACS1Lib {
    /// <summary>
    /// Inherits from XNACS1PrimitiveSet, allows a user to define behavior for a group of particles.
    /// </summary>
    public abstract partial class XNACS1ParticleEmitter : XNACS1Particle
    {
        /// <summary>
        /// Emits particles at the center of the emitter at a constant rate, which fade out according to InitialParticleLife.
        /// If the emitter is moving around this will look like a tail.  Works best if attached to other primitives.
        /// </summary>
        public class TailEmitter : XNACS1ParticleEmitter {
            /// <summary>
            /// Constructor for TailEmitter
            /// </summary>
            /// <param name="center"></param>
            /// <param name="initialParticleLife"></param>
            /// <param name="particleSize"></param>
            /// <param name="tex"></param>
            /// <param name="defaultColor"></param>
            public TailEmitter(Vector2 center, int initialParticleLife, float particleSize, string tex, Color defaultColor)
                : base(center, initialParticleLife, particleSize, tex, defaultColor) {
                m_AutoEmit = false;
            }

            /// <summary>
            /// Defines the behaviour for how the tail particles move.
            /// </summary>
            public ParticleTravelMode TailTravelMode;

            /// <summary>
            /// 
            /// </summary>
            /// <param name="newParticle"></param>
            protected override void NewParticle(XNACS1Particle newParticle) {
                if (TailTravelMode != ParticleTravelMode.Linear) {
                    newParticle.TravelMode = TailTravelMode;
                    newParticle.Velocity = m_Center - m_LastCenter;
                    newParticle.Velocity = Vector2.Normalize(newParticle.Velocity) / 100;
                    newParticle.DisplacementAmplitude = .5f;
                    newParticle.DisplacementFrequency = .3f;
                    newParticle.ShouldTravel = true;
                }
            }

            Vector2 m_LastLocation;

            /// <summary>
            /// Overloaded update functionality to make it only emit if the emitter has moved, so that the tail wont bunch up if it stops moving.
            /// </summary>
            protected override void Update() {

                base.Update();
                if (m_LastLocation != this.Center)
                    Emit();
                m_LastLocation = this.Center;
            }


            /// <summary>
            /// Overloaded UpdateParticle to cause particles to fade as they die.
            /// </summary>
            /// <param name="particle"></param>
            protected override void UpdateParticle(XNACS1Particle particle) {
                int temp = (255 * particle.Life) / m_DefaultLife;
                if (temp > 255) temp = 255;
                else if (temp < 0) temp = 0;
                particle.m_TextureColor.A = (byte)temp;
            }
        }

        /// <summary>
        /// Emits explosions when Explode() is called.
        /// </summary>
        public class ExplodeEmitter : XNACS1ParticleEmitter {
            float m_Radius;
            float m_ExplodeSpeed;

            /// <summary>
            /// Constructor for explode emitter.
            /// Explode emitters do nothing until the Explode() method is called.
            /// </summary>
            /// <param name="center"></param>
            /// <param name="initialParticleLife"></param>
            /// <param name="particleSize"></param>
            /// <param name="tex"></param>
            /// <param name="defaultColor"></param>
            /// <param name="radius">Approximate Radius of explosion</param>
            public ExplodeEmitter(Vector2 center, int initialParticleLife, float particleSize, string tex, Color defaultColor, float radius)
                : base(center, initialParticleLife, particleSize, tex, defaultColor) {
                AutoEmitEnabled = false;
                m_Radius = radius;
                m_ExplodeSpeed = radius / initialParticleLife;
            }


            /// <summary>
            /// Get or set the speed at which particles explode outward.
            /// Default is radius / life;
            /// </summary>
            public float ExplodeSpeed {
                get { return m_ExplodeSpeed; }
                set { m_ExplodeSpeed = value; }
            }

            /// <summary>
            /// Creates particles which explode outward from the center of the emitter at varying velocities.
            /// </summary>
            /// <param name="NumParticles">Number of particles to create.</param>
            public void Explode(int NumParticles) {
                for (int i = 0; i < NumParticles; i++) {
                    Emit();
                }
            }

            /// <summary>
            /// Overloaded NewParticle Method, gives explosion particles initial velocity and sets
            /// ShouldTravel = true.
            /// </summary>
            /// <param name="newParticle"></param>
            protected override void NewParticle(XNACS1Particle newParticle) {
                newParticle.CenterX += XNACS1Base.RandomFloat(-m_Radius / 10, m_Radius / 10);
                newParticle.CenterY += XNACS1Base.RandomFloat(-m_Radius / 10, m_Radius / 10);
                Vector2 randomVelocity = new Vector2(XNACS1Base.RandomFloat(-1, 1), XNACS1Base.RandomFloat(-1, 1));
                randomVelocity.Normalize();
                randomVelocity *= m_ExplodeSpeed * XNACS1Base.RandomFloat(.5f,1);
                newParticle.Velocity = randomVelocity;
                newParticle.ShouldTravel = true;
            }

            /// <summary>
            /// Overloaded UpdateParticle method, makes particles fade out according to what
            /// percentage of the default life they have remaining.
            /// </summary>
            /// <param name="particle"></param>
            protected override void UpdateParticle(XNACS1Particle particle) {
                int temp = (255 * particle.Life) / m_DefaultLife;
                if (temp > 255) temp = 255;
                else if (temp < 0) temp = 0;
                particle.m_TextureColor.A = (byte)temp;
            }
        }

        /// <summary>
        /// Emits a fire
        /// </summary>
        public class FireEmitter : XNACS1ParticleEmitter {
            float m_FireHeight;
            float m_FireWidth;
            float m_Speed;
            Vector2 m_FireDirection;

            /// <summary>
            /// constructor for fire emitter
            /// </summary>
            /// <param name="center"></param>
            /// <param name="initialParticleLife"></param>
            /// <param name="particleSize"></param>
            /// <param name="tex">The texture for the fire particles</param>
            /// <param name="defaultColor"></param>
            /// <param name="fireHeight">The height of the fire</param>
            /// <param name="fireWidth">The width of the fire</param>
            /// <param name="fireDirection">The direction that the fire will travel in.</param>
            // /// <param name="fireDirection">angle of the fire</param>
            public FireEmitter(Vector2 center, int initialParticleLife, float particleSize, string tex, Color defaultColor, float fireHeight,
                float fireWidth, Vector2 fireDirection)
                : base(center, initialParticleLife, particleSize, tex, defaultColor) {
                m_Speed = fireHeight / initialParticleLife;
                m_FireHeight = fireHeight;
                m_FireWidth = fireWidth;
                m_FireDirection = fireDirection;
                m_FireDirection.Normalize();
            }

            #region Accessors
            /// <summary>
            /// gets and sets the angle for the fire
            /// </summary>
            public Vector2 FireDirection
            {
                get { return m_FireDirection; }
                set { m_FireDirection = value; }
            }
            #endregion

            /// <summary>
            /// Creates a new particle with a random start location, and verticle velocity
            /// </summary>1
            /// <param name="newParticle"></param>
            protected override void NewParticle(XNACS1Particle newParticle) {
                newParticle.CenterX += XNACS1Base.RandomFloat(-(m_FireWidth / 2), (m_FireWidth / 2));
                newParticle.CenterY += XNACS1Base.RandomFloat(-1, 1);
                Vector2 velocity = m_FireDirection * XNACS1Base.RandomFloat(0, 1);
                

                velocity *= m_Speed;
                newParticle.Velocity = velocity;
                newParticle.ShouldTravel = true;
            }

            /// <summary>
            /// Overloaded UpdateParticle method, makes particles fade out according to what
            /// percentage of the default life they have remaining.
            /// </summary>
            /// <param name="particle"></param>
            protected override void UpdateParticle(XNACS1Particle particle) {
                int temp = (255 * particle.Life) / m_DefaultLife;
                if (temp > 255) temp = 255;
                else if (temp < 0) temp = 0;
                particle.m_TextureColor.A = (byte)temp;
            }
        }
    }
}