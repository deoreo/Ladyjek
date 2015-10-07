package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.droidparts.activity.Activity;
import org.w3c.dom.Document;

import java.util.ArrayList;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelOrder;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Service.ServiceLocation;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.GoogleAPIManager;
import ladyjek.twiscode.com.ladyjek.Utilities.NetworkManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityTracking extends ActionBarActivity implements LocationListener, OnMapReadyCallback {

    private Toolbar mToolbar;
    private android.app.Activity mActivity;
    private GoogleMap googleMap;
    private LatLng posFrom, posDest;
    private Marker markerFrom, markerDestination;
    private final String TAG_FROM = "FROM";
    private final String TAG_DESTINATION = "DESTINATION";
    private ApplicationManager appManager;
    private RelativeLayout wrapperRegister;
    private Double latFrom, lonFrom, latDest, lonDest;
    private ServiceLocation serviceLocation;
    private Runnable mRunnable;
    private Handler mHandler;
    private final int AUTOUPDATE_INTERVAL_TIME = 15 * 1000; // 15 detik
    private BroadcastReceiver start, end, doEmergency;
    private Boolean isStart = false, isEnd = false;
    private SocketManager socketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        socketManager = ApplicationData.socketManager;
        start = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("start", message);
                if (message == "true") {
                    appManager.setTrip("end");
                } else {
                    Log.d("cant start", "");
                }
                isStart = false;

            }
        };

        end = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("end", message);
                if (message == "true") {
                    ApplicationData.socketManager = socketManager;
                    appManager.setTrip("");
                    appManager.setActivity("ActivityRate");
                    Intent i = new Intent(mActivity, ActivityRate.class);
                    startActivity(i);
                    finish();
                } else {
                    Log.d("cant end", "");
                }
                isEnd = false;

            }
        };

        doEmergency = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("doEmergency", message);
                DialogManager.DismissLoading(ActivityTracking.this);
                if (message == "true") {
                    DialogManager.showDialog(mActivity, "Informasi", "Emergency Call Anda telah diterima");
                } else {
                    Log.d("cant doEmergency", "");
                }
                isStart = false;

            }
        };

        mActivity = this;
        wrapperRegister = (RelativeLayout) findViewById(R.id.wrapperRegister);
        appManager = new ApplicationManager(ActivityTracking.this);
        mHandler = new Handler();
        serviceLocation = new ServiceLocation();
        if (appManager.getUserFrom() != null) {
            latFrom = appManager.getUserFrom().getLatitude();
            lonFrom = appManager.getUserFrom().getLongitude();
        } else {
            latFrom = Double.parseDouble(ApplicationData.order.getFromLatitude());
            lonFrom = Double.parseDouble(ApplicationData.order.getFromLongitude());
        }

        if (appManager.getUserDestination() != null) {
            latDest = appManager.getUserDestination().getLatitude();
            lonDest = appManager.getUserDestination().getLongitude();
        } else {
            latDest = Double.parseDouble(ApplicationData.order.getToLatitude());
            lonDest = Double.parseDouble(ApplicationData.order.getToLongitude());
        }
        posFrom = new LatLng(latFrom, lonFrom);
        posDest = new LatLng(latDest, lonDest);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {

            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
            googleMap = fm.getMap();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-6.1995921, 106.872451), 10f));
            drawNewMarker(posFrom, TAG_FROM);
            drawNewMarker(posDest, TAG_DESTINATION);
            drawDriveLine(googleMap, posFrom, posDest);
        }
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, AUTOUPDATE_INTERVAL_TIME);
                if (NetworkManager.getInstance(mActivity).isConnectedInternet()) {
                    Log.d("ServiceLocation", "Running");
                    serviceLocation.GetMap(mActivity, googleMap);
                }
            }
        };
        mRunnable.run();

        wrapperRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DialogManager.ShowLoading(ActivityTracking.this, "Calling...");
                //socketManager.EmergencyCall();
                try {
                    Context ctx = ActivityTracking.this;
                    DialogManager.DismissLoading(ctx);
                    new MaterialDialog.Builder(ActivityTracking.this)
                            .title("Emrgency Call")
                            .content("Anda yakin menghubungi (021)29568696 ?")
                            .positiveText("OK")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                    callIntent.setData(Uri.parse("tel:02129568696"));
                                    startActivity(callIntent);
                                    dialog.dismiss();
                                }
                            })
                            .negativeText("Cancel")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .icon(getResources().getDrawable(R.drawable.ladyjek_icon))
                            .cancelable(false)
                            .typeface("GothamRnd-Medium.otf", "Gotham.ttf")
                            .show();
                } catch (Exception e) {

                }

            }
        });

    }


    public void drawNewMarker(LatLng latLng, String taglocation) {
        try {
            if (taglocation.equals(TAG_FROM)) {
                markerFrom = googleMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
            } else if (taglocation.equals(TAG_DESTINATION)) {
                markerDestination = googleMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));
            }
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(posFrom, 12);
            googleMap.animateCamera(cameraUpdate);
        } catch (Exception e) {

        }
    }

    private void drawDriveLine(GoogleMap gMap, LatLng pFrom, LatLng pDest) {
        try {
            Document doc = GoogleAPIManager.getRoute(pFrom, pDest, "driving");

            ArrayList<LatLng> directionPoint = GoogleAPIManager.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(15).color(getResources().getColor(R.color.bg_grad_2));

            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }
            gMap.addPolyline(rectLine);
        } catch (Exception e) {

        }
    }


    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));


    }

    @Override
    public void onBackPressed() {
        //finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_transport, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SetActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, AUTOUPDATE_INTERVAL_TIME);
        LocalBroadcastManager.getInstance(this).registerReceiver(start,
                new IntentFilter("goStart"));
        LocalBroadcastManager.getInstance(this).registerReceiver(end,
                new IntentFilter("goEnd"));
        LocalBroadcastManager.getInstance(this).registerReceiver(doEmergency,
                new IntentFilter("doEmergency"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(start);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(end);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(doEmergency);
    }

    @Override
    public void onPause() {
        // Unregister since the activity is not visible
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean isScreenOn = powerManager.isScreenOn();
        mHandler.removeCallbacks(mRunnable);
        if (!isScreenOn) {
            mRunnable.run();
        }
        super.onPause();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
