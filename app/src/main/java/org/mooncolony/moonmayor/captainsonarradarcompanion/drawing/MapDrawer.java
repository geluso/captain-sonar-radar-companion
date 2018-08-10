package org.mooncolony.moonmayor.captainsonarradarcompanion.drawing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import org.mooncolony.moonmayor.captainsonarradarcompanion.GameState;
import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPoint;
import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPointPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moonmayor on 10/6/16.
 */
public class MapDrawer {
  private Activity activity;
  private ImageView map;
  private Canvas canvas;
  private Bitmap bitmap;

  private GameState gameState;

  private int width;
  private int height;

  private float circleRadius;
  private float initialXOffset;
  private float initialYOffset;
  private float xIterateOffset;
  private float yIterateOffset;
  private float textColumnAdditionalOffset;

  public MapDrawer(Activity activity, ImageView map, GameState gameState) {
    this.activity = activity;
    this.map = map;
    this.gameState = gameState;

    setDimensions();
  }

  public void setDimensions() {
    DisplayMetrics display = activity.getResources().getDisplayMetrics();
    this.width = display.widthPixels;
    this.height = display.heightPixels;

    // constrain the ImageView to the exact dimensions of the bitmap.
    map.setMinimumHeight(this.width);
    map.setMaxHeight(this.width);

    this.bitmap = Bitmap.createBitmap(this.width, this.width, Bitmap.Config.RGB_565);
    Canvas canvas = new Canvas(this.bitmap);
    this.canvas = canvas;

    // add one to account for a half circle padding on each edge
    this.circleRadius = (float) ((1.0 * this.width) / (getCols() + 1) / 2);

    this.initialXOffset = 3 * this.circleRadius;
    this.initialYOffset = 3 * this.circleRadius;

    this.xIterateOffset = this.circleRadius * 2;
    this.yIterateOffset = this.circleRadius * 2;

    this.textColumnAdditionalOffset = circleRadius/2;
  }

  // create getters so rows and cols are always up to date with latest gameState.
  private int getRows() {
    return gameState.radar.map.rows;
  }

  private int getCols() {
    return gameState.radar.map.cols;
  }

  public int xToCol(float x) {
    float xx = x - this.initialXOffset;
    xx = xx / this.xIterateOffset;
    xx = Math.round(xx);

    int col = (int) xx;
    return col;
  }

  public int yToRow(float y) {
    float yy = y - this.initialYOffset;
    yy = yy / this.yIterateOffset;
    yy = Math.round(yy);
    int row = (int) yy;
    return row;
  }

  public float colToX(int col) {
    float x = col * this.xIterateOffset + this.initialXOffset;
    return x;
  }

  public float rowToY(int row) {
    float y = row * this.yIterateOffset + this.initialYOffset;
    return y;
  }

