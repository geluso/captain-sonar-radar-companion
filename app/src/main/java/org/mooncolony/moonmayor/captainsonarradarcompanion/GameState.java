package org.mooncolony.moonmayor.captainsonarradarcompanion;

import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPoint;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.AlphaRealTime;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.MarineMap;
import org.mooncolony.moonmayor.captainsonarradarcompanion.trackers.RadarTracker;
import org.mooncolony.moonmayor.captainsonarradarcompanion.trackers.TorpedoTracker;

import java.util.ArrayList;
import java.util.List;

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

  public boolean placing

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

    this.radar = new RadarTracker(this.map);
  }

  public void setPathStart(int row, int col) {
    this.pathStartRow = row;
    this.pathStartCol = col;
  }

  public void setPathEnd(int row, int col) {
    this.pathEndRow = row;
    this.pathEndCol = col;
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

    torpedoTracker.track(torpedoTarget);
    radar.crossReferenceTorpedo(torpedoTarget);
  }

  public void addSilence() {
    radar.inferSilence();
  }
}
