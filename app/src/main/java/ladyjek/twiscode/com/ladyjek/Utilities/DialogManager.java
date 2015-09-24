package ladyjek.twiscode.com.ladyjek.Utilities;

import android.app.ProgressDialog;
import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import ladyjek.twiscode.com.ladyjek.R;

/**
 * Created by ModelUser on 8/3/2015.
 */
public class DialogManager {
    private static ProgressDialog progressDialog;

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
    public static void ShowLoading(Context ctx, String message){
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
    public static void DismissLoading(Context ctx){
        progressDialog.dismiss();
    }

}
