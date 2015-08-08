package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import ladyjek.twiscode.com.ladyjek.Adapter.AdapterAddress;
import ladyjek.twiscode.com.ladyjek.Model.ModelGeocode;
import ladyjek.twiscode.com.ladyjek.Model.ModelPlace;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.PlaceAPI;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.widget.AdapterView.OnItemClickListener;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class ActivityTransport extends ActionBarActivity implements
        LocationListener, OnItemClickListener, View.OnKeyListener {
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
    private String placeId = "", description = "", strDistance="", strDuration="";

    private LatLng mapCenter, posFrom, posDest;
    private AdapterAddress mPlaceArrayAdapter;
    private Marker markerFrom, markerDestination;
    private CameraUpdate cameraUpdate;
    private Polyline driveLine;

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

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
            googleMap = fm.getMap();
            //googleMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {

                onLocationChanged(location);
            }
            //locationManager.requestLocationUpdates(provider, 20000, 0, this);

            mapCenter = googleMap.getCameraPosition().target;
            //drawNewMarker(getAddress(mapCenter));


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
                Bundle args = new Bundle();
                args.putString("from", txtFrom.getText().toString());
                args.putString("destination", txtDestination.getText().toString());
                args.putString("distance", strDistance);
                args.putString("duration", strDuration);
                Intent intent = new Intent(getBaseContext(), ActivityConfirm.class);
                intent.putExtras(args);
                startActivity(intent);
            }
        });


        txtFrom.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //do stuff here
                    tagLocation = TAG_FROM;
                    Log.d("ActivityTransport", tagLocation);
                }
                return false;
            }
        });

        txtDestination.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //do stuff here
                    tagLocation = TAG_DESTINATION;
                    Log.d("ActivityTransport", tagLocation);
                }


                return false;
            }
        });

        mPlaceArrayAdapter = new AdapterAddress(this, android.R.layout.simple_list_item_1);
        txtFrom.setAdapter(mPlaceArrayAdapter);
        txtFrom.setOnItemClickListener(this);
        txtFrom.setOnKeyListener(this);
        txtDestination.setAdapter(mPlaceArrayAdapter);
        txtDestination.setOnItemClickListener(this);
        txtDestination.setOnKeyListener(this);
    }



    @Override
    public void onLocationChanged(Location location) {
        googleMap.clear();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng currentPosition = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        CircleOptions circleOptions = new CircleOptions()
                .center(currentPosition)
                .radius(500)
                .strokeWidth(2)
                .strokeColor(Color.BLUE)
                .fillColor(Color.parseColor("#500084d3"));
        //googleMap.addCircle(circleOptions);
        drawNewMarker(getAddress(currentPosition));

        cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentPosition, 15);
        googleMap.animateCamera(cameraUpdate);

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final ModelPlace item = mPlaceArrayAdapter.getItem(position);
        description = String.valueOf(item.description);
        txtLocationFrom.setText(description);
        if(tagLocation.equals(TAG_FROM)) {
            if (markerFrom != null) {
                markerFrom.remove();
            }
        }
        else if(tagLocation.equals(TAG_DESTINATION)) {
            if (markerDestination != null) {
                markerDestination.remove();
            }
        }
        drawNewMarker(description);

    }

    public void drawNewMarker(String address) {
        ModelGeocode geocode = PlaceAPI.geocode(address);
        LatLng locationMarker = new LatLng(geocode.getLat(), geocode.getLon());
        if(driveLine!=null){driveLine.remove();}
        if (tagLocation.equals(TAG_FROM)) {
            posFrom = locationMarker;
            markerFrom = googleMap.addMarker(
                    new MarkerOptions()
                            .position(posFrom)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));

            cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationMarker, 15);
            googleMap.animateCamera(cameraUpdate);
        } else if (tagLocation.equals(TAG_DESTINATION)) {
            posDest = locationMarker;
            markerDestination = googleMap.addMarker(
                    new MarkerOptions()
                            .position(posDest)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));

            Document doc = PlaceAPI.getRoute(posFrom, posDest, "driving");

            ArrayList<LatLng> directionPoint = PlaceAPI.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(5).color(
                    Color.BLUE);

            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }
            strDistance = ""+PlaceAPI.getDistanceText(doc);
            strDuration = ""+PlaceAPI.getDurationText(doc);
            driveLine = googleMap.addPolyline(rectLine);


            cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationMarker, 13);
            googleMap.animateCamera(cameraUpdate);
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            driveLine.remove();
        }

        return false;
    }
}
