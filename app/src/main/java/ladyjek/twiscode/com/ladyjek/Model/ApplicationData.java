package ladyjek.twiscode.com.ladyjek.Model;


import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Unity on 19/05/2015.
 */
public class ApplicationData {

    public static String server = "http://192.168.1.101:3000";
    private static String id="1";
    private static String nama ="Edo";
    private static String email = "edo@gmail.com";
    private static String password = "abcd";
    private static String hp = "83857565559";

    public static String phoneNumber = "089682587567";

    public static String registered_id = "";
    public static String login_id = "";



    public static ModelUser modelUser = new ModelUser(id,nama,email,password,hp,null,null);
    public static ModelUser userLogin;
    public static String phone = "2789";
    public static Boolean editPhone = false;
    public static Boolean editHome = false;

    public static LatLng posFrom;
    public static LatLng posDestination;
    public static LatLng posDriver = new LatLng(-7.2855448024207226,112.7096968626571);

    public static Class act = null;

    public static String addressFrom="";
    public static String addressDestination="";
    public static String detailFrom="";
    public static String detailDestination="";
    public static String distance="";
    public static String duration="";
    public static  String price = "";





}
