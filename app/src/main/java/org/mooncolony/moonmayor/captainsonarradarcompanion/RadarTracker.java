package org.mooncolony.moonmayor.captainsonarradarcompanion;

import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.Map;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by moonmayor on 10/3/16.
 */
public class RadarTracker {
  public Map map;
  public Set<GridPoint> islands;
  private Set<GridPoint> startingPoints;
  private Set<GridPoint> invalidatedPoints;

  public RadarTracker(Map map) {
    this.map = map;
    islands = new HashSet<>();
    startingPoints = new HashSet<>();
    invalidatedPoints = new HashSet<>();

    // initialize the new Radar Tracker with all starting positions that are
    // not on islands.
    for (int row = 0; row < map.rows; row++) {
      for (int col = 0; col < map.cols; col++) {
        GridPoint startingLocation = new GridPoint(row, col);
        if (map.getCoord(startingLocation)) {
          startingPoints.add(startingLocation);
        } else {
          islands.add(startingLocation);
          invalidatedPoints.add(startingLocation);
        }
      }
    }
  }

  public void track(List<GridPoint> path) {
    Set<GridPoint> filteredStarts = new HashSet<>();

    for (GridPoint start : startingPoints) {
      if (isValidPath(start, path)) {
        filteredStarts.add(start);
      } else {
        invalidatedPoints.add(start);
      }
    }
    startingPoints = filteredStarts;
  }

  private boolean isValidPath(GridPoint start, List<GridPoint> path) {
    GridPoint current = start;

    // check to see if the starting position is a valid position.
    if (!map.getCoord(current)) {
      return false;
    }

    // walk through each movement and make sure the path doesn't cross an island.
    for (GridPoint movement : path) {
      current = current.add(movement);
      if (!map.getCoord(current)) {
        return false;
      }
    }

    return true;
  }

  public void setStartingPoints(Set<GridPoint> startingPoints) {
    this.startingPoints = startingPoints;
  }

  public Set<GridPoint> getStartingPoints() {
    return startingPoints;
  }

  public Set<GridPoint> getInvalidatedPoints() {
    return invalidatedPoints;
  }
}
