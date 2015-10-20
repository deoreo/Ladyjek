package ladyjek.twiscode.com.ladyjek.Parse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ladyjek.twiscode.com.ladyjek.Activity.ActivityHandphoneKonfirmasi;
import ladyjek.twiscode.com.ladyjek.Activity.ActivityRegister;
import ladyjek.twiscode.com.ladyjek.Adapter.AdapterMessage;
import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelUserOrder;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivityNotif extends AppCompatActivity {

    private static String TAG = ActivityNotif.class.getSimpleName();
    private ListView listView;
    private AdapterMessage adapter;
    private ApplicationManager appManager;
    private TextView noItems;
    private ImageView btnBack;
    List<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        noItems = (TextView) findViewById(R.id.noItems);
        listView = (ListView) findViewById(R.id.list_view);



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

         new GetMessage(this).execute();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private class GetMessage extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;
        private ArrayList<Message> listmessage;

        public GetMessage(Activity activity) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Membuka inbox. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ApplicationManager appManager = new ApplicationManager(activity);
                //messages = appManager.ge
                if(messages.size()>0){
                    return "OK";
                }
                else {
                    return "FAIL";
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
                    listView.setVisibility(View.GONE);
                    noItems.setVisibility(View.VISIBLE);
                    break;
                case "OK":
                    adapter = new AdapterMessage(ActivityNotif.this,messages);
                    listView.setAdapter(adapter);
                    listView.setVisibility(View.VISIBLE);
                    noItems.setVisibility(View.GONE);
                    break;
            }


        }


    }


}