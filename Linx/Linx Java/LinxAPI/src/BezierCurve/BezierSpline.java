package BezierCurve;

// Original file information:

// <copyright file="BezierSpline.cs" company="Oleg V. Polikarpotchkin">
// Copyright © 2008-2009 Oleg V. Polikarpotchkin. All Right Reserved
// </copyright>
// <author>Oleg V. Polikarpotchkin</author>
// <email>ov-p@yandex.ru</email>
// <date>2008-12-17</date>
// <summary>
// Methods to calculate Bezier Spline points.
// Modified: Peter Lee (peterlee.com.cn < at > gmail.com)
//   Update: 2009-03-16
// </summary>

// Updated/Modified by Brian Hecox 5/20/13
// Ported to Java by Brian Hecox 8/09/13

/// <summary>
/// Bezier Spline methods
/// </summary>
/// <remarks>
/// Modified: Peter Lee (peterlee.com.cn < at > gmail.com)
///   Update: 2009-03-16
/// 
/// see also:
/// Draw a smooth curve through a set of 2D points with Bezier primitives
/// http://www.codeproject.com/KB/graphics/BezierSpline.aspx
/// By Oleg V. Polikarpotchkin
/// 
/// Algorithm Descripition:
/// 
/// To make a sequence of individual Bezier curves to be a spline, we
/// should calculate Bezier control points so that the spline curve
/// has two continuous derivatives at knot points.
/// 
/// Note: `[]` denotes subscript
///        `^` denotes supscript
///        `'` denotes first derivative
///       `''` denotes second derivative
///       
/// A Bezier curve on a single interval can be expressed as:
/// 
/// B(t) = (1-t)^3 P0 + 3(1-t)^2 t P1 + 3(1-t)t^2 P2 + t^3 P3          (*)
/// 
/// where t is in [0,1], and
///     1. P0 - first knot point
///     2. P1 - first control point (close to P0)
///     3. P2 - second control point (close to P3)
///     4. P3 - second knot point
///     
/// The first derivative of (*) is:
/// 
/// B'(t) = -3(1-t)^2 P0 + 3(3t^2–4t+1) P1 + 3(2–3t)t P2 + 3t^2 P3
/// 
/// The second derivative of (*) is:
/// 
/// B''(t) = 6(1-t) P0 + 6(3t-2) P1 + 6(1–3t) P2 + 6t P3
/// 
/// Considering a set of piecewise Bezier curves with n+1 points
/// (Q[0..n]) and n subintervals, the (i-1)-th curve should connect
/// to the i-th one:
/// 
/// Q[0] = P0[1],
/// Q[1] = P0[2] = P3[1], ... , Q[i-1] = P0[i] = P3[i-1]  (i = 1..n)   (@)
/// 
/// At the i-th subinterval, the Bezier curve is:
/// 
/// B[i](t) = (1-t)^3 P0[i] + 3(1-t)^2 t P1[i] + 
///           3(1-t)t^2 P2[i] + t^3 P3[i]                 (i = 1..n)
/// 
/// applying (@):
/// 
/// B[i](t) = (1-t)^3 Q[i-1] + 3(1-t)^2 t P1[i] + 
///           3(1-t)t^2 P2[i] + t^3 Q[i]                  (i = 1..n)   (i)
///           
/// From (i), the first derivative at the i-th subinterval is:
/// 
/// B'[i](t) = -3(1-t)^2 Q[i-1] + 3(3t^2–4t+1) P1[i] +
///            3(2–3t)t P2[i] + 3t^2 Q[i]                 (i = 1..n)
/// 
/// Using the first derivative continuity condition:
/// 
/// B'[i-1](1) = B'[i](0)
/// 
/// we get:
/// 
/// P1[i] + P2[i-1] = 2Q[i-1]                             (i = 2..n)   (1)
/// 
/// From (i), the second derivative at the i-th subinterval is:
/// 
/// B''[i](t) = 6(1-t) Q[i-1] + 6(3t-2) P1[i] +
///             6(1-3t) P2[i] + 6t Q[i]                   (i = 1..n)
/// 
/// Using the second derivative continuity condition:
/// 
/// B''[i-1](1) = B''[i](0)
/// 
/// we get:
/// 
/// P1[i-1] + 2P1[i] = P2[i] + 2P2[i-1]                   (i = 2..n)   (2)
/// 
/// Then, using the so-called "natural conditions":
/// 
/// B''[1](0) = 0
/// 
/// B''[n](1) = 0
/// 
/// to the second derivative equations, and we get:
/// 
/// 2P1[1] - P2[1] = Q[0]                                              (3)
/// 
/// 2P2[n] - P1[n] = Q[n]                                              (4)
/// 
/// From (1)(2)(3)(4), we have 2n conditions for n first control points
/// P1[1..n], and n second control points P2[1..n].
/// 
/// Eliminating P2[1..n], we get (be patient to get :-) a set of n
/// equations for solving P1[1..n]:
/// 
///   2P1[1]   +  P1[2]   +            = Q[0] + 2Q[1]
///    P1[1]   + 4P1[2]   +    P1[3]   = 4Q[1] + 2Q[2]
///  ...
///    P1[i-1] + 4P1[i]   +    P1[i+1] = 4Q[i-1] + 2Q[i]
///  ...
///    P1[n-2] + 4P1[n-1] +    P1[n]   = 4Q[n-2] + 2Q[n-1]
///               P1[n-1] + 3.5P1[n]   = (8Q[n-1] + Q[n]) / 2
///  
/// From this set of equations, P1[1..n] are easy but tedious to solve.
/// </remarks>

