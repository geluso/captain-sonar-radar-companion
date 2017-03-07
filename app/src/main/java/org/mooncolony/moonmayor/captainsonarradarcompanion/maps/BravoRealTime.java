package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

/**
 * Created by moonmayor on 10/3/16.
 */
public class BravoRealTime extends MarineMap {
  public static final String name = "Bravo Real Time";
  public static final String template =
     //ABCDEFGHIJKLMNO
      "...............\n" + // 1
      "..X.........XX.\n" + // 2
      "..X.....X......\n" + // 3
      "........X......\n" + // 4
      "...............\n" + // 5
      "...............\n" + // 6
      "...............\n" + // 7
      "...X...........\n" + // 8
      "...X.......XXX.\n" + // 9
      "...............\n" + // 10
      "...X...........\n" + // 11
      "..X....X...X...\n" + // 12
      "...............\n" + // 13
      "........X......\n" + // 14
      "...............";  // 15

  public BravoRealTime() {
    this.water = templateToArray(template);
  }
}
