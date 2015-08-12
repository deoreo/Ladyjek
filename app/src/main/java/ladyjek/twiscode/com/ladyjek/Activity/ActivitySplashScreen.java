package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import ladyjek.twiscode.com.ladyjek.Database.DatabaseHandler;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelUser;
import ladyjek.twiscode.com.ladyjek.R;

public class ActivitySplashScreen extends Activity {

    private DatabaseHandler db;
    private ProgressBar mProgressBar;
    private int mWaited = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        db = new DatabaseHandler(this);
        mProgressBar = (ProgressBar) findViewById(R.id.splash_progress);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i <= 1000; i++) {
                        sleep(2);
                        mProgressBar.setProgress(mWaited/10);
                        mWaited+=1;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    int countUser = db.getUserCount();
                    if(countUser >= 0) {
                        if(ApplicationData.act==null){
                            Intent i = new Intent(getBaseContext(), Main.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                        else {
                            Intent i = new Intent(getBaseContext(), ApplicationData.act);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }

                    }
                    else{
                        Intent i = new Intent(getBaseContext(), ActivityLogin.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                    finish();


                }
            }
        };
        splashThread.start();
    }

    private void Dummy(){
        Thread background = new Thread() {
            public void run() {

                try {
                    sleep(5 * 1000);
                    int countUser = db.getUserCount();
                    if(countUser > 0) {
                        Intent i = new Intent(getBaseContext(), Main.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        ApplicationData.userLogin = db.getUser();
                    }
                    else{
                        // After 5 seconds redirect to another intent
                        Intent i = new Intent(getBaseContext(), ActivityLogin.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                    finish();

                } catch (Exception e) {

                }
            }
        };


        background.start();
    }

    @Override
    public void onBackPressed() {

    }
}
