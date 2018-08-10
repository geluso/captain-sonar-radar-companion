package org.mooncolony.moonmayor.captainsonarradarcompanion.roles;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RolePagerAdapter extends FragmentPagerAdapter {
    public RolePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new RadarRoleFragment();
        } else if (position == 2) {
            return new RadarRoleFragment();
        }
        return new RadarRoleFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
