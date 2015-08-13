package ladyjek.twiscode.com.ladyjek.Model;

/**
 * Created by ModelUser on 8/3/2015.
 */
public class ModelPlace {


    private String placeId="";
    private String address="";
    private String addressDetail="";
    private Double latitude;
    private Double longitude;

    public ModelPlace(String placeId, String address, String addressDetail ) {
        this.placeId = placeId;
        this.address = address;
        this.addressDetail = addressDetail;
    }

    public ModelPlace(Double latitude, Double longitude){
        this.latitude = longitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return address.toString();
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
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
