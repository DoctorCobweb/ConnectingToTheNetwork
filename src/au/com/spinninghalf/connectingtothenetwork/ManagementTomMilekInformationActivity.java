package au.com.spinninghalf.connectingtothenetwork;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.os.Bundle;

public class ManagementTomMilekInformationActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.management_tom_milek_information);
	}
	
	@Override
	  public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance().activityStart(this); // Add this method.
	  }
	
	@Override
	public void onStop() {
		super.onStop();
		
		EasyTracker.getInstance().activityStop(this); // Add this method.
	}
	
	
	
}
