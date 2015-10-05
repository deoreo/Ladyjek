package ladyjek.twiscode.com.ladyjek.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.json.JSONArray;

import ladyjek.twiscode.com.ladyjek.Fragment.PromoSliderFragment;

/**
 * Created by Unity on 01/09/2015.
 */
public class HelpSliderAdapter extends FragmentPagerAdapter {
    private int pagerCount;
    private JSONArray data;


    public HelpSliderAdapter(FragmentManager fm, JSONArray dt) {
        super(fm);
        data = dt;
        pagerCount = data.length();
    }

    @Override public Fragment getItem(int i) {
        String url = "";
        try{
            url = data.getString(i);

        }
        catch (Exception e){

        }
        return PromoSliderFragment.newInstance(url);
    }

    @Override public int getCount() {
        return pagerCount;
    }
}
