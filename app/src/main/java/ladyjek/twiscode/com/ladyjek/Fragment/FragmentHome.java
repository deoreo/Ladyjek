package ladyjek.twiscode.com.ladyjek.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ladyjek.twiscode.com.ladyjek.Activity.ActivityConfirm;
import ladyjek.twiscode.com.ladyjek.Activity.ActivityHandphoneKonfirmasi;
import ladyjek.twiscode.com.ladyjek.Activity.ActivityLoading;
import ladyjek.twiscode.com.ladyjek.Activity.ActivityLogin;
import ladyjek.twiscode.com.ladyjek.Activity.ActivityPickUp;
import ladyjek.twiscode.com.ladyjek.Activity.ActivityPromoWebView;
import ladyjek.twiscode.com.ladyjek.Activity.ActivityRate;
import ladyjek.twiscode.com.ladyjek.Activity.ActivityRegister;
import ladyjek.twiscode.com.ladyjek.Activity.ActivityTracking;
import ladyjek.twiscode.com.ladyjek.Activity.ActivityVerifyPayment;
import ladyjek.twiscode.com.ladyjek.Activity.Main;
import ladyjek.twiscode.com.ladyjek.Adapter.AdapterAddress;
import ladyjek.twiscode.com.ladyjek.Adapter.AdapterSuggestion;
import ladyjek.twiscode.com.ladyjek.Control.JSONControl;
import ladyjek.twiscode.com.ladyjek.Database.DatabaseHandler;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelDriver;
import ladyjek.twiscode.com.ladyjek.Model.ModelGeocode;
import ladyjek.twiscode.com.ladyjek.Model.ModelOrder;
import ladyjek.twiscode.com.ladyjek.Model.ModelPlace;
import ladyjek.twiscode.com.ladyjek.Model.ModelUserOrder;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.GoogleAPIManager;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.MySupportMapFragment;
import ladyjek.twiscode.com.ladyjek.Utilities.NetworkManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import org.droidparts.widget.ClearableEditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * ladyjek.twiscode.com.ladyjek.Utilities
 * Created by Unity on 18/05/2015.
 */
public class FragmentHome extends Fragment implements GoogleMap.OnMapClickListener {

    private Toolbar mToolbar;
    ModelPlace mPlace, selectedPlaceFrom, selectedPlaceDestination;
    private List<ModelPlace> LIST_PLACE = null;
    private static final String KEY_ID = "place_id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DETAIL = "detail";

    private GoogleMap googleMap;
    private TextView btnRequestRide, txtAddressCurrent;
    private final String TAG_FROM = "FROM";
    private final String TAG_DESTINATION = "DESTINATION";
    private ClearableEditText txtFrom, txtDestination;
    private TextView txtHint;
    private String add, tagLocation;
    private String placeId = "", description = "", strDistance = "", strDuration = "", strDetailFrom = "a", strDetailDestination = "b";

