package ladyjek.twiscode.com.ladyjek.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import ladyjek.twiscode.com.ladyjek.Model.ModelOrder;
import ladyjek.twiscode.com.ladyjek.Model.ModelPlace;

/**
 * Created by User on 8/11/2015.
 */
public class ApplicationManager {
    private static ApplicationManager sInstance = null;

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LadyjekApplication";

    private static final String KEY_REFRESH_TOKEN = "refreshToken";
    private static final String KEY_EXPIRED_ON = "expiredOn";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ARRIVE = "isArrive";
    private static final String KEY_ACTIVITY = "activity";
    private static final String KEY_FROM = "userFrom";
    private static final String KEY_DESTINATION = "userDestination";
    private static final String KEY_DRIVER = "driverlocation";
    private static final String TAG = "ApplicationManager";
    private static final String KEY_ORDER = "order";
    private static final String KEY_TRIP = "trip";
    private Context mContext;


    public ApplicationManager(Context context){
        mContext = context;
        mPref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mPref.edit();
    }

    public static ApplicationManager getInstance(Context ctx) {
        if (sInstance == null)
            sInstance = new ApplicationManager(ctx);
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

    public void setUserMail(String userMail) {
        try {
            mEditor.putString(KEY_USER_EMAIL, userMail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
        mEditor.commit();
    }

    public String getUserMail() {
        return mPref.getString(KEY_USER_EMAIL, null);
    }

    public void logoutUser() {
        mEditor.clear();
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

    public void setUserFrom(ModelPlace modelPlace){
        Gson gson = new Gson();
        String dataJson = gson.toJson(modelPlace);
        mEditor.putString(KEY_FROM, dataJson);
        mEditor.commit();

    }

    public ModelPlace getUserFrom(){
        Gson gson = new Gson();
        String json = mPref.getString(KEY_FROM, "");
        ModelPlace obj = gson.fromJson(json, ModelPlace.class);
        return obj;
    }
    public void setUserDestination(ModelPlace modelPlace){
        Gson gson = new Gson();
        String dataJson = gson.toJson(modelPlace);
        mEditor.putString(KEY_DESTINATION, dataJson);
        mEditor.commit();

    }

    public ModelPlace getUserDestination(){
        Gson gson = new Gson();
        String json = mPref.getString(KEY_DESTINATION, "");
        ModelPlace obj = gson.fromJson(json, ModelPlace.class);
        return obj;
    }

    public void setDriver(ModelPlace modelPlace){
        Gson gson = new Gson();
        String dataJson = gson.toJson(modelPlace);
        mEditor.putString(KEY_DRIVER, dataJson);
        mEditor.commit();

    }

    public ModelPlace getDriver(){
        Gson gson = new Gson();
        String json = mPref.getString(KEY_DRIVER, "");
        ModelPlace obj = gson.fromJson(json, ModelPlace.class);
        return obj;
    }

    public void setUserToken(String userToken) {
        try {
            mEditor.putString(KEY_TOKEN, userToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
        mEditor.commit();
    }
    public String getUserToken() {
        return mPref.getString(KEY_TOKEN, null);
    }

    public void setArrive(Boolean userToken) {
        try {
            mEditor.putBoolean(KEY_ARRIVE, userToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
        mEditor.commit();
    }
    public Boolean isArrive() {
        return mPref.getBoolean(KEY_ARRIVE, false);
    }

    public void setOrder(ModelOrder modelOrder){
        Gson gson = new Gson();
        String dataJson = gson.toJson(modelOrder);
        mEditor.putString(KEY_ORDER, dataJson);
        mEditor.commit();

    }

    public ModelOrder getOrder(){
        Gson gson = new Gson();
        String json = mPref.getString(KEY_ORDER, "");
        ModelOrder obj = gson.fromJson(json, ModelOrder.class);
        return obj;
    }

    public String getTrip() {
        return mPref.getString(KEY_TRIP, "");
    }

    public void setTrip(String act) {
        try {
            mEditor.putString(KEY_TRIP, act);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
        mEditor.commit();
    }



}