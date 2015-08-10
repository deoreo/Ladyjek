package ladyjek.twiscode.com.ladyjek.Model;


import android.support.v4.app.Fragment;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedHashMap;
import java.util.Stack;

/**
 * Created by Unity on 19/05/2015.
 */
public class ApplicationData {

    private static String id="1";
    private static String nama ="Edo";
    private static String email = "edo@gmail.com";
    private static String password = "abcd";
    private static String hp = "83857565559";


    public static User user = new User(id,nama,email,password,hp,null,null);
    public static String phone = "2789";
    public static Boolean editPhone = false;
    public static Boolean editHome = false;




}
