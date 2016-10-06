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
import org.mooncolony.moonmayor.captainsonarradarcompanion.GridPoint;

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
  private int rows;
  private int cols;

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

    this.rows = gameState.radar.map.rows;
    this.cols = gameState.radar.map.cols;

    DisplayMetrics display = activity.getResources().getDisplayMetrics();
    this.width = display.widthPixels;
    this.height = display.heightPixels;

    // constrain the ImageView to the exact dimensions of the bitmap.
    map.setMinimumHeight(this.width);
    map.setMaxHeight(this.width);

    this.bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.RGB_565);
    Canvas canvas = new Canvas(this.bitmap);
    this.canvas = canvas;

    // add one to account for a half circle padding on each edge
    this.circleRadius = (float) ((1.0 * this.width) / (this.cols + 1) / 2);

    this.initialXOffset = 3 * this.circleRadius;
    this.initialYOffset = 3 * this.circleRadius;

    this.xIterateOffset = this.circleRadius * 2;
    this.yIterateOffset = this.circleRadius * 2;

    this.textColumnAdditionalOffset = circleRadius/2;
  }

  public void drawAll() {
    drawBase();
//    drawPossibleStartingLocations();
    drawInvalidStartArea();
//    drawInvalidCurrentArea();
    drawPath();
  }

  public void drawBase() {
    clearCanvas();
    drawSectionLines();
    drawCircles();
    drawIslands();
    drawLetters();
  }

  public void drawSectionLines() {
    for (int row = 0; row < this.rows-1; row+=5) {
      float x = this.initialXOffset + row * this.xIterateOffset -xIterateOffset/2;
      this.canvas.drawLine(x, 0, x, this.width, Paints.BLACK);
    }

    for (int col = 0; col < this.cols-1; col+=5) {
      float y = this.initialYOffset + col * this.yIterateOffset -yIterateOffset/2;
      this.canvas.drawLine(0, y, this.width, y, Paints.BLACK);
    }
  }

  public void drawCircles() {
    for (int row = 0; row < this.rows; row++) {
      for (int col = 0; col < this.cols; col++) {
        float y = this.initialYOffset + col * this.yIterateOffset;
        float x = this.initialXOffset + row * this.xIterateOffset;
        this.canvas.drawCircle(x, y, circleRadius/8, Paints.WHITE);
      }
    }
  }

  public void drawIslands() {
    for (GridPoint island : gameState.radar.islands) {
      int x = Math.round(circleRadius + xIterateOffset/2 + island.col * this.xIterateOffset);
      int y = Math.round(circleRadius + yIterateOffset/2 + island.row * this.yIterateOffset);
      int squareSize = Math.round(2 * circleRadius);

      Rect rect = new Rect(x, y, x + squareSize, y + squareSize);
      this.canvas.drawRect(rect, Paints.ISLAND);
    }
  }

  public void drawLetters() {
    for (int row = 0; row < this.rows; row++) {
      float x = this.initialXOffset + row * this.xIterateOffset;
      String colLetter = ""+(char)(row+65);

      Paints.TEXT.setTextSize(4 * circleRadius / 3);
      canvas.drawText(colLetter,x,initialXOffset-3*xIterateOffset/4,Paints.TEXT);
    }

    for (int col = 0; col < this.cols; col++) {
      //the additional text column offset is to account for centring the letter
      float y = this.initialYOffset + col * this.yIterateOffset+this.textColumnAdditionalOffset;
      String rowNum = ""+(col+1);
      canvas.drawText(rowNum,circleRadius,y,Paints.TEXT);
    }


  }

  public void clearCanvas() {
    this.bitmap = Bitmap.createBitmap(this.width, this.width, Bitmap.Config.RGB_565);
    canvas = new Canvas(this.bitmap);

    Rect rect = new Rect(0, 0, this.width, this.width);
    canvas.drawRect(rect, Paints.WATER);

    //Attach the canvas to the ImageView
    map.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

  public void drawPath(List<GridPoint> path, float x, float y) {
    gameState.showingPath = true;

    // don't try and draw a path if there's no path to draw.
    if (path.size() == 0) {
      return;
    }

    gameState.currentPath = path;
    int col1 = this.xToCol(x);
    int row1 = this.yToRow(y);

    // if the path is already drawn from this path and column then return
    // from this function before anything computationally expensive happens.
    if (col1 == gameState.pathStartCol && row1 == gameState.pathStartRow) {
      return;
    }

    gameState.pathStartCol = col1;
    gameState.pathStartRow = row1;
    drawPath();
  }

  public void drawPath() {
    if (!gameState.showingPath) {
      return;
    }

    if (gameState.currentPath.size() == 0) {
      return;
    }

    // clear the board, draw the map, draw the possibilities and draw the path from the click position.
    drawBase();
//    drawPossibleStartingLocations();
    drawInvalidStartArea();
//    drawInvalidCurrentArea();
    int col1 = gameState.pathStartCol;
    int row1 = gameState.pathStartRow;

    for (GridPoint direction : gameState.currentPath) {
      int col2 = col1;
      int row2 = row1;

      if (direction == GridPoint.MINE) {
        drawMine(col1, row1);
        continue;
      }else if (direction == GridPoint.NORTH) {
        row2--;
      } else if (direction == GridPoint.SOUTH) {
        row2++;
      } else if (direction == GridPoint.EAST) {
        col2++;
      } else if (direction == GridPoint.WEST) {
        col2--;
      }

      // draw the current line segment.
      drawLineSegment(col1, row1, col2, row2, Paints.PATH);

      // update the current row and col to the next row and col.
      col1 = col2;
      row1 = row2;
    }
  }

  public void drawLineSegment(int col1, int row1, int col2, int row2, Paint paint) {
    float x1 = initialXOffset + col1 * xIterateOffset;
    float y1 = initialYOffset + row1 * yIterateOffset;

    float x2 = initialXOffset + col2 * xIterateOffset;
    float y2 = initialYOffset + row2 * yIterateOffset;

    canvas.drawLine(x1, y1, x2, y2, paint);

    map.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

  public void drawTorpedoTarget(int row, int col) {
    drawBase();
//    drawPossibleStartingLocations();
    drawInvalidStartArea();
//    drawInvalidCurrentArea();
    drawPath();

    drawTargetingLines(row, col);
    drawTorpedo(row, col);
  }

  public void drawTargetingLines(int row, int col) {
    int maxCol = this.cols;
    int maxRow = this.rows;
    drawLineSegment(0, row, maxCol, row, Paints.YELLOW);
    drawLineSegment(col, 0, col, maxRow, Paints.YELLOW);
  }

  public void drawTorpedo(int row, int col) {
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

  public void drawMine(GridPoint point) {
    drawMine(point.col, point.row);
  }

  public void drawMine(int col, int row) {
    // adjust the radius of the circle so it's slightly smaller than really defined.
    float radius = (float) (this.circleRadius * .9);

    canvas.drawCircle(initialXOffset + col * xIterateOffset, initialYOffset + row * yIterateOffset, radius, Paints.RED);
    canvas.drawCircle(initialXOffset + col * xIterateOffset, initialYOffset + row * yIterateOffset, (float) .66 * radius, Paints.WHITE);
    canvas.drawCircle(initialXOffset + col * xIterateOffset, initialYOffset + row * yIterateOffset, (float) .33 * radius, Paints.RED);

    map.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
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

  public void drawInvalidCurrentArea() {
    for (GridPoint g : gameState.radar.getInvalidCurrentPoints()) {
      this.invalidatePoint(g);
    }
  }

  public void drawInvalidStartArea() {
    for (GridPoint g : gameState.radar.getInvalidStartPoints()) {
      this.invalidatePoint(g);
    }
  }

  private void invalidatePoint(GridPoint point) {
    int col = point.col, row = point.row;
    int x = Math.round(circleRadius + xIterateOffset/2 + col * this.xIterateOffset);
    int y = Math.round(circleRadius + yIterateOffset/2 + row * this.yIterateOffset);
    int squareSize = Math.round(2 * circleRadius);

    Rect rect = new Rect(x, y, x + squareSize, y + squareSize);
    this.canvas.drawRect(rect, Paints.AREA);

    this.map.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));

  }

  public void drawPossibleStartingLocations() {
    for (GridPoint g : gameState.radar.getValidStartPoints()) {
      this.addCircle(g, Color.GREEN);
    }
  }

  private void addCircle(GridPoint point, int greenOrRed) {
    addCircle(point.col, point.row, greenOrRed);
  }

  private void addCircle(int col, int row, int greenOrRed) {
    Paint paint = greenOrRed == Color.RED ? Paints.RED : Paints.CIRCLE;

    canvas.drawCircle(initialXOffset + col * xIterateOffset, initialYOffset + row * yIterateOffset, 3 * circleRadius / 4, paint);

    this.map.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

}
