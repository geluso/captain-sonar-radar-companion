package org.mooncolony.moonmayor.captainsonarradarcompanion.roles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mooncolony.moonmayor.captainsonarradarcompanion.R;

import butterknife.ButterKnife;

public class CaptainRoleFragment extends RoleFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.role_captain, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
