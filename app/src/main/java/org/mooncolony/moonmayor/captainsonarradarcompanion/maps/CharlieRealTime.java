package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

/**
 * Created by moonmayor on 10/3/16.
 */
public class CharlieRealTime extends Map {
  public static final String name = "Charlie Real Time";
  public static final String template =
     //ABCDEFGHIJKLMNO
      "...............\n" + // 1
      "...............\n" + // 2
      ".....X......X..\n" + // 3
      ".....X.....X...\n" + // 4
      "...............\n" + // 5
      "...............\n" + // 6
      "...............\n" + // 7
      "...X....X......\n" + // 8
      "........X......\n" + // 9
      ".......XX......\n" + // 10
      "...............\n" + // 11
      "...............\n" + // 12
      "...X...........\n" + // 13
      "....X..........\n" + // 14
      "...............";  // 15

  public CharlieRealTime() {
    this.water = templateToArray(template);
  }
}
