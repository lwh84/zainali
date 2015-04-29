package com.wozainali.manho.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.otto.Subscribe;
import com.wozainali.manho.myapplication.asynctasks.ReadKmlTask;
import com.wozainali.manho.myapplication.asynctasks.ShowCountryNameAndBorder;
import com.wozainali.manho.myapplication.bus.ZaiNaliBus;
import com.wozainali.manho.myapplication.bus.events.AddMarkerEvent;
import com.wozainali.manho.myapplication.bus.events.DrawPolygonsEvent;
import com.wozainali.manho.myapplication.bus.events.ZoomToPointEvent;
import com.wozainali.manho.myapplication.data.PlacemarksManager;
import com.wozainali.manho.myapplication.fragments.NavigationDrawer;
import com.wozainali.manho.myapplication.kml.PlaceMarkPolygon;
import com.wozainali.manho.myapplication.kml.Placemark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapFragment mMap;
    NavigationDrawer navigationDrawer;
    DrawerLayout drawerLayout;
    GoogleMap googleMap;
    Marker currentMarker;
    ArrayList<Polyline> currentPolylines = new ArrayList<>();

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
        ZaiNaliBus.getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ZaiNaliBus.getBus().unregister(this);
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
        final int worldData = R.raw.world;
        ReadKmlTask readKmlTask = new ReadKmlTask(worldData, getResources());
        readKmlTask.execute();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        showCurrentLocation(null);







//        String countryName = "";
//
//        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
//        List<Address> addresses;
//
//        try {
//            addresses = geocoder.getFromLocation(latitude,longitude,1);
//            if (addresses.size() > 0) {
//                System.out.println(addresses.get(0).getLocality());
//                countryName = addresses.get(0).getCountryName();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.i("mapsActivity", "no addresses = " + e);
//        }
//
//        Log.i("country", "countryname = " + countryName);
//
//        IconGenerator iconGenerator = new IconGenerator(this);
//        Bitmap iconBitmap = iconGenerator.makeIcon(countryName);
//        LatLng myPosition = new LatLng(latitude, longitude);
//        googleMap.addMarker(new MarkerOptions().position(myPosition).icon(BitmapDescriptorFactory.fromBitmap(iconBitmap)));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Check if we were successful in obtaining the map.
        setUpMap(googleMap);
        this.googleMap = googleMap;
    }

    public void searchForCountry(View view) {
        // get name of country
        // then search for that one in the list.
        Log.i("MapsActivity", "view =" + view);

        TextView country = (TextView) view;

        ArrayList<Placemark> placemarksFromManager = PlacemarksManager.getInstance().getPlacemarks();
        Log.i("country", "countryname = " + country.getText());
        for (Placemark placemark : placemarksFromManager) {
            if (placemark.getName().equals(country.getText())) {
                Log.i("placemark", "this placemark = " + placemark);
                ShowCountryNameAndBorder showCountryNameAndBorder = new ShowCountryNameAndBorder(placemark);
                showCountryNameAndBorder.execute();
            }
        }

        navigationDrawer.closeDrawer();
    }

    public void showCurrentLocation(View view){
        // Getting LocationManager object from System Service LOCATION_SERVICE
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

            ShowCountryNameAndBorder showCountryNameAndBorder = new ShowCountryNameAndBorder(longitude,latitude);
            showCountryNameAndBorder.execute();
        } else {
            // snackbar to let user know, that location can not found and check settings...
        }
    }

    // SUBSCRIBE METHODS
    @Subscribe
    public void onZoomtoPointEvent(ZoomToPointEvent event) {
        Log.i("mapsactivity", "zoomtopoint = " + event);
        LatLng southWest = new LatLng(event.getMinLat(), event.getMinLong());
        LatLng northEast = new LatLng(event.getMaxLat(), event.getMaxLong());

        LatLngBounds pointOfInterest = new LatLngBounds(southWest,northEast);

        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(pointOfInterest,Math.round(padding)));

    }

    @Subscribe
    public void onAddMarkerEvent(AddMarkerEvent event) {
        Log.i("mapsActivity", "addmarker " + event);

        IconGenerator iconGenerator = new IconGenerator(this);
        Bitmap iconBitmap = iconGenerator.makeIcon(event.name);
        LatLng myPosition = new LatLng(event.latitude, event.longitude);
        if (currentMarker != null) currentMarker.remove();
        currentMarker = googleMap.addMarker(new MarkerOptions().position(myPosition).icon(BitmapDescriptorFactory.fromBitmap(iconBitmap)));
    }

    @Subscribe
    public  void onDrawPolygonsEvent(DrawPolygonsEvent event) {
        Log.i("mapsActivity", "drawpolygons " + event);

        for (Polyline polyline : currentPolylines) {
            polyline.remove();
        }

        currentPolylines = new ArrayList<>();

        for (PlaceMarkPolygon placemarkPolygon : event.polygons) {
            ArrayList<Double> latitudes = placemarkPolygon.getLatitudes();
            ArrayList<Double> longitudes = placemarkPolygon.getLongitudes();
            ArrayList<LatLng> points = new ArrayList<>();

            for (int i = 0 ; i < latitudes.size(); i++) {
                points.add(new LatLng(latitudes.get(i), longitudes.get(i)));
            }



            Polyline currentPolyline = googleMap.addPolyline(new PolylineOptions()
                    .addAll(points)
                    .width(5)
                    .color(Color.RED));

            currentPolylines.add(currentPolyline);


        }
    }





}
