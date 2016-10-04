package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

import org.mooncolony.moonmayor.captainsonarradarcompanion.GridPoint;

/**
 * Created by moonmayor on 10/3/16.
 */
public class Map {
  public int rows;
  public int cols;

  // true for water, false for land.
  public boolean[][] water;

  public Map() {
    //this.water = templateToArray(MapRealTimeAlpha.template);
    this.water = templateToArray(MapTiny.template);
    this.rows = water.length;
    this.cols = water[0].length;
  }

  public boolean getCoord(GridPoint point) {
    return this.getCoord(point.row, point.col);
  }

  public boolean getCoord(int row, int col) {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      return false;
    }
    return water[row][col];
  }

  protected boolean[][] templateToArray(String template) {
    String[] rows = template.split("\n");
    int numCols = rows[0].length();
    int numRows = rows.length;

    boolean[][] water = new boolean[numRows][numCols];
    for (int row  = 0; row < numRows; row++) {
      for (int col  = 0; col < numCols; col++) {
        water[row][col] = rows[row].charAt(col) == '.';
      }
    }

    return water;
  }
}
