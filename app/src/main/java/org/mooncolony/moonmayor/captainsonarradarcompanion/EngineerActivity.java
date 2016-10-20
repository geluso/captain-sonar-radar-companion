package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import org.mooncolony.moonmayor.captainsonarradarcompanion.drawing.EngineerDrawing;

public class EngineerActivity extends AppCompatActivity {

  EngineerDrawing drawer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_engineer);

    this.drawer = new EngineerDrawing(this, (ImageView) findViewById(R.id.engineerCanvas));

  }
}
