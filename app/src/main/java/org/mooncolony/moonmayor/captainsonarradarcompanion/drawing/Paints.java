package org.mooncolony.moonmayor.captainsonarradarcompanion.drawing;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by moonmayor on 10/6/16.
 */
public class Paints {
  public static final Paint RED, WHITE, BLACK, YELLOW;
  public static final Paint CIRCLE, WATER, ISLAND, PATH, TEXT, AREA;

  static {
    RED = new Paint(Paint.ANTI_ALIAS_FLAG);
    RED.setColor(Color.RED);
    RED.setStyle(Paint.Style.FILL);

    CIRCLE = new Paint(Paint.ANTI_ALIAS_FLAG);
    CIRCLE.setStrokeWidth(6.0f);
    CIRCLE.setColor(Color.rgb(13, 71, 161));
    CIRCLE.setStyle(Paint.Style.STROKE);

    WHITE = new Paint(Paint.ANTI_ALIAS_FLAG);
    WHITE.setColor(Color.WHITE);
    WHITE.setStyle(Paint.Style.FILL);

    TEXT = new Paint(Paint.ANTI_ALIAS_FLAG);
    TEXT.setColor(Color.rgb(13, 71, 161));
    TEXT.setStyle(Paint.Style.FILL);
    TEXT.setFakeBoldText(true);
    TEXT.setTextAlign(Paint.Align.CENTER);

    BLACK = new Paint(Paint.ANTI_ALIAS_FLAG);
    BLACK.setColor(Color.BLACK);
    BLACK.setStyle(Paint.Style.FILL);

    YELLOW = new Paint(Paint.ANTI_ALIAS_FLAG);
    YELLOW.setColor(Color.YELLOW);
    YELLOW.setStrokeWidth(4.0f);
    YELLOW.setStyle(Paint.Style.FILL);

    PATH = new Paint(Paint.ANTI_ALIAS_FLAG);
    PATH.setColor(Color.BLACK);
    PATH.setStrokeWidth(8.0f);
    PATH.setStyle(Paint.Style.FILL);

    WATER = new Paint(Paint.ANTI_ALIAS_FLAG);
    WATER.setColor(Color.rgb(79, 195, 247));
    WATER.setStyle(Paint.Style.FILL);

    ISLAND = new Paint(Paint.ANTI_ALIAS_FLAG);
    ISLAND.setColor(Color.rgb(56, 142, 60));
    ISLAND.setStyle(Paint.Style.FILL);

    AREA = new Paint(Paint.ANTI_ALIAS_FLAG);
    AREA.setColor(Color.argb(100, 255, 255, 255));
    AREA.setStyle(Paint.Style.FILL);
  }
}
