package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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

import org.json.JSONArray;
import org.json.JSONObject;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Database.DatabaseHandler;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelUserOrder;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.KeyboardManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ActivityLogin extends Activity  implements KeyboardManager.Listener {

    private Activity mActivity;
    private TextView btnRegister, btnLogin;
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
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        wrapperLogin = (RelativeLayout) findViewById(R.id.wrapperLogin);
        wrapperRegister = (RelativeLayout) findViewById(R.id.wrapperRegister);
        btnClearEmail = (RelativeLayout) findViewById(R.id.btnClearEmail);
        btnClearPassword = (RelativeLayout) findViewById(R.id.btnClearPassword);

        KeyboardManager mainLayout = (KeyboardManager) findViewById(R.id.layoutLogin);
        mainLayout.setListener(this);

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

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationData.nomorLogin = txtEmail.getText().toString();
                Intent i = new Intent(getBaseContext(), ActivityRegister.class);
                startActivity(i);
            }
        });

        wrapperRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationData.nomorLogin = txtEmail.getText().toString();
                Intent i = new Intent(getBaseContext(), ActivityRegister.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
                    DialogManager.showDialog(mActivity, "Warning", "Isi Email dan Password!");
                } else {
                    hideKeyboard();
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
                    DialogManager.showDialog(mActivity, "Warning", "Isi Email dan Password!");
                } else {
                    hideKeyboard();
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
                JSONObject responseUser = response.getJSONObject("user");
                String token = response.getString("token");
                appManager.setUserToken(token);

                Log.d("json response", response.toString());
                Log.d("json response",token );
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
                        JSONObject kantor = responseUser.getJSONObject("officeGeo");
                        JSONArray latlgKantor = rumah.getJSONArray("coordinates");
                        String latKantor = latlgKantor.getString(1);
                        String lonKantor = latlgKantor.getString(0);

                        userLogin = new ModelUserOrder();
                        userLogin.setPhone(phoneNumber);
                        userLogin.setPassword(password);
                        userLogin.setId(_id);
                        userLogin.setEmail(email);
                        userLogin.setName(name);
                        userLogin.setHomeLat(latRumah);
                        userLogin.setHomeLon(lonRumah);
                        userLogin.setOfficeLat(latKantor);
                        userLogin.setOfficeLat(lonKantor);
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
                    Intent i = new Intent(getBaseContext(), Main.class);
                    startActivity(i);
                    finish();
                    break;
            }


        }


    }

}

