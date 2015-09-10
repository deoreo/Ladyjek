package ladyjek.twiscode.com.ladyjek.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.Html;
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
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ladyjek.twiscode.com.ladyjek.Activity.ActivityConfirm;
import ladyjek.twiscode.com.ladyjek.Activity.ActivityLoading;
import ladyjek.twiscode.com.ladyjek.Activity.Main;
import ladyjek.twiscode.com.ladyjek.Adapter.AdapterAddress;
import ladyjek.twiscode.com.ladyjek.Adapter.AdapterSuggestion;
import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelGeocode;
import ladyjek.twiscode.com.ladyjek.Model.ModelPlace;
import ladyjek.twiscode.com.ladyjek.Model.ModelUserOrder;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.GoogleAPIManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.NetworkManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.widget.AdapterView.OnItemClickListener;

import org.droidparts.widget.ClearableEditText;
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
 * ladyjek.twiscode.com.ladyjek.Utilities
 * Created by Unity on 18/05/2015.
 */
public class FragmentHome extends Fragment implements GoogleMap.OnMapClickListener {

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
    private ClearableEditText txtFrom, txtDestination;
    private String add, tagLocation;
    private String placeId = "", description = "", strDistance = "", strDuration = "", strDetailFrom = "a", strDetailDestination = "b";

    private LatLng mapCenter, posFrom, posDest, posTemp, posDriver;
    private AdapterAddress mPlaceArrayAdapter;
    private Marker markerFrom, markerDestination;
    public static Marker markerTemp;
    private CameraUpdate cameraUpdate;
    private Polyline driveLine;
    private Circle mapCircle;
    private static Activity mActivity;
    private ProgressBar pSuggestion;
    private AdapterSuggestion mAdapter;
    private static RelativeLayout layoutSuggestion, wrapperRegister, mapWrapper;
    private LinearLayout layoutfillForm;
    public static LinearLayout layoutMarkerFrom, layoutMarkerDestination;
    private FrameLayout itemCurrent;
    private ListView mListView;
    private Button btnLocationFrom, btnLocationDestination;
    private Location location;
    private TextView txtLocationFrom, txtLocationDestinton;
    private ProgressBar progressMapFrom, progressMapDestination;
    private SupportMapFragment fm;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000 * 60 * 1;
    private static final long MIN_TIME_BW_UPDATES = 1;
    public static boolean mTouchMap = true;
    private ApplicationManager appManager;
    private final String TAG = "FragmentHome";
    private SocketManager socketManager;
    private BroadcastReceiver createOrder;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TouchableWrapper", "OnCreate");
        mActivity = getActivity();
        appManager = new ApplicationManager(mActivity);
        posFrom = ApplicationData.posFrom;
        tagLocation = TAG_FROM;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_transport4, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("TouchableWrapper", "OnCreateView");