  public void draw() {
    clearCanvas();
    drawSectionLines();
    drawCircles();
    drawIslands();
    drawLetters();

    // drawPossibleStartingLocations();
    // drawInvalidStartArea();
    // drawInvalidCurrentArea();
    drawCurrentPossiblePosition();

    if (gameState.placingTorpedo) {
      int row = gameState.placingTorpedoRow;
      int col = gameState.placingTorpedoCol;
      drawTargetingLines(row, col);
      drawTorpedo(row, col);
    }

    if (gameState.isRunningSonar) {
      int row = gameState.placingSonarRow;
      int col = gameState.placingSonarCol;
      drawTargetingLines(row, col);
    }

    drawTorpedoTracking();
    drawTorpedoes();

    drawPathForward();


    //drawPathBackward();

    map.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

  private void drawSectionLines() {
    if (getRows() == 10) {
      drawSectionLines10x10();
    } else {
      drawSectionLines15x15();
    }
  }

  private void drawSectionLines10x10() {
    for (int row = 0; row < getRows()-1; row+=5) {
      float x = this.initialXOffset + row * this.xIterateOffset -xIterateOffset/2;
      this.canvas.drawLine(x, 0, x, this.width, Paints.BLACK);
    }

    for (int col = 0; col < getCols()-1; col+=5) {
      float y = this.initialYOffset + col * this.yIterateOffset -yIterateOffset/2;
      this.canvas.drawLine(0, y, this.width, y, Paints.BLACK);
    }

  }

  private void drawSectionLines15x15() {
    for (int row = 0; row < getRows()-1; row+=5) {
      float x = this.initialXOffset + row * this.xIterateOffset -xIterateOffset/2;
      this.canvas.drawLine(x, 0, x, this.width, Paints.BLACK);
    }

    for (int col = 0; col < getCols()-1; col+=5) {
      float y = this.initialYOffset + col * this.yIterateOffset -yIterateOffset/2;
      this.canvas.drawLine(0, y, this.width, y, Paints.BLACK);
    }

  }

  private void drawCircle(int row, int col) {
    float y = this.initialYOffset + col * this.yIterateOffset;
    float x = this.initialXOffset + row * this.xIterateOffset;
    this.drawCircle(x, y);
  }

  private void drawCircle(float x, float y) {
    this.canvas.drawCircle(x, y, circleRadius/8, Paints.WHITE);
  }

  private void drawCircles() {
    for (int row = 0; row < getRows(); row++) {
      for (int col = 0; col < getCols(); col++) {
        drawCircle(row, col);
      }
    }
  }

  private void drawIslands() {
    for (GridPoint island : gameState.radar.islands) {
      int x = Math.round(circleRadius + xIterateOffset/2 + island.col * this.xIterateOffset);
      int y = Math.round(circleRadius + yIterateOffset/2 + island.row * this.yIterateOffset);
      int squareSize = Math.round(2 * circleRadius);

      Rect rect = new Rect(x, y, x + squareSize, y + squareSize);
      this.canvas.drawRect(rect, Paints.ISLAND);
    }
  }

  private void drawLetters() {
    for (int row = 0; row < getRows(); row++) {
      float x = this.initialXOffset + row * this.xIterateOffset;
      String colLetter = ""+(char)(row+65);

      Paints.TEXT.setTextSize(4 * circleRadius / 3);
      canvas.drawText(colLetter,x,initialXOffset-3*xIterateOffset/4,Paints.TEXT);
    }

    for (int col = 0; col < getCols(); col++) {
      //the additional text column offset is to account for centring the letter
      float y = this.initialYOffset + col * this.yIterateOffset+this.textColumnAdditionalOffset;
      String rowNum = ""+(col+1);
      canvas.drawText(rowNum,circleRadius,y,Paints.TEXT);
    }

  }

  private void clearCanvas() {
    this.bitmap = Bitmap.createBitmap(this.width, this.width, Bitmap.Config.RGB_565);
    canvas = new Canvas(this.bitmap);

    Rect rect = new Rect(0, 0, this.width, this.width);
    canvas.drawRect(rect, Paints.WATER);
  }

  private void drawPathForward() {
    int row1 = gameState.pathStartRow;
    int col1 = gameState.pathStartCol;

    GridPoint currentLocation = new GridPoint(row1, col1);

    drawStartingCircle(col1,row1,Paints.GREEN);

    for (int i = 0; i < gameState.currentPath.size(); i++) {
      GridPoint direction = gameState.currentPath.get(i);

      if (direction == GridPoint.MINE) {
        drawMines(currentLocation, i);
        continue;
      } else if (direction == GridPoint.TORPEDO) {
        drawTorpedoFirePointInPath(currentLocation);
        continue;
      } else if (direction.type.equals(GridPoint.DRONE_RESULT.type)) {
        drawDroneResultInPath(currentLocation, direction);
        continue;
      }

      GridPoint nextLocation = currentLocation.add(direction);

      drawLineSegment(currentLocation, nextLocation, Paints.PATH);

      // update the current row and col to the next row and col.
      currentLocation = nextLocation;
    }

    drawStartingCircle(currentLocation.col, currentLocation.row, Paints.RED);
  }

  private void drawPathBackward() {
    int row1 = gameState.pathEndRow;
    int col1 = gameState.pathEndCol;

    drawStartingCircle(col1,row1,Paints.RED);

    for (int i = gameState.currentPath.size()-1; i >=0; i--) {
      GridPoint direction = gameState.currentPath.get(i);
      int col2 = col1;
      int row2 = row1;

      if (direction == GridPoint.MINE) {
        GridPoint currentPoint = new GridPoint(row1, col1);
        drawMines(currentPoint, i);
        continue;
      } else if (direction == GridPoint.TORPEDO) {
          // draw a T at the position in the path the submarine fired from.
          drawTorpedoFirePointInPath(new GridPoint(row1, col1));
          continue;
      } else if (direction == GridPoint.NORTH) {
        row2++;
      } else if (direction == GridPoint.SOUTH) {
        row2--;
      } else if (direction == GridPoint.EAST) {
        col2--;
      } else if (direction == GridPoint.WEST) {
        col2++;
      }

      // draw the current line segment.
      drawLineSegment(col1, row1, col2, row2, Paints.PATH);

      // update the current row and col to the next row and col.
      col1 = col2;
      row1 = row2;
    }
    drawStartingCircle(col1, row1, Paints.GREEN);
  }

  private void drawStartingCircle(int col, int row, Paint paint) {
    float x = initialXOffset + col * xIterateOffset;
    float y = initialYOffset + row * yIterateOffset;
    canvas.drawCircle(x, y, circleRadius / 2, paint);
  }

  private void drawLineSegment(GridPoint current, GridPoint next, Paint paint) {
    drawLineSegment(current.col, current.row, next.col, next.row, paint);
  }
  private void drawLineSegment(int col1, int row1, int col2, int row2, Paint paint) {
    float x1 = initialXOffset + col1 * xIterateOffset;
    float y1 = initialYOffset + row1 * yIterateOffset;

    float x2 = initialXOffset + col2 * xIterateOffset;
    float y2 = initialYOffset + row2 * yIterateOffset;

    canvas.drawLine(x1, y1, x2, y2, paint);
  }

  private void drawTorpedoTarget(int row, int col) {
    //drawer.drawTorpedoTarget(row, col);
    drawTorpedo(row, col);
  }

  private void drawTargetingLines(int row, int col) {
    int maxCol = getCols();
    int maxRow = getRows();
    drawLineSegment(0, row, maxCol, row, Paints.YELLOW);
    drawLineSegment(col, 0, col, maxRow, Paints.YELLOW);
  }

  private void drawTorpedo(int row, int col) {
    Path path = new Path();
    path.setFillType(Path.FillType.EVEN_ODD);

    float x = colToX(col);
    float y = rowToY(row);

    float halfCircleRadius = this.circleRadius / 2;

    float leftX = x - halfCircleRadius;
    float rightX = x + halfCircleRadius;
    float topY = y - halfCircleRadius;
    float botY = y + halfCircleRadius;

    path.moveTo(rightX, topY);
    path.lineTo(leftX, topY);
    path.lineTo(leftX, botY);
    path.lineTo(rightX, botY);
    path.close();

    canvas.drawPath(path, Paints.RED);
  }

  private void drawTorpedoTracking() {
    for (GridPoint start : gameState.torpedoTracker.pointToPaths.keySet()) {
      List<GridPointPath> possiblePaths = gameState.torpedoTracker.pointToPaths.get(start);
      for (GridPointPath possiblePath : possiblePaths) {
        drawTorpedoPossiblePath(possiblePath);
      }
    }
  }

  private void drawTorpedoPossiblePath(GridPointPath path) {
    for (int i = 0; i < path.size() - 1; i++) {
      int row1 = path.get(i).row;
      int col1 = path.get(i).col;
      int row2 = path.get(i + 1).row;
      int col2 = path.get(i + 1).col;

      // prevent drawing diagonal lines.
      if (row1 == row2 || col1 == col2) {
        drawLineSegment(col1, row1, col2, row2, Paints.YELLOW);
      } else {
        // not sure why coordinates would ever be diagonal from each other.
      }
    }
  }

  private void drawTorpedoes() {
    for (GridPoint point : gameState.torpedoes) {
      drawTorpedo(point.row, point.col);
    }
  }

  private void drawTorpedoFirePointInPath(GridPoint point) {
    float xx = initialXOffset + point.col * xIterateOffset;
    float yy = initialYOffset + point.row * yIterateOffset + yIterateOffset * .25f;
    float textSize = .8f * xIterateOffset;

    Paints.TORPEDO_POINT_ON_PATH.setTextSize(textSize);
    canvas.drawText("T", xx, yy, Paints.TORPEDO_POINT_ON_PATH);
  }

  private void drawDroneResultInPath(GridPoint location, GridPoint information) {
    float xx = initialXOffset + location.col * xIterateOffset;
    float yy = initialYOffset + location.row * yIterateOffset + yIterateOffset * .25f;
    float textSize = .8f * xIterateOffset;

    Paint paint;
    String text = "" + information.droneRegionId;
    if (information.droneResult) {
      text += "+";
      paint = Paints.DRONE_RESULT_PAINT_POSITIVE;
    } else {
      text += "X";
      paint = Paints.DRONE_RESULT_PAINT_NEGATIVE;
    }

    paint.setTextSize(textSize);
    canvas.drawText(text, xx, yy, paint);
  }

  private void drawMines(GridPoint currentPoint, int i) {
    // gather all immediate positions when the mine was laid
    // eliminate false possibilities.
    // subs can't travel through mines they've placed.
    GridPoint lastDirection = null;
    GridPoint nextDirection = null;

    if (i > 0) {
      lastDirection = gameState.currentPath.get(i - 1);
    }

    if ((i + 1) < gameState.currentPath.size()) {
      nextDirection = gameState.currentPath.get(i + 1);
    }

    float radius = (float) (this.circleRadius * .9);
    List<GridPoint> points = new ArrayList<>();

    if (lastDirection != GridPoint.SOUTH && nextDirection != GridPoint.NORTH) {
      points.add(currentPoint.north());
    }

    if (lastDirection != GridPoint.NORTH && nextDirection != GridPoint.SOUTH) {
      points.add(currentPoint.south());
    }

    if (lastDirection != GridPoint.WEST && nextDirection != GridPoint.EAST) {
      points.add(currentPoint.east());
    }

    if (lastDirection != GridPoint.EAST && nextDirection != GridPoint.WEST) {
      points.add(currentPoint.west());
    }

    for (GridPoint gp : points) {
      // only draw a mine if it's in a water location.
      // don't draw mines on land.
      if (this.gameState.radar.map.getCoord(gp)) {
        canvas.drawCircle(initialXOffset + gp.col * xIterateOffset, initialYOffset + gp.row * yIterateOffset, radius, Paints.RED);
        canvas.drawCircle(initialXOffset + gp.col * xIterateOffset, initialYOffset + gp.row * yIterateOffset, (float) .66 * radius, Paints.WHITE);
        canvas.drawCircle(initialXOffset + gp.col * xIterateOffset, initialYOffset + gp.row * yIterateOffset, (float) .33 * radius, Paints.RED);
      }
    }
  }

  private void drawCurrentPossiblePosition() {
    for (GridPoint g : gameState.radar.possibleCurrentPositions) {
      addCircle(g, Color.GREEN);
    }
  }

  private void addCircle(GridPoint point, int greenOrRed) {
    addCircle(point.col, point.row, greenOrRed);
  }

  private void addCircle(int col, int row, int greenOrRed) {
    Paint paint = greenOrRed == Color.RED ? Paints.RED : Paints.CIRCLE;
    canvas.drawCircle(initialXOffset + col * xIterateOffset, initialYOffset + row * yIterateOffset, 3 * circleRadius / 4, paint);
  }

  private void drawCurrentPosition() {
    drawStartingCircle(gameState.currentRow, gameState.currentCol, Paints.YELLOW);
  }
}
