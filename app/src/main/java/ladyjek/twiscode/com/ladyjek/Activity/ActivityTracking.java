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
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.droidparts.activity.Activity;
import org.w3c.dom.Document;

import java.util.ArrayList;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.GoogleAPIManager;

public class ActivityTracking extends ActionBarActivity implements LocationListener, OnMapReadyCallback {

    private Toolbar mToolbar;
    private GoogleMap googleMap;
    private LatLng posFrom, posDest;
    private Marker markerFrom, markerDestination;
    private final String TAG_FROM = "FROM";
    private final String TAG_DESTINATION = "DESTINATION";
    private ApplicationManager appManager;
    private RelativeLayout wrapperRegister;
    private BroadcastReceiver orderStart,orderEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        wrapperRegister = (RelativeLayout) findViewById(R.id.wrapperRegister);
        appManager = new ApplicationManager(ActivityTracking.this);
        Double latFrom = appManager.getUserFrom().getLatitude();
        Double longFrom = appManager.getUserFrom().getLongitude();
        posFrom = new LatLng(latFrom,longFrom);

        Double latDestination = appManager.getUserDestination().getLatitude();
        Double longDestination = appManager.getUserDestination().getLongitude();
        posDest = new LatLng(latDestination,longDestination);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status!= ConnectionResult.SUCCESS){
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }else {

            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
            googleMap = fm.getMap();
            drawNewMarker(posFrom, TAG_FROM);
            drawNewMarker(posDest, TAG_DESTINATION);
            CircleOptions circleOptions = new CircleOptions()
                    .center(posFrom)
                    .radius(500)
                    .strokeWidth(1)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.parseColor("#500084d3"));
            Circle mapCircle = googleMap.addCircle(circleOptions);
            drawDriveLine(googleMap , posFrom , posDest);
        }

        orderStart  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("orderTaken", message);
                if(message=="true"){
                    Log.d("taken","true");
                    new AlertDialogWrapper.Builder(ActivityTracking.this)
                            .setTitle("order start !!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.drawable.ladyjek_icon)
                            .show();
                }
                else {
                    Log.d("getStart","false");
                }

            }
        };

        orderEnd  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("orderTaken", message);
                if(message=="true"){
                    Log.d("taken","true");
                    new AlertDialogWrapper.Builder(ActivityTracking.this)
                            .setTitle("anda sudah sampai ditujuan !!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(getBaseContext(), ActivityRate.class);
                                    ApplicationManager um = new ApplicationManager(ActivityTracking.this);
                                    //um.setActivity("ActivityTracking");
                                    startActivity(i);
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.drawable.ladyjek_icon)
                            .show();
                }
                else {
                    Log.d("getTimeout","false");
                }

            }
        };

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
        } catch (Exception e){

        }
    }

    private void drawDriveLine(GoogleMap gMap, LatLng pFrom, LatLng pDest){
        try {
            Document doc = GoogleAPIManager.getRoute(pFrom, pDest, "driving");

            ArrayList<LatLng> directionPoint = GoogleAPIManager.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(15).color(getResources().getColor(R.color.bg_grad_2));

            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }
            gMap.addPolyline(rectLine);
        }
        catch (Exception e){

        }
    }



    private void Dummy(){
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                MovetoTracking();
            }
        }.start();
    }

    private void MovetoTracking(){
        new AlertDialogWrapper.Builder(ActivityTracking.this)
                .setTitle("Anda telah selamat sampai tujuan!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=new Intent(getBaseContext(),ActivityRate.class);
                        ApplicationManager um = new ApplicationManager(ActivityTracking.this);
                        um.setActivity("ActivityRate");
                        startActivity(i);
                        finish();
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.ladyjek_icon)
                .show();

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
        // Register mMessageReceiver to receive messages.
        Log.i("adding receiver", "fragment ontainer for profile");
        LocalBroadcastManager.getInstance(ActivityTracking.this).registerReceiver(orderStart,
                new IntentFilter("goStart"));
        LocalBroadcastManager.getInstance(ActivityTracking.this).registerReceiver(orderEnd,
                new IntentFilter("goEnd"));


    }

    @Override
    public void onPause() {
        // Unregister since the activity is not visible
        Log.i("unreg receiver", "fragment unregister");
        LocalBroadcastManager.getInstance(ActivityTracking.this).unregisterReceiver(orderStart);
        LocalBroadcastManager.getInstance(ActivityTracking.this).unregisterReceiver(orderEnd);
        super.onPause();
    }
}
