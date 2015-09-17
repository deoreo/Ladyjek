package ladyjek.twiscode.com.ladyjek.Model;

/**
 * Created by ModelUser on 8/3/2015.
 */
public class ModelGeocode {


    public String lat;
    public String lon;
    public Double latitude;
    public Double longitude;

    public ModelGeocode(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public ModelGeocode(Double lat, Double lon) {
        this.latitude = lat;
        this.longitude = lon;
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

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