        btnRequestRide = (TextView) rootView.findViewById(R.id.btnRequestRide);
        txtFrom = (ClearableEditText) rootView.findViewById(R.id.txtFrom);
        txtDestination = (ClearableEditText) rootView.findViewById(R.id.txtDestination);
        mListView = (ListView) rootView.findViewById(R.id.lvSuggestion);
        pSuggestion = (ProgressBar) rootView.findViewById(R.id.progressSuggestion);
        layoutSuggestion = (RelativeLayout) rootView.findViewById(R.id.layoutSuggestion);
        itemCurrent = (FrameLayout) rootView.findViewById(R.id.itemCurrent);
        txtAddressCurrent = (TextView) rootView.findViewById(R.id.txtAddressCurrent);
        wrapperRegister = (RelativeLayout) rootView.findViewById(R.id.wrapperRegister);
        layoutfillForm = (LinearLayout) rootView.findViewById(R.id.layoutfillForm);
        btnLocationFrom = (Button) rootView.findViewById(R.id.btnLocationFrom);
        btnLocationDestination = (Button) rootView.findViewById(R.id.btnLocationDestination);
        layoutMarkerFrom = (LinearLayout) rootView.findViewById(R.id.layoutMarkerFrom);
        layoutMarkerDestination = (LinearLayout) rootView.findViewById(R.id.layoutMarkerDestination);
        mapWrapper = (RelativeLayout) rootView.findViewById(R.id.mapWrapper);
        progressMapFrom = (ProgressBar) rootView.findViewById(R.id.progressMapFrom);
        progressMapDestination = (ProgressBar) rootView.findViewById(R.id.progressMapDestination);
        txtLocationDestinton = (TextView) rootView.findViewById(R.id.txtLocationDestination);
        txtLocationFrom = (TextView) rootView.findViewById(R.id.txtLocationFrom);
        btnLocationFrom = (Button) rootView.findViewById(R.id.btnLocationFrom);
        btnLocationDestination = (Button) rootView.findViewById(R.id.btnLocationDestination);
        btnRequestRide.setText(Html.fromHtml(getResources().getString(R.string.pesan)));
        socketManager = new SocketManager();
        socketManager.InitSocket(mActivity);
        socketManager.Connect();
        ApplicationData.socketManager = socketManager;
        createOrder = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "broadcast createOrder");
                Intent i = new Intent(mActivity, ActivityLoading.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                mActivity.finish();
            }
        };


        SupportMapFragment fm = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        googleMap = fm.getMap();

        if (NetworkManager.getInstance(mActivity).isConnectedInternet()) {
            new GetMyLocation(mActivity).execute();
        } else {
            DialogManager.showDialog(mActivity, "Warning", "No internet connection");
        }

        wrapperRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strDistance.isEmpty() && strDuration.isEmpty()) {
                    DialogManager.showDialog(mActivity, "Warning", "Tentukan lokasi awal dan akhir!");
                } else {
                    ApplicationData.addressFrom = txtFrom.getText().toString();
                    ApplicationData.addressDestination = txtDestination.getText().toString();
                    ApplicationData.detailFrom = strDetailFrom;
                    ApplicationData.detailDestination = strDetailDestination;
                    ApplicationData.distance = strDistance;
                    ApplicationData.duration = strDuration;
                    socketManager.CreateOrder(posFrom, posDest);
                    Intent intent = new Intent(mActivity, ActivityConfirm.class);
                    startActivity(intent);

                    // /new DoPesan(mActivity,socketManager, posFrom, posDest);
                }
            }
        });
        layoutfillForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnRequestRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strDistance.isEmpty() && strDuration.isEmpty()) {
                    DialogManager.showDialog(mActivity, "Warning", "Tentukan lokasi awal dan akhir!");
                } else {
                    ApplicationData.addressFrom = txtFrom.getText().toString();
                    ApplicationData.addressDestination = txtDestination.getText().toString();
                    ApplicationData.detailFrom = strDetailFrom;
                    ApplicationData.detailDestination = strDetailDestination;
                    ApplicationData.distance = strDistance;
                    ApplicationData.duration = strDuration;
                    socketManager.CreateOrder(posFrom, posDest);
                    Intent intent = new Intent(mActivity, ActivityConfirm.class);
                    startActivity(intent);
                }
            }
        });

        txtFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //do stuff here
                    tagLocation = TAG_FROM;
                    mTouchMap = false;
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
                    mTouchMap = false;
                    Log.d("ActivityTransport", tagLocation);
                }


                return false;
            }
        });

        layoutSuggestion.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (itemCurrent.getVisibility() == GONE)
                        layoutSuggestion.setVisibility(GONE);
                }
                return false;
            }
        });


        mapWrapper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("touch", "Map Touch");
                layoutMarkerFrom.setVisibility(GONE);
                layoutMarkerDestination.setVisibility(GONE);
                return false;
            }
        });

        txtFrom.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                Log.d("txt", "" + actionId);
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    mTouchMap = false;
                    handled = true;
                }
                return handled;
            }
        });

        txtFrom.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(txtFrom.getText().toString().equals("")) {
                    if (markerFrom != null) {
                        markerFrom.remove();
                        posFrom = null;
                    }
                    if (driveLine != null) {
                        driveLine.remove();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!mTouchMap) {
                    if (s.length() >= 3) {
                        new GetSuggestion(s.toString(), tagLocation).execute();
                    } else if (s.length() == 0) {
                        layoutSuggestion.setVisibility(GONE);
                    }
                }


            }
        });
        txtDestination.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(txtDestination.getText().toString().equals("")) {
                    if (markerDestination != null) {
                        markerDestination.remove();
                        posDest = null;
                    }
                    if (driveLine != null) {
                        driveLine.remove();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!mTouchMap) {
                    if (s.length() >= 3) {
                        new GetSuggestion(s.toString(), tagLocation).execute();
                    } else if (s.length() == 0) {
                        layoutSuggestion.setVisibility(GONE);
                            if (markerDestination != null) {
                                markerDestination.remove();
                            }
                            if (driveLine != null) {
                                driveLine.remove();
                            }

                    }
                }
            }


    }

    );


    itemCurrent.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){

        if (txtFrom.getText().toString().isEmpty() || txtDestination.getText().toString().isEmpty()) {
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
    }

    );

    btnLocationFrom.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        posFrom = posTemp;
        if (driveLine != null) {
            driveLine.remove();
        }
        if (markerFrom != null) {
            markerFrom.remove();
        }
        if (markerTemp != null) {
            markerTemp.remove();
        }
        layoutMarkerDestination.setVisibility(GONE);
        layoutMarkerFrom.setVisibility(GONE);
        String address = getAddress(posFrom);
        txtFrom.setText(address);
        strDetailFrom = address;
        ApplicationData.posFrom = posFrom;
        appManager.setUserFrom(new ModelPlace(posFrom.latitude, posFrom.longitude));

        markerFrom = googleMap.addMarker(
                new MarkerOptions()
                        .position(posFrom)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
        if (posFrom != null && posDest != null) {
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

    }
    }

    );

    btnLocationDestination.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        posDest = posTemp;
        if (driveLine != null) {
            driveLine.remove();
        }
        if (markerDestination != null) {
            markerDestination.remove();
        }
        if (markerTemp != null) {
            markerTemp.remove();
        }
        layoutMarkerDestination.setVisibility(GONE);
        layoutMarkerFrom.setVisibility(GONE);
        String address = getAddress(posDest);
        txtDestination.setText(address);
        strDetailDestination = address;
        ApplicationData.posDestination = posDest;
        appManager.setUserDestination(new ModelPlace(posDest.latitude, posDest.longitude));
        markerDestination = googleMap.addMarker(
                new MarkerOptions()
                        .position(posDest)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));

        if (posFrom != null && posDest != null) {
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

    }
    }

    );

    googleMap.setOnMapClickListener(this);
    googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener()

    {
        @Override
        public void onCameraChange (CameraPosition cameraPosition){
        mapCenter = googleMap.getCameraPosition().target;
        add = getAddress(mapCenter);
        txtLocationFrom.setText(add);
        txtLocationDestinton.setText(add);
        progressMapFrom.setVisibility(View.GONE);
        progressMapDestination.setVisibility(View.GONE);
    }
    }

    );


    // Inflate the layout for this fragment
    return rootView;
}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks whether a keyboard is available
        if (newConfig.KEYBOARDHIDDEN_NO == Configuration.KEYBOARDHIDDEN_NO) {
            Log.d("keyboard", "show");
        } else if (newConfig.KEYBOARDHIDDEN_YES == Configuration.KEYBOARDHIDDEN_YES) {
            Log.d("keyboard", "hidden");
        }
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

        } catch (Exception e) {

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
            if (tagLocation.equals(TAG_FROM)) {
                strDetailFrom = obj.getAddressLine(1) + " , " + obj.getAddressLine(2);
            } else if (tagLocation.equals(TAG_DESTINATION)) {
                strDetailDestination = obj.getAddressLine(1) + " , " + obj.getAddressLine(2);
            }

        } catch (IOException e) {
        } catch (Exception e) {
        }
        return addressLine;
    }

    public static void hideKeyboard() {
        View view = mActivity.getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            layoutSuggestion.setVisibility(GONE);
        }

    }


    @Override
    public void onMapClick(LatLng latLng) {
        //drawNewMarker
        if (tagLocation.equals(TAG_FROM)) {
            //posFrom=latLng;
            posTemp = latLng;
            markerTemp = googleMap.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    layoutMarkerFrom.setVisibility(VISIBLE);
                    layoutMarkerDestination.setVisibility(GONE);
                }

                @Override
                public void onCancel() {
                }
            });

        } else if (tagLocation.equals(TAG_DESTINATION)) {
            //posDest = latLng;
            posTemp = latLng;
            markerTemp = googleMap.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    layoutMarkerFrom.setVisibility(GONE);
                    layoutMarkerDestination.setVisibility(VISIBLE);
                }

                @Override
                public void onCancel() {
                }
            });

        }
        layoutSuggestion.setVisibility(GONE);
    }


