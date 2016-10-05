package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

/**
 * Created by moonmayor on 10/3/16.
 */
public class EchoRealTime extends Map {
  public static final String name = "Echo Real Time";
  public static final String template =
      // the M's in this map represent mines which any player can explode.
      // they are not really supported by our boolean[][] right now.
     //ABCDEFGHIJKLMNO
      "..X............\n" + // 1
      ".......XX..M...\n" + // 2
      ".X.X..M.....XX.\n" + // 3
      "..M...X........\n" + // 4
      "...............\n" + // 5
      ".....M...M.....\n" + // 6
      ".X...........X.\n" + // 7
      "...X..X........\n" + // 8
      ".M......X..X.M.\n" + // 9
      ".....M...M.....\n" + // 10
      "...............\n" + // 11
      "...X..X....M...\n" + // 12
      ".............X.\n" + // 13
      ".X.M..XM.....X.\n" + // 14
      "...............";  // 15

  public EchoRealTime() {
    this.water = templateToArray(template);
  }
}
