package org.mooncolony.moonmayor.captainsonarradarcompanion.geometry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by moonmayor on 10/3/16.
 */
public class GridPoint {
  public static final GridPoint NORTH = new GridPoint(-1, 0);
  public static final GridPoint SOUTH = new GridPoint(1, 0);
  public static final GridPoint EAST =  new GridPoint(0, 1);
  public static final GridPoint WEST =  new GridPoint(0, -1);

  public static final GridPoint MINE =  new GridPoint(0, 0);
  public static final GridPoint TORPEDO =  new GridPoint(0, 0);

  public int row;
  public int col;

  public GridPoint(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public GridPoint north() {
    return this.add(NORTH);
  }

  public GridPoint south() {
    return this.add(SOUTH);
  }

  public GridPoint east() {
    return this.add(EAST);
  }

  public GridPoint west() {
    return this.add(WEST);
  }

  public GridPoint add(GridPoint other) {
    return new GridPoint(this.row + other.row, this.col + other.col);
  }
}
