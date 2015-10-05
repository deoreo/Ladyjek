package ladyjek.twiscode.com.ladyjek.Control;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Utilities.ConfigManager;

public class JSONControl {
    private JSONResponse _JSONResponse;


    public JSONControl() {
        _JSONResponse = new JSONResponse();
    }

    public JSONArray listPlace(String addressInput) {
        JSONArray json = null;
        JSONObject jsonObj = null;
        try {
            String url = ConfigManager.URL_SUGGESTION +
                            URLEncoder.encode(addressInput, "utf8");
            Log.d("url", url);


            jsonObj = _JSONResponse.GETResponse(url);
            json = jsonObj.getJSONArray("predictions");
        } catch (ConnectException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (JSONException e) {
        }
        return json;

    }

    public JSONObject postLogin(String phoneNumber, String password) {
        JSONObject jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("phoneNumber", phoneNumber));
            params.add(new BasicNameValuePair("password", password));
            jsonObj = _JSONResponse.POSTResponse(ConfigManager.LOGIN, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;

    }

    public JSONObject postRegister(String name, String email, String password) {

        JSONObject jsonObj = null;

        try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("phoneNumber", email));
                params.add(new BasicNameValuePair("password", password));
                jsonObj = _JSONResponse.POSTResponse(ConfigManager.REGISTER, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public String postPhone(String token, String phone) {

        String jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("phoneNumber", phone));
            jsonObj = _JSONResponse.POSTPhone(ConfigManager.PHONE_NUMBER, token, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public String postVerifyPhone(String token, String verificationCode) {

        String jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("verificationCode", verificationCode));
            jsonObj = _JSONResponse.POSTPhone(ConfigManager.VERIFY_PHONE_NUMBER, token, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject postRefreshToken(String token) {
        JSONObject jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("token", token));
            jsonObj = _JSONResponse.POSTResponse(ConfigManager.REFRESH_TOKEN, params);


        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("url token",ConfigManager.LOGIN);
        Log.d("params token",token);
        Log.d("return token",jsonObj.toString());
        return jsonObj;

    }

    public String postDeviceToken(String token, String deviceToken) {

        String jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("deviceToken", deviceToken));
            jsonObj = _JSONResponse.POSTDeviceToken(ConfigManager.DEVICE_TOKEN, token, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public String postResendVerifyCode(String token) {

        String jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            jsonObj = _JSONResponse.POSTResendVerifyCode(ConfigManager.RESEND_VERIFY_CODE, token, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public String postForgotPassword(String phone) {

        String jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("phoneNumber", phone));
            jsonObj = _JSONResponse.POSTForgotPassword(ConfigManager.FORGOT_PASSWORD, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public String postCheckCode(String phone, String token) {

        String jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("phoneNumber", phone));
            params.add(new BasicNameValuePair("token", token));
            jsonObj = _JSONResponse.POSTForgotPassword(ConfigManager.CHECK_RESET_PASSWORD, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public String postResendCode(String phone) {

        String jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("phoneNumber", phone));
            jsonObj = _JSONResponse.POSTForgotPassword(ConfigManager.RESEND_RESET_PASSWORD, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public String postResetPassword(String phone, String token, String password) {

        String jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("phoneNumber", phone));
            params.add(new BasicNameValuePair("token", token));
            params.add(new BasicNameValuePair("password", password));
            jsonObj = _JSONResponse.POSTForgotPassword(ConfigManager.RESET_PASSWORD, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public String postLogoutAll(String token) {

        String jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            jsonObj = _JSONResponse.POSTLogoutAll(ConfigManager.LOGOUT_ALL, token, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject postPromo() {
        JSONObject jsonObj = null;

        try {
            jsonObj = _JSONResponse.GETResponse(ConfigManager.PROMO);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;

    }

    public JSONObject postPromoImage() {
        JSONObject jsonObj = null;

        try {
            jsonObj = _JSONResponse.GETResponse(ConfigManager.PROMO_IMAGE);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;

    }

    public JSONObject getHelp() {
        JSONObject jsonObj = null;

        try {
            jsonObj = _JSONResponse.GETResponse(ConfigManager.HELP);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;

    }





}