public class BezierSpline
{
    public Vector2[] userKnots;
    public Vector2[] firstControlPoints;
    public Vector2[] secondControlPoints;
    
    public int segments;

    /// <summary>
    /// Get open-ended Bezier Spline Control Points.
    /// </summary>
    /// <param name="knots">Input Knot Bezier spline points.</param>
    /// <exception cref="ArgumentNullException"><paramref name="knots"/> parameter must be not null.</exception>
    /// <exception cref="ArgumentException"><paramref name="knots"/> array must containg at least two points.</exception>
    public void setupCurve(Vector2[] knots)
    {
        userKnots = null;
        firstControlPoints = null;
        secondControlPoints = null;

        if (knots == null)
            throw new Error("Knots must not be null");
        int n = knots.length - 1;
        if (n < 1)
            throw new Error("At least two knot points required");
        userKnots = knots;
        if (n == 1)
        { // Special case: Bezier curve should be a straight line.
            firstControlPoints = new Vector2[1];
            
            Vector2 temp0 = knots[0].clone();
            Vector2 temp1 = knots[1].clone();
            // 3P1 = 2P0 + P3
            firstControlPoints[0] = temp1.add(temp0.scale(2f)).divide(3f);

            secondControlPoints = new Vector2[1];
            // P2 = 2P1 – P0
            secondControlPoints[0].set(firstControlPoints[0]);
            secondControlPoints[0].scale(2).sub(knots[0]);

            segments = 1;
            return;
        }

        // Calculate first Bezier control points
        // Right hand side vector
        Vector2[] rhs = new Vector2[n];

        // Set right hand side X/Y values
        for (int i = 1; i < n - 1; ++i)
            rhs[i] = knots[i].clone().scale(4).add(knots[i + 1].clone().scale(2));
        rhs[0] = knots[0].clone().add(knots[1].clone().scale(2));
        rhs[n - 1] = knots[n - 1].clone().scale(8).add(knots[n].clone()).divide(2f);
        // Get first control points X/Y-values
        firstControlPoints = getFirstControlPoints(rhs);

        // Fill output arrays.
        secondControlPoints = new Vector2[n];
        for (int i = 0; i < n; ++i)
        {
            // Second control point
            if (i < n - 1)
                secondControlPoints[i] = knots[i + 1].clone().scale(2).sub(firstControlPoints[i + 1]);
            else
                secondControlPoints[i] = knots[n].clone().add(firstControlPoints[n - 1]).divide(2f);
        }

        // set the number of bezier segments we have now
        segments = firstControlPoints.length;
    }

