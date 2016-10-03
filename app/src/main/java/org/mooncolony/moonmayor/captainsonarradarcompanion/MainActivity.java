package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  MapInfo mapInfo;
  RadarTracker gameTracker;

  @BindView(R.id.mapView) ImageView mapView;
  @BindView(R.id.textView) TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    gameTracker = new RadarTracker(new Map());
    initializeMap();

  }

  private void initializeGame() {
    for (GridPoint gp : gameTracker.getStartingPoints()) {
      mapInfo.addCircle(gp.col,gp.row,Color.GREEN);
    }
  }

  private void initializeMap() {
    ImageView mapImageView = (ImageView) findViewById(R.id.mapView);
    Bitmap mapBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map);

    //Create a new image bitmap and attach a brand new canvas to it
    Bitmap newBitmap = Bitmap.createBitmap(mapBitmap.getWidth(), mapBitmap.getHeight(), Bitmap.Config.RGB_565);
    Canvas canvas = new Canvas(newBitmap);

    //Draw the image bitmap into the canvas
    canvas.drawBitmap(mapBitmap, 0, 0, null);

    //Attach the canvas to the ImageView
    mapImageView.setImageDrawable(new BitmapDrawable(getResources(), newBitmap));

    mapInfo = new MapInfo(this,mapImageView,canvas,newBitmap);
  }

  private void updateMap() {

  }

  @OnClick({R.id.northButton})
  void northButtonClick() {
    String newString = textView.getText().toString();
    if (newString.length() != 0) {
      newString+=", ";
    }
    textView.setText(newString + "N");
  }
  @OnClick({R.id.eastButton})
  void eastButtonClick() {
    String newString = textView.getText().toString();
    if (newString.length() != 0) {
      newString+=", ";
    }
    textView.setText(newString + "E");
  }
  @OnClick({R.id.southButton})
  void southButtonClick() {
    String newString = textView.getText().toString();
    if (newString.length() != 0) {
      newString+=", ";
    }
    textView.setText(newString + "S");
  }
  @OnClick({R.id.westButton})
  void westButtonClick() {
    String newString = textView.getText().toString();
    if (newString.length() != 0) {
      newString+=", ";
    }
    textView.setText(newString + "W");
  }

  @OnClick({R.id.resetButton})
  void resetButtonClick() {
    textView.setText("");
    mapInfo.clearCanvas();
  }

  @OnClick({R.id.startButton})
  void startButtonClick() {
    initializeGame();
  }

}
