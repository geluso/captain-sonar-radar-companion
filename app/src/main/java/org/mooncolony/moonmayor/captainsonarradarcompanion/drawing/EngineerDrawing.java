package org.mooncolony.moonmayor.captainsonarradarcompanion.drawing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.Coordinate;

import java.util.ArrayList;

/**
 * Created by matthewtduffin on 20/10/16.
 */
public class EngineerDrawing {
  private Activity activity;

  private int width, height;

  //position on screen, row, column
  Coordinate[][][] cards;
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
    this.cards = new Coordinate[4][2][3];

    for (int i = 0; i < 4; i++) {

      cards[i][0][0] = new Coordinate(3*cardPadding,3*cardPadding+i*cardVerticalOffset);
      cards[i][1][0] = new Coordinate(3*cardPadding,(i+1)*cardVerticalOffset - 2*cardPadding);

      cards[i][0][1] = new Coordinate(width/2,3*cardPadding + i*cardVerticalOffset);
      cards[i][1][1] = new Coordinate(width/2,(i+1)*cardVerticalOffset - 2*cardPadding);

      cards[i][0][2] = new Coordinate(width-3*cardPadding,3*cardPadding+i*cardVerticalOffset);
      cards[i][1][2] = new Coordinate(width-3*cardPadding,(i+1)*cardVerticalOffset - 2*cardPadding);

    }


    this.circleRadius = width/20;

    this.bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    Canvas canvas = new Canvas(this.bitmap);

    this.canvas = canvas;

  }

  private void draw() {
    drawBackground();
    drawCards();
    drawText();
    drawCircles();

    imageView.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

  private void drawBackground() {
    Rect rect = new Rect(0, 0, this.width, this.height);
    canvas.drawRect(rect, Paints.GREY);
  }

  private void drawText() {

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
    ArrayList<Coordinate> yellows = new ArrayList<>(),
                          reds = new ArrayList<>(),
                          greens = new ArrayList<>(),
                          whites = new ArrayList<>();
    yellows.add(cards[0][0][0]);
    yellows.add(cards[0][0][2]);
    yellows.add(cards[1][0][1]);
    yellows.add(cards[2][1][2]);
    yellows.add(cards[3][0][1]);

    reds.add(cards[0][0][1]);
    reds.add(cards[1][1][1]);
    reds.add(cards[2][0][2]);
    reds.add(cards[3][0][0]);
    reds.add(cards[3][1][1]);

    greens.add(cards[0][1][1]);
    greens.add(cards[1][1][0]);
    greens.add(cards[1][0][2]);
    greens.add(cards[2][1][1]);
    greens.add(cards[3][0][2]);

    whites.add(cards[0][1][0]);
    whites.add(cards[1][0][0]);
    whites.add(cards[2][0][0]);
    whites.add(cards[2][1][0]);
    whites.add(cards[3][1][0]);

    for (Coordinate c : yellows) {
      drawCircle(c.x, c.y, Paints.YELLOW);
    }

    for (Coordinate c : reds) {
      drawCircle(c.x, c.y, Paints.RED);
    }

    for (Coordinate c : greens) {
      drawCircle(c.x, c.y, Paints.GREEN);
    }

    for (Coordinate c : whites) {
      drawCircle(c.x, c.y, Paints.WHITE);
    }

  }

  private void drawCircle(float x, float y, Paint p) {
    this.canvas.drawCircle(x,y,circleRadius*1.1f,Paints.BLACK);
    this.canvas.drawCircle(x, y, circleRadius, p);
  }

}
