package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelUserOrder;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.NetworkManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityInformasiPribadi extends Activity {

    private EditText txtNama, txtEmail, txtPassword, txtHp, txtHpPrimary;
    private ImageView editNama, editEmail,editPass,editHp,editKantor,editRumah,btnBack;
    private TextView txtHomeAddress,txtHomeAddressDetail, txtOfficeAddress, txtOfficeAddressDetail;
    private RelativeLayout btnSimpan,btnVerify;
    SocketManager socketManager;
    private BroadcastReceiver doEditNama,doEditEmail;
    ApplicationManager applicationManager;
    Activity act;
    ModelUserOrder user = new ModelUserOrder();
    ProgressDialog progressDialog;
    private BroadcastReceiver changeName, changeMail, changeHP, changeHouseLocation, changeOfficeLocation;
    String noHP, strDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_pribadi);

        act = this;
        applicationManager = new ApplicationManager(act);
        socketManager = ApplicationData.socketManager;
        user = applicationManager.getUser();


        btnSimpan = (RelativeLayout) findViewById(R.id.wrapperSimpan);
        btnVerify = (RelativeLayout) findViewById(R.id.wrapperVerify);

        txtHomeAddress = (TextView) findViewById(R.id.txtHomeAddress);
        txtHomeAddressDetail = (TextView) findViewById(R.id.txtHomeAddressDetail);
        txtOfficeAddress = (TextView) findViewById(R.id.txtOfficeAddress);
        txtOfficeAddressDetail = (TextView) findViewById(R.id.txtOfficeAddressDetail);

        txtNama = (EditText) findViewById(R.id.txtNama);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtHpPrimary = (EditText) findViewById(R.id.txtPhone1);
        txtHp = (EditText) findViewById(R.id.txtPhone);

        editNama = (ImageView) findViewById(R.id.editNama);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        editEmail = (ImageView) findViewById(R.id.editEmail);
        editPass = (ImageView) findViewById(R.id.editPassword);
        editHp = (ImageView) findViewById(R.id.editPhone);
        editRumah = (ImageView) findViewById(R.id.editRumah);
        editKantor = (ImageView) findViewById(R.id.editKantor);



        editNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNama.setEnabled(true);
                txtNama.requestFocus();
            }
        });

        txtNama.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    txtNama.setEnabled(false);
                    handled = true;
                }
                return handled;
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail.setEnabled(true);
                txtEmail.requestFocus();
            }
        });

        txtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    txtEmail.setEnabled(false);
                    handled = true;
                }
                return handled;
            }
        });

        editHp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtHp.setEnabled(true);
                txtHp.requestFocus();
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

                ApplicationData.editHome = false;
                Intent i = new Intent(getBaseContext(), ActivityChangeLocation.class);
                startActivity(i);
                finish();

            }
        });

        editRumah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApplicationData.editHome = true;
                Intent i = new Intent(getBaseContext(), ActivityChangeLocation.class);
                startActivity(i);
                finish();

            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtNama.getText().toString();
                if(!name.equals(user.getName())){
                    if(NetworkManager.getInstance(act).isConnectedInternet()){
                        if(socketManager.isConnected()){
                            OpenLoading();
                            socketManager.ChangeName(name);
                        }
                    }
                }
                else {
                    String mail = txtEmail.getText().toString();
                    if(mail.length() > 0){
                        if(!mail.equals(user.getEmail())){
                            if(NetworkManager.getInstance(act).isConnectedInternet()){
                                if(socketManager.isConnected()){
                                    OpenLoading();
                                    socketManager.ChangeEmail(mail);
                                }
                            }
                        }
                        else{
                            noHP = txtHp.getText().toString();
                            Log.d("phone",noHP);
                            if(noHP.length() > 0){
                                String num=noHP.substring(0,1);
                                Log.d("phone num",num);
                                Log.d("phone 0",noHP);
                                if(num.contains("0")){
                                    noHP = noHP.substring(1);
                                    Log.d("phone 1",noHP);
                                }
                                if(!noHP.equals(user.getPhone2nd())){
                                    if(NetworkManager.getInstance(act).isConnectedInternet()){
                                        if(socketManager.isConnected()){
                                            OpenLoading();
                                            socketManager.ChangeSecondHP(noHP);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else{
                            noHP = txtHp.getText().toString();
                            Log.d("phone",noHP);
                            if(noHP.length() > 0){
                                String num=noHP.substring(0,1);
                                Log.d("phone num",num);
                                Log.d("phone 0",noHP);
                                if(num.contains("0")){
                                    noHP = noHP.substring(1);
                                    Log.d("phone 1",noHP);
                                }
                                if(!noHP.equals(user.getPhone2nd())){
                                    if(NetworkManager.getInstance(act).isConnectedInternet()){
                                        if(socketManager.isConnected()){
                                            OpenLoading();
                                            socketManager.ChangeSecondHP(noHP);
                                        }
                                    }
                                }
                            }

                    }
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        changeName = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("broadcast", "changeName");
                String message = intent.getStringExtra("message");
                if (message.equals("true")) {
                    user.setName(txtNama.getText().toString());
                    applicationManager.setUser(user);
                    String mail = txtEmail.getText().toString();
                    if(mail.length() > 0){
                        if(!mail.equals(user.getEmail())){
                            if(NetworkManager.getInstance(act).isConnectedInternet()){
                                if(socketManager.isConnected()){
                                    socketManager.ChangeEmail(mail);
                                }
                            }
                            else {
                                progressDialog.dismiss();
                            }
                        }
                        else {
                            progressDialog.dismiss();
                        }
                    }
                    else {
                        progressDialog.dismiss();
                    }

                }
                else {
                    String mail = txtEmail.getText().toString();
                    if(mail.length() > 0){
                        if(!mail.equals(user.getEmail())){
                            if(NetworkManager.getInstance(act).isConnectedInternet()){
                                if(socketManager.isConnected()){
                                    socketManager.ChangeEmail(mail);
                                }
                                else {
                                    progressDialog.dismiss();
                                }
                            }
                            else {
                                progressDialog.dismiss();
                            }
                        }
                        else {
                            progressDialog.dismiss();
                        }
                    }
                    else {
                        noHP = txtHp.getText().toString();
                        if(noHP.length() > 0){
                            String num=noHP.substring(0,1);
                            Log.d("phone num",num);
                            Log.d("phone",noHP);
                            if(num.contains("0")){
                                noHP = noHP.substring(1);
                                Log.d("phone 1",noHP);
                            }
                            if(!noHP.equals(user.getPhone2nd())){
                                if(NetworkManager.getInstance(act).isConnectedInternet()){
                                    if(socketManager.isConnected()){
                                        socketManager.ChangeSecondHP(noHP);
                                    }
                                    else {
                                        progressDialog.dismiss();
                                    }
                                }
                                else {
                                    progressDialog.dismiss();
                                }
                            }
                            else {
                                progressDialog.dismiss();
                            }
                        }
                        else {
                            progressDialog.dismiss();
                        }
                    }
                }


            }
        };

        changeMail = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("broadcast", "changeMail");
                String message = intent.getStringExtra("message");
                if (message.equals("true")) {
                    user.setEmail(txtEmail.getText().toString());
                    applicationManager.setUser(user);
                    noHP = txtHp.getText().toString();
                    if(noHP.length() > 0){
                        String num=noHP.substring(0,1);
                        Log.d("phone num",num);
                        Log.d("phone",noHP);
                        if(num.contains("0")){
                            noHP = noHP.substring(1);
                            Log.d("phone 1",noHP);
                        }
                        if(!noHP.equals(user.getPhone2nd())){
                            if(NetworkManager.getInstance(act).isConnectedInternet()){
                                if(socketManager.isConnected()){
                                    socketManager.ChangeSecondHP(noHP);
                                }
                                else {
                                    progressDialog.dismiss();
                                }
                            }
                            else {
                                progressDialog.dismiss();
                            }
                        }
                        else {
                            progressDialog.dismiss();
                        }
                    }
                    else {
                        progressDialog.dismiss();
                    }
                }
                else {
                    noHP = txtHp.getText().toString();
                    if(noHP.length() > 0){
                        String num=noHP.substring(0,1);
                        Log.d("phone num",num);
                        Log.d("phone",noHP);
                        if(num.contains("0")){
                            noHP = noHP.substring(1);
                            Log.d("phone 1",noHP);
                        }
                        if(!noHP.equals(user.getPhone2nd())){
                            if(NetworkManager.getInstance(act).isConnectedInternet()){
                                if(socketManager.isConnected()){
                                    OpenLoading();
                                    socketManager.ChangeSecondHP(noHP);
                                }
                                else {
                                    progressDialog.dismiss();
                                }
                            }
                            else {
                                progressDialog.dismiss();
                            }
                        }
                        else {
                            progressDialog.dismiss();
                        }
                    }
                    else {
                        progressDialog.dismiss();
                    }
                }




            }
        };

        changeHP = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("broadcast", "changeHP");
                String message = intent.getStringExtra("message");
                if (message.equals("true")) {
                    user.setPhone2nd(noHP);
                    applicationManager.setUser(user);

                }
                else {

                }

                progressDialog.dismiss();


            }
        };





    }

    @Override
    public void onStart(){
        super.onStart();
        new GetInfoPribadi(ActivityInformasiPribadi.this).execute();
    }



    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }

    void OpenLoading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading. . .");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        Log.i("adding receiver", "fragment ontainer for profile");

        LocalBroadcastManager.getInstance(act).registerReceiver(changeName,
                new IntentFilter("editName"));
        LocalBroadcastManager.getInstance(this).registerReceiver(changeMail,
                new IntentFilter("editEmail"));
        LocalBroadcastManager.getInstance(this).registerReceiver(changeHP,
                new IntentFilter("editHP"));

    }


    public String getAddress(Activity activity, LatLng latlng) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        double lat = latlng.latitude;
        double lng = latlng.longitude;
        String addressLine = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            addressLine = obj.getAddressLine(0);
            strDetail = obj.getAddressLine(1) + " , " + obj.getAddressLine(2);


        } catch (IOException e) {
        } catch (Exception e) {
        }
        return addressLine;
    }

    private class GetInfoPribadi extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;
        private String nama ="";
        private String email="";
        private String password ="";
        private String hp = "";
        private String hp1 = "";
        private String strHomeAddress="";
        private String strHomeAddressDetail="";
        private String strOfficeAddress="";
        private String strOfficeAddressDetail="";

        public GetInfoPribadi(Activity activity) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Loading. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ModelUserOrder user = ApplicationManager.getInstance(activity).getUser();
                nama = user.getName();
                email = user.getEmail();
                password = user.getPassword();
                hp1 = user.getPhone();
                hp = user.getPhone2nd();
                strHomeAddress = user.getAddressHome();
                strOfficeAddress = user.getAddressOffice();
                LatLng rumah = user.getRumah();
                LatLng kantor = user.getKantor();
                try {
                    getAddress(activity,rumah);
                    strHomeAddressDetail = strDetail;
                    getAddress(activity,kantor );
                    strOfficeAddressDetail = strDetail;
                }catch (Exception e){

                }
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            switch (result) {
                case "FAIL":

                    break;
                case "OK":
                    txtNama.setText(nama);
                    txtEmail.setText(email);
                    txtPassword.setText(password);
                    txtHp.setText(hp);
                    txtHpPrimary.setText(hp1);
                    txtHomeAddress.setText("Lokasi Rumah");
                    txtHomeAddressDetail.setText(strHomeAddress);
                    txtOfficeAddress.setText("Lokasi Kantor");
                    txtOfficeAddressDetail.setText(strOfficeAddress);
                    break;
            }
            progressDialog.dismiss();

        }


    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
