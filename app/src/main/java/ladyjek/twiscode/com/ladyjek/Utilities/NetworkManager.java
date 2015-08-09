package ladyjek.twiscode.com.ladyjek.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {

    private Context _context;
    private static NetworkManager _instance = null;

    public NetworkManager(Context context) {
        this._context = context;
    }
    public static NetworkManager getInstance(Context ctx) {
        if (_instance == null)
            _instance = new NetworkManager(ctx);
        return _instance;
    }
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}

