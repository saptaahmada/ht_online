package com.sapta.htonline;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sapta.htonline.entities.Parameter;
import com.sapta.htonline.entities.ResponseParameter;
import com.sapta.htonline.service.BaseServiceObserver;
import com.sapta.htonline.service.IServiceObserver;
import com.sapta.htonline.service.MumbleService;

import org.junit.Assert;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private IServiceObserver mObserver;

    private MumbleService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        public void onServiceConnected(
                final ComponentName className,
                final IBinder binder) {
            Log.d("MASUKKSERVICECONNECT", "1");

            mService = ((MumbleService.LocalBinder) binder).getService();
            Log.d("MASUKKSERVICECONNECT", "2");

            Log.i("Mumble", "mService set");
            registerConnectionReceiver();

            Log.d("WOKEE", "A");
            if(mService != null) {
                Log.d("WOKEE", "B");
                if(mService.isConnected()) {
                    Log.d("WOKEE", "C");
                    final Intent i = new Intent(getApplicationContext(), ChannelListActivity.class);
                    startActivityForResult(i, 1);
                }
                Log.d("WOKEE", "D");
            }
            Log.d("WOKEE", "E");

        }

        public void onServiceDisconnected(final ComponentName arg0) {
            mService = null;
        }
    };

    private Button button;
    private TextView textView;
    private static final int DRAW_OVER_OTHER_APP_PERMISSION = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MASUKKCREATE", "1");

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setMode(AudioManager.MODE_IN_CALL);
        am.setMicrophoneMute(false);
        am.setSpeakerphoneOn(true);

        askForSystemOverlayPermission();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1);

        }
    }

    private void askForSystemOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    //Permission is not available. Display error text.
                    errorToast();
                    finish();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void errorToast() {
        Toast.makeText(this, "Draw over other app permission not available. Can't start the application without the permission.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MASUKKSTART", "1");

        Intent intent = new Intent(this, MumbleService.class);
        Log.d("MASUKKSTART", "2");
        bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
        Log.d("MASUKKSTART", "3");

    }

    public void onLogin(View view) {

        String username = "sapta";

        //USE PUBLIC SERVER LIST
        //VoicePacket.net - Free Temp. Servers! - US East
        //HOST : wa.voicepacket.us
        //PORT : 64738
        final Intent connectionIntent = new Intent(this, MumbleService.class);
        connectionIntent.setAction(MumbleService.ACTION_CONNECT);
        connectionIntent.putExtra(MumbleService.EXTRA_HOST, "wa.voicepacket.us");
        connectionIntent.putExtra(MumbleService.EXTRA_PORT, 64738);
        connectionIntent.putExtra(MumbleService.EXTRA_USERNAME, username);
        connectionIntent.putExtra(MumbleService.EXTRA_PASSWORD, "");
        startService(connectionIntent);

    }

    private class ServerServiceObserver extends BaseServiceObserver {
        @Override
        public void onConnectionStateChanged(final int state) {
            Log.d("MASUKKSSO", "1");
            checkConnectionState();
            Log.d("MASUKKSSO", "2");

        }
    }

    private final boolean checkConnectionState() {
        switch (mService.getConnectionState()) {
            case MumbleService.CONNECTION_STATE_CONNECTING:
                Log.d("MASUKKCHECK", "1");
                //show progress bar or something
                break;
            case MumbleService.CONNECTION_STATE_SYNCHRONIZING:
                Log.d("MASUKKCHECK", "2");
                //show progress bar or something
                break;
            case MumbleService.CONNECTION_STATE_CONNECTED:
                Log.d("MASUKKCHECK", "3");
                unregisterConnectionReceiver();
                final Intent i = new Intent(this, ChannelListActivity.class);
                startActivityForResult(i, 1);
                finish();
                return true;
            case MumbleService.CONNECTION_STATE_DISCONNECTED:
                Log.d("MASUKKCHECK", "4");

                Log.i(Globals.LOG_TAG, "ServerList: Disconnected");
                break;
            default:
                Log.d("MASUKKSSO", "5");
                Assert.fail("Unknown connection state");
        }

        return false;
    }

    private void registerConnectionReceiver() {
        Log.d("MASUKKREGIS", "1");

        if (mObserver != null) {
            Log.d("MASUKKREGIS", "2");
            return;
        }
        Log.d("MASUKKREGIS", "3");

        mObserver = new ServerServiceObserver();
        Log.d("MASUKKREGIS", "4");

        if (mService != null) {
            Log.d("MASUKKREGIS", "5");

            mService.registerObserver(mObserver);
            Log.d("MASUKKREGIS", "6");

        }
    }

    private void unregisterConnectionReceiver() {
        Log.d("MASUKKUNREG", "1");

        if (mObserver == null) {
            Log.d("MASUKKUNREG", "2");
            return;
        }
        Log.d("MASUKKUNREG", "3");

        if (mService != null) {
            Log.d("MASUKKUNREG", "4");

            mService.unregisterObserver(mObserver);
            Log.d("MASUKKUNREG", "5");

        }

        mObserver = null;
    }

}
