package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

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
      waterOrLand.add(i != 2);
    }

    BoardAdapter adapty = new BoardAdapter(this, waterOrLand);
    gameBoard.setAdapter(adapty);
    gameBoard.setLayoutManager(new GridLayoutManager(this,15));
  }
}
