package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewtduffin on 03/10/16.
 */
public class MapInfo {
  int width, height;

  float circleRadius;
  float initialXOffset, initialYOffset;
  float xIterateOffset, yIterateOffset;
  Activity activity;
  ImageView mapImageView;
  Canvas canvas;
  Bitmap bitmap;
  List<GridPoint> currentPath;
  RadarTracker gameTracker;
  Paint redPaint, greenPaint, whitePaint, blackPaint;
  Paint waterPaint, islandPaint, pathPaint;


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

    Log.i("MATT-TEST", "Phone width is: " + this.width + "pix");
    Log.i("MATT-TEST", "Circle radius is: " + this.circleRadius + "pix");

    this.initialXOffset = 2 * this.circleRadius;
    this.initialYOffset = 2 * this.circleRadius;

    this.xIterateOffset = this.circleRadius * 2;
    this.yIterateOffset = this.circleRadius * 2;

    this.redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.redPaint.setColor(Color.RED);
    this.redPaint.setStyle(Paint.Style.FILL);

    this.greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.greenPaint.setStrokeWidth(6.0f);
    this.greenPaint.setColor(Color.GREEN);
    this.greenPaint.setStyle(Paint.Style.STROKE);

    this.whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.whitePaint.setColor(Color.WHITE);
    this.whitePaint.setStyle(Paint.Style.FILL);

    this.blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.blackPaint.setColor(Color.BLACK);
    this.blackPaint.setStyle(Paint.Style.FILL);

    this.pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.pathPaint.setColor(Color.BLACK);
    this.pathPaint.setStrokeWidth(8.0f);
    this.pathPaint.setStyle(Paint.Style.FILL);

    this.waterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.waterPaint.setColor(Color.BLUE);
    this.waterPaint.setStyle(Paint.Style.FILL);

    this.islandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.islandPaint.setColor(Color.rgb(255,248,220));
    this.islandPaint.setStyle(Paint.Style.FILL);

    initialize();
  }

  public void initialize() {
    drawBase();
    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

  public void drawBase() {
    clearCanvas();
    drawGrid();
    drawIslands();
  }

  public void drawGrid() {
    for (int row = 0; row < gameTracker.map.rows; row++) {
      float x = this.initialXOffset + row * this.xIterateOffset;
      this.canvas.drawLine(x, 0, x, this.width, blackPaint);
    }

    for (int col = 0; col < gameTracker.map.cols; col++) {
      float y = this.initialYOffset + col * this.yIterateOffset;
      this.canvas.drawLine(0, y, this.width, y, blackPaint);
    }
  }

  public void drawIslands() {
    for (GridPoint island : gameTracker.islands) {
      int x = Math.round(this.circleRadius + island.col * this.xIterateOffset);
      int y = Math.round(this.circleRadius + island.row * this.yIterateOffset);
      int squareSize = Math.round(2 * circleRadius);

      Rect rect = new Rect(x, y, x + squareSize, y + squareSize);
      this.canvas.drawRect(rect, islandPaint);
    }
  }

  public void clearCanvas() {
    this.bitmap = Bitmap.createBitmap(this.width, this.width, Bitmap.Config.RGB_565);
    canvas = new Canvas(this.bitmap);

    Rect rect = new Rect(0, 0, this.width, this.width);
    canvas.drawRect(rect, waterPaint);

    //Attach the canvas to the ImageView
    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

  public void restartGame() {
    this.gameTracker = new RadarTracker(new Map());
    clearCanvas();
    this.currentPath = new ArrayList<>();
  }

  public void updatePath(GridPoint gp) {
    drawBase();

    this.currentPath.add(gp);
    gameTracker.track(this.currentPath);

    drawPossibleStartingLocations();
  }

  public void drawPossibleStartingLocations() {
    for (GridPoint g : gameTracker.getStartingPoints()) {
      this.addCircle(g.col,g.row,Color.GREEN);
    }
  }

  public void addCircle(GridPoint point, int greenOrRed) {
    addCircle(point.col, point.row, greenOrRed);
  }

  public void addCircle(int col, int row, int greenOrRed) {
    Paint paint = greenOrRed == Color.RED ? redPaint : greenPaint;

    canvas.drawCircle(initialXOffset+col*xIterateOffset, initialYOffset+row*yIterateOffset, circleRadius, paint);

    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

  public void addMine(GridPoint point) {
    addMine(point.col, point.row);
  }

  public void addMine(int col, int row) {
    // adjust the radius of the circle so it's slightly smaller than really defined.
    float radius = (float) (this.circleRadius * .9);

    canvas.drawCircle(initialXOffset+col*xIterateOffset, initialYOffset+row*yIterateOffset, radius, redPaint);
    canvas.drawCircle(initialXOffset+col*xIterateOffset, initialYOffset+row*yIterateOffset, (float) .66 * radius, whitePaint);
    canvas.drawCircle(initialXOffset+col*xIterateOffset, initialYOffset+row*yIterateOffset, (float) .33 * radius, redPaint);

    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

  public void drawPath(float x, float y) {
    // clear the board, draw the map, draw the possibilities and draw the path from the click position.
    drawBase();
    drawPossibleStartingLocations();

    // don't try and draw a path if there's no path to draw.
    if (currentPath.size() == 0) {
      return;
    }

    float xx = x - this.initialXOffset;
    float yy = y - this.initialYOffset;

    xx = x / this.xIterateOffset;
    yy = y / this.yIterateOffset;

    xx = Math.round(xx);
    yy = Math.round(yy);

    int col1 = (int) xx - 1;
    int row1 = (int) yy - 1;

    for (GridPoint direction : currentPath) {
      int col2 = col1;
      int row2 = row1;

      if (direction == GridPoint.NORTH) {
        row2--;
      } else if (direction == GridPoint.SOUTH) {
        row2++;
      } else if (direction == GridPoint.EAST) {
        col2++;
      } else if (direction == GridPoint.WEST) {
        col2--;
      }

      // draw the current line segment.
      drawLineSegment(col1, row1, col2, row2);

      // update the current row and col to the next row and col.
      col1 = col2;
      row1 = row2;
    }
  }

  public void drawLineSegment(int col1, int row1, int col2, int row2) {
    float x1 = initialXOffset + col1 * xIterateOffset;
    float y1 = initialYOffset + row1 * yIterateOffset;

    float x2 = initialXOffset + col2 * xIterateOffset;
    float y2 = initialYOffset + row2 * yIterateOffset;

    canvas.drawLine(x1, y1, x2, y2, pathPaint);

    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

}
