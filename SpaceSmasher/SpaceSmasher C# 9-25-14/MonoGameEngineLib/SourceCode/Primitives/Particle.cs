//*******************************************
// PredefinedEmitter.cs
// Author: Samuel Cook and Ron Cook 
//*******************************************

using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using System;

namespace XNACS1Lib {

    /// <summary>
    /// A particle primitive, inherits from circle but draws using additive blending.
    /// Used in particleEmitters as well.
    /// </summary>
    public class XNACS1Particle : XNACS1Circle {

        internal int m_ParticleLife;

        //Alternate Travel Modes
        internal Vector2 m_Displacement;
        internal ParticleTravelMode m_TravelMode;
        internal float m_DisplacementAmplitude;
        internal float m_DisplacementFrequency;
        internal int m_TravelTime;
        internal Vector2 m_LastCenter;

        /// <summary>
        /// Constructs the particle
        /// </summary>
        /// <param name="center">center of particle</param>
        /// <param name="size">radius of particle</param>
        /// <param name="life">Life parameter, used by emitters, autodecrements by one each tick if larger than zero.</param>
        public XNACS1Particle(Vector2 center, float size, int life)
            : this(center, size, life, null) {}

        /// <summary>
        /// Constructs the particle
        /// </summary>
        /// <param name="center">Center of particle.</param>
        /// <param name="size">Radius of particle.</param>
        /// <param name="life">Life parameter, used by emitters, autodecrements by one each tick if larger than zero.</param>
        /// <param name="tex">Texture to be used by particle.</param>
        public XNACS1Particle(Vector2 center, float size, int life, string tex) : base(center,size,tex) {
            m_ParticleLife = life;
            m_Displacement = Vector2.Zero;
            m_DisplacementAmplitude = 1;
            m_DisplacementFrequency = 1;
            m_TravelTime = 0;
            m_TravelMode = ParticleTravelMode.Linear;

            m_DrawBlendState = BlendState.Additive;
        }

        internal XNACS1Particle(PrimitiveType type) : base(type) 
        {
            m_DrawBlendState = BlendState.Additive;
        }


        /// <summary>
        /// Draws the particle.
        /// </summary>
        protected override void DrawPrimitive() {
            if (m_UseSpriteSheet && m_SpriteSourceRect != null)
                sm_DrawHelper.DrawCircle(Center, Radius, m_SpriteSourceRect, m_Rotate, m_TextureColor, m_Texture);
            else
                sm_DrawHelper.DrawCircle(Center, Radius, null, m_Rotate, m_TextureColor, m_Texture);
        }

        /// <summary>
        /// Gets or sets the life parameter of a particle.
        /// </summary>
        public int Life {
            get { return m_ParticleLife; }
            set { m_ParticleLife = value; }
        }

        /// <summary>
        /// Center of the primitive. 
        /// </summary>
        /// <remarks>
        /// For PrimitiveCircle (center), PrimitiveRectangle (center), PrimitiveSet (undefined).
        /// </remarks>
        /// <seealso cref="XNACS1Rectangle"/>
        /// <seealso cref="XNACS1Circle"/>
        new public Vector2 Center {
            get { return m_Center + m_Displacement; }
            set {
                m_CenterReference = true;
                m_Center = value;
            }
        }

        /// <summary>
        /// Defines if the primitive should move by its current velocity. Defaults to false.
        /// </summary>
        /// <remarks>Must be set to true for a primitive to move.</remarks>
        new public bool ShouldTravel {
            get { return m_ShouldTravel; }
            set {
                m_ShouldTravel = value;
                m_TravelTime = 0;
                m_Displacement = Vector2.Zero;
            }
        }



        /// <summary>
        /// Gets or Sets the primitive's TravelMode if ShouldTravel = True.
        /// Used in conjunction with DisplacementAmplitude and DisplacementFrequecy.
        /// </summary>
        public ParticleTravelMode TravelMode {
            get { return m_TravelMode; }
            set {
                m_TravelMode = value;
            }
        }

        /// <summary>
        /// The amplitude of the Particle TravelMode displacement.  For example, the amplitude of the Sine Wave.
        /// </summary>
        public float DisplacementAmplitude {
            get { return m_DisplacementAmplitude; }
            set { m_DisplacementAmplitude = value; }
        }

        /// <summary>
        /// The frequency of the Particle TravelMode displacement.  The default is 1, values between 0..1 would stretch a sine wave while values >1 would compress it.
        /// </summary>
        public float DisplacementFrequency {
            get { return m_DisplacementFrequency; }
            set { m_DisplacementFrequency = value; }
        }

        /// <summary>
        /// Overloaded TravelPrimitive for Particles.
        /// Moves the particle during each update cycle. This function is automacially called for all visible 
        /// particle. Default behavior is to move the particle if:
        ///     . Primitive is visible (invivislble primitive is not updated)
        ///     . ShouldTravel is true
        ///     . HasNonZeroVelocity() is true 
        /// This method moves the particle in the direction of its velocity and also displaces it according to the ParticleTravelMode chosen.
        /// </summary>
        public override void TravelPrimitive() {
            if (m_ParticleLife > 0) m_ParticleLife--;
            if (Visible && ShouldTravel) {
                m_TravelTime++;
                if (HasNonZeroVelocity()) {
                    m_Center += Velocity;
                }

                if (m_TravelMode != ParticleTravelMode.Linear) {
                    Vector2 velocityNormal = Vector2.Zero;
                    Vector2 velocityFront;
                    if (m_LastCenter == m_Center) {
                        velocityFront = VelocityDirection;
                    }
                    else {
                        velocityFront = (m_Center - m_LastCenter);
                    }
                    velocityNormal.X = -(velocityFront).Y;
                    velocityNormal.Y = (velocityFront).X;
                    velocityNormal.Normalize();

                    //not dependent on velocity;
                    switch (m_TravelMode) {
                        case ParticleTravelMode.RandomDisplace:
                            m_Displacement += new Vector2(XNACS1Base.RandomFloat(-1, 1), XNACS1Base.RandomFloat(-1, 1)) * m_DisplacementFrequency;
                            if (m_Displacement.Length() > m_DisplacementAmplitude)
                                m_Displacement.Normalize();
                            break;
                        case ParticleTravelMode.TransverseSine:
                            m_Displacement = velocityNormal * m_DisplacementAmplitude * (float)Math.Sin(m_DisplacementFrequency * m_TravelTime);
                            break;
                        case ParticleTravelMode.CompressionSine:
                            m_Displacement = Vector2.Normalize(m_Center - m_LastCenter) * m_DisplacementAmplitude * (float)Math.Sin(m_DisplacementFrequency * m_TravelTime);
                            break;

                        case ParticleTravelMode.VerticalSine:
                            m_Displacement = Vector2.UnitY * m_DisplacementAmplitude * (float)Math.Sin(m_DisplacementFrequency * m_TravelTime);
                            break;

                        case ParticleTravelMode.HorizontalSine:
                            m_Displacement = Vector2.UnitX * m_DisplacementAmplitude * (float)Math.Sin(m_DisplacementFrequency * m_TravelTime);
                            break;
                    }
                }
                m_LastCenter = m_Center;
            }
        }



    }
}
