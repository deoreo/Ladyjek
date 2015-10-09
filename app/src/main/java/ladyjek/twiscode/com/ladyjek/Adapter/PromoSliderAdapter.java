package ladyjek.twiscode.com.ladyjek.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import org.json.JSONArray;

import java.util.Random;

import ladyjek.twiscode.com.ladyjek.Fragment.ColorFragment;
import ladyjek.twiscode.com.ladyjek.Fragment.PromoSliderFragment;

/**
 * Created by Unity on 01/09/2015.
 */
public class PromoSliderAdapter extends FragmentPagerAdapter {
    private int pagerCount;
    private JSONArray data;


    public PromoSliderAdapter(FragmentManager fm, JSONArray dt) {
        super(fm);
        data = dt;
        pagerCount = data.length();
    }

    @Override public Fragment getItem(int i) {
        String url = "";
        try{
            url = data.getString(i);
            Log.d("promo url images",url);

        }
        catch (Exception e){

        }
        return PromoSliderFragment.newInstance(url);
    }

    @Override public int getCount() {
        return pagerCount;
    }
}
