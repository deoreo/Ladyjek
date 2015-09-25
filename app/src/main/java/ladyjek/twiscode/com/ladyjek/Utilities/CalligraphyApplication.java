package ladyjek.twiscode.com.ladyjek.Utilities;

import android.app.Application;

import ladyjek.twiscode.com.ladyjek.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class CalligraphyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/GothamRnd-Book.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .addCustomStyle(TextFields.class, R.attr.textFieldStyle)
                        .build()
        );
    }
}