package com.wozainali.manho.myapplication.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;
import com.wozainali.manho.myapplication.R;
import com.wozainali.manho.myapplication.bus.ZaiNaliBus;
import com.wozainali.manho.myapplication.bus.events.ReadKmlFinishedEvent;

public class NavigationDrawer extends Fragment {

    public ActionBarDrawerToggle drawerToggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation,container,false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void setup(DrawerLayout drawerLayout) {

        drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        drawerToggle.syncState();

    }

    @Subscribe
    public void onReadKmlFinishedEvent(ReadKmlFinishedEvent event) {
        Log.i("NavigationDrawer", "event = " + event.getPlacemarks());
    }



    @Override
    public void onResume() {
        super.onResume();
        ZaiNaliBus.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ZaiNaliBus.getBus().unregister(this);
    }
}
