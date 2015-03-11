using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using CustomWindower.CoreEngine;
using GhostFinder.Interface;

namespace GhostFinder.GhostEngine{
    public class PowerBar : DrawRectangle{
      private DrawRectangle fillBar = null;
      private DrawRectangle fillBarBackground = null;
      private float percent = 1.0f;
      private float edgeSize = 0.25f;
      private bool fillOnX = true;

      private float maxScore = 10.0f;

      public PowerBar() {
        base.fillColor = Color.Black;
        base.edgeColor = Color.FromArgb(0, 0, 0, 0);

        fillBarBackground = new DrawRectangle();
        fillBarBackground.fillColor = Color.DarkGray;
        fillBarBackground.edgeColor = Color.FromArgb(0, 0, 0, 0);
    
        setPercent(1.0f);
      }

      public void setVisibility(bool visible){
	      fillBar.visible = visible;
	      fillBarBackground.visible = visible;
	      base.visible = visible;
      }
      public void setMaxScore(float value) {
        maxScore = value;
      }

      public void setToVertical() {
        fillOnX = false;
        setPercent(percent);
      }

      public void setToHorizontal() {
        fillOnX = true;
        setPercent(percent);
      }

      public float getScore() {
        return (getPercent() * maxScore);
      }

      public float getPercent() {
        return percent;
      }

      public void increaseScore(float value) {
        increasePercent(value / maxScore);
      }

      public void setScore(float value) {
        setPercent(value / maxScore);
      }

      public void increasePercent(float value) {
        setPercent(percent + value);
      }
      
      public void setPercent(float value) {
        fillBarBackground.setCenter(base.getCenterX(), base.getCenterY());
        fillBarBackground.setSize(base.getWidth() - (edgeSize * 2.0f), base.getHeight() - (edgeSize * 2.0f));

        percent = value;

        if(fillBar == null) {
          fillBar = new DrawRectangle();
          fillBar.fillColor = Color.Chartreuse;
          fillBar.edgeColor = Color.FromArgb(0, 0, 0, 0);
        }

        if(percent <= 0.0f) {
          percent = 0.0f;
          fillBar.rectangleVisible = false;
        }
        else {
            if (percent > 1.0f) {
                percent = 1.0f;
            }
            fillBar.rectangleVisible = true;
        }

        float bufferSizeX = edgeSize;
        float bufferSizeY = edgeSize;

        float barSizeX = base.getWidth() - (bufferSizeX * 2.0f);
        float barSizeY = base.getHeight() - (bufferSizeY * 2.0f);

        if(fillOnX) {
          barSizeX *= percent;

          fillBar.setWidth(barSizeX);
          fillBar.setHeight(barSizeY);

          fillBar.setCenterX(base.getCenterX() - (base.getWidth() * 0.5f) + bufferSizeX +
              (barSizeX * 0.5f));
          fillBar.setCenterY(base.getCenterY());
        }
        else {
          barSizeY *= percent;

          fillBar.setWidth(barSizeX);
          fillBar.setHeight(barSizeY);

          fillBar.setCenterX(base.getCenterX());
          fillBar.setCenterY(base.getCenterY() + (base.getHeight() * 0.5f) - bufferSizeY -
              (barSizeY * 0.5f));
        }
      }
      public override void paint(Camera drawLocation){
          base.paint(drawLocation);
          fillBarBackground.paint(drawLocation);
	      if(fillBar != null){
              fillBar.paint(drawLocation);
	      }
      }
    }
}