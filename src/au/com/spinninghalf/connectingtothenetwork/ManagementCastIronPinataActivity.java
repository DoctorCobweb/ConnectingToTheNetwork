package au.com.spinninghalf.connectingtothenetwork;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ManagementCastIronPinataActivity extends Activity {
	public static final String CAST_IRON_PINATA_SOUNDCLOUD = "https://soundcloud.com/cast-iron-pinata";
	public static final String CAST_IRON_PINATA_YOUTUBE = "http://www.youtube.com/user/CASTIRONPINATA?feature=watch";
	public static final String CAST_IRON_PINATA_FACEBOOK = "http://www.facebook.com/castironpinata";
	
	
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
		setContentView(R.layout.management_cast_iron_pinata);
		
		//find references to each button
		Button listenButton = (Button) findViewById(R.id.managementCastIronPinataButton2);
		Button watchButton = (Button) findViewById(R.id.managementCastIronPinataButton3);
		Button socialButton = (Button) findViewById(R.id.managementCastIronPinataButton4);
		
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
			Intent intent = new Intent(ManagementCastIronPinataActivity.this, ManagementCastIronPinataInformationActivity.class);
			startActivity(intent);
		}
	};
	
	public OnClickListener listenButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementCastIronPinataActivity.CAST_IRON_PINATA_SOUNDCLOUD));
			startActivity(intent);
		}
	};
	
	public OnClickListener watchButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementCastIronPinataActivity.CAST_IRON_PINATA_YOUTUBE));
			startActivity(intent);
		}
	};
	
	public OnClickListener socialButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementCastIronPinataActivity.CAST_IRON_PINATA_FACEBOOK));
			startActivity(intent);
		}
	};

}
