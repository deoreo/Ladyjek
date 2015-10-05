package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import ladyjek.twiscode.com.ladyjek.Adapter.ColorPagerAdapter;
import ladyjek.twiscode.com.ladyjek.Adapter.PromoPagerAdapter;
import ladyjek.twiscode.com.ladyjek.Adapter.PromoSliderAdapter;
import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import me.relex.circleindicator.CircleIndicator;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityPromo extends FragmentActivity {

    private ImageView btnBack;
    private TextView btnOpen;
    CircleIndicator defaultIndicator;
    ViewPager defaultViewpager;
    private WebView webview;
    LinearLayout imageSlide;
    private final String TAG = "ActivityPromoWebView";
    TextView noPromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        defaultViewpager = (ViewPager) findViewById(R.id.viewpager_default);
        defaultIndicator = (CircleIndicator) findViewById(R.id.indicator_default);
        imageSlide = (LinearLayout) findViewById(R.id.imageslide);
        webview = (WebView) findViewById(R.id.webview);
        noPromo = (TextView) findViewById(R.id.txtNoPromo);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

        new CheckPromo(this).execute();





    }

    void SetImageSlideView(){
        webview.setVisibility(View.GONE);
        noPromo.setVisibility(View.GONE);
        imageSlide.setVisibility(View.VISIBLE);

        PromoSliderAdapter defaultPagerAdapter = new PromoSliderAdapter(getSupportFragmentManager(),ApplicationData.promo_images);
        defaultViewpager.setAdapter(defaultPagerAdapter);
        defaultIndicator.setViewPager(defaultViewpager);
    }

    void SetWebView(){
        imageSlide.setVisibility(View.GONE);
        noPromo.setVisibility(View.GONE);
        webview.setVisibility(View.VISIBLE);
        webview.loadUrl(ApplicationData.promo_url);
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

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            verifyURL(url);
            Log.d(TAG, "onPageStarted :" + url);

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

    private class CheckPromo extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;

        public CheckPromo(Activity activity) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Loading. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.postPromoImage();
                Log.d("json promo", response.toString());
                if(response!=null){
                    try{
                        String url = response.getString("url");
                        ApplicationData.promo_url = url;
                        ApplicationData.promo_images = new JSONArray();
                        return "WEBVIEW";
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        try{
                            JSONArray url = response.getJSONArray("images");
                            ApplicationData.promo_images = url;
                            ApplicationData.promo_url = "";
                            return "IMAGE";
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            switch (result) {
                case "WEBVIEW":
                    SetWebView();
                    break;
                case "IMAGE":
                    SetImageSlideView();
                    break;
                default:
                    webview.setVisibility(View.GONE);
                    imageSlide.setVisibility(View.GONE);
                    noPromo.setVisibility(View.VISIBLE);
            }
        }


    }
}
