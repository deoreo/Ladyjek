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
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONObject;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityLoading extends Activity {

    private ProgressBar mProgressBar;
    private Activity mActivity;
    private ApplicationManager appManager;
    private SocketManager socketManager;
    private BroadcastReceiver orderTaken,orderTimeout, doCancel;
    private ImageView btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBarPickUp);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        mActivity = this;
        appManager = new ApplicationManager(mActivity);
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

                    try {
                        Context ctx = ActivityLoading.this;
                        new MaterialDialog.Builder(ctx)
                                .title("Tidak menemukan driver LadyJek")
                                .positiveText("OK")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        Intent i = new Intent(getBaseContext(), ActivityConfirm.class);
                                        ApplicationManager um = new ApplicationManager(ActivityLoading.this);
                                        startActivity(i);
                                        finish();
                                        dialog.dismiss();
                                    }
                                })
                                .icon(getResources().getDrawable(R.drawable.ladyjek_icon))
                                .cancelable(false)
                                .typeface("GothamRnd-Medium.otf", "Gotham.ttf")
                                .show();
                    } catch (Exception e) {

                    }

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
                    Log.d("taken", "true");
                    try {
                        Context ctx = ActivityLoading.this;
                        new MaterialDialog.Builder(ctx)
                                .title("Driver LadyJek sedang menjemput Anda")
                                .positiveText("OK")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        Intent i = new Intent(getBaseContext(), ActivityPickUp.class);
                                        ApplicationManager um = new ApplicationManager(ActivityLoading.this);
                                        startActivity(i);
                                        finish();
                                        dialog.dismiss();
                                    }
                                })
                                .icon(getResources().getDrawable(R.drawable.ladyjek_icon))
                                .cancelable(false)
                                .typeface("GothamRnd-Medium.otf", "Gotham.ttf")
                                .show();
                    } catch (Exception e) {

                    }

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
                    DialogManager.DismissLoading(context);
                    Intent i = new Intent(context, Main.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(doCancel);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(orderTaken);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(orderTimeout);
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
                DialogManager.ShowLoading(mActivity, "Membatalkan pemesanan...");
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



}
