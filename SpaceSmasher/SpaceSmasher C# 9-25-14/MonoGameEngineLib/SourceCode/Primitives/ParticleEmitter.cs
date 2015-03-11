//*******************************************
// ParticleEmitter.cs
// Author:   Samuel Cook and Ron Cook
//  Description:    Particle emitter class meant to be
//      inhertied from for custom particle emitters.
//*******************************************

using System;
using System.IO;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
// using Microsoft.Xna.Framework.Storage;
using Microsoft.Xna.Framework.Content;

namespace XNACS1Lib {
    /// <summary>
    /// Inherits from XNACS1PrimitiveSet, allows a user to define behavior for a group of particles.
    /// </summary>
    public abstract partial class XNACS1ParticleEmitter : XNACS1Particle {
        internal int m_EmitFrequency = 1;
        internal bool m_AutoEmit = true;
        internal int m_EmitTimer = 1;
        internal int m_DefaultLife;
        internal float m_DefaultSize;
        internal int m_ParticlesPerEmit;
        internal Color m_DefaultColor;
        internal string m_DefaultTexture;
        internal List<XNACS1Particle> m_ParticleList;

        internal XNACS1Primitive m_AttachedPrimitive;

        internal bool m_AutoRemoveDead = true;

        /// <summary>
        /// Constructs a particle Emitter.
        /// </summary>
        /// <param name="center">The center of the particle Emitter</param>
        /// <param name="initialParticleLife">The life in ticks of newly emitted particles.</param>
        /// <param name="particleSize">The radius of newly emitted particles.</param>
        /// <param name="defaultTex">The default texture of newly emitted particles.</param>
        /// <param name="defaultTint">The default tint of newly emitted particles.</param>
        public XNACS1ParticleEmitter(Vector2 center, int initialParticleLife, float particleSize, string defaultTex, Color defaultTint) : base(PrimitiveType.PrimitiveEmitter) {
            m_Center = center;
            m_DefaultLife = initialParticleLife;
            m_DefaultSize = particleSize;
            m_DefaultTexture = defaultTex;
            m_DefaultColor = defaultTint;
            m_ParticlesPerEmit = 1;
            m_ParticleList = new List<XNACS1Particle>();
            AddEmitterToUpdateSet();
        }


#region Accessors
        /// <summary>
        /// Gets or Sets the primitive that this emitter is attached to.
        /// The emitter's center will be automatically updated each tick to match
        /// the attached primitive's center.
        /// </summary>
        public XNACS1Primitive AttachedPrimitive {
            get { return m_AttachedPrimitive; }
            set { m_AttachedPrimitive = value; }
        }

        /// <summary>
        /// Gets or Sets the number of particles to be emitted per emit cycle if AutoEmitEnabled = true.
        /// </summary>
        public int ParticlesPerEmit
        {
            get { return m_ParticlesPerEmit; }
            set { m_ParticlesPerEmit = value; }
        }

        /// <summary>
        /// Gets or sets whether or not particles with zero life remaining will be automatically
        /// deleted from the particle emitter set.
        /// Setting a particle's life to negative will ensure it is never deleted via this functionality.
        /// </summary>
        public bool AutoRemoveDeadParticles {
            get { return m_AutoRemoveDead; }
            set { m_AutoRemoveDead = value; }
        }

        ///// <summary>
        ///// List of all the particles in the emitter.
        ///// </summary>
        //public List<XNACS1Particle> ParticleList {
        //    get { return this.m_ParticleList; }
        //}

        /// <summary>
        /// Gets or Sets how many ticks between calls of EmitParticle if AutoEmitEnable = True.
        /// <seealso cref="AutoEmitEnabled"/>
        /// </summary>
        public int EmitFrequency {
            get { return this.m_EmitFrequency; }
            set { m_EmitFrequency = value; }
        }

        /// <summary>
        /// Gets or Sets the default life in ticks for emitted particles.
        /// </summary>
        public int InitialLife {
            get { return m_DefaultLife; }
            set { m_DefaultLife = value; }
        }

        /// <summary>
        /// Gets or Sets The default size of emitted particles.
        /// </summary>
        public float InitialSize {
            get { return m_DefaultSize; }
            set { m_DefaultSize = value; }
        }

        /// <summary>
        /// Gets or Sets The default texture of emitted particles.
        /// </summary>
        public string DefaultTexture {
            get { return m_DefaultTexture; }
            set { m_DefaultTexture = value; }
        }

        /// <summary>
        /// Gets or Sets The default color of emitted particles.
        /// </summary>
        public Color DefaultColor {
            get { return m_DefaultColor; }
            set { m_DefaultColor = value; }
        }

