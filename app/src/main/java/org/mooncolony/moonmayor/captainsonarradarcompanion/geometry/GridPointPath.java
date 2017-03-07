package org.mooncolony.moonmayor.captainsonarradarcompanion.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moonmayor on 10/7/16.
 */
public class GridPointPath {
  public List<GridPoint> path;

  public GridPointPath() {
    path = new ArrayList<>();
  }

  public GridPointPath(GridPoint start) {
    this();
    path.add(start);
  }

  public GridPointPath(List<GridPoint> path) {
    this.path = path;
  }

  public void add(GridPoint point) {
    path.add(point);
  }

  public int size() {
    return path.size();
  }

  public GridPoint get(int i) {
    return path.get(i);
  }

  public GridPointPath copy() {
    GridPointPath result = new GridPointPath();
    result.path.addAll(this.path);
    return result;
  }
}
