package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

import android.app.Activity;

import org.mooncolony.moonmayor.captainsonarradarcompanion.GridPoint;
import org.mooncolony.moonmayor.captainsonarradarcompanion.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by moonmayor on 10/3/16.
 */
public class Map {
  List<String> mapList;
  public int rows;
  public int cols;

  // true for water, false for land.
  public boolean[][] water;

  //required empty constructor
  public Map() {}

  public Map(String s) {
    this.water = templateToArray(s);
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
