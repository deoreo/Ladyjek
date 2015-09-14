package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import org.json.JSONObject;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;

public class ActivityHandphoneKonfirmasi extends Activity {

    private Activity act;
    private TextView txtConfirmSmsCode;
    private EditText txtSmsCode;
    private RelativeLayout wrapperRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handphone_konfirmasi);

        act = this;
        txtConfirmSmsCode = (TextView)findViewById(R.id.txtConfirmSmsCode);
        txtSmsCode = (EditText) findViewById(R.id.txtSmsCode);
        wrapperRegister = (RelativeLayout) findViewById(R.id.wrapperRegister);

        if(ApplicationData.editPhone){
            txtConfirmSmsCode.setText(R.string.lanjut);
        }
        else {
            txtConfirmSmsCode.setText(R.string.login);
        }

        txtSmsCode.setText(ApplicationData.phone);



        txtConfirmSmsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApplicationData.editPhone) {
                    finish();
                } else {
                    new DoVerifyCode(act).execute(
                            ApplicationData.token,
                            txtSmsCode.getText().toString()
                    );
                }

            }
        });

        wrapperRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ApplicationData.editPhone){
                    finish();
                }
                else{
                    new DoVerifyCode(act).execute(
                            ApplicationData.token,
                            txtSmsCode.getText().toString()
                    );
                }

            }
        });

    }



    @Override
    public void onBackPressed()
    {
        if(ApplicationData.editPhone){
            ApplicationData.editPhone = false;
        }
        finish();
        super.onBackPressed();  // optional depending on your needs
    }
    private class DoVerifyCode extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;

        public DoVerifyCode(Activity activity) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Verifying. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String code = params[0];

                JSONControl jsonControl = new JSONControl();
                JSONObject objRefreshToken = jsonControl.postRefreshToken(ApplicationData.token);
                String token = objRefreshToken.getString("token");
                String response = jsonControl.postVerifyPhone(token, code);
                Log.d("json response phone", response);
                if(response.equalsIgnoreCase("true")){
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
            switch (result) {
                case "FAIL":
                    DialogManager.showDialog(activity, "Warning", "Verify Phone Number failed!");
                    break;
                case "OK":
                    new AlertDialogWrapper.Builder(ActivityHandphoneKonfirmasi.this)
                            .setTitle("Success register!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(ActivityHandphoneKonfirmasi.this, ActivityLogin.class);
                                    startActivity(i);
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

