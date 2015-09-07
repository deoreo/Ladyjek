package ladyjek.twiscode.com.ladyjek.Service;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;

import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;

/**
 * Created by User on 9/4/2015.
 */
public class ServiceLocation implements LocationListener {
    private SocketManager socketManager;
    private LocationManager locationManager;
    private Location location;
    private Context context = null;
    private LatLng mPosition;
    private Runnable mRunnable;
    private Handler mHandler;
    //private final int AUTOUPDATE_INTERVAL_TIME = 15 * 60 * 1000; // 15 menit
    private final int AUTOUPDATE_INTERVAL_TIME = 1 * 5 * 1000; // 5 detik
    private static ServiceLocation sInstance;

    public ServiceLocation(final Context context) {
        Log.d("ServiceLocation", "Execute");
        this.context = context;
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
        return pos;

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
}

