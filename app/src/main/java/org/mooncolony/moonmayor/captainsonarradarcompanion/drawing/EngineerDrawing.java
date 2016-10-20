package org.mooncolony.moonmayor.captainsonarradarcompanion.drawing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.CircleHelper;

import java.util.ArrayList;

/**
 * Created by matthewtduffin on 20/10/16.
 */
public class EngineerDrawing {
  private Activity activity;

  private int width, height;

  //position on screen, row, column
  CircleHelper[][][] cards;
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
    this.cards = new CircleHelper[4][2][3];

    for (int i = 0; i < 4; i++) {

      cards[i][0][0] = new CircleHelper(3*cardPadding,3*cardPadding+i*cardVerticalOffset);
      cards[i][1][0] = new CircleHelper(3*cardPadding,(i+1)*cardVerticalOffset - 2*cardPadding);

      cards[i][0][1] = new CircleHelper(width/2,3*cardPadding + i*cardVerticalOffset);
      cards[i][1][1] = new CircleHelper(width/2,(i+1)*cardVerticalOffset - 2*cardPadding);

      cards[i][0][2] = new CircleHelper(width-3*cardPadding,3*cardPadding+i*cardVerticalOffset);
      cards[i][1][2] = new CircleHelper(width-3*cardPadding,(i+1)*cardVerticalOffset - 2*cardPadding);

    }

    cards[0][0][0].paint = Paints.YELLOW;
    cards[0][0][2].paint = Paints.YELLOW;
    cards[1][0][1].paint = Paints.YELLOW;
    cards[2][1][2].paint = Paints.YELLOW;
    cards[3][0][1].paint = Paints.YELLOW;

    cards[0][0][1].paint = Paints.RED;
    cards[1][1][1].paint = Paints.RED;
    cards[2][0][2].paint = Paints.RED;
    cards[3][0][0].paint = Paints.RED;
    cards[3][1][1].paint = Paints.RED;

    cards[0][1][1].paint = Paints.GREEN;
    cards[1][1][0].paint = Paints.GREEN;
    cards[1][0][2].paint = Paints.GREEN;
    cards[2][1][1].paint = Paints.GREEN;
    cards[3][0][2].paint = Paints.GREEN;

    cards[0][1][0].paint = Paints.GREEN;
    cards[1][0][0].paint = Paints.GREEN;
    cards[2][0][0].paint = Paints.GREEN;
    cards[2][1][0].paint = Paints.GREEN;
    cards[3][1][0].paint = Paints.GREEN;


    this.circleRadius = width/20;

    this.bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    Canvas canvas = new Canvas(this.bitmap);

    this.canvas = canvas;

  }

  private void draw() {
    drawBackground();
    drawCards();
    drawConnectors();
    drawCircles();
    drawText();

    imageView.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));
  }

  private void drawBackground() {
    Rect rect = new Rect(0, 0, this.width, this.height);
    canvas.drawRect(rect, Paints.GREY);
  }

  private void drawText() {
    Paints.ENGINEER_TEXT.setTextSize(cardHeight*2/3);
    canvas.drawText("N",width/2,cardVerticalOffset - cardHeight/4,Paints.ENGINEER_TEXT);
    canvas.drawText("E",width/2,2*cardVerticalOffset - cardHeight/4,Paints.ENGINEER_TEXT);
    canvas.drawText("W",width/2,3*cardVerticalOffset - cardHeight/4,Paints.ENGINEER_TEXT);
    canvas.drawText("S",width/2,4*cardVerticalOffset - cardHeight/4,Paints.ENGINEER_TEXT);
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
    ArrayList<CircleHelper> circles = new ArrayList<>();

    circles.add(cards[0][0][0]);
    circles.add(cards[0][0][2]);
    circles.add(cards[1][0][1]);
    circles.add(cards[2][1][2]);
    circles.add(cards[3][0][1]);

    circles.add(cards[0][0][1]);
    circles.add(cards[1][1][1]);
    circles.add(cards[2][0][2]);
    circles.add(cards[3][0][0]);
    circles.add(cards[3][1][1]);

    circles.add(cards[0][1][1]);
    circles.add(cards[1][1][0]);
    circles.add(cards[1][0][2]);
    circles.add(cards[2][1][1]);
    circles.add(cards[3][0][2]);

    circles.add(cards[0][1][0]);
    circles.add(cards[1][0][0]);
    circles.add(cards[2][0][0]);
    circles.add(cards[2][1][0]);
    circles.add(cards[3][1][0]);

    for (CircleHelper c : circles) {
      drawCircle(c);
    }

  }

  private void drawCircle(CircleHelper c) {
    this.canvas.drawCircle(c.x,c.y,circleRadius*1.1f,Paints.BLACK);
    this.canvas.drawCircle(c.x, c.y, circleRadius, c.paint);
  }

  private void drawConnectors() {

    //draw regular connectors
    drawConnector(cards[0][0][1], cards[0][1][1]);
    drawConnector(cards[0][0][1], cards[0][0][2]);
    drawConnector(cards[0][0][2], cards[1][0][2]);
    drawConnector(cards[1][1][1], cards[2][1][1]);
    drawConnector(cards[2][1][1], cards[2][1][2]);
    drawConnector(cards[2][1][2], cards[2][0][2]);
    drawConnector(cards[3][0][2], cards[3][0][1]);
    drawConnector(cards[3][0][1], cards[3][1][1]);

    //draw special connector
    CircleHelper start = cards[1][0][1], end = cards[3][1][1];
    float midPoint = (start.x+cards[0][0][0].x)/2;
    this.canvas.drawLine(start.x, start.y, midPoint, start.y, Paints.ENGINEER_CONNECTOR);
    this.canvas.drawLine(midPoint, start.y, midPoint, end.y, Paints.ENGINEER_CONNECTOR);
    this.canvas.drawLine(end.x, end.y, midPoint, end.y, Paints.ENGINEER_CONNECTOR);

  }

  private void drawConnector(CircleHelper start, CircleHelper end) {
    this.canvas.drawLine(start.x, start.y, end.x, end.y, Paints.ENGINEER_CONNECTOR);
  }

  public void addCross(float xTouch, float yTouch) {
    float c = circleRadius/2;
    int card = 0, row = 0, col = 0;

    //set column based on touch
    if (xTouch > width*2/3) {
      col = 2;
    } else if (xTouch > width/3) {
      col = 1;
    }

    //set card based on touch
    if (yTouch > height*3/4) {
      card = 3;
    } else if (yTouch > height/2) {
      card = 2;
    } else if (yTouch > height/4) {
      card = 1;
    }

    //set row based on touch & card number
    yTouch-=(card*cardVerticalOffset);
    if (yTouch > cardPadding+cardHeight/2) {
      row = 1;
    }

    CircleHelper co = cards[card][row][col];

    if (co == cards[0][1][2] || co == cards[1][1][2] || co == cards[2][0][1] || co == cards[3][1][2]) {
      return;
    }

    if (!co.marked) {
      float x = co.x;
      float y = co.y;

      this.canvas.drawLine(x-c, y-c, x+c, y+c, Paints.CROSS_PAINT);
      this.canvas.drawLine(x+c, y-c, x-c, y+c, Paints.CROSS_PAINT);
    } else {
      drawCircle(co);

    }
    co.marked = !co.marked;
    imageView.setImageDrawable(new BitmapDrawable(activity.getResources(), this.bitmap));


  }

}
