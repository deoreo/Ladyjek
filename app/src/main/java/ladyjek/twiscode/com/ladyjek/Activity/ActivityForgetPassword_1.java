package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.NetworkManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ActivityForgetPassword_1 extends FragmentActivity {

    private EditText txtPhone;
    private RelativeLayout btnKirim;
    private RelativeLayout btnClear;

    ApplicationManager appManager;
    Activity act;

    private String phone;
    private boolean isClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_1);

        appManager = new ApplicationManager(this);
        act = this;

        txtPhone = (EditText) findViewById(R.id.txtPhone);
        btnKirim = (RelativeLayout) findViewById(R.id.wrapperRegister);
        btnClear = (RelativeLayout) findViewById(R.id.btnClear);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClick){
                    if(NetworkManager.getInstance(act).isConnectedInternet()){
                        isClick = true;
                        phone = txtPhone.getText().toString();
                        String num=phone.substring(0,1);
                        Log.d("phone num",num);
                        Log.d("phone",phone);
                        if(num.contains("0")){
                            phone = phone.substring(1);
                            Log.d("phone 1",phone);
                        }
                        new ForgotPassword(act).execute(phone);
                    }
                    else {
                        DialogManager.showDialog(act, "Peringatan", "Tidak ada koneksi internet!");
                    }
                }

            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPhone.setText("");
                btnClear.setVisibility(GONE);
            }
        });

        txtPhone.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() >= 1) {
                    btnClear.setVisibility(VISIBLE);
                } else if (s.length() == 0) {
                    btnClear.setVisibility(GONE);
                }
            }
        });

        txtPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!txtPhone.getText().toString().isEmpty()) {
                        btnClear.setVisibility(VISIBLE);
                    }
                }
                return false;
            }
        });

        txtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    btnClear.setVisibility(GONE);
                } else {
                    btnClear.setVisibility(VISIBLE);
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

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }

    private class ForgotPassword extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;

        public ForgotPassword(Activity activity) {
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

                String phone = params[0];

                JSONControl jsControl = new JSONControl();
                String response = jsControl.postForgotPassword(phone);
                Log.d("json response phone", response);

                if(response.contains("true") && !response.contains("error")){
                    return "OK";
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
            isClick = false;
            switch (result) {
                case "FAIL":
                    DialogManager.showDialog(activity, "Warning", "Register Phone Number failed!");
                    break;
                case "OK":
                    ApplicationData.phoneNumber = phone;
                    Intent i = new Intent(getBaseContext(), ActivityForgetPassword_2.class);
                    startActivity(i);
                    finish();
                    break;
            }


        }


    }


}
