package ladyjek.twiscode.com.ladyjek.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
<<<<<<< HEAD
import android.widget.TextView;
=======
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
>>>>>>> a622c70fa52ef365192958b06f1068d206ed7e05

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ActivityRate extends ActionBarActivity {


    private Toolbar mToolbar;
    private TextView txtPrice;

    private EditText txtFeedback;
    private RelativeLayout btnClearFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        txtPrice = (TextView)findViewById(R.id.txtTotal);
        txtPrice.setText("Total : "+ ApplicationData.price);

        txtFeedback = (EditText) findViewById(R.id.feedbackDriver);
        btnClearFeedback = (RelativeLayout) findViewById(R.id.btnFeedback);

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

    }

    private void SetActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }



}
