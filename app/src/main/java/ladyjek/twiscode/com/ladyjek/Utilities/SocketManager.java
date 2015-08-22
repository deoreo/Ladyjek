package ladyjek.twiscode.com.ladyjek.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;

/**
 * Created by Unity on 05/08/2015.
 */
public class SocketManager {
    private Socket socket;
    private HashMap<String,Socket> rooms = new HashMap<>();
    private Activity act;

    public void InitSocket(Activity act){
        this.act = act;
        try {
            socket = IO.socket(ApplicationData.server);
        } catch (URISyntaxException e) {
        }

    }

    public void Connect(){
        socket.on("unauthorized", onUnauthorized);
        socket.on("authenticated", onAuthenticated);
        socket.on(Socket.EVENT_CONNECT, onConnected);
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeOut);
        socket.connect();
    }

    public void Disconnect(){
        socket.off("unauthorized");
        socket.off("authenticated");
        socket.off(Socket.EVENT_CONNECT);
        socket.off(Socket.EVENT_CONNECT_ERROR);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT);

        socket.disconnect();
    }



    private Emitter.Listener onUnauthorized = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //Log.d("Socket io", "Unauthorized");
        }
    };

    private Emitter.Listener onAuthenticated = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("Socket io", "Authenticated");
        }
    };

    private Emitter.Listener onConnectTimeOut = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });

        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });

        }
    };

    private Emitter.Listener onConnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("token", ApplicationManager.getInstance(act).getUserToken());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socket.emit("post create order", obj);
                }
            });

        }
    };


    private Emitter.Listener onLastOrder = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject obj = new JSONObject();
            try {
                String[] fromGeo = new String[2];
                String[] toGeo = new String[2];
                fromGeo[0] = ""+ApplicationData.posFrom.longitude;
                fromGeo[1] =  ""+ApplicationData.posFrom.latitude;
                toGeo[0] = ""+ApplicationData.posDestination.longitude;
                toGeo[1] =  ""+ApplicationData.posDestination.latitude;
                obj.put("fromGeo", fromGeo);
                obj.put("toGeo", toGeo);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("authentication", obj);
        }
    };



    private Emitter.Listener onConnectedRooms = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Log.d("socket io", "rooms connected");
                }
            });

        }
    };

    private Emitter.Listener onConnectTimeOutRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Log.d("Socket io", "Rooms Time Out");
                }
            });

        }
    };

    private Emitter.Listener onConnectErrorRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Log.d("Socket io", "Rooms Error");
                }
            });

        }
    };

    private Emitter.Listener onNewGroupMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Log.d("Socket io", "New Group Message");
                }
            });

        }
    };

    private void SendBroadcast(String typeBroadcast,String type){
        Intent intent = new Intent(typeBroadcast);
        intent.putExtra("message", type);
        LocalBroadcastManager.getInstance(act).sendBroadcast(intent);
    }
}
