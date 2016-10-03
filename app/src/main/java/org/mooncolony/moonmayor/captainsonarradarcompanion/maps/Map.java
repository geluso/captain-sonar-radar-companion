package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

/**
 * Created by moonmayor on 10/3/16.
 */
public class Map {
  public int width;
  public int height;

  // true for water, false for land.
  public boolean[][] water;

  public boolean getCoord(int row, int col) {
    if (row < 0 || row >= height || col < 0 || col >= width) {
      return false;
    }
    return water[row][col];
  }

  protected boolean[][] templateToArray(String template) {
    String[] rows = template.split("\n");
    int numCols = rows[0].length();

    boolean[][] water = new boolean[rows.length][numCols];
    for (int row  = 0; row < water.length; row++) {
      for (int col  = 0; col < numCols; col++) {
        water[row][col] = rows[row].charAt(col) == '.';
      }
    }

    return water;
  }
}
