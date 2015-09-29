package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelOrder;
import ladyjek.twiscode.com.ladyjek.Model.ModelUserOrder;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.GoogleAPIManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityConfirm extends ActionBarActivity {


    private Toolbar mToolbar;
    private Activity mActivity;
    private Spinner pay;
    private ArrayAdapter<CharSequence> adapterPay;
    private ImageView btnBack;
    private RelativeLayout wrapperRegister;
    private TextView txtConfirm, txtFrom, txtDestination, txtDistance, txtDuration, txtTotal, txtDetailFrom, txtDetailDestination;
    private String strFrom = "", strDest = "", strDistance = "",
            strDuration = "", strDetailFrom = "",strDetailDest="" ;
    private int totalPrice = 0;
    private NumberFormat numberFormat;
    private DecimalFormat decimalFormat;
    private SocketManager socketManager;

    int pembayaran = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        mActivity = this;

        socketManager = ApplicationData.socketManager;
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(otherSymbols);
        //SetActionBar();
        SetPaySpinner();
        new GetInfoOrder(mActivity).execute();


        txtConfirm = (TextView) findViewById(R.id.btnConfirm);
        txtFrom = (TextView) findViewById(R.id.txtFrom);
        txtDestination = (TextView) findViewById(R.id.txtDestination);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtDuration = (TextView) findViewById(R.id.txtDuration);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtDetailFrom = (TextView) findViewById(R.id.txtDetailFrom);
        txtDetailDestination = (TextView) findViewById(R.id.txtDetailDestination);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        wrapperRegister = (RelativeLayout) findViewById(R.id.wrapperRegister);

        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pembayaran == 0) {
                    socketManager.CreateOrder(ApplicationData.posFrom, ApplicationData.posDestination);
                    Intent i = new Intent(getBaseContext(), ActivityLoading.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ApplicationManager um = new ApplicationManager(ActivityConfirm.this);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(getBaseContext(), ActivityVerifyPayment.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ApplicationManager um = new ApplicationManager(ActivityConfirm.this);
                    startActivity(i);
                    finish();
                }

            }
        });

        wrapperRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketManager.CreateOrder(ApplicationData.posFrom, ApplicationData.posDestination);
                Intent i = new Intent(getBaseContext(), ActivityLoading.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ApplicationManager um = new ApplicationManager(ActivityConfirm.this);
                um.setActivity("ActivityLoading");
                startActivity(i);
                finish();
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
        switch (id) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void SetActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void SetPaySpinner() {
        pay = (Spinner) findViewById(R.id.txtPay);
        adapterPay = ArrayAdapter.createFromResource(this, R.array.payList, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterPay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        pay.setAdapter(adapterPay);

        pay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pembayaran = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = new Intent(getBaseContext(), Main.class);
        startActivity(i);
        finish();
    }

    private class GetInfoOrder extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;
        private ModelOrder order;

        public GetInfoOrder(Activity activity) {
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
                //order = ApplicationManager.getInstance(activity).getOrder();
                //if(order == null) {
                    strFrom = ApplicationData.addressFrom;
                    strDest = ApplicationData.addressDestination;
                    strDetailFrom = ApplicationData.detailFrom;
                    strDetailDest = ApplicationData.detailDestination;
                    strDistance = ApplicationData.distance;
                    strDuration = ApplicationData.duration;
                    String[] strDist = strDistance.split(" ");
                    float distance = Float.parseFloat(strDist[0]);
                    if(distance<=6.0) {
                        totalPrice = 25000;
                        //totalPrice = 4000 * Math.round(distance);
                    }
                    else{
                        int floatprice = (int)((distance*100)%100);
                        int price = 4000*floatprice/100;
                        totalPrice = 25000 + price;
                    }
                //}
                /*
                else{

                    strFrom = order.getFrom();
                    strDest = order.getTo();
                    Double LatFrom = Double.parseDouble(order.getFromLatitude());
                    Double LonFrom = Double.parseDouble(order.getFromLongitude());
                    GoogleAPIManager.getAddress(activity, new LatLng(LatFrom, LonFrom), strDetailFrom);

                    Double LatDestination = Double.parseDouble(order.getToLatitude());
                    Double LonDestination = Double.parseDouble(order.getToLongitude());
                    GoogleAPIManager.getAddress(activity, new LatLng(LatDestination, LonDestination), strDetailDest);

                    strDistance = order.getDistance();
                    strDuration = order.getDuration();
                    totalPrice = Integer.parseInt(order.getPrice().replaceAll("[^\\d]", ""));

                }*/
                return "OK";
            } catch (Exception e) {

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
                    txtFrom.setText(strFrom);
                    txtDestination.setText(strDest);
                    txtDetailFrom.setText(strDetailFrom);
                    txtDetailDestination.setText(strDetailDest);
                    txtDistance.setText(strDistance);
                    txtDuration.setText(strDuration);
                    txtTotal.setText("Rp " + decimalFormat.format(totalPrice));
                    ApplicationData.price = txtTotal.getText().toString();
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
