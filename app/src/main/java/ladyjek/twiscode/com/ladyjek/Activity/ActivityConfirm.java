package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ladyjek.twiscode.com.ladyjek.R;

public class ActivityConfirm extends ActionBarActivity {


    private Toolbar mToolbar;
    Spinner pay;
    ArrayAdapter<CharSequence> adapterPay;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        SetActionBar();
        SetPaySpinner();

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ActivityPickUp.class);
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
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void SetPaySpinner(){
        pay = (Spinner)findViewById(R.id.txtPay);
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







}
