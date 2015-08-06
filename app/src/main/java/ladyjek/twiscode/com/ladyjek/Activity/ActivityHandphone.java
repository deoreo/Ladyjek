package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.DetectSoftwareKeyboard;
import ladyjek.twiscode.com.ladyjek.Utilities.Utilities;

public class ActivityHandphone extends Activity {

    private Activity act;
    private TextView txtRegisterHandphone, txtCountryCode;
    private EditText txtPhoneNumber;
    private String countryCode, phNum, strPhoneNumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handphone);

        act = this;
        txtCountryCode = (TextView)findViewById(R.id.txtCountryCode);
        txtRegisterHandphone = (TextView)findViewById(R.id.txtRegisterHandphone);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        countryCode = txtCountryCode.getText().toString();
        phNum = txtPhoneNumber.getText().toString();
        strPhoneNumber = countryCode+phNum;

        txtRegisterHandphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ActivityHandphoneKonfirmasi.class);
                startActivity(i);
                finish();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

