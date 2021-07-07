package com.sapta.htonline.service;

import com.sapta.htonline.service.model.Channel;
import com.sapta.htonline.service.model.Message;
import com.sapta.htonline.service.model.User;

/**
 * Callback interface for Protocol to communicate back to the Service
 *
 * @author Rantanen
 *
 */
public interface MumbleProtocolHost {
	public void channelAdded(Channel channel);

	public void channelRemoved(int channelId);

	public void channelUpdated(Channel channel);

	public void currentChannelChanged();

	public void currentUserUpdated();

	public void messageReceived(Message msg);

	public void messageSent(Message msg);

	public void setError(String error);

	public void setSynchronized(boolean synced);

	public void userAdded(User user);

	public void userRemoved(int userId);

	public void userUpdated(User user);
}
