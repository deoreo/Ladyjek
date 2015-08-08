package ladyjek.twiscode.com.ladyjek.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import ladyjek.twiscode.com.ladyjek.Activity.ActivityConfirm;
import ladyjek.twiscode.com.ladyjek.Adapter.AdapterAddress;
import ladyjek.twiscode.com.ladyjek.Adapter.AdapterSuggestion;
import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Unity on 18/05/2015.
 */
public class FragmentHome extends Fragment implements
        LocationListener, OnItemClickListener, View.OnKeyListener {

    private Toolbar mToolbar;
    ModelPlace mPlace;
    private List<ModelPlace> LIST_PLACE = null;
    private static final String KEY_ID = "place_id";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DETAIL = "detail";
    private static final String KEY_DESCRIPTION = "description";
    private GoogleMap googleMap;
    private TextView btnRequestRide;
    private final String TAG_FROM = "FROM";
    private final String TAG_DESTINATION = "DESTINATION";
    private EditText txtFrom, txtDestination;
    private String add, tagLocation = TAG_FROM;
    private String placeId = "", description = "", strDistance="", strDuration="";

    private LatLng mapCenter, posFrom, posDest;
    private AdapterAddress mPlaceArrayAdapter;
    private Marker markerFrom, markerDestination;
    private CameraUpdate cameraUpdate;
    private Polyline driveLine;
    private Activity mActivity;

    private AdapterSuggestion mAdapter;
private ListView mListView;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_transport4, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mActivity = getActivity();
        btnRequestRide = (TextView) rootView.findViewById(R.id.btnRequestRide);
        txtFrom = (EditText) rootView.findViewById(R.id.txtFrom);
        txtDestination = (EditText) rootView.findViewById(R.id.txtDestination);
        mListView = (ListView) rootView.findViewById(R.id.lvSuggestion);
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);

        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, mActivity, requestCode);
            dialog.show();
        } else {
            SupportMapFragment fm = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
            googleMap = fm.getMap();
            LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                onLocationChanged(location);
            }
            mapCenter = googleMap.getCameraPosition().target;

        }

        btnRequestRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("from", txtFrom.getText().toString());
                args.putString("destination", txtDestination.getText().toString());
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

        /*
        mPlaceArrayAdapter = new AdapterAddress(mActivity, android.R.layout.simple_list_item_1);
        txtFrom.setAdapter(mPlaceArrayAdapter);
        txtFrom.setOnItemClickListener(this);
        txtFrom.setOnKeyListener(this);
        txtDestination.setAdapter(mPlaceArrayAdapter);
        txtDestination.setOnItemClickListener(this);
        txtDestination.setOnKeyListener(this);
        */

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
                    new GetSuggestion(s.toString()).execute();
                }
            }
        });




        // Inflate the layout for this fragment
        return rootView;
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
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final ModelPlace item = mPlaceArrayAdapter.getItem(position);
        description = String.valueOf(item.getAddress())+String.valueOf(item.getAddressDetail());

        if(tagLocation.equals(TAG_FROM)) {
            if (markerFrom != null) {
                //txtFrom.setText(address);
                markerFrom.remove();

            }
        }
        else if(tagLocation.equals(TAG_DESTINATION)) {
            if (markerDestination != null) {
                //txtDestination.setText(address);
                markerDestination.remove();
            }
        }
        drawNewMarker(description);
    }


    public void drawNewMarker(String address) {
        try {
            ModelGeocode geocode = PlaceAPI.geocode(address);
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

                Document doc = PlaceAPI.getRoute(posFrom, posDest, "driving");

                ArrayList<LatLng> directionPoint = PlaceAPI.getDirection(doc);
                PolylineOptions rectLine = new PolylineOptions().width(10).color(getResources().getColor(R.color.bg_grad_2));

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                strDistance = "" + PlaceAPI.getDistanceText(doc);
                strDuration = "" + PlaceAPI.getDurationText(doc);
                driveLine = googleMap.addPolyline(rectLine);


                cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationMarker, 13);
                googleMap.animateCamera(cameraUpdate);
            }
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


    public class GetSuggestion extends AsyncTask<String, Void, JSONArray> {
        ProgressDialog pDialog;
        String address;

        public GetSuggestion(String address){
            this.address = address;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                    String description  ="";
                    String address = "";
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
            mListView.setAdapter(mAdapter);
        }
    }




}
