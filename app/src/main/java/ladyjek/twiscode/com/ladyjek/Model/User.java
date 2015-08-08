package ladyjek.twiscode.com.ladyjek.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Unity on 08/08/2015.
 */
public class User {
    public String id, nama, email, password, hp;
    public LatLng rumah, kantor;

    public User(){}

    public User(String id, String nama, String email, String password, String hp, LatLng kantor, LatLng rumah){
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.hp = hp;
        this.rumah = rumah;
        this.kantor = kantor;
    }
}
