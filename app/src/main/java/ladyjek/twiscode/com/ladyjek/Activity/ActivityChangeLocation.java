package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ladyjek.twiscode.com.ladyjek.Adapter.AdapterSuggestion;
import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelGeocode;
import ladyjek.twiscode.com.ladyjek.Model.ModelPlace;
import ladyjek.twiscode.com.ladyjek.Model.ModelUserOrder;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Service.ServiceLocation;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.GoogleAPIManager;
import ladyjek.twiscode.com.ladyjek.Utilities.NetworkManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ActivityChangeLocation extends FragmentActivity implements GoogleMap.OnMapClickListener {

    private TextView lblChange,btnSimpan, txtAddress;
    private ImageView btnBack;
    private RelativeLayout wrapperSimpan;
    private ServiceLocation serviceLocation;
    private static Activity mActivity;
    private GoogleMap googleMap;
    private String strDetail;
    private LatLng posTemp;
    public static Marker markerTemp;
    ApplicationManager applicationManager;
    private ModelUserOrder user = new ModelUserOrder();;
    private SocketManager socketManager;
    ProgressDialog progressDialog;
    private BroadcastReceiver changeHouseLocation, changeOfficeLocation;
    public static boolean mTouchMap = true;
    private static RelativeLayout layoutSuggestion;
    private ProgressBar pSuggestion;
    private FrameLayout itemCurrent;
    private ListView mListView;
    private List<ModelPlace> LIST_PLACE = null;
    private String description = "";
    private static final String KEY_ID = "place_id";
    private static final String KEY_DESCRIPTION = "description";
    private ModelPlace mPlace;
    private AdapterSuggestion mAdapter;
    private LinearLayout layoutfillForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);
        mActivity =this;
        btnSimpan = (TextView) findViewById(R.id.btnSimpan);
        wrapperSimpan = (RelativeLayout)findViewById(R.id.wrapperSimpan);
        lblChange = (TextView) findViewById(R.id.txtLocation);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        pSuggestion = (ProgressBar) findViewById(R.id.progressSuggestion);
        layoutSuggestion = (RelativeLayout) findViewById(R.id.layoutSuggestion);
        itemCurrent = (FrameLayout) findViewById(R.id.itemCurrent);
        mListView = (ListView) findViewById(R.id.lvSuggestion);
        layoutfillForm = (LinearLayout) findViewById(R.id.layoutfillForm);


        user = ApplicationManager.getInstance(mActivity).getUser();
        socketManager = ApplicationData.socketManager;



        changeHouseLocation = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("broadcast", "changeHouseLocation");
                String message = intent.getStringExtra("message");
                if (message.equals("true")) {
                    user.setRumah(ApplicationData.Home);
                    user.setAddressHome(ApplicationData.homeAddress);
                    ApplicationManager.getInstance(mActivity).setUser(user);
                    Intent i = new Intent(getBaseContext(), ActivityInformasiPribadi.class);
                    startActivity(i);
                    finish();
                }
                else {

                }

                progressDialog.dismiss();


            }
        };

        changeOfficeLocation = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("broadcast", "changeOfficeLocation");
                String message = intent.getStringExtra("message");
                progressDialog.dismiss();
                if (message.equals("true")) {
                    user.setKantor(ApplicationData.Office);
                    user.setAddressOffice(ApplicationData.officeAddress);
                    ApplicationManager.getInstance(mActivity).setUser(user);
                    Intent i = new Intent(getBaseContext(), ActivityInformasiPribadi.class);
                    startActivity(i);
                    finish();
                }
                else {

                }




            }
        };


        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        googleMap = fm.getMap();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-6.2211361, 106.8665963), 10f));
        btnSimpan.setText(Html.fromHtml(getResources().getString(R.string.simpan)));
        serviceLocation = new ServiceLocation();
        LatLng rumah = user.getRumah();
        LatLng kantor = user.getKantor();

        if(ApplicationData.editHome) {
            lblChange.setText(R.string.title_activity_change_location_rumah);
            if (NetworkManager.getInstance(mActivity).isConnectedInternet()) {
                if(rumah == null) {
                    new GetMyLocation(mActivity, googleMap, serviceLocation).execute();
                }else{
                    LatLng cPos = user.getRumah();
                    GetUserLocation(cPos);
                }
            } else {
                DialogManager.showDialog(mActivity, "Peringatan", "Tidak dapat menemukan lokasi anda!");
            }
        }
        else{
            lblChange.setText(R.string.title_activity_change_location_kantor);
            if (NetworkManager.getInstance(mActivity).isConnectedInternet()) {
                if(kantor == null) {
                    new GetMyLocation(mActivity, googleMap, serviceLocation).execute();
                }else{
                    LatLng cPos = user.getKantor();
                    GetUserLocation(cPos);
                }
            } else {
                DialogManager.showDialog(mActivity, "Peringatan", "Tidak dapat menemukan lokasi anda!");
            }
        }




        googleMap.setOnMapClickListener(this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ActivityInformasiPribadi.class);
                startActivity(i);
                finish();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLoading();
                if(ApplicationData.editHome) {
                    socketManager.ChangeHouseLocation(ApplicationData.Home, ApplicationData.homeAddress);
                } else{
                    socketManager.ChangeOfficeLocation(ApplicationData.Office, ApplicationData.officeAddress);
                }

            }
        });

        wrapperSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLoading();
                if(ApplicationData.editHome) {
                    socketManager.ChangeHouseLocation(ApplicationData.Home, ApplicationData.homeAddress);
                } else{
                    socketManager.ChangeOfficeLocation(ApplicationData.Office, ApplicationData.officeAddress);
                }
            }
        });

        layoutfillForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txtAddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //do stuff here
                    mTouchMap = false;

                }
                return false;
            }
        });

        txtAddress.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (txtAddress.getText().toString().equals("")) {
                    if (markerTemp != null) {
                        markerTemp.remove();
                        posTemp = null;
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
                        new GetSuggestion(mActivity, s.toString()).execute();
                    } else if (s.length() == 0) {
                        layoutSuggestion.setVisibility(GONE);
                    }
                }


            }
        });

    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }


    public String getAddress(Activity activity, LatLng latlng) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        double lat = latlng.latitude;
        double lng = latlng.longitude;
        String addressLine = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            addressLine = obj.getAddressLine(0);
            strDetail = obj.getAddressLine(1) + " , " + obj.getAddressLine(2);


        } catch (IOException e) {
        } catch (Exception e) {
        }
        return addressLine;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("Memuat lokasi anda. . .");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        googleMap.clear();
        posTemp = latLng;
        float zoom = googleMap.getCameraPosition().zoom;
        if(zoom<=15){
            zoom=15;
        }
        if(ApplicationData.editHome) {
            Marker markerFrom = googleMap.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
        }else{
            Marker markerFrom = googleMap.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {

                String address = getAddress(mActivity, posTemp);
                String detail= strDetail;
                txtAddress.setText(address);
                if(ApplicationData.editHome) {
                    ApplicationData.Home = posTemp;
                    ApplicationData.homeAddress = address;
                    ApplicationData.homeAddressDetail = detail;
                }
                else{
                    ApplicationData.Office = posTemp;
                    ApplicationData.officeAddress = address;
                    ApplicationData.officeAddressDetail = detail;
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancel() {
            }
        });
    }

    public void GetUserLocation(LatLng latLng){
        googleMap.clear();
        posTemp = latLng;
        if(ApplicationData.editHome) {
            markerTemp = googleMap.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
        }else{
            markerTemp = googleMap.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                txtAddress.setText(getAddress(mActivity, posTemp));
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private class GetMyLocation extends AsyncTask<String, Void, String> implements LocationListener {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;
        private double latitude, longitude;
        private GoogleMap googleMap;
        private LocationManager locationManager;
        private ServiceLocation serviceLocation;
        private String address, detail;

        public GetMyLocation(Activity activity, GoogleMap googleMap, ServiceLocation serviceLocation) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
            this.googleMap = googleMap;
            this.serviceLocation = serviceLocation;
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

                    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
                    if (status != ConnectionResult.SUCCESS) {
                        int requestCode = 10;
                    } else {

                        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                        Criteria criteria = new Criteria();
                        String provider = locationManager.getBestProvider(criteria, true);
                        Location location = locationManager.getLastKnownLocation(provider);
                        if (!isGPSEnabled && !isNetworkEnabled) {
                            return "FAIL";
                        } else {
                            if (isNetworkEnabled) {
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this, Looper.getMainLooper());
                                Log.v("locationManager", "Network");
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                        address = getAddress(activity, new LatLng(latitude, longitude));
                                        detail = strDetail;
                                        if(ApplicationData.editHome) {
                                            ApplicationData.Home = new LatLng(latitude, longitude);
                                            ApplicationData.homeAddress = address;
                                            ApplicationData.homeAddressDetail = detail;
                                        }
                                        else{
                                            ApplicationData.Office = new LatLng(latitude, longitude);
                                            ApplicationData.officeAddress = address;
                                            ApplicationData.officeAddressDetail = detail;
                                        }
                                    }
                                }
                            }
                            if (isGPSEnabled) {
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this, Looper.getMainLooper());
                                Log.v("locationManager", "GPS");
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                        address = getAddress(activity, new LatLng(latitude, longitude));
                                        detail = strDetail;
                                        if(ApplicationData.editHome) {
                                            ApplicationData.Home = new LatLng(latitude, longitude);
                                            ApplicationData.homeAddress = address;
                                            ApplicationData.homeAddressDetail = detail;
                                        }
                                        else{
                                            ApplicationData.Office = new LatLng(latitude, longitude);
                                            ApplicationData.officeAddress = address;
                                            ApplicationData.officeAddressDetail = detail;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("posisi gps", "onPost");
            progressDialog.dismiss();
            switch (result) {
                case "FAIL":
                    DialogManager.showDialog(activity, "Peringatan", "Tidak dapat menemukan lokasi anda!");
                    break;
                case "OK":
                    try {
                        LatLng currentLocation = serviceLocation.updateLocation(activity);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        Marker markerFrom ;
                        if(ApplicationData.editHome) {
                            markerFrom = googleMap.addMarker(
                                    new MarkerOptions()
                                            .position(currentLocation)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
                        }else{
                            markerFrom = googleMap.addMarker(
                                    new MarkerOptions()
                                            .position(currentLocation)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));
                        }

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 15);


                        googleMap.animateCamera(cameraUpdate);
                        txtAddress.setText(address);

                        Log.v("posisi gps", currentLocation.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }


        }


        @Override
        public void onLocationChanged(Location location) {

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

    @Override
    public void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        Log.i("adding receiver", "fragment ontainer for profile");

        LocalBroadcastManager.getInstance(this).registerReceiver(changeHouseLocation,
                new IntentFilter("changeHouseLocation"));
        LocalBroadcastManager.getInstance(this).registerReceiver(changeOfficeLocation,
                new IntentFilter("changeOfficeLocation"));

    }

    void OpenLoading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading. . .");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public class GetSuggestion extends AsyncTask<String, Void, JSONArray> {
        String address, tag;
        Activity activity;
        LatLng latLng;
        public GetSuggestion(Activity activity, String address) {
            this.address = address;
            this.tag = tag;
            this.activity = activity;
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
                    mAdapter = new AdapterSuggestion(activity, LIST_PLACE);
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
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        mTouchMap = true;
                        ModelPlace selectedPlace = LIST_PLACE.get(position);
                            txtAddress.setText(selectedPlace.getAddress());
                            if (markerTemp != null) {
                                markerTemp.remove();
                            }
                        layoutSuggestion.setVisibility(GONE);
                        hideKeyboard();
                        ModelGeocode geocode = GoogleAPIManager.geocode(description);
                        latLng = new LatLng(geocode.getLat(), geocode.getLon());
                        markerTemp = googleMap.addMarker(
                                new MarkerOptions()
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {

                                String address = getAddress(mActivity, latLng);
                                String detail = strDetail;
                                txtAddress.setText(address);
                                if (ApplicationData.editHome) {
                                    ApplicationData.Home = latLng;
                                    ApplicationData.homeAddress = address;
                                    ApplicationData.homeAddressDetail = detail;
                                } else {
                                    ApplicationData.Office = latLng;
                                    ApplicationData.officeAddress = address;
                                    ApplicationData.officeAddressDetail = detail;
                                }
                            }

                            @Override
                            public void onCancel() {
                            }
                        });


                    } catch (Exception e) {
                    }
                }
            });

        }
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
