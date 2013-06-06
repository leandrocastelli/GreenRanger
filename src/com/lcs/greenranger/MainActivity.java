package com.lcs.greenranger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.lcs.greenranger.filemanager.FileManager;
import com.lcs.greenranger.filemanager.FileManager.Props;
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

		AdRequest adRequest = new AdRequest();
		
		AdView adView = (AdView)findViewById(R.id.ad);
//		adRequest.addTestDevice("89E1AAF3C3FB0B29BA39B0E77040BDEF");
//		Map<String, Object> extras = new HashMap<String, Object>();
//		extras.put("color_bg", "000000");
//		adRequest.setExtras(extras);
//		adRequest.addMediationExtra("color_bg", "000000");
		adRequest.addKeyword("games");
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
		adRequest.setLocation(lastKnownLocation);
		adView.loadAd(adRequest);
		
		
		img.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				soundPlayer.startPlaying();

			}
		});
		

	}
	@Override
	public void onResume() {
		
		super.onResume();
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
		final InputStream in = getResources().openRawResource(R.raw.green);
		switch(item.getItemId()){
		case R.id.menu_share: {
			
			String path = FileManager.getInstance().copyFile(this, Props.SEND, in);
			if(path!=null) {
				Intent share = new Intent(Intent.ACTION_SEND);
				share.setType("audio/*");
				share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + path));
				startActivity(Intent.createChooser(share, getString(R.string.share_sound)));
			}
			else {
				Toast.makeText(this, getString(R.string.share_error), Toast.LENGTH_SHORT).show();
			}
		}
		break;
		case R.id.menu_setas: {


			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.dialog_set_title));

			builder.setNegativeButton(getString(R.string.cancel), new AlertDialog.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();

				}
			});

			List<String> listString = new ArrayList<String>(3);
			listString.add(getString(R.string.ringtone));
			listString.add(getString(R.string.notification));
			listString.add(getString(R.string.alarm));

			final ArrayAdapter<String>  adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, android.R.id.text1, listString);
			builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					
						String path;
						boolean result = false;
						switch(arg1) {
						case 0:
							path = FileManager.getInstance().copyFile(adapter.getContext(), Props.RINGTONE, in);
							result = FileManager.getInstance().setAs(path,Props.RINGTONE, adapter.getContext());
											
							break;
						case 1:
							path = FileManager.getInstance().copyFile(adapter.getContext(), Props.NOTIFICATION, in);
							result = FileManager.getInstance().setAs(path,Props.NOTIFICATION, adapter.getContext());
							break;
						case 2:
							path = FileManager.getInstance().copyFile(adapter.getContext(), Props.ALARM, in);
							result = FileManager.getInstance().setAs(path,Props.ALARM, adapter.getContext());
							break;
						
						}
						if(result) {
							Toast.makeText(adapter.getContext(), adapter.getItem(arg1)+getString(R.string.set_success),Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(adapter.getContext(), adapter.getItem(arg1)+getString(R.string.set_fail),Toast.LENGTH_SHORT).show();
						}
					
					arg0.dismiss();

				}
			});
			builder.show();
		}
		}
		return true;
	}
}
