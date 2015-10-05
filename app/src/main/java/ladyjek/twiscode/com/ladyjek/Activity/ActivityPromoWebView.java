package ladyjek.twiscode.com.ladyjek.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import ladyjek.twiscode.com.ladyjek.Adapter.ColorPagerAdapter;
import ladyjek.twiscode.com.ladyjek.Adapter.PromoSliderAdapter;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import me.relex.circleindicator.CircleIndicator;

public class ActivityPromoWebView extends FragmentActivity {

    private WebView webview;
    private ProgressBar mProgressBar;
    private final String TAG = "ActivityPromoWebView";
    ImageView btnClose;
    LinearLayout imageSlide;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_web_view);

        webview = (WebView) findViewById(R.id.webview);
        btnClose = (ImageView) findViewById(R.id.btnClose);
        imageSlide = (LinearLayout) findViewById(R.id.imageslide);
        ViewPager defaultViewpager = (ViewPager) findViewById(R.id.viewpager_default);
        CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.indicator_default);

        mProgressBar = (ProgressBar) findViewById(R.id.webviewProgress);

        webview.setWebViewClient(new myWebClient() {
            public void onPageFinished(WebView view, String url) {
            }
        });
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

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), Main.class);
                startActivity(i);
                finish();
            }
        });


        if(ApplicationData.promo_url!=""){
            imageSlide.setVisibility(View.GONE);
            webview.setVisibility(View.VISIBLE);
            webview.loadUrl(ApplicationData.promo_url);

        }
        else{
            webview.setVisibility(View.GONE);
            imageSlide.setVisibility(View.VISIBLE);
            PromoSliderAdapter defaultPagerAdapter = new PromoSliderAdapter(getSupportFragmentManager(),ApplicationData.promo_images);
            defaultViewpager.setAdapter(defaultPagerAdapter);
            defaultIndicator.setViewPager(defaultViewpager);
        }



    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
            verifyURL(url);
            Log.d(TAG, "onPageStarted :"+url);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            handler.proceed();
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished :"+url);

        }


        private void verifyURL(String url){
            String code;
            Uri uri = Uri.parse(url);
            code = uri.getQueryParameter("id");
            Log.v(TAG, "verifyURL: " + url);
            
        }

    }



}
