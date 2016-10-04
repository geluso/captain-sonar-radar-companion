package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewtduffin on 03/10/16.
 */
public class MapInfo {
  float circleRadius;
  float initialXOffset, initialYOffset;
  float xIterateOffset, yIterateOffset;
  Activity activity;
  ImageView mapImageView;
  Canvas canvas;
  Bitmap newBitmap, mapBitmap;
  List<GridPoint> currentPath;
  RadarTracker gameTracker;
  Paint redPaint, greenPaint, whitePaint;


  public MapInfo(Activity activity, ImageView mapImageView, Canvas canvas,
                 Bitmap newBitmap, RadarTracker gameTracker, Bitmap mapBitmap) {

    this.mapBitmap = mapBitmap;
    this.gameTracker = gameTracker;
    this.activity = activity;
    this.mapImageView = mapImageView;
    this.canvas = canvas;
    this.newBitmap = newBitmap;
    this.currentPath = new ArrayList<>();

    Display display = activity.getWindowManager().getDefaultDisplay();
    int phoneWidth = display.getWidth();
    Log.i("MATT-TEST", "Phone width is: " + phoneWidth + "pix");


    this.initialXOffset = (float) ((phoneWidth*1.0/1440) * 157);
    this.initialYOffset = (float) ((phoneWidth*1.0/1440) * 161);

    this.xIterateOffset = (float) ((phoneWidth*1.0/1440) *  99);
    this.yIterateOffset = (float) ((phoneWidth*1.0/1440) *  99);

    this.circleRadius = (float) ((phoneWidth*1.0/1440) * 40);

    this.redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.redPaint.setColor(Color.RED);
    this.redPaint.setStyle(Paint.Style.FILL);

    this.greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.greenPaint.setStrokeWidth(10.0f);
    this.greenPaint.setColor(Color.GREEN);
    this.greenPaint.setStyle(Paint.Style.STROKE);

    this.whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.whitePaint.setColor(Color.WHITE);
    this.whitePaint.setStyle(Paint.Style.FILL);
  }

  public void updatePath(GridPoint gp) {
    clearCanvas();
    this.currentPath.add(gp);
    gameTracker.track(this.currentPath);
    for (GridPoint g : gameTracker.getStartingPoints()) {
      this.addCircle(g.col,g.row,Color.GREEN);
    }
  }

  public void initialize() {
    clearCanvas();
    for (GridPoint gp : gameTracker.getStartingPoints()) {
      this.addCircle(gp.col,gp.row,Color.GREEN);
    }
  }

  public void restartGame() {
    this.gameTracker = new RadarTracker(new Map());
    clearCanvas();
    this.currentPath = new ArrayList<>();
  }

  public void clearCanvas() {
    newBitmap = Bitmap.createBitmap(mapBitmap.getWidth(), mapBitmap.getHeight(), Bitmap.Config.RGB_565);
    canvas = new Canvas(newBitmap);

    //Draw the image bitmap into the canvas
    canvas.drawBitmap(mapBitmap, 0, 0, null);

    //Attach the canvas to the ImageView
    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), newBitmap));
  }

  public void addCircle(GridPoint point, int greenOrRed) {
    addCircle(point.col, point.row, greenOrRed);
  }

  public void addCircle(int col, int row, int greenOrRed) {

    Paint paint = greenOrRed == Color.RED ? redPaint : greenPaint;

    canvas.drawCircle(initialXOffset+col*xIterateOffset, initialYOffset+row*yIterateOffset, circleRadius, paint);

    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), newBitmap));
  }

  public void addMine(GridPoint point) {
    addMine(point.col, point.row);
  }

  public void addMine(int col, int row) {
    canvas.drawCircle(initialXOffset+col*xIterateOffset, initialYOffset+row*yIterateOffset, circleRadius, redPaint);
    canvas.drawCircle(initialXOffset+col*xIterateOffset, initialYOffset+row*yIterateOffset, (float) .66 * circleRadius, whitePaint);
    canvas.drawCircle(initialXOffset+col*xIterateOffset, initialYOffset+row*yIterateOffset, (float) .33 * circleRadius, redPaint);

    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), newBitmap));
  }

}
