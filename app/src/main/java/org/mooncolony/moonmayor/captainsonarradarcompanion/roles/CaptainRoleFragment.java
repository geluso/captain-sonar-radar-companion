package org.mooncolony.moonmayor.captainsonarradarcompanion.roles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.mooncolony.moonmayor.captainsonarradarcompanion.R;
import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPoint;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaptainRoleFragment extends RoleFragment {
    @BindView(R.id.goNorth) Button goNorth;
    @BindView(R.id.goSouth) Button goSouth;
    @BindView(R.id.goEast) Button goEast;
    @BindView(R.id.goWest) Button goWest;

    @BindView(R.id.surface) Button surface;
    @BindView(R.id.fireTorpedo) Button fireTorpedo;
    @BindView(R.id.layMine) Button layMine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.role_captain, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.goNorth)
    public void goNorth() {
        gameState.myCurrentRow--;
        gameState.myCurrentPath.add(GridPoint.NORTH);
        drawer.draw();
    }

    @OnClick(R.id.goSouth)
    public void goSouth() {
        gameState.myCurrentRow++;
        gameState.myCurrentPath.add(GridPoint.SOUTH);
        drawer.draw();
    }

    @OnClick(R.id.goEast)
    public void goEast() {
        gameState.myCurrentCol++;
        gameState.myCurrentPath.add(GridPoint.EAST);
        drawer.draw();
    }

    @OnClick(R.id.goWest)
    public void goWest() {
        gameState.myCurrentCol--;
        gameState.myCurrentPath.add(GridPoint.WEST);
        drawer.draw();
    }
}
