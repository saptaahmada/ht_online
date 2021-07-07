package com.sapta.htonline.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

// Receiver
public class MediaButtonIntentReceiver extends BroadcastReceiver {

    private static final String TAG = "MediaButtonIntentReceiver";
    public MediaButtonIntentReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        Log.d("OALAHBOSKU", "1");

        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            Log.d("OALAHBOSKU", "2");
            return;
        }
        KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            Log.d("OALAHBOSKU", "3");

            return;
        }

        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            Log.d("OALAHBOSKU", "4");

            Toast.makeText(context, "BUTTON PRESSED!", Toast.LENGTH_SHORT).show();
        }

        Log.d("OALAHBOSKU", "5");

        abortBroadcast();
    }
}