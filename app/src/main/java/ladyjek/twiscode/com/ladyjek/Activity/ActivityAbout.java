package ladyjek.twiscode.com.ladyjek.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ladyjek.twiscode.com.ladyjek.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityAbout extends FragmentActivity {

    private ImageView btnBack;
    private TextView btnOpen,txtTitle,txtAbout_1,txtAbout_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        btnOpen = (TextView) findViewById(R.id.btnOpenWebsite);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtAbout_1 = (TextView) findViewById(R.id.txtAbout_1);
        txtAbout_2 = (TextView) findViewById(R.id.txtAbout_2);
        btnBack = (ImageView) findViewById(R.id.btnBack);

        Typeface bold= Typeface.createFromAsset(getAssets(), "fonts/GothamRnd-Bold.otf");
        Typeface medium= Typeface.createFromAsset(getAssets(), "fonts/GothamRnd-Medium.otf");

        txtTitle.setTypeface(bold);
        txtAbout_1.setTypeface(medium);
        txtAbout_2.setTypeface(medium);
        btnOpen.setTypeface(bold);

        String about_1 = getString(R.string.about_1);
        String about_2 = getString(R.string.about_2);

        txtAbout_1.setText(Html.fromHtml(about_1));
        txtAbout_2.setText(Html.fromHtml(about_2));


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent internetIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.ladyjek.com"));
                //internetIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
                internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(internetIntent);
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
