package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

/**
 * Created by moonmayor on 10/3/16.
 */
public class DeltaRealTime extends MarineMap {
  public static final String name = "Delta Real Time";
  public static final String template =
      // the M's in this map represent mines which any player can explode.
      // they are not really supported by our boolean[][] right now.
      //ABCDEFGHIJKLMNO
      "...............\n" + // 1
      "..X...X.....XX.\n" + // 2
      "..X............\n" + // 3
      "......X.X......\n" + // 4
      "...............\n" + // 5
      "...............\n" + // 6
      ".X....X.X......\n" + // 7
      ".X.............\n" + // 8
      "...X...X...X.X.\n" + // 9
      "...............\n" + // 10
      "...X...........\n" + // 11
      "..X....X...X...\n" + // 12
      "X............X.\n" + // 13
      "..X...X......X.\n" + // 14
      "...............";  // 15

  public DeltaRealTime() {
    this.water = templateToArray(template);
  }
}
