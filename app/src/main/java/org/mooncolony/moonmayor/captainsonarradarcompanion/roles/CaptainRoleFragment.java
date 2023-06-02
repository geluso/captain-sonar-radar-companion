package org.mooncolony.moonmayor.captainsonarradarcompanion.roles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import org.mooncolony.moonmayor.captainsonarradarcompanion.R;
import org.mooncolony.moonmayor.captainsonarradarcompanion.geometry.GridPoint;

public class CaptainRoleFragment extends RoleFragment {
    Button goNorth;
    Button goSouth;
    Button goEast;
    Button goWest;

    //@BindView(R.id.surface) Button surface;
    //@BindView(R.id.fireTorpedo) Button fireTorpedo;
    //@BindView(R.id.layMine) Button layMine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.role_captain, container, false);

        rootView.findViewById(R.id.goNorth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNorth();
            }
        });

        rootView.findViewById(R.id.goEast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goEast();
            }
        });

        rootView.findViewById(R.id.goSouth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSouth();
            }
        });

        rootView.findViewById(R.id.goWest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goWest();
            }
        });

        return rootView;
    }

    public void goNorth() {
        gameState.myCurrentRow--;
        gameState.myCurrentPath.add(GridPoint.NORTH);
        drawer.draw();
    }

    public void goSouth() {
        gameState.myCurrentRow++;
        gameState.myCurrentPath.add(GridPoint.SOUTH);
        drawer.draw();
    }

    public void goEast() {
        gameState.myCurrentCol++;
        gameState.myCurrentPath.add(GridPoint.EAST);
        drawer.draw();
    }

    public void goWest() {
        gameState.myCurrentCol--;
        gameState.myCurrentPath.add(GridPoint.WEST);
        drawer.draw();
    }
}
