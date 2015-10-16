package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityCashless extends FragmentActivity {

    private ImageView btnBack;
    private TextView lblMandiri,lblXL;
    private LinearLayout btnMandiri,btnXL;
    ApplicationManager applicationManager;
    Activity mAct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashless);

        mAct = this;
        applicationManager = new ApplicationManager(mAct);

        btnMandiri = (LinearLayout) findViewById(R.id.btnMandiri);
        btnXL = (LinearLayout) findViewById(R.id.btnXL);
        lblMandiri = (TextView) findViewById(R.id.txtMandiri);
        lblXL = (TextView) findViewById(R.id.txtXL);
        btnBack = (ImageView) findViewById(R.id.btnBack);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnMandiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ActivityMandiri.class);
                startActivity(i);
            }
        });

        try {
            lblMandiri.setText("0" + applicationManager.getUser().getPhone());
        }catch (Exception e){
            lblMandiri.setText("-");
        }






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
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
