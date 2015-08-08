package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.User;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.Utilities;

public class ActivityRegister extends ActionBarActivity {

    private EditText txtEmail,txtPassword,txtConfirm;
    private TextView btnRegister;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        act = this;
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirm = (EditText) findViewById(R.id.txtPassword);
        btnRegister = (TextView) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String confirm = txtConfirm.getText().toString();
                if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty() || confirm == null || confirm.trim().isEmpty()) {
                    Utilities.showDialog(act, "Warning", "please fill all data!");
                    txtEmail.setText("");
                    txtPassword.setText("");
                    txtConfirm.setText("");
                } else if (!email.trim().contains("@") ||
                        !email.trim().contains(".") ||
                        email.trim().contains(" ")) {
                    Utilities.showDialog(act, "Warning", "Wrong email format!");
                    txtEmail.setText("");
                    txtPassword.setText("");
                    txtConfirm.setText("");
                } else if (!confirm.equals(password)) {
                    Utilities.showDialog(act, "Warning", "Confirmation password not match!");
                    txtEmail.setText("");
                    txtPassword.setText("");
                    txtConfirm.setText("");
                } else {
                    ApplicationData.user = new User("1","nama kamu",email,password,"",new LatLng(0,0),new LatLng(0,0));
                    Intent i = new Intent(getBaseContext(), ActivityHandphone.class);
                    startActivity(i);
                    finish();
                    /*
                    new DoRegister(act).execute(
                            email,
                            password
                    );
                    */

                }
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



}
