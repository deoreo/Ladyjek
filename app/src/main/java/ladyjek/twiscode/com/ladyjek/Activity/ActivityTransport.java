package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ladyjek.twiscode.com.ladyjek.R;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityTransport extends FragmentActivity implements LocationListener {

    GoogleMap googleMap;
    Button btnRequestRide;
    private static final double EARTH_RADIUS = 6378100.0;
    private int offset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);
        btnRequestRide = (Button)findViewById(R.id.btnRequestRide);
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available

            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);

            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();

            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);

            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);

            if(location!=null){
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, this);



        }

        btnRequestRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ActivityConfirm.class);
                startActivity(i);
            }
        });


    }


    @Override
    public void onLocationChanged(Location location) {


        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(500)
                .strokeWidth(2)
                .strokeColor(Color.BLUE)
                .fillColor(Color.parseColor("#500084d3"));
        // Supported formats are: #RRGGBB #AARRGGBB
        //   #AA is the alpha, or amount of transparency
       googleMap.addCircle(circleOptions);
        //googleMap.addTileOverlay();
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.green));


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

    private int convertMetersToPixels(double lat, double lng, double radiusInMeters) {

        double lat1 = radiusInMeters / EARTH_RADIUS;
        double lng1 = radiusInMeters / (EARTH_RADIUS * Math.cos((Math.PI * lat / 180)));

        double lat2 = lat + lat1 * 180 / Math.PI;
        double lng2 = lng + lng1 * 180 / Math.PI;

        Point p1 = googleMap.getProjection().toScreenLocation(new LatLng(lat, lng));
        Point p2 = googleMap.getProjection().toScreenLocation(new LatLng(lat2, lng2));

        return Math.abs(p1.x - p2.x);
    }

    private Bitmap getBitmap(int lat, int lng) {

        // fill color
        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setColor(0x110000FF);
        paint1.setStyle(Paint.Style.FILL);

        // stroke color
        Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2.setColor(0xFF0000FF);
        paint2.setStyle(Paint.Style.STROKE);

        // icon
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.blue);

        // circle radius - 200 meters
        int radius = offset = convertMetersToPixels(lat, lng, 200);

        // if zoom too small
        if (radius < icon.getWidth() / 2) {

            radius = icon.getWidth() / 2;
        }

        // create empty bitmap
        Bitmap b = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        // draw blue area if area > icon size
        if (radius != icon.getWidth() / 2) {

            c.drawCircle(radius, radius, radius, paint1);
            c.drawCircle(radius, radius, radius, paint2);
        }

        // draw icon
        c.drawBitmap(icon, radius - icon.getWidth() / 2, radius - icon.getHeight() / 2, new Paint());

        return b;
    }

    private LatLng getCoords(double lat, double lng) {

        LatLng latLng = new LatLng(lat, lng);

        Projection proj = googleMap.getProjection();
        Point p = proj.toScreenLocation(latLng);
        p.set(p.x, p.y + offset);

        MarkerOptions options = new MarkerOptions();
        options.position(getCoords(lat, lng));


        //marker = googleMap.addMarker(options);

        return proj.fromScreenLocation(p);
    }



}
