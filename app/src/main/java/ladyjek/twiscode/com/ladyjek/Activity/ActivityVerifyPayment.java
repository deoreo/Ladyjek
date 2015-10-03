package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.cookie.Cookie;

import java.util.List;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelOrder;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.view.View.*;

public class ActivityVerifyPayment extends Activity {

    RelativeLayout wrapperPopup;
    private ProgressBar mProgressBar;
    private Activity act;
    private final String TAG = "ActivityVerifyPayment";
    ApplicationManager applicationManager;
    ModelOrder order;
    private SocketManager socketManager;
    private BroadcastReceiver doVerify;
    private RelativeLayout layoutVerifying;
    private LinearLayout layoutWebview;
    private WebView webview;
    //PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_payment);
        wrapperPopup = (RelativeLayout)findViewById(R.id.wrapperPopup);
        layoutVerifying = (RelativeLayout)findViewById(R.id.layoutVerifying);
        layoutWebview = (LinearLayout)findViewById(R.id.layoutWebview);
        webview = (WebView) findViewById(R.id.webview);
        mProgressBar = (ProgressBar)findViewById(R.id.webviewProgress);
        act = this;
        applicationManager = new ApplicationManager(act);
        order = applicationManager.getOrder();
        socketManager = ApplicationData.socketManager;

        doVerify  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("doCreateOrder", message);
                //DialogManager.DismissLoading(ActivityVerifyPayment.this);
                //popupWindow.dismiss();
                if(message=="true") {

                    popupSuccess();
                }
                else {
                    popupFailed();
                }


            }
        };





    }

    @Override
    public void onStart(){
        super.onStart();
        Dummy();
    }


    private void Dummy(){
        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                //popupWebview();
                layoutVerifying.setVisibility(GONE);
                layoutWebview.setVisibility(VISIBLE);
                showwebview();
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

    private void popupWebview(){
        LayoutInflater layoutInflater  = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_webview, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

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
                if (newProgress == 100)
                    mProgressBar.setVisibility(GONE);
            }

            

        });
        webview.setOnTouchListener(new OnTouchListener() {
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


        popupWindow.setFocusable(true);
        try {
            popupWindow.showAtLocation(findViewById(R.id.wrapperPopup), Gravity.CENTER, 0, 0);
        }
        catch (Exception e){
            wrapperPopup = (RelativeLayout)findViewById(R.id.wrapperPopup);
            popupWindow.showAtLocation(findViewById(R.id.wrapperPopup), Gravity.CENTER, 0, 0);
        }

    }

    private void popupFailed(){
        LayoutInflater layoutInflater  = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_failed_ecash, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        TextView btnJemput = (TextView)popupView.findViewById(R.id.btnConfirm);


        btnJemput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext(), Main.class);
                ApplicationManager um = new ApplicationManager(ActivityVerifyPayment.this);
                startActivity(i);
                finish();
                popupWindow.dismiss();


            }
        });


        try {
            popupWindow.showAtLocation(findViewById(R.id.wrapperPopup), Gravity.CENTER, 0, 0);
        }
        catch (Exception e){
            wrapperPopup = (RelativeLayout)findViewById(R.id.wrapperPopup);
            popupWindow.showAtLocation(findViewById(R.id.wrapperPopup), Gravity.CENTER, 0, 0);
        }
    }

    private void popupSuccess(){
        LayoutInflater layoutInflater  = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_payment_confirmation, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        TextView btnJemput = (TextView)popupView.findViewById(R.id.btnConfirm);


        btnJemput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext(), ActivityLoading.class);
                ApplicationManager um = new ApplicationManager(ActivityVerifyPayment.this);
                startActivity(i);

                finish();
                popupWindow.dismiss();


            }
        });

        try {
            popupWindow.showAtLocation(findViewById(R.id.wrapperPopup), Gravity.CENTER, 0, 0);
        }
        catch (Exception e){
            wrapperPopup = (RelativeLayout)findViewById(R.id.wrapperPopup);
            popupWindow.showAtLocation(findViewById(R.id.wrapperPopup), Gravity.CENTER, 0, 0);
        }
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(VISIBLE);
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
            if(url.contains("payment-callback/mandiriecash") && code!= null){
                //popupWindow.dismiss();
                Log.v(TAG, "code detected :" + code);
                //DialogManager.ShowLoading(act, "Verifying...");
                socketManager.VerifyEcash(code);
            }
        }

    }

    private void showwebview(){
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
                if (newProgress == 100)
                    mProgressBar.setVisibility(GONE);
            }


        });
        webview.setOnTouchListener(new OnTouchListener() {
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



    }





    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(doVerify,
                new IntentFilter("ecash"));

    }

}