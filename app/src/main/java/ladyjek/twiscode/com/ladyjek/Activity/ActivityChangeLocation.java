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
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelUserOrder;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Service.ServiceLocation;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.NetworkManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;

public class ActivityChangeLocation extends FragmentActivity implements GoogleMap.OnMapClickListener {

    private TextView lblChange,btnSimpan, txtAddress;
    private ImageView btnBack;
    private ServiceLocation serviceLocation;
    private Activity mActivity;
    private GoogleMap googleMap;
    private String strDetail;
    private LatLng posTemp;
    public static Marker markerTemp;
    ApplicationManager applicationManager;
    private ModelUserOrder user = new ModelUserOrder();;
    private SocketManager socketManager;
    ProgressDialog progressDialog;
    private BroadcastReceiver changeHouseLocation, changeOfficeLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);
        mActivity =this;
        btnSimpan = (TextView) findViewById(R.id.btnSimpan);
        lblChange = (TextView) findViewById(R.id.txtLocation);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        user = ApplicationManager.getInstance(mActivity).getUser();
        socketManager = ApplicationData.socketManager;

        changeHouseLocation = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("broadcast", "changeHouseLocation");
                String message = intent.getStringExtra("message");
                if (message.equals("true")) {
                    //OpenLoading();
                    user.setRumah(ApplicationData.Home);
                    ApplicationManager.getInstance(mActivity).setUser(user);
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
                if (message.equals("true")) {
                    //OpenLoading();
                    user.setKantor(ApplicationData.Office);
                    ApplicationManager.getInstance(mActivity).setUser(user);
                }
                else {

                }

                progressDialog.dismiss();


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
                if(ApplicationData.editHome) {
                    socketManager.ChangeHouseLocation(ApplicationData.Home, ApplicationData.homeAddress);
                } else{
                    socketManager.ChangeOfficeLocation(ApplicationData.Office, ApplicationData.officeAddress);
                }
                Intent i = new Intent(getBaseContext(), ActivityInformasiPribadi.class);
                startActivity(i);
                finish();
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
        googleMap.clear();
        posTemp = latLng;

        markerTemp = googleMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), new GoogleMap.CancelableCallback() {
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
            }

            @Override
            public void onCancel() {
            }
        });
    }

    public void GetUserLocation(LatLng latLng){
        googleMap.clear();
        posTemp = latLng;
        markerTemp = googleMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
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
                        Marker markerFrom = googleMap.addMarker(
                                new MarkerOptions()
                                        .position(currentLocation)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
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




}
