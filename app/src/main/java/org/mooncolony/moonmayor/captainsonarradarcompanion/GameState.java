package org.mooncolony.moonmayor.captainsonarradarcompanion;

import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPoint;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.AlphaRealTime;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.MarineMap;
import org.mooncolony.moonmayor.captainsonarradarcompanion.trackers.RadarTracker;
import org.mooncolony.moonmayor.captainsonarradarcompanion.trackers.TorpedoTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by matthewtduffin on 03/10/16.
 */
public class GameState {
  public RadarTracker radar;

  private String currentMapTemplate;
  private MarineMap map;

  // The list of North, South, East, West, mine, torpedo moves.
  public List<GridPoint> currentPath;
  public int pathStartCol, pathStartRow;
  public int pathEndRow, pathEndCol;

  // List of coordinates where torpedoes have been fired.
  public TorpedoTracker torpedoTracker;
  public List<GridPoint> torpedoes;

  public boolean placingTorpedo = false;
  public int placingTorpedoRow = -1;
  public int placingTorpedoCol = -1;

  public boolean isAskingDrone = false;
  public int currentDroneRegionId = -1;

  public boolean isRunningSonar = false;
  public int placingSonarRow = -1;
  public int placingSonarCol = -1;
  // one of three modes: ["row,col", "sector,row", "sector,col"]
  public String sonarMode = "row,col";

  public GameState() {
    newGame(AlphaRealTime.template);
  }

  public void newGame() {
    this.newGame(this.currentMapTemplate);
  }

  public void newGame(String template) {
    this.currentMapTemplate = template;
    this.map = new MarineMap(this.currentMapTemplate);

    this.currentPath = new ArrayList<>();
    this.torpedoes = new ArrayList<>();

    this.pathStartRow = this.map.rows / 2;
    this.pathStartCol = this.map.cols / 2;

    this.torpedoTracker = new TorpedoTracker(this.map);
    this.placingTorpedo = false;
    this.placingTorpedoRow = -1;
    this.placingTorpedoCol = -1;

    // radar tracker is for tracking basic movement.
    this.radar = new RadarTracker(this.map);

    this.isRunningSonar = false;
    this.sonarMode = "row,col";
    this.placingSonarRow = -1;
    this.placingSonarCol = -1;
  }

  public void setPathStart(int row, int col) {
    this.pathStartRow = row;
    this.pathStartCol = col;
  }

  public void setPathEnd(int row, int col) {
    this.pathEndRow = row;
    this.pathEndCol = col;

    followPathEndToPathStart();
  }

  private void followPathEndToPathStart() {
    int row = this.pathEndRow;
    int col = this.pathEndCol;
    GridPoint currentPoint = new GridPoint(row, col);

    for (int i  = this.currentPath.size() - 1; i >= 0; i--) {
      GridPoint direction = this.currentPath.get(i);
      currentPoint = currentPoint.back(direction);
    }

    setPathStart(currentPoint.row, currentPoint.col);
  }

  public void updatePath(GridPoint gp) {
    this.currentPath.add(gp);
    radar.track(gp);
  }

  public void addMine() {
    currentPath.add(GridPoint.MINE);
  }

  public void addTorpedo() {
    GridPoint torpedoTarget = new GridPoint(placingTorpedoRow, placingTorpedoCol);
    torpedoes.add(torpedoTarget);

    Set<GridPoint> possibleFiringPositions = torpedoTracker.track(torpedoTarget);
    radar.crossReferenceTorpedo(possibleFiringPositions);
  }

  public void analyzeDrone(boolean isSonarPositive) {
    radar.drone(this.currentDroneRegionId, isSonarPositive);
  }

  public void addSilence() {
    radar.inferSilence();
  }

  public void sonarStart() {
    this.isRunningSonar = true;
    this.sonarMode = "row,col";
    this.placingSonarRow = this.map.rows / 2;
    this.placingSonarCol = this.map.cols / 2;
  }

  public void sonarCancel() {
    this.isRunningSonar = false;
  }

  public void sonarToggleMode() {
    if (this.sonarMode.equals("row,col")) {
      this.sonarMode = "sector,row";
    } else if (this.sonarMode.equals("sector,row")) {
      this.sonarMode = "sector,col";
    } else {
      this.sonarMode = "row,col";
    }
  }

  public void sonarConfirm() {
    this.isRunningSonar = false;
    int row = this.placingSonarRow;
    int col = this.placingSonarCol;
    int sector = this.map.pointToQuadrant(row, col);

    // zero out whichever piece of information was not given.
    if (this.sonarMode.equals("row,col")) {
      sector = -1;
    } else if (this.sonarMode.equals("sector,row")) {
      col = -1;
    } else if (this.sonarMode.equals("sector,col")) {
      row = -1;
    }

    this.radar.sonar(row, col, sector);
  }
}
