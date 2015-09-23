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
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private BroadcastReceiver orderTaken,orderTimeout, doCancel;
    private ImageView btnCancel;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBarPickUp);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        act = this;
        appManager = new ApplicationManager(act);
        socketManager = ApplicationData.socketManager;

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
            }
        });

        orderTimeout  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("orderTimeout", message);
                if(message=="true"){
                    Log.d("timeout", "true");
                    Toast.makeText(ActivityLoading.this,"Tidak menemukan driver ladyjek. . .", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getBaseContext(), ActivityConfirm.class);
                    ApplicationManager um = new ApplicationManager(ActivityLoading.this);
                    startActivity(i);
                    finish();
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
                    Toast.makeText(ActivityLoading.this, "Driver ladyjek sedang menjemput. . .", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getBaseContext(), ActivityPickUp.class);
                    ApplicationManager um = new ApplicationManager(ActivityLoading.this);
                    startActivity(i);
                    finish();

                } else {
                    Log.d("getTimeout", "false");
                }

            }
        };

        doCancel  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("doCancel", message);
                if(message=="true"){
                    progressDialog.dismiss();
                    Intent i = new Intent(getBaseContext(), Main.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(ActivityLoading.this, "Anda tidak dapat cancel karena driver sudah dekat", Toast.LENGTH_SHORT).show();
                    /*
                    new AlertDialogWrapper.Builder(ActivityLoading.this)
                            .setTitle("Driver sudah dekat !")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.drawable.ladyjek_icon)
                            .show();
                            */
                }

            }
        };

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
        LocalBroadcastManager.getInstance(this).registerReceiver(doCancel,
                new IntentFilter("doCancel"));

    }




    @Override
    public void onPause() {
        // Unregister since the activity is not visible

        //LocalBroadcastManager.getInstance(this).unregisterReceiver(orderTaken);
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(orderTimeout);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean isScreenOn = powerManager.isScreenOn();

        super.onPause();
    }


    private void popup(){
        LayoutInflater layoutInflater  = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_cancel_order, null);
        TextView btnConfirm = (TextView)popupView.findViewById(R.id.btnConfirm);
        ImageView btnClose = (ImageView)popupView.findViewById(R.id.btnClose);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);


        btnConfirm.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLoading();
                socketManager.CancelOrder();


                //popupWindow.dismiss();
            }
        });

        btnClose.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(findViewById(R.id.wrapperPopup), Gravity.CENTER, 0, 0);
    }

    void OpenLoading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading. . .");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }



}
