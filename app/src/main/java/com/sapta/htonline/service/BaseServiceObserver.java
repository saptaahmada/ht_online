package com.sapta.htonline.service;

import com.sapta.htonline.service.model.Channel;
import com.sapta.htonline.service.model.Message;
import com.sapta.htonline.service.model.User;

import android.os.RemoteException;

public class BaseServiceObserver implements IServiceObserver {
	@Override
	public void onChannelAdded(final Channel channel) throws RemoteException {
	}

	@Override
	public void onChannelRemoved(final Channel channel) throws RemoteException {
	}

	@Override
	public void onChannelUpdated(final Channel channel) throws RemoteException {
	}

	@Override
	public void onConnectionStateChanged(final int state)
		throws RemoteException {
	}

	@Override
	public void onCurrentChannelChanged() throws RemoteException {
	}

	@Override
	public void onCurrentUserUpdated() throws RemoteException {
	}

	@Override
	public void onMessageReceived(final Message msg) throws RemoteException {
	}

	@Override
	public void onMessageSent(final Message msg) throws RemoteException {
	}

	@Override
	public void onUserAdded(final User user) throws RemoteException {
	}

	@Override
	public void onUserRemoved(final User user) throws RemoteException {
	}

	@Override
	public void onUserUpdated(final User user) throws RemoteException {
	}
}
