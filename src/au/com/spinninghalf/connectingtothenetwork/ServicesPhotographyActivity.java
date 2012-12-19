package au.com.spinninghalf.connectingtothenetwork;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ServicesPhotographyActivity extends Activity {
	public static final String BRENTON_FORD_URL = "https://www.facebook.com/brentonfordphotography";
	public static final String LEIKOGRAPHY_URL = "https://www.facebook.com/leikography";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.services_photography);
		
		//find references to each button
		Button brentonFordButton = (Button) findViewById(R.id.servicesPhotographyBrentonFordButton);
		Button leikographyButton = (Button) findViewById(R.id.servicesPhotographyLeikographyButton);
		
		//attach the OnClickListeners to each button.
		brentonFordButton.setOnClickListener(brentonFordButtonListener);
		leikographyButton.setOnClickListener(leikographyButtonListener);
		
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
	
	public OnClickListener brentonFordButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ServicesPhotographyActivity.BRENTON_FORD_URL));
			startActivity(intent);
		}
	};
	
	public OnClickListener leikographyButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ServicesPhotographyActivity.LEIKOGRAPHY_URL));
			startActivity(intent);
		}
	};

}
