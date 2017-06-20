package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

/**
 * Created by moonmayor on 10/3/16.
 */
public class DeltaTurnByTurn extends MarineMap {
  public static final String name = "Delta Turn by Turn";
  public static final String template =
      // the M's in this map represent mines which any player can explode.
      // they are not really supported by our boolean[][] right now.
     //ABCDEFGHIJKLMNO
      "..........\n" + // 1
      "..X...X...\n" + // 2
      "..X.......\n" + // 3
      "........X.\n" + // 4
      "..........\n" + // 5
      "..........\n" + // 6
      ".X....X.X.\n" + // 7
      ".X........\n" + // 8
      "...X...X..\n" + // 9
      "..........";    // 10

  public DeltaTurnByTurn() {
    this.water = templateToArray(template);
  }
}
