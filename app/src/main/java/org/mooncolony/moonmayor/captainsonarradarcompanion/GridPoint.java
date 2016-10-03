package org.mooncolony.moonmayor.captainsonarradarcompanion;

/**
 * Created by moonmayor on 10/3/16.
 */
public class GridPoint {
  public static final GridPoint NORTH = new GridPoint(-1, 0);
  public static final GridPoint SOUTH = new GridPoint(1, 0);
  public static final GridPoint EAST =  new GridPoint(0, 1);
  public static final GridPoint WEST =  new GridPoint(0, -1);

  public int row;
  public int col;

  public GridPoint(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public GridPoint add(GridPoint other) {
    return new GridPoint(this.row + other.row, this.col + other.col);
  }
}
