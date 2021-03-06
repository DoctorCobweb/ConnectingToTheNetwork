package au.com.spinninghalf.connectingtothenetwork;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ManagementTheUniversalActivity extends Activity {
	public static final String THE_UNIVERSAL_SOUNDCLOUD = "http://soundcloud.com/theuniversalrock";
	public static final String THE_UNIVERSAL_YOUTUBE = "http://www.youtube.com/user/TheUniversalOfficial?feature=watch";
	public static final String THE_UNIVERSAL_FACEBOOK = "http://www.facebook.com/theuniversalrock";
	
	
	//private ArrayList<String> bandLinkCategories;
	//private ArrayAdapter<String> bandLinkCategoriesArrayAdapter;
	//private static final String[] bandLinkCategories = {"Information", "Listen", "Watch", "Social" };
	Button informationButton;
	Button listenButton;
	Button watchButton;
	Button socialButton;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.management_the_universal);
		
		//find references to each button
		Button listenButton = (Button) findViewById(R.id.managementTheUniversalButton2);
		Button watchButton = (Button) findViewById(R.id.managementTheUniversalButton3);
		Button socialButton = (Button) findViewById(R.id.managementTheUniversalButton4);
		
		//attach the OnClickListeners to each button.
		//informationButton.setOnClickListener(informationButtonListener);
		listenButton.setOnClickListener(listenButtonListener);
		watchButton.setOnClickListener(watchButtonListener);
		socialButton.setOnClickListener(socialButtonListener);
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
	
	public OnClickListener informationButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ManagementTheUniversalActivity.this, ManagementTheUniversalInformationActivity.class);
			startActivity(intent);
		}
	};
	
	public OnClickListener listenButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementTheUniversalActivity.THE_UNIVERSAL_SOUNDCLOUD));
			startActivity(intent);
		}
	};
	
	public OnClickListener watchButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementTheUniversalActivity.THE_UNIVERSAL_YOUTUBE));
			startActivity(intent);
		}
	};
	
	public OnClickListener socialButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementTheUniversalActivity.THE_UNIVERSAL_FACEBOOK));
			startActivity(intent);
		}
	};

}
