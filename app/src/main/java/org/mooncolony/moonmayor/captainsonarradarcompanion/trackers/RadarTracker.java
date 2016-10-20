package org.mooncolony.moonmayor.captainsonarradarcompanion.trackers;

import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPoint;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.MarineMap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by moonmayor on 10/3/16.
 */
public class RadarTracker {
  public MarineMap map;
  public Set<GridPoint> water;
  public Set<GridPoint> islands;
  private Set<GridPoint> validStartPoints;
  private Set<GridPoint> invalidStartPoints;

  private Set<GridPoint> invalidCurrentPoints;

  public RadarTracker(MarineMap map) {
    this.map = map;
    water = new HashSet<>();
    islands = new HashSet<>();
    validStartPoints = new HashSet<>();
    invalidStartPoints = new HashSet<>();
    invalidCurrentPoints = new HashSet<>();

    // initialize the new Radar Tracker with all starting positions that are
    // not on islands.
    for (int row = 0; row < map.rows; row++) {
      for (int col = 0; col < map.cols; col++) {
        GridPoint location = new GridPoint(row, col);
        if (map.getCoord(location)) {
          water.add(location);
          validStartPoints.add(location);
        } else {
          islands.add(location);
          invalidCurrentPoints.add(location);
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

  public Set<GridPoint> getPossibleCurrentPositions(List<GridPoint> path) {
    Set<GridPoint> possiblePositions = new HashSet<>();

    // start a path from every piece of water
    for (GridPoint location : this.water) {
      GridPoint currentSpot = location;

      // follow the path
      for (int i = 0; i < path.size(); i++) {
        GridPoint direction = path.get(i);
        currentSpot = currentSpot.add(direction);

        // if the position is an island stop exploring this path from this start position.
        if (!map.getCoord(currentSpot)) {
          break;
        }

        // if this is the last point in the path then add
        // the current position as a possible current position.
        if (i == path.size() - 1) {
          possiblePositions.add(currentSpot);
        }
      }
    }

    return possiblePositions;
  }

  public Set<GridPoint> getValidStartPoints() {
    return validStartPoints;
  }

  public Set<GridPoint> getInvalidStartPoints() {
    return invalidStartPoints;
  }
}
