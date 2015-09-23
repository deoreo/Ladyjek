package ladyjek.twiscode.com.ladyjek.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import ladyjek.twiscode.com.ladyjek.Fragment.FragmentDrawer;
import ladyjek.twiscode.com.ladyjek.Fragment.FragmentHome;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.DialogManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;

import static ladyjek.twiscode.com.ladyjek.Utilities.DialogManager.*;


public class Main extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;


    private final String TAG = "Main";

    private ActionBar actionBarCustom;
    private SocketManager socketManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<b><font color='#bd3b97'>ORDER LADYJEK</font></b>"));
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        displayView(0);


    }

    @Override
    public void onBackPressed() {
        String counterStack = Integer.toString(getFragmentManager().getBackStackEntryCount());
        Log.d("counter stack", "" + counterStack);
        finish();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position + 1);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (position) {
            case 0:
                fragment = new FragmentHome();
                //title = getString(R.string.app_name);
                title = "Ghat!";
                break;

            default:
                break;
        }

        if (position == 0) {
            if (fragment != null) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();

            }
        } else {
            if (position == 1) {
                Intent i = new Intent(getBaseContext(), ActivityInformasiPribadi.class);
                startActivity(i);
            }
            else if (position == 2) {
                Intent i = new Intent(getBaseContext(), ActivityHistory.class);
                startActivity(i);
            }
            else if (position == 3) {
                Intent i = new Intent(getBaseContext(), ActivityCashless.class);
                startActivity(i);
            }
            else if (position == 4) {
                Intent i = new Intent(getBaseContext(), ActivityPromo.class);
                startActivity(i);
            }
            else if (position == 5) {
                Intent i = new Intent(getBaseContext(), ActivityAbout.class);
                startActivity(i);
            }
            else if (position == 6) {
                Intent i = new Intent(getBaseContext(), ActivityHelp.class);
                startActivity(i);
            }
        }


    }

    private void Share() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "http://www.google.com";
                /*
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                */
        sharingIntent.putExtra(Intent.EXTRA_TITLE, "try try");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    @Override
    public void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        Log.i("adding receiver", "fragment ontainer for profile");

    }

    @Override
    public void onPause() {
        // Unregister since the activity is not visible
        Log.i("unreg receiver", "fragment unregister");
        super.onPause();
    }


}
