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

  boolean placingTorpedo = false;

  public GameState() {
    this.currentMapTemplate = AlphaRealTime.template;
    this.radar = new RadarTracker(new Map(this.currentMapTemplate));
    this.currentPath = new ArrayList<>();
  }

  public void newGame() {
    this.newGame(this.currentMapTemplate);
  }

  public void newGame(String template) {
    this.currentPath = new ArrayList<>();
    this.showingPath = false;
    this.pathStartRow = 0;
    this.pathStartCol = 0;

    this.radar = new RadarTracker(new Map(template));
  }

  public void updatePath(GridPoint gp) {
    this.currentPath.add(gp);
    radar.track(this.currentPath);
  }

  public void addMine() {
    currentPath.add(GridPoint.MINE);
  }
}
