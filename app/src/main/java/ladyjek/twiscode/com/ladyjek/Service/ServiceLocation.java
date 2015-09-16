package ladyjek.twiscode.com.ladyjek.Service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.Calendar;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;


/**
 * Created by User on 9/4/2015.
 */
public class ServiceLocation implements LocationListener {
    private LocationManager locationManager;
    private Location location;
    private LatLng mPosition;
    private static ServiceLocation sInstance;
    private final String TAG = "ServiceLocation";

    public ServiceLocation(final Context context) {
        Log.d("ServiceLocation", "Execute");
    }

    public ServiceLocation() { }

    public static ServiceLocation getInstance() {
        if (sInstance == null) {
            sInstance = new ServiceLocation();
        }
        return sInstance;
    }


    public LatLng updateLocation(Context context) {

        LatLng pos = null;
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
        } else {
            try {
                locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);
                location = locationManager.getLastKnownLocation(provider);
                if (!isGPSEnabled && !isNetworkEnabled) {
                    return null;
                } else {
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                        Log.d("locationManager", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                Double latitude = location.getLatitude();
                                Double longitude = location.getLongitude();
                                pos = new LatLng(latitude, longitude);

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
                                Double latitude = location.getLatitude();
                                Double longitude = location.getLongitude();
                                pos = new LatLng(latitude, longitude);
                            }
                        }
                    }
                }
                mPosition = pos;
                Log.d("ServiceLocation", getPosition().toString());
            }
            catch(Exception e){

            }
        }
        return pos;

    }

    public void GetMap(Activity activity, GoogleMap googleMap){
        new UpdateMap(activity, googleMap).execute();
    }

    public void GetDriverMarker(GoogleMap googleMap){
        //new UpdateDriverMarker(activity, googleMap).execute();
        mPosition = ApplicationData.posDriver;
        Log.d(TAG, "ApplicationData.posDriver : "+ ApplicationData.posDriver);
        try {
            float zoom = googleMap.getCameraPosition().zoom;
            if(zoom<=13){
                zoom=13;
            }
            if(ApplicationData.markerDriver!=null) {
                ApplicationData.markerDriver.remove();
            }
            ApplicationData.markerDriver = googleMap.addMarker(
                    new MarkerOptions()
                            .position(mPosition)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_driver)));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mPosition, zoom);
            googleMap.animateCamera(cameraUpdate);

        } catch (Exception e) {
            e.printStackTrace();
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

    public LatLng getPosition() {
        return mPosition;
    }

    private class UpdateMap extends AsyncTask<String, Void, String> implements LocationListener {
        private Activity activity;
        private Context context;
        private Resources resources;
        private Handler mUserLocationHandler = null;
        private Handler handler = new Handler();
        private Double latitude, longitude;
        private GoogleMap gMap;
        private LocationManager locationManager;
        private CameraUpdate cameraUpdate;
        private Location location;
        private LatLng posFrom;
        private ProgressDialog progressDialog;

        public UpdateMap(Activity activity, GoogleMap gMap) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
            this.gMap = gMap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "doInbackground posisi gps");
            try {
                try {
                    Looper.prepare();
                    mUserLocationHandler = new Handler();
                    Log.d(TAG, "doInbackground get current location");
                    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
                    if (status != ConnectionResult.SUCCESS) {
                        int requestCode = 10;
                        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, activity, requestCode);
                        dialog.show();
                    } else {

                        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
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
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                        posFrom = new LatLng(latitude, longitude);
                                        ApplicationData.posFrom = posFrom;

                                        mPosition = posFrom;
                                    }
                                }
                            }
                            if (isGPSEnabled) {
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                        posFrom = new LatLng(latitude, longitude);
                                        ApplicationData.posFrom = posFrom;
                                        mPosition = posFrom;
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
            } catch (Exception e) {

            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            switch (result) {
                case "FAIL":
                    ApplicationData.isFindLocation = false;
                    break;
                case "OK":
                    ApplicationData.isFindLocation = true;
                    try {
                        float zoom = gMap.getCameraPosition().zoom;
                        if(zoom<=13){
                            zoom=13;
                        }
                        if(ApplicationData.markerFrom!=null) {
                            ApplicationData.markerFrom.remove();
                        }
                        ApplicationData.markerFrom = gMap.addMarker(
                                new MarkerOptions()
                                        .position(mPosition)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
                        cameraUpdate = CameraUpdateFactory.newLatLngZoom(mPosition, zoom);
                        gMap.animateCamera(cameraUpdate);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }


        @Override
        public void onLocationChanged(Location location) {
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

    private class UpdateDriverMarker extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        double latitude, longitude;
        private GoogleMap gMap;
        private LocationManager locationManager;
        private CameraUpdate cameraUpdate;
        private Location location;
        private LatLng posFrom;

        public UpdateDriverMarker(Activity activity, GoogleMap gMap) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
            this.gMap = gMap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "doInbackground posisi gps");
            try {
                try {
                    mPosition = ApplicationData.posDriver;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "OK";
            } catch (Exception e) {

            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            switch (result) {
                case "FAIL":
                    Log.d(TAG, "FAIL");
                    Log.d(TAG, "ApplicationData.posDriver : "+ ApplicationData.posDriver);
                    break;
                case "OK":
                    Log.d(TAG, "OK");
                    mPosition = ApplicationData.posDriver;
                    Log.d(TAG, "ApplicationData.posDriver : "+ ApplicationData.posDriver);
                    try {
                        float zoom = gMap.getCameraPosition().zoom;
                        if(zoom<=13){
                            zoom=13;
                        }
                        if(ApplicationData.markerDriver!=null) {
                            ApplicationData.markerDriver.remove();
                        }
                        ApplicationData.markerDriver = gMap.addMarker(
                                new MarkerOptions()
                                        .position(mPosition)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_driver)));
                        cameraUpdate = CameraUpdateFactory.newLatLngZoom(mPosition, zoom);
                        gMap.animateCamera(cameraUpdate);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    }
}