public class GetSuggestion extends AsyncTask<String, Void, JSONArray> {
    String address, tag;

    public GetSuggestion(String address, String tag) {
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
                description = "";
                address = "";
                String detail = "";
                boolean status = true;
                try {
                    JSONObject jsonObject = json.getJSONObject(i);
                    id = jsonObject.getString(KEY_ID);
                    description = jsonObject.getString(KEY_DESCRIPTION);
                    String[] descSplit = description.split(",");
                    address = descSplit[0];
                    detail = descSplit[1] + "," + descSplit[2];
                    status = true;

                } catch (JSONException e) {
                } catch (Exception e) {

                }

                if (status) {
                    mPlace = new ModelPlace(id, address, detail);
                    LIST_PLACE.add(mPlace);
                }
            }
            try {
                mAdapter = new AdapterSuggestion(getActivity(), LIST_PLACE);
            } catch (NullPointerException e) {
            }
        } else {
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
                        ModelGeocode geocode = GoogleAPIManager.geocode(selectedPlace.getAddress());
                        ApplicationData.posFrom = new LatLng(geocode.getLat(), geocode.getLon());
                        appManager.setUserFrom(new ModelPlace(geocode.getLat(), geocode.getLon()));

                    } else if (tag.equals(TAG_DESTINATION)) {
                        txtDestination.setText(selectedPlace.getAddress());
                        strDetailDestination = selectedPlace.getAddressDetail();
                        if (markerDestination != null) {
                            markerDestination.remove();
                        }
                        ModelGeocode geocode = GoogleAPIManager.geocode(selectedPlace.getAddress());
                        ApplicationData.posDestination = new LatLng(geocode.getLat(), geocode.getLon());
                        appManager.setUserDestination(new ModelPlace(geocode.getLat(), geocode.getLon()));
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
    double latitude, longitude;
    private GoogleMap gMap;
    LocationManager locationManager;

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
        Log.d("posisi gps", "doInbackground");
        try {
            try {
                Looper.prepare();
                mUserLocationHandler = new Handler();
                Log.d("loc", "get current location");
                SupportMapFragment fm = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
                gMap = fm.getMap();
                int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
                if (status != ConnectionResult.SUCCESS) {
                    int requestCode = 10;
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, mActivity, requestCode);
                    dialog.show();
                } else {

                    locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
                    boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    Criteria criteria = new Criteria();
                    String provider = locationManager.getBestProvider(criteria, true);
                    location = locationManager.getLastKnownLocation(provider);
                    if (!isGPSEnabled && !isNetworkEnabled) {
                        return "FAIL";
                    } else {
                        if (isNetworkEnabled) {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                            Log.d("locationManager", "Network");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    posFrom = new LatLng(latitude, longitude);
                                    ApplicationData.posFrom = posFrom;
                                    appManager.setUserFrom(new ModelPlace(posFrom.latitude, posFrom.longitude));
                                }
                            }
                        }
                        if (isGPSEnabled) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                            Log.d("locationManager", "GPS");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    posFrom = new LatLng(latitude, longitude);
                                    ApplicationData.posFrom = posFrom;

                                }
                            }
                        }
                    }
                }

                Looper.loop();
            } catch (Exception e) {
            }
            return "OK";
        } catch (Exception e) {

        }
        return "FAIL";

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("posisi gps", "onPost");
        switch (result) {
            case "FAIL":
                DialogManager.showDialog(activity, "Peringatan", "Can not find your location!");

                break;
            case "OK":
                try {
                    LatLng pFrom = ApplicationData.posFrom;
                    Log.d("posisi gps", "pFrom");
                    if (gMap == null) {
                        SupportMapFragment fm = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
                        gMap = fm.getMap();
                    } else {
                        Log.d("posisi gps", "map not null");
                    }
                    gMap.moveCamera(CameraUpdateFactory.newLatLng(pFrom));
                    gMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    markerFrom = gMap.addMarker(
                            new MarkerOptions()
                                    .position(pFrom)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(pFrom, 15);
                    txtFrom.setText(getAddress(pFrom));
                    gMap.animateCamera(cameraUpdate);
                    Log.d("posisi gps", pFrom.toString());
                } catch (Exception e) {
                }
                break;
        }

        progressDialog.dismiss();
    }


    @Override
    public void onLocationChanged(Location location) {
        try {
            LatLng pFrom = new LatLng(latitude, longitude);
            ApplicationData.posFrom = pFrom;
        } catch (Exception e) {
            Log.d("FragmentHome", "OnLocationChange");
        }
        Message msg = new Message();
        handler.sendMessage(msg);
        if (mUserLocationHandler != null) {
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


    private class DoPesan extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;
        private SocketManager socketManager;
        private LatLng pFrom, pDestination;

        public DoPesan(Activity activity, SocketManager socketManager, LatLng pFrom, LatLng pDestination) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
            this.socketManager = socketManager;
            this.pFrom = pFrom;
            this.pDestination = pDestination;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Sedang Memesan. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                socketManager.CreateOrder(pFrom, pDestination);
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            switch (result) {
                case "FAIL":
                    DialogManager.showDialog(activity, "Warning", "Gagal memesan, Silakan mencoba lagi!");
                    break;
                case "OK":
                    Intent intent = new Intent(activity, ActivityConfirm.class);
                    startActivity(intent);
                    activity.finish();
                    break;
            }


        }


    }

    @Override
    public void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        Log.i("adding receiver", "fragment ontainer for profile");
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(createOrder,
                new IntentFilter("createOrder"));

    }

    @Override
    public void onPause() {
        // Unregister since the activity is not visible
        Log.i("unreg receiver", "fragment unregister");
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(createOrder);
        super.onPause();
    }

}
