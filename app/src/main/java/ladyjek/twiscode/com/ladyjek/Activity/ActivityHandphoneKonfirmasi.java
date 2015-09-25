package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import org.json.JSONObject;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.NetworkManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ActivityHandphoneKonfirmasi extends Activity {

    private Activity act;
    private TextView txtConfirmSmsCode,resendCode;
    private EditText txtSmsCode1;
    private EditText txtSmsCode2;
    private EditText txtSmsCode3;
    private EditText txtSmsCode4;
    private EditText txtSmsCode5;
    private EditText txtSmsCode6;
    String txtSmsCode;
    private RelativeLayout wrapperRegister;
    private BroadcastReceiver smsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handphone_konfirmasi2);

        smsCode = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String messageCode = intent.getStringExtra("messageCode");
                txtSmsCode1.setText(messageCode.substring(0));
                txtSmsCode2.setText(messageCode.substring(1));
                txtSmsCode3.setText(messageCode.substring(2));
                txtSmsCode4.setText(messageCode.substring(3));
                txtSmsCode5.setText(messageCode.substring(4));
                txtSmsCode6.setText(messageCode.substring(5));
                txtSmsCode = messageCode;
            }
        };



        act = this;
        txtConfirmSmsCode = (TextView)findViewById(R.id.txtConfirmSmsCode);
        resendCode = (TextView)findViewById(R.id.resendCode);
        txtSmsCode1 = (EditText) findViewById(R.id.txtSmsCode1);
        txtSmsCode2 = (EditText) findViewById(R.id.txtSmsCode2);
        txtSmsCode3 = (EditText) findViewById(R.id.txtSmsCode3);
        txtSmsCode4 = (EditText) findViewById(R.id.txtSmsCode4);
        txtSmsCode5 = (EditText) findViewById(R.id.txtSmsCode5);
        txtSmsCode6 = (EditText) findViewById(R.id.txtSmsCode6);
        wrapperRegister = (RelativeLayout) findViewById(R.id.wrapperRegister);

        if(ApplicationData.editPhone){
            txtConfirmSmsCode.setText(R.string.lanjut);
        }
        else {
            txtConfirmSmsCode.setText(R.string.login);
        }

        txtConfirmSmsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApplicationData.editPhone) {
                    finish();
                } else {
                    new DoVerifyCode(act).execute(
                            txtSmsCode
                    );
                }

            }
        });

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkManager.getInstance(ActivityHandphoneKonfirmasi.this).isConnectedInternet()){
                    new DoResendVerifyCode(act).execute();
                }
                else {
                    DialogManager.showDialog(ActivityHandphoneKonfirmasi.this, "Peringatan", "Tidak ada koneksi internet!");
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
                            txtSmsCode
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
                JSONObject objRefreshToken = jsonControl.postRefreshToken(ApplicationManager.getInstance(context).getUserToken());
                Log.d("refresh token", objRefreshToken.toString());
                String token = objRefreshToken.getString("token");
                ApplicationManager.getInstance(context).setUserToken(token);
                String response = jsonControl.postVerifyPhone(token, code);
                Log.d("json response phone", response);
                if(response.contains("true") && !response.contains("jwt") && !response.contains("error")){
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

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(smsCode,
                new IntentFilter("smsCode"));
    }


    private class DoResendVerifyCode extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;

        public DoResendVerifyCode(Activity activity) {
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


                JSONControl jsonControl = new JSONControl();
                JSONObject objRefreshToken = jsonControl.postRefreshToken(ApplicationManager.getInstance(context).getUserToken());
                Log.d("refresh token", objRefreshToken.toString());
                String token = objRefreshToken.getString("token");
                ApplicationManager.getInstance(context).setUserToken(token);
                String response = jsonControl.postResendVerifyCode(token);
                Log.d("json response verify", response);
                if(response.contains("true") && !response.contains("jwt") && !response.contains("error")){
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
                    DialogManager.showDialog(activity, "Warning", "Resend verify code gagal!");
                    break;
                case "OK":
                    new AlertDialogWrapper.Builder(ActivityHandphoneKonfirmasi.this)
                            .setTitle("Sukses Resend verify code!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.drawable.ladyjek_icon)
                            .show();
                    break;
            }


        }


    }

    private void readSMSCode(Intent intent) {
        final SmsManager sms = SmsManager.getDefault();
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);


                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(act,
                            "senderNum: " + senderNum + ", message: " + message, duration);
                    toast.show();

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}

