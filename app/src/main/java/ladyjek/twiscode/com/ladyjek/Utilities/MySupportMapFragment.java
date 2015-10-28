package ladyjek.twiscode.com.ladyjek.Utilities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by User on 8/18/2015.
 */
public class MySupportMapFragment extends SupportMapFragment {

    protected View mOriginalContentView;
    public TouchableWrapper mTouchView;
    private final String TAG = "MySupportMapFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);
        mTouchView = new TouchableWrapper(getActivity());
        if(NetworkManager.getInstance(getActivity()).isConnectedInternet()) {
            mTouchView.addView(mOriginalContentView);
        }
        return mTouchView;
    }

    @Override
    public View getView() {
        Log.d(TAG, "getView");
        return mOriginalContentView;
    }


}
