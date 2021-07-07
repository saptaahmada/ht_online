package com.sapta.htonline;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sapta.htonline.service.BaseServiceObserver;
import com.sapta.htonline.service.IServiceObserver;
import com.sapta.htonline.service.MumbleService;
import com.sapta.htonline.service.model.Message;
import com.sapta.htonline.service.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private MumbleService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        public void onServiceConnected(
                final ComponentName className,
                final IBinder binder) {
            mService = ((MumbleService.LocalBinder) binder).getService();
            updateListView();
            mService.registerObserver(mObserver);
        }

        public void onServiceDisconnected(final ComponentName arg0) {
            mService = null;
            ((ListView) findViewById(R.id.userList)).setAdapter(null);
        }
    };
    private ArrayAdapter<User> userAdapter;
    private List<User> selectableUsers=new ArrayList<User>();
    private IServiceObserver mObserver=new BaseServiceObserver(){
        @Override
        public void onUserAdded(User user) {
            refillSelectableUsers();
            userAdapter.notifyDataSetChanged();
        }

        @Override
        public void onUserRemoved(User user) {
            refillSelectableUsers();
            userAdapter.notifyDataSetChanged();
        }

        @Override
        public void onUserUpdated(User user) {
            refillSelectableUsers();
            userAdapter.notifyDataSetChanged();
        }

        @Override
        public void onMessageReceived(Message msg) {
            Toast.makeText(getApplicationContext(), "MASUKK", Toast.LENGTH_SHORT).show();
            refillSelectableUsers();
            userAdapter.notifyDataSetChanged();
        }
    };

    private void updateListView() {

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.user_header, (ViewGroup) findViewById(R.id.userList), false);
        ((ListView) findViewById(R.id.userList)).addHeaderView(header, null, false);
        ((TextView) findViewById(R.id.textUser)).setText("Channel : " + mService.getCurrentChannel().name);
        findViewById(R.id.faMicUser).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    mService.setRecording(true);
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
                    mService.setRecording(false);
                    return true;
                }
                return false;
            }
        });
        refillSelectableUsers();
        userAdapter = new ArrayAdapter<User>(this, R.layout.channel_user_row, R.id.userRowName, selectableUsers) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View resView = super.getView(position, convertView, parent);
                ((TextView)resView.findViewById(R.id.userRowName)).setText(selectableUsers.get(position).name);
                if (selectableUsers.get(position).talkingState>0)
                    ((ImageView)resView.findViewById(R.id.userRowState)).setImageDrawable(getResources().getDrawable(R.drawable.talking_on));
                else
                    ((ImageView)resView.findViewById(R.id.userRowState)).setImageDrawable(getResources().getDrawable(R.drawable.talking_off));
                return resView;
            }
        };
        ((ListView)findViewById(R.id.userList)).setAdapter(userAdapter);
    }

    private void refillSelectableUsers() {
        List<User> users = mService.getUserList();
        selectableUsers.clear();
        for (User u : users) {
            if (u.getChannel().id == mService.getCurrentChannel().id) selectableUsers.add(u);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MumbleService.class);
        getApplicationContext().bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    mService.joinChannel(0);//go back to root
                    mService.unregisterObserver(mObserver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        ((ListView)findViewById(R.id.userList)).setAdapter(null);
        super.onStop();
    }
}
