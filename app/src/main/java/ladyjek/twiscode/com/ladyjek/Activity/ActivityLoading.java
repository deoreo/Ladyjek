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
import android.widget.ProgressBar;

import org.json.JSONObject;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelUser;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;

public class ActivityLoading extends Activity {

    private ProgressBar mProgressBar;
    private int mWaited = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBarPickUp);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i <= 1000; i++) {
                        sleep(20);
                        mProgressBar.setProgress(mWaited/10);
                        mWaited+=1;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                        Intent i = new Intent(getBaseContext(), ActivityPickUp.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    finish();


                }
            }
        };
        splashThread.start();


        }

    private void Dummy(){
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(5*1000);
                    mProgressBar.setIndeterminate(true);
                    Intent i=new Intent(getBaseContext(),ActivityPickUp.class);
                    startActivity(i);
                    finish();

                } catch (Exception e) {
                }
            }
        };


        background.start();
    }

    @Override
    public void onBackPressed() {
        finish();

    }

    private class GetDriver extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;

        public GetDriver(Activity activity) {
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

                String email = params[0];
                String password = params[1];

                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.postLogin(email,password);
                JSONObject responseUser = response.getJSONObject("driver");
                Log.d("json response", response.toString());
                try {
                    String _id = responseUser.getString("_id");
                    Log.d("json response id",_id.toString());
                    if(_id!=null){
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
                    DialogManager.showDialog(activity, "Warning", "");
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
