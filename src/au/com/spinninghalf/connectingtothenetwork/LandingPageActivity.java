package au.com.spinninghalf.connectingtothenetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.analytics.tracking.android.EasyTracker;

public class LandingPageActivity extends Activity {
	private static final String TAG = "LandingPageActivity";
	private static final int SPLASH_DISPLAY_TIME = 2000;
	private long ms=0;
	private long splashTime = 1000;
	private boolean splashActive = true;
	private boolean paused = false;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.landing_page);
		
		Log.i(TAG, "we are in LandingPageActivity's onCreate()");
		
			Thread myThread = new Thread() {
				public void run() {
					try {
						while (splashActive && ms < splashTime) {
							if (!paused) {
								ms = ms + 400;
							}
							sleep(100);
						}
						
					} catch (Exception e) {}
					finally {
						Intent intent = new Intent(LandingPageActivity.this, ActionBarTabsPager.class);
						startActivity(intent);
					}
					}
				};
				myThread.start();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		EasyTracker.getInstance().activityStart(this); 
	}
	
	
	@Override
	public void onStop() {
		super.onStop();
		
		EasyTracker.getInstance().activityStop(this); // Add this method.
	}
}


		
		
		
		/* Create a new handler with which to start the main activity
        and close this splash activity after SPLASH_DISPLAY_TIME has
        elapsed. */
		/*
	    new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	                   
	                    // Create an intent that will start the main activity. 
	                    Intent mainIntent = new Intent(LandingPageActivity.this,
	                            ActionBarTabsPager.class);
	                    LandingPageActivity.this.startActivity(mainIntent);
	                   
	                    // Finish splash activity so user cant go back to it.
	                    LandingPageActivity.this.finish();
	                   
	                    // Apply our splash exit (fade out) and main
	                    // entry (fade in) animation transitions.
	                    overridePendingTransition(R.anim.fadein,
	                            R.anim.fadeout);
	            }
	    }, SPLASH_DISPLAY_TIME);
			
			Intent intent = new Intent(this, ActionBarTabsPager.class);
			startActivity(intent);
		*/



