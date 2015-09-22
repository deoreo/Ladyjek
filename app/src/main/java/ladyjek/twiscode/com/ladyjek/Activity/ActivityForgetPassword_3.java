package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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

import com.afollestad.materialdialogs.AlertDialogWrapper;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.NetworkManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ActivityForgetPassword_3 extends FragmentActivity {

    private TextView txtPass,txtConfirm;
    private RelativeLayout btnKirim;
    private RelativeLayout btnClearConfirm, btnClearPassword;

    ApplicationManager appManager;
    Activity act;

    private boolean isClick = false;
    private String password = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_3);

        appManager = new ApplicationManager(this);
        act = this;

        txtPass = (EditText) findViewById(R.id.txtPassword);
        txtConfirm = (EditText) findViewById(R.id.txtConfirmPassword);
        btnKirim = (RelativeLayout) findViewById(R.id.wrapperRegister);
        btnClearConfirm = (RelativeLayout) findViewById(R.id.btnClearConfirmPassword);
        btnClearPassword = (RelativeLayout) findViewById(R.id.btnClearPassword);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("isClick",""+isClick);
                if(!isClick){

                    if(NetworkManager.getInstance(act).isConnectedInternet()){
                        String pass = txtPass.getText().toString();
                        String confirm = txtConfirm.getText().toString();
                        if(confirm.equals(pass)){
                            isClick = true;
                            password = pass;
                            new ResetPassword(act).execute();
                        }
                        else {
                            DialogManager.showDialog(act, "Peringatan", "konfirmasi password tidak sama!");
                        }
                    }
                    else {
                        DialogManager.showDialog(act, "Peringatan", "Tidak ada koneksi internet!");
                    }
                }

            }
        });

        txtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {

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
                    btnClearConfirm.setVisibility(GONE);
                } else {
                    btnClearConfirm.setVisibility(VISIBLE);
                }
            }
        });

        txtPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnClearConfirm.setVisibility(GONE);
                    if (!txtPass.getText().toString().isEmpty()) {
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
                    btnClearPassword.setVisibility(GONE);
                    if (!txtPass.getText().toString().isEmpty()) {
                        btnClearConfirm.setVisibility(VISIBLE);
                    }
                }
                return false;
            }
        });

        txtPass.addTextChangedListener(new TextWatcher() {

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
                    btnClearPassword.setVisibility(GONE);
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
                    btnClearConfirm.setVisibility(VISIBLE);
                } else if (s.length() == 0) {
                    btnClearConfirm.setVisibility(GONE);
                }
            }
        });

        btnClearConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtConfirm.setText("");
                btnClearConfirm.setVisibility(GONE);
            }
        });
        btnClearPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPass.setText("");
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
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }

    private class ResetPassword extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;

        public ResetPassword(Activity activity) {
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

                JSONControl jsControl = new JSONControl();
                String response = jsControl.postResetPassword(ApplicationData.phoneNumber, ApplicationData.tokenPass, password);
                Log.d("json response phone", response);

                if(response.contains("true")){
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
                    DialogManager.showDialog(activity, "Warning", "Reset password gagal!");
                    break;
                case "OK":
                    ApplicationData.phoneNumber="";
                    ApplicationData.tokenPass="";
                    new AlertDialogWrapper.Builder(act)
                            .setTitle("reset password suksess!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.drawable.ladyjek_icon)
                            .show();
                    break;
            }


        }


    }


}
