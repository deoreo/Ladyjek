package ladyjek.twiscode.com.ladyjek.Model;


import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Unity on 19/05/2015.
 */
public class ApplicationData {

    public static String server = "http://playpal.id:5051/";
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
    public static LatLng posDriver = new LatLng(-7.26545746,112.70968597);

    public static Class act = null;

    public static String addressFrom="";
    public static String addressDestination="";
    public static String detailFrom="";
    public static String detailDestination="";
    public static String distance="";
    public static String duration="";
    public static  String price = "";


    public static final String PARSE_CHANNEL = "ladyjek";
    public static final String PARSE_APPLICATION_ID = "OqKceLUMcPo769myUmHlAlNLNGa0PxWgu6xNEYRD";
    public static final String PARSE_CLIENT_KEY = "WVJeHzaOwLUH5pQHTwnk9laqfQOrWIdRSPzcbGpC";
    public static final int NOTIFICATION_ID = 100;






}
