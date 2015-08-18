package ladyjek.twiscode.com.ladyjek.Utilities;

import android.support.v4.app.FragmentActivity;

/**
 * Created by User on 8/18/2015.
 */
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import ladyjek.twiscode.com.ladyjek.Fragment.FragmentHome;

public class TouchableWrapper extends FrameLayout {

    public TouchableWrapper(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                FragmentHome.hideKeyboard();
                break;
            case MotionEvent.ACTION_UP:
                FragmentHome.layoutMarkerFrom.setVisibility(GONE);
                FragmentHome.layoutMarkerDestination.setVisibility(GONE);
                FragmentHome.mTouchMap = true;
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
