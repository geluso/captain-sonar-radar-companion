package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

/**
 * Created by moonmayor on 10/3/16.
 */
public class EchoTurnByTurn extends Map {
  public static final String name = "Echo Turn by Turn";
  public static final String template =
      // the M's in this map represent mines which any player can explode.
      // they are not really supported by our boolean[][] right now.
     //ABCDEFGHIJKLMNO
      "..........\n" + // 1
      ".X.....M..\n" + // 2
      "....M.X...\n" + // 3
      ".M........\n" + // 4
      ".......M.X\n" + // 5
      "..........\n" + // 6
      ".M.....X..\n" + // 7
      "......M...\n" + // 8
      ".X.M..X.M.\n" + // 9
      "..........";    // 10

  public EchoTurnByTurn() {
    this.water = templateToArray(template);
  }
}
