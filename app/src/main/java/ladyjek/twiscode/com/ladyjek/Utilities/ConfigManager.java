package ladyjek.twiscode.com.ladyjek.Utilities;

public class ConfigManager {
    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String GEOCODE_API_BASE = "https://maps.googleapis.com/maps/api/geocode";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String OUT_JSON = "/json";
    public static final String API_KEY = "AIzaSyAJ6wF29SmwinrHoyJ-KjWwlZ3IFtVz0vY";
    public static final String URL_SUGGESTION = PLACES_API_BASE +"/autocomplete/json?components=country:id&key="+API_KEY+"&input=";
    public static final String SERVER = "https://playpal.id:3001/ladyjek/api";
    public static final String LOGIN = SERVER+"/authenticate";

    public static final String REGISTER = SERVER+"/register";
    public static final String REFRESH_TOKEN = SERVER+"/refresh-token";
    public static final String PHONE_NUMBER = SERVER+"/phone-number";
    public static final String VERIFY_PHONE_NUMBER = SERVER+"/verify";
    public static final String RESEND_VERIFY_CODE = SERVER+"/resend-code";
    public static final String DEVICE_TOKEN = SERVER+"/device-token/create";
    public static final String SERVER_SOCKET = "https://playpal.id:5051/";
}
