package org.mooncolony.moonmayor.captainsonarradarcompanion.trackers;

import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPoint;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.MarineMap;

import java.util.HashSet;
import java.util.Set;

public class RadarTracker {
  public MarineMap map;
  public Set<GridPoint> water;
  public Set<GridPoint> islands;
  public Set<GridPoint> possibleCurrentPositions;

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

  public void crossReferenceTorpedo(GridPoint location) {
    Set<GridPoint> stillPossiblePositions = new HashSet<>();

    for (GridPoint position : this.possibleCurrentPositions) {
      int dx = Math.abs(location.col - position.col);
      int dy = Math.abs(location.row - position.row);
      if (dx + dy <= 4) {
        stillPossiblePositions.add(position);
      }
    }

    this.possibleCurrentPositions = stillPossiblePositions;
  }

  public void inferSilence() {
    Set<GridPoint> stillPossiblePositions = new HashSet<>();

    GridPoint[] directions = {GridPoint.NORTH, GridPoint.SOUTH, GridPoint.WEST, GridPoint.EAST};

    for (GridPoint position : this.possibleCurrentPositions) {
      for (GridPoint direction : directions) {

        // take the first movement.
        GridPoint newPos = position.add(direction);

        int movements = 1;
        while (movements <= 4 && map.getCoord(newPos)) {
          stillPossiblePositions.add(newPos);

          // continue in the same direction
          newPos = newPos.add(direction);

          // count up how many movements the sub has gone.
          movements++;
        }
      }
    }

    this.possibleCurrentPositions = stillPossiblePositions;

  }
}