    /// <summary>
    /// Solves a tridiagonal system for one of coordinates (x or y) of first Bezier control points.
    /// </summary>
    /// <param name="rhs">Right hand side vector.</param>
    /// <returns>Solution vector.</returns>
    private Vector2[] getFirstControlPoints(Vector2[] rhs)
    {
        int n = rhs.length;
        Vector2[] x = new Vector2[n]; // Solution vector.
        Vector2[] tmp = new Vector2[n]; // Temp workspace.

        Vector2 b = new Vector2(2f);
        x[0] = rhs[0].clone().divide(b);
        for (int i = 1; i < n; i++) // Decomposition and forward substitution.
        {
            tmp[i] = new Vector2(1f).divide(b);
            b = new Vector2(i < n - 1 ? 4.0f : 3.5f).sub(tmp[i]);
            x[i] = rhs[i].clone().sub(x[i - 1]).divide(b);
        }
        for (int i = 1; i < n; i++)
            x[n - i - 1].sub(tmp[n - i].mult(x[n - i])); // Backsubstitution.

        return x;
    }

    // help from http://www.blitzbasic.com/codearcs/codearcs.php?code=1523
    public float getSpeed(float tCurve)
    {
        if (tCurve < 0 || tCurve >= 1)
            return 0;

        // multiply by segments to get values for the specific segment vs. 0-1t
        int currentSegment = (int)(tCurve * segments);
        float t = (tCurve * segments) % 1;
        float b = 1 - t;

        // derivative of bezier (awful looking without operator overloading...)
        Vector2 v = userKnots[currentSegment].clone().scale(-3).scale(b * b).add(
                    firstControlPoints[currentSegment].clone().scale(3).scale(b * (b - 2 * t))).add(
                    secondControlPoints[currentSegment].clone().scale(3).scale(t * (2 * b - t))).add(
                    userKnots[currentSegment + 1].clone().scale(3 * t * t));

        // 1/length gives greater 't' jump for greater slope of 'v'
        float magnitude = v.length(); 
        float speed = .05f / magnitude;

        // divide by segments to give back 't' in 0-1 terms
        return speed / segments;
    }

    public Vector2 getDirection(float tCurve)
    {
        if (tCurve < 0 || tCurve >= 1)
            return new Vector2(0f);

        // multiply by segments to get values for the specific segment vs. 0-1t
        int currentSegment = (int)(tCurve * segments);
        float t = (tCurve * segments) % 1;
        float b = 1 - t;

        // derivative of bezier
        Vector2 v = userKnots[currentSegment].clone().scale(-3).scale(b * b).add(
                    firstControlPoints[currentSegment].clone().scale(3).scale(b * (b - 2 * t))).add(
                    secondControlPoints[currentSegment].clone().scale(3).scale(t * (2 * b - t))).add(
                    userKnots[currentSegment + 1].clone().scale(3 * t * t));

        // return normalized v for direction
        v.normalize();
        return v;
    }



    // get the x/y of a specific t location on the bezier curve
    public Vector2 getPoint(float tCurve)
    {
        if (tCurve < 0 || tCurve >= 1)
            return new Vector2(0f);

        // multiply by segments to get values for the specific segment vs. 0-1t
        int currentSegment = (int)(tCurve * segments);
        float t = (tCurve * segments) % 1;

        // cubic bezier curve function
        Vector2 c = firstControlPoints[currentSegment].clone().sub(userKnots[currentSegment]).scale(3);
        Vector2 b = secondControlPoints[currentSegment].clone().sub(firstControlPoints[currentSegment]).scale(3).sub(c);
        Vector2 a = userKnots[currentSegment + 1].clone().sub(userKnots[currentSegment]).sub(c).sub(b);            
        Vector2 point = a.scale(t*t*t).add(b.scale(t*t)).add(c.scale(t)).add(userKnots[currentSegment]);

        return point;
    }
}
