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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.BubbleIconFactory;
import com.google.maps.android.ui.IconGenerator;
import com.wozainali.manho.myapplication.fragments.NavigationDrawer;
import com.wozainali.manho.myapplication.kml.MyKmlReader;
import com.wozainali.manho.myapplication.kml.Placemarks;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapFragment mMap; // Might be null if Google Play services APK is not available.
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

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
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


    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
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
