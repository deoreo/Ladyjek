package ladyjek.twiscode.com.ladyjek.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ladyjek.twiscode.com.ladyjek.Adapter.ColorPagerAdapter;
import ladyjek.twiscode.com.ladyjek.Adapter.PromoPagerAdapter;
import ladyjek.twiscode.com.ladyjek.R;
import me.relex.circleindicator.CircleIndicator;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityPromo extends FragmentActivity {

    private ImageView btnBack;
    private TextView btnOpen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);



        btnBack = (ImageView) findViewById(R.id.btnBack);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ViewPager defaultViewpager = (ViewPager) findViewById(R.id.viewpager_default);
        CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.indicator_default);
        //ColorPagerAdapter defaultPagerAdapter = new ColorPagerAdapter(getSupportFragmentManager(),5);
        PromoPagerAdapter defaultPagerAdapter = new PromoPagerAdapter(getSupportFragmentManager(),null,5);
        defaultViewpager.setAdapter(defaultPagerAdapter);
        defaultIndicator.setViewPager(defaultViewpager);
        
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
