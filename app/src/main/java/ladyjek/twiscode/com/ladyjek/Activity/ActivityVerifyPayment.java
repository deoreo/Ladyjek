package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;

public class ActivityVerifyPayment extends Activity {

    RelativeLayout wrapperPopup;
    private ProgressBar mProgressBar;
    private Activity act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_payment);
        wrapperPopup = (RelativeLayout)findViewById(R.id.wrapperPopup);
        act = this;
        Dummy();
    }
    private void Dummy(){
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                popupFailed();
            }
        }.start();
    }



    @Override
    public void onBackPressed() {
        //finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void popupFailed(){
        LayoutInflater layoutInflater  = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_failed_ecash, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        TextView btnJemput = (TextView)popupView.findViewById(R.id.btnConfirm);
        ImageView btnClose = (ImageView)popupView.findViewById(R.id.btnClose);


        btnJemput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ActivityConfirm.class);
                ApplicationManager um = new ApplicationManager(ActivityVerifyPayment.this);
                startActivity(i);
                finish();
                popupWindow.dismiss();


            }
        });
        btnClose.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getBaseContext(), ActivityConfirm.class);
                ApplicationManager um = new ApplicationManager(ActivityVerifyPayment.this);
                startActivity(i);
                finish();
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(findViewById(R.id.wrapperPopup), Gravity.CENTER, 0, 0);
    }





}
