package org.mooncolony.moonmayor.captainsonarradarcompanion.drawing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by matthewtduffin on 20/10/16.
 */
public class EngineerDrawing {
  private Activity activity;

  private int width, height;

  private int cardPadding, cardWidth, cardHeight, cardVerticalOffset;

  private int circleRadius;

  private Bitmap bitmap;
  private Canvas canvas;

  private ImageView imageView;



  public EngineerDrawing(Activity activity, ImageView imageView) {
    this.activity = activity;
    this.imageView = imageView;
    setDimensions();
    draw();
  }

  private void setDimensions() {
    DisplayMetrics display = activity.getResources().getDisplayMetrics();

    this.width = display.widthPixels;
    this.height = display.heightPixels;

    this.cardPadding = width/20;
    this.cardHeight = (height - 5*cardPadding)/4;
    this.cardWidth =  width - cardPadding;
    this.cardVerticalOffset = cardHeight + cardPadding;

    this.circleRadius = width/20;

    this.bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    Canvas canvas = new Canvas(this.bitmap);

    this.canvas = canvas;

  }

  private void draw() {
    drawBackground();
    drawCards();
    drawCircles();

    imageView.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

  private void drawBackground() {
    Rect rect = new Rect(0, 0, this.width, this.height);
    canvas.drawRect(rect, Paints.GREY);
  }

  private void drawCards() {
    for (int i = 0; i < 4; i++) {
      int top = cardPadding + i*(cardVerticalOffset);
      int bottom = (i+1)*cardVerticalOffset;
      Rect rect = new Rect(cardPadding, top, width-cardPadding, bottom);
      canvas.drawRect(rect, Paints.WHITE);
    }
  }

  private void drawCircles() {
    float x = cardPadding*3;
    float y = cardPadding*3;

    drawCircle(x,y,Paints.YELLOW);

  }

  private void drawCircle(float x, float y, Paint p) {
    this.canvas.drawCircle(x,y,circleRadius*1.1f,Paints.BLACK);
    this.canvas.drawCircle(x, y, circleRadius, p);
  }

}
