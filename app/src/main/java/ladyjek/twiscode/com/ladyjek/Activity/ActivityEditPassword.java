package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;

public class ActivityEditPassword extends Activity {


    private Activity act;
    private ImageView btnBack;
    private TextView btnSimpan;
    private EditText oldpass,newpass,confirmpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        act = this;

        btnSimpan = (TextView) findViewById(R.id.btnSimpan);

        oldpass = (EditText) findViewById(R.id.txtSandiLama);
        newpass = (EditText) findViewById(R.id.txtSandiBaru);
        confirmpass = (EditText) findViewById(R.id.txtKonfirmasiSandi);

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
