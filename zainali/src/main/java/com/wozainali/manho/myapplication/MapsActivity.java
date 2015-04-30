package com.wozainali.manho.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.wozainali.manho.myapplication.bus.events.ShowCurrentNumberEvent;
import com.wozainali.manho.myapplication.bus.events.TotalCountriesEvent;
import com.wozainali.manho.myapplication.bus.events.ZoomToPointEvent;
import com.wozainali.manho.myapplication.data.PlacemarksManager;
import com.wozainali.manho.myapplication.fragments.NavigationDrawer;
import com.wozainali.manho.myapplication.kml.PlaceMarkPolygon;
import com.wozainali.manho.myapplication.kml.Placemark;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapFragment mMap;
    NavigationDrawer navigationDrawer;
    DrawerLayout drawerLayout;
    GoogleMap googleMap;
    Marker currentMarker;
    ArrayList<Polyline> currentPolylines = new ArrayList<>();

    TextView currentNumber, totalCountries;
    LinearLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setupToolBar();
        setupNavigationDrawer();

        // set other views
        currentNumber = (TextView) findViewById(R.id.current_number);
        totalCountries = (TextView) findViewById(R.id.total_countries);
        loadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
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
        if (navigationDrawer.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpMap(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        showCurrentLocation(null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setUpMap(googleMap);
        this.googleMap = googleMap;
    }

    public void searchForCountry(View view) {
        TextView country = (TextView) view;

        ArrayList<Placemark> placemarksFromManager = PlacemarksManager.getInstance().getPlacemarks();
        for (Placemark placemark : placemarksFromManager) {
            if (placemark.getName().equals(country.getText())) {
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

            loadingLayout.setVisibility(View.VISIBLE);
            ShowCountryNameAndBorder showCountryNameAndBorder = new ShowCountryNameAndBorder(longitude,latitude, getBaseContext());
            showCountryNameAndBorder.execute();
        }

        navigationDrawer.closeDrawer();
    }

    // SUBSCRIBE METHODS
    @Subscribe
    public void onZoomtoPointEvent(ZoomToPointEvent event) {

        LatLng southWest = new LatLng(event.getMinLat(), event.getMinLong());
        LatLng northEast = new LatLng(event.getMaxLat(), event.getMaxLong());

        LatLngBounds pointOfInterest = new LatLngBounds(southWest,northEast);

        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(pointOfInterest, Math.round(padding)));

    }

    @Subscribe
    public void onAddMarkerEvent(AddMarkerEvent event) {

        IconGenerator iconGenerator = new IconGenerator(this);
        Bitmap iconBitmap = iconGenerator.makeIcon(event.name);
        LatLng myPosition = new LatLng(event.latitude, event.longitude);
        if (currentMarker != null) currentMarker.remove();
        currentMarker = googleMap.addMarker(new MarkerOptions().position(myPosition).icon(BitmapDescriptorFactory.fromBitmap(iconBitmap)));
    }

    @Subscribe
    public void onDrawPolygonsEvent(DrawPolygonsEvent event) {
        loadingLayout.setVisibility(View.GONE);
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
                    .width(8)
                    .color(Color.BLUE));

            currentPolylines.add(currentPolyline);

        }
    }

    @Subscribe
    public void onCurrentNumber(final ShowCurrentNumberEvent event) {
        // not so nice part
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentNumber.setText("" + event.number);
            }
        });
    }

    @Subscribe
    public void onTotalCountries(TotalCountriesEvent event) {
        totalCountries.setText(event.total + "");
    }

}
