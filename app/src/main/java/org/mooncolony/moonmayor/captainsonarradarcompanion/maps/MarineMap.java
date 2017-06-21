package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPoint;

/**
 * Created by moonmayor on 10/3/16.
 */
public class MarineMap {
  // It's hard to combine these array into just one array.
  // The problem is that they're all classes. You'd have to
  // do something like declaring: Class[] aa = {AlphaRealTime.class, ...}.
  // But then since the array would be direct references to just type "Class"
  // you can't access aa[0].name. Gotta figure out a good way to do that!
  public static final String[] AVAILABLE_MAPS = {
      AlphaRealTime.name, AlphaTurnByTurn.name,
      BravoRealTime.name, BravoTurnByTurn.name,
      CharlieRealTime.name, CharlieTurnByTurn.name,
      DeltaRealTime.name, DeltaTurnByTurn.name,
      EchoRealTime.name, EchoTurnByTurn.name,
      MapTiny.name};

  public static final String[] MAP_TEMPLATES = {
      AlphaRealTime.template, AlphaTurnByTurn.template,
      BravoRealTime.template, BravoTurnByTurn.template,
      CharlieRealTime.template, CharlieTurnByTurn.template,
      DeltaRealTime.template, DeltaTurnByTurn.template,
      EchoRealTime.template, EchoTurnByTurn.template,
      MapTiny.template
  };

  public static String name;
  public int rows;
  public int cols;

  // true for water, false for land.
  public boolean[][] water;

  //required empty constructor
  public MarineMap() {}

  public MarineMap(String s) {
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

  public int pointToQuadrant(int row, int col) {
    return pointToQuadrant(new GridPoint(row, col));
  }

  public int pointToQuadrant(GridPoint point) {
    int regionColumns = 3;
    if (this.rows == 10) {
      regionColumns = 2;
    }

    int regionId = (point.row / 5) * regionColumns + (point.col / 5)+ 1;
    return regionId;
  }

  public boolean isInQuadrant(GridPoint point, int quadrant) {
    return pointToQuadrant(point) == quadrant;
  }
}
