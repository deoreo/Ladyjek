package ladyjek.twiscode.com.ladyjek.Utilities;

public class ConfigManager {
    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String GEOCODE_API_BASE = "https://maps.googleapis.com/maps/api/geocode";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String OUT_JSON = "/json";
    public static final String API_KEY = "AIzaSyAJ6wF29SmwinrHoyJ-KjWwlZ3IFtVz0vY";
    public static final String URL_SUGGESTION = PLACES_API_BASE +"/autocomplete/json?components=country:id&key="+API_KEY+"&input=";
}
