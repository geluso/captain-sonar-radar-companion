package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

/**
 * Created by moonmayor on 10/3/16.
 */
public class CharlieTurnByTurn extends Map {
  public static final String name = "Charlie Turn by Turn";
  public static final String template =
     //ABCDEFGHIJKLMNO
      "..........\n" + // 1
      "..........\n" + // 2
      "..........\n" + // 3
      "..XXX.....\n" + // 4
      "..........\n" + // 5
      "......X...\n" + // 6
      "......XX..\n" + // 7
      "...... ...\n" + // 8
      "..........\n" + // 9
      "..........";    // 10

  public CharlieTurnByTurn() {
    this.water = templateToArray(template);
  }
}
