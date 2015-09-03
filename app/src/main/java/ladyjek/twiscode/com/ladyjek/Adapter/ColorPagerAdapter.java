package ladyjek.twiscode.com.ladyjek.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Random;

import ladyjek.twiscode.com.ladyjek.Fragment.ColorFragment;

/**
 * Created by Unity on 01/09/2015.
 */
public class ColorPagerAdapter extends FragmentPagerAdapter {
    private int pagerCount = 5;

    private Random random = new Random();

    public ColorPagerAdapter(FragmentManager fm, int count) {
        super(fm);
        pagerCount = count;
    }

    @Override public Fragment getItem(int i) {
        return ColorFragment.newInstance(0xff000000 | random.nextInt(0x00ffffff));
    }

    @Override public int getCount() {
        return pagerCount;
    }
}
