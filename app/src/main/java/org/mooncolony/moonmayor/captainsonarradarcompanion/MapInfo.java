package org.mooncolony.moonmayor.captainsonarradarcompanion;

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

import org.mooncolony.moonmayor.captainsonarradarcompanion.drawing.Paints;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewtduffin on 03/10/16.
 */
public class MapInfo {
  int width, height;
  boolean showingPath = false;
  int pathStartCol, pathStartRow;

  float circleRadius;
  float initialXOffset, initialYOffset;
  float xIterateOffset, yIterateOffset;
  float textColumnAdditionalOffset;
  Activity activity;
  ImageView mapImageView;
  Canvas canvas;
  Bitmap bitmap;
  List<GridPoint> currentPath;
  RadarTracker gameTracker;


  public MapInfo(Activity activity, ImageView mapImageView, RadarTracker gameTracker) {
    DisplayMetrics display = activity.getResources().getDisplayMetrics();
    this.width = display.widthPixels;
    this.height = display.heightPixels;

    // constrain the ImageView to the exact dimensions of the bitmap.
    mapImageView.setMinimumHeight(this.width);
    mapImageView.setMaxHeight(this.width);

    this.bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.RGB_565);
    Canvas canvas = new Canvas(this.bitmap);

    this.activity = activity;
    this.mapImageView = mapImageView;
    this.gameTracker = gameTracker;
    this.canvas = canvas;
    this.currentPath = new ArrayList<>();

    // add one to account for a half circle padding on each edge
    this.circleRadius = (float) ((1.0 * this.width) / (gameTracker.map.cols + 1) / 2);

    this.initialXOffset = 3 * this.circleRadius;
    this.initialYOffset = 3 * this.circleRadius;

    this.xIterateOffset = this.circleRadius * 2;
    this.yIterateOffset = this.circleRadius * 2;

    this.textColumnAdditionalOffset = circleRadius/2;

    initialize();
  }

  public void initialize() {
    drawBase();
    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

  public void drawBase() {
    clearCanvas();
    drawSectionLines();
    drawCircles();
    drawIslands();
    drawLetters();
  }

  public void drawSectionLines() {
    for (int row = 0; row < gameTracker.map.rows-1; row+=5) {
      float x = this.initialXOffset + row * this.xIterateOffset -xIterateOffset/2;
      this.canvas.drawLine(x, 0, x, this.width, Paints.BLACK);
    }

    for (int col = 0; col < gameTracker.map.cols-1; col+=5) {
      float y = this.initialYOffset + col * this.yIterateOffset -yIterateOffset/2;
      this.canvas.drawLine(0, y, this.width, y, Paints.BLACK);
    }
  }

  public void drawCircles() {
    for (int row = 0; row < gameTracker.map.rows; row++) {
      for (int col = 0; col < gameTracker.map.cols; col++) {
        float y = this.initialYOffset + col * this.yIterateOffset;
        float x = this.initialXOffset + row * this.xIterateOffset;
        this.canvas.drawCircle(x, y, circleRadius/8, Paints.WHITE);
      }
    }
  }

  public void drawIslands() {
    for (GridPoint island : gameTracker.islands) {
      int x = Math.round(circleRadius + xIterateOffset/2 + island.col * this.xIterateOffset);
      int y = Math.round(circleRadius + yIterateOffset/2 + island.row * this.yIterateOffset);
      int squareSize = Math.round(2 * circleRadius);

      Rect rect = new Rect(x, y, x + squareSize, y + squareSize);
      this.canvas.drawRect(rect, Paints.ISLAND);
    }
  }

  public void drawLetters() {
    for (int row = 0; row < gameTracker.map.rows; row++) {
      float x = this.initialXOffset + row * this.xIterateOffset;
      String colLetter = ""+(char)(row+65);

      Paints.TEXT.setTextSize(4 * circleRadius / 3);
      canvas.drawText(colLetter,x,initialXOffset-3*xIterateOffset/4,Paints.TEXT);
    }

    for (int col = 0; col < gameTracker.map.cols; col++) {
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
    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

  public void restartGame(String template) {
    this.showingPath = false;
    this.pathStartRow = 0;
    this.pathStartCol = 0;

    this.gameTracker = new RadarTracker(new Map(template));
    clearCanvas();
    this.currentPath = new ArrayList<>();
  }

  public void updatePath(GridPoint gp) {
    drawBase();

    this.currentPath.add(gp);
    gameTracker.track(this.currentPath);

    drawPossibleStartingLocations();

    drawPath();
  }

  public void drawPossibleStartingLocations() {
    for (GridPoint g : gameTracker.getStartingPoints()) {
      this.addCircle(g,Color.GREEN);
    }
  }

  public void addCircle(GridPoint point, int greenOrRed) {
    addCircle(point.col, point.row, greenOrRed);
  }

  public void addCircle(int col, int row, int greenOrRed) {
    Paint paint = greenOrRed == Color.RED ? Paints.RED : Paints.CIRCLE;

    canvas.drawCircle(initialXOffset + col * xIterateOffset, initialYOffset + row * yIterateOffset, 3 * circleRadius / 4, paint);

    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

  public void addMine() {
    currentPath.add(GridPoint.MINE);
    drawPath();
  }

  public void drawMine(GridPoint point) {
    drawMine(point.col, point.row);
  }

  public void drawMine(int col, int row) {
    // adjust the radius of the circle so it's slightly smaller than really defined.
    float radius = (float) (this.circleRadius * .9);

    canvas.drawCircle(initialXOffset+col*xIterateOffset, initialYOffset+row*yIterateOffset, radius, Paints.RED);
    canvas.drawCircle(initialXOffset+col*xIterateOffset, initialYOffset+row*yIterateOffset, (float) .66 * radius, Paints.WHITE);
    canvas.drawCircle(initialXOffset+col*xIterateOffset, initialYOffset+row*yIterateOffset, (float) .33 * radius, Paints.RED);

    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
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

  public void drawPath(float x, float y) {
    this.showingPath = true;

    // don't try and draw a path if there's no path to draw.
    if (currentPath.size() == 0) {
      return;
    }

    int col1 = this.xToCol(x);
    int row1 = this.yToRow(y);

    // if the path is already drawn from this path and column then return
    // from this function before anything computationally expensive happens.
    if (col1 == this.pathStartCol && row1 == this.pathStartRow) {
      return;
    }

    this.pathStartCol = col1;
    this.pathStartRow = row1;
    drawPath();
  }

  public void drawPath() {
    if (!this.showingPath) {
      return;
    }

    // clear the board, draw the map, draw the possibilities and draw the path from the click position.
    drawBase();
    drawPossibleStartingLocations();

    int col1 = this.pathStartCol;
    int row1 = this.pathStartRow;

    for (GridPoint direction : currentPath) {
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

    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

  public void drawTorpedoTarget(int row, int col) {
    drawBase();
    drawPossibleStartingLocations();
    drawPath();

    drawTargetingLines(row, col);
    drawTorpedo(row, col);
  }

  public void drawTargetingLines(int row, int col) {
    int maxCol = gameTracker.map.cols;
    int maxRow = gameTracker.map.rows;
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

  public void placeTorpedo() {

  }

  public void cancelTorpedo() {

  }

}
