package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

/**
 * Created by moonmayor on 10/3/16.
 */
public class AlphaRealTime extends MarineMap {
  public static final String name = "Alpha Real Time";
  public static final String template =
     //ABCDEFGHIJKLMNO
      "...............\n" + // 1
      "..X...X.....XX.\n" + // 2
      "..X.....X...X..\n" + // 3
      "........X......\n" + // 4
      "...............\n" + // 5
      "...............\n" + // 6
      ".X.X..X.X......\n" + // 7
      ".X.X..X........\n" + // 8
      "...X...X...XXX.\n" + // 9
      "...............\n" + // 10
      "...X...........\n" + // 11
      "..X....X...X...\n" + // 12
      "X...........X..\n" + // 13
      "..X...X.X....X.\n" + // 14
      "...X...........";  // 15

  public AlphaRealTime() {
    this.water = templateToArray(template);
  }
}
