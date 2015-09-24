package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import ladyjek.twiscode.com.ladyjek.Fragment.FragmentHome;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelOrder;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.NetworkManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ActivityRate extends ActionBarActivity {


    private Toolbar mToolbar;
    private TextView txtPrice, txtName, txtJarak, txtWaktu, txtPembayaran, btnSaran;
    private EditText txtFeedback;
    private RelativeLayout btnClearFeedback, wrapperRegister;
    ApplicationManager appManager;
    Activity mActivity;
    ModelOrder order = new ModelOrder();
    SocketManager socketManager;
    private BroadcastReceiver lastOrder, feedback;
    private RatingBar txtRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        mActivity = this;
        appManager = new ApplicationManager(mActivity);
        socketManager = ApplicationData.socketManager;
        txtPrice = (TextView) findViewById(R.id.txtTotal);
        txtName = (TextView) findViewById(R.id.nameDriver);
        txtJarak = (TextView) findViewById(R.id.txtDistance);
        txtWaktu = (TextView) findViewById(R.id.txtDuration);
        txtPembayaran = (TextView) findViewById(R.id.txtMetode);
        //txtPrice.setText("Total : " + ApplicationData.price);
        txtFeedback = (EditText) findViewById(R.id.feedbackDriver);
        btnClearFeedback = (RelativeLayout) findViewById(R.id.btnFeedback);
        btnSaran = (TextView) findViewById(R.id.btnSaran);
        txtRate = (RatingBar) findViewById(R.id.rateDriver);
        wrapperRegister = (RelativeLayout) findViewById(R.id.wrapperRegister);



        txtFeedback.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    btnClearFeedback.setVisibility(GONE);
                } else {
                    if (txtFeedback.getText().length() >= 1) {
                        btnClearFeedback.setVisibility(VISIBLE);
                    } else if (txtFeedback.getText().length() == 0) {
                        btnClearFeedback.setVisibility(GONE);
                    }

                }
            }
        });

        txtFeedback.addTextChangedListener(new TextWatcher() {

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
                    btnClearFeedback.setVisibility(VISIBLE);
                } else if (s.length() == 0) {
                    btnClearFeedback.setVisibility(GONE);
                }
            }
        });

        btnClearFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtFeedback.setText("");
            }
        });

        btnSaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int rate = 0;

                if(txtRate.getRating() > 0 && txtFeedback.getText().length()> 0){
                    rate = Math.round(txtRate.getRating());
                    if(NetworkManager.getInstance(ActivityRate.this).isConnectedInternet()){
                        socketManager.Feedback(rate,txtFeedback.getText().toString());
                    }
                    else {
                        DialogManager.showDialog(ActivityRate.this, "Peringatan", "Tidak ada koneksi internet!");
                    }
                }
                else {
                    DialogManager.showDialog(ActivityRate.this, "Peringatan", "Beri feedback untuk driver!");
                }
            }
        });
        feedback = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("broadcast", "feedback");
                String message = intent.getStringExtra("message");
                DialogManager.DismissLoading(ActivityRate.this);
                if (message.equals("true")) {

                    new AlertDialogWrapper.Builder(ActivityRate.this)
                            .setTitle("Terima kasih!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    appManager.setActivity("");
                                    Intent i = new Intent(getBaseContext(), Main.class);
                                    ApplicationManager um = new ApplicationManager(ActivityRate.this);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    //um.setActivity("ActivityTracking");
                                    startActivity(i);
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.drawable.ladyjek_icon)
                            .show();
                }


            }
        };


        wrapperRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rate = 0;

                if(txtRate.getRating() > 0 && txtFeedback.getText().length()>0){
                    rate = Math.round(txtRate.getRating());
                    if(NetworkManager.getInstance(ActivityRate.this).isConnectedInternet()){
                        DialogManager.ShowLoading(ActivityRate.this , "Loading. . .");
                        socketManager.Feedback(rate,txtFeedback.getText().toString());
                    }
                    else {
                        DialogManager.showDialog(ActivityRate.this, "Peringatan", "Tidak ada koneksi internet!");
                    }
                }
                else {
                    DialogManager.showDialog(ActivityRate.this, "Peringatan", "Beri feedback untuk driver!");
                }
            }
        });

        if(appManager.getOrder()!=null){
            order = appManager.getOrder();
            SetUI();
        }
        else {
            if(NetworkManager.getInstance(mActivity).isConnectedInternet()){
                socketManager.GetLastOrder();
            }
        }

        lastOrder = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("", "broadcast lastOrder");
                String message = intent.getStringExtra("message");
                Intent i = null;
                String pref = appManager.getActivity();
                if (message.equals("true")) {
                    order = ApplicationData.order;
                    SetUI();
                }

            }
        };





    }

    private void SetUI(){
        txtName.setText(ApplicationData.driver.getName());
        txtJarak.setText(order.getDistance());
        txtWaktu.setText(order.getDuration());
        txtPrice.setText(order.getPrice());
        txtPembayaran.setText(order.getPayment());
    }

    private void SetActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void onBackPressed() {
        //finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        Log.i("adding receiver", "fragment ontainer for profile");

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(lastOrder,
                new IntentFilter("lastOrder"));
        LocalBroadcastManager.getInstance(this).registerReceiver(feedback,
                new IntentFilter("doFeedback"));

    }

    @Override
    public void onPause() {
        // Unregister since the activity is not visible
        Log.i("unreg receiver", "fragment unregister");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(feedback);
        super.onPause();
    }


}
