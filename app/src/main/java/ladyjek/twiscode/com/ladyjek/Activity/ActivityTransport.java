package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ladyjek.twiscode.com.ladyjek.Adapter.AdapterAddress;
import ladyjek.twiscode.com.ladyjek.Model.ModelGeocode;
import ladyjek.twiscode.com.ladyjek.Model.ModelPlace;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.PlaceAPI;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.widget.AdapterView.OnItemClickListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ActivityTransport extends ActionBarActivity implements
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnItemClickListener {
    private ActivityTransport activityTransport;
    private Toolbar mToolbar;
    private GoogleMap googleMap;
    private Button btnLocationFrom, btnLocationDestination, btnRequestRide;
    private final String TAG_FROM = "FROM";
    private final String TAG_DESTINATION = "DESTINATION";
    private AutoCompleteTextView txtFrom, txtDestination;
    private LinearLayout layoutMarkerFrom, layoutMarkerDestination;
    private TextView txtLocationFrom, txtLocationDestinton;
    private ProgressBar progressMapFrom, progressMapDestination;
    private String add, tagLocation = TAG_FROM;
    private String placeId = "", description = "";

    private LatLng mapCenter;
    private AdapterAddress mPlaceArrayAdapter;
    private GoogleApiClient mGoogleApiClient;
    private Marker markerFrom, markerDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport3);
        SetActionBar();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        btnRequestRide = (Button) findViewById(R.id.btnRequestRide);
        btnLocationFrom = (Button) findViewById(R.id.btnLocationFrom);
        btnLocationDestination = (Button) findViewById(R.id.btnLocationDestination);
        txtFrom = (AutoCompleteTextView) findViewById(R.id.txtFrom);
        txtDestination = (AutoCompleteTextView) findViewById(R.id.txtDestination);
        layoutMarkerFrom = (LinearLayout) findViewById(R.id.layoutMarkerFrom);
        layoutMarkerDestination = (LinearLayout) findViewById(R.id.layoutMarkerDestination);
        progressMapFrom = (ProgressBar) findViewById(R.id.progressMapFrom);
        progressMapDestination = (ProgressBar) findViewById(R.id.progressMapDestination);
        txtLocationDestinton = (TextView) findViewById(R.id.txtLocationDestination);
        txtLocationFrom = (TextView) findViewById(R.id.txtLocationFrom);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
            googleMap = fm.getMap();
            googleMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {

                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, this);

            mapCenter = googleMap.getCameraPosition().target;
            //addMarker(getAddress(mapCenter));


            googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    mapCenter = googleMap.getCameraPosition().target;
                    add = getAddress(mapCenter);
                    txtLocationFrom.setText(add);
                    txtLocationDestinton.setText(add);
                    progressMapFrom.setVisibility(View.GONE);
                    progressMapDestination.setVisibility(View.GONE);
                }
            });


        }

        btnRequestRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ActivityConfirm.class);
                startActivity(i);
            }
        });

        btnLocationFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtFrom.setText(add);
                googleMap.addMarker(new MarkerOptions()
                        .position(mapCenter)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker)));
            }
        });
        btnLocationDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDestination.setText(add);
                googleMap.addMarker(new MarkerOptions()
                        .position(mapCenter)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker)));
            }
        });


        txtFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // layoutMarkerFrom.setVisibility(View.VISIBLE);
                //layoutMarkerDestination.setVisibility(View.GONE);
                tagLocation = TAG_FROM;
                if(markerFrom!=null) {
                    markerFrom.remove();
                }
            }
        });

        txtDestination.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // layoutMarkerFrom.setVisibility(View.GONE);
                //layoutMarkerDestination.setVisibility(View.VISIBLE);
                tagLocation = TAG_DESTINATION;
                if(markerDestination!=null) {
                    markerDestination.remove();
                }

            }
        });


        mPlaceArrayAdapter = new AdapterAddress(this, android.R.layout.simple_list_item_1);
        txtFrom.setAdapter(mPlaceArrayAdapter);
        txtFrom.setOnItemClickListener(this);

        txtDestination.setAdapter(mPlaceArrayAdapter);
        txtDestination.setOnItemClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        //googleMap.clear();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(500)
                .strokeWidth(2)
                .strokeColor(Color.BLUE)
                .fillColor(Color.parseColor("#500084d3"));
        googleMap.addCircle(circleOptions);

    }


    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }


    private void SetActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final ModelPlace item = mPlaceArrayAdapter.getItem(position);
        description = String.valueOf(item.description);
        txtLocationFrom.setText(description);
        addMarker(description);

    }

    public void addMarker(String address) {
        ModelGeocode geocode = PlaceAPI.geocode(address);
        LatLng locationMarker = new LatLng(geocode.getLat(), geocode.getLon());
        if (tagLocation.equals(TAG_FROM)) {
            markerFrom = googleMap.addMarker(
                    new MarkerOptions()
                            .position(locationMarker)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
        } else if (tagLocation.equals(TAG_DESTINATION)) {
            markerDestination = googleMap.addMarker(
                    new MarkerOptions()
                            .position(locationMarker)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));
        }

    }

    public String getAddress(LatLng latlng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        double lat = latlng.latitude;
        double lng = latlng.longitude;
        String addressLine = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            addressLine = obj.getAddressLine(0);

        } catch (IOException e) {
        } catch (Exception e) {
            Log.w("ActivityTransport", "Canont get Address!");
        }
        return addressLine;
    }

}
