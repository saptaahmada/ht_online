package com.sapta.htonline.service.audio;

import com.sapta.htonline.service.model.User;

public interface AudioOutputHost {
	public static final int STATE_PASSIVE = 0;
	public static final int STATE_TALKING = 1;

	public void setTalkState(User user, int talkState);
}
