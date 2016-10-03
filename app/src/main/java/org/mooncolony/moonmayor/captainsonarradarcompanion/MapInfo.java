package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

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
  Paint redPaint, greenPaint;


  public MapInfo(Activity activity, ImageView mapImageView, Canvas canvas, Bitmap newBitmap) {

    this.activity = activity;
    this.mapImageView = mapImageView;
    this.canvas = canvas;
    this.newBitmap = newBitmap;

    Display display = activity.getWindowManager().getDefaultDisplay();
    int phoneWidth = display.getWidth();
    Log.i("MATT-TEST", "Phone width is: " + phoneWidth + "pix");


    this.initialXOffset = (int) ((phoneWidth*1.0/1440) * 157);
    this.initialYOffset = (int) ((phoneWidth*1.0/1440) * 161);

    this.xIterateOffset = (int) ((phoneWidth*1.0/1440) *  99);
    this.yIterateOffset = (int) ((phoneWidth*1.0/1440) *  99);

    this.circleRadius = (int) ((phoneWidth*1.0/1440) * 40);

    this.redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.redPaint.setColor(Color.RED);
    this.redPaint.setStyle(Paint.Style.FILL);

    this.greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    this.greenPaint.setStrokeWidth(10.0f);
    this.greenPaint.setColor(Color.GREEN);
    this.greenPaint.setStyle(Paint.Style.STROKE);
  }

  public void clearCanvas(Bitmap mapBitmap) {
    newBitmap = Bitmap.createBitmap(mapBitmap.getWidth(), mapBitmap.getHeight(), Bitmap.Config.RGB_565);
    canvas = new Canvas(newBitmap);

    //Draw the image bitmap into the canvas
    canvas.drawBitmap(mapBitmap, 0, 0, null);

    //Attach the canvas to the ImageView
    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), newBitmap));
  }

  public void addCircle(int col, int row, int greenOrRed) {

    Paint paint = greenOrRed == Color.RED ? redPaint : greenPaint;

    canvas.drawCircle(initialXOffset+col*xIterateOffset, initialYOffset+row*yIterateOffset, circleRadius, paint);

    mapImageView.setImageDrawable(new BitmapDrawable(activity.getResources(), newBitmap));
  }

}
