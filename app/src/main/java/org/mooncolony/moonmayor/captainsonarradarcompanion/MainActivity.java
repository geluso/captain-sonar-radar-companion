package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
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

  @BindView(R.id.mapSpinner) Spinner spinner;
  @BindView(R.id.mapView) ImageView mapView;
  @BindView(R.id.textView) TextView textView;

  private String currentMapTemplate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    String[] options = Map.AVAILABLE_MAPS;
    ArrayAdapter<String> mapChoices = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, options);
    spinner.setAdapter(mapChoices);

    // initialize the map
    this.currentMapTemplate = MapRealTimeAlpha.template;
    mapInfo = new MapInfo(this, mapView, new RadarTracker(new Map(this.currentMapTemplate)));
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
    String mapName = Map.AVAILABLE_MAPS[position];
    String template = null;

    if (mapName.equals(MapRealTimeAlpha.name)) {
      template = MapRealTimeAlpha.template;
    } else if (mapName.equals(MapTiny.name)) {
      template = MapTiny.template;
    }

    if (template == null) {
      Toast.makeText(MainActivity.this, "Unknown map.", Toast.LENGTH_SHORT).show();
      return;
    }

    this.currentMapTemplate = template;
    Map newMap = new Map(template);

    String size = newMap.name + " rows:" + newMap.rows + " cols:" + newMap.cols;
    Toast.makeText(MainActivity.this, size, Toast.LENGTH_SHORT).show();

    mapInfo = new MapInfo(this, mapView, new RadarTracker(newMap));
    resetButtonClick();
  }

  @OnClick({R.id.resetButton})
  void resetButtonClick() {
    textView.setText("");
    mapInfo.restartGame(this.currentMapTemplate);
    mapInfo.initialize();
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
