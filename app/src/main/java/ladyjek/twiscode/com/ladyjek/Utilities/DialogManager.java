package ladyjek.twiscode.com.ladyjek.Utilities;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import ladyjek.twiscode.com.ladyjek.R;

/**
 * Created by User on 8/3/2015.
 */
public class DialogManager {
    public static void showDialog(Context ctx, String title, String content){
        new MaterialDialog.Builder(ctx)
                .title(title)
                .content(content)
                .positiveText("OK")
                .show();
    }

    public static void showProgressDialog(Context ctx, String title, String content){
        new MaterialDialog.Builder(ctx)
                .title(title)
                .content(content)
                .positiveText("OK")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
    }

}
