package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;

public class ActivityHandphone extends Activity {

    private Activity act;
    private TextView txtRegisterHandphone ;
    private EditText txtPhoneNumber,txtCountryCode;
    private String countryCode, phNum, strPhoneNumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handphone);

        act = this;
        txtCountryCode = (EditText)findViewById(R.id.txtCountryCode);
        txtRegisterHandphone = (TextView)findViewById(R.id.txtRegisterHandphone);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        countryCode = txtCountryCode.getText().toString();
        phNum = txtPhoneNumber.getText().toString();
        strPhoneNumber = countryCode+phNum;

        txtRegisterHandphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hp = txtPhoneNumber.getText().toString();
                if (hp == null || hp.trim().isEmpty() || hp.length() <6) {
                    DialogManager.showDialog(act, "Warning", "Minimal 6 Character!");
                    txtPhoneNumber.setText("");
                }
                else{

                    new DoPhone(act).execute(
                            ApplicationData.registered_id,
                            "+62"+hp
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DoPhone extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;

        public DoPhone(Activity activity) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Loading. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String id = params[0];
                String phone = params[1];

                /*
                if (email.equals(ApplicationData.user.email) && password.equals(ApplicationData.user.password)) {
                    return "OK";
                }
                */
                JSONControl jsControl = new JSONControl();
                String response = jsControl.postPhone(id, phone);
                Log.d("json response phone",response);
                if(response.contains("true")){
                    return "OK";
                }
                /*
                try {
                    JSONObject _id = response.getJSONObject("_id");
                    if(_id!=null){
                        ApplicationData.registered_id = _id.toString();
                        return "OK";
                    }
                    else {
                        return "FAIL";
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                */





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
                    DialogManager.showDialog(activity, "Warning", "Register Phone Number failed!");
                    break;
                case "OK":
                    Intent i = new Intent(getBaseContext(), ActivityHandphoneKonfirmasi.class);
                    startActivity(i);
                    finish();
                    break;
            }


        }


    }


}

