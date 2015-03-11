using System;
using Microsoft.Xna.Framework;


namespace XNACS1Lib
{

    /// <summary>
    /// Oriented BoundingBox 2D collision: copied from
    /// http://www.flipcode.com/archives/2D_OBB_Intersection.shtml
    /// Original Author: Morgan McGuire morgan@cs.brown.edu
    /// Implementation based on: Stefan Gottschalk's PhD Thesis 
    ///         Collision Queries using Oriented Bounding Boxes, 
    ///         Ph.D. Thesis, 
    ///         Department of Computer Science, 
    ///         University of North Carolina at Chapel Hill, 1999
    /// </summary>
    internal class OBB2D
    {
        internal static bool OBB2DIntersect(XNACS1Primitive a, XNACS1Primitive b, out Vector2 pos)
        {
            pos = Vector2.Zero;
            OBB2D aOBB = new OBB2D(a);
            OBB2D bOBB = new OBB2D(b);
            bool touched = aOBB.overlaps1Way(bOBB) && bOBB.overlaps1Way(aOBB);

            if (touched)
            {
                pos = a.Center + b.Center;
                pos /= 2.0f;
            }
            return touched;
        }

        internal static void Draw(XNACS1Primitive p, XNACS1LibDrawHelper dh)
        {
            OBB2D a = new OBB2D(p);
            dh.DrawLineSegments(a.m_Corner[0], a.m_Corner[1]);
            dh.DrawLineSegments(a.m_Corner[1], a.m_Corner[2]);
            dh.DrawLineSegments(a.m_Corner[2], a.m_Corner[3]);
            dh.DrawLineSegments(a.m_Corner[3], a.m_Corner[0]);
        }

        private Vector2[] m_Corner;   // Corners of the box, where 0 is the lower left.
        private Vector2[] m_Axis;     // Two edges of the box extended away from corner[0]. */
        private double[] m_Origin;     // origin[a] = corner[0].dot(axis[a]); 

        /** Returns true if other overlaps one dimension of this. */
        private bool overlaps1Way(OBB2D other)
        {
            for (int a = 0; a < 2; ++a)
            {

                double t = Vector2.Dot(m_Axis[a], other.m_Corner[0]);

                // Find the extent of box 2 on axis a
                double tMin = t;
                double tMax = t;

                for (int c = 1; c < 4; ++c)
                {
                    t = Vector2.Dot(m_Axis[a], other.m_Corner[c]);

                    if (t < tMin)
                    {
                        tMin = t;
                    }
                    else if (t > tMax)
                    {
                        tMax = t;
                    }
                }

                // We have to subtract off the origin

                // See if [tMin, tMax] intersects [0, 1]
                if ((tMin > 1.0 + m_Origin[a]) || (tMax < m_Origin[a]))
                {
                    // There was no intersection along this dimension;
                    // the boxes cannot possibly overlap.
                    return false;
                }
            }

            // There was no dimension along which there is no intersection.
            // Therefore the boxes overlap.
            return true;
        }


        /** Updates the axes after the corners move.  Assumes the
            corners actually form a rectangle. */
        private void computeAxes()
        {
            m_Axis[0] = m_Corner[1] - m_Corner[0];
            m_Axis[1] = m_Corner[3] - m_Corner[0];

            // Make the length of each axis 1/edge length so we know any
            // dot product must be less than 1 to fall within the edge.

            for (int a = 0; a < 2; ++a)
            {
                m_Axis[a] /= m_Axis[a].LengthSquared();
                m_Origin[a] = Vector2.Dot(m_Corner[0], m_Axis[a]);
            }
        }

        public OBB2D(XNACS1Primitive p)
        {
            m_Axis = new Vector2[2];
            m_Corner = new Vector2[4];
            m_Origin = new double[2];

            double angle = p.RotateAngle * Math.PI / 180.0f;

            Vector2 X = new Vector2((float)Math.Cos(angle), (float)Math.Sin(angle));
            Vector2 Y = new Vector2((float)-Math.Sin(angle), (float)Math.Cos(angle));

            X *= p.SizeX / 2;
            Y *= p.SizeY / 2;

            m_Corner[0] = p.Center - X - Y;
            m_Corner[1] = p.Center + X - Y;
            m_Corner[2] = p.Center + X + Y;
            m_Corner[3] = p.Center - X + Y;

            computeAxes();
        }
    }
}