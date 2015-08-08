package ladyjek.twiscode.com.ladyjek.Model;

/**
 * Created by User on 8/3/2015.
 */
public class ModelPlace {


    private String placeId;
    private String address;
    private String addressDetail;

    public ModelPlace(String placeId, String address, String addressDetail ) {
        this.placeId = placeId;
        this.address = address;
        this.addressDetail = addressDetail;
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
}
