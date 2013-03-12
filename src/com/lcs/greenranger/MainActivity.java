package com.lcs.greenranger;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class MainActivity extends Activity {

	private MediaPlayer player;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
         player = MediaPlayer.create(this,R.raw.green);
        
		
		player.start();
        ImageView img = (ImageView)findViewById(R.id.rangerImage);
        img.setScaleType(ScaleType.FIT_XY);
        img.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				 
				if (player.isPlaying())
					player.stop();
				player.start();
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
    	if (player.isPlaying())
    		player.stop();
    }
}
