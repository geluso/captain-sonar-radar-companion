package org.mooncolony.moonmayor.captainsonarradarcompanion;

import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.AlphaRealTime;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewtduffin on 03/10/16.
 */
public class GameState {
  public RadarTracker radar;
  private String currentMapTemplate;

  // The list of North, South, East, West, mine, torpedo moves.
  public List<GridPoint> currentPath;
  public boolean showingPath = false;
  public int pathStartCol, pathStartRow;

  // List of coordinates where torpedoes have been fired.
  public List<GridPoint> torpedoes;

  public boolean placingTorpedo = false;
  public int placingTorpedoRow = -1;
  public int placingTorpedoCol = -1;

  public GameState() {
    this.currentMapTemplate = AlphaRealTime.template;
    this.radar = new RadarTracker(new Map(this.currentMapTemplate));
    this.currentPath = new ArrayList<>();
    this.torpedoes = new ArrayList<>();
  }

  public void newGame() {
    this.newGame(this.currentMapTemplate);
  }

  public void newGame(String template) {
    this.currentMapTemplate = template;

    this.currentPath = new ArrayList<>();
    this.torpedoes = new ArrayList<>();

    this.showingPath = false;
    this.pathStartRow = 0;
    this.pathStartCol = 0;

    this.placingTorpedo = false;
    this.placingTorpedoRow = -1;
    this.placingTorpedoCol = -1;

    this.radar = new RadarTracker(new Map(template));
  }

  public void setPathStart(int row, int col) {
    this.showingPath = true;
    this.pathStartRow = row;
    this.pathStartCol = col;
  }

  public void updatePath(GridPoint gp) {
    this.currentPath.add(gp);
    radar.track(this.currentPath);
  }

  public void addMine() {
    currentPath.add(GridPoint.MINE);
  }

  public void addTorpedo() {
    GridPoint torpedoTarget = new GridPoint(placingTorpedoRow, placingTorpedoCol);
    torpedoes.add(torpedoTarget);
  }
}
