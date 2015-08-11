package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ladyjek.twiscode.com.ladyjek.Database.DatabaseHandler;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelUser;
import ladyjek.twiscode.com.ladyjek.R;

public class ActivitySplashScreen extends Activity {

    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        db = new DatabaseHandler(this);

        /*
        int countUser = db.getUserCount();
        if(countUser > 0) {

            Intent i = new Intent(getBaseContext(), Main.class);
            startActivity(i);
            ApplicationData.userLogin = db.getUser();
            finish();
        }
        else{
            // After 5 seconds redirect to another intent
            Intent i = new Intent(getBaseContext(), ActivityLogin.class);
            startActivity(i);
            finish();
        }
        */

        Dummy();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Dummy(){
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(3 * 1000);
                    int countUser = db.getUserCount();
                    if(countUser > 0) {
                        Intent i = new Intent(getBaseContext(), Main.class);
                        startActivity(i);
                        ApplicationData.userLogin = db.getUser();
                    }
                    else{
                        // After 5 seconds redirect to another intent
                        Intent i = new Intent(getBaseContext(), ActivityLogin.class);
                        startActivity(i);
                    }
                    finish();

                } catch (Exception e) {

                }
            }
        };


        background.start();
    }
}
