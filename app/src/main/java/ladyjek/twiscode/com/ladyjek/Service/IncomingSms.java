package ladyjek.twiscode.com.ladyjek.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;

/**
 * Created by User on 9/24/2015.
 */
public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    if(message.contains("Selamat datang di LadyJek.")) {
                        ApplicationData.smsCode = message.replaceAll("[^\\d]", "");
                        Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + ApplicationData.smsCode);
                        Intent broadcastIntent = new Intent("smsCode");
                        broadcastIntent.putExtra("messageCode", ApplicationData.smsCode);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
                    }

                } // end for loop
            } // bundle is null


        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

    private void SendBroadcast(String typeBroadcast, String type) {

    }

}
