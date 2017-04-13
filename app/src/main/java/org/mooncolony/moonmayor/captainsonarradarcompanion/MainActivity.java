package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.mooncolony.moonmayor.captainsonarradarcompanion.custom_views.SquareImageView;
import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPoint;
import org.mooncolony.moonmayor.captainsonarradarcompanion.drawing.MapDrawer;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.MarineMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTouch;

public class MainActivity extends AppCompatActivity {

  GameState gameState;
  MapDrawer drawer;

//  @BindView(R.id.mapSpinner) Spinner spinner;
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

    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    int height = displayMetrics.heightPixels;
    mapView.getLayoutParams().width = height/2;

//    String[] options = MarineMap.AVAILABLE_MAPS;
//    ArrayAdapter<String> mapChoices = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, options);
//    spinner.setAdapter(mapChoices);

    // initialize the map
    gameState = new GameState();
    this.drawer = new MapDrawer(this, mapView, gameState);

  }

  //TODO: update to new layout
  @OnTouch({R.id.mapView})
  boolean mapTouch(MotionEvent event) {
    int action = event.getAction();
    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN) {
      float x = event.getX();
      float y = event.getY();

      int row = drawer.yToRow(y);
      int col = drawer.xToCol(x);

      if (gameState.placingTorpedo) {
        gameState.placingTorpedoRow = row;
        gameState.placingTorpedoCol = col;
        drawer.draw();
      } else {
        //TODO: Check that the path lands at a valid circle
        // gameState.setPathStart(row, col);
        gameState.setPathEnd(row, col);
        drawer.draw();
      }
    }
    return true;
  }

  boolean mapRelease(MotionEvent event) {
    int action = event.getAction();
    if (action == MotionEvent.ACTION_UP) {

    }
    return true;
  }

  private void updateMap(GridPoint gp) {
    gameState.updatePath(gp);
    drawer.draw();
  }
//**** start compass button clicks
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
//**** end compass button clicks

  @OnClick({R.id.mineButton})
  void mineButtonClick() {
    appendText("mine");
    gameState.addMine();
    drawer.draw();
  }

  @OnClick({R.id.silenceButton})
  void silence() {
    gameState.addSilence();
    drawer.draw();
  }

  @OnClick({R.id.torpedoButton})
  void torpedoButtonClick() {
    showTorpedoMenu();

    // target the center of the board at first and allow user to move it around.
    gameState.placingTorpedo = true;
    gameState.placingTorpedoRow = gameState.radar.map.rows / 2;
    gameState.placingTorpedoCol = gameState.radar.map.cols / 2;

    drawer.draw();
  }

  @OnClick({R.id.confirmTorpedo})
  void confirmTorpedo() {
    appendText("torpedo");
    showCompass();

    gameState.addTorpedo();
    gameState.placingTorpedo = false;
    drawer.draw();
  }

  @OnClick({R.id.cancelTorpedo})
  void cancelTorpedo() {
    showCompass();

    gameState.placingTorpedo = false;
    drawer.draw();
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);

    gameState.newGame(MarineMap.MAP_TEMPLATES[0]);
    drawer.setDimensions();
    resetGame();

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int position = -1;
    switch (item.getItemId()) {

      case R.id.reset_button:
        resetGame();
        break;

      case R.id.engineer_button:
        startActivity(new Intent(MainActivity.this,EngineerActivity.class));
        break;

      case R.id.alpha_real_time: position = 0;
        break;
      case R.id.alpha_turn_by_turn: position = 1;
        break;
      case R.id.bravo_real_time: position = 2;
        break;
      case R.id.bravo_turn_by_turn: position = 3;
        break;
      case R.id.charlie_real_time: position = 4;
        break;
      case R.id.charlie_turn_by_turn: position = 5;
        break;
      case R.id.delta_real_time: position = 6;
        break;
      case R.id.delta_turn_by_turn: position = 7;
        break;
      case R.id.echo_real_time: position = 8;
        break;
      case R.id.echo_turn_by_turn: position = 9;
        break;

      default:
        return false;
    }

    if (position >= 0) {
      changeMap(position);
    }


    return super.onOptionsItemSelected(item);
  }

  private void changeMap(int position) {
    String template = null;
    if (position >= 0) {
      template = MarineMap.MAP_TEMPLATES[position];
    }

    if (template == null) {
      Toast.makeText(MainActivity.this, "Unknown map.", Toast.LENGTH_SHORT).show();
      return;
    }

    String msg = "Loaded " + MarineMap.AVAILABLE_MAPS[position];
    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

    gameState.newGame(template);
    drawer.setDimensions();
    resetGame();
  }

  private void resetGame() {
    textView.setText("");
    gameState.newGame();
    showCompass();
    drawer.draw();
  }

}
