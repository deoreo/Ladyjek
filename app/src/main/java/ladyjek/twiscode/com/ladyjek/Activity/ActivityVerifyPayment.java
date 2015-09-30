package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.cookie.Cookie;

import java.util.List;

import ladyjek.twiscode.com.ladyjek.Model.ModelOrder;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityVerifyPayment extends Activity {

    RelativeLayout wrapperPopup;
    private ProgressBar mProgressBar;
    private Activity act;
    private String mUsername;
    private String mPassword;
    private int mComeFrom;
    private boolean mAlreadyEverGiveCredential = false;
    private final String TAG = "ActivityVerifyPayment";
    private final String TAG_ACCESS_CODE = "code";
    ApplicationManager applicationManager;
    ModelOrder order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_payment);
        wrapperPopup = (RelativeLayout)findViewById(R.id.wrapperPopup);
        act = this;
        applicationManager = new ApplicationManager(act);
        order = applicationManager.getOrder();
        Dummy();
    }
    private void Dummy(){
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                popupSucces();
            }
        }.start();
    }



    @Override
    public void onBackPressed() {
        //finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void popupSucces(){
        LayoutInflater layoutInflater  = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_webview, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        CookieSyncManager.createInstance(this);
        ImageView btnClose = (ImageView)popupView.findViewById(R.id.btnClose);
        WebView webview = (WebView) popupView.findViewById(R.id.webview);
        mProgressBar = (ProgressBar)popupView.findViewById(R.id.webviewProgress);
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
        webview.loadUrl(order.getUrl());


        btnClose.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getBaseContext(), ActivityConfirm.class);
                ApplicationManager um = new ApplicationManager(ActivityVerifyPayment.this);
                startActivity(i);
                finish();
                popupWindow.dismiss();
            }
        });

        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(findViewById(R.id.wrapperPopup), Gravity.CENTER, 0, 0);

    }


    private void popupFailed(){
        LayoutInflater layoutInflater  = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_failed_ecash, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        TextView btnJemput = (TextView)popupView.findViewById(R.id.btnConfirm);
        ImageView btnClose = (ImageView)popupView.findViewById(R.id.btnClose);


        btnJemput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent i = new Intent(getBaseContext(), Main.class);
                ApplicationManager um = new ApplicationManager(ActivityVerifyPayment.this);
                startActivity(i);
                */
                finish();
                //popupWindow.dismiss();


            }
        });
        btnClose.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*
                Intent i = new Intent(getBaseContext(), ActivityConfirm.class);
                ApplicationManager um = new ApplicationManager(ActivityVerifyPayment.this);
                startActivity(i);
                finish();
                popupWindow.dismiss();
                */
                finish();
            }
        });

        popupWindow.showAtLocation(findViewById(R.id.wrapperPopup), Gravity.CENTER, 0, 0);
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);

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
            detectCodeInCurrentURL(url, true);
        }

        private void detectCodeInCurrentURL(String urlORcode, boolean directThrough){
            String code = isMandiriURL(urlORcode);
            if(code != null) {
                oAuthProceed(code);
            }
        }

        private String isMandiriURL(String url){
            String code;
            Uri uri = Uri.parse(url);
            code = uri.getQueryParameter("id");
            Log.v(TAG, "url:" + url);
            if(url.contains("payment-callback/mandiriecash") || code!= null){
                Log.v(TAG, "code detected :" + code);
                return code;
            }
            return null;
        }

        private void oAuthProceed(String code){
            Log.v(TAG,"oAuthProceed");
            Bundle conData = new Bundle();
            conData.putString(TAG_ACCESS_CODE, code);
            Intent intent = new Intent();
            intent.putExtras(conData);
            setResult(RESULT_OK, intent);
            finish();
        }
    }






    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



}