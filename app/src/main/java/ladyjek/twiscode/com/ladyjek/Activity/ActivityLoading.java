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
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import org.json.JSONObject;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;

public class ActivityLoading extends Activity {

    private ProgressBar mProgressBar;
    private Activity act;
    private ApplicationManager appManager;
    private SocketManager socketManager;
    private BroadcastReceiver orderTaken,orderTimeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBarPickUp);
        act = this;
        appManager = new ApplicationManager(act);
        socketManager = ApplicationData.socketManager;

        orderTimeout  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("orderTimeout", message);
                if(message=="true"){
                    Log.d("timeout","true");
                    new AlertDialogWrapper.Builder(ActivityLoading.this)
                            .setTitle("sesi order sudah habis. silahkan coba lagi !!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(getBaseContext(), ActivityConfirm.class);
                                    ApplicationManager um = new ApplicationManager(ActivityLoading.this);
                                    //um.setActivity("ActivityTracking");
                                    startActivity(i);
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.drawable.ladyjek_icon)
                            .show();
                }
                else {
                    Log.d("getTimeout","false");
                }

            }
        };

        orderTaken  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("orderTaken", message);
                if(message=="true"){
                    Log.d("taken","true");
                    new AlertDialogWrapper.Builder(ActivityLoading.this)
                            .setTitle("order sudah diterima !!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(getBaseContext(), ActivityPickUp.class);
                                    ApplicationManager um = new ApplicationManager(ActivityLoading.this);
                                    //um.setActivity("ActivityTracking");
                                    startActivity(i);
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.drawable.ladyjek_icon)
                            .show();
                }
                else {
                    Log.d("getTimeout","false");
                }

            }
        };

        //Dummy();
    }


    private void MovetoTracking() {
        Intent i = new Intent(getBaseContext(), ActivityPickUp.class);
        ApplicationManager um = new ApplicationManager(ActivityLoading.this);
        um.setActivity("ActivityTracking");
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        //finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(orderTaken,
                new IntentFilter("onOrderTaken"));
        LocalBroadcastManager.getInstance(this).registerReceiver(orderTimeout,
                new IntentFilter("doOrderTimeout"));

    }




    @Override
    public void onPause() {
        // Unregister since the activity is not visible

        LocalBroadcastManager.getInstance(this).unregisterReceiver(orderTaken);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(orderTimeout);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean isScreenOn = powerManager.isScreenOn();

        super.onPause();
    }





}
