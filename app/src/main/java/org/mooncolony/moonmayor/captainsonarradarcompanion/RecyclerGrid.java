package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.mooncolony.moonmayor.captainsonarradarcompanion.maps.AlphaRealTime;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerGrid extends AppCompatActivity {

  @BindView(R.id.gameBoard) RecyclerView gameBoard;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recycler_grid);
    ButterKnife.bind(this);

    ArrayList<Boolean> waterOrLand = new ArrayList<>();
    for (int i=0;i<225;i++) {
      waterOrLand.add(true);
    }

    String[] rows = AlphaRealTime.template.split("\n");
    int count = 0;
    for (String s : rows) {
      for (int i = 0; i < s.length(); i++) {
        if (s.charAt(i) == 'X') {
          waterOrLand.set(count,false);
        }
        count++;
      }
    }


    BoardAdapter adapty = new BoardAdapter(this, waterOrLand);
    gameBoard.setAdapter(adapty);
    gameBoard.setLayoutManager(new GridLayoutManager(this,15));
  }
}
