package com.lcs.greenranger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.lcs.greenranger.filemanager.FileManager;
import com.lcs.greenranger.service.MediaService;
import com.lcs.greenranger.service.MediaService.LocalBinder;
import com.lcs.greenranger.service.SoundPlayer;

public class MainActivity extends Activity implements ServiceConnection{

	
	private SoundPlayer soundPlayer;
	private final int START_PLAY = 1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bindService(new Intent(this,MediaService.class), this, Context.BIND_AUTO_CREATE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		ImageView img = (ImageView)findViewById(R.id.rangerImage);
		img.setScaleType(ScaleType.FIT_XY);
		
		
		img.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				soundPlayer.startPlaying();
				
			}
		});

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@Override
	public void onPause()
	{
		
		super.onPause();
	}
	@Override
	public void onDestroy()
	{
		unbindService(this);
		super.onDestroy();
	}

	public void onServiceConnected(ComponentName name, IBinder service) {
		LocalBinder localBinder = (LocalBinder) service;
		soundPlayer = localBinder.getSoundPlayer();
		Message msg = new Message();
		msg.what = START_PLAY;
		handler.sendMessage(msg);
		
	}

	public void onServiceDisconnected(ComponentName name) {
		soundPlayer = null;
		
	}

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what)
			{
				case START_PLAY:
					soundPlayer.startPlaying();
			}
		};
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		switch(item.getItemId()){
		case R.id.menu_share: {
			String path = FileManager.getInstance().getFilePath(this);
			if(path!=null) {
				Intent share = new Intent(Intent.ACTION_SEND);
	        	share.setType("audio/*");
	        	share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + path));
	        	startActivity(Intent.createChooser(share, getString(R.string.share_sound)));
			}
			else {
				Toast.makeText(this, getString(R.string.share_error), 4).show();
			}
		}
		
		}
		return true;
	}
}
