package org.mooncolony.moonmayor.captainsonarradarcompanion.roles;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.mooncolony.moonmayor.captainsonarradarcompanion.GameState;
import org.mooncolony.moonmayor.captainsonarradarcompanion.drawing.MapDrawer;

public class RolePagerAdapter extends FragmentPagerAdapter {
    private GameState mGame;
    private MapDrawer mMap;
    public RadarRoleFragment mCurrentFragment;

    public RolePagerAdapter(FragmentManager fm, GameState game, MapDrawer map) {
        super(fm);
        this.mGame = game;
        this.mMap = map;
    }

    @Override
    public Fragment getItem(int position) {
        mCurrentFragment = new RadarRoleFragment();
        mCurrentFragment.setArgs(mGame, mMap);
        return mCurrentFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void dispatchTouch(int row, int col) {
        mCurrentFragment.processTouch(row, col);
    }
}
