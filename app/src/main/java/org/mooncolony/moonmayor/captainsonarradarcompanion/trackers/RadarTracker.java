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
  public GridPoint lastMovement;

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

    lastMovement = direction;
    possibleCurrentPositions = stillPossiblePositions;
  }

  public void surface() {
    lastMovement = null;
  }

  public void crossReferenceTorpedo(Set<GridPoint> possibleFiringLocations) {
    Set<GridPoint> stillPossiblePositions = new HashSet<>();

    for (GridPoint p1 : this.possibleCurrentPositions) {
      for (GridPoint p2 : possibleFiringLocations) {
        if (p1.row == p2.row && p1.col == p2.col) {
          stillPossiblePositions.add(p1);
        }
      }
    }

    this.possibleCurrentPositions = stillPossiblePositions;
  }

  public void inferSilence() {
    Set<GridPoint> stillPossiblePositions = new HashSet<>();

    GridPoint[] directions = {GridPoint.NORTH, GridPoint.SOUTH, GridPoint.WEST, GridPoint.EAST};

    for (GridPoint position : this.possibleCurrentPositions) {
      // technically, silence includes moving in 0-4 directions.
      // this accounts for zero movement.
      stillPossiblePositions.add(position);

      for (GridPoint direction : directions) {
        // submarines can't go back across the last way they came.
        if (lastMovement != null && direction == lastMovement.oppositeDirection()) {
          continue;
        }

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

  public void drone(int quadrant, boolean isEnemyThere) {
    Set<GridPoint> stillPossiblePositions = new HashSet<>();

    for (GridPoint pos : possibleCurrentPositions) {
      if (map.isInQuadrant(pos, quadrant) == isEnemyThere) {
        stillPossiblePositions.add(pos);
      }
    }

    this.possibleCurrentPositions = stillPossiblePositions;
  }

  // The user provides two pieces of information of the three parameters defined here.
  // The unused third piece of information should be passed in as -1.
  // Therefore, if a user says they're at "row 2, quadrant 1" then this function should
  // be called as: sonar(2, -1, 1);
  public void sonar(int row, int col, int quadrant) {
    Set<GridPoint> stillPossiblePositions = new HashSet<>();

    for (GridPoint pos : possibleCurrentPositions) {
      if (row == pos.row || col == pos.col || map.pointToQuadrant(pos) == quadrant) {
        // a possible position that matches both pieces of info sonar exactly can't
        // be a position, because player is forced to give one false piece of information.
        boolean tooTrue = pos.row == row && pos.col == col ||
            pos.row == row && map.pointToQuadrant(pos) == quadrant ||
            pos.col == col && map.pointToQuadrant(pos) == quadrant;

        if (!tooTrue) {
          stillPossiblePositions.add(pos);
        }
      }
    }

    this.possibleCurrentPositions = stillPossiblePositions;
  }
}
