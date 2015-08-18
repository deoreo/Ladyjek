package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;

public class ActivityHandphoneKonfirmasi extends Activity {

    private Activity act;
    private TextView txtConfirmSmsCode;
    private EditText txtSmsCode;
    private RelativeLayout wrapperRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handphone_konfirmasi);

        act = this;
        txtConfirmSmsCode = (TextView)findViewById(R.id.txtConfirmSmsCode);
        txtSmsCode = (EditText) findViewById(R.id.txtSmsCode);
        wrapperRegister = (RelativeLayout) findViewById(R.id.wrapperRegister);

        if(ApplicationData.editPhone){
            txtConfirmSmsCode.setText(R.string.lanjut);
        }
        else {
            txtConfirmSmsCode.setText(R.string.login);
        }

        txtSmsCode.setText(ApplicationData.phone);



        txtConfirmSmsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApplicationData.editPhone) {
                    finish();
                } else {
                    new AlertDialogWrapper.Builder(ActivityHandphoneKonfirmasi.this)
                            .setTitle("Success register!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(ActivityHandphoneKonfirmasi.this, ActivityLogin.class);
                                    startActivity(i);
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.drawable.ladyjek_icon)
                            .show();
                }

            }
        });

        wrapperRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ApplicationData.editPhone){
                    finish();
                }
                else{
                    new AlertDialogWrapper.Builder(ActivityHandphoneKonfirmasi.this)
                            .setTitle("Success register!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(ActivityHandphoneKonfirmasi.this, ActivityLogin.class);
                                    startActivity(i);
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.drawable.ladyjek_icon)
                            .show();
                }

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
        if(ApplicationData.editPhone){
            ApplicationData.editPhone = false;
        }
        finish();
        super.onBackPressed();  // optional depending on your needs
    }


}

