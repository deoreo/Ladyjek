package ladyjek.twiscode.com.ladyjek.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import ladyjek.twiscode.com.ladyjek.Activity.ActivitySplashScreen;

/**
 * Created by User on 8/11/2015.
 */
public class UserManager {
    private static UserManager sInstance = null;

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LadyjekUser";

    private static final String KEY_REFRESH_TOKEN = "refreshToken";
    private static final String KEY_EXPIRED_ON = "expiredOn";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ACTIVITY = "activity";
    private Context mContext;
    private static final String TAG = "UserManager";

    public UserManager(Context context){
        mContext = context;
        mPref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mPref.edit();
    }

    public static UserManager getInstance(Context ctx) {
        if (sInstance == null)
            sInstance = new UserManager(ctx);
        return sInstance;
    }


    /**
     * Quick check login session
     *
     * @return true if user already has access token, false if otherwise
     */
    public boolean isLoggedIn() {
        if (!getUserMail().isEmpty())
            return true;
        else
            return false;
    }



    public String getUserMail() {
        return mPref.getString(KEY_USER_EMAIL, null);
    }

    public void setUserMail(String userMail) {
        try {
            mEditor.putString(KEY_USER_EMAIL, userMail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
        mEditor.commit();
    }

    public String getActivity() {
        return mPref.getString(KEY_ACTIVITY, "");
    }

    public void setActivity(String act) {
        try {
            mEditor.putString(KEY_ACTIVITY, act);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
        mEditor.commit();
    }

    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }

}