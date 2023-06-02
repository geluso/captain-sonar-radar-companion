package org.mooncolony.moonmayor.captainsonarradarcompanion.roles;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.mooncolony.moonmayor.captainsonarradarcompanion.GameState;
import org.mooncolony.moonmayor.captainsonarradarcompanion.drawing.MapDrawer;

public class RolePagerAdapter extends FragmentPagerAdapter {
    private GameState mGame;
    private MapDrawer mMap;
    public RoleFragment mCurrentFragment;

    public RolePagerAdapter(FragmentManager fm, GameState game, MapDrawer map) {
        super(fm);
        this.mGame = game;
        this.mMap = map;
    }

    @Override
    public Fragment getItem(int position) {
//        mCurrentFragment = new CaptainRoleFragment();
//        if (position == 1) {
//            mCurrentFragment = new RadarRoleFragment();
//        }
        mCurrentFragment = new RadarRoleFragment();
        mCurrentFragment.setArgs(mGame, mMap);
        return mCurrentFragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

    public void dispatchTouch(int row, int col) {
        mCurrentFragment.processTouch(row, col);
    }
}
