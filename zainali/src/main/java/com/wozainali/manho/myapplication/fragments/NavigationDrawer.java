package com.wozainali.manho.myapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.wozainali.manho.myapplication.R;
import com.wozainali.manho.myapplication.adapters.CountryAdapter;
import com.wozainali.manho.myapplication.bus.ZaiNaliBus;
import com.wozainali.manho.myapplication.bus.events.ReadKmlFinishedEvent;
import com.wozainali.manho.myapplication.bus.events.TotalCountriesEvent;
import com.wozainali.manho.myapplication.data.PlacemarksManager;
import com.wozainali.manho.myapplication.kml.Placemark;

import java.util.ArrayList;

public class NavigationDrawer extends Fragment {

    public ActionBarDrawerToggle drawerToggle;
    CountryAdapter countryAdapter;
    RecyclerView recyclerView;
    TextView loadingView;
    DrawerLayout drawerLayout;
    LinearLayout navigation;
    boolean navigationLoaded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        navigation = (LinearLayout) inflater.inflate(R.layout.fragment_navigation, container, false);

        recyclerView = (RecyclerView) navigation.findViewById(R.id.country_recylcer);
        loadingView = (TextView) navigation.findViewById(R.id.loading);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        countryAdapter = new CountryAdapter();

        recyclerView.setAdapter(countryAdapter);

        return navigation;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void setup(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;

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
        setCountryAdapter(event.placemarksWrapper.getPlacemarks());
    }

    @Override
    public void onResume() {
        super.onResume();
        ZaiNaliBus.getBus().register(this);
        if (navigationLoaded == false) {
            if (PlacemarksManager.getInstance().getPlacemarks() != null)
                setCountryAdapter(PlacemarksManager.getInstance().getPlacemarks());
        }
    }

    private void setCountryAdapter(ArrayList<Placemark> placemarks) {
        countryAdapter.setPlacemarks(placemarks);
        recyclerView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
        navigationLoaded = true;
        ZaiNaliBus.getBus().post(new TotalCountriesEvent(placemarks.size()));
    }


    @Override
    public void onPause() {
        super.onPause();
        ZaiNaliBus.getBus().unregister(this);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(navigation);
    }
}
