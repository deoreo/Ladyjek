package ladyjek.twiscode.com.ladyjek.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Unity on 08/08/2015.
 */
public class ModelDriver implements Serializable {
    public String id, name, image, nopol, phone, rate;
    public ModelDriver(){}

    public ModelDriver(String id, String name, String image, String nopol, String phone, String rate){
        this.id = id;
        this.name = name;
        this.image = image;
        this.nopol = nopol;
        this.phone = phone;
        this.rate = rate;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNopol() {
        return nopol;
    }

    public void setNopol(String nopol) {
        this.nopol = nopol;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
