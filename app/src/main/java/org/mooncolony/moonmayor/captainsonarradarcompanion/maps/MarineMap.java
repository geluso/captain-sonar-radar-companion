package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPoint;

/**
 * Created by moonmayor on 10/3/16.
 */
public class MarineMap {
  public static final String[] AVAILABLE_MAPS = {
      AlphaRealTime.name, AlphaTurnByTurn.name,
      BravoRealTime.name, BravoTurnByTurn.name,
      CharlieRealTime.name, CharlieTurnByTurn.name,
      EchoRealTime.name, EchoTurnByTurn.name,
      MapTiny.name};

  public static final String[] MAP_TEMPLATES = {
      AlphaRealTime.template, AlphaTurnByTurn.template,
      BravoRealTime.template, BravoTurnByTurn.template,
      CharlieRealTime.template, CharlieTurnByTurn.template,
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

  private int pointToQuadrant10x10(GridPoint point) {
    if (point.row <= 5 && point.col <= 5) {
      return 1;
    }

    if (point.row <= 5 && point.col <= 10) {
      return 2;
    }

    if (point.col <= 5) {
      return 3;
    }

    return 4;
  }

  private int pointToQuadrant15x15(GridPoint point) {
    // top three quadrants
    if (point.row <= 5 && point.col <= 5) {
      return 1;
    }

    if (point.row <= 5 && point.col <= 10) {
      return 2;
    }

    if (point.row <= 5 && point.col <= 15) {
      return 3;
    }

    // middle three quadrants
    if (point.row <= 10 && point.col <= 5) {
      return 4;
    }

    if (point.row <= 10 && point.col <= 10) {
      return 5;
    }

    if (point.row <= 10 && point.col <= 15) {
      return 6;
    }

    // bottom three quadrants
    if (point.row <= 15 && point.col <= 5) {
      return 7;
    }

    if (point.row <= 15 && point.col <= 10) {
      return 8;
    }

    return 9;
  }

  public int pointToQuadrant(GridPoint point) {
    if (this.rows == 10) {
      return pointToQuadrant10x10(point);
    } else {
      return pointToQuadrant15x15(point);
    }
  }

  public boolean isInQuadrant(GridPoint point, int quadrant) {
    return pointToQuadrant(point) == quadrant;
  }
}
