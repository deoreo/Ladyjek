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
import ladyjek.twiscode.com.ladyjek.Model.ModelPlace;
import ladyjek.twiscode.com.ladyjek.R;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ActivityTransport extends ActionBarActivity implements
        LocationListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks
{
    private Toolbar mToolbar;
    private GoogleMap googleMap;
    private Button btnLocationFrom, btnLocationDestination, btnRequestRide;
    private AutoCompleteTextView txtFrom, txtDestination;
    private LinearLayout layoutMarkerFrom, layoutMarkerDestination;
    private TextView txtLocationFrom, txtLocationDestinton;
    private ProgressBar progressMapFrom, progressMapDestination;
    private String add, tagLocation = "";
    private final String TAG_FROM = "FROM";
    private final String TAG_DESTINATION = "DESTINATION";
    private LatLng mapCenter;
    private AdapterAddress mPlaceArrayAdapter;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport3);
        SetActionBar();
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
        mGoogleApiClient = new GoogleApiClient.Builder(ActivityTransport.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
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
            txtFrom.setText(getAddress(mapCenter));

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
            }
        });
        btnLocationDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDestination.setText(add);
            }
        });


        txtFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                layoutMarkerFrom.setVisibility(View.VISIBLE);
                layoutMarkerDestination.setVisibility(View.GONE);
                tagLocation = TAG_FROM;
            }
        });

        txtDestination.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                layoutMarkerFrom.setVisibility(View.GONE);
                layoutMarkerDestination.setVisibility(View.VISIBLE);
                tagLocation = TAG_DESTINATION;

            }
        });


        mPlaceArrayAdapter = new AdapterAddress(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        txtFrom.setAdapter(mPlaceArrayAdapter);
        txtDestination.setAdapter(mPlaceArrayAdapter);
    }


    @Override
    public void onLocationChanged(Location location) {
        googleMap.clear();

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


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final ModelPlace item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            txtLocationFrom.setText(Html.fromHtml(place.getAddress() + ""));

        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }


    private class DoChangeLocation extends AsyncTask<String, Void, String> {
        private Activity activity;
        private String tagLocation;

        public DoChangeLocation(String tagLocation) {
            super();
            this.tagLocation = tagLocation;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (tagLocation.equals(TAG_FROM)) {
                progressMapFrom.setVisibility(View.VISIBLE);
                progressMapDestination.setVisibility(View.GONE);
            } else if (tagLocation.equals(TAG_DESTINATION)) {
                progressMapFrom.setVisibility(View.GONE);
                progressMapDestination.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                add = getAddress(mapCenter);

                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            switch (result) {
                case "FAIL":
                    txtLocationFrom.setText("No Location");
                    txtLocationDestinton.setText("No Location");
                    progressMapFrom.setVisibility(View.GONE);
                    progressMapDestination.setVisibility(View.GONE);
                    break;
                case "OK":
                    txtLocationFrom.setText(add);
                    txtLocationDestinton.setText(add);
                    progressMapFrom.setVisibility(View.GONE);
                    progressMapDestination.setVisibility(View.GONE);
                    break;
            }

        }


    }


}
