package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import ladyjek.twiscode.com.ladyjek.Database.DatabaseHandler;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.NetworkManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ActivityEditPassword extends Activity {


    private Activity act;
    private ImageView btnBack;
    private TextView btnSimpan;
    private EditText oldpass,newpass,confirmpass;
    private RelativeLayout btnClearOldPass, btnClearNewPass, btnClearConfirmPass, wrapperRegister;
    SocketManager socketManager;
    private BroadcastReceiver editPassword;
    boolean isClick = false;
    ProgressDialog progressDialog;
    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        act = this;
        socketManager = ApplicationData.socketManager;
        db = new DatabaseHandler(act);
        btnSimpan = (TextView) findViewById(R.id.btnSimpan);

        oldpass = (EditText) findViewById(R.id.txtSandiLama);
        newpass = (EditText) findViewById(R.id.txtSandiBaru);
        confirmpass = (EditText) findViewById(R.id.txtKonfirmasiSandi);

        btnClearOldPass = (RelativeLayout) findViewById(R.id.btnOldPass);
        btnClearNewPass = (RelativeLayout) findViewById(R.id.btnNewPass);
        btnClearConfirmPass = (RelativeLayout) findViewById(R.id.btnConfirmPass);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        wrapperRegister = (RelativeLayout) findViewById(R.id.wrapperRegister);

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
                    DialogManager.showDialog(act, "Peringtan", "Silahkan isi password Anda");

                } else if (!npass.equals(cpass)) {
                    DialogManager.showDialog(act, "Peringatan", "Konfirmasi password tidak sama");
                } else {

                    Log.d("deviceToken",ApplicationData.PARSE_DEVICE_TOKEN);
                    if(!isClick){
                        if(ApplicationData.PARSE_DEVICE_TOKEN != null){
                            if(NetworkManager.getInstance(ActivityEditPassword.this).isConnectedInternet()){
                                progressDialog = new ProgressDialog(act);
                                progressDialog.setMessage("Loading. . .");
                                progressDialog.setIndeterminate(false);
                                progressDialog.setCancelable(false);
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.show();
                                socketManager.EditPassword(opass, npass, ApplicationData.PARSE_DEVICE_TOKEN);
                                isClick = true;
                            }
                            else {
                                DialogManager.showDialog(ActivityEditPassword.this, "Peringatan", "Tidak ada koneksi internet!");
                            }
                        }
                        else{
                            DialogManager.showDialog(ActivityEditPassword.this, "Peringatan", "Device Token null!");
                        }
                    }



                }
            }
        });

        wrapperRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String opass = oldpass.getText().toString();
                String npass = newpass.getText().toString();
                String cpass = confirmpass.getText().toString();
                if (opass == null || npass == null || cpass == null || opass.trim().isEmpty() || cpass.trim().isEmpty() || npass.trim().isEmpty()) {
                    DialogManager.showDialog(act, "Peringtan", "Silahkan isi password Anda");

                } else if (!npass.equals(cpass)) {
                    DialogManager.showDialog(act, "Peringatan", "Konfirmasi password tidak sama");
                } else {

                    //Log.d("deviceToken",ApplicationData.PARSE_DEVICE_TOKEN);
                    if(!isClick){
                        if(ApplicationData.PARSE_DEVICE_TOKEN != null){
                            if(NetworkManager.getInstance(ActivityEditPassword.this).isConnectedInternet()){
                                progressDialog = new ProgressDialog(act);
                                progressDialog.setMessage("Loading. . .");
                                progressDialog.setIndeterminate(false);
                                progressDialog.setCancelable(false);
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.show();
                                socketManager.EditPassword(opass, npass, ApplicationData.PARSE_DEVICE_TOKEN);
                                isClick = true;
                            }
                            else {
                                DialogManager.showDialog(ActivityEditPassword.this, "Peringatan", "Tidak ada koneksi internet!");
                            }
                        }
                        else{
                            DialogManager.showDialog(ActivityEditPassword.this, "Peringatan", "Device Token null!");
                        }
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

        editPassword = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("broadcast", "editPassword");
                String message = intent.getStringExtra("message");
                if(isClick){
                    progressDialog.dismiss();
                }
                if (message.equals("true")) {
                    new AlertDialogWrapper.Builder(ActivityEditPassword.this)
                            .setTitle("Success")
                            .setMessage("Silahkan login lagi!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    db.logout();
                                    Intent i = new Intent(act, ActivityLogin.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();

                                }
                            })
                            .setIcon(R.drawable.ladyjek_icon)
                            .show();
                }
                else {
                    new AlertDialogWrapper.Builder(ActivityEditPassword.this)
                            .setTitle("Fail")
                            .setMessage("Password gagal diubah!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.drawable.ladyjek_icon)
                            .show();
                }
                isClick = false;


            }
        };




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

    public void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        Log.i("adding receiver", "fragment ontainer for profile");

        LocalBroadcastManager.getInstance(this).registerReceiver(editPassword,
                new IntentFilter("editPassword"));

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
