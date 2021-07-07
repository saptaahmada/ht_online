package com.sapta.htonline;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andremion.counterfab.CounterFab;
import com.sapta.htonline.adapters.ListUserAdapter;
import com.sapta.htonline.service.BaseServiceObserver;
import com.sapta.htonline.service.IServiceObserver;
import com.sapta.htonline.service.MumbleService;
import com.sapta.htonline.service.model.Channel;
import com.sapta.htonline.service.model.User;

import java.util.ArrayList;
import java.util.List;

public class ChannelListActivity extends AppCompatActivity {

    private MumbleService mService;
    private int NOTIFICATION_ID = 1;
    private List<Channel> selectableChannels = new ArrayList<>();
    private List<User> selectableUsers = new ArrayList<>();

    private boolean mIsRecord = false;

    private WindowManager mWindowManager;
    private View mOverlayView;
    CounterFab counterFab;

    private  void  startMyOwnForegroundOldVersion() {
        // create the notification
        Notification.Builder m_notificationBuilder = new Notification.Builder(getApplicationContext())
                .setContentTitle("Harmoni")
                .setContentText("Harmoni Running")
                .setSmallIcon(R.drawable.talking_on);

        // create the pending intent and add to the notification
        Intent intent = new Intent(getApplicationContext(), MumbleService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        m_notificationBuilder.setContentIntent(pendingIntent);

        mService.startForeground(NOTIFICATION_ID, m_notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.ttl.harmoni";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.talking_on)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        mService.startForeground(2, notification);
    }

    ServiceConnection mServiceConn = new ServiceConnection() {
        public void onServiceConnected(
                final ComponentName className,
                final IBinder binder) {
            mService = ((MumbleService.LocalBinder) binder).getService();
            updateListView();
            mService.registerObserver(mObserver);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startMyOwnForeground();
            } else {
                startMyOwnForegroundOldVersion();
            }
        }

        public void onServiceDisconnected(final ComponentName arg0) {
            mService = null;
            ((ListView) findViewById(R.id.channelList)).setAdapter(null);
        }
    };

    private ArrayAdapter<Channel> channelAdapter;
    private IServiceObserver mObserver = new BaseServiceObserver(){

        @Override
        public void onChannelAdded(Channel channel) throws RemoteException {
            channelAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChannelRemoved(Channel channel) throws RemoteException {
            channelAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChannelUpdated(Channel channel) throws RemoteException {
            channelAdapter.notifyDataSetChanged();
        }
        //dfdf

        @Override
        public void onCurrentChannelChanged() throws RemoteException {
            channelAdapter.notifyDataSetChanged();
        }

        @Override
        public void onUserAdded(User user) throws RemoteException {
            refillSelectableUsers();
            channelAdapter.notifyDataSetChanged();
        }

        @Override
        public void onUserRemoved(User user) throws RemoteException {
            refillSelectableUsers();
            channelAdapter.notifyDataSetChanged();
        }

        @Override
        public void onUserUpdated(User user) throws RemoteException {
            refillSelectableUsers();
            channelAdapter.notifyDataSetChanged();
        }
    };


    private void updateListView() {

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.public_header, (ViewGroup) findViewById(R.id.channelList), false);
        ((ListView) findViewById(R.id.channelList)).addHeaderView(header, null, false);

        refillSelectableUsers();
        channelAdapter = new ArrayAdapter<Channel>(this, R.layout.channel_list_item, R.id.txt_channel_name, selectableChannels) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Channel channel = selectableChannels.get(position);
                View resView = super.getView(position, convertView, parent);
                ((TextView)resView.findViewById(R.id.txt_channel_name)).setText(channel.name);

                ImageView imgArrow = resView.findViewById(R.id.img_arrow);
                LinearLayout layoutUser = resView.findViewById(R.id.layout_user);
                imgArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(imgArrow.getTag().equals("0")) {
                            layoutUser.setVisibility(View.VISIBLE);
                            imgArrow.setTag("1");
                            imgArrow.setImageResource(R.drawable.ic_arrow_drop_down);
                        } else {
                            layoutUser.setVisibility(View.GONE);
                            imgArrow.setTag("0");
                            imgArrow.setImageResource(R.drawable.ic_arrow_left_24);
                        }
                    }
                });

