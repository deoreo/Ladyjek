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

    private final String TAG = "TouchableWrapper";
    private Context context;

    public TouchableWrapper(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(NetworkManager.getInstance(context).isConnectedInternet()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    FragmentHome.hideKeyboard();
                    FragmentHome.layoutMarkerFrom.setVisibility(GONE);
                    FragmentHome.layoutMarkerDestination.setVisibility(GONE);
                    FragmentHome.mTouchMap = true;
                    if (FragmentHome.markerTemp != null) {
                        FragmentHome.markerTemp.remove();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
        }

        return super.dispatchTouchEvent(event);
    }
}
