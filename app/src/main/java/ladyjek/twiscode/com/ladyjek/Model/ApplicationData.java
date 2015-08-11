package ladyjek.twiscode.com.ladyjek.Model;


/**
 * Created by Unity on 19/05/2015.
 */
public class ApplicationData {

    private static String id="1";
    private static String nama ="Edo";
    private static String email = "edo@gmail.com";
    private static String password = "abcd";
    private static String hp = "83857565559";

    public static String registered_id = "";
    public static String login_id = "";



    public static ModelUser modelUser = new ModelUser(id,nama,email,password,hp,null,null);
    public static ModelUser userLogin;
    public static String phone = "2789";
    public static Boolean editPhone = false;
    public static Boolean editHome = false;




}
