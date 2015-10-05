package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Database.DatabaseHandler;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelUserOrder;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.KeyboardManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ActivityLogin extends Activity  implements KeyboardManager.Listener {

    private Activity mActivity;
    private TextView btnRegister, btnLogin, btnRegisteronLogin, forgetPassword,txtTitle,lblLogin;
    private EditText txtEmail, txtPassword;
    private RelativeLayout wrapperLogin, wrapperRegister;
    private ModelUserOrder userLogin;
    private ApplicationManager applicationManager;
    private DatabaseHandler db;
    private RelativeLayout btnClearEmail, btnClearPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mActivity = this;

        db = new DatabaseHandler(mActivity);
        btnRegister = (TextView) findViewById(R.id.btnRegister);
        btnLogin = (TextView) findViewById(R.id.btnLogin);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        lblLogin = (TextView) findViewById(R.id.lblLogin);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        wrapperLogin = (RelativeLayout) findViewById(R.id.wrapperLogin);
        wrapperRegister = (RelativeLayout) findViewById(R.id.wrapperRegister);
        btnClearEmail = (RelativeLayout) findViewById(R.id.btnClearEmail);
        btnClearPassword = (RelativeLayout) findViewById(R.id.btnClearPassword);
        btnRegisteronLogin = (TextView) findViewById(R.id.btnRegisteronLogin);
        KeyboardManager mainLayout = (KeyboardManager) findViewById(R.id.layoutLogin);
        mainLayout.setListener(this);

        Typeface bold= Typeface.createFromAsset(getAssets(), "fonts/GothamRnd-Bold.otf");
        Typeface medium= Typeface.createFromAsset(getAssets(), "fonts/GothamRnd-Medium.otf");
        txtTitle.setTypeface(bold);
        lblLogin.setTypeface(bold);
        btnLogin.setTypeface(bold);
        btnRegister.setTypeface(bold);
        txtEmail.setTypeface(medium);
        txtPassword.setTypeface(medium);
        forgetPassword.setTypeface(medium);


        txtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String email = txtEmail.getText().toString();
                if (!email.isEmpty()) {
                    wrapperLogin.setVisibility(VISIBLE);
                    wrapperRegister.setVisibility(GONE);
                }
                if (!hasFocus) {
                    btnClearEmail.setVisibility(GONE);
                } else {
                    btnClearEmail.setVisibility(VISIBLE);
                }
            }
        });

        txtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                wrapperLogin.setVisibility(VISIBLE);
                wrapperRegister.setVisibility(GONE);

                if(!hasFocus){
                    btnClearPassword.setVisibility(GONE);
                }
                else {
                    btnClearPassword.setVisibility(VISIBLE);
                }
            }
        });

        btnRegisteronLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationData.nomorLogin = txtEmail.getText().toString();
                Intent i = new Intent(getBaseContext(), ActivityRegister.class);
                startActivity(i);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationData.nomorLogin = txtEmail.getText().toString();
                Intent i = new Intent(getBaseContext(), ActivityRegister.class);
                startActivity(i);
                finish();
            }
        });

        wrapperRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationData.nomorLogin = txtEmail.getText().toString();
                Intent i = new Intent(getBaseContext(), ActivityRegister.class);
                startActivity(i);
                finish();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ActivityForgetPassword_1.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
                    DialogManager.showDialog(mActivity, "Warning", "Isi Nomor Ponsel dan Password Anda!");
                } else {
                    hideKeyboard();
                    String num=email.substring(0,1);
                    Log.d("phone num",num);
                    Log.d("phone", email);
                    if(num.contains("0")){
                        email = email.substring(1);
                        Log.d("phone 1",email);
                    }
                    new DoLogin(mActivity).execute(
                            email,
                            password
                    );
                }
            }
        });

        wrapperLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
                    DialogManager.showDialog(mActivity, "Warning", "Isi Nomor Ponsel dan Password Anda!");
                } else {
                    hideKeyboard();
                    String num=email.substring(0,1);
                    Log.d("phone num",num);
                    Log.d("phone", email);
                    if(num.contains("0")){
                        email = email.substring(1);
                        Log.d("phone 1",email);
                    }
                    new DoLogin(mActivity).execute(
                            email,
                            password
                    );
                }
            }
        });

        txtEmail.addTextChangedListener(new TextWatcher() {

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
                    btnClearEmail.setVisibility(VISIBLE);
                } else if (s.length() == 0) {
                    btnClearEmail.setVisibility(GONE);
                }
            }
        });
        txtPassword.addTextChangedListener(new TextWatcher() {

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
                    btnClearPassword.setVisibility(VISIBLE);
                }
                else if(s.length()==0){
                    btnClearPassword.setVisibility(GONE);
                }
            }
        });
        txtEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnClearPassword.setVisibility(GONE);
                    if(!txtEmail.getText().toString().isEmpty()){
                        btnClearEmail.setVisibility(VISIBLE);
                    }
                }
                return false;
            }
        });
        txtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnClearEmail.setVisibility(GONE);
                    if(!txtPassword.getText().toString().isEmpty()){
                        btnClearPassword.setVisibility(VISIBLE);
                    }
                }
                return false;
            }
        });

        btnClearEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail.setText("");
                btnClearEmail.setVisibility(GONE);
            }
        });
        btnClearPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPassword.setText("");
                btnClearPassword.setVisibility(GONE);
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
    public void onSoftKeyboardShown(boolean isShowing) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        if (isShowing || !email.isEmpty()|| !password.isEmpty()) {
            wrapperLogin.setVisibility(VISIBLE);
            wrapperRegister.setVisibility(GONE);
        } else {
            wrapperLogin.setVisibility(GONE);
            wrapperRegister.setVisibility(VISIBLE);
        }
    }





    public void hideKeyboard(){
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private class DoLogin extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;

        public DoLogin(Activity activity) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Signing in. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ApplicationManager appManager = new ApplicationManager(activity);
                String phoneNumber = params[0];
                String password = params[1];

                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.postLogin(phoneNumber,password);
                Log.d("json response", response.toString());
                JSONObject responseUser = response.getJSONObject("user");
                String token = response.getString("token");
                appManager.setUserToken(token);


                try {
                    String _verified = responseUser.getString("verified");
                    String _id = responseUser.getString("_id");
                    Log.d("json response id",_id.toString());
                    if(_id!=null && _verified.equals("true")){
                        String name = responseUser.getString("name");
                        String email = "";
                        try {
                            email = responseUser.getString("email");
                        }
                        catch (Exception ex){
                            email="";
                        }
                        JSONObject rumah = responseUser.getJSONObject("houseGeo");

                        JSONArray latlgRumah = rumah.getJSONArray("coordinates");
                        String latRumah = latlgRumah.getString(1);
                        String lonRumah = latlgRumah.getString(0);
                        String addressRumah = "";
                        if(!latRumah.equals("0") && !lonRumah.equals("0")) {
                            addressRumah = getAddress(new LatLng(Double.parseDouble(latRumah), Double.parseDouble(lonRumah)));
                        }
                        JSONObject kantor = responseUser.getJSONObject("officeGeo");

                        JSONArray latlgKantor = kantor.getJSONArray("coordinates");
                        String latKantor = latlgKantor.getString(1);
                        String lonKantor = latlgKantor.getString(0);
                        String addressKantor = "";
                        if(!latKantor.equals("0") && !lonKantor.equals("0")) {
                            addressKantor = getAddress(new LatLng(Double.parseDouble(latKantor), Double.parseDouble(lonKantor)));
                        }
                        JSONArray phNumbers = responseUser.getJSONArray("phoneNumbers");
                        String hp2nd="";
                        if(phNumbers.length()==2){
                            hp2nd = phNumbers.getString(1).substring(3);
                        }
                        userLogin = new ModelUserOrder();
                        userLogin.setPhone(phoneNumber);
                        userLogin.setPassword(password);
                        userLogin.setId(_id);
                        userLogin.setEmail(email);
                        userLogin.setName(name);
                        userLogin.setAddressHome(addressRumah);
                        userLogin.setHomeLat(latRumah);
                        userLogin.setHomeLon(lonRumah);
                        userLogin.setRumah(new LatLng(Double.parseDouble(latRumah), Double.parseDouble(lonRumah)));
                        userLogin.setAddressOffice(addressKantor);
                        userLogin.setOfficeLat(latKantor);
                        userLogin.setOfficeLat(lonKantor);
                        userLogin.setKantor(new LatLng(Double.parseDouble(latKantor), Double.parseDouble(lonKantor)));
                        userLogin.setPhone2nd(hp2nd);
                        db.insertUser(userLogin);
                        appManager.setUser(userLogin);
                        ApplicationData.login_id = _id.toString();
                        ApplicationData.userLogin = userLogin;


                        String deviceToken = ApplicationData.PARSE_DEVICE_TOKEN;
                        ApplicationManager.getInstance(context).setUserToken(token);
                        ApplicationData.registered_id = _id.toString();
                        JSONControl jsonControl = new JSONControl();
                        JSONObject objRefreshToken = jsonControl.postRefreshToken(ApplicationManager.getInstance(context).getUserToken());
                        Log.d("refresh token", objRefreshToken.toString());
                        String refreshToken = objRefreshToken.getString("token");
                        ApplicationManager.getInstance(context).setUserToken(refreshToken);
                        String responseDeviceToken = jsonControl.postDeviceToken(refreshToken, deviceToken);
                        Log.d("json response phone", responseDeviceToken);

                        if(!responseDeviceToken.contains("jwt") && !responseDeviceToken.contains("error")){
                            return "OK";
                        }
                    }
                    else if(!_verified.contains("true")){
                        return "VERIFY";
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
                    new AlertDialogWrapper.Builder(activity)
                            .setTitle("Anda belum terdaftar, silakan mendaftar")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    ApplicationData.nomorLogin = txtEmail.getText().toString();
                                    Intent i = new Intent(getBaseContext(), ActivityRegister.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .setIcon(R.drawable.ladyjek_icon)
                            .show();

                    break;
                case "VERIFY":
                    Intent verify = new Intent(getBaseContext(), ActivityHandphoneKonfirmasi.class);
                    startActivity(verify);
                    finish();
                    break;
                case "OK":
                    new CheckPromo(activity).execute();
                    break;
            }


        }


    }

    public String getAddress(LatLng latlng) {
        Geocoder geocoder = new Geocoder(mActivity, Locale.getDefault());
        double lat = latlng.latitude;
        double lng = latlng.longitude;
        String addressLine = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            addressLine = obj.getAddressLine(0);

        } catch (IOException e) {
        } catch (Exception e) {
        }
        return addressLine;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private class CheckPromo extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;

        public CheckPromo(Activity activity) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.postPromo();
                Log.d("json promo", response.toString());
                if(response!=null){
                    try{
                        String url = response.getString("url");
                        ApplicationData.promo_url = url;
                        ApplicationData.promo_images = new JSONArray();
                        return "OK";
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        try{
                            JSONArray url = response.getJSONArray("images");
                            ApplicationData.promo_images = url;
                            ApplicationData.promo_url = "";
                            return "OK";
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Intent i = new Intent();
            switch (result) {
                case "OK":
                    i = new Intent(getBaseContext(), ActivityPromoWebView.class);
                    startActivity(i);
                    finish();
                    break;
                case "FAIL":
                    i = new Intent(getBaseContext(), Main.class);
                    startActivity(i);
                    finish();
                    break;
            }
        }


    }



}

