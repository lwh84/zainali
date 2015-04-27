package com.wozainali.manho.myapplication;

import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.wozainali.manho.myapplication.fragments.NavigationDrawer;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapFragment mMap;
    NavigationDrawer navigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setupToolBar();
        setupNavigationDrawer();
        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mMap.getMapAsync(this);
        }
    }

    public void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    // TODO using the getsupportfragmentManager, and supportfragmentManger...
    public void setupNavigationDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationDrawer = (NavigationDrawer) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        navigationDrawer.setup(drawerLayout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (navigationDrawer.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private void setUpMap(GoogleMap googleMap) {
//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        googleMap.setMyLocationEnabled(true);
//      // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        double latitude = 0;
        double longitude = 0;

        if(location!=null) {
            // Getting latitude of the current location
            latitude = location.getLatitude();

            // Getting longitude of the current location
            longitude = location.getLongitude();
        }

        String countryName = "";

        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude,longitude,1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                countryName = addresses.get(0).getCountryName();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("mapsActivity", "no addresses = " + e);
        }

        Log.i("country", "countryname = " + countryName);

        IconGenerator iconGenerator = new IconGenerator(this);
        Bitmap iconBitmap = iconGenerator.makeIcon(countryName);
        LatLng myPosition = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(myPosition).icon(BitmapDescriptorFactory.fromBitmap(iconBitmap)));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Check if we were successful in obtaining the map.
        setUpMap(googleMap);
    }

}
