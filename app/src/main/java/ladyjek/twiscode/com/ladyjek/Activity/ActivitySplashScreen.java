package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Database.DatabaseHandler;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.ConfigManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivitySplashScreen extends Activity {

    private DatabaseHandler db;
    private ProgressBar mProgressBar;
    private int mWaited = 0;
    private SocketManager socketManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        socketManager = new SocketManager();
        db = new DatabaseHandler(this);
        mProgressBar = (ProgressBar) findViewById(R.id.splash_progress);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i <= 1000; i++) {
                        sleep(2);
                        mProgressBar.setProgress(mWaited/10);
                        mWaited+=1;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    //CheckLogin();
                    new CheckVersion().execute();



                }
            }
        };
        splashThread.start();
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    void GoHome(){
        ApplicationManager um = new ApplicationManager(ActivitySplashScreen.this);
        String pref = um.getActivity();
        Intent i = new Intent(getBaseContext(), Main.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private class CheckPromo extends AsyncTask<String, Void, String> {

        public CheckPromo() {
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
            switch (result) {
                case "OK":
                    Intent i = new Intent(getBaseContext(), ActivityPromoWebView.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    overridePendingTransition(0, 0);

                    //finish();
                    break;
                case "FAIL":
                    Intent y = new Intent(getBaseContext(), Main.class);
                    startActivity(y);
                    //finish();
                    break;
            }
        }


    }

    private class CheckVersion extends AsyncTask<String, Void, String> {

        public CheckVersion() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.getVersion();
                Log.d("json version", response.toString());

                try{
                    String url = response.getJSONObject("popup").getString("url");
                    ApplicationData.promo_url = url;
                    ApplicationData.promo_images = new JSONArray();
                    Log.d("json promo url", url);
                }
                catch (Exception e){
                    e.printStackTrace();
                    try{
                        JSONArray url = response.getJSONObject("popup").getJSONArray("images");
                        Log.d("json promo images", url.toString());
                        ApplicationData.promo_images = url;
                        ApplicationData.promo_url = "";
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                int version = Integer.parseInt(response.getJSONObject("version").getString("android"));
                Log.d("json version",Integer.toString(version));
                String value = "false";
                if(ConfigManager.VERSION < version){
                    value ="true";
                }
                return value;


            } catch (Exception e) {
                e.printStackTrace();
            }
            return "error";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            switch (result) {
                case "true":
                    try {
                        new MaterialDialog.Builder(ActivitySplashScreen.this)
                                .title("Silahkan update aplikasi LadyJek versi terbaru!")
                                .positiveText("OK")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                        }
                                    }
                                })
                                .icon(getResources().getDrawable(R.drawable.ladyjek_icon))
                                .cancelable(false)
                                .typeface("GothamRnd-Medium.otf", "Gotham.ttf")
                                .show();
                    }catch(Exception e){

                    }
                    //finish();
                    break;
                case "false":
                    CheckLogin();
                    break;
            }
        }


    }

    void CheckLogin(){
        int countUser = db.getUserCount();
        if(countUser > 0) {

            //new CheckPromo().execute();
            Intent i = new Intent(getBaseContext(), ActivityPromoWebView.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            overridePendingTransition(0, 0);


        }
        else{
            Intent i = new Intent(getBaseContext(), ActivityLogin.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        finish();
    }
}
