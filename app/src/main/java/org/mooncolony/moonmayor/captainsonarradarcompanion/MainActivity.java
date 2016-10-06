package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.mooncolony.moonmayor.captainsonarradarcompanion.drawing.MapDrawer;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTouch;

public class MainActivity extends AppCompatActivity {

  GameState gameState;
  MapDrawer drawer;

  @BindView(R.id.mapSpinner) Spinner spinner;
  @BindView(R.id.mapView) ImageView mapView;
  @BindView(R.id.textView) TextView textView;

  @BindView(R.id.torpedoButton) View torpedoButton;

  @BindView(R.id.compass) View compass;
  @BindView(R.id.torpedoMenu) View torpedoMenu;
  @BindView(R.id.confirmTorpedo) Button confirmTorpedo;
  @BindView(R.id.cancelTorpedo) Button cancelTorpedo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    String[] options = Map.AVAILABLE_MAPS;
    ArrayAdapter<String> mapChoices = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, options);
    spinner.setAdapter(mapChoices);

    // initialize the map
    gameState = new GameState();
    this.drawer = new MapDrawer(this, mapView, gameState);
  }

  @OnTouch({R.id.mapView})
  boolean mapTouch(MotionEvent event) {
    int action = event.getAction();
    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN) {
      float x = event.getX();
      float y = event.getY();

      if (gameState.placingTorpedo) {
        int row = drawer.yToRow(y);
        int col = drawer.xToCol(x);
        drawer.drawTorpedoTarget(row, col);
      } else {
        //TODO: Check that the path lands at a valid circle
        gameState.showingPath = true;
        drawer.drawPath(gameState.currentPath, x, y);
      }
    }
    return true;
  }

  @OnItemSelected(R.id.mapSpinner)
  public void spinnerItemSelected(Spinner spinner, int position) {
    String template = Map.MAP_TEMPLATES[position];

    if (template == null) {
      Toast.makeText(MainActivity.this, "Unknown map.", Toast.LENGTH_SHORT).show();
      return;
    }

    String msg = "Loaded " + Map.AVAILABLE_MAPS[position];
    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

    gameState.newGame(template);
    resetButtonClick();
  }

  @OnClick({R.id.resetButton})
  void resetButtonClick() {
    textView.setText("");
    gameState.newGame();

    showCompass();

    drawer.drawAll();
  }

  private void updateMap(GridPoint gp) {
    gameState.updatePath(gp);

//    drawer.drawPossibleStartingLocations();
    drawer.drawInvalidArea();
    drawer.drawAll();
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
    gameState.addMine();
    drawer.drawAll();
  }
  @OnClick({R.id.torpedoButton})
  void torpedoButtonClick() {
    showTorpedoMenu();

    // target the center of the board at first and allow user to move it around.
    int row = gameState.radar.map.rows / 2;
    int col = gameState.radar.map.cols / 2;
    drawer.drawTorpedoTarget(row, col);

    gameState.placingTorpedo = true;
  }

  @OnClick({R.id.confirmTorpedo})
  void confirmTorpedo() {
    appendText("torpedo");
    showCompass();

    gameState.placingTorpedo = false;
  }

  @OnClick({R.id.cancelTorpedo})
  void cancelTorpedo() {
    showCompass();

    gameState.placingTorpedo = false;
  }

  void showTorpedoMenu() {
    compass.setVisibility(View.GONE);
    torpedoMenu.setVisibility(View.VISIBLE);
  }

  void showCompass() {
    compass.setVisibility(View.VISIBLE);
    torpedoMenu.setVisibility(View.GONE);
  }

  private void appendText(String message) {
    String newString = textView.getText().toString();
    if (newString.length() != 0) {
      message+=", ";
    }
    textView.setText(message+newString);
  }
}
