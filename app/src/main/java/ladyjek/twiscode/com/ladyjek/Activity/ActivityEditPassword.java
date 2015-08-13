package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ActivityEditPassword extends Activity {


    private Activity act;
    private ImageView btnBack;
    private TextView btnSimpan;
    private EditText oldpass,newpass,confirmpass;
    private RelativeLayout btnClearOldPass, btnClearNewPass, btnClearConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        act = this;

        btnSimpan = (TextView) findViewById(R.id.btnSimpan);

        oldpass = (EditText) findViewById(R.id.txtSandiLama);
        newpass = (EditText) findViewById(R.id.txtSandiBaru);
        confirmpass = (EditText) findViewById(R.id.txtKonfirmasiSandi);

        btnClearOldPass = (RelativeLayout) findViewById(R.id.btnOldPass);
        btnClearNewPass = (RelativeLayout) findViewById(R.id.btnNewPass);
        btnClearConfirmPass = (RelativeLayout) findViewById(R.id.btnConfirmPass);

        btnBack = (ImageView) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String opass = oldpass.getText().toString();
                String npass = newpass.getText().toString();
                String cpass = confirmpass.getText().toString();
                if (opass == null || npass == null || cpass == null || opass.trim().isEmpty() || cpass.trim().isEmpty() || npass.trim().isEmpty()) {
                    DialogManager.showDialog(act, "Warning", "Please fill all data!");

                } else if (!npass.equals(cpass)) {
                    DialogManager.showDialog(act, "Warning", "Confirm password not match");
                } else {
                    if(opass.equals(ApplicationData.modelUser.password)){
                        ApplicationData.modelUser.password = npass;
                        finish();
                    }
                    else {
                        DialogManager.showDialog(act, "Warning", "Wrong old password");
                    }


                }
            }
        });

        oldpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    btnClearOldPass.setVisibility(GONE);
                } else {
                    if (oldpass.getText().length() >= 1) {
                        btnClearOldPass.setVisibility(VISIBLE);
                    } else if (oldpass.getText().length() == 0) {
                        btnClearOldPass.setVisibility(GONE);
                    }

                }
            }
        });

        newpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    btnClearNewPass.setVisibility(GONE);
                } else {
                    if (newpass.getText().length() >= 1) {
                        btnClearNewPass.setVisibility(VISIBLE);
                    } else if (oldpass.getText().length() == 0) {
                        btnClearNewPass.setVisibility(GONE);
                    }
                }
            }
        });

        confirmpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    btnClearConfirmPass.setVisibility(GONE);
                } else {
                    if (confirmpass.getText().length() >= 1) {
                        btnClearConfirmPass.setVisibility(VISIBLE);
                    } else if (confirmpass.getText().length() == 0) {
                        btnClearConfirmPass.setVisibility(GONE);
                    }
                }
            }
        });

        oldpass.addTextChangedListener(new TextWatcher() {

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
                    btnClearOldPass.setVisibility(VISIBLE);
                } else if (s.length() == 0) {
                    btnClearOldPass.setVisibility(GONE);
                }
            }
        });

        newpass.addTextChangedListener(new TextWatcher() {

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
                    btnClearNewPass.setVisibility(VISIBLE);
                } else if (s.length() == 0) {
                    btnClearNewPass.setVisibility(GONE);
                }
            }
        });
        confirmpass.addTextChangedListener(new TextWatcher() {

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
                    btnClearConfirmPass.setVisibility(VISIBLE);
                } else if (s.length() == 0) {
                    btnClearConfirmPass.setVisibility(GONE);
                }
            }
        });

        btnClearOldPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpass.setText("");
            }
        });

        btnClearNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newpass.setText("");
            }
        });

        btnClearConfirmPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmpass.setText("");
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }


}
