package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

import org.mooncolony.moonmayor.captainsonarradarcompanion.GridPoint;

/**
 * Created by moonmayor on 10/3/16.
 */
public class Map {
  public static final String[] AVAILABLE_MAPS = {
      AlphaRealTime.name, AlphaTurnByTurn.name,
      BravoRealTime.name, BravoTurnByTurn.name,
      CharlieRealTime.name, CharlieTurnByTurn.name,
      EchoRealTime.name, CharlieTurnByTurn.name,
      MapTiny.name};

  public static final String[] MAP_TEMPLATES = {
      AlphaRealTime.template, AlphaTurnByTurn.template,
      BravoRealTime.template, BravoTurnByTurn.template,
      CharlieRealTime.template, CharlieTurnByTurn.template,
      EchoRealTime.template, CharlieTurnByTurn.template,
      MapTiny.template
  };

  public static String name;
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
