package ladyjek.twiscode.com.ladyjek.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ProgressBar;

import java.util.List;
import java.util.Random;

import ladyjek.twiscode.com.ladyjek.Activity.ActivityPromoWebView;
import ladyjek.twiscode.com.ladyjek.Fragment.ColorFragment;
import ladyjek.twiscode.com.ladyjek.Fragment.PromoFragment;
import ladyjek.twiscode.com.ladyjek.Model.ModelPromo;
import ladyjek.twiscode.com.ladyjek.R;

/**
 * Created by Unity on 01/09/2015.
 */
public class PromoPagerAdapter extends FragmentPagerAdapter {
    private FragmentActivity mAct;
    private int pagerCount = 5;
    private List<ModelPromo> mSourceData;
    private Random random = new Random();
    private LayoutInflater mInflater =null;
    private ProgressBar mProgressBar;
    private WebView webview;

    public PromoPagerAdapter(FragmentManager fm, List<ModelPromo> sourcedata, int count) {
        super(fm);
        this.pagerCount = count;
        this.mSourceData = sourcedata;
    }



    @Override
    public int getCount() {
        return pagerCount;
    }

    @Override
    public Fragment getItem(int position) {
        return PromoFragment.newInstance(mSourceData,position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
