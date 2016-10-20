package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import org.mooncolony.moonmayor.captainsonarradarcompanion.drawing.EngineerDrawing;

import butterknife.ButterKnife;
import butterknife.OnTouch;

public class EngineerActivity extends AppCompatActivity {

  EngineerDrawing drawer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_engineer);
    ButterKnife.bind(this);

    this.drawer = new EngineerDrawing(this, (ImageView) findViewById(R.id.engineerCanvas));

  }

  @OnTouch({R.id.engineerCanvas})
  boolean mapTouch(MotionEvent event) {
    int action = event.getAction();
    if (action == MotionEvent.ACTION_UP) {
      float x = event.getX();
      float y = event.getY();

      drawer.addCross(x,y);
    }
    return true;
  }
}
