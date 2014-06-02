package com.bespoke.zaghi;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity{

	MediaPlayer ourSong;
	@Override
	protected void onCreate(Bundle Bacon) {
		
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(Bacon);
		setContentView(R.layout.splash);
		
		ourSong = MediaPlayer.create(Splash.this, R.raw.splash_sound);
		ourSong.start();
		
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(3000);
				}catch (InterruptedException e){
					e.printStackTrace();
				}finally{//Intent action names must match with the manifest action name
					Intent openStartingPoint = new Intent("com.bespoke.zaghi.Menu");
					startActivity(openStartingPoint); //calls this to onPause
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
	
		super.onPause();
		ourSong.release();	
		finish(); //kills splash activity when another thing is called on top (when it pauses)
	}
	
}
