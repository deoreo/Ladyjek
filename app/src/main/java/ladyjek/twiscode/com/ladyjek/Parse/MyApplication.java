package ladyjek.twiscode.com.ladyjek.Parse;

import android.app.Application;

import com.parse.PushService;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //ParseManager.registerParse(this);
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}
