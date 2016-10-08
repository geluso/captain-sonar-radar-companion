package org.mooncolony.moonmayor.captainsonarradarcompanion.trackers;

import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPoint;
import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPointPath;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.MarineMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by moonmayor on 10/7/16.
 */
public class TorpedoTracker {
  private MarineMap map;
  public Map<GridPoint, List<GridPointPath>> pointToPaths;


  public TorpedoTracker(MarineMap map) {
    this.map = map;
    pointToPaths = new HashMap<>();
  }

  public void track(GridPoint point) {
    List<GridPointPath> paths = new ArrayList<>();
    GridPointPath currentPath = new GridPointPath(point);
    explore(paths, currentPath, point, 4);

    // save the paths that were just tracked.
    pointToPaths.put(point, paths);
  }

  public void explore(List<GridPointPath> paths, GridPointPath currentPath, GridPoint start, int moves) {
    // stop exploring if there's an island, or moves are depleted.
    if (!map.getCoord(start)) {
      return;
    }

    currentPath.add(start);

    if (moves == 0) {
      paths.add(currentPath.copy());
      return;
    }

    moves = moves - 1;

    explore(paths, currentPath, start.add(GridPoint.NORTH), moves);
    explore(paths, currentPath, start.add(GridPoint.SOUTH), moves);
    explore(paths, currentPath, start.add(GridPoint.EAST), moves);
    explore(paths, currentPath, start.add(GridPoint.WEST), moves);

    currentPath.path.remove(currentPath.size() - 1);
  }
}
