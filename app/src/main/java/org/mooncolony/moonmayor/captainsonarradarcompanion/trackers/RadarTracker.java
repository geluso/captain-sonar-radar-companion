package org.mooncolony.moonmayor.captainsonarradarcompanion.trackers;

import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPoint;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.MarineMap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RadarTracker {
  public MarineMap map;
  public Set<GridPoint> water;
  public Set<GridPoint> islands;
  private Set<GridPoint> possibleCurrentPositions;

  public RadarTracker(MarineMap map) {
    this.map = map;
    water = new HashSet<>();
    islands = new HashSet<>();
    possibleCurrentPositions = new HashSet<>();

    // initialize the new Radar Tracker with all starting positions that are
    // not on islands.
    for (int row = 0; row < map.rows; row++) {
      for (int col = 0; col < map.cols; col++) {
        GridPoint location = new GridPoint(row, col);
        if (map.getCoord(location)) {
          water.add(location);
          possibleCurrentPositions.add(location);
        } else {
          islands.add(location);
        }
      }
    }
  }

  public void track(GridPoint direction) {
    Set<GridPoint> stillPossiblePositions = new HashSet<>();
    for (GridPoint position : this.possibleCurrentPositions) {
      GridPoint newPos = position.add(direction);
      if (map.getCoord(newPos)) {
        stillPossiblePositions.add(newPos);
      }
    }

    possibleCurrentPositions = stillPossiblePositions;
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
}
