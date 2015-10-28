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
            jsonObj = _JSONResponse.POSTResponse(ConfigManager.LOGIN, ConfigManager.DUKUHKUPANG, params);

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
                jsonObj = _JSONResponse.POSTResponse(ConfigManager.REGISTER, ConfigManager.DUKUHKUPANG, params);

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
            jsonObj = _JSONResponse.POSTPhone(ConfigManager.PHONE_NUMBER, token, ConfigManager.DUKUHKUPANG, params);

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
            jsonObj = _JSONResponse.POSTPhone(ConfigManager.VERIFY_PHONE_NUMBER, token, ConfigManager.DUKUHKUPANG, params);

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
            jsonObj = _JSONResponse.POSTResponse(ConfigManager.REFRESH_TOKEN, ConfigManager.DUKUHKUPANG, params);


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
            jsonObj = _JSONResponse.POSTDeviceToken(ConfigManager.DEVICE_TOKEN, token, ConfigManager.DUKUHKUPANG, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public String postResendVerifyCode(String token) {

        String jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            jsonObj = _JSONResponse.POSTResendVerifyCode(ConfigManager.RESEND_VERIFY_CODE, token, ConfigManager.DUKUHKUPANG, params);

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
            jsonObj = _JSONResponse.POSTForgotPassword(ConfigManager.FORGOT_PASSWORD, ConfigManager.DUKUHKUPANG, params);

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
            jsonObj = _JSONResponse.POSTForgotPassword(ConfigManager.CHECK_RESET_PASSWORD, ConfigManager.DUKUHKUPANG, params);

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
            jsonObj = _JSONResponse.POSTForgotPassword(ConfigManager.RESEND_RESET_PASSWORD, ConfigManager.DUKUHKUPANG, params);

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
            jsonObj = _JSONResponse.POSTForgotPassword(ConfigManager.RESET_PASSWORD, ConfigManager.DUKUHKUPANG, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public String postLogoutAll(String token) {

        String jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            jsonObj = _JSONResponse.POSTLogoutAll(ConfigManager.LOGOUT_ALL, token, ConfigManager.DUKUHKUPANG, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject postPromo() {
        JSONObject jsonObj = null;

        try {
            jsonObj = _JSONResponse.GETResponseHeader(ConfigManager.PROMO, ConfigManager.DUKUHKUPANG);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;

    }

    public JSONObject postPromoImage() {
        JSONObject jsonObj = null;

        try {
            Log.d("json url promo:",ConfigManager.PROMO_IMAGE);
            jsonObj = _JSONResponse.GETResponseHeader(ConfigManager.PROMO_IMAGE, ConfigManager.DUKUHKUPANG);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;

    }

    public JSONObject getHelp() {
        JSONObject jsonObj = null;

        try {
            jsonObj = _JSONResponse.GETResponseApp(ConfigManager.HELP, ConfigManager.DUKUHKUPANG);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;

    }



    public JSONObject getVersion() {
        JSONObject jsonObj = null;
        try{
            jsonObj = _JSONResponse.GETVersion(ConfigManager.CHECK_VERSION, ConfigManager.DUKUHKUPANG);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;


    }


    public JSONObject postXLTunai(String request_id, String partner_id, String customer_msisdn, String partner_tx_id, String amount, String created_on, String channel_id, String partner_pin, String parter_alias) {
        JSONObject jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("customer_msisdn", customer_msisdn));
            params.add(new BasicNameValuePair("partner_tx_id", partner_tx_id));
            params.add(new BasicNameValuePair("amount", amount));
            params.add(new BasicNameValuePair("customer_msisdn", customer_msisdn));
            params.add(new BasicNameValuePair("created_on", created_on));
            params.add(new BasicNameValuePair("channel_id", channel_id));
            params.add(new BasicNameValuePair("partner_pin", partner_pin));
            params.add(new BasicNameValuePair("parter_alias", parter_alias));
            jsonObj = _JSONResponse.POSTXLResponse(ConfigManager.XL_TUNAI, request_id, partner_id, ConfigManager.DUKUHKUPANG, params);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;

    }




}