        /// <summary>
        /// Gets or Sets whether or not particles should automatically be emitted every EmitFrequency ticks.
        /// <seealso cref="EmitFrequency"/>
        /// </summary>
        public bool AutoEmitEnabled {
            get { return m_AutoEmit; }
            set { m_AutoEmit = value; }
        }
#endregion

        /// <summary>
        /// Removes the emitter from the emitter update list.
        /// To be called before removing all references to the emitter so that
        /// garbage collection can collect it.
        /// </summary>
        public void RemoveEmitterFromUpdateSet() {
            XNACS1Primitive.sm_DrawHelper.RemoveEmitter(this);
        }

        /// <summary>
        /// Adds the emitter to the emitter update list.  This will cause the emitter's
        /// UpdateEmitter function to be called once per tick and it to emit particles as
        /// defined.
        /// Newly created emitters are automatically in the update set.
        /// </summary>
        public void AddEmitterToUpdateSet() {
            XNACS1Primitive.sm_DrawHelper.AddEmitter(this);
        }


        internal void CallUpdate() {
            Update();
        }

        /// <summary>
        /// Controls updates for AttachedPrimitive, AutoRemoveDead, and AutoEmit.  
        /// Overload at your own risk!
        /// </summary>
        protected virtual void Update() {
            if (AttachedPrimitive != null)
                m_Center = m_AttachedPrimitive.Center;

            for (int i = 0; i < m_ParticleList.Count; i++) {
                if (m_ParticleList[i] != null) {
                    if (m_ParticleList[i].Life == 0 && m_AutoRemoveDead) {
                        KillParticle(i);
                    }
                    else {
                        m_ParticleList[i].TravelPrimitive();
                        UpdateParticle(m_ParticleList[i]);
                    }
                }
            }


            if (m_AutoEmit) {
                m_EmitTimer -= 1;
                if (m_EmitTimer <= 0) {
                    for (int i = 0; i < m_ParticlesPerEmit; i++ )
                        Emit();
                    m_EmitTimer = m_EmitFrequency;
                }
            }
        }

        /// <summary>
        /// Removes a particle from the emitter list.
        /// </summary>
        /// <param name="i">Index of particle</param>
        protected void KillParticle(int i) {
            m_ParticleList.RemoveAt(i);
        }
        /// <summary>
        /// Removes a particle from the emitter list.
        /// </summary>
        /// <param name="particle">Particle to remove.</param>
        protected void KillParticle(XNACS1Particle particle) {
            m_ParticleList.Remove(particle);
        }

        /// <summary>
        /// When this method is called a new particle will be added to the emitter set and NewParticle() will be called to change the
        /// initial parameters of the particle if needed.
        /// </summary>
        protected void Emit() {
            XNACS1Particle particle = new XNACS1Particle(m_Center, m_DefaultSize, m_DefaultLife, m_DefaultTexture);
            particle.m_TextureColor = m_DefaultColor;
            particle.RemoveFromAutoDrawSet();
            m_ParticleList.Add(particle);
            NewParticle(particle);
        }

        /// <summary>
        /// Draws all the particles in the emitter
        /// </summary>
        protected override void DrawPrimitive() {
            XNACS1Primitive p;
            for (int i = 0; i < m_ParticleList.Count; i++) {
                p = m_ParticleList[i];
                if (m_ParticleList[i] != null)
                    p.Draw();
            }
        }

        /// <summary>
        /// This is where you define the behavior of particles in the emitter.  This method
        /// will be called once a tick for each particle if the emitter is in the Emitter
        /// Update Set.
        /// This is an abstract method and must be overloaded.
        /// <seealso cref="AddEmitterToUpdateSet"/>
        /// <seealso cref="RemoveEmitterFromUpdateSet"/>
        /// </summary>
        /// <param name="particle">The particle being updated</param>
        protected abstract void UpdateParticle(XNACS1Particle particle);

        /// <summary>
        /// This method is called by the Emit method whenever a particle is created.  By default
        /// particles are created at the Center location of the emitter with InitialLife, InitialSize, DefaultTexture, and DefaultColor.
        /// Newly created particles also have shouldtravel set to false and velocity = 0.
        /// By overloading this method you can change these properties to make particles emit in different ways.
        /// <param name="newParticle">Particle that has just been created and added to the set.</param>
        /// <seealso cref="AutoEmitEnabled"/>
        /// <seealso cref="InitialLife"/>
        /// <seealso cref="InitialSize"/>
        /// <seealso cref="DefaultTexture"/>
        /// <seealso cref="DefaultColor"/>
        /// <seealso cref="EmitFrequency"/>
        /// <seealso cref="Emit"/>
        /// <seealso cref="UpdateParticle"/>
        /// </summary>
        protected virtual void NewParticle(XNACS1Particle newParticle) {
            
        }

    }
}
