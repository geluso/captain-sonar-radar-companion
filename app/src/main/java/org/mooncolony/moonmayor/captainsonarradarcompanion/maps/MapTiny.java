package org.mooncolony.moonmayor.captainsonarradarcompanion.maps;

/**
 * Created by moonmayor on 10/3/16.
 */
public class MapTiny extends MarineMap {
  public static final String name = "Tiny";
  public static final String template =
     //ABCDEFGHIJKLMNO
      ".X...\n" +
      "....X\n" +
      ".X...\n" +
      "....X\n" +
      "..X..";

  public MapTiny() {
    this.water = templateToArray(template);
  }
}
