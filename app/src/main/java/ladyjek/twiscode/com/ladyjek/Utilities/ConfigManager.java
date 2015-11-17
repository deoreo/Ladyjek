package ladyjek.twiscode.com.ladyjek.Utilities;

public class ConfigManager {
    public static final int VERSION = 25;
    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String GEOCODE_API_BASE = "https://maps.googleapis.com/maps/api/geocode";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String OUT_JSON = "/json";
    public static final String API_KEY = "AIzaSyAJ6wF29SmwinrHoyJ-KjWwlZ3IFtVz0vY";
    public static final String URL_SUGGESTION = PLACES_API_BASE +"/autocomplete/json?components=country:id&key="+API_KEY+"&input=";

    //SERVER PRODUCTION
    public static final String SERVER = "https://ladyjek.com:2053/ladyjek/api";
    //SERVER TES
    //public static final String SERVER = "https://ladyjek.com:2083/ladyjek/api";
    //LOCAL TES
    //public static final String SERVER = "https://192.168.0.110:2053/ladyjek/api";

    public static final String LOGIN = SERVER+"/authenticate";

    public static final String REGISTER = SERVER+"/register";
    public static final String REFRESH_TOKEN = SERVER+"/refresh-token";
    public static final String PHONE_NUMBER = SERVER+"/phone-number";
    public static final String VERIFY_PHONE_NUMBER = SERVER+"/verify";
    public static final String RESEND_VERIFY_CODE = SERVER+"/resend-code";
    public static final String FORGOT_PASSWORD = SERVER+"/forgot";
    public static final String RESEND_RESET_PASSWORD = SERVER+"/forgot/resend-token";
    public static final String CHECK_RESET_PASSWORD = SERVER+"/forgot/check";
    public static final String RESET_PASSWORD = SERVER+"/forgot/reset";
    public static final String DEVICE_TOKEN = SERVER+"/device-token/create";
    public static final String LOGOUT_ALL = SERVER+"/logout-all";
    public static final String PROMO = SERVER+"/promo/init";
    public static final String PROMO_IMAGE = SERVER+"/promo/page";
    public static final String HELP = SERVER+"/help";
    public static final String CHECK_VERSION = SERVER+"/app/init";
    //SERVER PRODUCTION
    public static final String SERVER_SOCKET = "https://ladyjek.com:2096/"; //public
    //SERVER TES
    //public static final String SERVER_SOCKET = "https://ladyjek.com:2087/";
    //LOCAL TES
    //public static final String SERVER_SOCKET = "https://192.168.0.110:2096/";

    public static final String DUKUHKUPANG = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhcHBQYXNzd29yZCI6Ii8hPTxKVVY5S35zdGtUPFYiLCJpYXQiOjE0NDQ0MTIxODAsImV4cCI6MTQ0NzAwNDE4MH0.WpK0u4-CYMrkvxXjDVCjjzTBA8jC1cnSh-QWm0lFmYM";
}
