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

    public JSONArray login(String addressInput) {
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




}
