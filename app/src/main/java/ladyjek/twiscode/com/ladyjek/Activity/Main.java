package ladyjek.twiscode.com.ladyjek.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import ladyjek.twiscode.com.ladyjek.Fragment.FragmentDrawer;
import ladyjek.twiscode.com.ladyjek.Fragment.FragmentHome;
import ladyjek.twiscode.com.ladyjek.R;


public class Main extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;





    private ActionBar actionBarCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        //mToolbar.setTitleTextColor(0xbd3b97);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer); //getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);



        displayView(0);


    }

    @Override
    public void onBackPressed() {
        Log.d("counter stack", Integer.toString(getFragmentManager().getBackStackEntryCount()));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */
        /*
        if(id == R.id.action_write){
            Toast.makeText(getApplicationContext(), "Write post action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        //displayView(position);
        //ApplicationData.menu = position;
        //ChangeMain();
        displayView(position);
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


        if (fragment != null ) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();


            /*
            FragmentManager fragmentManager = getSupportFragmentManager();
            datafragmentHelper.SetDataFragmentHelper(fragment, fragmentManager);
            datafragmentHelper.ChangeFragment(fragment);

            // set the toolbar title
            getSupportActionBar().setTitle(title);
            */
        }


    }

    private void Share(){
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
