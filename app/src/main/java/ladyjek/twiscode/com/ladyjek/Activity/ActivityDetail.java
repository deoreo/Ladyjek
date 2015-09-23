package ladyjek.twiscode.com.ladyjek.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelHistory;
import ladyjek.twiscode.com.ladyjek.R;

public class ActivityDetail extends FragmentActivity {

    private ImageView btnBack;
    ModelHistory history = new ModelHistory();
    private TextView txtFrom, txtDestination, txtDistance, txtDuration, txtTotal, txtDetailFrom, txtDetailDestination,txtPayment;
    private String strFrom = "", strDest = "", strDistance = "",
            strDuration = "", strDetailFrom = "",strDetailDest="",strPayment="" ;
    private int totalPrice = 0;
    private NumberFormat numberFormat;
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(otherSymbols);

        history = ApplicationData.detail;
        btnBack = (ImageView) findViewById(R.id.btnBack);
        txtPayment = (TextView) findViewById(R.id.txtPayment);
        txtFrom = (TextView) findViewById(R.id.txtFrom);
        txtDestination = (TextView) findViewById(R.id.txtDestination);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtDuration = (TextView) findViewById(R.id.txtDuration);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtDetailFrom = (TextView) findViewById(R.id.txtDetailFrom);
        txtDetailDestination = (TextView) findViewById(R.id.txtDetailDestination);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        try {
            strFrom = history.getFrom();
            strDest = history.getDestination();
            strDistance = history.getDistance();
            strDuration = history.getDuration();
            strPayment = history.getPayment();
            float intDist = Float.parseFloat(history.getPrice());
            totalPrice = Integer.parseInt(history.getPrice());

        } catch (Exception e) {

        }

        String[] from = strFrom.split(", ");
        String[] dest = strDest.split(", ");

        txtFrom.setText(from[0]);
        txtDestination.setText(dest[0]);
        txtDetailFrom.setText(from[1]+", "+from[2]+", "+from[3]+", "+from[4]);
        txtDetailDestination.setText(dest[1]+", "+dest[2]+", "+dest[3]+", "+dest[4]);
        txtDistance.setText(strDistance);
        txtDuration.setText(strDuration);
        txtTotal.setText("Rp. " + decimalFormat.format(totalPrice));









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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }


}
