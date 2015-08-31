package ladyjek.twiscode.com.ladyjek.Parse;

import android.app.Application;

import ladyjek.twiscode.com.ladyjek.Parse.ParseUtils;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // register with parse
        ParseUtils.registerParse(this);
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}
