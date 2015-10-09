package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONObject;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Parse.ParseManager;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.NetworkManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ActivityRegister extends ActionBarActivity {

    private EditText txtPhone,txtPassword,txtConfirm, txtName;
    private TextView btnRegister;
    private RelativeLayout btnClearEmail,btnClearName, btnClearPassword, btnClearConfirmPassword, wrapperRegister;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        act = this;
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirm = (EditText) findViewById(R.id.txtConfirmPassword);
        txtName = (EditText) findViewById(R.id.txtName);
        btnRegister = (TextView) findViewById(R.id.btnRegister);
        btnClearEmail = (RelativeLayout) findViewById(R.id.btnClearEmail);
        btnClearName = (RelativeLayout) findViewById(R.id.btnClearName);
        btnClearPassword = (RelativeLayout) findViewById(R.id.btnClearPassword);
        btnClearConfirmPassword = (RelativeLayout) findViewById(R.id.btnClearConfirmPassword);
        wrapperRegister = (RelativeLayout) findViewById(R.id.wrapperRegister);

        txtPhone.setText(ApplicationData.nomorLogin);



        wrapperRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtName.getText().toString();
                String phoneNumber = txtPhone.getText().toString();
                String password = txtPassword.getText().toString();
                String confirm = txtConfirm.getText().toString();
                if (phoneNumber == null || password == null || phoneNumber.trim().isEmpty() || password.trim().isEmpty() || confirm == null || confirm.trim().isEmpty() || name == null || name.trim().isEmpty()) {
                    DialogManager.showDialog(act, "Warning", "Masukkan semua data Anda!");
                } else if (!confirm.equals(password)) {
                    DialogManager.showDialog(act, "Warning", "Password tidak sesuai!");
                } else {
                    String num = phoneNumber.substring(0, 1);
                    Log.d("phone num", num);
                    Log.d("phone", phoneNumber);
                    if (num.contains("0")) {
                        Log.d("phone 1", phoneNumber);
                        /*
                        phoneNumber = phoneNumber.substring(1);

                        */
                        DialogManager.showDialog(act,"Informasi","Masukkan nomor ponsel seperti berikut : 085959084701");

                    }
                    else {
                        phoneNumber = "8"+phoneNumber;
                        new DoRegister(act).execute(
                                name,
                                phoneNumber,
                                password
                        );
                    }

                }

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
                    btnClearEmail.setVisibility(VISIBLE);
                } else if (s.length() == 0) {
                    btnClearEmail.setVisibility(GONE);
                    btnClearConfirmPassword.setVisibility(GONE);
                    btnClearName.setVisibility(GONE);
                }
            }
        });


        txtName.addTextChangedListener(new TextWatcher() {

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
                    btnClearName.setVisibility(VISIBLE);
                } else if (s.length() == 0) {
                    btnClearEmail.setVisibility(GONE);
                    btnClearConfirmPassword.setVisibility(GONE);
                    btnClearName.setVisibility(GONE);
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
                } else if (s.length() == 0) {
                    btnClearEmail.setVisibility(GONE);
                    btnClearConfirmPassword.setVisibility(GONE);
                    btnClearName.setVisibility(GONE);
                }
            }
        });
        txtConfirm.addTextChangedListener(new TextWatcher() {

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
                    btnClearConfirmPassword.setVisibility(VISIBLE);
                }
                else if(s.length()==0){
                    btnClearEmail.setVisibility(GONE);
                    btnClearPassword.setVisibility(GONE);
                    btnClearName.setVisibility(GONE);
                }
            }
        });

        btnClearEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPhone.setText("");
                btnClearEmail.setVisibility(GONE);
                btnClearConfirmPassword.setVisibility(GONE);
                btnClearName.setVisibility(GONE);
            }
        });
        btnClearName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtName.setText("");
                btnClearEmail.setVisibility(GONE);
                btnClearConfirmPassword.setVisibility(GONE);
                btnClearName.setVisibility(GONE);
            }
        });

        btnClearPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPassword.setText("");
                btnClearPassword.setVisibility(GONE);
                btnClearEmail.setVisibility(GONE);
                btnClearName.setVisibility(GONE);
            }
        });
        btnClearConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtConfirm.setText("");
                btnClearConfirmPassword.setVisibility(GONE);
                btnClearEmail.setVisibility(GONE);
                btnClearName.setVisibility(GONE);
            }
        });

        txtPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnClearPassword.setVisibility(GONE);
                    btnClearConfirmPassword.setVisibility(GONE);
                    btnClearName.setVisibility(GONE);
                    if (!txtPhone.getText().toString().isEmpty()) {
                        btnClearEmail.setVisibility(VISIBLE);
                    }
                }
                return false;
            }
        });
        txtName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnClearPassword.setVisibility(GONE);
                    btnClearConfirmPassword.setVisibility(GONE);
                    btnClearEmail.setVisibility(GONE);
                    if (!txtName.getText().toString().isEmpty()) {
                        btnClearName.setVisibility(VISIBLE);
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
                    btnClearConfirmPassword.setVisibility(GONE);
                    btnClearName.setVisibility(GONE);
                    if (!txtPassword.getText().toString().isEmpty()) {
                        btnClearPassword.setVisibility(VISIBLE);
                    }
                }
                return false;
            }
        });
        txtConfirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnClearEmail.setVisibility(GONE);
                    btnClearPassword.setVisibility(GONE);
                    btnClearName.setVisibility(GONE);
                    if (!txtPassword.getText().toString().isEmpty()) {
                        btnClearConfirmPassword.setVisibility(VISIBLE);
                    }
                }
                return false;
            }
        });

        txtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

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
                if (!hasFocus) {
                    btnClearPassword.setVisibility(GONE);
                } else {
                    btnClearPassword.setVisibility(VISIBLE);
                }
            }
        });

        txtConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    btnClearConfirmPassword.setVisibility(GONE);
                } else {
                    btnClearConfirmPassword.setVisibility(VISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), ActivityLogin.class);
        startActivity(i);
        finish();

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
        String error = "";

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

                String name = params[0];
                String phoneNumber = params[1];
                String password = params[2];
                JSONControl jsControl = new JSONControl();
                JSONObject responseRegister = jsControl.postRegister(name,phoneNumber, password);
                Log.d("json responseRegister", responseRegister.toString());
                try {
                    String token = responseRegister.getString("token");
                    JSONObject user = responseRegister.getJSONObject("user");
                    String _id = user.getString("_id");
                    Log.d("json response token",token);
                    Log.d("json response id",_id.toString());
                    if (!token.isEmpty() && _id != null) {

                        String deviceToken = ApplicationData.PARSE_DEVICE_TOKEN;
                        ApplicationManager.getInstance(context).setUserToken(token);
                        ApplicationData.registered_id = _id.toString();
                        JSONControl jsonControl = new JSONControl();
                        JSONObject objRefreshToken = jsonControl.postRefreshToken(ApplicationManager.getInstance(context).getUserToken());
                        Log.d("refresh token", objRefreshToken.toString());
                        String refreshToken = objRefreshToken.getString("token");
                        ApplicationManager.getInstance(context).setUserToken(refreshToken);

                        String response = jsonControl.postDeviceToken(refreshToken, deviceToken);
                        Log.d("json response phone", response);
                        if(response.contains("true") && !response.contains("jwt") && !response.contains("error")){
                            return "OK";
                        }
                        else{
                            return "FAIL";
                        }

                    }
                    else {
                        return "FAIL";
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    error = responseRegister.getJSONObject("message").getJSONObject("phoneNumbers").getString("message");
                    if(error.contains("already exist")){
                        error = "nomor telah terdaftar. Silahkan login!";
                    }
                    Log.d("error", error);
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
                    if(error==""){
                        DialogManager.showDialog(activity, "Warning", "Request Time Out!");
                    }
                    else {
                        try {
                            Context ctx = ActivityRegister.this;
                            new MaterialDialog.Builder(ActivityRegister.this)
                                    .title(error)
                                    .positiveText("OK")
                                    .callback(new MaterialDialog.ButtonCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            //dialog.dismiss();
                                            Intent i = new Intent(getBaseContext(), ActivityLogin.class);
                                            startActivity(i);
                                            finish();

                                        }
                                    })
                                    .icon(getResources().getDrawable(R.drawable.ladyjek_icon))
                                    .cancelable(false)
                                    .typeface("GothamRnd-Medium.otf", "Gotham.ttf")
                                    .show();
                        }catch(Exception e){

                        }
                    }

                    break;
                case "OK":
                    Intent i = new Intent(getBaseContext(), ActivityHandphoneKonfirmasi.class);
                    startActivity(i);
                    finish();
                    break;
            }
        }


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
