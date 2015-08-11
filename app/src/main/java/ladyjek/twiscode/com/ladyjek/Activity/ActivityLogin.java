package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Database.DatabaseHandler;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelUser;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.KeyboardManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.UserManager;

public class ActivityLogin extends Activity implements KeyboardManager.Listener {

    private Activity mActivity;
    private TextView btnRegister, btnLogin;
    private EditText txtEmail, txtPassword;
    private RelativeLayout wrapperLogin, wrapperRegister;
    private ModelUser userLogin;
    private UserManager userManager;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mActivity = this;

        db = new DatabaseHandler(mActivity);
        btnRegister = (TextView) findViewById(R.id.btnRegister);
        btnLogin = (TextView) findViewById(R.id.btnLogin);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        wrapperLogin = (RelativeLayout) findViewById(R.id.wrapperLogin);
        wrapperRegister = (RelativeLayout) findViewById(R.id.wrapperRegister);


        KeyboardManager mainLayout = (KeyboardManager) findViewById(R.id.layoutLogin);
        mainLayout.setListener(this);

        txtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String email = txtEmail.getText().toString();
                if(!email.isEmpty()) {
                    wrapperLogin.setVisibility(View.VISIBLE);
                    wrapperRegister.setVisibility(View.GONE);
                }
            }
        });

        txtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                wrapperLogin.setVisibility(View.VISIBLE);
                wrapperRegister.setVisibility(View.GONE);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ActivityRegister.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
                    DialogManager.showDialog(mActivity, "Warning", "Email or Password is empty!");
                    txtEmail.setText("");
                    txtPassword.setText("");
                } else if (!email.trim().contains("@") ||
                        !email.trim().contains(".") ||
                        email.trim().contains(" ")) {
                    DialogManager.showDialog(mActivity, "Warning", "Wrong email format!");
                    txtEmail.setText("");
                    txtPassword.setText("");
                } else {
                    hideKeyboard();
                    //txtEmail.setText("");
                    //txtPassword.setText("");
                    new DoLogin(mActivity).execute(
                            email,
                            password
                    );
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
    public void onSoftKeyboardShown(boolean isShowing) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        if (isShowing || !email.isEmpty()|| !password.isEmpty()) {
            wrapperLogin.setVisibility(View.VISIBLE);
            wrapperRegister.setVisibility(View.GONE);
        } else {
            wrapperLogin.setVisibility(View.GONE);
            wrapperRegister.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard(){
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private class DoLogin extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;

        public DoLogin(Activity activity) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Signing in. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String email = params[0];
                String password = params[1];

                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.postLogin(email,password);
                JSONObject responseUser = response.getJSONObject("user");
                Log.d("json response", response.toString());
                try {
                    String _id = responseUser.getString("_id");
                    Log.d("json response id",_id.toString());
                    if(_id!=null){
                        userLogin = new ModelUser();
                        userLogin.setEmail(email);
                        userLogin.setPassword(password);
                        //UserManager.getInstance(mActivity).setUserMail(email);
                        db.insertUser(userLogin);
                        ApplicationData.login_id = _id.toString();
                        return "OK";
                    }
                    else {
                        return "FAIL";
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            switch (result) {
                case "FAIL":
                    DialogManager.showDialog(activity, "Warning", "Please register!");
                    break;
                case "OK":

                    Intent i = new Intent(getBaseContext(), Main.class);
                    startActivity(i);
                    finish();
                    break;
            }


        }


    }

}

