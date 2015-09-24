package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.github.nkzawa.socketio.client.Socket;
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
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelGeocode;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Service.ServiceLocation;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.GoogleAPIManager;
import ladyjek.twiscode.com.ladyjek.Utilities.NetworkManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;

public class ActivityPickUp extends ActionBarActivity implements LocationListener {

    private Toolbar mToolbar;
    private Activity mActivity;
    private GoogleMap googleMap;
    private LatLng posFrom, posDriver, updatedPosition;
    private Marker markerFrom, markerDriver;
    private Button btnCall, btnSMS;
    private TextView txtEstimate, txtName, txtRate, txtNopol, btnCancel;
    private ApplicationManager appManager;
    private final String TAG_FROM = "FROM";
    private final String TAG_DRIVER = "DRIVER";
    private String driverDuration;
    private Boolean isArrive = false;
    private SocketManager socketManager;
    private BroadcastReceiver goTrip, doCancel, driverChange, getDriver, start;
    private Boolean isCancel = false, isPhone = false, isSMS = false;
    private Double latFrom, lonFrom;
    private ServiceLocation serviceLocation;
    //private Runnable mRunnable;
    //private Handler mHandler;
    //private final int AUTOUPDATE_INTERVAL_TIME =  5 * 1000; // 15 detik


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);
        mActivity = this;
        btnCall = (Button) findViewById(R.id.btnCall);
        btnSMS = (Button) findViewById(R.id.btnSMS);
        btnCancel = (TextView) findViewById(R.id.btnCancel);
        txtEstimate = (TextView) findViewById(R.id.txtEstimate);
        txtName = (TextView) findViewById(R.id.nameDriver);
        txtNopol = (TextView) findViewById(R.id.platDriver);
        txtRate = (TextView) findViewById(R.id.rateDriver);
        //mHandler = new Handler();
        appManager = new ApplicationManager(ActivityPickUp.this);
        socketManager = ApplicationData.socketManager;
        serviceLocation = new ServiceLocation();
        if (appManager.getUserFrom() != null) {
            latFrom = appManager.getUserFrom().getLatitude();
            lonFrom = appManager.getUserFrom().getLongitude();
            posFrom = new LatLng(latFrom, lonFrom);
        } else {
            posFrom = serviceLocation.updateLocation(mActivity);
            latFrom = posFrom.latitude;
            lonFrom = posFrom.longitude;
        }

        //posDriver = ApplicationData.posDriver;

        ApplicationManager.getInstance(ActivityPickUp.this).getPosDriver();
        Double lat = ApplicationManager.getInstance(ActivityPickUp.this).getPosDriver().getLatitude();
        Double lon = ApplicationManager.getInstance(ActivityPickUp.this).getPosDriver().getLongitude();
        posDriver = new LatLng(lat, lon);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) {

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else {

            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);

            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();
            appManager = new ApplicationManager(ActivityPickUp.this);
            drawNewMarker(posFrom, TAG_FROM);
            drawNewMarker(posDriver, TAG_DRIVER);
            drawDriveLine(googleMap, posDriver, posFrom);
            drawDriverMarker(googleMap, posFrom, posDriver);
        }

        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPhone) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + ApplicationData.driver.getPhone()));
                    startActivity(callIntent);
                }

            }
        });

        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSMS) {
                    try {

                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.putExtra("address", ApplicationData.driver.getPhone());
                        sendIntent.putExtra("sms_body", "Halo cantik, jemput aku dong !");
                        sendIntent.setType("vnd.android-dir/mms-sms");
                        startActivity(sendIntent);

                    } catch (Exception e) {
                        Log.d("error sms", e.toString());
                    }
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.getInstance(ActivityPickUp.this).isConnectedInternet()) {
                    if (!isCancel) {
                        DialogManager.ShowLoading(ActivityPickUp.this, "Membatalkan pemesanan. . .");
                        socketManager.CancelOrder();
                        isCancel = true;
                    }

                } else {
                    DialogManager.showDialog(ActivityPickUp.this, "Peringatan", "Tidak ada koneksi internet!");
                }
            }
        });


        start = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("start", message);
                if (message == "true") {
                    appManager.setTrip("end");
                    Intent i = new Intent(getBaseContext(), ActivityTracking.class);
                    ApplicationManager um = new ApplicationManager(ActivityPickUp.this);
                    um.setActivity("ActivityTracking");
                    startActivity(i);
                    finish();
                } else {
                    Log.d("cant start", "");
                }


            }
        };


        goTrip = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("goTrip", message);
                if (message == "true") {
                    appManager.setArrive(true);
                    /*
                    Toast.makeText(ActivityPickUp.this, "Driver telah sampai di tempat Anda", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getBaseContext(), ActivityTracking.class);
                    ApplicationManager um = new ApplicationManager(ActivityPickUp.this);
                    um.setActivity("ActivityTracking");
                    startActivity(i);
                    finish();
                    */
                    txtEstimate.setText("Driver telah sampai di tempat Anda");
                    try {
                        Context ctx = ActivityPickUp.this;
                        new AlertDialogWrapper.Builder(ctx)
                                .setTitle("Driver telah sampai di tempat Anda")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getBaseContext(), ActivityTracking.class);
                                        ApplicationManager um = new ApplicationManager(ActivityPickUp.this);
                                        um.setActivity("ActivityTracking");
                                        startActivity(i);
                                        finish();
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(R.drawable.ladyjek_icon)
                                .show();
                    } catch (Exception e) {

                    }
                } else {
                    Log.d("cant goTrip", "");
                }

            }
        };
        doCancel = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                DialogManager.DismissLoading(context);
                String message = intent.getStringExtra("message");
                Log.d("doCancel", message);
                if (message == "true") {
                    Intent i = new Intent(getBaseContext(), Main.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                } else {
                    //Toast.makeText(ActivityPickUp.this, "Anda tidak dapat cancel karena driver sudah dekat", Toast.LENGTH_SHORT).show();
                    try {
                        Context ctx = ActivityPickUp.this;
                        new AlertDialogWrapper.Builder(ctx)
                                .setTitle("Informasi")
                                .setMessage("Driver sudah dekat, silakan menghubungi driver!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(R.drawable.ladyjek_icon)
                                .show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                isCancel = false;

            }
        };

        driverChange = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("driverChange", message);
                if (message == "true") {
                    Log.d("driver change", "true");
                    ApplicationManager.getInstance(ActivityPickUp.this).getPosDriver();
                    Double lat = ApplicationManager.getInstance(ActivityPickUp.this).getPosDriver().getLatitude();
                    Double lon = ApplicationManager.getInstance(ActivityPickUp.this).getPosDriver().getLongitude();
                    posDriver = new LatLng(lat, lon);
                    serviceLocation.GetDriverMarker(googleMap, posDriver);
                } else {
                    Log.d("driver change", "false");
                }

            }
        };

        getDriver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("getDriver", message);
                if (message == "true") {
                    Log.d("getdriver", "true");
                    Log.d("socket", "driver : " + ApplicationData.driver.toString());
                    isSMS = true;
                    isPhone = true;
                    txtName.setText(ApplicationData.driver.getName());
                    txtNopol.setText(ApplicationData.driver.getNopol());
                    txtRate.setText(ApplicationData.driver.getRate());
                } else {
                    Log.d("getdriver", "false");
                }

            }
        };

        if (ApplicationData.driver != null) {
            Log.d("driver", "not null");
            isSMS = true;
            isPhone = true;
            txtName.setText(ApplicationData.driver.getName());
            txtNopol.setText(ApplicationData.driver.getNopol());
            txtRate.setText(ApplicationData.driver.getRate());
        } else {
            Log.d("driver", "null");
            if (NetworkManager.getInstance(ActivityPickUp.this).isConnectedInternet()) {
                socketManager.GetDriver(ApplicationData.order.getDriverID());
            } else {
                DialogManager.showDialog(ActivityPickUp.this, "Peringatan", "Tidak ada koneksi internet!");
            }
        }


    }


    private void MovetoTracking() {


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
    public void onLocationChanged(Location location) {

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

    public void drawNewMarker(LatLng latLng, String taglocation) {
        try {
            if (taglocation.equals(TAG_FROM)) {
                markerFrom = googleMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
            } else if (taglocation.equals(TAG_DRIVER)) {
                markerDriver = googleMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_driver)));
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
            PolylineOptions rectLine = new PolylineOptions().width(15).color(getResources().getColor(R.color.line_blue));

            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }
            gMap.addPolyline(rectLine);
        } catch (Exception e) {

        }
    }

    private void drawDriverMarker(GoogleMap gMap, LatLng pFrom, LatLng pDriver) {
        markerDriver = gMap.addMarker(
                new MarkerOptions()
                        .position(pDriver)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_driver)));
        Document doc = GoogleAPIManager.getRoute(pFrom, pDriver, "driving");
        ArrayList<LatLng> directionPoint = GoogleAPIManager.getDirection(doc);
        driverDuration = "" + GoogleAPIManager.getDurationText(doc);
        txtEstimate.setText("Estimasi waktu menunggu : " + driverDuration);
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
    public void onBackPressed() {
        //finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //mHandler.postDelayed(mRunnable, AUTOUPDATE_INTERVAL_TIME);
        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(goTrip,
                new IntentFilter("goTrip"));
        LocalBroadcastManager.getInstance(this).registerReceiver(start,
                new IntentFilter("goStart"));
        LocalBroadcastManager.getInstance(this).registerReceiver(doCancel,
                new IntentFilter("doCancel"));
        LocalBroadcastManager.getInstance(this).registerReceiver(driverChange,
                new IntentFilter("driverChange"));
        LocalBroadcastManager.getInstance(this).registerReceiver(getDriver,
                new IntentFilter("getDriver"));
    }


    @Override
    public void onPause() {
        // Unregister since the activity is not visible

        //LocalBroadcastManager.getInstance(this).unregisterReceiver(goTrip);
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(doCancel);
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(driverChange);
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(getDriver);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean isScreenOn = powerManager.isScreenOn();
        //mHandler.removeCallbacks(mRunnable);
        //if (!isScreenOn) {
        //   mRunnable.run();
        //}
        super.onPause();
    }


}
