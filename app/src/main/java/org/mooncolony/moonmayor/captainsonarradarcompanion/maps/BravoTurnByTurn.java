package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

/**
 * Created by moonmayor on 10/3/16.
 */
public class BravoTurnByTurn extends MarineMap {
  public static final String name = "Bravo Turn by Turn";
  public static final String template =
     //ABCDEFGHIJKLMNO
      "...X......\n" + // 1
      ".......X..\n" + // 2
      "..........\n" + // 3
      "..X....X..\n" + // 4
      "..........\n" + // 5
      "..........\n" + // 6
      "......X...\n" + // 7
      "..........\n" + // 8
      "...X......\n" + // 9
      "..........";    // 10

  public BravoTurnByTurn() {
    this.water = templateToArray(template);
  }
}
