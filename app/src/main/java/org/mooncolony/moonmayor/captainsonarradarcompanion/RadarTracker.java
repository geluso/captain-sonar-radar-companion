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
  private Set<GridPoint> validStartPoints;
  private Set<GridPoint> invalidStartPoints;

  private Set<GridPoint> invalidCurrentPoints;

  public RadarTracker(Map map) {
    this.map = map;
    islands = new HashSet<>();
    validStartPoints = new HashSet<>();
    invalidStartPoints = new HashSet<>();
    invalidCurrentPoints = new HashSet<>();

    // initialize the new Radar Tracker with all starting positions that are
    // not on islands.
    for (int row = 0; row < map.rows; row++) {
      for (int col = 0; col < map.cols; col++) {
        GridPoint startingLocation = new GridPoint(row, col);
        if (map.getCoord(startingLocation)) {
          validStartPoints.add(startingLocation);
        } else {
          islands.add(startingLocation);
//          invalidStartPoints.add(startingLocation);
        }
      }
    }
  }

  public Set<GridPoint> getInvalidCurrentPoints() {
    return invalidCurrentPoints;
  }

  public void track(List<GridPoint> path) {
    Set<GridPoint> filteredStarts = new HashSet<>();

    for (GridPoint start : validStartPoints) {
      if (isValidPath(start, path)) {
        filteredStarts.add(start);
      } else {
        invalidStartPoints.add(start);
      }
    }
    validStartPoints = filteredStarts;
    updateCurrentInvalidStartPoints(path);
  }

  private void updateCurrentInvalidStartPoints(List<GridPoint> path) {
    Set<GridPoint> newSet = new HashSet<>();

    for (GridPoint gp : this.invalidStartPoints) {
      GridPoint newGp = performPath(gp,path);
      if (newGp!=null) {
        newSet.add(newGp);
      }
    }

    invalidCurrentPoints = newSet;
  }

  private GridPoint performPath(GridPoint gp, List<GridPoint> path) {
    if (!map.getCoord(gp)) {
      return null;
    }
    for (GridPoint movement : path) {
      gp = gp.add(movement);
    }
    if (map.getCoord(gp)) {
      return gp;
    } else {
      return null;
    }
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

  public void setValidStartPoints(Set<GridPoint> validStartPoints) {
    this.validStartPoints = validStartPoints;
  }

  public Set<GridPoint> getValidStartPoints() {
    return validStartPoints;
  }

  public Set<GridPoint> getInvalidStartPoints() {
    return invalidStartPoints;
  }
}
