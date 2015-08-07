package ladyjek.twiscode.com.ladyjek.Model;

/**
 * Created by User on 8/3/2015.
 */
public class ModelGeocode {


    public String lat;
    public String lon;

    public ModelGeocode(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "( lat: "+lat+", lon: "+lon+" )";
    }

    public Double getLat() {
        double lat = Double.parseDouble(this.lat);
        return lat;
    }

    public Double getLon() {
        double lon = Double.parseDouble(this.lon);
        return lon;
    }
}
