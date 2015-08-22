package ladyjek.twiscode.com.ladyjek.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

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
            //Log.d("socket io",e.toString());
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

    public void InitRoom(Socket sock,String room){
        sock.on(Socket.EVENT_CONNECT, onConnectedRooms);
        sock.on(Socket.EVENT_CONNECT_ERROR, onConnectErrorRoom);
        sock.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeOutRoom);
        sock.on("group message", onNewGroupMessage);
        rooms.put(room, sock);
        sock.connect();
    }

    public void JoinRoom(){
        ArrayList<Socket> room = new ArrayList<>(rooms.values());
        for(int i=0;i<rooms.size();i++){
            room.get(i).connect();
        }
    }

    public void LeaveRoom(String room){
        Socket sRoom = rooms.get(room);
        sRoom.off(Socket.EVENT_CONNECT);
        sRoom.off(Socket.EVENT_CONNECT_ERROR);
        sRoom.off(Socket.EVENT_CONNECT_TIMEOUT);
        sRoom.off("group message");
        sRoom.disconnect();
        rooms.remove(room);
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
            /*Log.d("Socket io", "Authenticated");
            SendBroadcast("auth", "auth");
            Log.d("socket io", "send broadcast");
            ArrayList<String> grup = new ArrayList<>(ApplicationData.grup.keySet());
            for(int i=0;i<grup.size();i++){
                try {
                    Socket sRoom = IO.socket(ApplicationData.room+grup.get(i));
                    InitRoom(sRoom, grup.get(i));
                    //Log.d("socket io", "room "+grup.get(i));
                } catch (URISyntaxException e) {
                    //Log.d("socket io",e.toString());
                }


            }
            */
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

                    socket.emit("authentication", obj);
                }
            });

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
