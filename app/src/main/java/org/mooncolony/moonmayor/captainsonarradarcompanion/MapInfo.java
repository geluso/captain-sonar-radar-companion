package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by matthewtduffin on 03/10/16.
 */
public class MapInfo {
  int circleRadius;
  int initialXOffset, initialYOffset;
  int xIterateOffset, yIterateOffset;
  Activity activity;
  ImageView mapImageView;
  Canvas canvas;
  Bitmap newBitmap;
  Paint paint;


  public MapInfo(Activity activity, ImageView mapImageView, Canvas canvas, Bitmap newBitmap) {

    this.activity = activity;
    this.mapImageView = mapImageView;
    this.canvas = canvas;
    this.newBitmap = newBitmap;

    Display display = activity.getWindowManager().getDefaultDisplay();
    int phoneWidth = display.getWidth();
    Log.i("MATT-TEST", "Phone width is: " + phoneWidth + "pix");

    this.initialXOffset = 157;
    this.initialYOffset = 161;

    this.xIterateOffset = 99;
    this.yIterateOffset = 99;

    this.circleRadius = 30;

    this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.paint.setStrokeWidth(4.0f);
    this.paint.setColor(Color.RED);
    this.paint.setStyle(Paint.Style.STROKE);
  }

  public void addCircle(int xVal, int yVal) {

    canvas.drawCircle(initialXOffset+xVal*xIterateOffset, initialYOffset+yVal*yIterateOffset, circleRadius, paint);

    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), newBitmap));
  }

}
