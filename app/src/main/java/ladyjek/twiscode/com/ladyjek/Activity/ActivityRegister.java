package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.User;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;

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
                    DialogManager.showDialog(act, "Warning", "please fill all data!");
                    txtEmail.setText("");
                    txtPassword.setText("");
                    txtConfirm.setText("");
                } else if (!email.trim().contains("@") ||
                        !email.trim().contains(".") ||
                        email.trim().contains(" ")) {
                    DialogManager.showDialog(act, "Warning", "Wrong email format!");
                    txtEmail.setText("");
                    txtPassword.setText("");
                    txtConfirm.setText("");
                } else if (!confirm.equals(password)) {
                    DialogManager.showDialog(act, "Warning", "Confirmation password not match!");
                    txtEmail.setText("");
                    txtPassword.setText("");
                    txtConfirm.setText("");
                } else {
                    /*
                    ApplicationData.user = new User("1","nama kamu",email,password,"",new LatLng(0,0),new LatLng(0,0));
                    Intent i = new Intent(getBaseContext(), ActivityHandphone.class);
                    startActivity(i);
                    finish();
                    */
                    new DoRegister(act).execute(
                            email,
                            password
                    );
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

    private class DoRegister extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;

        public DoRegister(Activity activity) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Signing Up. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String email = params[0];
                String password = params[1];

                /*
                if (email.equals(ApplicationData.user.email) && password.equals(ApplicationData.user.password)) {
                    return "OK";
                }
                */
                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.postRegister(email,password);

                try {
                    JSONObject _id = response.getJSONObject("_id");
                    if(_id!=null){
                        JSONObject _email = response.getJSONObject("email");
                        JSONObject _verified = response.getJSONObject("verified");

                        return "OK";
                    }
                    else {
                        return "FAIL";
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }





            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            switch (result) {
                case "FAIL":
                    DialogManager.showDialog(activity, "Warning", "Sign up Failed!");
                    break;
                case "OK":
                    Intent i = new Intent(getBaseContext(), ActivityHandphone.class);
                    startActivity(i);
                    finish();
                    break;
            }


        }


    }



}
