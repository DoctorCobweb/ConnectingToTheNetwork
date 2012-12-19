package au.com.spinninghalf.connectingtothenetwork;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ManagementTheSoulenikoesActivity extends Activity {
	public static final String THE_SOULENIKOES_SOUNDCLOUD = "http://au.myspace.com/thesoulenikoes";
	public static final String THE_SOULENIKOES_YOUTUBE = "http://www.youtube.com/user/theSoulenikoes?feature=watch";
	public static final String THE_SOULENIKOES_FACEBOOK = "http://www.facebook.com/pages/The-Soulenikoes/49572892044";
	
	
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
		setContentView(R.layout.management_the_soulenikoes);
		
		//find references to each button
		Button informationButton = (Button) findViewById(R.id.managementTheSoulenikoesButton1);
		Button listenButton = (Button) findViewById(R.id.managementTheSoulenikoesButton2);
		Button watchButton = (Button) findViewById(R.id.managementTheSoulenikoesButton3);
		Button socialButton = (Button) findViewById(R.id.managementTheSoulenikoesButton4);
		
		//attach the OnClickListeners to each button.
		informationButton.setOnClickListener(informationButtonListener);
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
			Intent intent = new Intent(ManagementTheSoulenikoesActivity.this, ManagementTheSoulenikoesInformationActivity.class);
			startActivity(intent);
		}
	};
	
	public OnClickListener listenButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementTheSoulenikoesActivity.THE_SOULENIKOES_SOUNDCLOUD));
			startActivity(intent);
		}
	};
	
	public OnClickListener watchButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementTheSoulenikoesActivity.THE_SOULENIKOES_YOUTUBE));
			startActivity(intent);
		}
	};
	
	public OnClickListener socialButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementTheSoulenikoesActivity.THE_SOULENIKOES_FACEBOOK));
			startActivity(intent);
		}
	};

}
