package ladyjek.twiscode.com.ladyjek.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ladyjek.twiscode.com.ladyjek.Activity.ActivityConfirm;
import ladyjek.twiscode.com.ladyjek.Adapter.AdapterAddress;
import ladyjek.twiscode.com.ladyjek.Adapter.AdapterSuggestion;
import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ModelGeocode;
import ladyjek.twiscode.com.ladyjek.Model.ModelPlace;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.GoogleAPIManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.widget.AdapterView.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
/**
 * Created by Unity on 18/05/2015.
 */
public class FragmentHome extends Fragment
         {

    private Toolbar mToolbar;
    ModelPlace mPlace, selectedPlaceFrom, selectedPlaceDestination;
    private List<ModelPlace> LIST_PLACE = null;
    private static final String KEY_ID = "place_id";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DETAIL = "detail";
    private static final String KEY_DESCRIPTION = "description";
    private GoogleMap googleMap;
    private TextView btnRequestRide, txtAddressCurrent;
    private final String TAG_FROM = "FROM";
    private final String TAG_DESTINATION = "DESTINATION";
    private EditText txtFrom, txtDestination;
    private String add, tagLocation = TAG_FROM;
    private String placeId = "", description = "", strDistance="", strDuration="", strDetailFrom = "a", strDetailDestination ="b";

    private LatLng mapCenter, posFrom, posDest;
    private AdapterAddress mPlaceArrayAdapter;
    private Marker markerFrom, markerDestination;
    private CameraUpdate cameraUpdate;
    private Polyline driveLine;
    private Circle mapCircle;
    private Activity mActivity;
    private ProgressBar pSuggestion;
    private AdapterSuggestion mAdapter;
    private RelativeLayout layoutSuggestion;
    private FrameLayout itemCurrent;
    private ListView mListView;
    private Location location;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000 * 60 * 1;
    private static final long MIN_TIME_BW_UPDATES =  1;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_transport4, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnRequestRide = (TextView) rootView.findViewById(R.id.btnRequestRide);
        txtFrom = (EditText) rootView.findViewById(R.id.txtFrom);
        txtDestination = (EditText) rootView.findViewById(R.id.txtDestination);
        mListView = (ListView) rootView.findViewById(R.id.lvSuggestion);
        pSuggestion = (ProgressBar) rootView.findViewById(R.id.progressSuggestion);
        layoutSuggestion = (RelativeLayout) rootView.findViewById(R.id.layoutSuggestion);
        itemCurrent = (FrameLayout) rootView.findViewById(R.id.itemCurrent);
        txtAddressCurrent = (TextView) rootView.findViewById(R.id.txtAddressCurrent);

        new GetMyLocation(mActivity).execute();

        btnRequestRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("from", txtFrom.getText().toString());
                args.putString("destination", txtDestination.getText().toString());
                args.putString("detailfrom", strDetailFrom);
                args.putString("detaildestination", strDetailDestination);
                args.putString("distance", strDistance);
                args.putString("duration", strDuration);
                args.putString("lat", "" + posFrom.latitude);
                args.putString("lon", "" + posFrom.longitude);

                Intent intent = new Intent(mActivity, ActivityConfirm.class);
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
                    if (posFrom != null) {
                        layoutSuggestion.setVisibility(VISIBLE);
                        itemCurrent.setVisibility(VISIBLE);
                        mListView.setVisibility(GONE);
                        pSuggestion.setVisibility(GONE);
                        txtAddressCurrent.setText(getAddress(posFrom));
                    } else {
                        itemCurrent.setVisibility(GONE);
                        mListView.setVisibility(VISIBLE);
                    }
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
                    if (posDest != null) {
                        layoutSuggestion.setVisibility(VISIBLE);
                        itemCurrent.setVisibility(VISIBLE);
                        mListView.setVisibility(GONE);
                        pSuggestion.setVisibility(GONE);
                        txtAddressCurrent.setText(getAddress(posDest));
                    } else {
                        itemCurrent.setVisibility(GONE);
                        mListView.setVisibility(VISIBLE);
                    }
                    Log.d("ActivityTransport", tagLocation);
                }


                return false;
            }
        });



        txtFrom.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() >=3)
                {
                    new GetSuggestion(s.toString(), tagLocation).execute();
                }
            }
        });
        txtDestination.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() >= 3) {
                    new GetSuggestion(s.toString(), tagLocation).execute();
                }
            }
        });

        itemCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtFrom.getText().toString().isEmpty() || txtDestination.getText().toString().isEmpty()) {
                    if (tagLocation.equals(TAG_FROM)) {
                        txtFrom.setText(txtAddressCurrent.getText().toString());
                    }
                    if (tagLocation.equals(TAG_DESTINATION)) {
                        txtDestination.setText(txtAddressCurrent.getText().toString());

                    }
                }
                hideKeyboard();
                layoutSuggestion.setVisibility(GONE);

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public void drawNewMarker(String address) {
        Log.d("FragmentHome", address);
        try {
            ModelGeocode geocode = GoogleAPIManager.geocode(address);
            LatLng locationMarker = new LatLng(geocode.getLat(), geocode.getLon());
            if (driveLine != null) {
                driveLine.remove();
            }
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
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationMarker, 13);
                googleMap.animateCamera(cameraUpdate);
            }
            Document doc = GoogleAPIManager.getRoute(posFrom, posDest, "driving");

            ArrayList<LatLng> directionPoint = GoogleAPIManager.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(15).color(getResources().getColor(R.color.bg_grad_2));

            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }
            strDistance = "" + GoogleAPIManager.getDistanceText(doc);
            strDuration = "" + GoogleAPIManager.getDurationText(doc);
            driveLine = googleMap.addPolyline(rectLine);

        }
            catch(Exception e) {

            }

    }

    public String getAddress(LatLng latlng) {
        Geocoder geocoder = new Geocoder(mActivity, Locale.getDefault());
        double lat = latlng.latitude;
        double lng = latlng.longitude;
        String addressLine = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            addressLine = obj.getAddressLine(0);
            if(tagLocation.equals(TAG_FROM)){
                strDetailFrom = obj.getAddressLine(1)+" , "+obj.getAddressLine(2);
            }
            else if(tagLocation.equals(TAG_DESTINATION)){
                strDetailDestination = obj.getAddressLine(1)+" , "+obj.getAddressLine(2);
            }

        } catch (IOException e) {
        } catch (Exception e) {
        }
        return addressLine;
    }

    public void hideKeyboard(){
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public class GetSuggestion extends AsyncTask<String, Void, JSONArray> {
        String address,tag;

        public GetSuggestion(String address, String tag){
            this.address = address;
            this.tag = tag;
        }

        @Override
        protected void onPreExecute() {
            layoutSuggestion.setVisibility(VISIBLE);
            pSuggestion.setVisibility(VISIBLE);
            itemCurrent.setVisibility(GONE);
            mListView.setVisibility(VISIBLE);
            mListView.setAdapter(null);
        }

        @Override
        protected JSONArray doInBackground(String... arg) {
            JSONArray json = null;
            LIST_PLACE = new ArrayList<ModelPlace>();
            JSONControl JSONControl = new JSONControl();
            try {
                json = JSONControl.listPlace(address);
            } catch (Exception e) {
            }
            if (json != null) {
                for (int i = 0; i < json.length(); i++) {
                    String id = "";
                     description  ="";
                     address = "";
                    String detail = "";
                    boolean status = true;
                    try {
                        JSONObject jsonObject = json.getJSONObject(i);
                        id = jsonObject.getString(KEY_ID);
                        description = jsonObject.getString(KEY_DESCRIPTION);
                        String[] descSplit = description.split(",");
                        address = descSplit[0];
                        detail = descSplit[1]+","+descSplit[2];
                        status=true;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (status) {
                        mPlace = new ModelPlace(id, address, detail);
                        LIST_PLACE.add(mPlace);
                    }
                }
                    try {
                    mAdapter = new AdapterSuggestion(getActivity(), LIST_PLACE);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            else{
                LIST_PLACE = null;
            }

            return json;
        }

        @Override
        protected void onPostExecute(final JSONArray json) {
            // TODO Auto-generated method stub
            super.onPostExecute(json);
            pSuggestion.setVisibility(GONE);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        ModelPlace selectedPlace = LIST_PLACE.get(position);
                        if (tag.equals(TAG_FROM)) {
                            txtFrom.setText(selectedPlace.getAddress());
                            strDetailFrom = selectedPlace.getAddressDetail();
                            if (markerFrom != null) {
                                markerFrom.remove();
                            }
                        } else if (tag.equals(TAG_DESTINATION)) {
                            txtDestination.setText(selectedPlace.getAddress());
                            strDetailDestination = selectedPlace.getAddressDetail();
                            if (markerDestination != null) {
                                markerDestination.remove();
                            }

                        }
                        layoutSuggestion.setVisibility(GONE);
                        hideKeyboard();
                        drawNewMarker(selectedPlace.getAddress());
                    } catch (Exception e) {

                    }
                }
            });

        }
    }

    private class GetMyLocation extends AsyncTask<String, Void, String> implements LocationListener {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;
        private Handler mUserLocationHandler = null;
        private Handler handler = null;



        public GetMyLocation(Activity activity) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Memuat lokasi anda. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                try {
                    Looper.prepare();
                    mUserLocationHandler = new Handler();
                    Log.d("loc", "get current location");

                    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
                    if (status != ConnectionResult.SUCCESS) {
                        int requestCode = 10;
                        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, mActivity, requestCode);
                        dialog.show();
                    } else {
                        SupportMapFragment fm = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
                        googleMap = fm.getMap();
                        LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
                        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                        Criteria criteria = new Criteria();
                        String provider = locationManager.getBestProvider(criteria, true);
                        location = locationManager.getLastKnownLocation(provider);
                        if (!isGPSEnabled && !isNetworkEnabled)
                        {
                            DialogManager.showDialog(mActivity,"Warning","Turn on your GPS or network!");
                        }
                        else
                        {

                            if (isNetworkEnabled) {

                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,this );
                                Log.d("Network", "Network");
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                    if (location != null) {
                                        Double latitude = location.getLatitude();
                                        Double longitude = location.getLongitude();
                                        posFrom = new LatLng(latitude,longitude);
                                    }
                                }
                            }
                            if (isGPSEnabled) {
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,this );
                                Log.d("Network", "Network");
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    if (location != null) {
                                        Double latitude = location.getLatitude();
                                        Double longitude = location.getLongitude();
                                        posFrom = new LatLng(latitude,longitude);
                                    }
                                }
                            }
                        }
                    }
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "OK";
            }
            catch (Exception e){

            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            switch (result) {
                case "FAIL":
                    DialogManager.showDialog(activity, "Warning", "Can not find your location!");
                    break;
                case "OK":
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    posFrom = new LatLng(latitude,longitude);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(posFrom));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    CircleOptions circleOptions = new CircleOptions()
                            .center(posFrom)
                            .radius(500)
                            .strokeWidth(1)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.parseColor("#500084d3"));

                    // mapCircle = googleMap.addCircle(circleOptions);
                    drawNewMarker(getAddress(posFrom));
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(posFrom, 15);
                    googleMap.animateCamera(cameraUpdate);
                   // DialogManager.showDialog(activity, "", "Success find your location!");
                    break;
            }

            progressDialog.dismiss();
        }


        @Override
        public void onLocationChanged(Location location) {

            Message msg = new Message();
            handler.sendMessage(msg);
            if(mUserLocationHandler != null){
                mUserLocationHandler.getLooper().quit();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

}
