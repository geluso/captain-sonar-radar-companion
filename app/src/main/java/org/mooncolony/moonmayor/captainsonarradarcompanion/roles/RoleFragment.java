package org.mooncolony.moonmayor.captainsonarradarcompanion.roles;

import android.support.v4.app.Fragment;

import org.mooncolony.moonmayor.captainsonarradarcompanion.GameState;
import org.mooncolony.moonmayor.captainsonarradarcompanion.drawing.MapDrawer;

public abstract class RoleFragment extends Fragment {
    private static GameState staticGame;
    private static MapDrawer staticDrawer;

    protected GameState gameState;
    protected MapDrawer drawer;


    public RoleFragment() {
        super();

        if (staticGame != null) {
            this.gameState = staticGame;
        }
        if (staticDrawer != null) {
            this.drawer = staticDrawer;
        }
    }

    public void setArgs(GameState game, MapDrawer drawer) {
        this.gameState = game;
        this.drawer = drawer;

        staticGame = game;
        staticDrawer = drawer;
    }

    public void processTouch(int row, int col) {
        dealWithNormalTouch(row, col);
    }

    private void dealWithNormalTouch(int row, int col) {
        if (row != gameState.pathEndRow || col != gameState.pathEndCol) {
            //TODO: Check that the path lands at a valid circle
            gameState.setPathEnd(row, col);
            drawer.draw();
        }
    }

    public void reset() {
        // do nothing
    }
}