    private LatLng mapCenter, posFrom, posDest, posTemp, posDriver;
    private AdapterAddress mPlaceArrayAdapter;
    private Marker markerFrom, markerDestination = null, markerDrivers;
    public static Marker markerTemp;
    private CameraUpdate cameraUpdate;
    private Polyline driveLine;
    private Circle mapCircle;
    public static Activity mActivity;
    private ProgressBar pSuggestion;
    private AdapterSuggestion mAdapter;
    private static RelativeLayout layoutSuggestion, wrapperRegister, mapWrapper;
    private LinearLayout layoutfillForm;
    private ImageView btnCurrent;
    public static LinearLayout layoutMarkerFrom, layoutMarkerDestination;
    private FrameLayout itemCurrent;
    private ListView mListView;
    private Button btnLocationFrom, btnLocationDestination;
    private Location location;
    private TextView txtLocationFrom, txtLocationDestination, txtDriverTime;
    private ProgressBar progressMapFrom, progressMapDestination;
    private SupportMapFragment fm;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000 * 60 * 1;
    private static final long MIN_TIME_BW_UPDATES = 1;
    public static boolean mTouchMap = true;
    private boolean isGetNearestDrivers = false;
    private ApplicationManager appManager;
    private SocketManager socketManager;
    private BroadcastReceiver createOrder, lastOrder, lastFeedback, logout, nearestDrivers;
    //private ServiceLocation serviceLocation;
    private Runnable mRunnable;
    Handler mHandler = new Handler();
    boolean isRunning = true;
    private int AUTOUPDATE_INTERVAL_TIME = 1 * 1000;
    private int duration = 9999;
    private DatabaseHandler db;
    private final String TAG = "FragmentHome";
    private LatLng[] driverArray = null;
    private boolean isSearchCurrent = false;
    private ProgressDialog progressDialog;
    private Circle mCircle;
    private Marker markerCurrent;
    int count = 0;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "OnCreate");
        mActivity = getActivity();
        appManager = new ApplicationManager(mActivity);
        db = new DatabaseHandler(mActivity);
        posFrom = ApplicationData.posFrom;
        tagLocation = TAG_FROM;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_transport4, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.v(TAG, "OnCreateView");
        ApplicationData.driver = new ModelDriver();
        btnRequestRide = (TextView) rootView.findViewById(R.id.btnRequestRide);
        txtFrom = (ClearableEditText) rootView.findViewById(R.id.txtFrom);
        txtDestination = (ClearableEditText) rootView.findViewById(R.id.txtDestination);
        mListView = (ListView) rootView.findViewById(R.id.lvSuggestion);
        pSuggestion = (ProgressBar) rootView.findViewById(R.id.progressSuggestion);
        layoutSuggestion = (RelativeLayout) rootView.findViewById(R.id.layoutSuggestion);
        itemCurrent = (FrameLayout) rootView.findViewById(R.id.itemCurrent);
        txtAddressCurrent = (TextView) rootView.findViewById(R.id.txtAddressCurrent);
        wrapperRegister = (RelativeLayout) rootView.findViewById(R.id.wrapperRegister);
        layoutfillForm = (LinearLayout) rootView.findViewById(R.id.layoutfillForm);
        btnLocationFrom = (Button) rootView.findViewById(R.id.btnLocationFrom);
        btnLocationDestination = (Button) rootView.findViewById(R.id.btnLocationDestination);
        layoutMarkerFrom = (LinearLayout) rootView.findViewById(R.id.layoutMarkerFrom);
        layoutMarkerDestination = (LinearLayout) rootView.findViewById(R.id.layoutMarkerDestination);
        mapWrapper = (RelativeLayout) rootView.findViewById(R.id.mapWrapper);
        btnCurrent = (ImageView) rootView.findViewById(R.id.btnCurrent);
        progressMapFrom = (ProgressBar) rootView.findViewById(R.id.progressMapFrom);
        progressMapDestination = (ProgressBar) rootView.findViewById(R.id.progressMapDestination);
        txtLocationDestination = (TextView) rootView.findViewById(R.id.txtLocationDestination);
        txtDriverTime = (TextView) rootView.findViewById(R.id.txtDriverTime);
        txtLocationFrom = (TextView) rootView.findViewById(R.id.txtLocationFrom);
        btnRequestRide.setText(Html.fromHtml(getResources().getString(R.string.pesan)));
        txtHint= (TextView) rootView.findViewById(R.id.txtHint);


        if (ApplicationData.socketManager == null) {
            socketManager = new SocketManager();
            socketManager.InitSocket(mActivity);
            socketManager.Connect();

            ApplicationData.socketManager = socketManager;
        } else {
            socketManager = ApplicationData.socketManager;
        }
        createOrder = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v(TAG, "broadcast createOrder");
                Intent i = new Intent(mActivity, ActivityLoading.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(i);
                mActivity.finish();
            }
        };


        MySupportMapFragment fm = (MySupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        googleMap = fm.getMap();
        try {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-6.1995921, 106.872451), 10f));
        } catch (Exception e) {

        }
        mHandler = new Handler();
        //serviceLocation = new ServiceLocation(mActivity);

        if (NetworkManager.getInstance(mActivity).isConnectedInternet()) {
            new GetMyLocation(mActivity, googleMap, socketManager).execute();
        } else {
            DialogManager.showDialog(mActivity, "Mohon Maaf", "Anda tidak terhubung internet!");
        }

        if (ApplicationData.posFrom != null && ApplicationData.posDestination != null) {
            posFrom = ApplicationData.posFrom;
            posDest = ApplicationData.posDestination;
            txtFrom.setText(getAddress(ApplicationData.posFrom).toString());
            txtDestination.setText(getAddress(ApplicationData.posDestination).toString());
            drawLine();


        }


        wrapperRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strDistance.isEmpty() && strDuration.isEmpty()) {
                    DialogManager.showDialog(mActivity, "Mohon Maaf", "Tentukan lokasi awal dan akhir!");
                } else {
                    ApplicationData.addressFrom = txtFrom.getText().toString();
                    ApplicationData.addressDestination = txtDestination.getText().toString();
                    ApplicationData.detailFrom = strDetailFrom;
                    ApplicationData.detailDestination = strDetailDestination;
                    ApplicationData.distance = strDistance;
                    ApplicationData.duration = strDuration;
                    Intent intent = new Intent(mActivity, ActivityConfirm.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();


                    // /new DoPesan(mActivity,socketManager, posFrom, posDest);
                }
            }
        });
        layoutfillForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnRequestRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strDistance.isEmpty() && strDuration.isEmpty()) {
                    DialogManager.showDialog(mActivity, "Mohon Maaf", "Tentukan lokasi awal dan akhir!");
                } else {
                    ApplicationData.addressFrom = txtFrom.getText().toString();
                    ApplicationData.addressDestination = txtDestination.getText().toString();
                    ApplicationData.detailFrom = strDetailFrom;
                    ApplicationData.detailDestination = strDetailDestination;
                    ApplicationData.distance = strDistance;
                    ApplicationData.duration = strDuration;
                    Intent intent = new Intent(mActivity, ActivityConfirm.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        txtFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //do stuff here
                    tagLocation = TAG_FROM;
                    mTouchMap = false;
                    isSearchCurrent = false;
                    txtHint.setText("Click pada map untuk memilih lokasi awal");
                    Log.v(TAG, tagLocation);

                }
                return false;
            }
        });

        txtDestination.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //do stuff here
                    tagLocation = TAG_DESTINATION;
                    mTouchMap = false;
                    isSearchCurrent = false;
                    txtHint.setText("Click pada map untuk memilih lokasi akhir");
                    Log.v(TAG, tagLocation);
                }


                return false;
            }
        });

        layoutSuggestion.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (itemCurrent.getVisibility() == GONE)
                        layoutSuggestion.setVisibility(GONE);
                }
                return false;
            }
        });


        mapWrapper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v(TAG, "Map Touch");
                layoutMarkerFrom.setVisibility(GONE);
                layoutMarkerDestination.setVisibility(GONE);
                return false;
            }
        });

        btnCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnCurrent click");
                isSearchCurrent = true;
                if (!socketManager.isConnected()) {
                    socketManager.Connect();
                }

                if (NetworkManager.getInstance(mActivity).isConnectedInternet()) {
                    new GetMyLocation(mActivity, googleMap, socketManager).execute();
                } else {
                    DialogManager.showDialog(mActivity, "Mohon Maaf", "Anda tidak terhubung internet!");
                }
            }
        });

        txtFrom.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    mTouchMap = false;
                    handled = true;
                }
                return handled;
            }
        });

        txtFrom.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (txtFrom.getText().toString().equals("")) {
                    if (markerFrom != null) {
                        markerFrom.remove();
                        posFrom = null;
                    }
                    if (driveLine != null) {
                        driveLine.remove();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!mTouchMap) {
                    if (s.length() >= 3) {
                        if (NetworkManager.getInstance(mActivity).isConnectedInternet()) {
                            new GetSuggestion(s.toString(), tagLocation).execute();
                        }
                    } else if (s.length() == 0) {
                        layoutSuggestion.setVisibility(GONE);
                    }
                }


            }
        });
        txtDestination.addTextChangedListener(new TextWatcher() {

                                                  @Override
                                                  public void afterTextChanged(Editable s) {
                                                      if (txtDestination.getText().toString().equals("")) {
                                                          if (markerDestination != null) {
                                                              markerDestination.remove();
                                                              posDest = null;
                                                          }
                                                          if (driveLine != null) {
                                                              driveLine.remove();
                                                          }
                                                      }
                                                  }

                                                  @Override
                                                  public void beforeTextChanged(CharSequence s, int start,
                                                                                int count, int after) {
                                                  }

                                                  @Override
                                                  public void onTextChanged(CharSequence s, int start,
                                                                            int before, int count) {
                                                      if (!mTouchMap) {
                                                          if (s.length() >= 3) {
                                                              if (NetworkManager.getInstance(mActivity).isConnectedInternet()) {
                                                                  new GetSuggestion(s.toString(), tagLocation).execute();
                                                              }
                                                          } else if (s.length() == 0) {
                                                              layoutSuggestion.setVisibility(GONE);
                                                              if (markerDestination != null) {
                                                                  markerDestination.remove();
                                                              }
                                                              if (driveLine != null) {
                                                                  driveLine.remove();
                                                              }

                                                          }
                                                      }
                                                  }


                                              }

        );


        itemCurrent.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick(View v) {

                                               if (txtFrom.getText().toString().isEmpty() || txtDestination.getText().toString().isEmpty()) {
                                                   if (tagLocation.equals(TAG_FROM)) {
                                                       txtFrom.setText(txtAddressCurrent.getText().toString());
                                                   }
                                                   if (tagLocation.equals(TAG_DESTINATION)) {
                                                       txtDestination.setText(txtAddressCurrent.getText().toString());

                                                   }
                                               }
                                               hideKeyboard();
                                               layoutSuggestion.setVisibility(GONE);

                                           }
                                       }

        );

        btnLocationFrom.setOnClickListener(new View.OnClickListener()

                                           {
                                               @Override
                                               public void onClick(View v) {
                                                   posFrom = posTemp;
                                                   if (driveLine != null) {
                                                       driveLine.remove();
                                                   }
                                                   if (markerFrom != null) {
                                                       markerFrom.remove();
                                                   }
                                                   if (markerTemp != null) {
                                                       markerTemp.remove();
                                                   }
                                                   layoutMarkerDestination.setVisibility(GONE);
                                                   layoutMarkerFrom.setVisibility(GONE);
                                                   String address = getAddress(posFrom);
                                                   txtFrom.setText(address);
                                                   strDetailFrom = address;
                                                   ApplicationData.posFrom = posFrom;
                                                   appManager.setUserFrom(new ModelPlace(posFrom.latitude, posFrom.longitude));


                                                   markerFrom = googleMap.addMarker(
                                                           new MarkerOptions()
                                                                   .position(posFrom)
                                                                   .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));

                                                   if (posFrom != null && posDest != null) {
                                                       new DoDrawRute(mActivity,posFrom,posDest,googleMap).execute();
                                                   }

                                               }
                                           }

        );

        btnLocationDestination.setOnClickListener(new View.OnClickListener()

                                                  {
                                                      @Override
                                                      public void onClick(View v) {
                                                          posDest = posTemp;
                                                          if (driveLine != null) {
                                                              driveLine.remove();
                                                          }
                                                          if (markerDestination != null) {
                                                              markerDestination.remove();
                                                          }
                                                          if (markerTemp != null) {
                                                              markerTemp.remove();
                                                          }
                                                          layoutMarkerDestination.setVisibility(GONE);
                                                          layoutMarkerFrom.setVisibility(GONE);
                                                          String address = getAddress(posDest);
                                                          txtDestination.setText(address);
                                                          strDetailDestination = address;
                                                          ApplicationData.posDestination = posDest;
                                                          appManager.setUserDestination(new ModelPlace(posDest.latitude, posDest.longitude));
                                                          markerDestination = googleMap.addMarker(
                                                                  new MarkerOptions()
                                                                          .position(posDest)
                                                                          .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));

                                                          if (posFrom != null && posDest != null) {
                                                              new DoDrawRute(mActivity,posFrom,posDest,googleMap).execute();
                                                          }

                                                      }
                                                  }

        );

        googleMap.setOnMapClickListener(this);
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener()

                                            {
                                                @Override
                                                public void onCameraChange(CameraPosition cameraPosition) {
                                                    mapCenter = googleMap.getCameraPosition().target;
                                                    add = getAddress(mapCenter);
                                                    txtLocationFrom.setText(add);
                                                    txtLocationDestination.setText(add);
                                                    progressMapFrom.setVisibility(View.GONE);
                                                    progressMapDestination.setVisibility(View.GONE);
                                                }
                                            }

        );


        lastOrder = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.v(TAG, "broadcast lastOrder");
                DialogManager.DismissLoading(mActivity);
                String message = intent.getStringExtra("message");
                Intent i = null;
                String pref = appManager.getActivity();
                if (message.equals("true")) {
                    ApplicationData.socketManager = socketManager;
                    appManager.setOrder(ApplicationData.order);
                    ApplicationData.driver = null;
                    if (ApplicationData.order.getStatus().contains("taken")) {
                        i = new Intent(getActivity(), ActivityPickUp.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        getActivity().finish();
                    } else if (ApplicationData.order.getStatus().contains("pickedup")) {
                        i = new Intent(getActivity(), ActivityPickUp.class);
                        appManager.setTrip("started");
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        getActivity().finish();
                    } else if (ApplicationData.order.getStatus().contains("started")) {
                        i = new Intent(getActivity(), ActivityTracking.class);
                        appManager.setTrip("end");
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        getActivity().finish();
                    } else if (ApplicationData.order.getStatus().contains("queued")) {
                        i = new Intent(getActivity(), ActivityLoading.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        getActivity().finish();
                    } else if (ApplicationData.order.getStatus().contains("pending")) {
                        i = new Intent(getActivity(), ActivityVerifyPayment.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        getActivity().finish();
                    } else if (ApplicationData.order.getStatus().contains("ended")) {
                        i = new Intent(getActivity(), ActivityRate.class);
                        appManager.setTrip("");
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        getActivity().finish();
                    } else {
                        //GetNearestDriver(mActivity);
                    }

                } else {
                    DialogManager.DismissLoading(mActivity);
                    Log.v(TAG, "Ga ada last order coy...");
                    //GetNearestDriver(mActivity);
                }

            }
        };


        lastFeedback = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.v(TAG, "broadcast lastFeedback");
                String message = intent.getStringExtra("message");

                if (message.equalsIgnoreCase("true")) {
                    ApplicationData.driver = null;
                    appManager.setOrder(null);
                    Intent i = new Intent(getActivity(), ActivityRate.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    getActivity().finish();
                }
            }
        };

        nearestDrivers = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.v(TAG, "broadcast nearestDrivers");
                String message = intent.getStringExtra("message");
                if (message.equalsIgnoreCase("true")) {
                    try {
                        Log.d(TAG, "nearest drivers" + message);
                        JSONArray drivers = ApplicationData.nearestDrivers;
                        ApplicationData.posDrivers = new LatLng[ApplicationData.nearestDrivers.length()];

                        if (ApplicationData.posDrivers != null) {
                            for (int i = 0; i < ApplicationData.posDrivers.length; i++) {
                                Double lon = drivers.getJSONArray(i).getDouble(1);
                                Double lat = drivers.getJSONArray(i).getDouble(0);
                                ApplicationData.posDrivers[i] = new LatLng(lon, lat);
                                //Log.d(TAG, "getNearestDrivers ApplicationData.posDrivers["+i+"] : " + ApplicationData.posDrivers[i]);
                            }
/*
                            for (int i = 0; i < ApplicationData.posDrivers.length; i++) {
                                drawMarkerNearestDriver(posFrom, ApplicationData.posDrivers[i]);
                            }
*/


                        }
                        DialogManager.DismissLoading(mActivity);
                        if (ApplicationData.posDrivers.length > 0) {
                            DoDraw();
                        }
                    } catch (Exception e) {

                    }
                }
            }
        };

        logout = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.v(TAG, "broadcast logout");
                String message = intent.getStringExtra("message");
                if (message.equalsIgnoreCase("true")) {
                    try {
                        new AlertDialogWrapper.Builder(mActivity)
                                .setTitle("Token expired! silahkan login kembali")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        appManager.logoutUser();
                                        db.logout();
                                        Intent i = new Intent(getActivity(), ActivityLogin.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        getActivity().finish();
                                    }
                                })
                                .setIcon(R.drawable.ladyjek_icon)
                                .show();
                    } catch (Exception ex) {

                    }


                }
            }
        };

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks whether a keyboard is available
        if (newConfig.KEYBOARDHIDDEN_NO == Configuration.KEYBOARDHIDDEN_NO) {
            Log.v("keyboard", "show");
        } else if (newConfig.KEYBOARDHIDDEN_YES == Configuration.KEYBOARDHIDDEN_YES) {
            Log.v("keyboard", "hidden");
        }
    }


    public void drawNewMarker() {
        float zoom = googleMap.getCameraPosition().zoom;
        if (zoom <= 15) {
            zoom = 15;
        }
        try {
            //ModelGeocode geocode = GoogleAPIManager.geocode(address);
            //LatLng locationMarker = new LatLng(geocode.getLat(), geocode.getLon());
            if (driveLine != null) {
                driveLine.remove();
            }
            if (tagLocation.equals(TAG_FROM)) {
                posFrom = ApplicationData.posFrom;
                markerFrom = googleMap.addMarker(
                        new MarkerOptions()
                                .position(posFrom)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));

                cameraUpdate = CameraUpdateFactory.newLatLngZoom(posFrom, zoom);
                googleMap.animateCamera(cameraUpdate);
            } else if (tagLocation.equals(TAG_DESTINATION)) {
                posDest = ApplicationData.posDestination;
                markerDestination = googleMap.addMarker(
                        new MarkerOptions()
                                .position(posDest)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(posDest, zoom);
                googleMap.animateCamera(cameraUpdate);
            }
            new DoDrawRute(mActivity,posFrom,posDest,googleMap).execute();

        } catch (Exception e) {

        }

    }


    public void drawLine() {
        float zoom = googleMap.getCameraPosition().zoom;
        if (zoom <= 15) {
            zoom = 15;
        }
        try {
            if (driveLine != null) {
                driveLine.remove();
            }
            markerFrom = googleMap.addMarker(
                    new MarkerOptions()
                            .position(posFrom)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));


            markerDestination = googleMap.addMarker(
                    new MarkerOptions()
                            .position(posDest)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));

            new DoDrawRute(mActivity,posFrom,posDest,googleMap).execute();

        } catch (Exception e) {

        }

    }


    public void drawMarkerNearestDriver(LatLng pFrom, LatLng locationMarker, int index) {
        Log.v(TAG, "drawMarkerNearestDriver " + locationMarker.toString());

        try {
            googleMap.addMarker(
                    new MarkerOptions()
                            .position(locationMarker)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_driver)));

            Document doc = GoogleAPIManager.getRoute(pFrom, locationMarker, "driving");

            String strDurationDriver = "" + GoogleAPIManager.getDurationText(doc);
            String[] strDist = strDurationDriver.split(" ");
            int tempDuration = Integer.parseInt(strDist[0]);
            if (tempDuration < duration) {
                duration = tempDuration;
                if (duration < 0) {
                    duration = 1;
                }
            }
            txtDriverTime.setText("Estimasi waktu menunggu : " + Html.fromHtml("<b>" + duration + " menit</b>"));
            isGetNearestDrivers = true;
            drawMarkerNearestDriver(posFrom, ApplicationData.posDrivers[index + 1], index + 1);
        } catch (Exception e) {
            Log.v(TAG, "catch drawMarkerNearestDriver ");
        }

    }

    public String getAddress(LatLng latlng) {
        Geocoder geocoder = new Geocoder(mActivity, Locale.getDefault());
        double lat = latlng.latitude;
        double lng = latlng.longitude;
        String addressLine = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            addressLine = obj.getAddressLine(0);
            if (tagLocation.equals(TAG_FROM)) {
                strDetailFrom = obj.getAddressLine(1) + " , " + obj.getAddressLine(2);
            } else if (tagLocation.equals(TAG_DESTINATION)) {
                strDetailDestination = obj.getAddressLine(1) + " , " + obj.getAddressLine(2);
            }

        } catch (IOException e) {
        } catch (Exception e) {
        }
        return addressLine;
    }


    public static void hideKeyboard() {
        View view = mActivity.getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            layoutSuggestion.setVisibility(GONE);
        }

    }


    @Override
    public void onMapClick(LatLng latLng) {
        //drawNewMarker
        isSearchCurrent = false;
        float zoom = googleMap.getCameraPosition().zoom;
        if (zoom <= 15) {
            zoom = 15;
        }
        if (tagLocation.equals(TAG_FROM)) {
            //posFrom=latLng;
            posTemp = latLng;
            markerTemp = googleMap.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    layoutMarkerFrom.setVisibility(VISIBLE);
                    layoutMarkerDestination.setVisibility(GONE);
                }

                @Override
                public void onCancel() {
                }
            });

        } else if (tagLocation.equals(TAG_DESTINATION)) {
            //posDest = latLng;
            posTemp = latLng;
            markerTemp = googleMap.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    layoutMarkerFrom.setVisibility(GONE);
                    layoutMarkerDestination.setVisibility(VISIBLE);
                }

                @Override
                public void onCancel() {
                }
            });

        }
        layoutSuggestion.setVisibility(GONE);
    }


    public class GetSuggestion extends AsyncTask<String, Void, JSONArray> {
        String address, tag;

        public GetSuggestion(String address, String tag) {
            this.address = address;
            this.tag = tag;
        }

        @Override
        protected void onPreExecute() {
            layoutSuggestion.setVisibility(VISIBLE);
            pSuggestion.setVisibility(VISIBLE);
            itemCurrent.setVisibility(GONE);
            mListView.setVisibility(VISIBLE);
            mListView.setAdapter(null);
        }

        @Override
        protected JSONArray doInBackground(String... arg) {
            JSONArray json = null;
            LIST_PLACE = new ArrayList<ModelPlace>();
            JSONControl JSONControl = new JSONControl();
            try {
                json = JSONControl.listPlace(address);
            } catch (Exception e) {
            }
            if (json != null) {
                for (int i = 0; i < json.length(); i++) {
                    String id = "";
                    description = "";
                    address = "";
                    String detail = "";
                    boolean status = true;
                    try {
                        JSONObject jsonObject = json.getJSONObject(i);
                        id = jsonObject.getString(KEY_ID);
                        description = jsonObject.getString(KEY_DESCRIPTION);
                        String[] descSplit = description.split(",");
                        address = descSplit[0];
                        detail = descSplit[1] + "," + descSplit[2];
                        status = true;

                    } catch (JSONException e) {
                    } catch (Exception e) {

                    }

                    if (status) {
                        mPlace = new ModelPlace(id, address, detail);
                        LIST_PLACE.add(mPlace);
                    }
                }
                try {
                    mAdapter = new AdapterSuggestion(getActivity(), LIST_PLACE);
                } catch (NullPointerException e) {
                }
            } else {
                LIST_PLACE = null;
            }

            return json;
        }

        @Override
        protected void onPostExecute(final JSONArray json) {
            // TODO Auto-generated method stub
            super.onPostExecute(json);
            pSuggestion.setVisibility(GONE);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        ModelPlace selectedPlace = LIST_PLACE.get(position);
                        if (tag.equals(TAG_FROM)) {
                            txtFrom.setText(selectedPlace.getAddress());
                            strDetailFrom = selectedPlace.getAddressDetail();
                            String alamat = selectedPlace.getAddress()+","+selectedPlace.getAddressDetail();
                            if (markerFrom != null) {
                                markerFrom.remove();
                            }
                            ModelGeocode geocode = GoogleAPIManager.geocode(alamat);
                            ApplicationData.posFrom = new LatLng(geocode.getLat(), geocode.getLon());
                            appManager.setUserFrom(new ModelPlace(geocode.getLat(), geocode.getLon()));

                        } else if (tag.equals(TAG_DESTINATION)) {
                            txtDestination.setText(selectedPlace.getAddress());
                            strDetailDestination = selectedPlace.getAddressDetail();
                            String alamat = selectedPlace.getAddress()+","+selectedPlace.getAddressDetail();
                            if (markerDestination != null) {
                                markerDestination.remove();
                            }
                            ModelGeocode geocode = GoogleAPIManager.geocode(alamat);
                            ApplicationData.posDestination = new LatLng(geocode.getLat(), geocode.getLon());
                            appManager.setUserDestination(new ModelPlace(geocode.getLat(), geocode.getLon()));
                        }
                        layoutSuggestion.setVisibility(GONE);
                        hideKeyboard();
                        drawNewMarker();
                    } catch (Exception e) {
                        layoutSuggestion.setVisibility(GONE);
                        hideKeyboard();
                    }
                }
            });

        }
    }


    @Override
    public void onStart() {
        super.onStart();
/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning && NetworkManager.getInstance(mActivity).isConnectedInternet()) {
                    try {
                        Thread.sleep(10000);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                new GetMyLocation(mActivity, googleMap, socketManager).execute();
                                isRunning = false;

                            }
                        });
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    */
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        //mHandler.postDelayed(mRunnable, AUTOUPDATE_INTERVAL_TIME);
        // Register mMessageReceiver to receive messages.
        Log.i(TAG, "onResume");
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(createOrder,
                new IntentFilter("createOrder"));
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(lastOrder,
                new IntentFilter("lastOrder"));
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(lastFeedback,
                new IntentFilter("lastFeedback"));
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(nearestDrivers,
                new IntentFilter("nearestDrivers"));
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(logout,
                new IntentFilter("logout"));

    }

    @Override
    public void onPause() {
        // Unregister since the activity is not visible
        Log.i(TAG, "onPause");
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(createOrder);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(lastFeedback);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(nearestDrivers);

        //mHandler.removeCallbacks(mRunnable);
        //PowerManager powerManager = (PowerManager) mActivity.getSystemService(mActivity.POWER_SERVICE);
        // boolean isScreenOn = powerManager.isScreenOn();
        //if (!isScreenOn) {
        //     mRunnable.run();
        //}
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
        //getActivity().finish();
        //ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
        //Future longRunningTaskFuture = threadPoolExecutor.submit(mRunnable);
        //longRunningTaskFuture.cancel(true);
    }

    void OpenLoading() {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("Loading. . .");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }


    private class GetMyLocation extends AsyncTask<String, Void, String> implements LocationListener {
        private Activity activity;
        private Context context;
        private Resources resources;
        private double latitude, longitude;
        private GoogleMap googleMap;
        private LocationManager locationManager;
        private SocketManager socketManager;
        private CircleOptions circleOptions;


        public GetMyLocation(Activity activity, GoogleMap googleMap, SocketManager socketManager) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
            this.googleMap = googleMap;
            this.socketManager = socketManager;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DialogManager.ShowLoading(activity, "Memuat lokasi anda. . .");

        }


        @Override
        protected String doInBackground(String... params) {
            Log.v(TAG, "GetMyLocation doInBackground");


            try {
                try {
                    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
                    if (status != ConnectionResult.SUCCESS) {
                        int requestCode = 10;
                    } else {
                        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                        Criteria criteria = new Criteria();
                        String provider = locationManager.getBestProvider(criteria, true);
                        location = locationManager.getLastKnownLocation(provider);
                        if (!isGPSEnabled && !isNetworkEnabled) {
                            return "FAIL";
                        } else {
                            if (isNetworkEnabled) {
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this, Looper.getMainLooper());
                                Log.v("locationManager", "Network");
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                        //if(!isSearchCurrent) {
                                            posFrom = new LatLng(latitude, longitude);
                                            ApplicationData.posFrom = posFrom;
                                            appManager.setUserFrom(new ModelPlace(posFrom.latitude, posFrom.longitude));
                                        //}
                                    }
                                }
                            }
                            if (isGPSEnabled) {
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this, Looper.getMainLooper());
                                Log.v("locationManager", "Network");
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                        //if(!isSearchCurrent) {
                                            posFrom = new LatLng(latitude, longitude);
                                            ApplicationData.posFrom = posFrom;
                                            appManager.setUserFrom(new ModelPlace(posFrom.latitude, posFrom.longitude));
                                        //}
                                    }
                                }
                            }

                        }
                    }

                } catch (Exception e) {
                }
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("posisi gps", "onPost");

            switch (result) {
                case "FAIL":
                    DialogManager.showDialog(activity, "Mohon Maaf", "Tidak dapat menemukan lokasi Anda!");
                    break;
                case "OK":
                    try {
                        LatLng pFrom = ApplicationData.posFrom;
                        float zoom = googleMap.getCameraPosition().zoom;
                        if (zoom <= 15) {
                            zoom = 15;
                        }
                        double radiusInMeters = 5 * 100.0;
                        int strokeColor = 0xffffffff;
                        int shadeColor = 0x3366ffff;

                        if (mCircle != null) {
                            mCircle.remove();
                        }

                        if (markerCurrent != null) {
                            markerCurrent.remove();
                        }

                        circleOptions = new CircleOptions()
                                .center(pFrom)
                                .radius(radiusInMeters)
                                .fillColor(shadeColor)
                                .strokeColor(strokeColor)
                                .strokeWidth(10);
                        mCircle = googleMap.addCircle(circleOptions);
                        cameraUpdate = CameraUpdateFactory.newLatLngZoom(pFrom, zoom);
                        //if (!isSearchCurrent) {
                            markerFrom = googleMap.addMarker(
                                    new MarkerOptions()
                                            .position(pFrom)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
                            txtFrom.setText(getAddress(pFrom));
                            googleMap.animateCamera(cameraUpdate);
                        /*} else {
                            markerCurrent = googleMap.addMarker(
                                    new MarkerOptions()
                                            .position(pFrom)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_from)));
                            googleMap.moveCamera(cameraUpdate);
                        }*/


                        Log.v("posisi gps", pFrom.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
            DialogManager.DismissLoading(mActivity);
            GetNearestDriver(activity);
        }


        @Override
        public void onLocationChanged(Location location) {
            try {
                LatLng pFrom = new LatLng(latitude, longitude);
                //ApplicationData.posFrom = pFrom;
            } catch (Exception e) {
                Log.v("FragmentHome", "OnLocationChange");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    public void GetNearestDriver(Activity activity) {

        if (NetworkManager.getInstance(activity).isConnectedInternet()) {
            Log.v(TAG, "Running");

            try {

                socketManager.GetNearestDrivers(posFrom);

                Log.v(TAG, "Running GetNearestDrivers");
            } catch (Exception e) {
                Log.v(TAG, "Not Running GetNearestDrivers");
            }


        }
    }

    void DoDraw() {
        drawMarkerNearestDriver(posFrom, ApplicationData.posDrivers[0], 0);
    }


    private class DoDrawRute extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;
        private GoogleMap googleMap;
        private PolylineOptions  rectLine;
        private LatLng posFrom, posDest;

        public DoDrawRute(Activity activity, LatLng posFrom, LatLng posDest, GoogleMap googleMap) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
            this.googleMap = googleMap;
            this.posFrom = posFrom;
            this.posDest = posDest;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Mencari Rute. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Document doc = GoogleAPIManager.getRoute(posFrom, posDest, "driving");
                rectLine = new PolylineOptions().width(15).color(getResources().getColor(R.color.bg_grad_2));
                ArrayList<LatLng> directionPoint = GoogleAPIManager.getDirection(doc);
                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                strDistance = "" + GoogleAPIManager.getDistanceText(doc);
                strDuration = "" + GoogleAPIManager.getDurationText(doc);
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            switch (result) {
                case "FAIL":
                    break;
                case "OK":
                    if (driveLine != null) {
                        driveLine.remove();
                    }
                    driveLine = googleMap.addPolyline(rectLine);
                    break;
            }


        }


    }


}
