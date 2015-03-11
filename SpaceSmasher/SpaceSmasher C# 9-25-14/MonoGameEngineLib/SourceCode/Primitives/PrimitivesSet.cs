using System.Collections.Generic;


namespace XNACS1Lib
{

    /// <summary>
    /// XNACS1PrimitiveSet supports hiding/unhiding a collection of Primitives (Circle, or Rectangle). 
    /// </summary>
    /// <remarks>
    /// This class is designed
    /// to be used where related primitives can be added/removed from the AutoDrawSet as a group. Typically, you would insert
    /// related primitives (e.g., all primitives associated with a car) into a PrimitiveSet. 
    /// By default all the prmitives in the
    /// PrimitiveSet will be drawn in the application window. 
    /// When required to hide this group of primitives (e.g., car moved out of world bound), you can
    /// remove the PrimitiveSet from the AutoDrawSet resutling in hiding all the primitives inthe set. 
    /// Later on, you can add this PrimitiveSet back into the AutoDrawSet to unhide the primitives (e.g.,
    /// the car moved back into the world bound). Removing all primitives from this set will result in the primitives
    /// being removed from the AutoDrawSet.
    /// </remarks>
    public class XNACS1PrimitiveSet 
    {
        internal List<XNACS1Primitive> m_DrawSet;

        /// <summary>
        /// Defines a collection structure for storing a group of XNACS1Primitives (Circle, Rectangle, or other PrimitiveSet).
        /// </summary>
        public XNACS1PrimitiveSet()
        {
            m_DrawSet = new List<XNACS1Primitive>();
        }

        /// <summary>
        /// Adds the thePrimitive to this set.
        /// </summary>
        /// <param name="thePrimitive">The primitive to be added.</param>
        public void AddToSet(XNACS1Primitive thePrimitive)
        {
            if (!m_DrawSet.Contains(thePrimitive))
                m_DrawSet.Add(thePrimitive);
        }

        /// <summary>
        /// Remove thePrimitive from this set. Will also remove the primitive from the AutoDrawSet.
        /// </summary>
        /// <param name="thePrimitive">The primitive to be removed from this set.</param>
        public void RemoveFromSet(XNACS1Primitive thePrimitive)
        {
            m_DrawSet.Remove(thePrimitive);
            thePrimitive.RemoveFromAutoDrawSet();
        }

        /// <summary>
        /// Remove all the prmitives from this set. Will aslo remove all the primitives from the AutoDrawSet.
        /// </summary>
        public void RemoveAllFromSet()
        {
            foreach (XNACS1Primitive p in m_DrawSet)
                p.RemoveFromAutoDrawSet();

            m_DrawSet.Clear();
        }

        /// <summary>
        /// Returns the number of primitives in this set.
        /// </summary>
        /// <returns>Numver of primitives in this set.</returns>
        public int SetSize()
        {
            return m_DrawSet.Count;
        }

        /// <summary>
        /// Determs if thePrimitive is already in the draw set.
        /// </summary>
        /// <param name="thePrimitive">Primitive to be tested</param>
        /// <returns>True: if thePrmitive is already in the set, else returns False.</returns>
        public bool IsInDrawSet(XNACS1Primitive thePrimitive)
        {
            return m_DrawSet.Contains(thePrimitive);
        }

        /// <summary>
        /// Add all primitives in the set to the AutoDrawSet. If the primitives in this set are already in the AutoDrawSet
        /// then nothing will happen.
        /// </summary>
        public virtual void AddAllToAutoDraw()
        {
            foreach (XNACS1Primitive p in m_DrawSet)
                p.AddToAutoDrawSet();
        }

        /// <summary>
        /// Remove all primitives in this set from the AutoDrawSet. After this call, none of the primitives in this set
        /// will be visible in the appliation window.
        /// </summary>
        public virtual void RemoveAllFromAutoDrawSet()
        {
            foreach (XNACS1Primitive p in m_DrawSet)
                p.RemoveFromAutoDrawSet();
        }
    }
}