package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.User;
import ladyjek.twiscode.com.ladyjek.R;

public class ActivityInformasiPribadi extends Activity {

    private EditText nama,email,password,hp;
    private ImageView editNama, editEmail,editPass,editHp,editKantor,editRumah,btnBack;
    private TextView txtRumah,txtKantor,btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_pribadi);

        btnSimpan = (TextView) findViewById(R.id.btnSimpan);
        txtRumah = (TextView) findViewById(R.id.txtRumah);
        txtKantor = (TextView) findViewById(R.id.txtKantor);

        nama = (EditText) findViewById(R.id.txtNama);
        email = (EditText) findViewById(R.id.txtEmail);
        password = (EditText) findViewById(R.id.txtPassword);
        hp = (EditText) findViewById(R.id.txtPhone);

        editNama = (ImageView) findViewById(R.id.editNama);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        editEmail = (ImageView) findViewById(R.id.editEmail);
        editPass = (ImageView) findViewById(R.id.editPassword);
        editHp = (ImageView) findViewById(R.id.editPhone);
        editRumah = (ImageView) findViewById(R.id.editRumah);
        editKantor = (ImageView) findViewById(R.id.editKantor);

        SetData();

        editNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama.setEnabled(true);
                nama.setFocusable(true);
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setEnabled(true);
                email.setFocusable(true);
            }
        });

        editHp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hp.setEnabled(true);
                hp.setFocusable(true);
            }
        });

        editPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ActivityEditPassword.class);
                startActivity(i);
            }
        });

        editKantor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                ApplicationData.editHome = false;
                Intent i = new Intent(getBaseContext(), ActivityChangeLocation.class);
                startActivity(i);
                */
            }
        });

        editRumah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                ApplicationData.editHome = true;
                Intent i = new Intent(getBaseContext(), ActivityChangeLocation.class);
                startActivity(i);
                */
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng kantor = ApplicationData.user.kantor;
                LatLng rumah = ApplicationData.user.rumah;
                String id = ApplicationData.user.id;
                ApplicationData.user = new User(id,nama.getText().toString(),email.getText().toString(),password.getText().toString(),hp.getText().toString(),kantor,rumah);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

    private void SetData(){
        nama.setText(ApplicationData.user.nama);
        email.setText(ApplicationData.user.email);
        password.setText(ApplicationData.user.password);
        hp.setText(ApplicationData.user.hp);
    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }




}
