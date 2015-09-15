package ladyjek.twiscode.com.ladyjek.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelDriver;
import ladyjek.twiscode.com.ladyjek.Model.ModelOrder;

/**
 * Created by Unity on 05/08/2015.
 */
public class SocketManager {
    private Socket socket;
    private Context context;
    private ApplicationManager appManager;
    private boolean onAuth = true;
    private int doLogout = 0;
    private final String TAG = "SocketManager";

    public void InitSocket(Context context) {
        Log.d(TAG, "InitSocket");
        this.context = context;
        this.appManager = new ApplicationManager(context);
        try {
            socket = IO.socket(ConfigManager.SERVER_SOCKET);
        } catch (URISyntaxException e) {
        }
    }


    public void Connect() {
        Log.d(TAG, "Connect");
        socket.on(Socket.EVENT_CONNECT, onConnected);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnected);
        socket.on("authenticated", onAuthenticated);
        socket.on("unauthorized", onUnauthorized);
        socket.on("last order", onLastOrder);
        socket.on("order taken",onOrderTaken);
        socket.on("order canceled",onOrderCanceled);
        socket.on("driver location change",onDriverChangeLocation);
        socket.on("order pickedup",onOrderPickUp);
        socket.on("order started",onOrderStarted);
        socket.on("order ended",onOrderEnded);
        socket.on("last feedback", onLastFeedback);
        socket.on("order timeout", onOrderTimeout);
        socket.connect();
    }

    public void doConnect(){
        socket.connect();
    }

    public void DriverChange(){
        socket.on("driver location change",onDriverChangeLocation);
    }

    public void Disconnect() {
        Log.d(TAG, "Disconnect");
        socket.off(Socket.EVENT_CONNECT, onConnected);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnected);
        socket.off("authenticated", onAuthenticated);
        socket.off("unauthorized", onUnauthorized);
        socket.off("last order", onLastOrder);
        socket.on("order taken", onOrderTaken);
        socket.off("order canceled", onOrderCanceled);
        socket.off("driver location change", onDriverChangeLocation);
        socket.off("order pickedup", onOrderPickUp);
        socket.off("order started", onOrderStarted);
        socket.off("order ended", onOrderEnded);
        socket.disconnect();
    }

    private Emitter.Listener onConnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG,"onConnected");
            JSONObject obj = new JSONObject();
            try {
                obj.put("token", appManager.getUserToken());
                Log.d(TAG,"onConnected token:"+obj.toString());
                socket.emit("authentication", obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private Emitter.Listener onDisconnected = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG,"onDisconnected "+onAuth);
            if (!onAuth) {
                socket.connect();
            }
            Log.d(TAG,"onDisconnected "+doLogout);
            if(doLogout==1){

                SendBroadcast("logout", "true");
                Log.d(TAG, "send logout");
            }
            else if(doLogout==2){
                SendBroadcast("unverify", "true");
                Log.d(TAG, "send unverify");
            }
        }
    };

    private Emitter.Listener onAuthenticated = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG,"onAuthenticated");
            onAuth = true;
            doLogout = 0;
        }
    };

    private Emitter.Listener onUnauthorized = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG,"onUnauthorized");
            onAuth = true;
            try {
                JSONObject obj = (JSONObject) args[0];
                if (obj != null) {
                    Log.d(TAG, "onUnauthorized " + obj.toString());
                    String msg = obj.getString("message");
                    if (msg.equalsIgnoreCase("jwt expired")) {
                        JSONControl jsControl = new JSONControl();
                        JSONObject response = jsControl.postRefreshToken(appManager.getUserToken());

                        try {
                            if(response != null){
                                Log.d(TAG, "onUnauthorized respose" + response.toString());
                                String token = response.getString("token");
                                appManager.setUserToken(token);
                                onAuth = false;
                                Log.d(TAG, "onUnauthorized true");
                            }
                            else {
                                Log.d(TAG, "null respose" + response.toString());
                                doLogout = 1;
                                //SendBroadcast("logout","true");
                            }

                        } catch (Exception e) {
                            //e.printStackTrace();
                            Log.d(TAG, "onUnauthorized false");
                            doLogout = 1;
                            //SendBroadcast("logout", "true");
                        }
                    }
                    else {
                        Log.d(TAG, "onUnauthorized logout");
                        //doLogout = true;
                    }

                    //SendBroadcast("logout", "true");
                } else {
                    Log.d(TAG, "onUnauthorized null order");
                    doLogout = 1;
                    //SendBroadcast("logout", "true");
                }

            } catch (Exception ex) {
                Log.d(TAG, "onUnauthorized error");
                doLogout = 1;
            }
        }
    };

    private Emitter.Listener onLastOrder = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG,"onLastOrder");
            try {
                //Log.d("Socket io",""+args[0].toString());
                JSONObject obj = (JSONObject) args[0];
                if (obj != null) {
                    Log.d(TAG, "onLastOrder : " + obj.toString());
                    String id = obj.getString("_id");
                    String userID = obj.getString("user");
                    String driverID = "";
                    String to = obj.getString("to");
                    String from = obj.getString("from");
                    String distance = obj.getJSONObject("distance").getString("text");
                    String duration = obj.getJSONObject("duration").getString("text");
                    String status = obj.getString("status");
                    if(!status.contains("queued")){
                        driverID = obj.getString("driver");
                    }
                    String toLatitude = obj.getJSONObject("toGeo").getJSONArray("coordinates").getString(1);
                    String toLongitude = obj.getJSONObject("toGeo").getJSONArray("coordinates").getString(0);
                    String fromLatitude = obj.getJSONObject("fromGeo").getJSONArray("coordinates").getString(1);
                    String fromLongitude = obj.getJSONObject("fromGeo").getJSONArray("coordinates").getString(0);
                    String name = "Nabila";
                    String payment = "TUNAI";
                    String member = "12 Feb 2015";
                    String phone = "089682587567";
                    String rate = "4.5";
                    String price = "";
                    ModelOrder order = new ModelOrder(id, userID, driverID, name, to, from, distance, duration, status, toLongitude, toLatitude, fromLatitude, fromLongitude, rate, phone, member, payment, price);
                    ApplicationData.order = order;
                    ApplicationData.driver = new ModelDriver();
                    SendBroadcast("lastOrder", "true");
                } else {
                    Log.d(TAG,"onLastOrder null order");
                    SendBroadcast("lastOrder", "false");
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    };


    private Emitter.Listener onOrderCanceled = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG,"onOrderCanceled");
            SendBroadcast("doCancel", "true");
        }
    };

    private Emitter.Listener onOrderTimeout = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG,"onOrderTimeout");
            SendBroadcast("doOrderTimeout", "true");
        }
    };

    private Emitter.Listener onOrderPickUp = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG,"onOrderPickUp");
            SendBroadcast("goTrip", "true");
        }
    };

    private Emitter.Listener onOrderStarted = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG,"onOrderStarted");
            SendBroadcast("goStart", "true");
        }
    };

    private Emitter.Listener onOrderEnded = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG,"onOrderEnded");
            SendBroadcast("goEnd", "true");
        }
    };

    private Emitter.Listener onDriverChangeLocation = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG, "onDriverChangeLocation");
            JSONArray data = (JSONArray)args[0];
            try {
                if(data != null){
                    ApplicationData.posDriver = new LatLng(data.getDouble(1),data.getDouble(0));
                    SendBroadcast("driverChange", "true");
                }
                else {
                    //SendBroadcast("goDriverChange", "false");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }


        }
    };

    private Emitter.Listener onOrderTaken = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "onOrderTaken");
            try {
                JSONObject obj = (JSONObject) args[0];
                Log.d("orderTaken", obj.toString());
                if(obj != null){
                    String id = obj.getString("_id");
                    String name = obj.getString("name");
                    String rate = obj.getString("rating");
                    String nopol = obj.getString("vehicleNumber");
                    String hp = obj.getString("phoneNumber");
                    String img = "";
                    JSONObject objPosGeo = obj.getJSONObject("posGeo");
                    JSONArray arrayCoodinates = objPosGeo.getJSONArray("coordinates");
                    Double lon = arrayCoodinates.getDouble(0);
                    Double lat = arrayCoodinates.getDouble(1);
                    LatLng posDriver = new LatLng(lat,lon);
                    ModelDriver driver = new ModelDriver(id,name,img,nopol,hp,rate);
                    ApplicationData.driver = driver;
                    ApplicationData.posDriver=posDriver;
                    SendBroadcast("onOrderTaken", "true");
                }

            }

            catch (Exception e){
                e.printStackTrace();
            }



        }
    };

    public void PostLocation(LatLng pos) {
        try {

                JSONArray loc = new JSONArray();
                loc.put(0, pos.longitude);
                loc.put(1, pos.latitude);
                socket.emit("post location", loc);
                //Log.d(TAG,"PostLocation active:"+ pos.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void CancelOrder(){
        Log.d(TAG, "cancel");
        socket.emit("post cancel order", new Ack() {
            @Override
            public void call(Object... args) {
                try {
                    Log.d(TAG, "cancel args:" + args[0]);
                    String order = args[0].toString();
                    if (order == null) {
                        Log.d(TAG, "cancel true");
                        SendBroadcast("doCancel", "true");
                    } else {
                        Log.d(TAG, "cancel false");
                        SendBroadcast("doCancel", "false");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

        });
    }

    public void CreateOrder(LatLng from, LatLng destination){
        Log.d(TAG, "create order");
        JSONObject objs = new JSONObject();
        JSONArray fr = new JSONArray();
        JSONArray dt = new JSONArray();
        try {
            fr.put(0,from.longitude);
            fr.put(1,from.latitude);
            dt.put(0,destination.longitude);
            dt.put(1,destination.latitude);
            objs.put("fromGeo",fr);
            objs.put("toGeo",dt);
            socket.emit("post create order",objs, new Ack() {
                @Override
                public void call(Object... args) {
                    try {
                        Log.d(TAG, "order args:" + args[1]);
                        JSONObject obj = (JSONObject) args[1];
                        if (obj == null) {
                            String id = obj.getString("_id");
                            String userID = obj.getString("user");
                            String driverID = obj.getString("driver");
                            String to = obj.getString("to");
                            String from = obj.getString("from");
                            String distance = obj.getJSONObject("distance").getString("text");
                            String duration = obj.getJSONObject("duration").getString("text");
                            String status = obj.getString("status");
                            String toLatitude = obj.getJSONObject("toGeo").getJSONArray("coordinates").getString(1);
                            String toLongitude = obj.getJSONObject("toGeo").getJSONArray("coordinates").getString(0);
                            String fromLatitude = obj.getJSONObject("fromGeo").getJSONArray("coordinates").getString(1);
                            String fromLongitude = obj.getJSONObject("fromGeo").getJSONArray("coordinates").getString(0);
                            String name = "Nabila";
                            String payment = "TUNAI";
                            String member = "12 Feb 2015";
                            String phone = "089682587567";
                            String rate = "4.5";
                            String price = "";
                            ModelOrder order = new ModelOrder(id, userID, driverID, name, to, from, distance, duration, status, toLongitude, toLatitude, fromLatitude, fromLongitude, rate, phone, member, payment, price);
                            ApplicationData.order = order;
                            //Log.d(TAG, "cancel true");
                            SendBroadcast("createOrder", "true");
                        } else {
                            //Log.d(TAG, "cancel false");
                            SendBroadcast("createOrder", "false");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Emitter.Listener onLastFeedback = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG, "onLastFeedback");
            String id = args[0].toString();
            if(id!=null){
                ApplicationData.driverID = id;
                SendBroadcast("lastFeedback", "true");
            }


        }
    };

    public void GetDriver(String id){
        Log.d(TAG, "getDriver : "+id);
        //JSONObject ret = new JSONObject();
        socket.emit("get driver", id, new Ack() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "getDriver cuy");
                try {
                    Log.d(TAG, "getDriver : ");
                    Log.d(TAG, "id args:" + args[0]);
                    Log.d(TAG, "id args:" + args[1]);
                    JSONObject err = (JSONObject) args[0];
                    if (err == null) {
                        Log.d(TAG, "no error getDriver : ");
                        JSONObject user = (JSONObject) args[1];
                        if (user != null) {
                            Log.d(TAG, "get driver true");
                            String id = user.getString("_id");
                            String name = user.getString("name");
                            String rate = user.getString("rating");
                            String nopol = user.getString("vehicleNumber");
                            String hp = user.getString("phoneNumber");
                            String img = "";
                            ModelDriver driver = new ModelDriver(id,name,img,nopol,hp,rate);
                            ApplicationData.driver = driver;
                            //ApplicationData.driver = user;
                            SendBroadcast("getDriver", "true");
                            //ret = user;
                        } else {
                            Log.d(TAG, "getDriver false");
                        }
                    } else {
                        Log.d(TAG, "getDriver null");
                    }
                } catch (Exception ex) {
                    Log.d(TAG, "catch getDriver : ");
                    ex.printStackTrace();
                }
            }

        });
        //return ApplicationData.driver;
    }

    public void GetLastOrder(){
        Log.d(TAG, "get lastorder");
        socket.emit("get last order",new Ack() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject err = (JSONObject) args[0];
                    if (err == null) {
                        JSONObject obj = (JSONObject) args[1];
                        Log.d(TAG, obj.toString());
                        if (obj != null) {
                            Log.d(TAG, "onLastOrder : " + obj.toString());
                            String id = obj.getString("_id");
                            String userID = obj.getString("user");
                            String driverID = "";
                            String to = obj.getString("to");
                            String from = obj.getString("from");
                            String distance = obj.getJSONObject("distance").getString("text");
                            String duration = obj.getJSONObject("duration").getString("text");
                            String status = obj.getString("status");
                            if(!status.contains("queued")){
                                driverID = obj.getString("driver");
                            }
                            String toLatitude = obj.getJSONObject("toGeo").getJSONArray("coordinates").getString(1);
                            String toLongitude = obj.getJSONObject("toGeo").getJSONArray("coordinates").getString(0);
                            String fromLatitude = obj.getJSONObject("fromGeo").getJSONArray("coordinates").getString(1);
                            String fromLongitude = obj.getJSONObject("fromGeo").getJSONArray("coordinates").getString(0);
                            String name = "Nabila";
                            String payment = "TUNAI";
                            String member = "12 Feb 2015";
                            String phone = "089682587567";
                            String rate = "4.5";
                            String price = "";
                            ModelOrder order = new ModelOrder(id, userID, driverID, name, to, from, distance, duration, status, toLongitude, toLatitude, fromLatitude, fromLongitude, rate, phone, member, payment, price);
                            ApplicationData.order = order;
                            ApplicationData.driver = new ModelDriver();
                            SendBroadcast("lastOrder", "true");
                        } else {
                            Log.d(TAG,"onLastOrder null order");
                            SendBroadcast("lastOrder", "false");
                        }
                    } else {
                        Log.d(TAG, "getfeedback false");
                    }
                } catch (Exception ex) {
                    Log.d(TAG, "getfeedback error");
                    ex.printStackTrace();
                }

            }

        });
    }

    public void Feedback(int rate, String description){
        Log.d(TAG, "feedback");
        //boolean feed = false;
        JSONObject obj = new JSONObject();
        try {
            obj.put("rate",rate);
            obj.put("description",description);
            socket.emit("post feedback", obj, new Ack() {
                @Override
                public void call(Object... args) {
                    try {
                        JSONObject err = (JSONObject) args[0];
                        if (err == null) {
                            Log.d(TAG, "getfeedback true");
                            SendBroadcast("doFeedback","true");
                        } else {
                            Log.d(TAG, "getfeedback false");
                        }
                    } catch (Exception ex) {
                        Log.d(TAG, "getfeedback error");
                        ex.printStackTrace();
                    }

                }

            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }


    }






    private void SendBroadcast(String typeBroadcast,String type){
        Intent intent = new Intent(typeBroadcast);
        intent.putExtra("message", type);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
