package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  MapInfo mapInfo;
  RadarTracker gameTracker;
  public Bitmap mapBitmap;

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
    mapInfo.initialize();
    for (GridPoint gp : gameTracker.getStartingPoints()) {
      mapInfo.addCircle(gp, Color.GREEN);
    }
  }

  private void initializeMap() {
    ImageView mapImageView = (ImageView) findViewById(R.id.mapView);
    mapBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map);

    //Create a new image bitmap and attach a brand new canvas to it
    Bitmap newBitmap = Bitmap.createBitmap(mapBitmap.getWidth(), mapBitmap.getHeight(), Bitmap.Config.RGB_565);
    Canvas canvas = new Canvas(newBitmap);

    //Draw the image bitmap into the canvas
    canvas.drawBitmap(mapBitmap, 0, 0, null);

    //Attach the canvas to the ImageView
    mapImageView.setImageDrawable(new BitmapDrawable(getResources(), newBitmap));

    mapInfo = new MapInfo(this,mapImageView,canvas,newBitmap, gameTracker, mapBitmap);
  }

  private void updateMap(GridPoint gp) {
    mapInfo.updatePath(gp);
  }

  @OnClick({R.id.northButton})
  void northButtonClick() {
    String newString = textView.getText().toString();
    if (newString.length() != 0) {
      newString+=", ";
    }
    textView.setText(newString + "N");
    updateMap(GridPoint.NORTH);
  }
  @OnClick({R.id.eastButton})
  void eastButtonClick() {
    String newString = textView.getText().toString();
    if (newString.length() != 0) {
      newString+=", ";
    }
    textView.setText(newString + "E");
    updateMap(GridPoint.EAST);
  }
  @OnClick({R.id.southButton})
  void southButtonClick() {
    String newString = textView.getText().toString();
    if (newString.length() != 0) {
      newString+=", ";
    }
    textView.setText(newString + "S");
    updateMap(GridPoint.SOUTH);
  }
  @OnClick({R.id.westButton})
  void westButtonClick() {
    String newString = textView.getText().toString();
    if (newString.length() != 0) {
      newString+=", ";
    }
    textView.setText(newString + "W");
    updateMap(GridPoint.WEST);
  }
  @OnClick({R.id.mineButton})
  void mineButtonClick() {
    String newString = textView.getText().toString();
    if (newString.length() != 0) {
      newString+=", ";
    }
    textView.setText(newString + "mine");
  }

  @OnClick({R.id.resetButton})
  void resetButtonClick() {
    textView.setText("");
    mapInfo.restartGame();
  }

  @OnClick({R.id.startButton})
  void startButtonClick() {
    initializeGame();
  }

}
