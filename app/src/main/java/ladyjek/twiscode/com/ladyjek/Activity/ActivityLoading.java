package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import org.json.JSONObject;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;

public class ActivityLoading extends Activity {

    private ProgressBar mProgressBar;
    private Activity act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBarPickUp);
        act = this;
        Dummy();
    }
    private void Dummy(){
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                //mProgressBar.setIndeterminate(true);
            }
            public void onFinish() {
                //mProgressBar.setVisibility(View.GONE);
                MovetoTracking();
            }
        }.start();
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





}
