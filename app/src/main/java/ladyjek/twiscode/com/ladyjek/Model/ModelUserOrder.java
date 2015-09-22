package ladyjek.twiscode.com.ladyjek.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Unity on 08/08/2015.
 */
public class ModelUserOrder implements Serializable {
    private String id, name, email, password, phone,phone2nd, payment, addressHome, homeLat, homeLon, addressOffice, officeLat, officeLon;
    private LatLng rumah, kantor;
    public ModelUserOrder(){}

    public ModelUserOrder(String id, String name, String email, String password, String hp, String hp2nd, LatLng kantor, LatLng rumah){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = hp;
        this.rumah = rumah;
        this.kantor = kantor;
        this.phone2nd = hp2nd;
    }

    public ModelUserOrder(String id, String name, String email, String password,
                          String phone, String hp2nd,String payment, String homeLat, String homeLon,
                          String officeLat, String officeLon, String addressHome, String addressOffice
    ){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.payment = payment;
        this.homeLat = homeLat;
        this.homeLon = homeLon;
        this.officeLat = officeLat;
        this.officeLon = officeLon;
        this.phone2nd = hp2nd;
        this.addressHome = addressHome;
        this.addressOffice = addressOffice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone2nd() {
        return phone2nd;
    }

    public void setPhone2nd(String phone) {
        this.phone2nd = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getHomeLat() {
        return homeLat;
    }

    public void setHomeLat(String homeLat) {
        this.homeLat = homeLat;
    }

    public String getHomeLon() {
        return homeLon;
    }

    public void setHomeLon(String homeLon) {
        this.homeLon = homeLon;
    }

    public String getOfficeLat() {
        return officeLat;
    }

    public void setOfficeLat(String officeLat) {
        this.officeLat = officeLat;
    }

    public String getOfficeLon() {
        return officeLon;
    }

    public void setOfficeLon(String officeLon) {
        this.officeLon = officeLon;
    }

    public LatLng getRumah() {
        return rumah;
    }

    public void setRumah(LatLng rumah) {
        this.rumah = rumah;
    }

    public LatLng getKantor() {
        return kantor;
    }

    public void setKantor(LatLng kantor) {
        this.kantor = kantor;
    }

    public String getAddressHome() {
        return addressHome;
    }

    public void setAddressHome(String addressHome) {
        this.addressHome = addressHome;
    }

    public String getAddressOffice() {
        return addressOffice;
    }

    public void setAddressOffice(String addressOffice) {
        this.addressOffice = addressOffice;
    }
}
