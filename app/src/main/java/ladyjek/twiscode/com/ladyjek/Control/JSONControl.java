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

    public JSONObject postLogin(String email, String password) {
        JSONObject jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            jsonObj = _JSONResponse.POSTResponse(ConfigManager.LOGIN, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;

    }

    public JSONObject postRegister(String email, String password) {

        JSONObject jsonObj = null;

        try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
                jsonObj = _JSONResponse.POSTResponse(ConfigManager.REGISTER, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public String postPhone(String id, String phone) {

        String jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", id));
            params.add(new BasicNameValuePair("phoneNumber", phone));
            jsonObj = _JSONResponse.POSTResponseString(ConfigManager.PHONE_NUMBER, params);

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





}
