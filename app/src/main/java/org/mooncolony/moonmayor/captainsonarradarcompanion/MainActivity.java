package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.Map;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.MapRealTimeAlpha;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.MapTiny;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTouch;

public class MainActivity extends AppCompatActivity {

  MapInfo mapInfo;

  @BindView(R.id.mapView) ImageView mapView;
  @BindView(R.id.textView) TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    // initialize the map
    ImageView mapImageView = (ImageView) findViewById(R.id.mapView);
    mapInfo = new MapInfo(this, mapImageView, new RadarTracker(new Map(MapRealTimeAlpha.template)));
  }

  @OnTouch({R.id.mapView})
  boolean mapTouch(MotionEvent event) {
    int action = event.getAction();
    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN) {
      float x = event.getX();
      float y = event.getY();
      //TODO: Check that the path lands at a valid circle
      mapInfo.drawPath(x, y);
    }
    return true;
  }

  @OnItemSelected(R.id.mapSpinner)
  public void spinnerItemSelected(Spinner spinner, int position) {
    Toast.makeText(MainActivity.this,"Spinner item "+position, Toast.LENGTH_SHORT).show();
    //TODO: Add in method to change over board
    resetGame(position);
  }

  private void resetGame(int pos) {

  }

  @OnClick({R.id.resetButton})
  void resetButtonClick() {
    textView.setText("");
    mapInfo.restartGame();
    mapInfo.initialize();
  }

  private void updateMap(GridPoint gp) {
    mapInfo.updatePath(gp);
  }

  @OnClick(R.id.newActivityButton)
  void newActivityButtonClick() {
    Intent i = new Intent(MainActivity.this,RecyclerGrid.class);
    startActivity(i);
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
    mapInfo.addMine();
  }

  private void appendText(String message) {
    String newString = textView.getText().toString();
    if (newString.length() != 0) {
      message+=", ";
    }
    textView.setText(message+newString);
  }
}
