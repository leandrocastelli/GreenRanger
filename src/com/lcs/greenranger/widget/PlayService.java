package com.lcs.greenranger.widget;

import com.lcs.greenranger.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class PlayService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate()
	{
		MediaPlayer player = MediaPlayer.create(this,R.raw.green);
		player.start();
	}

}
