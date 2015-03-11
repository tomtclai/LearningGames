//Author: Michael Letter

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace CustomWindower.CoreEngine{
    /// <summary>
    /// Defines and draw a Line
    /// </summary>
    public class Line : Drawable{
        /// <summary>This line will only be drawn if this bool is true</summary>
        public bool visible = true;
        /// <summar>Represents the color of the line</summar></summary>
        public Color lineColor = Color.Red;
        /// <summary>The width of the line, Note, if this value is negative this line will not apear</summary>
        public float lineWidth = 3;
        /// <summary>Stores the number of Points in linePoints that are active</summary>
        private int numberOfLinePoints = 0; 
        /// <summary> Stores all of the points that compose the Line </summary>
        private PointF[] linePoints = new PointF[10];

        /// <summary>
        /// Will return the number of points that currently Define the Line
        /// </summary>
        /// <returns></returns>
        public int getNumberOfPoints() {
            return numberOfLinePoints;
        }
        /// <summary>
        /// Will set the Given point to equal to the point defined the given index if said index exists
        /// </summary>
        /// <param name="index">the index of the point defining the line</param>
        /// <param name="host">The point that will contain the given </param>
        /// <returns>If True the host was set Successfully, if False, no point at the given index exists</returns>
        public bool getPoint(int index, ref PointF host){
            if(index >= 0 && index < numberOfLinePoints){
                host = linePoints[index];
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will set the Point at the given index to match the given point
        /// </summary>
        /// <param name="index">The index of the target point</param>
        /// <param name="newPoint">The point to replace the given point</param>
        /// <returns>If True all changes made, if False no changes were made</returns>
        public bool setPoint(int index, PointF newPoint) {
            if(index >= 0 && index < numberOfLinePoints){
                linePoints[index].X = newPoint.X;
                linePoints[index].Y = newPoint.Y;
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will add the given Point to the end of the list of points that defines this line segment
        /// </summary>
        /// <param name="newPoint"></param>
        public void addPoint(PointF newPoint) {
            setNumberOfPoints(numberOfLinePoints + 1);
            setPoint(numberOfLinePoints - 1, newPoint);
        }
        /// <summary>
        /// Will set the number of points available in the line
        /// </summary>
        /// <returns>If True The change was made Successfully, if False, no changes were made</returns>
        public bool setNumberOfPoints(int count) {
            if(count >= 0){
                numberOfLinePoints = count;
                //Expand Array
                if(numberOfLinePoints > linePoints.Length){
                    PointF[] newPoints = new PointF[numberOfLinePoints + 10];
                    int loop = 0;
                    //migrate points
                    while(loop < linePoints.Length){
                        newPoints[loop].X = linePoints[loop].X;
                        newPoints[loop].Y = linePoints[loop].Y;
                        loop++;
                    }
                    //initialize
                    while(loop < newPoints.Length){
                        newPoints[loop].X = 0;
                        newPoints[loop].Y = 0;
                        loop++;
                    }
                    linePoints = newPoints;
                }
            }
            return false;
        }
        public override void paint(Camera drawLocation) {
            if(drawLocation != null && visible && lineWidth >= 0 && numberOfLinePoints > 0){
                drawLocation.paintLine(linePoints, numberOfLinePoints, lineColor, lineWidth, null);
            }
        }
    }
}
