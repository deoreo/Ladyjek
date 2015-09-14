package ladyjek.twiscode.com.ladyjek.Parse;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // register with parse
        //ParseManager.registerParse(this);
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}