                TextView txtUserCount = resView.findViewById(R.id.txt_user_count);
                txtUserCount.setText("("+channel.users.size()+")");

                resView.findViewById(R.id.buttonJoin).setTag(position);
                resView.findViewById(R.id.buttonJoin).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try  {
                                    mService.joinChannel(channel.id);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();
                    }
                });

                ListUserAdapter adapter = new ListUserAdapter(getApplicationContext(), channel.users);
                RecyclerView recyclerView = resView.findViewById(R.id.rec_user);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                return resView;
            }

            @Override
            public int getCount() {
                return selectableChannels.size() - 1;
            }
        };
        ((ListView)findViewById(R.id.channelList)).setAdapter(channelAdapter);
    }

    private void refillSelectableUsers() {
        selectableChannels = mService.getChannelList();
        for (int i=0; i<selectableChannels.size(); i++) {
            selectableChannels.get(i).users.clear();
        }

        selectableUsers = mService.getUserList();

        for (int i=0; i<selectableChannels.size(); i++) {
            Channel channel = selectableChannels.get(i);
            for(int j=0; j<selectableUsers.size(); j++) {
                User user = selectableUsers.get(j);
                if(channel.id == user.getChannel().id) {
                    selectableChannels.get(i).users.add(user);
                }
            }
        }
    }

    private User getUserTalkState() {
        User result = null;

        for(int i=0; i<selectableUsers.size(); i++) {
            User user = selectableUsers.get(i);
            if(user.talkingState == 1) {
                result = user;
                break;
            }
        }

        return result;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_channel_list);

        Intent intent = new Intent(this, MumbleService.class);
        getApplicationContext().bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "App::HtOnline");
        wakeLock.acquire();

        if(mWindowManager == null)
            showFloatingWidget();

//        IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
//        MediaButtonIntentReceiver r = new MediaButtonIntentReceiver();
//        filter.setPriority(1000); //this line sets receiver priority
//        registerReceiver(r, filter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_HEADSETHOOK) {
            User user = getUserTalkState();
            if(user == null) {
                mIsRecord = !mIsRecord;
                mService.setRecording(mIsRecord);
                return true;
            } else {
                MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alert1);
                mPlayer.start();
                mIsRecord = !mIsRecord;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_HEADSETHOOK) {
//            mService.setRecording(false);
//            mIsRecord = false;
//            Log.d("KOCOKBOSKU", "BUTTON UNPRESSED");
////            Toast.makeText(getApplicationContext(), "BUTTON PRESSED!", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onBackPressed() {
//        mService.disconnect();
//        mService.unregisterObserver(mObserver);
//        ((ListView)findViewById(R.id.channelList)).setAdapter(null);
//        super.onBackPressed();
        finish();
    }

    private void showFloatingWidget() {

        setTheme(R.style.AppTheme);

        mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null);

        WindowManager.LayoutParams params;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }


        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mOverlayView, params);


        counterFab = (CounterFab) mOverlayView.findViewById(R.id.fabHead);
//        counterFab.setCount(1);

        counterFab.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    User user = getUserTalkState();
                    if(user == null) {
                        mService.setRecording(true);
                        mIsRecord = true;
                    } else {
                            MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alert1);
                            mPlayer.start();
                            mIsRecord = false;
                    }
                }
                if(event.getAction() == MotionEvent.ACTION_UP ) {
                    mService.setRecording(false);
                    mIsRecord = false;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;


                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();


                        return true;
                    case MotionEvent.ACTION_UP:

                        return true;
                    case MotionEvent.ACTION_MOVE:


                        float Xdiff = Math.round(event.getRawX() - initialTouchX);
                        float Ydiff = Math.round(event.getRawY() - initialTouchY);


                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) Xdiff;
                        params.y = initialY + (int) Ydiff;

                        //Update the layout with new X & Y coordinates
                        mWindowManager.updateViewLayout(mOverlayView, params);


                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOverlayView != null)
            mWindowManager.removeView(mOverlayView);
    }
}
