package ladyjek.twiscode.com.ladyjek.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import ladyjek.twiscode.com.ladyjek.R;

public class ActivityConfirm extends ActionBarActivity {


    private Toolbar mToolbar;
    private Spinner pay;
    private ArrayAdapter<CharSequence> adapterPay;
    private ImageView btnBack;
    private TextView txtConfirm, txtFrom, txtDestination, txtDistance, txtDuration, txtTotal, txtDetailFrom, txtDetailDestination;
    private String strFrom = "", strDest = "", strDistance = "",
            strDuration = "", strLat = "", strLon = "",
            strDetailFrom = "",strDetailDest="" ;
    private int totalPrice = 0;
    private NumberFormat numberFormat;
    private DecimalFormat decimalFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        decimalFormat = new DecimalFormat("0.00##");
        //SetActionBar();
        SetPaySpinner();
        try {
            Bundle b = getIntent().getExtras();
            strFrom = b.getString("from");
            strDest = b.getString("destination");
            strDetailFrom = b.getString("detailfrom");
            strDetailDest = b.getString("detaildestination");
            strDistance = b.getString("distance");
            strDuration = b.getString("duration");
            strLat = b.getString("lat");
            strLon = b.getString("lon");

            String[] strDist = strDistance.split(" ");
            float intDist = Float.parseFloat(strDist[0]);
            totalPrice = 4000 * Math.round(intDist);

        } catch (Exception e) {

        }

        txtConfirm = (TextView) findViewById(R.id.btnConfirm);
        txtFrom = (TextView) findViewById(R.id.txtFrom);
        txtDestination = (TextView) findViewById(R.id.txtDestination);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtDuration = (TextView) findViewById(R.id.txtDuration);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtDetailFrom = (TextView) findViewById(R.id.txtDetailFrom);
        txtDetailDestination = (TextView) findViewById(R.id.txtDetailDestination);
        btnBack = (ImageView) findViewById(R.id.btnBack);

        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext(), ActivityLoading.class);
                //i.addCategory(Intent.CATEGORY_HOME);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        txtFrom.setText(strFrom);
        txtDestination.setText(strDest);
        txtDetailFrom.setText(strDetailFrom);
        txtDetailDestination.setText(strDetailDest);
        txtDistance.setText(strDistance);
        txtDuration.setText(strDuration);
        txtTotal.setText("Rp " + decimalFormat.format(totalPrice));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void SetActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void SetPaySpinner() {
        pay = (Spinner) findViewById(R.id.txtPay);
        adapterPay = ArrayAdapter.createFromResource(this, R.array.payList, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterPay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        pay.setAdapter(adapterPay);

        pay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else if (position == 1) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }


}
