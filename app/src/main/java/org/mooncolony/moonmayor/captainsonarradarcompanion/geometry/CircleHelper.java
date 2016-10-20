package org.mooncolony.moonmayor.captainsonarradarcompanion.geometry;

import android.graphics.Paint;

import org.mooncolony.moonmayor.captainsonarradarcompanion.drawing.Paints;

/**
 * Created by matthewtduffin on 20/10/16.
 */
public class CircleHelper {
  public float x, y;
  public boolean marked;
  public Paint paint;

  public CircleHelper(float x, float y) {
    this.x = x;
    this.y = y;
  }
}
