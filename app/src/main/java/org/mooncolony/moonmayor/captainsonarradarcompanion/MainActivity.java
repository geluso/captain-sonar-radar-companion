package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.graphics.Bitmap;
import android.graphics.Color;
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

    // initialize the map
    ImageView mapImageView = (ImageView) findViewById(R.id.mapView);
    mapInfo = new MapInfo(this, mapImageView, gameTracker);
  }

  @OnClick({R.id.startButton})
  void startButtonClick() {
    initializeGame();
  }

  @OnClick({R.id.resetButton})
  void resetButtonClick() {
    textView.setText("");
    mapInfo.restartGame();
  }

  private void initializeGame() {
    mapInfo.initialize();
    for (GridPoint gp : gameTracker.getStartingPoints()) {
      mapInfo.addCircle(gp, Color.GREEN);
    }
  }

  private void updateMap(GridPoint gp) {
    mapInfo.updatePath(gp);
  }

  @OnClick({R.id.northButton})
  void northButtonClick() {
    appendText("N");
    updateMap(GridPoint.NORTH);
  }
  @OnClick({R.id.eastButton})
  void eastButtonClick() {
    appendText("E");
    updateMap(GridPoint.EAST);
  }
  @OnClick({R.id.southButton})
  void southButtonClick() {
    appendText("S");
    updateMap(GridPoint.SOUTH);
  }
  @OnClick({R.id.westButton})
  void westButtonClick() {
    appendText("W");
    updateMap(GridPoint.WEST);
  }
  @OnClick({R.id.mineButton})
  void mineButtonClick() {
    appendText("mine");
  }

  private void appendText(String message) {
    String newString = textView.getText().toString();
    if (newString.length() != 0) {
      newString+=", ";
    }
    textView.setText(newString + message);
  }
}
