package com.sapta.htonline.service;

import android.os.RemoteException;

import com.sapta.htonline.service.model.User;
import com.sapta.htonline.service.model.Message;
import com.sapta.htonline.service.model.Channel;

public interface IServiceObserver {
    void onChannelAdded(Channel channel) throws RemoteException;
    void onChannelRemoved(Channel channel) throws RemoteException;
    void onChannelUpdated(Channel channel) throws RemoteException;

    void onCurrentChannelChanged() throws RemoteException;

    void onCurrentUserUpdated() throws RemoteException;

    void onUserAdded(User user) throws RemoteException;
    void onUserRemoved(User user) throws RemoteException;
    void onUserUpdated(User user) throws RemoteException;

    void onMessageReceived(Message msg) throws RemoteException;
    void onMessageSent(Message msg) throws RemoteException;

    /**
     * Called when the connection state changes.
     */
    void onConnectionStateChanged(int state) throws RemoteException;
}
