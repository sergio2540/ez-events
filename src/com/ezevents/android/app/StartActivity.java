package com.ezevents.android.app;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class StartActivity extends Activity {

	private Handler handler;
	private Runnable delayRunnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		
		
		handler = new Handler();
		delayRunnable = new Thread() {
			@Override
			public void run() {

				Intent createSP = new Intent(StartActivity.this,
						LoginActivity.class);
				StartActivity.this.startActivity(createSP);
				overridePendingTransition(R.layout.fade_in,R.layout.fade_out);
				StartActivity.this.finish();

			}
		};

		handler.postDelayed(delayRunnable, 3000);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(delayRunnable);
	}



}
