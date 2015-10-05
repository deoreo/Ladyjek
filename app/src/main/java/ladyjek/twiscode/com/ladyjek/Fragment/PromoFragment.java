package ladyjek.twiscode.com.ladyjek.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import java.util.List;

import ladyjek.twiscode.com.ladyjek.Model.ModelPromo;
import ladyjek.twiscode.com.ladyjek.R;

/**
 * Created by Unity on 01/09/2015.
 */
public class PromoFragment extends Fragment {
    private static final String TAG_COUNT_URL = "counturl";
    private static final String TAG_LIST_URL = "listurl";
    private static List<ModelPromo> mSourcedata;
    private int mPosition;
    private WebView webview;
    private ProgressBar mProgressBar;

    public static PromoFragment newInstance(List<ModelPromo> promoList,int param1) {
        PromoFragment fragment = new PromoFragment();
        mSourcedata = promoList;
        Bundle args = new Bundle();
        args.putInt(TAG_COUNT_URL, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public PromoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(TAG_COUNT_URL);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_promo_web_view, container, false);
        ModelPromo promo = mSourcedata.get(mPosition);
        webview = (WebView) v.findViewById(R.id.webview);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        webview.getSettings().setSavePassword(false);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSaveFormData(false);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) mProgressBar.setVisibility(View.GONE);
            }


        });
        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });

        webview.loadUrl(promo.getUrlPromo());

        return v;
    }
}
