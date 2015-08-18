package ladyjek.twiscode.com.ladyjek.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ActivityRate extends ActionBarActivity {


    private Toolbar mToolbar;
    private TextView txtPrice, btnSaran;
    private EditText txtFeedback;
    private RelativeLayout btnClearFeedback, wrapperRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        txtPrice = (TextView) findViewById(R.id.txtTotal);
        txtPrice.setText("Total : " + ApplicationData.price);
        txtFeedback = (EditText) findViewById(R.id.feedbackDriver);
        btnClearFeedback = (RelativeLayout) findViewById(R.id.btnFeedback);
        btnSaran = (TextView) findViewById(R.id.btnSaran);
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
                new AlertDialogWrapper.Builder(ActivityRate.this)
                        .setTitle("Terima kasih!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getBaseContext(), Main.class);
                                ApplicationManager um = new ApplicationManager(ActivityRate.this);
                                um.setActivity("ActivityRate");
                                startActivity(i);
                                finish();
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.ladyjek_icon)
                        .show();
            }
        });
        wrapperRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogWrapper.Builder(ActivityRate.this)
                        .setTitle("Terima kasih!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getBaseContext(), Main.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                ApplicationManager um = new ApplicationManager(ActivityRate.this);
                                um.setActivity("ActivityRate");
                                startActivity(i);
                                finish();
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.ladyjek_icon)
                        .show();
            }
        });

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



}
