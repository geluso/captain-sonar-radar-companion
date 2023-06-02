package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import org.mooncolony.moonmayor.captainsonarradarcompanion.drawing.MapDrawer;
import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.MarineMap;
import org.mooncolony.moonmayor.captainsonarradarcompanion.roles.RolePagerAdapter;

public class MainActivity extends AppCompatActivity {
  private ViewPager mViewPager;
  private RolePagerAdapter mRolePagerAdapter;

  GameState gameState;
  MapDrawer drawer;

  ImageView mapView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // initialize the map
    gameState = new GameState();
    mapView = findViewById(R.id.mapView);
    this.drawer = new MapDrawer(this, mapView, gameState);

    mRolePagerAdapter = new RolePagerAdapter(getSupportFragmentManager(), gameState, this.drawer);
    mViewPager = (ViewPager) findViewById(R.id.container);
    mViewPager.setAdapter(mRolePagerAdapter);

    mapView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN) {
          float x = event.getX();
          float y = event.getY();

          int row = drawer.yToRow(y);
          int col = drawer.xToCol(x);

          mRolePagerAdapter.dispatchTouch(row, col);
        }
        return true;
      }
    });

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
    gameState.newGame();
    mRolePagerAdapter.mCurrentFragment.reset();
    drawer.draw();
  }
}
